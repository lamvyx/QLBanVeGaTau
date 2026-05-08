package dao;

import connectDB.Database;
import entity.KhuyenMai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMai_DAO {
	private static final String SQL_LAY_TAT_CA = "SELECT maKM, tenKM, tyLeKM, ngayBD, ngayKT FROM KhuyenMai ORDER BY maKM";
	private static final String SQL_LAY_THEO_MA = "SELECT maKM, tenKM, tyLeKM, ngayBD, ngayKT FROM KhuyenMai WHERE maKM = ?";
	private static final String SQL_LAY_DANG_HIEU_LUC = """
			SELECT maKM, tenKM, tyLeKM, ngayBD, ngayKT
			FROM KhuyenMai
			WHERE CAST(GETDATE() AS DATE) BETWEEN ngayBD AND ngayKT
			ORDER BY maKM
			""";
	private static final String SQL_THEM = "INSERT INTO KhuyenMai (maKM, tenKM, tyLeKM, ngayBD, ngayKT) VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_CAP_NHAT = "UPDATE KhuyenMai SET tenKM = ?, tyLeKM = ?, ngayBD = ?, ngayKT = ? WHERE maKM = ?";
	private static final String SQL_XOA = "DELETE FROM KhuyenMai WHERE maKM = ?";

	public List<KhuyenMai> layTatCa() {
		return layTheoTruyVan(SQL_LAY_TAT_CA, null);
	}

	public List<KhuyenMai> layDangHieuLuc() {
		return layTheoTruyVan(SQL_LAY_DANG_HIEU_LUC, null);
	}

	public KhuyenMai layTheoMa(String maKM) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_MA)) {
			ps.setString(1, maKM);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? docKhuyenMai(rs) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public boolean them(KhuyenMai khuyenMai) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_THEM)) {
			ganKhuyenMai(ps, khuyenMai, true);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean capNhat(KhuyenMai khuyenMai) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_CAP_NHAT)) {
			ganKhuyenMai(ps, khuyenMai, false);
			ps.setString(5, khuyenMai.getMaKM());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean xoa(String maKM) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, maKM);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	private List<KhuyenMai> layTheoTruyVan(String sql, String ma) {
		List<KhuyenMai> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			if (ma != null) {
				ps.setString(1, ma);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ds.add(docKhuyenMai(rs));
				}
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	private void ganKhuyenMai(PreparedStatement ps, KhuyenMai km, boolean coMa) throws SQLException {
		int index = 1;
		if (coMa) {
			ps.setString(index++, km.getMaKM());
		}
		ps.setString(index++, km.getTenKM());
		ps.setBigDecimal(index++, km.getTyLeKM());
		ps.setDate(index++, DaoUtils.sqlDate(km.getNgayBD()));
		ps.setDate(index, DaoUtils.sqlDate(km.getNgayKT()));
	}

	private KhuyenMai docKhuyenMai(ResultSet rs) throws SQLException {
		KhuyenMai km = new KhuyenMai();
		km.setMaKM(rs.getString("maKM"));
		km.setTenKM(rs.getString("tenKM"));
		km.setTyLeKM(rs.getBigDecimal("tyLeKM"));
		km.setNgayBD(DaoUtils.localDate(rs.getDate("ngayBD")));
		km.setNgayKT(DaoUtils.localDate(rs.getDate("ngayKT")));
		return km;
	}
}