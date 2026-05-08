package view;

import controller.KhuyenMaiController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import service.KhuyenMaiService.KetQuaXuLy;

public class KhuyenMaiThemPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");
	
	private JTextField txtMaKM, txtTenKM, txtTyLeKM, txtNgayBD, txtNgayKT;
	private final KhuyenMaiController khuyenMaiController = new KhuyenMaiController();
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public KhuyenMaiThemPage() {
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

		JLabel title = new JLabel("Thêm khuyến mãi mới");
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

		// Row 0: Mã khuyến mãi
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Mã khuyến mãi *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtMaKM = new JTextField();
		txtMaKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtMaKM.setPreferredSize(new Dimension(250, 35));
		txtMaKM.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtMaKM, gbc);

		// Row 1: Tên khuyến mãi
		gbc.gridx = 0;
		gbc.gridy = 1;
		lbl = new JLabel("Tên khuyến mãi *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtTenKM = new JTextField();
		txtTenKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTenKM.setPreferredSize(new Dimension(250, 35));
		txtTenKM.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtTenKM, gbc);

		// Row 2: Tỷ lệ khuyến mãi (%)
		gbc.gridx = 0;
		gbc.gridy = 2;
		lbl = new JLabel("Tỷ lệ khuyến mãi (%) *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtTyLeKM = new JTextField();
		txtTyLeKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTyLeKM.setPreferredSize(new Dimension(250, 35));
		txtTyLeKM.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtTyLeKM.setText("0");
		formContainer.add(txtTyLeKM, gbc);

		// Row 3: Ngày bắt đầu
		gbc.gridx = 0;
		gbc.gridy = 3;
		lbl = new JLabel("Ngày bắt đầu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		JPanel pnlNgayBD = new JPanel(new BorderLayout(5, 0));
		pnlNgayBD.setBackground(Color.WHITE);
		txtNgayBD = new JTextField();
		txtNgayBD.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtNgayBD.setPreferredSize(new Dimension(200, 35));
		txtNgayBD.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtNgayBD.setEditable(false);
		txtNgayBD.setBackground(Color.decode("#F8FAFC"));
		pnlNgayBD.add(txtNgayBD, BorderLayout.CENTER);
		
		JButton btnLichBD = taoNutLich(date -> txtNgayBD.setText(date.format(dtf)));
		pnlNgayBD.add(btnLichBD, BorderLayout.EAST);
		formContainer.add(pnlNgayBD, gbc);

		// Row 4: Ngày kết thúc
		gbc.gridx = 0;
		gbc.gridy = 4;
		lbl = new JLabel("Ngày kết thúc *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		JPanel pnlNgayKT = new JPanel(new BorderLayout(5, 0));
		pnlNgayKT.setBackground(Color.WHITE);
		txtNgayKT = new JTextField();
		txtNgayKT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtNgayKT.setPreferredSize(new Dimension(200, 35));
		txtNgayKT.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		txtNgayKT.setEditable(false);
		txtNgayKT.setBackground(Color.decode("#F8FAFC"));
		pnlNgayKT.add(txtNgayKT, BorderLayout.CENTER);
		
		JButton btnLichKT = taoNutLich(date -> txtNgayKT.setText(date.format(dtf)));
		pnlNgayKT.add(btnLichKT, BorderLayout.EAST);
		formContainer.add(pnlNgayKT, gbc);

		// Row 5: Buttons
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(15, 10, 10, 10);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

		JButton btnThem = new JButton("Thêm khuyến mãi");
		btnThem.setBackground(MAU_CHINH);
		btnThem.setForeground(Color.WHITE);
		btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnThem.setFocusPainted(false);
		btnThem.setBorder(new EmptyBorder(8, 24, 8, 24));
		btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnThem.addActionListener(e -> xuLyThemKM());
		buttonPanel.add(btnThem);

		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setForeground(Color.decode("#2B4B74"));
		btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnLamMoi.setFocusPainted(false);
		btnLamMoi.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnLamMoi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnLamMoi.addActionListener(e -> lamMoiForm());
		buttonPanel.add(btnLamMoi);

		formContainer.add(buttonPanel, gbc);

		JPanel scrollWrapper = new JPanel(new BorderLayout());
		scrollWrapper.setBackground(Color.WHITE);
		scrollWrapper.add(formContainer, BorderLayout.NORTH);

		wrapper.add(scrollWrapper, BorderLayout.CENTER);
		return wrapper;
	}

	private JButton taoNutLich(java.util.function.Consumer<LocalDate> target) {
		JButton btn = new JButton();
		try {
			ImageIcon icon = new ImageIcon(getClass().getResource("/Image/icon_lich.png"));
			Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			btn.setIcon(new ImageIcon(img));
		} catch (Exception e) {
			btn.setText("📅");
		}
		btn.setPreferredSize(new Dimension(35, 35));
		btn.setBackground(Color.WHITE);
		btn.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setFocusPainted(false);
		
		btn.addActionListener(e -> {
			CalendarDatePicker picker = new CalendarDatePicker(LocalDate.now(), target);
			picker.showPopup(btn, 0, btn.getHeight());
		});
		return btn;
	}

	private void xuLyThemKM() {
		try {
			String maKM = txtMaKM.getText().trim();
			String tenKM = txtTenKM.getText().trim();
			BigDecimal tyLe = new BigDecimal(txtTyLeKM.getText().trim());
			
			if (txtNgayBD.getText().isEmpty() || txtNgayKT.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu và kết thúc");
				return;
			}
			
			LocalDate ngayBD = LocalDate.parse(txtNgayBD.getText(), dtf);
			LocalDate ngayKT = LocalDate.parse(txtNgayKT.getText(), dtf);
			
			KetQuaXuLy ketQua = khuyenMaiController.themKhuyenMai(maKM, tenKM, tyLe, ngayBD, ngayKT);
			JOptionPane.showMessageDialog(this, ketQua.thongBao);
			if (ketQua.thanhCong) {
				lamMoiForm();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Tỷ lệ khuyến mãi phải là số hợp lệ");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
		}
	}

	private void lamMoiForm() {
		txtMaKM.setText("");
		txtTenKM.setText("");
		txtTyLeKM.setText("0");
		txtNgayBD.setText("");
		txtNgayKT.setText("");
	}
}
