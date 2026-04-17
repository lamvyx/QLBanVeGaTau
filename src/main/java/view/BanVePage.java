package view;

import controller.ChuyenTauController;
import controller.HoaDonController;
import controller.KhachHangController;
import controller.ToaController;
import entity.ChuyenTau;
import entity.KhachHang;
import entity.Toa;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import service.HoaDonService.KetQuaLapHoaDon;

public class BanVePage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2563EB");
	private static final Color MAU_NEN = Color.decode("#F3F6FB");
	private static final Color MAU_TEXT = Color.decode("#35506B");
	private static final Color MAU_XANH = Color.decode("#22C55E");
	private static final Color MAU_DO = Color.decode("#EF4444");
	private static final DateTimeFormatter TICKET_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private final JPanel contentPanel = new JPanel(new BorderLayout());
	private final JPanel saleCard = new JPanel(new BorderLayout());
	private final JPanel successCard = new JPanel(new BorderLayout());
	private final JPanel printCard = new JPanel(new BorderLayout());

	private final Set<String> selectedSeats = new LinkedHashSet<>();
	private final Set<String> bookedSeats = new LinkedHashSet<>();
	private final List<JButton> seatButtons = new ArrayList<>();

	private JLabel lblTongTien;
	private JLabel lblSeatCount;
	private JLabel lblSeatSummary;
	private JLabel lblMaHD;
	private JLabel lblSoVe;
	private JLabel lblKhachHang;
	private JLabel lblChuyen;
	private JLabel lblTuyen;
	private JLabel lblKhoiHanh;
	private JLabel lblToaGhe;
	private JLabel lblGia;
	private JLabel lblNgayLap;
	private JLabel lblTrangThaiPrint;
	private JLabel lblSeatCountPrint;
	private JLabel lblSelectedSeatsPrint;
	private JLabel lblSeatTitle;
	private JLabel lblMaVeSuccess;
	private JLabel lblTongTienSuccess;
	private JLabel lblMaHDPrintValue;
	private JLabel lblSoVePrintValue;
	private JLabel lblKhachHangPrintValue;
	private JLabel lblChuyenPrintValue;
	private JLabel lblTuyenPrintValue;
	private JLabel lblKhoiHanhPrintValue;
	private JLabel lblToaGhePrintValue;
	private JLabel lblGiaPrintValue;
	private JLabel lblTrangThaiPrintValue;

	private JTextField txtMaKM;
	private JComboBox<KhachHangOption> cboKhachHang;
	private JComboBox<ChuyenTauOption> cboChuyenTau;
	private JComboBox<ToaOption> cboToaTau;
	private JPanel seatGridContainer;

	private BigDecimal selectedPrice = new BigDecimal("1105000");
	private BigDecimal discountAmount = BigDecimal.ZERO;
	private final HoaDonController hoaDonController = new HoaDonController();
	private final KhachHangController khachHangController = new KhachHangController();
	private final ChuyenTauController chuyenTauController = new ChuyenTauController();
	private final ToaController toaController = new ToaController();
	private final List<KhachHangOption> dsKhachHang = new ArrayList<>();
	private final List<ChuyenTauOption> dsChuyenTau = new ArrayList<>();
	private final Map<String, List<ToaOption>> toaTheoChuyen = new HashMap<>();

	private String selectedMaKH;
	private String selectedMaCT;
	private String selectedMaToa;
	private int currentSeatCount = 32;

	private String selectedKhachHang = "Nguyễn Thị Lan";
	private String selectedChuyen = "SE1-030426";
	private String selectedTuyen = "Sài Gòn - Hà Nội";
	private String selectedKhoiHanh = "19:00 3/4/2026";
	private String selectedToa = "Toa 2 (Ghế mềm)";
	private String selectedNgayLap = LocalDateTime.now().format(TICKET_TIME_FORMAT);
	private String selectedTrangThai = "Đang chờ lập hóa đơn";
	private String generatedMaHD = "HD03042601";
	private String generatedMaVe = "VE03042601";
	private boolean daChotGiaoDich;
	private int soVeDaChot;
	private String danhSachGheDaChot = "Chưa chọn ghế";
	private BigDecimal tongTienDaChot = BigDecimal.ZERO;
	private String khoiHanhDaChot = "";
	private String khachHangDaChot = "";
	private String chuyenDaChot = "";
	private String tuyenDaChot = "";
	private String toaDaChot = "";

	public BanVePage() {
		setLayout(new BorderLayout());
		setBackground(MAU_NEN);

		add(taoHeader(), BorderLayout.NORTH);
		add(taoBody(), BorderLayout.CENTER);

		buildSaleCard();
		buildSuccessCard();
		buildPrintCard();
		taiDuLieuBanDau();
		resetForm();
		hienThiCard(saleCard);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#DDE5F2")));
		header.setPreferredSize(new Dimension(0, 62));

		JLabel title = new JLabel("Bán vé tàu");
		title.setFont(new Font("Segoe UI", Font.BOLD, 20));
		title.setForeground(MAU_TEXT);
		title.setBorder(new EmptyBorder(0, 16, 0, 0));
		header.add(title, BorderLayout.WEST);

		JLabel subtitle = new JLabel("Lập hóa đơn, chọn nhiều chỗ và chuyển sang in vé");
		subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		subtitle.setForeground(new Color(108, 122, 138));
		subtitle.setBorder(new EmptyBorder(0, 0, 0, 16));
		header.add(subtitle, BorderLayout.EAST);
		return header;
	}

	private JPanel taoBody() {
		contentPanel.setOpaque(false);
		contentPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
		return contentPanel;
	}

	private void buildSaleCard() {
		saleCard.setOpaque(false);
		saleCard.setLayout(new BorderLayout(12, 12));

		JPanel left = new JPanel(new BorderLayout(0, 12));
		left.setOpaque(false);
		left.add(createInfoPanel(), BorderLayout.NORTH);
		left.add(createSeatPanel(), BorderLayout.CENTER);

		saleCard.add(left, BorderLayout.CENTER);
		saleCard.add(createSummaryPanel(), BorderLayout.EAST);
	}

	private JPanel createInfoPanel() {
		JPanel wrap = new JPanel(new GridBagLayout());
		wrap.setBackground(Color.WHITE);
		wrap.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(14, 14, 14, 14)
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;

		addLabelField(wrap, gbc, 0, 0, "Khách hàng *", createCustomerCombo());
		addLabelField(wrap, gbc, 1, 0, "Chuyến tàu *", createChuyenCombo());
		addLabelField(wrap, gbc, 0, 1, "Toa tàu *", createToaCombo());
		addLabelField(wrap, gbc, 1, 1, "Mã khuyến mãi", createPromotionField());
		return wrap;
	}

	private void addLabelField(JPanel parent, GridBagConstraints gbc, int column, int row, String label, java.awt.Component field) {
		gbc.gridy = row;
		gbc.gridx = column * 2;
		gbc.weightx = 0.18;
		JLabel lbl = new JLabel(label);
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lbl.setForeground(MAU_TEXT);
		parent.add(lbl, gbc);

		gbc.gridx = column * 2 + 1;
		gbc.weightx = 0.32;
		parent.add(field, gbc);
	}

	private JComboBox<KhachHangOption> createCustomerCombo() {
		cboKhachHang = new JComboBox<>();
		cboKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboKhachHang.setPreferredSize(new Dimension(220, 30));
		cboKhachHang.addActionListener(e -> {
			KhachHangOption value = (KhachHangOption) cboKhachHang.getSelectedItem();
			if (value != null) {
				selectedKhachHang = value.tenKH;
				selectedMaKH = value.maKH;
				refreshSummary();
			}
		});
		return cboKhachHang;
	}

	private JComboBox<ChuyenTauOption> createChuyenCombo() {
		cboChuyenTau = new JComboBox<>();
		cboChuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboChuyenTau.setPreferredSize(new Dimension(220, 30));
		cboChuyenTau.addActionListener(e -> {
			ChuyenTauOption value = (ChuyenTauOption) cboChuyenTau.getSelectedItem();
			if (value != null) {
				selectedChuyen = value.maCT;
				selectedMaCT = value.maCT;
				selectedTuyen = value.maTuyen;
				selectedKhoiHanh = value.thoiGianKhoiHanh;
				taiToaTheoChuyen(value);
				refreshSummary();
			}
		});
		return cboChuyenTau;
	}

	private JComboBox<ToaOption> createToaCombo() {
		cboToaTau = new JComboBox<>();
		cboToaTau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboToaTau.setPreferredSize(new Dimension(220, 30));
		cboToaTau.addActionListener(e -> {
			ToaOption value = (ToaOption) cboToaTau.getSelectedItem();
			if (value != null) {
				selectedMaToa = value.maToa;
				selectedToa = value.maToa + " (" + value.loaiToa + ")";
				selectedPrice = value.giaVe;
				currentSeatCount = value.soGhe;
				selectedSeats.clear();
				capNhatGheDaDatTheoToa();
				refreshSeatGrid();
				refreshSummary();
			}
		});
		return cboToaTau;
	}

	private JPanel createPromotionField() {
		JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		wrap.setOpaque(false);
		txtMaKM = new JTextField();
		txtMaKM.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtMaKM.setPreferredSize(new Dimension(140, 30));
		txtMaKM.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				refreshSummary();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				refreshSummary();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				refreshSummary();
			}
		});
		wrap.add(txtMaKM);
		JLabel hint = new JLabel("Nhập mã để giảm 5%");
		hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		hint.setForeground(new Color(108, 122, 138));
		wrap.add(hint);
		return wrap;
	}

	private JPanel createSeatPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setBackground(Color.WHITE);
		wrap.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(14, 14, 14, 14)
		));

		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		lblSeatTitle = new JLabel("2. Chọn chỗ ngồi");
		lblSeatTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblSeatTitle.setForeground(MAU_TEXT);
		header.add(lblSeatTitle, BorderLayout.NORTH);

		lblSeatSummary = new JLabel();
		lblSeatSummary.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSeatSummary.setForeground(new Color(108, 122, 138));
		header.add(lblSeatSummary, BorderLayout.SOUTH);
		wrap.add(header, BorderLayout.NORTH);

		JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
		legend.setOpaque(false);
		legend.add(createLegend(MAU_XANH, "Trống"));
		legend.add(createLegend(MAU_DO, "Đã đặt"));
		legend.add(createLegend(MAU_CHINH, "Đang chọn"));
		wrap.add(legend, BorderLayout.CENTER);

		JPanel gridWrap = new JPanel(new BorderLayout());
		gridWrap.setOpaque(false);
		seatGridContainer = gridWrap;
		gridWrap.add(createSeatGrid(), BorderLayout.CENTER);
		wrap.add(gridWrap, BorderLayout.SOUTH);
		return wrap;
	}

	private JPanel createLegend(Color color, String text) {
		JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		legend.setOpaque(false);
		JLabel dot = new JLabel("●");
		dot.setForeground(color);
		dot.setFont(new Font("Segoe UI", Font.BOLD, 12));
		legend.add(dot);
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		label.setForeground(MAU_TEXT);
		legend.add(label);
		return legend;
	}

	private JScrollPane createSeatGrid() {
		int cols = 4;
		int rows = Math.max(1, (int) Math.ceil((double) currentSeatCount / cols));
		JPanel seats = new JPanel(new GridLayout(rows, cols, 8, 8));
		seats.setOpaque(false);
		seatButtons.clear();
		int index = 0;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (index >= currentSeatCount) {
					JLabel spacer = new JLabel();
					seats.add(spacer);
					continue;
				}
				String seatCode = String.valueOf((char) ('A' + col)) + String.format("%02d", row + 1);
				JButton seat = new JButton(seatCode);
				seat.setActionCommand(seatCode);
				seat.setPreferredSize(new Dimension(76, 42));
				seat.setFocusPainted(false);
				seat.setFont(new Font("Segoe UI", Font.BOLD, 12));
				seat.addActionListener(e -> onSeatSelected((JButton) e.getSource()));
				seatButtons.add(seat);
				seats.add(seat);
				index++;
			}
		}
		refreshSeatButtonStyles();
		return new JScrollPane(seats, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	private JPanel createSummaryPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setPreferredSize(new Dimension(480, 0));
		wrap.setOpaque(false);
		wrap.add(createOrderPanel(), BorderLayout.NORTH);
		wrap.add(createPricePanel(), BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createOrderPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 12));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(14, 14, 14, 14)
		));

		JLabel title = new JLabel("Tóm tắt đơn hàng");
		title.setFont(new Font("Segoe UI", Font.BOLD, 15));
		title.setForeground(MAU_TEXT);
		panel.add(title, BorderLayout.NORTH);

		JPanel details = new JPanel(new GridBagLayout());
		details.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 0, 6, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.42;

		String[] labels = { "Khách hàng:", "Chuyến:", "Tuyến:", "Khởi hành:", "Toa:", "Ghế:" };
		String[] values = { selectedKhachHang, selectedChuyen, selectedTuyen, selectedKhoiHanh, selectedToa, getSelectedSeatSummary() };
		for (int i = 0; i < labels.length; i++) {
			gbc.gridx = 0;
			gbc.gridy = i;
			JLabel lbl = new JLabel(labels[i]);
			lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			lbl.setForeground(MAU_TEXT);
			details.add(lbl, gbc);

			gbc.gridx = 1;
			JLabel val = new JLabel(values[i]);
			val.setFont(new Font("Segoe UI", Font.BOLD, 13));
			val.setForeground(MAU_TEXT);
			val.setHorizontalAlignment(SwingConstants.RIGHT);
			details.add(val, gbc);
		}
		panel.add(details, BorderLayout.CENTER);

		lblSeatCount = new JLabel();
		lblSeatCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblSeatCount.setForeground(MAU_TEXT);
		lblSeatCount.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblSeatCount, BorderLayout.SOUTH);
		updateSeatSummary();
		return panel;
	}

	private JPanel createPricePanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(14, 14, 14, 14)
		));

		JPanel priceRows = new JPanel(new GridLayout(2, 2, 6, 6));
		priceRows.setOpaque(false);
		priceRows.add(priceRow("Giá 1 vé:", formatMoney(selectedPrice)));
		priceRows.add(priceRow("Thuế VAT:", formatMoney(getVatAmount())));
		priceRows.add(priceRow("Khuyến mãi:", selectedPromotion().isEmpty() ? "0 đ" : formatMoney(getDiscountAmount())));
		priceRows.add(priceRow("Số lượng vé:", selectedSeats.size() + " vé"));
		panel.add(priceRows, BorderLayout.NORTH);

		JPanel totalWrap = new JPanel(new BorderLayout());
		totalWrap.setOpaque(false);
		JLabel totalLabel = new JLabel("Tổng cộng:");
		totalLabel.setForeground(MAU_TEXT);
		totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		totalWrap.add(totalLabel, BorderLayout.WEST);

		lblTongTien = new JLabel(formatMoney(getGrandTotal()));
		lblTongTien.setForeground(MAU_CHINH);
		lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTongTien.setHorizontalAlignment(SwingConstants.RIGHT);
		totalWrap.add(lblTongTien, BorderLayout.EAST);
		panel.add(totalWrap, BorderLayout.CENTER);

		JButton btnLapHoaDon = new JButton("Lập hóa đơn");
		btnLapHoaDon.setBackground(MAU_CHINH);
		btnLapHoaDon.setForeground(Color.WHITE);
		btnLapHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnLapHoaDon.setFocusPainted(false);
		btnLapHoaDon.setBorder(new EmptyBorder(10, 12, 10, 12));
		btnLapHoaDon.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnLapHoaDon.addActionListener(e -> confirmSale());

		JButton btnMoi = new JButton("Làm mới");
		btnMoi.setBackground(Color.WHITE);
		btnMoi.setForeground(MAU_TEXT);
		btnMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnMoi.setFocusPainted(false);
		btnMoi.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnMoi.addActionListener(e -> resetForm());

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
		buttons.setOpaque(false);
		buttons.add(btnLapHoaDon);
		buttons.add(btnMoi);
		panel.add(buttons, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel priceRow(String label, String value) {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);
		JLabel lbl = new JLabel(label);
		lbl.setForeground(MAU_TEXT);
		lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		row.add(lbl, BorderLayout.WEST);
		JLabel val = new JLabel(value);
		val.setForeground(MAU_TEXT);
		val.setFont(new Font("Segoe UI", Font.BOLD, 12));
		row.add(val, BorderLayout.EAST);
		return row;
	}

	private void onSeatSelected(JButton source) {
		String seatCode = source.getActionCommand();
		if (bookedSeats.contains(chuanHoaMaGhe(seatCode))) {
			return;
		}

		if (selectedSeats.contains(seatCode)) {
			selectedSeats.remove(seatCode);
		} else {
			selectedSeats.add(seatCode);
		}

		refreshSeatButtonStyles();
		updateSeatSummary();
		refreshSummary();
	}

	private void refreshSeatButtonStyles() {
		for (JButton seat : seatButtons) {
			String seatCode = chuanHoaMaGhe(seat.getActionCommand());
			if (bookedSeats.contains(seatCode)) {
				seat.setBackground(new Color(255, 240, 240));
				seat.setForeground(MAU_DO);
				seat.setBorder(BorderFactory.createLineBorder(MAU_DO, 2));
			} else if (selectedSeats.contains(seat.getActionCommand())) {
				seat.setBackground(MAU_CHINH);
				seat.setForeground(Color.WHITE);
				seat.setBorder(BorderFactory.createLineBorder(MAU_CHINH.darker(), 2));
			} else {
				seat.setBackground(Color.WHITE);
				seat.setForeground(MAU_TEXT);
				seat.setBorder(BorderFactory.createLineBorder(MAU_XANH, 2));
			}
		}
	}

	private void updateSeatSummary() {
		int count = selectedSeats.size();
		if (lblSeatSummary != null) {
			lblSeatSummary.setText("Đã chọn: " + count + " ghế | " + getSelectedSeatSummary());
		}
		if (lblSeatCount != null) {
			lblSeatCount.setText(count + " vé");
		}
	}

	private String getSelectedSeatSummary() {
		if (selectedSeats.isEmpty()) {
			return "Chưa chọn ghế";
		}
		return String.join(", ", selectedSeats);
	}

	private BigDecimal getTotalBase() {
		return selectedPrice.multiply(BigDecimal.valueOf(selectedSeats.size()));
	}

	private BigDecimal getVatAmount() {
		return getTotalBase().multiply(new BigDecimal("0.08")).setScale(0, RoundingMode.HALF_UP);
	}

	private String selectedPromotion() {
		return txtMaKM == null ? "" : txtMaKM.getText().trim();
	}

	private BigDecimal getDiscountAmount() {
		if (selectedPromotion().isEmpty()) {
			return BigDecimal.ZERO;
		}
		return getTotalBase().multiply(new BigDecimal("0.05")).setScale(0, RoundingMode.HALF_UP);
	}

	private BigDecimal getGrandTotal() {
		discountAmount = getDiscountAmount();
		return getTotalBase().add(getVatAmount()).subtract(discountAmount);
	}

	private void refreshSummary() {
		if (lblTongTien != null) {
			lblTongTien.setText(formatMoney(getGrandTotal()));
		}
		if (lblSeatTitle != null) {
			lblSeatTitle.setText("2. Chọn chỗ ngồi - " + selectedToa);
		}
		updateSeatSummary();
		refreshSuccessCard();
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private void confirmSale() {
		if (selectedSeats.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một chỗ ngồi.", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		KhachHangOption khachDangChon = cboKhachHang == null ? null : (KhachHangOption) cboKhachHang.getSelectedItem();
		if (khachDangChon != null && khachDangChon.maKH != null && !khachDangChon.maKH.isBlank()) {
			selectedMaKH = khachDangChon.maKH.trim();
			selectedKhachHang = khachDangChon.tenKH;
		}
		if (selectedMaKH == null || selectedMaKH.isBlank() || selectedMaCT == null || selectedMaToa == null) {
			JOptionPane.showMessageDialog(this,
					"Thiếu thông tin khách hàng/chuyến tàu/toa tàu. Vui lòng chọn lại.",
					"Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
			return;
		}

		KetQuaLapHoaDon ketQua = hoaDonController.lapHoaDonBanVe("NV001", selectedMaKH, selectedPromotion(),
				selectedMaCT, selectedMaToa, new ArrayList<>(selectedSeats), selectedPrice);

		String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmm"));
		int soVeVuaChon = selectedSeats.size();
		generatedMaHD = ketQua.thanhCong ? ketQua.maHoaDon : "HD" + timeStamp;
		generatedMaVe = "VE" + timeStamp + String.format("%02d", soVeVuaChon);
		selectedNgayLap = LocalDateTime.now().format(TICKET_TIME_FORMAT);
		selectedTrangThai = ketQua.thanhCong ? "Đã lập hóa đơn" : "Không thể đặt vé";
		if (!ketQua.thanhCong) {
			JOptionPane.showMessageDialog(this,
					"Không thể ghi hóa đơn xuống CSDL: " + ketQua.thongBao,
					"Cảnh báo", JOptionPane.WARNING_MESSAGE);
			capNhatGheDaDatTheoToa();
			refreshSeatGrid();
			return;
		}

		daChotGiaoDich = true;
		soVeDaChot = soVeVuaChon;
		danhSachGheDaChot = String.join(", ", selectedSeats);
		tongTienDaChot = getGrandTotal();
		khoiHanhDaChot = selectedKhoiHanh;
		khachHangDaChot = selectedKhachHang;
		chuyenDaChot = selectedChuyen;
		tuyenDaChot = selectedTuyen;
		toaDaChot = selectedToa;

		capNhatGheDaDatTheoToa();
		selectedSeats.clear();
		refreshSeatGrid();
		refreshSuccessCard();
		hienThiCard(successCard);
	}

	private void refreshSuccessCard() {
		int soVeHienThi = daChotGiaoDich ? soVeDaChot : selectedSeats.size();
		String gheHienThi = daChotGiaoDich ? danhSachGheDaChot : getSelectedSeatSummary();
		BigDecimal tongTienHienThi = daChotGiaoDich ? tongTienDaChot : getGrandTotal();
		String khoiHanhHienThi = daChotGiaoDich ? khoiHanhDaChot : selectedKhoiHanh;
		String khachHangHienThi = daChotGiaoDich ? khachHangDaChot : selectedKhachHang;
		String chuyenHienThi = daChotGiaoDich ? chuyenDaChot : selectedChuyen;
		String tuyenHienThi = daChotGiaoDich ? tuyenDaChot : selectedTuyen;
		String toaHienThi = daChotGiaoDich ? toaDaChot : selectedToa;

		if (lblMaHD != null) {
			lblMaHD.setText(generatedMaHD);
		}
		if (lblMaVeSuccess != null) {
			lblMaVeSuccess.setText(generatedMaVe);
		}
		if (lblSoVe != null) {
			lblSoVe.setText(soVeHienThi + " vé");
		}
		if (lblKhachHang != null) {
			lblKhachHang.setText(khachHangHienThi);
		}
		if (lblChuyen != null) {
			lblChuyen.setText(chuyenHienThi);
		}
		if (lblTuyen != null) {
			lblTuyen.setText(tuyenHienThi);
		}
		if (lblKhoiHanh != null) {
			lblKhoiHanh.setText(khoiHanhHienThi);
		}
		if (lblToaGhe != null) {
			lblToaGhe.setText(toaHienThi + " · " + gheHienThi);
		}
		if (lblGia != null) {
			lblGia.setText(formatMoney(tongTienHienThi));
		}
		if (lblTongTienSuccess != null) {
			lblTongTienSuccess.setText(formatMoney(tongTienHienThi));
		}
		if (lblNgayLap != null) {
			lblNgayLap.setText(selectedNgayLap);
		}
		if (lblMaHDPrintValue != null) {
			lblMaHDPrintValue.setText(generatedMaHD);
		}
		if (lblSoVePrintValue != null) {
			lblSoVePrintValue.setText(soVeHienThi + " vé");
		}
		if (lblKhachHangPrintValue != null) {
			lblKhachHangPrintValue.setText(khachHangHienThi);
		}
		if (lblChuyenPrintValue != null) {
			lblChuyenPrintValue.setText(chuyenHienThi);
		}
		if (lblTuyenPrintValue != null) {
			lblTuyenPrintValue.setText(tuyenHienThi);
		}
		if (lblKhoiHanhPrintValue != null) {
			lblKhoiHanhPrintValue.setText(khoiHanhHienThi);
		}
		if (lblToaGhePrintValue != null) {
			lblToaGhePrintValue.setText(toaHienThi + " · " + gheHienThi);
		}
		if (lblGiaPrintValue != null) {
			lblGiaPrintValue.setText(formatMoney(tongTienHienThi));
		}
		if (lblTrangThaiPrintValue != null) {
			lblTrangThaiPrintValue.setText(selectedTrangThai);
		}
		if (lblTrangThaiPrint != null) {
			lblTrangThaiPrint.setText(selectedTrangThai);
		}
		if (lblSeatCountPrint != null) {
			lblSeatCountPrint.setText(soVeHienThi + " vé");
		}
		if (lblSelectedSeatsPrint != null) {
			lblSelectedSeatsPrint.setText(gheHienThi);
		}
	}

	private void buildSuccessCard() {
		successCard.setOpaque(false);
		JPanel center = new JPanel(new BorderLayout(0, 12));
		center.setBackground(Color.WHITE);
		center.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(24, 24, 24, 24)
		));

		JLabel icon = new JLabel("✓", SwingConstants.CENTER);
		icon.setOpaque(true);
		icon.setBackground(new Color(220, 252, 231));
		icon.setForeground(MAU_XANH);
		icon.setFont(new Font("Segoe UI", Font.BOLD, 34));
		icon.setPreferredSize(new Dimension(72, 72));
		icon.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		iconWrap.setOpaque(false);
		iconWrap.add(icon);
		center.add(iconWrap, BorderLayout.NORTH);

		JLabel title = new JLabel("Lập hóa đơn thành công!");
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		title.setForeground(MAU_TEXT);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		center.add(title, BorderLayout.CENTER);

		JLabel sub = new JLabel("Hóa đơn đã được tạo và sẵn sàng in.");
		sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		sub.setForeground(new Color(108, 122, 138));
		sub.setHorizontalAlignment(SwingConstants.CENTER);
		center.add(sub, BorderLayout.SOUTH);

		JPanel ticket = new JPanel(new BorderLayout());
		ticket.setBackground(MAU_CHINH);
		ticket.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

		JLabel shop = new JLabel("GA TÀU SÀI GÒN");
		shop.setForeground(new Color(191, 219, 254));
		shop.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblMaVeSuccess = new JLabel(generatedMaVe);
		lblMaVeSuccess.setForeground(Color.WHITE);
		lblMaVeSuccess.setFont(new Font("Segoe UI", Font.BOLD, 28));
		lblTongTienSuccess = new JLabel(formatMoney(getGrandTotal()));
		lblTongTienSuccess.setForeground(Color.WHITE);
		lblTongTienSuccess.setFont(new Font("Segoe UI", Font.BOLD, 20));
		JPanel head = new JPanel(new BorderLayout());
		head.setOpaque(false);
		head.add(shop, BorderLayout.WEST);
		head.add(lblTongTienSuccess, BorderLayout.EAST);
		ticket.add(head, BorderLayout.NORTH);
		ticket.add(lblMaVeSuccess, BorderLayout.CENTER);

		JPanel detail = new JPanel(new GridBagLayout());
		detail.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 0, 6, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		lblKhachHang = addSuccessDetail(detail, gbc, 0, "Khách hàng");
		lblTuyen = addSuccessDetail(detail, gbc, 1, "Tuyến");
		lblKhoiHanh = addSuccessDetail(detail, gbc, 2, "Khởi hành");
		lblToaGhe = addSuccessDetail(detail, gbc, 3, "Toa / Ghế");
		ticket.add(detail, BorderLayout.SOUTH);

		JButton btnInVe = new JButton("In vé");
		btnInVe.setBackground(MAU_CHINH);
		btnInVe.setForeground(Color.WHITE);
		btnInVe.setFocusPainted(false);
		btnInVe.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnInVe.setBorder(new EmptyBorder(8, 16, 8, 16));
		btnInVe.addActionListener(e -> hienThiCard(printCard));

		JButton btnMoi = new JButton("Bán vé mới");
		btnMoi.setBackground(Color.WHITE);
		btnMoi.setForeground(MAU_TEXT);
		btnMoi.setFocusPainted(false);
		btnMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnMoi.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnMoi.addActionListener(e -> resetForm());

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
		buttons.setOpaque(false);
		buttons.add(btnInVe);
		buttons.add(btnMoi);

		JPanel stack = new JPanel(new BorderLayout(0, 12));
		stack.setOpaque(false);
		stack.add(center, BorderLayout.NORTH);
		stack.add(ticket, BorderLayout.CENTER);
		stack.add(buttons, BorderLayout.SOUTH);
		successCard.add(stack, BorderLayout.CENTER);

		lblMaHD = new JLabel();
		lblSoVe = new JLabel();
		lblChuyen = new JLabel();
		lblGia = new JLabel();
		lblNgayLap = new JLabel();
		refreshSuccessCard();
	}

	private JLabel addSuccessDetail(JPanel parent, GridBagConstraints gbc, int row, String label) {
		gbc.gridx = 0;
		gbc.gridy = row;
		JLabel lbl = new JLabel(label);
		lbl.setForeground(new Color(191, 219, 254));
		lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		parent.add(lbl, gbc);

		gbc.gridx = 1;
		JLabel val = new JLabel();
		val.setForeground(Color.WHITE);
		val.setFont(new Font("Segoe UI", Font.BOLD, 12));
		val.setHorizontalAlignment(SwingConstants.RIGHT);
		parent.add(val, gbc);
		return val;
	}

	private void buildPrintCard() {
		printCard.setOpaque(false);
		JPanel wrapper = new JPanel(new BorderLayout(0, 12));
		wrapper.setBackground(Color.WHITE);
		wrapper.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(24, 24, 24, 24)
		));

		JLabel title = new JLabel("In vé / Hóa đơn");
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		title.setForeground(MAU_TEXT);
		wrapper.add(title, BorderLayout.NORTH);

		JPanel receipt = new JPanel(new GridBagLayout());
		receipt.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		lblMaHDPrintValue = addPrintReceiptRow(receipt, gbc, 0, "Mã hóa đơn");
		lblSoVePrintValue = addPrintReceiptRow(receipt, gbc, 1, "Số vé");
		lblKhachHangPrintValue = addPrintReceiptRow(receipt, gbc, 2, "Khách hàng");
		lblChuyenPrintValue = addPrintReceiptRow(receipt, gbc, 3, "Chuyến tàu");
		lblTuyenPrintValue = addPrintReceiptRow(receipt, gbc, 4, "Tuyến");
		lblKhoiHanhPrintValue = addPrintReceiptRow(receipt, gbc, 5, "Khởi hành");
		lblToaGhePrintValue = addPrintReceiptRow(receipt, gbc, 6, "Toa / Ghế");
		lblGiaPrintValue = addPrintReceiptRow(receipt, gbc, 7, "Giá vé");
		lblTrangThaiPrintValue = addPrintReceiptRow(receipt, gbc, 8, "Trạng thái");
		wrapper.add(receipt, BorderLayout.CENTER);

		lblSeatCountPrint = new JLabel();
		lblSelectedSeatsPrint = new JLabel();
		lblTrangThaiPrint = new JLabel();
		JPanel quickSummary = new JPanel(new GridLayout(3, 1, 0, 4));
		quickSummary.setOpaque(false);
		for (JLabel label : new JLabel[] { lblSeatCountPrint, lblSelectedSeatsPrint, lblTrangThaiPrint }) {
			label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			label.setForeground(new Color(108, 122, 138));
			quickSummary.add(label);
		}

		JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		footer.setOpaque(false);
		JButton btnIn = new JButton("In hóa đơn");
		btnIn.setBackground(MAU_CHINH);
		btnIn.setForeground(Color.WHITE);
		btnIn.setFocusPainted(false);
		btnIn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnIn.setBorder(new EmptyBorder(8, 16, 8, 16));
		btnIn.addActionListener(e -> JOptionPane.showMessageDialog(this,
				"Đã gửi lệnh in hóa đơn cho mã " + generatedMaHD + ".",
				"In hóa đơn",
				JOptionPane.INFORMATION_MESSAGE));
		JButton btnBack = new JButton("Quay lại");
		btnBack.setBackground(Color.WHITE);
		btnBack.setForeground(MAU_TEXT);
		btnBack.setFocusPainted(false);
		btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnBack.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnBack.addActionListener(e -> hienThiCard(successCard));
		JButton btnMoi = new JButton("Bán vé mới");
		btnMoi.setBackground(Color.WHITE);
		btnMoi.setForeground(MAU_TEXT);
		btnMoi.setFocusPainted(false);
		btnMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnMoi.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnMoi.addActionListener(e -> resetForm());
		footer.add(btnIn);
		footer.add(btnBack);
		footer.add(btnMoi);

		JPanel southWrap = new JPanel(new BorderLayout(0, 10));
		southWrap.setOpaque(false);
		southWrap.add(quickSummary, BorderLayout.NORTH);
		southWrap.add(footer, BorderLayout.SOUTH);
		wrapper.add(southWrap, BorderLayout.SOUTH);

		printCard.add(wrapper, BorderLayout.CENTER);
		refreshSuccessCard();
	}

	private JLabel addPrintReceiptRow(JPanel parent, GridBagConstraints gbc, int row, String label) {
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.weightx = 0.28;
		JLabel lbl = new JLabel(label + ":");
		lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lbl.setForeground(MAU_TEXT);
		parent.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.72;
		JLabel val = new JLabel();
		val.setFont(new Font("Segoe UI", Font.BOLD, 13));
		val.setForeground(MAU_TEXT);
		val.setHorizontalAlignment(SwingConstants.RIGHT);
		parent.add(val, gbc);
		return val;
	}

	private void resetForm() {
		selectedSeats.clear();
		daChotGiaoDich = false;
		soVeDaChot = 0;
		danhSachGheDaChot = "Chưa chọn ghế";
		tongTienDaChot = BigDecimal.ZERO;
		khoiHanhDaChot = "";
		khachHangDaChot = "";
		chuyenDaChot = "";
		tuyenDaChot = "";
		toaDaChot = "";
		if (cboKhachHang != null) {
			if (cboKhachHang.getItemCount() > 0) {
				cboKhachHang.setSelectedIndex(0);
			}
		}
		if (cboChuyenTau != null) {
			if (cboChuyenTau.getItemCount() > 0) {
				cboChuyenTau.setSelectedIndex(0);
			}
		}
		if (cboToaTau != null) {
			if (cboToaTau.getItemCount() > 0) {
				cboToaTau.setSelectedIndex(0);
			}
		}
		if (txtMaKM != null) {
			txtMaKM.setText("");
		}
		selectedTrangThai = "Đang chờ lập hóa đơn";
		selectedNgayLap = LocalDateTime.now().format(TICKET_TIME_FORMAT);
		generatedMaHD = "HD" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) + "01";
		generatedMaVe = "VE" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) + "01";
		capNhatGheDaDatTheoToa();
		refreshSeatGrid();
		refreshSeatButtonStyles();
		updateSeatSummary();
		refreshSummary();
	}

	private void taiDuLieuBanDau() {
		dsKhachHang.clear();
		for (KhachHang kh : khachHangController.layTatCaKhachHang()) {
			dsKhachHang.add(new KhachHangOption(kh.getMaKH(), kh.getTenKH(), kh.getSdt()));
		}
		if (cboKhachHang != null) {
			cboKhachHang.removeAllItems();
			for (KhachHangOption option : dsKhachHang) {
				cboKhachHang.addItem(option);
			}
		}

		dsChuyenTau.clear();
		for (ChuyenTau ct : chuyenTauController.timKiemChuyenTau("")) {
			dsChuyenTau.add(new ChuyenTauOption(ct.getMaCT(), ct.getMaTau(), ct.getMaTuyenTau(), formatKhoiHanh(ct)));
		}
		if (cboChuyenTau != null) {
			cboChuyenTau.removeAllItems();
			for (ChuyenTauOption option : dsChuyenTau) {
				cboChuyenTau.addItem(option);
			}
		}

		toaTheoChuyen.clear();
		List<Toa> dsToa = toaController.timKiemToa(null);
		for (Toa toa : dsToa) {
			ToaOption option = new ToaOption(toa.getMaToa(), toa.getLoaiToa(), toa.getSoGhe(),
					xacDinhGiaVeTheoLoaiToa(toa.getLoaiToa()));
			toaTheoChuyen.computeIfAbsent(toa.getMaTau(), k -> new ArrayList<>()).add(option);
		}

		if (cboKhachHang != null && cboKhachHang.getItemCount() > 0) {
			cboKhachHang.setSelectedIndex(0);
		} else {
			selectedMaKH = null;
			selectedKhachHang = "Chưa chọn khách hàng";
		}
		if (cboChuyenTau != null && cboChuyenTau.getItemCount() > 0) {
			cboChuyenTau.setSelectedIndex(0);
		} else {
			if (cboToaTau != null) {
				cboToaTau.removeAllItems();
			}
			selectedMaCT = null;
			selectedMaToa = null;
			selectedToa = "Chưa chọn toa";
			selectedChuyen = "Chưa chọn chuyến";
			selectedTuyen = "";
			selectedKhoiHanh = "";
		}
	}

	private void taiToaTheoChuyen(ChuyenTauOption chuyen) {
		if (cboToaTau == null) {
			return;
		}
		cboToaTau.removeAllItems();
		List<ToaOption> ds = toaTheoChuyen.getOrDefault(chuyen.maTau, Collections.emptyList());
		for (ToaOption toa : ds) {
			cboToaTau.addItem(toa);
		}
		if (cboToaTau.getItemCount() > 0) {
			cboToaTau.setSelectedIndex(0);
		} else {
			selectedMaToa = null;
			selectedToa = "Chưa chọn toa";
			bookedSeats.clear();
			selectedSeats.clear();
			refreshSeatGrid();
		}
	}

	private void capNhatGheDaDatTheoToa() {
		bookedSeats.clear();
		if (selectedMaCT == null || selectedMaToa == null) {
			return;
		}
		Set<String> daDat = hoaDonController.layGheDaDat(selectedMaCT, selectedMaToa);
		for (String ghe : daDat) {
			bookedSeats.add(chuanHoaMaGhe(ghe));
		}
	}

	private void refreshSeatGrid() {
		if (seatGridContainer == null) {
			return;
		}
		seatGridContainer.removeAll();
		seatGridContainer.add(createSeatGrid(), BorderLayout.CENTER);
		seatGridContainer.revalidate();
		seatGridContainer.repaint();
	}

	private BigDecimal xacDinhGiaVeTheoLoaiToa(String loaiToa) {
		if (loaiToa == null) {
			return new BigDecimal("1105000");
		}
		String value = loaiToa.toLowerCase();
		if (value.contains("ngồi mềm") || value.contains("ghế mềm")) {
			return new BigDecimal("1105000");
		}
		if (value.contains("nằm")) {
			return new BigDecimal("1530000");
		}
		return new BigDecimal("850000");
	}

	private String formatKhoiHanh(ChuyenTau ct) {
		if (ct.getNgayKhoiHanh() == null) {
			return "Chưa rõ";
		}
		return ct.getNgayKhoiHanh().format(TICKET_TIME_FORMAT);
	}

	private String chuanHoaMaGhe(String raw) {
		if (raw == null) {
			return "";
		}
		String value = raw.trim().toUpperCase();
		if (value.matches("[A-Z]\\d+")) {
			char cot = value.charAt(0);
			int row = Integer.parseInt(value.substring(1));
			return cot + String.format("%02d", row);
		}
		if (value.matches("\\d+[A-Z]")) {
			char cot = value.charAt(value.length() - 1);
			int row = Integer.parseInt(value.substring(0, value.length() - 1));
			return cot + String.format("%02d", row);
		}
		return value;
	}

	private void hienThiCard(JPanel card) {
		contentPanel.removeAll();
		contentPanel.add(card, BorderLayout.CENTER);
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private String formatMoney(BigDecimal value) {
		return String.format("%,.0f đ", value);
	}

	private static final class KhachHangOption {
		private final String maKH;
		private final String tenKH;
		private final String sdt;

		private KhachHangOption(String maKH, String tenKH, String sdt) {
			this.maKH = maKH;
			this.tenKH = tenKH == null ? "" : tenKH;
			this.sdt = sdt == null ? "" : sdt;
		}

		@Override
		public String toString() {
			return maKH + " - " + tenKH + (sdt.isBlank() ? "" : " - " + sdt);
		}
	}

	private static final class ChuyenTauOption {
		private final String maCT;
		private final String maTau;
		private final String maTuyen;
		private final String thoiGianKhoiHanh;

		private ChuyenTauOption(String maCT, String maTau, String maTuyen, String thoiGianKhoiHanh) {
			this.maCT = maCT;
			this.maTau = maTau;
			this.maTuyen = maTuyen;
			this.thoiGianKhoiHanh = thoiGianKhoiHanh;
		}

		@Override
		public String toString() {
			return maCT + " - " + maTau + " - " + maTuyen + " - " + thoiGianKhoiHanh;
		}
	}

	private static final class ToaOption {
		private final String maToa;
		private final String loaiToa;
		private final int soGhe;
		private final BigDecimal giaVe;

		private ToaOption(String maToa, String loaiToa, int soGhe, BigDecimal giaVe) {
			this.maToa = maToa;
			this.loaiToa = loaiToa;
			this.soGhe = soGhe;
			this.giaVe = giaVe;
		}

		@Override
		public String toString() {
			return maToa + " - " + loaiToa + " - " + soGhe + " ghế";
		}
	}
}
