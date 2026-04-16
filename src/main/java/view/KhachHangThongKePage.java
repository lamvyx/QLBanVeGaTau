package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class KhachHangThongKePage extends ThongKeBaoCaoBasePage {
	private static final long serialVersionUID = 1L;

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
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng khách hàng", "1.850", "đang hoạt động", MAU_CHINH),
			spec("Khách hàng mới", "286", "tháng này", new Color(34, 197, 94)),
			spec("Tổng điểm tích lũy", "9.500", "điểm", new Color(245, 158, 11)),
			spec("KH có điểm", "1.024", "55%", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Khách hàng mới theo tháng", new BarChartPanel(
			new long[] { 44L, 38L, 62L, 70L, 55L, 82L, 91L, 88L, 76L, 69L, 80L, 108L },
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(139, 92, 246), 120L)));
		charts.add(createChartCard("Top khách hàng (điểm tích lũy)", new RankingPanel(
			new String[] { "Phạm Quốc Hùng", "Trần Văn Bình", "Hoàng Minh Tuấn", "Đặng Văn Nam", "Nguyễn Thị Lan" },
			new int[] { 3200, 2100, 1250, 950, 800 }, new Color(245, 158, 11))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createQuarterPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Khách mới quý", "684", "Q2/2026", MAU_CHINH),
			spec("Khách quay lại", "412", "60%", new Color(34, 197, 94)),
			spec("Điểm phát sinh", "2.860", "điểm", new Color(245, 158, 11)),
			spec("Tỉ lệ giữ chân", "71%", "trung bình", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Khách mới theo quý", new BarChartPanel(
			new long[] { 162L, 210L, 184L, 255L },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(139, 92, 246), 280L)));
		charts.add(createChartCard("Điểm tích lũy theo quý", new LineChartPanel(
			new int[] { 540, 680, 720, 920 },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(245, 158, 11))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createYearPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng khách hàng", "1.850", "Năm 2026", MAU_CHINH),
			spec("Khách mới năm", "1.024", "56%", new Color(34, 197, 94)),
			spec("Tổng điểm tích lũy", "9.500", "điểm", new Color(245, 158, 11)),
			spec("KH có điểm", "1.024", "55%", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Khách hàng mới theo tháng", new BarChartPanel(
			new long[] { 44L, 38L, 62L, 70L, 55L, 82L, 91L, 88L, 76L, 69L, 80L, 108L },
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(139, 92, 246), 120L)));
		charts.add(createChartCard("Top khách hàng", new RankingPanel(
			new String[] { "Phạm Quốc Hùng", "Trần Văn Bình", "Hoàng Minh Tuấn", "Đặng Văn Nam", "Nguyễn Thị Lan" },
			new int[] { 3200, 2100, 1250, 950, 800 }, new Color(245, 158, 11))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
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
