package view;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CalendarDatePicker extends JPopupMenu {
    private static final Color MAU_CHINH = Color.decode("#4682A9");
    private LocalDate selectedDate;
    private LocalDate currentViewDate;
    private final Consumer<LocalDate> onDateSelected;
    private JPanel daysPanel;
    private JLabel lblMonthYear;

    public CalendarDatePicker(LocalDate initialDate, Consumer<LocalDate> onDateSelected) {
        this.selectedDate = initialDate != null ? initialDate : LocalDate.now();
        this.currentViewDate = selectedDate;
        this.onDateSelected = onDateSelected;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));

        add(taoHeader(), BorderLayout.NORTH);
        add(taoCalendarGrid(), BorderLayout.CENTER);
    }

    private JPanel taoHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAU_CHINH);
        header.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton btnPrev = createNavButton("<");
        btnPrev.addActionListener(e -> changeMonth(-1));

        JButton btnNext = createNavButton(">");
        btnNext.addActionListener(e -> changeMonth(1));

        lblMonthYear = new JLabel("", SwingConstants.CENTER);
        lblMonthYear.setForeground(Color.WHITE);
        lblMonthYear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateHeaderLabel();

        header.add(btnPrev, BorderLayout.WEST);
        header.add(lblMonthYear, BorderLayout.CENTER);
        header.add(btnNext, BorderLayout.EAST);

        return header;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(MAU_CHINH);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel taoCalendarGrid() {
        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setBackground(Color.WHITE);

        // Days of week header
        JPanel dowPanel = new JPanel(new GridLayout(1, 7));
        dowPanel.setBackground(Color.decode("#F0F5F9"));
        String[] dows = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
        for (String dow : dows) {
            JLabel lbl = new JLabel(dow, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setPreferredSize(new Dimension(35, 25));
            dowPanel.add(lbl);
        }
        gridWrapper.add(dowPanel, BorderLayout.NORTH);

        daysPanel = new JPanel(new GridLayout(0, 7));
        daysPanel.setBackground(Color.WHITE);
        updateDaysGrid();
        gridWrapper.add(daysPanel, BorderLayout.CENTER);

        return gridWrapper;
    }

    private void updateDaysGrid() {
        daysPanel.removeAll();
        YearMonth ym = YearMonth.from(currentViewDate);
        LocalDate firstOfMonth = ym.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 (Mon) to 7 (Sun)
        
        // Add empty slots for days before first of month
        for (int i = 1; i < dayOfWeek; i++) {
            daysPanel.add(new JLabel(""));
        }

        int daysInMonth = ym.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = ym.atDay(day);
            JButton btn = new JButton(String.valueOf(day));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btn.setPreferredSize(new Dimension(35, 35));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setBackground(Color.WHITE);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (date.equals(selectedDate)) {
                btn.setBackground(MAU_CHINH);
                btn.setForeground(Color.WHITE);
            } else if (date.equals(LocalDate.now())) {
                btn.setForeground(MAU_CHINH);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            }

            btn.addActionListener(e -> {
                selectedDate = date;
                onDateSelected.accept(selectedDate);
                setVisible(false);
            });

            // Hover effect
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!date.equals(selectedDate)) {
                        btn.setBackground(Color.decode("#E4EBF3"));
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!date.equals(selectedDate)) {
                        btn.setBackground(Color.WHITE);
                    }
                }
            });

            daysPanel.add(btn);
        }

        daysPanel.revalidate();
        daysPanel.repaint();
    }

    private void changeMonth(int delta) {
        currentViewDate = currentViewDate.plusMonths(delta);
        updateHeaderLabel();
        updateDaysGrid();
    }

    private void updateHeaderLabel() {
        lblMonthYear.setText("Tháng " + currentViewDate.getMonthValue() + ", " + currentViewDate.getYear());
    }

    public void showPopup(Component invoker, int x, int y) {
        show(invoker, x, y);
    }
}
