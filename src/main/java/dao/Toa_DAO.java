package dao;

import connectDB.DatabaseConnection;
import entity.Toa;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Toa_DAO {
	public List<Toa> layTatCaToa() {
		List<Toa> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return danhSach;
			}
			String sql = "SELECT maToa, loaiToa, soGhe, viTriToa, trangThai, maTau FROM Toa ORDER BY maToa";
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					danhSach.add(new Toa(
						rs.getString("maToa"),
						rs.getString("loaiToa"),
						rs.getInt("soGhe"),
						rs.getString("viTriToa"),
						rs.getBoolean("trangThai"),
						rs.getString("maTau")));
				}
			}
		} catch (SQLException e) {
			System.err.println("[Toa_DAO] Lỗi lấy danh sách toa: " + e.getMessage());
		}
		return danhSach;
	}

	public Toa timTheoMa(String maToa) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return null;
			}
			String sql = "SELECT maToa, loaiToa, soGhe, viTriToa, trangThai, maTau FROM Toa WHERE maToa = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maToa);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return new Toa(
							rs.getString("maToa"),
							rs.getString("loaiToa"),
							rs.getInt("soGhe"),
							rs.getString("viTriToa"),
							rs.getBoolean("trangThai"),
							rs.getString("maTau"));
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[Toa_DAO] Lỗi tìm toa theo mã: " + e.getMessage());
		}
		return null;
	}

	public boolean capNhatToa(Toa toa) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || toa == null) {
				return false;
			}
			String sql = "UPDATE Toa SET maTau = ?, loaiToa = ?, soGhe = ?, viTriToa = ?, trangThai = ? WHERE maToa = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, toa.getMaTau());
				ps.setString(2, toa.getLoaiToa());
				ps.setInt(3, toa.getSoGhe());
				ps.setString(4, toa.getViTriToa());
				ps.setBoolean(5, toa.isTrangThai());
				ps.setString(6, toa.getMaToa());
				return ps.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			System.err.println("[Toa_DAO] Lỗi cập nhật toa: " + e.getMessage());
			return false;
		}
	}

	public boolean xoaToa(String maToa) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || maToa == null || maToa.isBlank()) {
				return false;
			}
			String sql = "DELETE FROM Toa WHERE maToa = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maToa);
				return ps.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			System.err.println("[Toa_DAO] Lỗi xóa toa: " + e.getMessage());
			return false;
		}
	}

	public boolean themToa(Toa toa) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || toa == null) {
				return false;
			}
			String sql = "INSERT INTO Toa (maToa, loaiToa, soGhe, viTriToa, trangThai, maTau) VALUES (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, toa.getMaToa());
				ps.setString(2, toa.getLoaiToa());
				ps.setInt(3, toa.getSoGhe());
				ps.setString(4, toa.getViTriToa());
				ps.setBoolean(5, toa.isTrangThai());
				ps.setString(6, toa.getMaTau());
				return ps.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			System.err.println("[Toa_DAO] Lỗi thêm toa: " + e.getMessage());
			return false;
		}
	}

	public List<String> layDanhSachMaTau() {
		List<String> ds = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return ds;
			}
			String sql = "SELECT maTau FROM Tau ORDER BY maTau";
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ds.add(rs.getString("maTau"));
				}
			}
		} catch (SQLException e) {
			System.err.println("[Toa_DAO] Lỗi lấy danh sách mã tàu: " + e.getMessage());
		}
		return ds;
	}

	public List<Toa> layToaTheoTau(String maTau) {
		List<Toa> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || maTau == null || maTau.isBlank()) {
				return danhSach;
			}
			String sql = "SELECT maToa, loaiToa, soGhe, viTriToa, trangThai, maTau "
					+ "FROM Toa WHERE maTau = ? ORDER BY TRY_CAST(viTriToa AS INT), maToa";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maTau.trim());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						danhSach.add(new Toa(
								rs.getString("maToa"),
								rs.getString("loaiToa"),
								rs.getInt("soGhe"),
								rs.getString("viTriToa"),
								rs.getBoolean("trangThai"),
								rs.getString("maTau")));
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[Toa_DAO] Lỗi lấy toa theo tàu: " + e.getMessage());
		}
		return danhSach;
	}

	public List<Toa> layToaTheoChuyenTau(String maCT) {
		List<Toa> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || maCT == null || maCT.isBlank()) {
				return danhSach;
			}
			String sql = "SELECT t.maToa, t.loaiToa, t.soGhe, t.viTriToa, t.trangThai, t.maTau "
					+ "FROM ChiTietChuyenTau_Toa cttoa "
					+ "JOIN Toa t ON t.maToa = cttoa.maToa "
					+ "WHERE cttoa.maCT = ? "
					+ "ORDER BY cttoa.thuTuToa, TRY_CAST(t.viTriToa AS INT), t.maToa";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maCT.trim());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						danhSach.add(new Toa(
								rs.getString("maToa"),
								rs.getString("loaiToa"),
								rs.getInt("soGhe"),
								rs.getString("viTriToa"),
								rs.getBoolean("trangThai"),
								rs.getString("maTau")));
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[Toa_DAO] Lỗi lấy toa theo chuyến tàu: " + e.getMessage());
		}
		return danhSach;
	}

	public List<Toa> layToaRanhTheoThoiDiem(LocalDateTime thoiDiem, String maCTBoQua) {
		List<Toa> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return danhSach;
			}
			StringBuilder sql = new StringBuilder(
					"SELECT t.maToa, t.loaiToa, t.soGhe, t.viTriToa, t.trangThai, t.maTau "
					+ "FROM Toa t "
					+ "WHERE t.trangThai = 1 AND NOT EXISTS ("
					+ "SELECT 1 FROM ChiTietChuyenTau_Toa ctt "
					+ "JOIN ChuyenTau ct ON ct.maCT = ctt.maCT "
					+ "WHERE ctt.maToa = t.maToa AND ct.trangThai IN ('DA_LEN_LICH', 'DANG_CHAY')");
			if (maCTBoQua != null && !maCTBoQua.isBlank()) {
				sql.append(" AND ct.maCT <> ?");
			}
			sql.append(") ORDER BY TRY_CAST(t.viTriToa AS INT), t.maToa");
			try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
				if (maCTBoQua != null && !maCTBoQua.isBlank()) {
					ps.setString(1, maCTBoQua.trim());
				}
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						danhSach.add(new Toa(
								rs.getString("maToa"),
								rs.getString("loaiToa"),
								rs.getInt("soGhe"),
								rs.getString("viTriToa"),
								rs.getBoolean("trangThai"),
								rs.getString("maTau")));
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[Toa_DAO] Lỗi lấy toa rảnh theo thời điểm: " + e.getMessage());
		}
		return danhSach;
	}

}
