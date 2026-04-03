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
    password VARCHAR(255)
);
GO


-- =========================
-- NHAN VIEN
-- =========================
CREATE TABLE NhanVien (
    maNV VARCHAR(20) NOT NULL PRIMARY KEY,
    tenNV NVARCHAR(100),
    sdt VARCHAR(15),
    gioiTinh BIT,
    ngaySinh DATE,
    ngayVaoLam DATE,
    chucVu NVARCHAR(50),
	trangThai BIT,
    username VARCHAR(50),

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
    maKH VARCHAR(20) PRIMARY KEY,
    tenKH NVARCHAR(100),
    sdt VARCHAR(15),
    cccd VARCHAR(20),
	diaChi VARCHAR(100),
    email VARCHAR(100),
    gioiTinh BIT,
    ngaySinh DATE,
    loaiKH BIT
);
GO


-- =========================
-- KHUYEN MAI
-- =========================
CREATE TABLE KhuyenMai (
    maKM VARCHAR(20) PRIMARY KEY,
    tenKM NVARCHAR(100),
    tyLeKM DECIMAL(5,2),
    ngayBD DATE,
    ngayKT DATE
);
GO


-- =========================
-- DICH VU
-- =========================
CREATE TABLE DichVu (
    maDV VARCHAR(20) PRIMARY KEY,
    tenDV NVARCHAR(100),
    trangThai BIT,
    giaDV DECIMAL(10,2)
);
GO


-- =========================
-- TUYEN TAU
-- =========================
CREATE TABLE TuyenTau (
    maTT VARCHAR(20) PRIMARY KEY,
    maGaDi NVARCHAR(100),
    maGaDen NVARCHAR(100),
    khoangCach FLOAT
);
GO


-- =========================
-- TAU
-- =========================
CREATE TABLE Tau (
    maTau VARCHAR(20) PRIMARY KEY,
    tenTau NVARCHAR(100),
    soLuongToa INT
);
GO


-- =========================
-- TOA
-- =========================
CREATE TABLE Toa (
    maToa VARCHAR(20) PRIMARY KEY,
    loaiToa NVARCHAR(50),
    soGhe INT,
    viTriToa NVARCHAR(50),
    trangThai BIT,
    maTau VARCHAR(20),

    CONSTRAINT FK_Toa_Tau
    FOREIGN KEY (maTau)
    REFERENCES Tau(maTau)
);
GO


-- =========================
-- CHUYEN TAU
-- =========================
CREATE TABLE ChuyenTau (
    maCT VARCHAR(20) PRIMARY KEY,
	ngayKhoiHanh DATETIME,
    gioKhoiHanh DATETIME,
    trangThai BIT,
    maTau VARCHAR(20),
    maTuyenTau VARCHAR(20),

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
    maVeTau VARCHAR(20) PRIMARY KEY,
    maKH VARCHAR(20),
    maChuyenTau VARCHAR(20),
    maToa VARCHAR(20),
    viTriGhe VARCHAR(10),
	soLuongVe INT,
    maNhanVien VARCHAR(20),
    giaVe DECIMAL(10,2),

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
    maHD VARCHAR(20) PRIMARY KEY,
	maNV VARCHAR(20),
    maKH VARCHAR(20),
    thoiGian DATETIME,
    vat DECIMAL(5,2),
    tongTien DECIMAL(12,2),
    maKM VARCHAR(20),

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
    maHD VARCHAR(20),
    maVeTau VARCHAR(20),

    PRIMARY KEY (maHD, maVeTau),

    CONSTRAINT FK_CTHD_HoaDon
    FOREIGN KEY (maHD)
    REFERENCES HoaDon(maHD),

    CONSTRAINT FK_CTHD_VeTau
    FOREIGN KEY (maVeTau)
    REFERENCES VeTau(maVeTau)
);
GO
