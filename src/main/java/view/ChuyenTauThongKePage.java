package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
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
			spec("Tổng chuyến", String.valueOf(totalTrips), "Hệ thống", MAU_CHINH),
			spec("Đúng giờ", "---", "đang cập nhật", new Color(34, 197, 94)),
			spec("Lấp đầy trung bình", String.format("%.1f%%", avgFill), "Toàn hệ thống", new Color(245, 158, 11)),
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
			spec("Đúng giờ", "---", "tỷ lệ", new Color(34, 197, 94)),
			spec("Bị hủy", "---", "chuyến", new Color(239, 68, 68)),
			spec("Hệ số lấp đầy", String.format("%.1f%%", avgFill), "trung bình", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Tỷ lệ đúng giờ", new LineChartPanel(
			new int[] { 92, 93, 95, 94, 96, 95, 94, 93, 95, 96, 94, 95 },
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
			spec("Chuyến quý", String.format("%, d", totalTrips), "Q2/2026", MAU_CHINH),
			spec("Đúng giờ", "---", "tỷ lệ", new Color(34, 197, 94)),
			spec("Bị hủy", "---", "chuyến", new Color(239, 68, 68)),
			spec("Lấp đầy TB", String.format("%.1f%%", avgFill), "trung bình", new Color(245, 158, 11))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Chuyến theo quý", new BarChartPanel(
			new long[] { totalTrips / 4, totalTrips / 4, totalTrips / 4, totalTrips / 4 },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, MAU_CHINH, totalTrips > 0 ? totalTrips : 160L)));
		charts.add(createChartCard("Lấp đầy theo quý", new LineChartPanel(
			new int[] { 71, 74, 77, 79 },
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
			spec("Tổng chuyến tàu", String.format("%, d", totalTrips), "Năm 2026", MAU_CHINH),
			spec("Đúng giờ", "---", "tỷ lệ", new Color(34, 197, 94)),
			spec("Bị hủy", "---", "chuyến", new Color(239, 68, 68)),
			spec("Hệ số lấp đầy", String.format("%.1f%%", avgFill), "trung bình", new Color(245, 158, 11))
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
		charts.add(createChartCard("Lấp đầy theo tháng", new LineChartPanel(
			new int[] { 72, 74, 76, 78, 79, 81, 80, 77, 76, 75, 82, 87 },
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
}
