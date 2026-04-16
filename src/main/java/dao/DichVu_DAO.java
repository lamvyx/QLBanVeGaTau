package dao;

import connectDB.Database;
import entity.DichVu;
import java.util.ArrayList;
import java.util.List;

public class DichVu_DAO {
	private final Database database = Database.getInstance();

	/**
	 * Lấy danh sách tất cả dịch vụ
	 * @return Danh sách dịch vụ
	 */
	public List<DichVu> layTatCaDichVu() {
		try {
			return database.getAllDichVu();
		} catch (Exception e) {
			System.err.println("Không thể lấy tất cả dịch vụ: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Lấy danh sách dịch vụ đang hoạt động
	 * @return Danh sách dịch vụ hoạt động
	 */
	public List<DichVu> layDichVuHoatDong() {
		try {
			List<DichVu> allDV = database.getAllDichVu();
			List<DichVu> result = new ArrayList<>();
			
			for (DichVu dv : allDV) {
				if (dv.isTrangThai()) {
					result.add(dv);
				}
			}
			
			return result;
		} catch (Exception e) {
			System.err.println("Không thể lấy danh sách dịch vụ hoạt động: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Tìm dịch vụ theo mã
	 * @param maDV Mã dịch vụ
	 * @return Đối tượng dịch vụ hoặc null nếu không tìm thấy
	 */
	public DichVu timDichVuTheoMa(String maDV) {
		try {
			List<DichVu> allDV = database.getAllDichVu();
			for (DichVu dv : allDV) {
				if (dv.getMaDV().equals(maDV)) {
					return dv;
				}
			}
			return null;
		} catch (Exception e) {
			System.err.println("Không thể tìm dịch vụ theo mã " + maDV + ": " + e.getMessage());
			return null;
		}
	}

	/**
	 * Tìm dịch vụ theo tên (tìm kiếm gần đúng)
	 * @param tenDV Tên dịch vụ cần tìm
	 * @return Danh sách dịch vụ phù hợp
	 */
	public List<DichVu> timDichVuTheoTen(String tenDV) {
		try {
			List<DichVu> allDV = database.getAllDichVu();
			List<DichVu> result = new ArrayList<>();
			
			String tenSearch = tenDV.toLowerCase();
			for (DichVu dv : allDV) {
				if (dv.getTenDV().toLowerCase().contains(tenSearch)) {
					result.add(dv);
				}
			}
			
			return result;
		} catch (Exception e) {
			System.err.println("Không thể tìm dịch vụ theo tên " + tenDV + ": " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Đếm tổng số dịch vụ
	 * @return Tổng số dịch vụ
	 */
	public int demTongSoDichVu() {
		try {
			return database.getAllDichVu().size();
		} catch (Exception e) {
			System.err.println("Không thể đếm tổng số dịch vụ: " + e.getMessage());
			return 0;
		}
	}

	/**
	 * Đếm số dịch vụ đang hoạt động
	 * @return Số dịch vụ hoạt động
	 */
	public int demDichVuHoatDong() {
		try {
			return layDichVuHoatDong().size();
		} catch (Exception e) {
			System.err.println("Không thể đếm số dịch vụ hoạt động: " + e.getMessage());
			return 0;
		}
	}
}
