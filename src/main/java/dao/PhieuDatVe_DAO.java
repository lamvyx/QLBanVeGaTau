package dao;

import connectDB.DatabaseConnection;
import entity.ChiTietPhieuDat;
import entity.PhieuDatVe;
import entity.PhieuDatVeInfo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatVe_DAO {
	public String layMaPhieuTiepTheo() {
		int max = 0;
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return "PDV001";
			}
			String sql = "SELECT maPhieu FROM PhieuDatVe";
			try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
				while (rs.next()) {
					String ma = rs.getString(1);
					if (ma != null && ma.startsWith("PDV")) {
						try {
							max = Math.max(max, Integer.parseInt(ma.substring(3)));
						} catch (NumberFormatException ignored) {
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[PhieuDatVe_DAO] Lỗi lấy mã phiếu tiếp theo: " + e.getMessage());
		}
		return String.format("PDV%03d", max + 1);
	}

	public boolean taoPhieuDat(PhieuDatVe phieuDatVe, List<ChiTietPhieuDat> chiTietList) {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			if (conn == null || phieuDatVe == null || chiTietList == null || chiTietList.isEmpty()) {
				return false;
			}
			conn.setAutoCommit(false);

			String sqlPhieu = "INSERT INTO PhieuDatVe(maPhieu, maKH, maNV, ngayDat, hanThanhToan, trangThai) VALUES(?,?,?,?,?,?)";
			try (PreparedStatement ps = conn.prepareStatement(sqlPhieu)) {
				ps.setString(1, phieuDatVe.getMaPhieu());
				ps.setString(2, phieuDatVe.getMaKH());
				ps.setString(3, phieuDatVe.getMaNV());
				ps.setDate(4, Date.valueOf(phieuDatVe.getNgayDat() == null ? LocalDate.now() : phieuDatVe.getNgayDat()));
				ps.setDate(5, phieuDatVe.getHanThanhToan() == null ? null : Date.valueOf(phieuDatVe.getHanThanhToan()));
				ps.setBoolean(6, phieuDatVe.isTrangThai());
				if (ps.executeUpdate() <= 0) {
					conn.rollback();
					return false;
				}
			}

			String sqlCT = "INSERT INTO ChiTietPhieuDat(maPhieu, maCT, maToa, viTriGhe, giaVe, ghiChu) VALUES(?,?,?,?,?,?)";
			try (PreparedStatement ps = conn.prepareStatement(sqlCT)) {
				for (ChiTietPhieuDat ct : chiTietList) {
					if (ct == null || ct.getMaCT() == null || ct.getMaCT().isBlank() || ct.getMaToa() == null
							|| ct.getMaToa().isBlank() || ct.getViTriGhe() == null || ct.getViTriGhe().isBlank()) {
						continue;
					}
					ps.setString(1, phieuDatVe.getMaPhieu());
					ps.setString(2, ct.getMaCT().trim());
					ps.setString(3, ct.getMaToa().trim());
					ps.setString(4, ct.getViTriGhe().trim().toUpperCase());
					ps.setDouble(5, ct.getGiaVe());
					ps.setString(6, ct.getGhiChu());
					ps.addBatch();
				}
				ps.executeBatch();
			}

			conn.commit();
			return true;
		} catch (Exception e) {
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception ignored) {
			}
			System.err.println("[PhieuDatVe_DAO] Lỗi tạo phiếu đặt: " + e.getMessage());
			return false;
		} finally {
			try {
				if (conn != null) {
					conn.setAutoCommit(true);
				}
			} catch (Exception ignored) {
			}
		}
	}

	public PhieuDatVeInfo timPhieuDatTheoMa(String maPhieu) {
		PhieuDatVe phieu = null;
		List<ChiTietPhieuDat> details = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || maPhieu == null || maPhieu.isBlank()) {
				return null;
			}

			String sqlPhieu = "SELECT maPhieu, maKH, maNV, ngayDat, hanThanhToan, trangThai FROM PhieuDatVe WHERE maPhieu = ?";
			try (PreparedStatement ps = conn.prepareStatement(sqlPhieu)) {
				ps.setString(1, maPhieu.trim());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						phieu = new PhieuDatVe();
						phieu.setMaPhieu(rs.getString("maPhieu"));
						phieu.setMaKH(rs.getString("maKH"));
						phieu.setMaNV(rs.getString("maNV"));
						Date ngayDat = rs.getDate("ngayDat");
						if (ngayDat != null) phieu.setNgayDat(ngayDat.toLocalDate());
						Date han = rs.getDate("hanThanhToan");
						if (han != null) phieu.setHanThanhToan(han.toLocalDate());
						phieu.setTrangThai(rs.getBoolean("trangThai"));
					}
				}
			}

			if (phieu == null) {
				return null;
			}

			String sqlCT = "SELECT maPhieu, maCT, maToa, viTriGhe, giaVe, ghiChu FROM ChiTietPhieuDat WHERE maPhieu = ? ORDER BY maCT, maToa, viTriGhe";
			try (PreparedStatement ps = conn.prepareStatement(sqlCT)) {
				ps.setString(1, maPhieu.trim());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						ChiTietPhieuDat ct = new ChiTietPhieuDat();
						ct.setMaPhieu(rs.getString("maPhieu"));
						ct.setMaCT(rs.getString("maCT"));
						ct.setMaToa(rs.getString("maToa"));
						ct.setViTriGhe(rs.getString("viTriGhe"));
						ct.setGiaVe(rs.getDouble("giaVe"));
						ct.setGhiChu(rs.getString("ghiChu"));
						details.add(ct);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[PhieuDatVe_DAO] Lỗi tìm phiếu đặt: " + e.getMessage());
			return null;
		}
		return new PhieuDatVeInfo(phieu, details);
	}

	public List<PhieuDatVeInfo> layTatCaPhieuDat() {
		List<PhieuDatVeInfo> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return danhSach;
			}
			String sql = "SELECT maPhieu FROM PhieuDatVe ORDER BY ngayDat DESC, maPhieu DESC";
			try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
				while (rs.next()) {
					PhieuDatVeInfo info = timPhieuDatTheoMa(rs.getString(1));
					if (info != null) {
						danhSach.add(info);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[PhieuDatVe_DAO] Lỗi lấy danh sách phiếu đặt: " + e.getMessage());
		}
		return danhSach;
	}

	public boolean capNhatTrangThai(String maPhieu, boolean trangThai) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || maPhieu == null || maPhieu.isBlank()) {
				return false;
			}
			String sql = "UPDATE PhieuDatVe SET trangThai = ? WHERE maPhieu = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setBoolean(1, trangThai);
				ps.setString(2, maPhieu.trim());
				return ps.executeUpdate() > 0;
			}
		} catch (Exception e) {
			System.err.println("[PhieuDatVe_DAO] Lỗi cập nhật trạng thái phiếu đặt: " + e.getMessage());
			return false;
		}
	}
}