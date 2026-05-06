package view;

import connectDB.Database;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NhanVienThemPage extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextField txtMaNV;
	private JTextField txtTen;
	private JTextField txtEmail;
	private JTextField txtSdt;
	private JTextField txtNgaySinh;
	private JTextField txtNgayVaoLam;
	private JLabel lblAnh;
	private JComboBox<String> cbGioiTinh;
	private JComboBox<String> cbChucVu;
	private JRadioButton rdDangLam;
	private JRadioButton rdNgungLam;

	public NhanVienThemPage() {
		setLayout(new BorderLayout(0, 12));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoHeader(), BorderLayout.NORTH);
		add(taoNoiDungTop(), BorderLayout.CENTER);
	}

	private JScrollPane taoNoiDungTop() {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.add(taoKhuVucDuLieu(), BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(wrapper);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getVerticalScrollBar().setUnitIncrement(18);
		return scrollPane;
	}

	private JPanel taoHeader() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel lblTitle = new JLabel("Thêm thông tin nhân viên");
		lblTitle.setFont(AppTheme.font(Font.BOLD, 30));
		lblTitle.setForeground(AppTheme.PRIMARY);

		JLabel lblSub = new JLabel("Nhập thông tin cơ bản để tạo nhân viên mới");
		lblSub.setFont(AppTheme.font(Font.PLAIN, 12));
		lblSub.setForeground(AppTheme.TEXT_MUTED);

		panel.add(lblTitle);
		panel.add(Box.createVerticalStrut(4));
		panel.add(lblSub);
		return panel;
	}

	private JSplitPane taoKhuVucDuLieu() {
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, taoCardAnh(), taoFormCard());
		split.setResizeWeight(0.12);
		split.setBorder(BorderFactory.createEmptyBorder());
		split.setOpaque(false);
		split.setPreferredSize(new Dimension(10, 420));
		split.setDividerSize(8);
		return split;
	}

	private JPanel taoCardAnh() {
		JPanel card = new JPanel(new BorderLayout(0, 10));
		card.setMinimumSize(new Dimension(180, 260));
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

	private JPanel taoFormCard() {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 0, 10, 12);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtMaNV = new JTextField("NV-NEW");
		txtMaNV.setEditable(false);
		txtTen = new JTextField();
		txtEmail = new JTextField();
		txtSdt = new JTextField();
		txtNgaySinh = new JTextField("yyyy-mm-dd");
		txtNgayVaoLam = new JTextField("yyyy-mm-dd");

		cbGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });
		cbChucVu = new JComboBox<>(new String[] { "Quản lý", "Nhân viên bán vé", "Kỹ thuật viên", "Kế toán" });

		styleInput(txtMaNV);
		styleInput(txtTen);
		styleInput(txtEmail);
		styleInput(txtSdt);
		styleInput(txtNgaySinh);
		styleInput(txtNgayVaoLam);
		styleCombo(cbGioiTinh);
		styleCombo(cbChucVu);

		themDong(form, gbc, 0, 0, "Mã nhân viên (auto)", txtMaNV);
		themDong(form, gbc, 2, 0, "Họ và tên", txtTen);
		themDong(form, gbc, 0, 1, "Email", txtEmail);
		themDong(form, gbc, 2, 1, "Số điện thoại", txtSdt);
		themDong(form, gbc, 0, 2, "Giới tính", cbGioiTinh);
		themDong(form, gbc, 2, 2, "Chức vụ", cbChucVu);
		themDong(form, gbc, 0, 3, "Ngày sinh", txtNgaySinh);
		themDong(form, gbc, 2, 3, "Ngày vào làm", txtNgayVaoLam);
		themDong(form, gbc, 0, 4, "Trạng thái", taoTrangThaiPanel());

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
		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		actions.setOpaque(false);

		JButton btnLamMoi = new JButton("Làm mới");
		AppTheme.styleSecondaryButton(btnLamMoi);
		btnLamMoi.addActionListener(e -> lamMoiForm());

		JButton btnLuuMoi = new JButton("Lưu mới");
		AppTheme.stylePrimaryButton(btnLuuMoi);
		btnLuuMoi.addActionListener(e -> luuNhanVienMoi());

		actions.add(btnLamMoi);
		actions.add(btnLuuMoi);
		return actions;
	}

	private void luuNhanVienMoi() {
		String hoTen = txtTen.getText().trim();
		String email = txtEmail.getText().trim();
		String sdt = txtSdt.getText().trim();
		String chucVu = String.valueOf(cbChucVu.getSelectedItem());
		boolean gioiTinh = cbGioiTinh.getSelectedIndex() == 0;
		boolean trangThai = rdDangLam.isSelected();

		if (hoTen.isEmpty() || email.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập Họ tên và Email.");
			return;
		}

		if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			JOptionPane.showMessageDialog(this, "Email không hợp lệ.");
			return;
		}

		LocalDate ngaySinh;
		LocalDate ngayVaoLam;
		try {
			ngaySinh = LocalDate.parse(txtNgaySinh.getText().trim());
			ngayVaoLam = LocalDate.parse(txtNgayVaoLam.getText().trim());
		} catch (DateTimeParseException ex) {
			JOptionPane.showMessageDialog(this, "Ngày sinh/Ngày vào làm phải theo định dạng yyyy-mm-dd.");
			return;
		}

		try (Connection conn = Database.getConnection()) {
			conn.setAutoCommit(false);
			try {
				String maNV = taoMaNhanVien(conn);
				String username = maNV;

				if (daTonTaiTaiKhoan(conn, username)) {
					JOptionPane.showMessageDialog(this, "Mã nhân viên/tài khoản đã tồn tại, vui lòng thử lại.");
					conn.rollback();
					return;
				}
				String vaiTro = chucVu.toLowerCase().contains("quản lý") ? "ADMIN" : "NHAN_VIEN";

				try (PreparedStatement psTaiKhoan = conn.prepareStatement(
						"INSERT INTO TaiKhoan(username, [password], vaiTro) VALUES (?, ?, ?)")) {
					psTaiKhoan.setString(1, username);
					psTaiKhoan.setString(2, "123456");
					psTaiKhoan.setString(3, vaiTro);
					psTaiKhoan.executeUpdate();
				}

				try (PreparedStatement psNhanVien = conn.prepareStatement(
						"""
						INSERT INTO NhanVien(maNV, tenNV, sdt, gioiTinh, ngaySinh, ngayVaoLam, trangThai, email, chucVu, username)
						VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
						""")) {
					psNhanVien.setString(1, maNV);
					psNhanVien.setString(2, hoTen);
					if (sdt.isBlank()) {
						psNhanVien.setNull(3, java.sql.Types.VARCHAR);
					} else {
						psNhanVien.setString(3, sdt);
					}
					psNhanVien.setBoolean(4, gioiTinh);
					psNhanVien.setDate(5, Date.valueOf(ngaySinh));
					psNhanVien.setDate(6, Date.valueOf(ngayVaoLam));
					psNhanVien.setBoolean(7, trangThai);
					psNhanVien.setString(8, email);
					psNhanVien.setString(9, chucVu);
					psNhanVien.setString(10, username);
					psNhanVien.executeUpdate();
				}

				conn.commit();
				txtMaNV.setText(maNV);
				JOptionPane.showMessageDialog(this,
						"Thêm nhân viên thành công. Tài khoản đăng nhập là mã nhân viên: " + maNV
								+ " (mật khẩu mặc định: 123456).");
			} catch (SQLException ex) {
				conn.rollback();
				JOptionPane.showMessageDialog(this, "Không thể lưu nhân viên: " + ex.getMessage());
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + ex.getMessage());
		}
	}

	private boolean daTonTaiTaiKhoan(Connection conn, String username) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM TaiKhoan WHERE username = ?")) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	private String taoMaNhanVien(Connection conn) throws SQLException {
		String sql = """
				SELECT ISNULL(MAX(CAST(SUBSTRING(maNV, 3, LEN(maNV) - 2) AS INT)), 0) + 1 AS nextId
				FROM NhanVien
				WHERE maNV LIKE 'NV%'
				""";
		try (PreparedStatement ps = conn.prepareStatement(sql);
				 ResultSet rs = ps.executeQuery()) {
			int next = 1;
			if (rs.next()) {
				next = rs.getInt("nextId");
			}
			return String.format("NV%04d", next);
		}
	}

	private void themDong(JPanel form, GridBagConstraints gbc, int x, int y, String labelText, java.awt.Component input) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.weightx = 0.16;
		JLabel label = new JLabel(labelText);
		label.setFont(AppTheme.font(Font.BOLD, 12));
		label.setForeground(AppTheme.TEXT_PRIMARY);
		form.add(label, gbc);

		gbc.gridx = x + 1;
		gbc.weightx = 0.34;
		form.add(input, gbc);
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

	private void lamMoiForm() {
		txtTen.setText("");
		txtEmail.setText("");
		txtSdt.setText("");
		txtNgaySinh.setText("yyyy-mm-dd");
		txtNgayVaoLam.setText("yyyy-mm-dd");
		cbGioiTinh.setSelectedIndex(0);
		cbChucVu.setSelectedIndex(0);
		rdDangLam.setSelected(true);
		lblAnh.setIcon(null);
		lblAnh.setText("Ảnh 3x4");
	}
}
