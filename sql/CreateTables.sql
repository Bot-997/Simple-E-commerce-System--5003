-- ----User Info -----------
-- Users
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL COMMENT 'emmail account',
    password_hash VARCHAR(255) NOT NULL,
    is_admin TINYINT(1) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    -- [显式索引] 唯一索引，B-Tree 支持快速查找和唯一性校验
    UNIQUE KEY uk_email (email) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Addresses
CREATE TABLE Addresses (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    state VARCHAR(100),
    city VARCHAR(100),
    zip_code VARCHAR(20),
    is_default TINYINT(1) DEFAULT 0,
    
    CONSTRAINT fk_addr_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    
    -- [显式索引] 复合索引，加速"查询某用户的所有地址"
    KEY idx_user_default (user_id, is_default) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------- Product Info ----------
-- Categories
CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    parent_id INT DEFAULT NULL,
    
    CONSTRAINT fk_cat_parent FOREIGN KEY (parent_id) REFERENCES Categories(category_id) ON DELETE SET NULL,
    
    -- [显式索引] 加速查询子分类
    KEY idx_parent_id (parent_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Products
CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT,
    name VARCHAR(255) NOT NULL,
    base_price DECIMAL(10, 2) NOT NULL COMMENT 'original price',
    manufacturer VARCHAR(255),
    description TEXT,
    image_url VARCHAR(500),
    is_recommended TINYINT(1) DEFAULT 0,
    is_ad TINYINT(1) DEFAULT 0,
    
    CONSTRAINT fk_prod_cat FOREIGN KEY (category_id) REFERENCES Categories(category_id) ON DELETE SET NULL,
    
    -- [显式索引] 复合索引：加速"查询某分类下的推荐商品"
    -- B-Tree 支持最左前缀，这也可以加速单纯按 category_id 的查询
    KEY idx_cat_recommend (category_id, is_recommended) USING BTREE,
    
    -- [显式索引] 全文索引：如果需要搜名字（注意：这是全文检索，非B-Tree）
    FULLTEXT KEY fx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='basical information of products';

-- Product_SKUs
CREATE TABLE Product_SKUs (
    sku_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    sku_code VARCHAR(64) NOT NULL,
    price DECIMAL(10, 2) NOT NULL COMMENT 'current price',
    stock_quantity INT DEFAULT 0,
    sku_image VARCHAR(500),
    specs JSON COMMENT 'JSON example: {"color":"red", "size":"L"}',
    
    CONSTRAINT fk_sku_prod FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE,
    
    -- [显式索引] 业务唯一键，仓库扫码枪使用
    UNIQUE KEY uk_sku_code (sku_code) USING BTREE,
    
    -- [显式索引] 加速商品详情页加载该商品所有规格
    KEY idx_product_id (product_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ---------- Cart --------------
-- Wishlists
CREATE TABLE Wishlists (
    wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_wish_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_wish_prod FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE,
    
    -- [显式索引] 复合唯一索引：防止重复收藏，且加速"我收藏了哪些商品"
    UNIQUE KEY uk_user_prod (user_id, product_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Carts
CREATE TABLE Carts (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    
    -- [显式索引] 唯一索引：每个用户只有一个购物车
    UNIQUE KEY uk_user_id (user_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CartItems
CREATE TABLE CartItems (
    cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    sku_id INT NOT NULL,
    quantity INT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_ci_cart FOREIGN KEY (cart_id) REFERENCES Carts(cart_id) ON DELETE CASCADE,
    CONSTRAINT fk_ci_sku FOREIGN KEY (sku_id) REFERENCES Product_SKUs(sku_id) ON DELETE CASCADE,
    
    -- [显式索引] 复合索引：加速加载某人的购物车列表
    KEY idx_cart_sku (cart_id, sku_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------- Orders ------------
-- Orders
CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    shipping_address_id INT,
    status TINYINT DEFAULT 0 COMMENT '0: redey to pay, 1:payed, 2:sending, 3:done, 4:cancel',
    total_amount DECIMAL(12, 2) NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_ord_user FOREIGN KEY (user_id) REFERENCES Users(user_id),
    CONSTRAINT fk_ord_addr FOREIGN KEY (shipping_address_id) REFERENCES Addresses(address_id),
    
    -- [显式索引] 重点优化：用户查看"我的订单"通常按时间倒序
    -- B-Tree 索引是有序的，能消除 filesort，极大提升查询性能
    KEY idx_user_date (user_id, order_date) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- OrderItems
CREATE TABLE OrderItems (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    sku_id INT,
    price_at_purchase DECIMAL(10, 2) NOT NULL COMMENT 'price at purchase',
    quantity INT NOT NULL DEFAULT 1,
    
    CONSTRAINT fk_oi_order FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_oi_sku FOREIGN KEY (sku_id) REFERENCES Product_SKUs(sku_id) ON DELETE SET NULL,
    
    -- [显式索引] 加速订单详情页加载
    KEY idx_order_id (order_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;