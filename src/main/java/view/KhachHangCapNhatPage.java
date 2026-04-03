package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class KhachHangCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");

	private JTable table;
	private JPanel formPanel;
	
	private JTextField txtTen, txtSdt, txtCccd, txtEmail, txtDiaChi;
	private JComboBox<String> cbGioiTinh, cbLoaiKH;
	private JButton btnCapNhat, btnXoa, btnHuy;

	public KhachHangCapNhatPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoContent(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		JLabel title = new JLabel("Cập nhật thông tin khách hàng");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout());
		content.setBackground(Color.WHITE);
		content.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(14, 14, 14, 14)
		));

		// Tạo bảng
		String[] columns = { "#", "Mã KH", "Tên khách hàng", "Số ĐT", "Email", "CCCD", "Loại KH" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		model.addRow(new Object[] { 1, "KH001", "Trần Văn A", "0901234567", "tryvana@email.com", "012345678901", "Thường" });
		model.addRow(new Object[] { 2, "KH002", "Nguyễn Thị B", "0912345678", "nguyenb@email.com", "012345678902", "VIP" });
		model.addRow(new Object[] { 3, "KH003", "Phạm Văn C", "0923456789", "phamvan@email.com", "012345678903", "Thường" });
		model.addRow(new Object[] { 4, "KH004", "Hoàng Thị D", "0934567890", "hoang.d@email.com", "012345678904", "Doanh nghiệp" });
		model.addRow(new Object[] { 5, "KH005", "Võ Văn E", "0945678901", "vo.van.e@email.com", "012345678905", "VIP" });

		table = new JTable(model);
		table.setRowHeight(50);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setBackground(MAU_CHINH);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.setGridColor(Color.decode("#E4EBF3"));
		table.setSelectionBackground(Color.decode("#B3D9FF"));

		// Xử lý click vào hàng
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					displayForm(row);
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));

		// Form panel
		formPanel = taoFormPanel();

		// Split pane
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, formPanel);
		splitPane.setDividerLocation(300);
		splitPane.setBorder(null);
		content.add(splitPane, BorderLayout.CENTER);

		return content;
	}

	private JPanel taoFormPanel() {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(Color.WHITE);
		wrapper.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(15, 15, 15, 15)
		));

		// Label hướng dẫn
		JLabel lblHuongDan = new JLabel("Chọn khách hàng từ bảng trên để chỉnh sửa");
		lblHuongDan.setFont(new Font("Segoe UI", Font.ITALIC, 14));
		lblHuongDan.setForeground(Color.decode("#8B95A7"));
		wrapper.add(lblHuongDan, BorderLayout.CENTER);

		return wrapper;
	}

	private void displayForm(int row) {
		formPanel.removeAll();
		formPanel.setLayout(new BorderLayout());
		formPanel.setBackground(Color.WHITE);

		// Form chính
		JPanel formContainer = new JPanel(new GridBagLayout());
		formContainer.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Row 0: Tên khách hàng
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Tên khách hàng *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		txtTen = new JTextField(table.getValueAt(row, 2).toString());
		txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTen.setPreferredSize(new Dimension(250, 35));
		txtTen.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtTen, gbc);

		// Row 1: Số điện thoại
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		lbl = new JLabel("Số điện thoại *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		txtSdt = new JTextField(table.getValueAt(row, 3).toString());
		txtSdt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSdt.setPreferredSize(new Dimension(250, 35));
		txtSdt.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtSdt, gbc);

		// Row 2: CCCD
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.3;
		lbl = new JLabel("CCCD/Passport *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		txtCccd = new JTextField(table.getValueAt(row, 5).toString());
		txtCccd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtCccd.setPreferredSize(new Dimension(250, 35));
		txtCccd.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtCccd, gbc);

		// Row 3: Email
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.3;
		lbl = new JLabel("Email");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		txtEmail = new JTextField(table.getValueAt(row, 4).toString());
		txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtEmail.setPreferredSize(new Dimension(250, 35));
		txtEmail.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtEmail, gbc);

		// Row 4: Địa chỉ
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.3;
		lbl = new JLabel("Địa chỉ");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		txtDiaChi = new JTextField();
		txtDiaChi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtDiaChi.setPreferredSize(new Dimension(250, 35));
		txtDiaChi.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtDiaChi, gbc);

		// Row 5: Giới tính
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0.3;
		lbl = new JLabel("Giới tính");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
		cbGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbGioiTinh.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		formContainer.add(cbGioiTinh, gbc);

		// Row 6: Loại khách hàng
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 0.3;
		lbl = new JLabel("Loại khách hàng");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		cbLoaiKH = new JComboBox<>(new String[]{"Khách hàng thường", "Khách hàng VIP", "Khách hàng doanh nghiệp"});
		cbLoaiKH.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbLoaiKH.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		formContainer.add(cbLoaiKH, gbc);

		JPanel scrollWrapper = new JPanel(new BorderLayout());
		scrollWrapper.setBackground(Color.WHITE);
		JScrollPane scrollPane = new JScrollPane(formContainer);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);
		scrollWrapper.add(scrollPane, BorderLayout.CENTER);

		// Nút hành động
		JPanel actions = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
		actions.setBackground(Color.WHITE);

		btnCapNhat = new JButton("Cập nhật");
		btnCapNhat.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnCapNhat.setBackground(Color.decode("#00AA00"));
		btnCapNhat.setForeground(Color.WHITE);
		btnCapNhat.setFocusPainted(false);
		btnCapNhat.setBorder(new EmptyBorder(8, 20, 8, 20));
		btnCapNhat.setPreferredSize(new Dimension(120, 40));

		btnXoa = new JButton("Xóa");
		btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnXoa.setBackground(Color.decode("#FF0000"));
		btnXoa.setForeground(Color.WHITE);
		btnXoa.setFocusPainted(false);
		btnXoa.setBorder(new EmptyBorder(8, 20, 8, 20));
		btnXoa.setPreferredSize(new Dimension(100, 40));

		btnHuy = new JButton("Hủy");
		btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnHuy.setBackground(Color.WHITE);
		btnHuy.setForeground(Color.decode("#3A4D66"));
		btnHuy.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 20, 8, 20)
		));
		btnHuy.setPreferredSize(new Dimension(100, 40));

		btnHuy.addActionListener(e -> {
			formPanel.removeAll();
			formPanel.setLayout(new BorderLayout());
			JLabel lblHuongDan = new JLabel("Chọn khách hàng từ bảng trên để chỉnh sửa");
			lblHuongDan.setFont(new Font("Segoe UI", Font.ITALIC, 14));
			lblHuongDan.setForeground(Color.decode("#8B95A7"));
			formPanel.add(lblHuongDan, BorderLayout.CENTER);
			formPanel.revalidate();
			formPanel.repaint();
		});

		actions.add(btnCapNhat);
		actions.add(btnXoa);
		actions.add(btnHuy);

		formPanel.add(scrollWrapper, BorderLayout.CENTER);
		formPanel.add(actions, BorderLayout.SOUTH);
		formPanel.revalidate();
		formPanel.repaint();
	}
}
