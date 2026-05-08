package service;

import dao.HoaDon_DAO;
import dao.VeTau_DAO;
import dao.ThongKe_DAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ThongKeService {
	private final VeTau_DAO veTauDAO = new VeTau_DAO();
	private final HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
	private final ThongKe_DAO thongKeDAO = new ThongKe_DAO();

	public ThongKeSoLuongVe thongKeSoLuongVe() {
		ThongKeSoLuongVe ketQua = new ThongKeSoLuongVe();
		Map<String, Integer> counts = thongKeDAO.getTicketStatusCounts();
		ketQua.soVeDaBan = counts.getOrDefault("DA_THANH_TOAN", 0) + counts.getOrDefault("DA_BAN", 0);
		ketQua.soVeConTrong = counts.getOrDefault("CON_TRONG", 0);
		ketQua.soVeKhongHieuLuc = counts.getOrDefault("DA_HUY", 0);
		ketQua.tongSoVe = ketQua.soVeDaBan + ketQua.soVeConTrong + ketQua.soVeKhongHieuLuc;
		return ketQua;
	}

	public BigDecimal thongKeDoanhThu() {
		return thongKeDAO.getRevenueByPeriod("day");
	}

	public BigDecimal getRevenueByPeriod(String period) {
		return thongKeDAO.getRevenueByPeriod(period);
	}

	public Map<String, Long> getRevenueChartData(int days) {
		return thongKeDAO.getDailyRevenueChart(days);
	}

	public List<Object[]> getTopCustomers(int limit) {
		return thongKeDAO.getTopCustomers(limit);
	}

	public List<Object[]> getTripOccupancy() {
		return thongKeDAO.getTripOccupancy();
	}

	public static class ThongKeSoLuongVe {
		public int tongSoVe;
		public int soVeDaBan;
		public int soVeConTrong;
		public int soVeKhongHieuLuc;
	}
}
