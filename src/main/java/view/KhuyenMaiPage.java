package view;

import controller.KhuyenMaiController;
import entity.KhuyenMai;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
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
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final KhuyenMaiController khuyenMaiController = new KhuyenMaiController();
	private DefaultTableModel model;

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

		String[] columns = { "maKM", "tenKM", "tyLeKM", "ngayBD", "ngayKT", "trangThai" };
		model = new DefaultTableModel(columns, 0);
		loadDataFromDatabase();

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

	private void loadDataFromDatabase() {
		model.setRowCount(0);
		List<KhuyenMai> danhSach = khuyenMaiController.layTatCaKhuyenMai();
		LocalDate homNay = LocalDate.now();
		for (KhuyenMai km : danhSach) {
			String trangThai = (!homNay.isBefore(km.getNgayBD()) && !homNay.isAfter(km.getNgayKT())) ? "1" : "0";
			model.addRow(new Object[] { km.getMaKM(), km.getTenKM(), km.getTyLeKM(),
					km.getNgayBD() != null ? km.getNgayBD().format(DATE_FMT) : "",
					km.getNgayKT() != null ? km.getNgayKT().format(DATE_FMT) : "", trangThai });
		}
	}
}
