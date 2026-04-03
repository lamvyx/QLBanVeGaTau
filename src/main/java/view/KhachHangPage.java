package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class KhachHangPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = AppTheme.PRIMARY;

	public KhachHangPage() {
		setLayout(new BorderLayout());
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoHeader(), BorderLayout.NORTH);
		add(taoHub(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(AppTheme.CARD_BG);
		header.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Khách hàng");
		title.setFont(AppTheme.font(Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		JLabel subtitle = new JLabel("Chọn chức năng để quản lý khách hàng");
		subtitle.setFont(AppTheme.font(Font.PLAIN, 13));
		subtitle.setForeground(AppTheme.TEXT_MUTED);
		header.add(subtitle, BorderLayout.SOUTH);

		return header;
	}

	private JPanel taoHub() {
		JPanel hub = new JPanel(new GridLayout(2, 2, 16, 16));
		hub.setBackground(AppTheme.PAGE_BG);
		hub.setBorder(new EmptyBorder(18, 0, 0, 0));

		hub.add(taoNutChucNang("Thêm khách hàng", "Mở form nhập thông tin khách hàng mới", new KhachHangThemPage()));
		hub.add(taoNutChucNang("Tra cứu khách hàng", "Tìm kiếm và xem danh sách khách hàng", new KhachHangTraCuuPage()));
		hub.add(taoNutChucNang("Cập nhật khách hàng", "Chọn khách hàng và chỉnh sửa thông tin", new KhachHangCapNhatPage()));
		hub.add(taoNutChucNang("Lịch sử vé", "Xem lịch sử vé đã đặt", new LichSuVePage()));

		return hub;
	}

	private JPanel taoNutChucNang(String title, String description, JPanel targetPage) {
		JPanel card = new JPanel(new BorderLayout(0, 10));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(AppTheme.font(Font.BOLD, 20));
		lblTitle.setForeground(MAU_CHINH);

		JLabel lblDesc = new JLabel("<html><body style='width:220px'>" + description + "</body></html>");
		lblDesc.setFont(AppTheme.font(Font.PLAIN, 14));
		lblDesc.setForeground(AppTheme.TEXT_MUTED);

		JButton btn = new JButton("Mở trang");
		AppTheme.stylePrimaryButton(btn);
		btn.addActionListener(e -> moTrangMoi(targetPage));

		JPanel center = new JPanel(new BorderLayout(0, 8));
		center.setOpaque(false);
		center.add(lblTitle, BorderLayout.NORTH);
		center.add(lblDesc, BorderLayout.CENTER);

		card.add(center, BorderLayout.CENTER);
		card.add(btn, BorderLayout.SOUTH);
		return card;
	}

	private void moTrangMoi(JPanel page) {
		removeAll();
		setLayout(new BorderLayout());
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());
		add(taoHeader(), BorderLayout.NORTH);
		add(page, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
}
