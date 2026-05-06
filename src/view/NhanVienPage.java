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
	private static final Color MAU_CHINH = AppTheme.PRIMARY;

	public NhanVienPage() {
		setLayout(new BorderLayout());
		setBackground(AppTheme.PAGE_BG);

		// Header
		JPanel header = taoHeaderPanel();
		add(header, BorderLayout.NORTH);

		// Content
		JPanel content = taoContentPanel();
		add(content, BorderLayout.CENTER);
	}

	private JPanel taoHeaderPanel() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(AppTheme.CARD_BG);
		header.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Quản lý Nhân viên");
		title.setFont(AppTheme.font(Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);

		JButton btnThem = new JButton("+ Thêm nhân viên");
		AppTheme.stylePrimaryButton(btnThem);
		actions.add(btnThem);

		header.add(actions, BorderLayout.EAST);
		return header;
	}

	private JPanel taoContentPanel() {
		JPanel content = new JPanel(new BorderLayout());
		content.setBackground(AppTheme.PAGE_BG);
		content.setBorder(new EmptyBorder(12, 16, 12, 16));

		// Tạo bảng nhân viên
		String[] columns = { "ID", "Họ tên", "Email", "Số điện thoại", "Chức vụ", "Ngày vào làm" };
		DefaultTableModel model = new DefaultTableModel(columns, 0);

		// Sample data
		model.addRow(new Object[] { 1, "Nguyễn Văn A", "a@email.com", "0912345678", "Nhân viên", "2023-01-15" });
		model.addRow(new Object[] { 2, "Trần Thị B", "b@email.com", "0923456789", "Trưởng ca", "2022-05-20" });
		model.addRow(new Object[] { 3, "Phạm Văn C", "c@email.com", "0934567890", "Nhân viên", "2023-06-10" });

		JTable table = new JTable(model);
		AppTheme.styleTable(table);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		scrollPane.setPreferredSize(new Dimension(800, 400));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}
}
