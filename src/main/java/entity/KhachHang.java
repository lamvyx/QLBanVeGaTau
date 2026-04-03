package entity;

import java.time.LocalDate;

public class KhachHang {
	private String maKH;
	private String tenKH;
	private String sdt;
	private String cccd;
	private String diaChi;
	private String email;
	private boolean gioiTinh;
	private LocalDate ngaySinh;
	private boolean loaiKH;

	public KhachHang() {
	}

	public KhachHang(String maKH, String tenKH, String sdt, String cccd, String diaChi, String email,
			boolean gioiTinh, LocalDate ngaySinh, boolean loaiKH) {
		this.maKH = maKH;
		this.tenKH = tenKH;
		this.sdt = sdt;
		this.cccd = cccd;
		this.diaChi = diaChi;
		this.email = email;
		this.gioiTinh = gioiTinh;
		this.ngaySinh = ngaySinh;
		this.loaiKH = loaiKH;
	}

	public String getMaKH() { return maKH; }
	public void setMaKH(String maKH) { this.maKH = maKH; }
	public String getTenKH() { return tenKH; }
	public void setTenKH(String tenKH) { this.tenKH = tenKH; }
	public String getSdt() { return sdt; }
	public void setSdt(String sdt) { this.sdt = sdt; }
	public String getCccd() { return cccd; }
	public void setCccd(String cccd) { this.cccd = cccd; }
	public String getDiaChi() { return diaChi; }
	public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public boolean isGioiTinh() { return gioiTinh; }
	public void setGioiTinh(boolean gioiTinh) { this.gioiTinh = gioiTinh; }
	public LocalDate getNgaySinh() { return ngaySinh; }
	public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }
	public boolean isLoaiKH() { return loaiKH; }
	public void setLoaiKH(boolean loaiKH) { this.loaiKH = loaiKH; }
}
