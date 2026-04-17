package service;

import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;
import java.time.LocalDate;
import java.util.List;

public class NhanVienService {
	private final NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
	private final TaiKhoan_DAO taiKhoanDAO = new TaiKhoan_DAO();

	public KetQuaXuLy themNhanVien(String tenNV, String username, String matKhau, String sdt, String email,
			String chucVu, boolean gioiTinh, LocalDate ngaySinh, LocalDate ngayVaoLam) {
		KetQuaXuLy ketQua = new KetQuaXuLy();

		if (tenNV == null || tenNV.trim().isEmpty()) {
			ketQua.thongBao = "Tên nhân viên không được để trống";
			return ketQua;
		}
		if (username == null || username.trim().isEmpty()) {
			ketQua.thongBao = "Tên tài khoản không được để trống";
			return ketQua;
		}
		if (matKhau == null || matKhau.trim().length() < 6) {
			ketQua.thongBao = "Mật khẩu nhân viên phải có tối thiểu 6 ký tự";
			return ketQua;
		}
		if (email == null || email.trim().isEmpty() || !email.contains("@")) {
			ketQua.thongBao = "Email không hợp lệ";
			return ketQua;
		}
		if (nhanVienDAO.kiemTraUsernameTonTai(username.trim()) || taiKhoanDAO.tonTaiTaiKhoan(username.trim())) {
			ketQua.thongBao = "Tên tài khoản đã tồn tại";
			return ketQua;
		}
		if (sdt != null && !sdt.isBlank() && nhanVienDAO.kiemTraSDTTonTai(sdt.trim())) {
			ketQua.thongBao = "Số điện thoại đã tồn tại";
			return ketQua;
		}

		String maNV = nhanVienDAO.layMaNVTiepTheo();
		String vaiTro = xacDinhVaiTroTheoChucVu(chucVu);
		TaiKhoan taiKhoan = new TaiKhoan(username.trim(), matKhau.trim(), email.trim(), tenNV.trim(), vaiTro);

		if (!taiKhoanDAO.taoTaiKhoan(taiKhoan)) {
			ketQua.thongBao = "Không thể tạo tài khoản cho nhân viên";
			return ketQua;
		}

		NhanVien nhanVien = new NhanVien(maNV, tenNV.trim(), sdt != null ? sdt.trim() : null, gioiTinh, ngaySinh,
				ngayVaoLam, chucVu, true, username.trim());

		boolean themNhanVien = nhanVienDAO.themNhanVien(nhanVien, email.trim());
		ketQua.thanhCong = themNhanVien;
		ketQua.maThamChieu = maNV;
		ketQua.thongBao = themNhanVien ? "Thêm nhân viên thành công" : "Không thể tạo hồ sơ nhân viên";
		return ketQua;
	}

	public KetQuaXuLy capNhatNhanVien(String maNV, String tenNV, String sdt, String email, String chucVu,
			boolean gioiTinh, LocalDate ngaySinh, LocalDate ngayVaoLam) {
		KetQuaXuLy ketQua = new KetQuaXuLy();

		NhanVien nhanVien = nhanVienDAO.timNhanVienTheoMa(maNV);
		if (nhanVien == null) {
			ketQua.thongBao = "Nhân viên không tồn tại";
			return ketQua;
		}

		if (tenNV == null || tenNV.trim().isEmpty()) {
			ketQua.thongBao = "Tên nhân viên không được để trống";
			return ketQua;
		}

		if (sdt != null && !sdt.isBlank() && !sdt.equals(nhanVien.getSdt()) && nhanVienDAO.kiemTraSDTTonTai(sdt)) {
			ketQua.thongBao = "Số điện thoại đã tồn tại";
			return ketQua;
		}

		nhanVien.setTenNV(tenNV.trim());
		nhanVien.setSdt(sdt != null ? sdt.trim() : null);
		nhanVien.setChucVu(chucVu);
		nhanVien.setGioiTinh(gioiTinh);
		nhanVien.setNgaySinh(ngaySinh);
		nhanVien.setNgayVaoLam(ngayVaoLam);

		boolean capNhat = nhanVienDAO.capNhatNhanVien(nhanVien, email);
		ketQua.thanhCong = capNhat;
		ketQua.maThamChieu = maNV;
		ketQua.thongBao = capNhat ? "Cập nhật nhân viên thành công" : "Không thể cập nhật nhân viên";
		return ketQua;
	}

	public List<NhanVien> timKiemNhanVien(String maNV, String tenNV) {
		if (maNV != null && !maNV.trim().isEmpty()) {
			NhanVien nv = nhanVienDAO.timNhanVienTheoMa(maNV.trim());
			return nv != null ? List.of(nv) : List.of();
		}

		if (tenNV != null && !tenNV.trim().isEmpty()) {
			return nhanVienDAO.timNhanVienTheoTen(tenNV.trim());
		}

		return nhanVienDAO.layTatCaNhanVien();
	}

	public List<NhanVien> layTatCaNhanVien() {
		return nhanVienDAO.layTatCaNhanVien();
	}

	public String layEmailTheoUsername(String username) {
		return nhanVienDAO.timEmailTheoUsername(username);
	}

	public KetQuaXuLy xoaNhanVien(String maNV) {
		KetQuaXuLy ketQua = new KetQuaXuLy();

		if (maNV == null || maNV.isBlank()) {
			ketQua.thongBao = "Mã nhân viên không hợp lệ";
			return ketQua;
		}

		NhanVien nhanVien = nhanVienDAO.timNhanVienTheoMa(maNV.trim());
		if (nhanVien == null) {
			ketQua.thongBao = "Nhân viên không tồn tại";
			return ketQua;
		}

		boolean thanhCong = nhanVienDAO.xoaNhanVien(maNV.trim());
		ketQua.thanhCong = thanhCong;
		ketQua.maThamChieu = maNV.trim();
		ketQua.thongBao = thanhCong ? "Xóa nhân viên thành công" : "Không thể xóa nhân viên";
		return ketQua;
	}

	private String xacDinhVaiTroTheoChucVu(String chucVu) {
		if (chucVu != null && chucVu.toLowerCase().contains("quản")) {
			return "ADMIN";
		}
		return "NHAN_VIEN";
	}

	public static class KetQuaXuLy {
		public boolean thanhCong;
		public String thongBao;
		public String maThamChieu;

		public KetQuaXuLy() {
			this.thanhCong = false;
			this.thongBao = "";
			this.maThamChieu = "";
		}
	}
}
