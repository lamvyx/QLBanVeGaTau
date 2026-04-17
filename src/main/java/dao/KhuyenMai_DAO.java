package dao;

import connectDB.Database;
import connectDB.DatabaseConnection;
import entity.KhuyenMai;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KhuyenMai_DAO {
	private static final Logger LOGGER = Logger.getLogger(KhuyenMai_DAO.class.getName());
	private final Database database = Database.getInstance();

	/**
	 * Lấy danh sách tất cả khuyến mãi
	 * @return Danh sách khuyến mãi
	 */
	public List<KhuyenMai> layTatCaKhuyenMai() {
		try {
			return database.getAllKhuyenMai();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Lỗi lấy danh sách khuyến mãi", e);
			return new ArrayList<>();
		}
	}

	/**
	 * Lấy danh sách khuyến mãi đang hoạt động (trong khoảng thời gian hợp lệ)
	 * @return Danh sách khuyến mãi hoạt động
	 */
	public List<KhuyenMai> layKhuyenMaiHoatDong() {
		try {
			List<KhuyenMai> allKM = database.getAllKhuyenMai();
			List<KhuyenMai> result = new ArrayList<>();
			LocalDate today = LocalDate.now();
			
			for (KhuyenMai km : allKM) {
				if (!today.isBefore(km.getNgayBD()) && !today.isAfter(km.getNgayKT())) {
					result.add(km);
				}
			}
			
			return result;
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Lỗi lọc khuyến mãi hoạt động", e);
			return new ArrayList<>();
		}
	}

	/**
	 * Tìm khuyến mãi theo mã
	 * @param maKM Mã khuyến mãi
	 * @return Đối tượng khuyến mãi hoặc null nếu không tìm thấy
	 */
	public KhuyenMai timKhuyenMaiTheoMa(String maKM) {
		try {
			List<KhuyenMai> allKM = database.getAllKhuyenMai();
			for (KhuyenMai km : allKM) {
				if (km.getMaKM().equals(maKM)) {
					return km;
				}
			}
			return null;
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Lỗi tìm khuyến mãi theo mã", e);
			return null;
		}
	}

	/**
	 * Tìm khuyến mãi theo tên (tìm kiếm gần đúng)
	 * @param tenKM Tên khuyến mãi cần tìm
	 * @return Danh sách khuyến mãi phù hợp
	 */
	public List<KhuyenMai> timKhuyenMaiTheoTen(String tenKM) {
		try {
			List<KhuyenMai> allKM = database.getAllKhuyenMai();
			List<KhuyenMai> result = new ArrayList<>();
			
			String tenSearch = tenKM.toLowerCase();
			for (KhuyenMai km : allKM) {
				if (km.getTenKM().toLowerCase().contains(tenSearch)) {
					result.add(km);
				}
			}
			
			return result;
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Lỗi tìm khuyến mãi theo tên", e);
			return new ArrayList<>();
		}
	}

	/**
	 * Lấy khuyến mãi có mức giảm cao nhất
	 * @return Khuyến mãi có mức giảm cao nhất
	 */
	public KhuyenMai layKhuyenMaiCaoNhat() {
		try {
			List<KhuyenMai> allKM = database.getAllKhuyenMai();
			if (allKM.isEmpty()) {
				return null;
			}
			
			KhuyenMai max = allKM.get(0);
			for (KhuyenMai km : allKM) {
				if (km.getTyLeKM().compareTo(max.getTyLeKM()) > 0) {
					max = km;
				}
			}
			
			return max;
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Lỗi lấy khuyến mãi cao nhất", e);
			return null;
		}
	}

	/**
	 * Đếm tổng số khuyến mãi
	 * @return Tổng số khuyến mãi
	 */
	public int demTongSoKhuyenMai() {
		try {
			return database.getAllKhuyenMai().size();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Lỗi đếm tổng khuyến mãi", e);
			return 0;
		}
	}

	/**
	 * Đếm số khuyến mãi đang hoạt động
	 * @return Số khuyến mãi hoạt động
	 */
	public int demKhuyenMaiHoatDong() {
		try {
			return layKhuyenMaiHoatDong().size();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Lỗi đếm khuyến mãi hoạt động", e);
			return 0;
		}
	}

	public boolean capNhatKhuyenMai(KhuyenMai khuyenMai) {
		if (khuyenMai == null || khuyenMai.getMaKM() == null || khuyenMai.getMaKM().isBlank()) {
			return false;
		}
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return false;
			}
			String sql = "UPDATE KhuyenMai SET tenKM = ?, tyLeKM = ?, ngayBD = ?, ngayKT = ? WHERE maKM = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, khuyenMai.getTenKM());
				ps.setBigDecimal(2, khuyenMai.getTyLeKM());
				ps.setDate(3, Date.valueOf(khuyenMai.getNgayBD()));
				ps.setDate(4, Date.valueOf(khuyenMai.getNgayKT()));
				ps.setString(5, khuyenMai.getMaKM());
				return ps.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Lỗi cập nhật khuyến mãi " + khuyenMai.getMaKM(), e);
			return false;
		}
	}

	public boolean xoaKhuyenMai(String maKM) {
		if (maKM == null || maKM.isBlank()) {
			return false;
		}
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return false;
			}
			String sql = "DELETE FROM KhuyenMai WHERE maKM = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maKM);
				return ps.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Lỗi xóa khuyến mãi " + maKM, e);
			return false;
		}
	}
}
