package entity;

public class TaiKhoan {
	private String tenDangNhap;
	private String matKhau;
	private String email;
	private String hoTen;
	private String vaiTro;

	public TaiKhoan(String tenDangNhap, String matKhau, String email, String hoTen, String vaiTro) {
		this.tenDangNhap = tenDangNhap;
		this.matKhau = matKhau;
		this.email = email;
		this.hoTen = hoTen;
		this.vaiTro = vaiTro;
	}

	public String getTenDangNhap() {
		return tenDangNhap;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public String getEmail() {
		return email;
	}

	public String getHoTen() {
		return hoTen;
	}

	public String getVaiTro() {
		return vaiTro;
	}
}
