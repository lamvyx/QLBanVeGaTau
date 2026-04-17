package service;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiService {
	private final KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();

	public List<KhuyenMai> timKiemKhuyenMai(String maKM, String tenKM) {
		if (maKM != null && !maKM.trim().isEmpty()) {
			KhuyenMai km = khuyenMaiDAO.timKhuyenMaiTheoMa(maKM.trim());
			return km == null ? List.of() : List.of(km);
		}
		List<KhuyenMai> all = khuyenMaiDAO.layTatCaKhuyenMai();
		if (tenKM == null || tenKM.trim().isEmpty()) {
			return all;
		}
		String keyword = tenKM.trim().toLowerCase();
		List<KhuyenMai> filtered = new ArrayList<>();
		for (KhuyenMai km : all) {
			if (km.getTenKM() != null && km.getTenKM().toLowerCase().contains(keyword)) {
				filtered.add(km);
			}
		}
		return filtered;
	}

	public KetQuaXuLy capNhatKhuyenMai(String maKM, String tenKM, BigDecimal tyLeKM, LocalDate ngayBD, LocalDate ngayKT) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maKM == null || maKM.isBlank()) {
			ketQua.thongBao = "Mã khuyến mãi không hợp lệ";
			return ketQua;
		}
		if (tenKM == null || tenKM.isBlank()) {
			ketQua.thongBao = "Tên khuyến mãi không được để trống";
			return ketQua;
		}
		if (tyLeKM == null || tyLeKM.compareTo(BigDecimal.ZERO) < 0 || tyLeKM.compareTo(new BigDecimal("100")) > 0) {
			ketQua.thongBao = "Tỷ lệ khuyến mãi phải từ 0 đến 100";
			return ketQua;
		}
		if (ngayBD == null || ngayKT == null) {
			ketQua.thongBao = "Ngày bắt đầu/kết thúc không hợp lệ";
			return ketQua;
		}
		if (ngayKT.isBefore(ngayBD)) {
			ketQua.thongBao = "Ngày kết thúc phải >= ngày bắt đầu";
			return ketQua;
		}

		KhuyenMai current = khuyenMaiDAO.timKhuyenMaiTheoMa(maKM.trim());
		if (current == null) {
			ketQua.thongBao = "Không tìm thấy khuyến mãi";
			return ketQua;
		}

		current.setTenKM(tenKM.trim());
		current.setTyLeKM(tyLeKM);
		current.setNgayBD(ngayBD);
		current.setNgayKT(ngayKT);

		boolean ok = khuyenMaiDAO.capNhatKhuyenMai(current);
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maKM.trim();
		ketQua.thongBao = ok ? "Cập nhật khuyến mãi thành công" : "Không thể cập nhật khuyến mãi";
		return ketQua;
	}

	public KetQuaXuLy xoaKhuyenMai(String maKM) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maKM == null || maKM.isBlank()) {
			ketQua.thongBao = "Mã khuyến mãi không hợp lệ";
			return ketQua;
		}
		boolean ok = khuyenMaiDAO.xoaKhuyenMai(maKM.trim());
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maKM.trim();
		ketQua.thongBao = ok ? "Xóa khuyến mãi thành công" : "Không thể xóa khuyến mãi";
		return ketQua;
	}

	public static class KetQuaXuLy {
		public boolean thanhCong;
		public String thongBao;
		public String maThamChieu;
	}
}
