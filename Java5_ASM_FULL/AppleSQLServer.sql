/*
USE master;  
GO  
ALTER DATABASE Apple SET SINGLEUSER WITH ROLLBACK IMMEDIATE;  
GO 
DROP DATABASE Apple;  
*/

CREATE DATABASE Apple;  
GO  

USE Apple;  
GO  

-- Bảng Người Dùng  
CREATE TABLE Account (  
    MaNguoiDung INT IDENTITY(1,1) PRIMARY KEY,  
    HoTen NVARCHAR(100) NOT NULL,  
    Email NVARCHAR(100) UNIQUE NOT NULL,  
    MatKhau NVARCHAR(255) NOT NULL,  
    SoDienThoai NVARCHAR(15) UNIQUE NOT NULL,  
    DiaChi NVARCHAR(255),  
    VaiTro int,  
    NgayTao DATETIME DEFAULT GETDATE()  
);  

CREATE TABLE Discount (  
    MaDiscount int IDENTITY(1,1) PRIMARY KEY,  
    MaCoupon NVARCHAR(15),  
    SL int,  
    PhanTram float,  
    NgayTao DATETIME DEFAULT GETDATE(),  
    NgayHetHan DATETIME,  
    MaNguoiDung INT  
);  

CREATE TABLE UseDiscount (  
    MaUseDiscount INT IDENTITY(1,1) PRIMARY KEY,  
    MaNguoiDung INT,  
    MaDiscount int,  
    NgaySuDung DATETIME DEFAULT GETDATE(),  
    MaDonHang int  
);  

-- Bảng Danh Mục Sản Phẩm  
CREATE TABLE Category (  
    MaDanhMuc INT IDENTITY(1,1) PRIMARY KEY,  
    TenDanhMuc NVARCHAR(100) UNIQUE NOT NULL,  
    MoTa NVARCHAR(255),  
    NgayTao DATETIME DEFAULT GETDATE()  
);  

-- Bảng Sản Phẩm  
CREATE TABLE Product (  
    MaSanPham INT IDENTITY(1,1) PRIMARY KEY,  
    TenSanPham NVARCHAR(255) NOT NULL,  
    MaDanhMuc INT,  
    MoTa NTEXT,  
    NgayTao DATETIME DEFAULT GETDATE()  
); 

CREATE TABLE DetailProductMauSac (  
    MaDetailProductMauSac int IDENTITY(1,1) PRIMARY KEY,  
    MaMauSac int,  
    MaSanPham int,  
    NgayTao DATETIME DEFAULT GETDATE()  
);  

CREATE TABLE MauSac (  
    MaMauSac int IDENTITY(1,1) PRIMARY KEY,
	EnglishName varchar(100),
    TenMauSac NVARCHAR(100) NOT NULL,  
);  

CREATE TABLE HinhAnh (  
    MaHinhAnh int IDENTITY(1,1) PRIMARY KEY,  
    LinkHinhAnh NVARCHAR(255),  
    MaSanPham int,  
    MaMauSac int  
);  

CREATE TABLE ProductDungLuong (  
    MaProductDungLuong INT IDENTITY(1,1) PRIMARY KEY,  
    MaSanPham INT,  
    MaDungLuong INT,  
    Gia DECIMAL(18,2) NOT NULL,  
    SoLuongKho INT NOT NULL,    
); 


CREATE TABLE DungLuong (  
    MaDungLuong int IDENTITY(1,1) PRIMARY KEY,  
    SoDungLuong int,  
    DonVi NVARCHAR(10) DEFAULT 'GB'  
);    

-- Bảng Đơn Hàng  
CREATE TABLE Orders (  
    MaDonHang INT IDENTITY(1,1) PRIMARY KEY,  
    MaNguoiDung INT,  
    TongTien DECIMAL(18,2) NOT NULL,  
    TrangThai int DEFAULT 1, -- 1 = 'ChoXacNhan', 0 = Huy  
    NgayDat DATETIME DEFAULT GETDATE()  
);  

-- Bảng Chi Tiết Đơn Hàng  
CREATE TABLE OrderDetail (  
    MaChiTietDonHang INT IDENTITY(1,1) PRIMARY KEY,  
    MaDonHang INT,  
    MaSanPham INT,  
    SoLuong INT NOT NULL,  
    Gia DECIMAL(18,2) NOT NULL  
);  

-- Bảng Đánh Giá Sản Phẩm  
CREATE TABLE Assess (  
    MaDanhGia INT IDENTITY(1,1) PRIMARY KEY,  
    MaNguoiDung INT,  
    MaSanPham INT,  
    XepHang INT CHECK (XepHang BETWEEN 1 AND 5),  
    BinhLuan NTEXT,  
    NgayTao DATETIME DEFAULT GETDATE()  
);  

-- Bảng Thanh Toán  
CREATE TABLE Payment (  
    MaThanhToan INT IDENTITY(1,1) PRIMARY KEY,  
    MaDonHang INT,  
    MaPhuongThuc int NOT NULL,  
    TrangThaiThanhToan int DEFAULT 1, -- 1 = 'ChoXacNhan', 0 = Huy  
    SoTienThanhToan DECIMAL(18,2) NOT NULL,  
    NgayThanhToan DATETIME DEFAULT GETDATE()  
);

Create table PhuongThucThanhToan(
	MaPhuongThuc int IDENTITY(1,1) PRIMARY KEY,
	TenPhuongThuc nvarchar(50)
);


-- Bảng Vận Chuyển  
CREATE TABLE Ship (  
    MaVanChuyen INT IDENTITY(1,1) PRIMARY KEY,  
    MaDonHang INT,  
    MaVanDon NVARCHAR(50) UNIQUE,  
    DonViVanChuyen NVARCHAR(100),  
    TrangThai int DEFAULT 1, -- 1 = 'DangXuLy', 0 = giao hang ko thanh cong  
    NgayDuKienGiao DATETIME,  
    NgayGui DATETIME DEFAULT GETDATE()  
);  

-- Tạo các ràng buộc khóa ngoại  
ALTER TABLE Product  
ADD CONSTRAINT FK_Product_Category FOREIGN KEY (MaDanhMuc) REFERENCES Category(MaDanhMuc);  

ALTER TABLE Orders
ADD CONSTRAINT FK_Orders_Account FOREIGN KEY (MaNguoiDung) REFERENCES Account(MaNguoiDung);  

ALTER TABLE OrderDetail  
ADD CONSTRAINT FK_OrderDetail_Orders FOREIGN KEY (MaDonHang) REFERENCES Orders(MaDonHang);  

ALTER TABLE OrderDetail  
ADD CONSTRAINT FK_OrderDetail_Product FOREIGN KEY (MaSanPham) REFERENCES Product(MaSanPham);  

ALTER TABLE Assess  
ADD CONSTRAINT FK_Assess_Account FOREIGN KEY (MaNguoiDung) REFERENCES Account(MaNguoiDung);  

ALTER TABLE Assess  
ADD CONSTRAINT FK_Assess_Product FOREIGN KEY (MaSanPham) REFERENCES Product(MaSanPham);  

ALTER TABLE Payment  
ADD CONSTRAINT FK_Payment_Orders FOREIGN KEY (MaDonHang) REFERENCES Orders(MaDonHang);  

ALTER TABLE Ship  
ADD CONSTRAINT FK_Ship_Orders FOREIGN KEY (MaDonHang) REFERENCES Orders(MaDonHang);  

ALTER TABLE Discount  
ADD CONSTRAINT FK_Discount_Account FOREIGN KEY (MaNguoiDung) REFERENCES Account(MaNguoiDung);  

ALTER TABLE UseDiscount  
ADD CONSTRAINT FK_UseDiscount_Account FOREIGN KEY (MaNguoiDung) REFERENCES Account(MaNguoiDung);  

ALTER TABLE UseDiscount  
ADD CONSTRAINT FK_UseDiscount_Discount FOREIGN KEY (MaDiscount) REFERENCES Discount(MaDiscount);  

ALTER TABLE UseDiscount  
ADD CONSTRAINT FK_UseDiscount_Orders FOREIGN KEY (MaDonHang) REFERENCES Orders(MaDonHang);  

ALTER TABLE HinhAnh  
ADD CONSTRAINT FK_HinhAnh_Product FOREIGN KEY (MaSanPham) REFERENCES Product(MaSanPham);  

ALTER TABLE HinhAnh  
ADD CONSTRAINT FK_HinhAnh_MauSac FOREIGN KEY (MaMauSac) REFERENCES MauSac(MaMauSac);  

ALTER TABLE DetailProductMauSac  
ADD CONSTRAINT FK_DetailProductMauSac_Product FOREIGN KEY (MaSanPham) REFERENCES Product(MaSanPham);  

ALTER TABLE DetailProductMauSac  
ADD CONSTRAINT FK_DetailProductMauSac_MauSac FOREIGN KEY (MaMauSac) REFERENCES MauSac(MaMauSac);

Alter table ProductDungLuong
ADD CONSTRAINT FK_ProductDungLuong_Product FOREIGN KEY (MaSanPham) REFERENCES Product(MaSanPham);
	
Alter table ProductDungLuong
ADD CONSTRAINT FK_ProductDungLuong_DungLuong FOREIGN KEY (MaDungLuong) REFERENCES DungLuong(MaDungLuong); 

ALTER TABLE Payment  
ADD CONSTRAINT FK_Payment_PhuongThucThanhToan FOREIGN KEY (MaPhuongThuc) REFERENCES PhuongThucThanhToan(MaPhuongThuc); 

-- Insert dữ liệu cho bảng Account  
INSERT INTO Account (HoTen, Email, MatKhau, SoDienThoai, DiaChi, VaiTro) VALUES  
(N'Nguyễn Admin', 'admin@gmail.com', '123456', '0123456789', N'Hà Nội', 1),  
(N'Trần Khách Hàng', 'user1@gmail.com', '123456', '0987654321', N'TP.HCM', 2),  
(N'Lê Văn A', 'user2@gmail.com', '123456', '0369852147', N'Đà Nẵng', 2);  

-- Insert dữ liệu cho bảng Category  
INSERT INTO Category (TenDanhMuc, MoTa) VALUES  
(N'iPhone', N'Các dòng điện thoại iPhone'),  
(N'iPad', N'Các dòng máy tính bảng iPad'),  
(N'MacBook', N'Các dòng laptop MacBook'),  
(N'Apple Watch', N'Đồng hồ thông minh Apple Watch');  

-- Insert dữ liệu cho bảng Product  
INSERT INTO Product (TenSanPham, MaDanhMuc, MoTa) VALUES  
(N'iPhone 15 Pro Max', 1, N'iPhone 15 Pro Max mới nhất'),  
(N'iPhone 15 Pro', 1, N'iPhone 15 Pro phiên bản mới'),  
(N'iPad Pro M2', 2, N'iPad Pro chip M2 mạnh mẽ'),  
(N'MacBook Pro 14"', 3, N'MacBook Pro 14 inch mới nhất');  

-- Insert dữ liệu cho bảng MauSac  
INSERT INTO MauSac (EnglishName, TenMauSac) VALUES  
('Natural Titanium', N'Titan Tự Nhiên'),  
('Blue Titanium', N'Titan Xanh'),  
('White Titanium', N'Titan Trắng'),  
('Black Titanium', N'Titan Đen');  

-- Insert dữ liệu cho bảng DungLuong  
INSERT INTO DungLuong (SoDungLuong, DonVi) VALUES  
(128, 'GB'),  
(256, 'GB'),  
(512, 'GB'),  
(1, 'TB');  

-- Insert dữ liệu cho bảng DetailProductMauSac  
INSERT INTO DetailProductMauSac (MaMauSac, MaSanPham) VALUES  
(1, 1), -- iPhone 15 Pro Max - Titan Tự Nhiên  
(2, 1), -- iPhone 15 Pro Max - Titan Xanh  
(3, 1), -- iPhone 15 Pro Max - Titan Trắng  
(4, 1); -- iPhone 15 Pro Max - Titan Đen  

-- Insert dữ liệu cho bảng ProductDungLuong  
INSERT INTO ProductDungLuong (MaSanPham, MaDungLuong, Gia, SoLuongKho) VALUES  
(1, 1, 34990000, 50), -- iPhone 15 Pro Max 128GB  
(1, 2, 37990000, 40), -- iPhone 15 Pro Max 256GB  
(1, 3, 43990000, 30), -- iPhone 15 Pro Max 512GB  
(1, 4, 49990000, 20); -- iPhone 15 Pro Max 1TB  

-- Insert dữ liệu cho bảng HinhAnh  
INSERT INTO HinhAnh (LinkHinhAnh, MaSanPham, MaMauSac) VALUES  
('https://cdn.pico.vn/2024/01/16/170537354966251071641.png', 1, 1),  
('https://th.bing.com/th/id/OIP.sgrODs2ZfNW6ibsgb7vXLgHaHa?w=768&h=768&rs=1&pid=ImgDetMain', 1, 2),  
('https://www.ourfriday.co.uk/image/cache/catalog/Apple/apple-iphone-15-pro-uk-white-1-2-800x800w.png', 1, 3),  
('https://m-cdn.phonearena.com/images/phones/84461-350/Apple-iPhone-15-Pro-Max.webp?w=1', 1, 4);  

-- Insert dữ liệu cho bảng PhuongThucThanhToan  
INSERT INTO PhuongThucThanhToan (TenPhuongThuc) VALUES  
(N'Thanh toán khi nhận hàng (COD)'),  
(N'Chuyển khoản ngân hàng'),  
(N'Thẻ tín dụng/ghi nợ'),  
(N'Ví điện tử');  

-- Insert dữ liệu cho bảng Discount  
INSERT INTO Discount (MaCoupon, SL, PhanTram, NgayHetHan, MaNguoiDung) VALUES  
('WELCOME2024', 100, 10, DATEADD(MONTH, 1, GETDATE()), 1),  
('NEWYEAR2024', 50, 15, DATEADD(MONTH, 1, GETDATE()), 1),  
('TET2024', 30, 20, DATEADD(MONTH, 1, GETDATE()), 1);  

-- Insert dữ liệu cho bảng Orders  
INSERT INTO Orders (MaNguoiDung, TongTien, TrangThai) VALUES  
(2, 34990000, 1),  
(2, 37990000, 1),  
(3, 43990000, 1);  

-- Insert dữ liệu cho bảng OrderDetail  
INSERT INTO OrderDetail (MaDonHang, MaSanPham, SoLuong, Gia) VALUES  
(1, 1, 1, 34990000),  
(2, 1, 1, 37990000),  
(3, 1, 1, 43990000);  

-- Insert dữ liệu cho bảng Payment  
INSERT INTO Payment (MaDonHang, MaPhuongThuc, TrangThaiThanhToan, SoTienThanhToan) VALUES  
(1, 1, 1, 34990000),  
(2, 2, 1, 37990000),  
(3, 3, 1, 43990000);  

-- Insert dữ liệu cho bảng Ship  
INSERT INTO Ship (MaDonHang, MaVanDon, DonViVanChuyen, NgayDuKienGiao) VALUES  
(1, 'VN123456789', N'Giao hàng nhanh', DATEADD(DAY, 3, GETDATE())),  
(2, 'VN987654321', N'Giao hàng tiết kiệm', DATEADD(DAY, 5, GETDATE())),  
(3, 'VN456789123', N'Viettel Post', DATEADD(DAY, 4, GETDATE()));  

-- Insert dữ liệu cho bảng Assess  
INSERT INTO Assess (MaNguoiDung, MaSanPham, XepHang, BinhLuan) VALUES  
(2, 1, 5, N'Sản phẩm rất tốt, đáng mua'),  
(3, 1, 4, N'Chất lượng tốt, giá hơi cao'),  
(2, 2, 5, N'Rất hài lòng với sản phẩm');


-- Insert dữ liệu cho bảng UseDiscount  
INSERT INTO UseDiscount (MaNguoiDung, MaDiscount, MaDonHang) VALUES  
(2, 1, 1), -- Người dùng 2 sử dụng mã giảm giá WELCOME2024 cho đơn hàng 1  
(3, 2, 2), -- Người dùng 3 sử dụng mã giảm giá NEWYEAR2024 cho đơn hàng 2  
(2, 3, 3); -- Người dùng 2 sử dụng mã giảm giá TET2024 cho đơn hàng 3


-- 1. Kiểm tra bảng Account  
SELECT * FROM Account;  

-- 2. Kiểm tra bảng Category  
SELECT * FROM Category;  

-- 3. Kiểm tra bảng Product  
SELECT * FROM Product;  

-- 4. Kiểm tra bảng DungLuong  
SELECT * FROM DungLuong;  

-- 5. Kiểm tra bảng MauSac  
SELECT * FROM MauSac;  

-- 6. Kiểm tra bảng ProductDungLuong  
SELECT * FROM ProductDungLuong;  

-- 7. Kiểm tra bảng DetailProductMauSac  
SELECT * FROM DetailProductMauSac;  

-- 8. Kiểm tra bảng HinhAnh  
SELECT * FROM HinhAnh;  

-- 9. Kiểm tra bảng Discount  
SELECT * FROM Discount;  

-- 10. Kiểm tra bảng Orders  
SELECT * FROM Orders;  

-- 11. Kiểm tra bảng OrderDetail  
SELECT * FROM OrderDetail;  

-- 12. Kiểm tra bảng Payment  
SELECT * FROM Payment;  

-- 13. Kiểm tra bảng Ship  
SELECT * FROM Ship;  

-- 14. Kiểm tra bảng Assess  
SELECT * FROM Assess;  

-- 15. Kiểm tra bảng UseDiscount  
SELECT * FROM UseDiscount;

-- 16. Kiểm tra bảng UseDiscount  
SELECT * FROM PhuongThucThanhToan;