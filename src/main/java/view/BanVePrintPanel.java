package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class BanVePrintPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color MAU_CHINH = Color.decode("#2563EB");
    private static final Color MAU_TEXT = Color.decode("#35506B");

    private JLabel lblMaHD, lblNguoiLap, lblSoVe, lblKhachHang, lblChuyenTau, lblTuyen, lblKhoiHanh, lblToaGhe, lblGia, lblTrangThai;
    private JLabel lblLoadingMsg;
    private Runnable onNewSale;
    private java.util.function.Consumer<String> onPayment;
    private JButton btnMoi, btnIn, btnThanhToan;
    private List<entity.VeTau> danhSachVe = new java.util.ArrayList<>();

    public BanVePrintPanel(Runnable onNewSale, java.util.function.Consumer<String> onPayment) {
        this.onNewSale = onNewSale;
        this.onPayment = onPayment;
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
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblMaHD = addReceiptRow(receipt, gbc, 0, "Mã hóa đơn");
        lblNguoiLap = addReceiptRow(receipt, gbc, 1, "Người lập");
        lblSoVe = addReceiptRow(receipt, gbc, 2, "Số vé");
        lblKhachHang = addReceiptRow(receipt, gbc, 3, "Khách hàng");
        lblChuyenTau = addReceiptRow(receipt, gbc, 4, "Chuyến tàu");
        lblTuyen = addReceiptRow(receipt, gbc, 5, "Tuyến");
        lblKhoiHanh = addReceiptRow(receipt, gbc, 6, "Khởi hành");
        lblToaGhe = addReceiptRow(receipt, gbc, 7, "Toa / Ghế");
        lblGia = addReceiptRow(receipt, gbc, 8, "Giá vé");
        lblTrangThai = addReceiptRow(receipt, gbc, 9, "Trạng thái");
        wrapper.add(receipt, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        footer.setOpaque(false);

        btnMoi = new JButton("Bán vé mới");
        styleSecondary(btnMoi);
        btnMoi.addActionListener(e -> onNewSale.run());
 
        btnThanhToan = new JButton("Thanh toán");
        stylePrimary(btnThanhToan);
        btnThanhToan.setBackground(Color.decode("#10B981")); // Emerald green
        btnThanhToan.setBorder(BorderFactory.createLineBorder(Color.decode("#10B981"), 1));
        btnThanhToan.addActionListener(e -> onPayment.accept(lblMaHD.getText()));

        btnIn = new JButton("In vé");
        stylePrimary(btnIn);
        btnIn.addActionListener(e -> thucHienInVe());

        footer.add(btnMoi);
        footer.add(btnThanhToan);
        footer.add(btnIn);
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
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(145, 40));
    }

    private void styleSecondary(JButton b) {
        b.setBackground(Color.WHITE);
        b.setForeground(MAU_TEXT);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(145, 40));
    }

    public void updateData(String maHD, String nguoiLap, String soVe, String kh, String chuyen, String tuyen, String khoiHanh,
            String toaGhe, String gia, String trangThai, List<entity.VeTau> dsVe) {
        this.danhSachVe = dsVe != null ? dsVe : new java.util.ArrayList<>();
        lblMaHD.setText(maHD);
        lblNguoiLap.setText(nguoiLap);
        lblSoVe.setText(soVe);
        lblKhachHang.setText(kh);
        lblChuyenTau.setText(chuyen);
        lblTuyen.setText(tuyen);
        lblKhoiHanh.setText(khoiHanh);
        lblToaGhe.setText(toaGhe);
        lblGia.setText(gia);
        capNhatTrangThaiUI(trangThai);
    }

    public void capNhatTrangThaiUI(String trangThai) {
        lblTrangThai.setText(trangThai);
        if ("Đã thanh toán".equals(trangThai)) {
            btnIn.setEnabled(true);
            btnThanhToan.setEnabled(false);
            btnThanhToan.setVisible(false);
        } else {
            btnIn.setEnabled(false);
            btnThanhToan.setEnabled(true);
            btnThanhToan.setVisible(true);
        }
    }

    private JDialog createLoadingDialog(String message) {
        java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.getRootPane().setOpaque(false);
        
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#2563EB"), 2),
            new EmptyBorder(24, 32, 24, 32)
        ));
        
        lblLoadingMsg = new JLabel(message, SwingConstants.CENTER);
        lblLoadingMsg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLoadingMsg.setForeground(MAU_TEXT);
        panel.add(lblLoadingMsg, BorderLayout.NORTH);
        
        JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        progress.setForeground(MAU_CHINH);
        progress.setPreferredSize(new Dimension(240, 6));
        progress.setBorder(BorderFactory.createEmptyBorder());
        panel.add(progress, BorderLayout.CENTER);
        
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parentWindow);
        return dialog;
    }

    private void thucHienInVe() {
        btnIn.setEnabled(false);
        btnMoi.setEnabled(false);
        
        final JDialog loadingDialog = createLoadingDialog("Đang kết nối máy in, vui lòng chờ...");
        
        new Thread(() -> {
            try {
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setJobName("VeTau_" + lblMaHD.getText());

                job.setPrintable(new Printable() {
                    @Override
                    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                        int totalPages = danhSachVe.isEmpty() ? 1 : danhSachVe.size();
                        if (pageIndex >= totalPages) {
                            return NO_SUCH_PAGE;
                        }

                        Graphics2D g2d = (Graphics2D) graphics;
                        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                        
                        g2d.setColor(Color.BLACK);
                        int pageWidth = (int) pageFormat.getImageableWidth();
                        int cardWidth = Math.min(300, pageWidth - 20);
                        int x = Math.max(10, (pageWidth - cardWidth) / 2);
                        int y = 28;
                        
                        // Trích xuất vé hiện tại
                        entity.VeTau ve = null;
                        if (!danhSachVe.isEmpty()) {
                            ve = danhSachVe.get(pageIndex);
                        }

                        String ticketId = (ve != null) ? ve.getMaVeTau() : lblSoVe.getText();
                        String ticketPrice = (ve != null) ? BanVeUtils.formatMoney(ve.getGiaVe()) : lblGia.getText();
                        
                        // Trích xuất Toa / Ghế
                        String toaStr = "—";
                        String gheStr = "—";
                        if (ve != null) {
                            toaStr = ve.getMaToa();
                            gheStr = ve.getViTriGhe();
                            if (toaStr != null && toaStr.contains("(")) {
                                toaStr = toaStr.substring(0, toaStr.indexOf("(")).trim();
                            }
                        } else {
                            String toaGhe = lblToaGhe.getText();
                            if (toaGhe != null && toaGhe.contains("·")) {
                                String[] parts = toaGhe.split("·");
                                if (parts.length >= 2) {
                                    toaStr = parts[0].trim();
                                    gheStr = parts[1].trim();
                                    if (toaStr.contains("(")) {
                                        toaStr = toaStr.substring(0, toaStr.indexOf("(")).trim();
                                    }
                                }
                            }
                        }

                        // Trích xuất Phân loại/Class
                        String classStr = "Ngồi mềm";
                        String toaGheVal = lblToaGhe.getText();
                        if (toaGheVal != null && toaGheVal.contains("(")) {
                            int start = toaGheVal.indexOf("(");
                            int end = toaGheVal.indexOf(")");
                            if (start != -1 && end != -1 && end > start) {
                                classStr = toaGheVal.substring(start + 1, end).trim();
                            }
                        }
                        
                        // 1. Header Title
                        g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
                        drawCentered(g2d, "CÔNG TY CỔ PHẦN VẬN TẢI", x, cardWidth, y);
                        y += 15;
                        drawCentered(g2d, "ĐƯỜNG SẮT SÀI GÒN", x, cardWidth, y);
                        y += 20;
                        g2d.setFont(new Font("Segoe UI", Font.BOLD, 13));
                        drawCentered(g2d, "THẺ LÊN TÀU HỎA / BOARDING PASS", x, cardWidth, y);
                        y += 12;
                        
                        // 2. Barcode Simulation (được tạo độc nhất từ hash của ticketId)
                        int barcodeWidth = 140;
                        int barcodeX = x + (cardWidth - barcodeWidth) / 2;
                        int barcodeY = y;
                        int barcodeHeight = 34;
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(barcodeX, barcodeY, barcodeWidth, barcodeHeight);
                        g2d.setColor(Color.BLACK);
                        
                        int hash = ticketId.hashCode();
                        int curX = barcodeX + 8;
                        for (int i = 0; i < 25; i++) {
                            int w = ((hash >> (i % 32)) & 3) + 1;
                            if (i % 2 == 0) {
                                g2d.fillRect(curX, barcodeY, w, barcodeHeight);
                            }
                            curX += w + 2;
                        }
                        y += barcodeHeight + 14;
                        
                        // 3. Ticket ID
                        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                        drawCentered(g2d, "Mã vé/TicketID: " + ticketId + " (" + lblMaHD.getText() + ")", x, cardWidth, y);
                        y += 18;
                        
                        // Parsing Stations
                        String tuyen = lblTuyen.getText();
                        String gaDi = "Sài Gòn";
                        String gaDen = "Hà Nội";
                        if (tuyen != null && tuyen.contains("-")) {
                            String[] parts = tuyen.split("-");
                            if (parts.length >= 2) {
                                gaDi = parts[0].trim();
                                gaDen = parts[1].trim();
                            }
                        }
                        
                        g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
                        g2d.drawString("Ga đi / Depart: " + gaDi, x, y);
                        g2d.drawString("Ga đến / Arrive: " + gaDen, x + 150, y);
                        y += 18;
                        
                        // Draw separator
                        g2d.setStroke(new java.awt.BasicStroke(0.5f));
                        g2d.drawLine(x, y - 6, x + cardWidth, y - 6);
                        
                        // 4. Details
                        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                        
                        String departure = lblKhoiHanh.getText();
                        String dateStr = "—";
                        String timeStr = "—";
                        if (departure != null && !departure.equals("—")) {
                            String[] parts = departure.split(" ");
                            if (parts.length >= 2) {
                                dateStr = parts[0];
                                timeStr = parts[1];
                                if (dateStr.contains("-")) {
                                    String[] dp = dateStr.split("-");
                                    if (dp.length == 3) {
                                        dateStr = dp[2] + "/" + dp[1] + "/" + dp[0];
                                    }
                                }
                            } else {
                                dateStr = departure;
                            }
                        }
                        
                        y = drawKeyValue(g2d, x, y, cardWidth, "Tàu/Train", lblChuyenTau.getText());
                        
                        y = drawKeyValue(g2d, x, y, cardWidth, "Ngày đi/Date", dateStr);
                        y = drawKeyValue(g2d, x, y, cardWidth, "Giờ đi/Time", timeStr);
                        y = drawKeyValue(g2d, x, y, cardWidth, "Toa/Coach", toaStr + "    Chỗ/Seat: " + gheStr);
                        y = drawKeyValue(g2d, x, y, cardWidth, "Loại chỗ/Class", classStr);
                        y = drawKeyValue(g2d, x, y, cardWidth, "Loại vé/Ticket", "Người lớn / Adult");
                        y = drawKeyValue(g2d, x, y, cardWidth, "Họ tên/Name", lblKhachHang.getText());
                        y = drawKeyValue(g2d, x, y, cardWidth, "Giấy tờ/Passport", "—");
                        y = drawKeyValue(g2d, x, y, cardWidth, "Giá vé/Price", ticketPrice);
                        
                        y += 18;
                        g2d.drawLine(x, y - 5, x + cardWidth, y - 5);
                        
                        // 5. Footer
                        g2d.setFont(new Font("Segoe UI", Font.ITALIC, 7));
                        drawCentered(g2d, "Ghi chú: Để tra cứu và nhận hóa đơn điện tử", x, cardWidth, y);
                        y += 10;
                        drawCentered(g2d, "xin vui lòng truy cập địa chỉ web", x, cardWidth, y);
                        y += 10;
                        drawCentered(g2d, "To receive electronic invoice please visit website address", x, cardWidth, y);
                        y += 10;
                        drawCentered(g2d, "at: http://hoadon.duongsatsaigon.vn", x, cardWidth, y);
                        y += 12;
                        g2d.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 7));
                        drawCentered(g2d, "Thẻ này không có giá trị thanh toán.", x, cardWidth, y);
                        y += 10;
                        drawCentered(g2d, "This boarding pass is not an official invoice.", x, cardWidth, y);
                        y += 12;
                        
                        String printedDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 7));
                        drawCentered(g2d, "ALT-11835402 Ngày in/Printed date: " + printedDate, x, cardWidth, y);
                        
                        return PAGE_EXISTS;
                    }
                });

                SwingUtilities.invokeLater(() -> loadingDialog.setVisible(false));

                boolean accept = job.printDialog();
                if (accept) {
                    SwingUtilities.invokeLater(() -> {
                        lblLoadingMsg.setText("Đang thực hiện in vé, vui lòng chờ...");
                        loadingDialog.setVisible(true);
                    });
                    
                    job.print();
                    
                    SwingUtilities.invokeLater(() -> {
                        loadingDialog.dispose();
                        JOptionPane.showMessageDialog(this, "Yêu cầu in đã được gửi đến máy in thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    });
                }
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    loadingDialog.dispose();
                    JOptionPane.showMessageDialog(this, "Lỗi in ấn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                });
            } finally {
                SwingUtilities.invokeLater(() -> {
                    loadingDialog.dispose();
                    btnIn.setEnabled(true);
                    btnMoi.setEnabled(true);
                    btnIn.setText("In vé");
                });
            }
        }).start();

        loadingDialog.setVisible(true);
    }

    private void drawCentered(Graphics2D g2d, String text, int x, int width, int y) {
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        g2d.drawString(text, x + Math.max(0, (width - textWidth) / 2), y);
    }

    private int drawKeyValue(Graphics2D g2d, int x, int y, int width, String key, String value) {
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        g2d.drawString(key + ":", x, y);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 9));
        String safeValue = value == null || value.isBlank() ? "—" : value;
        int valueWidth = g2d.getFontMetrics().stringWidth(safeValue);
        g2d.drawString(safeValue, x + width - valueWidth, y);
        return y + 15;
    }
}
