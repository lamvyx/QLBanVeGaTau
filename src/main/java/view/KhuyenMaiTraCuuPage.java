package view;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
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
	private static final DateTimeFormatter FORMAT_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private final KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();

	private JTextField txtTimKiem;
	private JComboBox<String> cbSapXep;
	private JTable tableKhuyenMai;
	private DefaultTableModel model;

	public KhuyenMaiTraCuuPage() {
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

		String[] columns = { "Mã KM", "Tên khuyến mãi", "Tỷ lệ (%)", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Sample data
		napDuLieuKhuyenMai(model);

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

	private void napDuLieuKhuyenMai(DefaultTableModel model) {
		// Lấy khuyến mãi thật từ SQL để tra cứu không bị lệch dữ liệu.
		List<KhuyenMai> dsKhuyenMai = khuyenMaiDAO.layTatCa();
		for (KhuyenMai khuyenMai : dsKhuyenMai) {
			model.addRow(new Object[] {
				khuyenMai.getMaKM(),
				khuyenMai.getTenKM(),
				formatTyLe(khuyenMai.getTyLeKM()),
				khuyenMai.getNgayBD() == null ? "" : FORMAT_NGAY.format(khuyenMai.getNgayBD()),
				khuyenMai.getNgayKT() == null ? "" : FORMAT_NGAY.format(khuyenMai.getNgayKT()),
				tinhTrang(khuyenMai)
			});
		}
	}

	private String formatTyLe(BigDecimal tyLeKM) {
		if (tyLeKM == null) {
			return "0";
		}
		return tyLeKM.stripTrailingZeros().toPlainString();
	}

	private String tinhTrang(KhuyenMai khuyenMai) {
		if (khuyenMai.getNgayBD() == null || khuyenMai.getNgayKT() == null) {
			return "";
		}
		java.time.LocalDate homNay = java.time.LocalDate.now();
		if (homNay.isBefore(khuyenMai.getNgayBD())) {
			return "Sắp diễn ra";
		}
		if (homNay.isAfter(khuyenMai.getNgayKT())) {
			return "Kết thúc";
		}
		return "Đang chạy";
	}
}
