package view;

import controller.NhanVienController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class NhanVienThemPageImpl extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final NhanVienController nhanVienController = new NhanVienController();
	
	private JTextField txtTen, txtUsername, txtSdt, txtEmail, txtPassword;
	private JTextField txtNgaySinh, txtNgayVaoLam;
	private JComboBox<String> cbChucVu;
	private JCheckBox cbGioiTinh;
	private JButton btnThem, btnLamMoi;

	public NhanVienThemPageImpl() {
		setLayout(new BorderLayout());
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoHeader(), BorderLayout.NORTH);
		add(taoForm(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(AppTheme.CARD_BG);
		header.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Thêm nhân viên mới");
		title.setFont(AppTheme.font(Font.BOLD, 24));
		title.setForeground(AppTheme.PRIMARY);
		header.add(title, BorderLayout.WEST);

		JLabel subtitle = new JLabel("Nhập thông tin và xác nhận tạo mới nhân viên");
		subtitle.setFont(AppTheme.font(Font.PLAIN, 12));
		subtitle.setForeground(AppTheme.TEXT_MUTED);
		header.add(subtitle, BorderLayout.SOUTH);

		return header;
	}

	private JPanel taoForm() {
		JPanel wrapper = new JPanel(new BorderLayout(0, 16));
		wrapper.setOpaque(false);

		JPanel card = new JPanel(new BorderLayout(0, 16));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JPanel body = new JPanel(new BorderLayout(16, 0));
		body.setOpaque(false);

		JPanel formCard = taoFormCard();
		body.add(formCard, BorderLayout.CENTER);

		// Nút hành động
		JPanel actions = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
		actions.setOpaque(false);

		btnThem = new JButton("Thêm nhân viên");
		AppTheme.stylePrimaryButton(btnThem);
		btnThem.setPreferredSize(new Dimension(150, 40));
		btnThem.addActionListener(e -> xuLyThemNhanVien());

		btnLamMoi = new JButton("Làm mới");
		AppTheme.styleSecondaryButton(btnLamMoi);
		btnLamMoi.setPreferredSize(new Dimension(120, 40));
		btnLamMoi.addActionListener(e -> lamMoiForm());

		actions.add(btnLamMoi);
		actions.add(btnThem);

		card.add(body, BorderLayout.CENTER);
		card.add(actions, BorderLayout.SOUTH);
		wrapper.add(card, BorderLayout.CENTER);
		return wrapper;
	}

	private JPanel taoFormCard() {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(BorderFactory.createCompoundBorder(
			AppTheme.cardBorder(),
			new EmptyBorder(4, 4, 4, 4)
		));

		JLabel title = new JLabel("Thông tin nhân viên");
		title.setFont(AppTheme.font(Font.BOLD, 16));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		card.add(title, BorderLayout.NORTH);

		JPanel formContainer = new JPanel(new GridBagLayout());
		formContainer.setBackground(AppTheme.CARD_BG);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;

		addField(formContainer, gbc, 0, 0, "Họ và tên *", txtTen = new JTextField());
		addField(formContainer, gbc, 1, 0, "Tên tài khoản *", txtUsername = new JTextField());
		addField(formContainer, gbc, 0, 1, "Số điện thoại", txtSdt = new JTextField());
		addComboField(formContainer, gbc, 1, 1, "Chức vụ", cbChucVu = new JComboBox<>(
			new String[]{"Quản lý", "Bán vé", "Hỗ trợ", "Kỹ thuật"}));
		
		addField(formContainer, gbc, 0, 2, "Email *", txtEmail = new JTextField());
		addField(formContainer, gbc, 1, 2, "Mật khẩu *", txtPassword = new JTextField());
		
		// Ngày sinh
		JLabel lblNgaySinh = new JLabel("Ngày sinh");
		lblNgaySinh.setFont(AppTheme.font(Font.BOLD, 13));
		lblNgaySinh.setForeground(AppTheme.TEXT_PRIMARY);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.26;
		formContainer.add(lblNgaySinh, gbc);
		
		txtNgaySinh = new JTextField();
		txtNgaySinh.setText(LocalDate.now().toString());
		txtNgaySinh.setFont(AppTheme.font(Font.PLAIN, 13));
		txtNgaySinh.setPreferredSize(new Dimension(0, 38));
		txtNgaySinh.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(8, 8, 8, 8)
		));
		gbc.gridx = 1;
		gbc.weightx = 0.74;
		formContainer.add(txtNgaySinh, gbc);
		
		// Ngày vào làm
		JLabel lblNgayVaoLam = new JLabel("Ngày vào làm");
		lblNgayVaoLam.setFont(AppTheme.font(Font.BOLD, 13));
		lblNgayVaoLam.setForeground(AppTheme.TEXT_PRIMARY);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.26;
		formContainer.add(lblNgayVaoLam, gbc);
		
		txtNgayVaoLam = new JTextField();
		txtNgayVaoLam.setText(LocalDate.now().toString());
		txtNgayVaoLam.setFont(AppTheme.font(Font.PLAIN, 13));
		txtNgayVaoLam.setPreferredSize(new Dimension(0, 38));
		txtNgayVaoLam.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(8, 8, 8, 8)
		));
		gbc.gridx = 1;
		gbc.weightx = 0.74;
		formContainer.add(txtNgayVaoLam, gbc);
		
		// Giới tính
		JLabel lblGioiTinh = new JLabel("Giới tính");
		lblGioiTinh.setFont(AppTheme.font(Font.BOLD, 13));
		lblGioiTinh.setForeground(AppTheme.TEXT_PRIMARY);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0.26;
		formContainer.add(lblGioiTinh, gbc);
		
		cbGioiTinh = new JCheckBox("Nam");
		cbGioiTinh.setSelected(true);
		cbGioiTinh.setBackground(AppTheme.CARD_BG);
		gbc.gridx = 1;
		gbc.weightx = 0.74;
		formContainer.add(cbGioiTinh, gbc);

		JScrollPane scrollPane = new JScrollPane(formContainer);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		card.add(scrollPane, BorderLayout.CENTER);
		return card;
	}

	private void addField(JPanel parent, GridBagConstraints gbc, int col, int row, String labelText, JTextField field) {
		JLabel label = new JLabel(labelText);
		label.setFont(AppTheme.font(Font.BOLD, 13));
		label.setForeground(AppTheme.TEXT_PRIMARY);
		field.setFont(AppTheme.font(Font.PLAIN, 13));
		field.setPreferredSize(new Dimension(0, 38));
		field.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(8, 8, 8, 8)
		));

		gbc.gridx = col * 2;
		gbc.gridy = row;
		gbc.weightx = 0.26;
		parent.add(label, gbc);

		gbc.gridx = col * 2 + 1;
		gbc.weightx = 0.74;
		parent.add(field, gbc);
	}

	private void addComboField(JPanel parent, GridBagConstraints gbc, int col, int row, String labelText, JComboBox<String> comboBox) {
		JLabel label = new JLabel(labelText);
		label.setFont(AppTheme.font(Font.BOLD, 13));
		label.setForeground(AppTheme.TEXT_PRIMARY);
		comboBox.setFont(AppTheme.font(Font.PLAIN, 13));
		comboBox.setPreferredSize(new Dimension(0, 38));
		comboBox.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));

		gbc.gridx = col * 2;
		gbc.gridy = row;
		gbc.weightx = 0.26;
		parent.add(label, gbc);

		gbc.gridx = col * 2 + 1;
		gbc.weightx = 0.74;
		parent.add(comboBox, gbc);
	}

	/**
	 * Xử lý thêm nhân viên mới
	 */
	private void xuLyThemNhanVien() {
		String tenNV = txtTen.getText();
		String username = txtUsername.getText();
		String matKhau = txtPassword.getText();
		String sdt = txtSdt.getText();
		String email = txtEmail.getText();
		String chucVu = (String) cbChucVu.getSelectedItem();
		boolean gioiTinh = cbGioiTinh.isSelected();

		service.NhanVienService.KetQuaXuLy result = nhanVienController.themNhanVienTuForm(
			tenNV, username, matKhau, sdt, email, chucVu, gioiTinh, txtNgaySinh.getText(), txtNgayVaoLam.getText());

		if (result.thanhCong) {
			JOptionPane.showMessageDialog(this, 
				result.thongBao + "\nMã nhân viên: " + result.maThamChieu,
				"Thành công", JOptionPane.INFORMATION_MESSAGE);
			lamMoiForm();
		} else {
			JOptionPane.showMessageDialog(this, result.thongBao,
				"Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Làm mới form
	 */
	private void lamMoiForm() {
		txtTen.setText("");
		txtUsername.setText("");
		txtSdt.setText("");
		txtEmail.setText("");
		txtPassword.setText("");
		cbChucVu.setSelectedIndex(0);
		cbGioiTinh.setSelected(true);
		txtNgaySinh.setText(LocalDate.now().toString());
		txtNgayVaoLam.setText(LocalDate.now().toString());
	}
}
