package view;

import controller.KhuyenMaiController;
import entity.KhuyenMai;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import service.KhuyenMaiService.KetQuaXuLy;

public class KhuyenMaiCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private JTable table;
	private DefaultTableModel model;
	private JPanel formPanel;
	
	private String selectedMaKM = null;

	// UI Fields
	private JTextField txtMaKM, txtTenKM, txtTyLeKM, txtNgayBD, txtNgayKT;
	private JComboBox<String> cbSapXep;

	private final KhuyenMaiController khuyenMaiController = new KhuyenMaiController();

	public KhuyenMaiCapNhatPage() {
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

		JLabel title = new JLabel("Cập nhật khuyến mãi");
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

		// formPanel ở NORTH
		formPanel = new JPanel();
		renderFormPanel();
		content.add(formPanel, BorderLayout.NORTH);

		// Table
		String[] columns = { "#", "Mã KM", "Tên khuyến mãi", "Tỷ lệ (%)", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		taiDuLieuBang(null, null, "Tất cả");

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
					selectedMaKM = table.getValueAt(row, 1).toString();
					renderFormPanel();
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private void taiDuLieuBang(String ma, String ten, String filter) {
		model.setRowCount(0);
		List<KhuyenMai> ds = khuyenMaiController.timKiemKhuyenMai(ma, ten);
		LocalDate today = LocalDate.now();
		int stt = 1;
		for (KhuyenMai km : ds) {
			String trangThai;
			if (today.isBefore(km.getNgayBD())) {
				trangThai = "Sắp diễn ra";
			} else if (today.isAfter(km.getNgayKT())) {
				trangThai = "Kết thúc";
			} else {
				trangThai = "Đang chạy";
			}

			if (filter.equals("Tất cả") || filter.equals(trangThai)) {
				model.addRow(new Object[] {
						stt++,
						km.getMaKM(),
						km.getTenKM(),
						km.getTyLeKM(),
						km.getNgayBD().format(DATE_FORMAT),
						km.getNgayKT().format(DATE_FORMAT),
						trangThai
				});
			}
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

		if (selectedMaKM == null) {
			// --- STATE A: SEARCH STATE ---
			JLabel lblMa = new JLabel("Mã khuyến mãi:");
			lblMa.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblMa.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
			formPanel.add(lblMa, gbc);

			txtMaKM = new JTextField();
			txtMaKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtMaKM.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtMaKM.setPreferredSize(new Dimension(200, 36));
			txtMaKM.setToolTipText("Nhập mã khuyến mãi cần tìm");
			gbc.gridx = 1; gbc.weightx = 1;
			formPanel.add(txtMaKM, gbc);

			JLabel lblTen = new JLabel("Tên khuyến mãi:");
			lblTen.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblTen.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 2; gbc.weightx = 0;
			formPanel.add(lblTen, gbc);

			txtTenKM = new JTextField();
			txtTenKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtTenKM.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtTenKM.setPreferredSize(new Dimension(200, 36));
			txtTenKM.setToolTipText("Nhập tên khuyến mãi cần tìm");
			gbc.gridx = 3; gbc.weightx = 1;
			formPanel.add(txtTenKM, gbc);

			JLabel lblTrangThai = new JLabel("Trạng thái:");
			lblTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblTrangThai.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
			formPanel.add(lblTrangThai, gbc);

			cbSapXep = new JComboBox<>();
			cbSapXep.addItem("Tất cả");
			cbSapXep.addItem("Sắp diễn ra");
			cbSapXep.addItem("Đang chạy");
			cbSapXep.addItem("Kết thúc");
			cbSapXep.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			cbSapXep.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
			cbSapXep.setPreferredSize(new Dimension(200, 36));
			gbc.gridx = 1; gbc.weightx = 1;
			formPanel.add(cbSapXep, gbc);

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
				String ma = txtMaKM.getText().trim();
				String ten = txtTenKM.getText().trim();
				String filter = String.valueOf(cbSapXep.getSelectedItem());
				taiDuLieuBang(ma.isEmpty() ? null : ma, ten.isEmpty() ? null : ten, filter);
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
				txtMaKM.setText("");
				txtTenKM.setText("");
				cbSapXep.setSelectedIndex(0);
				taiDuLieuBang(null, null, "Tất cả");
			});
			buttonPanel.add(btnLamMoi);

			gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 0;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets = new Insets(15, 10, 5, 10);
			formPanel.add(buttonPanel, gbc);

		} else {
			// --- STATE B: EDIT STATE ---
			KhuyenMai km = timKhuyenMaiTheoMa(selectedMaKM);
			if (km == null) {
				selectedMaKM = null;
				renderFormPanel();
				return;
			}

			JLabel lblMa = new JLabel("Mã khuyến mãi:");
			lblMa.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblMa.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
			formPanel.add(lblMa, gbc);

			txtMaKM = new JTextField(km.getMaKM());
			txtMaKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtMaKM.setBackground(Color.decode("#F5F7FA"));
			txtMaKM.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtMaKM.setPreferredSize(new Dimension(200, 36));
			txtMaKM.setEnabled(false);
			gbc.gridx = 1; gbc.weightx = 1;
			formPanel.add(txtMaKM, gbc);

			JLabel lblTen = new JLabel("Tên khuyến mãi *:");
			lblTen.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblTen.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 2; gbc.weightx = 0;
			formPanel.add(lblTen, gbc);

			txtTenKM = new JTextField(km.getTenKM());
			txtTenKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtTenKM.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtTenKM.setPreferredSize(new Dimension(200, 36));
			gbc.gridx = 3; gbc.weightx = 1;
			formPanel.add(txtTenKM, gbc);

			JLabel lblTyLe = new JLabel("Tỷ lệ (%) *:");
			lblTyLe.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblTyLe.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
			formPanel.add(lblTyLe, gbc);

			txtTyLeKM = new JTextField(String.valueOf(km.getTyLeKM()));
			txtTyLeKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtTyLeKM.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtTyLeKM.setPreferredSize(new Dimension(200, 36));
			gbc.gridx = 1; gbc.weightx = 1;
			formPanel.add(txtTyLeKM, gbc);

			JLabel lblNgayBD = new JLabel("Ngày bắt đầu *:");
			lblNgayBD.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblNgayBD.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 2; gbc.weightx = 0;
			formPanel.add(lblNgayBD, gbc);

			JPanel pnlNgayBD = new JPanel(new BorderLayout(5, 0));
			pnlNgayBD.setBackground(Color.WHITE);
			txtNgayBD = new JTextField(km.getNgayBD().format(DATE_FORMAT));
			txtNgayBD.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtNgayBD.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtNgayBD.setEditable(false);
			pnlNgayBD.add(txtNgayBD, BorderLayout.CENTER);
			
			JButton btnLichBD = taoNutLich(date -> txtNgayBD.setText(date.format(DATE_FORMAT)));
			pnlNgayBD.add(btnLichBD, BorderLayout.EAST);
			gbc.gridx = 3; gbc.weightx = 1;
			formPanel.add(pnlNgayBD, gbc);

			JLabel lblNgayKT = new JLabel("Ngày kết thúc *:");
			lblNgayKT.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lblNgayKT.setForeground(Color.decode("#2B4B74"));
			gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
			formPanel.add(lblNgayKT, gbc);

			JPanel pnlNgayKT = new JPanel(new BorderLayout(5, 0));
			pnlNgayKT.setBackground(Color.WHITE);
			txtNgayKT = new JTextField(km.getNgayKT().format(DATE_FORMAT));
			txtNgayKT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			txtNgayKT.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(8, 8, 8, 8)));
			txtNgayKT.setEditable(false);
			pnlNgayKT.add(txtNgayKT, BorderLayout.CENTER);
			
			JButton btnLichKT = taoNutLich(date -> txtNgayKT.setText(date.format(DATE_FORMAT)));
			pnlNgayKT.add(btnLichKT, BorderLayout.EAST);
			gbc.gridx = 1; gbc.weightx = 1;
			formPanel.add(pnlNgayKT, gbc);

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
			btnCapNhat.addActionListener(e -> xuLyCapNhatKhuyenMai());
			buttonPanel.add(btnCapNhat);

			JButton btnXoa = new JButton("Xóa");
			btnXoa.setBackground(Color.decode("#FF6B6B"));
			btnXoa.setForeground(Color.WHITE);
			btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 14));
			btnXoa.setFocusPainted(false);
			btnXoa.setBorder(new EmptyBorder(8, 20, 8, 20));
			btnXoa.setPreferredSize(new Dimension(130, 40));
			btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			btnXoa.addActionListener(e -> xuLyXoaKhuyenMai());
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
				selectedMaKM = null;
				table.clearSelection();
				renderFormPanel();
			});
			buttonPanel.add(btnHuy);

			gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4; gbc.weightx = 0;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets = new Insets(20, 10, 5, 10);
			formPanel.add(buttonPanel, gbc);
		}

		formPanel.revalidate();
		formPanel.repaint();
	}

	private JButton taoNutLich(java.util.function.Consumer<LocalDate> target) {
		JButton btn = new JButton();
		try {
			ImageIcon icon = new ImageIcon(getClass().getResource("/Image/icon_lich.png"));
			Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
			btn.setIcon(new ImageIcon(img));
		} catch (Exception e) {
			btn.setText("📅");
		}
		btn.setPreferredSize(new Dimension(30, 30));
		btn.setBackground(Color.WHITE);
		btn.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setFocusPainted(false);
		
		btn.addActionListener(e -> {
			CalendarDatePicker picker = new CalendarDatePicker(LocalDate.now(), target);
			picker.showPopup(btn, 0, btn.getHeight());
		});
		return btn;
	}

	private KhuyenMai timKhuyenMaiTheoMa(String ma) {
		List<KhuyenMai> list = khuyenMaiController.timKiemKhuyenMai(ma, null);
		return list.isEmpty() ? null : list.get(0);
	}

	private void xuLyCapNhatKhuyenMai() {
		try {
			String tyLeText = txtTyLeKM.getText().replace("%", "").trim();
			BigDecimal tyLe = new BigDecimal(tyLeText);
			LocalDate ngayBD = LocalDate.parse(txtNgayBD.getText().trim(), DATE_FORMAT);
			LocalDate ngayKT = LocalDate.parse(txtNgayKT.getText().trim(), DATE_FORMAT);
			KetQuaXuLy ketQua = khuyenMaiController.capNhatKhuyenMai(txtMaKM.getText(), txtTenKM.getText(), tyLe, ngayBD, ngayKT);
			JOptionPane.showMessageDialog(this, ketQua.thongBao,
					ketQua.thanhCong ? "Thành công" : "Lỗi",
					ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
			if (ketQua.thanhCong) {
				taiDuLieuBang(null, null, "Tất cả");
				selectedMaKM = null;
				table.clearSelection();
				renderFormPanel();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Tỷ lệ khuyến mãi không hợp lệ.");
		} catch (DateTimeParseException ex) {
			JOptionPane.showMessageDialog(this, "Ngày không đúng định dạng (dd/MM/yyyy).");
		}
	}

	private void xuLyXoaKhuyenMai() {
		String maKM = txtMaKM.getText().trim();
		int xacNhan = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc muốn xóa khuyến mãi " + maKM + " không?",
				"Xác nhận xóa khuyến mãi",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (xacNhan != JOptionPane.YES_OPTION) {
			return;
		}

		KetQuaXuLy ketQua = khuyenMaiController.xoaKhuyenMai(maKM);
		JOptionPane.showMessageDialog(this, ketQua.thongBao,
				ketQua.thanhCong ? "Thành công" : "Lỗi",
				ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
		if (ketQua.thanhCong) {
			taiDuLieuBang(null, null, "Tất cả");
			selectedMaKM = null;
			table.clearSelection();
			renderFormPanel();
		}
	}
}
