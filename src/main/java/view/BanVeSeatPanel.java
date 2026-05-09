package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BanVeSeatPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color MAU_CHINH = Color.decode("#2563EB");
    private static final Color MAU_XANH = Color.decode("#22C55E");
    private static final Color MAU_DO = Color.decode("#EF4444");
    private static final Color MAU_TEXT = Color.decode("#35506B");

    private final List<JButton> seatButtons = new ArrayList<>();
    private final Runnable onSelectionChange;
    private final Set<String> selectedSeats;
    private final Set<String> bookedSeats;
    private int currentSeatCount = 32;

    public BanVeSeatPanel(Set<String> selectedSeats, Set<String> bookedSeats, Runnable onSelectionChange) {
        this.selectedSeats = selectedSeats;
        this.bookedSeats = bookedSeats;
        this.onSelectionChange = onSelectionChange;
        setLayout(new BorderLayout(0, 12));
        setOpaque(false);
        initUI();
    }

    private void initUI() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        legend.setOpaque(false);
        legend.add(createLegend(MAU_XANH, "Trống"));
        legend.add(createLegend(MAU_DO, "Đã đặt"));
        legend.add(createLegend(MAU_CHINH, "Đang chọn"));
        add(legend, BorderLayout.NORTH);

        add(createSeatGridWrapper(), BorderLayout.CENTER);
    }

    private JPanel createLegend(Color color, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setOpaque(false);
        JLabel dot = new JLabel("●");
        dot.setForeground(color);
        dot.setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(dot);
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(MAU_TEXT);
        p.add(lbl);
        return p;
    }

    private JPanel createSeatGridWrapper() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setPreferredSize(new Dimension(0, 320));
        refreshGrid(wrap);
        return wrap;
    }

    public void setSeatCount(int count) {
        this.currentSeatCount = count;
        refreshUI();
    }

    public void refreshUI() {
        removeAll();
        initUI();
        revalidate();
        repaint();
    }

    private void refreshGrid(JPanel container) {
        container.removeAll();
        int cols = 4;
        int rows = Math.max(1, (int) Math.ceil((double) currentSeatCount / cols));
        JPanel seats = new JPanel(new GridLayout(rows, cols, 8, 8));
        seats.setOpaque(false);
        seatButtons.clear();

        for (int i = 0; i < currentSeatCount; i++) {
            String seatCode = generateSeatCode(i);
            JButton seat = new JButton(seatCode);
            seat.setActionCommand(seatCode);
            seat.setPreferredSize(new Dimension(76, 42));
            seat.setFocusPainted(false);
            seat.setFont(new Font("Segoe UI", Font.BOLD, 12));
            seat.setCursor(new Cursor(Cursor.HAND_CURSOR));
            seat.addActionListener(e -> toggleSeat(seatCode));
            seatButtons.add(seat);
            seats.add(seat);
        }
        applyButtonStyles();
        
        JScrollPane scroll = new JScrollPane(seats, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        container.add(scroll, BorderLayout.CENTER);
    }

    private String generateSeatCode(int index) {
        char cot = (char) ('A' + (index % 4));
        int hang = (index / 4) + 1;
        return cot + String.format("%02d", hang);
    }

    private void toggleSeat(String code) {
        if (bookedSeats.contains(BanVeUtils.chuanHoaMaGhe(code))) return;
        if (selectedSeats.contains(code)) {
            selectedSeats.remove(code);
        } else {
            selectedSeats.add(code);
        }
        applyButtonStyles();
        if (onSelectionChange != null) onSelectionChange.run();
    }

    public void applyButtonStyles() {
        for (JButton btn : seatButtons) {
            String code = BanVeUtils.chuanHoaMaGhe(btn.getActionCommand());
            if (bookedSeats.contains(code)) {
                btn.setBackground(new Color(255, 240, 240));
                btn.setForeground(MAU_DO);
                btn.setBorder(BorderFactory.createLineBorder(MAU_DO, 2));
            } else if (selectedSeats.contains(btn.getActionCommand())) {
                btn.setBackground(MAU_CHINH);
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createLineBorder(MAU_CHINH.darker(), 2));
            } else {
                btn.setBackground(Color.WHITE);
                btn.setForeground(MAU_TEXT);
                btn.setBorder(BorderFactory.createLineBorder(MAU_XANH, 2));
            }
        }
    }
}
