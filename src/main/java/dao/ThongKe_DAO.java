package dao;

import connectDB.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKe_DAO {

	public BigDecimal getRevenueByPeriod(String period) {
		String sql = "";
		switch (period.toLowerCase()) {
			case "day":
				sql = "SELECT ISNULL(SUM(v.tongThanhToan), 0) FROM v_TongTienHoaDon v JOIN HoaDon h ON v.maHD = h.maHD WHERE h.[trangThaiThanhToan] = 1 AND CAST(h.[thoiGian] AS DATE) = CAST(GETDATE() AS DATE)";
				break;
			case "month":
				sql = "SELECT ISNULL(SUM(v.tongThanhToan), 0) FROM v_TongTienHoaDon v JOIN HoaDon h ON v.maHD = h.maHD WHERE h.[trangThaiThanhToan] = 1 AND MONTH(h.[thoiGian]) = MONTH(GETDATE()) AND YEAR(h.[thoiGian]) = YEAR(GETDATE())";
				break;
			case "year":
				sql = "SELECT ISNULL(SUM(v.tongThanhToan), 0) FROM v_TongTienHoaDon v JOIN HoaDon h ON v.maHD = h.maHD WHERE h.[trangThaiThanhToan] = 1 AND YEAR(h.[thoiGian]) = YEAR(GETDATE())";
				break;
			default:
				return BigDecimal.ZERO;
		}

		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return BigDecimal.ZERO;
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return BigDecimal.valueOf(rs.getDouble(1));
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getRevenueByPeriod: " + e.getMessage());
		}
		return BigDecimal.ZERO;
	}

	public Map<String, Integer> getTicketStatusCounts() {
		Map<String, Integer> stats = new HashMap<>();
		String sql = "SELECT trangThai, COUNT(*) FROM VeTau GROUP BY trangThai";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return stats;
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					stats.put(rs.getString(1), rs.getInt(2));
				}
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getTicketStatusCounts: " + e.getMessage());
		}
		return stats;
	}

	public Map<String, Long> getDailyRevenueChart(int days) {
		Map<String, Long> data = new HashMap<>();
		String sql = "SELECT TOP (?) CAST(h.[thoiGian] AS DATE) as d, SUM(v.tongThanhToan) " +
		             "FROM v_TongTienHoaDon v JOIN HoaDon h ON v.maHD = h.maHD " +
		             "WHERE h.[trangThaiThanhToan] = 1 GROUP BY CAST(h.[thoiGian] AS DATE) ORDER BY d DESC";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return data;
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, days);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						data.put(rs.getDate(1).toString(), (long)rs.getDouble(2));
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getDailyRevenueChart: " + e.getMessage());
		}
		return data;
	}

	public List<Object[]> getTopCustomers(int limit) {
		List<Object[]> list = new ArrayList<>();
		String sql = "SELECT TOP (?) kh.tenKH, SUM(v.tongThanhToan) as total " +
		             "FROM v_TongTienHoaDon v JOIN HoaDon h ON v.maHD = h.maHD " +
		             "JOIN KhachHang kh ON v.maKH = kh.maKH " +
		             "WHERE h.[trangThaiThanhToan] = 1 " +
		             "GROUP BY kh.tenKH ORDER BY total DESC";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return list;
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, limit);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						list.add(new Object[] { rs.getString(1), rs.getDouble(2) });
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getTopCustomers: " + e.getMessage());
		}
		return list;
	}

	public List<Object[]> getTripOccupancy() {
		List<Object[]> list = new ArrayList<>();
		String sql = "SELECT ct.maCT, COUNT(vt.maVeTau) as booked, " +
		             "(SELECT SUM(soGhe) FROM Toa WHERE maTau = ct.maTau) as total " +
		             "FROM ChuyenTau ct LEFT JOIN VeTau vt ON ct.maCT = vt.maCT " +
		             "GROUP BY ct.maCT, ct.maTau";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return list;
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(new Object[] { rs.getString(1), rs.getInt(2), rs.getInt(3) });
				}
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getTripOccupancy: " + e.getMessage());
		}
		return list;
	}

	public Map<String, Integer> getTicketsByCarriageType() {
		Map<String, Integer> data = new HashMap<>();
		String sql = "SELECT t.loaiToa, COUNT(vt.maVeTau) " +
		             "FROM Toa t LEFT JOIN VeTau vt ON t.maToa = vt.maToa " +
		             "GROUP BY t.loaiToa";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return data;
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					data.put(rs.getString(1), rs.getInt(2));
				}
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getTicketsByCarriageType: " + e.getMessage());
		}
		return data;
	}

	public Map<String, Integer> getNewCustomersByMonth(int year) {
		Map<String, Integer> data = new HashMap<>();
		String sql = "SELECT MONTH(CAST(CAST(ngaySinh AS VARCHAR) AS DATE)) as month, COUNT(*) " +
		             "FROM KhachHang WHERE YEAR(CAST(CAST(ngaySinh AS VARCHAR) AS DATE)) = ? " +
		             "GROUP BY MONTH(CAST(CAST(ngaySinh AS VARCHAR) AS DATE))";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return data;
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, year);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						data.put("T" + rs.getInt(1), rs.getInt(2));
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getNewCustomersByMonth: " + e.getMessage());
		}
		return data;
	}

	public int getTotalCustomers() {
		String sql = "SELECT COUNT(*) FROM KhachHang";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return 0;
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return rs.getInt(1);
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getTotalCustomers: " + e.getMessage());
		}
		return 0;
	}

	public Map<String, Integer> getTripCountByRoute() {
		Map<String, Integer> data = new HashMap<>();
		String sql = "SELECT tt.maGaDi + '-' + tt.maGaDen as route, COUNT(ct.maCT) " +
		             "FROM TuyenTau tt JOIN ChuyenTau ct ON tt.maTT = ct.maTuyenTau " +
		             "GROUP BY tt.maGaDi + '-' + tt.maGaDen";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return data;
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					data.put(rs.getString(1), rs.getInt(2));
				}
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getTripCountByRoute: " + e.getMessage());
		}
		return data;
	}

	public Map<String, Long> getRevenueByMonth(int year) {
		Map<String, Long> data = new HashMap<>();
		String sql = "SELECT MONTH(h.thoiGian) as month, SUM(v.tongThanhToan) " +
		             "FROM HoaDon h JOIN v_TongTienHoaDon v ON h.maHD = v.maHD " +
		             "WHERE h.trangThaiThanhToan = 1 AND YEAR(h.thoiGian) = ? " +
		             "GROUP BY MONTH(h.thoiGian)";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return data;
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, year);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						data.put("T" + rs.getInt(1), rs.getLong(2));
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getRevenueByMonth: " + e.getMessage());
		}
		return data;
	}

	public Map<String, Long> getRevenueByQuarter(int year) {
		Map<String, Long> data = new HashMap<>();
		String sql = "SELECT CASE WHEN MONTH(h.thoiGian) <= 3 THEN 'Q1' " +
		             "WHEN MONTH(h.thoiGian) <= 6 THEN 'Q2' " +
		             "WHEN MONTH(h.thoiGian) <= 9 THEN 'Q3' ELSE 'Q4' END as quarter, " +
		             "SUM(v.tongThanhToan) FROM HoaDon h JOIN v_TongTienHoaDon v ON h.maHD = v.maHD " +
		             "WHERE h.trangThaiThanhToan = 1 AND YEAR(h.thoiGian) = ? " +
		             "GROUP BY CASE WHEN MONTH(h.thoiGian) <= 3 THEN 'Q1' " +
		             "WHEN MONTH(h.thoiGian) <= 6 THEN 'Q2' " +
		             "WHEN MONTH(h.thoiGian) <= 9 THEN 'Q3' ELSE 'Q4' END";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return data;
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, year);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						data.put(rs.getString(1), rs.getLong(2));
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getRevenueByQuarter: " + e.getMessage());
		}
		return data;
	}

	public int getTotalTrips() {
		String sql = "SELECT COUNT(*) FROM ChuyenTau";
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) return 0;
			try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return rs.getInt(1);
			}
		} catch (Exception e) {
			System.err.println("[ThongKe_DAO] Error getTotalTrips: " + e.getMessage());
		}
		return 0;
	}
}
