package dao;

import connectDB.Database;
import entity.ChiTietVeTau;
import entity.VeTau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VeTau_DAO {
	private static final String SQL_LAY_TAT_CA = "SELECT maVeTau, maKH, maCT, maToa, giaVe, trangThai FROM VeTau ORDER BY maVeTau";
	private static final String SQL_LAY_THEO_MA = "SELECT maVeTau, maKH, maCT, maToa, giaVe, trangThai FROM VeTau WHERE maVeTau = ?";
	private static final String SQL_LAY_THEO_KH = "SELECT maVeTau, maKH, maCT, maToa, giaVe, trangThai FROM VeTau WHERE maKH = ? ORDER BY maVeTau";
	private static final String SQL_LAY_THEO_TRANG_THAI = "SELECT maVeTau, maKH, maCT, maToa, giaVe, trangThai FROM VeTau WHERE trangThai = ? ORDER BY maVeTau";
	private static final String SQL_THEM = "INSERT INTO VeTau (maVeTau, maKH, maCT, maToa, giaVe, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String SQL_CAP_NHAT_TRANG_THAI = "UPDATE VeTau SET trangThai = ? WHERE maVeTau = ?";
	private static final String SQL_CAP_NHAT = "UPDATE VeTau SET maKH = ?, maCT = ?, maToa = ?, giaVe = ?, trangThai = ? WHERE maVeTau = ?";
	private static final String SQL_XOA = "DELETE FROM VeTau WHERE maVeTau = ?";

	private final ChiTietVeTau_DAO chiTietVeTauDAO = new ChiTietVeTau_DAO();

	public List<VeTau> layTatCa() {
		return docDanhSach(SQL_LAY_TAT_CA, null, null);
	}

	public VeTau layTheoMa(String maVeTau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_LAY_THEO_MA)) {
			ps.setString(1, maVeTau);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? ganChiTiet(docVeTau(rs)) : null;
			}
		} catch (SQLException ex) {
			return null;
		}
	}

	public List<VeTau> layTheoKhachHang(String maKH) {
		return docDanhSach(SQL_LAY_THEO_KH, maKH, null);
	}

	public List<VeTau> layTheoTrangThai(String trangThai) {
		return docDanhSach(SQL_LAY_THEO_TRANG_THAI, trangThai, null);
	}

	public boolean them(VeTau veTau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_THEM)) {
			ganVeTau(ps, veTau, true);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean capNhatTrangThai(String maVeTau, String trangThai) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_CAP_NHAT_TRANG_THAI)) {
			ps.setString(1, trangThai);
			ps.setString(2, maVeTau);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean capNhat(VeTau veTau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_CAP_NHAT)) {
			ganVeTau(ps, veTau, false);
			ps.setString(6, veTau.getMaVeTau());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean xoa(String maVeTau) {
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SQL_XOA)) {
			ps.setString(1, maVeTau);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			return false;
		}
	}

	private List<VeTau> docDanhSach(String sql, String param1, String param2) {
		List<VeTau> ds = new ArrayList<>();
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			if (param1 != null) {
				ps.setString(1, param1);
			}
			if (param2 != null) {
				ps.setString(2, param2);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ds.add(docVeTau(rs));
				}
			}
			return ds;
		} catch (SQLException ex) {
			return List.of();
		}
	}

	private void ganVeTau(PreparedStatement ps, VeTau veTau, boolean coMa) throws SQLException {
		int index = 1;
		if (coMa) {
			ps.setString(index++, veTau.getMaVeTau());
		}
		ps.setString(index++, veTau.getMaKH());
		ps.setString(index++, veTau.getMaChuyenTau());
		ps.setString(index++, veTau.getMaToa());
		ps.setBigDecimal(index++, veTau.getGiaVe());
		ps.setString(index, veTau.getTrangThai());
	}

	private VeTau docVeTau(ResultSet rs) throws SQLException {
		VeTau veTau = new VeTau();
		veTau.setMaVeTau(rs.getString("maVeTau"));
		veTau.setMaKH(rs.getString("maKH"));
		veTau.setMaChuyenTau(rs.getString("maCT"));
		veTau.setMaToa(rs.getString("maToa"));
		veTau.setGiaVe(rs.getBigDecimal("giaVe") == null ? BigDecimal.ZERO : rs.getBigDecimal("giaVe"));
		veTau.setTrangThai(rs.getString("trangThai"));
		veTau.setDsChiTietVeTau(new ArrayList<ChiTietVeTau>(chiTietVeTauDAO.layTheoMaVe(veTau.getMaVeTau())));
		veTau.setSoLuongVe(veTau.getDsChiTietVeTau().size());
		return veTau;
	}

	private VeTau ganChiTiet(VeTau veTau) {
		veTau.setDsChiTietVeTau(new ArrayList<ChiTietVeTau>(chiTietVeTauDAO.layTheoMaVe(veTau.getMaVeTau())));
		veTau.setSoLuongVe(veTau.getDsChiTietVeTau().size());
		return veTau;
	}
}