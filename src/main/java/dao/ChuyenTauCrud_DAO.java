package dao;

import connectDB.DatabaseConnection;
import entity.ChuyenTau;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ChuyenTauCrud_DAO {
	public List<ChuyenTau> timKiemChuyenTau(String tuKhoa) {
		List<ChuyenTau> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return danhSach;
			}
			String key = tuKhoa == null ? "" : tuKhoa.trim();
			String sql = "SELECT maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai "
					+ "FROM ChuyenTau WHERE maCT LIKE ? OR maTau LIKE ? OR maTuyenTau LIKE ? "
					+ "ORDER BY ngayKhoiHanh, gioKhoiHanh";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				String pattern = "%" + key + "%";
				ps.setString(1, pattern);
				ps.setString(2, pattern);
				ps.setString(3, pattern);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						danhSach.add(docChuyenTau(rs));
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[ChuyenTauCrud_DAO] Lỗi tìm kiếm chuyến tàu: " + e.getMessage());
		}
		return danhSach;
	}

	public boolean themChuyenTau(ChuyenTau chuyenTau) {
		return themChuyenTau(chuyenTau, null);
	}

	public boolean themChuyenTau(ChuyenTau chuyenTau, List<String> maToaList) {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			if (conn == null || chuyenTau == null) {
				return false;
			}
			conn.setAutoCommit(false);

			String sql = "INSERT INTO ChuyenTau(maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai) "
					+ "VALUES(?,?,?,?,?,?)";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ganThongTinChuyenTau(ps, chuyenTau, false);
				if (ps.executeUpdate() <= 0) {
					conn.rollback();
					return false;
				}
			}

			luuDanhSachToaChoChuyen(conn, chuyenTau.getMaCT(), maToaList);
			conn.commit();
			return true;
		} catch (SQLException e) {
			rollbackQuietly(conn);
			System.err.println("[ChuyenTauCrud_DAO] Lỗi thêm chuyến tàu: " + e.getMessage());
			return false;
		} finally {
			restoreAutoCommit(conn);
		}
	}

	public boolean capNhatChuyenTau(ChuyenTau chuyenTau) {
		return capNhatChuyenTau(chuyenTau, null);
	}

	public boolean capNhatChuyenTau(ChuyenTau chuyenTau, List<String> maToaList) {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			if (conn == null || chuyenTau == null) {
				return false;
			}
			conn.setAutoCommit(false);

			String sql = "UPDATE ChuyenTau SET maTau = ?, maTuyenTau = ?, ngayKhoiHanh = ?, gioKhoiHanh = ?, "
					+ "trangThai = ? WHERE maCT = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ganThongTinChuyenTau(ps, chuyenTau, true);
				if (ps.executeUpdate() <= 0) {
					conn.rollback();
					return false;
				}
			}

			if (maToaList != null) {
				try (PreparedStatement deletePs = conn.prepareStatement("DELETE FROM ChiTietChuyenTau_Toa WHERE maCT = ?")) {
					deletePs.setString(1, chuyenTau.getMaCT());
					deletePs.executeUpdate();
				}
				luuDanhSachToaChoChuyen(conn, chuyenTau.getMaCT(), maToaList);
			}

			conn.commit();
			return true;
		} catch (SQLException e) {
			rollbackQuietly(conn);
			System.err.println("[ChuyenTauCrud_DAO] Lỗi cập nhật chuyến tàu: " + e.getMessage());
			return false;
		} finally {
			restoreAutoCommit(conn);
		}
	}

	public boolean xoaChuyenTau(String maCT) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || maCT == null || maCT.isBlank()) {
				return false;
			}
			String sql = "DELETE FROM ChuyenTau WHERE maCT = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maCT.trim());
				return ps.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			System.err.println("[ChuyenTauCrud_DAO] Lỗi xóa chuyến tàu: " + e.getMessage());
			return false;
		}
	}

	public String layMaChuyenTauTiepTheo() {
		int max = 0;
		for (ChuyenTau ct : timKiemChuyenTau("")) {
			String ma = ct.getMaCT();
			if (ma != null && ma.startsWith("CT")) {
				try {
					max = Math.max(max, Integer.parseInt(ma.substring(2)));
				} catch (NumberFormatException ignored) {
				}
			}
		}
		return String.format("CT%03d", max + 1);
	}

	public List<ChuyenTau> searchChuyenTauByGaAndNgay(String gaDi, String gaDen, LocalDate ngayDi) {
		List<ChuyenTau> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return danhSach;
			}

			StringBuilder sql = new StringBuilder(
					"SELECT ct.maCT, ct.maTau, ct.maTuyenTau, ct.ngayKhoiHanh, ct.gioKhoiHanh, ct.trangThai "
							+ "FROM ChuyenTau ct JOIN TuyenTau tt ON ct.maTuyenTau = tt.maTT "
							+ "WHERE ct.trangThai IN (?, ?)");
			if (gaDi != null && !gaDi.isBlank()) {
				sql.append(" AND tt.maGaDi = ?");
			}
			if (gaDen != null && !gaDen.isBlank()) {
				sql.append(" AND tt.maGaDen = ?");
			}
			if (ngayDi != null) {
				sql.append(" AND ct.ngayKhoiHanh = ?");
			}
			sql.append(" ORDER BY ct.ngayKhoiHanh, ct.gioKhoiHanh");

			try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
				int index = 1;
				ps.setString(index++, ChuyenTau.TRANG_THAI_DA_LEN_LICH);
				ps.setString(index++, ChuyenTau.TRANG_THAI_DANG_CHAY);
				if (gaDi != null && !gaDi.isBlank()) {
					ps.setString(index++, gaDi);
				}
				if (gaDen != null && !gaDen.isBlank()) {
					ps.setString(index++, gaDen);
				}
				if (ngayDi != null) {
					ps.setDate(index, Date.valueOf(ngayDi));
				}
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						danhSach.add(docChuyenTau(rs));
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[ChuyenTauCrud_DAO] Lỗi search nâng cao: " + e.getMessage());
		}
		return danhSach;
	}

	public List<String> layDanhSachMaToaTheoChuyen(String maCT) {
		List<String> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || maCT == null || maCT.isBlank()) {
				return danhSach;
			}
			String sql = "SELECT maToa FROM ChiTietChuyenTau_Toa WHERE maCT = ? ORDER BY thuTuToa, maToa";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maCT.trim());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						danhSach.add(rs.getString(1));
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[ChuyenTauCrud_DAO] Lỗi lấy toa theo chuyến: " + e.getMessage());
		}
		return danhSach;
	}

	private void luuDanhSachToaChoChuyen(Connection conn, String maCT, List<String> maToaList) throws SQLException {
		Set<String> danhSach = new LinkedHashSet<>();
		if (maToaList != null) {
			for (String maToa : maToaList) {
				if (maToa != null && !maToa.isBlank()) {
					danhSach.add(maToa.trim());
				}
			}
		}
		if (danhSach.isEmpty()) {
			throw new SQLException("Chuyến tàu phải có ít nhất một toa.");
		}

		String sql = "INSERT INTO ChiTietChuyenTau_Toa(maCT, maToa, thuTuToa) VALUES(?,?,?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			int thuTu = 1;
			for (String maToa : danhSach) {
				if (!tonTaiToa(conn, maToa)) {
					throw new SQLException("Không tìm thấy toa " + maToa);
				}
				ps.setString(1, maCT);
				ps.setString(2, maToa);
				ps.setInt(3, thuTu++);
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}

	private boolean tonTaiToa(Connection conn, String maToa) throws SQLException {
		String sql = "SELECT 1 FROM Toa WHERE maToa = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maToa);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	private void ganThongTinChuyenTau(PreparedStatement ps, ChuyenTau chuyenTau, boolean updateMode)
			throws SQLException {
		LocalDateTime dt = chuyenTau.getNgayKhoiHanh() != null ? chuyenTau.getNgayKhoiHanh() : LocalDateTime.now();
		if (updateMode) {
			ps.setString(1, chuyenTau.getMaTau());
			ps.setString(2, chuyenTau.getMaTuyenTau());
			ps.setDate(3, Date.valueOf(dt.toLocalDate()));
			ps.setTime(4, Time.valueOf(dt.toLocalTime()));
			ps.setString(5, chuyenTau.getTrangThai());
			ps.setString(6, chuyenTau.getMaCT());
		} else {
			ps.setString(1, chuyenTau.getMaCT());
			ps.setString(2, chuyenTau.getMaTau());
			ps.setString(3, chuyenTau.getMaTuyenTau());
			ps.setDate(4, Date.valueOf(dt.toLocalDate()));
			ps.setTime(5, Time.valueOf(dt.toLocalTime()));
			ps.setString(6, chuyenTau.getTrangThai());
		}
	}

	private ChuyenTau docChuyenTau(ResultSet rs) throws SQLException {
		LocalDate ngay = rs.getDate("ngayKhoiHanh").toLocalDate();
		LocalTime gio = rs.getTime("gioKhoiHanh").toLocalTime();
		LocalDateTime start = LocalDateTime.of(ngay, gio);
		return new ChuyenTau(rs.getString("maCT"), start, start, rs.getString("trangThai"),
				rs.getString("maTau"), rs.getString("maTuyenTau"));
	}

	private void rollbackQuietly(Connection conn) {
		if (conn == null) {
			return;
		}
		try {
			conn.rollback();
		} catch (SQLException ignored) {
		}
	}

	private void restoreAutoCommit(Connection conn) {
		if (conn == null) {
			return;
		}
		try {
			conn.setAutoCommit(true);
		} catch (SQLException ignored) {
		}
	}
}
