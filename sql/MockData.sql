-- Mock Data for testing

SET FOREIGN_KEY_CHECKS = 0;

-- Users
TRUNCATE TABLE Users;
INSERT INTO Users (user_id, email, password_hash, is_admin) VALUES 
(1, 'admin@store.com', 'admin', 1),  -- admin
(2, 'zhangsan@163.com', '123456', 0),  -- 用户 张三
(3, 'lisi@gmail.com', 'qwerty', 0);  -- 用户 李四


-- Addresses
TRUNCATE TABLE Addresses;
INSERT INTO Addresses (address_id, user_id, state, city, zip_code, is_default) VALUES
(1, 2, '北京', '北京市', '100000', 1),  -- default address for zhangsan
(2, 2, '上海', '上海市', '200000', 0),  -- alternate address
(3, 3, '广东', '深圳市', '518000', 1);  -- address for lisi

-- Categories
TRUNCATE TABLE Categories;
INSERT INTO Categories (category_id, name, parent_id) VALUES
(1, '电子产品', NULL),       -- 一级分类
(2, '服装服饰', NULL),       -- 一级分类
(3, '智能手机', 1),          -- 电子产品的子分类
(4, '笔记本电脑', 1),        -- 电子产品的子分类
(5, '男士T恤', 2);           -- 服装的子分类


-- Products
TRUNCATE TABLE Products;
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, is_recommended, is_ad) VALUES
(1, 3, 'iPhone 15 Pro', 7999.00, 'Apple', '钛金属边框，A17 Pro芯片', 1, 1),
(2, 3, '小米 14', 3999.00, 'Xiaomi', '徕卡光学镜头，骁龙8 Gen3', 1, 0),
(3, 5, '纯棉基础款T恤', 59.90, 'Uniqlo', '100%纯棉，舒适透气', 0, 0);


-- Product_SKUs
TRUNCATE TABLE Product_SKUs;
INSERT INTO Product_SKUs (sku_id, product_id, sku_code, price, stock_quantity, specs) VALUES
-- iPhone 15 Pro
(1, 1, 'IP15P-BLK-256', 8999.00, 100, '{"color": "BLACK", "storage": "256GB"}'),
(2, 1, 'IP15P-WHT-512', 10999.00, 50, '{"color": "WHITE", "storage": "512GB"}'),

-- MI13
(3, 2, 'MI14-GRN-12', 4299.00, 200, '{"color": "GREEN", "ram": "12GB", "rom": "256GB"}'),

-- T-shirt
(4, 3, 'TSHIRT-WHT-L', 59.90, 500, '{"color": "WHITE", "size": "L", "material": "Cotton"}'),
(5, 3, 'TSHIRT-BLK-M', 59.90, 300, '{"color": "BLACK", "size": "M", "material": "Cotton"}');


-- Carts & CartItems
-- 张三(user_id=2) added a iPhone and T-shirts to cart
TRUNCATE TABLE Carts;
INSERT INTO Carts (cart_id, user_id) VALUES (1, 2), (2, 3);

TRUNCATE TABLE CartItems;
INSERT INTO CartItems (cart_id, sku_id, quantity) VALUES
(1, 1, 1),  -- 1 iPhone 15 Pro (BLACK 256G)
(1, 4, 2);  -- 2 L Size T-shirts


-- Wishlists
-- 李四(user_id=3) added a MI14 to whishlist
TRUNCATE TABLE Wishlists;
INSERT INTO Wishlists (user_id, product_id) VALUES (3, 2);


-- Orders & OrderItems
-- Assume zhangsan bought a MI13 at 2023-11-11
TRUNCATE TABLE Orders;
INSERT INTO Orders (order_id, user_id, shipping_address_id, status, total_amount, order_date) VALUES
(1, 2, 1, 3, 4299.00, '2023-11-11 10:00:00'); -- status 3 represents done

TRUNCATE TABLE OrderItems;
INSERT INTO OrderItems (order_id, sku_id, price_at_purchase, quantity) VALUES
(1, 3, 4299.00, 1); 

SET FOREIGN_KEY_CHECKS = 1;