package view;

import controller.TauController;
import entity.Tau;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import service.TauService.KetQuaXuLy;

public class TauCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private final TauController tauController = new TauController();

	private JTable table;
	private DefaultTableModel model;
	private JPanel formPanel;
	private String selectedMaTau;

	private JTextField txtMaTau;
	private JTextField txtTenTau;
	private JSpinner spnSoToa;

	public TauCapNhatPage() {
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

		JLabel title = new JLabel("Cập nhật tàu");
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

		String[] columns = { "#", "Mã tàu", "Tên tàu", "Số toa kéo tối đa" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		loadDataFromDatabase(null, null);

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
					selectedMaTau = String.valueOf(table.getValueAt(row, 1));
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

		if (selectedMaTau == null) {
			JLabel lblMa = taoLabel("Mã tàu:");
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.2;
			formPanel.add(lblMa, gbc);

			txtMaTau = taoTextField("");
			gbc.gridx = 1;
			gbc.weightx = 0.8;
			formPanel.add(txtMaTau, gbc);

			JLabel lblTen = taoLabel("Tên tàu:");
			gbc.gridx = 2;
			gbc.weightx = 0.2;
			formPanel.add(lblTen, gbc);

			txtTenTau = taoTextField("");
			gbc.gridx = 3;
			gbc.weightx = 0.8;
			formPanel.add(txtTenTau, gbc);

			JPanel buttonPanel = taoButtonPanel();
			buttonPanel.add(taoNutChinh("Tìm kiếm", e -> xuLyTimKiem()));
			buttonPanel.add(taoNutPhu("Làm mới", e -> {
				txtMaTau.setText("");
				txtTenTau.setText("");
				loadDataFromDatabase(null, null);
			}));

			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 4;
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 1;
			formPanel.add(buttonPanel, gbc);
		} else {
			Tau tau = timTauTheoMa(selectedMaTau);
			if (tau == null) {
				selectedMaTau = null;
				renderFormPanel();
				return;
			}

			JLabel lblMa = taoLabel("Mã tàu:");
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.2;
			formPanel.add(lblMa, gbc);

			txtMaTau = taoTextField(tau.getMaTau());
			txtMaTau.setEditable(false);
			gbc.gridx = 1;
			gbc.weightx = 0.8;
			formPanel.add(txtMaTau, gbc);

			JLabel lblTen = taoLabel("Tên tàu *:");
			gbc.gridx = 2;
			gbc.weightx = 0.2;
			formPanel.add(lblTen, gbc);

			txtTenTau = taoTextField(tau.getTenTau());
			gbc.gridx = 3;
			gbc.weightx = 0.8;
			formPanel.add(txtTenTau, gbc);

			JLabel lblSoToa = taoLabel("Số toa kéo tối đa *:");
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0.2;
			formPanel.add(lblSoToa, gbc);

			spnSoToa = new JSpinner(new SpinnerNumberModel(Math.max(1, tau.getSoLuongToa()), 1, 100, 1));
			spnSoToa.setPreferredSize(new Dimension(0, 35));
			((JSpinner.DefaultEditor) spnSoToa.getEditor()).getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 13));
			gbc.gridx = 1;
			gbc.weightx = 0.8;
			formPanel.add(spnSoToa, gbc);

			JPanel buttonPanel = taoButtonPanel();
			buttonPanel.add(taoNutMau("Cập nhật", Color.decode("#00AA00"), e -> xuLyCapNhatTau()));
			buttonPanel.add(taoNutMau("Xóa", Color.decode("#DD3333"), e -> xuLyXoaTau()));
			buttonPanel.add(taoNutPhu("Hủy", e -> {
				selectedMaTau = null;
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

	private void loadDataFromDatabase(String maTau, String tenTau) {
		model.setRowCount(0);
		List<Tau> ds = tauController.timKiemTau(maTau, tenTau);
		for (int i = 0; i < ds.size(); i++) {
			Tau tau = ds.get(i);
			model.addRow(new Object[] { i + 1, tau.getMaTau(), tau.getTenTau(), tau.getSoLuongToa() });
		}
	}

	private void xuLyTimKiem() {
		loadDataFromDatabase(txtMaTau.getText().trim(), txtTenTau.getText().trim());
	}

	private void xuLyCapNhatTau() {
		KetQuaXuLy ketQua = tauController.capNhatTau(txtMaTau.getText().trim(), txtTenTau.getText().trim(),
				(Integer) spnSoToa.getValue());
		JOptionPane.showMessageDialog(this, ketQua.thongBao,
				ketQua.thanhCong ? "Thành công" : "Lỗi",
				ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
		if (ketQua.thanhCong) {
			selectedMaTau = null;
			table.clearSelection();
			loadDataFromDatabase(null, null);
			renderFormPanel();
		}
	}

	private void xuLyXoaTau() {
		String maTau = txtMaTau.getText().trim();
		int xacNhan = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc muốn xóa tàu " + maTau + " không?",
				"Xác nhận xóa tàu",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (xacNhan != JOptionPane.YES_OPTION) {
			return;
		}

		KetQuaXuLy ketQua = tauController.xoaTau(maTau);
		JOptionPane.showMessageDialog(this, ketQua.thongBao,
				ketQua.thanhCong ? "Thành công" : "Lỗi",
				ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
		if (ketQua.thanhCong) {
			selectedMaTau = null;
			table.clearSelection();
			loadDataFromDatabase(null, null);
			renderFormPanel();
		}
	}

	private Tau timTauTheoMa(String maTau) {
		List<Tau> ds = tauController.timKiemTau(maTau, null);
		return ds.isEmpty() ? null : ds.get(0);
	}

	private JLabel taoLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.BOLD, 13));
		label.setForeground(Color.decode("#2B4B74"));
		return label;
	}

	private JTextField taoTextField(String text) {
		JTextField field = new JTextField(text);
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
