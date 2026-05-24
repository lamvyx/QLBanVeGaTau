package entity;

import java.time.LocalDate;

public class NhanVien {
	private String maNV;
	private String tenNV;
	private String sdt;
	private boolean gioiTinh;
	private LocalDate ngaySinh;
	private LocalDate ngayVaoLam;
	private String chucVu;
	private boolean trangThai;
	private String username;

	public NhanVien() {
	}

	public NhanVien(String maNV, String tenNV, String sdt, boolean gioiTinh, LocalDate ngaySinh,
			LocalDate ngayVaoLam, String chucVu, boolean trangThai, String username) {
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.sdt = sdt;
		this.gioiTinh = gioiTinh;
		this.ngaySinh = ngaySinh;
		this.ngayVaoLam = ngayVaoLam;
		this.chucVu = chucVu;
		this.trangThai = trangThai;
		this.username = username;
	}

	public String getMaNV() { return maNV; }
	public void setMaNV(String maNV) { this.maNV = maNV; }
	public String getTenNV() { return tenNV; }
	public void setTenNV(String tenNV) { this.tenNV = tenNV; }
	public String getSdt() { return sdt; }
	public void setSdt(String sdt) { this.sdt = sdt; }
	public boolean isGioiTinh() { return gioiTinh; }
	public void setGioiTinh(boolean gioiTinh) { this.gioiTinh = gioiTinh; }
	public LocalDate getNgaySinh() { return ngaySinh; }
	public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }
	public LocalDate getNgayVaoLam() { return ngayVaoLam; }
	public void setNgayVaoLam(LocalDate ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }
	public String getChucVu() { return chucVu; }
	public void setChucVu(String chucVu) { this.chucVu = chucVu; }
	public boolean isTrangThai() { return trangThai; }
	public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
}
