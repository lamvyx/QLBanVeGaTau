package service;

import dao.ChuyenTauCrud_DAO;
import entity.ChuyenTau;
import java.time.LocalDateTime;
import java.util.List;

public class ChuyenTauService {
	private final ChuyenTauCrud_DAO chuyenTauDAO = new ChuyenTauCrud_DAO();

	public List<ChuyenTau> timKiemChuyenTau(String tuKhoa) {
		return chuyenTauDAO.timKiemChuyenTau(tuKhoa == null ? "" : tuKhoa.trim());
	}

	public KetQuaXuLy themChuyenTau(String maTau, String maTuyenTau, LocalDateTime ngayGioKhoiHanh) {
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
		String maCT = chuyenTauDAO.layMaChuyenTauTiepTheo();
		ChuyenTau ct = new ChuyenTau(maCT, tg, tg, true, maTau.trim(), maTuyenTau.trim());
		boolean ok = chuyenTauDAO.themChuyenTau(ct);
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maCT;
		ketQua.thongBao = ok ? "Thêm chuyến tàu thành công" : "Không thể thêm chuyến tàu";
		return ketQua;
	}

	public KetQuaXuLy capNhatChuyenTau(String maCT, String maTau, String maTuyenTau, LocalDateTime ngayGioKhoiHanh,
			boolean trangThai) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maCT == null || maCT.trim().isEmpty()) {
			ketQua.thongBao = "Mã chuyến tàu không được để trống";
			return ketQua;
		}
		LocalDateTime tg = ngayGioKhoiHanh == null ? LocalDateTime.now() : ngayGioKhoiHanh;
		ChuyenTau ct = new ChuyenTau(maCT.trim(), tg, tg, trangThai, maTau, maTuyenTau);
		boolean ok = chuyenTauDAO.capNhatChuyenTau(ct);
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maCT.trim();
		ketQua.thongBao = ok ? "Cập nhật chuyến tàu thành công" : "Không thể cập nhật chuyến tàu";
		return ketQua;
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
