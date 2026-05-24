package view;

import controller.NhanVienController;
import entity.NhanVien;
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

public class NhanVienPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = AppTheme.PRIMARY;
	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final NhanVienController nhanVienController = new NhanVienController();
	private DefaultTableModel model;

	public NhanVienPage() {
		setLayout(new BorderLayout());
		setBackground(AppTheme.PAGE_BG);

		// Header
		JPanel header = taoHeaderPanel();
		add(header, BorderLayout.NORTH);

		// Content
		JPanel content = taoContentPanel();
		add(content, BorderLayout.CENTER);
	}

	private JPanel taoHeaderPanel() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(AppTheme.CARD_BG);
		header.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Quản lý Nhân viên");
		title.setFont(AppTheme.font(Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);

		JButton btnThem = new JButton("+ Thêm nhân viên");
		AppTheme.stylePrimaryButton(btnThem);
		actions.add(btnThem);

		header.add(actions, BorderLayout.EAST);
		return header;
	}

	private JPanel taoContentPanel() {
		JPanel content = new JPanel(new BorderLayout());
		content.setBackground(AppTheme.PAGE_BG);
		content.setBorder(new EmptyBorder(12, 16, 12, 16));

		String[] columns = { "maNV", "tenNV", "sdt", "gioiTinh", "ngaySinh", "ngayVaoLam", "trangThai", "email", "chucVu", "username" };
		model = new DefaultTableModel(columns, 0);
		loadDataFromDatabase();

		JTable table = new JTable(model);
		AppTheme.styleTable(table);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		scrollPane.setPreferredSize(new Dimension(800, 400));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private void loadDataFromDatabase() {
		model.setRowCount(0);
		List<NhanVien> danhSach = nhanVienController.layTatCaNhanVien();
		for (NhanVien nv : danhSach) {
			model.addRow(new Object[] { nv.getMaNV(), nv.getTenNV(), nv.getSdt(), nv.isGioiTinh() ? "Nam" : "Nữ",
					nv.getNgaySinh() != null ? nv.getNgaySinh().format(DATE_FMT) : "",
					nv.getNgayVaoLam() != null ? nv.getNgayVaoLam().format(DATE_FMT) : "",
					nv.isTrangThai() ? "1" : "0", nhanVienController.layEmailTheoUsername(nv.getUsername()),
					nv.getChucVu(), nv.getUsername() });
		}
	}
}
