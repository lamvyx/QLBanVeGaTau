package dao;

import connectDB.Database;
import entity.TuyenTau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TuyenTau_DAO {
	private static final String SQL_LAY_TAT_CA = "SELECT maTT, maGaDi, maGaDen, khoangCach FROM TuyenTau ORDER BY maTT";
	private static final String SQL_LAY_THEO_MA = "SELECT maTT, maGaDi, maGaDen, khoangCach FROM TuyenTau WHERE maTT = ?";
	private static final String SQL_THEM = "INSERT INTO TuyenTau (maTT, maGaDi, maGaDen, khoangCach) VALUES (?, ?, ?, ?)";
	private static final String SQL_CAP_NHAT = "UPDATE TuyenTau SET maGaDi = ?, maGaDen = ?, khoangCach = ? WHERE maTT = ?";
	private static final String SQL_XOA = "DELETE FROM TuyenTau WHERE maTT = ?";

	public List<TuyenTau> layTatCa() {
		List<TuyenTau> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_TAT_CA);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ds.add(docTuyenTau(rs));
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public TuyenTau layTheoMa(String maTT) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_MA)) {
			ps.setString(1, maTT);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? docTuyenTau(rs) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public boolean them(TuyenTau tuyenTau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_THEM)) {
			ps.setString(1, tuyenTau.getMaTT());
			ps.setString(2, tuyenTau.getMaGaDi());
			ps.setString(3, tuyenTau.getMaGaDen());
			ps.setDouble(4, tuyenTau.getKhoangCach());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean capNhat(TuyenTau tuyenTau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_CAP_NHAT)) {
			ps.setString(1, tuyenTau.getMaGaDi());
			ps.setString(2, tuyenTau.getMaGaDen());
			ps.setDouble(3, tuyenTau.getKhoangCach());
			ps.setString(4, tuyenTau.getMaTT());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean xoa(String maTT) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, maTT);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	private TuyenTau docTuyenTau(ResultSet rs) throws SQLException {
		return new TuyenTau(rs.getString("maTT"), rs.getString("maGaDi"), rs.getString("maGaDen"), rs.getDouble("khoangCach"));
	}
}