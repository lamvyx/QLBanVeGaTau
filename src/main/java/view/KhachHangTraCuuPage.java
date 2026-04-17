package view;

import controller.KhachHangController;
import entity.KhachHang;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
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
	private final KhachHangController khachHangController = new KhachHangController();
	private DefaultTableModel model;
	private JTable table;

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
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Lấy dữ liệu từ database
		loadDataFromDatabase();

		table = new JTable(model);
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

	private void loadDataFromDatabase() {
		model.setRowCount(0);
		List<KhachHang> danhSach = khachHangController.layTatCaKhachHang();

		for (int i = 0; i < danhSach.size(); i++) {
			KhachHang kh = danhSach.get(i);
			model.addRow(new Object[] {
				i + 1,
				kh.getMaKH(),
				kh.getTenKH(),
				kh.getSdt(),
				kh.getEmail(),
				kh.getCccd(),
				kh.isLoaiKH() ? "VIP" : "Thường"
			});
		}
	}

	private JPanel taoSearchPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(15, 15, 15, 15)
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 8, 6, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		// Mã khách hàng
		JLabel lblMa = new JLabel("Mã khách hàng:");
		lblMa.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblMa.setForeground(Color.decode("#2B4B74"));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		panel.add(lblMa, gbc);

		JTextField txtMa = new JTextField();
		txtMa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtMa.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 8, 6, 8)
		));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		panel.add(txtMa, gbc);

		// Tên khách hàng
		JLabel lblTen = new JLabel("Tên khách hàng:");
		lblTen.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblTen.setForeground(Color.decode("#2B4B74"));
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0;
		panel.add(lblTen, gbc);

		JTextField txtTen = new JTextField();
		txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtTen.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 8, 6, 8)
		));
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 1;
		panel.add(txtTen, gbc);

		// Nút Tìm kiếm
		JButton btnTimKiem = new JButton("Tìm kiếm");
		btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnTimKiem.setBackground(MAU_CHINH);
		btnTimKiem.setForeground(Color.WHITE);
		btnTimKiem.setFocusPainted(false);
		btnTimKiem.setBorder(new EmptyBorder(6, 12, 6, 12));
		
		btnTimKiem.addActionListener(e -> {
			String ma = txtMa.getText().trim();
			String ten = txtTen.getText().trim();
			
			model.setRowCount(0);
			List<KhachHang> results = khachHangController.timKiemKhachHang(ma, ten);
			
			for (int i = 0; i < results.size(); i++) {
				KhachHang kh = results.get(i);
				model.addRow(new Object[] {
					i + 1,
					kh.getMaKH(),
					kh.getTenKH(),
					kh.getSdt(),
					kh.getEmail(),
					kh.getCccd(),
					kh.isLoaiKH() ? "VIP" : "Thường"
				});
			}
		});

		// Nút Làm mới
		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setForeground(Color.decode("#3A4D66"));
		btnLamMoi.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 12, 6, 12)
		));
		
		btnLamMoi.addActionListener(e -> {
			txtMa.setText("");
			txtTen.setText("");
			loadDataFromDatabase();
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.add(btnTimKiem);
		buttonPanel.add(btnLamMoi);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 4;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(buttonPanel, gbc);

		return panel;
	}
}
