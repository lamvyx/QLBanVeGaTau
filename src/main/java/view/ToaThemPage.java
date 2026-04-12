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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ToaThemPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");
	
	private JTextField txtMaToa, txtSoGhe, txtViTriToa;
	private JComboBox<String> cbLoaiToa, cbMaTau, cbTrangThai;

	public ToaThemPage() {
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

		JLabel title = new JLabel("Thêm toa tàu mới");
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

		JPanel formContainer = new JPanel(new GridBagLayout());
		formContainer.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Row 0: Mã toa
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Mã toa *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtMaToa = new JTextField();
		txtMaToa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtMaToa.setPreferredSize(new Dimension(250, 35));
		txtMaToa.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtMaToa, gbc);

		// Row 1: Loại toa
		gbc.gridx = 0;
		gbc.gridy = 1;
		lbl = new JLabel("Loại toa *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbLoaiToa = new JComboBox<>();
		cbLoaiToa.addItem("-- Chọn loại toa --");
		cbLoaiToa.addItem("Ghế ngồi");
		cbLoaiToa.addItem("Giường nằm");
		cbLoaiToa.addItem("Giường nằm khoang VIP");
		cbLoaiToa.addItem("Toa hàng");
		cbLoaiToa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbLoaiToa.setPreferredSize(new Dimension(250, 35));
		formContainer.add(cbLoaiToa, gbc);

		// Row 2: Mã tàu
		gbc.gridx = 0;
		gbc.gridy = 2;
		lbl = new JLabel("Tàu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbMaTau = new JComboBox<>();
		cbMaTau.addItem("-- Chọn tàu --");
		cbMaTau.addItem("T001 - Tàu hỏa XP1");
		cbMaTau.addItem("T002 - Tàu hỏa SB2");
		cbMaTau.addItem("T003 - Tàu Sapa Express");
		cbMaTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbMaTau.setPreferredSize(new Dimension(250, 35));
		formContainer.add(cbMaTau, gbc);

		// Row 3: Số ghế
		gbc.gridx = 0;
		gbc.gridy = 3;
		lbl = new JLabel("Số ghế *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtSoGhe = new JTextField();
		txtSoGhe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSoGhe.setPreferredSize(new Dimension(250, 35));
		txtSoGhe.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtSoGhe.setText("0");
		formContainer.add(txtSoGhe, gbc);

		// Row 4: Vị trí toa
		gbc.gridx = 0;
		gbc.gridy = 4;
		lbl = new JLabel("Vị trí toa");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtViTriToa = new JTextField();
		txtViTriToa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtViTriToa.setPreferredSize(new Dimension(250, 35));
		txtViTriToa.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtViTriToa.setText("Thứ 1");
		formContainer.add(txtViTriToa, gbc);

		// Row 5: Trạng thái
		gbc.gridx = 0;
		gbc.gridy = 5;
		lbl = new JLabel("Trạng thái");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbTrangThai = new JComboBox<>();
		cbTrangThai.addItem("Hoạt động");
		cbTrangThai.addItem("Bảo trì");
		cbTrangThai.addItem("Ngừng hoạt động");
		cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbTrangThai.setPreferredSize(new Dimension(250, 35));
		formContainer.add(cbTrangThai, gbc);

		// Row 6: Buttons
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(15, 10, 10, 10);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

		JButton btnThem = new JButton("Thêm toa");
		btnThem.setBackground(MAU_CHINH);
		btnThem.setForeground(Color.WHITE);
		btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnThem.setFocusPainted(false);
		btnThem.setBorder(new EmptyBorder(8, 24, 8, 24));
		btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		buttonPanel.add(btnThem);

		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setForeground(Color.decode("#2B4B74"));
		btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnLamMoi.setFocusPainted(false);
		btnLamMoi.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnLamMoi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		buttonPanel.add(btnLamMoi);

		formContainer.add(buttonPanel, gbc);

		JPanel scrollWrapper = new JPanel(new BorderLayout());
		scrollWrapper.setBackground(Color.WHITE);
		scrollWrapper.add(formContainer, BorderLayout.NORTH);

		wrapper.add(scrollWrapper, BorderLayout.CENTER);
		return wrapper;
	}
}
