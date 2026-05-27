package view;

import controller.TauController;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import service.TauService.KetQuaXuLy;

public class TauThemPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");
	private static final Dimension BUTTON_SIZE = new Dimension(150, 40);
	
	private JTextField txtMaTau, txtTenTau;
	private JSpinner spnSoLuongToa;
	private final TauController tauController = new TauController();

	public TauThemPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoForm(), BorderLayout.CENTER);
		capNhatMaTauTiepTheo();
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		JLabel title = new JLabel("Thêm đầu tàu");
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

		// Row 0: Mã tàu / Tên tàu
		gbc.gridx = 0;
		gbc.gridy = 0;
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
		txtMaTau.setEditable(false);
		txtMaTau.setBackground(new Color(245, 247, 250));
		formContainer.add(txtMaTau, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		lbl = new JLabel("Tên tàu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		txtTenTau = new JTextField();
		txtTenTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTenTau.setPreferredSize(new Dimension(260, 36));
		txtTenTau.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtTenTau, gbc);

		// Row 1: Giới hạn kéo toa
		gbc.gridx = 0;
		gbc.gridy = 1;
		lbl = new JLabel("Giới hạn kéo toa *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		spnSoLuongToa = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		spnSoLuongToa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		spnSoLuongToa.setPreferredSize(new Dimension(250, 35));
		((JSpinner.DefaultEditor) spnSoLuongToa.getEditor()).getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 14));
		formContainer.add(spnSoLuongToa, gbc);

		// Row 2: Buttons
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 4;
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
		btnThem.setPreferredSize(BUTTON_SIZE);
		btnThem.addActionListener(e -> xuLyThemTau());
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

	private void xuLyThemTau() {
		try {
			int soToa = (Integer) spnSoLuongToa.getValue();
			KetQuaXuLy ketQua = tauController.themTau(txtTenTau.getText(), soToa);
			JOptionPane.showMessageDialog(this, ketQua.thongBao + (ketQua.thanhCong ? " (" + ketQua.maThamChieu + ")" : ""));
			if (ketQua.thanhCong) {
				lamMoiForm();
			}
		} catch (ClassCastException ex) {
			JOptionPane.showMessageDialog(this, "Số lượng toa không hợp lệ.");
		}
	}

	private void capNhatMaTauTiepTheo() {
		txtMaTau.setText(tauController.layMaTauTiepTheo());
	}

	private void lamMoiForm() {
		capNhatMaTauTiepTheo();
		txtTenTau.setText("");
		spnSoLuongToa.setValue(1);
	}
}
