package dao;

import connectDB.Database;
import entity.NhanVien;
import entity.TaiKhoan;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_DAO {
	private final Database database = Database.getInstance();

	/**
	 * Thêm nhân viên mới vào cơ sở dữ liệu
	 * @param nhanVien Đối tượng nhân viên cần thêm
	 * @return true nếu thêm thành công, false nếu thất bại
	 */
	public boolean themNhanVien(NhanVien nhanVien) {
		return themNhanVien(nhanVien, null);
	}

	public boolean themNhanVien(NhanVien nhanVien, String email) {
		try {
			// Kiểm tra mã nhân viên đã tồn tại chưa
			if (kiemTraMaNVTonTai(nhanVien.getMaNV())) {
				return false;
			}
			
			// Kiểm tra username đã tồn tại chưa
			if (kiemTraUsernameTonTai(nhanVien.getUsername())) {
				return false;
			}
			
			return database.addNhanVien(nhanVien, email);
		} catch (Exception e) {
			System.err.println("Không thể thêm nhân viên " + (nhanVien != null ? nhanVien.getMaNV() : "null") + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 * Lấy danh sách tất cả nhân viên
	 * @return Danh sách nhân viên
	 */
	public List<NhanVien> layTatCaNhanVien() {
		try {
			return database.getAllNhanVien();
		} catch (Exception e) {
			System.err.println("Không thể lấy danh sách nhân viên: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Lấy danh sách nhân viên đang hoạt động
	 * @return Danh sách nhân viên hoạt động
	 */
	public List<NhanVien> layNhanVienHoatDong() {
		try {
			List<NhanVien> allNhanVien = database.getAllNhanVien();
			List<NhanVien> nhanVienHoatDong = new ArrayList<>();
			
			for (NhanVien nv : allNhanVien) {
				if (nv.isTrangThai()) {
					nhanVienHoatDong.add(nv);
				}
			}
			
			return nhanVienHoatDong;
		} catch (Exception e) {
			System.err.println("Không thể lấy danh sách nhân viên hoạt động: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Tìm nhân viên theo mã
	 * @param maNV Mã nhân viên
	 * @return Đối tượng nhân viên hoặc null nếu không tìm thấy
	 */
	public NhanVien timNhanVienTheoMa(String maNV) {
		try {
			return database.getNhanVienByMa(maNV);
		} catch (Exception e) {
			System.err.println("Không thể tìm nhân viên theo mã " + maNV + ": " + e.getMessage());
			return null;
		}
	}

	/**
	 * Tìm nhân viên theo username
	 * @param username Tên đăng nhập
	 * @return Đối tượng nhân viên hoặc null nếu không tìm thấy
	 */
	public NhanVien timNhanVienTheoUsername(String username) {
		try {
			return database.getNhanVienByUsername(username);
		} catch (Exception e) {
			System.err.println("Không thể tìm nhân viên theo username " + username + ": " + e.getMessage());
			return null;
		}
	}

	/**
	 * Tìm nhân viên theo tên (tìm kiếm gần đúng)
	 * @param tenNV Tên nhân viên cần tìm
	 * @return Danh sách nhân viên phù hợp
	 */
	public List<NhanVien> timNhanVienTheoTen(String tenNV) {
		try {
			List<NhanVien> allNhanVien = database.getAllNhanVien();
			List<NhanVien> result = new ArrayList<>();
			
			String tenSearch = tenNV.toLowerCase();
			for (NhanVien nv : allNhanVien) {
				if (nv.getTenNV().toLowerCase().contains(tenSearch)) {
					result.add(nv);
				}
			}
			
			return result;
		} catch (Exception e) {
			System.err.println("Không thể tìm nhân viên theo tên " + tenNV + ": " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Tìm nhân viên theo số điện thoại
	 * @param sdt Số điện thoại
	 * @return Đối tượng nhân viên hoặc null nếu không tìm thấy
	 */
	public NhanVien timNhanVienTheoSDT(String sdt) {
		try {
			List<NhanVien> allNhanVien = database.getAllNhanVien();
			
			for (NhanVien nv : allNhanVien) {
				if (nv.getSdt().equals(sdt)) {
					return nv;
				}
			}
			
			return null;
		} catch (Exception e) {
			System.err.println("Không thể tìm nhân viên theo số điện thoại " + sdt + ": " + e.getMessage());
			return null;
		}
	}

	/**
	 * Tìm nhân viên theo chức vụ
	 * @param chucVu Chức vụ cần tìm
	 * @return Danh sách nhân viên có chức vụ tương ứng
	 */
	public List<NhanVien> timNhanVienTheoChucVu(String chucVu) {
		try {
			List<NhanVien> allNhanVien = database.getAllNhanVien();
			List<NhanVien> result = new ArrayList<>();
			
			for (NhanVien nv : allNhanVien) {
				if (nv.getChucVu().equalsIgnoreCase(chucVu)) {
					result.add(nv);
				}
			}
			
			return result;
		} catch (Exception e) {
			System.err.println("Không thể tìm nhân viên theo chức vụ " + chucVu + ": " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Cập nhật thông tin nhân viên
	 * @param nhanVien Đối tượng nhân viên cần cập nhật
	 * @return true nếu cập nhật thành công, false nếu thất bại
	 */
	public boolean capNhatNhanVien(NhanVien nhanVien) {
		return capNhatNhanVien(nhanVien, null);
	}

	public boolean capNhatNhanVien(NhanVien nhanVien, String email) {
		try {
			NhanVien existing = database.getNhanVienByMa(nhanVien.getMaNV());
			if (existing == null) {
				return false;
			}
			
			return database.updateNhanVien(nhanVien, email);
		} catch (Exception e) {
			System.err.println("Không thể cập nhật nhân viên " + (nhanVien != null ? nhanVien.getMaNV() : "null") + ": " + e.getMessage());
			return false;
		}
	}

	public String timEmailTheoUsername(String username) {
		try {
			TaiKhoan taiKhoan = Database.getInstance().findByUsername(username);
			return taiKhoan != null ? taiKhoan.getEmail() : null;
		} catch (Exception e) {
			System.err.println("Không thể tìm email theo username " + username + ": " + e.getMessage());
			return null;
		}
	}

	/**
	 * Cập nhật chỉ một số trường của nhân viên
	 * @param maNV Mã nhân viên
	 * @param tenNV Tên nhân viên
	 * @param sdt Số điện thoại
	 * @param chucVu Chức vụ
	 * @return true nếu cập nhật thành công
	 */
	public boolean capNhatThongTinCoBan(String maNV, String tenNV, String sdt, String chucVu) {
		try {
			NhanVien nhanVien = database.getNhanVienByMa(maNV);
			if (nhanVien == null) {
				return false;
			}
			
			nhanVien.setTenNV(tenNV);
			nhanVien.setSdt(sdt);
			nhanVien.setChucVu(chucVu);
			
			return database.updateNhanVien(nhanVien);
		} catch (Exception e) {
			System.err.println("Không thể cập nhật thông tin cơ bản nhân viên " + maNV + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 * Cập nhật trạng thái nhân viên
	 * @param maNV Mã nhân viên
	 * @param trangThai Trạng thái (true = hoạt động, false = không hoạt động)
	 * @return true nếu cập nhật thành công
	 */
	public boolean capNhatTrangThai(String maNV, boolean trangThai) {
		try {
			NhanVien nhanVien = database.getNhanVienByMa(maNV);
			if (nhanVien == null) {
				return false;
			}
			
			nhanVien.setTrangThai(trangThai);
			return database.updateNhanVien(nhanVien);
		} catch (Exception e) {
			System.err.println("Không thể cập nhật trạng thái nhân viên " + maNV + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 * Xóa nhân viên (xóa mềm - đánh dấu trạng thái)
	 * @param maNV Mã nhân viên
	 * @return true nếu xóa thành công
	 */
	public boolean xoaNhanVien(String maNV) {
		try {
			NhanVien nhanVien = database.getNhanVienByMa(maNV);
			if (nhanVien == null) {
				return false;
			}
			
			// Xóa mềm: chỉ cập nhật trạng thái
			nhanVien.setTrangThai(false);
			return database.updateNhanVien(nhanVien);
		} catch (Exception e) {
			System.err.println("Không thể xóa mềm nhân viên " + maNV + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 * Xóa nhân viên vĩnh viễn
	 * @param maNV Mã nhân viên
	 * @return true nếu xóa thành công
	 */
	public boolean xoaVinhVien(String maNV) {
		try {
			return database.deleteNhanVien(maNV);
		} catch (Exception e) {
			System.err.println("Không thể xóa vĩnh viễn nhân viên " + maNV + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 * Kiểm tra mã nhân viên đã tồn tại chưa
	 * @param maNV Mã nhân viên
	 * @return true nếu tồn tại, false nếu không
	 */
	public boolean kiemTraMaNVTonTai(String maNV) {
		try {
			return database.getNhanVienByMa(maNV) != null;
		} catch (Exception e) {
			System.err.println("Không thể kiểm tra mã nhân viên " + maNV + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 * Kiểm tra username đã tồn tại chưa
	 * @param username Tên đăng nhập
	 * @return true nếu tồn tại, false nếu không
	 */
	public boolean kiemTraUsernameTonTai(String username) {
		try {
			return database.getNhanVienByUsername(username) != null;
		} catch (Exception e) {
			System.err.println("Không thể kiểm tra username " + username + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 * Kiểm tra số điện thoại đã tồn tại chưa
	 * @param sdt Số điện thoại
	 * @return true nếu tồn tại, false nếu không
	 */
	public boolean kiemTraSDTTonTai(String sdt) {
		try {
			return timNhanVienTheoSDT(sdt) != null;
		} catch (Exception e) {
			System.err.println("Không thể kiểm tra số điện thoại nhân viên " + sdt + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 * Lấy mã nhân viên tiếp theo
	 * @return Mã nhân viên dự kiến
	 */
	public String layMaNVTiepTheo() {
		try {
			List<NhanVien> allNhanVien = database.getAllNhanVien();
			if (allNhanVien.isEmpty()) {
				return "NV001";
			}
			
			int maxNumber = 0;
			for (NhanVien nv : allNhanVien) {
				String ma = nv.getMaNV();
				if (ma.startsWith("NV")) {
					try {
						int num = Integer.parseInt(ma.substring(2));
						maxNumber = Math.max(maxNumber, num);
					} catch (NumberFormatException e) {
						// Bỏ qua nếu không thể parse
					}
				}
			}
			
			return String.format("NV%03d", maxNumber + 1);
		} catch (Exception e) {
			System.err.println("Không thể lấy mã nhân viên tiếp theo: " + e.getMessage());
			return "NV001";
		}
	}

	/**
	 * Đếm tổng số nhân viên
	 * @return Tổng số nhân viên
	 */
	public int demTongSoNhanVien() {
		try {
			return database.getAllNhanVien().size();
		} catch (Exception e) {
			System.err.println("Không thể đếm tổng số nhân viên: " + e.getMessage());
			return 0;
		}
	}

	/**
	 * Đếm số nhân viên hoạt động
	 * @return Số nhân viên hoạt động
	 */
	public int demNhanVienHoatDong() {
		try {
			return layNhanVienHoatDong().size();
		} catch (Exception e) {
			System.err.println("Không thể đếm số nhân viên hoạt động: " + e.getMessage());
			return 0;
		}
	}
}
