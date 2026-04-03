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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class TauThemPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");
	
	private JTextField txtMaTau, txtTenTau, txtSoLuongToa, txtSucChua, txtNamSanXuat;

	public TauThemPage() {
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

		JLabel title = new JLabel("Thêm tàu mới");
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

		// Row 0: Mã tàu
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Mã tàu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtMaTau = new JTextField();
		txtMaTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtMaTau.setPreferredSize(new Dimension(250, 35));
		txtMaTau.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtMaTau, gbc);

		// Row 1: Tên tàu
		gbc.gridx = 0;
		gbc.gridy = 1;
		lbl = new JLabel("Tên tàu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtTenTau = new JTextField();
		txtTenTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTenTau.setPreferredSize(new Dimension(250, 35));
		txtTenTau.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtTenTau, gbc);

		// Row 2: Số lượng toa
		gbc.gridx = 0;
		gbc.gridy = 2;
		lbl = new JLabel("Số lượng toa *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtSoLuongToa = new JTextField();
		txtSoLuongToa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSoLuongToa.setPreferredSize(new Dimension(250, 35));
		txtSoLuongToa.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtSoLuongToa.setText("0");
		formContainer.add(txtSoLuongToa, gbc);

		// Row 3: Sức chứa
		gbc.gridx = 0;
		gbc.gridy = 3;
		lbl = new JLabel("Sức chứa (hành khách) *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtSucChua = new JTextField();
		txtSucChua.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSucChua.setPreferredSize(new Dimension(250, 35));
		txtSucChua.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtSucChua.setText("0");
		formContainer.add(txtSucChua, gbc);

		// Row 4: Năm sản xuất
		gbc.gridx = 0;
		gbc.gridy = 4;
		lbl = new JLabel("Năm sản xuất");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtNamSanXuat = new JTextField();
		txtNamSanXuat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtNamSanXuat.setPreferredSize(new Dimension(250, 35));
		txtNamSanXuat.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtNamSanXuat.setText("2024");
		formContainer.add(txtNamSanXuat, gbc);

		// Row 5: Buttons
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(15, 10, 10, 10);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

		JButton btnThem = new JButton("Thêm tàu");
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
