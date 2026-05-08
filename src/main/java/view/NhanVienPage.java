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
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class NhanVienPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = AppTheme.PRIMARY;
	private static final int COL_MA_NV = 0;
	private static final int COL_HO_TEN = 1;
	private static final int COL_USERNAME = 2;
	private static final int COL_EMAIL = 3;
	private static final int COL_SDT = 4;
	private static final int COL_CHUC_VU = 6;

	private final NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
	private final DefaultTableModel model;
	private final JTable table;
	private final TableRowSorter<DefaultTableModel> sorter;
	private final JTextField txtTimKiem = new JTextField();
	private final JComboBox<String> cbChucVu = new JComboBox<>(new String[] { "Tất cả", "Quản lý ga", "Nhân viên bán vé", "Kỹ thuật viên", "Kế toán trưởng" });

	public NhanVienPage() {
		setLayout(new BorderLayout(0, 12));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		String[] columns = { "Mã NV", "Họ tên", "Username", "Email", "Số điện thoại", "Giới tính", "Chức vụ", "Trạng thái" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		sorter = new TableRowSorter<>(model);

		add(taoHeaderPanel(), BorderLayout.NORTH);
		add(taoContentPanel(), BorderLayout.CENTER);

		refreshData();
	}

	private JPanel taoHeaderPanel() {
		JPanel header = new JPanel(new BorderLayout(0, 10));
		header.setOpaque(false);

		JPanel left = new JPanel();
		left.setOpaque(false);
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

		JLabel title = new JLabel("Quản lý Nhân viên");
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setForeground(MAU_CHINH);

		JLabel subtitle = new JLabel("Thêm, sửa, xóa và tìm nhân sự trực tiếp từ dữ liệu SQL");
		subtitle.setFont(AppTheme.font(Font.PLAIN, 12));
		subtitle.setForeground(AppTheme.TEXT_MUTED);

		left.add(title);
		left.add(Box.createVerticalStrut(4));
		left.add(subtitle);

		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);

		JButton btnThem = new JButton("+ Thêm");
		AppTheme.stylePrimaryButton(btnThem);
		btnThem.addActionListener(e -> moFormThem());

		JButton btnSua = new JButton("Sửa");
		AppTheme.styleSecondaryButton(btnSua);
		btnSua.addActionListener(e -> moFormSua());

		JButton btnXoa = new JButton("Xóa");
		btnXoa.setFont(AppTheme.font(Font.BOLD, 13));
		btnXoa.setBackground(Color.decode("#DC2626"));
		btnXoa.setForeground(Color.WHITE);
		btnXoa.setFocusPainted(false);
		btnXoa.setBorder(new EmptyBorder(8, 16, 8, 16));
		btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnXoa.addActionListener(e -> xoaNhanVien());

		JButton btnLamMoi = new JButton("Làm mới");
		AppTheme.styleSecondaryButton(btnLamMoi);
		btnLamMoi.addActionListener(e -> refreshData());

		actions.add(btnThem);
		actions.add(btnSua);
		actions.add(btnXoa);
		actions.add(btnLamMoi);

		header.add(left, BorderLayout.WEST);
		header.add(actions, BorderLayout.EAST);
		return header;
	}

	private JPanel taoContentPanel() {
		JPanel content = new JPanel(new BorderLayout(0, 12));
		content.setOpaque(false);

		content.add(taoThanhTimKiem(), BorderLayout.NORTH);

		AppTheme.styleTable(table);
		table.setRowHeight(32);
		table.setSelectionBackground(Color.decode("#B3D9FF"));
		table.setRowSorter(sorter);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private JPanel taoThanhTimKiem() {
		JPanel searchCard = new JPanel(new BorderLayout(0, 10));
		searchCard.setBackground(AppTheme.CARD_BG);
		searchCard.setBorder(AppTheme.cardBorder());

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel lblKeyword = new JLabel("Mã / tên / username");
		lblKeyword.setFont(AppTheme.font(Font.BOLD, 12));
		lblKeyword.setForeground(AppTheme.TEXT_PRIMARY);
		gbc.gridx = 0;
		gbc.gridy = 0;
		form.add(lblKeyword, gbc);

		txtTimKiem.setPreferredSize(new Dimension(220, 34));
		txtTimKiem.setFont(AppTheme.font(Font.PLAIN, 13));
		txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(6, 8, 6, 8)));
		gbc.gridx = 1;
		gbc.weightx = 1;
		form.add(txtTimKiem, gbc);

		JLabel lblChucVu = new JLabel("Chức vụ");
		lblChucVu.setFont(AppTheme.font(Font.BOLD, 12));
		lblChucVu.setForeground(AppTheme.TEXT_PRIMARY);
		gbc.gridx = 2;
		gbc.weightx = 0;
		form.add(lblChucVu, gbc);

		cbChucVu.setPreferredSize(new Dimension(180, 34));
		cbChucVu.setFont(AppTheme.font(Font.PLAIN, 13));
		cbChucVu.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		gbc.gridx = 3;
		form.add(cbChucVu, gbc);

		JButton btnTim = new JButton("Tìm kiếm");
		AppTheme.stylePrimaryButton(btnTim);
		btnTim.addActionListener(e -> apDungBoLoc());
		gbc.gridx = 4;
		form.add(btnTim, gbc);

		JButton btnXoaLoc = new JButton("Xóa lọc");
		AppTheme.styleSecondaryButton(btnXoaLoc);
		btnXoaLoc.addActionListener(e -> xoaBoLoc());
		gbc.gridx = 5;
		form.add(btnXoaLoc, gbc);

		txtTimKiem.addActionListener(e -> apDungBoLoc());
		cbChucVu.addActionListener(e -> apDungBoLoc());

		searchCard.add(form, BorderLayout.CENTER);
		return searchCard;
	}

	private void refreshData() {
		model.setRowCount(0);
		for (NhanVien nhanVien : nhanVienDAO.layTatCa()) {
			model.addRow(new Object[] {
				nhanVien.getMaNV(),
				nhanVien.getTenNV(),
				nhanVien.getUsername(),
				nhanVien.getEmail(),
				nhanVien.getSdt(),
				nhanVien.isGioiTinh() ? "Nam" : "Nữ",
				nhanVien.getChucVu(),
				nhanVien.isTrangThai() ? "Đang làm việc" : "Ngừng làm việc"
			});
		}
		apDungBoLoc();
	}

	private void xoaBoLoc() {
		txtTimKiem.setText("");
		cbChucVu.setSelectedIndex(0);
		sorter.setRowFilter(null);
	}

	private void apDungBoLoc() {
		String keyword = txtTimKiem.getText().trim().toLowerCase();
		String chucVu = String.valueOf(cbChucVu.getSelectedItem());
		List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();

		if (!keyword.isEmpty()) {
			filters.add(new RowFilter<DefaultTableModel, Integer>() {
				@Override
				public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
					String ma = String.valueOf(entry.getValue(COL_MA_NV)).toLowerCase();
					String ten = String.valueOf(entry.getValue(COL_HO_TEN)).toLowerCase();
					String username = String.valueOf(entry.getValue(COL_USERNAME)).toLowerCase();
					String email = String.valueOf(entry.getValue(COL_EMAIL)).toLowerCase();
					String sdt = String.valueOf(entry.getValue(COL_SDT)).toLowerCase();
					return ma.contains(keyword) || ten.contains(keyword) || username.contains(keyword)
						|| email.contains(keyword) || sdt.contains(keyword);
				}
			});
		}

		if (!"Tất cả".equals(chucVu)) {
			filters.add(RowFilter.regexFilter("(?i)^" + java.util.regex.Pattern.quote(chucVu) + "$", COL_CHUC_VU));
		}

		if (filters.isEmpty()) {
			sorter.setRowFilter(null);
		} else {
			sorter.setRowFilter(RowFilter.andFilter(filters));
		}
	}

	private void moFormThem() {
		NhanVienFormState state = new NhanVienFormState();
		state.maNV = taoMaNhanVienMoi();
		state.username = state.maNV;
		int result = moHopThoaiNhanVien("Thêm nhân viên", state, true);
		if (result == JOptionPane.OK_OPTION) {
			NhanVien nhanVien = taoNhanVienTuState(state);
			if (nhanVien != null && nhanVienDAO.them(nhanVien, state.matKhau, state.vaiTro)) {
				JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công.");
				refreshData();
				return;
			}
			JOptionPane.showMessageDialog(this, "Không thể thêm nhân viên.");
		}
	}

	private void moFormSua() {
		NhanVien hienTai = nhanVienDangChon();
		if (hienTai == null) {
			JOptionPane.showMessageDialog(this, "Chọn một nhân viên để sửa.");
			return;
		}

		NhanVienFormState state = taiStateTuNhanVien(hienTai);
		int result = moHopThoaiNhanVien("Sửa nhân viên", state, false);
		if (result == JOptionPane.OK_OPTION) {
			NhanVien capNhat = taoNhanVienTuState(state);
			String selectedMa = hienTai.getMaNV();
			String targetMa = capNhat == null ? "(null)" : capNhat.getMaNV();
			System.out.println("DEBUG update selectedMa=" + selectedMa + " targetMa=" + targetMa);
			JOptionPane.showMessageDialog(this, "DEBUG update selected=" + selectedMa + " target=" + targetMa);
			if (capNhat != null && nhanVienDAO.capNhat(capNhat)) {
				JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công.");
				refreshData();
				return;
			}
			JOptionPane.showMessageDialog(this, "Không thể cập nhật nhân viên.");
		}
	}

	private void xoaNhanVien() {
		NhanVien hienTai = nhanVienDangChon();
		if (hienTai == null) {
			JOptionPane.showMessageDialog(this, "Chọn một nhân viên để xóa.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Xóa nhân viên " + hienTai.getMaNV() + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}

		if (nhanVienDAO.xoa(hienTai.getMaNV())) {
			JOptionPane.showMessageDialog(this, "Đã xóa nhân viên.");
			refreshData();
			return;
		}
		JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên.");
	}

	private NhanVien nhanVienDangChon() {
		int viewRow = table.getSelectedRow();
		if (viewRow < 0) {
			return null;
		}
		int modelRow = table.convertRowIndexToModel(viewRow);
		String maNV = String.valueOf(model.getValueAt(modelRow, COL_MA_NV));
		return nhanVienDAO.layTheoMa(maNV);
	}

	private int moHopThoaiNhanVien(String title, NhanVienFormState state, boolean laThem) {
		JPanel panel = taoFormNhanVien(state, laThem);
		JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), title, JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setContentPane(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
		return state.ketQua;
	}

	private JPanel taoFormNhanVien(NhanVienFormState state, boolean laThem) {
		JPanel panel = new JPanel(new BorderLayout(0, 12));
		panel.setBackground(AppTheme.PAGE_BG);
		panel.setBorder(new EmptyBorder(16, 16, 16, 16));

		JPanel form = new JPanel(new GridBagLayout());
		form.setBackground(AppTheme.CARD_BG);
		form.setBorder(AppTheme.cardBorder());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 10, 8, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		state.txtMaNV = taoField(state.maNV, false);
		state.txtUsername = taoField(state.username, true);
		state.txtHoTen = taoField(state.hoTen, false);
		state.txtEmail = taoField(state.email, false);
		state.txtSdt = taoField(state.sdt, false);
		state.txtNgaySinh = taoField(state.ngaySinh == null ? "" : state.ngaySinh.toString(), false);
		state.txtNgayVaoLam = taoField(state.ngayVaoLam == null ? "" : state.ngayVaoLam.toString(), false);
		state.cbGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });
		state.cbChucVu = new JComboBox<>(new String[] { "Nhân viên bán vé", "Quản lý ga", "Kỹ thuật viên", "Kế toán trưởng" });
		state.rdDangLam = new JRadioButton("Đang làm việc", state.trangThai);
		state.rdNgungLam = new JRadioButton("Ngừng làm việc", !state.trangThai);

		state.cbGioiTinh.setSelectedIndex(state.gioiTinh ? 0 : 1);
		state.cbChucVu.setSelectedItem(state.chucVu == null || state.chucVu.isBlank() ? "Nhân viên bán vé" : state.chucVu);

		styleNhapLieu(state.txtMaNV);
		styleNhapLieu(state.txtUsername);
		styleNhapLieu(state.txtHoTen);
		styleNhapLieu(state.txtEmail);
		styleNhapLieu(state.txtSdt);
		styleNhapLieu(state.txtNgaySinh);
		styleNhapLieu(state.txtNgayVaoLam);
		styleNhapLieu(state.cbGioiTinh);
		styleNhapLieu(state.cbChucVu);

		ButtonGroup group = new ButtonGroup();
		group.add(state.rdDangLam);
		group.add(state.rdNgungLam);

		JPanel trangThaiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		trangThaiPanel.setOpaque(false);
		trangThaiPanel.add(state.rdDangLam);
		trangThaiPanel.add(state.rdNgungLam);

		themDong(form, gbc, 0, 0, "Mã NV", state.txtMaNV);
		themDong(form, gbc, 2, 0, "Username", state.txtUsername);
		themDong(form, gbc, 0, 1, "Họ tên", state.txtHoTen);
		themDong(form, gbc, 2, 1, "Email", state.txtEmail);
		themDong(form, gbc, 0, 2, "Số điện thoại", state.txtSdt);
		themDong(form, gbc, 2, 2, "Giới tính", state.cbGioiTinh);
		themDong(form, gbc, 0, 3, "Chức vụ", state.cbChucVu);
		themDong(form, gbc, 2, 3, "Ngày sinh", state.txtNgaySinh);
		themDong(form, gbc, 0, 4, "Ngày vào làm", state.txtNgayVaoLam);
		themDong(form, gbc, 2, 4, "Trạng thái", trangThaiPanel);

		panel.add(form, BorderLayout.CENTER);
		panel.add(taoNutHopThoai(panel, state, laThem), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel taoNutHopThoai(JPanel panel, NhanVienFormState state, boolean laThem) {
		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);
		actions.setBorder(new EmptyBorder(4, 0, 0, 0));

		JButton btnLuu = new JButton(laThem ? "Lưu" : "Cập nhật");
		AppTheme.stylePrimaryButton(btnLuu);
		btnLuu.addActionListener(e -> {
			if (!docDuLieuForm(state, laThem)) {
				return;
			}
			state.ketQua = JOptionPane.OK_OPTION;
			SwingUtilities.getWindowAncestor(panel).dispose();
		});

		JButton btnHuy = new JButton("Hủy");
		AppTheme.styleSecondaryButton(btnHuy);
		btnHuy.addActionListener(e -> {
			state.ketQua = JOptionPane.CANCEL_OPTION;
			SwingUtilities.getWindowAncestor(panel).dispose();
		});

		actions.add(btnLuu);
		actions.add(btnHuy);
		return actions;
	}

	private boolean docDuLieuForm(NhanVienFormState state, boolean laThem) {
		state.maNV = state.txtMaNV.getText().trim();
		state.username = state.txtUsername.getText().trim();
		state.hoTen = state.txtHoTen.getText().trim();
		state.email = state.txtEmail.getText().trim();
		state.sdt = state.txtSdt.getText().trim();
		state.chucVu = String.valueOf(state.cbChucVu.getSelectedItem());
		state.gioiTinh = state.cbGioiTinh.getSelectedIndex() == 0;
		state.trangThai = state.rdDangLam.isSelected();

		if (state.hoTen.isEmpty() || state.email.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập Họ tên và Email.");
			return false;
		}

		if (!state.email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			JOptionPane.showMessageDialog(this, "Email không hợp lệ.");
			return false;
		}

		if (laThem) {
			state.vaiTro = state.chucVu.toLowerCase().contains("quản lý") ? "ADMIN" : "NHAN_VIEN";
			state.matKhau = "123456";
		}

		try {
			state.ngaySinh = LocalDate.parse(state.txtNgaySinh.getText().trim());
			state.ngayVaoLam = LocalDate.parse(state.txtNgayVaoLam.getText().trim());
		} catch (DateTimeParseException ex) {
			JOptionPane.showMessageDialog(this, "Ngày sinh/Ngày vào làm phải theo định dạng yyyy-mm-dd.");
			return false;
		}

		return true;
	}

	private NhanVien taoNhanVienTuState(NhanVienFormState state) {
		NhanVien nhanVien = new NhanVien();
		nhanVien.setMaNV(state.maNV);
		nhanVien.setTenNV(state.hoTen);
		nhanVien.setEmail(state.email);
		nhanVien.setSdt(state.sdt.isBlank() ? null : state.sdt);
		nhanVien.setGioiTinh(state.gioiTinh);
		nhanVien.setNgaySinh(state.ngaySinh);
		nhanVien.setNgayVaoLam(state.ngayVaoLam);
		nhanVien.setChucVu(state.chucVu);
		nhanVien.setTrangThai(state.trangThai);
		nhanVien.setUsername(state.username);
		return nhanVien;
	}

	private NhanVienFormState taiStateTuNhanVien(NhanVien nhanVien) {
		NhanVienFormState state = new NhanVienFormState();
		state.maNV = nhanVien.getMaNV();
		state.username = nhanVien.getUsername();
		state.hoTen = nhanVien.getTenNV();
		state.email = nhanVien.getEmail();
		state.sdt = nhanVien.getSdt() == null ? "" : nhanVien.getSdt();
		state.gioiTinh = nhanVien.isGioiTinh();
		state.ngaySinh = nhanVien.getNgaySinh();
		state.ngayVaoLam = nhanVien.getNgayVaoLam();
		state.chucVu = nhanVien.getChucVu();
		state.trangThai = nhanVien.isTrangThai();
		return state;
	}

	private JTextField taoField(String value, boolean editable) {
		JTextField field = new JTextField(value == null ? "" : value);
		field.setEditable(editable);
		field.setPreferredSize(new Dimension(220, 34));
		field.setFont(AppTheme.font(Font.PLAIN, 13));
		field.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(6, 8, 6, 8)));
		return field;
	}

	private void styleNhapLieu(java.awt.Component component) {
		if (component instanceof JTextField textField) {
			textField.setPreferredSize(new Dimension(220, 34));
			textField.setFont(AppTheme.font(Font.PLAIN, 13));
			textField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(AppTheme.BORDER),
				new EmptyBorder(6, 8, 6, 8)));
			return;
		}
		if (component instanceof JComboBox<?> comboBox) {
			comboBox.setPreferredSize(new Dimension(220, 34));
			comboBox.setFont(AppTheme.font(Font.PLAIN, 13));
			comboBox.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		}
	}

	private void themDong(JPanel form, GridBagConstraints gbc, int x, int y, String labelText, java.awt.Component input) {
		JLabel label = new JLabel(labelText);
		label.setFont(AppTheme.font(Font.BOLD, 12));
		label.setForeground(AppTheme.TEXT_PRIMARY);

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.weightx = 0.16;
		form.add(label, gbc);

		gbc.gridx = x + 1;
		gbc.weightx = 0.34;
		form.add(input, gbc);
	}

	private String taoMaNhanVienMoi() {
		int max = 0;
		for (NhanVien nhanVien : nhanVienDAO.layTatCa()) {
			String ma = nhanVien.getMaNV();
			if (ma == null) {
				continue;
			}
			String digits = ma.replaceAll("\\D", "");
			if (!digits.isEmpty()) {
				try {
					max = Math.max(max, Integer.parseInt(digits));
				} catch (NumberFormatException ignored) {
					// Bỏ qua mã không chuẩn.
				}
			}
		}
		return String.format("NV%04d", max + 1);
	}

	private static final class NhanVienFormState {
		String maNV;
		String username;
		String hoTen;
		String email;
		String sdt;
		String chucVu;
		String vaiTro;
		String matKhau;
		boolean gioiTinh = true;
		boolean trangThai = true;
		LocalDate ngaySinh;
		LocalDate ngayVaoLam;
		JTextField txtMaNV;
		JTextField txtUsername;
		JTextField txtHoTen;
		JTextField txtEmail;
		JTextField txtSdt;
		JTextField txtNgaySinh;
		JTextField txtNgayVaoLam;
		JComboBox<String> cbGioiTinh;
		JComboBox<String> cbChucVu;
		JRadioButton rdDangLam;
		JRadioButton rdNgungLam;
		int ketQua = JOptionPane.CANCEL_OPTION;
	}
}
