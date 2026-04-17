package view;

import controller.KhuyenMaiController;
import entity.KhuyenMai;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import service.KhuyenMaiService.KetQuaXuLy;

public class KhuyenMaiCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");

	private JTable table;
	private JPanel formPanel;
	private DefaultTableModel model;
	
	private JTextField txtMaKM, txtTenKM, txtTyLeKM, txtNgayBD, txtNgayKT;
	private JButton btnCapNhat, btnXoa, btnHuy;
	private final KhuyenMaiController khuyenMaiController = new KhuyenMaiController();
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
			new EmptyBorder(12, 14, 12, 14)
		));

		JLabel title = new JLabel("Cập nhật khuyến mãi");
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

		String[] columns = { "#", "Mã KM", "Tên KM", "Tỷ lệ (%)", "Ngày BĐ", "Ngày KT", "Trạng thái" };
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
		splitPane.setDividerLocation(280);
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

		JLabel lblHuongDan = new JLabel("Chọn khuyến mãi từ bảng trên để chỉnh sửa");
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

		// Mã KM (Read-only)
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Mã khuyến mãi");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		txtMaKM = new JTextField(table.getValueAt(row, 1).toString());
		txtMaKM.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtMaKM.setPreferredSize(new Dimension(200, 30));
		txtMaKM.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		txtMaKM.setEnabled(false);
		formContainer.add(txtMaKM, gbc);

		// Tên KM
		gbc.gridx = 0;
		gbc.gridy = 1;
		lbl = new JLabel("Tên khuyến mãi *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtTenKM = new JTextField(table.getValueAt(row, 2).toString());
		txtTenKM.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtTenKM.setPreferredSize(new Dimension(200, 30));
		txtTenKM.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		formContainer.add(txtTenKM, gbc);

		// Tỷ lệ KM
		gbc.gridx = 0;
		gbc.gridy = 2;
		lbl = new JLabel("Tỷ lệ khuyến mãi (%) *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtTyLeKM = new JTextField(table.getValueAt(row, 3).toString());
		txtTyLeKM.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtTyLeKM.setPreferredSize(new Dimension(200, 30));
		txtTyLeKM.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		formContainer.add(txtTyLeKM, gbc);

		// Ngày bắt đầu
		gbc.gridx = 0;
		gbc.gridy = 3;
		lbl = new JLabel("Ngày bắt đầu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtNgayBD = new JTextField(table.getValueAt(row, 4).toString());
		txtNgayBD.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtNgayBD.setPreferredSize(new Dimension(200, 30));
		txtNgayBD.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		formContainer.add(txtNgayBD, gbc);

		// Ngày kết thúc
		gbc.gridx = 0;
		gbc.gridy = 4;
		lbl = new JLabel("Ngày kết thúc *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtNgayKT = new JTextField(table.getValueAt(row, 5).toString());
		txtNgayKT.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtNgayKT.setPreferredSize(new Dimension(200, 30));
		txtNgayKT.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		formContainer.add(txtNgayKT, gbc);

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
		btnCapNhat.addActionListener(e -> xuLyCapNhatKhuyenMai());
		buttonPanel.add(btnCapNhat);

		btnXoa = new JButton("Xóa");
		btnXoa.setBackground(Color.decode("#FF6B6B"));
		btnXoa.setForeground(Color.WHITE);
		btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnXoa.setFocusPainted(false);
		btnXoa.setBorder(new EmptyBorder(6, 16, 6, 16));
		btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnXoa.addActionListener(e -> xuLyXoaKhuyenMai());
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
		List<KhuyenMai> ds = khuyenMaiController.timKiemKhuyenMai(null, null);
		int stt = 1;
		LocalDate homNay = LocalDate.now();
		for (KhuyenMai km : ds) {
			String trangThai;
			if (homNay.isBefore(km.getNgayBD())) {
				trangThai = "Sắp diễn ra";
			} else if (homNay.isAfter(km.getNgayKT())) {
				trangThai = "Kết thúc";
			} else {
				trangThai = "Đang chạy";
			}
			model.addRow(new Object[] { stt++, km.getMaKM(), km.getTenKM(), km.getTyLeKM(),
					km.getNgayBD().format(DATE_FORMAT), km.getNgayKT().format(DATE_FORMAT), trangThai });
		}
	}

	private void xuLyCapNhatKhuyenMai() {
		if (txtMaKM == null) {
			return;
		}
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
				taiDuLieuBang();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Tỷ lệ khuyến mãi không hợp lệ.");
		} catch (DateTimeParseException ex) {
			JOptionPane.showMessageDialog(this, "Ngày không hợp lệ (định dạng dd/MM/yyyy).");
		}
	}

	private void xuLyXoaKhuyenMai() {
		if (txtMaKM == null || txtMaKM.getText().trim().isEmpty()) {
			return;
		}
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
			taiDuLieuBang();
			formPanel.removeAll();
			formPanel.revalidate();
			formPanel.repaint();
		}
	}
}
