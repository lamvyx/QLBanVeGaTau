package view;

import controller.ChuyenTauController;
import controller.TauController;
import controller.ToaController;
import dao.TuyenTau_DAO;
import entity.ChuyenTau;
import entity.Tau;
import entity.Toa;
import entity.TuyenTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import service.ChuyenTauService.KetQuaXuLy;

public class ChuyenTauCapNhatPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private static final DateTimeFormatter DATE_TIME_VIEW = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	private final ChuyenTauController chuyenTauController = new ChuyenTauController();
	private final TauController tauController = new TauController();
	private final ToaController toaController = new ToaController();
	private final TuyenTau_DAO tuyenTauDAO = new TuyenTau_DAO();

	private JTable table;
	private DefaultTableModel model;
	private JPanel formPanel;
	private JTextField txtKeyword;
	private String selectedMaCT;

	private JTextField txtMaChuyenTau;
	private JComboBox<TauOption> cbTau;
	private JComboBox<TuyenOption> cbTuyenTau;
	private JComboBox<String> cbTrangThai;
	private JSpinner spnKhoiHanh;
	private JTextField txtGioiHanKeo;
	private JTextField txtSoToaDaChon;
	private JTextField txtTongGheDuKien;
	private ToaChonPanel toaChonPanel;
	private JButton btnLuu;
	private JButton btnDatLai;
	private JButton btnXoa;
	private String trangThaiBanDau;
	private boolean dangCapNhatComboTrangThai;

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
				new EmptyBorder(12, 14, 12, 14)));
		JLabel title = new JLabel("Cập nhật chuyến tàu");
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

		String[] columns = { "#", "Mã chuyến", "Đầu tàu", "Tuyến", "Khởi hành", "Trạng thái", "Số toa" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.setRowHeight(42);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setBackground(MAU_CHINH);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.setGridColor(Color.decode("#E4EBF3"));
		table.setSelectionBackground(Color.decode("#DCEAFF"));
		table.getColumnModel().getColumn(5).setCellRenderer(new ChuyenTauBadgeRenderer());
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					selectedMaCT = String.valueOf(table.getValueAt(row, 1));
					renderFormPanel();
				}
			}
		});

		loadDataFromDatabase(null);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		content.add(scrollPane, BorderLayout.CENTER);
		return content;
	}

	private void renderFormPanel() {
		formPanel.removeAll();
		formPanel.setLayout(new BorderLayout());
		formPanel.setBackground(Color.WHITE);

		if (selectedMaCT == null) {
			formPanel.add(taoSearchPanel(), BorderLayout.CENTER);
		} else {
			ChuyenTau chuyenTau = timChuyenTauTheoMa(selectedMaCT);
			if (chuyenTau == null) {
				selectedMaCT = null;
				renderFormPanel();
				return;
			}
			formPanel.add(taoEditPanel(chuyenTau), BorderLayout.CENTER);
		}

		formPanel.revalidate();
		formPanel.repaint();
	}

	private JPanel taoSearchPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(15, 15, 15, 15)));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 10, 8, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(taoLabel("Mã chuyến / Đầu tàu / Tuyến"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 1;
		txtKeyword = new JTextField();
		txtKeyword.setPreferredSize(new Dimension(0, 36));
		txtKeyword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panel.add(txtKeyword, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 0;
		JPanel buttons = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));
		buttons.setOpaque(false);
		buttons.add(taoNutChinh("Tìm kiếm", e -> loadDataFromDatabase(txtKeyword.getText().trim())));
		buttons.add(taoNutPhu("Làm mới", e -> {
			txtKeyword.setText("");
			loadDataFromDatabase(null);
		}));
		panel.add(buttons, gbc);
		return panel;
	}

	private JScrollPane taoEditPanel(ChuyenTau chuyenTau) {
		JPanel wrapper = new JPanel(new BorderLayout(0, 14));
		wrapper.setBackground(Color.WHITE);
		wrapper.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(16, 16, 16, 16)));

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		form.add(taoLabel("Mã chuyến"), gbc);
		gbc.gridx = 1;
		txtMaChuyenTau = taoFieldKhoa(chuyenTau.getMaCT());
		form.add(txtMaChuyenTau, gbc);

		gbc.gridx = 2;
		form.add(taoLabel("Đầu tàu *"), gbc);
		gbc.gridx = 3;
		cbTau = new JComboBox<>();
		cbTau.setPreferredSize(new Dimension(260, 36));
		cbTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		for (Tau tau : tauController.timKiemTau(null, null)) {
			cbTau.addItem(new TauOption(tau.getMaTau(), tau.getTenTau(), tau.getSoLuongToa()));
		}
		chonTau(chuyenTau.getMaTau());
		cbTau.addActionListener(e -> capNhatDanhSachToa(chuyenTau.getMaCT()));
		form.add(cbTau, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		form.add(taoLabel("Tuyến tàu *"), gbc);
		gbc.gridx = 1;
		cbTuyenTau = new JComboBox<>();
		cbTuyenTau.setPreferredSize(new Dimension(260, 36));
		cbTuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		for (TuyenTau tt : tuyenTauDAO.layTatCaTuyenTau()) {
			cbTuyenTau.addItem(new TuyenOption(tt.getMaTT(), tt.getMaGaDi(), tt.getMaGaDen(), tt.getKhoangCach()));
		}
		chonTuyen(chuyenTau.getMaTuyenTau());
		form.add(cbTuyenTau, gbc);

		gbc.gridx = 2;
		form.add(taoLabel("Trạng thái *"), gbc);
		gbc.gridx = 3;
		cbTrangThai = new JComboBox<>(new String[] {
				ChuyenTau.HIEN_THI_DA_LEN_LICH,
				ChuyenTau.HIEN_THI_DANG_CHAY,
				ChuyenTau.HIEN_THI_DA_HOAN_THANH });
		cbTrangThai.setSelectedItem(chuyenTau.getTrangThaiHienThi());
		cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbTrangThai.addActionListener(e -> xuLyDoiTrangThaiTamThoi());
		form.add(cbTrangThai, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		form.add(taoLabel("Khởi hành *"), gbc);
		gbc.gridx = 1;
		spnKhoiHanh = taoDateTimeSpinner();
		spnKhoiHanh.setValue(Date.from(chuyenTau.getNgayKhoiHanh().atZone(ZoneId.systemDefault()).toInstant()));
		spnKhoiHanh.addChangeListener(e -> capNhatDanhSachToa(chuyenTau.getMaCT()));
		form.add(spnKhoiHanh, gbc);

		gbc.gridx = 2;
		form.add(taoLabel("Giới hạn kéo"), gbc);
		gbc.gridx = 3;
		txtGioiHanKeo = taoFieldKhoa("0 toa");
		form.add(txtGioiHanKeo, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		form.add(taoLabel("Số toa đã chọn"), gbc);
		gbc.gridx = 1;
		txtSoToaDaChon = taoFieldKhoa("0 toa");
		form.add(txtSoToaDaChon, gbc);

		gbc.gridx = 2;
		form.add(taoLabel("Tổng ghế dự kiến"), gbc);
		gbc.gridx = 3;
		txtTongGheDuKien = taoFieldKhoa("0 ghế");
		form.add(txtTongGheDuKien, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 4;
		JPanel buttons = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));
		buttons.setOpaque(false);
		btnLuu = taoNutMau("Lưu thay đổi", Color.decode("#159947"), e -> xuLyCapNhatChuyenTau());
		btnDatLai = taoNutPhu("Đặt lại", e -> {
			selectedMaCT = null;
			table.clearSelection();
			renderFormPanel();
		});
		btnXoa = taoNutMau("Xóa", Color.decode("#D64545"), e -> xuLyXoaChuyenTau());
		buttons.add(btnLuu);
		buttons.add(btnDatLai);
		buttons.add(btnXoa);
		form.add(buttons, gbc);

		wrapper.add(form, BorderLayout.NORTH);

		toaChonPanel = new ToaChonPanel();
		toaChonPanel.setTieuDe("Bố trí toa cho chuyến");
		toaChonPanel.setSelectionListener(ignored -> capNhatThongTinTongHop());
		wrapper.add(toaChonPanel, BorderLayout.CENTER);

		capNhatDanhSachToa(chuyenTau.getMaCT());
		apDungRangBuocTrangThai(chuyenTau);

		JScrollPane scrollPane = new JScrollPane(wrapper);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		return scrollPane;
	}

	private void loadDataFromDatabase(String tuKhoa) {
		model.setRowCount(0);
		List<ChuyenTau> ds = chuyenTauController.timKiemChuyenTau(tuKhoa);
		for (int i = 0; i < ds.size(); i++) {
			ChuyenTau ct = ds.get(i);
			model.addRow(new Object[] {
					i + 1,
					ct.getMaCT(),
					ct.getMaTau(),
					ct.getMaTuyenTau(),
					ct.getNgayKhoiHanh() == null ? "" : ct.getNgayKhoiHanh().format(DATE_TIME_VIEW),
					ct.getTrangThaiHienThi(),
					chuyenTauController.layDanhSachMaToaTheoChuyen(ct.getMaCT()).size()
			});
		}
	}

	private void capNhatDanhSachToa(String maCTHienTai) {
		TauOption tau = (TauOption) cbTau.getSelectedItem();
		int gioiHan = tau == null ? 0 : tau.soLuongToa;
		txtGioiHanKeo.setText(gioiHan + " toa");
		toaChonPanel.setGioiHanChon(gioiHan);

		Set<String> availableIds = new LinkedHashSet<>();
		for (Toa toa : toaController.layToaRanhTheoThoiDiem(layThoiGianKhoiHanh(), maCTHienTai)) {
			availableIds.add(toa.getMaToa());
		}

		Set<String> assigned = new LinkedHashSet<>(chuyenTauController.layDanhSachMaToaTheoChuyen(maCTHienTai));
		List<ToaChonPanel.CoachCardData> cardData = new ArrayList<>();
		for (Toa toa : toaController.timKiemToa(null)) {
			boolean available = availableIds.contains(toa.getMaToa()) || assigned.contains(toa.getMaToa());
			String moTa = available ? "Sẵn sàng" : "Đã xếp chuyến";
			cardData.add(new ToaChonPanel.CoachCardData(toa.getMaToa(), toa.getLoaiToa(), toa.getSoGhe(), moTa, available));
		}
		toaChonPanel.setDanhSachToa(cardData);
		toaChonPanel.setSelectedToa(assigned);
		capNhatThongTinTongHop();
	}

	private void apDungRangBuocTrangThai(ChuyenTau chuyenTau) {
		trangThaiBanDau = chuyenTau.getTrangThai();
		boolean laDaLenLich = chuyenTau.laDaLenLich();
		boolean laDangChay = chuyenTau.laDangChay();
		boolean laDaHoanThanh = chuyenTau.laDaHoanThanh();

		cbTau.setEnabled(laDaLenLich);
		cbTuyenTau.setEnabled(laDaLenLich);
		spnKhoiHanh.setEnabled(laDaLenLich);
		toaChonPanel.setSelectionEnabled(laDaLenLich);

		dangCapNhatComboTrangThai = true;
		cbTrangThai.removeAllItems();
		if (laDaLenLich) {
			cbTrangThai.addItem(ChuyenTau.HIEN_THI_DA_LEN_LICH);
			cbTrangThai.addItem(ChuyenTau.HIEN_THI_DANG_CHAY);
			cbTrangThai.addItem(ChuyenTau.HIEN_THI_DA_HOAN_THANH);
		} else if (laDangChay) {
			cbTrangThai.addItem(ChuyenTau.HIEN_THI_DANG_CHAY);
			cbTrangThai.addItem(ChuyenTau.HIEN_THI_DA_HOAN_THANH);
		} else if (laDaHoanThanh) {
			cbTrangThai.addItem(ChuyenTau.HIEN_THI_DA_HOAN_THANH);
		}
		cbTrangThai.setSelectedItem(chuyenTau.getTrangThaiHienThi());
		dangCapNhatComboTrangThai = false;

		if (btnLuu != null) {
			btnLuu.setEnabled(!laDaHoanThanh);
		}
		if (btnXoa != null) {
			btnXoa.setEnabled(!laDangChay);
		}
	}

	private void xuLyDoiTrangThaiTamThoi() {
		if (dangCapNhatComboTrangThai || cbTrangThai == null) {
			return;
		}
		String trangThaiDangChon = ChuyenTau.tuTrangThaiHienThi(String.valueOf(cbTrangThai.getSelectedItem()));
		if (ChuyenTau.TRANG_THAI_DA_LEN_LICH.equals(trangThaiBanDau)
				&& ChuyenTau.TRANG_THAI_DANG_CHAY.equals(trangThaiDangChon)) {
			dangCapNhatComboTrangThai = true;
			cbTrangThai.removeAllItems();
			cbTrangThai.addItem(ChuyenTau.HIEN_THI_DANG_CHAY);
			cbTrangThai.addItem(ChuyenTau.HIEN_THI_DA_HOAN_THANH);
			cbTrangThai.setSelectedItem(ChuyenTau.HIEN_THI_DANG_CHAY);
			dangCapNhatComboTrangThai = false;
		}

		boolean choPhepSuaThongTin = ChuyenTau.TRANG_THAI_DA_LEN_LICH.equals(trangThaiDangChon)
				&& ChuyenTau.TRANG_THAI_DA_LEN_LICH.equals(trangThaiBanDau);
		cbTau.setEnabled(choPhepSuaThongTin);
		cbTuyenTau.setEnabled(choPhepSuaThongTin);
		spnKhoiHanh.setEnabled(choPhepSuaThongTin);
		toaChonPanel.setSelectionEnabled(choPhepSuaThongTin);
	}

	private void capNhatThongTinTongHop() {
		Set<String> daChon = toaChonPanel.getSelectedToa();
		txtSoToaDaChon.setText(daChon.size() + " toa");
		int tongGhe = 0;
		for (String maToa : daChon) {
			Toa toa = toaController.timKiemToa(maToa).stream().findFirst().orElse(null);
			if (toa != null) {
				tongGhe += toa.getSoGhe();
			}
		}
		txtTongGheDuKien.setText(tongGhe + " ghế");
	}

	private void xuLyCapNhatChuyenTau() {
		TauOption tau = (TauOption) cbTau.getSelectedItem();
		TuyenOption tuyen = (TuyenOption) cbTuyenTau.getSelectedItem();
		if (tau == null || tau.maTau == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn đầu tàu.");
			return;
		}
		if (tuyen == null || tuyen.maTT == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn tuyến tàu.");
			return;
		}
		List<String> maToaList = new ArrayList<>(toaChonPanel.getSelectedToa());
		if (maToaList.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Chuyến tàu phải có ít nhất một toa.");
			return;
		}

		String trangThai = ChuyenTau.tuTrangThaiHienThi(String.valueOf(cbTrangThai.getSelectedItem()));
		KetQuaXuLy ketQua = chuyenTauController.capNhatChuyenTau(
				txtMaChuyenTau.getText().trim(),
				tau.maTau,
				tuyen.maTT,
				layThoiGianKhoiHanh(),
				trangThai,
				maToaList);
		JOptionPane.showMessageDialog(this, ketQua.thongBao,
				ketQua.thanhCong ? "Thành công" : "Lỗi",
				ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
		if (ketQua.thanhCong) {
			selectedMaCT = null;
			table.clearSelection();
			loadDataFromDatabase(null);
			renderFormPanel();
		}
	}

	private void xuLyXoaChuyenTau() {
		String maCT = txtMaChuyenTau.getText().trim();
		int xacNhan = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc muốn xóa chuyến tàu " + maCT + " không?",
				"Xác nhận xóa chuyến tàu",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (xacNhan != JOptionPane.YES_OPTION) {
			return;
		}
		KetQuaXuLy ketQua = chuyenTauController.xoaChuyenTau(maCT);
		JOptionPane.showMessageDialog(this, ketQua.thongBao,
				ketQua.thanhCong ? "Thành công" : "Lỗi",
				ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
		if (ketQua.thanhCong) {
			selectedMaCT = null;
			table.clearSelection();
			loadDataFromDatabase(null);
			renderFormPanel();
		}
	}

	private ChuyenTau timChuyenTauTheoMa(String maCT) {
		List<ChuyenTau> ds = chuyenTauController.timKiemChuyenTau(maCT);
		return ds.isEmpty() ? null : ds.get(0);
	}

	private void chonTau(String maTau) {
		for (int i = 0; i < cbTau.getItemCount(); i++) {
			TauOption option = cbTau.getItemAt(i);
			if (option != null && maTau.equals(option.maTau)) {
				cbTau.setSelectedIndex(i);
				return;
			}
		}
	}

	private void chonTuyen(String maTT) {
		for (int i = 0; i < cbTuyenTau.getItemCount(); i++) {
			TuyenOption option = cbTuyenTau.getItemAt(i);
			if (option != null && maTT.equals(option.maTT)) {
				cbTuyenTau.setSelectedIndex(i);
				return;
			}
		}
	}

	private JLabel taoLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.BOLD, 14));
		label.setForeground(Color.decode("#2B4B74"));
		return label;
	}

	private JTextField taoFieldKhoa(String value) {
		JTextField field = new JTextField(value);
		field.setEditable(false);
		field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		field.setBackground(Color.decode("#F6F8FB"));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(8, 8, 8, 8)));
		return field;
	}

	private JSpinner taoDateTimeSpinner() {
		JSpinner spinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy HH:mm");
		spinner.setEditor(editor);
		spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		spinner.setPreferredSize(new Dimension(260, 36));
		return spinner;
	}

	private JButton taoNutChinh(String text, java.awt.event.ActionListener action) {
		JButton button = taoNutMau(text, MAU_CHINH, action);
		button.setForeground(Color.WHITE);
		return button;
	}

	private JButton taoNutPhu(String text, java.awt.event.ActionListener action) {
		JButton button = new JButton(text);
		button.setBackground(Color.WHITE);
		button.setForeground(Color.decode("#2B4B74"));
		button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(9, 22, 9, 22)));
		button.addActionListener(action);
		return button;
	}

	private JButton taoNutMau(String text, Color color, java.awt.event.ActionListener action) {
		JButton button = new JButton(text);
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setBorder(new EmptyBorder(9, 22, 9, 22));
		button.addActionListener(action);
		return button;
	}

	private LocalDateTime layThoiGianKhoiHanh() {
		Date date = (Date) spnKhoiHanh.getValue();
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	private static class ChuyenTauBadgeRenderer extends javax.swing.table.DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			JLabel label = new JLabel(String.valueOf(value), JLabel.CENTER);
			label.setOpaque(true);
			label.setFont(new Font("Segoe UI", Font.BOLD, 13));
			label.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

			String val = String.valueOf(value);
			if (entity.ChuyenTau.HIEN_THI_DA_LEN_LICH.equals(val)) {
				label.setBackground(Color.decode("#FEF3C7")); // Light yellow
				label.setForeground(Color.decode("#B45309")); // Dark gold/amber
			} else if (entity.ChuyenTau.HIEN_THI_DANG_CHAY.equals(val)) {
				label.setBackground(Color.decode("#DBEAFE")); // Light blue
				label.setForeground(Color.decode("#1D4ED8")); // Dark blue
			} else if (entity.ChuyenTau.HIEN_THI_DA_HOAN_THANH.equals(val)) {
				label.setBackground(Color.decode("#D1FAE5")); // Light green
				label.setForeground(Color.decode("#047857")); // Dark green
			} else {
				label.setBackground(Color.WHITE);
				label.setForeground(Color.decode("#1E2D3D"));
			}

			if (isSelected) {
				label.setBackground(label.getBackground().darker());
			}
			return label;
		}
	}
}
