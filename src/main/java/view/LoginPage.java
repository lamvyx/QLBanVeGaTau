package view;

import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import connectDB.Database;

public class LoginPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = AppTheme.PRIMARY;
	private static final Color MAU_NEN_TRANG = Color.decode("#FFFFFF");
	private static final Color MAU_NEN = AppTheme.PAGE_BG;
	private static final Color MAU_LABEL = AppTheme.TEXT_MUTED;
	private static final Color MAU_BORDER = AppTheme.BORDER;
	private final TaiKhoan_DAO taiKhoanDAO = new TaiKhoan_DAO();
	private final JTextField txtTenDangNhap = new JTextField(20);
	private final JPasswordField txtMatKhau = new JPasswordField(20);
	private boolean isPasswordVisible = false;

	public LoginPage() {
		setTitle("Đăng nhập");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 800);
		setLocationRelativeTo(null);
		setContentPane(taoNoiDung());
	}

	private JPanel taoNoiDung() {
		JPanel panelChinh = new JPanel(new GridBagLayout());
		panelChinh.setBackground(MAU_NEN);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(20, 20, 20, 20);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;

		// Hình ảnh bên trái
		JLabel lblImage = new JLabel();
		lblImage.setOpaque(true);
		lblImage.setBackground(MAU_CHINH);
		lblImage.setHorizontalAlignment(JLabel.CENTER);
		lblImage.setVerticalAlignment(JLabel.CENTER);
		ImageIcon bgLogin = taiAnhDangNhap();
		if (bgLogin != null) {
			Image scaled = bgLogin.getImage().getScaledInstance(400, 560, Image.SCALE_SMOOTH);
			lblImage.setIcon(new ImageIcon(scaled));
		}
		lblImage.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		panelChinh.add(lblImage, gbc);

		// Card form bên phải
		gbc.gridx = 1;
		gbc.insets = new Insets(40, 40, 40, 40);
		panelChinh.add(taoFormCard(), gbc);

		return panelChinh;
	}

	private JPanel taoFormCard() {
		JPanel cardPanel = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Vẽ border tròn với màu nhẹ
				g2d.setColor(MAU_BORDER);
				g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				g2d.setColor(MAU_NEN_TRANG);
				g2d.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 14, 14);
			}
		};
		cardPanel.setBackground(MAU_NEN_TRANG);
		cardPanel.setOpaque(false);
		cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 0, 6, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;

		// Tiêu đề
		JLabel lblTieuDe = new JLabel("ĐĂNG NHẬP");
		lblTieuDe.setForeground(MAU_CHINH);
		lblTieuDe.setFont(AppTheme.font(java.awt.Font.BOLD, 28));
		gbc.gridy = 0;
		cardPanel.add(lblTieuDe, gbc);

		// Khoảng cách
		JPanel spacer1 = new JPanel();
		spacer1.setOpaque(false);
		spacer1.setPreferredSize(new Dimension(0, 5));
		gbc.gridy = 1;
		cardPanel.add(spacer1, gbc);

		// Label & field Tên đăng nhập
		gbc.gridwidth = 1;
		JLabel lblTenDangNhap = new JLabel("Tên đăng nhập");
		lblTenDangNhap.setForeground(MAU_LABEL);
		lblTenDangNhap.setFont(AppTheme.font(java.awt.Font.PLAIN, 12));
		gbc.gridx = 0;
		gbc.gridy = 2;
		cardPanel.add(lblTenDangNhap, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		txtTenDangNhap.setPreferredSize(new Dimension(300, 40));
		txtTenDangNhap.setFont(AppTheme.font(java.awt.Font.PLAIN, 13));
		txtTenDangNhap.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(MAU_BORDER, 1),
			BorderFactory.createEmptyBorder(5, 8, 5, 8)
		));
		cardPanel.add(txtTenDangNhap, gbc);

		// Label & field Mật khẩu
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		JLabel lblMatKhau = new JLabel("Mật khẩu");
		lblMatKhau.setForeground(MAU_LABEL);
		lblMatKhau.setFont(AppTheme.font(java.awt.Font.PLAIN, 12));
		cardPanel.add(lblMatKhau, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		JPanel panelMatKhau = new JPanel(new GridBagLayout());
		panelMatKhau.setBackground(MAU_NEN_TRANG);
		panelMatKhau.setBorder(BorderFactory.createLineBorder(MAU_BORDER, 1));

		GridBagConstraints gbcMK = new GridBagConstraints();
		gbcMK.gridx = 0;
		gbcMK.gridy = 0;
		gbcMK.fill = GridBagConstraints.BOTH;
		gbcMK.weightx = 1;
		gbcMK.insets = new Insets(5, 8, 5, 5);
		txtMatKhau.setPreferredSize(new Dimension(260, 30));
		txtMatKhau.setFont(AppTheme.font(java.awt.Font.PLAIN, 13));
		txtMatKhau.setBorder(BorderFactory.createEmptyBorder());
		panelMatKhau.add(txtMatKhau, gbcMK);

		gbcMK.gridx = 1;
		gbcMK.weightx = 0;
		gbcMK.insets = new Insets(5, 5, 5, 8);
		JButton btnTogglePassword = taoNutToggle();
		panelMatKhau.add(btnTogglePassword, gbcMK);

		cardPanel.add(panelMatKhau, gbc);

		// Khoảng cách
		JPanel spacer2 = new JPanel();
		spacer2.setOpaque(false);
		spacer2.setPreferredSize(new Dimension(0, 8));
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		cardPanel.add(spacer2, gbc);

		// Nút Quên mật khẩu
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		JButton btnQuenMatKhau = taoNutQuenMatKhau();
		cardPanel.add(btnQuenMatKhau, gbc);

		// Nút Đăng nhập
		gbc.gridx = 1;
		gbc.gridy = 7;
		JButton btnDangNhap = taoNutDangNhap();
		cardPanel.add(btnDangNhap, gbc);

		return cardPanel;
	}

	private JButton taoNutToggle() {
		JButton btn = new JButton() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				if (getModel().isArmed()) {
					g2d.setColor(new Color(220, 220, 220));
				} else if (getModel().isRollover()) {
					g2d.setColor(new Color(240, 240, 240));
				} else {
					g2d.setColor(new Color(248, 248, 248));
				}
				g2d.fillRect(0, 0, getWidth(), getHeight());
				
				super.paintComponent(g);
			}
		};
		
		// Load eye.png icon
		ImageIcon eyeIcon = taiIconEye();
		if (eyeIcon != null) {
			Image scaled = eyeIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
			btn.setIcon(new ImageIcon(scaled));
		}
		btn.setFont(AppTheme.font(java.awt.Font.PLAIN, 14));
		btn.setPreferredSize(new Dimension(32, 28));
		btn.setBackground(new Color(235, 235, 235));
		btn.setBorder(BorderFactory.createLineBorder(MAU_BORDER, 1));
		btn.setFocusPainted(false);
		btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btn.addActionListener(e -> togglePasswordVisibility());
		return btn;
	}
	
	private ImageIcon taiIconEye() {
		java.net.URL url = getClass().getResource("/Image/eye.png");
		if (url != null) {
			return new ImageIcon(url);
		}
		
		Path fallback = Path.of("src", "main", "resources", "Image", "eye.png");
		if (Files.exists(fallback)) {
			return new ImageIcon(fallback.toString());
		}
		
		return null;
	}

	private JButton taoNutQuenMatKhau() {
		JButton btn = new JButton("Quên mật khẩu");
		AppTheme.styleSecondaryButton(btn);
		btn.setFont(AppTheme.font(java.awt.Font.PLAIN, 11));
		btn.setPreferredSize(new Dimension(125, 38));
		btn.setMargin(new Insets(0, 5, 0, 5));
		btn.addActionListener(e -> xuLyQuenMatKhau());
		return btn;
	}

	private JButton taoNutDangNhap() {
		JButton btn = new JButton("Đăng nhập") {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				if (getModel().isArmed()) {
					g2d.setColor(new Color(58, 105, 138));
				} else if (getModel().isRollover()) {
					g2d.setColor(new Color(70, 130, 169));
				} else {
					g2d.setColor(MAU_CHINH);
				}
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
				
				super.paintComponent(g);
			}
		};
		btn.setFont(AppTheme.font(java.awt.Font.BOLD, 12));
		btn.setForeground(Color.WHITE);
		btn.setBackground(MAU_CHINH);
		btn.setBorder(BorderFactory.createEmptyBorder());
		btn.setFocusPainted(false);
		btn.setPreferredSize(new Dimension(100, 36));
		btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btn.addActionListener(e -> xuLyDangNhap());
		return btn;
	}

	private ImageIcon taiAnhDangNhap() {
		java.net.URL url = getClass().getResource("/Image/bg-login.png");
		if (url != null) {
			return new ImageIcon(url);
		}

		Path fallback = Path.of("src", "main", "resources", "Image", "bg-login.png");
		if (Files.exists(fallback)) {
			return new ImageIcon(fallback.toString());
		}

		return null;
	}

	private void xuLyDangNhap() {
		String tenDangNhap = txtTenDangNhap.getText().trim();
		String matKhau = new String(txtMatKhau.getPassword()).trim();
		TaiKhoan taiKhoan = taiKhoanDAO.timTaiKhoanDangNhap(tenDangNhap, matKhau);
		if (taiKhoan != null) {
			TrangChinhPage trangChinhPage = new TrangChinhPage(taiKhoan);
			trangChinhPage.setVisible(true);
			dispose();
			return;
		}
		JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu");
	}

	private void xuLyQuenMatKhau() {
		String tenDangNhap = txtTenDangNhap.getText().trim();
		if (tenDangNhap.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Nhập tên đăng nhập trước");
			return;
		}

		String email = taiKhoanDAO.layEmailTheoTaiKhoan(tenDangNhap);
		if (email == null) {
			JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại");
			return;
		}

		OtpVerificationPage otpPage = new OtpVerificationPage(this, email, verified -> {
			if (Boolean.TRUE.equals(verified)) {
				ResetPasswordPage resetPage = new ResetPasswordPage(this, tenDangNhap, taiKhoanDAO,
						() -> JOptionPane.showMessageDialog(this, "Bạn có thể đăng nhập bằng mật khẩu mới."));
				resetPage.setVisible(true);
			}
		});
		otpPage.setVisible(true);
	}

	private void togglePasswordVisibility() {
		if (isPasswordVisible) {
			// Ẩn mật khẩu: quay lại echo character mặc định
			txtMatKhau.setEchoChar('•');
			isPasswordVisible = false;
		} else {
			// Hiện mật khẩu: đặt echo character thành 0 (không che)
			txtMatKhau.setEchoChar((char) 0);
			isPasswordVisible = true;
		}
	}

	public static void main(String[] args) {
		AppTheme.installGlobalStyles();
		SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
		try (Connection conn = Database.getConnection()) {
		    System.out.println("Kết nối DB thành công!");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
