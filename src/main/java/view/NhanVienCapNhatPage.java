package view;

import controller.NhanVienController;
import entity.NhanVien;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class NhanVienCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private final NhanVienController nhanVienController = new NhanVienController();

	private JTable table;
	private DefaultTableModel model;
	private JPanel formPanel;
	
	private JTextField txtTen, txtUsername, txtSdt, txtEmail, cbChucVu;
	private JButton btnCapNhat, btnXoa, btnHuy;

	public NhanVienCapNhatPage() {
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

		JLabel title = new JLabel("Cập nhật thông tin nhân viên");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout());
		content.setBackground(Color.WHITE);
		content.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(14, 14, 14, 14)
		));

		// Tạo bảng
		String[] columns = { "#", "Mã NV", "Họ và tên", "Tài khoản", "Email", "Điện thoại", "Chức vụ" };
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
					displayForm(row);
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));

		// Form panel
		formPanel = taoFormPanel();

		// Split pane
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, formPanel);
		splitPane.setDividerLocation(300);
		splitPane.setBorder(null);
		content.add(splitPane, BorderLayout.CENTER);

		return content;
	}

	private void loadDataFromDatabase() {
		model.setRowCount(0);
		List<NhanVien> danhSach = nhanVienController.layTatCaNhanVien();
		for (int i = 0; i < danhSach.size(); i++) {
			NhanVien nv = danhSach.get(i);
			model.addRow(new Object[] { i + 1, nv.getMaNV(), nv.getTenNV(), nv.getUsername(),
					nhanVienController.layEmailTheoUsername(nv.getUsername()), nv.getSdt(), nv.getChucVu() });
		}
	}

	private JPanel taoFormPanel() {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(Color.WHITE);
		wrapper.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(15, 15, 15, 15)
		));

		// Label hướng dẫn
		JLabel lblHuongDan = new JLabel("Chọn nhân viên từ bảng trên để chỉnh sửa");
		lblHuongDan.setFont(new Font("Segoe UI", Font.ITALIC, 14));
		lblHuongDan.setForeground(Color.decode("#8B95A7"));
		wrapper.add(lblHuongDan, BorderLayout.CENTER);

		return wrapper;
	}

	private void displayForm(int row) {
		formPanel.removeAll();
		formPanel.setLayout(new BorderLayout());
		formPanel.setBackground(Color.WHITE);

		String maNV = table.getValueAt(row, 1).toString();

		// Form chính
		JPanel formContainer = new JPanel(new GridBagLayout());
		formContainer.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Row 0: Họ và tên
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.25;
		JLabel lbl = new JLabel("Họ và tên *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtTen = new JTextField(table.getValueAt(row, 2).toString());
		txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTen.setPreferredSize(new Dimension(200, 35));
		txtTen.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtTen, gbc);

		// Row 1: Tên tài khoản
		gbc.gridx = 0;
		gbc.gridy = 1;
		lbl = new JLabel("Tên tài khoản *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtUsername = new JTextField(table.getValueAt(row, 3).toString());
		txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtUsername.setPreferredSize(new Dimension(200, 35));
		txtUsername.setEditable(false);
		txtUsername.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtUsername, gbc);

		// Row 2: Số điện thoại
		gbc.gridx = 0;
		gbc.gridy = 2;
		lbl = new JLabel("Số điện thoại");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtSdt = new JTextField(table.getValueAt(row, 5).toString());
		txtSdt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSdt.setPreferredSize(new Dimension(200, 35));
		txtSdt.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtSdt, gbc);

		// Row 3: Chức vụ
		gbc.gridx = 0;
		gbc.gridy = 3;
		lbl = new JLabel("Chức vụ");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbChucVu = new JTextField(table.getValueAt(row, 6).toString());
		cbChucVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbChucVu.setPreferredSize(new Dimension(200, 35));
		cbChucVu.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(cbChucVu, gbc);

		// Row 4: Email
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.25;
		lbl = new JLabel("Email *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtEmail = new JTextField(table.getValueAt(row, 4).toString());
		txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtEmail.setPreferredSize(new Dimension(150, 35));
		txtEmail.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtEmail, gbc);

		JPanel scrollWrapper = new JPanel(new BorderLayout());
		scrollWrapper.setBackground(Color.WHITE);
		JScrollPane scrollPane = new JScrollPane(formContainer);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);
		scrollWrapper.add(scrollPane, BorderLayout.CENTER);

		// Nút hành động
		JPanel actions = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
		actions.setBackground(Color.WHITE);

		btnCapNhat = new JButton("Cập nhật");
		btnCapNhat.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnCapNhat.setBackground(Color.decode("#00AA00"));
		btnCapNhat.setForeground(Color.WHITE);
		btnCapNhat.setFocusPainted(false);
		btnCapNhat.setBorder(new EmptyBorder(8, 20, 8, 20));
		btnCapNhat.setPreferredSize(new Dimension(120, 40));
		btnCapNhat.addActionListener(e -> xuLyCapNhatNhanVien(maNV));

		btnXoa = new JButton("Xóa");
		btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnXoa.setBackground(Color.decode("#FF0000"));
		btnXoa.setForeground(Color.WHITE);
		btnXoa.setFocusPainted(false);
		btnXoa.setBorder(new EmptyBorder(8, 20, 8, 20));
		btnXoa.setPreferredSize(new Dimension(100, 40));
		btnXoa.addActionListener(e -> xuLyXoaNhanVien(maNV));

		btnHuy = new JButton("Hủy");
		btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnHuy.setBackground(Color.WHITE);
		btnHuy.setForeground(Color.decode("#3A4D66"));
		btnHuy.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 20, 8, 20)
		));
		btnHuy.setPreferredSize(new Dimension(100, 40));

		btnHuy.addActionListener(e -> resetFormPanel());

		actions.add(btnCapNhat);
		actions.add(btnXoa);
		actions.add(btnHuy);

		formPanel.add(scrollWrapper, BorderLayout.CENTER);
		formPanel.add(actions, BorderLayout.SOUTH);
		formPanel.revalidate();
		formPanel.repaint();
	}

	private void xuLyCapNhatNhanVien(String maNV) {
		NhanVien nhanVienCu = timNhanVienTheoMa(maNV);
		if (nhanVienCu == null) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên cần cập nhật", "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		service.NhanVienService.KetQuaXuLy ketQua = nhanVienController.capNhatNhanVien(maNV,
				txtTen.getText(), txtSdt.getText(), txtEmail.getText(), cbChucVu.getText(),
				nhanVienCu.isGioiTinh(), nhanVienCu.getNgaySinh(), nhanVienCu.getNgayVaoLam());

		if (ketQua.thanhCong) {
			JOptionPane.showMessageDialog(this, ketQua.thongBao, "Thành công", JOptionPane.INFORMATION_MESSAGE);
			loadDataFromDatabase();
		} else {
			JOptionPane.showMessageDialog(this, ketQua.thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void xuLyXoaNhanVien(String maNV) {
		int xacNhan = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc muốn xóa nhân viên " + maNV + " không?",
				"Xác nhận xóa nhân viên",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (xacNhan != JOptionPane.YES_OPTION) {
			return;
		}

		service.NhanVienService.KetQuaXuLy ketQua = nhanVienController.xoaNhanVien(maNV);
		if (ketQua.thanhCong) {
			JOptionPane.showMessageDialog(this, ketQua.thongBao, "Thành công", JOptionPane.INFORMATION_MESSAGE);
			loadDataFromDatabase();
			resetFormPanel();
		} else {
			JOptionPane.showMessageDialog(this, ketQua.thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void resetFormPanel() {
		formPanel.removeAll();
		formPanel.setLayout(new BorderLayout());
		JLabel lblHuongDan = new JLabel("Chọn nhân viên từ bảng trên để chỉnh sửa");
		lblHuongDan.setFont(new Font("Segoe UI", Font.ITALIC, 14));
		lblHuongDan.setForeground(Color.decode("#8B95A7"));
		formPanel.add(lblHuongDan, BorderLayout.CENTER);
		formPanel.revalidate();
		formPanel.repaint();
	}

	private NhanVien timNhanVienTheoMa(String maNV) {
		List<NhanVien> danhSach = nhanVienController.timKiemNhanVien(maNV, null);
		return danhSach.isEmpty() ? null : danhSach.get(0);
	}
}
