package entity;

public class ChiTietHoaDon {
	private String maHD;
	private String maVeTau;

	public ChiTietHoaDon() {
	}

	public ChiTietHoaDon(String maHD, String maVeTau) {
		this.maHD = maHD;
		this.maVeTau = maVeTau;
	}

	public String getMaHD() { return maHD; }
	public void setMaHD(String maHD) { this.maHD = maHD; }
	public String getMaVeTau() { return maVeTau; }
	public void setMaVeTau(String maVeTau) { this.maVeTau = maVeTau; }
}
