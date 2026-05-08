package dao;

import connectDB.Database;
import entity.ChiTietVeTau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietVeTau_DAO {
	private static final String SQL_LAY_THEO_VE = "SELECT maChiTiet, maVeTau, tenHanhKhach, CCCD, ngaySinh, viTriGhe, loaiVe, giaVeTheoLoai FROM ChiTietVeTau WHERE maVeTau = ? ORDER BY maChiTiet";
	private static final String SQL_LAY_THEO_MA = "SELECT maChiTiet, maVeTau, tenHanhKhach, CCCD, ngaySinh, viTriGhe, loaiVe, giaVeTheoLoai FROM ChiTietVeTau WHERE maChiTiet = ?";
	private static final String SQL_THEM = """
			INSERT INTO ChiTietVeTau (maChiTiet, maVeTau, tenHanhKhach, CCCD, ngaySinh, viTriGhe, loaiVe, giaVeTheoLoai)
			VALUES (?, ?, ?, ?, ?, ?, ?, ?)
			""";
	private static final String SQL_XOA = "DELETE FROM ChiTietVeTau WHERE maChiTiet = ?";
	private static final String SQL_GHE_DA_DAT = "SELECT viTriGhe FROM ChiTietVeTau ct JOIN VeTau vt ON vt.maVeTau = ct.maVeTau WHERE vt.maCT = ? AND vt.maToa = ?";

	public List<ChiTietVeTau> layTheoMaVe(String maVeTau) {
		List<ChiTietVeTau> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_VE)) {
			ps.setString(1, maVeTau);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ds.add(docChiTiet(rs));
				}
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	public ChiTietVeTau layTheoMa(String maChiTiet) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_MA)) {
			ps.setString(1, maChiTiet);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? docChiTiet(rs) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public boolean them(ChiTietVeTau chiTietVeTau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_THEM)) {
			ganChiTiet(ps, chiTietVeTau, true);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean xoa(String maChiTiet) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, maChiTiet);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public List<String> layGheDaDat(String maChuyenTau, String maToa) {
		List<String> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_GHE_DA_DAT)) {
			ps.setString(1, maChuyenTau);
			ps.setString(2, maToa);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ds.add(rs.getString("viTriGhe"));
				}
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	private void ganChiTiet(PreparedStatement ps, ChiTietVeTau ct, boolean coMa) throws SQLException {
		int index = 1;
		if (coMa) {
			ps.setString(index++, ct.getMaChiTiet());
		}
		ps.setString(index++, ct.getMaVeTau());
		ps.setString(index++, ct.getTenHanhKhach());
		ps.setString(index++, ct.getCccd());
		ps.setDate(index++, DaoUtils.sqlDate(ct.getNgaySinh()));
		ps.setString(index++, ct.getViTriGhe());
		ps.setString(index++, ct.getLoaiVe());
		ps.setBigDecimal(index, ct.getGiaVeTheoLoai());
	}

	private ChiTietVeTau docChiTiet(ResultSet rs) throws SQLException {
		ChiTietVeTau ct = new ChiTietVeTau();
		ct.setMaChiTiet(rs.getString("maChiTiet"));
		ct.setMaVeTau(rs.getString("maVeTau"));
		ct.setTenHanhKhach(rs.getString("tenHanhKhach"));
		ct.setCccd(rs.getString("CCCD"));
		ct.setNgaySinh(DaoUtils.localDate(rs.getDate("ngaySinh")));
		ct.setViTriGhe(rs.getString("viTriGhe"));
		ct.setLoaiVe(rs.getString("loaiVe"));
		ct.setGiaVeTheoLoai(rs.getBigDecimal("giaVeTheoLoai"));
		return ct;
	}
}