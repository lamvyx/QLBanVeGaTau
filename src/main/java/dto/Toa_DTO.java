package dto;

public class Toa_DTO {
	private String maToa;
	private String loaiToa;
	private int soGhe;
	private String viTriToa;
	private boolean trangThai;
	private String maTau;

	public Toa_DTO() {
	}

	public Toa_DTO(String maToa, String loaiToa, int soGhe, String viTriToa, boolean trangThai, String maTau) {
		this.maToa = maToa;
		this.loaiToa = loaiToa;
		this.soGhe = soGhe;
		this.viTriToa = viTriToa;
		this.trangThai = trangThai;
		this.maTau = maTau;
	}
	
	public Toa_DTO(String viTriToa, String loaiToa, int soGhe) {
		this.viTriToa = viTriToa;
		this.loaiToa = loaiToa;
		this.soGhe = soGhe;
	}
	
	public String getMaToa() { return maToa; }
	public void setMaToa(String maToa) { this.maToa = maToa; }
	public String getLoaiToa() { return loaiToa; }
	public void setLoaiToa(String loaiToa) { this.loaiToa = loaiToa; }
	public int getSoGhe() { return soGhe; }
	public void setSoGhe(int soGhe) { this.soGhe = soGhe; }
	public String getViTriToa() { return viTriToa; }
	public void setViTriToa(String viTriToa) { this.viTriToa = viTriToa; }
	public boolean isTrangThai() { return trangThai; }
	public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
	public String getMaTau() { return maTau; }
	public void setMaTau(String maTau) { this.maTau = maTau; }

	@Override
	public String toString() {
		return "Toa " + viTriToa + " - " + loaiToa + " - " + soGhe;	

	}
	
	
}

