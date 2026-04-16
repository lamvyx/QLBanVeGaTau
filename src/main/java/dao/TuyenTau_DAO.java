package dao;

import connectDB.DatabaseConnection;
import entity.TuyenTau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TuyenTau_DAO {
	public List<TuyenTau> layTatCaTuyenTau() {
		List<TuyenTau> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return danhSach;
			}
			String sql = "SELECT maTT, maGaDi, maGaDen, khoangCach FROM TuyenTau ORDER BY maTT";
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					danhSach.add(new TuyenTau(rs.getString("maTT"), rs.getString("maGaDi"), rs.getString("maGaDen"),
							rs.getDouble("khoangCach")));
				}
			}
		} catch (Exception e) {
			System.err.println("[TuyenTau_DAO] Lỗi lấy danh sách tuyến tàu: " + e.getMessage());
		}
		return danhSach;
	}

	public TuyenTau timTheoMa(String maTT) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return null;
			}
			String sql = "SELECT maTT, maGaDi, maGaDen, khoangCach FROM TuyenTau WHERE maTT = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maTT);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return new TuyenTau(rs.getString("maTT"), rs.getString("maGaDi"), rs.getString("maGaDen"),
								rs.getDouble("khoangCach"));
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[TuyenTau_DAO] Lỗi tìm tuyến tàu theo mã: " + e.getMessage());
		}
		return null;
	}

	public boolean themTuyenTau(TuyenTau tuyenTau) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || tuyenTau == null) {
				return false;
			}
			String sql = "INSERT INTO TuyenTau(maTT, maGaDi, maGaDen, khoangCach) VALUES(?,?,?,?)";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, tuyenTau.getMaTT());
				ps.setString(2, tuyenTau.getMaGaDi());
				ps.setString(3, tuyenTau.getMaGaDen());
				ps.setDouble(4, tuyenTau.getKhoangCach());
				return ps.executeUpdate() > 0;
			}
		} catch (Exception e) {
			System.err.println("[TuyenTau_DAO] Lỗi thêm tuyến tàu: " + e.getMessage());
			return false;
		}
	}

	public boolean capNhatTuyenTau(TuyenTau tuyenTau) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || tuyenTau == null) {
				return false;
			}
			String sql = "UPDATE TuyenTau SET maGaDi = ?, maGaDen = ?, khoangCach = ? WHERE maTT = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, tuyenTau.getMaGaDi());
				ps.setString(2, tuyenTau.getMaGaDen());
				ps.setDouble(3, tuyenTau.getKhoangCach());
				ps.setString(4, tuyenTau.getMaTT());
				return ps.executeUpdate() > 0;
			}
		} catch (Exception e) {
			System.err.println("[TuyenTau_DAO] Lỗi cập nhật tuyến tàu: " + e.getMessage());
			return false;
		}
	}

	public String layMaTuyenTauTiepTheo() {
		int max = 0;
		for (TuyenTau tt : layTatCaTuyenTau()) {
			String ma = tt.getMaTT();
			if (ma != null && ma.startsWith("TT")) {
				try {
					max = Math.max(max, Integer.parseInt(ma.substring(2)));
				} catch (NumberFormatException ignored) {
				}
			}
		}
		return String.format("TT%03d", max + 1);
	}
}
