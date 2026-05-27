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

	public int getSoldTicketsByPeriod(String period) {
		return thongKeDAO.getSoldTicketsByPeriod(period);
	}

	public Map<String, Integer> getSoldTicketsByMonth(int year) {
		return thongKeDAO.getSoldTicketsByMonth(year);
	}

	public int getSoldTicketsByQuarter(int year, int quarter) {
		return thongKeDAO.getSoldTicketsByQuarter(year, quarter);
	}

	public Map<Integer, Long> getHourlyRevenue() {
		return thongKeDAO.getHourlyRevenue();
	}

	public Map<Integer, Integer> getHourlyTicketSales() {
		return thongKeDAO.getHourlyTicketSales();
	}

	public Map<String, Integer> getTripStatusCounts() {
		return thongKeDAO.getTripStatusCounts();
	}

	public Map<Integer, Integer> getTripCountByHour() {
		return thongKeDAO.getTripCountByHour();
	}

	public Map<String, Integer> getTripCountByMonth(int year) {
		return thongKeDAO.getTripCountByMonth(year);
	}

	public Map<String, Integer> getTripCountByQuarter(int year) {
		return thongKeDAO.getTripCountByQuarter(year);
	}

	public Map<String, Double> getTripOccupancyByMonth(int year) {
		return thongKeDAO.getTripOccupancyByMonth(year);
	}

	public Map<String, Double> getTripOccupancyByQuarter(int year) {
		return thongKeDAO.getTripOccupancyByQuarter(year);
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

	public Map<String, Integer> getTicketsByCarriageType() {
		return thongKeDAO.getTicketsByCarriageType();
	}

	public Map<String, Integer> getNewCustomersByMonth(int year) {
		return thongKeDAO.getNewCustomersByMonth(year);
	}

	public int getTotalCustomers() {
		return thongKeDAO.getTotalCustomers();
	}

	public Map<String, Integer> getTripCountByRoute() {
		return thongKeDAO.getTripCountByRoute();
	}

	public Map<String, Long> getRevenueByMonth(int year) {
		return thongKeDAO.getRevenueByMonth(year);
	}

	public Map<String, Long> getRevenueByQuarter(int year) {
		return thongKeDAO.getRevenueByQuarter(year);
	}

	public int getTotalTrips() {
		return thongKeDAO.getTotalTrips();
	}

	public static class ThongKeSoLuongVe {
		public int tongSoVe;
		public int soVeDaBan;
		public int soVeConTrong;
		public int soVeKhongHieuLuc;
	}
}
