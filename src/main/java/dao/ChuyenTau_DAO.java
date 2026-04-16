package dao;

import connectDB.Database;
import entity.ChuyenTau;
import java.util.ArrayList;
import java.util.List;

public class ChuyenTau_DAO {
	private final Database database = Database.getInstance();

	/**
	 * Lấy danh sách tất cả chuyến tàu
	 * @return Danh sách chuyến tàu
	 */
	public List<ChuyenTau> layTatCaChuyenTau() {
		try {
			return database.getAllChuyenTau();
		} catch (Exception e) {
			System.err.println("[ChuyenTau_DAO] Lỗi lấy danh sách chuyến tàu: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Lấy danh sách chuyến tàu đang hoạt động
	 * @return Danh sách chuyến tàu hoạt động
	 */
	public List<ChuyenTau> layChuyenTauHoatDong() {
		try {
			List<ChuyenTau> allCT = database.getAllChuyenTau();
			List<ChuyenTau> result = new ArrayList<>();
			
			for (ChuyenTau ct : allCT) {
				if (ct.isTrangThai()) {
					result.add(ct);
				}
			}
			
			return result;
		} catch (Exception e) {
			System.err.println("[ChuyenTau_DAO] Lỗi lọc chuyến tàu hoạt động: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Tìm chuyến tàu theo mã
	 * @param maCT Mã chuyến tàu
	 * @return Đối tượng chuyến tàu hoặc null nếu không tìm thấy
	 */
	public ChuyenTau timChuyenTauTheoMa(String maCT) {
		try {
			List<ChuyenTau> allCT = database.getAllChuyenTau();
			for (ChuyenTau ct : allCT) {
				if (ct.getMaCT().equals(maCT)) {
					return ct;
				}
			}
			return null;
		} catch (Exception e) {
			System.err.println("[ChuyenTau_DAO] Lỗi tìm chuyến theo mã: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Lấy danh sách chuyến tàu theo mã tàu
	 * @param maTau Mã tàu
	 * @return Danh sách chuyến tàu của tàu đó
	 */
	public List<ChuyenTau> layChuyenTauTheoMaTau(String maTau) {
		try {
			List<ChuyenTau> allCT = database.getAllChuyenTau();
			List<ChuyenTau> result = new ArrayList<>();
			
			for (ChuyenTau ct : allCT) {
				if (ct.getMaTau().equals(maTau)) {
					result.add(ct);
				}
			}
			
			return result;
		} catch (Exception e) {
			System.err.println("[ChuyenTau_DAO] Lỗi lọc theo mã tàu: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Lấy danh sách chuyến tàu theo mã tuyến tàu
	 * @param maTuyenTau Mã tuyến tàu
	 * @return Danh sách chuyến tàu của tuyến đó
	 */
	public List<ChuyenTau> layChuyenTauTheoMaTuyenTau(String maTuyenTau) {
		try {
			List<ChuyenTau> allCT = database.getAllChuyenTau();
			List<ChuyenTau> result = new ArrayList<>();
			
			for (ChuyenTau ct : allCT) {
				if (ct.getMaTuyenTau().equals(maTuyenTau)) {
					result.add(ct);
				}
			}
			
			return result;
		} catch (Exception e) {
			System.err.println("[ChuyenTau_DAO] Lỗi lọc theo mã tuyến tàu: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Đếm tổng số chuyến tàu
	 * @return Tổng số chuyến tàu
	 */
	public int demTongSoChuyenTau() {
		try {
			return database.getAllChuyenTau().size();
		} catch (Exception e) {
			System.err.println("[ChuyenTau_DAO] Lỗi đếm tổng chuyến tàu: " + e.getMessage());
			return 0;
		}
	}

	/**
	 * Đếm số chuyến tàu đang hoạt động
	 * @return Số chuyến tàu hoạt động
	 */
	public int demChuyenTauHoatDong() {
		try {
			return layChuyenTauHoatDong().size();
		} catch (Exception e) {
			System.err.println("[ChuyenTau_DAO] Lỗi đếm chuyến hoạt động: " + e.getMessage());
			return 0;
		}
	}
}
