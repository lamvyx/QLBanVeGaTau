package connectDB;

import entity.ChuyenTau;
import entity.DichVu;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {
	private static final Database INSTANCE = new Database();

	private void logDatabaseError(String operation, Exception e) {
		System.err.println("Lỗi " + operation + ": " + e.getMessage());
	}

	private Database() {
		// Khởi tạo - kết nối sẽ được thực hiện khi cần thiết
	}

	public static Database getInstance() {
		return INSTANCE;
	}

	// ==================== TÀI KHOẢN - TaiKhoan ====================

	/**
	 * Tìm tài khoản theo username từ database
	 */
	public TaiKhoan findByUsername(String username) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null)
				return null;

			String query = "SELECT tk.username, tk.[password], tk.vaiTro, nv.email, nv.tenNV AS hoTen "
					+ "FROM TaiKhoan tk LEFT JOIN NhanVien nv ON tk.username = nv.username "
					+ "WHERE tk.username = ?";
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, username);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						return new TaiKhoan(rs.getString("username"), rs.getString("password"),
								rs.getString("email"), rs.getString("hoTen"), rs.getString("vaiTro"));
					}
				}
			}
		} catch (SQLException e) {
			logDatabaseError("tìm tài khoản theo username " + username, e);
		}
		return null;
	}

	/**
	 * Cập nhật mật khẩu tài khoản
	 */
	public boolean updatePassword(String username, String newPassword) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null)
				return false;

			String query = "UPDATE TaiKhoan SET [password] = ? WHERE username = ?";
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, newPassword);
				stmt.setString(2, username);
				int result = stmt.executeUpdate();
				return result > 0;
			}
		} catch (SQLException e) {
			logDatabaseError("cập nhật mật khẩu cho username " + username, e);
		}
		return false;
	}

	/**
	 * Tạo mới tài khoản
	 */
	public boolean createTaiKhoan(TaiKhoan taiKhoan) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || taiKhoan == null) {
				return false;
			}

			String query = "INSERT INTO TaiKhoan (username, [password], vaiTro) VALUES (?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, taiKhoan.getTenDangNhap());
				stmt.setString(2, taiKhoan.getMatKhau());
				stmt.setString(3, taiKhoan.getVaiTro());

				int result = stmt.executeUpdate();
				return result > 0;
			}
		} catch (SQLException e) {
			logDatabaseError("tạo tài khoản " + (taiKhoan != null ? taiKhoan.getTenDangNhap() : "null"), e);
		}
		return false;
	}

	// ==================== NHÂN VIÊN - NhanVien ====================

	/**
	 * Thêm nhân viên mới vào database
	 */
	public boolean addNhanVien(NhanVien nhanVien) {
		return addNhanVien(nhanVien, null);
	}

	/**
	 * Thêm nhân viên mới vào database (bao gồm email)
	 */
	public boolean addNhanVien(NhanVien nhanVien, String email) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || nhanVien == null)
				return false;

			String query = "INSERT INTO NhanVien (maNV, tenNV, sdt, gioiTinh, ngaySinh, ngayVaoLam, chucVu, trangThai, email, username) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, nhanVien.getMaNV());
				stmt.setString(2, nhanVien.getTenNV());
				stmt.setString(3, nhanVien.getSdt());
				stmt.setBoolean(4, nhanVien.isGioiTinh());
				stmt.setObject(5, nhanVien.getNgaySinh());
				stmt.setObject(6, nhanVien.getNgayVaoLam());
				stmt.setString(7, nhanVien.getChucVu());
				stmt.setBoolean(8, nhanVien.isTrangThai());
				stmt.setString(9, email);
				stmt.setString(10, nhanVien.getUsername());

				int result = stmt.executeUpdate();
				return result > 0;
			}
		} catch (SQLException e) {
			logDatabaseError("thêm nhân viên " + (nhanVien != null ? nhanVien.getMaNV() : "null"), e);
		}
		return false;
	}

	/**
	 * Lấy danh sách tất cả nhân viên từ database
	 */
	public List<NhanVien> getAllNhanVien() {
		List<NhanVien> nhanVienList = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null)
				return nhanVienList;

			String query = "SELECT * FROM NhanVien";
			try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					java.sql.Date ngaySinh = rs.getDate("ngaySinh");
					java.sql.Date ngayVaoLam = rs.getDate("ngayVaoLam");
					NhanVien nhanVien = new NhanVien(rs.getString("maNV"), rs.getString("tenNV"),
							rs.getString("sdt"), rs.getBoolean("gioiTinh"),
							ngaySinh != null ? ngaySinh.toLocalDate() : null,
							ngayVaoLam != null ? ngayVaoLam.toLocalDate() : null, rs.getString("chucVu"),
							rs.getBoolean("trangThai"),
							rs.getString("username"));
					nhanVienList.add(nhanVien);
				}
			}
		} catch (SQLException e) {
			logDatabaseError("lấy tất cả nhân viên", e);
		}
		return nhanVienList;
	}

	/**
	 * Lấy nhân viên theo mã
	 */
	public NhanVien getNhanVienByMa(String maNV) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null)
				return null;

			String query = "SELECT * FROM NhanVien WHERE maNV = ?";
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, maNV);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						java.sql.Date ngaySinh = rs.getDate("ngaySinh");
						java.sql.Date ngayVaoLam = rs.getDate("ngayVaoLam");
						return new NhanVien(rs.getString("maNV"), rs.getString("tenNV"),
								rs.getString("sdt"), rs.getBoolean("gioiTinh"),
								ngaySinh != null ? ngaySinh.toLocalDate() : null,
								ngayVaoLam != null ? ngayVaoLam.toLocalDate() : null, rs.getString("chucVu"),
								rs.getBoolean("trangThai"),
								rs.getString("username"));
					}
				}
			}
		} catch (SQLException e) {
			logDatabaseError("lấy nhân viên theo mã " + maNV, e);
		}
		return null;
	}

	/**
	 * Lấy nhân viên theo username
	 */
	public NhanVien getNhanVienByUsername(String username) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null)
				return null;

			String query = "SELECT * FROM NhanVien WHERE username = ?";
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, username);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						java.sql.Date ngaySinh = rs.getDate("ngaySinh");
						java.sql.Date ngayVaoLam = rs.getDate("ngayVaoLam");
						return new NhanVien(rs.getString("maNV"), rs.getString("tenNV"),
								rs.getString("sdt"), rs.getBoolean("gioiTinh"),
								ngaySinh != null ? ngaySinh.toLocalDate() : null,
								ngayVaoLam != null ? ngayVaoLam.toLocalDate() : null, rs.getString("chucVu"),
								rs.getBoolean("trangThai"),
								rs.getString("username"));
					}
				}
			}
		} catch (SQLException e) {
			logDatabaseError("lấy nhân viên theo username " + username, e);
		}
		return null;
	}

	/**
	 * Cập nhật thông tin nhân viên
	 */
	public boolean updateNhanVien(NhanVien nhanVien) {
		return updateNhanVien(nhanVien, null);
	}

	/**
	 * Cập nhật thông tin nhân viên
	 */
	public boolean updateNhanVien(NhanVien nhanVien, String email) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || nhanVien == null)
				return false;

			String query = "UPDATE NhanVien SET tenNV = ?, sdt = ?, gioiTinh = ?, ngaySinh = ?, ngayVaoLam = ?, "
					+ "chucVu = ?, trangThai = ?, email = COALESCE(?, email) WHERE maNV = ?";

			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, nhanVien.getTenNV());
				stmt.setString(2, nhanVien.getSdt());
				stmt.setBoolean(3, nhanVien.isGioiTinh());
				stmt.setObject(4, nhanVien.getNgaySinh());
				stmt.setObject(5, nhanVien.getNgayVaoLam());
				stmt.setString(6, nhanVien.getChucVu());
				stmt.setBoolean(7, nhanVien.isTrangThai());
				stmt.setString(8, email);
				stmt.setString(9, nhanVien.getMaNV());

				int result = stmt.executeUpdate();
				return result > 0;
			}
		} catch (SQLException e) {
			logDatabaseError("cập nhật nhân viên " + (nhanVien != null ? nhanVien.getMaNV() : "null"), e);
		}
		return false;
	}

	/**
	 * Xóa nhân viên
	 */
	public boolean deleteNhanVien(String maNV) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null)
				return false;

			String query = "DELETE FROM NhanVien WHERE maNV = ?";
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, maNV);
				int result = stmt.executeUpdate();
				return result > 0;
			}
		} catch (SQLException e) {
			logDatabaseError("xóa nhân viên " + maNV, e);
		}
		return false;
	}

	// ==================== KHÁCH HÀNG - KhachHang ====================

	/**
	 * Lấy danh sách tất cả khách hàng từ database
	 */
	public List<KhachHang> getAllKhachHang() {
		List<KhachHang> khachHangList = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null)
				return khachHangList;

			String query = "SELECT * FROM KhachHang";
			try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					java.sql.Date ngaySinh = rs.getDate("ngaySinh");
					KhachHang kh = new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("sdt"),
							rs.getString("cccd"), rs.getString("diaChi"), rs.getString("email"),
							rs.getBoolean("gioiTinh"), ngaySinh != null ? ngaySinh.toLocalDate() : null,
							rs.getBoolean("loaiKH"));
					khachHangList.add(kh);
				}
			}
		} catch (SQLException e) {
			logDatabaseError("lấy tất cả khách hàng", e);
		}
		return khachHangList;
	}

	/**
	 * Lấy khách hàng theo mã
	 */
	public KhachHang getKhachHangByMa(String maKH) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null) {
				return null;
			}

			String query = "SELECT * FROM KhachHang WHERE maKH = ?";
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, maKH);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						java.sql.Date ngaySinh = rs.getDate("ngaySinh");
						return new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("sdt"),
								rs.getString("cccd"), rs.getString("diaChi"), rs.getString("email"),
								rs.getBoolean("gioiTinh"), ngaySinh != null ? ngaySinh.toLocalDate() : null,
								rs.getBoolean("loaiKH"));
					}
				}
			}
		} catch (SQLException e) {
			logDatabaseError("lấy khách hàng theo mã " + maKH, e);
		}
		return null;
	}

	/**
	 * Thêm mới khách hàng
	 */
	public boolean addKhachHang(KhachHang khachHang) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || khachHang == null) {
				return false;
			}

			String query = "INSERT INTO KhachHang (maKH, tenKH, sdt, cccd, diaChi, email, gioiTinh, ngaySinh, loaiKH) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, khachHang.getMaKH());
				stmt.setString(2, khachHang.getTenKH());
				stmt.setString(3, khachHang.getSdt());
				stmt.setString(4, khachHang.getCccd());
				stmt.setString(5, khachHang.getDiaChi());
				stmt.setString(6, khachHang.getEmail());
				stmt.setBoolean(7, khachHang.isGioiTinh());
				stmt.setObject(8, khachHang.getNgaySinh());
				stmt.setBoolean(9, khachHang.isLoaiKH());

				int result = stmt.executeUpdate();
				return result > 0;
			}
		} catch (SQLException e) {
			logDatabaseError("thêm khách hàng " + (khachHang != null ? khachHang.getMaKH() : "null"), e);
		}
		return false;
	}

	/**
	 * Cập nhật thông tin khách hàng
	 */
	public boolean updateKhachHang(KhachHang khachHang) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null || khachHang == null) {
				return false;
			}

			String query = "UPDATE KhachHang SET tenKH = ?, sdt = ?, cccd = ?, diaChi = ?, email = ?, gioiTinh = ?, ngaySinh = ?, loaiKH = ? "
					+ "WHERE maKH = ?";
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, khachHang.getTenKH());
				stmt.setString(2, khachHang.getSdt());
				stmt.setString(3, khachHang.getCccd());
				stmt.setString(4, khachHang.getDiaChi());
				stmt.setString(5, khachHang.getEmail());
				stmt.setBoolean(6, khachHang.isGioiTinh());
				stmt.setObject(7, khachHang.getNgaySinh());
				stmt.setBoolean(8, khachHang.isLoaiKH());
				stmt.setString(9, khachHang.getMaKH());

				int result = stmt.executeUpdate();
				return result > 0;
			}
		} catch (SQLException e) {
			logDatabaseError("cập nhật khách hàng " + (khachHang != null ? khachHang.getMaKH() : "null"), e);
		}
		return false;
	}

	/**
	 * Xóa khách hàng
	 */
	public boolean deleteKhachHang(String maKH) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null)
				return false;

			String query = "DELETE FROM KhachHang WHERE maKH = ?";
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, maKH);
				int result = stmt.executeUpdate();
				return result > 0;
			}
		} catch (SQLException e) {
			logDatabaseError("xóa khách hàng " + maKH, e);
		}
		return false;
	}

	// ==================== DỊCH VỤ - DichVu ====================

	/**
	 * Lấy danh sách tất cả dịch vụ từ database
	 */
	public List<DichVu> getAllDichVu() {
		List<DichVu> dichVuList = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null)
				return dichVuList;

			String query = "SELECT * FROM DichVu";
			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
				while (rs.next()) {
					DichVu dv = new DichVu(rs.getString("maDV"), rs.getString("tenDV"), rs.getBoolean("trangThai"),
							rs.getBigDecimal("giaTien"));
					dichVuList.add(dv);
				}
			}
		} catch (SQLException e) {
			logDatabaseError("lấy tất cả dịch vụ", e);
		}
		return dichVuList;
	}

	// ==================== CHUYẾN TÀU - ChuyenTau ====================

	/**
	 * Lấy danh sách tất cả chuyến tàu từ database
	 */
	public List<ChuyenTau> getAllChuyenTau() {
		List<ChuyenTau> chuyenTauList = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null)
				return chuyenTauList;

			String query = "SELECT * FROM ChuyenTau";
			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
				while (rs.next()) {
					ChuyenTau ct = new ChuyenTau(rs.getString("maCT"), rs.getTimestamp("ngayKhoiHanh").toLocalDateTime(),
							rs.getTimestamp("gioKhoiHanh").toLocalDateTime(), rs.getBoolean("trangThai"),
							rs.getString("maTau"), rs.getString("maTuyenTau"));
					chuyenTauList.add(ct);
				}
			}
		} catch (SQLException e) {
			logDatabaseError("lấy tất cả chuyến tàu", e);
		}
		return chuyenTauList;
	}

	// ==================== KHUYẾN MÃI - KhuyenMai ====================

	/**
	 * Lấy danh sách tất cả khuyến mãi từ database
	 */
	public List<KhuyenMai> getAllKhuyenMai() {
		List<KhuyenMai> khuyenMaiList = new ArrayList<>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			if (conn == null)
				return khuyenMaiList;

			String query = "SELECT * FROM KhuyenMai";
			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
				while (rs.next()) {
					KhuyenMai km = new KhuyenMai(rs.getString("maKM"), rs.getString("tenKM"),
							rs.getBigDecimal("tyLeKM"), rs.getDate("ngayBD").toLocalDate(),
							rs.getDate("ngayKT").toLocalDate());
					khuyenMaiList.add(km);
				}
			}
		} catch (SQLException e) {
			logDatabaseError("lấy tất cả khuyến mãi", e);
		}
		return khuyenMaiList;
	}

	// ==================== TEST KẾT NỐI SQL SERVER ====================

	/**
	 * Test kết nối SQL Server
	 */
	public static boolean testSQLServerConnection() {
		return DatabaseConnection.testConnection();
	}

	/**
	 * Kiểm tra database có tồn tại không
	 */
	public static boolean checkDatabase() {
		return DatabaseConnection.databaseExists();
	}

	/**
	 * Hiển thị thông tin kết nối
	 */
	public static void showConnectionInfo() {
		DatabaseConnection.displayConnectionInfo();
	}

	/**
	 * Main method - Test kết nối
	 */
	public static void main(String[] args) {
		System.out.println("\n========== TEST HỆ THỐNG DATABASE ==========\n");

		// Hiển thị thông tin
		showConnectionInfo();

		// Kiểm tra database có tồn tại không
		System.out.println("1. Kiểm tra database:");
		boolean dbExists = checkDatabase();

		// Test kết nối
		System.out.println("\n2. Test kết nối:");
		boolean connected = testSQLServerConnection();

		// Hiển thị kết quả
		System.out.println("\n========== KẾT QUẢ ==========");
		if (connected && dbExists) {
			System.out.println("✓ Kết nối SQL Server thành công!");
			System.out.println("✓ Database sẵn sàng sử dụng!");
		} else {
			System.out.println("✗ Không thể kết nối SQL Server!");
			System.out.println("\nVui lòng kiểm tra:");
			System.out.println("1. SQL Server đã được khởi động chưa?");
			System.out.println("2. Thông tin server, port, username, password có chính xác không?");
			System.out.println("3. JDBC Driver có được thêm vào project không?");
			System.out.println("4. Database QL quản lý vé tàu đã được tạo chưa?");
		}
		System.out.println("=============================\n");

		// Đóng kết nối
		DatabaseConnection.disconnect();
	}
}