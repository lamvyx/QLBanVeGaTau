package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ChuyenTauTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");

	private JTextField txtTimKiem;
	private JComboBox<String> cbSapXep;
	private JTable tableChuyenTau;
	private DefaultTableModel model;

	public ChuyenTauTraCuuPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoSearchPanel(), BorderLayout.WEST);
		add(taoTablePanel(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		JLabel title = new JLabel("Tra cứu chuyến tàu");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		return header;
	}

	private JPanel taoSearchPanel() {
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));
		searchPanel.setPreferredSize(new Dimension(250, 200));

		JLabel lblTimKiem = new JLabel("Tìm kiếm:");
		lblTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
		searchPanel.add(lblTimKiem);

		txtTimKiem = new JTextField();
		txtTimKiem.setPreferredSize(new Dimension(200, 32));
		txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 8, 6, 8)
		));
		txtTimKiem.setText("Tìm kiếm...");
		searchPanel.add(txtTimKiem);

		JLabel lblSapXep = new JLabel("Sắp xếp:");
		lblSapXep.setFont(new Font("Segoe UI", Font.BOLD, 12));
		searchPanel.add(lblSapXep);

		cbSapXep = new JComboBox<>();
		cbSapXep.addItem("Tất cả trạng thái");
		cbSapXep.addItem("Lên lịch");
		cbSapXep.addItem("Đang chạy");
		cbSapXep.addItem("Hoàn thành");
		cbSapXep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cbSapXep.setPreferredSize(new Dimension(200, 32));
		searchPanel.add(cbSapXep);

		return searchPanel;
	}

	private JPanel taoTablePanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		String[] columns = { "#", "Mã chuyến", "Tàu", "Tuyến đường", "Khởi hành", "Đến nơi", "Chỗ trống", "Giá cơ bản", "Trạng thái", "Thao tác" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Sample data
		model.addRow(new Object[] { 1, "SE1-020426", "SE1", "Sài Gòn - Hà Nội", "19:00:00 2/4/2026", "01:00:00 4/4/2026", "112/196", "850.000 đ", "Lên lịch", "👁 ✏️ 🗑" });
		model.addRow(new Object[] { 2, "SE1-030426", "SE1", "Sài Gòn - Hà Nội", "19:00:00 3/4/2026", "01:00:00 5/4/2026", "145/196", "850.000 đ", "Lên lịch", "👁 ✏️ 🗑" });
		model.addRow(new Object[] { 3, "SE2-020426", "SE2", "Sài Gòn - Đà Nẵng", "07:00:00 2/4/2026", "23:00:00 2/4/2026", "56/76", "530.000 đ", "Đang chạy", "👁 ✏️ 🗑" });
		model.addRow(new Object[] { 4, "TN1-020426", "TN1", "Sài Gòn - Nha Trang", "06:00:00 2/4/2026", "13:00:00 2/4/2026", "0/96", "280.000 đ", "Hoàn thành", "👁 ✏️ 🗑" });
		model.addRow(new Object[] { 5, "SG1-020426", "SG1", "Sài Gòn - Hà Nội", "20:00:00 5/4/2026", "02:00:00 7/4/2026", "12/16", "1.500.000 đ", "Lên lịch", "👁 ✏️ 🗑" });
		model.addRow(new Object[] { 6, "SE2-030426", "SE2", "Sài Gòn - Nha Trang", "08:00:00 3/4/2026", "15:00:00 3/4/2026", "48/76", "280.000 đ", "Lên lịch", "👁 ✏️ 🗑" });

		tableChuyenTau = new JTable(model);
		tableChuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tableChuyenTau.setRowHeight(28);
		tableChuyenTau.setShowGrid(true);
		tableChuyenTau.setGridColor(new Color(220, 220, 220));
		tableChuyenTau.setBackground(Color.WHITE);
		tableChuyenTau.getTableHeader().setBackground(MAU_CHINH);
		tableChuyenTau.getTableHeader().setForeground(Color.WHITE);
		tableChuyenTau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

		JScrollPane scrollPane = new JScrollPane(tableChuyenTau);
		scrollPane.setPreferredSize(new Dimension(1000, 400));
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		return tablePanel;
	}
}
