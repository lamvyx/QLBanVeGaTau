
package service;

import dao.TaiKhoan_DAO;
import entity.TaiKhoan;

public class TaiKhoanService {
	private TaiKhoan_DAO dao;
	
	public TaiKhoanService() {
		dao = new TaiKhoan_DAO();
	}
	
	public TaiKhoan dangNhap(String user, String pass) {
	    if (user == null || user.trim().isEmpty()) {
	        throw new IllegalArgumentException("Tên đăng nhập không được rỗng");
	    }
	    if (pass == null || pass.trim().isEmpty()) {
	        throw new IllegalArgumentException("Mật khẩu không được rỗng");
	    }
	    
	    // Tài khoản bình thường, truy vấn database
	    TaiKhoan tk = dao.timTheoTenDangNhap(user);
	    
	    if (tk == null) return null;

	    if (!tk.getMatKhau().equals(pass)) return null;

	    return tk;
	}
	
	
	public String layEmailTheoTaiKhoan(String tenDangNhap) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được rỗng");
        }

        // Nếu là tài khoản admin, trả về email mặc định
        if ("admin".equalsIgnoreCase(tenDangNhap.trim())) {
            return "admin@local";
        }

        return dao.layEmailTheoTaiKhoan(tenDangNhap);
    }
}
