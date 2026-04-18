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

public class ToaCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");

	private JTable table;
	private JPanel formPanel;
	
	private JTextField txtMaToa, txtSoGhe, txtViTriToa;
	private JComboBox<String> cbLoaiToa, cbTau;
	private JButton btnCapNhat, btnXoa, btnHuy;

	public ToaCapNhatPage() {
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

		JLabel title = new JLabel("Cập nhật toa");
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

		String[] columns = { "Mã toa", "Loại toa", "Tàu", "Số ghế", "Vị trí" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		model.addRow(new Object[] { "TOA001", "Toa ngủ", "T001", "50", "Toa 1" });
		model.addRow(new Object[] { "TOA002", "Toa ngồi", "T001", "80", "Toa 2" });
		model.addRow(new Object[] { "TOA003", "Toa hàng hoá", "T002", "100", "Toa 1" });
		model.addRow(new Object[] { "TOA004", "Toa nhà bếp", "T002", "20", "Toa 2" });

		table = new JTable(model);
		table.setRowHeight(40);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		table.getTableHeader().setBackground(MAU_CHINH);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		table.setGridColor(Color.decode("#E4EBF3"));
		table.setSelectionBackground(Color.decode("#B3D9FF"));

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

		formPanel = taoFormPanel();

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, formPanel);
		splitPane.setDividerLocation(250);
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

		JLabel lblHuongDan = new JLabel("Chọn toa từ bảng trên để chỉnh sửa");
		lblHuongDan.setFont(new Font("Segoe UI", Font.ITALIC, 13));
		lblHuongDan.setForeground(Color.decode("#8B95A7"));
		wrapper.add(lblHuongDan, BorderLayout.CENTER);

		return wrapper;
	}

	private void displayForm(int row) {
		formPanel.removeAll();
		formPanel.setLayout(new BorderLayout());
		formPanel.setBackground(Color.WHITE);

		JPanel formContainer = new JPanel(new GridBagLayout());
		formContainer.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Mã toa (Read-only)
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Mã toa");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		txtMaToa = new JTextField(table.getValueAt(row, 1).toString());
		txtMaToa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtMaToa.setPreferredSize(new Dimension(200, 30));
		txtMaToa.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		txtMaToa.setEnabled(false);
		formContainer.add(txtMaToa, gbc);

		// Loại toa
		gbc.gridx = 0;
		gbc.gridy = 1;
		lbl = new JLabel("Loại toa *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbLoaiToa = new JComboBox<>();
		cbLoaiToa.addItem(table.getValueAt(row, 2).toString());
		cbLoaiToa.addItem("Toa ngủ");
		cbLoaiToa.addItem("Toa ngồi");
		cbLoaiToa.addItem("Toa hàng hoá");
		cbLoaiToa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cbLoaiToa.setPreferredSize(new Dimension(200, 30));
		formContainer.add(cbLoaiToa, gbc);

		// Tàu
		gbc.gridx = 0;
		gbc.gridy = 2;
		lbl = new JLabel("Tàu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbTau = new JComboBox<>();
		cbTau.addItem(table.getValueAt(row, 3).toString());
		cbTau.addItem("T001");
		cbTau.addItem("T002");
		cbTau.addItem("T003");
		cbTau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cbTau.setPreferredSize(new Dimension(200, 30));
		formContainer.add(cbTau, gbc);

		// Số ghế
		gbc.gridx = 0;
		gbc.gridy = 3;
		lbl = new JLabel("Số ghế *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtSoGhe = new JTextField(table.getValueAt(row, 4).toString());
		txtSoGhe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtSoGhe.setPreferredSize(new Dimension(200, 30));
		txtSoGhe.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		formContainer.add(txtSoGhe, gbc);

		// Vị trí toa
		gbc.gridx = 0;
		gbc.gridy = 4;
		lbl = new JLabel("Vị trí toa *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtViTriToa = new JTextField(table.getValueAt(row, 5).toString());
		txtViTriToa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtViTriToa.setPreferredSize(new Dimension(200, 30));
		txtViTriToa.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		formContainer.add(txtViTriToa, gbc);

		// Buttons
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(12, 8, 8, 8);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));

		btnCapNhat = new JButton("Cập nhật");
		btnCapNhat.setBackground(MAU_CHINH);
		btnCapNhat.setForeground(Color.WHITE);
		btnCapNhat.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnCapNhat.setFocusPainted(false);
		btnCapNhat.setBorder(new EmptyBorder(6, 16, 6, 16));
		btnCapNhat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		buttonPanel.add(btnCapNhat);

		btnXoa = new JButton("Xóa");
		btnXoa.setBackground(Color.decode("#FF6B6B"));
		btnXoa.setForeground(Color.WHITE);
		btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnXoa.setFocusPainted(false);
		btnXoa.setBorder(new EmptyBorder(6, 16, 6, 16));
		btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		buttonPanel.add(btnXoa);

		btnHuy = new JButton("Hủy");
		btnHuy.setBackground(Color.WHITE);
		btnHuy.setForeground(Color.decode("#2B4B74"));
		btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnHuy.setFocusPainted(false);
		btnHuy.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnHuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		buttonPanel.add(btnHuy);

		formContainer.add(buttonPanel, gbc);

		JScrollPane scrollWrapper = new JScrollPane(formContainer);
		scrollWrapper.setBorder(null);
		formPanel.add(scrollWrapper, BorderLayout.CENTER);

		formPanel.revalidate();
		formPanel.repaint();
	}
}
