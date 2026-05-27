package entity;

public class ChiTietHoaDonVe {
	private String maHD;
	private String maVeTau;
	private int soLuong;
	private double donGia;

	public ChiTietHoaDonVe() {
	}

	public ChiTietHoaDonVe(String maHD, String maVeTau, int soLuong, double donGia) {
		this.maHD = maHD;
		this.maVeTau = maVeTau;
		this.soLuong = soLuong;
		this.donGia = donGia;
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