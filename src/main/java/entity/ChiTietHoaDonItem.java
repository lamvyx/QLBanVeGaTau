package entity;

import java.math.BigDecimal;

public class ChiTietHoaDonItem {
	private String maCTHD;
	private String maHD;
	private String maVeTau;
	private String maDV;
	private int soLuong;
	private BigDecimal donGia;

	public ChiTietHoaDonItem() {
	}

	public ChiTietHoaDonItem(String maCTHD, String maHD, String maVeTau, String maDV, int soLuong, BigDecimal donGia) {
		this.maCTHD = maCTHD;
		this.maHD = maHD;
		this.maVeTau = maVeTau;
		this.maDV = maDV;
		this.soLuong = soLuong;
		this.donGia = donGia;
	}

	public String getMaCTHD() {
		return maCTHD;
	}

	public void setMaCTHD(String maCTHD) {
		this.maCTHD = maCTHD;
	}

	public String getMaHD() {
		return maHD;
	}

	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}

	public String getMaVeTau() {
		return maVeTau;
	}

	public void setMaVeTau(String maVeTau) {
		this.maVeTau = maVeTau;
	}

	public String getMaDV() {
		return maDV;
	}

	public void setMaDV(String maDV) {
		this.maDV = maDV;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public BigDecimal getDonGia() {
		return donGia;
	}

	public void setDonGia(BigDecimal donGia) {
		this.donGia = donGia;
	}
}
