package entity;

import java.math.BigDecimal;

public class VeTau {
	private String maVeTau;
	private String maKH;
	private String maCT;
	private String maToa;
	private String viTriGhe;
	private String loaiVe;
	private BigDecimal giaVe;
	private String trangThai;

	public VeTau() {
	}

	public VeTau(String maVeTau, String maKH, String maCT, String maToa, String viTriGhe, String loaiVe,
			BigDecimal giaVe, String trangThai) {
		this.maVeTau = maVeTau;
		this.maKH = maKH;
		this.maCT = maCT;
		this.maToa = maToa;
		this.viTriGhe = viTriGhe;
		this.loaiVe = loaiVe;
		this.giaVe = giaVe;
		this.trangThai = trangThai;
	}

	public String getLoaiVe() { return loaiVe; }
	public void setLoaiVe(String loaiVe) { this.loaiVe = loaiVe; }
	public String getTrangThai() { return trangThai; }
	public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
	public String getMaCT() { return maCT; }
	public void setMaCT(String maCT) { this.maCT = maCT; }

	public String getMaChuyenTau() { return maCT; }
	public void setMaChuyenTau(String maChuyenTau) { this.maCT = maChuyenTau; }

	public String getMaVeTau() { return maVeTau; }
	public void setMaVeTau(String maVeTau) { this.maVeTau = maVeTau; }
	public String getMaKH() { return maKH; }
	public void setMaKH(String maKH) { this.maKH = maKH; }
	public String getMaToa() { return maToa; }
	public void setMaToa(String maToa) { this.maToa = maToa; }
	public String getViTriGhe() { return viTriGhe; }
	public void setViTriGhe(String viTriGhe) { this.viTriGhe = viTriGhe; }
	public BigDecimal getGiaVe() { return giaVe; }
	public void setGiaVe(BigDecimal giaVe) { this.giaVe = giaVe; }
}
