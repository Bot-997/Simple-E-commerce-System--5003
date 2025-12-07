-- ========================================================
-- 初始化模拟数据脚本 (包含50条SKU)
-- ========================================================

SET FOREIGN_KEY_CHECKS = 0;

-- 1. 清理旧数据 (可选，防止ID冲突)
TRUNCATE TABLE CartItems;
TRUNCATE TABLE OrderItems;
TRUNCATE TABLE Product_SKUs;
TRUNCATE TABLE Wishlists;
TRUNCATE TABLE Products;
TRUNCATE TABLE Categories;

-- 2. 初始化分类 (Categories)
INSERT INTO Categories (category_id, name, parent_id) VALUES
(1, '数码电子', NULL),
(2, '潮流服饰', NULL),
(3, '家居生活', NULL),
(4, '休闲食品', NULL),
(5, '图书文娱', NULL),
(11, '智能手机', 1),
(12, '笔记本电脑', 1),
(13, '摄影影音', 1),
(21, '男装', 2),
(22, '运动鞋', 2),
(31, '家用电器', 3);

-- 3. 插入商品 (Products - SPU) 和 规格 (Product_SKUs - SKU)
-- 为了方便阅读，我将每个商品和它的SKU放在一起写

-- ==================== 商品 1: iPhone 15 Pro ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(1, 11, 'iPhone 15 Pro Max', 9999.00, 'Apple', 'A17 Pro芯片，钛金属机身，Action Button。', 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?q=80&w=800&auto=format&fit=crop', 1, 1);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(1, 'IP15PM-BLK-256', 9999.00, 50, '{"color": "黑色钛金属", "storage": "256GB"}'),
(1, 'IP15PM-BLK-512', 11999.00, 30, '{"color": "黑色钛金属", "storage": "512GB"}'),
(1, 'IP15PM-WHT-256', 9999.00, 20, '{"color": "白色钛金属", "storage": "256GB"}'),
(1, 'IP15PM-BLU-512', 11999.00, 15, '{"color": "蓝色钛金属", "storage": "512GB"}');

-- ==================== 商品 2: MacBook Air ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(2, 12, 'MacBook Air M3', 8999.00, 'Apple', '轻薄设计，M3芯片强力驱动，续航长达18小时。', 'https://cdsassets.apple.com/live/7WUAS350/images/tech-specs/mba-13inch-15inch.png', 1, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(2, 'MBA-M3-8-256', 8999.00, 100, '{"color": "午夜色", "ram": "8GB", "ssd": "256GB"}'),
(2, 'MBA-M3-16-512', 11999.00, 50, '{"color": "星光色", "ram": "16GB", "ssd": "512GB"}'),
(2, 'MBA-M3-16-1T', 13999.00, 10, '{"color": "银色", "ram": "16GB", "ssd": "1TB"}');

-- ==================== 商品 3: 索尼降噪耳机 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(3, 13, 'Sony WH-1000XM5', 2499.00, 'Sony', '行业领先的降噪技术，高清音质，30小时续航。', 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?q=80&w=800&auto=format&fit=crop', 0, 1);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(3, 'SONY-XM5-BLK', 2499.00, 200, '{"color": "黑色"}'),
(3, 'SONY-XM5-SLV', 2499.00, 150, '{"color": "铂金银"}');

-- ==================== 商品 4: 小米 14 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(4, 11, 'Xiaomi 14', 3999.00, 'Xiaomi', '徕卡光学镜头，骁龙8 Gen3，小屏旗舰。', 'https://cdn.cnbj1.fds.api.mi-img.com/product-images/xiaomi14ghnegm/index/40717.png', 1, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(4, 'MI14-BLK-12', 3999.00, 300, '{"color": "黑色", "ram": "12GB", "rom": "256GB"}'),
(4, 'MI14-GRN-16', 4599.00, 200, '{"color": "原野绿", "ram": "16GB", "rom": "512GB"}'),
(4, 'MI14-PNK-16', 4599.00, 100, '{"color": "雪山粉", "ram": "16GB", "rom": "512GB"}');

-- ==================== 商品 5: 机械键盘 Keychron ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(5, 1, 'Keychron K2 Pro', 588.00, 'Keychron', 'QMK/VIA开源改键，蓝牙双模机械键盘。', 'https://images.unsplash.com/photo-1595225476474-87563907a212?q=80&w=800&auto=format&fit=crop', 0, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(5, 'K2PRO-RED', 588.00, 80, '{"switch": "红轴", "backlight": "RGB"}'),
(5, 'K2PRO-BRN', 588.00, 60, '{"switch": "茶轴", "backlight": "RGB"}'),
(5, 'K2PRO-BLU', 588.00, 40, '{"switch": "青轴", "backlight": "白光"}');

-- ==================== 商品 6: Nike 空军一号 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(6, 22, 'Nike Air Force 1', 799.00, 'Nike', '经典纯白配色，百搭舒适，街头必备。', 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?q=80&w=800&auto=format&fit=crop', 1, 1);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(6, 'AF1-WHT-39', 799.00, 10, '{"color": "纯白", "size": "39"}'),
(6, 'AF1-WHT-40', 799.00, 20, '{"color": "纯白", "size": "40"}'),
(6, 'AF1-WHT-41', 799.00, 50, '{"color": "纯白", "size": "41"}'),
(6, 'AF1-WHT-42', 799.00, 50, '{"color": "纯白", "size": "42"}'),
(6, 'AF1-WHT-43', 799.00, 30, '{"color": "纯白", "size": "43"}');

-- ==================== 商品 7: 优衣库 T恤 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(7, 21, 'Uniqlo U系列 圆领T恤', 79.00, 'Uniqlo', '大师设计，重磅纯棉，版型挺括。', 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?q=80&w=800&auto=format&fit=crop', 1, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(7, 'UQ-TEE-WHT-M', 79.00, 500, '{"color": "白色", "size": "M"}'),
(7, 'UQ-TEE-WHT-L', 79.00, 500, '{"color": "白色", "size": "L"}'),
(7, 'UQ-TEE-BLK-M', 79.00, 400, '{"color": "黑色", "size": "M"}'),
(7, 'UQ-TEE-BLK-L', 79.00, 400, '{"color": "黑色", "size": "L"}'),
(7, 'UQ-TEE-BRN-XL', 79.00, 200, '{"color": "深褐色", "size": "XL"}');

-- ==================== 商品 8: 李维斯牛仔裤 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(8, 21, 'Levis 501 原创直筒牛仔裤', 599.00, 'Levis', '经典排扣设计，纯棉丹宁布。', 'https://tse4.mm.bing.net/th/id/OIP.scHGe6MrownAKtbzHIpN2wHaHa?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3', 0, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(8, 'LEV-501-30', 599.00, 20, '{"color": "水洗蓝", "waist": "30"}'),
(8, 'LEV-501-32', 599.00, 30, '{"color": "水洗蓝", "waist": "32"}'),
(8, 'LEV-501-34', 599.00, 15, '{"color": "水洗蓝", "waist": "34"}');

-- ==================== 商品 9: 戴森吹风机 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(9, 31, 'Dyson Supersonic HD15', 2999.00, 'Dyson', '快速干发，无叶设计，呵护头发光泽。', 'https://tse2.mm.bing.net/th/id/OIP.2Pali3TMJmgY7sFy5ksqTwHaEJ?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3', 1, 1);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(9, 'DYSON-PNK', 2999.00, 100, '{"color": "紫红色"}'),
(9, 'DYSON-BLU', 3199.00, 80, '{"color": "普鲁士蓝", "edition": "礼盒版"}');

-- ==================== 商品 10: 胶囊咖啡机 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(10, 31, 'Nespresso Essenza Mini', 899.00, 'Nespresso', '小巧机身，一键萃取，19Bar高压。', 'https://www.travelffeine.com/wp-content/uploads/2021/06/DSC_4965-2-scaled.jpg', 0, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(10, 'NES-MINI-WHT', 899.00, 60, '{"color": "纯白"}'),
(10, 'NES-MINI-GRY', 899.00, 40, '{"color": "深灰"}');

-- ==================== 商品 11: 佳能相机 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(11, 13, 'Canon EOS R6 Mark II', 15999.00, 'Canon', '全画幅微单，40张/秒连拍，4K60P视频。', 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?q=80&w=800&auto=format&fit=crop', 1, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(11, 'EOS-R6-BODY', 15999.00, 10, '{"type": "单机身"}'),
(11, 'EOS-R6-KIT', 18999.00, 5, '{"type": "24-105mm套机"}');

-- ==================== 商品 12: 电竞显示器 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(12, 1, 'LG UltraGear 27GP850', 2199.00, 'LG', '27英寸 NanoIPS，2K分辨率，180Hz刷新率。', 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?q=80&w=800&auto=format&fit=crop', 0, 1);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(12, 'LG-27-2K', 2199.00, 50, '{"size": "27英寸", "resolution": "2K"}');

-- ==================== 商品 13: 费列罗巧克力 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(13, 4, 'Ferrero Rocher 榛果威化巧克力', 45.00, 'Ferrero', '经典金球，层层美味，节日送礼首选。', 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?q=80&w=800&auto=format&fit=crop', 0, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(13, 'FR-8', 45.00, 1000, '{"count": "8粒装"}'),
(13, 'FR-24', 128.00, 500, '{"count": "24粒礼盒装"}');

-- ==================== 商品 14: 星巴克咖啡豆 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(14, 4, 'Starbucks 派克市场烘焙咖啡豆', 88.00, 'Starbucks', '中度烘焙，口感平衡，经典风味。', 'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?q=80&w=800&auto=format&fit=crop', 0, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(14, 'SB-PIKE-250', 88.00, 200, '{"weight": "250g"}');

-- ==================== 商品 15: 编程书籍 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(15, 5, 'Java编程思想 (Thinking in Java)', 108.00, '机械工业出版社', 'Java程序员必读经典，深入理解Java语言特性。', 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?q=80&w=800&auto=format&fit=crop', 0, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(15, 'BK-JAVA-CN', 108.00, 100, '{"language": "中文版", "cover": "平装"}');

-- ==================== 商品 16: 科幻小说 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(16, 5, '三体全集 (The Three-Body Problem)', 98.00, '重庆出版社', '刘慈欣著，雨果奖获奖作品，中国科幻巅峰。', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?q=80&w=800&auto=format&fit=crop', 1, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(16, 'BK-3BODY-SET', 98.00, 300, '{"content": "三部曲全集"}');

-- ==================== 商品 17: 运动手表 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(17, 1, 'Garmin Forerunner 965', 4980.00, 'Garmin', 'AMOLED屏幕，专业铁三模式，超长续航。', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=800&auto=format&fit=crop', 0, 0);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(17, 'GARMIN-965-BLK', 4980.00, 20, '{"color": "黑色"}'),
(17, 'GARMIN-965-WHT', 4980.00, 15, '{"color": "白色"}');

-- ==================== 商品 18: 乐高积木 ====================
INSERT INTO Products (product_id, category_id, name, base_price, manufacturer, description, image_url, is_recommended, is_ad) VALUES
(18, 5, 'LEGO 乐高 法拉利 Daytona SP3', 2899.00, 'LEGO', '机械组Technic旗舰，1:8比例还原，收藏级积木。', 'https://images.unsplash.com/photo-1585366119957-e9730b6d0f60?q=80&w=800&auto=format&fit=crop', 1, 1);

INSERT INTO Product_SKUs (product_id, sku_code, price, stock_quantity, specs) VALUES
(18, 'LEGO-42143', 2899.00, 30, '{"model": "42143"}');

SET FOREIGN_KEY_CHECKS = 1;