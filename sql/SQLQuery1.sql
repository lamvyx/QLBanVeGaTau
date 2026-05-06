-- ============================================
-- HỆ THỐNG QUẢN LÝ VÉ TÀU (NỘI BỘ NHÂN VIÊN)
-- SQL Server Version
-- ============================================
CREATE DATABASE QLBANVETAU
USE QLBANVETAU;
GO

-- ============================================
-- 1. THUE
-- ============================================
CREATE TABLE Thue (
    maThue      VARCHAR(10)     NOT NULL,
    tenThue     NVARCHAR(100)   NOT NULL,
    phamTram    FLOAT           NOT NULL,
    moTa        NVARCHAR(255)   NULL,
    CONSTRAINT PK_Thue PRIMARY KEY (maThue),
    CONSTRAINT CHK_Thue_PhamTram CHECK (phamTram >= 0 AND phamTram <= 100)
);
GO

-- ============================================
-- 2. TUYEN_TAU
-- ============================================
CREATE TABLE TuyenTau (
    maTT        VARCHAR(10)     NOT NULL,
    maGaDi      NVARCHAR(100)   NOT NULL,
    maGaDen     NVARCHAR(100)   NOT NULL,
    khoangCach  INT             NOT NULL,
    CONSTRAINT PK_TuyenTau PRIMARY KEY (maTT),
    CONSTRAINT CHK_TuyenTau_KhoangCach CHECK (khoangCach > 0)
);
GO

-- ============================================
-- 3. TAU
-- ============================================
CREATE TABLE Tau (
    maTau       VARCHAR(10)     NOT NULL,
    tenTau      NVARCHAR(100)   NOT NULL,
    soLuongToa  INT             NOT NULL,
    CONSTRAINT PK_Tau PRIMARY KEY (maTau),
    CONSTRAINT CHK_Tau_SoLuongToa CHECK (soLuongToa > 0)
);
GO

-- ============================================
-- 4. TOA
-- ============================================
CREATE TABLE Toa (
    maToa       VARCHAR(10)     NOT NULL,
    maTau       VARCHAR(10)     NOT NULL,
    loaiToa     NVARCHAR(50)    NOT NULL,
    soGhe       INT             NOT NULL,
    viTriToa    NVARCHAR(10)    NULL,
    trangThai   BIT             NOT NULL DEFAULT 1,
    CONSTRAINT PK_Toa PRIMARY KEY (maToa),
    CONSTRAINT FK_Toa_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau),
    CONSTRAINT CHK_Toa_SoGhe CHECK (soGhe > 0)
);
GO

-- ============================================
-- 5. CHUYEN_TAU
-- ============================================
CREATE TABLE ChuyenTau (
    maCT            VARCHAR(10)     NOT NULL,
    maTau           VARCHAR(10)     NOT NULL,
    maTuyenTau      VARCHAR(10)     NOT NULL,
    ngayKhoiHanh    DATE            NOT NULL,
    gioKhoiHanh     TIME            NOT NULL,
    trangThai       BIT             NOT NULL DEFAULT 1,
    CONSTRAINT PK_ChuyenTau PRIMARY KEY (maCT),
    CONSTRAINT FK_ChuyenTau_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau),
    CONSTRAINT FK_ChuyenTau_TuyenTau FOREIGN KEY (maTuyenTau) REFERENCES TuyenTau(maTT)
);
GO

-- ============================================
-- 6. DICH_VU
-- ============================================
CREATE TABLE DichVu (
    maDV        VARCHAR(10)     NOT NULL,
    tenDV       NVARCHAR(100)   NOT NULL,
    trangThai   BIT             NOT NULL DEFAULT 1,
    giaTien     FLOAT           NOT NULL,
    CONSTRAINT PK_DichVu PRIMARY KEY (maDV),
    CONSTRAINT CHK_DichVu_GiaTien CHECK (giaTien >= 0)
);
GO

-- ============================================
-- 7. TAI_KHOAN (chỉ dành cho nhân viên)
-- ============================================
CREATE TABLE TaiKhoan (
    username    VARCHAR(50)     NOT NULL,
    password    NVARCHAR(255)   NOT NULL,
    vaiTro      VARCHAR(20)     NOT NULL,
    CONSTRAINT PK_TaiKhoan PRIMARY KEY (username),
    CONSTRAINT CHK_TaiKhoan_VaiTro CHECK (vaiTro IN ('ADMIN', 'NHAN_VIEN'))
);
GO

-- ============================================
-- 8. NHAN_VIEN
-- ============================================
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
GO

-- ============================================
-- 9. KHACH_HANG (do nhân viên tạo, không có tài khoản)
-- ============================================
CREATE TABLE KhachHang (
    maKH        VARCHAR(10)     NOT NULL,
    tenKH       NVARCHAR(100)   NOT NULL,
    sdt         VARCHAR(15)     NULL,
    CCCD        VARCHAR(20)     NULL,
    diaChi      NVARCHAR(255)   NULL,
    email       NVARCHAR(100)   NULL,
    gioiTinh    BIT             NULL,
    ngaySinh    DATE            NULL,
    loaiKH      BIT             NOT NULL DEFAULT 0,
    CONSTRAINT PK_KhachHang PRIMARY KEY (maKH),
    CONSTRAINT UQ_KhachHang_CCCD UNIQUE (CCCD)
);
GO

-- ============================================
-- 10. KHUYEN_MAI
-- ============================================
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
GO

-- ============================================
-- 11. VE_TAU
-- ============================================
CREATE TABLE VeTau (
    maVeTau     VARCHAR(10)     NOT NULL,
    maKH        VARCHAR(10)     NOT NULL,
    maCT        VARCHAR(10)     NOT NULL,
    maToa       VARCHAR(10)     NOT NULL,
    giaVe       FLOAT           NOT NULL,
    trangThai   VARCHAR(20)     NOT NULL DEFAULT 'CHO_THANH_TOAN',
    CONSTRAINT PK_VeTau PRIMARY KEY (maVeTau),
    CONSTRAINT FK_VeTau_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_VeTau_ChuyenTau FOREIGN KEY (maCT) REFERENCES ChuyenTau(maCT),
    CONSTRAINT FK_VeTau_Toa FOREIGN KEY (maToa) REFERENCES Toa(maToa),
    CONSTRAINT CHK_VeTau_TrangThai CHECK (trangThai IN (
        'CHO_THANH_TOAN', 'DA_THANH_TOAN', 'DA_HOAN', 'DA_SU_DUNG'
    )),
    CONSTRAINT CHK_VeTau_GiaVe CHECK (giaVe >= 0)
);
GO

-- ============================================
-- 12. CHI_TIET_VE_TAU
-- ============================================
CREATE TABLE ChiTietVeTau (
    maChiTiet       VARCHAR(10)     NOT NULL,
    maVeTau         VARCHAR(10)     NOT NULL,
    tenHanhKhach    NVARCHAR(100)   NOT NULL,
    CCCD            VARCHAR(20)     NOT NULL,
    ngaySinh        DATE            NOT NULL,
    viTriGhe        VARCHAR(10)     NOT NULL,
    loaiVe          VARCHAR(20)     NOT NULL,
    giaVeTheoLoai   FLOAT           NOT NULL,
    CONSTRAINT PK_ChiTietVeTau PRIMARY KEY (maChiTiet),
    CONSTRAINT FK_ChiTietVeTau_VeTau FOREIGN KEY (maVeTau) REFERENCES VeTau(maVeTau),
    CONSTRAINT CHK_ChiTietVeTau_LoaiVe CHECK (loaiVe IN (
        'NGUOI_LON', 'TRE_EM', 'NGUOI_CAO_TUOI'
    )),
    CONSTRAINT CHK_ChiTietVeTau_Gia CHECK (giaVeTheoLoai >= 0)
);
GO

-- ============================================
-- 13. HOA_DON
-- ============================================
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
GO

-- ============================================
-- 14. CHI_TIET_HOA_DON
-- ============================================
CREATE TABLE ChiTietHoaDon (
    maCTHD      VARCHAR(10)     NOT NULL,
    maHD        VARCHAR(10)     NOT NULL,
    maVeTau     VARCHAR(10)     NULL,
    maDV        VARCHAR(10)     NULL,
    soLuong     INT             NOT NULL DEFAULT 1,
    donGia      FLOAT           NOT NULL,
    CONSTRAINT PK_ChiTietHoaDon PRIMARY KEY (maCTHD),
    CONSTRAINT FK_ChiTietHoaDon_HoaDon FOREIGN KEY (maHD)    REFERENCES HoaDon(maHD),
    CONSTRAINT FK_ChiTietHoaDon_VeTau  FOREIGN KEY (maVeTau) REFERENCES VeTau(maVeTau),
    CONSTRAINT FK_ChiTietHoaDon_DichVu FOREIGN KEY (maDV)    REFERENCES DichVu(maDV),
    CONSTRAINT CHK_ChiTietHoaDon_SoLuong CHECK (soLuong > 0),
    CONSTRAINT CHK_ChiTietHoaDon_DonGia CHECK (donGia >= 0),
    CONSTRAINT CHK_ChiTietHoaDon_LoaiDong CHECK (
        (maVeTau IS NOT NULL AND maDV IS NULL) OR
        (maVeTau IS NULL AND maDV IS NOT NULL)
    )
);
GO

-- ============================================
-- VIEW
-- ============================================

-- Tính thành tiền chi tiết hóa đơn (có thuế)
CREATE VIEW v_ChiTietHoaDon AS
SELECT
    ct.maCTHD,
    ct.maHD,
    ct.maVeTau,
    ct.maDV,
    ct.soLuong,
    ct.donGia,
    (ct.soLuong * ct.donGia)                                                AS thanhTien,
    (ct.soLuong * ct.donGia * (1 + ISNULL(t.phamTram, 0) / 100.0))        AS thanhTienSauThue
FROM ChiTietHoaDon ct
JOIN  HoaDon hd  ON ct.maHD   = hd.maHD
LEFT JOIN Thue t ON hd.maThue = t.maThue;
GO

-- Tổng tiền hóa đơn (sau thuế + khuyến mãi)
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
JOIN  ChiTietHoaDon ct ON hd.maHD   = ct.maHD
LEFT JOIN Thue t        ON hd.maThue = t.maThue
LEFT JOIN KhuyenMai km  ON hd.maKM   = km.maKM
GROUP BY hd.maHD, hd.maNV, hd.maKH, km.tyLeKM;
GO

-- Số chỗ còn trống theo toa trong từng chuyến
CREATE VIEW v_SoChoTrong AS
SELECT
    ct.maCT,
    toa.maToa,
    toa.loaiToa,
    toa.soGhe,
    COUNT(v.maVeTau)                    AS soVeDaBan,
    (toa.soGhe - COUNT(v.maVeTau))      AS soChoCon
FROM ChuyenTau ct
JOIN  Tau tau  ON ct.maTau   = tau.maTau
JOIN  Toa toa  ON toa.maTau  = tau.maTau
LEFT JOIN VeTau v ON v.maCT  = ct.maCT
    AND v.maToa = toa.maToa
    AND v.trangThai != 'DA_HOAN'
GROUP BY ct.maCT, toa.maToa, toa.loaiToa, toa.soGhe;
GO

-- Doanh thu theo nhân viên
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

-- ============================================
-- DỮ LIỆU MẪU
-- ============================================

INSERT INTO Thue VALUES ('T001', N'VAT', 10, N'Thuế giá trị gia tăng');
GO

INSERT INTO TuyenTau VALUES
    ('TT001', N'Hồ Chí Minh', N'Hà Nội',  1726),
    ('TT002', N'Hồ Chí Minh', N'Đà Nẵng', 935);
GO

INSERT INTO Tau VALUES
    ('TAU001', N'SE1', 14),
    ('TAU002', N'SE3', 14);
GO

INSERT INTO Toa VALUES
    ('TOA001', 'TAU001', N'Ngồi mềm',     64, N'1', 1),
    ('TOA002', 'TAU001', N'Nằm điều hòa', 28, N'2', 1),
    ('TOA003', 'TAU002', N'Ngồi mềm',     64, N'1', 1);
GO

INSERT INTO ChuyenTau VALUES
    ('CT001', 'TAU001', 'TT001', '2026-04-10', '22:00:00', 1),
    ('CT002', 'TAU002', 'TT002', '2026-04-11', '06:00:00', 1);
GO

INSERT INTO DichVu VALUES
    ('DV001', N'Suất ăn trên tàu', 1, 50000),
    ('DV002', N'Hành lý thêm',     1, 30000);
GO

INSERT INTO TaiKhoan VALUES
    ('admin01', 'hashed_pw_admin', 'ADMIN'),
    ('nv001',   'hashed_pw_nv001', 'NHAN_VIEN'),
    ('nv002',   'hashed_pw_nv002', 'NHAN_VIEN');
GO

INSERT INTO NhanVien VALUES
    ('NV001', N'Nguyễn Văn An', '0901234567', 1, '1990-05-15', '2020-01-01', 1, 'nva@tau.vn', N'Nhân viên bán vé',  'nv001'),
    ('NV002', N'Trần Thị Bích', '0912345678', 0, '1995-03-20', '2021-06-01', 1, 'ttb@tau.vn', N'Nhân viên bán vé',  'nv002'),
    ('NV003', N'Lê Văn Cường',  '0923456789', 1, '1988-11-10', '2018-03-15', 1, 'lvc@tau.vn', N'Quản trị hệ thống', 'admin01');
GO

INSERT INTO KhachHang VALUES
    ('KH001', N'Phạm Văn Dũng', '0934567890', '012345678901', N'Hà Nội', 'pvd@gmail.com', 1, '1992-07-25', 0),
    ('KH002', N'Ngô Thị Lan',   '0945678901', '012345678902', N'TP.HCM', 'ntl@gmail.com', 0, '1998-02-14', 1);
GO

INSERT INTO KhuyenMai VALUES
    ('KM001', N'Giảm 10% mùa hè',  10, '2026-06-01', '2026-08-31', N'Áp dụng cho vé nằm'),
    ('KM002', N'Ưu đãi thành viên',  5, '2026-01-01', '2026-12-31', N'Áp dụng cho khách thành viên');
GO

INSERT INTO VeTau VALUES
    ('VE001', 'KH001', 'CT001', 'TOA002', 850000, 'DA_THANH_TOAN'),
    ('VE002', 'KH002', 'CT001', 'TOA001', 900000, 'CHO_THANH_TOAN');
GO

-- Chi tiết từng hành khách trên vé
INSERT INTO ChiTietVeTau VALUES
    ('CTVE001', 'VE001', N'Phạm Văn Dũng', '012345678901', '1992-07-25', '5A',  'NGUOI_LON',      850000),
    ('CTVE002', 'VE002', N'Ngô Thị Lan',   '012345678902', '1998-02-14', '12B', 'NGUOI_LON',      450000),
    ('CTVE003', 'VE002', N'Ngô Văn Minh',  '012345678903', '2018-05-10', '12C', 'TRE_EM',         225000),
    ('CTVE004', 'VE002', N'Ngô Thị Hoa',   '012345678904', '1955-03-20', '12D', 'NGUOI_CAO_TUOI', 225000);
GO

INSERT INTO HoaDon VALUES
    ('HD001', 'NV001', 'KH001', NULL,   'T001', '2026-04-09', 'CHUYEN_KHOAN', '2026-04-09', 1),
    ('HD002', 'NV002', 'KH002', 'KM002','T001', '2026-04-09', 'TIEN_MAT',      NULL,         0);
GO

INSERT INTO ChiTietHoaDon VALUES
    ('CTHD001', 'HD001', 'VE001', NULL,    1, 850000),
    ('CTHD002', 'HD002', 'VE002', NULL,    1, 900000),
    ('CTHD003', 'HD001', NULL,    'DV001', 2, 50000);
GO

PRINT N'Tạo CSDL QuanLyVeTau thành công!';
GO
