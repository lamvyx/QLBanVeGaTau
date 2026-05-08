package dao;

import connectDB.Database;
import entity.HoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO {
	private static final String SQL_LAY_TAT_CA = "SELECT maHD, maNV, maKH, maKM, thoiGian FROM HoaDon ORDER BY thoiGian DESC, maHD DESC";
	private static final String SQL_LAY_THEO_MA = "SELECT maHD, maNV, maKH, maKM, thoiGian FROM HoaDon WHERE maHD = ?";
	private static final String SQL_THEM = "INSERT INTO HoaDon (maHD, maNV, maKH, maKM, thoiGian) VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_XOA = "DELETE FROM HoaDon WHERE maHD = ?";

	public List<HoaDon> layTatCa() {
		List<HoaDon> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_TAT_CA);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ds.add(docHoaDon(rs));
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public HoaDon layTheoMa(String maHD) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_MA)) {
			ps.setString(1, maHD);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? docHoaDon(rs) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public boolean them(HoaDon hoaDon) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_THEM)) {
			ps.setString(1, hoaDon.getMaHD());
			ps.setString(2, hoaDon.getMaNV());
			ps.setString(3, hoaDon.getMaKH());
			ps.setString(4, hoaDon.getMaKM());
			ps.setTimestamp(5, DaoUtils.sqlTimestamp(hoaDon.getThoiGian()));
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean xoa(String maHD) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, maHD);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	private HoaDon docHoaDon(ResultSet rs) throws SQLException {
		HoaDon hoaDon = new HoaDon();
		hoaDon.setMaHD(rs.getString("maHD"));
		hoaDon.setMaNV(rs.getString("maNV"));
		hoaDon.setMaKH(rs.getString("maKH"));
		hoaDon.setMaKM(rs.getString("maKM"));
		java.sql.Timestamp ts = rs.getTimestamp("thoiGian");
		hoaDon.setThoiGian(ts == null ? null : ts.toLocalDateTime());
		return hoaDon;
	}
}