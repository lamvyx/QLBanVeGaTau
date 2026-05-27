package view;

import controller.DichVuController;
import entity.DichVu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import service.DichVuService.KetQuaXuLy;

public class DichVuCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");

	private JTable table;
	private DefaultTableModel model;
	private JPanel formPanel;
	
	private String selectedMaDV = null;

	// UI Fields
	private JTextField txtMa;
	private JTextField txtTen;
	private JTextField txtGia;

	private final DichVuController dichVuController = new DichVuController();

	public DichVuCapNhatPage() {
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

		JLabel title = new JLabel("Cập nhật dịch vụ");
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

		// formPanel ở phía NORTH
		formPanel = new JPanel();
		renderFormPanel();
		content.add(formPanel, BorderLayout.NORTH);

		// Table
		String[] columns = { "#", "Mã dịch vụ", "Tên dịch vụ", "Giá (VND)", "Trạng thái" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		taiDuLieuBang(null, null);

		table = new JTable(model);
		table.setRowHeight(50);
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
					selectedMaDV = table.getValueAt(row, 1).toString();
					renderFormPanel();
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private void taiDuLieuBang(String ma, String ten) {
		model.setRowCount(0);
		List<DichVu> ds = dichVuController.timKiemDichVu(ma, ten);
		int stt = 1;
		for (DichVu dv : ds) {
			BigDecimal gia = dv.getGiaDV();
			String giaStr = (gia != null) ? String.format("%,.0f VND", gia) : "0 VND";
			model.addRow(new Object[] {
					stt++,
					dv.getMaDV(),
					dv.getTenDV(),
					giaStr,
					dv.isTrangThai() ? "Hoạt động" : "Không hoạt động"
			});
		}
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

		if (selectedMaDV == null) {
			// --- STATE A: SEARCH STATE ---
			JLabel lblMa = new JLabel("Mã dịch vụ:");
			lblMa.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblMa.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
			formPanel.add(lblMa, gbc);

			txtMa = new JTextField();
			txtMa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtMa.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtMa.setPreferredSize(new Dimension(220, 36));
			txtMa.setToolTipText("Nhập mã dịch vụ cần tìm");
			gbc.gridx = 1; gbc.weightx = 1;
			formPanel.add(txtMa, gbc);

			JLabel lblTen = new JLabel("Tên dịch vụ:");
			lblTen.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblTen.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 2; gbc.weightx = 0;
			formPanel.add(lblTen, gbc);

			txtTen = new JTextField();
			txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtTen.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtTen.setPreferredSize(new Dimension(220, 36));
			txtTen.setToolTipText("Nhập tên dịch vụ cần tìm");
			gbc.gridx = 3; gbc.weightx = 1;
			formPanel.add(txtTen, gbc);

			// Buttons
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
			buttonPanel.setOpaque(false);

			JButton btnTimKiem = new JButton("Tìm kiếm");
			btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 14));
			btnTimKiem.setBackground(MAU_CHINH);
			btnTimKiem.setForeground(Color.WHITE);
			btnTimKiem.setFocusPainted(false);
			btnTimKiem.setBorder(new EmptyBorder(8, 20, 8, 20));
			btnTimKiem.setPreferredSize(new Dimension(140, 40));
			btnTimKiem.addActionListener(e -> {
				String ma = txtMa.getText().trim();
				String ten = txtTen.getText().trim();
				taiDuLieuBang(ma.isEmpty() ? null : ma, ten.isEmpty() ? null : ten);
			});
			buttonPanel.add(btnTimKiem);

			JButton btnLamMoi = new JButton("Làm mới");
			btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			btnLamMoi.setBackground(Color.WHITE);
			btnLamMoi.setForeground(Color.decode("#3A4D66"));
			btnLamMoi.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 20, 8, 20)));
			btnLamMoi.setPreferredSize(new Dimension(140, 40));
			btnLamMoi.addActionListener(e -> {
				txtMa.setText("");
				txtTen.setText("");
				taiDuLieuBang(null, null);
			});
			buttonPanel.add(btnLamMoi);

			gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4; gbc.weightx = 0;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets = new Insets(15, 10, 5, 10);
			formPanel.add(buttonPanel, gbc);

		} else {
			// --- STATE B: EDIT STATE ---
			DichVu dv = timDichVuTheoMa(selectedMaDV);
			if (dv == null) {
				selectedMaDV = null;
				renderFormPanel();
				return;
			}

			JLabel lblMa = new JLabel("Mã dịch vụ:");
			lblMa.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblMa.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
			formPanel.add(lblMa, gbc);

			txtMa = new JTextField(dv.getMaDV());
			txtMa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtMa.setBackground(Color.decode("#F5F7FA"));
			txtMa.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtMa.setPreferredSize(new Dimension(200, 36));
			txtMa.setEnabled(false);
			gbc.gridx = 1; gbc.weightx = 1;
			formPanel.add(txtMa, gbc);

			JLabel lblTen = new JLabel("Tên dịch vụ *:");
			lblTen.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblTen.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 2; gbc.weightx = 0;
			formPanel.add(lblTen, gbc);

			txtTen = new JTextField(dv.getTenDV());
			txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtTen.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtTen.setPreferredSize(new Dimension(200, 36));
			gbc.gridx = 3; gbc.weightx = 1;
			formPanel.add(txtTen, gbc);

			JLabel lblGia = new JLabel("Giá dịch vụ (VND) *:");
			lblGia.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblGia.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
			formPanel.add(lblGia, gbc);

			txtGia = new JTextField(String.valueOf(dv.getGiaDV().setScale(0)));
			txtGia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtGia.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtGia.setPreferredSize(new Dimension(200, 36));
			gbc.gridx = 1; gbc.weightx = 1;
			formPanel.add(txtGia, gbc);

			// Buttons
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
			buttonPanel.setOpaque(false);

			JButton btnCapNhat = new JButton("Cập nhật");
			btnCapNhat.setBackground(MAU_CHINH);
			btnCapNhat.setForeground(Color.WHITE);
			btnCapNhat.setFont(new Font("Segoe UI", Font.BOLD, 14));
			btnCapNhat.setFocusPainted(false);
			btnCapNhat.setBorder(new EmptyBorder(8, 20, 8, 20));
			btnCapNhat.setPreferredSize(new Dimension(130, 40));
			btnCapNhat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			btnCapNhat.addActionListener(e -> xuLyCapNhatDichVu());
			buttonPanel.add(btnCapNhat);

			JButton btnXoa = new JButton("Xóa");
			btnXoa.setBackground(Color.decode("#FF6B6B"));
			btnXoa.setForeground(Color.WHITE);
			btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 14));
			btnXoa.setFocusPainted(false);
			btnXoa.setBorder(new EmptyBorder(8, 20, 8, 20));
			btnXoa.setPreferredSize(new Dimension(130, 40));
			btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			btnXoa.addActionListener(e -> xuLyXoaDichVu());
			buttonPanel.add(btnXoa);

			JButton btnHuy = new JButton("Hủy");
			btnHuy.setBackground(Color.WHITE);
			btnHuy.setForeground(Color.decode("#2B4B74"));
			btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			btnHuy.setFocusPainted(false);
			btnHuy.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 20, 8, 20)));
			btnHuy.setPreferredSize(new Dimension(130, 40));
			btnHuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			btnHuy.addActionListener(e -> {
				selectedMaDV = null;
				table.clearSelection();
				renderFormPanel();
			});
			buttonPanel.add(btnHuy);

			gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.weightx = 0;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets = new Insets(20, 10, 5, 10);
			formPanel.add(buttonPanel, gbc);
		}

		formPanel.revalidate();
		formPanel.repaint();
	}

	private DichVu timDichVuTheoMa(String ma) {
		List<DichVu> list = dichVuController.timKiemDichVu(ma, null);
		return list.isEmpty() ? null : list.get(0);
	}

	private void xuLyCapNhatDichVu() {
		try {
			BigDecimal gia = new BigDecimal(txtGia.getText().trim());
			KetQuaXuLy ketQua = dichVuController.capNhatDichVu(txtMa.getText(), txtTen.getText(), gia, true);
			JOptionPane.showMessageDialog(this, ketQua.thongBao,
					ketQua.thanhCong ? "Thành công" : "Lỗi",
					ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
			if (ketQua.thanhCong) {
				taiDuLieuBang(null, null);
				selectedMaDV = null;
				table.clearSelection();
				renderFormPanel();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Giá dịch vụ không hợp lệ.");
		}
	}

	private void xuLyXoaDichVu() {
		String maDV = txtMa.getText().trim();
		int xacNhan = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc muốn xóa dịch vụ " + maDV + " không?",
				"Xác nhận xóa dịch vụ",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (xacNhan != JOptionPane.YES_OPTION) {
			return;
		}

		KetQuaXuLy ketQua = dichVuController.xoaDichVu(maDV);
		JOptionPane.showMessageDialog(this, ketQua.thongBao,
				ketQua.thanhCong ? "Thành công" : "Lỗi",
				ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
		if (ketQua.thanhCong) {
			taiDuLieuBang(null, null);
			selectedMaDV = null;
			table.clearSelection();
			renderFormPanel();
		}
	}
}
