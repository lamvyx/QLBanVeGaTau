package service;

import dao.DonDoiTra_DAO;
import dao.VeTau_DAO;
import entity.DonDoiTra;
import entity.VeTau;
import java.time.LocalDateTime;
import java.util.List;

public class DoiTraService {
	private final DonDoiTra_DAO donDoiTraDAO = new DonDoiTra_DAO();
	private final VeTau_DAO veTauDAO = new VeTau_DAO();

	public KetQuaXuLy taoDonDoiTra(String maVeCu, String maVeMoi, String loaiDon, String ghiChu) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maVeCu == null || maVeCu.trim().isEmpty()) {
			ketQua.thongBao = "Mã vé cũ không được để trống";
			return ketQua;
		}
		String loai = loaiDon == null ? "" : loaiDon.trim().toUpperCase();
		if (!"DOI".equals(loai) && !"TRA".equals(loai)) {
			ketQua.thongBao = "Loại đơn chỉ nhận DOI hoặc TRA";
			return ketQua;
		}

		VeTau veCu = veTauDAO.timTheoMaVe(maVeCu.trim());
		if (veCu == null) {
			ketQua.thongBao = "Vé cũ không tồn tại";
			return ketQua;
		}
		if ("DOI".equals(loai)) {
			if (maVeMoi == null || maVeMoi.trim().isEmpty()) {
				ketQua.thongBao = "Đổi vé cần mã vé mới";
				return ketQua;
			}
			VeTau veMoi = veTauDAO.timTheoMaVe(maVeMoi.trim());
			if (veMoi == null) {
				ketQua.thongBao = "Vé mới không tồn tại";
				return ketQua;
			}
		}

		String maDon = donDoiTraDAO.layMaDonTiepTheo();
		DonDoiTra don = new DonDoiTra(maDon, maVeCu.trim(), maVeMoi == null ? null : maVeMoi.trim(), loai,
				"CHO_XAC_NHAN", ghiChu == null ? null : ghiChu.trim(), LocalDateTime.now(), null);
		boolean thanhCong = donDoiTraDAO.taoDon(don);
		ketQua.thanhCong = thanhCong;
		ketQua.maThamChieu = maDon;
		ketQua.thongBao = thanhCong ? "Tạo đơn đổi/trả thành công" : "Không thể tạo đơn đổi/trả";
		return ketQua;
	}

	public KetQuaXuLy xacNhanDonDoiTra(String maDon) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maDon == null || maDon.trim().isEmpty()) {
			ketQua.thongBao = "Mã đơn không được để trống";
			return ketQua;
		}
		DonDoiTra don = donDoiTraDAO.timTheoMaDon(maDon.trim());
		if (don == null) {
			ketQua.thongBao = "Không tìm thấy đơn đổi/trả";
			return ketQua;
		}
		if (!"CHO_XAC_NHAN".equalsIgnoreCase(don.getTrangThai())) {
			ketQua.thongBao = "Đơn đã được xử lý trước đó";
			return ketQua;
		}

		boolean updateDon = donDoiTraDAO.xacNhanDon(don.getMaDon(), "DA_XAC_NHAN");
		if (!updateDon) {
			ketQua.thongBao = "Không thể xác nhận đơn";
			return ketQua;
		}

		boolean updateVeCu = veTauDAO.capNhatTrangThaiVe(don.getMaVeCu(), "KHONG_HIEU_LUC");
		boolean updateVeMoi = true;
		if ("DOI".equalsIgnoreCase(don.getLoaiDon()) && don.getMaVeMoi() != null) {
			updateVeMoi = veTauDAO.capNhatTrangThaiVe(don.getMaVeMoi(), "DA_BAN");
		}

		ketQua.thanhCong = updateVeCu && updateVeMoi;
		ketQua.maThamChieu = don.getMaDon();
		ketQua.thongBao = ketQua.thanhCong ? "Xác nhận đơn đổi/trả thành công" : "Đơn đã xác nhận nhưng cập nhật trạng thái vé chưa hoàn tất";
		return ketQua;
	}

	public DonDoiTra timDonTheoMa(String maDon) {
		if (maDon == null || maDon.trim().isEmpty()) {
			return null;
		}
		return donDoiTraDAO.timTheoMaDon(maDon.trim());
	}

	public VeTau timVeTheoMa(String maVe) {
		if (maVe == null || maVe.trim().isEmpty()) {
			return null;
		}
		return veTauDAO.timTheoMaVe(maVe.trim());
	}

	public List<VeTau> layVeTheoChuyenTau(String maCT) {
		if (maCT == null || maCT.trim().isEmpty()) {
			return List.of();
		}
		return veTauDAO.layVeTheoChuyenTau(maCT.trim());
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
