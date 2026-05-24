package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class BanVeDatePickerDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final Color MAU_CHINH = Color.decode("#2563EB");
    
    private LocalDate selectedDate;
    private YearMonth viewingMonth;
    private final JPanel calendarGrid;
    private final JLabel lblMonthYear;

    public BanVeDatePickerDialog(JFrame parent, LocalDate initialDate) {
        super(parent, "Chọn ngày đi", true);
        this.selectedDate = initialDate != null ? initialDate : LocalDate.now();
        this.viewingMonth = YearMonth.from(selectedDate);

        setLayout(new BorderLayout());
        setSize(320, 360);
        setLocationRelativeTo(parent);
        setResizable(false);

        // Header: Month/Year navigation
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAU_CHINH);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        lblMonthYear = new JLabel("", SwingConstants.CENTER);
        lblMonthYear.setForeground(Color.WHITE);
        lblMonthYear.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JButton btnPrev = createNavButton(" < ");
        btnPrev.addActionListener(e -> {
            viewingMonth = viewingMonth.minusMonths(1);
            refreshCalendar();
        });

        JButton btnNext = createNavButton(" > ");
        btnNext.addActionListener(e -> {
            viewingMonth = viewingMonth.plusMonths(1);
            refreshCalendar();
        });

        header.add(btnPrev, BorderLayout.WEST);
        header.add(lblMonthYear, BorderLayout.CENTER);
        header.add(btnNext, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Day labels
        JPanel daysHeader = new JPanel(new GridLayout(1, 7));
        daysHeader.setBackground(Color.decode("#F8FAFC"));
        String[] days = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
        for (String d : days) {
            JLabel l = new JLabel(d, SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            l.setForeground(Color.decode("#64748B"));
            l.setBorder(new EmptyBorder(8, 0, 8, 0));
            daysHeader.add(l);
        }
        
        calendarGrid = new JPanel(new GridLayout(0, 7));
        calendarGrid.setBackground(Color.WHITE);

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.add(daysHeader, BorderLayout.NORTH);
        centerWrap.add(calendarGrid, BorderLayout.CENTER);
        add(centerWrap, BorderLayout.CENTER);

        refreshCalendar();
    }

    private JButton createNavButton(String text) {
        JButton b = new JButton(text);
        b.setForeground(Color.WHITE);
        b.setBackground(MAU_CHINH);
        b.setBorder(null);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return b;
    }

    private void refreshCalendar() {
        calendarGrid.removeAll();
        lblMonthYear.setText("Tháng " + viewingMonth.getMonthValue() + ", " + viewingMonth.getYear());

        LocalDate firstOfMonth = viewingMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 (Mon) to 7 (Sun)
        
        // Fill empty slots
        for (int i = 1; i < dayOfWeek; i++) {
            calendarGrid.add(new JLabel(""));
        }

        int daysInMonth = viewingMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();

        for (int day = 1; day <= daysInMonth; day++) {
            JButton btn = new JButton(String.valueOf(day));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setBackground(Color.WHITE);
            
            LocalDate date = viewingMonth.atDay(day);
            
            if (date.isBefore(today)) {
                btn.setForeground(Color.LIGHT_GRAY);
                btn.setEnabled(false);
            } else if (date.equals(selectedDate)) {
                btn.setBackground(MAU_CHINH);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setForeground(Color.decode("#1E293B"));
            }

            btn.addActionListener(e -> {
                selectedDate = date;
                dispose();
            });
            
            calendarGrid.add(btn);
        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }
}
