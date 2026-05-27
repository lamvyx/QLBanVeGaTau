package service;

import controller.*;
import entity.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import service.HoaDonService.KetQuaLapHoaDon;
import view.KhachHangOption;
import view.KhuyenMaiOption;
import view.ToaOption;
import view.BanVeUtils;
import view.DichVuOption;

public class BanVeService {
    private final HoaDonController hoaDonController = new HoaDonController();
    private final KhachHangController khachHangController = new KhachHangController();
    private final PhieuDatVeController phieuDatVeController = new PhieuDatVeController();
    private final KhuyenMaiController khuyenMaiController = new KhuyenMaiController();
    private final ChuyenTauController chuyenTauController = new ChuyenTauController();
    private final ToaController toaController = new ToaController();
    private final TuyenTauController tuyenTauController = new TuyenTauController();
    private final dao.VeTau_DAO veTauDAO = new dao.VeTau_DAO();

    /**
     * Tải dữ liệu ban đầu cho trang bán vé
     */
    public InitialData getInitialData() {
        List<KhachHangOption> customers = getCustomerOptions();

        List<KhuyenMaiOption> promos = new ArrayList<>();
        promos.add(new KhuyenMaiOption(null, "Không áp dụng", BigDecimal.ZERO));
        for (KhuyenMai km : khuyenMaiController.layTatCaKhuyenMai()) {
            promos.add(new KhuyenMaiOption(km.getMaKM(), km.getTenKM(), km.getTyLeKM()));
        }

        List<DichVuOption> services = new ArrayList<>();
        DichVuController dichVuController = new DichVuController();
        for (DichVu dv : dichVuController.layDichVuHoatDong()) {
            services.add(new DichVuOption(dv.getMaDV(), dv.getTenDV(), dv.getGiaDV()));
        }

        Map<String, List<ToaOption>> coachesByTrip = new HashMap<>();
        for (ChuyenTau trip : chuyenTauController.timKiemChuyenTau(null)) {
            List<ToaOption> toas = new ArrayList<>();
            for (Toa toa : toaController.layToaTheoChuyenTau(trip.getMaCT())) {
                toas.add(new ToaOption(toa.getMaToa(), toa.getLoaiToa(), toa.getSoGhe(),
                        BanVeUtils.xacDinhGiaVeTheoLoaiToa(toa.getLoaiToa())));
            }
            coachesByTrip.put(trip.getMaCT(), toas);
        }

        return new InitialData(customers, promos, coachesByTrip, services);
    }

    public List<KhachHangOption> getCustomerOptions() {
        List<KhachHangOption> customers = new ArrayList<>();
        for (KhachHang kh : khachHangController.layTatCaKhachHang()) {
            customers.add(new KhachHangOption(kh.getMaKH(), kh.getTenKH(), kh.getSdt()));
        }
        return customers;
    }

    public KhachHangOption findCustomerByPhoneOrCccd(String tuKhoa) {
        KhachHang kh = khachHangController.timKhachHangTheoSdtHoacCccd(tuKhoa);
        if (kh == null) {
            return null;
        }
        return new KhachHangOption(kh.getMaKH(), kh.getTenKH(), kh.getSdt());
    }

    /**
     * Tra cứu chuyến tàu
     */
    public List<ChuyenTau> searchTrips(String gaDi, String gaDen, java.time.LocalDate ngayDi) {
        return chuyenTauController.traCuuChuyenTau(gaDi, gaDen, ngayDi);
    }

    /**
     * Lấy danh sách toa cho một chuyến tàu
     */
    public List<ToaOption> getToasForTrip(String maCT, Map<String, List<ToaOption>> toaTheoChuyen) {
        return toaTheoChuyen.getOrDefault(maCT, Collections.emptyList());
    }

    /**
     * Lấy thông tin chuyến tàu đã chọn
     */
    public TripDetails getTripDetails(ChuyenTau trip) {
        String maCT = trip.getMaCT();
        String departureTime = trip.getNgayKhoiHanh().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String route = trip.getMaTuyenTau();

        try {
            var tuyenList = tuyenTauController.timKiemTuyenTau(trip.getMaTuyenTau(), null, null);
            if (tuyenList != null && !tuyenList.isEmpty()) {
                var tt = tuyenList.get(0);
                String gaDi = tt.getMaGaDi() == null ? "" : tt.getMaGaDi();
                String gaDen = tt.getMaGaDen() == null ? "" : tt.getMaGaDen();
                route = gaDi + " → " + gaDen;
            }
        } catch (Exception ignored) {
        }

        return new TripDetails(maCT, route, departureTime, trip.getMaTau());
    }

    /**
     * Tính toán chi tiết giá
     */
    public PriceSummary calculatePrice(BigDecimal unitPrice, int seatCount, String maKM) {
        return calculatePrice(unitPrice, seatCount, BigDecimal.ZERO, maKM);
    }

    public PriceSummary calculatePrice(BigDecimal unitPrice, int seatCount, BigDecimal serviceTotal, String maKM) {
        BigDecimal ticketTotal = unitPrice.multiply(BigDecimal.valueOf(seatCount));
        BigDecimal extraServiceTotal = serviceTotal == null ? BigDecimal.ZERO : serviceTotal;
        BigDecimal subtotal = ticketTotal.add(extraServiceTotal);
        BigDecimal vat = hoaDonController.tinhThueVAT(subtotal);
        BigDecimal discount = hoaDonController.tinhChietKhau(subtotal, maKM);
        BigDecimal total = hoaDonController.tinhTongThanhToan(subtotal, vat, discount);
        return new PriceSummary(unitPrice, ticketTotal, extraServiceTotal, vat, discount, total);
    }

    /**
     * Thực hiện lập hóa đơn bán vé
     */
    public KetQuaLapHoaDon confirmSale(String maNV, String maKH, String maKM, String maCT, String maToa,
            List<String> seats, BigDecimal unitPrice, Set<String> ignoredSeats) {
        return confirmSale(maNV, maKH, maKM, maCT, maToa, seats, unitPrice, ignoredSeats, null);
    }

    public KetQuaLapHoaDon confirmSale(String maNV, String maKH, String maKM, String maCT, String maToa,
            List<String> seats, BigDecimal unitPrice, Set<String> ignoredSeats, List<ChiTietHoaDonItem> dichVuItems) {
        if (ignoredSeats == null || ignoredSeats.isEmpty()) {
            return hoaDonController.lapHoaDonBanVe(maNV, maKH, maKM, maCT, maToa, seats, unitPrice, null, dichVuItems);
        } else {
            return hoaDonController.lapHoaDonBanVe(maNV, maKH, maKM, maCT, maToa, seats, unitPrice, ignoredSeats, dichVuItems);
        }
    }

    /**
     * Cập nhật trạng thái thanh toán của hóa đơn
     */
    public boolean updatePaymentStatus(String maHD, boolean paid) {
        return hoaDonController.updateTrangThaiThanhToan(maHD, paid);
    }

    /**
     * Lấy danh sách các vé tàu thuộc một hóa đơn
     */
    public List<VeTau> getTicketsForInvoice(String maHD) {
        List<VeTau> dsVe = new ArrayList<>();
        if (maHD == null || maHD.isBlank()) return dsVe;
        List<entity.ChiTietHoaDonItem> ctList = hoaDonController.layChiTietHoaDon(maHD);
        for (entity.ChiTietHoaDonItem ct : ctList) {
            if (ct.getMaVeTau() != null && !ct.getMaVeTau().isBlank()) {
                VeTau ve = veTauDAO.timTheoMaVe(ct.getMaVeTau());
                if (ve != null) {
                    dsVe.add(ve);
                }
            }
        }
        return dsVe;
    }

    /**
     * Cập nhật trạng thái phiếu đặt vé
     */
    public void updateReservationStatus(String maPhieu, boolean status) {
        phieuDatVeController.capNhatTrangThai(maPhieu, status);
    }

    /**
     * Lấy danh sách ghế đã đặt
     */
    public Set<String> getBookedSeats(String maCT, String maToa) {
        return hoaDonController.layGheDaDat(maCT, maToa);
    }

    /**
     * Phân tích dữ liệu từ phiếu đặt vé
     */
    public ReservationData parseReservation(PhieuDatVeInfo info) {
        String maKH = null;
        String tenKH = "Chưa chọn";
        if (info.getPhieuDatVe().getMaKH() != null) {
            KhachHang kh = khachHangController.timKiemKhachHang(info.getPhieuDatVe().getMaKH(), null, null)
                    .stream().findFirst().orElse(null);
            if (kh != null) {
                maKH = kh.getMaKH();
                tenKH = kh.getTenKH();
            }
        }

        if (info.getChiTietList() == null || info.getChiTietList().isEmpty()) {
            return new ReservationData(maKH, tenKH, null, null, null, null, null, 0, new ArrayList<>());
        }

        ChiTietPhieuDat itemFirst = info.getChiTietList().get(0);
        String maCT = itemFirst.getMaCT();
        String maToa = itemFirst.getMaToa();

        String route = "";
        String departureTime = "";
        List<ChuyenTau> chuyenList = chuyenTauController.timKiemChuyenTau(maCT);
        if (chuyenList != null && !chuyenList.isEmpty()) {
            TripDetails details = getTripDetails(chuyenList.get(0));
            route = details.route;
            departureTime = details.departureTime;
        }

        String toaInfo = maToa;
        int seatCount = 32;
        List<Toa> toaList = toaController.timKiemToa(maToa);
        if (toaList != null && !toaList.isEmpty()) {
            Toa toa = toaList.get(0);
            seatCount = toa.getSoGhe();
            toaInfo = toa.getMaToa() + " (" + toa.getLoaiToa() + ")";
        }

        List<String> seats = new ArrayList<>();
        for (ChiTietPhieuDat item : info.getChiTietList()) {
            if (item.getViTriGhe() != null && !item.getViTriGhe().isBlank()) {
                seats.add(BanVeUtils.chuanHoaMaGhe(item.getViTriGhe()));
            }
        }

        return new ReservationData(maKH, tenKH, maCT, route, departureTime, maToa, toaInfo, seatCount, seats);
    }

    // --- DTO Classes ---

    public static class InitialData {
        public final List<KhachHangOption> customers;
        public final List<KhuyenMaiOption> promotions;
        public final Map<String, List<ToaOption>> coachesByTrip;
        public final List<DichVuOption> services;

        public InitialData(List<KhachHangOption> customers, List<KhuyenMaiOption> promotions,
                Map<String, List<ToaOption>> coachesByTrip, List<DichVuOption> services) {
            this.customers = customers;
            this.promotions = promotions;
            this.coachesByTrip = coachesByTrip;
            this.services = services;
        }
    }

                
    public static class TripDetails {
        public final String maCT, route, departureTime, maTau;
        public TripDetails(String maCT, String route, String departureTime, String maTau) {
            this.maCT = maCT; this.route = route; this.departureTime = departureTime; this.maTau = maTau;
        }
    }

    public static class PriceSummary {
        public final BigDecimal unitPrice, ticketTotal, serviceTotal, vat, discount, total;
        public PriceSummary(BigDecimal unitPrice, BigDecimal ticketTotal, BigDecimal serviceTotal,
                BigDecimal vat, BigDecimal discount, BigDecimal total) {
            this.unitPrice = unitPrice;
            this.ticketTotal = ticketTotal;
            this.serviceTotal = serviceTotal;
            this.vat = vat;
            this.discount = discount;
            this.total = total;
        }
    }

    public static class ReservationData {
        public final String maKH, tenKH, maCT, route, departureTime, maToa, toaInfo;
        public final int seatCount;
        public final List<String> seats;

        public ReservationData(String maKH, String tenKH, String maCT, String route, String departureTime, String maToa, String toaInfo, int seatCount, List<String> seats) {
            this.maKH = maKH; this.tenKH = tenKH; this.maCT = maCT; this.route = route; this.departureTime = departureTime; this.maToa = maToa; this.toaInfo = toaInfo; this.seatCount = seatCount; this.seats = seats;
        }
    }
}
