package dao;

import connectDB.Database;
import entity.Tau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Tau_DAO {
	private static final String SQL_LAY_TAT_CA = "SELECT maTau, tenTau, soLuongToa FROM Tau ORDER BY maTau";
	private static final String SQL_LAY_THEO_MA = "SELECT maTau, tenTau, soLuongToa FROM Tau WHERE maTau = ?";
	private static final String SQL_THEM = "INSERT INTO Tau (maTau, tenTau, soLuongToa) VALUES (?, ?, ?)";
	private static final String SQL_CAP_NHAT = "UPDATE Tau SET tenTau = ?, soLuongToa = ? WHERE maTau = ?";
	private static final String SQL_XOA = "DELETE FROM Tau WHERE maTau = ?";

	public List<Tau> layTatCa() {
		List<Tau> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_TAT_CA);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ds.add(docTau(rs));
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public Tau layTheoMa(String maTau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_MA)) {
			ps.setString(1, maTau);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? docTau(rs) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public boolean them(Tau tau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_THEM)) {
			ps.setString(1, tau.getMaTau());
			ps.setString(2, tau.getTenTau());
			ps.setInt(3, tau.getSoLuongToa());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean capNhat(Tau tau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_CAP_NHAT)) {
			ps.setString(1, tau.getTenTau());
			ps.setInt(2, tau.getSoLuongToa());
			ps.setString(3, tau.getMaTau());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean xoa(String maTau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, maTau);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	private Tau docTau(ResultSet rs) throws SQLException {
		return new Tau(rs.getString("maTau"), rs.getString("tenTau"), rs.getInt("soLuongToa"));
	}
}