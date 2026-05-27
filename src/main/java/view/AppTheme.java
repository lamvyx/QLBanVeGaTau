package view;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

public final class AppTheme {
	public static final Color PRIMARY = Color.decode("#2A5ACB");
	public static final Color PAGE_BG = Color.decode("#F4F7FC");
	public static final Color CARD_BG = Color.WHITE;
	public static final Color TEXT_PRIMARY = Color.decode("#1F2E43");
	public static final Color TEXT_MUTED = Color.decode("#64748B");
	public static final Color BORDER = Color.decode("#DCE5F2");
	private static final String HOVER_BG_KEY = "appTheme.hover.originalBg";
	private static final String HOVER_FG_KEY = "appTheme.hover.originalFg";
	private static boolean hoverInstalled;

	private AppTheme() {
	}

	public static void installGlobalStyles() {
		UIManager.put("Panel.background", PAGE_BG);
		UIManager.put("Label.font", font(Font.PLAIN, 13));
		UIManager.put("Button.font", font(Font.BOLD, 13));
		UIManager.put("TextField.font", font(Font.PLAIN, 13));
		UIManager.put("PasswordField.font", font(Font.PLAIN, 13));
		UIManager.put("ComboBox.font", font(Font.PLAIN, 13));
		UIManager.put("Table.font", font(Font.PLAIN, 13));
		UIManager.put("TableHeader.font", font(Font.BOLD, 13));
		UIManager.put("OptionPane.messageFont", font(Font.PLAIN, 13));
		UIManager.put("OptionPane.buttonFont", font(Font.BOLD, 12));
		installGlobalButtonHover();
	}

	public static Font font(int style, int size) {
		return new Font("Segoe UI", style, size);
	}

	public static Border pagePadding() {
		return new EmptyBorder(16, 18, 16, 18);
	}

	public static Border cardBorder() {
		return BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDER),
				new EmptyBorder(14, 14, 14, 14));
	}

	public static void stylePrimaryButton(JButton button) {
		button.setFont(font(Font.BOLD, 13));
		button.setBackground(PRIMARY);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(new EmptyBorder(8, 16, 8, 16));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public static void styleSecondaryButton(JButton button) {
		button.setFont(font(Font.PLAIN, 12));
		button.setBackground(Color.WHITE);
		button.setForeground(PRIMARY);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createLineBorder(PRIMARY));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public static void styleTable(JTable table) {
		table.setFont(font(Font.PLAIN, 13));
		table.setRowHeight(30);
		table.setShowGrid(true);
		table.setGridColor(Color.decode("#E2E8F0"));
		table.setSelectionBackground(Color.decode("#E7EEFF"));
		table.setSelectionForeground(TEXT_PRIMARY);

		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setFont(font(Font.BOLD, 13));
		tableHeader.setBackground(Color.decode("#EEF3FB"));
		tableHeader.setForeground(TEXT_PRIMARY);
		tableHeader.setBorder(BorderFactory.createLineBorder(BORDER));
	}

	private static void installGlobalButtonHover() {
		if (hoverInstalled) {
			return;
		}
		hoverInstalled = true;
		Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
			if (!(event instanceof MouseEvent mouseEvent)) {
				return;
			}
			if (!(mouseEvent.getSource() instanceof JButton button)) {
				return;
			}
			if (!button.isEnabled() || !button.isShowing()) {
				return;
			}

			if (mouseEvent.getID() == MouseEvent.MOUSE_ENTERED) {
				applyHover(button);
			} else if (mouseEvent.getID() == MouseEvent.MOUSE_EXITED) {
				restoreHover(button);
			}
		}, AWTEvent.MOUSE_EVENT_MASK);
	}

	private static void applyHover(JButton button) {
		Color background = button.getBackground();
		Color foreground = button.getForeground();
		if (background == null || foreground == null) {
			return;
		}

		if (button.getClientProperty(HOVER_BG_KEY) == null) {
			button.putClientProperty(HOVER_BG_KEY, background);
		}
		if (button.getClientProperty(HOVER_FG_KEY) == null) {
			button.putClientProperty(HOVER_FG_KEY, foreground);
		}

		button.setBackground(adjustHoverColor((Color) button.getClientProperty(HOVER_BG_KEY)));
	}

	private static void restoreHover(JButton button) {
		Object originalBg = button.getClientProperty(HOVER_BG_KEY);
		Object originalFg = button.getClientProperty(HOVER_FG_KEY);
		if (originalBg instanceof Color bg) {
			button.setBackground(bg);
		}
		if (originalFg instanceof Color fg) {
			button.setForeground(fg);
		}
	}

	private static Color adjustHoverColor(Color color) {
		double luminance = (0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue()) / 255d;
		return luminance < 0.55 ? mix(color, Color.WHITE, 0.18) : mix(color, Color.BLACK, 0.08);
	}

	private static Color mix(Color base, Color target, double ratio) {
		double inverse = 1d - ratio;
		int red = (int) Math.round(base.getRed() * inverse + target.getRed() * ratio);
		int green = (int) Math.round(base.getGreen() * inverse + target.getGreen() * ratio);
		int blue = (int) Math.round(base.getBlue() * inverse + target.getBlue() * ratio);
		return new Color(red, green, blue, base.getAlpha());
	}
}
