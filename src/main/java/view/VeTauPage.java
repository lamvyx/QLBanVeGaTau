package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class VeTauPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private final CardLayout cardLayout = new CardLayout();
	private final JPanel workspacePanel = new JPanel(cardLayout);

	public VeTauPage() {
		this("BAN_VE");
	}

	public VeTauPage(String initialModule) {
		setLayout(new BorderLayout(0, 12));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoHeader(), BorderLayout.NORTH);
		add(taoBody(), BorderLayout.CENTER);
		cardLayout.show(workspacePanel, initialModule == null ? "BAN_VE" : initialModule);
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
		return header;
	}

	private JPanel taoBody() {
		JPanel body = new JPanel(new BorderLayout(0, 10));
		body.setOpaque(false);

		workspacePanel.setOpaque(false);
		workspacePanel.add(new BanVePage(), "BAN_VE");
		workspacePanel.add(new DoiVePage(), "DOI_VE");
		workspacePanel.add(new TraVePage(), "TRA_VE");
		workspacePanel.add(new InVePage(), "IN_VE");

		JScrollPane scroll = new JScrollPane(workspacePanel);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getViewport().setOpaque(false);
		scroll.setOpaque(false);
		scroll.getVerticalScrollBar().setUnitIncrement(18);
		body.add(scroll, BorderLayout.CENTER);
		cardLayout.show(workspacePanel, "BAN_VE");
		return body;
	}
}
