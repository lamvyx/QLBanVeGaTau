package view;

import javax.swing.JPanel;
import entity.TaiKhoan;
import service.PhanQuyenService;



public class PageFactory {
	private final PhanQuyenService phanQuyenService;
	public PageFactory() {
		phanQuyenService = new PhanQuyenService();
	}
	
	public JPanel taoPageTheoMenu(String menuName, String menuCon, TaiKhoan taiKhoan) {
		boolean laNhanVien = !phanQuyenService.laQuanLy(taiKhoan);
		
		if ("Nhân viên".equals(menuName)) {
			switch (menuCon) {
			case "Thêm nhân viên":
				return new NhanVienThemPage();
			case "Tra cứu nhân viên":
				return new NhanVienTraCuuPage();
			case "Cập nhật thông tin":
				return new NhanVienCapNhatPage();
			case "Lập hóa đơn":
				return new LapHoaDonPage();
			default:
				return new NhanVienTraCuuPage(); //Để chỉnh lại sau
			}
		}
		

		if ("Khách hàng".equals(menuName)) {
			switch (menuCon) {
			case "Thêm khách hàng":
				return new KhachHangThemPage();
			case "Tra cứu khách hàng":
				return new KhachHangTraCuuPage();
			case "Cập nhật thông tin":
				return new KhachHangCapNhatPage();
			case "Lịch sử vé":
				return new LichSuVePage();
			default:
				return new KhachHangPage();
			}
		}

		if ("Vé".equals(menuName)) {
			switch (menuCon) {
			case "Bán vé":
				return new VeTauPage("BAN_VE");
			case "Đổi vé":
				return new VeTauPage("DOI_VE");
			case "Trả vé":
				return new VeTauPage("TRA_VE");
			case "Kiểm tra chỗ trống":
				return new VeTauPage("KT_CHO");
			case "In vé":
				return new VeTauPage("IN_VE");
			default:
				return new VeTauPage();
			}
		}

		if ("Chuyến tàu".equals(menuName)) {
			switch (menuCon) {
			case "Thêm chuyến":
				return new ChuyenTauThemPage();
			case "Tra cứu chuyến":
				return new ChuyenTauTraCuuPage();
			case "Cập nhật":
				return new ChuyenTauCapNhatPage();
			default:
				return new ChuyenTauTraCuuPage();
			}
		}

		if ("Tàu".equals(menuName)) {
			switch (menuCon) {
			case "Thêm tàu":
				return new TauThemPage();
			case "Tra cứu tàu":
				return new TauTraCuuPage();
			case "Cập nhật":
				return new TauCapNhatPage();
			default:
				return new TauTraCuuPage();
			}
		}

		if ("Toa".equals(menuName)) {
			switch (menuCon) {
			case "Thêm toa":
				return new ToaThemPage();
			case "Tra cứu toa":
				return new ToaTraCuuPage();
			case "Cập nhật":
				return new ToaCapNhatPage();
			default:
				return new ToaTraCuuPage();
			}
		}

		if ("Dịch vụ".equals(menuName)) {
			switch (menuCon) {
			case "Thêm dịch vụ":
				return new DichVuThemPage();
			case "Tra cứu":
				return new DichVuTraCuuPage();
			case "Cập nhật":
				return new DichVuCapNhatPage();
			default:
				return new DichVuTraCuuPage();
			}
		}

		if ("Khuyến mãi".equals(menuName)) {
			switch (menuCon) {
			case "Thêm khuyến mãi":
				return new KhuyenMaiThemPage();
			case "Tra cứu":
				return new KhuyenMaiTraCuuPage();
			case "Cập nhật":
				return new KhuyenMaiCapNhatPage();
			case "Áp dụng":
				return new KhuyenMaiTraCuuPage();
			default:
				return new KhuyenMaiTraCuuPage();
			}
		}

		if ("Tuyến tàu".equals(menuName)) {
			switch (menuCon) {
			case "Thêm tuyến":
				return new TuyenTauThemPage();
			case "Tra cứu tuyến":
				return new TuyenTauTraCuuPage();
			case "Cập nhật":
				return new TuyenTauCapNhatPage();
			default:
				return new TuyenTauTraCuuPage();
			}
		}

		if ("Thống kê".equals(menuName)) {
			switch (menuCon) {
			case "Doanh thu":
				return new DoanhThuThongKePage(laNhanVien);
			case "Vé":
				return new DoanhThuThongKePage(laNhanVien);
			case "Khách hàng":
				return new DoanhThuThongKePage(laNhanVien);
			case "Chuyến tàu":
				return new DoanhThuThongKePage(laNhanVien);
			default:
				return new DoanhThuThongKePage(laNhanVien);
			}
		}

		switch (menuName) {
		case "Nhân viên":
			return new NhanVienPage();
		case "Khách hàng":
			return new KhachHangPage();
		case "Vé":
			return new VeTauPage();
		case "Chuyến tàu":
			return new ChuyenTauPage();
		case "Tàu":
			return new TauPage();
		case "Toa":
			return new ToaPage();
		case "Tuyến tàu":
			return new TuyenTauPage();
		case "Dịch vụ":
			return new DichVuPage();
		case "Khuyến mãi":
			return new KhuyenMaiPage();
		case "Thống kê":
			return new ThongKePage();
		default:
			return new JPanel();
		}
	}

	
}
