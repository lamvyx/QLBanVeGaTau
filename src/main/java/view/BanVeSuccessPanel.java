package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class BanVeSuccessPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color MAU_CHINH = Color.decode("#2563EB");
    private static final Color MAU_XANH = Color.decode("#22C55E");
    private static final Color MAU_TEXT = Color.decode("#35506B");

    private JLabel lblMaVe, lblTongTien, lblKhachHang, lblTuyen, lblKhoiHanh, lblToaGhe;
    private Runnable onPrint, onNewSale;

    public BanVeSuccessPanel(Runnable onPrint, Runnable onNewSale) {
        this.onPrint = onPrint;
        this.onNewSale = onNewSale;
        setLayout(new BorderLayout());
        setOpaque(false);
        initUI();
    }

    private void initUI() {
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

        lblMaVe = new JLabel("—");
        lblMaVe.setForeground(Color.WHITE);
        lblMaVe.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTongTien = new JLabel("0 đ");
        lblTongTien.setForeground(Color.WHITE);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        head.add(new JLabel("GA TÀU SÀI GÒN") {{ setForeground(new Color(191, 219, 254)); }}, BorderLayout.WEST);
        head.add(lblTongTien, BorderLayout.EAST);
        ticket.add(head, BorderLayout.NORTH);
        ticket.add(lblMaVe, BorderLayout.CENTER);

        JPanel detail = new JPanel(new GridBagLayout());
        detail.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        lblKhachHang = addDetailRow(detail, gbc, 0, "Khách hàng");
        lblTuyen = addDetailRow(detail, gbc, 1, "Tuyến");
        lblKhoiHanh = addDetailRow(detail, gbc, 2, "Khởi hành");
        lblToaGhe = addDetailRow(detail, gbc, 3, "Toa / Ghế");
        ticket.add(detail, BorderLayout.SOUTH);

        JButton btnInVe = new JButton("In vé");
        stylePrimary(btnInVe);
        btnInVe.addActionListener(e -> onPrint.run());

        JButton btnMoi = new JButton("Bán vé mới");
        styleSecondary(btnMoi);
        btnMoi.addActionListener(e -> onNewSale.run());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttons.setOpaque(false);
        buttons.add(btnInVe);
        buttons.add(btnMoi);

        JPanel stack = new JPanel(new BorderLayout(0, 12));
        stack.setOpaque(false);
        stack.add(center, BorderLayout.NORTH);
        stack.add(ticket, BorderLayout.CENTER);
        stack.add(buttons, BorderLayout.SOUTH);
        add(stack, BorderLayout.CENTER);
    }

    private JLabel addDetailRow(JPanel parent, GridBagConstraints gbc, int row, String label) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(new Color(191, 219, 254));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        parent.add(lbl, gbc);

        gbc.gridx = 1;
        JLabel val = new JLabel("—");
        val.setForeground(Color.WHITE);
        val.setFont(new Font("Segoe UI", Font.BOLD, 12));
        val.setHorizontalAlignment(SwingConstants.RIGHT);
        parent.add(val, gbc);
        return val;
    }

    private void stylePrimary(JButton b) {
        b.setBackground(MAU_CHINH);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(BorderFactory.createLineBorder(MAU_CHINH, 1));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(145, 40));
    }

    private void styleSecondary(JButton b) {
        b.setBackground(Color.WHITE);
        b.setForeground(MAU_TEXT);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5"), 1));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(145, 40));
    }

    public void setData(String maVe, String tongTien, String kh, String tuyen, String khoiHanh, String toaGhe) {
        lblMaVe.setText(maVe);
        lblTongTien.setText(tongTien);
        lblKhachHang.setText(kh);
        lblTuyen.setText(tuyen);
        lblKhoiHanh.setText(khoiHanh);
        lblToaGhe.setText(toaGhe);
    }
}
