package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class NhanVienPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color NAV_BG = Color.decode("#173A72");
	private static final Color TEXT_LIGHT = Color.decode("#EAF2FF");
	private static final Color MAU_CHINH = Color.decode("#4682A9");

	public NhanVienPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));

		// Header
		JPanel header = taoHeaderPanel();
		add(header, BorderLayout.NORTH);

		// Content
		JPanel content = taoContentPanel();
		add(content, BorderLayout.CENTER);
	}

	private JPanel taoHeaderPanel() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#E0E0E0")),
			new EmptyBorder(12, 16, 12, 16)
		));

		JLabel title = new JLabel("Quản lý Nhân viên");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);

		JButton btnThem = new JButton("+ Thêm nhân viên");
		btnThem.setBackground(MAU_CHINH);
		btnThem.setForeground(Color.WHITE);
		btnThem.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnThem.setFocusPainted(false);
		btnThem.setBorder(new EmptyBorder(6, 12, 6, 12));
		btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		actions.add(btnThem);

		header.add(actions, BorderLayout.EAST);
		return header;
	}

	private JPanel taoContentPanel() {
		JPanel content = new JPanel(new BorderLayout());
		content.setBackground(Color.decode("#F0F5F9"));
		content.setBorder(new EmptyBorder(12, 16, 12, 16));

		// Tạo bảng nhân viên
		String[] columns = { "ID", "Họ tên", "Email", "Số điện thoại", "Chức vụ", "Ngày vào làm" };
		DefaultTableModel model = new DefaultTableModel(columns, 0);

		// Sample data
		model.addRow(new Object[] { 1, "Nguyễn Văn A", "a@email.com", "0912345678", "Nhân viên", "2023-01-15" });
		model.addRow(new Object[] { 2, "Trần Thị B", "b@email.com", "0923456789", "Trưởng ca", "2022-05-20" });
		model.addRow(new Object[] { 3, "Phạm Văn C", "c@email.com", "0934567890", "Nhân viên", "2023-06-10" });

		JTable table = new JTable(model);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		table.setRowHeight(28);
		table.setShowGrid(true);
		table.setGridColor(new Color(220, 220, 220));
		table.setSelectionBackground(Color.decode("#E8F0F9"));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(800, 400));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}
}
