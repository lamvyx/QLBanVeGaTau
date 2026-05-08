package view;

import controller.DoiTraController;
import entity.DonDoiTra;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DonDoiTraTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JTable table;
	private final DefaultTableModel model;
	private final DoiTraController controller = new DoiTraController();
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	public DonDoiTraTraCuuPage() {
		setLayout(new BorderLayout(0, 16));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoHeader(), BorderLayout.NORTH);
		
		String[] columns = { "Mã đơn", "Mã vé cũ", "Mã vé mới", "Loại", "Trạng thái", "Ngày tạo", "Ghi chú" };
		model = new DefaultTableModel(columns, 0) {
			@Override public boolean isCellEditable(int r, int c) { return false; }
		};
		table = new JTable(model);
		AppTheme.styleTable(table);
		table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
		
		add(new JScrollPane(table), BorderLayout.CENTER);
		
		taiDuLieu();
	}

	private JPanel taoHeader() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		
		JLabel title = new JLabel("Lịch sử Đổi / Trả vé");
		title.setFont(AppTheme.font(Font.BOLD, 24));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		panel.add(title, BorderLayout.WEST);
		
		JTextField txtSearch = new JTextField();
		txtSearch.setPreferredSize(new Dimension(300, 40));
		txtSearch.setToolTipText("Tìm theo mã đơn hoặc mã vé...");
		panel.add(txtSearch, BorderLayout.EAST);
		
		return panel;
	}

	private void taiDuLieu() {
		model.setRowCount(0);
		List<DonDoiTra> ds = controller.layTatCaDon();
		for (DonDoiTra d : ds) {
			model.addRow(new Object[] {
				d.getMaDon(),
				d.getMaVeCu(),
				d.getMaVeMoi() == null ? "-" : d.getMaVeMoi(),
				"DOI".equals(d.getLoaiDon()) ? "Đổi vé" : "Trả vé",
				d.getTrangThai(),
				d.getThoiGianTao() != null ? d.getThoiGianTao().format(formatter) : "-",
				d.getGhiChu()
			});
		}
	}

	private static class StatusRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			label.setHorizontalAlignment(CENTER);
			String status = String.valueOf(value);
			label.setOpaque(true);
			if ("DA_XAC_NHAN".equals(status)) {
				label.setBackground(new Color(230, 255, 230));
				label.setForeground(new Color(0, 150, 0));
				label.setText("Thành công");
			} else if ("CHO_XAC_NHAN".equals(status)) {
				label.setBackground(new Color(255, 245, 230));
				label.setForeground(new Color(200, 120, 0));
				label.setText("Chờ xử lý");
			} else {
				label.setBackground(Color.WHITE);
				label.setForeground(Color.GRAY);
			}
			return label;
		}
	}
}
