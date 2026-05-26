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
GO

-- (ChiTietVeTau removed per class diagram: each `VeTau` now contains passenger details)

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
-- Chi tiết hóa đơn split into Vé và Dịch vụ so each PK can be composite FKs
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
GO

-- ============================================
-- 15. PHIEU_DAT_VE (Reservation)
-- ============================================
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
GO

-- ============================================
-- 16. CHI_TIET_PHIEU_DAT (Reservation details)
-- ============================================
CREATE TABLE ChiTietPhieuDat (
    maPhieu     VARCHAR(10)     NOT NULL,
    maCT        VARCHAR(10)     NOT NULL, -- ChuyenTau
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

-- ============================================
-- SAMPLE DATA: reservations
-- ============================================
INSERT INTO PhieuDatVe VALUES ('PDV001', 'KH002', 'NV002', '2026-04-08', '2026-04-09', 1);
GO

INSERT INTO ChiTietPhieuDat (maPhieu, maCT, maToa, viTriGhe, giaVe, ghiChu) VALUES ('PDV001', 'CT001', 'TOA001', N'12B', 900000, N'Yêu cầu ghế gần cửa sổ');
GO

-- ============================================
-- VIEW
-- ============================================

-- Tính thành tiền chi tiết hóa đơn (có thuế)
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
JOIN  v_ChiTietHoaDon ct ON hd.maHD   = ct.maHD
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
        COUNT(DISTINCT v.maVeTau)           AS soVeDaBan,
        COUNT(DISTINCT v.maVeTau)
            + COUNT(DISTINCT CASE WHEN pd.trangThai = 1 THEN ctpd.maPhieu + N'|' + ctpd.viTriGhe END) AS soVeDaBanVaGiu,
        (toa.soGhe - (
                COUNT(DISTINCT v.maVeTau)
            + COUNT(DISTINCT CASE WHEN pd.trangThai = 1 THEN ctpd.maPhieu + N'|' + ctpd.viTriGhe END)
        )) AS soChoCon
FROM ChuyenTau ct
JOIN  Tau tau  ON ct.maTau   = tau.maTau
JOIN  Toa toa  ON toa.maTau  = tau.maTau
LEFT JOIN VeTau v ON v.maCT  = ct.maCT
    AND v.maToa = toa.maToa
    AND v.trangThai != 'DA_HOAN'
LEFT JOIN ChiTietPhieuDat ctpd ON ctpd.maCT = ct.maCT AND ctpd.maToa = toa.maToa
LEFT JOIN PhieuDatVe pd ON pd.maPhieu = ctpd.maPhieu
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
    ('TT002', N'Hồ Chí Minh', N'Đà Nẵng', 935),
    ('TT003', N'Hồ Chí Minh', N'Nha Trang', 411),
    ('TT004', N'Hồ Chí Minh', N'Vinh', 939),

    ('TT005', N'Nha Trang', N'Đà Nẵng', 528),
    ('TT006', N'Nha Trang', N'Hà Nội', 1315),

    ('TT007', N'Đà Nẵng', N'Huế', 103),
    ('TT008', N'Đà Nẵng', N'Hà Nội', 791),

    ('TT009', N'Hà Nội', N'Hải Phòng', 102),
    ('TT010', N'Hà Nội', N'Vinh', 319),
	('TT011', N'Hà Nội', N'Huế', 979),

    ('TT012', N'Vinh', N'Hồ Chí Minh', 1407),

    ('TT013', N'Huế', N'Hồ Chí Minh', 978),

    ('TT014', N'Hải Phòng', N'Hà Nội', 102),

    ('TT015', N'Đà Nẵng', N'Nha Trang', 528);
GO

INSERT INTO Tau VALUES
	('TAU001', N'SE1', 14),
    ('TAU002', N'SE3', 14),
    ('TAU003', N'SE5',  15),
    ('TAU004', N'SE7',  16),
    ('TAU005', N'TN1',  12);
GO

INSERT INTO Toa VALUES
    ('TOA001', 'TAU001', N'Ngồi mềm',     64, N'1', 1),
    ('TOA002', 'TAU001', N'Nằm điều hòa', 28, N'2', 1),
    ('TOA003', 'TAU002', N'Ngồi mềm',     64, N'1', 1);
GO

INSERT INTO Toa VALUES
    ('TOA004', 'TAU001', N'Nằm điều hòa', 28, N'4', 1),
    ('TOA005', 'TAU001', N'Ngồi cứng',    80, N'5', 1),
    ('TOA006', 'TAU001', N'Nằm điều hòa', 32, N'6', 1),
    ('TOA007', 'TAU001', N'Ngồi mềm',     56, N'7', 1),
    ('TOA008', 'TAU001', N'Nằm thường',   48, N'8', 1),
    ('TOA009', 'TAU001', N'Ngồi cứng',    72, N'9', 1),
    ('TOA010', 'TAU001', N'Nằm điều hòa', 36, N'10', 1),
    ('TOA011', 'TAU001', N'Ngồi mềm',     60, N'11', 1),
    ('TOA012', 'TAU001', N'Ngồi cứng',     28, N'12', 1),
    ('TOA013', 'TAU001', N'Nằm thường',   52, N'13', 1),
    ('TOA014', 'TAU001', N'Ngồi cứng',    68, N'14', 1),
	('TOA015', 'TAU001', N'Ngồi mềm',   64, N'1', 1),

    ('TOA016', 'TAU002', N'Nằm điều hòa',28, N'2', 1),
    ('TOA017', 'TAU002', N'Ngồi mềm',   60, N'3', 1),
    ('TOA018', 'TAU002', N'Nằm điều hòa',32, N'4', 1),
    ('TOA019', 'TAU002', N'Ngồi cứng',  80, N'5', 1),
    ('TOA020', 'TAU002', N'Nằm thường', 48, N'6', 1),
    ('TOA021', 'TAU002', N'Ngồi mềm',   56, N'7', 1),
    ('TOA022', 'TAU002', N'Nằm điều hòa',36, N'8', 1),
    ('TOA023', 'TAU002', N'Ngồi cứng',  72, N'9', 1),
    ('TOA024', 'TAU002', N'Nằm thường', 44, N'10', 1),
    ('TOA025', 'TAU002', N'Ngồi mềm',   64, N'11', 1),
    ('TOA026', 'TAU002', N'Nằm điều hòa',30, N'12', 1),
    ('TOA027', 'TAU002', N'Ngồi cứng',  76, N'13', 1),
    ('TOA028', 'TAU002', N'Ngồi cứng',   28, N'14', 1),

	('TOA029', 'TAU003', N'Ngồi mềm',   64, N'1', 1),
    ('TOA030', 'TAU003', N'Nằm điều hòa',30, N'2', 1),
    ('TOA031', 'TAU003', N'Ngồi cứng',  80, N'3', 1),
    ('TOA032', 'TAU003', N'Nằm thường', 48, N'4', 1),
    ('TOA033', 'TAU003', N'Ngồi mềm',   60, N'5', 1),
    ('TOA034', 'TAU003', N'Nằm điều hòa',34, N'6', 1),
    ('TOA035', 'TAU003', N'Ngồi cứng',  72, N'7', 1),
    ('TOA036', 'TAU003', N'Nằm thường', 52, N'8', 1),
    ('TOA037', 'TAU003', N'Ngồi mềm',   56, N'9', 1),
    ('TOA038', 'TAU003', N'Nằm điều hòa',28, N'10', 1),
    ('TOA039', 'TAU003', N'Ngồi cứng',  68, N'11', 1),
    ('TOA040', 'TAU003', N'Ngồi cứng',  28, N'12', 1),
    ('TOA041', 'TAU003', N'Nằm thường', 46, N'13', 1),
    ('TOA042', 'TAU003', N'Ngồi mềm',   64, N'14', 1),
    ('TOA043', 'TAU003', N'Nằm điều hòa',32, N'15', 1),

	('TOA044', 'TAU004', N'Ngồi mềm',   60, N'1', 1),
    ('TOA045', 'TAU004', N'Nằm điều hòa',28, N'2', 1),
    ('TOA046', 'TAU004', N'Ngồi cứng',  76, N'3', 1),
    ('TOA047', 'TAU004', N'Nằm thường', 50, N'4', 1),
    ('TOA048', 'TAU004', N'Ngồi mềm',   64, N'5', 1),
    ('TOA049', 'TAU004', N'Nằm điều hòa',32, N'6', 1),
    ('TOA050', 'TAU004', N'Ngồi cứng',  80, N'7', 1),
    ('TOA051', 'TAU004', N'Nằm thường', 44, N'8', 1),
    ('TOA052', 'TAU004', N'Ngồi mềm',   56, N'9', 1),
    ('TOA053', 'TAU004', N'Nằm điều hòa',36, N'10', 1),
    ('TOA054', 'TAU004', N'Ngồi cứng',  72, N'11', 1),
    ('TOA055', 'TAU004', N'Ngồi cứng',   28, N'12', 1),
    ('TOA056', 'TAU004', N'Nằm thường', 52, N'13', 1),
    ('TOA057', 'TAU004', N'Ngồi mềm',   60, N'14', 1),
    ('TOA058', 'TAU004', N'Nằm điều hòa',30, N'15', 1),
    ('TOA059', 'TAU004', N'Ngồi cứng',  68, N'16', 1),

	('TOA060', 'TAU005', N'Ngồi mềm',   64, N'1', 1),
    ('TOA061', 'TAU005', N'Nằm điều hòa',28, N'2', 1),
    ('TOA062', 'TAU005', N'Ngồi cứng',  80, N'3', 1),
    ('TOA063', 'TAU005', N'Nằm thường', 48, N'4', 1),
    ('TOA064', 'TAU005', N'Ngồi mềm',   56, N'5', 1),
    ('TOA065', 'TAU005', N'Nằm điều hòa',32, N'6', 1),
    ('TOA066', 'TAU005', N'Ngồi cứng',  72, N'7', 1),
    ('TOA067', 'TAU005', N'Ngồi cứng',   28, N'8', 1),
    ('TOA068', 'TAU005', N'Nằm thường', 52, N'9', 1),
    ('TOA069', 'TAU005', N'Ngồi mềm',   60, N'10', 1),
    ('TOA070', 'TAU005', N'Nằm điều hòa',30, N'11', 1),
    ('TOA071', 'TAU005', N'Ngồi cứng',  76, N'12', 1);

GO

INSERT INTO ChuyenTau VALUES
    ('CT001', 'TAU001', 'TT001', '2026-05-29', '22:00:00', 1),
    ('CT002', 'TAU002', 'TT002', '2026-05-30', '06:00:00', 1),
    ('CT003', 'TAU003', 'TT003', '2026-05-29', '08:30:00', 1),
    ('CT004', 'TAU004', 'TT005', '2026-06-30', '09:15:00', 1),
    ('CT005', 'TAU005', 'TT007', '2026-04-29', '07:45:00', 1),

    ('CT006', 'TAU001', 'TT008', '2026-05-29', '20:30:00', 1),
    ('CT007', 'TAU002', 'TT009', '2026-05-29', '05:30:00', 1),
    ('CT008', 'TAU003', 'TT010', '2026-05-30', '21:15:00', 1),
    ('CT009', 'TAU004', 'TT011', '2026-05-29', '19:00:00', 1),
    ('CT010', 'TAU005', 'TT012', '2026-06-01', '18:45:00', 1),

    ('CT011', 'TAU001', 'TT013', '2026-05-30', '17:30:00', 1),
    ('CT012', 'TAU002', 'TT014', '2026-05-29', '06:15:00', 1),
    ('CT013', 'TAU003', 'TT015', '2026-05-29', '10:20:00', 1),
    ('CT014', 'TAU004', 'TT001', '2026-05-29', '21:40:00', 1),
    ('CT015', 'TAU005', 'TT002', '2026-05-30', '07:00:00', 1),

    ('CT016', 'TAU001', 'TT003', '2026-05-30', '08:00:00', 1),
    ('CT017', 'TAU002', 'TT006', '2026-05-29', '19:45:00', 1),
    ('CT018', 'TAU003', 'TT008', '2026-05-30', '20:10:00', 1),
    ('CT019', 'TAU004', 'TT010', '2026-05-29', '06:50:00', 1),
    ('CT020', 'TAU005', 'TT015', '2026-05-30', '11:00:00', 1);
GO

INSERT INTO DichVu VALUES
    ('DV001', N'Suất ăn trên tàu', 1, 50000),
    ('DV002', N'Hành lý thêm',     1, 30000);
GO

INSERT INTO TaiKhoan VALUES
	('admin01', '123456', 'ADMIN'),
	('admin02', '123456', 'ADMIN'),
    ('nv001',   '123456', 'NHAN_VIEN'),
    ('nv002',   '123456', 'NHAN_VIEN'),
    ('nv003',   '123456', 'NHAN_VIEN'),
    ('nv004',   '123456', 'NHAN_VIEN'),
    ('nv005',   '123456', 'NHAN_VIEN'),
    ('nv006',   '123456', 'NHAN_VIEN'),
    ('nv007',   '123456', 'NHAN_VIEN'),
    ('nv008',   '123456', 'NHAN_VIEN'),
    ('nv009',   '123456', 'NHAN_VIEN'),
	('nv010', '123456', 'NHAN_VIEN'),
    ('nv011', '123456', 'NHAN_VIEN'),
    ('nv012', '123456', 'NHAN_VIEN'),
    ('nv013', '123456', 'NHAN_VIEN'),
    ('nv014', '123456', 'NHAN_VIEN');
GO

INSERT INTO NhanVien VALUES
	('NV001', N'Nguyễn Văn An', '0901234567', 1, '1990-05-15', '2020-01-01', 1, 'nva@tau.vn', N'Nhân viên bán vé',  'nv001'),
    ('NV002', N'Trần Thị Bích', '0912345678', 0, '1995-03-20', '2021-06-01', 1, 'ttb@tau.vn', N'Nhân viên bán vé',  'nv002'),
    ('NV003', N'Lê Văn Cường',  '0923456789', 1, '1988-11-10', '2018-03-15', 1, 'lvc@tau.vn', N'Quản trị hệ thống', 'admin01'),
    ('NV004', N'Phạm Thị Dung',  '0934567890', 0, '1992-07-25', '2019-10-01', 1, 'ptdung@tau.vn', N'Nhân viên bán vé',  'nv004'),
    ('NV005', N'Hoàng Văn Em',   '0945678901', 1, '1987-12-05', '2017-05-20', 1, 'hve@tau.vn',     N'Nhân viên bán vé',  'nv005'),
    ('NV006', N'Vũ Thị Phượng',  '0956789012', 0, '1996-09-18', '2022-02-10', 1, 'vtphuong@tau.vn', N'Nhân viên bán vé',  'nv006'),
    ('NV007', N'Đỗ Văn Giang',   '0967890123', 1, '1993-04-12', '2020-08-15', 0, 'dvgiang@tau.vn',  N'Nhân viên bán vé',  'nv007'),  -- nghỉ việc (TrangThai = 0)
    ('NV008', N'Nguyễn Thị Hoa', '0978901234', 0, '1998-01-30', '2023-03-01', 1, 'nthoa@tau.vn',    N'Nhân viên bán vé',  'nv008'),
    ('NV009', N'Bùi Văn Khánh',  '0989012345', 1, '1991-06-22', '2019-11-11', 1, 'bvkhanh@tau.vn',  N'Nhân viên bán vé',  'nv009'),
    ('NV010', N'Trịnh Minh Tuấn','0990123456', 1, '1985-08-10', '2016-09-01', 1, 'admin02@tau.vn',  N'Quản trị hệ thống', 'admin02'),
	('NV011', N'Lý Thị Ngọc',    '0901112223', 0, '1994-09-12', '2021-07-01', 1, 'ltngoc@tau.vn',  N'Nhân viên bán vé', 'nv010'),
    ('NV012', N'Đinh Văn Hùng',   '0902223334', 1, '1989-11-20', '2019-04-15', 1, 'dvhung@tau.vn',  N'Nhân viên bán vé', 'nv011'),
    ('NV013', N'Mai Thị Thanh',   '0903334445', 0, '1997-02-28', '2022-09-10', 1, 'mtthanh@tau.vn', N'Nhân viên bán vé', 'nv012'),
    ('NV014', N'Lương Văn Tú',    '0904445556', 1, '1993-06-05', '2020-12-01', 1, 'lvtu@tau.vn',    N'Nhân viên bán vé', 'nv013'),
    ('NV015', N'Vương Thị Hải',   '0905556667', 0, '1996-08-19', '2023-01-20', 1, 'vthai@tau.vn',   N'Nhân viên bán vé', 'nv014');
GO

INSERT INTO KhachHang VALUES
	('KH001', N'Phạm Văn Dũng', '0934567890', '012345678901', N'Hà Nội', 'pvd@gmail.com', 1, '1992-07-25', 0),
    ('KH002', N'Ngô Thị Lan',   '0945678901', '012345678902', N'TP.HCM', 'ntl@gmail.com', 0, '1998-02-14', 1),
    ('KH003', N'Trần Thị Hương',   '0956789012', '012345678903', N'Đà Nẵng',       'tthuong@gmail.com',    0, '1995-12-10', 1),
    ('KH004', N'Lê Văn An',        '0967890123', '012345678904', N'Hải Phòng',     'levana@gmail.com',     1, '1990-05-20', 1),
    ('KH005', N'Hoàng Thị Bích',   '0978901234', '012345678905', N'Cần Thơ',       'htbich@gmail.com',     0, '1988-09-15', 0),
    ('KH006', N'Vũ Văn Cường',     '0989012345', '012345678906', N'Hà Nội',        'vvc1992@gmail.com',    1, '1992-03-08', 1),
    ('KH007', N'Phạm Thị Dung',    '0990123456', '012345678907', N'Bình Dương',     'ptdung@gmail.com',     0, '2000-07-30', 1),
    ('KH008', N'Nguyễn Văn Em',    '0901234567', '012345678908', N'TP.HCM',        'nve@gmail.com',        1, '1996-11-22', 0),
    ('KH009', N'Đỗ Thị Phượng',    '0912345678', '012345678909', N'Quảng Ninh',     'dtphuong@gmail.com',   0, '1993-04-05', 1),
    ('KH010', N'Bùi Văn Giang',    '0923456789', '012345678910', N'Hà Nội',        'bvgiang@gmail.com',    1, '1985-08-17', 1),
    ('KH011', N'Trịnh Thị Hoa',    '0934567891', '012345678911', N'Đà Nẵng',       'tthoa@gmail.com',      0, '1999-01-28', 0),
    ('KH012', N'Đặng Văn Khánh',   '0945678902', '012345678912', N'Hải Phòng',     'dvkhanh@gmail.com',    1, '1994-06-12', 1),
	('KH013', N'Nguyễn Thị Linh',    '0956789013', '012345678913', N'Bắc Ninh',      'ntlinh@gmail.com',     0, '1997-09-03', 1),
    ('KH014', N'Phan Văn Minh',      '0967890124', '012345678914', N'Hưng Yên',      'pvminh@gmail.com',     1, '1991-12-19', 0),
    ('KH015', N'Lý Thị Ngọc',        '0978901235', '012345678915', N'TP.HCM',        'ltngoc@gmail.com',     0, '2001-04-25', 1),
    ('KH016', N'Đinh Văn Hùng',      '0989012346', '012345678916', N'Hà Nam',        'dvhung@gmail.com',     1, '1989-11-11', 1),
    ('KH017', N'Mai Thị Thanh',      '0990123457', '012345678917', N'Thanh Hóa',     'mtthanh@gmail.com',    0, '1993-06-07', 0),
    ('KH018', N'Lương Văn Tú',       '0901234568', '012345678918', N'Hà Tĩnh',       'lvtu@gmail.com',       1, '1995-02-28', 1),
    ('KH019', N'Vương Thị Hải',      '0912345679', '012345678919', N'Nam Định',      'vthai@gmail.com',      0, '1998-08-14', 1),
    ('KH020', N'Trương Văn Hiếu',    '0923456780', '012345678920', N'Quảng Nam',     'tvhieu@gmail.com',     1, '1990-10-01', 0);
GO

INSERT INTO KhuyenMai VALUES
    ('KM001', N'Giảm 10% mùa hè',  10, '2026-06-01', '2026-08-31', N'Áp dụng cho vé nằm'),
    ('KM002', N'Ưu đãi thành viên',  5, '2026-01-01', '2026-12-31', N'Áp dụng cho khách thành viên');
GO



INSERT INTO VeTau VALUES
    ('VE001', 'KH001', 'CT001', 'TOA002', 'A01',  'NGUOI_LON',      850000, 'DA_THANH_TOAN'),
    ('VE002', 'KH002', 'CT001', 'TOA001', 'B01', 'NGUOI_LON',      900000, 'CHO_THANH_TOAN'),

    ('VE003', 'KH003', 'CT002', 'TOA016', 'C01',  'TRE_EM',         450000, 'DA_THANH_TOAN'),
    ('VE004', 'KH004', 'CT002', 'TOA017', 'D01',  'NGUOI_LON',      920000, 'CHO_THANH_TOAN'),

    ('VE005', 'KH005', 'CT003', 'TOA029', 'A01', 'NGUOI_CAO_TUOI', 350000, 'DA_THANH_TOAN'),
    ('VE006', 'KH006', 'CT003', 'TOA030', 'A01',  'NGUOI_LON',      500000, 'DA_SU_DUNG'),

    ('VE007', 'KH007', 'CT004', 'TOA044', 'A01', 'NGUOI_LON',      780000, 'CHO_THANH_TOAN'),
    ('VE008', 'KH008', 'CT004', 'TOA045', 'A01',  'TRE_EM',         420000, 'DA_THANH_TOAN'),

    ('VE009', 'KH009', 'CT006', 'TOA004', 'A01',  'NGUOI_LON',      1100000, 'DA_THANH_TOAN'),
    ('VE010', 'KH010', 'CT006', 'TOA005', 'A01', 'NGUOI_LON',      980000, 'CHO_THANH_TOAN'),

    ('VE011', 'KH001', 'CT007', 'TOA018', 'A01',  'NGUOI_CAO_TUOI', 300000, 'DA_THANH_TOAN'),
    ('VE012', 'KH002', 'CT008', 'TOA031', 'A01', 'NGUOI_LON',      750000, 'DA_SU_DUNG'),

    ('VE013', 'KH003', 'CT009', 'TOA046', 'A01',  'TRE_EM',         410000, 'CHO_THANH_TOAN'),
    ('VE014', 'KH004', 'CT010', 'TOA060', 'A01',  'NGUOI_LON',      950000, 'DA_THANH_TOAN'),

    ('VE015', 'KH005', 'CT011', 'TOA006', 'A01',  'NGUOI_CAO_TUOI', 650000, 'DA_HOAN'),
    ('VE016', 'KH006', 'CT012', 'TOA019', 'A01', 'NGUOI_LON',      250000, 'DA_THANH_TOAN'),

    ('VE017', 'KH007', 'CT013', 'TOA032', 'A01',  'TRE_EM',         390000, 'CHO_THANH_TOAN'),
    ('VE018', 'KH008', 'CT014', 'TOA047', 'A01', 'NGUOI_LON',      1200000, 'DA_THANH_TOAN'),

    ('VE019', 'KH009', 'CT015', 'TOA061', 'A01',  'NGUOI_CAO_TUOI', 500000, 'DA_SU_DUNG'),
    ('VE020', 'KH010', 'CT016', 'TOA007', 'A01', 'NGUOI_LON',      470000, 'CHO_THANH_TOAN');
GO

INSERT INTO VeTau VALUES
    ('VE021', 'KH001', 'CT017', 'TOA020', 'A02', 'NGUOI_LON',       820000, 'DA_THANH_TOAN'),
    ('VE022', 'KH002', 'CT018', 'TOA033', 'B03', 'TRE_EM',          420000, 'CHO_THANH_TOAN'),
    ('VE023', 'KH003', 'CT019', 'TOA048', 'C04', 'NGUOI_LON',       560000, 'DA_THANH_TOAN'),
    ('VE024', 'KH004', 'CT020', 'TOA062', 'D05', 'NGUOI_CAO_TUOI',  390000, 'DA_SU_DUNG'),

    ('VE025', 'KH005', 'CT001', 'TOA002', 'A06', 'NGUOI_LON',       850000, 'DA_THANH_TOAN'),
    ('VE026', 'KH006', 'CT001', 'TOA001', 'B07', 'TRE_EM',          480000, 'CHO_THANH_TOAN'),
    ('VE027', 'KH007', 'CT002', 'TOA016', 'C08', 'NGUOI_LON',       920000, 'DA_THANH_TOAN'),
    ('VE028', 'KH008', 'CT002', 'TOA017', 'D09', 'NGUOI_LON',       910000, 'DA_SU_DUNG'),

    ('VE029', 'KH009', 'CT003', 'TOA029', 'A10', 'NGUOI_CAO_TUOI',  340000, 'DA_THANH_TOAN'),
    ('VE030', 'KH010', 'CT003', 'TOA030', 'B11', 'NGUOI_LON',       510000, 'CHO_THANH_TOAN'),

    ('VE031', 'KH001', 'CT004', 'TOA044', 'C12', 'TRE_EM',          430000, 'DA_THANH_TOAN'),
    ('VE032', 'KH002', 'CT004', 'TOA045', 'D13', 'NGUOI_LON',       780000, 'DA_THANH_TOAN'),

    ('VE033', 'KH003', 'CT006', 'TOA004', 'A14', 'NGUOI_LON',       1100000, 'DA_SU_DUNG'),
    ('VE034', 'KH004', 'CT006', 'TOA005', 'B15', 'NGUOI_CAO_TUOI',  890000, 'DA_THANH_TOAN'),

    ('VE035', 'KH005', 'CT007', 'TOA018', 'C16', 'NGUOI_LON',       320000, 'CHO_THANH_TOAN'),
    ('VE036', 'KH006', 'CT008', 'TOA031', 'D01', 'TRE_EM',          410000, 'DA_THANH_TOAN'),

    ('VE037', 'KH007', 'CT009', 'TOA046', 'A03', 'NGUOI_LON',       760000, 'DA_THANH_TOAN'),
    ('VE038', 'KH008', 'CT010', 'TOA060', 'B04', 'NGUOI_LON',       930000, 'DA_SU_DUNG'),

    ('VE039', 'KH009', 'CT011', 'TOA006', 'C05', 'NGUOI_CAO_TUOI',  620000, 'DA_HOAN'),
    ('VE040', 'KH010', 'CT012', 'TOA019', 'D06', 'NGUOI_LON',       260000, 'DA_THANH_TOAN'),

    ('VE041', 'KH001', 'CT013', 'TOA032', 'A07', 'TRE_EM',          400000, 'CHO_THANH_TOAN'),
    ('VE042', 'KH002', 'CT014', 'TOA047', 'B08', 'NGUOI_LON',       1180000, 'DA_THANH_TOAN'),

    ('VE043', 'KH003', 'CT015', 'TOA061', 'C09', 'NGUOI_CAO_TUOI',  510000, 'DA_SU_DUNG'),
    ('VE044', 'KH004', 'CT016', 'TOA007', 'D10', 'NGUOI_LON',       480000, 'CHO_THANH_TOAN'),

    ('VE045', 'KH005', 'CT017', 'TOA020', 'A11', 'NGUOI_LON',       830000, 'DA_THANH_TOAN'),
    ('VE046', 'KH006', 'CT018', 'TOA033', 'B12', 'TRE_EM',          415000, 'DA_THANH_TOAN'),

    ('VE047', 'KH007', 'CT019', 'TOA048', 'C13', 'NGUOI_LON',       570000, 'DA_SU_DUNG'),
    ('VE048', 'KH008', 'CT020', 'TOA062', 'D14', 'NGUOI_CAO_TUOI',  395000, 'CHO_THANH_TOAN'),

    ('VE049', 'KH009', 'CT001', 'TOA002', 'A15', 'NGUOI_LON',       860000, 'DA_THANH_TOAN'),
    ('VE050', 'KH010', 'CT002', 'TOA016', 'B16', 'TRE_EM',          440000, 'DA_THANH_TOAN');
GO

-- (Previously ChiTietVeTau data removed; passenger details are stored in `VeTau` now.)

INSERT INTO HoaDon VALUES
    ('HD001', 'NV001', 'KH001', NULL,    'T001', '2026-05-20', 'CHUYEN_KHOAN', '2026-05-20', 1),
    ('HD002', 'NV002', 'KH002', 'KM002', 'T001', '2026-05-20', 'TIEN_MAT',      NULL,         0),

    ('HD003', 'NV003', 'KH003', NULL,    'T001', '2026-05-21', 'VI_DIEN_TU',    '2026-05-21', 1),
    ('HD004', 'NV001', 'KH004', 'KM001', 'T001', '2026-05-21', 'CHUYEN_KHOAN',  '2026-05-21', 1),

    ('HD005', 'NV002', 'KH005', NULL,    'T001', '2026-05-22', 'TIEN_MAT',      NULL,         0),
    ('HD006', 'NV003', 'KH006', 'KM001', 'T001', '2026-05-22', 'VI_DIEN_TU',    '2026-05-22', 1),

    ('HD007', 'NV004', 'KH007', NULL,    'T001', '2026-05-23', 'CHUYEN_KHOAN',  '2026-05-23', 1),
    ('HD008', 'NV005', 'KH008', 'KM001', 'T001', '2026-05-23', 'TIEN_MAT',      NULL,         0),

    ('HD009', 'NV001', 'KH009', NULL,    'T001', '2026-05-24', 'VI_DIEN_TU',    '2026-05-24', 1),
    ('HD010', 'NV002', 'KH010', 'KM002', 'T001', '2026-05-24', 'CHUYEN_KHOAN',  '2026-05-24', 1),

    ('HD011', 'NV003', 'KH001', NULL,    'T001', '2026-05-24', 'TIEN_MAT',      NULL,         0),
    ('HD012', 'NV004', 'KH002', 'KM002', 'T001', '2026-05-24', 'VI_DIEN_TU',    '2026-05-24', 1),

    ('HD013', 'NV005', 'KH003', NULL,    'T001', '2026-05-25', 'CHUYEN_KHOAN',  '2026-05-25', 1),
    ('HD014', 'NV001', 'KH004', 'KM001', 'T001', '2026-05-25', 'TIEN_MAT',      NULL,         0),

    ('HD015', 'NV002', 'KH005', NULL,    'T001', '2026-05-25', 'VI_DIEN_TU',    '2026-05-25', 1);
GO

INSERT INTO ChiTietHoaDon_Ve VALUES
    ('HD001', 'VE001', 1, 850000),
    ('HD002', 'VE002', 1, 900000),
    ('HD003', 'VE003', 1, 450000),
    ('HD004', 'VE004', 1, 920000),
    ('HD005', 'VE005', 1, 350000),
    ('HD006', 'VE006', 1, 500000),
    ('HD007', 'VE007', 1, 780000),
    ('HD008', 'VE008', 1, 420000),
    ('HD009', 'VE009', 1, 1100000),
    ('HD010', 'VE010', 1, 980000),

    ('HD011', 'VE011', 1, 300000),
    ('HD012', 'VE012', 1, 750000),
    ('HD013', 'VE013', 1, 410000),
    ('HD014', 'VE014', 1, 950000),
    ('HD015', 'VE015', 1, 650000);
GO

INSERT INTO ChiTietHoaDon_DichVu VALUES
    ('HD001', 'DV001', 2, 50000),
    ('HD002', 'DV002', 1, 30000),

    ('HD003', 'DV001', 1, 50000),
    ('HD004', 'DV002', 2, 30000),

    ('HD005', 'DV001', 3, 50000),
    ('HD006', 'DV002', 1, 30000),

    ('HD007', 'DV001', 2, 50000),
    ('HD008', 'DV002', 2, 30000),

    ('HD009', 'DV001', 1, 50000),
    ('HD010', 'DV002', 3, 30000),

    ('HD011', 'DV001', 2, 50000),
    ('HD012', 'DV002', 1, 30000),

    ('HD013', 'DV001', 1, 50000),
    ('HD014', 'DV002', 2, 30000),

    ('HD015', 'DV001', 2, 50000);
GO

PRINT N'Tạo CSDL QuanLyVeTau thành công!';
GO
