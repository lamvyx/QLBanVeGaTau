package dao;

import connectDB.Database;
import entity.KhachHang;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {
	private final Database database = Database.getInstance();

	/**
	 * Lấy danh sách tất cả khách hàng
	 * @return Danh sách khách hàng
	 */
	public List<KhachHang> layTatCaKhachHang() {
		try {
			return database.getAllKhachHang();
		} catch (Exception e) {
			System.err.println("Không thể lấy danh sách khách hàng: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Tìm khách hàng theo mã
	 * @param maKH Mã khách hàng
	 * @return Đối tượng khách hàng hoặc null nếu không tìm thấy
	 */
	public KhachHang timKhachHangTheoMa(String maKH) {
		try {
			return database.getKhachHangByMa(maKH);
		} catch (Exception e) {
			System.err.println("Không thể tìm khách hàng theo mã " + maKH + ": " + e.getMessage());
			return null;
		}
	}

	/**
	 * Tìm khách hàng theo tên (tìm kiếm gần đúng)
	 * @param tenKH Tên khách hàng cần tìm
	 * @return Danh sách khách hàng phù hợp
	 */
	public List<KhachHang> timKhachHangTheoTen(String tenKH) {
		try {
			List<KhachHang> allKH = database.getAllKhachHang();
			List<KhachHang> result = new ArrayList<>();
			
			String tenSearch = tenKH.toLowerCase();
			for (KhachHang kh : allKH) {
				if (kh.getTenKH().toLowerCase().contains(tenSearch)) {
					result.add(kh);
				}
			}
			
			return result;
		} catch (Exception e) {
			System.err.println("Không thể tìm khách hàng theo tên " + tenKH + ": " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Tìm khách hàng theo số điện thoại
	 * @param sdt Số điện thoại
	 * @return Đối tượng khách hàng hoặc null nếu không tìm thấy
	 */
	public KhachHang timKhachHangTheoSDT(String sdt) {
		try {
			List<KhachHang> allKH = database.getAllKhachHang();
			for (KhachHang kh : allKH) {
				if (kh.getSdt() != null && kh.getSdt().equals(sdt)) {
					return kh;
				}
			}
			return null;
		} catch (Exception e) {
			System.err.println("Không thể tìm khách hàng theo số điện thoại " + sdt + ": " + e.getMessage());
			return null;
		}
	}

	/**
	 * Tìm khách hàng theo CCCD
	 * @param cccd Chứng chỉ căn cước
	 * @return Đối tượng khách hàng hoặc null nếu không tìm thấy
	 */
	public KhachHang timKhachHangTheoCCCD(String cccd) {
		try {
			List<KhachHang> allKH = database.getAllKhachHang();
			for (KhachHang kh : allKH) {
				if (kh.getCccd() != null && kh.getCccd().equals(cccd)) {
					return kh;
				}
			}
			return null;
		} catch (Exception e) {
			System.err.println("Không thể tìm khách hàng theo CCCD " + cccd + ": " + e.getMessage());
			return null;
		}
	}

	/**
	 * Lấy danh sách khách hàng VIP
	 * @return Danh sách khách hàng VIP
	 */
	public List<KhachHang> layKhachHangVIP() {
		try {
			List<KhachHang> allKH = database.getAllKhachHang();
			List<KhachHang> result = new ArrayList<>();
			
			for (KhachHang kh : allKH) {
				if (kh.isLoaiKH()) {
					result.add(kh);
				}
			}
			
			return result;
		} catch (Exception e) {
			System.err.println("Không thể lấy danh sách khách hàng VIP: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Đếm tổng số khách hàng
	 * @return Tổng số khách hàng
	 */
	public int demTongSoKhachHang() {
		try {
			return database.getAllKhachHang().size();
		} catch (Exception e) {
			System.err.println("Không thể đếm tổng số khách hàng: " + e.getMessage());
			return 0;
		}
	}

	/**
	 * Đếm số khách hàng VIP
	 * @return Số khách hàng VIP
	 */
	public int demKhachHangVIP() {
		try {
			return layKhachHangVIP().size();
		} catch (Exception e) {
			System.err.println("Không thể đếm số khách hàng VIP: " + e.getMessage());
			return 0;
		}
	}

	public boolean themKhachHang(KhachHang khachHang) {
		try {
			if (khachHang == null) {
				return false;
			}

			if (kiemTraMaKHTonTai(khachHang.getMaKH())) {
				return false;
			}

			if (khachHang.getCccd() != null && !khachHang.getCccd().isBlank() && kiemTraCCCDTonTai(khachHang.getCccd())) {
				return false;
			}

			return database.addKhachHang(khachHang);
		} catch (Exception e) {
			System.err.println("Không thể thêm khách hàng " + (khachHang != null ? khachHang.getMaKH() : "null") + ": " + e.getMessage());
			return false;
		}
	}

	public boolean capNhatKhachHang(KhachHang khachHang) {
		try {
			if (khachHang == null || !kiemTraMaKHTonTai(khachHang.getMaKH())) {
				return false;
			}

			KhachHang khHienTai = timKhachHangTheoMa(khachHang.getMaKH());
			if (khHienTai == null) {
				return false;
			}

			if (khachHang.getCccd() != null && !khachHang.getCccd().isBlank()
					&& !khachHang.getCccd().equals(khHienTai.getCccd())
					&& kiemTraCCCDTonTai(khachHang.getCccd())) {
				return false;
			}

			return database.updateKhachHang(khachHang);
		} catch (Exception e) {
			System.err.println("Không thể cập nhật khách hàng " + (khachHang != null ? khachHang.getMaKH() : "null") + ": " + e.getMessage());
			return false;
		}
	}

	public boolean xoaKhachHang(String maKH) {
		try {
			if (maKH == null || maKH.isBlank() || !kiemTraMaKHTonTai(maKH)) {
				return false;
			}
			return database.deleteKhachHang(maKH);
		} catch (Exception e) {
			System.err.println("Không thể xóa khách hàng " + maKH + ": " + e.getMessage());
			return false;
		}
	}

	public boolean kiemTraMaKHTonTai(String maKH) {
		return timKhachHangTheoMa(maKH) != null;
	}

	public boolean kiemTraCCCDTonTai(String cccd) {
		return timKhachHangTheoCCCD(cccd) != null;
	}

	public String layMaKHTiepTheo() {
		try {
			List<KhachHang> danhSach = layTatCaKhachHang();
			if (danhSach.isEmpty()) {
				return "KH001";
			}

			int maxSo = 0;
			for (KhachHang kh : danhSach) {
				String ma = kh.getMaKH();
				if (ma != null && ma.startsWith("KH")) {
					try {
						int so = Integer.parseInt(ma.substring(2));
						maxSo = Math.max(maxSo, so);
					} catch (NumberFormatException ignored) {
						// Bỏ qua mã không đúng định dạng
					}
				}
			}

			return String.format("KH%03d", maxSo + 1);
		} catch (Exception e) {
			System.err.println("Không thể lấy mã khách hàng tiếp theo: " + e.getMessage());
			return "KH001";
		}
	}
}
