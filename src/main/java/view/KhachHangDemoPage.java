package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class KhachHangDemoPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private CardLayout cardLayout;
	private JPanel cardPanel;

	public KhachHangDemoPage() {
		setTitle("Quản lý Khách hàng - Demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700);
		setLocationRelativeTo(null);
		setResizable(true);

		// Navigation panel
		JPanel navPanel = new JPanel();
		navPanel.setBackground(MAU_CHINH);
		navPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JButton btnThem = new JButton("Thêm khách hàng");
		btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnThem.setForeground(Color.WHITE);
		btnThem.setBackground(MAU_CHINH);
		btnThem.setFocusPainted(false);
		btnThem.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		btnThem.addActionListener(e -> cardLayout.show(cardPanel, "them"));

		JButton btnTraCuu = new JButton("Tra cứu khách hàng");
		btnTraCuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnTraCuu.setForeground(Color.WHITE);
		btnTraCuu.setBackground(MAU_CHINH);
		btnTraCuu.setFocusPainted(false);
		btnTraCuu.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		btnTraCuu.addActionListener(e -> cardLayout.show(cardPanel, "tracuu"));

		JButton btnCapNhat = new JButton("Cập nhật khách hàng");
		btnCapNhat.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnCapNhat.setForeground(Color.WHITE);
		btnCapNhat.setBackground(MAU_CHINH);
		btnCapNhat.setFocusPainted(false);
		btnCapNhat.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		btnCapNhat.addActionListener(e -> cardLayout.show(cardPanel, "capnhat"));

		navPanel.add(btnThem);
		navPanel.add(btnTraCuu);
		navPanel.add(btnCapNhat);

		// Card panel
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		cardPanel.setBackground(Color.decode("#F0F5F9"));

		cardPanel.add(new KhachHangThemPage(), "them");
		cardPanel.add(new KhachHangTraCuuPage(), "tracuu");
		cardPanel.add(new KhachHangCapNhatPage(), "capnhat");

		add(navPanel, BorderLayout.NORTH);
		add(cardPanel, BorderLayout.CENTER);

		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new KhachHangDemoPage());
	}
}
