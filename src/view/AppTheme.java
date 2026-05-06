package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
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

	public static JScrollPane centeredFormPage(Component content, int preferredWidth) {
		Dimension current = content.getPreferredSize();
		int height = current != null && current.height > 0 ? current.height : 420;
		content.setPreferredSize(new Dimension(preferredWidth, height));

		JPanel holder = new JPanel(new GridBagLayout());
		holder.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		holder.add(content, gbc);

		JScrollPane scrollPane = new JScrollPane(holder);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getVerticalScrollBar().setUnitIncrement(18);
		return scrollPane;
	}

	public static JScrollPane topAlignedFormPage(Component content) {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.add(content, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(wrapper);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getVerticalScrollBar().setUnitIncrement(18);
		return scrollPane;
	}
}
