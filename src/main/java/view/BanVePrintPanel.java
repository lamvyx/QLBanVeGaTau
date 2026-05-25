package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class BanVePrintPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color MAU_CHINH = Color.decode("#2563EB");
    private static final Color MAU_TEXT = Color.decode("#35506B");

    private JLabel lblMaHD, lblSoVe, lblKhachHang, lblChuyenTau, lblTuyen, lblKhoiHanh, lblToaGhe, lblGia, lblTrangThai;
    private Runnable onNewSale;

    public BanVePrintPanel(Runnable onNewSale) {
        this.onNewSale = onNewSale;
        setLayout(new BorderLayout());
        setOpaque(false);
        initUI();
    }

    private void initUI() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
                new EmptyBorder(24, 24, 24, 24)));

        JLabel title = new JLabel("In vé / Hóa đơn");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(MAU_TEXT);
        wrapper.add(title, BorderLayout.NORTH);

        JPanel receipt = new JPanel(new GridBagLayout());
        receipt.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblMaHD = addReceiptRow(receipt, gbc, 0, "Mã hóa đơn");
        lblSoVe = addReceiptRow(receipt, gbc, 1, "Số vé");
        lblKhachHang = addReceiptRow(receipt, gbc, 2, "Khách hàng");
        lblChuyenTau = addReceiptRow(receipt, gbc, 3, "Chuyến tàu");
        lblTuyen = addReceiptRow(receipt, gbc, 4, "Tuyến");
        lblKhoiHanh = addReceiptRow(receipt, gbc, 5, "Khởi hành");
        lblToaGhe = addReceiptRow(receipt, gbc, 6, "Toa / Ghế");
        lblGia = addReceiptRow(receipt, gbc, 7, "Giá vé");
        lblTrangThai = addReceiptRow(receipt, gbc, 8, "Trạng thái");
        wrapper.add(receipt, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        footer.setOpaque(false);

        JButton btnIn = new JButton("In hóa đơn");
        stylePrimary(btnIn);
        btnIn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đã gửi lệnh in."));

        // JButton btnBack = new JButton("Quay lại");
        // styleSecondary(btnBack);
        // btnBack.addActionListener(e -> onBack.run());

        JButton btnMoi = new JButton("Bán vé mới");
        styleSecondary(btnMoi);
        btnMoi.addActionListener(e -> onNewSale.run());

        footer.add(btnIn);
        // footer.add(btnBack);
        footer.add(btnMoi);
        wrapper.add(footer, BorderLayout.SOUTH);

        add(wrapper, BorderLayout.CENTER);
    }

    private JLabel addReceiptRow(JPanel parent, GridBagConstraints gbc, int row, String label) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.28;
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(MAU_TEXT);
        parent.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.72;
        JLabel val = new JLabel("—");
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        val.setForeground(MAU_TEXT);
        val.setHorizontalAlignment(SwingConstants.RIGHT);
        parent.add(val, gbc);
        return val;
    }

    private void stylePrimary(JButton b) {
        b.setBackground(MAU_CHINH);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
    }

    private void styleSecondary(JButton b) {
        b.setBackground(Color.WHITE);
        b.setForeground(MAU_TEXT);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
    }

    public void updateData(String maHD, String soVe, String kh, String chuyen, String tuyen, String khoiHanh,
            String toaGhe, String gia, String trangThai) {
        lblMaHD.setText(maHD);
        lblSoVe.setText(soVe);
        lblKhachHang.setText(kh);
        lblChuyenTau.setText(chuyen);
        lblTuyen.setText(tuyen);
        lblKhoiHanh.setText(khoiHanh);
        lblToaGhe.setText(toaGhe);
        lblGia.setText(gia);
        lblTrangThai.setText(trangThai);
    }
}
