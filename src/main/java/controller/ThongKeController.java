package controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import service.ThongKeService;
import service.ThongKeService.ThongKeSoLuongVe;

public class ThongKeController {
	private final ThongKeService thongKeService = new ThongKeService();

	public ThongKeSoLuongVe thongKeSoLuongVe() {
		return thongKeService.thongKeSoLuongVe();
	}

	public BigDecimal thongKeDoanhThu() {
		return thongKeService.thongKeDoanhThu();
	}

	public BigDecimal getRevenueByPeriod(String period) {
		return thongKeService.getRevenueByPeriod(period);
	}

	public int getSoldTicketsByPeriod(String period) {
		return thongKeService.getSoldTicketsByPeriod(period);
	}

	public Map<String, Integer> getSoldTicketsByMonth(int year) {
		return thongKeService.getSoldTicketsByMonth(year);
	}

	public int getSoldTicketsByQuarter(int year, int quarter) {
		return thongKeService.getSoldTicketsByQuarter(year, quarter);
	}

	public Map<Integer, Long> getHourlyRevenue() {
		return thongKeService.getHourlyRevenue();
	}

	public Map<Integer, Integer> getHourlyTicketSales() {
		return thongKeService.getHourlyTicketSales();
	}

	public Map<String, Integer> getTripStatusCounts() {
		return thongKeService.getTripStatusCounts();
	}

	public Map<Integer, Integer> getTripCountByHour() {
		return thongKeService.getTripCountByHour();
	}

	public Map<String, Integer> getTripCountByMonth(int year) {
		return thongKeService.getTripCountByMonth(year);
	}

	public Map<String, Integer> getTripCountByQuarter(int year) {
		return thongKeService.getTripCountByQuarter(year);
	}

	public Map<String, Double> getTripOccupancyByMonth(int year) {
		return thongKeService.getTripOccupancyByMonth(year);
	}

	public Map<String, Double> getTripOccupancyByQuarter(int year) {
		return thongKeService.getTripOccupancyByQuarter(year);
	}

	public Map<String, Long> getRevenueChartData(int days) {
		return thongKeService.getRevenueChartData(days);
	}

	public List<Object[]> getTopCustomers(int limit) {
		return thongKeService.getTopCustomers(limit);
	}

	public List<Object[]> getTripOccupancy() {
		return thongKeService.getTripOccupancy();
	}

	public Map<String, Integer> getTicketsByCarriageType() {
		return thongKeService.getTicketsByCarriageType();
	}

	public Map<String, Integer> getNewCustomersByMonth(int year) {
		return thongKeService.getNewCustomersByMonth(year);
	}

	public int getTotalCustomers() {
		return thongKeService.getTotalCustomers();
	}

	public Map<String, Integer> getTripCountByRoute() {
		return thongKeService.getTripCountByRoute();
	}

	public Map<String, Long> getRevenueByMonth(int year) {
		return thongKeService.getRevenueByMonth(year);
	}

	public Map<String, Long> getRevenueByQuarter(int year) {
		return thongKeService.getRevenueByQuarter(year);
	}

	public int getTotalTrips() {
		return thongKeService.getTotalTrips();
	}
}
