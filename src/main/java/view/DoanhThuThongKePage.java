package view;

import controller.ThongKeController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.Map;
import javax.swing.JPanel;

public class DoanhThuThongKePage extends ThongKeBaoCaoBasePage {
	private static final long serialVersionUID = 1L;
	private final ThongKeController thongKeController = new ThongKeController();

	public DoanhThuThongKePage() {
		super("Thống kê doanh thu");
	}

	@Override
	protected JPanel createPeriodPanel(String periodKey) {
		switch (periodKey) {
		case "day":
			return createDayPanel();
		case "month":
			return createMonthPanel();
		case "quarter":
			return createQuarterPanel();
		case "year":
		default:
			return createYearPanel();
		}
	}

	private JPanel createDayPanel() {
		BigDecimal doanhThu = thongKeController.getRevenueByPeriod("day");
		int veBan = thongKeController.getSoldTicketsByPeriod("day");
		long tbVe = veBan == 0 ? 0 : doanhThu.longValue() / veBan;
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Tổng doanh thu", money(doanhThu.longValue()), "Hôm nay", MAU_CHINH),
			 spec("Số vé bán", String.valueOf(veBan), "Trong ngày", new Color(34, 197, 94)),
			 spec("Giá trị TB/vé", money(tbVe), "mỗi vé", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);

		Map<Integer, Long> hourlyRev = thongKeController.getHourlyRevenue();
		long[] revValues = new long[8];
		int[] hours = { 6, 8, 10, 12, 14, 16, 18, 20 };
		long maxHourlyRev = 0;
		for (int i = 0; i < 8; i++) {
			revValues[i] = hourlyRev.getOrDefault(hours[i], 0L);
			if (revValues[i] > maxHourlyRev) maxHourlyRev = revValues[i];
		}
		if (maxHourlyRev == 0) maxHourlyRev = 20000000L;

		charts.add(createChartCard("Doanh thu theo giờ", new BarChartPanel(
			revValues,
			new String[] { "6h", "8h", "10h", "12h", "14h", "16h", "18h", "20h" },
			MAU_CHINH, maxHourlyRev)));

		Map<Integer, Integer> hourlyTickets = thongKeController.getHourlyTicketSales();
		int[] ticketValues = new int[8];
		for (int i = 0; i < 8; i++) {
			ticketValues[i] = hourlyTickets.getOrDefault(hours[i], 0);
		}

		charts.add(createChartCard("Vé bán theo khung giờ", new LineChartPanel(
			ticketValues,
			new String[] { "6h", "8h", "10h", "12h", "14h", "16h", "18h", "20h" },
			new Color(34, 197, 94))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createMonthPanel() {
		BigDecimal doanhThu = thongKeController.getRevenueByPeriod("month");
		int veBan = thongKeController.getSoldTicketsByPeriod("month");
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Tổng doanh thu tháng", money(doanhThu.longValue()), "Tháng này", MAU_CHINH),
			 spec("Trung bình/ngày", money(doanhThu.longValue() / 30), "Ước tính", new Color(34, 197, 94)),
			 spec("Tổng vé bán ra", String.format("%, d", veBan), "vé", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		
		Map<String, Long> revenueByMonth = thongKeController.getRevenueByMonth(2026);
		long[] monthValues = new long[12];
		String[] monthLabels = new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" };
		long maxMonthValue = 0;
		for (int i = 1; i <= 12; i++) {
			String key = "T" + i;
			monthValues[i - 1] = revenueByMonth.getOrDefault(key, 0L);
			if (monthValues[i - 1] > maxMonthValue) maxMonthValue = monthValues[i - 1];
		}
		if (maxMonthValue == 0) maxMonthValue = 2400000000L;
		
		charts.add(createChartCard("Doanh thu theo tháng (2026)", new BarChartPanel(
			monthValues, monthLabels, MAU_CHINH, maxMonthValue)));

		Map<String, Integer> ticketsByMonth = thongKeController.getSoldTicketsByMonth(2026);
		int[] ticketsValues = new int[12];
		for (int i = 1; i <= 12; i++) {
			ticketsValues[i - 1] = ticketsByMonth.getOrDefault("T" + i, 0);
		}

		charts.add(createChartCard("Số vé bán theo tháng", new LineChartPanel(
			ticketsValues,
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(34, 197, 94))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createQuarterPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		
		Map<String, Long> revenueByQuarter = thongKeController.getRevenueByQuarter(2026);
		long q1 = revenueByQuarter.getOrDefault("Q1", 0L);
		long q2 = revenueByQuarter.getOrDefault("Q2", 0L);
		long q3 = revenueByQuarter.getOrDefault("Q3", 0L);
		long q4 = revenueByQuarter.getOrDefault("Q4", 0L);
		long total = q1 + q2 + q3 + q4;
		long avg = total / 4;
		long maxQuarter = Math.max(Math.max(q1, q2), Math.max(q3, q4));

		int q1Ve = thongKeController.getSoldTicketsByQuarter(2026, 1);
		int q2Ve = thongKeController.getSoldTicketsByQuarter(2026, 2);
		int q3Ve = thongKeController.getSoldTicketsByQuarter(2026, 3);
		int q4Ve = thongKeController.getSoldTicketsByQuarter(2026, 4);
		int totalVe = q1Ve + q2Ve + q3Ve + q4Ve;
		
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Quý hiện tại", money(q2), "Q2/2026", MAU_CHINH),
			 spec("Doanh thu TB/quý", money(avg), "4 quý", new Color(34, 197, 94)),
			 spec("Vé bán quý này", String.format("%, d", q2Ve), "Q2/2026", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Doanh thu theo quý", new BarChartPanel(
			new long[] { q1, q2, q3, q4 },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, MAU_CHINH, maxQuarter > 0 ? maxQuarter : 7500000000L)));
		charts.add(createChartCard("Vé bán theo quý", new BarChartPanel(
			new long[] { q1Ve, q2Ve, q3Ve, q4Ve },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(34, 197, 94), totalVe > 0 ? totalVe : 10000L)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createYearPanel() {
		BigDecimal doanhThu = thongKeController.getRevenueByPeriod("year");
		int veBan = thongKeController.getSoldTicketsByPeriod("year");
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Tổng doanh thu năm", money(doanhThu.longValue()), "Năm hiện tại", MAU_CHINH),
			 spec("Tổng vé bán", String.format("%, d", veBan), "vé", new Color(34, 197, 94)),
			 spec("Giá trị TB/vé", veBan == 0 ? "0 đ" : money(doanhThu.longValue() / veBan), "toàn hệ thống", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);

		Map<String, Long> revenueByMonth = thongKeController.getRevenueByMonth(2026);
		long[] monthValues = new long[12];
		String[] monthLabels = new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" };
		long maxMonthValue = 0;
		for (int i = 1; i <= 12; i++) {
			String key = "T" + i;
			monthValues[i - 1] = revenueByMonth.getOrDefault(key, 0L);
			if (monthValues[i - 1] > maxMonthValue) maxMonthValue = monthValues[i - 1];
		}
		if (maxMonthValue == 0) maxMonthValue = 2400000000L;

		charts.add(createChartCard("Xu hướng doanh thu năm", new BarChartPanel(
			monthValues, monthLabels, MAU_CHINH, maxMonthValue)));

		Map<String, Integer> ticketsByMonth = thongKeController.getSoldTicketsByMonth(2026);
		int[] ticketsValues = new int[12];
		for (int i = 1; i <= 12; i++) {
			ticketsValues[i - 1] = ticketsByMonth.getOrDefault("T" + i, 0);
		}

		charts.add(createChartCard("Số vé bán theo năm", new LineChartPanel(
			ticketsValues,
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(34, 197, 94))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}
}
