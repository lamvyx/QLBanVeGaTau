package view;

import controller.TuyenTauController;
import dao.TuyenTau_DAO;
import entity.TuyenTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import service.TuyenTauService.KetQuaXuLy;

public class TuyenTauCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");

	private JTable table;
	private JPanel formPanel;
	private DefaultTableModel model;
	
	private JTextField txtMaTT, txtKhoangCach;
	private JComboBox<String> cboMaGaDi, cboMaGaDen;
	private JButton btnCapNhat, btnXoa, btnHuy;
	private final TuyenTauController tuyenTauController = new TuyenTauController();

	public TuyenTauCapNhatPage() {
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

		JLabel title = new JLabel("Cập nhật tuyến tàu");
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
		String[] columns = { "#", "Mã tuyến", "Ga đi", "Ga đến", "Khoảng cách (km)" };
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

		// Label hướng dẫn
		JLabel lblHuongDan = new JLabel("Chọn tuyến tàu từ bảng trên để chỉnh sửa");
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

		// Row 0: Mã tuyến (Read-only)
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Mã tuyến");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		txtMaTT = new JTextField(table.getValueAt(row, 1).toString());
		txtMaTT.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtMaTT.setPreferredSize(new Dimension(200, 30));
		txtMaTT.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		txtMaTT.setEnabled(false);
		formContainer.add(txtMaTT, gbc);

		// Row 1: Mã ga đi
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		lbl = new JLabel("Ga đi *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		cboMaGaDi = new JComboBox<>();
		cboMaGaDi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cboMaGaDi.setPreferredSize(new Dimension(200, 30));
		formContainer.add(cboMaGaDi, gbc);

		// Row 2: Mã ga đến
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.3;
		lbl = new JLabel("Ga đến *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		cboMaGaDen = new JComboBox<>();
		cboMaGaDen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cboMaGaDen.setPreferredSize(new Dimension(200, 30));
		formContainer.add(cboMaGaDen, gbc);

		// Row 3: Khoảng cách
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.3;
		lbl = new JLabel("Khoảng cách (km) *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		txtKhoangCach = new JTextField(table.getValueAt(row, 4).toString());
		txtKhoangCach.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtKhoangCach.setPreferredSize(new Dimension(200, 30));
		txtKhoangCach.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		formContainer.add(txtKhoangCach, gbc);

		// Buttons
		gbc.gridx = 0;
		gbc.gridy = 4;
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
		btnCapNhat.addActionListener(e -> xuLyCapNhatTuyenTau());
		buttonPanel.add(btnCapNhat);

		btnXoa = new JButton("Xóa");
		btnXoa.setBackground(Color.decode("#FF6B6B"));
		btnXoa.setForeground(Color.WHITE);
		btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnXoa.setFocusPainted(false);
		btnXoa.setBorder(new EmptyBorder(6, 16, 6, 16));
		btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnXoa.addActionListener(e -> xuLyXoaTuyenTau());
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

		// load station lists into combos and try select current
		try {
			TuyenTau_DAO dao = new TuyenTau_DAO();
			List<TuyenTau> ds = dao.layTatCaTuyenTau();
			Set<String> setGa = new HashSet<>();
			for (TuyenTau tt : ds) {
				if (tt.getMaGaDi() != null) setGa.add(tt.getMaGaDi());
				if (tt.getMaGaDen() != null) setGa.add(tt.getMaGaDen());
			}
			List<String> sortedGa = new ArrayList<>(setGa);
			Collections.sort(sortedGa);
			cboMaGaDi.removeAllItems(); cboMaGaDen.removeAllItems();
			cboMaGaDi.addItem("-- Chọn ga --"); cboMaGaDen.addItem("-- Chọn ga --");
			for (String g : sortedGa) { cboMaGaDi.addItem(g); cboMaGaDen.addItem(g); }
			// select current values
			String curDi = table.getValueAt(row, 2).toString();
			String curDen = table.getValueAt(row, 3).toString();
			for (int i = 0; i < cboMaGaDi.getItemCount(); i++) if (cboMaGaDi.getItemAt(i).equals(curDi)) { cboMaGaDi.setSelectedIndex(i); break; }
			for (int i = 0; i < cboMaGaDen.getItemCount(); i++) if (cboMaGaDen.getItemAt(i).equals(curDen)) { cboMaGaDen.setSelectedIndex(i); break; }
		} catch (Exception ex) {}
	}

	private void taiDuLieuBang() {
		if (model == null) {
			return;
		}
		model.setRowCount(0);
		List<TuyenTau> ds = tuyenTauController.timKiemTuyenTau(null, null, null);
		int stt = 1;
		for (TuyenTau tt : ds) {
			model.addRow(new Object[] { stt++, tt.getMaTT(), tt.getMaGaDi(), tt.getMaGaDen(), tt.getKhoangCach() });
		}
	}

	private void xuLyCapNhatTuyenTau() {
		if (txtMaTT == null) {
			return;
		}
		try {
			double khoangCach = Double.parseDouble(txtKhoangCach.getText().trim());
			String gaDi = (String) cboMaGaDi.getSelectedItem();
			String gaDen = (String) cboMaGaDen.getSelectedItem();
			if (gaDi != null && gaDi.startsWith("--")) gaDi = null;
			if (gaDen != null && gaDen.startsWith("--")) gaDen = null;
			if (gaDi == null || gaDen == null) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn cả ga đi và ga đến.");
				return;
			}
			KetQuaXuLy ketQua = tuyenTauController.capNhatTuyenTau(txtMaTT.getText(), gaDi, gaDen, khoangCach);
			JOptionPane.showMessageDialog(this, ketQua.thongBao);
			if (ketQua.thanhCong) {
				taiDuLieuBang();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Khoảng cách phải là số hợp lệ.");
		}
	}

	private void xuLyXoaTuyenTau() {
		if (txtMaTT == null || txtMaTT.getText().trim().isEmpty()) {
			return;
		}
		String maTT = txtMaTT.getText().trim();
		int xacNhan = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc muốn xóa tuyến tàu " + maTT + " không?",
				"Xác nhận xóa tuyến tàu",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (xacNhan != JOptionPane.YES_OPTION) {
			return;
		}

		KetQuaXuLy ketQua = tuyenTauController.xoaTuyenTau(maTT);
		JOptionPane.showMessageDialog(this, ketQua.thongBao,
				ketQua.thanhCong ? "Thành công" : "Lỗi",
				ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
		if (ketQua.thanhCong) {
			taiDuLieuBang();
			formPanel.removeAll();
			formPanel.revalidate();
			formPanel.repaint();
		}
	}
}
