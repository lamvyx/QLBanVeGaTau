package dao;

import connectDB.Database;
import entity.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_DAO {
	private static final String SQL_LAY_TAT_CA = """
			SELECT maNV, tenNV, email, sdt, gioiTinh, ngaySinh, ngayVaoLam, chucVu, trangThai, username, '' AS hinhAnh
			FROM NhanVien
			ORDER BY maNV
			""";
	private static final String SQL_LAY_THEO_MA = """
			SELECT maNV, tenNV, email, sdt, gioiTinh, ngaySinh, ngayVaoLam, chucVu, trangThai, username, '' AS hinhAnh
			FROM NhanVien
			WHERE maNV = ?
			""";
	private static final String SQL_LAY_THEO_USERNAME = """
			SELECT maNV, tenNV, email, sdt, gioiTinh, ngaySinh, ngayVaoLam, chucVu, trangThai, username, '' AS hinhAnh
			FROM NhanVien
			WHERE username = ?
			""";
	private static final String SQL_THEM_TAI_KHOAN = "INSERT INTO TaiKhoan (username, [password], vaiTro) VALUES (?, ?, ?)";
	private static final String SQL_THEM_NHAN_VIEN = """
			INSERT INTO NhanVien (maNV, tenNV, sdt, gioiTinh, ngaySinh, ngayVaoLam, trangThai, email, chucVu, username)
			VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			""";
	private static final String SQL_CAP_NHAT = """
			UPDATE NhanVien
			SET tenNV = ?, sdt = ?, gioiTinh = ?, ngaySinh = ?, ngayVaoLam = ?, trangThai = ?, email = ?, chucVu = ?, username = ?
			WHERE maNV = ?
			""";
	private static final String SQL_XOA = "DELETE FROM NhanVien WHERE maNV = ?";

	public List<NhanVien> layTatCa() {
		List<NhanVien> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_TAT_CA);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ds.add(docNhanVien(rs));
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public NhanVien layTheoMa(String maNV) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_MA)) {
			ps.setString(1, maNV);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? docNhanVien(rs) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public NhanVien layTheoUsername(String username) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_USERNAME)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? docNhanVien(rs) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public boolean them(NhanVien nhanVien, String matKhau, String vaiTro) {
		try (Connection conn = Database.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement psTaiKhoan = conn.prepareStatement(SQL_THEM_TAI_KHOAN);
				 PreparedStatement psNhanVien = conn.prepareStatement(SQL_THEM_NHAN_VIEN)) {
				them(conn, nhanVien, matKhau, vaiTro, psTaiKhoan, psNhanVien);
				conn.commit();
				return true;
			} catch (SQLException ex) {
				conn.rollback();
				return false;
			} finally {
				conn.setAutoCommit(true);
			}
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean them(Connection conn, NhanVien nhanVien, String matKhau, String vaiTro) throws SQLException {
		try (PreparedStatement psTaiKhoan = conn.prepareStatement(SQL_THEM_TAI_KHOAN);
			 PreparedStatement psNhanVien = conn.prepareStatement(SQL_THEM_NHAN_VIEN)) {
			return them(conn, nhanVien, matKhau, vaiTro, psTaiKhoan, psNhanVien);
		}
	}

	private boolean them(Connection conn, NhanVien nhanVien, String matKhau, String vaiTro,
			PreparedStatement psTaiKhoan, PreparedStatement psNhanVien) throws SQLException {
		psTaiKhoan.setString(1, nhanVien.getUsername());
		psTaiKhoan.setString(2, matKhau);
		psTaiKhoan.setString(3, vaiTro);
		psTaiKhoan.executeUpdate();

		ganNhanVien(psNhanVien, nhanVien, true);
		psNhanVien.executeUpdate();
		return true;
	}

	public boolean capNhat(NhanVien nhanVien) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_CAP_NHAT)) {
			ganNhanVien(ps, nhanVien, false);
			ps.setString(10, nhanVien.getMaNV());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean xoa(String maNV) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, maNV);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	private void ganNhanVien(PreparedStatement ps, NhanVien nv, boolean coMa) throws SQLException {
		int index = 1;
		if (coMa) {
			ps.setString(index++, nv.getMaNV());
		}
		ps.setString(index++, nv.getTenNV());
		ps.setString(index++, nv.getSdt());
		ps.setBoolean(index++, nv.isGioiTinh());
		ps.setDate(index++, DaoUtils.sqlDate(nv.getNgaySinh()));
		ps.setDate(index++, DaoUtils.sqlDate(nv.getNgayVaoLam()));
		ps.setBoolean(index++, nv.isTrangThai());
		ps.setString(index++, nv.getEmail());
		ps.setString(index++, nv.getChucVu());
		ps.setString(index, nv.getUsername());
	}

	private NhanVien docNhanVien(ResultSet rs) throws SQLException {
		NhanVien nv = new NhanVien();
		nv.setMaNV(rs.getString("maNV"));
		nv.setTenNV(rs.getString("tenNV"));
		nv.setEmail(rs.getString("email"));
		nv.setSdt(rs.getString("sdt"));
		nv.setGioiTinh(rs.getBoolean("gioiTinh"));
		nv.setNgaySinh(DaoUtils.localDate(rs.getDate("ngaySinh")));
		nv.setNgayVaoLam(DaoUtils.localDate(rs.getDate("ngayVaoLam")));
		nv.setChucVu(rs.getString("chucVu"));
		nv.setTrangThai(rs.getBoolean("trangThai"));
		nv.setUsername(rs.getString("username"));
		nv.setHinhAnh(rs.getString("hinhAnh"));
		return nv;
	}
}