package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import entity.TuyenTau;
import dao.TuyenTau_DAO;

public class BanVeSearchPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color MAU_CHINH = Color.decode("#2563EB");
    private static final Color MAU_TEXT = Color.decode("#35506B");

    private JComboBox<String> cboGaDi;
    private JComboBox<String> cboGaDen;
    private JButton btnChonNgay;
    private LocalDate ngayDangChon;
    private JButton btnTraCuu;
    
    private final SearchListener listener;

    public interface SearchListener {
        void onSearch(String gaDi, String gaDen, LocalDate ngayDi);
    }

    public BanVeSearchPanel(SearchListener listener) {
        this.listener = listener;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JPanel inputs = new JPanel(new GridBagLayout());
        inputs.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ga đi
        gbc.gridx = 0; gbc.weightx = 0.3;
        inputs.add(createInputGroup("Ga đi", cboGaDi = new JComboBox<>()), gbc);

        // Ga đến
        gbc.gridx = 1; gbc.weightx = 0.3;
        inputs.add(createInputGroup("Ga đến", cboGaDen = new JComboBox<>()), gbc);

        // Ngày đi
        gbc.gridx = 2; gbc.weightx = 0.2;
        ngayDangChon = LocalDate.now();
        btnChonNgay = new JButton(ngayDangChon.toString());
        btnChonNgay.setBackground(Color.WHITE);
        btnChonNgay.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChonNgay.addActionListener(e -> {
            javax.swing.JFrame parent = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
            BanVeDatePickerDialog dialog = new BanVeDatePickerDialog(parent, ngayDangChon);
            dialog.setVisible(true);
            LocalDate res = dialog.getSelectedDate();
            if (res != null) {
                ngayDangChon = res;
                btnChonNgay.setText(ngayDangChon.toString());
            }
        });
        
        JButton btnXoaNgay = new JButton("X");
        btnXoaNgay.setToolTipText("Xem tất cả các ngày");
        btnXoaNgay.setFocusPainted(false);
        btnXoaNgay.addActionListener(e -> {
            ngayDangChon = null;
            btnChonNgay.setText("Tất cả các ngày");
        });

        JPanel dateWrap = new JPanel(new BorderLayout(4, 0));
        dateWrap.setOpaque(false);
        dateWrap.add(btnChonNgay, BorderLayout.CENTER);
        dateWrap.add(btnXoaNgay, BorderLayout.EAST);
        inputs.add(createInputGroup("Ngày đi", dateWrap), gbc);

        // Nút tra cứu
        gbc.gridx = 3; gbc.weightx = 0.2;
        btnTraCuu = new JButton("Tra cứu chuyến tàu");
        btnTraCuu.setBackground(MAU_CHINH);
        btnTraCuu.setForeground(Color.WHITE);
        btnTraCuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTraCuu.setPreferredSize(new Dimension(180, 40));
        btnTraCuu.setFocusPainted(false);
        btnTraCuu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTraCuu.addActionListener(e -> performSearch());
        
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 24));
        btnWrap.setOpaque(false);
        btnWrap.add(btnTraCuu);
        inputs.add(btnWrap, gbc);

        add(inputs, BorderLayout.CENTER);
        
        taiDanhSachGa();
    }

    private JPanel createInputGroup(String label, java.awt.Component field) {
        JPanel group = new JPanel(new BorderLayout(0, 6));
        group.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(MAU_TEXT);
        group.add(lbl, BorderLayout.NORTH);
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (field instanceof JTextField) {
            field.setPreferredSize(new Dimension(0, 32));
        } else if (field instanceof JComboBox) {
            field.setPreferredSize(new Dimension(0, 32));
        } else if (field instanceof JPanel) {
            field.setPreferredSize(new Dimension(0, 32));
        }
        group.add(field, BorderLayout.CENTER);
        return group;
    }

    private void taiDanhSachGa() {
        TuyenTau_DAO dao = new TuyenTau_DAO();
        List<TuyenTau> dsTuyen = dao.layTatCaTuyenTau();
        Set<String> setGa = new HashSet<>();
        for (TuyenTau tt : dsTuyen) {
            setGa.add(tt.getMaGaDi());
            setGa.add(tt.getMaGaDen());
        }
        List<String> sortedGa = new ArrayList<>(setGa);
        Collections.sort(sortedGa);
        
        cboGaDi.removeAllItems();
        cboGaDen.removeAllItems();
        cboGaDi.addItem("-- Chọn ga đi --");
        cboGaDen.addItem("-- Chọn ga đến --");
        for (String ga : sortedGa) {
            cboGaDi.addItem(ga);
            cboGaDen.addItem(ga);
        }
    }

    private void performSearch() {
        String gaDi = (String) cboGaDi.getSelectedItem();
        String gaDen = (String) cboGaDen.getSelectedItem();
        if (gaDi != null && gaDi.startsWith("--")) gaDi = null;
        if (gaDen != null && gaDen.startsWith("--")) gaDen = null;
        
        if (ngayDangChon != null && ngayDangChon.isBefore(LocalDate.now())) {
            javax.swing.JOptionPane.showMessageDialog(this, "Không thể chọn ngày đi trước ngày hôm nay.", "Ngày không hợp lệ", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (listener != null) {
            listener.onSearch(gaDi, gaDen, ngayDangChon);
        }
    }
}
