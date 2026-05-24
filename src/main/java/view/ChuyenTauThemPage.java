package view;

import controller.ChuyenTauController;
import controller.ToaController;
import dao.TuyenTau_DAO;
import entity.Tau;
import entity.Toa;
import entity.TuyenTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import service.ChuyenTauService.KetQuaXuLy;

public class ChuyenTauThemPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");
	private static final ImageIcon ICON_LICH = taiAnhIcon("/Image/icon_lich.png", 16, 16);

	private JTextField txtMaChuyenTau, txtGiaCoban, txtTongSoCho;
	private JSpinner spnGioKhoiHanh, spnGioDenNoi;
	private JComboBox<TauOption> cbTau;
	private JComboBox<TuyenOption> cbTuyenTau;
	private JComboBox<String> cbTrangThai;
	private final ChuyenTauController chuyenTauController = new ChuyenTauController();
	private final ToaController toaController = new ToaController();

	public ChuyenTauThemPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoForm(), BorderLayout.CENTER);
		taiDanhSachTau();
		taiDanhSachTuyen();
		capNhatThongTinTuChon();
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(12, 14, 12, 14)));

		JLabel title = new JLabel("Thêm chuyến tàu mới");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		return header;
	}

	private JPanel taoForm() {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(Color.WHITE);
		wrapper.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(20, 20, 20, 20)));

		JPanel formContainer = new JPanel(new GridBagLayout());
		formContainer.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Row 0: Mã chuyến
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbl = new JLabel("Mã chuyến *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtMaChuyenTau = new JTextField();
		txtMaChuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtMaChuyenTau.setPreferredSize(new Dimension(250, 35));
		txtMaChuyenTau.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(8, 8, 8, 8)));
		formContainer.add(txtMaChuyenTau, gbc);

		// Row 1: Tàu
		gbc.gridx = 0;
		gbc.gridy = 1;
		lbl = new JLabel("Tàu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbTau = new JComboBox<>();
		cbTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbTau.setPreferredSize(new Dimension(250, 35));
		cbTau.addActionListener(e -> capNhatThongTinTuChon());
		formContainer.add(cbTau, gbc);

		// Row 2: Tuyến tàu
		gbc.gridx = 0;
		gbc.gridy = 2;
		lbl = new JLabel("Tuyến tàu *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbTuyenTau = new JComboBox<>();
		cbTuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbTuyenTau.setPreferredSize(new Dimension(250, 35));
		cbTuyenTau.addActionListener(e -> capNhatThongTinTuChon());
		formContainer.add(cbTuyenTau, gbc);

		// Row 3: Giờ khởi hành
		gbc.gridx = 0;
		gbc.gridy = 3;
		lbl = new JLabel("Giờ khởi hành *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		spnGioKhoiHanh = taoDateTimePicker();
		formContainer.add(taoPanelChonNgayNhanh(spnGioKhoiHanh), gbc);

		// Row 4: Giờ đến nơi
		gbc.gridx = 0;
		gbc.gridy = 4;
		lbl = new JLabel("Giờ đến nơi");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		spnGioDenNoi = taoDateTimePicker();
		formContainer.add(taoPanelChonNgayNhanh(spnGioDenNoi), gbc);

		// Row 5: Giá cơ bản
		gbc.gridx = 0;
		gbc.gridy = 5;
		lbl = new JLabel("Giá cơ bản (VND) *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtGiaCoban = new JTextField();
		txtGiaCoban.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtGiaCoban.setPreferredSize(new Dimension(250, 35));
		txtGiaCoban.setEditable(false);
		txtGiaCoban.setBackground(new Color(245, 247, 250));
		txtGiaCoban.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(8, 8, 8, 8)));
		formContainer.add(txtGiaCoban, gbc);

		// Row 6: Tổng số chỗ
		gbc.gridx = 0;
		gbc.gridy = 6;
		lbl = new JLabel("Tổng số chỗ *");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		txtTongSoCho = new JTextField();
		txtTongSoCho.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTongSoCho.setPreferredSize(new Dimension(250, 35));
		txtTongSoCho.setEditable(false);
		txtTongSoCho.setBackground(new Color(245, 247, 250));
		txtTongSoCho.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(8, 8, 8, 8)));
		formContainer.add(txtTongSoCho, gbc);

		// Row 7: Trạng thái
		gbc.gridx = 0;
		gbc.gridy = 7;
		lbl = new JLabel("Trạng thái");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lbl.setForeground(Color.decode("#2B4B74"));
		formContainer.add(lbl, gbc);

		gbc.gridx = 1;
		cbTrangThai = new JComboBox<>();
		cbTrangThai.addItem("Lên lịch");
		cbTrangThai.addItem("Đang chạy");
		cbTrangThai.addItem("Hoàn thành");
		cbTrangThai.addItem("Hủy");
		cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbTrangThai.setPreferredSize(new Dimension(250, 35));
		formContainer.add(cbTrangThai, gbc);

		// Row 8: Buttons
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(15, 10, 10, 10);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

		JButton btnThem = new JButton("Thêm chuyến");
		btnThem.setBackground(MAU_CHINH);
		btnThem.setForeground(Color.WHITE);
		btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnThem.setFocusPainted(false);
		btnThem.setBorder(new EmptyBorder(8, 24, 8, 24));
		btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnThem.addActionListener(e -> xuLyThemChuyenTau());
		buttonPanel.add(btnThem);

		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setForeground(Color.decode("#2B4B74"));
		btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnLamMoi.setFocusPainted(false);
		btnLamMoi.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnLamMoi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnLamMoi.addActionListener(e -> lamMoiForm());
		buttonPanel.add(btnLamMoi);

		formContainer.add(buttonPanel, gbc);

		JPanel scrollWrapper = new JPanel(new BorderLayout());
		scrollWrapper.setBackground(Color.WHITE);
		scrollWrapper.add(formContainer, BorderLayout.NORTH);

		wrapper.add(scrollWrapper, BorderLayout.CENTER);
		return wrapper;
	}

	private JSpinner taoDateTimePicker() {
		JSpinner spinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy HH:mm");
		spinner.setEditor(editor);
		spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		spinner.setPreferredSize(new Dimension(250, 35));
		spinner.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(2, 2, 2, 2)));
		return spinner;
	}

	private JPanel taoPanelChonNgayNhanh(JSpinner spn) {
		JPanel wrap = new JPanel(new BorderLayout(5, 0));
		wrap.setBackground(Color.WHITE);
		wrap.add(spn, BorderLayout.CENTER);

		JPanel buttons = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
		buttons.setBackground(Color.WHITE);

		JButton btnCalendar = new JButton(ICON_LICH);
		btnCalendar.setToolTipText("Chọn từ lịch");
		btnCalendar.setFocusPainted(false);
		btnCalendar.setPreferredSize(new Dimension(30, 35));
		btnCalendar.addActionListener(e -> {
			LocalDate current = layLocalDateTimeTuSpinner(spn).toLocalDate();
			CalendarDatePicker picker = new CalendarDatePicker(current, date -> {
				LocalDateTime time = layLocalDateTimeTuSpinner(spn);
				spn.setValue(Date.from(date.atTime(time.toLocalTime()).atZone(ZoneId.systemDefault()).toInstant()));
			});
			picker.showPopup(btnCalendar, 0, btnCalendar.getHeight());
		});
		buttons.add(btnCalendar);

		JButton btnNow = new JButton("Nay");
		btnNow.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnNow.setMargin(new Insets(2, 5, 2, 5));
		btnNow.addActionListener(e -> spn.setValue(new Date()));

		JButton btnTomorrow = new JButton("Mai");
		btnTomorrow.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnTomorrow.setMargin(new Insets(2, 5, 2, 5));
		btnTomorrow.addActionListener(e -> {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, 1);
			spn.setValue(cal.getTime());
		});

		JButton btnPlus1h = new JButton("+1h");
		btnPlus1h.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnPlus1h.setMargin(new Insets(2, 5, 2, 5));
		btnPlus1h.addActionListener(e -> {
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date) spn.getValue());
			cal.add(Calendar.HOUR_OF_DAY, 1);
			spn.setValue(cal.getTime());
		});

		JButton btnPlus3h = new JButton("+3h");
		btnPlus3h.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnPlus3h.setMargin(new Insets(2, 5, 2, 5));
		btnPlus3h.addActionListener(e -> {
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date) spn.getValue());
			cal.add(Calendar.HOUR_OF_DAY, 3);
			spn.setValue(cal.getTime());
		});

		buttons.add(btnNow);
		buttons.add(btnTomorrow);
		buttons.add(btnPlus1h);
		buttons.add(btnPlus3h);
		wrap.add(buttons, BorderLayout.EAST);
		return wrap;
	}

	private static ImageIcon taiAnhIcon(String path, int w, int h) {
		try {
			java.net.URL url = ChuyenTauThemPage.class.getResource(path);
			if (url != null) {
				Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
				return new ImageIcon(img);
			}
		} catch (Exception e) {
		}
		return null;
	}

	private LocalDateTime layLocalDateTimeTuSpinner(JSpinner spn) {
		Date date = (Date) spn.getValue();
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	private void xuLyThemChuyenTau() {
		try {
			TauOption tauOpt = (TauOption) cbTau.getSelectedItem();
			TuyenOption tuyenOpt = (TuyenOption) cbTuyenTau.getSelectedItem();
			if (tauOpt == null || tauOpt.maTau == null || tuyenOpt == null || tuyenOpt.maTT == null) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn tàu và tuyến tàu.");
				return;
			}
			String maTau = tauOpt.maTau;
			String maTuyen = tuyenOpt.maTT;
			LocalDateTime thoiGian = layLocalDateTimeTuSpinner(spnGioKhoiHanh);
			KetQuaXuLy ketQua = chuyenTauController.themChuyenTau(maTau, maTuyen, thoiGian);
			JOptionPane.showMessageDialog(this,
					ketQua.thongBao + (ketQua.thanhCong ? " (" + ketQua.maThamChieu + ")" : ""));
			if (ketQua.thanhCong) {
				lamMoiForm();
			}
		} catch (NumberFormatException | ClassCastException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi khi thêm chuyến tàu: " + ex.getMessage());
		}
	}

	private void taiDanhSachTau() {
		cbTau.removeAllItems();
		cbTau.addItem(new TauOption(null, "-- Chọn tàu --", 0));
		for (Tau tau : new controller.TauController().timKiemTau(null, null)) {
			cbTau.addItem(new TauOption(tau.getMaTau(), tau.getTenTau(), tau.getSoLuongToa()));
		}
	}

	private void taiDanhSachTuyen() {
		cbTuyenTau.removeAllItems();
		cbTuyenTau.addItem(new TuyenOption(null, "-- Chọn tuyến --", null, 0));
		for (TuyenTau tt : new TuyenTau_DAO().layTatCaTuyenTau()) {
			cbTuyenTau.addItem(new TuyenOption(tt.getMaTT(), tt.getMaGaDi(), tt.getMaGaDen(), tt.getKhoangCach()));
		}
	}

	private void capNhatThongTinTuChon() {
		TauOption tauOpt = cbTau == null ? null : (TauOption) cbTau.getSelectedItem();
		TuyenOption tuyenOpt = cbTuyenTau == null ? null : (TuyenOption) cbTuyenTau.getSelectedItem();
		if (tauOpt == null || tauOpt.maTau == null || tuyenOpt == null || tuyenOpt.maTT == null) {
			txtGiaCoban.setText("0 đ");
			txtTongSoCho.setText("0");
			return;
		}

		BigDecimal gia = BigDecimal.valueOf(tuyenOpt.khoangCach).multiply(BigDecimal.valueOf(1000)).setScale(0, java.math.RoundingMode.HALF_UP);
		int tongSoCho = 0;
		for (Toa toa : toaController.timKiemToa(null)) {
			if (tauOpt.maTau.equals(toa.getMaTau())) {
				tongSoCho += toa.getSoGhe();
			}
		}
		txtGiaCoban.setText(BanVeUtils.formatMoney(gia));
		txtTongSoCho.setText(String.valueOf(tongSoCho));
	}

	private void lamMoiForm() {
		txtMaChuyenTau.setText("");
		if (cbTau.getItemCount() > 0) cbTau.setSelectedIndex(0);
		if (cbTuyenTau.getItemCount() > 0) cbTuyenTau.setSelectedIndex(0);
		spnGioKhoiHanh.setValue(new Date());
		spnGioDenNoi.setValue(new Date());
		capNhatThongTinTuChon();
		cbTrangThai.setSelectedIndex(0);
	}
}
