package entity;

public class ChiTietHoaDonDichVu {
	private String maHD;
	private String maDV;
	private int soLuong;
	private double donGia;

	public ChiTietHoaDonDichVu() {
	}

	public ChiTietHoaDonDichVu(String maHD, String maDV, int soLuong, double donGia) {
		this.maHD = maHD;
		this.maDV = maDV;
		this.soLuong = soLuong;
		this.donGia = donGia;
	}

	public String getMaHD() {
		return maHD;
	}

	public void setMaHD(String maHD) {
		this.maHD = maHD;
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

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}
}