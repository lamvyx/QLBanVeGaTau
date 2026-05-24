package entity;

public class ChiTietPhieuDat {
	private String maPhieu;
	private String maCT;
	private String maToa;
	private String viTriGhe;
	private double giaVe;
	private String ghiChu;

	public ChiTietPhieuDat() {
	}

	public ChiTietPhieuDat(String maPhieu, String maCT, String maToa, String viTriGhe, double giaVe, String ghiChu) {
		this.maPhieu = maPhieu;
		this.maCT = maCT;
		this.maToa = maToa;
		this.viTriGhe = viTriGhe;
		this.giaVe = giaVe;
		this.ghiChu = ghiChu;
	}

	public String getMaPhieu() {
		return maPhieu;
	}

	public void setMaPhieu(String maPhieu) {
		this.maPhieu = maPhieu;
	}

	public String getMaCT() {
		return maCT;
	}

	public void setMaCT(String maCT) {
		this.maCT = maCT;
	}

	public String getMaToa() {
		return maToa;
	}

	public void setMaToa(String maToa) {
		this.maToa = maToa;
	}

	public String getViTriGhe() {
		return viTriGhe;
	}

	public void setViTriGhe(String viTriGhe) {
		this.viTriGhe = viTriGhe;
	}

	public double getGiaVe() {
		return giaVe;
	}

	public void setGiaVe(double giaVe) {
		this.giaVe = giaVe;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
}