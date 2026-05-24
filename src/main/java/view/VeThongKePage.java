package view;

import controller.ThongKeController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
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
		ThongKeSoLuongVe tk = thongKeController.thongKeSoLuongVe();
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng vé", String.valueOf(tk.tongSoVe), "Từ dữ liệu hệ thống", MAU_CHINH),
			spec("Vé đã bán", String.valueOf(tk.soVeDaBan), "Trạng thái DA_BAN", new Color(34, 197, 94)),
			spec("Vé còn trống", String.valueOf(tk.soVeConTrong), "Trạng thái CON_TRONG", new Color(100, 116, 139)),
			spec("Vé không hiệu lực", String.valueOf(tk.soVeKhongHieuLuc), "Trạng thái KHONG_HIEU_LUC", new Color(239, 68, 68))
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
		ThongKeSoLuongVe tk = thongKeController.thongKeSoLuongVe();
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setOpaque(false);
		wrap.add(createStatGrid(new StatSpec[] {
			spec("Tổng vé tháng", String.format("%, d", tk.tongSoVe), "Năm 2026", MAU_CHINH),
			spec("Vé đang hoạt động", String.format("%, d", tk.soVeConTrong), "Sẵn sàng", new Color(34, 197, 94)),
			spec("Vé đã dùng", String.format("%, d", tk.soVeDaBan), "Đã bán", new Color(100, 116, 139)),
			spec("Vé trả/hủy", String.format("%, d", tk.soVeKhongHieuLuc), "Hủy bỏ", new Color(239, 68, 68))
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
			spec("Vé dùng", String.format("%, d", tk.soVeDaBan), "Đã bán", new Color(245, 158, 11)),
			spec("Vé hủy", String.format("%, d", tk.soVeKhongHieuLuc), "Hủy bỏ", new Color(239, 68, 68))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Vé bán theo quý", new BarChartPanel(
			new long[] { tk.tongSoVe / 4, tk.tongSoVe / 4, tk.tongSoVe / 4, tk.tongSoVe / 4 },
			new String[] { "Q1", "Q2", "Q3", "Q4" }, new Color(34, 197, 94), tk.tongSoVe > 0 ? tk.tongSoVe : 10000L)));
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
			spec("Tỷ lệ sử dụng", tk.tongSoVe > 0 ? String.format("%.0f%%", (tk.soVeDaBan * 100.0 / tk.tongSoVe)) : "0%", "trung bình", new Color(34, 197, 94)),
			spec("Tỷ lệ hoàn", tk.tongSoVe > 0 ? String.format("%.0f%%", (tk.soVeKhongHieuLuc * 100.0 / tk.tongSoVe)) : "0%", "toàn năm", new Color(245, 158, 11)),
			spec("Tỷ lệ hủy", "---", "toàn năm", new Color(239, 68, 68))
		}), BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(2, 1, 12, 12));
		charts.setOpaque(false);
		charts.add(createChartCard("Số vé theo tháng", new LineChartPanel(
			new int[] { 1410, 980, 1920, 1450, 1580, 2140, 2360, 2300, 2030, 1900, 2250, 2620 },
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
			new Color(34, 197, 94),
			new Color(59, 130, 246),
			new Color(239, 68, 68),
			new Color(245, 158, 11)
		};
		private final String[] labels = { "Đang sử dụng", "Đã dùng", "Đã trả", "Đã đổi" };

		private TicketStatusPanel() {
			this.values = new int[] { 42, 38, 12, 8 };
			setOpaque(false);
		}

		private TicketStatusPanel(ThongKeSoLuongVe stats) {
			int total = stats.tongSoVe;
			if (total == 0) {
				this.values = new int[] { 0, 0, 0, 0 };
			} else {
				this.values = new int[] {
					(int) ((stats.soVeConTrong * 100.0) / total),
					(int) ((stats.soVeDaBan * 100.0) / total),
					(int) ((stats.soVeKhongHieuLuc * 100.0) / total),
					0
				};
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
			g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
			g2.setColor(new Color(34, 197, 94));
			g2.drawString("Đang sử dụng: " + values[0] + "%", centerX - 50, centerY - radius - 12);
			g2.setColor(new Color(59, 130, 246));
			g2.drawString("Đã dùng: " + values[1] + "%", centerX - radius - 40, centerY + radius / 2 + 14);
			g2.setColor(new Color(239, 68, 68));
			g2.drawString("Đã trả: " + values[2] + "%", centerX + radius - 18, centerY + radius / 2 + 14);
			g2.setColor(new Color(245, 158, 11));
			g2.drawString("Đã đổi: " + values[3] + "%", centerX + radius - 18, centerY - 10);

			g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
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
