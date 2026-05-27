package view;

import entity.ChuyenTau;
import entity.PhieuDatVeInfo;
import java.awt.BorderLayout;
import service.BanVeService;
import service.BanVeService.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
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
    private JComboBox<KhuyenMaiOption> cboKhuyenMai;
    private JComboBox<KhachHangOption> cboKhachHang;
    private JComboBox<ToaOption> cboToaTau;
    private JPanel dichVuListPanel;

    private BigDecimal selectedPrice = new BigDecimal("1105000");
    private final BanVeService banVeService = new BanVeService();
    private final List<ServiceSelectionRow> serviceRows = new ArrayList<>();

    private final Map<String, List<ToaOption>> toaTheoChuyen = new HashMap<>();

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
    private PhieuDatVeInfo phieuDatDaNap;
    private final Set<String> gheCuaPhieuDat = new LinkedHashSet<>();

    public BanVePage() {
        this(null);
    }

    public BanVePage(PhieuDatVeInfo phieuDatVeInfo) {
        this.successCard = new BanVeSuccessPanel(() -> hienThiCard(printCard), this::startNewSale);
        this.printCard = new BanVePrintPanel(this::startNewSale, this::handlePayment);
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
        if (phieuDatVeInfo != null) {
            apDungPhieuDat(phieuDatVeInfo);
        }
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

    // Chỗ lắp ráp các Panel con
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 16, 0);
        centerWrap.add(tripListPanel, gbc);

        gbc.gridy = 1;
        centerWrap.add(createBookingInfoPanel(), gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.0;
        centerWrap.add(createServicePanel(), gbc);

        gbc.gridy = 3;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
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
                new EmptyBorder(14, 14, 14, 14)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabelField(wrap, gbc, 0, 0, "Khách hàng *", createCustomerField());
        addLabelField(wrap, gbc, 1, 0, "Mã khuyến mãi", createPromotionField());
        addLabelField(wrap, gbc, 0, 1, "Toa tàu *", createToaCombo());

        return wrap;
    }

    private void addLabelField(JPanel p, GridBagConstraints gbc, int col, int row, String label,
            java.awt.Component field) {
        gbc.gridy = row;
        gbc.gridx = col * 2;
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
                new EmptyBorder(14, 14, 14, 14)));
        lblSeatTitle = new JLabel("2. Chọn chỗ ngồi (Vui lòng chọn chuyến & toa trước)");
        lblSeatTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSeatTitle.setForeground(MAU_TEXT);
        wrap.add(lblSeatTitle, BorderLayout.NORTH);
        wrap.add(seatPanel, BorderLayout.CENTER);
        wrap.setVisible(false); // Ẩn mặc định cho đến khi chọn toa
        return wrap;
    }

    private JPanel createServicePanel() {
        JPanel wrap = new JPanel(new BorderLayout(0, 12));
        wrap.setBackground(Color.WHITE);
        wrap.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
                new EmptyBorder(14, 14, 14, 14)));

        JLabel title = new JLabel("Dịch vụ kèm theo");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(MAU_TEXT);
        wrap.add(title, BorderLayout.NORTH);

        dichVuListPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        dichVuListPanel.setOpaque(false);
        wrap.add(dichVuListPanel, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel createCustomerField() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.add(createCustomerCombo(), BorderLayout.CENTER);

        JPanel actions = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);

        JButton btnTimNhanh = taoNutHanhDongKhachHang("Tìm nhanh", false);
        btnTimNhanh.addActionListener(e -> moTimKhachHangNhanh());
        actions.add(btnTimNhanh);

        JButton btnThemKhach = taoNutHanhDongKhachHang("Thêm khách hàng", true);
        btnThemKhach.addActionListener(e -> moTrangThemKhachHang());
        actions.add(btnThemKhach);

        panel.add(actions, BorderLayout.SOUTH);
        return panel;
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

    private JButton taoNutHanhDongKhachHang(String text, boolean primary) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(primary ? 144 : 118, 34));

        if (primary) {
            button.setBackground(Color.decode("#2A5ACB"));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.decode("#2148A5")),
                    new EmptyBorder(6, 14, 6, 14)));
        } else {
            button.setBackground(Color.decode("#EAF2FF"));
            button.setForeground(Color.decode("#1E4FBF"));
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.decode("#8EB3F4")),
                    new EmptyBorder(6, 14, 6, 14)));
        }
        return button;
    }

    private void moTimKhachHangNhanh() {
        JTextField txtTuKhoa = new JTextField();
        txtTuKhoa.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        Object[] message = {
                "Nhập số điện thoại hoặc CCCD:",
                txtTuKhoa
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Tìm nhanh khách hàng",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String tuKhoa = txtTuKhoa.getText();
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại hoặc CCCD.", "Thiếu dữ liệu",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        KhachHangOption kh = banVeService.findCustomerByPhoneOrCccd(tuKhoa);
        if (kh == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng theo SĐT/CCCD đã nhập.", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        chonKhachHang(kh.maKH);
    }

    private void moTrangThemKhachHang() {
        JFrame frame = new JFrame("Thêm khách hàng");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(new KhachHangThemPage());
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(this);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                taiLaiDanhSachKhachHang();
            }
        });
        frame.setVisible(true);
    }

    private void taiLaiDanhSachKhachHang() {
        List<KhachHangOption> customers = banVeService.getCustomerOptions();
        cboKhachHang.removeAllItems();
        for (KhachHangOption option : customers) {
            cboKhachHang.addItem(option);
        }
        if (selectedMaKH != null && chonKhachHang(selectedMaKH)) {
            return;
        }
        if (cboKhachHang.getItemCount() > 0) {
            cboKhachHang.setSelectedIndex(0);
        }
    }

    private boolean chonKhachHang(String maKH) {
        if (maKH == null || cboKhachHang == null) {
            return false;
        }
        for (int i = 0; i < cboKhachHang.getItemCount(); i++) {
            KhachHangOption option = cboKhachHang.getItemAt(i);
            if (option != null && maKH.equals(option.maKH)) {
                cboKhachHang.setSelectedIndex(i);
                return true;
            }
        }
        return false;
    }

    private void onSearchTrips(String gaDi, String gaDen, java.time.LocalDate ngayDi) {
        List<ChuyenTau> trips = banVeService.searchTrips(gaDi, gaDen, ngayDi);
        tripListPanel.setTrips(trips);
        if (trips.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến tàu nào phù hợp.", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onTripSelected(ChuyenTau trip) {
        if (trip != null) {
            TripDetails details = banVeService.getTripDetails(trip);
            selectedMaCT = details.maCT;
            selectedChuyen = details.maCT;
            selectedTuyen = details.route;
            selectedKhoiHanh = details.departureTime;

            taiToaChoChuyen(details.maCT);
            refreshSummary();
        }
    }

    private void taiToaChoChuyen(String maCT) {
        cboToaTau.removeAllItems();
        List<ToaOption> ds = banVeService.getToasForTrip(maCT, toaTheoChuyen);
        for (ToaOption t : ds)
            cboToaTau.addItem(t);
        if (cboToaTau.getItemCount() > 0)
            cboToaTau.setSelectedIndex(0);
        else {
            selectedMaToa = null;
            selectedToa = "Chưa chọn";
            bookedSeats.clear();
            selectedSeats.clear();
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
                if (seatCardContainer != null)
                    seatCardContainer.setVisible(true);
                refreshSummary();
            }
        });
        return cboToaTau;
    }

    private JComboBox<KhuyenMaiOption> createPromotionField() {
        cboKhuyenMai = new JComboBox<>();
        cboKhuyenMai.addActionListener(e -> refreshSummary());
        return cboKhuyenMai;
    }

    private void onSelectionChanged() {
        refreshSummary();
    }

    private void refreshSummary() {
        String maKM = "";
        KhuyenMaiOption kmOpt = (KhuyenMaiOption) cboKhuyenMai.getSelectedItem();
        if (kmOpt != null && kmOpt.maKM != null)
            maKM = kmOpt.maKM;

        BigDecimal serviceTotal = tinhTongTienDichVu();
        PriceSummary summary = banVeService.calculatePrice(selectedPrice, selectedSeats.size(), serviceTotal, maKM);

        String gheStr = selectedSeats.isEmpty() ? "Chưa chọn" : String.join(", ", selectedSeats);
        summaryPanel.updateOrderInfo(selectedKhachHang, selectedChuyen, selectedTuyen, selectedKhoiHanh, selectedToa,
                gheStr, selectedSeats.size());
        summaryPanel.updatePriceInfo(summary.unitPrice, summary.ticketTotal, summary.serviceTotal,
                summary.vat, summary.discount, summary.total);

        if (lblSeatTitle != null) {
            lblSeatTitle.setText("2. Chọn chỗ ngồi - " + selectedToa);
        }
        refreshSuccessCard();
        revalidate();
        repaint();
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

        String maKM = "";
        KhuyenMaiOption kmOpt = (KhuyenMaiOption) cboKhuyenMai.getSelectedItem();
        if (kmOpt != null && kmOpt.maKM != null)
            maKM = kmOpt.maKM;

        String maNV = "NV001";
        entity.TaiKhoan tk = service.PhienDangNhapService.layTaiKhoanDangNhap();
        if (tk != null) {
            dao.NhanVien_DAO nvDAO = new dao.NhanVien_DAO();
            entity.NhanVien nv = nvDAO.timNhanVienTheoUsername(tk.getTenDangNhap());
            if (nv != null) {
                maNV = nv.getMaNV();
            }
        }

        KetQuaLapHoaDon res = banVeService.confirmSale(maNV, selectedMaKH, maKM,
                selectedMaCT, selectedMaToa, new ArrayList<>(selectedSeats), selectedPrice,
                phieuDatDaNap == null ? null : gheCuaPhieuDat, taoChiTietDichVuDaChon());

        if (!res.thanhCong) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + res.thongBao);
            capNhatGheDaDatTheoToa();
            seatPanel.refreshUI();
            return;
        }

        if (phieuDatDaNap != null && phieuDatDaNap.getPhieuDatVe() != null) {
            banVeService.updateReservationStatus(phieuDatDaNap.getPhieuDatVe().getMaPhieu(), false);
        }

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmm"));
        generatedMaHD = res.maHoaDon;
        generatedMaVe = "VE" + ts + selectedSeats.size();
        selectedTrangThai = "Đã lập hóa đơn";

        daChotGiaoDich = true;
        soVeDaChot = selectedSeats.size();
        danhSachGheDaChot = String.join(", ", selectedSeats);
        tongTienDaChot = res.tongTien;

        khoiHanhDaChot = selectedKhoiHanh;
        khachHangDaChot = selectedKhachHang;
        chuyenDaChot = selectedChuyen;
        tuyenDaChot = selectedTuyen;
        toaDaChot = selectedToa;

        capNhatGheDaDatTheoToa();
        selectedSeats.clear();
        seatPanel.refreshUI();
        refreshSuccessCard();
        hienThiCard(printCard);
    }

    private void refreshSuccessCard() {
        String ghe = daChotGiaoDich ? danhSachGheDaChot
                : (selectedSeats.isEmpty() ? "—" : String.join(", ", selectedSeats));
        BigDecimal tong = daChotGiaoDich ? tongTienDaChot
                : banVeService.calculatePrice(selectedPrice, selectedSeats.size(), tinhTongTienDichVu(),
                        ((KhuyenMaiOption) cboKhuyenMai.getSelectedItem()) != null
                                ? ((KhuyenMaiOption) cboKhuyenMai.getSelectedItem()).maKM
                                : null).total;

        successCard.setData(generatedMaVe, BanVeUtils.formatMoney(tong),
                daChotGiaoDich ? khachHangDaChot : selectedKhachHang,
                daChotGiaoDich ? tuyenDaChot : selectedTuyen,
                daChotGiaoDich ? khoiHanhDaChot : selectedKhoiHanh,
                (daChotGiaoDich ? toaDaChot : selectedToa) + " · " + ghe);

        entity.TaiKhoan tk = service.PhienDangNhapService.layTaiKhoanDangNhap();
        String nguoiLap = "Quản trị viên";
        if (tk != null) {
            nguoiLap = tk.getHoTen();
            if (nguoiLap == null || nguoiLap.isBlank()) {
                nguoiLap = tk.getTenDangNhap();
            }
        }

        List<entity.VeTau> dsVe = banVeService.getTicketsForInvoice(generatedMaHD);
        printCard.updateData(generatedMaHD, nguoiLap, (daChotGiaoDich ? soVeDaChot : selectedSeats.size()) + " vé",
                daChotGiaoDich ? khachHangDaChot : selectedKhachHang,
                daChotGiaoDich ? chuyenDaChot : selectedChuyen,
                daChotGiaoDich ? tuyenDaChot : selectedTuyen,
                daChotGiaoDich ? khoiHanhDaChot : selectedKhoiHanh,
                (daChotGiaoDich ? toaDaChot : selectedToa) + " · " + ghe,
                BanVeUtils.formatMoney(tong), selectedTrangThai, dsVe);
    }

    private void resetForm() {
        selectedSeats.clear();
        daChotGiaoDich = false;
        phieuDatDaNap = null;
        gheCuaPhieuDat.clear();
        if (cboKhachHang != null && cboKhachHang.getItemCount() > 0)
            cboKhachHang.setSelectedIndex(0);
        if (cboToaTau != null)
            cboToaTau.removeAllItems();
        if (cboKhuyenMai != null && cboKhuyenMai.getItemCount() > 0)
            cboKhuyenMai.setSelectedIndex(0);
        resetDichVuSelections();
        if (tripListPanel != null)
            tripListPanel.setTrips(new ArrayList<>());
        if (seatCardContainer != null)
            seatCardContainer.setVisible(false);
        selectedTrangThai = "Đang chờ";
        capNhatGheDaDatTheoToa();
        seatPanel.refreshUI();
        refreshSummary();
    }

    private void taiDuLieuBanDau() {
        InitialData data = banVeService.getInitialData();

        cboKhachHang.removeAllItems();
        for (KhachHangOption o : data.customers)
            cboKhachHang.addItem(o);

        cboKhuyenMai.removeAllItems();
        for (KhuyenMaiOption o : data.promotions)
            cboKhuyenMai.addItem(o);

        toaTheoChuyen.clear();
        toaTheoChuyen.putAll(data.coachesByTrip);
        renderDichVu(data.services);
    }

    private void capNhatGheDaDatTheoToa() {
        bookedSeats.clear();
        if (selectedMaCT == null || selectedMaToa == null)
            return;
        Set<String> daDat = banVeService.getBookedSeats(selectedMaCT, selectedMaToa);
        for (String ghe : gheCuaPhieuDat) {
            if (ghe != null)
                daDat.remove(ghe.trim().toUpperCase());
        }
        for (String g : daDat)
            bookedSeats.add(BanVeUtils.chuanHoaMaGhe(g));
    }

    private void apDungPhieuDat(PhieuDatVeInfo info) {
        ReservationData data = banVeService.parseReservation(info);
        phieuDatDaNap = info;
        gheCuaPhieuDat.clear();
        selectedSeats.clear();

        selectedMaKH = data.maKH;
        selectedKhachHang = data.tenKH;
        selectedMaCT = data.maCT;
        selectedTuyen = data.route;
        selectedKhoiHanh = data.departureTime;
        selectedMaToa = data.maToa;
        selectedToa = data.toaInfo;
        currentSeatCount = data.seatCount;

        if (data.maToa != null) {
            seatPanel.setSeatCount(currentSeatCount);
        }

        for (String ghe : data.seats) {
            selectedSeats.add(ghe);
            gheCuaPhieuDat.add(ghe);
        }

        capNhatGheDaDatTheoToa();
        seatPanel.refreshUI();
        refreshSummary();
    }

    private void renderDichVu(List<DichVuOption> services) {
        serviceRows.clear();
        if (dichVuListPanel == null) {
            return;
        }
        dichVuListPanel.removeAll();

        if (services == null || services.isEmpty()) {
            JLabel empty = new JLabel("Hiện chưa có dịch vụ đang hoạt động.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            empty.setForeground(Color.decode("#6C7A8A"));
            dichVuListPanel.add(empty);
            dichVuListPanel.revalidate();
            dichVuListPanel.repaint();
            return;
        }

        for (DichVuOption service : services) {
            ServiceSelectionRow row = new ServiceSelectionRow(service);
            serviceRows.add(row);
            dichVuListPanel.add(row.container);
        }
        dichVuListPanel.revalidate();
        dichVuListPanel.repaint();
    }

    private void resetDichVuSelections() {
        for (ServiceSelectionRow row : serviceRows) {
            row.checkbox.setSelected(false);
            row.quantity.setValue(1);
            row.quantity.setEnabled(false);
        }
    }

    private BigDecimal tinhTongTienDichVu() {
        BigDecimal total = BigDecimal.ZERO;
        for (ServiceSelectionRow row : serviceRows) {
            if (row.checkbox.isSelected()) {
                int soLuong = ((Number) row.quantity.getValue()).intValue();
                total = total.add(row.option.giaDV.multiply(BigDecimal.valueOf(soLuong)));
            }
        }
        return total;
    }

    private List<entity.ChiTietHoaDonItem> taoChiTietDichVuDaChon() {
        List<entity.ChiTietHoaDonItem> items = new ArrayList<>();
        for (ServiceSelectionRow row : serviceRows) {
            if (!row.checkbox.isSelected()) {
                continue;
            }
            int soLuong = ((Number) row.quantity.getValue()).intValue();
            items.add(new entity.ChiTietHoaDonItem(null, null, null, row.option.maDV, soLuong, row.option.giaDV));
        }
        return items;
    }

    private class ServiceSelectionRow {
        private final DichVuOption option;
        private final JPanel container;
        private final JCheckBox checkbox;
        private final JSpinner quantity;

        private ServiceSelectionRow(DichVuOption option) {
            this.option = option;
            this.container = new JPanel(new BorderLayout(10, 0));
            this.container.setOpaque(false);
            this.container.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.decode("#E3EAF4")),
                    new EmptyBorder(8, 10, 8, 10)));

            this.quantity = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
            this.quantity.setEnabled(false);
            this.quantity.addChangeListener(e -> refreshSummary());

            this.checkbox = new JCheckBox(option.tenDV + " (" + BanVeUtils.formatMoney(option.giaDV) + ")");
            this.checkbox.setOpaque(false);
            this.checkbox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            this.checkbox.addActionListener(e -> {
                quantity.setEnabled(checkbox.isSelected());
                refreshSummary();
            });
            this.container.add(this.checkbox, BorderLayout.CENTER);

            JPanel qtyWrap = new JPanel(new BorderLayout(6, 0));
            qtyWrap.setOpaque(false);
            JLabel lblQty = new JLabel("SL");
            lblQty.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblQty.setForeground(MAU_TEXT);
            qtyWrap.add(lblQty, BorderLayout.WEST);
            qtyWrap.add(this.quantity, BorderLayout.CENTER);
            this.container.add(qtyWrap, BorderLayout.EAST);
        }
    }

    private void hienThiCard(JPanel card) {
        contentPanel.removeAll();
        contentPanel.add(card, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handlePayment(String maHD) {
        if (maHD == null || maHD.equals("—")) {
            JOptionPane.showMessageDialog(this, "Không có mã hóa đơn hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = banVeService.updatePaymentStatus(maHD, true);
        if (success) {
            selectedTrangThai = "Đã thanh toán";
            printCard.capNhatTrangThaiUI("Đã thanh toán");
            JOptionPane.showMessageDialog(this, "Thanh toán hóa đơn " + maHD + " thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật trạng thái thanh toán hóa đơn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startNewSale() {
        resetForm();
        hienThiCard(saleCard);
    }
}
