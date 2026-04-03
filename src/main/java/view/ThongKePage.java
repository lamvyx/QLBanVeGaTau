package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ThongKePage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2563EB");
	private static final Color MAU_NEN = Color.decode("#F3F6FB");
	private static final Color MAU_TEXT = Color.decode("#35506B");
	private static final DecimalFormat MONEY = new DecimalFormat("#,###");

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel cardPanel = new JPanel(cardLayout);
	private JComboBox<String> cboDanhMuc;

	public ThongKePage() {
		setLayout(new BorderLayout());
		setBackground(MAU_NEN);
		add(taoHeaderPanel(), BorderLayout.NORTH);
		add(taoBodyPanel(), BorderLayout.CENTER);
	}

	private JPanel taoHeaderPanel() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#DDE5F2")));
		header.setPreferredSize(new Dimension(0, 64));

		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
		left.setOpaque(false);
		JLabel title = new JLabel("Thống kê & Báo cáo");
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		title.setForeground(MAU_TEXT);
		left.add(title);
		header.add(left, BorderLayout.WEST);

		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
		right.setOpaque(false);
		JLabel lblNam = new JLabel("Năm:");
		lblNam.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblNam.setForeground(MAU_TEXT);
		right.add(lblNam);

		JComboBox<String> cboNam = new JComboBox<>(new String[] { "2024", "2025", "2026" });
		cboNam.setSelectedItem("2026");
		cboNam.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboNam.setPreferredSize(new Dimension(72, 28));
		right.add(cboNam);

		JButton btnExcel = new JButton("Xuất Excel");
		btnExcel.setBackground(MAU_CHINH);
		btnExcel.setForeground(Color.WHITE);
		btnExcel.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnExcel.setFocusPainted(false);
		btnExcel.setBorder(new EmptyBorder(6, 12, 6, 12));
		btnExcel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnExcel.addActionListener(e -> {});
		right.add(btnExcel);
		header.add(right, BorderLayout.EAST);

		return header;
	}

	private JPanel taoBodyPanel() {
		JPanel body = new JPanel(new BorderLayout());
		body.setOpaque(false);
		body.setBorder(new EmptyBorder(12, 12, 12, 12));

		body.add(taoDropdownBar(), BorderLayout.NORTH);
		body.add(taoCardArea(), BorderLayout.CENTER);

		return body;
	}

	private JPanel taoDropdownBar() {
		JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		bar.setOpaque(false);
		bar.setBorder(new EmptyBorder(0, 0, 10, 0));

		JLabel label = new JLabel("Chọn báo cáo:");
		label.setFont(new Font("Segoe UI", Font.BOLD, 13));
		label.setForeground(MAU_TEXT);
		label.setBorder(new EmptyBorder(0, 0, 0, 8));
		bar.add(label);

		cboDanhMuc = new JComboBox<>(new String[] { "Doanh thu", "Vé", "Khách hàng", "Chuyến tàu" });
		cboDanhMuc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboDanhMuc.setPreferredSize(new Dimension(180, 30));
		cboDanhMuc.addActionListener(e -> {
			String selection = (String) cboDanhMuc.getSelectedItem();
			if (selection == null) {
				return;
			}
			switch (selection) {
			case "Doanh thu":
				showCard("doanhthu");
				break;
			case "Vé":
				showCard("ve");
				break;
			case "Khách hàng":
				showCard("khachhang");
				break;
			case "Chuyến tàu":
				showCard("chuyentau");
				break;
			default:
				showCard("doanhthu");
				break;
			}
		});
		bar.add(cboDanhMuc);
		return bar;
	}

	private JPanel taoCardArea() {
		cardPanel.setOpaque(false);
		cardPanel.add(taoDoanhThuPanel(), "doanhthu");
		cardPanel.add(taoVePanel(), "ve");
		cardPanel.add(taoKhachHangPanel(), "khachhang");
		cardPanel.add(taoChuyenTauPanel(), "chuyentau");
		return cardPanel;
	}

	private void showCard(String name) {
		cardLayout.show(cardPanel, name);
	}

	private JPanel taoDoanhThuPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(taoStatGrid(new StatSpec[] {
			new StatSpec("Tổng doanh thu năm", formatMoney(20320000000L), "Năm 2026", MAU_CHINH),
			new StatSpec("Trung bình/tháng", formatMoney(1693333333L), "12 tháng", new Color(34, 197, 94)),
			new StatSpec("Tổng vé bán ra", "23.820", "vé", new Color(245, 158, 11)),
			new StatSpec("Doanh thu cao nhất", formatMoney(2350000000L), "Tháng 12", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		charts.add(taoChartCard("Doanh thu theo tháng (2026)", new BarChartPanel(
			new long[] { 1250000000L, 930000000L, 1580000000L, 1320000000L, 1440000000L, 1890000000L,
				2120000000L, 2070000000L, 1740000000L, 1600000000L, 1980000000L, 2350000000L },
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			MAU_CHINH, 2400000000L)));
		charts.add(taoChartCard("Số vé bán theo tháng (2026)", new LineChartPanel(
			new int[] { 1410, 980, 1920, 1450, 1580, 2140, 2360, 2300, 2030, 1900, 2250, 2620 },
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(34, 197, 94))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel taoVePanel() {
		JPanel wrap = new JPanel(new BorderLayout(12, 12));
		wrap.setOpaque(false);
		wrap.add(taoStatGrid(new StatSpec[] {
			new StatSpec("Tổng vé", "23.820", "tất cả", MAU_CHINH),
			new StatSpec("Vé đang hoạt động", "10.004", "42%", new Color(34, 197, 94)),
			new StatSpec("Vé đã sử dụng", "9.052", "38%", new Color(100, 116, 139)),
			new StatSpec("Vé bị trả/hủy", "2.858", "12%", new Color(239, 68, 68))
		}), BorderLayout.NORTH);

		JPanel split = new JPanel(new GridLayout(1, 2, 12, 12));
		split.setOpaque(false);
		split.add(taoChartCard("Trạng thái vé", new TicketStatusPanel()));
		split.add(taoChartCard("Vé bán theo loại toa", new HorizontalBarChartPanel(
			new int[] { 8400, 6200, 9800, 4700, 1300 },
			new String[] { "Ghế cứng", "Ghế mềm", "Nằm cứng", "Nằm mềm", "VIP" },
			MAU_CHINH, 10000)));
		wrap.add(split, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel taoKhachHangPanel() {
		JPanel wrap = new JPanel(new BorderLayout());
		wrap.setOpaque(false);
		wrap.add(taoStatGrid(new StatSpec[] {
			new StatSpec("Khách hàng", "1.850", "đang hoạt động", MAU_CHINH),
			new StatSpec("Khách mới", "286", "tháng này", new Color(34, 197, 94)),
			new StatSpec("Khách quay lại", "1.130", "61%", new Color(245, 158, 11)),
			new StatSpec("Tỉ lệ hài lòng", "96%", "khảo sát", new Color(139, 92, 246))
		}), BorderLayout.NORTH);

		JPanel placeholder = taoPlaceholderCard("Thống kê khách hàng sẽ hiển thị ở đây");
		wrap.add(placeholder, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel taoChuyenTauPanel() {
		JPanel wrap = new JPanel(new BorderLayout());
		wrap.setOpaque(false);
		wrap.add(taoStatGrid(new StatSpec[] {
			new StatSpec("Chuyến tàu", "128", "đang khai thác", MAU_CHINH),
			new StatSpec("Đúng giờ", "94%", "tháng này", new Color(34, 197, 94)),
			new StatSpec("Trễ chuyến", "6%", "tháng này", new Color(245, 158, 11)),
			new StatSpec("Hủy chuyến", "2", "trong năm", new Color(239, 68, 68))
		}), BorderLayout.NORTH);

		JPanel placeholder = taoPlaceholderCard("Thống kê chuyến tàu sẽ hiển thị ở đây");
		wrap.add(placeholder, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel taoStatGrid(StatSpec[] specs) {
		JPanel grid = new JPanel(new GridLayout(1, 4, 12, 12));
		grid.setOpaque(false);
		for (StatSpec spec : specs) {
			grid.add(taoStatCard(spec));
		}
		return grid;
	}

	private JPanel taoStatCard(StatSpec spec) {
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(14, 14, 14, 14)
		));

		JLabel title = new JLabel(spec.title);
		title.setForeground(new Color(108, 122, 138));
		title.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		card.add(title, BorderLayout.NORTH);

		JPanel center = new JPanel(new BorderLayout());
		center.setOpaque(false);
		center.setBorder(new EmptyBorder(10, 0, 0, 0));
		JLabel value = new JLabel(spec.value);
		value.setForeground(spec.color);
		value.setFont(new Font("Segoe UI", Font.BOLD, 28));
		center.add(value, BorderLayout.NORTH);
		JLabel sub = new JLabel(spec.subtitle);
		sub.setForeground(new Color(126, 139, 155));
		sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		center.add(sub, BorderLayout.SOUTH);
		card.add(center, BorderLayout.CENTER);
		return card;
	}

	private JPanel taoChartCard(String title, JPanel chart) {
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(12, 12, 12, 12)
		));
		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblTitle.setForeground(MAU_TEXT);
		card.add(lblTitle, BorderLayout.NORTH);
		card.add(chart, BorderLayout.CENTER);
		return card;
	}

	private JPanel taoPlaceholderCard(String message) {
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(24, 24, 24, 24)
		));
		JLabel label = new JLabel(message, JLabel.CENTER);
		label.setFont(new Font("Segoe UI", Font.ITALIC, 15));
		label.setForeground(new Color(126, 139, 155));
		card.add(label, BorderLayout.CENTER);
		return card;
	}

	private String formatMoney(long value) {
		return MONEY.format(value) + " đ";
	}

	private static final class StatSpec {
		private final String title;
		private final String value;
		private final String subtitle;
		private final Color color;

		private StatSpec(String title, String value, String subtitle, Color color) {
			this.title = title;
			this.value = value;
			this.subtitle = subtitle;
			this.color = color;
		}
	}

	private static final class BarChartPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final long[] values;
		private final String[] labels;
		private final Color barColor;
		private final long maxValue;

		private BarChartPanel(long[] values, String[] labels, Color barColor, long maxValue) {
			this.values = values;
			this.labels = labels;
			this.barColor = barColor;
			this.maxValue = maxValue;
			setOpaque(false);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int width = getWidth();
			int height = getHeight();
			int left = 48;
			int right = 16;
			int top = 18;
			int bottom = 34;
			int chartWidth = width - left - right;
			int chartHeight = height - top - bottom;
			g2.setColor(new Color(235, 240, 248));
			for (int i = 0; i < 5; i++) {
				int y = top + (chartHeight * i / 4);
				g2.drawLine(left, y, width - right, y);
			}
			int barWidth = Math.max(24, chartWidth / values.length - 10);
			int slot = chartWidth / values.length;
			for (int i = 0; i < values.length; i++) {
				int barHeight = (int) ((values[i] * 1.0 / maxValue) * (chartHeight - 10));
				int x = left + i * slot + (slot - barWidth) / 2;
				int y = top + chartHeight - barHeight;
				g2.setColor(barColor);
				g2.fillRoundRect(x, y, barWidth, barHeight, 6, 6);
				g2.setColor(new Color(95, 108, 125));
				g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
				g2.drawString(labels[i], x + barWidth / 2 - 8, height - 10);
			}
			g2.dispose();
		}
	}

	private static final class LineChartPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int[] values;
		private final String[] labels;
		private final Color lineColor;

		private LineChartPanel(int[] values, String[] labels, Color lineColor) {
			this.values = values;
			this.labels = labels;
			this.lineColor = lineColor;
			setOpaque(false);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int width = getWidth();
			int height = getHeight();
			int left = 40;
			int right = 16;
			int top = 18;
			int bottom = 34;
			int chartWidth = width - left - right;
			int chartHeight = height - top - bottom;
			int max = 2800;
			g2.setColor(new Color(235, 240, 248));
			for (int i = 0; i < 5; i++) {
				int y = top + (chartHeight * i / 4);
				g2.drawLine(left, y, width - right, y);
			}
			int[] xs = new int[values.length];
			int[] ys = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				xs[i] = left + (chartWidth * i / (values.length - 1));
				ys[i] = top + chartHeight - (int) ((values[i] * 1.0 / max) * (chartHeight - 10));
			}
			g2.setColor(lineColor);
			g2.setStroke(new BasicStroke(2.5f));
			for (int i = 0; i < xs.length - 1; i++) {
				g2.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
			}
			for (int i = 0; i < xs.length; i++) {
				g2.fillOval(xs[i] - 4, ys[i] - 4, 8, 8);
				g2.setColor(new Color(95, 108, 125));
				g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
				g2.drawString(labels[i], xs[i] - 10, height - 10);
				g2.setColor(lineColor);
			}
			g2.dispose();
		}
	}

	private static final class HorizontalBarChartPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int[] values;
		private final String[] labels;
		private final Color barColor;
		private final int maxValue;

		private HorizontalBarChartPanel(int[] values, String[] labels, Color barColor, int maxValue) {
			this.values = values;
			this.labels = labels;
			this.barColor = barColor;
			this.maxValue = maxValue;
			setOpaque(false);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int width = getWidth();
			int height = getHeight();
			int left = 90;
			int right = 18;
			int top = 20;
			int bottom = 20;
			int chartWidth = width - left - right;
			int chartHeight = height - top - bottom;
			int rowHeight = chartHeight / values.length;
			for (int i = 0; i < values.length; i++) {
				int y = top + i * rowHeight + 8;
				int barWidth = (int) ((values[i] * 1.0 / maxValue) * (chartWidth - 10));
				g2.setColor(new Color(95, 108, 125));
				g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
				g2.drawString(labels[i], 12, y + 16);
				g2.setColor(new Color(234, 240, 248));
				g2.fillRoundRect(left, y, chartWidth - 10, 28, 6, 6);
				g2.setColor(barColor);
				g2.fillRoundRect(left, y, barWidth, 28, 6, 6);
			}
			g2.dispose();
		}
	}

	private static final class TicketStatusPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int[] values = { 42, 38, 12, 8 };
		private final Color[] colors = {
			new Color(34, 197, 94),
			new Color(59, 130, 246),
			new Color(239, 68, 68),
			new Color(245, 158, 11)
		};
		private final String[] labels = { "Đang sử dụng", "Đã dùng", "Đã trả", "Đã đổi" };

		private TicketStatusPanel() {
			setOpaque(false);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int width = getWidth();
			int height = getHeight();
			int centerX = width / 2;
			int centerY = height / 2 - 8;
			int radius = Math.min(width, height) / 4;
			double start = 0;
			for (int i = 0; i < values.length; i++) {
				double angle = 360.0 * values[i] / 100.0;
				g2.setColor(colors[i]);
				g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, (int) start, (int) angle);
				start += angle;
			}
			g2.setColor(Color.WHITE);
			g2.fillOval(centerX - radius / 2, centerY - radius / 2, radius, radius);
			g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
			g2.setColor(new Color(34, 197, 94));
			g2.drawString("Đang sử dụng: 42%", centerX - 50, centerY - radius - 12);
			g2.setColor(new Color(59, 130, 246));
			g2.drawString("Đã dùng: 38%", centerX - radius - 40, centerY + radius / 2 + 14);
			g2.setColor(new Color(239, 68, 68));
			g2.drawString("Đã trả: 12%", centerX + radius - 18, centerY + radius / 2 + 14);
			g2.setColor(new Color(245, 158, 11));
			g2.drawString("Đã đổi: 8%", centerX + radius - 18, centerY - 10);

			g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
			int legendY = height - 18;
			int legendX = width / 2 - 145;
			for (int i = 0; i < labels.length; i++) {
				g2.setColor(colors[i]);
				g2.fillRect(legendX, legendY - 10, 14, 10);
				g2.setColor(new Color(95, 108, 125));
				g2.drawString(labels[i], legendX + 18, legendY);
				legendX += 78;
			}
			g2.dispose();
		}
	}
}
