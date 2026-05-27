package view;

import controller.ToaController;
import entity.Toa;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
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
import service.ToaService.KetQuaXuLy;

public class ToaCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private final ToaController toaController = new ToaController();

	private JTable table;
	private DefaultTableModel model;
	private JPanel formPanel;
	private String selectedMaToa;

	private JTextField txtMaToa;
	private JTextField txtSoGhe;
	private JTextField txtViTriToa;
	private JComboBox<String> cbLoaiToa;
	private JComboBox<String> cbTau;

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
				new EmptyBorder(12, 14, 12, 14)));

		JLabel title = new JLabel("Cập nhật toa");
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

		String[] columns = { "#", "Mã toa", "Loại toa", "Tàu", "Số ghế", "Vị trí" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		loadDataFromDatabase(null);

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
					selectedMaToa = String.valueOf(table.getValueAt(row, 1));
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

		if (selectedMaToa == null) {
			JLabel lblMa = taoLabel("Mã toa:");
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.2;
			formPanel.add(lblMa, gbc);

			txtMaToa = taoTextField("");
			gbc.gridx = 1;
			gbc.weightx = 0.8;
			formPanel.add(txtMaToa, gbc);

			JPanel buttonPanel = taoButtonPanel();
			buttonPanel.add(taoNutChinh("Tìm kiếm", e -> loadDataFromDatabase(txtMaToa.getText().trim())));
			buttonPanel.add(taoNutPhu("Làm mới", e -> {
				txtMaToa.setText("");
				loadDataFromDatabase(null);
			}));

			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 4;
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 1;
			formPanel.add(buttonPanel, gbc);
		} else {
			Toa toa = timToaTheoMa(selectedMaToa);
			if (toa == null) {
				selectedMaToa = null;
				renderFormPanel();
				return;
			}

			JLabel lblMa = taoLabel("Mã toa:");
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.2;
			formPanel.add(lblMa, gbc);

			txtMaToa = taoTextField(toa.getMaToa());
			txtMaToa.setEditable(false);
			gbc.gridx = 1;
			gbc.weightx = 0.8;
			formPanel.add(txtMaToa, gbc);

			JLabel lblLoai = taoLabel("Loại toa *:");
			gbc.gridx = 2;
			gbc.weightx = 0.2;
			formPanel.add(lblLoai, gbc);

			cbLoaiToa = new JComboBox<>(new String[] { "Ngồi mềm", "Nằm điều hòa", "Ghế cứng", "Ngồi cứng", "Toa hàng hoá" });
			cbLoaiToa.setSelectedItem(toa.getLoaiToa());
			cbLoaiToa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			cbLoaiToa.setPreferredSize(new Dimension(0, 35));
			gbc.gridx = 3;
			gbc.weightx = 0.8;
			formPanel.add(cbLoaiToa, gbc);

			JLabel lblTau = taoLabel("Mã tàu *:");
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0.2;
			formPanel.add(lblTau, gbc);

			cbTau = new JComboBox<>();
			for (String maTau : toaController.layDanhSachMaTau()) {
				cbTau.addItem(maTau);
			}
			cbTau.setSelectedItem(toa.getMaTau());
			cbTau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			cbTau.setPreferredSize(new Dimension(0, 35));
			gbc.gridx = 1;
			gbc.weightx = 0.8;
			formPanel.add(cbTau, gbc);

			JLabel lblSoGhe = taoLabel("Số ghế *:");
			gbc.gridx = 2;
			gbc.weightx = 0.2;
			formPanel.add(lblSoGhe, gbc);

			txtSoGhe = taoTextField(String.valueOf(toa.getSoGhe()));
			gbc.gridx = 3;
			gbc.weightx = 0.8;
			formPanel.add(txtSoGhe, gbc);

			JLabel lblViTri = taoLabel("Vị trí toa *:");
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.weightx = 0.2;
			formPanel.add(lblViTri, gbc);

			txtViTriToa = taoTextField(toa.getViTriToa());
			gbc.gridx = 1;
			gbc.weightx = 0.8;
			formPanel.add(txtViTriToa, gbc);

			JPanel buttonPanel = taoButtonPanel();
			buttonPanel.add(taoNutMau("Cập nhật", Color.decode("#00AA00"), e -> xuLyCapNhatToa()));
			buttonPanel.add(taoNutMau("Xóa", Color.decode("#DD3333"), e -> xuLyXoaToa()));
			buttonPanel.add(taoNutPhu("Hủy", e -> {
				selectedMaToa = null;
				table.clearSelection();
				renderFormPanel();
			}));

			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.gridwidth = 4;
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 1;
			formPanel.add(buttonPanel, gbc);
		}

		formPanel.revalidate();
		formPanel.repaint();
	}

	private void loadDataFromDatabase(String maToa) {
		model.setRowCount(0);
		List<Toa> ds = toaController.timKiemToa(maToa);
		for (int i = 0; i < ds.size(); i++) {
			Toa toa = ds.get(i);
			model.addRow(new Object[] { i + 1, toa.getMaToa(), toa.getLoaiToa(), toa.getMaTau(), toa.getSoGhe(), toa.getViTriToa() });
		}
	}

	private void xuLyCapNhatToa() {
		try {
			int soGhe = Integer.parseInt(txtSoGhe.getText().trim());
			KetQuaXuLy ketQua = toaController.capNhatToa(
					txtMaToa.getText().trim(),
					String.valueOf(cbTau.getSelectedItem()),
					String.valueOf(cbLoaiToa.getSelectedItem()),
					soGhe,
					txtViTriToa.getText().trim(),
					true);
			JOptionPane.showMessageDialog(this, ketQua.thongBao,
					ketQua.thanhCong ? "Thành công" : "Lỗi",
					ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
			if (ketQua.thanhCong) {
				selectedMaToa = null;
				table.clearSelection();
				loadDataFromDatabase(null);
				renderFormPanel();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Số ghế phải là số nguyên hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void xuLyXoaToa() {
		String maToa = txtMaToa.getText().trim();
		int xacNhan = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc muốn xóa toa " + maToa + " không?",
				"Xác nhận xóa toa",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (xacNhan != JOptionPane.YES_OPTION) {
			return;
		}

		KetQuaXuLy ketQua = toaController.xoaToa(maToa);
		JOptionPane.showMessageDialog(this, ketQua.thongBao,
				ketQua.thanhCong ? "Thành công" : "Lỗi",
				ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
		if (ketQua.thanhCong) {
			selectedMaToa = null;
			table.clearSelection();
			loadDataFromDatabase(null);
			renderFormPanel();
		}
	}

	private Toa timToaTheoMa(String maToa) {
		List<Toa> ds = toaController.timKiemToa(maToa);
		return ds.isEmpty() ? null : ds.get(0);
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
