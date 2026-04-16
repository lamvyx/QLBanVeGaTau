package view;

import controller.ChuyenTauController;
import entity.ChuyenTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import service.ChuyenTauService.KetQuaXuLy;

public class ChuyenTauCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");

	private JTable table;
	private JPanel formPanel;
	private DefaultTableModel model;
	private final ChuyenTauController chuyenTauController = new ChuyenTauController();
	private static final DateTimeFormatter DATE_TIME_INPUT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	private JTextField txtMaChuyenTau, txtGioKhoiHanh, txtGiaCoban;
	private JComboBox<String> cbTau, cbTuyenTau;
	private JButton btnCapNhat, btnXoa, btnHuy;

	public ChuyenTauCapNhatPage() {
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

		JLabel title = new JLabel("Cập nhật chuyến tàu");
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

		String[] columns = { "#", "Mã chuyến", "Tàu", "Tuyến", "Giờ khởi hành", "Giá cơ bản" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

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
		taiDuLieuBang();

		return content;
	}

	private JPanel taoFormPanel() {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(Color.WHITE);
		wrapper.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(15, 15, 15, 15)
		));

		JLabel lblHuongDan = new JLabel("Chọn chuyến tàu từ bảng trên để chỉnh sửa");
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

		// Mã chuyến (Read-only)
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Mã chuyến");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		txtMaChuyenTau = new JTextField(table.getValueAt(row, 1).toString());
		txtMaChuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtMaChuyenTau.setPreferredSize(new Dimension(200, 30));
		txtMaChuyenTau.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		txtMaChuyenTau.setEnabled(false);
		formContainer.add(txtMaChuyenTau, gbc);

		// Tàu
		gbc.gridx = 0;
		gbc.gridy = 1;
		lbl = new JLabel("Tàu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbTau = new JComboBox<>();
		cbTau.addItem(table.getValueAt(row, 2).toString());
		cbTau.addItem("TAU001");
		cbTau.addItem("TAU002");
		cbTau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cbTau.setPreferredSize(new Dimension(200, 30));
		formContainer.add(cbTau, gbc);

		// Tuyến tàu
		gbc.gridx = 0;
		gbc.gridy = 2;
		lbl = new JLabel("Tuyến tàu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbTuyenTau = new JComboBox<>();
		cbTuyenTau.addItem(table.getValueAt(row, 3).toString());
		cbTuyenTau.addItem("TT001");
		cbTuyenTau.addItem("TT002");
		cbTuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cbTuyenTau.setPreferredSize(new Dimension(200, 30));
		formContainer.add(cbTuyenTau, gbc);

		// Giờ khởi hành
		gbc.gridx = 0;
		gbc.gridy = 3;
		lbl = new JLabel("Giờ khởi hành *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtGioKhoiHanh = new JTextField(table.getValueAt(row, 4).toString());
		txtGioKhoiHanh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtGioKhoiHanh.setPreferredSize(new Dimension(200, 30));
		txtGioKhoiHanh.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		formContainer.add(txtGioKhoiHanh, gbc);

		// Giá cơ bản
		gbc.gridx = 0;
		gbc.gridy = 4;
		lbl = new JLabel("Giá cơ bản (VND) *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtGiaCoban = new JTextField(table.getValueAt(row, 5).toString());
		txtGiaCoban.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtGiaCoban.setPreferredSize(new Dimension(200, 30));
		txtGiaCoban.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		formContainer.add(txtGiaCoban, gbc);

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
		btnCapNhat.addActionListener(e -> xuLyCapNhatChuyenTau());
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
		btnHuy.addActionListener(e -> formPanel.removeAll());
		buttonPanel.add(btnHuy);

		formContainer.add(buttonPanel, gbc);

		JScrollPane scrollWrapper = new JScrollPane(formContainer);
		scrollWrapper.setBorder(null);
		formPanel.add(scrollWrapper, BorderLayout.CENTER);

		formPanel.revalidate();
		formPanel.repaint();
	}

	private void taiDuLieuBang() {
		if (model == null) {
			return;
		}
		model.setRowCount(0);
		List<ChuyenTau> ds = chuyenTauController.timKiemChuyenTau(null);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		int stt = 1;
		for (ChuyenTau ct : ds) {
			String ngayGio = ct.getNgayKhoiHanh() == null ? "" : ct.getNgayKhoiHanh().format(dtf);
			model.addRow(new Object[] { stt++, ct.getMaCT(), ct.getMaTau(), ct.getMaTuyenTau(), ngayGio, "-" });
		}
	}

	private void xuLyCapNhatChuyenTau() {
		if (txtMaChuyenTau == null) {
			return;
		}
		try {
			LocalDateTime ngayGio = LocalDateTime.parse(txtGioKhoiHanh.getText().trim(), DATE_TIME_INPUT);
			KetQuaXuLy ketQua = chuyenTauController.capNhatChuyenTau(txtMaChuyenTau.getText(),
					String.valueOf(cbTau.getSelectedItem()), String.valueOf(cbTuyenTau.getSelectedItem()), ngayGio, true);
			JOptionPane.showMessageDialog(this, ketQua.thongBao);
			if (ketQua.thanhCong) {
				taiDuLieuBang();
			}
		} catch (DateTimeParseException ex) {
			JOptionPane.showMessageDialog(this, "Giờ khởi hành không hợp lệ (dd/MM/yyyy HH:mm).");
		}
	}
}
