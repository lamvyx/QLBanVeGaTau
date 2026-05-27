package view;

import controller.VeTauController;
import entity.LichSuVeDTO;
import java.time.format.DateTimeFormatter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
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
	private final DefaultTableModel model;
	private final JTextField txtTimKiem = new JTextField();
	private final VeTauController veTauController = new VeTauController();

	public LichSuVePage() {
		setLayout(new BorderLayout(0, 14));
		setBackground(MAU_NEN);
		setBorder(new EmptyBorder(14, 14, 14, 14));

		String[] columns = { "#", "Mã vé", "Tuyến đường", "Giờ khởi hành", "Chỗ ngồi", "Giá vé", "Trạng thái",
				"Ngày mua" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		add(taoKhoiChonKhachHang(), BorderLayout.NORTH);
		add(taoKhoiLichSuVe(), BorderLayout.CENTER);
		taiDuLieu(null);
	}

	private void taiDuLieu(String query) {
		model.setRowCount(0);
		List<LichSuVeDTO> dsVe = veTauController.layLichSuVeTheoBoLoc(query);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		int i = 1;
		for (LichSuVeDTO ve : dsVe) {
			model.addRow(new Object[] {
				i++,
				ve.getMaVeTau(),
				ve.getTuyenDuong(),
				ve.getNgayKhoiHanh().format(dtf),
				ve.getMaToa(),
				ve.getGiaVe(),
				ve.getTrangThai(),
				ve.getNgayMua() != null ? ve.getNgayMua().format(dtf) : ""
			});
		}
	}

	private JPanel taoKhoiChonKhachHang() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(MAU_VIEN),
				new EmptyBorder(16, 16, 16, 16)));

		JLabel title = new JLabel("Tìm kiếm hành khách (Tên, SĐT, Email, CCCD)");
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		title.setForeground(Color.decode("#1F3F72"));
		panel.add(title, BorderLayout.NORTH);

		JPanel body = new JPanel(new BorderLayout(10, 0));
		body.setOpaque(false);
		body.setBorder(new EmptyBorder(14, 0, 0, 0));

		txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(MAU_VIEN),
				new EmptyBorder(8, 10, 8, 10)));
		txtTimKiem.setPreferredSize(new Dimension(380, 36));
		txtTimKiem.setToolTipText("Nhập tên, số điện thoại, email hoặc CCCD của hành khách");
		txtTimKiem.addActionListener(e -> taiDuLieu(txtTimKiem.getText()));

		JButton btnLoc = new JButton("Tìm kiếm");
		btnLoc.setBackground(MAU_CHINH);
		btnLoc.setForeground(Color.WHITE);
		btnLoc.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnLoc.setFocusPainted(false);
		btnLoc.setPreferredSize(new Dimension(100, 36));
		btnLoc.addActionListener(e -> taiDuLieu(txtTimKiem.getText()));

		body.add(txtTimKiem, BorderLayout.WEST);
		body.add(btnLoc, BorderLayout.CENTER);
		panel.add(body, BorderLayout.CENTER);
		return panel;
	}

	private JPanel taoKhoiLichSuVe() {
		JPanel panel = new JPanel(new BorderLayout(0, 12));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(MAU_VIEN),
				new EmptyBorder(16, 16, 16, 16)));

		JLabel title = new JLabel("Danh sách lịch sử vé");
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		title.setForeground(Color.decode("#1F3F72"));
		panel.add(title, BorderLayout.NORTH);

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
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
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
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
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
