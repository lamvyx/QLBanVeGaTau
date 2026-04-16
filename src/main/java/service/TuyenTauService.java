package service;

import dao.TuyenTau_DAO;
import entity.TuyenTau;
import java.util.ArrayList;
import java.util.List;

public class TuyenTauService {
	private final TuyenTau_DAO tuyenTauDAO = new TuyenTau_DAO();

	public List<TuyenTau> timKiemTuyenTau(String maTT, String gaDi, String gaDen) {
		List<TuyenTau> all = tuyenTauDAO.layTatCaTuyenTau();
		if (maTT != null && !maTT.trim().isEmpty()) {
			TuyenTau tt = tuyenTauDAO.timTheoMa(maTT.trim());
			return tt == null ? List.of() : List.of(tt);
		}
		String di = gaDi == null ? "" : gaDi.trim().toLowerCase();
		String den = gaDen == null ? "" : gaDen.trim().toLowerCase();
		if (di.isEmpty() && den.isEmpty()) {
			return all;
		}
		List<TuyenTau> filtered = new ArrayList<>();
		for (TuyenTau tt : all) {
			boolean matchDi = di.isEmpty() || (tt.getMaGaDi() != null && tt.getMaGaDi().toLowerCase().contains(di));
			boolean matchDen = den.isEmpty() || (tt.getMaGaDen() != null && tt.getMaGaDen().toLowerCase().contains(den));
			if (matchDi && matchDen) {
				filtered.add(tt);
			}
		}
		return filtered;
	}

	public KetQuaXuLy themTuyenTau(String maGaDi, String maGaDen, double khoangCach) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maGaDi == null || maGaDi.trim().isEmpty() || maGaDen == null || maGaDen.trim().isEmpty()) {
			ketQua.thongBao = "Mã ga đi và mã ga đến không được để trống";
			return ketQua;
		}
		if (khoangCach <= 0) {
			ketQua.thongBao = "Khoảng cách phải lớn hơn 0";
			return ketQua;
		}
		String maTT = tuyenTauDAO.layMaTuyenTauTiepTheo();
		boolean ok = tuyenTauDAO.themTuyenTau(new TuyenTau(maTT, maGaDi.trim(), maGaDen.trim(), khoangCach));
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maTT;
		ketQua.thongBao = ok ? "Thêm tuyến tàu thành công" : "Không thể thêm tuyến tàu";
		return ketQua;
	}

	public KetQuaXuLy capNhatTuyenTau(String maTT, String maGaDi, String maGaDen, double khoangCach) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maTT == null || maTT.trim().isEmpty()) {
			ketQua.thongBao = "Mã tuyến tàu không được để trống";
			return ketQua;
		}
		TuyenTau current = tuyenTauDAO.timTheoMa(maTT.trim());
		if (current == null) {
			ketQua.thongBao = "Không tìm thấy tuyến tàu";
			return ketQua;
		}
		if (maGaDi != null && !maGaDi.trim().isEmpty()) {
			current.setMaGaDi(maGaDi.trim());
		}
		if (maGaDen != null && !maGaDen.trim().isEmpty()) {
			current.setMaGaDen(maGaDen.trim());
		}
		if (khoangCach > 0) {
			current.setKhoangCach(khoangCach);
		}
		boolean ok = tuyenTauDAO.capNhatTuyenTau(current);
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = current.getMaTT();
		ketQua.thongBao = ok ? "Cập nhật tuyến tàu thành công" : "Không thể cập nhật tuyến tàu";
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
