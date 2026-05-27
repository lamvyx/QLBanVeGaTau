package view;

import controller.KhuyenMaiController;
import entity.KhuyenMai;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private final KhuyenMaiController khuyenMaiController = new KhuyenMaiController();
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private DefaultTableModel model;
	private JTable tableKhuyenMai;

	public KhuyenMaiTraCuuPage() {
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

		JLabel title = new JLabel("Tra cứu khuyến mãi");
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
		String[] columns = { "#", "Mã KM", "Tên khuyến mãi", "Tỷ lệ (%)", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Lấy dữ liệu từ database
		taiDuLieuBang(null, null, "Tất cả");

		tableKhuyenMai = new JTable(model);
		tableKhuyenMai.setRowHeight(50);
		tableKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tableKhuyenMai.getTableHeader().setBackground(MAU_CHINH);
		tableKhuyenMai.getTableHeader().setForeground(Color.WHITE);
		tableKhuyenMai.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		tableKhuyenMai.setGridColor(Color.decode("#E4EBF3"));
		tableKhuyenMai.setSelectionBackground(Color.decode("#B3D9FF"));

		JScrollPane scrollPane = new JScrollPane(tableKhuyenMai);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private void taiDuLieuBang(String ma, String ten, String filter) {
		model.setRowCount(0);
		List<KhuyenMai> ds = khuyenMaiController.timKiemKhuyenMai(ma, ten);
		LocalDate today = LocalDate.now();
		int stt = 1;
		for (KhuyenMai km : ds) {
			String trangThai;
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
						trangThai
				});
			}
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

		// Mã khuyến mãi
		JLabel lblMa = new JLabel("Mã khuyến mãi:");
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
		txtMa.setToolTipText("Nhập mã khuyến mãi cần tìm");
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		panel.add(txtMa, gbc);

		// Tên khuyến mãi
		JLabel lblTen = new JLabel("Tên khuyến mãi:");
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
		txtTen.setToolTipText("Nhập tên khuyến mãi cần tìm");
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 1;
		panel.add(txtTen, gbc);

		// Trạng thái
		JLabel lblTrangThai = new JLabel("Trạng thái:");
		lblTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblTrangThai.setForeground(Color.decode("#2B4B74"));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		panel.add(lblTrangThai, gbc);

		JComboBox<String> cbSapXep = new JComboBox<>();
		cbSapXep.addItem("Tất cả");
		cbSapXep.addItem("Sắp diễn ra");
		cbSapXep.addItem("Đang chạy");
		cbSapXep.addItem("Kết thúc");
		cbSapXep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cbSapXep.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		panel.add(cbSapXep, gbc);

		// Nút Tìm kiếm & Làm mới
		JButton btnTimKiem = new JButton("Tìm kiếm");
		btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnTimKiem.setBackground(MAU_CHINH);
		btnTimKiem.setForeground(Color.WHITE);
		btnTimKiem.setFocusPainted(false);
		btnTimKiem.setBorder(new EmptyBorder(6, 12, 6, 12));
		btnTimKiem.addActionListener(e -> {
			String ma = txtMa.getText().trim();
			String ten = txtTen.getText().trim();
			String filter = String.valueOf(cbSapXep.getSelectedItem());
			taiDuLieuBang(ma.isEmpty() ? null : ma, ten.isEmpty() ? null : ten, filter);
		});

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
			cbSapXep.setSelectedIndex(0);
			taiDuLieuBang(null, null, "Tất cả");
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.add(btnTimKiem);
		buttonPanel.add(btnLamMoi);

		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(buttonPanel, gbc);

		return panel;
	}
}
