package service;

import dao.DichVu_DAO;
import entity.DichVu;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DichVuService {
	private final DichVu_DAO dichVuDAO = new DichVu_DAO();

	public List<DichVu> timKiemDichVu(String maDV, String tenDV) {
		if (maDV != null && !maDV.trim().isEmpty()) {
			DichVu dv = dichVuDAO.timDichVuTheoMa(maDV.trim());
			return dv == null ? List.of() : List.of(dv);
		}

		List<DichVu> all = dichVuDAO.layTatCaDichVu();
		if (tenDV == null || tenDV.trim().isEmpty()) {
			return all;
		}
		String keyword = tenDV.trim().toLowerCase();
		List<DichVu> filtered = new ArrayList<>();
		for (DichVu dv : all) {
			if (dv.getTenDV() != null && dv.getTenDV().toLowerCase().contains(keyword)) {
				filtered.add(dv);
			}
		}
		return filtered;
	}

	public KetQuaXuLy capNhatDichVu(String maDV, String tenDV, BigDecimal giaDV, boolean trangThai) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maDV == null || maDV.isBlank()) {
			ketQua.thongBao = "Mã dịch vụ không hợp lệ";
			return ketQua;
		}
		if (tenDV == null || tenDV.isBlank()) {
			ketQua.thongBao = "Tên dịch vụ không được để trống";
			return ketQua;
		}
		if (giaDV == null || giaDV.compareTo(BigDecimal.ZERO) < 0) {
			ketQua.thongBao = "Giá dịch vụ phải >= 0";
			return ketQua;
		}

		DichVu current = dichVuDAO.timDichVuTheoMa(maDV.trim());
		if (current == null) {
			ketQua.thongBao = "Không tìm thấy dịch vụ";
			return ketQua;
		}

		current.setTenDV(tenDV.trim());
		current.setGiaDV(giaDV);
		current.setTrangThai(trangThai);
		boolean ok = dichVuDAO.capNhatDichVu(current);
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maDV.trim();
		ketQua.thongBao = ok ? "Cập nhật dịch vụ thành công" : "Không thể cập nhật dịch vụ";
		return ketQua;
	}

	public KetQuaXuLy xoaDichVu(String maDV) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maDV == null || maDV.isBlank()) {
			ketQua.thongBao = "Mã dịch vụ không hợp lệ";
			return ketQua;
		}
		boolean ok = dichVuDAO.xoaDichVu(maDV.trim());
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maDV.trim();
		ketQua.thongBao = ok ? "Xóa dịch vụ thành công" : "Không thể xóa dịch vụ (có thể đang phát sinh hóa đơn liên quan)";
		return ketQua;
	}

	public static class KetQuaXuLy {
		public boolean thanhCong;
		public String thongBao;
		public String maThamChieu;
	}
}
