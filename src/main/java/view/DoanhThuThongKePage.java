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
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Tổng doanh thu", money(doanhThu.longValue()), "Hôm nay", MAU_CHINH),
			 spec("Số vé bán", "---", "Trong ngày", new Color(34, 197, 94)),
			 spec("Giá trị TB/vé", "---", "mỗi vé", new Color(245, 158, 11)),
			 spec("Tình trạng", "Hoạt động", "đang cập nhật", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Doanh thu theo giờ", new BarChartPanel(
			new long[] { 4000000L, 7000000L, 12000000L, 15000000L, 11000000L, 18000000L, 9000000L, 16000000L },
			new String[] { "6h", "8h", "10h", "12h", "14h", "16h", "18h", "20h" },
			MAU_CHINH, 20000000L)));
		charts.add(createChartCard("Vé bán theo khung giờ", new LineChartPanel(
			new int[] { 35, 72, 110, 126, 98, 145, 90, 138 },
			new String[] { "6h", "8h", "10h", "12h", "14h", "16h", "18h", "20h" },
			new Color(34, 197, 94))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createMonthPanel() {
		BigDecimal doanhThu = thongKeController.getRevenueByPeriod("month");
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Tổng doanh thu tháng", money(doanhThu.longValue()), "Tháng này", MAU_CHINH),
			 spec("Trung bình/ngày", money(doanhThu.longValue() / 30), "Ước tính", new Color(34, 197, 94)),
			 spec("Tổng vé bán ra", "---", "vé", new Color(245, 158, 11)),
			 spec("Trạng thái", "Ổn định", "Hệ thống", new Color(139, 92, 246))
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
		charts.add(createChartCard("Số vé bán theo tháng", new LineChartPanel(
			new int[] { 1410, 980, 1920, 1450, 1580, 2140, 2360, 2300, 2030, 1900, 2250, 2620 },
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
		
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Quý hiện tại", money(q2), "Q2/2026", MAU_CHINH),
			 spec("Doanh thu TB/quý", money(avg), "4 quý", new Color(34, 197, 94)),
			 spec("Tăng trưởng quý", "+12.5%", "so với quý trước", new Color(245, 158, 11)),
			 spec("Đóng góp cao nhất", "Quý " + (maxQuarter == q1 ? "1" : maxQuarter == q2 ? "2" : maxQuarter == q3 ? "3" : "4"), money(maxQuarter), new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Doanh thu theo quý", new BarChartPanel(
			new long[] { q1, q2, q3, q4 },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, MAU_CHINH, maxQuarter > 0 ? maxQuarter : 7500000000L)));
		charts.add(createChartCard("Vé bán theo quý", new BarChartPanel(
			new long[] { 6120L, 7340L, 6890L, 9470L },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(34, 197, 94), 10000L)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createYearPanel() {
		BigDecimal doanhThu = thongKeController.getRevenueByPeriod("year");
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Tổng doanh thu năm", money(doanhThu.longValue()), "Năm hiện tại", MAU_CHINH),
			 spec("Tăng trưởng", "---", "so với năm trước", new Color(34, 197, 94)),
			 spec("Tổng vé bán", "---", "vé", new Color(245, 158, 11)),
			 spec("Trạng thái", "Ổn định", "Toàn hệ thống", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Xu hướng doanh thu năm", new BarChartPanel(
			new long[] { 20L, 22L, 24L, 25L, 27L, 28L, 30L, 29L, 31L, 33L, 35L, 38L },
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			MAU_CHINH, 40L)));
		charts.add(createChartCard("Số vé bán theo năm", new LineChartPanel(
			new int[] { 1200, 1290, 1450, 1520, 1680, 1820, 1910, 1850, 1980, 2100, 2250, 2380 },
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(34, 197, 94))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}
}
