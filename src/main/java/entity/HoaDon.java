package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HoaDon {
	private String maHD;
	private String maNV;
	private String maKH;
	private LocalDateTime thoiGian;
	private BigDecimal vat;
	private BigDecimal tongTien;
	private String maKM;

	public HoaDon() {
	}

	public HoaDon(String maHD, String maNV, String maKH, LocalDateTime thoiGian, BigDecimal vat,
			BigDecimal tongTien, String maKM) {
		this.maHD = maHD;
		this.maNV = maNV;
		this.maKH = maKH;
		this.thoiGian = thoiGian;
		this.vat = vat;
		this.tongTien = tongTien;
		this.maKM = maKM;
	}

	public String getMaHD() { return maHD; }
	public void setMaHD(String maHD) { this.maHD = maHD; }
	public String getMaNV() { return maNV; }
	public void setMaNV(String maNV) { this.maNV = maNV; }
	public String getMaKH() { return maKH; }
	public void setMaKH(String maKH) { this.maKH = maKH; }
	public LocalDateTime getThoiGian() { return thoiGian; }
	public void setThoiGian(LocalDateTime thoiGian) { this.thoiGian = thoiGian; }
	public BigDecimal getVat() { return vat; }
	public void setVat(BigDecimal vat) { this.vat = vat; }
	public BigDecimal getTongTien() { return tongTien; }
	public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
	public String getMaKM() { return maKM; }
	public void setMaKM(String maKM) { this.maKM = maKM; }
}
