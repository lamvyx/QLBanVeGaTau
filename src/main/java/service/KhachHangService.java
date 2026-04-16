package service;

import dao.KhachHang_DAO;
import entity.KhachHang;
import java.util.List;

public class KhachHangService {
	private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();

	public KetQuaXuLy themKhachHang(String tenKH, String sdt, String cccd, String email, String diaChi,
			boolean gioiTinh, boolean loaiKH) {
		KetQuaXuLy ketQua = new KetQuaXuLy();

		if (tenKH == null || tenKH.trim().isEmpty()) {
			ketQua.thongBao = "Tên khách hàng không được để trống";
			return ketQua;
		}
		if (sdt == null || sdt.trim().isEmpty()) {
			ketQua.thongBao = "Số điện thoại không được để trống";
			return ketQua;
		}
		if (cccd == null || cccd.trim().isEmpty()) {
			ketQua.thongBao = "CCCD/Passport không được để trống";
			return ketQua;
		}
		if (email != null && !email.isBlank() && !email.contains("@")) {
			ketQua.thongBao = "Email không hợp lệ";
			return ketQua;
		}
		if (khachHangDAO.kiemTraCCCDTonTai(cccd.trim())) {
			ketQua.thongBao = "CCCD/Passport đã tồn tại";
			return ketQua;
		}

		String maKH = khachHangDAO.layMaKHTiepTheo();
		KhachHang khachHang = new KhachHang(maKH, tenKH.trim(), sdt.trim(), cccd.trim(), diaChi,
				email, gioiTinh, null, loaiKH);

		boolean thanhCong = khachHangDAO.themKhachHang(khachHang);
		ketQua.thanhCong = thanhCong;
		ketQua.maThamChieu = maKH;
		ketQua.thongBao = thanhCong ? "Thêm khách hàng thành công" : "Không thể thêm khách hàng";
		return ketQua;
	}

	public KetQuaXuLy capNhatKhachHang(String maKH, String tenKH, String sdt, String cccd, String email,
			String diaChi, boolean gioiTinh, boolean loaiKH) {
		KetQuaXuLy ketQua = new KetQuaXuLy();

		KhachHang khachHang = khachHangDAO.timKhachHangTheoMa(maKH);
		if (khachHang == null) {
			ketQua.thongBao = "Khách hàng không tồn tại";
			return ketQua;
		}

		if (tenKH == null || tenKH.trim().isEmpty()) {
			ketQua.thongBao = "Tên khách hàng không được để trống";
			return ketQua;
		}

		if (email != null && !email.isBlank() && !email.contains("@")) {
			ketQua.thongBao = "Email không hợp lệ";
			return ketQua;
		}

		if (cccd != null && !cccd.equals(khachHang.getCccd()) && khachHangDAO.kiemTraCCCDTonTai(cccd)) {
			ketQua.thongBao = "CCCD/Passport đã tồn tại";
			return ketQua;
		}

		khachHang.setTenKH(tenKH.trim());
		khachHang.setSdt(sdt != null ? sdt.trim() : null);
		khachHang.setCccd(cccd != null ? cccd.trim() : null);
		khachHang.setEmail(email != null ? email.trim() : null);
		khachHang.setDiaChi(diaChi);
		khachHang.setGioiTinh(gioiTinh);
		khachHang.setLoaiKH(loaiKH);

		boolean thanhCong = khachHangDAO.capNhatKhachHang(khachHang);
		ketQua.thanhCong = thanhCong;
		ketQua.maThamChieu = maKH;
		ketQua.thongBao = thanhCong ? "Cập nhật khách hàng thành công" : "Không thể cập nhật khách hàng";
		return ketQua;
	}

	public List<KhachHang> timKiemKhachHang(String maKH, String tenKH) {
		if (maKH != null && !maKH.trim().isEmpty()) {
			KhachHang kh = khachHangDAO.timKhachHangTheoMa(maKH.trim());
			return kh != null ? List.of(kh) : List.of();
		}

		if (tenKH != null && !tenKH.trim().isEmpty()) {
			return khachHangDAO.timKhachHangTheoTen(tenKH.trim());
		}

		return khachHangDAO.layTatCaKhachHang();
	}

	public List<KhachHang> layTatCaKhachHang() {
		return khachHangDAO.layTatCaKhachHang();
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
