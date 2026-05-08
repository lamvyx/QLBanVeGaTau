package view;

import connectDB.Database;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class NghiepVuVeService {
	private static final Map<String, HoldInfo> HOLDS = new ConcurrentHashMap<>();

	private NghiepVuVeService() {
	}

	public static synchronized void giuCho(String maChuyen, String maToa, String maGhe, String nguoiGiu, int phut)
			throws IllegalArgumentException {
		validateKey(maChuyen, maToa, maGhe);
		String key = key(maChuyen, maToa, maGhe);
		cleanupExpired();

		HoldInfo current = HOLDS.get(key);
		if (current != null && !current.isExpired()) {
			throw new IllegalArgumentException("Ghế " + maGhe + " đã được giữ chỗ.");
		}

		HOLDS.put(key, new HoldInfo(LocalDateTime.now().plusMinutes(Math.max(1, phut))));
	}

	public static synchronized void huyGiuCho(String maChuyen, String maToa, String maGhe) {
		if (isBlank(maChuyen) || isBlank(maToa) || isBlank(maGhe)) {
			return;
		}
		HOLDS.remove(key(maChuyen, maToa, maGhe));
	}

	public static List<VeThongTin> taoVeNhapTuBanVe(
			String khachHang,
			String giayTo,
			String chuyen,
			String tuyen,
			String khoiHanh,
			String thoiGianDen,
			String toa,
			Set<String> gheDaChon,
			BigDecimal giaGoc,
			String phuongThucThanhToan,
			String nhanVien) {

		if (gheDaChon == null || gheDaChon.isEmpty()) {
			return Collections.emptyList();
		}

		List<String> gheSapXep = new ArrayList<>(gheDaChon);
		Collections.sort(gheSapXep);

		List<VeThongTin> result = new ArrayList<>();
		for (int i = 0; i < gheSapXep.size(); i++) {
			String ghe = gheSapXep.get(i);
			String maVe = buildMaVe(chuyen, toa, ghe, i);
			VeThongTin ve = new VeThongTin(
					maVe,
					khachHang,
					giayTo,
					chuyen,
					tuyen,
					khoiHanh,
					thoiGianDen,
					toa,
					ghe,
					giaGoc,
					phuongThucThanhToan,
					nhanVien);
			
			// Thêm logic lưu vào CSDL
			luuVeVaoCSDL(ve);
			result.add(ve);
		}
		return result;
	}

	private static void luuVeVaoCSDL(VeThongTin ve) {
		String sqlVe = "INSERT INTO VeTau (maVeTau, maKH, maCT, maToa, giaVe, trangThai) VALUES (?, (SELECT maKH FROM KhachHang WHERE CCCD = ? OR tenKH = ?), ?, ?, ?, 'CHO_THANH_TOAN')";
		String sqlChiTiet = "INSERT INTO ChiTietVeTau (maChiTiet, maVeTau, tenHanhKhach, CCCD, ngaySinh, viTriGhe, loaiVe, giaVeTheoLoai) VALUES (?, ?, ?, ?, '2000-01-01', ?, 'NGUOI_LON', ?)";
		
		try (Connection conn = Database.getConnection()) {
			conn.setAutoCommit(false);
			try {
				try (PreparedStatement ps = conn.prepareStatement(sqlVe)) {
					ps.setString(1, ve.getMaVe());
					ps.setString(2, ve.getGiayTo());
					ps.setString(3, ve.getKhachHang());
					ps.setString(4, ve.getChuyen());
					ps.setString(5, ve.getToa());
					ps.setBigDecimal(6, ve.getGiaGoc());
					ps.executeUpdate();
				}
				
				try (PreparedStatement ps = conn.prepareStatement(sqlChiTiet)) {
					ps.setString(1, "CT" + ve.getMaVe().substring(2));
					ps.setString(2, ve.getMaVe());
					ps.setString(3, ve.getKhachHang());
					ps.setString(4, ve.getGiayTo());
					ps.setString(5, ve.getGhe());
					ps.setBigDecimal(6, ve.getGiaGoc());
					ps.executeUpdate();
				}
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				System.err.println("Lỗi lưu vé: " + e.getMessage());
			}
		} catch (SQLException e) {
			System.err.println("Lỗi kết nối CSDL: " + e.getMessage());
		}
	}

	private static void validateKey(String maChuyen, String maToa, String maGhe) {
		if (isBlank(maChuyen) || isBlank(maToa) || isBlank(maGhe)) {
			throw new IllegalArgumentException("Thiếu thông tin chuyến, toa hoặc ghế.");
		}
	}

	private static String key(String maChuyen, String maToa, String maGhe) {
		return maChuyen.trim() + "|" + maToa.trim() + "|" + maGhe.trim();
	}

	private static void cleanupExpired() {
		for (Map.Entry<String, HoldInfo> entry : HOLDS.entrySet()) {
			if (entry.getValue().isExpired()) {
				HOLDS.remove(entry.getKey());
			}
		}
	}

	private static boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private static String buildMaVe(String chuyen, String toa, String ghe, int index) {
		String base = Objects.toString(chuyen, "CT") + Objects.toString(toa, "TOA") + ghe + index + System.currentTimeMillis();
		int hash = Math.abs(base.hashCode());
		return "VE" + String.format("%08d", hash % 100000000);
	}

	private static final class HoldInfo {
		private final LocalDateTime expiresAt;

		private HoldInfo(LocalDateTime expiresAt) {
			this.expiresAt = expiresAt;
		}

		private boolean isExpired() {
			return LocalDateTime.now().isAfter(expiresAt);
		}
	}

	public static final class VeThongTin {
		private final String maVe;
		private final String khachHang;
		private final String giayTo;
		private final String chuyen;
		private final String tuyen;
		private final String khoiHanh;
		private final String thoiGianDen;
		private final String toa;
		private final String ghe;
		private final BigDecimal giaGoc;
		private final String phuongThucThanhToan;
		private final String nhanVien;

		private VeThongTin(
				String maVe,
				String khachHang,
				String giayTo,
				String chuyen,
				String tuyen,
				String khoiHanh,
				String thoiGianDen,
				String toa,
				String ghe,
				BigDecimal giaGoc,
				String phuongThucThanhToan,
				String nhanVien) {
			this.maVe = maVe;
			this.khachHang = khachHang;
			this.giayTo = giayTo;
			this.chuyen = chuyen;
			this.tuyen = tuyen;
			this.khoiHanh = khoiHanh;
			this.thoiGianDen = thoiGianDen;
			this.toa = toa;
			this.ghe = ghe;
			this.giaGoc = giaGoc;
			this.phuongThucThanhToan = phuongThucThanhToan;
			this.nhanVien = nhanVien;
		}

		public String getMaVe() {
			return maVe;
		}

		public String getKhachHang() {
			return khachHang;
		}

		public String getGiayTo() {
			return giayTo;
		}

		public String getChuyen() {
			return chuyen;
		}

		public String getTuyen() {
			return tuyen;
		}

		public String getKhoiHanh() {
			return khoiHanh;
		}

		public String getThoiGianDen() {
			return thoiGianDen;
		}

		public String getToa() {
			return toa;
		}

		public String getGhe() {
			return ghe;
		}

		public BigDecimal getGiaGoc() {
			return giaGoc;
		}

		public String getPhuongThucThanhToan() {
			return phuongThucThanhToan;
		}

		public String getNhanVien() {
			return nhanVien;
		}
	}
}
