package dao;

import connectDB.DatabaseConnection;
import entity.Tau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Tau_DAO {
	public List<Tau> layTatCaTau() {
		List<Tau> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return danhSach;
			}
			String sql = "SELECT maTau, tenTau, soLuongToa FROM Tau ORDER BY maTau";
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					danhSach.add(new Tau(rs.getString("maTau"), rs.getString("tenTau"), rs.getInt("soLuongToa")));
				}
			}
		} catch (java.sql.SQLException e) {
			System.err.println("[Tau_DAO] Lỗi lấy danh sách tàu: " + e.getMessage());
		}
		return danhSach;
	}

	public Tau timTheoMa(String maTau) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return null;
			}
			String sql = "SELECT maTau, tenTau, soLuongToa FROM Tau WHERE maTau = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maTau);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return new Tau(rs.getString("maTau"), rs.getString("tenTau"), rs.getInt("soLuongToa"));
					}
				}
			}
		} catch (java.sql.SQLException e) {
			System.err.println("[Tau_DAO] Lỗi tìm tàu theo mã: " + e.getMessage());
		}
		return null;
	}

	public boolean themTau(Tau tau) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || tau == null) {
				return false;
			}
			String sql = "INSERT INTO Tau(maTau, tenTau, soLuongToa) VALUES(?,?,?)";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, tau.getMaTau());
				ps.setString(2, tau.getTenTau());
				ps.setInt(3, tau.getSoLuongToa());
				return ps.executeUpdate() > 0;
			}
		} catch (java.sql.SQLException e) {
			System.err.println("[Tau_DAO] Lỗi thêm tàu: " + e.getMessage());
			return false;
		}
	}

	public boolean capNhatTau(Tau tau) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || tau == null) {
				return false;
			}
			String sql = "UPDATE Tau SET tenTau = ?, soLuongToa = ? WHERE maTau = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, tau.getTenTau());
				ps.setInt(2, tau.getSoLuongToa());
				ps.setString(3, tau.getMaTau());
				return ps.executeUpdate() > 0;
			}
		} catch (java.sql.SQLException e) {
			System.err.println("[Tau_DAO] Lỗi cập nhật tàu: " + e.getMessage());
			return false;
		}
	}

	public boolean xoaTau(String maTau) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || maTau == null || maTau.isBlank()) {
				return false;
			}
			String sql = "DELETE FROM Tau WHERE maTau = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maTau);
				return ps.executeUpdate() > 0;
			}
		} catch (java.sql.SQLException e) {
			System.err.println("[Tau_DAO] Lỗi xóa tàu: " + e.getMessage());
			return false;
		}
	}

	public String layMaTauTiepTheo() {
		int max = 0;
		for (Tau tau : layTatCaTau()) {
			String ma = tau.getMaTau();
			if (ma != null && ma.startsWith("TAU")) {
				try {
					max = Math.max(max, Integer.parseInt(ma.substring(3)));
				} catch (NumberFormatException ignored) {
				}
			}
		}
		return String.format("TAU%03d", max + 1);
	}
}
