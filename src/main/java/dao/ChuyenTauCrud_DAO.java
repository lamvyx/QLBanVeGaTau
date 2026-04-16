package dao;

import connectDB.DatabaseConnection;
import entity.ChuyenTau;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ChuyenTauCrud_DAO {
	public List<ChuyenTau> timKiemChuyenTau(String tuKhoa) {
		List<ChuyenTau> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return danhSach;
			}
			String key = tuKhoa == null ? "" : tuKhoa.trim();
			String sql = "SELECT maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai FROM ChuyenTau WHERE maCT LIKE ? OR maTau LIKE ? OR maTuyenTau LIKE ? ORDER BY ngayKhoiHanh, gioKhoiHanh";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				String pattern = "%" + key + "%";
				ps.setString(1, pattern);
				ps.setString(2, pattern);
				ps.setString(3, pattern);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						LocalDate ngay = rs.getDate("ngayKhoiHanh").toLocalDate();
						LocalTime gio = rs.getTime("gioKhoiHanh").toLocalTime();
						LocalDateTime start = LocalDateTime.of(ngay, gio);
						danhSach.add(new ChuyenTau(rs.getString("maCT"), start, start, rs.getBoolean("trangThai"),
								rs.getString("maTau"), rs.getString("maTuyenTau")));
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[ChuyenTauCrud_DAO] Lỗi tìm kiếm chuyến tàu: " + e.getMessage());
		}
		return danhSach;
	}

	public boolean themChuyenTau(ChuyenTau chuyenTau) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || chuyenTau == null) {
				return false;
			}
			String sql = "INSERT INTO ChuyenTau(maCT, maTau, maTuyenTau, ngayKhoiHanh, gioKhoiHanh, trangThai) VALUES(?,?,?,?,?,?)";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, chuyenTau.getMaCT());
				ps.setString(2, chuyenTau.getMaTau());
				ps.setString(3, chuyenTau.getMaTuyenTau());
				LocalDateTime dt = chuyenTau.getNgayKhoiHanh() != null ? chuyenTau.getNgayKhoiHanh() : LocalDateTime.now();
				ps.setDate(4, Date.valueOf(dt.toLocalDate()));
				ps.setTime(5, Time.valueOf(dt.toLocalTime()));
				ps.setBoolean(6, chuyenTau.isTrangThai());
				return ps.executeUpdate() > 0;
			}
		} catch (Exception e) {
			System.err.println("[ChuyenTauCrud_DAO] Lỗi thêm chuyến tàu: " + e.getMessage());
			return false;
		}
	}

	public boolean capNhatChuyenTau(ChuyenTau chuyenTau) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || chuyenTau == null) {
				return false;
			}
			String sql = "UPDATE ChuyenTau SET maTau = ?, maTuyenTau = ?, ngayKhoiHanh = ?, gioKhoiHanh = ?, trangThai = ? WHERE maCT = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, chuyenTau.getMaTau());
				ps.setString(2, chuyenTau.getMaTuyenTau());
				LocalDateTime dt = chuyenTau.getNgayKhoiHanh() != null ? chuyenTau.getNgayKhoiHanh() : LocalDateTime.now();
				ps.setDate(3, Date.valueOf(dt.toLocalDate()));
				ps.setTime(4, Time.valueOf(dt.toLocalTime()));
				ps.setBoolean(5, chuyenTau.isTrangThai());
				ps.setString(6, chuyenTau.getMaCT());
				return ps.executeUpdate() > 0;
			}
		} catch (Exception e) {
			System.err.println("[ChuyenTauCrud_DAO] Lỗi cập nhật chuyến tàu: " + e.getMessage());
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
}
