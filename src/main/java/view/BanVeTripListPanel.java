package view;

import entity.ChuyenTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class BanVeTripListPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color MAU_CHINH = Color.decode("#2563EB");
    private static final Color MAU_TEXT = Color.decode("#35506B");

    private JList<ChuyenTau> listTrips;
    private DefaultListModel<ChuyenTau> listModel;
    private final TripSelectListener listener;

    public interface TripSelectListener {
        void onTripSelected(ChuyenTau trip);
    }

    public BanVeTripListPanel(TripSelectListener listener) {
        this.listener = listener;
        setLayout(new BorderLayout(0, 12));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("Danh sách chuyến tàu phù hợp");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(MAU_TEXT);
        add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listTrips = new JList<>(listModel);
        listTrips.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listTrips.setCellRenderer(new TripRenderer());
        listTrips.setFixedCellHeight(60);
        listTrips.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ChuyenTau selected = listTrips.getSelectedValue();
                if (selected != null && this.listener != null) {
                    this.listener.onTripSelected(selected);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(listTrips);
        scroll.setBorder(BorderFactory.createLineBorder(Color.decode("#E5E7EB")));
        scroll.setPreferredSize(new Dimension(0, 200));
        add(scroll, BorderLayout.CENTER);
    }

    public void setTrips(List<ChuyenTau> trips) {
        listModel.clear();
        if (trips != null) {
            for (ChuyenTau t : trips) {
                listModel.addElement(t);
            }
        }
        if (listModel.isEmpty()) {
            listTrips.setToolTipText("Không có chuyến tàu nào phù hợp");
        }
    }

    private static class TripRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JPanel panel = new JPanel(new BorderLayout(10, 0));
            panel.setBorder(new EmptyBorder(8, 12, 8, 12));
            panel.setBackground(isSelected ? Color.decode("#EFF6FF") : Color.WHITE);
            
            if (value instanceof ChuyenTau) {
                ChuyenTau ct = (ChuyenTau) value;
                
                JLabel lblMa = new JLabel(ct.getMaCT());
                lblMa.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblMa.setForeground(MAU_CHINH);
                panel.add(lblMa, BorderLayout.WEST);
                
                String time = ct.getNgayKhoiHanh().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
                JLabel lblInfo = new JLabel("Khởi hành: " + time + " - Tàu: " + ct.getMaTau());
                lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                lblInfo.setForeground(MAU_TEXT);
                panel.add(lblInfo, BorderLayout.CENTER);
                
                JLabel lblTuyen = new JLabel("Tuyến: " + ct.getMaTuyenTau());
                lblTuyen.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                lblTuyen.setForeground(new Color(100, 116, 139));
                panel.add(lblTuyen, BorderLayout.EAST);
            }
            
            return panel;
        }
    }
}
