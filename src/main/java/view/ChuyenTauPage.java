package view;

import dao.ChuyenTau_DAO;
import entity.ChuyenTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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

public class ChuyenTauPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");
	private static final DateTimeFormatter FORMAT_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter FORMAT_GIO = DateTimeFormatter.ofPattern("HH:mm");
	private final ChuyenTau_DAO chuyenTauDAO = new ChuyenTau_DAO();

	public ChuyenTauPage() {
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

		JLabel title = new JLabel("Quản lý Chuyến Tàu");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);

		JButton btnThem = new JButton("+ Thêm chuyến");
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

		String[] columns = { "Mã chuyến", "Tàu", "Tuyến", "Ngày khởi hành", "Giờ khởi hành", "Trạng thái" };
		DefaultTableModel model = new DefaultTableModel(columns, 0);

		napDuLieuChuyenTau(model);

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

	private void napDuLieuChuyenTau(DefaultTableModel model) {
		// Chuyến tàu phải bám dữ liệu thật vì đây là nguồn cho bán vé và lịch sử vé.
		List<ChuyenTau> dsChuyenTau = chuyenTauDAO.layTatCa();
		for (ChuyenTau chuyenTau : dsChuyenTau) {
			model.addRow(new Object[] {
				chuyenTau.getMaCT(),
				chuyenTau.getMaTau(),
				chuyenTau.getMaTuyenTau(),
				chuyenTau.getNgayKhoiHanh() == null ? "" : FORMAT_NGAY.format(chuyenTau.getNgayKhoiHanh().toLocalDate()),
				chuyenTau.getGioKhoiHanh() == null ? "" : FORMAT_GIO.format(chuyenTau.getGioKhoiHanh().toLocalTime()),
				chuyenTau.isTrangThai() ? "Đang chạy" : "Ngừng"
			});
		}
	}
}
