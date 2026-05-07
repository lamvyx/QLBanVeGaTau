package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.TrangChinhController;
import entity.TaiKhoan;
import service.PhanQuyenService;

public class TrangChinhPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Color NAV_BG = Color.decode("#123562");
	private static final Color NAV_BG_ACTIVE = Color.decode("#1B4A87");
	private static final Color TEXT_LIGHT = Color.decode("#EDF4FF");
	private static final ImageIcon ARROW_DOWN_ICON = taiAnhIcon("/Image/arrow-down.png",
			Path.of("src", "main", "resources", "Image", "arrow-down.png"), 12, 12);

	private final TaiKhoan taiKhoan;
	private final PhanQuyenService phanQuyenService;
	private final TrangChinhController controller;
	
	private JPanel contentPanel;

	public TrangChinhPage(TaiKhoan taiKhoan) {
		this.taiKhoan = taiKhoan;

		phanQuyenService = new PhanQuyenService();
		controller = new TrangChinhController(taiKhoan);

		setTitle("Trang chính - " + taiKhoan.getHoTen());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1400, 720);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		add(taoThanhDieuHuong(), BorderLayout.NORTH);

		contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(taoBannerChaoMung(), BorderLayout.CENTER);
		add(contentPanel, BorderLayout.CENTER);
	}

	private JPanel taoThanhDieuHuong() {
		JPanel nav = new JPanel(new BorderLayout());
		nav.setBackground(NAV_BG);
		nav.setBorder(new EmptyBorder(6, 10, 6, 10));

		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		left.setOpaque(false);

		JLabel logo = new JLabel("Ga Tàu Sài Gòn");
		logo.setForeground(Color.WHITE);
		logo.setFont(AppTheme.font(Font.BOLD, 17));
		logo.setBorder(new EmptyBorder(0, 2, 0, 8));
		left.add(logo);

		for (String item : phanQuyenService.layMenuTheoVaiTro(taiKhoan)) {
			left.add(taoMenuButton(item, "Trang chủ".equals(item)));
		}

		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		right.setOpaque(false);

		JLabel lblAvatar = new JLabel(taiKhoan.getHoTen().substring(0, 1), SwingConstants.CENTER);
		lblAvatar.setOpaque(true);
		lblAvatar.setBackground(Color.decode("#4B9BFF"));
		lblAvatar.setForeground(Color.WHITE);
		lblAvatar.setPreferredSize(new Dimension(34, 34));
		lblAvatar.setFont(AppTheme.font(Font.BOLD, 16));

		JButton btnUser = new JButton(taiKhoan.getHoTen() + "  •  " + phanQuyenService.hienThiVaiTro(taiKhoan));
		btnUser.setForeground(TEXT_LIGHT);
		btnUser.setBackground(NAV_BG);
		btnUser.setFont(AppTheme.font(Font.BOLD, 12));
		btnUser.setFocusPainted(false);
		btnUser.setBorder(new EmptyBorder(6, 10, 6, 10));
		btnUser.setOpaque(true);
		btnUser.addActionListener(this::hienThiMenuNguoiDung);

		right.add(lblAvatar);
		right.add(btnUser);

		nav.add(left, BorderLayout.WEST);
		nav.add(right, BorderLayout.EAST);
		return nav;
	}

	private JPanel taoBannerChaoMung() {
		JPanel banner = new BannerCanvasPanel(taiAnhBanner());

		banner.setBorder(BorderFactory.createEmptyBorder(40, 28, 40, 28));

		JPanel textWrap = new JPanel();
		textWrap.setOpaque(false);
		textWrap.setLayout(new javax.swing.BoxLayout(textWrap, javax.swing.BoxLayout.Y_AXIS));

		JLabel hello = new JLabel("Chào mừng trở lại,");
		hello.setForeground(TEXT_LIGHT);
		hello.setFont(AppTheme.font(Font.PLAIN, 30));

		JLabel name = new JLabel(taiKhoan.getHoTen());
		name.setForeground(Color.WHITE);
		name.setFont(AppTheme.font(Font.BOLD, 52));
		
		JLabel date = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", new Locale("vi", "VN"))));
		date.setForeground(Color.decode("#9DD2FF"));
		date.setFont(AppTheme.font(Font.PLAIN, 24));

		textWrap.add(hello);
		textWrap.add(name);
		textWrap.add(date);

		JPanel bottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		bottomLeft.setOpaque(false);
		bottomLeft.add(textWrap);
		banner.add(bottomLeft, BorderLayout.SOUTH);
		return banner;
	}

	private JButton taoMenuButton(String text, boolean active) {
		JButton button = new JButton(text);
		button.setFocusPainted(false);
		button.setForeground(TEXT_LIGHT);
		button.setFont(AppTheme.font(Font.BOLD, 12));
		button.setBorder(new EmptyBorder(6, 10, 6, 10));
		button.setBackground(active ? NAV_BG_ACTIVE : NAV_BG);
		button.setOpaque(true);
		button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		if ("Trang chủ".equals(text)) {
			button.addActionListener(e -> {
				contentPanel.removeAll();
				contentPanel.add(taoBannerChaoMung(), BorderLayout.CENTER);
				contentPanel.revalidate();
				contentPanel.repaint();
			});
		} else {
			button.setText(text);
			button.setIcon(ARROW_DOWN_ICON);
			button.setHorizontalTextPosition(JButton.LEFT);
			button.setIconTextGap(4);
			button.addActionListener(e -> hienThiDropdown(button, text));
		}
		return button;
	}

	private void hienThiDropdown(JButton source, String menuName) {
		JPopupMenu popupMenu = taoMenuDropdown(menuName);
		popupMenu.show(source, 0, source.getHeight());
	}

	private JPopupMenu taoMenuDropdown(String menuName) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));

		if ("Tàu".equals(menuName)) {
			if (phanQuyenService.laQuanLy(taiKhoan)) {
				themMenuItem(popupMenu, "Thêm tàu", () -> moPage("Tàu", "Thêm tàu"));
				themMenuItem(popupMenu, "Tra cứu tàu", () -> moPage("Tàu", "Tra cứu tàu"));
				themMenuItem(popupMenu, "Cập nhật tàu", () -> moPage("Tàu", "Cập nhật"));
			} else {
				themMenuItem(popupMenu, "Tra cứu tàu", () -> moPage("Tàu", "Tra cứu tàu"));
			}
			popupMenu.addSeparator();

			popupMenu.add(taoMenuConNhieuCap("Toa", phanQuyenService.layMenuCon("Toa", taiKhoan)));
			popupMenu.add(taoMenuConNhieuCap("Tuyến tàu", phanQuyenService.layMenuCon("Tuyến tàu", taiKhoan)));
			popupMenu.add(taoMenuConNhieuCap("Chuyến tàu", phanQuyenService.layMenuCon("Chuyến tàu", taiKhoan)));
			return popupMenu;
		}

		for (String item : phanQuyenService.layMenuCon(menuName, taiKhoan)) {
			JMenuItem menuItem = new JMenuItem(item);
			menuItem.setFont(AppTheme.font(Font.PLAIN, 13));
			menuItem.addActionListener(e -> moPage(menuName, item));
			popupMenu.add(menuItem);
		}

		return popupMenu;
	}

	private void themMenuItem(JPopupMenu popupMenu, String text, Runnable action) {
		JMenuItem menuItem = new JMenuItem(text);
		menuItem.setFont(AppTheme.font(Font.PLAIN, 13));
		menuItem.addActionListener(e -> action.run());
		popupMenu.add(menuItem);
	}

	private JMenu taoMenuConNhieuCap(String menuCha, String[] items) {
		JMenu menu = new JMenu(menuCha);
		menu.setFont(AppTheme.font(Font.PLAIN, 13));
		for (String item : items) {
			JMenuItem menuItem = new JMenuItem(item);
			menuItem.setFont(AppTheme.font(Font.PLAIN, 13));
			menuItem.addActionListener(e -> moPage(menuCha, item));
			menu.add(menuItem);
		}
		return menu;
	}

	private void moPage(String menuName, String menuCon) {
		JPanel page = controller.moPage(menuName, menuCon);
		contentPanel.removeAll();
		contentPanel.add(page, BorderLayout.CENTER);
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private void hienThiMenuNguoiDung(ActionEvent event) {
		JButton source = (JButton) event.getSource();
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		JMenuItem dangXuat = new JMenuItem("Đăng xuất");
		dangXuat.setFont(AppTheme.font(Font.PLAIN, 13));
		dangXuat.addActionListener(e -> {
			dispose();
			new LoginPage().setVisible(true);
		});
		popupMenu.add(dangXuat);
		popupMenu.show(source, 0, source.getHeight());
	}

	private Image taiAnhBanner() {
		java.net.URL url = getClass().getResource("/Image/hero.png");
		if (url != null) {
			return new ImageIcon(url).getImage();
		}
		Path fallback = Path.of("src", "main", "resources", "Image", "hero.png");
		if (Files.exists(fallback)) {
			return new ImageIcon(fallback.toString()).getImage();
		}
		return null;
	}

	private static ImageIcon taiAnhIcon(String resourcePath, Path fallbackPath, int width, int height) {
		java.net.URL url = TrangChinhPage.class.getResource(resourcePath);
		if (url != null) {
			return scaledIcon(new ImageIcon(url), width, height);
		}
		if (Files.exists(fallbackPath)) {
			return scaledIcon(new ImageIcon(fallbackPath.toString()), width, height);
		}
		return null;
	}

	private static ImageIcon scaledIcon(ImageIcon icon, int width, int height) {
		Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}

	private static class BannerCanvasPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final Image bannerImage;

		private BannerCanvasPanel(Image bannerImage) {
			super(new BorderLayout());
			this.bannerImage = bannerImage;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

			if (bannerImage != null) {
				g2.drawImage(bannerImage, 0, 0, getWidth(), getHeight(), this);
			} else {
				g2.setColor(Color.decode("#234A84"));
				g2.fillRect(0, 0, getWidth(), getHeight());
			}

			GradientPaint leftOverlay = new GradientPaint(
					0, 0, new Color(13, 44, 110, 210),
					getWidth(), 0, new Color(13, 44, 110, 80));
			g2.setPaint(leftOverlay);
			g2.fillRect(0, 0, getWidth(), getHeight());

			GradientPaint topShade = new GradientPaint(
					0, 0, new Color(7, 28, 70, 130),
					0, getHeight() / 2f, new Color(7, 28, 70, 0));
			g2.setPaint(topShade);
			g2.fillRect(0, 0, getWidth(), getHeight() / 2);

			g2.dispose();
		}
	}
}
