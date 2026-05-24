package service;

import dao.KhachHang_DAO;
import dao.PhieuDatVe_DAO;
import dao.VeTau_DAO;
import entity.ChiTietPhieuDat;
import entity.KhachHang;
import entity.PhieuDatVe;
import entity.PhieuDatVeInfo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PhieuDatVeService {
	private final PhieuDatVe_DAO phieuDatVeDAO = new PhieuDatVe_DAO();
	private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
	private final VeTau_DAO veTauDAO = new VeTau_DAO();

	public KetQuaTaoPhieuDat taoPhieuDat(String maNV, String maKH, String maCT, String maToa, List<String> viTriGheList,
			BigDecimal giaVe, String ghiChu) {
		KetQuaTaoPhieuDat ketQua = new KetQuaTaoPhieuDat();
		if (maKH == null || maKH.isBlank()) {
			ketQua.thongBao = "Vui lòng chọn khách hàng";
			return ketQua;
		}
		if (maCT == null || maCT.isBlank() || maToa == null || maToa.isBlank()) {
			ketQua.thongBao = "Vui lòng chọn chuyến tàu và toa tàu";
			return ketQua;
		}
		if (viTriGheList == null || viTriGheList.isEmpty()) {
			ketQua.thongBao = "Vui lòng chọn ít nhất 1 ghế";
			return ketQua;
		}
		if (giaVe == null || giaVe.compareTo(BigDecimal.ZERO) < 0) {
			ketQua.thongBao = "Giá vé không hợp lệ";
			return ketQua;
		}

		KhachHang kh = khachHangDAO.timKhachHangTheoMa(maKH.trim());
		if (kh == null) {
			ketQua.thongBao = "Không tìm thấy khách hàng";
			return ketQua;
		}

		Set<String> gheHopLe = new LinkedHashSet<>();
		for (String ghe : viTriGheList) {
			if (ghe != null && !ghe.isBlank()) {
				gheHopLe.add(ghe.trim().toUpperCase());
			}
		}
		if (gheHopLe.isEmpty()) {
			ketQua.thongBao = "Danh sách ghế không hợp lệ";
			return ketQua;
		}

		Set<String> daDat = veTauDAO.layGheDaDat(maCT.trim(), maToa.trim());
		for (String ghe : gheHopLe) {
			if (daDat.contains(ghe)) {
				ketQua.thongBao = "Ghế " + ghe + " đã được giữ hoặc đã bán";
				return ketQua;
			}
		}

		String maPhieu = phieuDatVeDAO.layMaPhieuTiepTheo();
		PhieuDatVe phieu = new PhieuDatVe(maPhieu, maKH.trim(), maNV == null ? null : maNV.trim(), LocalDate.now(),
				LocalDate.now().plusDays(1), true);

		List<ChiTietPhieuDat> chiTietList = new ArrayList<>();
		for (String ghe : gheHopLe) {
			chiTietList.add(new ChiTietPhieuDat(maPhieu, maCT.trim(), maToa.trim(), ghe,
					giaVe.doubleValue(), ghiChu));
		}

		boolean thanhCong = phieuDatVeDAO.taoPhieuDat(phieu, chiTietList);
		ketQua.thanhCong = thanhCong;
		ketQua.maPhieu = maPhieu;
		ketQua.phieuDatVeInfo = thanhCong ? new PhieuDatVeInfo(phieu, chiTietList) : null;
		ketQua.tongTien = thanhCong ? giaVe.multiply(BigDecimal.valueOf(gheHopLe.size())) : BigDecimal.ZERO;
		ketQua.thongBao = thanhCong ? "Lưu phiếu đặt thành công" : "Không thể lưu phiếu đặt";
		return ketQua;
	}

	public PhieuDatVeInfo layPhieuDatTheoMa(String maPhieu) {
		return phieuDatVeDAO.timPhieuDatTheoMa(maPhieu);
	}

	public List<PhieuDatVeInfo> layTatCaPhieuDat() {
		return phieuDatVeDAO.layTatCaPhieuDat();
	}

	public boolean capNhatTrangThai(String maPhieu, boolean trangThai) {
		return phieuDatVeDAO.capNhatTrangThai(maPhieu, trangThai);
	}

	public static class KetQuaTaoPhieuDat {
		public boolean thanhCong;
		public String thongBao;
		public String maPhieu;
		public BigDecimal tongTien;
		public PhieuDatVeInfo phieuDatVeInfo;

		public KetQuaTaoPhieuDat() {
			this.thanhCong = false;
			this.thongBao = "";
			this.maPhieu = "";
			this.tongTien = BigDecimal.ZERO;
			this.phieuDatVeInfo = null;
		}
	}
}