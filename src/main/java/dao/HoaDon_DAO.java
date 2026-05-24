package dao;

import connectDB.DatabaseConnection;
import entity.ChiTietHoaDonItem;
import entity.HoaDon;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO {
	public String layMaHoaDonTiepTheo() {
		int max = 0;
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return "HD001";
			}
			String sql = "SELECT maHD FROM HoaDon";
			try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
				while (rs.next()) {
					String ma = rs.getString(1);
					if (ma != null && ma.startsWith("HD")) {
						try {
							max = Math.max(max, Integer.parseInt(ma.substring(2)));
						} catch (NumberFormatException ignored) {
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[HoaDon_DAO] Lỗi lấy mã hóa đơn tiếp theo: " + e.getMessage());
		}
		return String.format("HD%03d", max + 1);
	}

	public String layMaChiTietTiepTheo() {
		// Generate a CTHD id based on current count of detail rows across both detail tables
		int count = 0;
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return "CTHD001";
			}
			String sql = "SELECT (SELECT COUNT(*) FROM ChiTietHoaDon_Ve) + (SELECT COUNT(*) FROM ChiTietHoaDon_DichVu)";
			try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
				if (rs.next()) count = rs.getInt(1);
			}
		} catch (Exception e) {
			System.err.println("[HoaDon_DAO] Lỗi lấy mã chi tiết hóa đơn tiếp theo: " + e.getMessage());
		}
		return String.format("CTHD%03d", count + 1);
	}

	public boolean taoHoaDon(HoaDon hoaDon, List<ChiTietHoaDonItem> chiTietItems) {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			if (conn == null || hoaDon == null || chiTietItems == null || chiTietItems.isEmpty()) {
				return false;
			}
			conn.setAutoCommit(false);

			String sqlHoaDon = "INSERT INTO HoaDon(maHD, maNV, maKH, maKM, maThue, thoiGian, phuongThucThanhToan, ngayThanhToan, trangThaiThanhToan) VALUES(?,?,?,?,?,?,?,?,?)";
			try (PreparedStatement ps = conn.prepareStatement(sqlHoaDon)) {
				ps.setString(1, hoaDon.getMaHD());
				ps.setString(2, hoaDon.getMaNV());
				ps.setString(3, hoaDon.getMaKH());
				ps.setString(4, hoaDon.getMaKM());
				ps.setString(5, "T001");
				LocalDate date = hoaDon.getThoiGian() != null ? hoaDon.getThoiGian().toLocalDate() : LocalDate.now();
				ps.setDate(6, Date.valueOf(date));
				ps.setString(7, "TIEN_MAT");
				ps.setDate(8, Date.valueOf(date));
				ps.setBoolean(9, true);
				if (ps.executeUpdate() <= 0) {
					conn.rollback();
					return false;
				}
			}

			String sqlCTVe = "INSERT INTO ChiTietHoaDon_Ve(maHD, maVeTau, soLuong, donGia) VALUES(?,?,?,?)";
			String sqlCTDV = "INSERT INTO ChiTietHoaDon_DichVu(maHD, maDV, soLuong, donGia) VALUES(?,?,?,?)";
			try (PreparedStatement psVe = conn.prepareStatement(sqlCTVe);
					PreparedStatement psDv = conn.prepareStatement(sqlCTDV)) {
				for (ChiTietHoaDonItem item : chiTietItems) {
					if (item.getMaVeTau() != null && !item.getMaVeTau().isBlank()) {
						psVe.setString(1, item.getMaHD());
						psVe.setString(2, item.getMaVeTau());
						psVe.setInt(3, item.getSoLuong());
						psVe.setDouble(4, item.getDonGia().doubleValue());
						psVe.addBatch();
					} else if (item.getMaDV() != null && !item.getMaDV().isBlank()) {
						psDv.setString(1, item.getMaHD());
						psDv.setString(2, item.getMaDV());
						psDv.setInt(3, item.getSoLuong());
						psDv.setDouble(4, item.getDonGia().doubleValue());
						psDv.addBatch();
					}
				}
				psVe.executeBatch();
				psDv.executeBatch();
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
			System.err.println("[HoaDon_DAO] Lỗi tạo hóa đơn: " + e.getMessage());
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

	public boolean taoHoaDonBanVe(HoaDon hoaDon, String maCT, String maToa, List<String> viTriGheList,
			BigDecimal giaVe, String tenHanhKhach, String cccd, LocalDate ngaySinh) {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			if (conn == null || hoaDon == null || maCT == null || maCT.isBlank() || maToa == null || maToa.isBlank()
					|| viTriGheList == null || viTriGheList.isEmpty() || giaVe == null) {
				return false;
			}

			conn.setAutoCommit(false);

			String maKH = hoaDon.getMaKH() == null ? null : hoaDon.getMaKH().trim();
			if (maKH == null || maKH.isBlank() || !tonTaiKhachHang(conn, maKH)) {
				System.err.println("[HoaDon_DAO] Không tồn tại khách hàng với mã: " + hoaDon.getMaKH());
				conn.rollback();
				return false;
			}

			String sqlHoaDon = "INSERT INTO HoaDon(maHD, maNV, maKH, maKM, maThue, thoiGian, phuongThucThanhToan, ngayThanhToan, trangThaiThanhToan) VALUES(?,?,?,?,?,?,?,?,?)";
			try (PreparedStatement ps = conn.prepareStatement(sqlHoaDon)) {
				ps.setString(1, hoaDon.getMaHD());
				ps.setString(2, hoaDon.getMaNV());
				ps.setString(3, maKH);
				ps.setString(4, hoaDon.getMaKM());
				ps.setString(5, "T001");
				LocalDate date = hoaDon.getThoiGian() != null ? hoaDon.getThoiGian().toLocalDate() : LocalDate.now();
				ps.setDate(6, Date.valueOf(date));
				ps.setString(7, "TIEN_MAT");
				ps.setDate(8, Date.valueOf(date));
				ps.setBoolean(9, true);
				if (ps.executeUpdate() <= 0) {
					conn.rollback();
					return false;
				}
			}

			int nextVe = laySoThuTuTiepTheo(conn, "VeTau", "maVeTau", "VE", 2);

			String sqlVeTau = "INSERT INTO VeTau(maVeTau, maKH, maCT, maToa, viTriGhe, loaiVe, giaVe, trangThai) VALUES(?,?,?,?,?,?,?,?)";
			String sqlCTVe = "INSERT INTO ChiTietHoaDon_Ve(maHD, maVeTau, soLuong, donGia) VALUES(?,?,?,?)";

			try (PreparedStatement psVeTau = conn.prepareStatement(sqlVeTau);
					PreparedStatement psCTVe = conn.prepareStatement(sqlCTVe)) {
				for (String viTriGhe : viTriGheList) {
					if (viTriGhe == null || viTriGhe.isBlank()) continue;

					String maVeTau = String.format("VE%03d", nextVe++);

					psVeTau.setString(1, maVeTau);
					psVeTau.setString(2, maKH);
					psVeTau.setString(3, maCT.trim());
					psVeTau.setString(4, maToa.trim());
					psVeTau.setString(5, viTriGhe.trim().toUpperCase());
					psVeTau.setString(6, "NGUOI_LON");
					psVeTau.setDouble(7, giaVe.doubleValue());
					psVeTau.setString(8, "DA_THANH_TOAN");
					psVeTau.addBatch();

					psCTVe.setString(1, hoaDon.getMaHD());
					psCTVe.setString(2, maVeTau);
					psCTVe.setInt(3, 1);
					psCTVe.setDouble(4, giaVe.doubleValue());
					psCTVe.addBatch();
				}

				psVeTau.executeBatch();
				psCTVe.executeBatch();
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
			System.err.println("[HoaDon_DAO] Lỗi tạo hóa đơn bán vé: " + e.getMessage());
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

	private int laySoThuTuTiepTheo(Connection conn, String tableName, String idColumn, String prefix, int prefixLength)
			throws Exception {
		int max = 0;
		String sql = "SELECT " + idColumn + " FROM " + tableName;
		try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				String ma = rs.getString(1);
				if (ma != null && ma.startsWith(prefix)) {
					try {
						max = Math.max(max, Integer.parseInt(ma.substring(prefixLength)));
					} catch (NumberFormatException ignored) {
					}
				}
			}
		}
		return max + 1;
	}

	private boolean tonTaiKhachHang(Connection conn, String maKH) throws Exception {
		String sql = "SELECT 1 FROM KhachHang WHERE maKH = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maKH);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public List<HoaDon> timKiemHoaDon(String tuKhoa) {
		List<HoaDon> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return danhSach;
			}
			String keyword = tuKhoa == null ? "" : tuKhoa.trim();
			String sql = "SELECT maHD, maNV, maKH, maKM, thoiGian, trangThaiThanhToan, ngayThanhToan FROM HoaDon WHERE maHD LIKE ? OR maKH LIKE ? OR maNV LIKE ? ORDER BY thoiGian DESC";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				String pattern = "%" + keyword + "%";
				ps.setString(1, pattern);
				ps.setString(2, pattern);
				ps.setString(3, pattern);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Timestamp ts = rs.getTimestamp("thoiGian");
						LocalDateTime thoiGian = ts != null ? ts.toLocalDateTime() : rs.getDate("thoiGian").toLocalDate().atStartOfDay();
						HoaDon hd = new HoaDon(rs.getString("maHD"), rs.getString("maNV"), rs.getString("maKH"),
								thoiGian, BigDecimal.ZERO, BigDecimal.ZERO, rs.getString("maKM"));
						hd.setTrangThaiThanhToan(rs.getBoolean("trangThaiThanhToan"));
						Date ntt = rs.getDate("ngayThanhToan");
						if (ntt != null) hd.setNgayThanhToan(ntt.toLocalDate());
						danhSach.add(hd);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[HoaDon_DAO] Lỗi tìm kiếm hóa đơn: " + e.getMessage());
		}
		return danhSach;
	}

	public BigDecimal layTongThanhToanHoaDon(String maHD) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return BigDecimal.ZERO;
			}
			String sql = "SELECT tongThanhToan FROM v_TongTienHoaDon WHERE maHD = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maHD);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return BigDecimal.valueOf(rs.getDouble(1));
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[HoaDon_DAO] Lỗi lấy tổng thanh toán hóa đơn: " + e.getMessage());
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal layTongDoanhThu() {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return BigDecimal.ZERO;
			}
			String sql = "SELECT ISNULL(SUM(tongThanhToan), 0) FROM v_TongTienHoaDon";
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return BigDecimal.valueOf(rs.getDouble(1));
				}
			}
		} catch (Exception e) {
			System.err.println("[HoaDon_DAO] Lỗi lấy tổng doanh thu: " + e.getMessage());
		}
		return BigDecimal.ZERO;
	}

	public List<ChiTietHoaDonItem> layChiTietHoaDon(String maHD) {
		List<ChiTietHoaDonItem> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return danhSach;
			String sql = "SELECT maCTHD, maHD, maVeTau, maDV, soLuong, donGia FROM v_ChiTietHoaDon WHERE maHD = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maHD);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						danhSach.add(new ChiTietHoaDonItem(
							rs.getString("maCTHD"),
							rs.getString("maHD"),
							rs.getString("maVeTau"),
							rs.getString("maDV"),
							rs.getInt("soLuong"),
							BigDecimal.valueOf(rs.getDouble("donGia"))
						));
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[HoaDon_DAO] Lỗi lấy chi tiết hóa đơn: " + e.getMessage());
		}
		return danhSach;
	}
	public boolean updateTrangThaiThanhToan(String maHD, boolean daThanhToan) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return false;
			String sql = "UPDATE HoaDon SET trangThaiThanhToan = ?, ngayThanhToan = ? WHERE maHD = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setBoolean(1, daThanhToan);
				ps.setDate(2, daThanhToan ? Date.valueOf(LocalDate.now()) : null);
				ps.setString(3, maHD);
				return ps.executeUpdate() > 0;
			}
		} catch (Exception e) {
			System.err.println("[HoaDon_DAO] Lỗi cập nhật trạng thái thanh toán: " + e.getMessage());
			return false;
		}
	}

	public boolean kiemTraThanhToan(String maHD) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return false;
			String sql = "SELECT trangThaiThanhToan FROM HoaDon WHERE maHD = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maHD);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) return rs.getBoolean(1);
				}
			}
		} catch (Exception e) {
			System.err.println("[HoaDon_DAO] Lỗi kiểm tra thanh toán: " + e.getMessage());
		}
		return false;
	}
}
