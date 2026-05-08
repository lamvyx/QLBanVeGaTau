package dao;

import connectDB.Database;
import entity.Toa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Toa_DAO {
	private static final String SQL_LAY_TAT_CA = "SELECT maToa, maTau, loaiToa, soGhe, viTriToa, trangThai FROM Toa ORDER BY maTau, maToa";
	private static final String SQL_LAY_THEO_MA = "SELECT maToa, maTau, loaiToa, soGhe, viTriToa, trangThai FROM Toa WHERE maToa = ?";
	private static final String SQL_LAY_THEO_TAU = "SELECT maToa, maTau, loaiToa, soGhe, viTriToa, trangThai FROM Toa WHERE maTau = ? ORDER BY maToa";
	private static final String SQL_THEM = "INSERT INTO Toa (maToa, maTau, loaiToa, soGhe, viTriToa, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String SQL_CAP_NHAT = "UPDATE Toa SET maTau = ?, loaiToa = ?, soGhe = ?, viTriToa = ?, trangThai = ? WHERE maToa = ?";
	private static final String SQL_XOA = "DELETE FROM Toa WHERE maToa = ?";

	public List<Toa> layTatCa() {
		List<Toa> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_TAT_CA);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ds.add(docToa(rs));
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public List<Toa> layTheoMaTau(String maTau) {
		List<Toa> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_TAU)) {
			ps.setString(1, maTau);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ds.add(docToa(rs));
				}
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public Toa layTheoMa(String maToa) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_MA)) {
			ps.setString(1, maToa);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? docToa(rs) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public boolean them(Toa toa) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_THEM)) {
			ganToa(ps, toa, true);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean capNhat(Toa toa) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_CAP_NHAT)) {
			ganToa(ps, toa, false);
			ps.setString(6, toa.getMaToa());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean xoa(String maToa) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, maToa);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	private void ganToa(PreparedStatement ps, Toa toa, boolean coMa) throws SQLException {
		int index = 1;
		if (coMa) {
			ps.setString(index++, toa.getMaToa());
		}
		ps.setString(index++, toa.getMaTau());
		ps.setString(index++, toa.getLoaiToa());
		ps.setInt(index++, toa.getSoGhe());
		ps.setString(index++, toa.getViTriToa());
		ps.setBoolean(index, toa.isTrangThai());
	}

	private Toa docToa(ResultSet rs) throws SQLException {
		return new Toa(rs.getString("maToa"), rs.getString("loaiToa"), rs.getInt("soGhe"), rs.getString("viTriToa"), rs.getBoolean("trangThai"), rs.getString("maTau"));
	}
}