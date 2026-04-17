package service;

import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.VeTau_DAO;
import entity.ChiTietHoaDonItem;
import entity.HoaDon;
import entity.KhachHang;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class HoaDonService {
	private final HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
	private final VeTau_DAO veTauDAO = new VeTau_DAO();
	private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();

	public KetQuaLapHoaDon lapHoaDon(String maNV, String maKH, String maKM, List<ChiTietHoaDonItem> items) {
		KetQuaLapHoaDon ketQua = new KetQuaLapHoaDon();
		if (maNV == null || maNV.trim().isEmpty()) {
			ketQua.thongBao = "Mã nhân viên không được để trống";
			return ketQua;
		}
		if (maKH == null || maKH.trim().isEmpty()) {
			ketQua.thongBao = "Mã khách hàng không được để trống";
			return ketQua;
		}
		if (items == null || items.isEmpty()) {
			ketQua.thongBao = "Hóa đơn cần ít nhất 1 dòng chi tiết";
			return ketQua;
		}

		String maHD = hoaDonDAO.layMaHoaDonTiepTheo();
		BigDecimal tongTien = BigDecimal.ZERO;
		List<ChiTietHoaDonItem> preparedItems = new ArrayList<>();
		for (ChiTietHoaDonItem item : items) {
			if (item == null || item.getDonGia() == null || item.getSoLuong() <= 0) {
				ketQua.thongBao = "Chi tiết hóa đơn không hợp lệ";
				return ketQua;
			}
			String maCT = hoaDonDAO.layMaChiTietTiepTheo();
			ChiTietHoaDonItem clone = new ChiTietHoaDonItem(maCT, maHD, item.getMaVeTau(), item.getMaDV(),
					item.getSoLuong(), item.getDonGia());
			preparedItems.add(clone);
			tongTien = tongTien.add(item.getDonGia().multiply(BigDecimal.valueOf(item.getSoLuong())));
		}

		HoaDon hoaDon = new HoaDon(maHD, maNV.trim(), maKH.trim(), LocalDateTime.now(), BigDecimal.ZERO, tongTien,
				maKM == null || maKM.trim().isEmpty() ? null : maKM.trim());
		boolean thanhCong = hoaDonDAO.taoHoaDon(hoaDon, preparedItems);

		ketQua.thanhCong = thanhCong;
		ketQua.maHoaDon = maHD;
		ketQua.tongTien = thanhCong ? hoaDonDAO.layTongThanhToanHoaDon(maHD) : BigDecimal.ZERO;
		ketQua.thongBao = thanhCong ? "Lập hóa đơn thành công" : "Không thể lập hóa đơn";
		return ketQua;
	}

	public List<HoaDon> timKiemHoaDon(String tuKhoa) {
		return hoaDonDAO.timKiemHoaDon(tuKhoa == null ? "" : tuKhoa.trim());
	}

	public Set<String> layGheDaDat(String maCT, String maToa) {
		return veTauDAO.layGheDaDat(maCT, maToa);
	}

	public KetQuaLapHoaDon lapHoaDonBanVe(String maNV, String maKH, String maKM, String maCT, String maToa,
			List<String> viTriGheList, BigDecimal giaVe) {
		KetQuaLapHoaDon ketQua = new KetQuaLapHoaDon();
		if (maNV == null || maNV.isBlank()) {
			ketQua.thongBao = "Mã nhân viên không được để trống";
			return ketQua;
		}
		if (maKH == null || maKH.isBlank()) {
			ketQua.thongBao = "Mã khách hàng không được để trống";
			return ketQua;
		}
		if (maCT == null || maCT.isBlank() || maToa == null || maToa.isBlank()) {
			ketQua.thongBao = "Chuyến tàu/toa tàu không hợp lệ";
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

		LinkedHashSet<String> gheHopLe = new LinkedHashSet<>();
		for (String viTri : viTriGheList) {
			if (viTri != null && !viTri.isBlank()) {
				gheHopLe.add(viTri.trim().toUpperCase());
			}
		}
		if (gheHopLe.isEmpty()) {
			ketQua.thongBao = "Danh sách ghế không hợp lệ";
			return ketQua;
		}

		Set<String> daDat = veTauDAO.layGheDaDat(maCT, maToa);
		for (String ghe : gheHopLe) {
			if (daDat.contains(ghe)) {
				ketQua.thongBao = "Ghế " + ghe + " đã có người đặt. Vui lòng chọn ghế khác.";
				return ketQua;
			}
		}

		KhachHang khachHang = khachHangDAO.timKhachHangTheoMa(maKH.trim());
		if (khachHang == null) {
			ketQua.thongBao = "Không tìm thấy khách hàng";
			return ketQua;
		}

		String tenHanhKhach = khachHang.getTenKH() == null || khachHang.getTenKH().isBlank()
				? "Hành khách"
				: khachHang.getTenKH().trim();
		String cccd = khachHang.getCccd() == null || khachHang.getCccd().isBlank()
				? "AUTO" + maKH.trim().replaceAll("\\s+", "")
				: khachHang.getCccd().trim();
		if (cccd.length() > 20) {
			cccd = cccd.substring(0, 20);
		}
		LocalDate ngaySinh = khachHang.getNgaySinh() == null ? LocalDate.of(1990, 1, 1) : khachHang.getNgaySinh();

		String maHD = hoaDonDAO.layMaHoaDonTiepTheo();
		HoaDon hoaDon = new HoaDon(maHD, maNV.trim(), maKH.trim(), LocalDateTime.now(), BigDecimal.ZERO,
				giaVe.multiply(BigDecimal.valueOf(gheHopLe.size())),
				maKM == null || maKM.isBlank() ? null : maKM.trim());

		boolean thanhCong = hoaDonDAO.taoHoaDonBanVe(hoaDon, maCT.trim(), maToa.trim(), new ArrayList<>(gheHopLe),
				giaVe, tenHanhKhach, cccd, ngaySinh);

		ketQua.thanhCong = thanhCong;
		ketQua.maHoaDon = maHD;
		ketQua.tongTien = thanhCong ? hoaDonDAO.layTongThanhToanHoaDon(maHD) : BigDecimal.ZERO;
		ketQua.thongBao = thanhCong ? "Đặt vé và lập hóa đơn thành công" : "Không thể đặt vé/lập hóa đơn";
		return ketQua;
	}

	public BigDecimal layTongDoanhThu() {
		return hoaDonDAO.layTongDoanhThu();
	}

	public static class KetQuaLapHoaDon {
		public boolean thanhCong;
		public String thongBao;
		public String maHoaDon;
		public BigDecimal tongTien;

		public KetQuaLapHoaDon() {
			this.thanhCong = false;
			this.thongBao = "";
			this.maHoaDon = "";
			this.tongTien = BigDecimal.ZERO;
		}
	}
}
