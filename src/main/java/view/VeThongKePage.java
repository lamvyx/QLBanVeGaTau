package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class VeThongKePage extends ThongKeBaoCaoBasePage {
	private static final long serialVersionUID = 1L;

	public VeThongKePage() {
		super("Thống kê vé");
	}

	public VeThongKePage(boolean chiThongKeTheoNgay) {
		super("Thống kê vé", chiThongKeTheoNgay);
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
			spec("Tổng vé trong ngày", "1.284", "03/04/2026", MAU_CHINH),
			spec("Vé đang hoạt động", "542", "42%", new Color(34, 197, 94)),
			spec("Vé đã dùng", "487", "38%", new Color(100, 116, 139)),
			spec("Vé trả/hủy", "109", "8.5%", new Color(239, 68, 68))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Trạng thái vé trong ngày", new TicketStatusPanel()));
		charts.add(createChartCard("Vé theo khung giờ", new HorizontalBarChartPanel(
			new int[] { 90, 120, 160, 210, 180, 240, 160, 124 },
			new String[] { "6h", "8h", "10h", "12h", "14h", "16h", "18h", "20h" },
			MAU_CHINH, 260)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createMonthPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng vé tháng", "23.820", "Năm 2026", MAU_CHINH),
			spec("Vé đang hoạt động", "10.004", "42%", new Color(34, 197, 94)),
			spec("Vé đã dùng", "9.052", "38%", new Color(100, 116, 139)),
			spec("Vé trả/hủy", "2.858", "12%", new Color(239, 68, 68))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Trạng thái vé", new TicketStatusPanel()));
		charts.add(createChartCard("Vé bán theo loại toa", new HorizontalBarChartPanel(
			new int[] { 8400, 6200, 9800, 4700, 1300 },
			new String[] { "Ghế cứng", "Ghế mềm", "Nằm cứng", "Nằm mềm", "VIP" },
			MAU_CHINH, 10000)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createQuarterPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Quý hiện tại", "Q2/2026", "3 tháng", MAU_CHINH),
			spec("Tổng vé quý", "6.120", "vé", new Color(34, 197, 94)),
			spec("Vé dùng", "4.820", "79%", new Color(245, 158, 11)),
			spec("Vé hủy", "188", "3%", new Color(239, 68, 68))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Vé bán theo quý", new BarChartPanel(
			new long[] { 6120L, 7340L, 6890L, 9470L },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(34, 197, 94), 10000L)));
		charts.add(createChartCard("Tỷ lệ trạng thái vé", new TicketStatusPanel()));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createYearPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng vé năm", "23.820", "Năm 2026", MAU_CHINH),
			spec("Tỷ lệ sử dụng", "80%", "trung bình", new Color(34, 197, 94)),
			spec("Tỷ lệ hoàn", "12%", "toàn năm", new Color(245, 158, 11)),
			spec("Tỷ lệ hủy", "8%", "toàn năm", new Color(239, 68, 68))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Số vé theo tháng", new LineChartPanel(
			new int[] { 1410, 980, 1920, 1450, 1580, 2140, 2360, 2300, 2030, 1900, 2250, 2620 },
			new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" },
			new Color(34, 197, 94))));
		charts.add(createChartCard("Vé theo loại toa", new HorizontalBarChartPanel(
			new int[] { 8400, 6200, 9800, 4700, 1300 },
			new String[] { "Ghế cứng", "Ghế mềm", "Nằm cứng", "Nằm mềm", "VIP" },
			MAU_CHINH, 10000)));
		wrap.add(charts, BorderLayout.CENTER);
		return wrap;
	}
}
