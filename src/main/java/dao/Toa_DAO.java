package dao;

import connectDB.DatabaseConnection;
import entity.Toa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
