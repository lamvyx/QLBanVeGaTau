package view;

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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class PhieuDatVeSummaryPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2563EB");
	private static final Color MAU_TEXT = Color.decode("#35506B");

	private JLabel lblMaPhieu, lblKhachHang, lblChuyen, lblTuyen, lblKhoiHanh, lblToa, lblGhe, lblSeatCount;
	private JLabel lblGiaMotVe, lblTongTien;
	private JButton btnChuyen;

	private final Runnable onLuuPhieu, onChuyen, onReset;

	public PhieuDatVeSummaryPanel(Runnable onLuuPhieu, Runnable onChuyen, Runnable onReset) {
		this.onLuuPhieu = onLuuPhieu;
		this.onChuyen = onChuyen;
		this.onReset = onReset;
		setLayout(new BorderLayout(0, 12));
		setPreferredSize(new Dimension(480, 0));
		setOpaque(false);
		initUI();
	}

	private void initUI() {
		add(createOrderPanel(), BorderLayout.NORTH);
		add(createPricePanel(), BorderLayout.CENTER);
	}

	private JPanel createOrderPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 12));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
				new EmptyBorder(14, 14, 14, 14)));

		JLabel title = new JLabel("Tóm tắt phiếu đặt");
		title.setFont(new Font("Segoe UI", Font.BOLD, 15));
		title.setForeground(MAU_TEXT);
		panel.add(title, BorderLayout.NORTH);

		JPanel details = new JPanel(new GridBagLayout());
		details.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 0, 6, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.42;

		lblMaPhieu = addRow(details, gbc, 0, "Mã phiếu:");
		lblKhachHang = addRow(details, gbc, 1, "Khách hàng:");
		lblChuyen = addRow(details, gbc, 2, "Chuyến:");
		lblTuyen = addRow(details, gbc, 3, "Tuyến:");
		lblKhoiHanh = addRow(details, gbc, 4, "Khởi hành:");
		lblToa = addRow(details, gbc, 5, "Toa:");
		lblGhe = addRow(details, gbc, 6, "Ghế:");
		panel.add(details, BorderLayout.CENTER);

		lblSeatCount = new JLabel("0 ghế");
		lblSeatCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblSeatCount.setForeground(MAU_TEXT);
		lblSeatCount.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblSeatCount, BorderLayout.SOUTH);
		return panel;
	}

	private JLabel addRow(JPanel p, GridBagConstraints gbc, int row, String label) {
		gbc.gridx = 0; gbc.gridy = row;
		JLabel l = new JLabel(label);
		l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		l.setForeground(MAU_TEXT);
		p.add(l, gbc);

		gbc.gridx = 1;
		JLabel v = new JLabel("—");
		v.setFont(new Font("Segoe UI", Font.BOLD, 13));
		v.setForeground(MAU_TEXT);
		v.setHorizontalAlignment(SwingConstants.RIGHT);
		p.add(v, gbc);
		return v;
	}

	private JPanel createPricePanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
				new EmptyBorder(14, 14, 14, 14)));

		JPanel priceRows = new JPanel(new GridLayout(2, 1, 6, 6));
		priceRows.setOpaque(false);
		lblGiaMotVe = createPriceRow(priceRows, "Giá 1 vé:");
		lblTongTien = createPriceRow(priceRows, "Tổng cộng:");
		panel.add(priceRows, BorderLayout.NORTH);

		JButton btnLuu = new JButton("Lưu phiếu đặt");
		styleBtn(btnLuu, MAU_CHINH, Color.WHITE);
		btnLuu.addActionListener(e -> onLuuPhieu.run());

		btnChuyen = new JButton("Chuyển sang bán vé");
		styleBtn(btnChuyen, Color.WHITE, MAU_TEXT);
		btnChuyen.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnChuyen.addActionListener(e -> onChuyen.run());

		JButton btnReset = new JButton("Làm mới");
		styleBtn(btnReset, Color.WHITE, MAU_TEXT);
		btnReset.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnReset.addActionListener(e -> onReset.run());

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
		buttons.setOpaque(false);
		buttons.add(btnLuu);
		buttons.add(btnChuyen);
		buttons.add(btnReset);
		panel.add(buttons, BorderLayout.SOUTH);
		return panel;
	}

	private JLabel createPriceRow(JPanel parent, String label) {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);
		JLabel l = new JLabel(label);
		l.setForeground(MAU_TEXT);
		l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		row.add(l, BorderLayout.WEST);
		JLabel v = new JLabel("0 đ");
		v.setForeground(MAU_TEXT);
		v.setFont(new Font("Segoe UI", Font.BOLD, 12));
		v.setHorizontalAlignment(SwingConstants.RIGHT);
		row.add(v, BorderLayout.EAST);
		parent.add(row);
		return v;
	}

	private void styleBtn(JButton b, Color bg, Color fg) {
		b.setBackground(bg);
		b.setForeground(fg);
		b.setFont(new Font("Segoe UI", Font.BOLD, 13));
		b.setFocusPainted(false);
		b.setCursor(new Cursor(Cursor.HAND_CURSOR));
		b.setBorder(new EmptyBorder(10, 20, 10, 20));
	}

	public void setChuyenSangBanEnabled(boolean enabled) {
		btnChuyen.setEnabled(enabled);
	}

	public void updateReservationCode(String maPhieu) {
		lblMaPhieu.setText(maPhieu == null || maPhieu.isBlank() ? "—" : maPhieu);
	}

	public void updateOrderInfo(String kh, String chuyen, String tuyen, String khoiHanh, String toa, String ghe, int soGhe) {
		lblKhachHang.setText(kh);
		lblChuyen.setText(chuyen);
		lblTuyen.setText(tuyen);
		lblKhoiHanh.setText(khoiHanh);
		lblToa.setText(toa);
		lblGhe.setText(ghe);
		lblSeatCount.setText(soGhe + " ghế");
	}

	public void updatePriceInfo(BigDecimal giaMotVe, BigDecimal tongTien) {
		lblGiaMotVe.setText(BanVeUtils.formatMoney(giaMotVe));
		lblTongTien.setText(BanVeUtils.formatMoney(tongTien));
	}
}