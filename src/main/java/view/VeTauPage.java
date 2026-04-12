package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class VeTauPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private final CardLayout cardLayout = new CardLayout();
	private final JPanel workspacePanel = new JPanel(cardLayout);
	private final Map<String, JButton> navButtons = new LinkedHashMap<>();

	public VeTauPage() {
		this("BAN_VE");
	}

	public VeTauPage(String initialModule) {
		setLayout(new BorderLayout(0, 12));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoHeader(), BorderLayout.NORTH);
		add(taoBody(), BorderLayout.CENTER);
		chonModule(initialModule);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout(0, 8));
		header.setOpaque(false);

		JLabel title = new JLabel("Trung tâm nghiệp vụ Vé");
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setForeground(AppTheme.PRIMARY);

		JLabel subtitle = new JLabel("Bán, đổi, trả, kiểm tra chỗ và in vé trong cùng một không gian làm việc");
		subtitle.setFont(AppTheme.font(Font.PLAIN, 12));
		subtitle.setForeground(AppTheme.TEXT_MUTED);

		header.add(title, BorderLayout.NORTH);
		header.add(subtitle, BorderLayout.CENTER);
		header.add(taoThanhDieuHuong(), BorderLayout.SOUTH);
		return header;
	}

	private JPanel taoThanhDieuHuong() {
		JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		nav.setOpaque(false);
		nav.setBorder(new EmptyBorder(4, 0, 0, 0));

		nav.add(taoNutModule("BAN_VE", "Bán vé"));
		nav.add(taoNutModule("DOI_VE", "Đổi vé"));
		nav.add(taoNutModule("TRA_VE", "Trả vé"));
		nav.add(taoNutModule("KT_CHO", "Kiểm tra chỗ"));
		nav.add(taoNutModule("IN_VE", "In vé"));
		return nav;
	}

	private JButton taoNutModule(String key, String label) {
		JButton button = new JButton(label);
		button.setFont(AppTheme.font(Font.BOLD, 12));
		button.setFocusPainted(false);
		button.setBorder(new EmptyBorder(8, 14, 8, 14));
		button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		button.addActionListener(e -> chonModule(key));
		navButtons.put(key, button);
		capNhatMauButton(button, false);
		return button;
	}

	private JPanel taoBody() {
		JPanel body = new JPanel(new BorderLayout(0, 10));
		body.setOpaque(false);
		body.add(taoThongKeTongQuan(), BorderLayout.NORTH);

		workspacePanel.setOpaque(false);
		workspacePanel.add(new BanVePage(), "BAN_VE");
		workspacePanel.add(new DoiVePage(), "DOI_VE");
		workspacePanel.add(new TraVePage(), "TRA_VE");
		workspacePanel.add(new KiemTraChoTrongPage(), "KT_CHO");
		workspacePanel.add(new InVePage(), "IN_VE");

		JScrollPane scroll = new JScrollPane(workspacePanel);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getViewport().setOpaque(false);
		scroll.setOpaque(false);
		scroll.getVerticalScrollBar().setUnitIncrement(18);
		body.add(scroll, BorderLayout.CENTER);
		return body;
	}

	private JPanel taoThongKeTongQuan() {
		JPanel stats = new JPanel(new java.awt.GridLayout(1, 4, 10, 0));
		stats.setOpaque(false);
		stats.add(taoCardStat("Vé trong ngày", "124", Color.decode("#0EA5E9")));
		stats.add(taoCardStat("Đổi vé", "18", Color.decode("#F59E0B")));
		stats.add(taoCardStat("Trả vé", "9", Color.decode("#EF4444")));
		stats.add(taoCardStat("Vé chờ in", "26", Color.decode("#22C55E")));
		return stats;
	}

	private JPanel taoCardStat(String label, String value, Color accent) {
		JPanel card = new JPanel(new BorderLayout(0, 6));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel lblLabel = new JLabel(label);
		lblLabel.setFont(AppTheme.font(Font.PLAIN, 12));
		lblLabel.setForeground(AppTheme.TEXT_MUTED);

		JLabel lblValue = new JLabel(value);
		lblValue.setFont(AppTheme.font(Font.BOLD, 24));
		lblValue.setForeground(accent);

		card.add(lblLabel, BorderLayout.NORTH);
		card.add(lblValue, BorderLayout.CENTER);
		return card;
	}

	private void chonModule(String key) {
		cardLayout.show(workspacePanel, key);
		for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
			capNhatMauButton(entry.getValue(), entry.getKey().equals(key));
		}
	}

	private void capNhatMauButton(JButton btn, boolean active) {
		btn.setOpaque(true);
		btn.setBackground(active ? AppTheme.PRIMARY : AppTheme.CARD_BG);
		btn.setForeground(active ? Color.WHITE : AppTheme.TEXT_PRIMARY);
		btn.setBorder(active
				? new EmptyBorder(8, 14, 8, 14)
				: BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(AppTheme.BORDER),
						new EmptyBorder(7, 13, 7, 13)));
	}
}
