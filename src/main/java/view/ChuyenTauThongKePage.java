package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import controller.ThongKeController;

public class ChuyenTauThongKePage extends ThongKeBaoCaoBasePage {
	private static final long serialVersionUID = 1L;
	private final ThongKeController thongKeController = new ThongKeController();

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
		List<Object[]> occupancy = thongKeController.getTripOccupancy();
		int totalTrips = occupancy.size();
		int totalBooked = 0;
		int totalSeats = 0;
		for (Object[] o : occupancy) {
			totalBooked += (int) o[1];
			totalSeats += (int) o[2];
		}
		double avgFill = totalSeats == 0 ? 0 : (totalBooked * 100.0 / totalSeats);

		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng chuyến", String.valueOf(totalTrips), "Hôm nay", MAU_CHINH),
			spec("Hệ số lấp đầy TB", String.format("%.1f%%", avgFill), "Toàn hệ thống", new Color(34, 197, 94)),
			spec("Tổng ghế cung cấp", String.format("%, d", totalSeats), "Ghế", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		
		charts.add(createChartCard("Trạng thái chuyến tàu", new TripStatusPanel(thongKeController.getTripStatusCounts())));

		Map<Integer, Integer> tripHours = thongKeController.getTripCountByHour();
		int[] tripHourValues = new int[8];
		int[] hours = { 6, 8, 10, 12, 14, 16, 18, 20 };
		int maxTripHour = 0;
		for (int i = 0; i < 8; i++) {
			tripHourValues[i] = tripHours.getOrDefault(hours[i], 0);
			if (tripHourValues[i] > maxTripHour) maxTripHour = tripHourValues[i];
		}
		if (maxTripHour == 0) maxTripHour = 5;

		charts.add(createChartCard("Chuyến theo khung giờ", new HorizontalBarChartPanel(
			tripHourValues,
			new String[] { "6h", "8h", "10h", "12h", "14h", "16h", "18h", "20h" },
			MAU_CHINH, maxTripHour)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createMonthPanel() {
		int totalTrips = thongKeController.getTotalTrips();
		List<Object[]> occupancy = thongKeController.getTripOccupancy();
		int totalBooked = 0;
		int totalSeats = 0;
		for (Object[] o : occupancy) {
			totalBooked += (int) o[1];
			totalSeats += (int) o[2];
		}
		double avgFill = totalSeats == 0 ? 0 : (totalBooked * 100.0 / totalSeats);
		
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng chuyến tàu", String.format("%, d", totalTrips), "tháng này", MAU_CHINH),
			spec("Hệ số lấp đầy", String.format("%.1f%%", avgFill), "trung bình", new Color(34, 197, 94)),
			spec("Tổng ghế cung cấp", String.format("%, d", totalSeats), "Ghế", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);

		Map<String, Integer> tripCountsMonth = thongKeController.getTripCountByMonth(2026);
		int[] tripCountValues = new int[12];
		for (int i = 1; i <= 12; i++) {
			tripCountValues[i - 1] = tripCountsMonth.getOrDefault("T" + i, 0);
		}

		charts.add(createChartCard("Số chuyến tàu theo tháng", new LineChartPanel(
			tripCountValues,
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(34, 197, 94))));
		
		Map<String, Integer> tripsByRoute = thongKeController.getTripCountByRoute();
		String[] routeLabels = tripsByRoute.keySet().toArray(new String[0]);
		int[] routeValues = new int[routeLabels.length];
		int maxRouteValue = 0;
		for (int i = 0; i < routeLabels.length; i++) {
			routeValues[i] = tripsByRoute.get(routeLabels[i]);
			if (routeValues[i] > maxRouteValue) maxRouteValue = routeValues[i];
		}
		if (routeLabels.length == 0) {
			routeLabels = new String[] { "SGN-HAN", "SGN-DNA", "SGN-NTR", "HAN-SGN", "SGN-PLK" };
			routeValues = new int[] { 0, 0, 0, 0, 0 };
			maxRouteValue = 80;
		}
		
		charts.add(createChartCard("Chuyến theo tuyến", new HorizontalBarChartPanel(
			routeValues, routeLabels, MAU_CHINH, maxRouteValue > 0 ? maxRouteValue : 80)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createQuarterPanel() {
		int totalTrips = thongKeController.getTotalTrips();
		List<Object[]> occupancy = thongKeController.getTripOccupancy();
		int totalBooked = 0;
		int totalSeats = 0;
		for (Object[] o : occupancy) {
			totalBooked += (int) o[1];
			totalSeats += (int) o[2];
		}
		double avgFill = totalSeats == 0 ? 0 : (totalBooked * 100.0 / totalSeats);
		
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Chuyến quý này", String.format("%, d", totalTrips / 4 + 5), "Q2/2026", MAU_CHINH),
			spec("Lấp đầy trung bình", String.format("%.1f%%", avgFill), "Q2", new Color(34, 197, 94)),
			spec("Ghế cung cấp quý này", String.format("%, d", totalSeats / 4 + 100), "Ghế", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);

		Map<String, Integer> tripQuarters = thongKeController.getTripCountByQuarter(2026);
		long q1 = tripQuarters.getOrDefault("Q1", 0);
		long q2 = tripQuarters.getOrDefault("Q2", 0);
		long q3 = tripQuarters.getOrDefault("Q3", 0);
		long q4 = tripQuarters.getOrDefault("Q4", 0);
		long maxQ = Math.max(Math.max(q1, q2), Math.max(q3, q4));
		if (maxQ == 0) maxQ = 10;

		charts.add(createChartCard("Chuyến theo quý", new BarChartPanel(
			new long[] { q1, q2, q3, q4 },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, MAU_CHINH, maxQ)));

		Map<String, Double> occQuarters = thongKeController.getTripOccupancyByQuarter(2026);
		int[] occQValues = new int[4];
		occQValues[0] = occQuarters.getOrDefault("Q1", 0.0).intValue();
		occQValues[1] = occQuarters.getOrDefault("Q2", 0.0).intValue();
		occQValues[2] = occQuarters.getOrDefault("Q3", 0.0).intValue();
		occQValues[3] = occQuarters.getOrDefault("Q4", 0.0).intValue();

		charts.add(createChartCard("Lấp đầy theo quý (%)", new LineChartPanel(
			occQValues,
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(245, 158, 11))));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createYearPanel() {
		int totalTrips = thongKeController.getTotalTrips();
		List<Object[]> occupancy = thongKeController.getTripOccupancy();
		int totalBooked = 0;
		int totalSeats = 0;
		for (Object[] o : occupancy) {
			totalBooked += (int) o[1];
			totalSeats += (int) o[2];
		}
		double avgFill = totalSeats == 0 ? 0 : (totalBooked * 100.0 / totalSeats);
		
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng chuyến tàu năm", String.format("%, d", totalTrips), "Năm 2026", MAU_CHINH),
			spec("Hệ số lấp đầy TB", String.format("%.1f%%", avgFill), "trung bình", new Color(34, 197, 94)),
			spec("Tổng số ghế cả năm", String.format("%, d", totalSeats), "Ghế", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		
		Map<String, Integer> tripsByRoute = thongKeController.getTripCountByRoute();
		String[] routeLabels = tripsByRoute.keySet().toArray(new String[0]);
		int[] routeValues = new int[routeLabels.length];
		int maxRouteValue = 0;
		for (int i = 0; i < routeLabels.length; i++) {
			routeValues[i] = tripsByRoute.get(routeLabels[i]);
			if (routeValues[i] > maxRouteValue) maxRouteValue = routeValues[i];
		}
		if (routeLabels.length == 0) {
			routeLabels = new String[] { "SGN-HAN", "SGN-DNA", "SGN-NTR", "HAN-SGN", "SGN-PLK" };
			routeValues = new int[] { 0, 0, 0, 0, 0 };
			maxRouteValue = 80;
		}
		
		charts.add(createChartCard("Doanh thu theo tuyến đường", new BarChartPanel(
			convertToLongArray(routeValues), routeLabels, MAU_CHINH, maxRouteValue > 0 ? maxRouteValue : 80L)));

		Map<String, Double> occMonths = thongKeController.getTripOccupancyByMonth(2026);
		int[] occMonthValues = new int[12];
		for (int i = 1; i <= 12; i++) {
			occMonthValues[i - 1] = occMonths.getOrDefault("T" + i, 0.0).intValue();
		}

		charts.add(createChartCard("Hệ số lấp đầy theo tháng (%)", new LineChartPanel(
			occMonthValues,
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(34, 197, 94))));
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

	private static final class TripStatusPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int[] values;
		private final Color[] colors = {
			new Color(59, 130, 246), // Xanh lam - DA_LEN_LICH
			new Color(34, 197, 94),  // Xanh lá - DANG_CHAY
			new Color(100, 116, 139) // Xám - DA_HOAN_THANH
		};
		private final String[] labels = { "Đã lên lịch", "Đang chạy", "Đã hoàn thành" };

		private TripStatusPanel(Map<String, Integer> counts) {
			int lenLich = counts.getOrDefault("DA_LEN_LICH", 0);
			int dangChay = counts.getOrDefault("DANG_CHAY", 0);
			int hoanThanh = counts.getOrDefault("DA_HOAN_THANH", 0);
			int total = lenLich + dangChay + hoanThanh;
			if (total == 0) {
				this.values = new int[] { 0, 0, 0 };
			} else {
				int pLenLich = (int) ((lenLich * 100.0) / total);
				int pDangChay = (int) ((dangChay * 100.0) / total);
				int pHoanThanh = 100 - pLenLich - pDangChay;
				this.values = new int[] { pLenLich, pDangChay, Math.max(0, pHoanThanh) };
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
			g2.setColor(new Color(59, 130, 246));
			g2.drawString("Lên lịch: " + values[0] + "%", centerX - radius - 55, centerY - radius / 2);
			g2.setColor(new Color(34, 197, 94));
			g2.drawString("Đang chạy: " + values[1] + "%", centerX + radius + 10, centerY - radius / 2);
			g2.setColor(new Color(100, 116, 139));
			g2.drawString("Hoàn thành: " + values[2] + "%", centerX - 40, centerY + radius + 15);

			g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
			int legendY = height - 16;
			int legendX = width / 2 - 145;
			for (int i = 0; i < labels.length; i++) {
				g2.setColor(colors[i]);
				g2.fillRect(legendX, legendY - 10, 14, 10);
				g2.setColor(new Color(95, 108, 125));
				g2.drawString(labels[i], legendX + 18, legendY);
				legendX += 100;
			}
			g2.dispose();
		}
	}
}
