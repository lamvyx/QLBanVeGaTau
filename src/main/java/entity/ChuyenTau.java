package entity;

import java.time.LocalDateTime;

public class ChuyenTau {
	private String maCT;
	private LocalDateTime ngayKhoiHanh;
	private LocalDateTime gioKhoiHanh;
	private boolean trangThai;
	private String maTau;
	private String maTuyenTau;

	public ChuyenTau() {
	}

	public ChuyenTau(String maCT, LocalDateTime ngayKhoiHanh, LocalDateTime gioKhoiHanh, boolean trangThai,
			String maTau, String maTuyenTau) {
		this.maCT = maCT;
		this.ngayKhoiHanh = ngayKhoiHanh;
		this.gioKhoiHanh = gioKhoiHanh;
		this.trangThai = trangThai;
		this.maTau = maTau;
		this.maTuyenTau = maTuyenTau;
	}

	public String getMaCT() { return maCT; }
	public void setMaCT(String maCT) { this.maCT = maCT; }
	public LocalDateTime getNgayKhoiHanh() { return ngayKhoiHanh; }
	public void setNgayKhoiHanh(LocalDateTime ngayKhoiHanh) { this.ngayKhoiHanh = ngayKhoiHanh; }
	public LocalDateTime getGioKhoiHanh() { return gioKhoiHanh; }
	public void setGioKhoiHanh(LocalDateTime gioKhoiHanh) { this.gioKhoiHanh = gioKhoiHanh; }
	public boolean isTrangThai() { return trangThai; }
	public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
	public String getMaTau() { return maTau; }
	public void setMaTau(String maTau) { this.maTau = maTau; }
	public String getMaTuyenTau() { return maTuyenTau; }
	public void setMaTuyenTau(String maTuyenTau) { this.maTuyenTau = maTuyenTau; }
}
