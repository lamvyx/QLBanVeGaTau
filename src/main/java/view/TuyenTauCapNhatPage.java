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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import service.TuyenTauService.KetQuaXuLy;

public class TuyenTauCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private final TuyenTauController tuyenTauController = new TuyenTauController();

	private JTable table;
	private DefaultTableModel model;
	private JPanel formPanel;
	private String selectedMaTT;

	private JTextField txtMaTT;
	private JTextField txtKhoangCach;
	private JTextField txtGaDiSearch;
	private JTextField txtGaDenSearch;
	private JComboBox<String> cboMaGaDi;
	private JComboBox<String> cboMaGaDen;

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
				new EmptyBorder(12, 14, 12, 14)));

		JLabel title = new JLabel("Cập nhật tuyến tàu");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);
		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout(0, 14));
		content.setBackground(Color.WHITE);
		content.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(14, 14, 14, 14)));

		formPanel = new JPanel();
		renderFormPanel();
		content.add(formPanel, BorderLayout.NORTH);

		String[] columns = { "#", "Mã tuyến", "Ga đi", "Ga đến", "Khoảng cách (km)" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		loadDataFromDatabase(null, null, null);

		table = new JTable(model);
		table.setRowHeight(46);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setBackground(MAU_CHINH);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.setGridColor(Color.decode("#E4EBF3"));
		table.setSelectionBackground(Color.decode("#B3D9FF"));
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					selectedMaTT = String.valueOf(table.getValueAt(row, 1));
					renderFormPanel();
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		content.add(scrollPane, BorderLayout.CENTER);
		return content;
	}

	private void renderFormPanel() {
		formPanel.removeAll();
		formPanel.setLayout(new GridBagLayout());
		formPanel.setBackground(Color.WHITE);
		formPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(15, 15, 15, 15)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 10, 8, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		if (selectedMaTT == null) {
			JLabel lblMa = taoLabel("Mã tuyến:");
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.2;
			formPanel.add(lblMa, gbc);

			txtMaTT = taoTextField("");
			gbc.gridx = 1;
			gbc.weightx = 0.8;
			formPanel.add(txtMaTT, gbc);

			JLabel lblGaDi = taoLabel("Ga đi:");
			gbc.gridx = 2;
			gbc.weightx = 0.2;
			formPanel.add(lblGaDi, gbc);

			txtGaDiSearch = taoTextField("");
			gbc.gridx = 3;
			gbc.weightx = 0.8;
			formPanel.add(txtGaDiSearch, gbc);

			JLabel lblGaDen = taoLabel("Ga đến:");
			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.weightx = 0.2;
			formPanel.add(lblGaDen, gbc);

			txtGaDenSearch = taoTextField("");
			gbc.gridx = 3;
			gbc.weightx = 0.8;
			formPanel.add(txtGaDenSearch, gbc);

			JPanel buttonPanel = taoButtonPanel();
			buttonPanel.add(taoNutChinh("Tìm kiếm", e -> loadDataFromDatabase(
					txtMaTT.getText().trim(),
					txtGaDiSearch.getText().trim(),
					txtGaDenSearch.getText().trim())));
			buttonPanel.add(taoNutPhu("Làm mới", e -> {
				txtMaTT.setText("");
				txtGaDiSearch.setText("");
				txtGaDenSearch.setText("");
				loadDataFromDatabase(null, null, null);
			}));

			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridwidth = 4;
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 1;
			formPanel.add(buttonPanel, gbc);
		} else {
			TuyenTau tuyenTau = timTuyenTauTheoMa(selectedMaTT);
			if (tuyenTau == null) {
				selectedMaTT = null;
				renderFormPanel();
				return;
			}

			JLabel lblMa = taoLabel("Mã tuyến:");
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.2;
			formPanel.add(lblMa, gbc);

			txtMaTT = taoTextField(tuyenTau.getMaTT());
			txtMaTT.setEditable(false);
			gbc.gridx = 1;
			gbc.weightx = 0.8;
			formPanel.add(txtMaTT, gbc);

			JLabel lblGaDi = taoLabel("Ga đi *:");
			gbc.gridx = 2;
			gbc.weightx = 0.2;
			formPanel.add(lblGaDi, gbc);

			cboMaGaDi = taoComboGa();
			cboMaGaDi.setSelectedItem(tuyenTau.getMaGaDi());
			gbc.gridx = 3;
			gbc.weightx = 0.8;
			formPanel.add(cboMaGaDi, gbc);

			JLabel lblGaDen = taoLabel("Ga đến *:");
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0.2;
			formPanel.add(lblGaDen, gbc);

			cboMaGaDen = taoComboGa();
			cboMaGaDen.setSelectedItem(tuyenTau.getMaGaDen());
			gbc.gridx = 1;
			gbc.weightx = 0.8;
			formPanel.add(cboMaGaDen, gbc);

			JLabel lblKhoangCach = taoLabel("Khoảng cách (km) *:");
			gbc.gridx = 2;
			gbc.weightx = 0.2;
			formPanel.add(lblKhoangCach, gbc);

			txtKhoangCach = taoTextField(String.valueOf(tuyenTau.getKhoangCach()));
			gbc.gridx = 3;
			gbc.weightx = 0.8;
			formPanel.add(txtKhoangCach, gbc);

			JPanel buttonPanel = taoButtonPanel();
			buttonPanel.add(taoNutMau("Lưu thay đổi", Color.decode("#00AA00"), e -> xuLyCapNhatTuyenTau()));
			buttonPanel.add(taoNutMau("Xóa", Color.decode("#DD3333"), e -> xuLyXoaTuyenTau()));
			buttonPanel.add(taoNutPhu("Đặt lại", e -> {
				selectedMaTT = null;
				table.clearSelection();
				renderFormPanel();
			}));

			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridwidth = 4;
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 1;
			formPanel.add(buttonPanel, gbc);
		}

		formPanel.revalidate();
		formPanel.repaint();
	}

	private void loadDataFromDatabase(String maTT, String gaDi, String gaDen) {
		model.setRowCount(0);
		List<TuyenTau> ds = tuyenTauController.timKiemTuyenTau(maTT, gaDi, gaDen);
		for (int i = 0; i < ds.size(); i++) {
			TuyenTau tt = ds.get(i);
			model.addRow(new Object[] { i + 1, tt.getMaTT(), tt.getMaGaDi(), tt.getMaGaDen(), tt.getKhoangCach() });
		}
	}

	private void xuLyCapNhatTuyenTau() {
		try {
			double khoangCach = Double.parseDouble(txtKhoangCach.getText().trim());
			String gaDi = (String) cboMaGaDi.getSelectedItem();
			String gaDen = (String) cboMaGaDen.getSelectedItem();
			KetQuaXuLy ketQua = tuyenTauController.capNhatTuyenTau(txtMaTT.getText().trim(), gaDi, gaDen, khoangCach);
			JOptionPane.showMessageDialog(this, ketQua.thongBao,
					ketQua.thanhCong ? "Thành công" : "Lỗi",
					ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
			if (ketQua.thanhCong) {
				selectedMaTT = null;
				table.clearSelection();
				loadDataFromDatabase(null, null, null);
				renderFormPanel();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Khoảng cách phải là số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void xuLyXoaTuyenTau() {
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
			selectedMaTT = null;
			table.clearSelection();
			loadDataFromDatabase(null, null, null);
			renderFormPanel();
		}
	}

	private TuyenTau timTuyenTauTheoMa(String maTT) {
		List<TuyenTau> ds = tuyenTauController.timKiemTuyenTau(maTT, null, null);
		return ds.isEmpty() ? null : ds.get(0);
	}

	private JComboBox<String> taoComboGa() {
		JComboBox<String> combo = new JComboBox<>();
		combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		combo.setPreferredSize(new Dimension(0, 35));
		for (String ga : layDanhSachGa()) {
			combo.addItem(ga);
		}
		return combo;
	}

	private List<String> layDanhSachGa() {
		Set<String> setGa = new HashSet<>();
		for (TuyenTau tt : new TuyenTau_DAO().layTatCaTuyenTau()) {
			if (tt.getMaGaDi() != null && !tt.getMaGaDi().isBlank()) {
				setGa.add(tt.getMaGaDi());
			}
			if (tt.getMaGaDen() != null && !tt.getMaGaDen().isBlank()) {
				setGa.add(tt.getMaGaDen());
			}
		}
		List<String> danhSach = new ArrayList<>(setGa);
		Collections.sort(danhSach);
		return danhSach;
	}

	private JLabel taoLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.BOLD, 13));
		label.setForeground(Color.decode("#2B4B74"));
		return label;
	}

	private JTextField taoTextField(String text) {
		JTextField field = new JTextField(text == null ? "" : text);
		field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		field.setPreferredSize(new Dimension(0, 35));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)));
		return field;
	}

	private JPanel taoButtonPanel() {
		JPanel panel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
		panel.setOpaque(false);
		return panel;
	}

	private JButton taoNutChinh(String text, java.awt.event.ActionListener action) {
		return taoNutMau(text, MAU_CHINH, action);
	}

	private JButton taoNutPhu(String text, java.awt.event.ActionListener action) {
		JButton button = new JButton(text);
		button.setFont(new Font("Segoe UI", Font.BOLD, 13));
		button.setBackground(Color.WHITE);
		button.setForeground(Color.decode("#3A4D66"));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 16, 6, 16)));
		button.setPreferredSize(new Dimension(110, 38));
		button.addActionListener(action);
		return button;
	}

	private JButton taoNutMau(String text, Color color, java.awt.event.ActionListener action) {
		JButton button = new JButton(text);
		button.setFont(new Font("Segoe UI", Font.BOLD, 13));
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(120, 38));
		button.addActionListener(action);
		return button;
	}
}
