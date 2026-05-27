package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class HoaDon {
	private String maHD;
	private String maNV;
	private String maKH;
	private String tenNV;
	private String tenKH;
	private String maKM;
	private String maThue;
	private String phuongThucThanhToan;
	private LocalDateTime thoiGian;
	private BigDecimal vat;
	private BigDecimal tongTien;
	private boolean trangThaiThanhToan;
	private LocalDate ngayThanhToan;

	public HoaDon() {
	}

	public HoaDon(String maHD, String maNV, String maKH, LocalDateTime thoiGian, BigDecimal vat,
			BigDecimal tongTien, String maKM) {
		this.maHD = maHD;
		this.maNV = maNV;
		this.maKH = maKH;
		this.maKM = maKM;
		this.thoiGian = thoiGian;
		this.vat = vat;
		this.tongTien = tongTien;
	}

	public String getMaHD() { return maHD; }
	public void setMaHD(String maHD) { this.maHD = maHD; }
	public String getMaNV() { return maNV; }
	public void setMaNV(String maNV) { this.maNV = maNV; }
	public String getMaKH() { return maKH; }
	public void setMaKH(String maKH) { this.maKH = maKH; }
	public String getTenNV() { return tenNV; }
	public void setTenNV(String tenNV) { this.tenNV = tenNV; }
	public String getTenKH() { return tenKH; }
	public void setTenKH(String tenKH) { this.tenKH = tenKH; }
	public String getMaKM() { return maKM; }
	public void setMaKM(String maKM) { this.maKM = maKM; }
	public String getMaThue() { return maThue; }
	public void setMaThue(String maThue) { this.maThue = maThue; }
	public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
	public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }
	public LocalDateTime getThoiGian() { return thoiGian; }
	public void setThoiGian(LocalDateTime thoiGian) { this.thoiGian = thoiGian; }
	public BigDecimal getVat() { return vat; }
	public void setVat(BigDecimal vat) { this.vat = vat; }
	public BigDecimal getTongTien() { return tongTien; }
	public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
	public boolean isTrangThaiThanhToan() { return trangThaiThanhToan; }
	public void setTrangThaiThanhToan(boolean trangThaiThanhToan) { this.trangThaiThanhToan = trangThaiThanhToan; }
	public LocalDate getNgayThanhToan() { return ngayThanhToan; }
	public void setNgayThanhToan(LocalDate ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }
}
