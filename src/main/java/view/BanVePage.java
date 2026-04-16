package view;

import controller.HoaDonController;
import entity.ChiTietHoaDonItem;
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

	private JTextField txtMaKM;
	private JComboBox<String> cboKhachHang;
	private JComboBox<String> cboChuyenTau;
	private JComboBox<String> cboToaTau;

	private BigDecimal selectedPrice = new BigDecimal("1105000");
	private BigDecimal discountAmount = BigDecimal.ZERO;
	private final HoaDonController hoaDonController = new HoaDonController();

	private String selectedKhachHang = "Nguyễn Thị Lan";
	private String selectedChuyen = "SE1-030426";
	private String selectedTuyen = "Sài Gòn - Hà Nội";
	private String selectedKhoiHanh = "19:00 3/4/2026";
	private String selectedToa = "Toa 2 (Ghế mềm)";
	private String selectedNgayLap = LocalDateTime.now().format(TICKET_TIME_FORMAT);
	private String selectedTrangThai = "Đang chờ lập hóa đơn";
	private String generatedMaHD = "HD03042601";
	private String generatedMaVe = "VE03042601";

	public BanVePage() {
		setLayout(new BorderLayout());
		setBackground(MAU_NEN);

		bookedSeats.add("A02");
		bookedSeats.add("A04");
		bookedSeats.add("B02");
		bookedSeats.add("C01");
		bookedSeats.add("C06");
		bookedSeats.add("D03");

		add(taoHeader(), BorderLayout.NORTH);
		add(taoBody(), BorderLayout.CENTER);

		buildSaleCard();
		buildSuccessCard();
		buildPrintCard();
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

	private JComboBox<String> createCustomerCombo() {
		cboKhachHang = new JComboBox<>(new String[] {
			"C002 - Nguyễn Thị Lan - 0976543210",
			"C001 - Trần Văn Minh - 0901234567",
			"C003 - Lê Anh Tuấn - 0987654321"
		});
		cboKhachHang.setSelectedIndex(0);
		cboKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboKhachHang.setPreferredSize(new Dimension(220, 30));
		cboKhachHang.addActionListener(e -> {
			String value = (String) cboKhachHang.getSelectedItem();
			if (value != null) {
				String[] parts = value.split(" - ");
				selectedKhachHang = parts.length > 1 ? parts[1] : value;
				refreshSummary();
			}
		});
		return cboKhachHang;
	}

	private JComboBox<String> createChuyenCombo() {
		cboChuyenTau = new JComboBox<>(new String[] {
			"SE1-030426 - Sài Gòn - Hà Nội - 19:00",
			"SE2-030426 - Sài Gòn - Đà Nẵng - 07:00",
			"TN1-030426 - Sài Gòn - Nha Trang - 06:00"
		});
		cboChuyenTau.setSelectedIndex(0);
		cboChuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboChuyenTau.setPreferredSize(new Dimension(220, 30));
		cboChuyenTau.addActionListener(e -> {
			String value = (String) cboChuyenTau.getSelectedItem();
			if (value != null) {
				String[] parts = value.split(" - ");
				selectedChuyen = parts.length > 0 ? parts[0] : value;
				if (value.contains("Hà Nội")) {
					selectedTuyen = "Sài Gòn - Hà Nội";
				} else if (value.contains("Đà Nẵng")) {
					selectedTuyen = "Sài Gòn - Đà Nẵng";
				} else {
					selectedTuyen = "Sài Gòn - Nha Trang";
				}
				selectedKhoiHanh = parts.length > 3 ? parts[3] + " 3/4/2026" : selectedKhoiHanh;
				refreshSummary();
			}
		});
		return cboChuyenTau;
	}

	private JComboBox<String> createToaCombo() {
		cboToaTau = new JComboBox<>(new String[] {
			"Toa 1 - Ghế cứng - 40 ghế",
			"Toa 2 - Ghế mềm - 40 ghế",
			"Toa 3 - Nằm mềm - 24 ghế"
		});
		cboToaTau.setSelectedIndex(1);
		cboToaTau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboToaTau.setPreferredSize(new Dimension(220, 30));
		cboToaTau.addActionListener(e -> {
			String value = (String) cboToaTau.getSelectedItem();
			if (value != null) {
				int firstDash = value.indexOf(" - ");
				int secondDash = value.lastIndexOf(" - ");
				String toaName = firstDash >= 0 ? value.substring(0, firstDash) : value;
				String toaType = firstDash >= 0 && secondDash > firstDash ? value.substring(firstDash + 3, secondDash) : value;
				selectedToa = toaName + " (" + toaType + ")";
				if (value.contains("Ghế cứng")) {
					selectedPrice = new BigDecimal("850000");
				} else if (value.contains("Ghế mềm")) {
					selectedPrice = new BigDecimal("1105000");
				} else {
					selectedPrice = new BigDecimal("1530000");
				}
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
		JLabel title = new JLabel("2. Chọn chỗ ngồi - Toa 2 (Ghế mềm)");
		title.setFont(new Font("Segoe UI", Font.BOLD, 15));
		title.setForeground(MAU_TEXT);
		header.add(title, BorderLayout.NORTH);

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
		JPanel seats = new JPanel(new GridLayout(8, 4, 8, 8));
		seats.setOpaque(false);
		seatButtons.clear();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 4; col++) {
				String seatCode = String.valueOf((char) ('A' + col)) + String.format("%02d", row + 1);
				JButton seat = new JButton(seatCode);
				seat.setActionCommand(seatCode);
				seat.setPreferredSize(new Dimension(76, 42));
				seat.setFocusPainted(false);
				seat.setFont(new Font("Segoe UI", Font.BOLD, 12));
				seat.addActionListener(e -> onSeatSelected((JButton) e.getSource()));
				seatButtons.add(seat);
				seats.add(seat);
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
		if (bookedSeats.contains(seatCode)) {
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
			String seatCode = seat.getActionCommand();
			if (bookedSeats.contains(seatCode)) {
				seat.setBackground(new Color(255, 240, 240));
				seat.setForeground(MAU_DO);
				seat.setBorder(BorderFactory.createLineBorder(MAU_DO, 2));
			} else if (selectedSeats.contains(seatCode)) {
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

		String maKH = null;
		if (cboKhachHang != null && cboKhachHang.getSelectedItem() != null) {
			String selected = String.valueOf(cboKhachHang.getSelectedItem());
			String[] parts = selected.split(" - ");
			maKH = parts.length > 0 ? parts[0].trim() : null;
		}

		List<ChiTietHoaDonItem> items = new ArrayList<>();
		for (int i = 0; i < selectedSeats.size(); i++) {
			items.add(new ChiTietHoaDonItem(null, null, null, "DV001", 1, selectedPrice));
		}
		KetQuaLapHoaDon ketQua = hoaDonController.lapHoaDon("NV001", maKH, selectedPromotion(), items);

		String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmm"));
		generatedMaHD = ketQua.thanhCong ? ketQua.maHoaDon : "HD" + timeStamp;
		generatedMaVe = "VE" + timeStamp + String.format("%02d", selectedSeats.size());
		selectedNgayLap = LocalDateTime.now().format(TICKET_TIME_FORMAT);
		selectedTrangThai = ketQua.thanhCong ? "Đã lập hóa đơn" : "Lập hóa đơn nội bộ (chưa ghi DB)";
		if (!ketQua.thanhCong) {
			JOptionPane.showMessageDialog(this,
					"Không thể ghi hóa đơn xuống CSDL: " + ketQua.thongBao + "\nHệ thống vẫn tạo hóa đơn tạm để tiếp tục thao tác.",
					"Cảnh báo", JOptionPane.WARNING_MESSAGE);
		}
		refreshSuccessCard();
		hienThiCard(successCard);
	}

	private void refreshSuccessCard() {
		if (lblMaHD != null) {
			lblMaHD.setText(generatedMaHD);
		}
		if (lblSoVe != null) {
			lblSoVe.setText(selectedSeats.size() + " vé");
		}
		if (lblKhachHang != null) {
			lblKhachHang.setText(selectedKhachHang);
		}
		if (lblChuyen != null) {
			lblChuyen.setText(selectedChuyen);
		}
		if (lblTuyen != null) {
			lblTuyen.setText(selectedTuyen);
		}
		if (lblKhoiHanh != null) {
			lblKhoiHanh.setText(selectedNgayLap);
		}
		if (lblToaGhe != null) {
			lblToaGhe.setText(selectedToa + " · " + getSelectedSeatSummary());
		}
		if (lblGia != null) {
			lblGia.setText(formatMoney(getGrandTotal()));
		}
		if (lblNgayLap != null) {
			lblNgayLap.setText(selectedNgayLap);
		}
		if (lblTrangThaiPrint != null) {
			lblTrangThaiPrint.setText(selectedTrangThai);
		}
		if (lblSeatCountPrint != null) {
			lblSeatCountPrint.setText(selectedSeats.size() + " vé");
		}
		if (lblSelectedSeatsPrint != null) {
			lblSelectedSeatsPrint.setText(getSelectedSeatSummary());
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
		JLabel code = new JLabel(generatedMaVe);
		code.setForeground(Color.WHITE);
		code.setFont(new Font("Segoe UI", Font.BOLD, 28));
		JLabel price = new JLabel(formatMoney(getGrandTotal()));
		price.setForeground(Color.WHITE);
		price.setFont(new Font("Segoe UI", Font.BOLD, 20));
		JPanel head = new JPanel(new BorderLayout());
		head.setOpaque(false);
		head.add(shop, BorderLayout.WEST);
		head.add(price, BorderLayout.EAST);
		ticket.add(head, BorderLayout.NORTH);
		ticket.add(code, BorderLayout.CENTER);

		JPanel detail = new JPanel(new GridBagLayout());
		detail.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 0, 6, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		addDetail(detail, gbc, 0, "Khách hàng", selectedKhachHang);
		addDetail(detail, gbc, 1, "Tuyến", selectedTuyen);
		addDetail(detail, gbc, 2, "Khởi hành", selectedNgayLap);
		addDetail(detail, gbc, 3, "Toa / Ghế", selectedToa + " · " + getSelectedSeatSummary());
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
	}

	private void addDetail(JPanel parent, GridBagConstraints gbc, int row, String label, String value) {
		gbc.gridx = 0;
		gbc.gridy = row;
		JLabel lbl = new JLabel(label);
		lbl.setForeground(new Color(191, 219, 254));
		lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		parent.add(lbl, gbc);

		gbc.gridx = 1;
		JLabel val = new JLabel(value);
		val.setForeground(Color.WHITE);
		val.setFont(new Font("Segoe UI", Font.BOLD, 12));
		val.setHorizontalAlignment(SwingConstants.RIGHT);
		parent.add(val, gbc);
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
		String[][] fields = {
			{ "Mã hóa đơn", generatedMaHD },
			{ "Số vé", selectedSeats.size() + " vé" },
			{ "Khách hàng", selectedKhachHang },
			{ "Chuyến tàu", selectedChuyen },
			{ "Tuyến", selectedTuyen },
			{ "Khởi hành", selectedNgayLap },
			{ "Toa / Ghế", selectedToa + " · " + getSelectedSeatSummary() },
			{ "Giá vé", formatMoney(getGrandTotal()) },
			{ "Trạng thái", selectedTrangThai }
		};
		for (int i = 0; i < fields.length; i++) {
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.weightx = 0.28;
			JLabel lbl = new JLabel(fields[i][0] + ":");
			lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			lbl.setForeground(MAU_TEXT);
			receipt.add(lbl, gbc);

			gbc.gridx = 1;
			gbc.weightx = 0.72;
			JLabel val = new JLabel(fields[i][1]);
			val.setFont(new Font("Segoe UI", Font.BOLD, 13));
			val.setForeground(MAU_TEXT);
			val.setHorizontalAlignment(SwingConstants.RIGHT);
			receipt.add(val, gbc);
		}
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
		wrapper.add(quickSummary, BorderLayout.SOUTH);

		JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		footer.setOpaque(false);
		JButton btnIn = new JButton("In hóa đơn");
		btnIn.setBackground(MAU_CHINH);
		btnIn.setForeground(Color.WHITE);
		btnIn.setFocusPainted(false);
		btnIn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnIn.setBorder(new EmptyBorder(8, 16, 8, 16));
		btnIn.addActionListener(e -> {});
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
		wrapper.add(footer, BorderLayout.SOUTH);

		printCard.add(wrapper, BorderLayout.CENTER);
	}

	private void resetForm() {
		selectedSeats.clear();
		selectedSeats.add("B03");
		if (cboKhachHang != null) {
			cboKhachHang.setSelectedIndex(0);
		}
		if (cboChuyenTau != null) {
			cboChuyenTau.setSelectedIndex(0);
		}
		if (cboToaTau != null) {
			cboToaTau.setSelectedIndex(1);
		}
		if (txtMaKM != null) {
			txtMaKM.setText("");
		}
		selectedTrangThai = "Đang chờ lập hóa đơn";
		selectedNgayLap = LocalDateTime.now().format(TICKET_TIME_FORMAT);
		generatedMaHD = "HD" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) + "01";
		generatedMaVe = "VE" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) + "01";
		refreshSeatButtonStyles();
		updateSeatSummary();
		refreshSummary();
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
}
