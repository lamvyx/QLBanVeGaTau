package view;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CalendarDatePicker extends JDialog {
    private static final Color MAU_CHINH = Color.decode("#4682A9");
    private LocalDate selectedDate;
    private LocalDate currentViewDate;
    private final Consumer<LocalDate> onDateSelected;
    private JPanel daysPanel;
    private JComboBox<String> cbMonth;
    private JComboBox<Integer> cbYear;
    private boolean isUpdatingCombos = false;

    public CalendarDatePicker(LocalDate initialDate, Consumer<LocalDate> onDateSelected) {
        super((Frame) null, false);
        setUndecorated(true);
        setAlwaysOnTop(true);

        this.selectedDate = initialDate != null ? initialDate : LocalDate.now();
        this.currentViewDate = selectedDate;
        this.onDateSelected = onDateSelected;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC"), 1));

        add(taoHeader(), BorderLayout.NORTH);
        add(taoCalendarGrid(), BorderLayout.CENTER);
        
        pack();
    }

    private JPanel taoHeader() {
        JPanel header = new JPanel(new BorderLayout(5, 0));
        header.setBackground(MAU_CHINH);
        header.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton btnPrev = createNavButton("<");
        btnPrev.addActionListener(e -> changeMonth(-1));

        JButton btnNext = createNavButton(">");
        btnNext.addActionListener(e -> changeMonth(1));

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        centerPanel.setOpaque(false);

        cbMonth = new JComboBox<>(new String[]{
            "T1", "T2", "T3", "T4", "T5", "T6",
            "T7", "T8", "T9", "T10", "T11", "T12"
        });
        cbMonth.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cbMonth.setPreferredSize(new Dimension(55, 25));

        Integer[] years = new Integer[150];
        int currentYear = LocalDate.now().getYear();
        int startYear = currentYear - 100;
        for (int i = 0; i < 150; i++) {
            years[i] = startYear + i;
        }
        cbYear = new JComboBox<>(years);
        cbYear.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cbYear.setPreferredSize(new Dimension(75, 25));

        ActionListener comboListener = e -> {
            if (isUpdatingCombos) return;
            int m = cbMonth.getSelectedIndex() + 1;
            Integer selectedYear = (Integer) cbYear.getSelectedItem();
            if (selectedYear != null) {
                currentViewDate = LocalDate.of(selectedYear, m, 1);
                updateDaysGrid();
            }
        };
        cbMonth.addActionListener(comboListener);
        cbYear.addActionListener(comboListener);

        updateHeaderLabel();

        centerPanel.add(cbMonth);
        centerPanel.add(cbYear);

        header.add(btnPrev, BorderLayout.WEST);
        header.add(centerPanel, BorderLayout.CENTER);
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
                dispose();
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
        isUpdatingCombos = true;
        if (cbMonth != null) {
            cbMonth.setSelectedIndex(currentViewDate.getMonthValue() - 1);
        }
        if (cbYear != null) {
            cbYear.setSelectedItem(currentViewDate.getYear());
        }
        isUpdatingCombos = false;
    }

    public void showPopup(Component invoker, int x, int y) {
        Point p = invoker.getLocationOnScreen();
        setLocation(p.x + x, p.y + y);
        
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {}

            @Override
            public void windowLostFocus(WindowEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
                    if (activeWindow != CalendarDatePicker.this && (activeWindow == null || activeWindow.getOwner() != CalendarDatePicker.this)) {
                        dispose();
                    }
                });
            }
        });
        
        setVisible(true);
    }
}
