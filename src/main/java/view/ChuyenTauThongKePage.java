package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class ChuyenTauThongKePage extends ThongKeBaoCaoBasePage {
	private static final long serialVersionUID = 1L;

	public ChuyenTauThongKePage() {
		super("Thống kê chuyến tàu");
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
			spec("Chuyến hôm nay", "18", "03/04/2026", MAU_CHINH),
			spec("Đúng giờ", "16", "89%", new Color(34, 197, 94)),
			spec("Trễ", "2", "11%", new Color(245, 158, 11)),
			spec("Hủy", "0", "0%", new Color(239, 68, 68))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Tình trạng chuyến", new TicketStatusPanel()));
		charts.add(createChartCard("Chuyến theo khung giờ", new HorizontalBarChartPanel(
			new int[] { 2, 3, 4, 3, 2, 2, 1, 1 },
			new String[] { "6h", "8h", "10h", "12h", "14h", "16h", "18h", "20h" },
			MAU_CHINH, 5)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createMonthPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng chuyến tàu", "128", "tháng này", MAU_CHINH),
			spec("Đúng giờ", "94.2%", "tỷ lệ", new Color(34, 197, 94)),
			spec("Bị hủy", "23", "chuyến", new Color(239, 68, 68)),
			spec("Hệ số lấp đầy", "78.5%", "trung bình", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Tỷ lệ đúng giờ", new LineChartPanel(
			new int[] { 92, 93, 95, 94, 96, 95, 94, 93, 95, 96, 94, 95 },
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(34, 197, 94))));
		charts.add(createChartCard("Chuyến theo tuyến", new HorizontalBarChartPanel(
			new int[] { 72, 28, 19, 67, 11 },
			new String[] { "SGN-HAN", "SGN-DNA", "SGN-NTR", "HAN-SGN", "SGN-PLK" },
			MAU_CHINH, 80)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createQuarterPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Chuyến quý", "385", "Q2/2026", MAU_CHINH),
			spec("Đúng giờ", "91.7%", "tỷ lệ", new Color(34, 197, 94)),
			spec("Bị hủy", "7", "chuyến", new Color(239, 68, 68)),
			spec("Lấp đầy TB", "76.2%", "trung bình", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Chuyến theo quý", new BarChartPanel(
			new long[] { 96L, 121L, 83L, 152L },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, MAU_CHINH, 160L)));
		charts.add(createChartCard("Lấp đầy theo quý", new LineChartPanel(
			new int[] { 71, 74, 77, 79 },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(245, 158, 11))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createYearPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng chuyến tàu", "1.247", "Năm 2026", MAU_CHINH),
			spec("Đúng giờ", "94.2%", "tỷ lệ", new Color(34, 197, 94)),
			spec("Bị hủy", "23", "chuyến", new Color(239, 68, 68)),
			spec("Hệ số lấp đầy", "78.5%", "trung bình", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Doanh thu theo tuyến đường", new BarChartPanel(
			new long[] { 72L, 28L, 19L, 67L, 11L },
			new String[] { "SGN-HAN", "SGN-DNA", "SGN-NTR", "HAN-SGN", "SGN-PLK" }, MAU_CHINH, 80L)));
		charts.add(createChartCard("Lấp đầy theo tháng", new LineChartPanel(
			new int[] { 72, 74, 76, 78, 79, 81, 80, 77, 76, 75, 82, 87 },
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(34, 197, 94))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}
}
