package view;

import controller.KhachHangController;
import entity.KhachHang;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class KhachHangCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private final KhachHangController khachHangController = new KhachHangController();

	private JTable table;
	private DefaultTableModel model;
	private JPanel formPanel;
	
	private String selectedMaKH = null;

	// UI Fields
	private JTextField txtMa;
	private JTextField txtTen;
	private JTextField txtSdt;
	private JTextField txtCccd;
	private JTextField txtEmail;
	private JTextField txtDiaChi;
	private JComboBox<String> cbGioiTinh;
	private JComboBox<String> cbLoaiKH;

	// Buttons
	private JButton btnTimKiem, btnLamMoi, btnCapNhat, btnXoa, btnHuy;

	public KhachHangCapNhatPage() {
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

		JLabel title = new JLabel("Cập nhật thông tin khách hàng");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout(0, 14));
		content.setBackground(Color.WHITE);
		content.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(14, 14, 14, 14)
		));

		// dynamic input panel (both search and edit)
		formPanel = new JPanel();
		renderFormPanel();
		content.add(formPanel, BorderLayout.NORTH);

		// Table
		String[] columns = { "#", "Mã KH", "Tên khách hàng", "Số ĐT", "Email", "CCCD", "Loại KH" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		loadDataFromDatabase();

		table = new JTable(model);
		table.setRowHeight(50);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setBackground(MAU_CHINH);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.setGridColor(Color.decode("#E4EBF3"));
		table.setSelectionBackground(Color.decode("#B3D9FF"));

		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					selectedMaKH = table.getValueAt(row, 1).toString();
					renderFormPanel();
				}
			}
		});

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
			model.addRow(new Object[] { i + 1, kh.getMaKH(), kh.getTenKH(), kh.getSdt(), kh.getEmail(),
					kh.getCccd(), kh.isLoaiKH() ? "VIP" : "Thường" });
		}
	}

	private void renderFormPanel() {
		formPanel.removeAll();
		formPanel.setLayout(new GridBagLayout());
		formPanel.setBackground(Color.WHITE);
		formPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(15, 15, 15, 15)
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 10, 8, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		if (selectedMaKH == null) {
			// --- STATE A: SEARCH STATE ---
			
			// Mã khách hàng
			JLabel lblMa = new JLabel("Mã khách hàng:");
			lblMa.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblMa.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
			formPanel.add(lblMa, gbc);

			txtMa = new JTextField();
			txtMa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			txtMa.setPreferredSize(new Dimension(0, 35));
			txtMa.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)
			));
			gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8;
			formPanel.add(txtMa, gbc);

			// Tên khách hàng
			JLabel lblTen = new JLabel("Tên khách hàng:");
			lblTen.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblTen.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.2;
			formPanel.add(lblTen, gbc);

			txtTen = new JTextField();
			txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			txtTen.setPreferredSize(new Dimension(0, 35));
			txtTen.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)
			));
			gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.8;
			formPanel.add(txtTen, gbc);

			// Số điện thoại
			JLabel lblSdt = new JLabel("Số điện thoại:");
			lblSdt.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblSdt.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
			formPanel.add(lblSdt, gbc);

			txtSdt = new JTextField();
			txtSdt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			txtSdt.setPreferredSize(new Dimension(0, 35));
			txtSdt.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)
			));
			gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.8;
			formPanel.add(txtSdt, gbc);

			// Spacer to balance grid
			JLabel lblSpacer = new JLabel("");
			gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
			formPanel.add(lblSpacer, gbc);
			gbc.gridwidth = 1; // Reset

			// Buttons
			btnTimKiem = new JButton("Tìm kiếm");
			btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
			btnTimKiem.setBackground(MAU_CHINH);
			btnTimKiem.setForeground(Color.WHITE);
			btnTimKiem.setFocusPainted(false);
			btnTimKiem.setPreferredSize(new Dimension(120, 38));
			btnTimKiem.addActionListener(e -> xuLyTimKiem());

			btnLamMoi = new JButton("Làm mới");
			btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 13));
			btnLamMoi.setBackground(Color.WHITE);
			btnLamMoi.setForeground(Color.decode("#3A4D66"));
			btnLamMoi.setFocusPainted(false);
			btnLamMoi.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 12, 6, 12)
			));
			btnLamMoi.setPreferredSize(new Dimension(120, 38));
			btnLamMoi.addActionListener(e -> {
				txtMa.setText(""); txtTen.setText(""); txtSdt.setText("");
				loadDataFromDatabase();
			});

			java.awt.FlowLayout fl = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0);
			JPanel buttonPanel = new JPanel(fl);
			buttonPanel.setOpaque(false);
			buttonPanel.add(btnTimKiem);
			buttonPanel.add(btnLamMoi);

			gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.NONE;
			formPanel.add(buttonPanel, gbc);

		} else {
			// --- STATE B: EDIT STATE ---
			KhachHang kh = timKhachHangTheoMa(selectedMaKH);
			if (kh == null) {
				selectedMaKH = null;
				renderFormPanel();
				return;
			}

			// Row 0: Mã khách hàng & Tên khách hàng
			JLabel lblMa = new JLabel("Mã khách hàng:");
			lblMa.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblMa.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
			formPanel.add(lblMa, gbc);

			txtMa = new JTextField(kh.getMaKH());
			txtMa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			txtMa.setEditable(false);
			txtMa.setPreferredSize(new Dimension(0, 35));
			txtMa.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)
			));
			gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8;
			formPanel.add(txtMa, gbc);

			JLabel lblTen = new JLabel("Tên khách hàng *:");
			lblTen.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblTen.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.2;
			formPanel.add(lblTen, gbc);

			txtTen = new JTextField(kh.getTenKH());
			txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			txtTen.setPreferredSize(new Dimension(0, 35));
			txtTen.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)
			));
			gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.8;
			formPanel.add(txtTen, gbc);

			// Row 1: Số điện thoại & CCCD
			JLabel lblSdt = new JLabel("Số điện thoại *:");
			lblSdt.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblSdt.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
			formPanel.add(lblSdt, gbc);

			txtSdt = new JTextField(kh.getSdt() != null ? kh.getSdt() : "");
			txtSdt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			txtSdt.setPreferredSize(new Dimension(0, 35));
			txtSdt.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)
			));
			gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.8;
			formPanel.add(txtSdt, gbc);

			JLabel lblCccd = new JLabel("CCCD/Passport *:");
			lblCccd.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblCccd.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.2;
			formPanel.add(lblCccd, gbc);

			txtCccd = new JTextField(kh.getCccd() != null ? kh.getCccd() : "");
			txtCccd.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			txtCccd.setPreferredSize(new Dimension(0, 35));
			txtCccd.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)
			));
			gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.8;
			formPanel.add(txtCccd, gbc);

			// Row 2: Email & Địa chỉ
			JLabel lblEmail = new JLabel("Email:");
			lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblEmail.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
			formPanel.add(lblEmail, gbc);

			txtEmail = new JTextField(kh.getEmail() != null ? kh.getEmail() : "");
			txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			txtEmail.setPreferredSize(new Dimension(0, 35));
			txtEmail.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)
			));
			gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.8;
			formPanel.add(txtEmail, gbc);

			JLabel lblDiaChi = new JLabel("Địa chỉ:");
			lblDiaChi.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblDiaChi.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0.2;
			formPanel.add(lblDiaChi, gbc);

			txtDiaChi = new JTextField(kh.getDiaChi() != null ? kh.getDiaChi() : "");
			txtDiaChi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			txtDiaChi.setPreferredSize(new Dimension(0, 35));
			txtDiaChi.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)
			));
			gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.8;
			formPanel.add(txtDiaChi, gbc);

			// Row 3: Giới tính & Loại khách hàng
			JLabel lblGioiTinh = new JLabel("Giới tính:");
			lblGioiTinh.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblGioiTinh.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.2;
			formPanel.add(lblGioiTinh, gbc);

			cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
			cbGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			cbGioiTinh.setPreferredSize(new Dimension(0, 35));
			cbGioiTinh.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
			cbGioiTinh.setSelectedIndex(kh.isGioiTinh() ? 0 : 1);
			gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.8;
			formPanel.add(cbGioiTinh, gbc);

			JLabel lblLoaiKH = new JLabel("Loại khách hàng:");
			lblLoaiKH.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblLoaiKH.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0.2;
			formPanel.add(lblLoaiKH, gbc);

			cbLoaiKH = new JComboBox<>(new String[]{"Khách hàng thường", "Khách hàng VIP"});
			cbLoaiKH.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			cbLoaiKH.setPreferredSize(new Dimension(0, 35));
			cbLoaiKH.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
			cbLoaiKH.setSelectedIndex(kh.isLoaiKH() ? 1 : 0);
			gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 0.8;
			formPanel.add(cbLoaiKH, gbc);

			// Row 4: Action Buttons (Cập nhật, Xóa, Hủy)
			btnCapNhat = new JButton("Cập nhật");
			btnCapNhat.setFont(new Font("Segoe UI", Font.BOLD, 13));
			btnCapNhat.setBackground(Color.decode("#00AA00"));
			btnCapNhat.setForeground(Color.WHITE);
			btnCapNhat.setFocusPainted(false);
			btnCapNhat.setPreferredSize(new Dimension(120, 38));
			btnCapNhat.addActionListener(e -> xuLyCapNhatKhachHang(selectedMaKH));

			btnXoa = new JButton("Xóa");
			btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 13));
			btnXoa.setBackground(Color.decode("#DD3333"));
			btnXoa.setForeground(Color.WHITE);
			btnXoa.setFocusPainted(false);
			btnXoa.setPreferredSize(new Dimension(100, 38));
			btnXoa.addActionListener(e -> xuLyXoaKhachHang(selectedMaKH));

			btnHuy = new JButton("Hủy");
			btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 13));
			btnHuy.setBackground(Color.WHITE);
			btnHuy.setForeground(Color.decode("#3A4D66"));
			btnHuy.setFocusPainted(false);
			btnHuy.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 16, 6, 16)
			));
			btnHuy.setPreferredSize(new Dimension(100, 38));
			btnHuy.addActionListener(e -> {
				selectedMaKH = null;
				table.clearSelection();
				renderFormPanel();
			});

			java.awt.FlowLayout fl = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0);
			JPanel buttonPanel = new JPanel(fl);
			buttonPanel.setOpaque(false);
			buttonPanel.add(btnCapNhat);
			buttonPanel.add(btnXoa);
			buttonPanel.add(btnHuy);

			gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.NONE;
			formPanel.add(buttonPanel, gbc);
		}

		formPanel.revalidate();
		formPanel.repaint();
	}

	private void xuLyTimKiem() {
		String ma = txtMa.getText().trim();
		String ten = txtTen.getText().trim();
		String sdt = txtSdt.getText().trim();

		model.setRowCount(0);
		List<KhachHang> results = khachHangController.timKiemKhachHang(ma, ten, sdt);
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
	}

	private void xuLyCapNhatKhachHang(String maKH) {
		boolean gioiTinh = "Nam".equals(cbGioiTinh.getSelectedItem());
		boolean loaiKH = cbLoaiKH.getSelectedIndex() > 0;

		service.KhachHangService.KetQuaXuLy ketQua = khachHangController.capNhatKhachHang(maKH,
				txtTen.getText().trim(), txtSdt.getText().trim(), txtCccd.getText().trim(), txtEmail.getText().trim(), 
				txtDiaChi.getText().trim(), gioiTinh, loaiKH);

		if (ketQua.thanhCong) {
			JOptionPane.showMessageDialog(this, ketQua.thongBao, "Thành công", JOptionPane.INFORMATION_MESSAGE);
			loadDataFromDatabase();
			selectedMaKH = null;
			table.clearSelection();
			renderFormPanel();
		} else {
			JOptionPane.showMessageDialog(this, ketQua.thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void xuLyXoaKhachHang(String maKH) {
		int xacNhan = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc muốn xóa khách hàng " + maKH + " không?",
				"Xác nhận xóa khách hàng",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (xacNhan != JOptionPane.YES_OPTION) {
			return;
		}

		service.KhachHangService.KetQuaXuLy ketQua = khachHangController.xoaKhachHang(maKH);
		if (ketQua.thanhCong) {
			JOptionPane.showMessageDialog(this, ketQua.thongBao, "Thành công", JOptionPane.INFORMATION_MESSAGE);
			loadDataFromDatabase();
			selectedMaKH = null;
			table.clearSelection();
			renderFormPanel();
		} else {
			JOptionPane.showMessageDialog(this, ketQua.thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private KhachHang timKhachHangTheoMa(String maKH) {
		List<KhachHang> danhSach = khachHangController.timKiemKhachHang(maKH, null, null);
		return danhSach.isEmpty() ? null : danhSach.get(0);
	}
}
