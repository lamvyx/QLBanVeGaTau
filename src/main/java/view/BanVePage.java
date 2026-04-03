package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

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

	private JLabel lblTongTien;
	private JLabel lblSoVe;
	private JLabel lblMaHD;
	private JLabel lblKhachHang;
	private JLabel lblChuyen;
	private JLabel lblTuyen;
	private JLabel lblKhoiHanh;
	private JLabel lblToaGhe;
	private JLabel lblGia;
	private JLabel lblNgayLap;
	private JLabel lblTrangThaiPrint;

	private JTextField txtMaKM;
	private JComboBox<String> cboKhachHang;
	private JComboBox<String> cboChuyenTau;
	private JComboBox<String> cboToaTau;

	private String selectedSeat = "E03";
	private String generatedMaHD = "HD0008";
	private String generatedMaVe = "VE0008";
	private BigDecimal selectedPrice = new BigDecimal("1105000");
	private String selectedKhachHang = "Nguyễn Thị Lan";
	private String selectedChuyen = "SE1-030426";
	private String selectedTuyen = "Sài Gòn - Hà Nội";
	private String selectedKhoiHanh = "19:00 3/4/2026";
	private String selectedToa = "Toa 2 (Ghế mềm)";
	private String selectedNgayLap = LocalDateTime.of(2026, 4, 3, 19, 0).format(TICKET_TIME_FORMAT);
	private String selectedTrangThai = "Đã lập hóa đơn";

	public BanVePage() {
		setLayout(new BorderLayout());
		setBackground(MAU_NEN);
		add(taoHeader(), BorderLayout.NORTH);
		add(taoBody(), BorderLayout.CENTER);
		buildSaleCard();
		buildSuccessCard();
		buildPrintCard();
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

		JLabel subtitle = new JLabel("Lập hóa đơn, chuyển sang in vé hoặc bán vé mới");
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

	private void addLabelField(JPanel parent, GridBagConstraints gbc, int x, int y, String label, java.awt.Component field) {
		gbc.gridx = x * 2;
		gbc.gridy = y;
		gbc.weightx = 0.18;
		JLabel jLabel = new JLabel(label);
		jLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		jLabel.setForeground(MAU_TEXT);
		parent.add(jLabel, gbc);

		gbc.gridx = x * 2 + 1;
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
		cboKhachHang.addActionListener(e -> selectedKhachHang = ((String) cboKhachHang.getSelectedItem()).split(" - ")[1]);
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
			String text = (String) cboChuyenTau.getSelectedItem();
			selectedChuyen = text.split(" - ")[0];
			selectedTuyen = text.contains("Hà Nội") ? "Sài Gòn - Hà Nội" : text.contains("Đà Nẵng") ? "Sài Gòn - Đà Nẵng" : "Sài Gòn - Nha Trang";
			selectedKhoiHanh = text.substring(text.lastIndexOf("-") + 2) + " 3/4/2026";
			refreshSummary();
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
			String text = (String) cboToaTau.getSelectedItem();
			int firstDash = text.indexOf(" - ");
			int secondDash = text.lastIndexOf(" - ");
			String loaiToa = firstDash >= 0 && secondDash > firstDash ? text.substring(firstDash + 3, secondDash) : text;
			selectedToa = text.substring(0, firstDash) + " (" + loaiToa + ")";
			selectedPrice = text.contains("Ghế cứng") ? new BigDecimal("850000") : text.contains("Ghế mềm") ? new BigDecimal("1105000") : new BigDecimal("1530000");
			refreshSummary();
		});
		return cboToaTau;
	}

	private JPanel createPromotionField() {
		JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		wrap.setOpaque(false);
		txtMaKM = new JTextField();
		txtMaKM.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtMaKM.setPreferredSize(new Dimension(140, 30));
		txtMaKM.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 6, 6, 6)
		));
		txtMaKM.setToolTipText("Nhập mã giảm giá nếu có");
		JButton btnApDung = new JButton("Áp dụng");
		btnApDung.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnApDung.setForeground(MAU_TEXT);
		btnApDung.setBackground(Color.WHITE);
		btnApDung.setFocusPainted(false);
		btnApDung.setBorder(BorderFactory.createLineBorder(Color.decode("#DDE5F2")));
		btnApDung.addActionListener(e -> refreshSummary());
		wrap.add(txtMaKM);
		wrap.add(btnApDung);
		return wrap;
	}

	private JPanel createSeatPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setBackground(Color.WHITE);
		wrap.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(14, 14, 14, 14)
		));

		JLabel title = new JLabel("2. Chọn ghế - Toa 2 (Ghế mềm)");
		title.setFont(new Font("Segoe UI", Font.BOLD, 15));
		title.setForeground(MAU_TEXT);
		wrap.add(title, BorderLayout.NORTH);

		JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
		legend.setOpaque(false);
		legend.add(createLegend(MAU_XANH, "Trống (26)"));
		legend.add(createLegend(MAU_DO, "Đã đặt (14)"));
		legend.add(createLegend(MAU_CHINH, "Đang chọn"));
		wrap.add(legend, BorderLayout.BEFORE_FIRST_LINE);

		JPanel seatGridWrap = new JPanel(new BorderLayout());
		seatGridWrap.setOpaque(false);
		seatGridWrap.setBorder(new EmptyBorder(10, 0, 0, 0));
		seatGridWrap.add(createSeatGrid(), BorderLayout.WEST);
		wrap.add(new JScrollPane(seatGridWrap), BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createSeatGrid() {
		JPanel gridWrap = new JPanel(new BorderLayout());
		gridWrap.setOpaque(false);

		JPanel labels = new JPanel(new GridLayout(1, 4, 8, 8));
		labels.setOpaque(false);
		for (String col : new String[] { "A", "B", "C", "D" }) {
			JLabel lbl = new JLabel(col, SwingConstants.CENTER);
			lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lbl.setForeground(MAU_TEXT);
			labels.add(lbl);
		}
		gridWrap.add(labels, BorderLayout.NORTH);

		JPanel seats = new JPanel(new GridLayout(8, 4, 8, 8));
		seats.setOpaque(false);
		for (int row = 1; row <= 8; row++) {
			for (int col = 1; col <= 4; col++) {
				String code = String.format("%02d", row);
				JButton seat = new JButton(code);
				seat.setPreferredSize(new Dimension(42, 32));
				seat.setFocusPainted(false);
				seat.setFont(new Font("Segoe UI", Font.BOLD, 12));
				seat.setBorder(BorderFactory.createLineBorder(MAU_XANH, 2));
				seat.setBackground(Color.WHITE);
				seat.setForeground(MAU_TEXT);
				if (row == 3 && col == 3) {
					seat.setBackground(MAU_CHINH);
					seat.setForeground(Color.WHITE);
					selectedSeat = "E03";
				}
				if ((row == 1 && (col == 2 || col == 4)) || (row == 2 && col == 3) || (row == 4 && col == 2) || (row == 6 && col != 1) || (row == 7 && (col == 1 || col == 3 || col == 4)) || (row == 8 && col == 2)) {
					seat.setBackground(new Color(255, 240, 240));
					seat.setForeground(MAU_DO);
					seat.setBorder(BorderFactory.createLineBorder(MAU_DO, 2));
				}
				seat.addActionListener(this::onSeatSelected);
				seats.add(seat);
			}
		}
		gridWrap.add(seats, BorderLayout.CENTER);

		JLabel wagon = new JLabel("TOA XE - GHẾ MỀM", SwingConstants.CENTER);
		wagon.setOpaque(true);
		wagon.setBackground(Color.WHITE);
		wagon.setForeground(MAU_TEXT);
		wagon.setFont(new Font("Segoe UI", Font.BOLD, 13));
		wagon.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(6, 14, 6, 14)
		));
		wagon.setAlignmentX(CENTER_ALIGNMENT);
		gridWrap.add(wagon, BorderLayout.SOUTH);
		return gridWrap;
	}

	private JPanel createLegend(Color color, String text) {
		JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		legend.setOpaque(false);
		JPanel box = new JPanel();
		box.setPreferredSize(new Dimension(14, 14));
		box.setBackground(color);
		box.setBorder(BorderFactory.createLineBorder(color.darker(), 1));
		legend.add(box);
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		label.setForeground(MAU_TEXT);
		legend.add(label);
		return legend;
	}

	private JPanel createSummaryPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setPreferredSize(new Dimension(460, 0));
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
		gbc.gridx = 0;
		gbc.weightx = 0.42;

		String[] labels = { "Khách hàng:", "Chuyến:", "Tuyến:", "Khởi hành:", "Toa:", "Ghế:" };
		String[] values = { selectedKhachHang, selectedChuyen, selectedTuyen, selectedKhoiHanh, selectedToa, selectedSeat };
		for (int i = 0; i < labels.length; i++) {
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
			gbc.gridx = 0;
		}
		panel.add(details, BorderLayout.CENTER);
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
		priceRows.add(priceRow("Giá gốc:", formatMoney(selectedPrice)));
		priceRows.add(priceRow("Thuế VAT:", formatMoney(selectedPrice.multiply(new BigDecimal("0.08")).setScale(0, RoundingMode.HALF_UP))));
		priceRows.add(priceRow("Khuyến mãi:", txtMaKM.getText().trim().isEmpty() ? "0 đ" : "- 5%"));
		priceRows.add(priceRow("Số lượng vé:", "1 vé"));
		panel.add(priceRows, BorderLayout.NORTH);

		JPanel totalWrap = new JPanel(new BorderLayout());
		totalWrap.setOpaque(false);
		lblTongTien = new JLabel(formatMoney(selectedPrice));
		lblTongTien.setForeground(MAU_CHINH);
		lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTongTien.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel totalLabel = new JLabel("Tổng cộng:");
		totalLabel.setForeground(MAU_TEXT);
		totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		totalWrap.add(totalLabel, BorderLayout.WEST);
		totalWrap.add(lblTongTien, BorderLayout.EAST);
		panel.add(totalWrap, BorderLayout.CENTER);

		JButton btnXacNhan = new JButton("Xác nhận bán vé");
		btnXacNhan.setBackground(MAU_CHINH);
		btnXacNhan.setForeground(Color.WHITE);
		btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnXacNhan.setFocusPainted(false);
		btnXacNhan.setBorder(new EmptyBorder(10, 12, 10, 12));
		btnXacNhan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnXacNhan.addActionListener(e -> confirmSale());

		JButton btnMoi = new JButton("Làm mới");
		btnMoi.setBackground(Color.WHITE);
		btnMoi.setForeground(MAU_TEXT);
		btnMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnMoi.setFocusPainted(false);
		btnMoi.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnMoi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnMoi.addActionListener(e -> resetForm());

		JPanel buttons = new JPanel(new GridLayout(2, 1, 8, 8));
		buttons.setOpaque(false);
		buttons.add(btnXacNhan);
		buttons.add(btnMoi);
		panel.add(buttons, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel priceRow(String label, String value) {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);
		JLabel lbl = new JLabel(label);
		lbl.setForeground(MAU_TEXT);
		lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		JLabel val = new JLabel(value);
		val.setForeground(MAU_TEXT);
		val.setFont(new Font("Segoe UI", Font.BOLD, 13));
		val.setHorizontalAlignment(SwingConstants.RIGHT);
		row.add(lbl, BorderLayout.WEST);
		row.add(val, BorderLayout.EAST);
		return row;
	}

	private void onSeatSelected(ActionEvent event) {
		JButton source = (JButton) event.getSource();
		if (MAU_DO.equals(source.getBackground())) {
			return;
		}
		selectedSeat = source.getText().equals("03") ? "E03" : "E" + source.getText();
		refreshSummary();
		for (java.awt.Component comp : source.getParent().getComponents()) {
			if (comp instanceof JButton button && !MAU_DO.equals(button.getBackground())) {
				button.setBackground(Color.WHITE);
				button.setForeground(MAU_TEXT);
			}
		}
		source.setBackground(MAU_CHINH);
		source.setForeground(Color.WHITE);
	}

	private void confirmSale() {
		generatedMaHD = "HD" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) + "08";
		generatedMaVe = "VE0008";
		selectedNgayLap = LocalDateTime.of(2026, 4, 3, 19, 0).format(TICKET_TIME_FORMAT);
		selectedTrangThai = "Đã lập hóa đơn";
		refreshSuccessCard();
		hienThiCard(successCard);
	}

	private void refreshSuccessCard() {
		lblMaHD.setText(generatedMaHD);
		lblSoVe.setText(generatedMaVe);
		lblKhachHang.setText(selectedKhachHang);
		lblChuyen.setText(selectedChuyen);
		lblTuyen.setText(selectedTuyen);
		lblKhoiHanh.setText(selectedNgayLap);
		lblToaGhe.setText(selectedToa + " · " + selectedSeat);
		lblGia.setText(formatMoney(selectedPrice));
		lblNgayLap.setText(selectedNgayLap);
		lblTrangThaiPrint.setText(selectedTrangThai);
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

		JLabel title = new JLabel("Bán vé thành công!");
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		title.setForeground(MAU_TEXT);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		center.add(title, BorderLayout.CENTER);

		JLabel sub = new JLabel("Vé đã được tạo và in thành công.");
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
		JLabel price = new JLabel(formatMoney(selectedPrice));
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
		addDetail(detail, gbc, 3, "Toa / Ghế", selectedToa + " · " + selectedSeat);
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
			{ "Mã vé", generatedMaVe },
			{ "Khách hàng", selectedKhachHang },
			{ "Chuyến tàu", selectedChuyen },
			{ "Tuyến", selectedTuyen },
			{ "Khởi hành", selectedNgayLap },
			{ "Toa / Ghế", selectedToa + " · " + selectedSeat },
			{ "Giá vé", formatMoney(selectedPrice) },
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

		JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		footer.setOpaque(false);
		JButton btnIn = new JButton("In vé");
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

	private void refreshSummary() {
		lblTongTien.setText(formatMoney(selectedPrice));
		refreshSuccessCard();
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private void resetForm() {
		selectedSeat = "E03";
		selectedPrice = new BigDecimal("1105000");
		selectedKhachHang = "Nguyễn Thị Lan";
		selectedChuyen = "SE1-030426";
		selectedTuyen = "Sài Gòn - Hà Nội";
		selectedKhoiHanh = "19:00 3/4/2026";
		selectedToa = "Toa 2 (Ghế mềm)";
		selectedNgayLap = LocalDateTime.of(2026, 4, 3, 19, 0).format(TICKET_TIME_FORMAT);
		selectedTrangThai = "Đã lập hóa đơn";
		if (cboKhachHang != null) cboKhachHang.setSelectedIndex(0);
		if (cboChuyenTau != null) cboChuyenTau.setSelectedIndex(0);
		if (cboToaTau != null) cboToaTau.setSelectedIndex(1);
		if (txtMaKM != null) txtMaKM.setText("");
		refreshSummary();
		hienThiCard(saleCard);
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
