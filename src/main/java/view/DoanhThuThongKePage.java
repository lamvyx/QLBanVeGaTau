package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class DoanhThuThongKePage extends ThongKeBaoCaoBasePage {
	private static final long serialVersionUID = 1L;

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
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Doanh thu hôm nay", money(98600000L), "Ngày 03/04/2026", MAU_CHINH),
			 spec("Số vé bán", "1.284", "vé", new Color(34, 197, 94)),
			 spec("Giá trị TB/vé", money(76800L), "mỗi vé", new Color(245, 158, 11)),
			 spec("Tăng trưởng so với hôm qua", "+8.4%", "đang tăng", new Color(139, 92, 246))
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
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Tổng doanh thu tháng", money(20320000000L), "Năm 2026", MAU_CHINH),
			 spec("Trung bình/ngày", money(677333333L), "30 ngày", new Color(34, 197, 94)),
			 spec("Tổng vé bán ra", "23.820", "vé", new Color(245, 158, 11)),
			 spec("Tháng cao nhất", money(2350000000L), "Tháng 12", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Doanh thu theo tháng (2026)", new BarChartPanel(
			new long[] { 1250000000L, 930000000L, 1580000000L, 1320000000L, 1440000000L, 1890000000L,
				2120000000L, 2070000000L, 1740000000L, 1600000000L, 1980000000L, 2350000000L },
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			MAU_CHINH, 2400000000L)));
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
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Quý hiện tại", money(5620000000L), "Q2/2026", MAU_CHINH),
			 spec("Doanh thu TB/quý", money(4870000000L), "4 quý", new Color(34, 197, 94)),
			 spec("Tăng trưởng quý", "+12.5%", "so với quý trước", new Color(245, 158, 11)),
			 spec("Đóng góp cao nhất", "Quý 4", money(7140000000L), new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Doanh thu theo quý", new BarChartPanel(
			new long[] { 4200000000L, 5600000000L, 4870000000L, 7140000000L },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, MAU_CHINH, 7500000000L)));
		charts.add(createChartCard("Vé bán theo quý", new BarChartPanel(
			new long[] { 6120L, 7340L, 6890L, 9470L },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(34, 197, 94), 10000L)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createYearPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			 spec("Tổng doanh thu năm", money(20320000000L), "Năm 2026", MAU_CHINH),
			 spec("Tăng trưởng năm", "+18.2%", "so với 2025", new Color(34, 197, 94)),
			 spec("Tổng vé bán", "23.820", "vé", new Color(245, 158, 11)),
			 spec("Tháng cao nhất", "Tháng 12", money(2350000000L), new Color(139, 92, 246))
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
