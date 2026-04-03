package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class NhanVienTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");

	public NhanVienTraCuuPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoContent(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		JLabel title = new JLabel("Tra cứu nhân viên");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout(0, 15));
		content.setBackground(Color.WHITE);
		content.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(14, 14, 14, 14)
		));

		// Phần tìm kiếm
		JPanel searchPanel = taoSearchPanel();
		content.add(searchPanel, BorderLayout.NORTH);

		// Phần bảng kết quả
		String[] columns = { "#", "Mã NV", "Họ và tên", "Tài khoản", "Email", "Điện thoại", "Chức vụ" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		model.addRow(new Object[] { 1, "U001", "Nguyễn Văn Admin", "admin", "admin@saigontrain.vn", "0901234567", "Quản lý" });
		model.addRow(new Object[] { 2, "U002", "Trần Thị Nhân Viên", "staff", "staff@saigontrain.vn", "0901234568", "Bán vé" });
		model.addRow(new Object[] { 3, "U003", "Nguyễn Phát Đạt", "nguyen.phat", "phat@saigontrain.vn", "0901234569", "Bán vé" });
		model.addRow(new Object[] { 4, "U004", "Lê Thị Mai", "le.mai", "mai@saigontrain.vn", "0901234570", "Hỗ trợ" });
		model.addRow(new Object[] { 5, "U005", "Phạm Văn Cường", "pham.cuong", "cuong@saigontrain.vn", "0901234571", "Quản lý" });

		JTable table = new JTable(model);
		table.setRowHeight(50);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setBackground(MAU_CHINH);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.setGridColor(Color.decode("#E4EBF3"));
		table.setSelectionBackground(Color.decode("#B3D9FF"));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private JPanel taoSearchPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(15, 15, 15, 15)
		));

		// Mã nhân viên
		JLabel lblMa = new JLabel("Mã nhân viên:");
		lblMa.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblMa.setForeground(Color.decode("#2B4B74"));
		panel.add(lblMa);

		JTextField txtMa = new JTextField();
		txtMa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtMa.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 8, 6, 8)
		));
		panel.add(txtMa);

		// Nút Tìm kiếm
		JButton btnTimKiem = new JButton("Tìm kiếm");
		btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnTimKiem.setBackground(MAU_CHINH);
		btnTimKiem.setForeground(Color.WHITE);
		btnTimKiem.setFocusPainted(false);
		btnTimKiem.setBorder(new EmptyBorder(6, 12, 6, 12));
		panel.add(btnTimKiem);

		// Tên nhân viên
		JLabel lblTen = new JLabel("Tên nhân viên:");
		lblTen.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblTen.setForeground(Color.decode("#2B4B74"));
		panel.add(lblTen);

		JTextField txtTen = new JTextField();
		txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtTen.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 8, 6, 8)
		));
		panel.add(txtTen);

		// Nút Làm mới
		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setForeground(Color.decode("#3A4D66"));
		btnLamMoi.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 12, 6, 12)
		));
		panel.add(btnLamMoi);

		btnLamMoi.addActionListener(e -> {
			txtMa.setText("");
			txtTen.setText("");
		});

		return panel;
	}
}
