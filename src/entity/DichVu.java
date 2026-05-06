package entity;

import java.math.BigDecimal;

public class DichVu {
	private String maDV;
	private String tenDV;
	private boolean trangThai;
	private BigDecimal giaDV;

	public DichVu() {
	}

	public DichVu(String maDV, String tenDV, boolean trangThai, BigDecimal giaDV) {
		this.maDV = maDV;
		this.tenDV = tenDV;
		this.trangThai = trangThai;
		this.giaDV = giaDV;
	}

	public String getMaDV() { return maDV; }
	public void setMaDV(String maDV) { this.maDV = maDV; }
	public String getTenDV() { return tenDV; }
	public void setTenDV(String tenDV) { this.tenDV = tenDV; }
	public boolean isTrangThai() { return trangThai; }
	public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
	public BigDecimal getGiaDV() { return giaDV; }
	public void setGiaDV(BigDecimal giaDV) { this.giaDV = giaDV; }
}
