package dao;

import connectDB.Database;
import entity.ChuyenTau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChuyenTau_DAO {
	private static final String SQL_LAY_TAT_CA = "SELECT maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai FROM ChuyenTau ORDER BY ngayKhoiHanh, gioKhoiHanh, maCT";
	private static final String SQL_LAY_THEO_MA = "SELECT maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai FROM ChuyenTau WHERE maCT = ?";
	private static final String SQL_LAY_THEO_NGAY = "SELECT maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai FROM ChuyenTau WHERE ngayKhoiHanh = ? ORDER BY gioKhoiHanh";
	private static final String SQL_THEM = "INSERT INTO ChuyenTau (maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String SQL_CAP_NHAT = "UPDATE ChuyenTau SET maTau = ?, maTuyenTau = ?, ngayKhoiHanh = ?, gioKhoiHanh = ?, trangThai = ? WHERE maCT = ?";
	private static final String SQL_XOA = "DELETE FROM ChuyenTau WHERE maCT = ?";

	public List<ChuyenTau> layTatCa() {
		List<ChuyenTau> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_TAT_CA);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ds.add(docChuyenTau(rs));
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public ChuyenTau layTheoMa(String maCT) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_MA)) {
			ps.setString(1, maCT);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? docChuyenTau(rs) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public List<ChuyenTau> layTheoNgay(LocalDate ngayKhoiHanh) {
		List<ChuyenTau> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_NGAY)) {
			ps.setDate(1, DaoUtils.sqlDate(ngayKhoiHanh));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ds.add(docChuyenTau(rs));
				}
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public boolean them(ChuyenTau chuyenTau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_THEM)) {
			ganChuyenTau(ps, chuyenTau, true);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean capNhat(ChuyenTau chuyenTau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_CAP_NHAT)) {
			ganChuyenTau(ps, chuyenTau, false);
			ps.setString(6, chuyenTau.getMaCT());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean xoa(String maCT) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, maCT);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	private void ganChuyenTau(PreparedStatement ps, ChuyenTau ct, boolean coMa) throws SQLException {
		int index = 1;
		if (coMa) {
			ps.setString(index++, ct.getMaCT());
		}
		ps.setString(index++, ct.getMaTau());
		ps.setString(index++, ct.getMaTuyenTau());
		ps.setDate(index++, DaoUtils.sqlDate(ct.getNgayKhoiHanh() == null ? null : ct.getNgayKhoiHanh().toLocalDate()));
		ps.setTime(index++, DaoUtils.sqlTime(ct.getGioKhoiHanh()));
		ps.setBoolean(index, ct.isTrangThai());
	}

	private ChuyenTau docChuyenTau(ResultSet rs) throws SQLException {
		ChuyenTau ct = new ChuyenTau();
		ct.setMaCT(rs.getString("maCT"));
		ct.setMaTau(rs.getString("maTau"));
		ct.setMaTuyenTau(rs.getString("maTuyenTau"));
		ct.setNgayKhoiHanh(DaoUtils.localDateTime(rs.getDate("ngayKhoiHanh"), rs.getTime("gioKhoiHanh")));
		ct.setGioKhoiHanh(DaoUtils.localDateTime(rs.getDate("ngayKhoiHanh"), rs.getTime("gioKhoiHanh")));
		ct.setTrangThai(rs.getBoolean("trangThai"));
		return ct;
	}
}