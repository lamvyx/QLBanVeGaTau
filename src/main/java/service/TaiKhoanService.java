package service;

import dao.TaiKhoan_DAO;
import entity.TaiKhoan;

public class TaiKhoanService {
	private final TaiKhoan_DAO taiKhoanDAO = new TaiKhoan_DAO();

	public KetQuaDangNhap dangNhap(String tenDangNhap, String matKhau) {
		KetQuaDangNhap ketQua = new KetQuaDangNhap();

		if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
			ketQua.thongBao = "Tên đăng nhập không được để trống";
			return ketQua;
		}

		if (matKhau == null || matKhau.trim().isEmpty()) {
			ketQua.thongBao = "Mật khẩu không được để trống";
			return ketQua;
		}

		TaiKhoan taiKhoan = taiKhoanDAO.timTaiKhoanDangNhap(tenDangNhap.trim(), matKhau.trim());
		if (taiKhoan == null) {
			ketQua.thongBao = "Sai tên đăng nhập hoặc mật khẩu";
			return ketQua;
		}

		PhienDangNhapService.batDauPhien(taiKhoan);
		ketQua.thanhCong = true;
		ketQua.thongBao = "Đăng nhập thành công";
		ketQua.taiKhoan = taiKhoan;
		return ketQua;
	}

	public void dangXuat() {
		PhienDangNhapService.ketThucPhien();
	}

	public TaiKhoan layTaiKhoanDangNhap() {
		return PhienDangNhapService.layTaiKhoanDangNhap();
	}

	public String layEmailTheoTaiKhoan(String tenDangNhap) {
		if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
			return null;
		}
		return taiKhoanDAO.layEmailTheoTaiKhoan(tenDangNhap.trim());
	}

	public KetQuaXuLy datLaiMatKhau(String tenDangNhap, String matKhauMoi, String xacNhan) {
		KetQuaXuLy ketQua = new KetQuaXuLy();

		if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
			ketQua.thongBao = "Tên đăng nhập không hợp lệ";
			return ketQua;
		}

		if (matKhauMoi == null || matKhauMoi.trim().length() < 6) {
			ketQua.thongBao = "Mật khẩu cần tối thiểu 6 ký tự";
			return ketQua;
		}

		if (!matKhauMoi.equals(xacNhan)) {
			ketQua.thongBao = "Xác nhận mật khẩu không khớp";
			return ketQua;
		}

		boolean capNhat = taiKhoanDAO.doiMatKhau(tenDangNhap.trim(), matKhauMoi.trim());
		ketQua.thanhCong = capNhat;
		ketQua.thongBao = capNhat ? "Đổi mật khẩu thành công" : "Không thể cập nhật mật khẩu";
		return ketQua;
	}

	public static class KetQuaDangNhap {
		public boolean thanhCong;
		public String thongBao;
		public TaiKhoan taiKhoan;

		public KetQuaDangNhap() {
			this.thanhCong = false;
			this.thongBao = "";
			this.taiKhoan = null;
		}
	}

	public static class KetQuaXuLy {
		public boolean thanhCong;
		public String thongBao;

		public KetQuaXuLy() {
			this.thanhCong = false;
			this.thongBao = "";
		}
	}
}
