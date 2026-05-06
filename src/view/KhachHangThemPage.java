package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class KhachHangThemPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	
	private JTextField txtTen, txtSdt, txtCccd, txtEmail, txtDiaChi;
	private JComboBox<String> cbGioiTinh, cbLoaiKH;

	public KhachHangThemPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(AppTheme.topAlignedFormPage(taoForm()), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		JLabel title = new JLabel("Thêm khách hàng mới");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		return header;
	}

	private JPanel taoForm() {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(Color.WHITE);
		wrapper.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(20, 20, 20, 20)
		));

		// Form chính
		JPanel formContainer = new JPanel(new GridBagLayout());
		formContainer.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Row 0: Tên khách hàng
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.25;
		JLabel lbl = new JLabel("Tên khách hàng *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.75;
		txtTen = new JTextField();
		txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTen.setPreferredSize(new Dimension(250, 35));
		txtTen.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtTen, gbc);

		// Row 1: Số điện thoại
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.25;
		lbl = new JLabel("Số điện thoại *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.75;
		txtSdt = new JTextField();
		txtSdt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSdt.setPreferredSize(new Dimension(250, 35));
		txtSdt.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtSdt, gbc);

		// Row 2: CCCD
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.25;
		lbl = new JLabel("CCCD/Passport *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.75;
		txtCccd = new JTextField();
		txtCccd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtCccd.setPreferredSize(new Dimension(250, 35));
		txtCccd.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtCccd, gbc);

		// Row 3: Email
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.25;
		lbl = new JLabel("Email");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.75;
		txtEmail = new JTextField();
		txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtEmail.setPreferredSize(new Dimension(250, 35));
		txtEmail.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtEmail, gbc);

		// Row 4: Địa chỉ
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.25;
		lbl = new JLabel("Địa chỉ");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.75;
		txtDiaChi = new JTextField();
		txtDiaChi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtDiaChi.setPreferredSize(new Dimension(250, 35));
		txtDiaChi.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtDiaChi, gbc);

		// Row 5: Giới tính
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0.25;
		lbl = new JLabel("Giới tính");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.75;
		cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
		cbGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbGioiTinh.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		formContainer.add(cbGioiTinh, gbc);

		// Row 6: Loại khách hàng
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 0.25;
		lbl = new JLabel("Loại khách hàng");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.75;
		cbLoaiKH = new JComboBox<>(new String[]{"Khách hàng thường", "Khách hàng VIP", "Khách hàng doanh nghiệp"});
		cbLoaiKH.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbLoaiKH.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		formContainer.add(cbLoaiKH, gbc);

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

		JButton btnThem = new JButton("Thêm khách hàng");
		btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnThem.setBackground(MAU_CHINH);
		btnThem.setForeground(Color.WHITE);
		btnThem.setFocusPainted(false);
		btnThem.setBorder(new EmptyBorder(8, 20, 8, 20));
		btnThem.setPreferredSize(new Dimension(150, 40));

		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setForeground(Color.decode("#3A4D66"));
		btnLamMoi.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 20, 8, 20)
		));
		btnLamMoi.setPreferredSize(new Dimension(120, 40));

		btnLamMoi.addActionListener(e -> {
			txtTen.setText("");
			txtSdt.setText("");
			txtCccd.setText("");
			txtEmail.setText("");
			txtDiaChi.setText("");
			cbGioiTinh.setSelectedIndex(0);
			cbLoaiKH.setSelectedIndex(0);
		});

		actions.add(btnThem);
		actions.add(btnLamMoi);

		wrapper.add(scrollWrapper, BorderLayout.CENTER);
		wrapper.add(actions, BorderLayout.SOUTH);
		return wrapper;
	}
}
