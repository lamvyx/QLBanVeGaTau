package dao;

import connectDB.Database;
import entity.TaiKhoan;

public class TaiKhoan_DAO {
	private final Database database = Database.getInstance();

	public TaiKhoan timTaiKhoanDangNhap(String tenDangNhap, String matKhau) {
		TaiKhoan taiKhoan = database.findByUsername(tenDangNhap);
		if (taiKhoan == null) {
			return null;
		}
		return taiKhoan.getMatKhau().equals(matKhau) ? taiKhoan : null;
	}

	public boolean dangNhap(String tenDangNhap, String matKhau) {
		return timTaiKhoanDangNhap(tenDangNhap, matKhau) != null;
	}

	public String layEmailTheoTaiKhoan(String tenDangNhap) {
		TaiKhoan taiKhoan = database.findByUsername(tenDangNhap);
		return taiKhoan == null ? null : taiKhoan.getEmail();
	}

	public boolean doiMatKhau(String tenDangNhap, String matKhauMoi) {
		return database.updatePassword(tenDangNhap, matKhauMoi);
	}

	public boolean taoTaiKhoan(TaiKhoan taiKhoan) {
		return database.createTaiKhoan(taiKhoan);
	}

	public boolean tonTaiTaiKhoan(String tenDangNhap) {
		return database.findByUsername(tenDangNhap) != null;
	}
}
