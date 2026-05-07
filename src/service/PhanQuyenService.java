package service;

import entity.TaiKhoan;

public class PhanQuyenService {
	
	// Kiêm tra xem tài khoản có phải là quản lý hay không
	public boolean laQuanLy(TaiKhoan taiKhoan) {
		if (taiKhoan == null || taiKhoan.getVaiTro() == null) {
	        return false;
	    }

	    String vaiTro = taiKhoan.getVaiTro();
	    
		return "QUAN_LY".equalsIgnoreCase(vaiTro) || "ADMIN".equalsIgnoreCase(vaiTro);
	}
	
	public String[] layMenuTheoVaiTro(TaiKhoan taiKhoan) {
		if (laQuanLy(taiKhoan)) {
			return new String[] { "Trang chủ", "Nhân viên", "Khách hàng", "Vé", "Tàu", "Dịch vụ", "Khuyến mãi", "Thống kê" };
		}
		return new String[] { "Trang chủ", "Khách hàng", "Vé", "Tàu", "Dịch vụ", "Khuyến mãi", "Thống kê" };
	}

	public String hienThiVaiTro(TaiKhoan taiKhoan) {
		return laQuanLy(taiKhoan) ? "Quản trị viên" : "Nhân viên";
	}

	public String[] layMenuCon(String menuName, TaiKhoan taiKhoan) {
	    boolean isQuanLy = laQuanLy(taiKhoan);

	    switch (menuName) {
	        case "Nhân viên":
	            return new String[] { "Thêm nhân viên", "Tra cứu nhân viên", "Cập nhật thông tin", "Lập hóa đơn" };

	        case "Khách hàng":
	            return isQuanLy
	                ? new String[] { "Thêm khách hàng", "Tra cứu khách hàng", "Cập nhật thông tin", "Lịch sử vé" }
	                : new String[] { "Tra cứu khách hàng", "Lịch sử vé" };

	        case "Vé":
	            return new String[] { "Bán vé", "Đổi vé", "Trả vé", "Kiểm tra chỗ trống", "In vé" };

	        case "Chuyến tàu":
	            return isQuanLy
	                ? new String[] { "Thêm chuyến", "Tra cứu chuyến", "Cập nhật" }
	                : new String[] { "Tra cứu chuyến" };

	        case "Tàu":
	            return isQuanLy
	                ? new String[] { "Thêm tàu", "Tra cứu tàu", "Cập nhật" }
	                : new String[] { "Tra cứu tàu" };

	        case "Toa":
	            return isQuanLy
	                ? new String[] { "Thêm toa", "Tra cứu toa", "Cập nhật" }
	                : new String[] { "Tra cứu toa" };

	        case "Tuyến tàu":
	            return isQuanLy
	                ? new String[] { "Thêm tuyến", "Tra cứu tuyến", "Cập nhật" }
	                : new String[] { "Tra cứu tuyến" };

	        case "Dịch vụ":
	            return isQuanLy
	                ? new String[] { "Thêm dịch vụ", "Tra cứu", "Cập nhật" }
	                : new String[] { "Tra cứu" };

	        case "Khuyến mãi":
	            return isQuanLy
	                ? new String[] { "Thêm khuyến mãi", "Tra cứu", "Cập nhật" }
	                : new String[] { "Tra cứu" };

	        case "Thống kê":
	            return new String[] { "Doanh thu", "Vé", "Khách hàng", "Chuyến tàu" };

	        default:
	        	return new String[0];
	    }
	}
	
}
