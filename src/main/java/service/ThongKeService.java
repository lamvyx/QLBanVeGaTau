package service;

import dao.HoaDon_DAO;
import dao.VeTau_DAO;
import java.math.BigDecimal;

public class ThongKeService {
	private final VeTau_DAO veTauDAO = new VeTau_DAO();
	private final HoaDon_DAO hoaDonDAO = new HoaDon_DAO();

	public ThongKeSoLuongVe thongKeSoLuongVe() {
		ThongKeSoLuongVe ketQua = new ThongKeSoLuongVe();
		ketQua.tongSoVe = veTauDAO.demTongSoVe();
		ketQua.soVeDaBan = veTauDAO.demTheoTrangThai("DA_BAN");
		ketQua.soVeConTrong = veTauDAO.demTheoTrangThai("CON_TRONG");
		ketQua.soVeKhongHieuLuc = veTauDAO.demTheoTrangThai("KHONG_HIEU_LUC");
		return ketQua;
	}

	public BigDecimal thongKeDoanhThu() {
		return hoaDonDAO.layTongDoanhThu();
	}

	public static class ThongKeSoLuongVe {
		public int tongSoVe;
		public int soVeDaBan;
		public int soVeConTrong;
		public int soVeKhongHieuLuc;
	}
}
