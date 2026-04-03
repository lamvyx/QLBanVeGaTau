-- =========================
-- TAO DATABASE
-- =========================
CREATE DATABASE QuanLyVT;
GO

USE QuanLyVT;
GO


-- =========================
-- TAI KHOAN
-- =========================
CREATE TABLE TaiKhoan (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    hoTen NVARCHAR(100),
    vaiTro VARCHAR(50) NOT NULL
);
GO


-- =========================
-- NHAN VIEN
-- =========================
CREATE TABLE NhanVien (
    maNV VARCHAR(20) NOT NULL PRIMARY KEY,
    tenNV NVARCHAR(100) NOT NULL,
    sdt VARCHAR(15),
    gioiTinh BIT NOT NULL,
    ngaySinh DATE,
    ngayVaoLam DATE,
    chucVu NVARCHAR(50),
	trangThai BIT NOT NULL,
    username VARCHAR(50) UNIQUE,
    hinhAnh NVARCHAR(255),

    CONSTRAINT FK_NhanVien_TaiKhoan
    FOREIGN KEY (username)
    REFERENCES TaiKhoan(username)
);
GO


-- =========================
-- PHIEN
-- =========================
CREATE TABLE Phien (
    maPhien VARCHAR(20) PRIMARY KEY,
    thoiGianBD DATETIME,
    thoiGianKT DATETIME
);
GO


-- =========================
-- KHACH HANG
-- =========================
CREATE TABLE KhachHang (
    maKH VARCHAR(20) NOT NULL PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    sdt VARCHAR(15),
    cccd VARCHAR(20),
	diaChi NVARCHAR(100),
    email VARCHAR(100),
    gioiTinh BIT NOT NULL,
    ngaySinh DATE,
    loaiKH BIT NOT NULL
);
GO


-- =========================
-- KHUYEN MAI
-- =========================
CREATE TABLE KhuyenMai (
    maKM VARCHAR(20) NOT NULL PRIMARY KEY,
    tenKM NVARCHAR(100) NOT NULL,
    tyLeKM DECIMAL(5,2) NOT NULL,
    ngayBD DATE NOT NULL,
    ngayKT DATE NOT NULL,

    CONSTRAINT CK_KhuyenMai_TyLeKM
    CHECK (tyLeKM >= 0 AND tyLeKM <= 100),

    CONSTRAINT CK_KhuyenMai_Ngay
    CHECK (ngayKT >= ngayBD)
);
GO


-- =========================
-- DICH VU
-- =========================
CREATE TABLE DichVu (
    maDV VARCHAR(20) NOT NULL PRIMARY KEY,
    tenDV NVARCHAR(100) NOT NULL,
    trangThai BIT NOT NULL,
    giaDV DECIMAL(10,2) NOT NULL,

    CONSTRAINT CK_DichVu_GiaDV
    CHECK (giaDV >= 0)
);
GO


-- =========================
-- TUYEN TAU
-- =========================
CREATE TABLE TuyenTau (
    maTT VARCHAR(20) NOT NULL PRIMARY KEY,
    maGaDi NVARCHAR(100) NOT NULL,
    maGaDen NVARCHAR(100) NOT NULL,
    khoangCach DECIMAL(10,2) NOT NULL,

    CONSTRAINT CK_TuyenTau_KhoangCach
    CHECK (khoangCach > 0)
);
GO


-- =========================
-- TAU
-- =========================
CREATE TABLE Tau (
    maTau VARCHAR(20) NOT NULL PRIMARY KEY,
    tenTau NVARCHAR(100) NOT NULL,
    soLuongToa INT NOT NULL,

    CONSTRAINT CK_Tau_SoLuongToa
    CHECK (soLuongToa >= 0)
);
GO


-- =========================
-- TOA
-- =========================
CREATE TABLE Toa (
    maToa VARCHAR(20) NOT NULL PRIMARY KEY,
    loaiToa NVARCHAR(50) NOT NULL,
    soGhe INT NOT NULL,
    viTriToa NVARCHAR(50),
    trangThai BIT NOT NULL,
    maTau VARCHAR(20) NOT NULL,

    CONSTRAINT CK_Toa_SoGhe
    CHECK (soGhe > 0),

    CONSTRAINT FK_Toa_Tau
    FOREIGN KEY (maTau)
    REFERENCES Tau(maTau)
);
GO


-- =========================
-- CHUYEN TAU
-- =========================
CREATE TABLE ChuyenTau (
    maCT VARCHAR(20) NOT NULL PRIMARY KEY,
	ngayKhoiHanh DATETIME NOT NULL,
    gioKhoiHanh DATETIME NOT NULL,
    trangThai BIT NOT NULL,
    maTau VARCHAR(20) NOT NULL,
    maTuyenTau VARCHAR(20) NOT NULL,

    CONSTRAINT FK_ChuyenTau_Tau
    FOREIGN KEY (maTau)
    REFERENCES Tau(maTau),

    CONSTRAINT FK_ChuyenTau_TuyenTau
    FOREIGN KEY (maTuyenTau)
    REFERENCES TuyenTau(maTT)
);
GO


-- =========================
-- VE TAU
-- =========================
CREATE TABLE VeTau (
    maVeTau VARCHAR(20) NOT NULL PRIMARY KEY,
    maKH VARCHAR(20) NOT NULL,
    maChuyenTau VARCHAR(20) NOT NULL,
    maToa VARCHAR(20) NOT NULL,
    viTriGhe VARCHAR(10) NOT NULL,
	soLuongVe INT NOT NULL,
    maNhanVien VARCHAR(20) NOT NULL,
    giaVe DECIMAL(10,2) NOT NULL,

    CONSTRAINT CK_VeTau_SoLuongVe
    CHECK (soLuongVe > 0),

    CONSTRAINT CK_VeTau_GiaVe
    CHECK (giaVe >= 0),

    CONSTRAINT FK_VeTau_KhachHang
    FOREIGN KEY (maKH)
    REFERENCES KhachHang(maKH),

    CONSTRAINT FK_VeTau_ChuyenTau
    FOREIGN KEY (maChuyenTau)
    REFERENCES ChuyenTau(maCT),

    CONSTRAINT FK_VeTau_Toa
    FOREIGN KEY (maToa)
    REFERENCES Toa(maToa),

    CONSTRAINT FK_VeTau_NhanVien
    FOREIGN KEY (maNhanVien)
    REFERENCES NhanVien(maNV)
);
GO


-- =========================
-- HOA DON
-- =========================
CREATE TABLE HoaDon (
    maHD VARCHAR(20) NOT NULL PRIMARY KEY,
	maNV VARCHAR(20) NOT NULL,
    maKH VARCHAR(20) NOT NULL,
    thoiGian DATETIME NOT NULL,
    vat DECIMAL(5,2) NOT NULL,
    tongTien DECIMAL(12,2) NOT NULL,
    maKM VARCHAR(20),

    CONSTRAINT CK_HoaDon_VAT
    CHECK (vat >= 0 AND vat <= 100),

    CONSTRAINT CK_HoaDon_TongTien
    CHECK (tongTien >= 0),

	CONSTRAINT FK_HoaDon_NhanVien
    FOREIGN KEY (maNV)
    REFERENCES NhanVien(maNV),

    CONSTRAINT FK_HoaDon_KhachHang
    FOREIGN KEY (maKH)
    REFERENCES KhachHang(maKH),

    CONSTRAINT FK_HoaDon_KhuyenMai
    FOREIGN KEY (maKM)
    REFERENCES KhuyenMai(maKM)
);
GO


-- =========================
-- CHI TIET HOA DON
-- =========================
CREATE TABLE ChiTietHoaDon (
    maHD VARCHAR(20) NOT NULL,
    maVeTau VARCHAR(20) NOT NULL,

    PRIMARY KEY (maHD, maVeTau),

    CONSTRAINT FK_CTHD_HoaDon
    FOREIGN KEY (maHD)
    REFERENCES HoaDon(maHD),

    CONSTRAINT FK_CTHD_VeTau
    FOREIGN KEY (maVeTau)
    REFERENCES VeTau(maVeTau)
);
GO


-- =========================
-- CHI TIET HOA DON - DICH VU
-- =========================
CREATE TABLE ChiTietDichVu (
    maHD VARCHAR(20) NOT NULL,
    maDV VARCHAR(20) NOT NULL,
    soLuong INT NOT NULL,
    donGia DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (maHD, maDV),

    CONSTRAINT CK_CTDV_SoLuong
    CHECK (soLuong > 0),

    CONSTRAINT CK_CTDV_DonGia
    CHECK (donGia >= 0),

    CONSTRAINT FK_CTDV_HoaDon
    FOREIGN KEY (maHD)
    REFERENCES HoaDon(maHD),

    CONSTRAINT FK_CTDV_DichVu
    FOREIGN KEY (maDV)
    REFERENCES DichVu(maDV)
);
GO
