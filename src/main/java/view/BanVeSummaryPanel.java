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

public class BanVeSummaryPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color MAU_CHINH = Color.decode("#2563EB");
    private static final Color MAU_TEXT = Color.decode("#35506B");

    private JLabel lblKhachHang, lblChuyen, lblTuyen, lblKhoiHanh, lblToa, lblGhe, lblSeatCount;
    private JLabel lblGiaMotVe, lblVAT, lblKM, lblSoLuongVe, lblTongTien;
    
    private final Runnable onCheckout, onReset;

    public BanVeSummaryPanel(Runnable onCheckout, Runnable onReset) {
        this.onCheckout = onCheckout;
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

        lblKhachHang = addRow(details, gbc, 0, "Khách hàng:");
        lblChuyen = addRow(details, gbc, 1, "Chuyến:");
        lblTuyen = addRow(details, gbc, 2, "Tuyến:");
        lblKhoiHanh = addRow(details, gbc, 3, "Khởi hành:");
        lblToa = addRow(details, gbc, 4, "Toa:");
        lblGhe = addRow(details, gbc, 5, "Ghế:");
        panel.add(details, BorderLayout.CENTER);

        lblSeatCount = new JLabel("0 vé");
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
            new EmptyBorder(14, 14, 14, 14)
        ));

        JPanel priceRows = new JPanel(new GridLayout(4, 1, 6, 6));
        priceRows.setOpaque(false);
        lblGiaMotVe = createPriceRow(priceRows, "Giá 1 vé:");
        lblVAT = createPriceRow(priceRows, "Thuế VAT:");
        lblKM = createPriceRow(priceRows, "Khuyến mãi:");
        lblSoLuongVe = createPriceRow(priceRows, "Số lượng vé:");
        panel.add(priceRows, BorderLayout.NORTH);

        JPanel totalWrap = new JPanel(new BorderLayout());
        totalWrap.setOpaque(false);
        JLabel totalLabel = new JLabel("Tổng cộng:");
        totalLabel.setForeground(MAU_TEXT);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalWrap.add(totalLabel, BorderLayout.WEST);

        lblTongTien = new JLabel("0 đ");
        lblTongTien.setForeground(MAU_CHINH);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTongTien.setHorizontalAlignment(SwingConstants.RIGHT);
        totalWrap.add(lblTongTien, BorderLayout.EAST);
        panel.add(totalWrap, BorderLayout.CENTER);

        JButton btnLap = new JButton("Lập hóa đơn");
        styleBtn(btnLap, MAU_CHINH, Color.WHITE);
        btnLap.addActionListener(e -> onCheckout.run());

        JButton btnReset = new JButton("Làm mới");
        styleBtn(btnReset, Color.WHITE, MAU_TEXT);
        btnReset.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
        btnReset.addActionListener(e -> onReset.run());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttons.setOpaque(false);
        buttons.add(btnLap);
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
        b.setBackground(bg); b.setForeground(fg);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
    }

    public void updateOrderInfo(String kh, String chuyen, String tuyen, String khoiHanh, String toa, String ghe, int soVe) {
        lblKhachHang.setText(kh);
        lblChuyen.setText(chuyen);
        lblTuyen.setText(tuyen);
        lblKhoiHanh.setText(khoiHanh);
        lblToa.setText(toa);
        lblGhe.setText(ghe);
        lblSeatCount.setText(soVe + " vé");
        lblSoLuongVe.setText(soVe + " vé");
    }

    public void updatePriceInfo(BigDecimal giaMotVe, BigDecimal vat, BigDecimal km, BigDecimal tong) {
        lblGiaMotVe.setText(BanVeUtils.formatMoney(giaMotVe));
        lblVAT.setText(BanVeUtils.formatMoney(vat));
        lblKM.setText(BanVeUtils.formatMoney(km));
        lblTongTien.setText(BanVeUtils.formatMoney(tong));
    }
    public void hienThiGiaTien(BigDecimal Tong) {
    	lblTongTien.setText(BanVeUtils.formatMoney(Tong));
    }
}

