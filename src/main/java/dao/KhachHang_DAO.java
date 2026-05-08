package dao;

import connectDB.Database;
import entity.KhachHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {
	private static final String SQL_LAY_TAT_CA = """
			SELECT maKH, tenKH, sdt, CCCD, diaChi, email, gioiTinh, ngaySinh, loaiKH
			FROM KhachHang
			ORDER BY maKH
			""";
	private static final String SQL_LAY_THEO_MA = """
			SELECT maKH, tenKH, sdt, CCCD, diaChi, email, gioiTinh, ngaySinh, loaiKH
			FROM KhachHang
			WHERE maKH = ?
			""";
	private static final String SQL_THEM = """
			INSERT INTO KhachHang (maKH, tenKH, sdt, CCCD, diaChi, email, gioiTinh, ngaySinh, loaiKH)
			VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
			""";
	private static final String SQL_CAP_NHAT = """
			UPDATE KhachHang
			SET tenKH = ?, sdt = ?, CCCD = ?, diaChi = ?, email = ?, gioiTinh = ?, ngaySinh = ?, loaiKH = ?
			WHERE maKH = ?
			""";
	private static final String SQL_XOA = "DELETE FROM KhachHang WHERE maKH = ?";
	private static final String SQL_TIM_KIEM = """
			SELECT maKH, tenKH, sdt, CCCD, diaChi, email, gioiTinh, ngaySinh, loaiKH
			FROM KhachHang
			WHERE maKH LIKE ? OR tenKH LIKE ? OR CCCD LIKE ? OR sdt LIKE ?
			ORDER BY maKH
			""";
	private static final String SQL_KIEM_TRA_CCCD = "SELECT 1 FROM KhachHang WHERE CCCD = ?";

	public List<KhachHang> layTatCa() {
		List<KhachHang> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_TAT_CA);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ds.add(docKhachHang(rs));
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public KhachHang layTheoMa(String maKH) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_MA)) {
			ps.setString(1, maKH);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? docKhachHang(rs) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public List<KhachHang> timKiem(String tuKhoa) {
		String key = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
		List<KhachHang> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_TIM_KIEM)) {
			for (int i = 1; i <= 4; i++) {
				ps.setString(i, key);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ds.add(docKhachHang(rs));
				}
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public boolean them(KhachHang khachHang) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_THEM)) {
			ganKhachHang(ps, khachHang, true);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean capNhat(KhachHang khachHang) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_CAP_NHAT)) {
			ganKhachHang(ps, khachHang, false);
			ps.setString(9, khachHang.getMaKH());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean xoa(String maKH) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, maKH);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean daTonTaiCCCD(String cccd) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_KIEM_TRA_CCCD)) {
			ps.setString(1, cccd);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException ex) {
			return false;
		}
	}

	private void ganKhachHang(PreparedStatement ps, KhachHang kh, boolean coMa) throws SQLException {
		int index = 1;
		if (coMa) {
			ps.setString(index++, kh.getMaKH());
		}
		ps.setString(index++, kh.getTenKH());
		ps.setString(index++, kh.getSdt());
		ps.setString(index++, kh.getCccd());
		ps.setString(index++, kh.getDiaChi());
		ps.setString(index++, kh.getEmail());
		ps.setBoolean(index++, kh.isGioiTinh());
		ps.setDate(index++, DaoUtils.sqlDate(kh.getNgaySinh()));
		ps.setBoolean(index, kh.isLoaiKH());
	}

	private KhachHang docKhachHang(ResultSet rs) throws SQLException {
		KhachHang kh = new KhachHang();
		kh.setMaKH(rs.getString("maKH"));
		kh.setTenKH(rs.getString("tenKH"));
		kh.setSdt(rs.getString("sdt"));
		kh.setCccd(rs.getString("CCCD"));
		kh.setDiaChi(rs.getString("diaChi"));
		kh.setEmail(rs.getString("email"));
		kh.setGioiTinh(rs.getBoolean("gioiTinh"));
		kh.setNgaySinh(DaoUtils.localDate(rs.getDate("ngaySinh")));
		kh.setLoaiKH(rs.getBoolean("loaiKH"));
		return kh;
	}
}