package entity;

import java.time.LocalDate;

public class PhieuDatVe {
	private String maPhieu;
	private String maKH;
	private String maNV;
	private LocalDate ngayDat;
	private LocalDate hanThanhToan;
	private boolean trangThai;

	public PhieuDatVe() {
	}

	public PhieuDatVe(String maPhieu, String maKH, String maNV, LocalDate ngayDat, LocalDate hanThanhToan,
			boolean trangThai) {
		this.maPhieu = maPhieu;
		this.maKH = maKH;
		this.maNV = maNV;
		this.ngayDat = ngayDat;
		this.hanThanhToan = hanThanhToan;
		this.trangThai = trangThai;
	}

	public String getMaPhieu() {
		return maPhieu;
	}

	public void setMaPhieu(String maPhieu) {
		this.maPhieu = maPhieu;
	}

	public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}

	public LocalDate getNgayDat() {
		return ngayDat;
	}

	public void setNgayDat(LocalDate ngayDat) {
		this.ngayDat = ngayDat;
	}

	public LocalDate getHanThanhToan() {
		return hanThanhToan;
	}

	public void setHanThanhToan(LocalDate hanThanhToan) {
		this.hanThanhToan = hanThanhToan;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
}