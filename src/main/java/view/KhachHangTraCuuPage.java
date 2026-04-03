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

public class KhachHangTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");

	public KhachHangTraCuuPage() {
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

		JLabel title = new JLabel("Tra cứu khách hàng");
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
		String[] columns = { "#", "Mã KH", "Tên khách hàng", "Số ĐT", "Email", "CCCD", "Loại KH" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		model.addRow(new Object[] { 1, "KH001", "Trần Văn A", "0901234567", "tryvana@email.com", "012345678901", "Thường" });
		model.addRow(new Object[] { 2, "KH002", "Nguyễn Thị B", "0912345678", "nguyenb@email.com", "012345678902", "VIP" });
		model.addRow(new Object[] { 3, "KH003", "Phạm Văn C", "0923456789", "phamvan@email.com", "012345678903", "Thường" });
		model.addRow(new Object[] { 4, "KH004", "Hoàng Thị D", "0934567890", "hoang.d@email.com", "012345678904", "Doanh nghiệp" });
		model.addRow(new Object[] { 5, "KH005", "Võ Văn E", "0945678901", "vo.van.e@email.com", "012345678905", "VIP" });

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

		// Mã khách hàng
		JLabel lblMa = new JLabel("Mã khách hàng:");
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

		// Tên khách hàng
		JLabel lblTen = new JLabel("Tên khách hàng:");
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
