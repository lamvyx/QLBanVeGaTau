package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import controller.ThongKeController;

public class KhachHangThongKePage extends ThongKeBaoCaoBasePage {
	private static final long serialVersionUID = 1L;
	private final ThongKeController thongKeController = new ThongKeController();

	public KhachHangThongKePage() {
		super("Thống kê khách hàng");
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
			spec("Khách mới trong ngày", "8", "03/04/2026", MAU_CHINH),
			spec("Khách quay lại", "5", "62%", new Color(34, 197, 94)),
			spec("Điểm tích lũy phát sinh", "260", "điểm", new Color(245, 158, 11)),
			spec("Tỉ lệ hài lòng", "98%", "khảo sát", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Khách mới theo khung giờ", new BarChartPanel(
			new long[] { 1L, 1L, 2L, 3L, 2L, 4L, 3L, 2L },
			new String[] { "6h", "8h", "10h", "12h", "14h", "16h", "18h", "20h" },
			new Color(139, 92, 246), 4L)));
		charts.add(createChartCard("Điểm tích lũy phát sinh", new LineChartPanel(
			new int[] { 12, 18, 35, 22, 28, 41, 37, 29 },
			new String[] { "6h", "8h", "10h", "12h", "14h", "16h", "18h", "20h" },
			new Color(245, 158, 11))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createMonthPanel() {
		int totalCustomers = thongKeController.getTotalCustomers();
		Map<String, Integer> newCustomersByMonth = thongKeController.getNewCustomersByMonth(2026);
		int currentMonth = java.time.LocalDate.now().getMonthValue();
		int newCustomersThisMonth = newCustomersByMonth.getOrDefault("T" + currentMonth, 0);
		
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng khách hàng", String.format("%, d", totalCustomers), "đang hoạt động", MAU_CHINH),
			spec("Khách hàng mới", String.format("%, d", newCustomersThisMonth), "tháng này", new Color(34, 197, 94)),
			spec("Tổng điểm tích lũy", "---", "điểm", new Color(245, 158, 11)),
			spec("KH có điểm", "---", "55%", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		
		int[] monthValues = new int[12];
		String[] monthLabels = new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" };
		int maxMonthValue = 0;
		for (int i = 1; i <= 12; i++) {
			String key = "T" + i;
			monthValues[i - 1] = newCustomersByMonth.getOrDefault(key, 0);
			if (monthValues[i - 1] > maxMonthValue) maxMonthValue = monthValues[i - 1];
		}
		if (maxMonthValue == 0) maxMonthValue = 120;
		
		charts.add(createChartCard("Khách hàng mới theo tháng", new BarChartPanel(
			convertToLongArray(monthValues), monthLabels, new Color(139, 92, 246), maxMonthValue)));
		charts.add(createChartCard("Top khách hàng (điểm tích lũy)", new RankingPanel(
			new String[] { "Phạm Quốc Hùng", "Trần Văn Bình", "Hoàng Minh Tuấn", "Đặng Văn Nam", "Nguyễn Thị Lan" },
			new int[] { 3200, 2100, 1250, 950, 800 }, new Color(245, 158, 11))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createQuarterPanel() {
		Map<String, Integer> newCustomersByMonth = thongKeController.getNewCustomersByMonth(2026);
		int q1 = newCustomersByMonth.getOrDefault("T1", 0) + newCustomersByMonth.getOrDefault("T2", 0) + newCustomersByMonth.getOrDefault("T3", 0);
		int q2 = newCustomersByMonth.getOrDefault("T4", 0) + newCustomersByMonth.getOrDefault("T5", 0) + newCustomersByMonth.getOrDefault("T6", 0);
		int q3 = newCustomersByMonth.getOrDefault("T7", 0) + newCustomersByMonth.getOrDefault("T8", 0) + newCustomersByMonth.getOrDefault("T9", 0);
		int q4 = newCustomersByMonth.getOrDefault("T10", 0) + newCustomersByMonth.getOrDefault("T11", 0) + newCustomersByMonth.getOrDefault("T12", 0);
		
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Khách mới quý", String.format("%, d", q2), "Q2/2026", MAU_CHINH),
			spec("Khách quay lại", "---", "60%", new Color(34, 197, 94)),
			spec("Điểm phát sinh", "---", "điểm", new Color(245, 158, 11)),
			spec("Tỉ lệ giữ chân", "---", "trung bình", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		int maxQuarter = Math.max(Math.max(q1, q2), Math.max(q3, q4));
		charts.add(createChartCard("Khách mới theo quý", new BarChartPanel(
			new long[] { q1, q2, q3, q4 },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(139, 92, 246), maxQuarter > 0 ? maxQuarter : 280L)));
		charts.add(createChartCard("Điểm tích lũy theo quý", new LineChartPanel(
			new int[] { 540, 680, 720, 920 },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(245, 158, 11))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createYearPanel() {
		int totalCustomers = thongKeController.getTotalCustomers();
		Map<String, Integer> newCustomersByMonth = thongKeController.getNewCustomersByMonth(2026);
		int totalNewCustomers = newCustomersByMonth.values().stream().mapToInt(Integer::intValue).sum();
		
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng khách hàng", String.format("%, d", totalCustomers), "Năm 2026", MAU_CHINH),
			spec("Khách mới năm", String.format("%, d", totalNewCustomers), "56%", new Color(34, 197, 94)),
			spec("Tổng điểm tích lũy", "---", "điểm", new Color(245, 158, 11)),
			spec("KH có điểm", "---", "55%", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		List<Object[]> topKH = thongKeController.getTopCustomers(5);
		String[] labels = new String[5];
		int[] values = new int[5];
		for (int i = 0; i < 5; i++) {
			if (i < topKH.size()) {
				labels[i] = (String) topKH.get(i)[0];
				values[i] = (int) ((Double) topKH.get(i)[1] / 1000); // Scale to 'k' units or points
			} else {
				labels[i] = "---";
				values[i] = 0;
			}
		}

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		
		int[] monthValues = new int[12];
		String[] monthLabels = new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" };
		int maxMonthValue = 0;
		for (int i = 1; i <= 12; i++) {
			String key = "T" + i;
			monthValues[i - 1] = newCustomersByMonth.getOrDefault(key, 0);
			if (monthValues[i - 1] > maxMonthValue) maxMonthValue = monthValues[i - 1];
		}
		if (maxMonthValue == 0) maxMonthValue = 120;
		
		charts.add(createChartCard("Khách hàng mới theo tháng", new BarChartPanel(
			convertToLongArray(monthValues), monthLabels, new Color(139, 92, 246), maxMonthValue)));
		charts.add(createChartCard("Top khách hàng (Doanh thu kđ)", new RankingPanel(labels, values, new Color(245, 158, 11))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private long[] convertToLongArray(int[] arr) {
		long[] result = new long[arr.length];
		for (int i = 0; i < arr.length; i++) {
			result[i] = arr[i];
		}
		return result;
	}

	private static final class RankingPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final String[] labels;
		private final int[] values;
		private final Color color;

		private RankingPanel(String[] labels, int[] values, Color color) {
			this.labels = labels;
			this.values = values;
			this.color = color;
			setOpaque(false);
		}

		@Override
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
			g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
			int width = getWidth();
			int height = getHeight();
			int top = 18;
			int rowHeight = (height - 36) / labels.length;
			int max = 1;
			for (int value : values) {
				if (value > max) {
					max = value;
				}
			}
			for (int i = 0; i < labels.length; i++) {
				int y = top + i * rowHeight;
				g2.setColor(new Color(95, 108, 125));
				g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
				g2.drawString((i + 1) + ".", 10, y + 18);
				g2.drawString(labels[i], 32, y + 18);
				g2.setColor(new Color(230, 236, 244));
				g2.fillRoundRect(32, y + 28, width - 54, 8, 8, 8);
				g2.setColor(color);
				int barWidth = (int) ((values[i] * 1.0 / max) * (width - 54));
				g2.fillRoundRect(32, y + 28, barWidth, 8, 8, 8);
				g2.setColor(new Color(245, 124, 0));
				g2.drawString(values[i] + " điểm", width - 94, y + 18);
			}
			g2.dispose();
		}
	}
}
