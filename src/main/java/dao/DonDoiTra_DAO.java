package dao;

import connectDB.DatabaseConnection;
import entity.DonDoiTra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DonDoiTra_DAO {
	public DonDoiTra_DAO() {
		khoiTaoBangNeuChuaCo();
	}

	private void khoiTaoBangNeuChuaCo() {
		String sql = "IF OBJECT_ID('DonDoiTra', 'U') IS NULL\n" +
				"CREATE TABLE DonDoiTra (\n" +
				"  maDon VARCHAR(20) PRIMARY KEY,\n" +
				"  maVeCu VARCHAR(10) NOT NULL,\n" +
				"  maVeMoi VARCHAR(10) NULL,\n" +
				"  loaiDon VARCHAR(10) NOT NULL,\n" +
				"  trangThai VARCHAR(20) NOT NULL,\n" +
				"  ghiChu NVARCHAR(255) NULL,\n" +
				"  thoiGianTao DATETIME NOT NULL DEFAULT GETDATE(),\n" +
				"  thoiGianXacNhan DATETIME NULL\n" +
				")";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return;
			}
			try (Statement st = conn.createStatement()) {
				st.execute(sql);
			}
		} catch (Exception e) {
			System.err.println("[DonDoiTra_DAO] Lỗi khởi tạo bảng DonDoiTra: " + e.getMessage());
		}
	}

	public String layMaDonTiepTheo() {
		int max = 0;
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return "DDT001";
			}
			String sql = "SELECT maDon FROM DonDoiTra";
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String ma = rs.getString(1);
					if (ma != null && ma.startsWith("DDT")) {
						try {
							max = Math.max(max, Integer.parseInt(ma.substring(3)));
						} catch (NumberFormatException ignored) {
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[DonDoiTra_DAO] Lỗi lấy mã đơn đổi trả tiếp theo: " + e.getMessage());
		}
		return String.format("DDT%03d", max + 1);
	}

	public boolean taoDon(DonDoiTra don) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || don == null) {
				return false;
			}
			String sql = "INSERT INTO DonDoiTra(maDon, maVeCu, maVeMoi, loaiDon, trangThai, ghiChu, thoiGianTao) VALUES(?,?,?,?,?,?,?)";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, don.getMaDon());
				ps.setString(2, don.getMaVeCu());
				ps.setString(3, don.getMaVeMoi());
				ps.setString(4, don.getLoaiDon());
				ps.setString(5, don.getTrangThai());
				ps.setString(6, don.getGhiChu());
				ps.setTimestamp(7, Timestamp.valueOf(don.getThoiGianTao() != null ? don.getThoiGianTao() : LocalDateTime.now()));
				return ps.executeUpdate() > 0;
			}
		} catch (Exception e) {
			System.err.println("[DonDoiTra_DAO] Lỗi tạo đơn đổi trả: " + e.getMessage());
			return false;
		}
	}

	public DonDoiTra timTheoMaDon(String maDon) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return null;
			}
			String sql = "SELECT maDon, maVeCu, maVeMoi, loaiDon, trangThai, ghiChu, thoiGianTao, thoiGianXacNhan FROM DonDoiTra WHERE maDon = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maDon);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						Timestamp tao = rs.getTimestamp("thoiGianTao");
						Timestamp xacNhan = rs.getTimestamp("thoiGianXacNhan");
						return new DonDoiTra(rs.getString("maDon"), rs.getString("maVeCu"), rs.getString("maVeMoi"),
								rs.getString("loaiDon"), rs.getString("trangThai"), rs.getString("ghiChu"),
								tao != null ? tao.toLocalDateTime() : null,
								xacNhan != null ? xacNhan.toLocalDateTime() : null);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[DonDoiTra_DAO] Lỗi tìm đơn đổi trả: " + e.getMessage());
		}
		return null;
	}

	public boolean xacNhanDon(String maDon, String trangThaiMoi) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return false;
			}
			String sql = "UPDATE DonDoiTra SET trangThai = ?, thoiGianXacNhan = ? WHERE maDon = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, trangThaiMoi);
				ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
				ps.setString(3, maDon);
				return ps.executeUpdate() > 0;
			}
		} catch (Exception e) {
			System.err.println("[DonDoiTra_DAO] Lỗi xác nhận đơn đổi trả: " + e.getMessage());
			return false;
		}
	}
}
