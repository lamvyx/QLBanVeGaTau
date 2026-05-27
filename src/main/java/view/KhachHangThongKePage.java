package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class KhachHangThongKePage extends ThongKeBaoCaoBasePage {
	private static final long serialVersionUID = 1L;

	public KhachHangThongKePage() {
		super("Thống kê khách hàng");
	}

	@Override
	protected JPanel createPeriodPanel(String periodKey) {
		switch (periodKey) {
		case "day":
			return createDemoPanel("ngày");
		case "month":
			return createDemoPanel("tháng");
		case "quarter":
			return createDemoPanel("quý");
		case "year":
		default:
			return createDemoPanel("năm");
		}
	}

	private JPanel createDemoPanel(String periodName) {
		JPanel mainPanel = new JPanel(new java.awt.GridBagLayout());
		mainPanel.setBackground(MAU_NEN);
		
		JPanel card = new JPanel(new BorderLayout(0, 20));
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2"), 1),
			new EmptyBorder(40, 40, 40, 40)
		));
		card.setPreferredSize(new Dimension(650, 320));
		
		JLabel lblIcon = new JLabel("📊", JLabel.CENTER);
		lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 56));
		card.add(lblIcon, BorderLayout.NORTH);
		
		JPanel center = new JPanel(new GridLayout(3, 1, 10, 10));
		center.setOpaque(false);
		
		JLabel lblTitle = new JLabel("Thống Kê Khách Hàng", JLabel.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitle.setForeground(MAU_TEXT);
		center.add(lblTitle);
		
		JLabel lblNotice = new JLabel("Đây là chức năng demo sẽ làm trong tương lai.", JLabel.CENTER);
		lblNotice.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 16));
		lblNotice.setForeground(Color.decode("#D97706")); // Amber color for premium warning style
		center.add(lblNotice);
		
		JLabel lblDesc = new JLabel("<html><center>Hệ thống phân tích hành vi khách hàng, thống kê tần suất mua vé, bảng xếp hạng khách hàng thân thiết<br>và các đề xuất ưu đãi tự động đang được xây dựng.<br>Tính năng này sẽ chính thức ra mắt trong phiên bản tiếp theo.</center></html>", JLabel.CENTER);
		lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblDesc.setForeground(Color.decode("#6B7280"));
		center.add(lblDesc);
		
		card.add(center, BorderLayout.CENTER);
		
		mainPanel.add(card);
		return mainPanel;
	}
}
