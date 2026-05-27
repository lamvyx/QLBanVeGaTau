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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public abstract class ThongKeBaoCaoBasePage extends JPanel {
	private static final long serialVersionUID = 1L;
	protected static final Color MAU_CHINH = Color.decode("#2563EB");
	protected static final Color MAU_NEN = Color.decode("#F3F6FB");
	protected static final Color MAU_TEXT = Color.decode("#35506B");
	private static final DecimalFormat MONEY = new DecimalFormat("#,###");

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel cardPanel = new JPanel(cardLayout);
	private final JPanel bodyHost = new JPanel(new BorderLayout());
	private final JComboBox<String> cboTongHop = new JComboBox<>(new String[] { "Ngày", "Tháng", "Quý", "Năm" });
	private final JComboBox<String> cboNam = new JComboBox<>(new String[] { "2024", "2025", "2026" });

	protected ThongKeBaoCaoBasePage(String title) {
		setLayout(new BorderLayout());
		setBackground(MAU_NEN);
		add(taoHeaderPanel(title), BorderLayout.NORTH);
		bodyHost.setOpaque(false);
		add(bodyHost, BorderLayout.CENTER);
		SwingUtilities.invokeLater(this::khoiTaoNoiDungSau);
	}

	private void khoiTaoNoiDungSau() {
		bodyHost.removeAll();
		bodyHost.add(taoBodyPanel(), BorderLayout.CENTER);
		bodyHost.revalidate();
		bodyHost.repaint();
		showPeriod((String) cboTongHop.getSelectedItem());
	}

	private JPanel taoHeaderPanel(String titleText) {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#DDE5F2")));
		header.setPreferredSize(new Dimension(0, 64));

		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
		left.setOpaque(false);
		JLabel title = new JLabel(titleText);
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		title.setForeground(MAU_TEXT);
		left.add(title);
		header.add(left, BorderLayout.WEST);

		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
		right.setOpaque(false);
		JLabel lblTongHop = new JLabel("Tổng hợp:");
		lblTongHop.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblTongHop.setForeground(MAU_TEXT);
		right.add(lblTongHop);

		cboTongHop.setSelectedItem("Tháng");
		cboTongHop.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboTongHop.setPreferredSize(new Dimension(96, 28));
		right.add(cboTongHop);

		JLabel lblNam = new JLabel("Năm:");
		lblNam.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblNam.setForeground(MAU_TEXT);
		right.add(lblNam);

		cboNam.setSelectedItem("2026");
		cboNam.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboNam.setPreferredSize(new Dimension(72, 28));
		right.add(cboNam);

		header.add(right, BorderLayout.EAST);

		cboTongHop.addActionListener(e -> showPeriod((String) cboTongHop.getSelectedItem()));
		showPeriod((String) cboTongHop.getSelectedItem());
		return header;
	}

	private JPanel taoBodyPanel() {
		JPanel body = new JPanel(new BorderLayout());
		body.setOpaque(false);
		body.setBorder(new EmptyBorder(12, 12, 12, 12));
		body.add(taoFilterBar(), BorderLayout.NORTH);
		body.add(taoCardArea(), BorderLayout.CENTER);
		return body;
	}

	private JPanel taoFilterBar() {
		JPanel bar = new JPanel();
		bar.setOpaque(false);
		bar.setPreferredSize(new Dimension(0, 5));
		return bar;
	}

	private JPanel taoCardArea() {
		cardPanel.setOpaque(false);
		cardPanel.add(createPeriodPanel("day"), "Ngày");
		cardPanel.add(createPeriodPanel("month"), "Tháng");
		cardPanel.add(createPeriodPanel("quarter"), "Quý");
		cardPanel.add(createPeriodPanel("year"), "Năm");
		return cardPanel;
	}

	private void showPeriod(String period) {
		cardLayout.show(cardPanel, period == null ? "Tháng" : period);
	}


	protected abstract JPanel createPeriodPanel(String periodKey);

	protected JPanel createStatGrid(StatSpec[] specs) {
		JPanel grid = new JPanel(new GridLayout(1, specs.length, 12, 12));
		grid.setOpaque(false);
		for (StatSpec spec : specs) {
			grid.add(createStatCard(spec));
		}
		return grid;
	}

	protected JPanel createTwoColumnCharts(JPanel left, JPanel right) {
		JPanel split = new JPanel(new GridLayout(1, 2, 12, 12));
		split.setOpaque(false);
		split.add(left);
		split.add(right);
		return split;
	}

	protected JPanel createChartCard(String title, JPanel chart) {
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

	protected JPanel createPlaceholderCard(String message) {
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

	protected String money(long value) {
		return MONEY.format(value) + " đ";
	}

	protected StatSpec spec(String title, String value, String subtitle, Color color) {
		return new StatSpec(title, value, subtitle, color);
	}

	protected JPanel createStatCard(StatSpec spec) {
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

	protected static final class StatSpec {
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

	protected static class BarChartPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final long[] values;
		private final String[] labels;
		private final Color barColor;
		private final long maxValue;

		protected BarChartPanel(long[] values, String[] labels, Color barColor, long maxValue) {
			this.values = values;
			this.labels = labels;
			this.barColor = barColor;
			
			// Dynamically adjust maxValue to fit actual data perfectly if it is 0 or smaller than actual data
			long actualMax = 0;
			for (long v : values) {
				if (v > actualMax) actualMax = v;
			}
			if (actualMax == 0) {
				this.maxValue = 10;
			} else {
				this.maxValue = Math.max(maxValue, actualMax);
			}
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
			
			g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
			for (int i = 0; i < 5; i++) {
				int y = top + (chartHeight * i / 4);
				g2.setColor(new Color(235, 240, 248));
				g2.drawLine(left, y, width - right, y);
				
				long gridVal = maxValue - (maxValue * i / 4);
				g2.setColor(new Color(126, 139, 155));
				String valStr = String.valueOf(gridVal);
				if (gridVal >= 1000000000L) valStr = (gridVal / 1000000000L) + "B";
				else if (gridVal >= 1000000L) valStr = (gridVal / 1000000L) + "M";
				else if (gridVal >= 1000L) valStr = (gridVal / 1000L) + "K";
				g2.drawString(valStr, 6, y + 4);
			}
			
			int barWidth = Math.max(16, (chartWidth / Math.max(1, values.length)) - 12);
			int slot = chartWidth / Math.max(1, values.length);
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

	protected static class LineChartPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int[] values;
		private final String[] labels;
		private final Color lineColor;

		protected LineChartPanel(int[] values, String[] labels, Color lineColor) {
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
			int left = 48;
			int right = 16;
			int top = 18;
			int bottom = 34;
			int chartWidth = width - left - right;
			int chartHeight = height - top - bottom;
			
			// Dynamically calculate the max limit for line chart auto-scaling
			int max = 0;
			for (int v : values) {
				if (v > max) max = v;
			}
			if (max == 0) {
				max = 10;
			} else {
				if (max < 10) max = 10;
				else if (max < 50) max = ((max + 9) / 10) * 10;
				else if (max < 100) max = ((max + 19) / 20) * 20;
				else max = (int) (max * 1.15);
			}
			
			g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
			for (int i = 0; i < 5; i++) {
				int y = top + (chartHeight * i / 4);
				g2.setColor(new Color(235, 240, 248));
				g2.drawLine(left, y, width - right, y);
				
				int gridVal = max - (max * i / 4);
				g2.setColor(new Color(126, 139, 155));
				g2.drawString(String.valueOf(gridVal), 6, y + 4);
			}
			
			int[] xs = new int[values.length];
			int[] ys = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				xs[i] = left + (chartWidth * i / Math.max(1, values.length - 1));
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

	protected static class HorizontalBarChartPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int[] values;
		private final String[] labels;
		private final Color barColor;
		private final int maxValue;

		protected HorizontalBarChartPanel(int[] values, String[] labels, Color barColor, int maxValue) {
			this.values = values;
			this.labels = labels;
			this.barColor = barColor;
			
			int actualMax = 0;
			for (int v : values) {
				if (v > actualMax) actualMax = v;
			}
			if (actualMax == 0) {
				this.maxValue = 10;
			} else {
				this.maxValue = Math.max(maxValue, actualMax);
			}
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
			int right = 36; // Increased right padding to prevent numeric labels from being cut off
			int top = 20;
			int bottom = 20;
			int chartWidth = width - left - right;
			int chartHeight = height - top - bottom;
			int rowHeight = chartHeight / Math.max(1, values.length);
			for (int i = 0; i < values.length; i++) {
				int y = top + i * rowHeight + 8;
				int barWidth = (int) ((values[i] * 1.0 / maxValue) * (chartWidth - 10));
				g2.setColor(new Color(95, 108, 125));
				g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
				g2.drawString(labels[i], 12, y + 18);
				g2.setColor(new Color(234, 240, 248));
				g2.fillRoundRect(left, y, chartWidth - 10, 24, 6, 6);
				g2.setColor(barColor);
				g2.fillRoundRect(left, y, barWidth, 24, 6, 6);
				
				// Draw precise actual count value next to the horizontal bar
				g2.setColor(MAU_TEXT);
				g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
				g2.drawString(String.valueOf(values[i]), left + barWidth + 8, y + 16);
			}
			g2.dispose();
		}
	}

	protected static class TicketStatusPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int[] values = { 42, 38, 12, 8 };
		private final Color[] colors = {
			new Color(34, 197, 94),
			new Color(59, 130, 246),
			new Color(239, 68, 68),
			new Color(245, 158, 11)
		};
		private final String[] labels = { "Đang sử dụng", "Đã dùng", "Đã trả", "Đã đổi" };

		protected TicketStatusPanel() {
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
