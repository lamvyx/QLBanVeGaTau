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

public class ChuyenTauThemPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");
	
	private JTextField txtMaChuyenTau, txtGioKhoiHanh, txtGioDenNoi, txtGiaCoban, txtTongSoCho;
	private JComboBox<String> cbTau, cbTuyenTau, cbTrangThai;

	public ChuyenTauThemPage() {
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

		JLabel title = new JLabel("Thêm chuyến tàu mới");
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

		// Row 0: Mã chuyến
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Mã chuyến *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtMaChuyenTau = new JTextField();
		txtMaChuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtMaChuyenTau.setPreferredSize(new Dimension(250, 35));
		txtMaChuyenTau.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtMaChuyenTau, gbc);

		// Row 1: Tàu
		gbc.gridx = 0;
		gbc.gridy = 1;
		lbl = new JLabel("Tàu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbTau = new JComboBox<>();
		cbTau.addItem("-- Chọn tàu --");
		cbTau.addItem("T001 - Tàu Sapa");
		cbTau.addItem("T002 - Tàu Hà Nội");
		cbTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbTau.setPreferredSize(new Dimension(250, 35));
		formContainer.add(cbTau, gbc);

		// Row 2: Tuyến tàu
		gbc.gridx = 0;
		gbc.gridy = 2;
		lbl = new JLabel("Tuyến tàu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbTuyenTau = new JComboBox<>();
		cbTuyenTau.addItem("-- Chọn tuyến --");
		cbTuyenTau.addItem("Sài Gòn - Hà Nội");
		cbTuyenTau.addItem("Sài Gòn - Đà Nẵng");
		cbTuyenTau.addItem("Sài Gòn - Nha Trang");
		cbTuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbTuyenTau.setPreferredSize(new Dimension(250, 35));
		formContainer.add(cbTuyenTau, gbc);

		// Row 3: Giờ khởi hành
		gbc.gridx = 0;
		gbc.gridy = 3;
		lbl = new JLabel("Giờ khởi hành *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtGioKhoiHanh = new JTextField();
		txtGioKhoiHanh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtGioKhoiHanh.setPreferredSize(new Dimension(250, 35));
		txtGioKhoiHanh.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtGioKhoiHanh.setText("dd/mm/yyyy --:-- --");
		formContainer.add(txtGioKhoiHanh, gbc);

		// Row 4: Giờ đến nơi
		gbc.gridx = 0;
		gbc.gridy = 4;
		lbl = new JLabel("Giờ đến nơi");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtGioDenNoi = new JTextField();
		txtGioDenNoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtGioDenNoi.setPreferredSize(new Dimension(250, 35));
		txtGioDenNoi.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtGioDenNoi.setText("dd/mm/yyyy --:-- --");
		formContainer.add(txtGioDenNoi, gbc);

		// Row 5: Giá cơ bản
		gbc.gridx = 0;
		gbc.gridy = 5;
		lbl = new JLabel("Giá cơ bản (VND) *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtGiaCoban = new JTextField();
		txtGiaCoban.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtGiaCoban.setPreferredSize(new Dimension(250, 35));
		txtGiaCoban.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtGiaCoban.setText("0");
		formContainer.add(txtGiaCoban, gbc);

		// Row 6: Tổng số chỗ
		gbc.gridx = 0;
		gbc.gridy = 6;
		lbl = new JLabel("Tổng số chỗ *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtTongSoCho = new JTextField();
		txtTongSoCho.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTongSoCho.setPreferredSize(new Dimension(250, 35));
		txtTongSoCho.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtTongSoCho.setText("0");
		formContainer.add(txtTongSoCho, gbc);

		// Row 7: Trạng thái
		gbc.gridx = 0;
		gbc.gridy = 7;
		lbl = new JLabel("Trạng thái");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbTrangThai = new JComboBox<>();
		cbTrangThai.addItem("Lên lịch");
		cbTrangThai.addItem("Đang chạy");
		cbTrangThai.addItem("Hoàn thành");
		cbTrangThai.addItem("Hủy");
		cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbTrangThai.setPreferredSize(new Dimension(250, 35));
		formContainer.add(cbTrangThai, gbc);

		// Row 8: Buttons
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(15, 10, 10, 10);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

		JButton btnThem = new JButton("Thêm chuyến");
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
