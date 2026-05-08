package view;

import controller.KhuyenMaiController;
import entity.KhuyenMai;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class KhuyenMaiTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");

	private JTextField txtTimKiem;
	private JComboBox<String> cbSapXep;
	private JTable tableKhuyenMai;
	private DefaultTableModel model;
	private final KhuyenMaiController khuyenMaiController = new KhuyenMaiController();
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public KhuyenMaiTraCuuPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoSearchPanel(), BorderLayout.WEST);
		add(taoTablePanel(), BorderLayout.CENTER);
		taiDuLieuBang();
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		JLabel title = new JLabel("Tra cứu khuyến mãi");
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

		JLabel lblSapXep = new JLabel("Trạng thái:");
		lblSapXep.setFont(new Font("Segoe UI", Font.BOLD, 12));
		searchPanel.add(lblSapXep);

		cbSapXep = new JComboBox<>();
		cbSapXep.addItem("Tất cả");
		cbSapXep.addItem("Sắp diễn ra");
		cbSapXep.addItem("Đang chạy");
		cbSapXep.addItem("Kết thúc");
		cbSapXep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cbSapXep.setPreferredSize(new Dimension(200, 32));
		cbSapXep.addActionListener(e -> taiDuLieuBang());
		searchPanel.add(cbSapXep);

		JButton btnTimKiem = new JButton("Tìm kiếm");
		btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnTimKiem.setBackground(MAU_CHINH);
		btnTimKiem.setForeground(Color.WHITE);
		btnTimKiem.addActionListener(e -> taiDuLieuBang());
		searchPanel.add(btnTimKiem);

		return searchPanel;
	}

	private JPanel taoTablePanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		String[] columns = { "#", "Mã KM", "Tên khuyến mãi", "Tỷ lệ (%)", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái", "Thao tác" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableKhuyenMai = new JTable(model);
		tableKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tableKhuyenMai.setRowHeight(28);
		tableKhuyenMai.setShowGrid(true);
		tableKhuyenMai.setGridColor(new Color(220, 220, 220));
		tableKhuyenMai.setBackground(Color.WHITE);
		tableKhuyenMai.getTableHeader().setBackground(MAU_CHINH);
		tableKhuyenMai.getTableHeader().setForeground(Color.WHITE);
		tableKhuyenMai.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

		JScrollPane scrollPane = new JScrollPane(tableKhuyenMai);
		scrollPane.setPreferredSize(new Dimension(1000, 400));
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		return tablePanel;
	}

	private void taiDuLieuBang() {
		if (model == null) return;
		model.setRowCount(0);
		String keyword = txtTimKiem.getText().trim();
		if (keyword.equals("Tìm kiếm...")) keyword = "";
		
		List<KhuyenMai> ds = khuyenMaiController.timKiemKhuyenMai(null, keyword);
		String filter = (String) cbSapXep.getSelectedItem();
		LocalDate today = LocalDate.now();
		
		int stt = 1;
		for (KhuyenMai km : ds) {
			String trangThai = "";
			if (today.isBefore(km.getNgayBD())) {
				trangThai = "Sắp diễn ra";
			} else if (today.isAfter(km.getNgayKT())) {
				trangThai = "Kết thúc";
			} else {
				trangThai = "Đang chạy";
			}
			
			if (filter.equals("Tất cả") || filter.equals(trangThai)) {
				model.addRow(new Object[] { 
					stt++,
					km.getMaKM(),
					km.getTenKM(),
					km.getTyLeKM(),
					km.getNgayBD().format(dtf),
					km.getNgayKT().format(dtf),
					trangThai,
					"👁 ✏️ 🗑"
				});
			}
		}
	}
}
