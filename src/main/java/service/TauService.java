package service;

import dao.Tau_DAO;
import entity.Tau;
import java.util.ArrayList;
import java.util.List;

public class TauService {
	private final Tau_DAO tauDAO = new Tau_DAO();

	public List<Tau> timKiemTau(String maTau, String tenTau) {
		if (maTau != null && !maTau.trim().isEmpty()) {
			Tau tau = tauDAO.timTheoMa(maTau.trim());
			return tau == null ? List.of() : List.of(tau);
		}
		List<Tau> all = tauDAO.layTatCaTau();
		if (tenTau == null || tenTau.trim().isEmpty()) {
			return all;
		}
		String keyword = tenTau.trim().toLowerCase();
		List<Tau> filtered = new ArrayList<>();
		for (Tau tau : all) {
			if (tau.getTenTau() != null && tau.getTenTau().toLowerCase().contains(keyword)) {
				filtered.add(tau);
			}
		}
		return filtered;
	}

	public KetQuaXuLy themTau(String tenTau, int soLuongToa) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (tenTau == null || tenTau.trim().isEmpty()) {
			ketQua.thongBao = "Tên tàu không được để trống";
			return ketQua;
		}
		if (soLuongToa <= 0) {
			ketQua.thongBao = "Số lượng toa phải lớn hơn 0";
			return ketQua;
		}

		String maTau = tauDAO.layMaTauTiepTheo();
		boolean ok = tauDAO.themTau(new Tau(maTau, tenTau.trim(), soLuongToa));
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maTau;
		ketQua.thongBao = ok ? "Thêm tàu thành công" : "Không thể thêm tàu";
		return ketQua;
	}

	public KetQuaXuLy capNhatTau(String maTau, String tenTau, int soLuongToa) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maTau == null || maTau.trim().isEmpty()) {
			ketQua.thongBao = "Mã tàu không được để trống";
			return ketQua;
		}
		Tau current = tauDAO.timTheoMa(maTau.trim());
		if (current == null) {
			ketQua.thongBao = "Không tìm thấy tàu";
			return ketQua;
		}
		current.setTenTau(tenTau == null ? current.getTenTau() : tenTau.trim());
		if (soLuongToa > 0) {
			current.setSoLuongToa(soLuongToa);
		}
		boolean ok = tauDAO.capNhatTau(current);
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = current.getMaTau();
		ketQua.thongBao = ok ? "Cập nhật tàu thành công" : "Không thể cập nhật tàu";
		return ketQua;
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
