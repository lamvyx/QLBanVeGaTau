package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class LichSuVePage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private static final Color MAU_NEN = Color.decode("#F0F5F9");
	private static final Color MAU_VIEN = Color.decode("#DCE3EC");

	public LichSuVePage() {
		setLayout(new BorderLayout(0, 14));
		setBackground(MAU_NEN);
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoKhoiChonKhachHang(), BorderLayout.NORTH);
		add(taoKhoiLichSuVe(), BorderLayout.CENTER);
	}

	private JPanel taoKhoiChonKhachHang() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(MAU_VIEN),
			new EmptyBorder(16, 16, 16, 16)
		));

		JLabel title = new JLabel("Chọn khách hàng");
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		title.setForeground(Color.decode("#1F3F72"));
		panel.add(title, BorderLayout.NORTH);

		JPanel body = new JPanel(new BorderLayout());
		body.setOpaque(false);
		body.setBorder(new EmptyBorder(14, 0, 0, 0));

		JComboBox<String> cboKhachHang = new JComboBox<>(new String[] {
			"-- Tất cả khách hàng --",
			"Nguyễn Thị Hoa - KH001",
			"Trần Văn Đạt - KH002",
			"Phạm Văn Cường - KH003",
			"Hoàng Thị Dung - KH004"
		});
		cboKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cboKhachHang.setBackground(Color.WHITE);
		cboKhachHang.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(MAU_VIEN),
			new EmptyBorder(8, 10, 8, 10)
		));
		cboKhachHang.setPreferredSize(new Dimension(260, 36));

		body.add(cboKhachHang, BorderLayout.WEST);
		panel.add(body, BorderLayout.CENTER);
		return panel;
	}

	private JPanel taoKhoiLichSuVe() {
		JPanel panel = new JPanel(new BorderLayout(0, 12));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(MAU_VIEN),
			new EmptyBorder(16, 16, 16, 16)
		));

		JLabel title = new JLabel("Lịch sử vé (Tất cả)");
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		title.setForeground(Color.decode("#1F3F72"));
		panel.add(title, BorderLayout.NORTH);

		JPanel searchWrap = new JPanel(new BorderLayout());
		searchWrap.setOpaque(false);
		searchWrap.setBorder(new EmptyBorder(8, 0, 0, 0));

		JTextField txtSearch = new JTextField();
		txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSearch.setPreferredSize(new Dimension(380, 36));
		txtSearch.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(MAU_VIEN),
			new EmptyBorder(6, 10, 6, 10)
		));
		txtSearch.setToolTipText("Tìm kiếm...");
		searchWrap.add(txtSearch, BorderLayout.WEST);

		panel.add(searchWrap, BorderLayout.NORTH);

		String[] columns = { "#", "Mã vé", "Tuyến đường", "Giờ khởi hành", "Chỗ ngồi", "Giá vé", "Trạng thái", "Ngày mua" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		model.addRow(new Object[] { 1, "VE0001", "Sài Gòn - Hà Nội", "02/04/2026 19:00", "A05", "850.000 đ", "Hoạt động", "2026-03-20" });
		model.addRow(new Object[] { 2, "VE0002", "Sài Gòn - Đà Nẵng", "02/04/2026 07:00", "B12", "477.000 đ", "Hoạt động", "2026-03-22" });
		model.addRow(new Object[] { 3, "VE0003", "Sài Gòn - Nha Trang", "02/04/2026 06:00", "C08", "280.000 đ", "Đã dùng", "2026-03-25" });
		model.addRow(new Object[] { 4, "VE0004", "Sài Gòn - Hà Nội", "05/04/2026 20:00", "VIP03", "1.350.000 đ", "Hoạt động", "2026-03-28" });
		model.addRow(new Object[] { 5, "VE0005", "Sài Gòn - Hà Nội", "02/04/2026 19:00", "D15", "1.020.000 đ", "Đã trả", "2026-03-18" });
		model.addRow(new Object[] { 6, "VE0006", "Sài Gòn - Cần Thơ", "03/04/2026 08:00", "E09", "340.000 đ", "Hoạt động", "2026-03-29" });

		JTable table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				if (column == 6) {
					return new BadgeRenderer();
				}
				return super.getCellRenderer(row, column);
			}
		};
		table.setRowHeight(38);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setSelectionBackground(Color.decode("#DCEBFF"));
		table.setGridColor(MAU_VIEN);
		table.getTableHeader().setBackground(MAU_CHINH);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		table.getTableHeader().setPreferredSize(new Dimension(0, 40));
		table.setDefaultRenderer(Object.class, new TableStyleRenderer());
		table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Color.WHITE);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private static class TableStyleRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setFont(new Font("Segoe UI", Font.PLAIN, 13));
			setBorder(new EmptyBorder(0, 10, 0, 10));
			setOpaque(true);
			if (isSelected) {
				setBackground(Color.decode("#DCEBFF"));
			} else {
				setBackground(Color.WHITE);
			}

			if (column == 1) {
				setForeground(Color.decode("#0B5FFF"));
				setFont(new Font("Segoe UI", Font.BOLD, 13));
			} else if (column == 5) {
				setForeground(Color.decode("#0A8F3C"));
				setFont(new Font("Segoe UI", Font.BOLD, 13));
			} else {
				setForeground(Color.decode("#1E2D3D"));
			}
			return c;
		}
	}

	private static class BadgeRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = new JLabel(String.valueOf(value), JLabel.CENTER);
			label.setOpaque(true);
			label.setFont(new Font("Segoe UI", Font.BOLD, 13));
			label.setBorder(new EmptyBorder(4, 10, 4, 10));
			if ("Hoạt động".equals(value)) {
				label.setBackground(Color.decode("#D9F7D6"));
				label.setForeground(Color.decode("#0A8F3C"));
			} else if ("Đã dùng".equals(value)) {
				label.setBackground(Color.decode("#EEF1F5"));
				label.setForeground(Color.decode("#7A8698"));
			} else if ("Đã trả".equals(value)) {
				label.setBackground(Color.decode("#FFE3E0"));
				label.setForeground(Color.decode("#D93025"));
			} else {
				label.setBackground(Color.WHITE);
				label.setForeground(Color.decode("#1E2D3D"));
			}
			return label;
		}
	}
}
