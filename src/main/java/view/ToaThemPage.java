package view;

import controller.ToaController;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import service.ToaService.KetQuaXuLy;

public class ToaThemPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");
	private static final Dimension BUTTON_SIZE = new Dimension(150, 40);
	
	private JTextField txtMaToa, txtSoGhe;
	private JComboBox<String> cbLoaiToa, cbTrangThai;
	private final ToaController toaController = new ToaController();

	public ToaThemPage() {
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

		JLabel title = new JLabel("Thêm toa tàu");
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
		gbc.insets = new Insets(10, 12, 10, 12);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1;

		// Row 0: Mã toa
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel lbl = new JLabel("Mã toa *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtMaToa = new JTextField();
		txtMaToa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtMaToa.setPreferredSize(new Dimension(260, 36));
		txtMaToa.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtMaToa, gbc);

		// Row 1: Loại toa
		gbc.gridx = 2;
		gbc.gridy = 0;
		lbl = new JLabel("Loại toa *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		cbLoaiToa = new JComboBox<>();
		cbLoaiToa.addItem("-- Chọn loại toa --");
		cbLoaiToa.addItem("Ghế ngồi");
		cbLoaiToa.addItem("Giường nằm");
		cbLoaiToa.addItem("Giường nằm khoang VIP");
		cbLoaiToa.addItem("Toa hàng");
		cbLoaiToa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbLoaiToa.setPreferredSize(new Dimension(260, 36));
		formContainer.add(cbLoaiToa, gbc);

		// Row 1: Số ghế
		gbc.gridx = 0;
		gbc.gridy = 1;
		lbl = new JLabel("Số ghế *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtSoGhe = new JTextField();
		txtSoGhe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSoGhe.setPreferredSize(new Dimension(260, 36));
		txtSoGhe.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtSoGhe.setText("0");
		formContainer.add(txtSoGhe, gbc);

		// Row 1: Trạng thái
		gbc.gridx = 2;
		gbc.gridy = 1;
		lbl = new JLabel("Trạng thái");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		cbTrangThai = new JComboBox<>();
		cbTrangThai.addItem("Hoạt động");
		cbTrangThai.addItem("Bảo trì");
		cbTrangThai.addItem("Ngừng hoạt động");
		cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbTrangThai.setPreferredSize(new Dimension(260, 36));
		formContainer.add(cbTrangThai, gbc);

		// Row 2: Buttons
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 4;
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
		btnThem.setPreferredSize(BUTTON_SIZE);
		btnThem.addActionListener(e -> xuLyThemToa());
		buttonPanel.add(btnThem);
 
		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setForeground(Color.decode("#2B4B74"));
		btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnLamMoi.setFocusPainted(false);
		btnLamMoi.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnLamMoi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnLamMoi.setPreferredSize(BUTTON_SIZE);
		btnLamMoi.addActionListener(e -> lamMoiForm());
		buttonPanel.add(btnLamMoi);
 
		formContainer.add(buttonPanel, gbc);
 
		JPanel scrollWrapper = new JPanel(new BorderLayout());
		scrollWrapper.setBackground(Color.WHITE);
		scrollWrapper.add(formContainer, BorderLayout.NORTH);
 
		wrapper.add(scrollWrapper, BorderLayout.CENTER);
		return wrapper;
	}

	private void xuLyThemToa() {
		try {
			String maToa = txtMaToa.getText().trim();
			String loaiToa = (String) cbLoaiToa.getSelectedItem();
			
			if (loaiToa.startsWith("--")) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn loại toa");
				return;
			}
			int soGhe = Integer.parseInt(txtSoGhe.getText().trim());
			boolean trangThai = cbTrangThai.getSelectedIndex() == 0; // Hoạt động
			
			KetQuaXuLy ketQua = toaController.themToa(maToa, null, loaiToa, soGhe, null, trangThai);
			JOptionPane.showMessageDialog(this, ketQua.thongBao);
			if (ketQua.thanhCong) {
				lamMoiForm();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Số ghế phải là số nguyên");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
		}
	}

	private void lamMoiForm() {
		txtMaToa.setText("");
		cbLoaiToa.setSelectedIndex(0);
		txtSoGhe.setText("0");
		cbTrangThai.setSelectedIndex(0);
	}
}
