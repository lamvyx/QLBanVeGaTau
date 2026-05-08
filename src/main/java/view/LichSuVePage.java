package view;

import dao.KhachHang_DAO;
import dao.VeTau_DAO;
import entity.KhachHang;
import entity.VeTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

public class LichSuVePage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private static final Color MAU_NEN = Color.decode("#F0F5F9");
	private static final Color MAU_VIEN = Color.decode("#DCE3EC");
	private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
	private final VeTau_DAO veTauDAO = new VeTau_DAO();
	private TableRowSorter<DefaultTableModel> sorter;

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

		JPanel body = new JPanel(new BorderLayout(10, 0));
		body.setOpaque(false);
		body.setBorder(new EmptyBorder(14, 0, 0, 0));

		JLabel lblSdt = new JLabel("Số điện thoại khách hàng:");
		lblSdt.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblSdt.setForeground(Color.decode("#1F3F72"));
		body.add(lblSdt, BorderLayout.WEST);

		JTextField txtSdt = new JTextField();
		txtSdt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSdt.setPreferredSize(new Dimension(260, 36));
		txtSdt.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(MAU_VIEN),
			new EmptyBorder(8, 10, 8, 10)
		));

		JButton btnTimKiem = new JButton("Tra cứu");
		btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnTimKiem.setBackground(MAU_CHINH);
		btnTimKiem.setForeground(Color.WHITE);
		btnTimKiem.setFocusPainted(false);
		btnTimKiem.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

		JPanel right = new JPanel(new BorderLayout(8, 0));
		right.setOpaque(false);
		right.add(txtSdt, BorderLayout.CENTER);
		right.add(btnTimKiem, BorderLayout.EAST);

		body.add(right, BorderLayout.CENTER);
		panel.add(body, BorderLayout.CENTER);

		btnTimKiem.addActionListener(e -> {
			String sdt = txtSdt.getText().trim();
			if (sorter != null) {
				if (sdt.isEmpty()) {
					sorter.setRowFilter(null);
				} else {
					sorter.setRowFilter(RowFilter.regexFilter(java.util.regex.Pattern.quote(sdt), 0));
				}
			}
		});
		txtSdt.addActionListener(e -> btnTimKiem.doClick());
		return panel;
	}

	private JPanel taoKhoiLichSuVe() {
		JPanel panel = new JPanel(new BorderLayout(0, 12));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(MAU_VIEN),
			new EmptyBorder(16, 16, 16, 16)
		));

		JLabel title = new JLabel("Lịch sử vé theo số điện thoại");
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		title.setForeground(Color.decode("#1F3F72"));
		panel.add(title, BorderLayout.NORTH);

		String[] columns = { "Số ĐT", "Mã vé", "Mã chuyến", "Mã toa", "Giá vé", "Trạng thái", "Ngày mua" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		napDuLieuLichSuVe(model);

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
		sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);
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

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Color.WHITE);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private void napDuLieuLichSuVe(DefaultTableModel model) {
		// Lịch sử vé được ghép từ khách hàng + vé tàu để phản ánh dữ liệu SQL thật.
		List<KhachHang> dsKhachHang = khachHangDAO.layTatCa();
		for (KhachHang khachHang : dsKhachHang) {
			List<VeTau> dsVe = veTauDAO.layTheoKhachHang(khachHang.getMaKH());
			for (VeTau veTau : dsVe) {
				model.addRow(new Object[] {
					khachHang.getSdt(),
					veTau.getMaVeTau(),
					veTau.getMaChuyenTau(),
					veTau.getMaToa() + " - " + veTau.getViTriGhe(),
					formatTien(veTau.getGiaVe()),
					veTau.getTrangThai(),
					""
				});
			}
		}
	}

	private String formatTien(BigDecimal giaVe) {
		if (giaVe == null) {
			return "0";
		}
		NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
		return formatter.format(giaVe);
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

			switch (column) {
				case 2 -> {
					setForeground(Color.decode("#0B5FFF"));
					setFont(new Font("Segoe UI", Font.BOLD, 13));
				}
				case 6 -> {
					setForeground(Color.decode("#0A8F3C"));
					setFont(new Font("Segoe UI", Font.BOLD, 13));
				}
				default -> setForeground(Color.decode("#1E2D3D"));
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
