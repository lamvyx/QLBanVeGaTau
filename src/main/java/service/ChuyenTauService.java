package service;

import dao.ChuyenTauCrud_DAO;
import entity.ChuyenTau;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChuyenTauService {
	private final ChuyenTauCrud_DAO chuyenTauDAO = new ChuyenTauCrud_DAO();
	private final dao.Tau_DAO tauDAO = new dao.Tau_DAO();

	public List<ChuyenTau> timKiemChuyenTau(String tuKhoa) {
		return chuyenTauDAO.timKiemChuyenTau(tuKhoa == null ? "" : tuKhoa.trim());
	}

	public List<ChuyenTau> traCuuChuyenTau(String gaDi, String gaDen, java.time.LocalDate ngayDi) {
		return chuyenTauDAO.searchChuyenTauByGaAndNgay(gaDi, gaDen, ngayDi);
	}

	public KetQuaXuLy themChuyenTau(String maTau, String maTuyenTau, LocalDateTime ngayGioKhoiHanh) {
		return themChuyenTau(maTau, maTuyenTau, ngayGioKhoiHanh, null);
	}

	public KetQuaXuLy themChuyenTau(String maTau, String maTuyenTau, LocalDateTime ngayGioKhoiHanh,
			List<String> maToaList) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maTau == null || maTau.trim().isEmpty()) {
			ketQua.thongBao = "Mã tàu không được để trống";
			return ketQua;
		}
		if (maTuyenTau == null || maTuyenTau.trim().isEmpty()) {
			ketQua.thongBao = "Mã tuyến tàu không được để trống";
			return ketQua;
		}
		LocalDateTime tg = ngayGioKhoiHanh == null ? LocalDateTime.now() : ngayGioKhoiHanh;
		var tau = tauDAO.timTheoMa(maTau.trim());
		if (tau == null) {
			ketQua.thongBao = "Không tìm thấy đầu tàu";
			return ketQua;
		}
		if (maToaList == null || maToaList.isEmpty()) {
			ketQua.thongBao = "Vui lòng chọn ít nhất 1 toa cho chuyến tàu";
			return ketQua;
		}
		if (maToaList.size() > tau.getSoLuongToa()) {
			ketQua.thongBao = "Số toa vượt quá giới hạn kéo của đầu tàu";
			return ketQua;
		}
		String maCT = chuyenTauDAO.layMaChuyenTauTiepTheo();
		ChuyenTau ct = new ChuyenTau(maCT, tg, tg, true, maTau.trim(), maTuyenTau.trim());
		boolean ok = chuyenTauDAO.themChuyenTau(ct, maToaList);
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maCT;
		ketQua.thongBao = ok ? "Thêm chuyến tàu thành công" : "Không thể thêm chuyến tàu";
		return ketQua;
	}

	public KetQuaXuLy capNhatChuyenTau(String maCT, String maTau, String maTuyenTau, LocalDateTime ngayGioKhoiHanh,
			boolean trangThai) {
		return capNhatChuyenTau(maCT, maTau, maTuyenTau, ngayGioKhoiHanh,
				trangThai ? ChuyenTau.TRANG_THAI_DA_LEN_LICH : ChuyenTau.TRANG_THAI_DA_HOAN_THANH, null);
	}

	public KetQuaXuLy capNhatChuyenTau(String maCT, String maTau, String maTuyenTau, LocalDateTime ngayGioKhoiHanh,
			boolean trangThai, List<String> maToaList) {
		return capNhatChuyenTau(maCT, maTau, maTuyenTau, ngayGioKhoiHanh,
				trangThai ? ChuyenTau.TRANG_THAI_DA_LEN_LICH : ChuyenTau.TRANG_THAI_DA_HOAN_THANH, maToaList);
	}

	public KetQuaXuLy capNhatChuyenTau(String maCT, String maTau, String maTuyenTau, LocalDateTime ngayGioKhoiHanh,
			String trangThai, List<String> maToaList) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maCT == null || maCT.trim().isEmpty()) {
			ketQua.thongBao = "Mã chuyến tàu không được để trống";
			return ketQua;
		}
		if (maTau == null || maTau.trim().isEmpty()) {
			ketQua.thongBao = "Mã tàu không được để trống";
			return ketQua;
		}
		if (maTuyenTau == null || maTuyenTau.trim().isEmpty()) {
			ketQua.thongBao = "Mã tuyến tàu không được để trống";
			return ketQua;
		}
		LocalDateTime tg = ngayGioKhoiHanh == null ? LocalDateTime.now() : ngayGioKhoiHanh;
		ChuyenTau hienTai = timChuyenTauTheoMa(maCT.trim());
		if (hienTai == null) {
			ketQua.thongBao = "Không tìm thấy chuyến tàu";
			return ketQua;
		}
		var tau = tauDAO.timTheoMa(maTau.trim());
		if (tau == null) {
			ketQua.thongBao = "Không tìm thấy đầu tàu";
			return ketQua;
		}
		if (maToaList == null || maToaList.isEmpty()) {
			ketQua.thongBao = "Vui lòng chọn ít nhất 1 toa cho chuyến tàu";
			return ketQua;
		}
		if (maToaList.size() > tau.getSoLuongToa()) {
			ketQua.thongBao = "Số toa vượt quá giới hạn kéo của đầu tàu";
			return ketQua;
		}
		List<String> toaDangGan = new ArrayList<>(layDanhSachMaToaTheoChuyen(maCT.trim()));
		if (hienTai.laDangChay()) {
			if (ChuyenTau.TRANG_THAI_DA_LEN_LICH.equals(trangThai)) {
				ketQua.thongBao = "Chuyến đang chạy không thể chuyển lại về trạng thái đã lên lịch";
				return ketQua;
			}
			boolean thayDoiThongTin = !maTau.trim().equals(hienTai.getMaTau())
					|| !maTuyenTau.trim().equals(hienTai.getMaTuyenTau())
					|| !tg.equals(hienTai.getNgayKhoiHanh())
					|| !toaDangGan.equals(new ArrayList<>(maToaList));
			if (thayDoiThongTin) {
				ketQua.thongBao = "Chuyến đang chạy chỉ được phép cập nhật trạng thái";
				return ketQua;
			}
		}
		if (hienTai.laDaHoanThanh() && !ChuyenTau.TRANG_THAI_DA_HOAN_THANH.equals(trangThai)) {
			ketQua.thongBao = "Chuyến đã hoàn thành không thể chuyển ngược trạng thái";
			return ketQua;
		}
		ChuyenTau ct = new ChuyenTau(maCT.trim(), tg, tg, trangThai, maTau.trim(), maTuyenTau.trim());
		boolean ok = chuyenTauDAO.capNhatChuyenTau(ct, maToaList);
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maCT.trim();
		ketQua.thongBao = ok ? "Cập nhật chuyến tàu thành công" : "Không thể cập nhật chuyến tàu";
		return ketQua;
	}

	public List<String> layDanhSachMaToaTheoChuyen(String maCT) {
		if (maCT == null || maCT.isBlank()) {
			return List.of();
		}
		return chuyenTauDAO.layDanhSachMaToaTheoChuyen(maCT.trim());
	}

	private ChuyenTau timChuyenTauTheoMa(String maCT) {
		List<ChuyenTau> ds = chuyenTauDAO.timKiemChuyenTau(maCT);
		for (ChuyenTau chuyenTau : ds) {
			if (maCT.equals(chuyenTau.getMaCT())) {
				return chuyenTau;
			}
		}
		return null;
	}

	public KetQuaXuLy xoaChuyenTau(String maCT) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maCT == null || maCT.trim().isEmpty()) {
			ketQua.thongBao = "Mã chuyến tàu không hợp lệ";
			return ketQua;
		}

		boolean tonTai = !chuyenTauDAO.timKiemChuyenTau(maCT.trim()).isEmpty();
		if (!tonTai) {
			ketQua.thongBao = "Không tìm thấy chuyến tàu";
			return ketQua;
		}

		boolean ok = chuyenTauDAO.xoaChuyenTau(maCT.trim());
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maCT.trim();
		ketQua.thongBao = ok ? "Xóa chuyến tàu thành công"
				: "Không thể xóa chuyến tàu (có thể đã phát sinh vé liên quan)";
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
