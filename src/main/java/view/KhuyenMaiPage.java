package view;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class KhuyenMaiPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");
	private static final DateTimeFormatter FORMAT_NGAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();

	public KhuyenMaiPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));

		JPanel header = taoHeaderPanel();
		add(header, BorderLayout.NORTH);

		JPanel content = taoContentPanel();
		add(content, BorderLayout.CENTER);
	}

	private JPanel taoHeaderPanel() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#E0E0E0")),
			new EmptyBorder(12, 16, 12, 16)
		));

		JLabel title = new JLabel("Quản lý Khuyến mãi");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);

		JButton btnThem = new JButton("+ Thêm khuyến mãi");
		btnThem.setBackground(MAU_CHINH);
		btnThem.setForeground(Color.WHITE);
		btnThem.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnThem.setFocusPainted(false);
		btnThem.setBorder(new EmptyBorder(6, 12, 6, 12));
		btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		actions.add(btnThem);

		header.add(actions, BorderLayout.EAST);
		return header;
	}

	private JPanel taoContentPanel() {
		JPanel content = new JPanel(new BorderLayout());
		content.setBackground(Color.decode("#F0F5F9"));
		content.setBorder(new EmptyBorder(12, 16, 12, 16));

		String[] columns = { "Mã KM", "Tên khuyến mãi", "Mức giảm", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái" };
		DefaultTableModel model = new DefaultTableModel(columns, 0);

		napDuLieuKhuyenMai(model);

		JTable table = new JTable(model);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		table.setRowHeight(28);
		table.setShowGrid(true);
		table.setGridColor(new Color(220, 220, 220));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(800, 400));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private void napDuLieuKhuyenMai(DefaultTableModel model) {
		// Nạp khuyến mãi từ SQL để các màn quản lý không bị lệch dữ liệu.
		List<KhuyenMai> dsKhuyenMai = khuyenMaiDAO.layTatCa();
		for (KhuyenMai khuyenMai : dsKhuyenMai) {
			model.addRow(new Object[] {
				khuyenMai.getMaKM(),
				khuyenMai.getTenKM(),
				formatPhanTram(khuyenMai.getTyLeKM()),
				khuyenMai.getNgayBD() == null ? "" : FORMAT_NGAY.format(khuyenMai.getNgayBD()),
				khuyenMai.getNgayKT() == null ? "" : FORMAT_NGAY.format(khuyenMai.getNgayKT()),
				"Hoạt động"
			});
		}
	}

	private String formatPhanTram(BigDecimal tyLeKM) {
		if (tyLeKM == null) {
			return "0%";
		}
		return tyLeKM.stripTrailingZeros().toPlainString() + "%";
	}
}
