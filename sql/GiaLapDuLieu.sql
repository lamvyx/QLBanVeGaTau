-- ============================================================================
-- SQL SCRIPT KHỞI TẠO CƠ SỞ DỮ LIỆU MỚI VÀ GIẢ LẬP DỮ LIỆU HỆ THỐNG QUẢN LÝ VÉ TÀU
-- Khởi tạo hoàn chỉnh Database, Bảng, Ràng buộc, View và nạp Dữ liệu mô phỏng
-- Neo thời gian hệ thống (Anchor Date): 2026-05-27
-- ============================================================================

USE master;
GO

-- 1. DROP DATABASE NẾU ĐÃ TỒN TẠI (Đóng tất cả kết nối cũ để tránh lỗi Database in use)
PRINT N'Đang kiểm tra và xóa cơ sở dữ liệu cũ nếu có...';
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'QLBANVETAU')
BEGIN
    ALTER DATABASE QLBANVETAU SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QLBANVETAU;
    PRINT N'✓ Đã xóa cơ sở dữ liệu QLBANVETAU cũ.';
END
GO

-- 2. TẠO CƠ SỞ DỮ LIỆU MỚI
PRINT N'Đang tạo cơ sở dữ liệu QLBANVETAU mới...';
CREATE DATABASE QLBANVETAU;
GO

USE QLBANVETAU;
GO

-- ============================================================================
-- PHẦN I: KHỞI TẠO CẤU TRÚC BẢNG (DDL)
-- ============================================================================
PRINT N'Đang tạo các bảng dữ liệu...';

-- 1. THUẾ
CREATE TABLE Thue (
    maThue      VARCHAR(10)     NOT NULL,
    tenThue     NVARCHAR(100)   NOT NULL,
    phamTram    FLOAT           NOT NULL,
    moTa        NVARCHAR(255)   NULL,
    CONSTRAINT PK_Thue PRIMARY KEY (maThue),
    CONSTRAINT CHK_Thue_PhamTram CHECK (phamTram >= 0 AND phamTram <= 100)
);

-- 2. TUYẾN TÀU
CREATE TABLE TuyenTau (
    maTT        VARCHAR(10)     NOT NULL,
    maGaDi      NVARCHAR(100)   NOT NULL,
    maGaDen     NVARCHAR(100)   NOT NULL,
    khoangCach  INT             NOT NULL,
    CONSTRAINT PK_TuyenTau PRIMARY KEY (maTT),
    CONSTRAINT CHK_TuyenTau_KhoangCach CHECK (khoangCach > 0)
);

-- 3. TÀU
CREATE TABLE Tau (
    maTau       VARCHAR(10)     NOT NULL,
    tenTau      NVARCHAR(100)   NOT NULL,
    soLuongToa  INT             NOT NULL,
    CONSTRAINT PK_Tau PRIMARY KEY (maTau),
    CONSTRAINT CHK_Tau_SoLuongToa CHECK (soLuongToa > 0)
);

-- 4. TOA TÀU
CREATE TABLE Toa (
    maToa       VARCHAR(10)     NOT NULL,
    maTau       VARCHAR(10)     NULL,
    loaiToa     NVARCHAR(50)    NOT NULL,
    soGhe       INT             NOT NULL,
    viTriToa    NVARCHAR(10)    NULL,
    trangThai   BIT             NOT NULL DEFAULT 1,
    CONSTRAINT PK_Toa PRIMARY KEY (maToa),
    CONSTRAINT FK_Toa_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau),
    CONSTRAINT CHK_Toa_SoGhe CHECK (soGhe > 0)
);

-- 5. CHUYẾN TÀU
CREATE TABLE ChuyenTau (
    maCT            VARCHAR(10)     NOT NULL,
    maTau           VARCHAR(10)     NOT NULL,
    maTuyenTau      VARCHAR(10)     NOT NULL,
    ngayKhoiHanh    DATE            NOT NULL,
    gioKhoiHanh     TIME            NOT NULL,
    trangThai       VARCHAR(20)     NOT NULL DEFAULT 'DA_LEN_LICH',
    CONSTRAINT PK_ChuyenTau PRIMARY KEY (maCT),
    CONSTRAINT FK_ChuyenTau_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau),
    CONSTRAINT FK_ChuyenTau_TuyenTau FOREIGN KEY (maTuyenTau) REFERENCES TuyenTau(maTT),
    CONSTRAINT CHK_ChuyenTau_TrangThai CHECK (trangThai IN ('DA_LEN_LICH', 'DANG_CHAY', 'DA_HOAN_THANH'))
);

-- 5.1 CHI TIẾT CHUYẾN TÀU - TOA
CREATE TABLE ChiTietChuyenTau_Toa (
    maCT        VARCHAR(10)     NOT NULL,
    maToa       VARCHAR(10)     NOT NULL,
    thuTuToa    INT             NOT NULL DEFAULT 1,
    CONSTRAINT PK_ChiTietChuyenTau_Toa PRIMARY KEY (maCT, maToa),
    CONSTRAINT FK_ChiTietChuyenTau_Toa_ChuyenTau FOREIGN KEY (maCT) REFERENCES ChuyenTau(maCT),
    CONSTRAINT FK_ChiTietChuyenTau_Toa_Toa FOREIGN KEY (maToa) REFERENCES Toa(maToa),
    CONSTRAINT CHK_ChiTietChuyenTau_Toa_ThuTu CHECK (thuTuToa > 0)
);

-- 6. DỊCH VỤ
CREATE TABLE DichVu (
    maDV        VARCHAR(10)     NOT NULL,
    tenDV       NVARCHAR(100)   NOT NULL,
    trangThai   BIT             NOT NULL DEFAULT 1,
    giaTien     FLOAT           NOT NULL,
    CONSTRAINT PK_DichVu PRIMARY KEY (maDV),
    CONSTRAINT CHK_DichVu_GiaTien CHECK (giaTien >= 0)
);

-- 7. TÀI KHOẢN (chỉ dành cho nhân viên)
CREATE TABLE TaiKhoan (
    username    VARCHAR(50)     NOT NULL,
    password    NVARCHAR(255)   NOT NULL,
    vaiTro      VARCHAR(20)     NOT NULL,
    CONSTRAINT PK_TaiKhoan PRIMARY KEY (username),
    CONSTRAINT CHK_TaiKhoan_VaiTro CHECK (vaiTro IN ('ADMIN', 'NHAN_VIEN'))
);

-- 8. NHÂN VIÊN
CREATE TABLE NhanVien (
    maNV        VARCHAR(10)     NOT NULL,
    tenNV       NVARCHAR(100)   NOT NULL,
    sdt         VARCHAR(15)     NULL,
    gioiTinh    BIT             NULL,
    ngaySinh    DATE            NULL,
    ngayVaoLam  DATE            NULL,
    trangThai   BIT             NOT NULL DEFAULT 1,
    email       NVARCHAR(100)   NULL,
    chucVu      NVARCHAR(50)    NULL,
    username    VARCHAR(50)     NOT NULL,
    CONSTRAINT PK_NhanVien PRIMARY KEY (maNV),
    CONSTRAINT FK_NhanVien_TaiKhoan FOREIGN KEY (username) REFERENCES TaiKhoan(username),
    CONSTRAINT UQ_NhanVien_Username UNIQUE (username)
);

-- 9. KHÁCH HÀNG (do nhân viên tạo, không có tài khoản)
CREATE TABLE KhachHang (
    maKH        VARCHAR(10)     NOT NULL,
    tenKH       NVARCHAR(100)   NOT NULL,
    sdt         VARCHAR(15)     NOT NULL,
    CCCD        VARCHAR(20)     NULL,
    diaChi      NVARCHAR(255)   NULL,
    email       NVARCHAR(100)   NULL,
    gioiTinh    BIT             NULL,
    ngaySinh    DATE            NULL,
    loaiKH      BIT             NOT NULL DEFAULT 0,
    CONSTRAINT PK_KhachHang PRIMARY KEY (maKH),
    CONSTRAINT UQ_KhachHang_CCCD UNIQUE (CCCD)
);

-- 10. KHUYẾN MÃI
CREATE TABLE KhuyenMai (
    maKM            VARCHAR(10)     NOT NULL,
    tenKM           NVARCHAR(100)   NOT NULL,
    tyLeKM          FLOAT           NOT NULL,
    ngayBD          DATE            NOT NULL,
    ngayKT          DATE            NOT NULL,
    dieuKienApDung  NVARCHAR(255)   NULL,
    CONSTRAINT PK_KhuyenMai PRIMARY KEY (maKM),
    CONSTRAINT CHK_KhuyenMai_TyLe CHECK (tyLeKM >= 0 AND tyLeKM <= 100),
    CONSTRAINT CHK_KhuyenMai_Ngay CHECK (ngayKT >= ngayBD)
);

-- 11. VÉ TÀU
CREATE TABLE VeTau (
    maVeTau     VARCHAR(10)     NOT NULL,
    maKH        VARCHAR(10)     NOT NULL,
    maCT        VARCHAR(10)     NOT NULL,
    maToa       VARCHAR(10)     NOT NULL,
    viTriGhe     VARCHAR(10)    NULL,
    loaiVe       VARCHAR(20)    NULL,
    giaVe        FLOAT          NOT NULL,
    trangThai    VARCHAR(20)    NOT NULL DEFAULT 'CHO_THANH_TOAN',
    CONSTRAINT PK_VeTau PRIMARY KEY (maVeTau),
    CONSTRAINT FK_VeTau_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_VeTau_ChuyenTau FOREIGN KEY (maCT) REFERENCES ChuyenTau(maCT),
    CONSTRAINT FK_VeTau_Toa FOREIGN KEY (maToa) REFERENCES Toa(maToa),
    CONSTRAINT CHK_VeTau_LoaiVe CHECK (loaiVe IN ('NGUOI_LON', 'TRE_EM', 'NGUOI_CAO_TUOI') OR loaiVe IS NULL),
    CONSTRAINT CHK_VeTau_TrangThai CHECK (trangThai IN (
        'CHO_THANH_TOAN', 'DA_THANH_TOAN', 'DA_HOAN', 'DA_SU_DUNG'
    )),
    CONSTRAINT CHK_VeTau_GiaVe CHECK (giaVe >= 0)
);

-- 12. HÓA ĐƠN
CREATE TABLE HoaDon (
    maHD                VARCHAR(10)     NOT NULL,
    maNV                VARCHAR(10)     NOT NULL,
    maKH                VARCHAR(10)     NOT NULL,
    maKM                VARCHAR(10)     NULL,
    maThue              VARCHAR(10)     NULL,
    thoiGian            DATE            NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    phuongThucThanhToan VARCHAR(20)     NULL,
    ngayThanhToan       DATE            NULL,
    trangThaiThanhToan  BIT             NOT NULL DEFAULT 0,
    CONSTRAINT PK_HoaDon PRIMARY KEY (maHD),
    CONSTRAINT FK_HoaDon_NhanVien  FOREIGN KEY (maNV)   REFERENCES NhanVien(maNV),
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKH)   REFERENCES KhachHang(maKH),
    CONSTRAINT FK_HoaDon_KhuyenMai FOREIGN KEY (maKM)   REFERENCES KhuyenMai(maKM),
    CONSTRAINT FK_HoaDon_Thue      FOREIGN KEY (maThue) REFERENCES Thue(maThue),
    CONSTRAINT CHK_HoaDon_PhuongThuc CHECK (phuongThucThanhToan IN (
        'TIEN_MAT', 'CHUYEN_KHOAN', 'VI_DIEN_TU'
    ))
);

-- 13. CHI TIẾT HÓA ĐƠN - VÉ
CREATE TABLE ChiTietHoaDon_Ve (
    maHD        VARCHAR(10)     NOT NULL,
    maVeTau     VARCHAR(10)     NOT NULL,
    soLuong     INT             NOT NULL DEFAULT 1,
    donGia      FLOAT           NOT NULL,
    CONSTRAINT PK_ChiTietHoaDon_Ve PRIMARY KEY (maHD, maVeTau),
    CONSTRAINT FK_ChiTietHoaDonVe_HoaDon FOREIGN KEY (maHD)   REFERENCES HoaDon(maHD),
    CONSTRAINT FK_ChiTietHoaDonVe_VeTau FOREIGN KEY (maVeTau) REFERENCES VeTau(maVeTau),
    CONSTRAINT CHK_ChiTietHoaDonVe_SoLuong CHECK (soLuong > 0),
    CONSTRAINT CHK_ChiTietHoaDonVe_DonGia CHECK (donGia >= 0)
);

-- 14. CHI TIẾT HÓA ĐƠN - DỊCH VỤ
CREATE TABLE ChiTietHoaDon_DichVu (
    maHD        VARCHAR(10)     NOT NULL,
    maDV        VARCHAR(10)     NOT NULL,
    soLuong     INT             NOT NULL DEFAULT 1,
    donGia      FLOAT           NOT NULL,
    CONSTRAINT PK_ChiTietHoaDon_DV PRIMARY KEY (maHD, maDV),
    CONSTRAINT FK_ChiTietHoaDonDV_HoaDon FOREIGN KEY (maHD)  REFERENCES HoaDon(maHD),
    CONSTRAINT FK_ChiTietHoaDonDV_DichVu FOREIGN KEY (maDV)  REFERENCES DichVu(maDV),
    CONSTRAINT CHK_ChiTietHoaDonDV_SoLuong CHECK (soLuong > 0),
    CONSTRAINT CHK_ChiTietHoaDonDV_DonGia CHECK (donGia >= 0)
);

-- 15. PHIẾU ĐẶT VÉ (Giữ chỗ)
CREATE TABLE PhieuDatVe (
    maPhieu     VARCHAR(10)     NOT NULL,
    maKH        VARCHAR(10)     NOT NULL,
    maNV        VARCHAR(10)     NULL,
    ngayDat     DATE            NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    hanThanhToan DATE           NULL,
    trangThai   BIT             NOT NULL DEFAULT 1,
    CONSTRAINT PK_PhieuDatVe PRIMARY KEY (maPhieu),
    CONSTRAINT FK_PhieuDatVe_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_PhieuDatVe_NhanVien  FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);

-- 16. CHI TIẾT PHIẾU ĐẶT
CREATE TABLE ChiTietPhieuDat (
    maPhieu     VARCHAR(10)     NOT NULL,
    maCT        VARCHAR(10)     NOT NULL,
    maToa       VARCHAR(10)     NOT NULL,
    viTriGhe    NVARCHAR(100)   NOT NULL,
    giaVe       FLOAT           NOT NULL,
    ghiChu      NVARCHAR(255)   NULL,
    CONSTRAINT PK_ChiTietPhieuDat PRIMARY KEY (maPhieu, maCT, maToa, viTriGhe),
    CONSTRAINT FK_ChiTietPhieuDat_PhieuDat FOREIGN KEY (maPhieu) REFERENCES PhieuDatVe(maPhieu),
    CONSTRAINT FK_ChiTietPhieuDat_ChuyenTau FOREIGN KEY (maCT) REFERENCES ChuyenTau(maCT),
    CONSTRAINT FK_ChiTietPhieuDat_Toa FOREIGN KEY (maToa) REFERENCES Toa(maToa),
    CONSTRAINT CHK_ChiTietPhieuDat_GiaVe CHECK (giaVe >= 0)
);
GO

-- ============================================================================
-- PHẦN II: KHỞI TẠO CÁC KHUNG NHÌN THỐNG KÊ (VIEWS)
-- ============================================================================
PRINT N'Đang tạo các Views hỗ trợ thống kê...';
GO

-- 1. VIEW TÍNH THÀNH TIỀN CHI TIẾT HÓA ĐƠN (CÓ THUẾ)
CREATE VIEW v_ChiTietHoaDon AS
SELECT
    CAST(NULL AS VARCHAR(10)) AS maCTHD,
    v.maHD,
    v.maVeTau,
    CAST(NULL AS VARCHAR(10)) AS maDV,
    v.soLuong,
    v.donGia,
    (v.soLuong * v.donGia)                                                AS thanhTien,
    (v.soLuong * v.donGia * (1 + ISNULL(t.phamTram, 0) / 100.0))        AS thanhTienSauThue
FROM ChiTietHoaDon_Ve v
JOIN HoaDon hd ON v.maHD = hd.maHD
LEFT JOIN Thue t ON hd.maThue = t.maThue

UNION ALL

SELECT
    CAST(NULL AS VARCHAR(10)) AS maCTHD,
    d.maHD,
    CAST(NULL AS VARCHAR(10)) AS maVeTau,
    d.maDV,
    d.soLuong,
    d.donGia,
    (d.soLuong * d.donGia)                                                AS thanhTien,
    (d.soLuong * d.donGia * (1 + ISNULL(t.phamTram, 0) / 100.0))        AS thanhTienSauThue
FROM ChiTietHoaDon_DichVu d
JOIN HoaDon hd ON d.maHD = hd.maHD
LEFT JOIN Thue t ON hd.maThue = t.maThue;
GO

-- 2. VIEW TỔNG TIỀN HÓA ĐƠN (SAU THUẾ + KHUYẾN MÃI)
CREATE VIEW v_TongTienHoaDon AS
SELECT
    hd.maHD,
    hd.maNV,
    hd.maKH,
    SUM(ct.soLuong * ct.donGia)                                             AS tongTruocThue,
    SUM(ct.soLuong * ct.donGia * (1 + ISNULL(t.phamTram, 0) / 100.0))     AS tongSauThue,
    ISNULL(km.tyLeKM, 0)                                                    AS tyLeKhuyenMai,
    SUM(ct.soLuong * ct.donGia * (1 + ISNULL(t.phamTram, 0) / 100.0))
        * (1 - ISNULL(km.tyLeKM, 0) / 100.0)                               AS tongThanhToan
FROM HoaDon hd
JOIN  v_ChiTietHoaDon ct ON hd.maHD   = ct.maHD
LEFT JOIN Thue t        ON hd.maThue = t.maThue
LEFT JOIN KhuyenMai km  ON hd.maKM   = km.maKM
GROUP BY hd.maHD, hd.maNV, hd.maKH, km.tyLeKM;
GO

-- 3. VIEW TÍNH SỐ CHỖ CÒN TRỐNG THEO TOA TRONG TỪNG CHUYẾN TÀU
CREATE VIEW v_SoChoTrong AS
SELECT
    ct.maCT,
    toa.maToa,
    toa.loaiToa,
    toa.soGhe,
        COUNT(DISTINCT v.maVeTau)           AS soVeDaBan,
        COUNT(DISTINCT v.maVeTau)
            + COUNT(DISTINCT CASE WHEN pd.trangThai = 1 THEN ctpd.maPhieu + N'|' + ctpd.viTriGhe END) AS soVeDaBanVaGiu,
        (toa.soGhe - (
                COUNT(DISTINCT v.maVeTau)
            + COUNT(DISTINCT CASE WHEN pd.trangThai = 1 THEN ctpd.maPhieu + N'|' + ctpd.viTriGhe END)
        )) AS soChoCon
FROM ChuyenTau ct
JOIN  ChiTietChuyenTau_Toa cttt ON cttt.maCT = ct.maCT
JOIN  Toa toa  ON toa.maToa = cttt.maToa
LEFT JOIN VeTau v ON v.maCT  = ct.maCT
    AND v.maToa = toa.maToa
    AND v.trangThai != 'DA_HOAN'
LEFT JOIN ChiTietPhieuDat ctpd ON ctpd.maCT = ct.maCT AND ctpd.maToa = toa.maToa
LEFT JOIN PhieuDatVe pd ON pd.maPhieu = ctpd.maPhieu
GROUP BY ct.maCT, toa.maToa, toa.loaiToa, toa.soGhe;
GO

-- 4. VIEW THỐNG KÊ DOANH THU THEO NHÂN VIÊN BÁN VÉ
CREATE VIEW v_DoanhThuNhanVien AS
SELECT
    nv.maNV,
    nv.tenNV,
    COUNT(hd.maHD)          AS soHoaDon,
    SUM(tt.tongThanhToan)   AS tongDoanhThu
FROM NhanVien nv
JOIN HoaDon hd           ON hd.maNV = nv.maNV
JOIN v_TongTienHoaDon tt ON tt.maHD = hd.maHD
WHERE hd.trangThaiThanhToan = 1
GROUP BY nv.maNV, nv.tenNV;
GO


-- ============================================================================
-- PHẦN III: NẠP DỮ LIỆU GIẢ LẬP HỆ THỐNG (DML)
-- ============================================================================

-- 1. THUẾ (12 loại thuế/phí)
PRINT N'Đang nạp dữ liệu Thuế...';
INSERT INTO Thue (maThue, tenThue, phamTram, moTa) VALUES
('T001', N'Thuế GTGT (VAT)', 10.0, N'Thuế giá trị gia tăng mặc định'),
('T002', N'Thuế Bảo vệ Môi trường', 2.0, N'Thuế bảo vệ môi trường đường sắt'),
('T003', N'Thuế Dịch vụ Đặc biệt', 5.0, N'Thuế tiêu thụ đặc biệt cho dịch vụ cao cấp'),
('T004', N'Thuế GTGT Giảm', 8.0, N'Nghị quyết giảm thuế VAT hỗ trợ đi lại'),
('T005', N'Thuế Sử dụng Ga Tàu', 1.0, N'Thuế khai thác hạ tầng nhà ga'),
('T006', N'Phí Dịch vụ Hành lý', 3.0, N'Thuế dịch vụ ký gửi hành lý vượt định mức'),
('T007', N'Phí Bảo hiểm Hành khách', 1.5, N'Thuế bảo hiểm tai nạn giao thông đường sắt'),
('T008', N'Phí An ninh Đường sắt', 0.5, N'Phí bảo đảm an ninh quốc phòng ngành đường sắt'),
('T009', N'Phí Vệ sinh Môi trường', 0.2, N'Thuế xử lý chất thải trên toa'),
('T010', N'Phí Duy tu Đường ray', 0.8, N'Phí đóng góp quỹ duy tu hạ tầng kỹ thuật'),
('T011', N'Thuế Tiêu dùng Đồ uống cồn', 6.0, N'Áp dụng cho đồ uống có cồn bán trên toa hàng ăn'),
('T012', N'Phí Đô thị Ga trung tâm', 0.4, N'Thuế hạ tầng giao thông đô thị quanh ga');
GO

-- 2. TÀI KHOẢN (12 tài khoản nhân viên và admin)
PRINT N'Đang nạp dữ liệu Tài khoản...';
INSERT INTO TaiKhoan (username, password, vaiTro) VALUES
('admin01', '123456', 'ADMIN'),
('admin02', '123456', 'ADMIN'),
('nv001', '123456', 'NHAN_VIEN'),
('nv002', '123456', 'NHAN_VIEN'),
('nv003', '123456', 'NHAN_VIEN'),
('nv004', '123456', 'NHAN_VIEN'),
('nv005', '123456', 'NHAN_VIEN'),
('nv006', '123456', 'NHAN_VIEN'),
('nv007', '123456', 'NHAN_VIEN'),
('nv008', '123456', 'NHAN_VIEN'),
('nv009', '123456', 'NHAN_VIEN'),
('nv010', '123456', 'NHAN_VIEN');
GO

-- 3. NHÂN VIÊN (12 nhân viên tương ứng)
PRINT N'Đang nạp dữ liệu Nhân viên...';
INSERT INTO NhanVien (maNV, tenNV, sdt, gioiTinh, ngaySinh, ngayVaoLam, trangThai, email, chucVu, username) VALUES
('NV001', N'Nguyễn Văn An', '0901234567', 1, '1990-05-15', '2020-01-01', 1, 'nva@tau.vn', N'Nhân viên bán vé', 'nv001'),
('NV002', N'Trần Thị Bích', '0912345678', 0, '1995-03-20', '2021-06-01', 1, 'ttb@tau.vn', N'Nhân viên bán vé', 'nv002'),
('NV003', N'Lê Văn Cường', '0923456789', 1, '1988-11-10', '2018-03-15', 1, 'lvc@tau.vn', N'Quản trị hệ thống', 'admin01'),
('NV004', N'Phạm Thị Dung', '0934567890', 0, '1992-07-25', '2019-10-01', 1, 'ptdung@tau.vn', N'Nhân viên bán vé', 'nv004'),
('NV005', N'Hoàng Văn Em', '0945678901', 1, '1987-12-05', '2017-05-20', 1, 'hve@tau.vn', N'Nhân viên bán vé', 'nv005'),
('NV006', N'Vũ Thị Phượng', '0956789012', 0, '1996-09-18', '2022-02-10', 1, 'vtphuong@tau.vn', N'Nhân viên bán vé', 'nv006'),
('NV007', N'Đỗ Văn Giang', '0967890123', 1, '1993-04-12', '2020-08-15', 1, 'dvgiang@tau.vn', N'Nhân viên bán vé', 'nv007'),
('NV008', N'Nguyễn Thị Hoa', '0978901234', 0, '1998-01-30', '2023-03-01', 1, 'nthoa@tau.vn', N'Nhân viên bán vé', 'nv008'),
('NV009', N'Bùi Văn Khánh', '0989012345', 1, '1991-06-22', '2019-11-11', 1, 'bvkhanh@tau.vn', N'Nhân viên bán vé', 'nv009'),
('NV010', N'Trịnh Minh Tuấn', '0990123456', 1, '1985-08-10', '2016-09-01', 1, 'admin02@tau.vn', N'Quản trị hệ thống', 'admin02'),
('NV011', N'Lý Thị Ngọc', '0901112223', 0, '1994-09-12', '2021-07-01', 1, 'ltngoc@tau.vn', N'Nhân viên bán vé', 'nv003'),
('NV012', N'Đinh Văn Hùng', '0902223334', 1, '1989-11-20', '2019-04-15', 1, 'dvhung@tau.vn', N'Nhân viên bán vé', 'nv010');
GO

-- 4. KHÁCH HÀNG (20 khách hàng)
PRINT N'Đang nạp dữ liệu Khách hàng...';
INSERT INTO KhachHang (maKH, tenKH, sdt, CCCD, diaChi, email, gioiTinh, ngaySinh, loaiKH) VALUES
('KH001', N'Phạm Văn Dũng', '0934567890', '012345678901', N'Hà Nội', 'pvd@gmail.com', 1, '1992-07-25', 0),
('KH002', N'Ngô Thị Lan', '0945678901', '012345678902', N'TP.HCM', 'ntl@gmail.com', 0, '1998-02-14', 1),
('KH003', N'Trần Thị Hương', '0956789012', '012345678903', N'Đà Nẵng', 'tthuong@gmail.com', 0, '1995-12-10', 1),
('KH004', N'Lê Văn An', '0967890123', '012345678904', N'Hải Phòng', 'levana@gmail.com', 1, '1990-05-20', 1),
('KH005', N'Hoàng Thị Bích', '0978901234', '012345678905', N'Cần Thơ', 'htbich@gmail.com', 0, '1988-09-15', 0),
('KH006', N'Vũ Văn Cường', '0989012345', '012345678906', N'Hà Nội', 'vvc1992@gmail.com', 1, '1992-03-08', 1),
('KH007', N'Phạm Thị Dung', '0990123456', '012345678907', N'Bình Dương', 'ptdung@gmail.com', 0, '2000-07-30', 1),
('KH008', N'Nguyễn Văn Em', '0901234567', '012345678908', N'TP.HCM', 'nve@gmail.com', 1, '1996-11-22', 0),
('KH009', N'Đỗ Thị Phượng', '0912345678', '012345678909', N'Quảng Ninh', 'dtphuong@gmail.com', 0, '1993-04-05', 1),
('KH010', N'Bùi Văn Giang', '0923456789', '012345678910', N'Hà Nội', 'bvgiang@gmail.com', 1, '1985-08-17', 1),
('KH011', N'Trịnh Thị Hoa', '0934567891', '012345678911', N'Đà Nẵng', 'tthoa@gmail.com', 0, '1999-01-28', 0),
('KH012', N'Đặng Văn Khánh', '0945678902', '012345678912', N'Hải Phòng', 'dvkhanh@gmail.com', 1, '1994-06-12', 1),
('KH013', N'Nguyễn Thị Linh', '0956789013', '012345678913', N'Bắc Ninh', 'ntlinh@gmail.com', 0, '1997-09-03', 1),
('KH014', N'Phan Văn Minh', '0967890124', '012345678914', N'Hưng Yên', 'pvminh@gmail.com', 1, '1991-12-19', 0),
('KH015', N'Lý Thị Ngọc', '0978901235', '012345678915', N'TP.HCM', 'ltngoc@gmail.com', 0, '2001-04-25', 1),
('KH016', N'Đinh Văn Hùng', '0989012346', '012345678916', N'Hà Nam', 'dvhung@gmail.com', 1, '1989-11-11', 1),
('KH017', N'Mai Thị Thanh', '0990123457', '012345678917', N'Thanh Hóa', 'mtthanh@gmail.com', 0, '1993-06-07', 0),
('KH018', N'Lương Văn Tú', '0901234568', '012345678918', N'Hà Tĩnh', 'lvtu@gmail.com', 1, '1995-02-28', 1),
('KH019', N'Vương Thị Hải', '0912345679', '012345678919', N'Nam Định', 'vthai@gmail.com', 0, '1998-08-14', 1),
('KH020', N'Trương Văn Hiếu', '0923456780', '012345678920', N'Quảng Nam', 'tvhieu@gmail.com', 1, '1990-10-01', 0);
GO

-- 5. DỊCH VỤ (12 dịch vụ ăn uống và tiện ích)
PRINT N'Đang nạp dữ liệu Dịch vụ...';
INSERT INTO DichVu (maDV, tenDV, trangThai, giaTien) VALUES
('DV001', N'Suất ăn mặn tiêu chuẩn', 1, 50000.0),
('DV002', N'Hành lý ký gửi quá khổ', 1, 30000.0),
('DV003', N'Nước khoáng đóng chai', 1, 15000.0),
('DV004', N'Cà phê đá Việt Nam', 1, 25000.0),
('DV005', N'Suất ăn chay dinh dưỡng', 1, 45000.0),
('DV006', N'Gối ngủ cổ cao cấp', 1, 60000.0),
('DV007', N'Khăn lạnh kháng khuẩn', 1, 5000.0),
('DV008', N'Đồ ăn vặt (Bánh ngọt/Snack)', 1, 20000.0),
('DV009', N'Nước ngọt lon (Coca/Pesi)', 1, 22000.0),
('DV010', N'Dịch vụ Wifi tốc độ cao VIP', 1, 40000.0),
('DV011', N'Phòng chờ hạng thương gia', 1, 100000.0),
('DV012', N'Đón đưa tận nơi (Bán kính 5km)', 1, 80000.0);
GO

-- 6. KHUYẾN MÃI (12 chương trình khuyến mãi)
PRINT N'Đang nạp dữ liệu Khuyến mãi...';
INSERT INTO KhuyenMai (maKM, tenKM, tyLeKM, ngayBD, ngayKT, dieuKienApDung) VALUES
('KM001', N'Tri ân hè rực rỡ', 10.0, '2026-06-01', '2026-08-31', N'Áp dụng cho mọi vé khoang nằm'),
('KM002', N'Ưu đãi thành viên VIP', 5.0, '2026-01-01', '2026-12-31', N'Áp dụng cho khách hàng có thẻ thành viên'),
('KM003', N'Chào xuân Bính Ngọ', 15.0, '2026-01-15', '2026-02-15', N'Ưu đãi dịp tết nguyên đán'),
('KM004', N'Mừng ngày Giải Phóng', 8.0, '2026-04-25', '2026-05-05', N'Dành riêng dịp nghỉ lễ 30/4 - 1/5'),
('KM005', N'Ưu đãi tựu trường', 12.0, '2026-08-15', '2026-09-15', N'Ưu đãi cho học sinh, sinh viên'),
('KM006', N'Mùa vàng du lịch Thu', 6.0, '2026-09-01', '2026-10-31', N'Khuyến khích du lịch mùa thu'),
('KM007', N'Tết Dương Lịch 2026', 10.0, '2025-12-25', '2026-01-05', N'Chào đón năm mới đầu năm'),
('KM008', N'Ưu đãi Ngày Nhà Giáo', 7.0, '2026-11-15', '2026-11-25', N'Tri ân thầy cô giáo Việt Nam'),
('KM009', N'Giảm giá mùa đông trầm ấm', 5.0, '2025-11-01', '2025-11-30', N'Kích cầu hành khách đi lại mùa đông'),
('KM010', N'Lễ hội Giáng Sinh lung linh', 10.0, '2025-12-20', '2025-12-30', N'Khuyến mãi mùa Giáng Sinh'),
('KM011', N'Quốc Khánh nước VN', 9.0, '2026-08-30', '2026-09-05', N'Kỷ niệm ngày Quốc Khánh 2/9'),
('KM012', N'Đại tiệc vé đôi chặng xa', 12.5, '2026-03-01', '2026-03-31', N'Giảm giá sâu chặng Bắc Nam mua từ 2 vé');
GO

-- 7. TUYẾN TÀU (15 tuyến)
PRINT N'Đang nạp dữ liệu Tuyến tàu...';
INSERT INTO TuyenTau (maTT, maGaDi, maGaDen, khoangCach) VALUES
('TT001', N'Sài Gòn', N'Hà Nội', 1726),
('TT002', N'Sài Gòn', N'Đà Nẵng', 935),
('TT003', N'Sài Gòn', N'Nha Trang', 411),
('TT004', N'Sài Gòn', N'Vinh', 1407),
('TT005', N'Nha Trang', N'Đà Nẵng', 528),
('TT006', N'Nha Trang', N'Hà Nội', 1315),
('TT007', N'Đà Nẵng', N'Huế', 103),
('TT008', N'Đà Nẵng', N'Hà Nội', 791),
('TT009', N'Hà Nội', N'Hải Phòng', 102),
('TT010', N'Hà Nội', N'Vinh', 319),
('TT011', N'Hà Nội', N'Huế', 979),
('TT012', N'Vinh', N'Sài Gòn', 1407),
('TT013', N'Huế', N'Sài Gòn', 978),
('TT014', N'Hải Phòng', N'Hà Nội', 102),
('TT015', N'Đà Nẵng', N'Nha Trang', 528);
GO

-- 8. TÀU (12 đoàn tàu)
PRINT N'Đang nạp dữ liệu Đoàn tàu...';
INSERT INTO Tau (maTau, tenTau, soLuongToa) VALUES
('TAU001', N'Thống Nhất SE1', 5),
('TAU002', N'Thống Nhất SE2', 5),
('TAU003', N'Thống Nhất SE3', 5),
('TAU004', N'Thống Nhất SE4', 5),
('TAU005', N'Thống Nhất SE5', 5),
('TAU006', N'Thống Nhất SE6', 5),
('TAU007', N'Thống Nhất SE7', 5),
('TAU008', N'Thống Nhất SE8', 5),
('TAU009', N'Tàu Chợ TN1', 5),
('TAU010', N'Tàu Chợ TN2', 5),
('TAU011', N'Du Lịch Sapa SP1', 5),
('TAU012', N'Du Lịch Sapa SP2', 5);
GO

-- 9. TOA TÀU (72 toa tàu, có dư 12 toa sẵn có trong kho với maTau = NULL)
PRINT N'Đang nạp dữ liệu Toa tàu...';
INSERT INTO Toa (maToa, maTau, loaiToa, soGhe, viTriToa, trangThai) VALUES
-- Tàu 1
('TOA001', 'TAU001', N'Ngồi mềm', 64, N'1', 1),
('TOA002', 'TAU001', N'Ngồi cứng', 80, N'2', 1),
('TOA003', 'TAU001', N'Nằm thường', 48, N'3', 1),
('TOA004', 'TAU001', N'Nằm điều hòa', 28, N'4', 1),
('TOA005', 'TAU001', N'Nằm điều hòa', 28, N'5', 1),
-- Tàu 2
('TOA006', 'TAU002', N'Ngồi mềm', 64, N'1', 1),
('TOA007', 'TAU002', N'Ngồi cứng', 80, N'2', 1),
('TOA008', 'TAU002', N'Nằm thường', 48, N'3', 1),
('TOA009', 'TAU002', N'Nằm điều hòa', 28, N'4', 1),
('TOA010', 'TAU002', N'Nằm điều hòa', 28, N'5', 1),
-- Tàu 3
('TOA011', 'TAU003', N'Ngồi mềm', 64, N'1', 1),
('TOA012', 'TAU003', N'Ngồi cứng', 80, N'2', 1),
('TOA013', 'TAU003', N'Nằm thường', 48, N'3', 1),
('TOA014', 'TAU003', N'Nằm điều hòa', 28, N'4', 1),
('TOA015', 'TAU003', N'Nằm điều hòa', 28, N'5', 1),
-- Tàu 4
('TOA016', 'TAU004', N'Ngồi mềm', 64, N'1', 1),
('TOA017', 'TAU004', N'Ngồi cứng', 80, N'2', 1),
('TOA018', 'TAU004', N'Nằm thường', 48, N'3', 1),
('TOA019', 'TAU004', N'Nằm điều hòa', 28, N'4', 1),
('TOA020', 'TAU004', N'Nằm điều hòa', 28, N'5', 1),
-- Tàu 5
('TOA021', 'TAU005', N'Ngồi mềm', 64, N'1', 1),
('TOA022', 'TAU005', N'Ngồi cứng', 80, N'2', 1),
('TOA023', 'TAU005', N'Nằm thường', 48, N'3', 1),
('TOA024', 'TAU005', N'Nằm điều hòa', 28, N'4', 1),
('TOA025', 'TAU005', N'Nằm điều hòa', 28, N'5', 1),
-- Tàu 6
('TOA026', 'TAU006', N'Ngồi mềm', 64, N'1', 1),
('TOA027', 'TAU006', N'Ngồi cứng', 80, N'2', 1),
('TOA028', 'TAU006', N'Nằm thường', 48, N'3', 1),
('TOA029', 'TAU006', N'Nằm điều hòa', 28, N'4', 1),
('TOA030', 'TAU006', N'Nằm điều hòa', 28, N'5', 1),
-- Tàu 7
('TOA031', 'TAU007', N'Ngồi mềm', 64, N'1', 1),
('TOA032', 'TAU007', N'Ngồi cứng', 80, N'2', 1),
('TOA033', 'TAU007', N'Nằm thường', 48, N'3', 1),
('TOA034', 'TAU007', N'Nằm điều hòa', 28, N'4', 1),
('TOA035', 'TAU007', N'Nằm điều hòa', 28, N'5', 1),
-- Tàu 8
('TOA036', 'TAU008', N'Ngồi mềm', 64, N'1', 1),
('TOA037', 'TAU008', N'Ngồi cứng', 80, N'2', 1),
('TOA038', 'TAU008', N'Nằm thường', 48, N'3', 1),
('TOA039', 'TAU008', N'Nằm điều hòa', 28, N'4', 1),
('TOA040', 'TAU008', N'Nằm điều hòa', 28, N'5', 1),
-- Tàu 9
('TOA041', 'TAU009', N'Ngồi mềm', 64, N'1', 1),
('TOA042', 'TAU009', N'Ngồi cứng', 80, N'2', 1),
('TOA043', 'TAU009', N'Nằm thường', 48, N'3', 1),
('TOA044', 'TAU009', N'Nằm điều hòa', 28, N'4', 1),
('TOA045', 'TAU009', N'Nằm điều hòa', 28, N'5', 1),
-- Tàu 10
('TOA046', 'TAU010', N'Ngồi mềm', 64, N'1', 1),
('TOA047', 'TAU010', N'Ngồi cứng', 80, N'2', 1),
('TOA048', 'TAU010', N'Nằm thường', 48, N'3', 1),
('TOA049', 'TAU010', N'Nằm điều hòa', 28, N'4', 1),
('TOA050', 'TAU010', N'Nằm điều hòa', 28, N'5', 1),
-- Tàu 11
('TOA051', 'TAU011', N'Ngồi mềm', 64, N'1', 1),
('TOA052', 'TAU011', N'Ngồi cứng', 80, N'2', 1),
('TOA053', 'TAU011', N'Nằm thường', 48, N'3', 1),
('TOA054', 'TAU011', N'Nằm điều hòa', 28, N'4', 1),
('TOA055', 'TAU011', N'Nằm điều hòa', 28, N'5', 1),
-- Tàu 12
('TOA056', 'TAU012', N'Ngồi mềm', 64, N'1', 1),
('TOA057', 'TAU012', N'Ngồi cứng', 80, N'2', 1),
('TOA058', 'TAU012', N'Nằm thường', 48, N'3', 1),
('TOA059', 'TAU012', N'Nằm điều hòa', 28, N'4', 1),
('TOA060', 'TAU012', N'Nằm điều hòa', 28, N'5', 1);

-- Yêu cầu đặc biệt: "riêng toa thì luôn phải dư 10 toa có sẵn"
-- Ta chèn thêm 12 toa sẵn sàng hoạt động trong yard (maTau = NULL)
INSERT INTO Toa (maToa, maTau, loaiToa, soGhe, viTriToa, trangThai) VALUES
('TOA071', NULL, N'Ngồi mềm cao cấp', 64, NULL, 1),
('TOA072', NULL, N'Nằm khoang VIP', 28, NULL, 1),
('TOA073', NULL, N'Ngồi cứng phổ thông', 80, NULL, 1),
('TOA074', NULL, N'Nằm thường tự nhiên', 48, NULL, 1),
('TOA075', NULL, N'Ngồi mềm cao cấp', 60, NULL, 1),
('TOA076', NULL, N'Nằm khoang VIP', 32, NULL, 1),
('TOA077', NULL, N'Ngồi cứng phổ thông', 72, NULL, 1),
('TOA078', NULL, N'Nằm thường tự nhiên', 44, NULL, 1),
('TOA079', NULL, N'Ngồi mềm cao cấp', 56, NULL, 1),
('TOA080', NULL, N'Nằm khoang VIP', 36, NULL, 1),
('TOA081', NULL, N'Ngồi cứng phổ thông', 76, NULL, 1),
('TOA082', NULL, N'Nằm thường tự nhiên', 50, NULL, 1);
GO


-- 10. CHUYẾN TÀU (Mô phỏng 6 tháng trước & sau + Chi tiết 10 ngày)
PRINT N'Đang nạp dữ liệu Chuyến tàu...';

-- A. 6 Tháng trước (Doanh thu gánh cả tháng)
-- Trạng thái: 'DA_HOAN_THANH'
INSERT INTO ChuyenTau (maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai) VALUES
('CT_P01', 'TAU001', 'TT001', '2025-11-15', '06:00:00', 'DA_HOAN_THANH'),
('CT_P02', 'TAU002', 'TT002', '2025-12-15', '08:00:00', 'DA_HOAN_THANH'),
('CT_P03', 'TAU003', 'TT003', '2026-01-15', '12:00:00', 'DA_HOAN_THANH'),
('CT_P04', 'TAU004', 'TT004', '2026-02-15', '14:30:00', 'DA_HOAN_THANH'),
('CT_P05', 'TAU005', 'TT005', '2026-03-15', '19:00:00', 'DA_HOAN_THANH'),
('CT_P06', 'TAU006', 'TT006', '2026-04-15', '21:15:00', 'DA_HOAN_THANH');

-- B. 6 Tháng sau
-- Trạng thái: 'DA_LEN_LICH'
INSERT INTO ChuyenTau (maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai) VALUES
('CT_F01', 'TAU007', 'TT007', '2026-06-15', '06:15:00', 'DA_LEN_LICH'),
('CT_F02', 'TAU008', 'TT008', '2026-07-15', '08:45:00', 'DA_LEN_LICH'),
('CT_F03', 'TAU009', 'TT009', '2026-08-15', '11:00:00', 'DA_LEN_LICH'),
('CT_F04', 'TAU010', 'TT010', '2026-09-15', '15:20:00', 'DA_LEN_LICH'),
('CT_F05', 'TAU011', 'TT011', '2026-10-15', '18:00:00', 'DA_LEN_LICH'),
('CT_F06', 'TAU012', 'TT012', '2026-11-15', '22:10:00', 'DA_LEN_LICH');

-- C. Chi tiết trong 10 ngày trước/sau (2026-05-17 -> 2026-06-06)
-- Past trips
INSERT INTO ChuyenTau (maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai) VALUES
('CT_D01', 'TAU001', 'TT001', '2026-05-18', '05:30:00', 'DA_HOAN_THANH'),
('CT_D02', 'TAU002', 'TT002', '2026-05-19', '09:00:00', 'DA_HOAN_THANH'),
('CT_D03', 'TAU003', 'TT003', '2026-05-20', '13:15:00', 'DA_HOAN_THANH'),
('CT_D04', 'TAU004', 'TT004', '2026-05-22', '17:45:00', 'DA_HOAN_THANH'),
('CT_D05', 'TAU005', 'TT005', '2026-05-23', '06:00:00', 'DA_HOAN_THANH'),
('CT_D06', 'TAU006', 'TT006', '2026-05-24', '10:30:00', 'DA_HOAN_THANH'),
('CT_D07', 'TAU007', 'TT007', '2026-05-25', '14:00:00', 'DA_HOAN_THANH'),
('CT_D08', 'TAU008', 'TT008', '2026-05-26', '20:15:00', 'DA_HOAN_THANH');

-- Today
INSERT INTO ChuyenTau (maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai) VALUES
('CT_D09', 'TAU009', 'TT009', '2026-05-27', '07:30:00', 'DANG_CHAY'),
('CT_D10', 'TAU010', 'TT010', '2026-05-27', '18:00:00', 'DANG_CHAY');

-- Future trips
INSERT INTO ChuyenTau (maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai) VALUES
('CT_D11', 'TAU011', 'TT011', '2026-05-28', '06:00:00', 'DA_LEN_LICH'),
('CT_D12', 'TAU012', 'TT012', '2026-05-29', '09:30:00', 'DA_LEN_LICH'),
('CT_D13', 'TAU001', 'TT013', '2026-05-31', '14:20:00', 'DA_LEN_LICH'),
('CT_D14', 'TAU002', 'TT014', '2026-06-02', '16:00:00', 'DA_LEN_LICH'),
('CT_D15', 'TAU003', 'TT015', '2026-06-04', '20:45:00', 'DA_LEN_LICH'),
('CT_D16', 'TAU004', 'TT001', '2026-06-06', '22:00:00', 'DA_LEN_LICH');
GO


-- 10.1 CHI TIẾT CHUYẾN TÀU - TOA (Map các toa vào chuyến)
PRINT N'Đang lắp ráp toa vào chuyến tàu...';
INSERT INTO ChiTietChuyenTau_Toa (maCT, maToa, thuTuToa) VALUES
-- Past 6 months
('CT_P01', 'TOA001', 1), ('CT_P01', 'TOA002', 2), ('CT_P01', 'TOA003', 3), ('CT_P01', 'TOA004', 4), ('CT_P01', 'TOA005', 5),
('CT_P02', 'TOA006', 1), ('CT_P02', 'TOA007', 2), ('CT_P02', 'TOA008', 3), ('CT_P02', 'TOA009', 4), ('CT_P02', 'TOA010', 5),
('CT_P03', 'TOA011', 1), ('CT_P03', 'TOA012', 2), ('CT_P03', 'TOA013', 3), ('CT_P03', 'TOA014', 4), ('CT_P03', 'TOA015', 5),
('CT_P04', 'TOA016', 1), ('CT_P04', 'TOA017', 2), ('CT_P04', 'TOA018', 3), ('CT_P04', 'TOA019', 4), ('CT_P04', 'TOA020', 5),
('CT_P05', 'TOA021', 1), ('CT_P05', 'TOA022', 2), ('CT_P05', 'TOA023', 3), ('CT_P05', 'TOA024', 4), ('CT_P05', 'TOA025', 5),
('CT_P06', 'TOA026', 1), ('CT_P06', 'TOA027', 2), ('CT_P06', 'TOA028', 3), ('CT_P06', 'TOA029', 4), ('CT_P06', 'TOA030', 5),
-- Future 6 months
('CT_F01', 'TOA031', 1), ('CT_F01', 'TOA032', 2), ('CT_F01', 'TOA033', 3), ('CT_F01', 'TOA034', 4), ('CT_F01', 'TOA035', 5),
('CT_F02', 'TOA036', 1), ('CT_F02', 'TOA037', 2), ('CT_F02', 'TOA038', 3), ('CT_F02', 'TOA039', 4), ('CT_F02', 'TOA040', 5),
('CT_F03', 'TOA041', 1), ('CT_F03', 'TOA042', 2), ('CT_F03', 'TOA043', 3), ('CT_F03', 'TOA044', 4), ('CT_F03', 'TOA045', 5),
('CT_F04', 'TOA046', 1), ('CT_F04', 'TOA047', 2), ('CT_F04', 'TOA048', 3), ('CT_F04', 'TOA049', 4), ('CT_F04', 'TOA050', 5),
('CT_F05', 'TOA051', 1), ('CT_F05', 'TOA052', 2), ('CT_F05', 'TOA053', 3), ('CT_F05', 'TOA054', 4), ('CT_F05', 'TOA055', 5),
('CT_F06', 'TOA056', 1), ('CT_F06', 'TOA057', 2), ('CT_F06', 'TOA058', 3), ('CT_F06', 'TOA059', 4), ('CT_F06', 'TOA060', 5),
-- Detailed 10 days before/after
('CT_D01', 'TOA001', 1), ('CT_D01', 'TOA002', 2), ('CT_D01', 'TOA003', 3), ('CT_D01', 'TOA004', 4), ('CT_D01', 'TOA005', 5),
('CT_D02', 'TOA006', 1), ('CT_D02', 'TOA007', 2), ('CT_D02', 'TOA008', 3), ('CT_D02', 'TOA009', 4), ('CT_D02', 'TOA010', 5),
('CT_D03', 'TOA011', 1), ('CT_D03', 'TOA012', 2), ('CT_D03', 'TOA013', 3), ('CT_D03', 'TOA014', 4), ('CT_D03', 'TOA015', 5),
('CT_D04', 'TOA016', 1), ('CT_D04', 'TOA017', 2), ('CT_D04', 'TOA018', 3), ('CT_D04', 'TOA019', 4), ('CT_D04', 'TOA020', 5),
('CT_D05', 'TOA021', 1), ('CT_D05', 'TOA022', 2), ('CT_D05', 'TOA023', 3), ('CT_D05', 'TOA024', 4), ('CT_D05', 'TOA025', 5),
('CT_D06', 'TOA026', 1), ('CT_D06', 'TOA027', 2), ('CT_D06', 'TOA028', 3), ('CT_D06', 'TOA029', 4), ('CT_D06', 'TOA030', 5),
('CT_D07', 'TOA031', 1), ('CT_D07', 'TOA032', 2), ('CT_D07', 'TOA033', 3), ('CT_D07', 'TOA034', 4), ('CT_D07', 'TOA035', 5),
('CT_D08', 'TOA036', 1), ('CT_D08', 'TOA037', 2), ('CT_D08', 'TOA038', 3), ('CT_D08', 'TOA039', 4), ('CT_D08', 'TOA040', 5),
('CT_D09', 'TOA041', 1), ('CT_D09', 'TOA042', 2), ('CT_D09', 'TOA043', 3), ('CT_D09', 'TOA044', 4), ('CT_D09', 'TOA045', 5),
('CT_D10', 'TOA046', 1), ('CT_D10', 'TOA047', 2), ('CT_D10', 'TOA048', 3), ('CT_D10', 'TOA049', 4), ('CT_D10', 'TOA050', 5),
('CT_D11', 'TOA051', 1), ('CT_D11', 'TOA052', 2), ('CT_D11', 'TOA053', 3), ('CT_D11', 'TOA054', 4), ('CT_D11', 'TOA055', 5),
('CT_D12', 'TOA056', 1), ('CT_D12', 'TOA057', 2), ('CT_D12', 'TOA058', 3), ('CT_D12', 'TOA059', 4), ('CT_D12', 'TOA060', 5),
('CT_D13', 'TOA001', 1), ('CT_D13', 'TOA002', 2), ('CT_D13', 'TOA003', 3), ('CT_D13', 'TOA004', 4), ('CT_D13', 'TOA005', 5),
('CT_D14', 'TOA006', 1), ('CT_D14', 'TOA007', 2), ('CT_D14', 'TOA008', 3), ('CT_D14', 'TOA009', 4), ('CT_D14', 'TOA010', 5),
('CT_D15', 'TOA011', 1), ('CT_D15', 'TOA012', 2), ('CT_D15', 'TOA013', 3), ('CT_D15', 'TOA014', 4), ('CT_D15', 'TOA015', 5),
('CT_D16', 'TOA016', 1), ('CT_D16', 'TOA017', 2), ('CT_D16', 'TOA018', 3), ('CT_D16', 'TOA019', 4), ('CT_D16', 'TOA020', 5);
GO


-- 11. VÉ TÀU
PRINT N'Đang nạp dữ liệu Vé tàu...';
INSERT INTO VeTau (maVeTau, maKH, maCT, maToa, viTriGhe, loaiVe, giaVe, trangThai) VALUES
-- Doanh thu khủng gánh 6 tháng trước
('VE_P01', 'KH001', 'CT_P01', 'TOA004', 'A01', 'NGUOI_LON', 75000000.0, 'DA_SU_DUNG'),
('VE_P02', 'KH002', 'CT_P02', 'TOA009', 'A02', 'NGUOI_LON', 68000000.0, 'DA_SU_DUNG'),
('VE_P03', 'KH003', 'CT_P03', 'TOA014', 'A03', 'NGUOI_LON', 80000000.0, 'DA_SU_DUNG'),
('VE_P04', 'KH004', 'CT_P04', 'TOA019', 'A04', 'NGUOI_LON', 92000000.0, 'DA_SU_DUNG'),
('VE_P05', 'KH005', 'CT_P05', 'TOA024', 'A05', 'NGUOI_LON', 88000000.0, 'DA_SU_DUNG'),
('VE_P06', 'KH006', 'CT_P06', 'TOA029', 'A06', 'NGUOI_LON', 95000000.0, 'DA_SU_DUNG');

-- Doanh thu thanh toán trước cho 6 tháng sau
INSERT INTO VeTau (maVeTau, maKH, maCT, maToa, viTriGhe, loaiVe, giaVe, trangThai) VALUES
('VE_F01', 'KH007', 'CT_F01', 'TOA034', 'B01', 'NGUOI_LON', 60000000.0, 'DA_THANH_TOAN'),
('VE_F02', 'KH008', 'CT_F02', 'TOA039', 'B02', 'NGUOI_LON', 72000000.0, 'DA_THANH_TOAN'),
('VE_F03', 'KH009', 'CT_F03', 'TOA044', 'B03', 'NGUOI_LON', 85000000.0, 'DA_THANH_TOAN'),
('VE_F04', 'KH010', 'CT_F04', 'TOA049', 'B04', 'NGUOI_LON', 78000000.0, 'DA_THANH_TOAN'),
('VE_F05', 'KH011', 'CT_F05', 'TOA054', 'B05', 'NGUOI_LON', 90000000.0, 'DA_THANH_TOAN'),
('VE_F06', 'KH012', 'CT_F06', 'TOA059', 'B06', 'NGUOI_LON', 82000000.0, 'DA_THANH_TOAN');

-- Vé bán chi tiết trong 10 ngày trước/sau (25 vé thực tế)
INSERT INTO VeTau (maVeTau, maKH, maCT, maToa, viTriGhe, loaiVe, giaVe, trangThai) VALUES
('VE_D01', 'KH001', 'CT_D01', 'TOA001', '01A', 'NGUOI_LON', 850000.0, 'DA_SU_DUNG'),
('VE_D02', 'KH002', 'CT_D01', 'TOA004', '02F', 'NGUOI_LON', 1200000.0, 'DA_SU_DUNG'),
('VE_D03', 'KH003', 'CT_D02', 'TOA006', '12B', 'TRE_EM', 450000.0, 'DA_SU_DUNG'),
('VE_D04', 'KH004', 'CT_D02', 'TOA009', '03D', 'NGUOI_CAO_TUOI', 800000.0, 'DA_SU_DUNG'),
('VE_D05', 'KH005', 'CT_D03', 'TOA011', '08A', 'NGUOI_LON', 650000.0, 'DA_SU_DUNG'),
('VE_D06', 'KH006', 'CT_D03', 'TOA014', '05C', 'NGUOI_LON', 1100000.0, 'DA_SU_DUNG'),
('VE_D07', 'KH007', 'CT_D04', 'TOA016', '14C', 'NGUOI_LON', 900000.0, 'DA_SU_DUNG'),
('VE_D08', 'KH008', 'CT_D04', 'TOA019', '02E', 'TRE_EM', 550000.0, 'DA_SU_DUNG'),
('VE_D09', 'KH009', 'CT_D05', 'TOA021', '06B', 'NGUOI_LON', 750000.0, 'DA_SU_DUNG'),
('VE_D10', 'KH010', 'CT_D05', 'TOA024', '04F', 'NGUOI_LON', 1250000.0, 'DA_SU_DUNG'),
('VE_D11', 'KH011', 'CT_D06', 'TOA026', '10A', 'NGUOI_CAO_TUOI', 520000.0, 'DA_SU_DUNG'),
('VE_D12', 'KH012', 'CT_D06', 'TOA029', '02A', 'NGUOI_LON', 1150000.0, 'DA_SU_DUNG'),
('VE_D13', 'KH013', 'CT_D07', 'TOA031', '05B', 'NGUOI_LON', 300000.0, 'DA_SU_DUNG'),
('VE_D14', 'KH014', 'CT_D07', 'TOA034', '01A', 'NGUOI_LON', 500000.0, 'DA_SU_DUNG'),
('VE_D15', 'KH015', 'CT_D08', 'TOA036', '08C', 'TRE_EM', 350000.0, 'DA_SU_DUNG'),
('VE_D16', 'KH016', 'CT_D08', 'TOA039', '04E', 'NGUOI_LON', 750000.0, 'DA_SU_DUNG'),
-- Hôm nay (2026-05-27)
('VE_D17', 'KH017', 'CT_D09', 'TOA041', '02C', 'NGUOI_LON', 250000.0, 'DA_THANH_TOAN'),
('VE_D18', 'KH018', 'CT_D09', 'TOA044', '03F', 'NGUOI_LON', 420000.0, 'DA_THANH_TOAN'),
('VE_D19', 'KH019', 'CT_D10', 'TOA046', '09A', 'NGUOI_LON', 380000.0, 'DA_THANH_TOAN'),
('VE_D20', 'KH020', 'CT_D10', 'TOA049', '01B', 'NGUOI_LON', 650000.0, 'DA_THANH_TOAN'),
-- Ngày mai và tương lai đã thanh toán
('VE_D21', 'KH001', 'CT_D11', 'TOA051', '04A', 'NGUOI_LON', 950000.0, 'DA_THANH_TOAN'),
('VE_D22', 'KH002', 'CT_D12', 'TOA056', '06C', 'TRE_EM', 480000.0, 'DA_THANH_TOAN'),
('VE_D23', 'KH003', 'CT_D13', 'TOA001', '08A', 'NGUOI_LON', 860000.0, 'DA_THANH_TOAN'),
('VE_D24', 'KH004', 'CT_D14', 'TOA006', '03B', 'NGUOI_CAO_TUOI', 600000.0, 'DA_THANH_TOAN'),
-- Đang chờ thanh toán
('VE_D25', 'KH005', 'CT_D15', 'TOA011', '02E', 'NGUOI_LON', 500000.0, 'CHO_THANH_TOAN');
GO


-- 12. HÓA ĐƠN DOANH THU
PRINT N'Đang nạp dữ liệu Hóa đơn...';

-- Hóa đơn cho 6 tháng trước (Lịch sử)
INSERT INTO HoaDon (maHD, maNV, maKH, maKM, maThue, thoiGian, phuongThucThanhToan, ngayThanhToan, trangThaiThanhToan) VALUES
('HD_P01', 'NV001', 'KH001', NULL, 'T001', '2025-11-15', 'CHUYEN_KHOAN', '2025-11-15', 1),
('HD_P02', 'NV002', 'KH002', NULL, 'T001', '2025-12-15', 'CHUYEN_KHOAN', '2025-12-15', 1),
('HD_P03', 'NV004', 'KH003', NULL, 'T001', '2026-01-15', 'CHUYEN_KHOAN', '2026-01-15', 1),
('HD_P04', 'NV005', 'KH004', NULL, 'T001', '2026-02-15', 'CHUYEN_KHOAN', '2026-02-15', 1),
('HD_P05', 'NV006', 'KH005', NULL, 'T001', '2026-03-15', 'CHUYEN_KHOAN', '2026-03-15', 1),
('HD_P06', 'NV008', 'KH006', NULL, 'T001', '2026-04-15', 'CHUYEN_KHOAN', '2026-04-15', 1);

-- Hóa đơn cho 6 tháng sau
INSERT INTO HoaDon (maHD, maNV, maKH, maKM, maThue, thoiGian, phuongThucThanhToan, ngayThanhToan, trangThaiThanhToan) VALUES
('HD_F01', 'NV009', 'KH007', 'KM001', 'T001', '2026-06-15', 'CHUYEN_KHOAN', '2026-06-15', 1),
('HD_F02', 'NV011', 'KH008', 'KM001', 'T001', '2026-07-15', 'CHUYEN_KHOAN', '2026-07-15', 1),
('HD_F03', 'NV012', 'KH009', 'KM002', 'T001', '2026-08-15', 'CHUYEN_KHOAN', '2026-08-15', 1),
('HD_F04', 'NV001', 'KH010', 'KM002', 'T001', '2026-09-15', 'CHUYEN_KHOAN', '2026-09-15', 1),
('HD_F05', 'NV002', 'KH011', 'KM002', 'T001', '2026-10-15', 'CHUYEN_KHOAN', '2026-10-15', 1),
('HD_F06', 'NV004', 'KH012', 'KM002', 'T001', '2026-11-15', 'CHUYEN_KHOAN', '2026-11-15', 1);

-- Hóa đơn cho 10 ngày trước/sau ngày neo (2026-05-17 -> 2026-06-06)
INSERT INTO HoaDon (maHD, maNV, maKH, maKM, maThue, thoiGian, phuongThucThanhToan, ngayThanhToan, trangThaiThanhToan) VALUES
('HD_D01', 'NV001', 'KH001', NULL, 'T001', '2026-05-18', 'TIEN_MAT', '2026-05-18', 1),
('HD_D02', 'NV001', 'KH002', NULL, 'T001', '2026-05-18', 'CHUYEN_KHOAN', '2026-05-18', 1),
('HD_D03', 'NV002', 'KH003', 'KM002', 'T001', '2026-05-19', 'VI_DIEN_TU', '2026-05-19', 1),
('HD_D04', 'NV002', 'KH004', NULL, 'T001', '2026-05-19', 'TIEN_MAT', '2026-05-19', 1),
('HD_D05', 'NV004', 'KH005', NULL, 'T001', '2026-05-20', 'CHUYEN_KHOAN', '2026-05-20', 1),
('HD_D06', 'NV004', 'KH006', NULL, 'T001', '2026-05-20', 'TIEN_MAT', '2026-05-20', 1),
('HD_D07', 'NV005', 'KH007', NULL, 'T001', '2026-05-22', 'VI_DIEN_TU', '2026-05-22', 1),
('HD_D08', 'NV005', 'KH008', NULL, 'T001', '2026-05-22', 'CHUYEN_KHOAN', '2026-05-22', 1),
('HD_D09', 'NV006', 'KH009', 'KM012', 'T001', '2026-05-23', 'TIEN_MAT', '2026-05-23', 1),
('HD_D10', 'NV006', 'KH010', NULL, 'T001', '2026-05-23', 'CHUYEN_KHOAN', '2026-05-23', 1),
('HD_D11', 'NV008', 'KH011', NULL, 'T001', '2026-05-24', 'VI_DIEN_TU', '2026-05-24', 1),
('HD_D12', 'NV008', 'KH012', NULL, 'T001', '2026-05-24', 'CHUYEN_KHOAN', '2026-05-24', 1),
('HD_D13', 'NV009', 'KH013', NULL, 'T001', '2026-05-25', 'TIEN_MAT', '2026-05-25', 1),
('HD_D14', 'NV009', 'KH014', NULL, 'T001', '2026-05-25', 'CHUYEN_KHOAN', '2026-05-25', 1),
('HD_D15', 'NV011', 'KH015', NULL, 'T001', '2026-05-26', 'VI_DIEN_TU', '2026-05-26', 1),
('HD_D16', 'NV011', 'KH016', NULL, 'T001', '2026-05-26', 'CHUYEN_KHOAN', '2026-05-26', 1),
-- Hôm nay 2026-05-27
('HD_D17', 'NV012', 'KH017', NULL, 'T001', '2026-05-27', 'TIEN_MAT', '2026-05-27', 1),
('HD_D18', 'NV012', 'KH018', NULL, 'T001', '2026-05-27', 'CHUYEN_KHOAN', '2026-05-27', 1),
('HD_D19', 'NV001', 'KH019', NULL, 'T001', '2026-05-27', 'VI_DIEN_TU', '2026-05-27', 1),
('HD_D20', 'NV002', 'KH020', NULL, 'T001', '2026-05-27', 'CHUYEN_KHOAN', '2026-05-27', 1),
('HD_D21', 'NV004', 'KH001', NULL, 'T001', '2026-05-27', 'CHUYEN_KHOAN', '2026-05-27', 1),
('HD_D22', 'NV005', 'KH002', 'KM002', 'T001', '2026-05-27', 'VI_DIEN_TU', '2026-05-27', 1),
('HD_D23', 'NV006', 'KH003', NULL, 'T001', '2026-05-27', 'TIEN_MAT', '2026-05-27', 1),
('HD_D24', 'NV008', 'KH004', NULL, 'T001', '2026-05-27', 'CHUYEN_KHOAN', '2026-05-27', 1),
-- Hóa đơn chưa thanh toán
('HD_D25', 'NV009', 'KH005', NULL, 'T001', '2026-05-27', NULL, NULL, 0);
GO


-- 13. CHI TIẾT HÓA ĐƠN - VÉ
PRINT N'Đang liên kết Vé vào Hóa đơn...';
INSERT INTO ChiTietHoaDon_Ve (maHD, maVeTau, soLuong, donGia) VALUES
-- Past 6 months
('HD_P01', 'VE_P01', 1, 75000000.0),
('HD_P02', 'VE_P02', 1, 68000000.0),
('HD_P03', 'VE_P03', 1, 80000000.0),
('HD_P04', 'VE_P04', 1, 92000000.0),
('HD_P05', 'VE_P05', 1, 88000000.0),
('HD_P06', 'VE_P06', 1, 95000000.0),
-- Future 6 months
('HD_F01', 'VE_F01', 1, 60000000.0),
('HD_F02', 'VE_F02', 1, 72000000.0),
('HD_F03', 'VE_F03', 1, 85000000.0),
('HD_F04', 'VE_F04', 1, 78000000.0),
('HD_F05', 'VE_F05', 1, 90000000.0),
('HD_F06', 'VE_F06', 1, 82000000.0),
-- Detailed 10 days
('HD_D01', 'VE_D01', 1, 850000.0),
('HD_D02', 'VE_D02', 1, 1200000.0),
('HD_D03', 'VE_D03', 1, 450000.0),
('HD_D04', 'VE_D04', 1, 800000.0),
('HD_D05', 'VE_D05', 1, 650000.0),
('HD_D06', 'VE_D06', 1, 1100000.0),
('HD_D07', 'VE_D07', 1, 900000.0),
('HD_D08', 'VE_D08', 1, 550000.0),
('HD_D09', 'VE_D09', 1, 750000.0),
('HD_D10', 'VE_D10', 1, 1250000.0),
('HD_D11', 'VE_D11', 1, 520000.0),
('HD_D12', 'VE_D12', 1, 1150000.0),
('HD_D13', 'VE_D13', 1, 300000.0),
('HD_D14', 'VE_D14', 1, 500000.0),
('HD_D15', 'VE_D15', 1, 350000.0),
('HD_D16', 'VE_D16', 1, 750000.0),
('HD_D17', 'VE_D17', 1, 250000.0),
('HD_D18', 'VE_D18', 1, 420000.0),
('HD_D19', 'VE_D19', 1, 380000.0),
('HD_D20', 'VE_D20', 1, 650000.0),
('HD_D21', 'VE_D21', 1, 950000.0),
('HD_D22', 'VE_D22', 1, 480000.0),
('HD_D23', 'VE_D23', 1, 860000.0),
('HD_D24', 'VE_D24', 1, 600000.0),
('HD_D25', 'VE_D25', 1, 500000.0);
GO


-- 14. CHI TIẾT HÓA ĐƠN - DỊCH VỤ
PRINT N'Đang liên kết Dịch vụ vào Hóa đơn lẻ...';
INSERT INTO ChiTietHoaDon_DichVu (maHD, maDV, soLuong, donGia) VALUES
('HD_D01', 'DV001', 2, 50000.0),
('HD_D01', 'DV003', 2, 15000.0),
('HD_D02', 'DV002', 1, 30000.0),
('HD_D03', 'DV001', 1, 50000.0),
('HD_D05', 'DV004', 2, 25000.0),
('HD_D05', 'DV008', 3, 20000.0),
('HD_D06', 'DV010', 1, 40000.0),
('HD_D08', 'DV001', 1, 50000.0),
('HD_D09', 'DV011', 1, 100000.0),
('HD_D10', 'DV002', 2, 30000.0),
('HD_D12', 'DV001', 2, 50000.0),
('HD_D12', 'DV009', 2, 22000.0),
('HD_D15', 'DV006', 1, 60000.0),
('HD_D17', 'DV001', 1, 50000.0),
('HD_D18', 'DV012', 1, 80000.0),
('HD_D20', 'DV003', 1, 15000.0),
('HD_D21', 'DV011', 2, 100000.0),
('HD_D22', 'DV001', 1, 45000.0);
GO


-- 15. PHIẾU ĐẶT VÉ (Giữ chỗ)
PRINT N'Đang tạo dữ liệu giữ chỗ...';
INSERT INTO PhieuDatVe (maPhieu, maKH, maNV, ngayDat, hanThanhToan, trangThai) VALUES
('PDV001', 'KH001', 'NV001', '2026-05-18', '2026-05-19', 0),
('PDV002', 'KH002', 'NV001', '2026-05-19', '2026-05-20', 0),
('PDV003', 'KH003', 'NV002', '2026-05-20', '2026-05-21', 1),
('PDV004', 'KH004', 'NV002', '2026-05-22', '2026-05-23', 0),
('PDV005', 'KH005', 'NV004', '2026-05-23', '2026-05-24', 1),
('PDV006', 'KH006', 'NV004', '2026-05-24', '2026-05-25', 0),
('PDV007', 'KH007', 'NV005', '2026-05-25', '2026-05-26', 1),
('PDV008', 'KH008', 'NV005', '2026-05-26', '2026-05-27', 1),
('PDV009', 'KH009', 'NV006', '2026-05-27', '2026-05-28', 1),
('PDV010', 'KH010', 'NV006', '2026-05-27', '2026-05-28', 1),
('PDV011', 'KH011', 'NV008', '2026-05-27', '2026-05-28', 1),
('PDV012', 'KH012', 'NV008', '2026-05-27', '2026-05-28', 1);

-- 16. CHI TIẾT PHIẾU ĐẶT
INSERT INTO ChiTietPhieuDat (maPhieu, maCT, maToa, viTriGhe, giaVe, ghiChu) VALUES
('PDV001', 'CT_D01', 'TOA001', '03B', 850000.0, N'Gần lối đi'),
('PDV002', 'CT_D01', 'TOA002', '12F', 950000.0, N'Gần cửa sổ'),
('PDV003', 'CT_D02', 'TOA006', '12B', 450000.0, N'Vé trẻ em'),
('PDV004', 'CT_D02', 'TOA007', '05C', 800000.0, N'Không lấy suất ăn'),
('PDV005', 'CT_D03', 'TOA011', '08A', 650000.0, N'Đã thanh toán bằng vé VE_D05'),
('PDV006', 'CT_D03', 'TOA012', '06F', 700000.0, N'Ưu tiên tầng thấp'),
('PDV007', 'CT_D04', 'TOA016', '14C', 900000.0, N'Đã đổi thành vé VE_D07'),
('PDV008', 'CT_D05', 'TOA021', '06B', 750000.0, N'Hành khách cao tuổi'),
('PDV009', 'CT_D09', 'TOA041', '10C', 250000.0, N'Khách quen đặt giữ chỗ trước'),
('PDV010', 'CT_D10', 'TOA046', '12A', 380000.0, N'Đặt cho đoàn khách du lịch'),
('PDV011', 'CT_D11', 'TOA051', '04A', 950000.0, N'Gửi hành lý quá khổ'),
('PDV012', 'CT_D12', 'TOA056', '06C', 480000.0, N'Đang chờ thanh toán chuyển khoản');
GO

-- ============================================================================
-- PHẦN IV: KIỂM TRA TỔNG QUAN HỆ THỐNG SAU KHI KHỞI TẠO MỚI
-- ============================================================================
PRINT N'======================================================';
PRINT N' KẾT QUẢ KHỞI TẠO VÀ GIẢ LẬP CƠ SỞ DỮ LIỆU THÀNH CÔNG!';
PRINT N'======================================================';
SELECT N'Thuế' AS [Bảng], COUNT(*) AS [Số bản ghi] FROM Thue UNION ALL
SELECT N'Tài Khoản', COUNT(*) FROM TaiKhoan UNION ALL
SELECT N'Nhân Viên', COUNT(*) FROM NhanVien UNION ALL
SELECT N'Khách Hàng', COUNT(*) FROM KhachHang UNION ALL
SELECT N'Dịch Vụ', COUNT(*) FROM DichVu UNION ALL
SELECT N'Khuyến Mãi', COUNT(*) FROM KhuyenMai UNION ALL
SELECT N'Tuyến Tàu', COUNT(*) FROM TuyenTau UNION ALL
SELECT N'Đoàn Tàu', COUNT(*) FROM Tau UNION ALL
SELECT N'Toa Tàu', COUNT(*) FROM Toa UNION ALL
SELECT N'Chuyến Tàu', COUNT(*) FROM ChuyenTau UNION ALL
SELECT N'Vé Tàu', COUNT(*) FROM VeTau UNION ALL
SELECT N'Hóa Đơn', COUNT(*) FROM HoaDon UNION ALL
SELECT N'Phiếu Đặt Vé', COUNT(*) FROM PhieuDatVe;

SELECT COUNT(*) AS [Số toa tàu sẵn có trong kho (maTau IS NULL)] FROM Toa WHERE maTau IS NULL;
PRINT N'Cơ sở dữ liệu QLBANVETAU mới đã hoàn thành thiết lập hoàn hảo!';
GO
