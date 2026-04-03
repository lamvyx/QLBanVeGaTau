package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class NhanVienCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");

	private JTable table;
	private JPanel formPanel;
	private JLabel lblHinhAnh;
	private String selectedImagePath = "";
	
	private JTextField txtTen, txtUsername, txtSdt, txtEmail, cbChucVu;
	private JButton btnCapNhat, btnXoa, btnHuy;

	public NhanVienCapNhatPage() {
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

		JLabel title = new JLabel("Cập nhật thông tin nhân viên");
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
		String[] columns = { "#", "Mã NV", "Họ và tên", "Tài khoản", "Email", "Điện thoại", "Chức vụ" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		model.addRow(new Object[] { 1, "U001", "Nguyễn Văn Admin", "admin", "admin@saigontrain.vn", "0901234567", "Quản lý" });
		model.addRow(new Object[] { 2, "U002", "Trần Thị Nhân Viên", "staff", "staff@saigontrain.vn", "0901234568", "Bán vé" });
		model.addRow(new Object[] { 3, "U003", "Nguyễn Phát Đạt", "nguyen.phat", "phat@saigontrain.vn", "0901234569", "Bán vé" });
		model.addRow(new Object[] { 4, "U004", "Lê Thị Mai", "le.mai", "mai@saigontrain.vn", "0901234570", "Hỗ trợ" });
		model.addRow(new Object[] { 5, "U005", "Phạm Văn Cường", "pham.cuong", "cuong@saigontrain.vn", "0901234571", "Quản lý" });

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
		JLabel lblHuongDan = new JLabel("Chọn nhân viên từ bảng trên để chỉnh sửa");
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

		// Row 0: Hình ảnh (to ra, chiếm 2 cột)
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 4;
		gbc.weightx = 0.4;
		JPanel imagePanel = taoImagePanel();
		formContainer.add(imagePanel, gbc);

		// Reset gridwidth về 1 cho các trường khác
		gbc.gridwidth = 1;
		gbc.gridheight = 1;

		// Row 0: Họ và tên
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Họ và tên *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		txtTen = new JTextField(table.getValueAt(row, 2).toString());
		txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTen.setPreferredSize(new Dimension(200, 35));
		txtTen.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtTen, gbc);

		// Row 1: Tên tài khoản
		gbc.gridx = 2;
		gbc.gridy = 1;
		lbl = new JLabel("Tên tài khoản *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		txtUsername = new JTextField(table.getValueAt(row, 3).toString());
		txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtUsername.setPreferredSize(new Dimension(200, 35));
		txtUsername.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtUsername, gbc);

		// Row 2: Số điện thoại
		gbc.gridx = 2;
		gbc.gridy = 2;
		lbl = new JLabel("Số điện thoại");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		txtSdt = new JTextField(table.getValueAt(row, 5).toString());
		txtSdt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSdt.setPreferredSize(new Dimension(200, 35));
		txtSdt.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtSdt, gbc);

		// Row 3: Chức vụ
		gbc.gridx = 2;
		gbc.gridy = 3;
		lbl = new JLabel("Chức vụ");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 3;
		cbChucVu = new JTextField(table.getValueAt(row, 6).toString());
		cbChucVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbChucVu.setPreferredSize(new Dimension(200, 35));
		cbChucVu.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(cbChucVu, gbc);

		// Row 4: Email
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.25;
		lbl = new JLabel("Email *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtEmail = new JTextField(table.getValueAt(row, 4).toString());
		txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtEmail.setPreferredSize(new Dimension(150, 35));
		txtEmail.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(8, 8, 8, 8)
		));
		formContainer.add(txtEmail, gbc);

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
			JLabel lblHuongDan = new JLabel("Chọn nhân viên từ bảng trên để chỉnh sửa");
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

	private JPanel taoImagePanel() {
		JPanel imagePanel = new JPanel(new BorderLayout(10, 10));
		imagePanel.setBackground(Color.decode("#F8FAFC"));
		imagePanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5"), 2),
			new EmptyBorder(10, 10, 10, 10)
		));

		// Khu vực hiển thị hình ảnh
		lblHinhAnh = new JLabel();
		lblHinhAnh.setHorizontalAlignment(JLabel.CENTER);
		lblHinhAnh.setVerticalAlignment(JLabel.CENTER);
		lblHinhAnh.setText("Chưa chọn hình ảnh");
		lblHinhAnh.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		lblHinhAnh.setForeground(Color.decode("#8B95A7"));
		lblHinhAnh.setPreferredSize(new Dimension(180, 200));
		lblHinhAnh.setBackground(Color.WHITE);
		lblHinhAnh.setOpaque(true);
		lblHinhAnh.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		imagePanel.add(lblHinhAnh, BorderLayout.CENTER);

		// Nút chọn hình ảnh
		JPanel btnPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 5));
		btnPanel.setBackground(Color.decode("#F8FAFC"));

		JButton btnChon = new JButton("Chọn hình");
		btnChon.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnChon.setBackground(MAU_CHINH);
		btnChon.setForeground(Color.WHITE);
		btnChon.setFocusPainted(false);
		btnChon.setBorder(new EmptyBorder(6, 12, 6, 12));

		btnChon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
				int result = fileChooser.showOpenDialog(NhanVienCapNhatPage.this);

				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					selectedImagePath = selectedFile.getAbsolutePath();

					// Hiển thị ảnh
					ImageIcon icon = new ImageIcon(selectedImagePath);
					java.awt.Image image = icon.getImage().getScaledInstance(160, 180, java.awt.Image.SCALE_SMOOTH);
					lblHinhAnh.setIcon(new ImageIcon(image));
					lblHinhAnh.setText("");
				}
			}
		});

		btnPanel.add(btnChon);
		imagePanel.add(btnPanel, BorderLayout.SOUTH);

		return imagePanel;
	}
}
