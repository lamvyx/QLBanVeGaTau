package view;

import controller.ChuyenTauController;
import controller.HoaDonController;
import controller.KhachHangController;
import controller.ToaController;
import entity.ChuyenTau;
import entity.KhachHang;
import entity.Toa;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import service.HoaDonService.KetQuaLapHoaDon;

public class BanVePage extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color MAU_NEN = Color.decode("#F3F6FB");
    private static final Color MAU_TEXT = Color.decode("#35506B");

    private final JPanel contentPanel = new JPanel(new BorderLayout());
    private final JPanel saleCard = new JPanel(new BorderLayout());
    
    private BanVeSuccessPanel successCard;
    private BanVePrintPanel printCard;
    private BanVeSeatPanel seatPanel;
    private BanVeSummaryPanel summaryPanel;
    private BanVeSearchPanel searchPanel;
    private BanVeTripListPanel tripListPanel;
    private JPanel seatCardContainer;

    private final Set<String> selectedSeats = new LinkedHashSet<>();
    private final Set<String> bookedSeats = new LinkedHashSet<>();

    private JLabel lblSeatTitle;
    private JTextField txtMaKM;
    private JComboBox<KhachHangOption> cboKhachHang;
    private JComboBox<ToaOption> cboToaTau;

    private BigDecimal selectedPrice = new BigDecimal("1105000");
    private final HoaDonController hoaDonController = new HoaDonController();
    private final KhachHangController khachHangController = new KhachHangController();
    private final ChuyenTauController chuyenTauController = new ChuyenTauController();
    private final ToaController toaController = new ToaController();
    
    private final List<KhachHangOption> dsKhachHang = new ArrayList<KhachHangOption>();
    private final Map<String, List<ToaOption>> toaTheoChuyen = new HashMap<String, List<ToaOption>>();

    private String selectedMaKH;
    private String selectedMaCT;
    private String selectedMaToa;
    private int currentSeatCount = 32;

    private String selectedKhachHang = "Chưa chọn";
    private String selectedChuyen = "Chưa chọn";
    private String selectedTuyen = "";
    private String selectedKhoiHanh = "";
    private String selectedToa = "Chưa chọn";
    private String selectedTrangThai = "Đang chờ";
    private String generatedMaHD = "";
    private String generatedMaVe = "";
    private boolean daChotGiaoDich;
    private int soVeDaChot;
    private String danhSachGheDaChot = "";
    private BigDecimal tongTienDaChot = BigDecimal.ZERO;
    private String khoiHanhDaChot = "";
    private String khachHangDaChot = "";
    private String chuyenDaChot = "";
    private String tuyenDaChot = "";
    private String toaDaChot = "";

    public BanVePage() {
        this.successCard = new BanVeSuccessPanel(() -> hienThiCard(printCard), this::resetForm);
        this.printCard = new BanVePrintPanel(() -> hienThiCard(successCard), this::resetForm);
        this.seatPanel = new BanVeSeatPanel(selectedSeats, bookedSeats, this::onSelectionChanged);
        this.summaryPanel = new BanVeSummaryPanel(this::confirmSale, this::resetForm);
        this.searchPanel = new BanVeSearchPanel(this::onSearchTrips);
        this.tripListPanel = new BanVeTripListPanel(this::onTripSelected);

        setLayout(new BorderLayout());
        setBackground(MAU_NEN);

        add(taoHeader(), BorderLayout.NORTH);
        add(taoBody(), BorderLayout.CENTER);

        buildSaleCard();
        taiDuLieuBanDau();
        resetForm();
        hienThiCard(saleCard);
    }

    private JPanel taoHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#DDE5F2")));
        header.setPreferredSize(new Dimension(0, 62));

        JLabel title = new JLabel("Bán vé tàu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(MAU_TEXT);
        title.setBorder(new EmptyBorder(0, 16, 0, 0));
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Tra cứu lịch trình và lập hóa đơn chuẩn xác");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(108, 122, 138));
        subtitle.setBorder(new EmptyBorder(0, 0, 0, 16));
        header.add(subtitle, BorderLayout.EAST);
        return header;
    }

    private JPanel taoBody() {
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        return contentPanel;
    }

    private void buildSaleCard() {
        saleCard.setOpaque(false);
        saleCard.setLayout(new BorderLayout(16, 16));

        JPanel left = new JPanel(new BorderLayout(0, 16));
        left.setOpaque(false);

        // North: Search Area
        left.add(searchPanel, BorderLayout.NORTH);

        // Center: Results + Form + Seats
        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(0, 0, 16, 0);
        centerWrap.add(tripListPanel, gbc);

        gbc.gridy = 1;
        centerWrap.add(createBookingInfoPanel(), gbc);

        gbc.gridy = 2; gbc.weighty = 0.5; gbc.fill = GridBagConstraints.BOTH;
        seatCardContainer = taoSeatContainer();
        centerWrap.add(seatCardContainer, gbc);

        JScrollPane leftScroll = new JScrollPane(centerWrap);
        leftScroll.setOpaque(false);
        leftScroll.getViewport().setOpaque(false);
        leftScroll.setBorder(null);
        leftScroll.getVerticalScrollBar().setUnitIncrement(16);

        left.add(leftScroll, BorderLayout.CENTER);

        saleCard.add(left, BorderLayout.CENTER);
        saleCard.add(summaryPanel, BorderLayout.EAST);
    }

    private JPanel createBookingInfoPanel() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(Color.WHITE);
        wrap.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
            new EmptyBorder(14, 14, 14, 14)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabelField(wrap, gbc, 0, 0, "Khách hàng *", createCustomerCombo());
        addLabelField(wrap, gbc, 1, 0, "Mã khuyến mãi", createPromotionField());
        addLabelField(wrap, gbc, 0, 1, "Toa tàu *", createToaCombo());
        
        return wrap;
    }

    private void addLabelField(JPanel p, GridBagConstraints gbc, int col, int row, String label, java.awt.Component field) {
        gbc.gridy = row; gbc.gridx = col * 2;
        gbc.weightx = 0.15;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(MAU_TEXT);
        p.add(l, gbc);

        gbc.gridx = col * 2 + 1;
        gbc.weightx = 0.35;
        p.add(field, gbc);
    }

    private JPanel taoSeatContainer() {
        JPanel wrap = new JPanel(new BorderLayout(0, 12));
        wrap.setBackground(Color.WHITE);
        wrap.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
            new EmptyBorder(14, 14, 14, 14)
        ));
        lblSeatTitle = new JLabel("2. Chọn chỗ ngồi (Vui lòng chọn chuyến & toa trước)");
        lblSeatTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSeatTitle.setForeground(MAU_TEXT);
        wrap.add(lblSeatTitle, BorderLayout.NORTH);
        wrap.add(seatPanel, BorderLayout.CENTER);
        wrap.setVisible(false); // Ẩn mặc định cho đến khi chọn toa
        return wrap;
    }

    private JComboBox<KhachHangOption> createCustomerCombo() {
        cboKhachHang = new JComboBox<>();
        cboKhachHang.addActionListener(e -> {
            KhachHangOption opt = (KhachHangOption) cboKhachHang.getSelectedItem();
            if (opt != null) {
                selectedKhachHang = opt.tenKH;
                selectedMaKH = opt.maKH;
                refreshSummary();
            }
        });
        return cboKhachHang;
    }

    private void onSearchTrips(String gaDi, String gaDen, LocalDate ngayDi) {
        List<ChuyenTau> trips = chuyenTauController.traCuuChuyenTau(gaDi, gaDen, ngayDi);
        tripListPanel.setTrips(trips);
        if (trips.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến tàu nào phù hợp.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onTripSelected(ChuyenTau trip) {
        if (trip != null) {
            selectedMaCT = trip.getMaCT();
            selectedChuyen = trip.getMaCT();
            selectedTuyen = trip.getMaTuyenTau();
            selectedKhoiHanh = trip.getNgayKhoiHanh().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            
            // Tìm toa của tàu này
            taiToaChoTau(trip.getMaTau());
            refreshSummary();
        }
    }

    private void taiToaChoTau(String maTau) {
        cboToaTau.removeAllItems();
        List<ToaOption> ds = toaTheoChuyen.getOrDefault(maTau, Collections.emptyList());
        for (ToaOption t : ds) cboToaTau.addItem(t);
        if (cboToaTau.getItemCount() > 0) cboToaTau.setSelectedIndex(0);
        else {
            selectedMaToa = null; selectedToa = "Chưa chọn";
            bookedSeats.clear(); selectedSeats.clear();
            seatPanel.refreshUI();
        }
    }

    private JComboBox<ToaOption> createToaCombo() {
        cboToaTau = new JComboBox<>();
        cboToaTau.addActionListener(e -> {
            ToaOption opt = (ToaOption) cboToaTau.getSelectedItem();
            if (opt != null) {
                selectedMaToa = opt.maToa;
                selectedToa = opt.maToa + " (" + opt.loaiToa + ")";
                selectedPrice = opt.giaVe;
                currentSeatCount = opt.soGhe;
                selectedSeats.clear();
                capNhatGheDaDatTheoToa();
                seatPanel.setSeatCount(currentSeatCount);
                if (seatCardContainer != null) seatCardContainer.setVisible(true);
                refreshSummary();
            }
        });
        return cboToaTau;
    }

    private JPanel createPromotionField() {
        txtMaKM = new JTextField();
        txtMaKM.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshSummary(); }
            public void removeUpdate(DocumentEvent e) { refreshSummary(); }
            public void changedUpdate(DocumentEvent e) { refreshSummary(); }
        });
        JPanel p = new JPanel(new BorderLayout());
        p.add(txtMaKM, BorderLayout.CENTER);
        return p;
    }

    private void onSelectionChanged() {
        refreshSummary();
    }

    private void refreshSummary() {
        BigDecimal base = selectedPrice.multiply(BigDecimal.valueOf(selectedSeats.size()));
        BigDecimal vat = hoaDonController.tinhThueVAT(base);
        BigDecimal km = hoaDonController.tinhChietKhau(base, txtMaKM.getText().trim());
        BigDecimal tong = hoaDonController.tinhTongThanhToan(base, vat, km);

        String gheStr = selectedSeats.isEmpty() ? "Chưa chọn" : String.join(", ", selectedSeats);
        summaryPanel.updateOrderInfo(selectedKhachHang, selectedChuyen, selectedTuyen, selectedKhoiHanh, selectedToa, gheStr, selectedSeats.size());
        summaryPanel.updatePriceInfo(selectedPrice, vat, km, tong);

        if (lblSeatTitle != null) {
            lblSeatTitle.setText("2. Chọn chỗ ngồi - " + selectedToa);
        }
        refreshSuccessCard();
        revalidate(); repaint();
    }

    private void confirmSale() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một chỗ ngồi.");
            return;
        }
        if (selectedMaKH == null || selectedMaCT == null || selectedMaToa == null) {
            JOptionPane.showMessageDialog(this, "Thiếu thông tin cần thiết.");
            return;
        }

        KetQuaLapHoaDon res = hoaDonController.lapHoaDonBanVe("NV001", selectedMaKH, txtMaKM.getText().trim(),
                selectedMaCT, selectedMaToa, new ArrayList<String>(selectedSeats), selectedPrice);

        if (!res.thanhCong) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + res.thongBao);
            capNhatGheDaDatTheoToa();
            seatPanel.refreshUI();
            return;
        }

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmm"));
        generatedMaHD = res.maHoaDon;
        generatedMaVe = "VE" + ts + selectedSeats.size();
        selectedTrangThai = "Đã lập hóa đơn";

        daChotGiaoDich = true;
        soVeDaChot = selectedSeats.size();
        danhSachGheDaChot = String.join(", ", selectedSeats);
        tongTienDaChot = selectedPrice.multiply(BigDecimal.valueOf(soVeDaChot)); 
        
        khoiHanhDaChot = selectedKhoiHanh;
        khachHangDaChot = selectedKhachHang;
        chuyenDaChot = selectedChuyen;
        tuyenDaChot = selectedTuyen;
        toaDaChot = selectedToa;

        capNhatGheDaDatTheoToa();
        selectedSeats.clear();
        seatPanel.refreshUI();
        refreshSuccessCard();
        hienThiCard(successCard);
    }

    private void refreshSuccessCard() {
        String ghe = daChotGiaoDich ? danhSachGheDaChot : (selectedSeats.isEmpty() ? "—" : String.join(", ", selectedSeats));
        BigDecimal tong = daChotGiaoDich ? tongTienDaChot : selectedPrice.multiply(BigDecimal.valueOf(selectedSeats.size()));
        
        successCard.setData(generatedMaVe, BanVeUtils.formatMoney(tong), 
                daChotGiaoDich ? khachHangDaChot : selectedKhachHang,
                daChotGiaoDich ? tuyenDaChot : selectedTuyen,
                daChotGiaoDich ? khoiHanhDaChot : selectedKhoiHanh,
                (daChotGiaoDich ? toaDaChot : selectedToa) + " · " + ghe);
        
        printCard.updateData(generatedMaHD, (daChotGiaoDich ? soVeDaChot : selectedSeats.size()) + " vé",
                daChotGiaoDich ? khachHangDaChot : selectedKhachHang,
                daChotGiaoDich ? chuyenDaChot : selectedChuyen,
                daChotGiaoDich ? tuyenDaChot : selectedTuyen,
                daChotGiaoDich ? khoiHanhDaChot : selectedKhoiHanh,
                (daChotGiaoDich ? toaDaChot : selectedToa) + " · " + ghe,
                BanVeUtils.formatMoney(tong), selectedTrangThai);
    }

    private void resetForm() {
        selectedSeats.clear();
        daChotGiaoDich = false;
        if (cboKhachHang != null && cboKhachHang.getItemCount() > 0) cboKhachHang.setSelectedIndex(0);
        if (cboToaTau != null) cboToaTau.removeAllItems();
        if (txtMaKM != null) txtMaKM.setText("");
        if (tripListPanel != null) tripListPanel.setTrips(new ArrayList<ChuyenTau>());
        if (seatCardContainer != null) seatCardContainer.setVisible(false);
        selectedTrangThai = "Đang chờ";
        capNhatGheDaDatTheoToa();
        seatPanel.refreshUI();
        refreshSummary();
    }

    private void taiDuLieuBanDau() {
        dsKhachHang.clear();
        for (KhachHang kh : khachHangController.layTatCaKhachHang()) {
            dsKhachHang.add(new KhachHangOption(kh.getMaKH(), kh.getTenKH(), kh.getSdt()));
        }
        cboKhachHang.removeAllItems();
        for (KhachHangOption o : dsKhachHang) cboKhachHang.addItem(o);

        toaTheoChuyen.clear();
        for (Toa toa : toaController.timKiemToa(null)) {
            ToaOption opt = new ToaOption(toa.getMaToa(), toa.getLoaiToa(), toa.getSoGhe(),
                    BanVeUtils.xacDinhGiaVeTheoLoaiToa(toa.getLoaiToa()));
            toaTheoChuyen.computeIfAbsent(toa.getMaTau(), k -> new ArrayList<ToaOption>()).add(opt);
        }
    }

    private void capNhatGheDaDatTheoToa() {
        bookedSeats.clear();
        if (selectedMaCT == null || selectedMaToa == null) return;
        Set<String> daDat = hoaDonController.layGheDaDat(selectedMaCT, selectedMaToa);
        for (String g : daDat) bookedSeats.add(BanVeUtils.chuanHoaMaGhe(g));
    }

    private void hienThiCard(JPanel card) {
        contentPanel.removeAll();
        contentPanel.add(card, BorderLayout.CENTER);
        contentPanel.revalidate(); contentPanel.repaint();
    }
}
