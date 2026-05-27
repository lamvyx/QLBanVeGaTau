package view;

import controller.ThongKeController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.Map;
import javax.swing.JPanel;
import service.ThongKeService.ThongKeSoLuongVe;

public class VeThongKePage extends ThongKeBaoCaoBasePage {
	private static final long serialVersionUID = 1L;
	private final ThongKeController thongKeController = new ThongKeController();

	public VeThongKePage() {
		super("Thống kê vé");
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
		int soldTickets = thongKeController.getSoldTicketsByPeriod("day");
		BigDecimal revenue = thongKeController.getRevenueByPeriod("day");
		ThongKeSoLuongVe tk = thongKeController.thongKeSoLuongVe();
		
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Vé đã bán hôm nay", String.format("%, d", soldTickets), "Trong ngày", MAU_CHINH),
			spec("Doanh thu hôm nay", money(revenue.longValue()), "Từ hóa đơn đã thanh toán", new Color(34, 197, 94)),
			spec("Giá vé trung bình", soldTickets == 0 ? "0 đ" : money(revenue.longValue() / soldTickets), "Ước tính", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Trạng thái vé", new TicketStatusPanel(tk)));

		Map<Integer, Integer> hourlyTickets = thongKeController.getHourlyTicketSales();
		int[] ticketValues = new int[8];
		int[] hours = { 6, 8, 10, 12, 14, 16, 18, 20 };
		int maxTicketVal = 0;
		for (int i = 0; i < 8; i++) {
			ticketValues[i] = hourlyTickets.getOrDefault(hours[i], 0);
			if (ticketValues[i] > maxTicketVal) maxTicketVal = ticketValues[i];
		}
		if (maxTicketVal == 0) maxTicketVal = 100;

		charts.add(createChartCard("Vé theo khung giờ", new HorizontalBarChartPanel(
			ticketValues,
			new String[] { "6h", "8h", "10h", "12h", "14h", "16h", "18h", "20h" },
			MAU_CHINH, maxTicketVal)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createMonthPanel() {
		ThongKeSoLuongVe tk = thongKeController.thongKeSoLuongVe();
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng vé tháng", String.format("%, d", tk.tongSoVe), "Năm 2026", MAU_CHINH),
			spec("Vé đã bán", String.format("%, d", tk.soVeDaBan), "Đã thanh toán", new Color(34, 197, 94)),
			spec("Vé còn trống", String.format("%, d", tk.soVeConTrong), "Sẵn sàng bán", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Trạng thái vé", new TicketStatusPanel(tk)));
		
		Map<String, Integer> ticketsByType = thongKeController.getTicketsByCarriageType();
		String[] typeLabels = ticketsByType.keySet().toArray(new String[0]);
		int[] typeValues = new int[typeLabels.length];
		int maxTypeValue = 0;
		for (int i = 0; i < typeLabels.length; i++) {
			typeValues[i] = ticketsByType.get(typeLabels[i]);
			if (typeValues[i] > maxTypeValue) maxTypeValue = typeValues[i];
		}
		if (typeLabels.length == 0) {
			typeLabels = new String[] { "Ghế cứng", "Ghế mềm", "Nằm cứng", "Nằm mềm", "VIP" };
			typeValues = new int[] { 0, 0, 0, 0, 0 };
			maxTypeValue = 100;
		}
		
		charts.add(createChartCard("Vé bán theo loại toa", new HorizontalBarChartPanel(
			typeValues, typeLabels, MAU_CHINH, maxTypeValue > 0 ? maxTypeValue : 100)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createQuarterPanel() {
		ThongKeSoLuongVe tk = thongKeController.thongKeSoLuongVe();
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Quý hiện tại", "Q2/2026", "3 tháng", MAU_CHINH),
			spec("Tổng vé quý", String.format("%, d", tk.tongSoVe), "vé", new Color(34, 197, 94)),
			spec("Vé đã bán", String.format("%, d", tk.soVeDaBan), "Đã bán thành công", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);

		int q1Ve = thongKeController.getSoldTicketsByQuarter(2026, 1);
		int q2Ve = thongKeController.getSoldTicketsByQuarter(2026, 2);
		int q3Ve = thongKeController.getSoldTicketsByQuarter(2026, 3);
		int q4Ve = thongKeController.getSoldTicketsByQuarter(2026, 4);
		long maxQ = Math.max(Math.max(q1Ve, q2Ve), Math.max(q3Ve, q4Ve));
		if (maxQ == 0) maxQ = 100;

		charts.add(createChartCard("Vé bán theo quý", new BarChartPanel(
			new long[] { q1Ve, q2Ve, q3Ve, q4Ve },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(34, 197, 94), maxQ)));
		charts.add(createChartCard("Tỷ lệ trạng thái vé", new TicketStatusPanel(tk)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createYearPanel() {
		ThongKeSoLuongVe tk = thongKeController.thongKeSoLuongVe();
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng vé năm", String.format("%, d", tk.tongSoVe), "Năm 2026", MAU_CHINH),
			spec("Tỷ lệ đã bán", tk.tongSoVe > 0 ? String.format("%.0f%%", (tk.soVeDaBan * 100.0 / tk.tongSoVe)) : "0%", "vé đã thanh toán", new Color(34, 197, 94)),
			spec("Vé đã bán cả năm", String.format("%, d", tk.soVeDaBan), "vé", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);

		Map<String, Integer> ticketsByMonth = thongKeController.getSoldTicketsByMonth(2026);
		int[] ticketsValues = new int[12];
		for (int i = 1; i <= 12; i++) {
			ticketsValues[i - 1] = ticketsByMonth.getOrDefault("T" + i, 0);
		}

		charts.add(createChartCard("Số vé theo tháng", new LineChartPanel(
			ticketsValues,
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(34, 197, 94))));
		
		Map<String, Integer> ticketsByType = thongKeController.getTicketsByCarriageType();
		String[] typeLabels = ticketsByType.keySet().toArray(new String[0]);
		int[] typeValues = new int[typeLabels.length];
		int maxTypeValue = 0;
		for (int i = 0; i < typeLabels.length; i++) {
			typeValues[i] = ticketsByType.get(typeLabels[i]);
			if (typeValues[i] > maxTypeValue) maxTypeValue = typeValues[i];
		}
		if (typeLabels.length == 0) {
			typeLabels = new String[] { "Ghế cứng", "Ghế mềm", "Nằm cứng", "Nằm mềm", "VIP" };
			typeValues = new int[] { 0, 0, 0, 0, 0 };
			maxTypeValue = 100;
		}
		
		charts.add(createChartCard("Vé theo loại toa", new HorizontalBarChartPanel(
			typeValues, typeLabels, MAU_CHINH, maxTypeValue > 0 ? maxTypeValue : 100)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private static final class TicketStatusPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int[] values;
		private final Color[] colors = {
			new Color(34, 197, 94), // Xanh lá - Còn trống
			new Color(59, 130, 246), // Xanh lam - Đã bán
			new Color(239, 68, 68)  // Đỏ - Hoàn/Hủy
		};
		private final String[] labels = { "Vé còn trống", "Vé đã bán", "Vé hoàn/hủy" };

		private TicketStatusPanel(ThongKeSoLuongVe stats) {
			int total = stats.tongSoVe;
			if (total == 0) {
				this.values = new int[] { 0, 0, 0 };
			} else {
				int percentConTrong = (int) ((stats.soVeConTrong * 100.0) / total);
				int percentDaBan = (int) ((stats.soVeDaBan * 100.0) / total);
				int percentHuy = 100 - percentConTrong - percentDaBan;
				this.values = new int[] { percentConTrong, percentDaBan, Math.max(0, percentHuy) };
			}
			setOpaque(false);
		}

		@Override
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
			g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
			int width = getWidth();
			int height = getHeight();
			int centerX = width / 2;
			int centerY = height / 2 - 12;
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
			
			g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
			g2.setColor(new Color(34, 197, 94));
			g2.drawString("Còn trống: " + values[0] + "%", centerX - radius - 60, centerY - radius / 2);
			
			g2.setColor(new Color(59, 130, 246));
			g2.drawString("Đã bán: " + values[1] + "%", centerX + radius + 10, centerY - radius / 2);
			
			g2.setColor(new Color(239, 68, 68));
			g2.drawString("Hoàn/Hủy: " + values[2] + "%", centerX - 35, centerY + radius + 15);

			g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
			int legendY = height - 16;
			int legendX = width / 2 - 130;
			for (int i = 0; i < labels.length; i++) {
				g2.setColor(colors[i]);
				g2.fillRect(legendX, legendY - 10, 14, 10);
				g2.setColor(new Color(95, 108, 125));
				g2.drawString(labels[i], legendX + 18, legendY);
				legendX += 90;
			}
			g2.dispose();
		}
	}
}
