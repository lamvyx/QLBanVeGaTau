package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ChiTietVeTau {
	private String maChiTiet;
	private String maVeTau;
	private String tenHanhKhach;
	private String cccd;
	private LocalDate ngaySinh;
	private String viTriGhe;
	private String loaiVe;
	private BigDecimal giaVeTheoLoai;

	public ChiTietVeTau() {
	}

	public ChiTietVeTau(String maChiTiet, String maVeTau, String tenHanhKhach, String cccd,
			LocalDate ngaySinh, String viTriGhe, String loaiVe, BigDecimal giaVeTheoLoai) {
		this.maChiTiet = maChiTiet;
		this.maVeTau = maVeTau;
		this.tenHanhKhach = tenHanhKhach;
		this.cccd = cccd;
		this.ngaySinh = ngaySinh;
		this.viTriGhe = viTriGhe;
		this.loaiVe = loaiVe;
		this.giaVeTheoLoai = giaVeTheoLoai;
	}

	public String getMaChiTiet() { return maChiTiet; }
	public void setMaChiTiet(String maChiTiet) { this.maChiTiet = maChiTiet; }
	public String getMaVeTau() { return maVeTau; }
	public void setMaVeTau(String maVeTau) { this.maVeTau = maVeTau; }
	public String getTenHanhKhach() { return tenHanhKhach; }
	public void setTenHanhKhach(String tenHanhKhach) { this.tenHanhKhach = tenHanhKhach; }
	public String getCccd() { return cccd; }
	public void setCccd(String cccd) { this.cccd = cccd; }
	public LocalDate getNgaySinh() { return ngaySinh; }
	public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }
	public String getViTriGhe() { return viTriGhe; }
	public void setViTriGhe(String viTriGhe) { this.viTriGhe = viTriGhe; }
	public String getLoaiVe() { return loaiVe; }
	public void setLoaiVe(String loaiVe) { this.loaiVe = loaiVe; }
	public BigDecimal getGiaVeTheoLoai() { return giaVeTheoLoai; }
	public void setGiaVeTheoLoai(BigDecimal giaVeTheoLoai) { this.giaVeTheoLoai = giaVeTheoLoai; }
}