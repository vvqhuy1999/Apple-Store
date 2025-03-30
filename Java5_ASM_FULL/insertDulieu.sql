use Apple;

select * from Account

select * from Category;
select * from Product;
select * from ProductDungLuong;
select * from HinhAnh



INSERT INTO Product (TenSanPham, MaDanhMuc, MoTa) VALUES  
(N'iPhone 15', 1, N'iPhone 15 phiên bản tiêu chuẩn với thiết kế mới'),  
(N'iPhone 15 Plus', 1, N'iPhone 15 Plus với màn hình lớn hơn'),  
(N'iPhone 14 Pro', 1, N'iPhone 14 Pro với camera nâng cấp'),  
(N'iPhone 14', 1, N'iPhone 14 phiên bản tiêu chuẩn năm 2023'),  
(N'iPad Air 5', 2, N'iPad Air thế hệ 5 với chip M1'),  
(N'iPad Mini 6', 2, N'iPad Mini thế hệ 6 nhỏ gọn'),  
(N'iPad 10', 2, N'iPad thế hệ 10 với thiết kế toàn màn hình'),  
(N'iPad Pro 11" M2', 2, N'iPad Pro 11 inch chip M2'),  
(N'MacBook Air M2', 3, N'MacBook Air với chip M2 siêu mỏng nhẹ'),  
(N'MacBook Pro 16"', 3, N'MacBook Pro 16 inch hiệu năng cao'),  
(N'MacBook Air M1', 3, N'MacBook Air với chip M1 tiết kiệm năng lượng'),  
(N'MacBook Pro 13" M2', 3, N'MacBook Pro 13 inch chip M2'),  
(N'Apple Watch Series 9', 4, N'Apple Watch Series 9 với cảm biến mới'),  
(N'Apple Watch Ultra 2', 4, N'Apple Watch Ultra 2 dành cho thể thao mạo hiểm'),  
(N'Apple Watch SE 2', 4, N'Apple Watch SE thế hệ 2 giá phải chăng'),  
(N'Apple Watch Series 8', 4, N'Apple Watch Series 8 với tính năng đo nhiệt độ'),
-- Dữ liệu bổ sung mới (20 dòng)
(N'iPhone 13 Pro Max', 1, N'iPhone 13 Pro Max với camera cải tiến'),  
(N'iPhone 13 Pro', 1, N'iPhone 13 Pro phiên bản cao cấp'),  
(N'iPhone 13', 1, N'iPhone 13 tiêu chuẩn năm 2021'),  
(N'iPhone 12 Pro', 1, N'iPhone 12 Pro với thiết kế vuông vức'),  
(N'iPhone 12', 1, N'iPhone 12 phiên bản tiêu chuẩn'),  
(N'iPad Pro 12.9" M1', 2, N'iPad Pro 12.9 inch với chip M1'),  
(N'iPad Air 4', 2, N'iPad Air thế hệ 4 với màn hình Liquid Retina'),  
(N'iPad 9', 2, N'iPad thế hệ 9 giá phải chăng'),  
(N'iPad Mini 5', 2, N'iPad Mini thế hệ 5 nhỏ gọn'),  
(N'iPad Pro 11" M1', 2, N'iPad Pro 11 inch chip M1'),  
(N'MacBook Pro 16" M1 Pro', 3, N'MacBook Pro 16 inch với chip M1 Pro'),  
(N'MacBook Pro 14" M1 Max', 3, N'MacBook Pro 14 inch chip M1 Max'),  
(N'MacBook Air 13" M2', 3, N'MacBook Air 13 inch với chip M2'),  
(N'MacBook Pro 13" M1', 3, N'MacBook Pro 13 inch chip M1'),  
(N'MacBook Air Retina', 3, N'MacBook Air với màn hình Retina'),  
(N'Apple Watch Series 7', 4, N'Apple Watch Series 7 với màn hình lớn hơn'),  
(N'Apple Watch Ultra', 4, N'Apple Watch Ultra cho hoạt động ngoài trời'),  
(N'Apple Watch Series 6', 4, N'Apple Watch Series 6 với đo SpO2'),  
(N'Apple Watch SE', 4, N'Apple Watch SE phiên bản tiết kiệm'),  
(N'Apple Watch Series 5', 4, N'Apple Watch Series 5 với màn hình luôn bật');


-- Insert dữ liệu cho bảng ProductDungLuong
INSERT INTO ProductDungLuong (MaSanPham, MaDungLuong, Gia, SoLuongKho) VALUES  
-- iPhone 15 Pro (MaSanPham = 2)
(2, 1, 31990000, 60),  -- 128GB
(2, 2, 34990000, 50),  -- 256GB
(2, 3, 39990000, 40),  -- 512GB
(2, 4, 45990000, 30),  -- 1TB

-- iPad Pro M2 (MaSanPham = 3)
(3, 1, 20990000, 70),  -- 128GB
(3, 2, 23990000, 60),  -- 256GB
(3, 3, 27990000, 50),  -- 512GB
(3, 4, 33990000, 40),  -- 1TB

-- MacBook Pro 14" (MaSanPham = 4)
(4, 2, 49990000, 50),  -- 256GB
(4, 3, 55990000, 40),  -- 512GB
(4, 4, 63990000, 30),  -- 1TB

-- iPhone 15 (MaSanPham = 5)
(5, 1, 22990000, 80),  -- 128GB
(5, 2, 25990000, 70),  -- 256GB
(5, 3, 29990000, 60),  -- 512GB

-- iPhone 15 Plus (MaSanPham = 6)
(6, 1, 25990000, 70),  -- 128GB
(6, 2, 28990000, 60),  -- 256GB
(6, 3, 32990000, 50),  -- 512GB

-- iPhone 14 Pro (MaSanPham = 7)
(7, 1, 29990000, 60),  -- 128GB
(7, 2, 32990000, 50),  -- 256GB
(7, 3, 37990000, 40),  -- 512GB
(7, 4, 43990000, 30),  -- 1TB

-- iPhone 14 (MaSanPham = 8)
(8, 1, 20990000, 80),  -- 128GB
(8, 2, 23990000, 70),  -- 256GB
(8, 3, 27990000, 60),  -- 512GB

-- iPad Air 5 (MaSanPham = 9)
(9, 1, 15990000, 90),  -- 128GB
(9, 2, 18990000, 80),  -- 256GB

-- iPad Mini 6 (MaSanPham = 10)
(10, 1, 12990000, 100), -- 128GB
(10, 2, 15990000, 90),  -- 256GB

-- iPad 10 (MaSanPham = 11)
(11, 1, 11990000, 100), -- 128GB
(11, 2, 14990000, 90),  -- 256GB

-- iPad Pro 11" M2 (MaSanPham = 12)
(12, 1, 18990000, 70),  -- 128GB
(12, 2, 21990000, 60),  -- 256GB
(12, 3, 25990000, 50),  -- 512GB
(12, 4, 31990000, 40),  -- 1TB

-- MacBook Air M2 (MaSanPham = 13)
(13, 2, 32990000, 60),  -- 256GB
(13, 3, 37990000, 50),  -- 512GB
(13, 4, 45990000, 40),  -- 1TB

-- MacBook Pro 16" (MaSanPham = 14)
(14, 3, 64990000, 40),  -- 512GB
(14, 4, 72990000, 30),  -- 1TB

-- MacBook Air M1 (MaSanPham = 15)
(15, 2, 25990000, 70),  -- 256GB
(15, 3, 30990000, 60),  -- 512GB

-- MacBook Pro 13" M2 (MaSanPham = 16)
(16, 2, 35990000, 60),  -- 256GB
(16, 3, 40990000, 50),  -- 512GB
(16, 4, 48990000, 40),  -- 1TB

-- Apple Watch Series 9 (MaSanPham = 17)
(17, 1, 9990000, 100),  -- Không áp dụng dung lượng lớn, giả định 128GB là bộ nhớ tối thiểu
(17, 2, 10990000, 90),  -- 256GB

-- Apple Watch Ultra 2 (MaSanPham = 18)
(18, 2, 21990000, 50),  -- 256GB

-- Apple Watch SE 2 (MaSanPham = 19)
(19, 1, 6990000, 120),  -- 128GB

-- Apple Watch Series 8 (MaSanPham = 20)
(20, 1, 8990000, 100),  -- 128GB
(20, 2, 9990000, 90);   -- 256GB


INSERT INTO MauSac (EnglishName, TenMauSac) VALUES  
('Pink', N'Hồng'),  
('Yellow', N'Vàng'),  
('Green', N'Xanh Lá'),  
('Blue', N'Xanh Dương'),  
('Black', N'Đen'),  
('Silver', N'Bạc'),  
('Space Gray', N'Xám Không Gian');

-- Insert dữ liệu cho DetailProductMauSac
INSERT INTO DetailProductMauSac (MaMauSac, MaSanPham) VALUES  
-- iPhone 15 Pro Max (MaSanPham = 1) - Đã có trong dữ liệu của bạn
(1, 1), -- Titan Tự Nhiên
(2, 1), -- Titan Xanh
(3, 1), -- Titan Trắng
(4, 1), -- Titan Đen

-- iPhone 15 Pro (MaSanPham = 2)
(1, 2), -- Titan Tự Nhiên
(2, 2), -- Titan Xanh
(3, 2), -- Titan Trắng
(4, 2), -- Titan Đen

-- iPad Pro M2 (MaSanPham = 3)
(10, 3), -- Silver (Bạc)
(11, 3), -- Space Gray (Xám Không Gian)

-- MacBook Pro 14" (MaSanPham = 4)
(10, 4), -- Silver (Bạc)
(11, 4), -- Space Gray (Xám Không Gian)

-- iPhone 15 (MaSanPham = 5)
(5, 5), -- Pink (Hồng)
(6, 5), -- Yellow (Vàng)
(7, 5), -- Green (Xanh Lá)
(8, 5), -- Blue (Xanh Dương)
(9, 5), -- Black (Đen)

-- iPhone 15 Plus (MaSanPham = 6)
(5, 6), -- Pink (Hồng)
(6, 6), -- Yellow (Vàng)
(7, 6), -- Green (Xanh Lá)
(8, 6), -- Blue (Xanh Dương)
(9, 6), -- Black (Đen)

-- iPhone 14 Pro (MaSanPham = 7)
(1, 7), -- Titan Tự Nhiên
(2, 7), -- Titan Xanh
(3, 7), -- Titan Trắng
(4, 7), -- Titan Đen

-- iPhone 14 (MaSanPham = 8)
(5, 8), -- Pink (Hồng)
(6, 8), -- Yellow (Vàng)
(7, 8), -- Green (Xanh Lá)
(8, 8), -- Blue (Xanh Dương)
(9, 8), -- Black (Đen)

-- iPad Air 5 (MaSanPham = 9)
(10, 9), -- Silver (Bạc)
(11, 9), -- Space Gray (Xám Không Gian)

-- iPad Mini 6 (MaSanPham = 10)
(10, 10), -- Silver (Bạc)
(11, 10), -- Space Gray (Xám Không Gian)

-- iPad 10 (MaSanPham = 11)
(10, 11), -- Silver (Bạc)
(11, 11), -- Space Gray (Xám Không Gian)

-- iPad Pro 11" M2 (MaSanPham = 12)
(10, 12), -- Silver (Bạc)
(11, 12), -- Space Gray (Xám Không Gian)

-- MacBook Air M2 (MaSanPham = 13)
(10, 13), -- Silver (Bạc)
(11, 13), -- Space Gray (Xám Không Gian)

-- MacBook Pro 16" (MaSanPham = 14)
(10, 14), -- Silver (Bạc)
(11, 14), -- Space Gray (Xám Không Gian)

-- MacBook Air M1 (MaSanPham = 15)
(10, 15), -- Silver (Bạc)
(11, 15), -- Space Gray (Xám Không Gian)

-- MacBook Pro 13" M2 (MaSanPham = 16)
(10, 16), -- Silver (Bạc)
(11, 16), -- Space Gray (Xám Không Gian)

-- Apple Watch Series 9 (MaSanPham = 17)
(1, 17), -- Titan Tự Nhiên
(10, 17), -- Silver (Bạc)
(9, 17), -- Black (Đen)

-- Apple Watch Ultra 2 (MaSanPham = 18)
(1, 18), -- Titan Tự Nhiên

-- Apple Watch SE 2 (MaSanPham = 19)
(10, 19), -- Silver (Bạc)
(9, 19), -- Black (Đen)

-- Apple Watch Series 8 (MaSanPham = 20)
(1, 20), -- Titan Tự Nhiên
(10, 20), -- Silver (Bạc)
(9, 20); -- Black (Đen)



-- Insert dữ liệu cho HinhAnh

INSERT INTO HinhAnh (LinkHinhAnh, MaSanPham, MaMauSac) VALUES  
-- iPhone 15 Pro (MaSanPham = 2)
('https://thatvirtualboy.com/assets/images/15proheader.jpeg', 2, 1),  
('https://m-cdn.phonearena.com/images/articles/406828-image/iphone-15-pro.webp?w=1', 2, 2),  
('https://www.cellcom.com/sites/default/files/styles/max_650x650/public/2023-09/iPhone_15_Pro_White_Titanium_PDP_Image_Position-8__en-US.jpg?itok=53KZwupM', 2, 3),  
('https://www.phonemart.ng/wp-content/uploads/2024/06/15-promax6.jpeg', 2, 4),  

-- iPad Pro M2 (MaSanPham = 3)
('https://via.placeholder.com/300x200?text=iPad+Pro+M2+Silver', 3, 10),  
('https://via.placeholder.com/300x200?text=iPad+Pro+M2+Space+Gray', 3, 11),  

-- MacBook Pro 14" (MaSanPham = 4)
('https://everymac.com/images/cpu_pictures/macbook-pro-m3-14-2023-silver.jpg', 4, 10),  
('https://cdsassets.apple.com/live/SZLF0YNV/images/sp/111340_macbook-pro-2023-14in.png', 4, 11),  

-- iPhone 15 (MaSanPham = 5)
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/a867edb5-186c-52c9-994f-0b4d633f1e3b/d9ecf1cb-796b-5477-a20a-80226f4b9bc9.jpg', 5, 5),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/28026410-1438-5eb7-bcb6-2895fcd4e328/a43be764-1577-5a54-9c99-5d3253c03a76.jpg', 5, 6),  
('https://applecenter.com.vn/uploads/cms/16947658435170.jpg', 5, 7),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/2e44a35b-55b1-5c7b-946b-b9ca6440ec3c/9c777b18-f346-59da-898a-2645558869da.jpge', 5, 8),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/2b0511a0-1146-501d-b503-ac114c443d53/19fff8e5-c8d2-5bcc-b34c-43e2ef28b80a.jpg', 5, 9),  

-- iPhone 15 Plus (MaSanPham = 6)
('https://i.redd.it/3kq50uqn4b9e1.jpeg', 6, 5),  
('https://www.i-blason.com/cdn/shop/files/iB_iPhone15_2Lens_AresMag_1x1_Yellow_1_620x.png?v=1693175434', 6, 6),  
('https://recel.co.uk/cdn/shop/files/Apple_iPhone_15_Plus_Green_106a2da0-9a17-4cbc-a258-373910127f5b.png?v=1695787019&width=600', 6, 7),  
('https://hanoicomputercdn.com/media/product/76462_iphone_15_plus_128gb_blue_mu163vn_a_3.jpg', 6, 8),  
('https://didongviet.vn/dchannel/wp-content/uploads/2023/11/iphone-15-plus-black-didongviet.jpg', 6, 9),  

-- iPhone 14 Pro (MaSanPham = 7)
('https://i.redd.it/z1y4wuaiw1qb1.jpg', 7, 1),  
('https://www.notebookcheck.net/fileadmin/Notebooks/News/_nc3/Sierra_Blue.jpg', 7, 2),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/a94557ac-92f0-541c-bdf1-686e999b6117/19fff8e5-c8d2-5bcc-b34c-43e2ef28b80a.jpg', 7, 3),  
('https://phucanhcdn.com/media/lib/01-10-2022/iphone-14-pro-promax-space-black.png', 7, 4),  

-- iPhone 14 (MaSanPham = 8)
('https://techresearchonline.com/wp-content/uploads/2022/08/LEAKED-iPhone-14-Range-Colors-are-here-Can-we-expect-it-in-a-pink-shade-1200x900.webp', 8, 5),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/9c0fed38-e4e3-50ef-9722-8b09385c1e17/f0d0296c-b859-5f08-b09d-ecbd34406eaf.jpg', 8, 6),  
('https://www.digitaltrends.com/wp-content/uploads/2023/03/apple-iphone-14-color-mockup-dark-green.jpg?fit=720%2C479&p=1', 8, 7),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/6825b827-cde1-597c-988b-6f389080b700/2f72419f-8366-5966-9da4-bc14aa998848.jpg', 8, 8),  
('https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2F42834d0f-f83f-4824-b195-7a3be6347ad6_5120x2880.jpeg', 8, 9),  

-- iPad Air 5 (MaSanPham = 9)
('https://media.karousell.com/media/photos/products/2022/10/5/ipad_air_5_64gb_wifi_silver_us_1664950377_0c8b9f41_progressive', 9, 10),  
('https://product.hstatic.net/200000845283/product/apple_ipad_air_5_10.9_inch_wi-fi_64gb--space_grey_2_7b129fb98fa04377bc858b86a37acfdb.jpg', 9, 11),  

-- iPad Mini 6 (MaSanPham = 10)
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/1b177e9b-199b-5439-9314-58652a56d517/b8f3c92c-1704-552a-8e04-2671377778b0.jpg', 10, 10),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/5018ca00-18d7-5576-bdfd-7e1823cf531b/d9ecf1cb-796b-5477-a20a-80226f4b9bc9.jpg', 10, 11),  

-- iPad 10 (MaSanPham = 11)
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/82c1f463-f454-5fd8-8a8b-090934089a48/3e8cf789-6124-5215-bbd5-ca04762847e5.jpg', 11, 10),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/7573c916-4373-5fca-9d18-f3f7b060e7c2/78b7eb7a-896e-57ff-b047-c69590dd28e7.jpg', 11, 11),  

-- iPad Pro 11" M2 (MaSanPham = 12)
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/6e51b1cd-2f83-58f0-aace-eb5fa943d5ca/aec80b18-b86f-5732-973c-d917cf3d30d2.jpg', 12, 10),  
('https://phucanhcdn.com/media/product/49278_space_grey.jpg', 12, 11),  

-- MacBook Air M2 (MaSanPham = 13)
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/e29d7404-baa5-50e1-b0f1-b611726e3390/7827f6f1-d41b-53de-a1ab-e30a40179677.jpg', 13, 10),  
('https://shopdunk.com/images/uploaded/macbook-air-m2-2022-mau-xam.jpg', 13, 11),  

-- MacBook Pro 16" (MaSanPham = 14)
('https://m.media-amazon.com/images/I/61BwmCTlBpL.jpg', 14, 10),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/8dad190c-e068-55f9-9a51-20e0dfd160cf/2b2cf9fe-a28c-5eff-97fb-950c6fb96713.jpg', 14, 11),  

-- MacBook Air M1 (MaSanPham = 15)
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/f89a915c-8628-5339-ba25-9e6befd8ebe5/19fff8e5-c8d2-5bcc-b34c-43e2ef28b80a.jpg', 15, 10),  
('https://i.redd.it/76lwbw0l2nhb1.jpg', 15, 11),  

-- MacBook Pro 13" M2 (MaSanPham = 16)
('https://cdn.shoplightspeed.com/shops/638486/files/53059240/1600x2048x2/apple-13-inch-macbook-pro-apple-m2-chip-with-8-cor.jpg', 16, 10),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/802daa0b-df2f-5fe3-b04e-bb69190b828e/3a06f831-ba1d-5f54-b203-52c2c00f69c8.jpg', 16, 11),  

-- Apple Watch Series 9 (MaSanPham = 17)
('https://product.hstatic.net/200000348419/product/apple_watch_series_9_case_gold_stainless_clay_sport_band_cellular_0_0dcc933500d2433d8a31d45054d5d84c_master.png', 17, 1),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/37c773ce-bed6-5898-a81b-639cf9a09411/19fff8e5-c8d2-5bcc-b34c-43e2ef28b80a.jpg', 17, 10),  
('https://pos.nvncdn.com/c2ad3b-44417/ps/20241014_4vnGQNTnkY.webp', 17, 9),  

-- Apple Watch Ultra 2 (MaSanPham = 18)
('https://www.macworld.com/wp-content/uploads/2024/09/Apple-Watch-Ultra-2-review-smart-stack.jpg?quality=50&strip=all&w=1024', 18, 1),  

-- Apple Watch SE 2 (MaSanPham = 19)
('https://ap24h.vn/images/products/2022/09/27/large/mp7m3ref_vw_34frwatch-44-alum-silver-nc-se_vw_34fr_wf_co_1664253032.jpg', 19, 10),  
('https://d2u1z1lopyfwlx.cloudfront.net/thumbnails/22772b73-c2f6-54db-8f91-95e150dadb36/1ee4cc77-0b76-5a60-9fab-1db2b3db4c10.jpg', 19, 9);

-- Apple Watch Series 8 (MaSanPham = 20