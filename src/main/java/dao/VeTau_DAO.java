package dao;

import connectDB.DatabaseConnection;
import entity.LichSuVeDTO;
import entity.VeTau;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;

public class VeTau_DAO {
	public VeTau timTheoMaVe(String maVeTau) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return null;
			}
			String sql = "SELECT maVeTau, maKH, maCT, maToa, viTriGhe, loaiVe, giaVe, trangThai FROM VeTau WHERE maVeTau = ?";
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
						ve.setViTriGhe(rs.getString("viTriGhe"));
						ve.setLoaiVe(rs.getString("loaiVe"));
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
			String sql = "SELECT maVeTau, maKH, maCT, maToa, viTriGhe, loaiVe, giaVe, trangThai FROM VeTau WHERE maCT = ?";
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
						ve.setViTriGhe(rs.getString("viTriGhe"));
						ve.setLoaiVe(rs.getString("loaiVe"));
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

	public Set<String> layGheDaDat(String maCT, String maToa) {
		Set<String> dsGhe = new LinkedHashSet<>();
		if (maCT == null || maCT.isBlank() || maToa == null || maToa.isBlank()) {
			return dsGhe;
		}

		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return dsGhe;
			}

			String sql = "SELECT viTriGhe FROM VeTau WHERE maCT = ? AND maToa = ? AND trangThai <> 'DA_HOAN'";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maCT.trim());
				ps.setString(2, maToa.trim());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						String viTri = rs.getString("viTriGhe");
						if (viTri != null && !viTri.isBlank()) {
							dsGhe.add(viTri.trim().toUpperCase());
						}
					}
				}
			}

			String sqlPhieu = "SELECT ctpd.viTriGhe FROM ChiTietPhieuDat ctpd JOIN PhieuDatVe pd ON pd.maPhieu = ctpd.maPhieu "
					+ "WHERE ctpd.maCT = ? AND ctpd.maToa = ? AND pd.trangThai = 1";
			try (PreparedStatement ps = conn.prepareStatement(sqlPhieu)) {
				ps.setString(1, maCT.trim());
				ps.setString(2, maToa.trim());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						String viTri = rs.getString("viTriGhe");
						if (viTri != null && !viTri.isBlank()) {
							dsGhe.add(viTri.trim().toUpperCase());
						}
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[VeTau_DAO] Lỗi lấy danh sách ghế đã đặt: " + e.getMessage());
		}
		return dsGhe;
	}
	public List<LichSuVeDTO> layLichSuVe(String maKH) {
		List<LichSuVeDTO> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return danhSach;
			
			String sqlSimple = "SELECT vt.maVeTau, tt.maGaDi, tt.maGaDen, ct.ngayKhoiHanh, h.thoiGian AS ngayMua, vt.maToa, vt.giaVe, vt.trangThai " +
			                   "FROM VeTau vt " +
			                   "JOIN ChuyenTau ct ON vt.maCT = ct.maCT " +
			                   "JOIN TuyenTau tt ON ct.maTuyenTau = tt.maTT " +
			                   "LEFT JOIN ChiTietHoaDon_Ve cthdv ON vt.maVeTau = cthdv.maVeTau " +
			                   "LEFT JOIN HoaDon h ON cthdv.maHD = h.maHD ";
			
			if (maKH != null && !maKH.trim().isEmpty()) {
				sqlSimple += " WHERE vt.maKH = ?";
			}
			sqlSimple += " ORDER BY vt.maVeTau DESC";

			try (PreparedStatement ps = conn.prepareStatement(sqlSimple)) {
				if (maKH != null && !maKH.trim().isEmpty()) {
					ps.setString(1, maKH.trim());
				}
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						LichSuVeDTO dto = new LichSuVeDTO();
						dto.setMaVeTau(rs.getString("maVeTau"));
						dto.setGaDi(rs.getString("maGaDi"));
						dto.setGaDen(rs.getString("maGaDen"));
						dto.setNgayKhoiHanh(docLocalDateTime(rs, "ngayKhoiHanh"));
						dto.setNgayMua(docLocalDateTime(rs, "ngayMua"));
						dto.setMaToa(rs.getString("maToa"));
						dto.setGiaVe(rs.getDouble("giaVe"));
						dto.setTrangThai(rs.getString("trangThai"));
						danhSach.add(dto);
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[VeTau_DAO] Lỗi lấy lịch sử vé: " + e.getMessage());
		}
		return danhSach;
	}

	public List<LichSuVeDTO> layLichSuVeTheoBoLoc(String query) {
		List<LichSuVeDTO> danhSach = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return danhSach;
			
			String sql = "SELECT vt.maVeTau, tt.maGaDi, tt.maGaDen, ct.ngayKhoiHanh, h.thoiGian AS ngayMua, vt.maToa, vt.giaVe, vt.trangThai " +
			             "FROM VeTau vt " +
			             "JOIN ChuyenTau ct ON vt.maCT = ct.maCT " +
			             "JOIN TuyenTau tt ON ct.maTuyenTau = tt.maTT " +
			             "LEFT JOIN KhachHang kh ON vt.maKH = kh.maKH " +
			             "LEFT JOIN ChiTietHoaDon_Ve cthdv ON vt.maVeTau = cthdv.maVeTau " +
			             "LEFT JOIN HoaDon h ON cthdv.maHD = h.maHD ";
			
			if (query != null && !query.trim().isEmpty()) {
				sql += " WHERE kh.tenKH LIKE ? OR kh.sdt LIKE ? OR kh.email LIKE ? OR kh.cccd LIKE ? OR vt.maKH = ?";
			}
			sql += " ORDER BY vt.maVeTau DESC";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				if (query != null && !query.trim().isEmpty()) {
					String val = "%" + query.trim() + "%";
					ps.setString(1, val);
					ps.setString(2, val);
					ps.setString(3, val);
					ps.setString(4, val);
					ps.setString(5, query.trim());
				}
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						LichSuVeDTO dto = new LichSuVeDTO();
						dto.setMaVeTau(rs.getString("maVeTau"));
						dto.setGaDi(rs.getString("maGaDi"));
						dto.setGaDen(rs.getString("maGaDen"));
						dto.setNgayKhoiHanh(docLocalDateTime(rs, "ngayKhoiHanh"));
						dto.setNgayMua(docLocalDateTime(rs, "ngayMua"));
						dto.setMaToa(rs.getString("maToa"));
						dto.setGiaVe(rs.getDouble("giaVe"));
						dto.setTrangThai(rs.getString("trangThai"));
						danhSach.add(dto);
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("[VeTau_DAO] Lỗi lấy lịch sử vé theo bộ lọc: " + e.getMessage());
		}
		return danhSach;
	}

	private LocalDateTime docLocalDateTime(ResultSet rs, String column) throws SQLException {
		Timestamp ts = rs.getTimestamp(column);
		return ts != null ? ts.toLocalDateTime() : null;
	}
}
