package view;

import controller.DichVuController;
import entity.DichVu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
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

public class DichVuTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private final DichVuController dichVuController = new DichVuController();
	private DefaultTableModel model;
	private JTable tableDichVu;

	public DichVuTraCuuPage() {
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
				new EmptyBorder(12, 14, 12, 14)));

		JLabel title = new JLabel("Tra cứu dịch vụ");
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
				new EmptyBorder(14, 14, 14, 14)));

		// Phần tìm kiếm
		JPanel searchPanel = taoSearchPanel();
		content.add(searchPanel, BorderLayout.NORTH);

		// Phần bảng kết quả
		String[] columns = { "#", "Mã dịch vụ", "Tên dịch vụ", "Giá (VND)", "Trạng thái" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Lấy dữ liệu từ database
		taiDuLieuBang(null, null);

		tableDichVu = new JTable(model);
		tableDichVu.setRowHeight(50);
		tableDichVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tableDichVu.getTableHeader().setBackground(MAU_CHINH);
		tableDichVu.getTableHeader().setForeground(Color.WHITE);
		tableDichVu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		tableDichVu.setGridColor(Color.decode("#E4EBF3"));
		tableDichVu.setSelectionBackground(Color.decode("#B3D9FF"));

		JScrollPane scrollPane = new JScrollPane(tableDichVu);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private void taiDuLieuBang(String ma, String ten) {
		model.setRowCount(0);
		List<DichVu> ds = dichVuController.timKiemDichVu(ma, ten);
		int stt = 1;
		for (DichVu dv : ds) {
			BigDecimal gia = dv.getGiaDV();
			String giaStr = (gia != null) ? String.format("%,.0f VND", gia) : "0 VND";
			model.addRow(new Object[] {
					stt++,
					dv.getMaDV(),
					dv.getTenDV(),
					giaStr,
					dv.isTrangThai() ? "Hoạt động" : "Không hoạt động"
			});
		}
	}

	private JPanel taoSearchPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(15, 15, 15, 15)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 8, 6, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		// Mã dịch vụ
		JLabel lblMa = new JLabel("Mã dịch vụ:");
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
				new EmptyBorder(6, 8, 6, 8)));
		txtMa.setToolTipText("Nhập mã dịch vụ cần tìm");
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		panel.add(txtMa, gbc);

		// Tên dịch vụ
		JLabel lblTen = new JLabel("Tên dịch vụ:");
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
				new EmptyBorder(6, 8, 6, 8)));
		txtTen.setToolTipText("Nhập tên dịch vụ cần tìm");
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
			taiDuLieuBang(ma.isEmpty() ? null : ma, ten.isEmpty() ? null : ten);
		});

		// Nút Làm mới
		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setForeground(Color.decode("#3A4D66"));
		btnLamMoi.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 12, 6, 12)));
		btnLamMoi.addActionListener(e -> {
			txtMa.setText("");
			txtTen.setText("");
			taiDuLieuBang(null, null);
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.add(btnTimKiem);
		buttonPanel.add(btnLamMoi);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 4;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(buttonPanel, gbc);

		return panel;
	}
}
