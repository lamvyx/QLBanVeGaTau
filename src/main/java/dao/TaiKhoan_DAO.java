package dao;

import connectDB.Database;
import entity.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoan_DAO {
	private static final String ADMIN_MAC_DINH_USERNAME = "admin";
	private static final String ADMIN_MAC_DINH_PASSWORD = "admin123";
	private static final String ADMIN_MAC_DINH_EMAIL = "admin@local";
	private static final String ADMIN_MAC_DINH_HOTEN = "Quan tri he thong";

	private static final String SQL_TIM_TAI_KHOAN = """
			SELECT tk.username, tk.[password] AS matKhau, tk.vaiTro,
			       ISNULL(nv.email, '') AS email,
			       ISNULL(nv.tenNV, tk.username) AS hoTen
			FROM TaiKhoan tk
			LEFT JOIN NhanVien nv ON nv.username = tk.username
			WHERE tk.username = ?
			""";

	private static final String SQL_CAP_NHAT_MAT_KHAU = "UPDATE TaiKhoan SET [password] = ? WHERE username = ?";

	public TaiKhoan timTaiKhoanDangNhap(String tenDangNhap, String matKhau) {
		if (laTaiKhoanAdminMacDinh(tenDangNhap, matKhau)) {
			return taoTaiKhoanAdminMacDinh();
		}

		TaiKhoan taiKhoan = timTheoTenDangNhap(tenDangNhap);
		if (taiKhoan == null) {
			return null;
		}
		return taiKhoan.getMatKhau().equals(matKhau) ? taiKhoan : null;
	}

	public boolean dangNhap(String tenDangNhap, String matKhau) {
		return timTaiKhoanDangNhap(tenDangNhap, matKhau) != null;
	}

	public String layEmailTheoTaiKhoan(String tenDangNhap) {
		if (ADMIN_MAC_DINH_USERNAME.equalsIgnoreCase(tenDangNhap == null ? "" : tenDangNhap.trim())) {
			return ADMIN_MAC_DINH_EMAIL;
		}

		TaiKhoan taiKhoan = timTheoTenDangNhap(tenDangNhap);
		return taiKhoan == null ? null : taiKhoan.getEmail();
	}

	public boolean doiMatKhau(String tenDangNhap, String matKhauMoi) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_CAP_NHAT_MAT_KHAU)) {
			ps.setString(1, matKhauMoi);
			ps.setString(2, tenDangNhap);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	private TaiKhoan timTheoTenDangNhap(String tenDangNhap) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_TIM_TAI_KHOAN)) {
			ps.setString(1, tenDangNhap);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					return null;
				}
				return new TaiKhoan(
					rs.getString("username"),
					rs.getString("matKhau"),
					rs.getString("email"),
					rs.getString("hoTen"),
					rs.getString("vaiTro")
				);
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	private boolean laTaiKhoanAdminMacDinh(String tenDangNhap, String matKhau) {
		if (tenDangNhap == null || matKhau == null) {
			return false;
		}
		return ADMIN_MAC_DINH_USERNAME.equalsIgnoreCase(tenDangNhap.trim())
				&& ADMIN_MAC_DINH_PASSWORD.equals(matKhau);
	}

	private TaiKhoan taoTaiKhoanAdminMacDinh() {
		return new TaiKhoan(
			ADMIN_MAC_DINH_USERNAME,
			ADMIN_MAC_DINH_PASSWORD,
			ADMIN_MAC_DINH_EMAIL,
			ADMIN_MAC_DINH_HOTEN,
			"ADMIN"
		);
	}
}
