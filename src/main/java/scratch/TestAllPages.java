package scratch;

import connectDB.DatabaseConnection;
import entity.TaiKhoan;
import service.PhienDangNhapService;
import view.*;
import javax.swing.JPanel;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestAllPages {
    public static void main(String[] args) {
        System.out.println("========== BẮT ĐẦU KIỂM THỬ TOÀN BỘ GIAO DIỆN & CHỨC NĂNG BẰNG CODE ==========");
        
        // 1. Khởi động kết nối CSDL
        try {
            System.out.println("\n[1/3] Đang kết nối Cơ sở dữ liệu...");
            if (DatabaseConnection.connect() == null) {
                System.err.println("✗ Lỗi: Không thể kết nối cơ sở dữ liệu. Vui lòng kiểm tra lại dịch vụ SQLEXPRESS.");
                System.exit(1);
            }
            System.out.println("✓ Kết nối CSDL thành công!");
        } catch (Exception e) {
            System.err.println("✗ Lỗi kết nối CSDL: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // 2. Cài đặt Theme và thiết lập phiên đăng nhập giả lập cho Admin
        try {
            System.out.println("\n[2/3] Cài đặt Theme và Phân quyền đăng nhập giả lập...");
            AppTheme.installGlobalStyles();
            TaiKhoan admin = new TaiKhoan("admin01", "123456", "admin@tau.vn", "Nguyễn Văn Admin", "ADMIN");
            PhienDangNhapService.batDauPhien(admin);
            System.out.println("✓ Khởi tạo phiên làm việc thành công!");
        } catch (Exception e) {
            System.err.println("✗ Lỗi thiết lập phiên đăng nhập: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // 3. Khởi tạo thử tất cả các Trang (View / Panel) xem có bị crash / lỗi NPE hay SQLException
        System.out.println("\n[3/3] Đang kiểm thử khởi tạo các giao diện thành phần...");
        Map<String, Runnable> pages = new LinkedHashMap<>();
        
        // Nhóm Bán vé & Giao dịch
        pages.put("view.BanVePage (Giao diện Bán vé)", BanVePage::new);
        pages.put("view.DoiVePage (Giao diện Đổi vé)", DoiVePage::new);
        pages.put("view.TraVePage (Giao diện Trả vé)", TraVePage::new);
        pages.put("view.LichSuVePage (Lịch sử vé)", LichSuVePage::new);
        pages.put("view.PhieuDatVePage (Đặt vé khách gọi)", PhieuDatVePage::new);
        pages.put("view.HoaDonTraCuuPage (Tra cứu hóa đơn)", HoaDonTraCuuPage::new);

        // Nhóm Thống kê & Báo cáo
        pages.put("view.ThongKePage (Tổng quan Thống kê)", ThongKePage::new);
        pages.put("view.DoanhThuThongKePage (Thống kê Doanh thu)", DoanhThuThongKePage::new);
        pages.put("view.VeThongKePage (Thống kê Vé)", VeThongKePage::new);
        pages.put("view.KhachHangThongKePage (Thống kê Khách hàng)", KhachHangThongKePage::new);
        pages.put("view.ChuyenTauThongKePage (Thống kê Chuyến tàu)", ChuyenTauThongKePage::new);

        // Nhóm Khách hàng & Nhân viên
        pages.put("view.KhachHangThemPage (Thêm khách hàng)", KhachHangThemPage::new);
        pages.put("view.KhachHangTraCuuPage (Tra cứu khách hàng)", KhachHangTraCuuPage::new);
        pages.put("view.KhachHangCapNhatPage (Cập nhật khách hàng)", KhachHangCapNhatPage::new);
        pages.put("view.NhanVienThemPage (Thêm nhân viên)", NhanVienThemPage::new);
        pages.put("view.NhanVienTraCuuPage (Tra cứu nhân viên)", NhanVienTraCuuPage::new);
        pages.put("view.NhanVienCapNhatPage (Cập nhật nhân viên)", NhanVienCapNhatPage::new);

        // Nhóm Tàu & Toa & Tuyến & Chuyến
        pages.put("view.TauThemPage (Thêm tàu)", TauThemPage::new);
        pages.put("view.TauTraCuuPage (Tra cứu tàu)", TauTraCuuPage::new);
        pages.put("view.TauCapNhatPage (Cập nhật tàu)", TauCapNhatPage::new);
        pages.put("view.ToaThemPage (Thêm toa)", ToaThemPage::new);
        pages.put("view.ToaTraCuuPage (Tra cứu toa)", ToaTraCuuPage::new);
        pages.put("view.ToaCapNhatPage (Cập nhật toa)", ToaCapNhatPage::new);
        pages.put("view.TuyenTauThemPage (Thêm tuyến)", TuyenTauThemPage::new);
        pages.put("view.TuyenTauTraCuuPage (Tra cứu tuyến)", TuyenTauTraCuuPage::new);
        pages.put("view.TuyenTauCapNhatPage (Cập nhật tuyến)", TuyenTauCapNhatPage::new);
        pages.put("view.ChuyenTauThemPage (Thêm chuyến)", ChuyenTauThemPage::new);
        pages.put("view.ChuyenTauTraCuuPage (Tra cứu chuyến)", ChuyenTauTraCuuPage::new);
        pages.put("view.ChuyenTauCapNhatPage (Cập nhật chuyến)", ChuyenTauCapNhatPage::new);

        // Nhóm Khuyến mãi & Dịch vụ
        pages.put("view.KhuyenMaiThemPage (Thêm khuyến mãi)", KhuyenMaiThemPage::new);
        pages.put("view.KhuyenMaiTraCuuPage (Tra cứu khuyến mãi)", KhuyenMaiTraCuuPage::new);
        pages.put("view.KhuyenMaiCapNhatPage (Cập nhật khuyến mãi)", KhuyenMaiCapNhatPage::new);
        pages.put("view.DichVuThemPage (Thêm dịch vụ)", DichVuThemPage::new);
        pages.put("view.DichVuTraCuuPage (Tra cứu dịch vụ)", DichVuTraCuuPage::new);
        pages.put("view.DichVuCapNhatPage (Cập nhật dịch vụ)", DichVuCapNhatPage::new);

        int successfulCount = 0;
        int failedCount = 0;

        for (Map.Entry<String, Runnable> entry : pages.entrySet()) {
            String pageName = entry.getKey();
            Runnable loader = entry.getValue();
            System.out.println("Đang tải " + pageName + "... ");
            try {
                loader.run();
                System.out.println("  => OK");
                successfulCount++;
            } catch (Throwable t) {
                System.out.println("  => THẤT BẠI");
                System.err.println("--- CHI TIẾT LỖI TẠI " + pageName + " ---");
                t.printStackTrace();
                System.err.println("----------------------------------------------");
                failedCount++;
            }
        }

        System.out.println("\n========== KẾT QUẢ KIỂM THỬ TOÀN BỘ GIAO DIỆN ==========");
        System.out.println("Tổng số giao diện kiểm thử: " + pages.size());
        System.out.println("Thành công: " + successfulCount + " / " + pages.size());
        System.out.println("Thất bại: " + failedCount + " / " + pages.size());
        System.out.println("========================================================");
        
        DatabaseConnection.disconnect();
        
        if (failedCount > 0) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }
}
