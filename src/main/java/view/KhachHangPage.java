package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class KhachHangPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");

	public KhachHangPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoHub(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		JLabel title = new JLabel("Khách hàng");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		JLabel subtitle = new JLabel("Chọn chức năng để quản lý khách hàng");
		subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		subtitle.setForeground(Color.decode("#6B7A90"));
		header.add(subtitle, BorderLayout.SOUTH);

		return header;
	}

	private JPanel taoHub() {
		JPanel hub = new JPanel(new GridLayout(2, 2, 16, 16));
		hub.setBackground(Color.decode("#F0F5F9"));
		hub.setBorder(new EmptyBorder(18, 0, 0, 0));

		hub.add(taoNutChucNang("Thêm khách hàng", "Mở form nhập thông tin khách hàng mới", new KhachHangThemPage()));
		hub.add(taoNutChucNang("Tra cứu khách hàng", "Tìm kiếm và xem danh sách khách hàng", new KhachHangTraCuuPage()));
		hub.add(taoNutChucNang("Cập nhật khách hàng", "Chọn khách hàng và chỉnh sửa thông tin", new KhachHangCapNhatPage()));
		hub.add(taoNutChucNang("Lịch sử vé", "Xem lịch sử vé đã đặt", new LichSuVePage()));

		return hub;
	}

	private JPanel taoNutChucNang(String title, String description, JPanel targetPage) {
		JPanel card = new JPanel(new BorderLayout(0, 10));
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(18, 18, 18, 18)
		));

		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitle.setForeground(MAU_CHINH);

		JLabel lblDesc = new JLabel("<html><body style='width:220px'>" + description + "</body></html>");
		lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblDesc.setForeground(Color.decode("#5C6F87"));

		JButton btn = new JButton("Mở trang");
		btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btn.setBackground(MAU_CHINH);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorder(new EmptyBorder(8, 16, 8, 16));
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
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));
		add(taoHeader(), BorderLayout.NORTH);
		add(page, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
}
