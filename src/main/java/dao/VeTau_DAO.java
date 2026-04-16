package dao;

import connectDB.DatabaseConnection;
import entity.VeTau;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VeTau_DAO {
	public VeTau timTheoMaVe(String maVeTau) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return null;
			}
			String sql = "SELECT maVeTau, maKH, maCT, maToa, giaVe, trangThai FROM VeTau WHERE maVeTau = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maVeTau);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						VeTau ve = new VeTau();
						ve.setMaVeTau(rs.getString("maVeTau"));
						ve.setMaKH(rs.getString("maKH"));
						ve.setMaChuyenTau(rs.getString("maCT"));
						ve.setMaToa(rs.getString("maToa"));
						ve.setGiaVe(BigDecimal.valueOf(rs.getDouble("giaVe")));
						ve.setViTriGhe(rs.getString("trangThai"));
						return ve;
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[VeTau_DAO] Lỗi tìm vé theo mã: " + e.getMessage());
		}
		return null;
	}

	public List<VeTau> layVeTheoChuyenTau(String maCT) {
		List<VeTau> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return danhSach;
			}
			String sql = "SELECT maVeTau, maKH, maCT, maToa, giaVe, trangThai FROM VeTau WHERE maCT = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maCT);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						VeTau ve = new VeTau();
						ve.setMaVeTau(rs.getString("maVeTau"));
						ve.setMaKH(rs.getString("maKH"));
						ve.setMaChuyenTau(rs.getString("maCT"));
						ve.setMaToa(rs.getString("maToa"));
						ve.setGiaVe(BigDecimal.valueOf(rs.getDouble("giaVe")));
						ve.setViTriGhe(rs.getString("trangThai"));
						danhSach.add(ve);
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[VeTau_DAO] Lỗi lấy vé theo chuyến tàu: " + e.getMessage());
		}
		return danhSach;
	}

	public boolean capNhatTrangThaiVe(String maVeTau, String trangThai) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return false;
			}
			String sql = "UPDATE VeTau SET trangThai = ? WHERE maVeTau = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, trangThai);
				ps.setString(2, maVeTau);
				return ps.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			System.err.println("[VeTau_DAO] Lỗi cập nhật trạng thái vé: " + e.getMessage());
			return false;
		}
	}

	public int demTongSoVe() {
		return demTheoTrangThai(null);
	}

	public int demTheoTrangThai(String trangThai) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return 0;
			}
			String sql = trangThai == null ? "SELECT COUNT(*) FROM VeTau" : "SELECT COUNT(*) FROM VeTau WHERE trangThai = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				if (trangThai != null) {
					ps.setString(1, trangThai);
				}
				try (ResultSet rs = ps.executeQuery()) {
					return rs.next() ? rs.getInt(1) : 0;
				}
			}
		} catch (SQLException e) {
			System.err.println("[VeTau_DAO] Lỗi đếm vé: " + e.getMessage());
		}
		return 0;
	}
}
