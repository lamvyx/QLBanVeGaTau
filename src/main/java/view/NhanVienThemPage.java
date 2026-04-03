package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NhanVienThemPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	
	private JLabel lblHinhAnh;
	private String selectedImagePath = "";
	private JTextField txtTen, txtUsername, txtSdt, txtEmail, txtPassword;
	private JComboBox<String> cbChucVu;

	public NhanVienThemPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoForm(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		JLabel title = new JLabel("Thêm nhân viên mới");
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

		// Row 0: Hình ảnh (to ra, chiếm 2 cột)
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 4;
		gbc.weightx = 0.4;
		JPanel imagePanel = taoImagePanel();
		formContainer.add(imagePanel, gbc);

		// Reset gridwidth về 1 cho các trường khác
		gbc.gridwidth = 1;
		gbc.gridheight = 1;

		// Row 0: Họ và tên
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Họ và tên *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		txtTen = new JTextField();
		txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTen.setPreferredSize(new Dimension(200, 35));
		txtTen.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtTen, gbc);

		// Row 1: Tên tài khoản
		gbc.gridx = 2;
		gbc.gridy = 1;
		lbl = new JLabel("Tên tài khoản *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtUsername.setPreferredSize(new Dimension(200, 35));
		txtUsername.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtUsername, gbc);

		// Row 2: Số điện thoại
		gbc.gridx = 2;
		gbc.gridy = 2;
		lbl = new JLabel("Số điện thoại");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		txtSdt = new JTextField();
		txtSdt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSdt.setPreferredSize(new Dimension(200, 35));
		txtSdt.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtSdt, gbc);

		// Row 3: Chức vụ
		gbc.gridx = 2;
		gbc.gridy = 3;
		lbl = new JLabel("Chức vụ");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		cbChucVu = new JComboBox<>(new String[]{"Quản lý", "Bán vé", "Hỗ trợ", "Kỹ thuật"});
		cbChucVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbChucVu.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
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
		txtEmail = new JTextField();
		txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtEmail.setPreferredSize(new Dimension(150, 35));
		txtEmail.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtEmail, gbc);

		// Row 4: Mật khẩu
		gbc.gridx = 2;
		lbl = new JLabel("Mật khẩu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		txtPassword = new JTextField();
		txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtPassword.setPreferredSize(new Dimension(150, 35));
		txtPassword.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtPassword, gbc);

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

		JButton btnThem = new JButton("Thêm nhân viên");
		btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnThem.setBackground(MAU_CHINH);
		btnThem.setForeground(Color.WHITE);
		btnThem.setFocusPainted(false);
		btnThem.setBorder(new EmptyBorder(8, 20, 8, 20));
		btnThem.setPreferredSize(new Dimension(140, 40));

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
			txtUsername.setText("");
			txtSdt.setText("");
			txtEmail.setText("");
			txtPassword.setText("");
			cbChucVu.setSelectedIndex(0);
			selectedImagePath = "";
			lblHinhAnh.setIcon(null);
			lblHinhAnh.setText("Chưa chọn hình ảnh");
		});

		actions.add(btnThem);
		actions.add(btnLamMoi);

		wrapper.add(scrollWrapper, BorderLayout.CENTER);
		wrapper.add(actions, BorderLayout.SOUTH);
		return wrapper;
	}

	private JPanel taoImagePanel() {
		JPanel imagePanel = new JPanel(new BorderLayout(10, 10));
		imagePanel.setBackground(Color.decode("#F8FAFC"));
		imagePanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5"), 2),
			new EmptyBorder(10, 10, 10, 10)
		));

		// Khu vực hiển thị hình ảnh
		lblHinhAnh = new JLabel();
		lblHinhAnh.setHorizontalAlignment(JLabel.CENTER);
		lblHinhAnh.setVerticalAlignment(JLabel.CENTER);
		lblHinhAnh.setText("Chưa chọn hình ảnh");
		lblHinhAnh.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		lblHinhAnh.setForeground(Color.decode("#8B95A7"));
		lblHinhAnh.setPreferredSize(new Dimension(180, 200));
		lblHinhAnh.setBackground(Color.WHITE);
		lblHinhAnh.setOpaque(true);
		lblHinhAnh.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		imagePanel.add(lblHinhAnh, BorderLayout.CENTER);

		// Nút chọn hình ảnh
		JPanel btnPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 5));
		btnPanel.setBackground(Color.decode("#F8FAFC"));

		JButton btnChon = new JButton("Chọn hình");
		btnChon.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnChon.setBackground(MAU_CHINH);
		btnChon.setForeground(Color.WHITE);
		btnChon.setFocusPainted(false);
		btnChon.setBorder(new EmptyBorder(6, 12, 6, 12));

		btnChon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
				int result = fileChooser.showOpenDialog(NhanVienThemPage.this);

				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					selectedImagePath = selectedFile.getAbsolutePath();

					// Hiển thị ảnh
					ImageIcon icon = new ImageIcon(selectedImagePath);
					java.awt.Image image = icon.getImage().getScaledInstance(160, 180, java.awt.Image.SCALE_SMOOTH);
					lblHinhAnh.setIcon(new ImageIcon(image));
					lblHinhAnh.setText("");
				}
			}
		});

		btnPanel.add(btnChon);
		imagePanel.add(btnPanel, BorderLayout.SOUTH);

		return imagePanel;
	}
}
