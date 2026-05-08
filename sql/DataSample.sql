USE QuanLyVeTau;
GO

-- XÓA DỮ LIỆU CŨ (NẾU CẦN)
-- DELETE FROM ChiTietHoaDon;
-- DELETE FROM HoaDon;
-- DELETE FROM ChiTietVeTau;
-- DELETE FROM VeTau;
-- DELETE FROM ChuyenTau;
-- DELETE FROM Toa;
-- DELETE FROM Tau;
-- DELETE FROM TuyenTau;
-- DELETE FROM KhachHang;
-- DELETE FROM NhanVien;
-- DELETE FROM TaiKhoan;
-- DELETE FROM KhuyenMai;
-- DELETE FROM DichVu;
-- DELETE FROM Thue;
-- GO

-- 1. THUẾ
INSERT INTO Thue (maThue, tenThue, phamTram, moTa) VALUES 
('T001', N'VAT', 10, N'Thuế giá trị gia tăng');

-- 2. TUYẾN TÀU
INSERT INTO TuyenTau (maTT, maGaDi, maGaDen, khoangCach) VALUES 
('TT001', N'Sài Gòn', N'Hà Nội', 1726),
('TT002', N'Sài Gòn', N'Đà Nẵng', 935),
('TT003', N'Sài Gòn', N'Nha Trang', 411),
('TT004', N'Sài Gòn', N'Huế', 1038),
('TT005', N'Sài Gòn', N'Phan Thiết', 186);

-- 3. TÀU
INSERT INTO Tau (maTau, tenTau, soLuongToa) VALUES 
('SE1', N'Tàu SE1', 10),
('SE2', N'Tàu SE2', 10),
('SE3', N'Tàu SE3', 12),
('SE4', N'Tàu SE4', 12),
('SPT1', N'Tàu Phan Thiết 1', 6);

-- 4. TOA
INSERT INTO Toa (maToa, maTau, loaiToa, soGhe, viTriToa, trangThai) VALUES 
('T1_SE1', 'SE1', N'Ngồi mềm', 64, '1', 1),
('T2_SE1', 'SE1', N'Ngồi mềm điều hòa', 64, '2', 1),
('T3_SE1', 'SE1', N'Nằm khoang 6', 42, '3', 1),
('T4_SE1', 'SE1', N'Nằm khoang 4', 28, '4', 1),
('T1_SE3', 'SE3', N'Ngồi mềm', 64, '1', 1),
('T2_SE3', 'SE3', N'Nằm khoang 4 điều hòa', 28, '2', 1);

-- 5. CHUYẾN TÀU (Cập nhật ngày khởi hành gần đây)
INSERT INTO ChuyenTau (maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai) VALUES 
('CT001', 'SE1', 'TT001', CAST(GETDATE() AS DATE), '22:00:00', 1),
('CT002', 'SE3', 'TT002', CAST(GETDATE() + 1 AS DATE), '06:00:00', 1),
('CT003', 'SE1', 'TT003', CAST(GETDATE() + 2 AS DATE), '13:30:00', 1),
('CT004', 'SPT1', 'TT005', CAST(GETDATE() AS DATE), '07:30:00', 1);

-- 6. DỊCH VỤ
INSERT INTO DichVu (maDV, tenDV, trangThai, giaTien) VALUES 
('DV001', N'Cơm hộp', 1, 35000),
('DV002', N'Nước suối', 1, 10000),
('DV003', N'Hành lý ký gửi', 1, 50000),
('DV004', N'Combo Ăn sáng', 1, 45000);

-- 7. TÀI KHOẢN (Password mặc định là 123456 cho dễ test)
INSERT INTO TaiKhoan (username, password, vaiTro) VALUES 
('admin', '123456', 'ADMIN'),
('nv001', '123456', 'NHAN_VIEN'),
('nv002', '123456', 'NHAN_VIEN');

-- 8. NHÂN VIÊN
INSERT INTO NhanVien (maNV, tenNV, sdt, gioiTinh, ngaySinh, ngayVaoLam, trangThai, email, chucVu, username) VALUES 
('NV001', N'Lâm Vy Xuân', '0987654321', 0, '2004-01-01', '2023-01-01', 1, 'lamvyx@tau.vn', N'Quản lý', 'admin'),
('NV002', N'Nguyễn Văn A', '0123456789', 1, '1995-10-10', '2023-05-01', 1, 'nva@tau.vn', N'Nhân viên bán vé', 'nv001'),
('NV003', N'Trần Thị B', '0999888777', 0, '1998-12-20', '2023-06-15', 1, 'ttb@tau.vn', N'Nhân viên bán vé', 'nv002');

-- 9. KHÁCH HÀNG
INSERT INTO KhachHang (maKH, tenKH, sdt, CCCD, diaChi, email, gioiTinh, ngaySinh, loaiKH) VALUES 
('KH001', N'Khách lẻ 1', '0901112223', '123456789001', N'TP. Hồ Chí Minh', 'kh1@gmail.com', 1, '1990-01-01', 0),
('KH002', N'Nguyễn Thị Lan', '0903334445', '123456789002', N'Hà Nội', 'lannt@gmail.com', 0, '1985-05-05', 1),
('KH003', N'Trần Văn Cường', '0905556667', '123456789003', N'Đà Nẵng', 'cuongtv@gmail.com', 1, '1992-08-08', 1);

-- 10. KHUYẾN MÃI
INSERT INTO KhuyenMai (maKM, tenKM, tyLeKM, ngayBD, ngayKT, dieuKienApDung) VALUES 
('KM001', N'Giảm giá mùa hè', 10, '2026-05-01', '2026-08-31', N'Tất cả các tuyến'),
('KM002', N'Tri ân thành viên', 15, '2026-01-01', '2026-12-31', N'Dành cho khách thành viên'),
('KM003', N'Khai trương tuyến mới', 20, '2026-05-01', '2026-05-31', N'Tuyến Sài Gòn - Phan Thiết');

-- 11. VÉ TÀU (Một số dữ liệu mẫu để hiện stats)
INSERT INTO VeTau (maVeTau, maKH, maCT, maToa, giaVe, trangThai) VALUES 
('VE00000001', 'KH002', 'CT001', 'T1_SE1', 1200000, 'DA_THANH_TOAN'),
('VE00000002', 'KH003', 'CT002', 'T2_SE3', 950000, 'DA_THANH_TOAN'),
('VE00000003', 'KH001', 'CT004', 'T1_SE1', 180000, 'CHO_THANH_TOAN'),
('VE00000004', 'KH002', 'CT001', 'T3_SE1', 850000, 'DA_SU_DUNG'),
('VE00000005', 'KH003', 'CT002', 'T4_SE1', 1500000, 'DA_HOAN');

-- 12. CHI TIẾT VÉ TÀU
INSERT INTO ChiTietVeTau (maChiTiet, maVeTau, tenHanhKhach, CCCD, ngaySinh, viTriGhe, loaiVe, giaVeTheoLoai) VALUES 
('CT00000001', 'VE00000001', N'Nguyễn Thị Lan', '123456789002', '1985-05-05', 'A01', 'NGUOI_LON', 1200000),
('CT00000002', 'VE00000002', N'Trần Văn Cường', '123456789003', '1992-08-08', 'B05', 'NGUOI_LON', 950000),
('CT00000003', 'VE00000003', N'Khách lẻ 1', '123456789001', '1990-01-01', 'A10', 'NGUOI_LON', 180000);

-- 13. HÓA ĐƠN
INSERT INTO HoaDon (maHD, maNV, maKH, maKM, maThue, thoiGian, phuongThucThanhToan, ngayThanhToan, trangThaiThanhToan) VALUES 
('HD2026050401', 'NV001', 'KH002', 'KM002', 'T001', GETDATE(), 'CHUYEN_KHOAN', GETDATE(), 1),
('HD2026050402', 'NV002', 'KH003', NULL, 'T001', GETDATE(), 'TIEN_MAT', GETDATE(), 1);

-- 14. CHI TIẾT HÓA ĐƠN
INSERT INTO ChiTietHoaDon (maCTHD, maHD, maVeTau, maDV, soLuong, donGia) VALUES 
('CTHD000001', 'HD2026050401', 'VE00000001', NULL, 1, 1200000),
('CTHD000002', 'HD2026050401', NULL, 'DV001', 2, 35000),
('CTHD000003', 'HD2026050402', 'VE00000002', NULL, 1, 950000);

PRINT N'Chèn dữ liệu mẫu thành công!';
GO
