package dao;

import connectDB.Database;
import entity.DichVu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DichVu_DAO {
	private static final String SQL_LAY_TAT_CA = "SELECT maDV, tenDV, trangThai, giaTien FROM DichVu ORDER BY maDV";
	private static final String SQL_LAY_THEO_MA = "SELECT maDV, tenDV, trangThai, giaTien FROM DichVu WHERE maDV = ?";
	private static final String SQL_THEM = "INSERT INTO DichVu (maDV, tenDV, trangThai, giaTien) VALUES (?, ?, ?, ?)";
	private static final String SQL_CAP_NHAT = "UPDATE DichVu SET tenDV = ?, trangThai = ?, giaTien = ? WHERE maDV = ?";
	private static final String SQL_XOA = "DELETE FROM DichVu WHERE maDV = ?";

	public List<DichVu> layTatCa() {
		List<DichVu> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_TAT_CA);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ds.add(docDichVu(rs));
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public DichVu layTheoMa(String maDV) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_MA)) {
			ps.setString(1, maDV);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? docDichVu(rs) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public boolean them(DichVu dichVu) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_THEM)) {
			ganDichVu(ps, dichVu, true);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean capNhat(DichVu dichVu) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_CAP_NHAT)) {
			ganDichVu(ps, dichVu, false);
			ps.setString(4, dichVu.getMaDV());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean xoa(String maDV) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, maDV);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	private void ganDichVu(PreparedStatement ps, DichVu dv, boolean coMa) throws SQLException {
		int index = 1;
		if (coMa) {
			ps.setString(index++, dv.getMaDV());
		}
		ps.setString(index++, dv.getTenDV());
		ps.setBoolean(index++, dv.isTrangThai());
		ps.setBigDecimal(index, dv.getGiaDV());
	}

	private DichVu docDichVu(ResultSet rs) throws SQLException {
		return new DichVu(rs.getString("maDV"), rs.getString("tenDV"), rs.getBoolean("trangThai"), rs.getBigDecimal("giaTien"));
	}
}