package view;

import dao.NhanVien_DAO;
import entity.NhanVien;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class NhanVienCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private final NhanVien_DAO nhanVienDAO = new NhanVien_DAO();

	private JTable table;
	private DefaultTableModel tableModel;
	private TableRowSorter<DefaultTableModel> sorter;
	private JLabel lblAnh;

	private JTextField txtMaNV;
	private JTextField txtHoTen;
	private JTextField txtEmail;
	private JTextField txtNgaySinh;
	private JTextField txtNgayVaoLam;
	private JComboBox<String> cbGioiTinh;
	private JComboBox<String> cbChucVu;
	private JTextField txtSdt;
	private JRadioButton rdDangLam;
	private JRadioButton rdNgungLam;

	public NhanVienCapNhatPage() {
		setLayout(new BorderLayout(0, 12));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoHeader(), BorderLayout.NORTH);
		add(taoNoiDungResponsive(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel lblTitle = new JLabel("Cập nhật thông tin nhân viên");
		lblTitle.setFont(AppTheme.font(Font.BOLD, 30));
		lblTitle.setForeground(AppTheme.PRIMARY);

		JLabel lblSub = new JLabel("Chọn nhân viên trong danh sách, cập nhật thông tin và lưu thay đổi");
		lblSub.setFont(AppTheme.font(Font.PLAIN, 12));
		lblSub.setForeground(AppTheme.TEXT_MUTED);

		panel.add(lblTitle);
		panel.add(Box.createVerticalStrut(4));
		panel.add(lblSub);
		return panel;
	}

	private JScrollPane taoNoiDungResponsive() {
		JPanel wrapper = new JPanel(new BorderLayout(0, 12));
		wrapper.setOpaque(false);

		wrapper.add(taoKhuVucBang(), BorderLayout.NORTH);
		wrapper.add(taoKhuVucDuLieu(), BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane(wrapper);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getVerticalScrollBar().setUnitIncrement(18);
		return scrollPane;
	}

	private JPanel taoKhuVucBang() {
		JPanel card = new JPanel(new BorderLayout(0, 8));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JPanel top = new JPanel(new BorderLayout());
		top.setOpaque(false);

		JLabel lbl = new JLabel("Danh sách nhân sự hiện tại");
		lbl.setFont(AppTheme.font(Font.BOLD, 13));
		lbl.setForeground(AppTheme.TEXT_PRIMARY);

		JTextField txtSearch = new JTextField("Tìm kiếm mã/tên...");
		txtSearch.setPreferredSize(new Dimension(220, 32));
		txtSearch.setFont(AppTheme.font(Font.PLAIN, 12));
		txtSearch.setForeground(AppTheme.TEXT_MUTED);
		txtSearch.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(AppTheme.BORDER),
				new EmptyBorder(6, 8, 6, 8)));

		top.add(lbl, BorderLayout.WEST);
		top.add(txtSearch, BorderLayout.EAST);

		String[] cols = { "Mã NV", "Họ tên", "Username", "Email", "Số điện thoại", "Giới tính", "Chức vụ", "Trạng thái" };
		tableModel = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		napDanhSachNhanVien(tableModel);

		table = new JTable(tableModel);
		sorter = new TableRowSorter<>(tableModel);
		table.setRowSorter(sorter);
		AppTheme.styleTable(table);
		table.setRowHeight(32);
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					doDuLieuLenForm(row);
				}
			}
		});

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		scroll.setPreferredSize(new Dimension(10, 170));

		card.add(top, BorderLayout.NORTH);
		card.add(scroll, BorderLayout.CENTER);
		return card;
	}

	private void napDanhSachNhanVien(DefaultTableModel model) {
		List<NhanVien> dsNhanVien = nhanVienDAO.layTatCa();
		for (NhanVien nhanVien : dsNhanVien) {
			model.addRow(new Object[] {
				nhanVien.getMaNV(),
				nhanVien.getTenNV(),
				nhanVien.getUsername(),
				nhanVien.getEmail(),
				nhanVien.getSdt(),
				nhanVien.isGioiTinh() ? "Nam" : "Nữ",
				dienGiaiChucVu(nhanVien.getChucVu()),
				nhanVien.isTrangThai() ? "Đang làm việc" : "Ngừng làm việc"
			});
		}
	}

	private String dienGiaiChucVu(String chucVu) {
		if (chucVu == null) {
			return "";
		}
		String normalized = chucVu.trim().toLowerCase();
		if (normalized.contains("quản lý") || normalized.contains("quan ly") || normalized.contains("admin")) {
			return "Quản lý ga";
		}
		if (normalized.contains("bán vé") || normalized.contains("ban ve")) {
			return "Nhân viên bán vé";
		}
		if (normalized.contains("kỹ thuật") || normalized.contains("ky thuat")) {
			return "Kỹ thuật viên";
		}
		if (normalized.contains("kế toán") || normalized.contains("ke toan")) {
			return "Kế toán trưởng";
		}
		return chucVu;
	}

	private JSplitPane taoKhuVucDuLieu() {
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, taoCardAnh(), taoCardForm());
		split.setResizeWeight(0.25);
		split.setBorder(BorderFactory.createEmptyBorder());
		split.setOpaque(false);
		return split;
	}

	private JPanel taoCardAnh() {
		JPanel card = new JPanel(new BorderLayout(0, 10));
		card.setMinimumSize(new Dimension(260, 260));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		lblAnh = new JLabel("Ảnh 3x4", JLabel.CENTER);
		lblAnh.setOpaque(true);
		lblAnh.setBackground(java.awt.Color.WHITE);
		lblAnh.setFont(AppTheme.font(Font.PLAIN, 12));
		lblAnh.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));

		JPanel buttons = new JPanel(new GridBagLayout());
		buttons.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 6, 0);

		JButton btnThayAnh = new JButton("Thay đổi ảnh");
		AppTheme.styleSecondaryButton(btnThayAnh);
		btnThayAnh.setPreferredSize(new Dimension(140, 34));
		btnThayAnh.addActionListener(e -> chonAnh());

		JButton btnLichSu = new JButton("Xem lịch sử");
		AppTheme.styleSecondaryButton(btnLichSu);
		btnLichSu.setPreferredSize(new Dimension(140, 34));

		buttons.add(btnThayAnh, gbc);
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		buttons.add(btnLichSu, gbc);

		card.add(lblAnh, BorderLayout.CENTER);
		card.add(buttons, BorderLayout.SOUTH);
		return card;
	}

	private JPanel taoCardForm() {
		JPanel card = new JPanel(new BorderLayout(0, 10));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 0, 10, 12);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;

		txtMaNV = new JTextField();
		txtHoTen = new JTextField();
		txtEmail = new JTextField();
		txtNgaySinh = new JTextField("yyyy-mm-dd");
		txtNgayVaoLam = new JTextField("yyyy-mm-dd");
		cbGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });
		cbChucVu = new JComboBox<>(new String[] { "Nhân viên bán vé", "Quản lý ga", "Kỹ thuật viên", "Kế toán trưởng" });
		txtSdt = new JTextField();

		txtMaNV.setEditable(false);
		styleInput(txtMaNV);
		styleInput(txtHoTen);
		styleInput(txtEmail);
		styleInput(txtNgaySinh);
		styleInput(txtNgayVaoLam);
		styleInput(txtSdt);
		styleCombo(cbGioiTinh);
		styleCombo(cbChucVu);
		cbChucVu.setSelectedIndex(0);

		themDong(form, gbc, 0, 0, "MÃ NHÂN VIÊN (READ-ONLY)", txtMaNV);
		themDong(form, gbc, 2, 0, "HỌ VÀ TÊN", txtHoTen);
		themDong(form, gbc, 0, 1, "EMAIL", txtEmail);
		themDong(form, gbc, 2, 1, "SỐ ĐIỆN THOẠI", txtSdt);
		themDong(form, gbc, 0, 2, "GIỚI TÍNH", cbGioiTinh);
		themDong(form, gbc, 2, 2, "CHỨC VỤ", cbChucVu);
		themDong(form, gbc, 0, 3, "NGÀY SINH", txtNgaySinh);
		themDong(form, gbc, 2, 3, "NGÀY VÀO LÀM", txtNgayVaoLam);

		JPanel trangThaiPanel = taoTrangThaiPanel();
		themDong(form, gbc, 0, 4, "TRẠNG THÁI", trangThaiPanel);

		card.add(form, BorderLayout.CENTER);
		card.add(taoActionPanel(), BorderLayout.SOUTH);
		return card;
	}

	private JPanel taoTrangThaiPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		panel.setOpaque(false);

		rdDangLam = new JRadioButton("Đang làm việc", true);
		rdNgungLam = new JRadioButton("Ngừng làm việc");

		ButtonGroup group = new ButtonGroup();
		group.add(rdDangLam);
		group.add(rdNgungLam);

		styleRadio(rdDangLam);
		styleRadio(rdNgungLam);

		panel.add(rdDangLam);
		panel.add(rdNgungLam);
		return panel;
	}

	private JPanel taoActionPanel() {
		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);
		actions.setBorder(new EmptyBorder(8, 0, 0, 0));

		JButton btnCapNhat = new JButton("Cập nhật");
		AppTheme.stylePrimaryButton(btnCapNhat);
		btnCapNhat.addActionListener(e -> capNhatNhanVien());

		JButton btnXoa = new JButton("Xóa");
		btnXoa.setFont(AppTheme.font(Font.BOLD, 13));
		btnXoa.setBackground(Color.decode("#DC2626"));
		btnXoa.setForeground(Color.WHITE);
		btnXoa.setFocusPainted(false);
		btnXoa.setBorder(new EmptyBorder(8, 16, 8, 16));
		btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		JButton btnHuy = new JButton("Hủy");
		AppTheme.styleSecondaryButton(btnHuy);
		btnHuy.addActionListener(e -> xoaForm());
		btnCapNhat.addActionListener(e -> {
		});
		btnXoa.addActionListener(e -> xoaNhanVien());

		actions.add(btnCapNhat);
		actions.add(btnXoa);
		actions.add(btnHuy);
		return actions;
	}

	private void doDuLieuLenForm(int row) {
		txtMaNV.setText(String.valueOf(table.getValueAt(row, 0)));
		txtHoTen.setText(String.valueOf(table.getValueAt(row, 1)));
		txtEmail.setText(String.valueOf(table.getValueAt(row, 3)));
		txtSdt.setText(String.valueOf(table.getValueAt(row, 4)));
		cbGioiTinh.setSelectedItem(String.valueOf(table.getValueAt(row, 5)));
		cbChucVu.setSelectedItem(String.valueOf(table.getValueAt(row, 6)));
		txtNgaySinh.setText("1995-01-01");
		txtNgayVaoLam.setText("2020-01-01");

		String trangThai = String.valueOf(table.getValueAt(row, 7));
		rdDangLam.setSelected("Đang làm việc".equalsIgnoreCase(trangThai));
		rdNgungLam.setSelected("Ngừng làm việc".equalsIgnoreCase(trangThai));
	}

	private void xoaForm() {
		txtMaNV.setText("");
		txtHoTen.setText("");
		txtEmail.setText("");
		txtNgaySinh.setText("yyyy-mm-dd");
		txtNgayVaoLam.setText("yyyy-mm-dd");
		txtSdt.setText("");
		cbGioiTinh.setSelectedIndex(0);
		cbChucVu.setSelectedIndex(0);
		rdDangLam.setSelected(true);
		lblAnh.setIcon(null);
		lblAnh.setText("Ảnh 3x4");
	}

	private void capNhatNhanVien() {
		JOptionPane.showMessageDialog(this, "Chức năng cập nhật đang dùng dữ liệu từ bảng hiện tại. Nếu bạn muốn, tôi có thể nối luôn nút Cập nhật vào DAO tiếp theo.");
	}

	private void xoaNhanVien() {
		String maNV = txtMaNV.getText().trim();
		if (maNV.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Chọn một nhân viên để xóa.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Xóa nhân viên " + maNV + " khỏi hệ thống?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}

		if (!nhanVienDAO.xoa(maNV)) {
			JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên.");
			return;
		}

		int rowToRemove = -1;
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			if (maNV.equals(String.valueOf(tableModel.getValueAt(i, 0)))) {
				rowToRemove = i;
				break;
			}
		}
		if (rowToRemove >= 0) {
			tableModel.removeRow(rowToRemove);
		}

		xoaForm();
		JOptionPane.showMessageDialog(this, "Đã xóa nhân viên " + maNV + ".");
	}

	private void chonAnh() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
		int result = chooser.showOpenDialog(this);
		if (result != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File file = chooser.getSelectedFile();
		ImageIcon icon = new ImageIcon(file.getAbsolutePath());
		Image scaled = icon.getImage().getScaledInstance(150, 180, Image.SCALE_SMOOTH);
		lblAnh.setIcon(new ImageIcon(scaled));
		lblAnh.setText("");
	}

	private void themDong(JPanel form, GridBagConstraints gbc, int gridX, int gridY, String label, java.awt.Component field) {
		gbc.gridx = gridX;
		gbc.gridy = gridY;
		gbc.weightx = 0.16;
		JLabel lbl = new JLabel(label);
		lbl.setFont(AppTheme.font(Font.BOLD, 12));
		lbl.setForeground(AppTheme.TEXT_PRIMARY);
		form.add(lbl, gbc);

		gbc.gridx = gridX + 1;
		gbc.weightx = 0.34;
		form.add(field, gbc);
	}

	private void styleInput(JTextField field) {
		field.setFont(AppTheme.font(Font.PLAIN, 13));
		field.setPreferredSize(new Dimension(170, 34));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(AppTheme.BORDER),
				new EmptyBorder(6, 8, 6, 8)));
	}

	private void styleCombo(JComboBox<String> combo) {
		combo.setFont(AppTheme.font(Font.PLAIN, 13));
		combo.setPreferredSize(new Dimension(170, 34));
		combo.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
	}

	private void styleRadio(JRadioButton radio) {
		radio.setOpaque(false);
		radio.setFont(AppTheme.font(Font.PLAIN, 12));
		radio.setForeground(AppTheme.TEXT_PRIMARY);
	}
}
