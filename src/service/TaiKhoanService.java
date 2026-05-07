
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

        return dao.layEmailTheoTaiKhoan(tenDangNhap);
    }
	
	public boolean doiMatKhau(String tenDangNhap, String matKhauMoi) {
		validateMatKhau(matKhauMoi);
	    return dao.doiMatKhau(tenDangNhap, matKhauMoi);
	}
	
	public void validateMatKhau(String matKhau) {
		if(matKhau.length() < 8) {
			throw new IllegalArgumentException("Mật khẩu phải có ít nhất 8 ký tự.");
		}
		if(!matKhau.matches(".*[A-Z].*")) {
			throw new IllegalArgumentException("Mật khẩu phải chứa ít nhất một chữ cái viết hoa.");
		}
		if(!matKhau.matches(".*[a-z].*")) {
			throw new IllegalArgumentException("Mật khẩu phải chứa ít nhất một chữ cái viết thường.");
		}
		if(!matKhau.matches(".*\\d.*")) {
			throw new IllegalArgumentException("Mật khẩu phải chứa ít nhất một chữ số.");
		}
	}
	
	public String kiemTraTaiKhoanQuenMatKhau(String tenDangNhap) {
	    if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
	        throw new IllegalArgumentException("Vui lòng nhập tên đăng nhập để tiếp tục");
	    }

	    String email = dao.layEmailTheoTaiKhoan(tenDangNhap);

	    if (email == null) {
	        throw new IllegalArgumentException("Tài khoản không tồn tại");
	    }

	    return email;
	}
}
