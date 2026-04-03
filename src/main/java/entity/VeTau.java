package entity;

import java.math.BigDecimal;

public class VeTau {
	private String maVeTau;
	private String maKH;
	private String maChuyenTau;
	private String maToa;
	private String viTriGhe;
	private int soLuongVe;
	private String maNhanVien;
	private BigDecimal giaVe;

	public VeTau() {
	}

	public VeTau(String maVeTau, String maKH, String maChuyenTau, String maToa, String viTriGhe, int soLuongVe,
			String maNhanVien, BigDecimal giaVe) {
		this.maVeTau = maVeTau;
		this.maKH = maKH;
		this.maChuyenTau = maChuyenTau;
		this.maToa = maToa;
		this.viTriGhe = viTriGhe;
		this.soLuongVe = soLuongVe;
		this.maNhanVien = maNhanVien;
		this.giaVe = giaVe;
	}

	public String getMaVeTau() { return maVeTau; }
	public void setMaVeTau(String maVeTau) { this.maVeTau = maVeTau; }
	public String getMaKH() { return maKH; }
	public void setMaKH(String maKH) { this.maKH = maKH; }
	public String getMaChuyenTau() { return maChuyenTau; }
	public void setMaChuyenTau(String maChuyenTau) { this.maChuyenTau = maChuyenTau; }
	public String getMaToa() { return maToa; }
	public void setMaToa(String maToa) { this.maToa = maToa; }
	public String getViTriGhe() { return viTriGhe; }
	public void setViTriGhe(String viTriGhe) { this.viTriGhe = viTriGhe; }
	public int getSoLuongVe() { return soLuongVe; }
	public void setSoLuongVe(int soLuongVe) { this.soLuongVe = soLuongVe; }
	public String getMaNhanVien() { return maNhanVien; }
	public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }
	public BigDecimal getGiaVe() { return giaVe; }
	public void setGiaVe(BigDecimal giaVe) { this.giaVe = giaVe; }
}
