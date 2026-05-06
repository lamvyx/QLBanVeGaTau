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

public class TauTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");

	private JTextField txtTimKiem;
	private JComboBox<String> cbSapXep;
	private JTable tableTau;
	private DefaultTableModel model;

	public TauTraCuuPage() {
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

		JLabel title = new JLabel("Tra cứu tàu");
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
		cbSapXep.addItem("Tất cả");
		cbSapXep.addItem("Hoạt động");
		cbSapXep.addItem("Bảo trì");
		cbSapXep.addItem("Ngừng hoạt động");
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

		String[] columns = { "Mã tàu", "Tên tàu", "Số toa", "Sức chứa", "Năm sản xuất", "Trạng thái" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Sample data
		model.addRow(new Object[] { "T001", "Tàu hỏa XP1", 8, 320, 2015, "Hoạt động" });
		model.addRow(new Object[] { "T002", "Tàu hỏa SB2", 10, 400, 2018, "Hoạt động" });
		model.addRow(new Object[] { "T003", "Tàu Sapa Express", 12, 480, 2019, "Hoạt động" });
		model.addRow(new Object[] { "T004", "Tàu Nightly", 8, 320, 2016, "Bảo trí" });
		model.addRow(new Object[] { "T005", "Tàu Hỏa Thường", 6, 240, 2014, "NgẺ ng hoạt động" });

		tableTau = new JTable(model);
		tableTau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tableTau.setRowHeight(28);
		tableTau.setShowGrid(true);
		tableTau.setGridColor(new Color(220, 220, 220));
		tableTau.setBackground(Color.WHITE);
		tableTau.getTableHeader().setBackground(MAU_CHINH);
		tableTau.getTableHeader().setForeground(Color.WHITE);
		tableTau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

		JScrollPane scrollPane = new JScrollPane(tableTau);
		scrollPane.setPreferredSize(new Dimension(1000, 400));
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		return tablePanel;
	}
}
