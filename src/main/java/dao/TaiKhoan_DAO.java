package dao;

import connectDB.Database;
import entity.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoan_DAO {
	private static final String SQL_TIM_TAI_KHOAN = """
			SELECT tk.username, tk.[password] AS matKhau, tk.vaiTro,
			       ISNULL(nv.email, '') AS email,
			       ISNULL(nv.tenNV, tk.username) AS hoTen
			FROM TaiKhoan tk
			LEFT JOIN NhanVien nv ON nv.username = tk.username
			WHERE tk.username = ?
			""";

	private static final String SQL_LAY_TAT_CA = """
			SELECT tk.username, tk.[password] AS matKhau, tk.vaiTro,
			       ISNULL(nv.email, '') AS email,
			       ISNULL(nv.tenNV, tk.username) AS hoTen
			FROM TaiKhoan tk
			LEFT JOIN NhanVien nv ON nv.username = tk.username
			ORDER BY tk.username
			""";

	private static final String SQL_THEM = "INSERT INTO TaiKhoan (username, [password], vaiTro) VALUES (?, ?, ?)";
	private static final String SQL_XOA = "DELETE FROM TaiKhoan WHERE username = ?";

	private static final String SQL_CAP_NHAT_MAT_KHAU = "UPDATE TaiKhoan SET [password] = ? WHERE username = ?";

	public TaiKhoan timTaiKhoanDangNhap(String tenDangNhap, String matKhau) {
		TaiKhoan taiKhoan = timTheoTenDangNhap(tenDangNhap);
		if (taiKhoan == null) {
			return null;
		}
		return taiKhoan.getMatKhau().equals(matKhau) ? taiKhoan : null;
	}

	public boolean dangNhap(String tenDangNhap, String matKhau) {
		return timTaiKhoanDangNhap(tenDangNhap, matKhau) != null;
	}

	public List<TaiKhoan> layTatCa() {
		List<TaiKhoan> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_TAT_CA);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ds.add(docTaiKhoan(rs));
			}
		} catch (SQLException ex) {
			return List.of();
		}
		return ds;
	}

	public TaiKhoan layTheoTenDangNhap(String tenDangNhap) {
		return timTheoTenDangNhap(tenDangNhap);
	}

	public boolean them(TaiKhoan taiKhoan) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_THEM)) {
			ps.setString(1, taiKhoan.getTenDangNhap());
			ps.setString(2, taiKhoan.getMatKhau());
			ps.setString(3, taiKhoan.getVaiTro());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public String layEmailTheoTaiKhoan(String tenDangNhap) {
		TaiKhoan taiKhoan = timTheoTenDangNhap(tenDangNhap);
		return taiKhoan == null ? null : taiKhoan.getEmail();
	}

	public boolean xoa(String tenDangNhap) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, tenDangNhap);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
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

	private TaiKhoan docTaiKhoan(ResultSet rs) throws SQLException {
		return new TaiKhoan(
			rs.getString("username"),
			rs.getString("matKhau"),
			rs.getString("email"),
			rs.getString("hoTen"),
			rs.getString("vaiTro")
		);
	}


}
