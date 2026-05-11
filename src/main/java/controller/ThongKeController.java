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
