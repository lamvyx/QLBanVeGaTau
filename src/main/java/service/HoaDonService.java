package service;

import dao.HoaDon_DAO;
import entity.ChiTietHoaDonItem;
import entity.HoaDon;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDonService {
	private final HoaDon_DAO hoaDonDAO = new HoaDon_DAO();

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
