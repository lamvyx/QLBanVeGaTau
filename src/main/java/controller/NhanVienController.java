package controller;

import entity.NhanVien;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import service.NhanVienService;
import service.NhanVienService.KetQuaXuLy;

public class NhanVienController {
	private final NhanVienService nhanVienService = new NhanVienService();

	public KetQuaXuLy themNhanVienTuForm(String tenNV, String username, String matKhau, String sdt, String email,
			String chucVu, boolean gioiTinh, String ngaySinhText, String ngayVaoLamText) {
		LocalDate ngaySinh = parseDateOrDefault(ngaySinhText, LocalDate.now().minusYears(25));
		LocalDate ngayVaoLam = parseDateOrDefault(ngayVaoLamText, LocalDate.now());

		return themNhanVien(tenNV, username, matKhau, sdt, email, chucVu, gioiTinh, ngaySinh, ngayVaoLam);
	}

	public KetQuaXuLy themNhanVien(String tenNV, String username, String matKhau, String sdt, String email,
			String chucVu, boolean gioiTinh, LocalDate ngaySinh, LocalDate ngayVaoLam) {
		return nhanVienService.themNhanVien(chuanHoaChuoi(tenNV), chuanHoaChuoi(username), chuanHoaChuoi(matKhau),
				chuanHoaChuoiRongThanhNull(sdt), chuanHoaChuoi(email), chuanHoaChuoiRongThanhNull(chucVu), gioiTinh,
				ngaySinh, ngayVaoLam);
	}

	public KetQuaXuLy capNhatNhanVien(String maNV, String tenNV, String sdt, String email, String chucVu,
			boolean gioiTinh, LocalDate ngaySinh, LocalDate ngayVaoLam) {
		return nhanVienService.capNhatNhanVien(chuanHoaChuoi(maNV), chuanHoaChuoi(tenNV), chuanHoaChuoiRongThanhNull(sdt),
				chuanHoaChuoiRongThanhNull(email), chuanHoaChuoiRongThanhNull(chucVu), gioiTinh, ngaySinh, ngayVaoLam);
	}

	public List<NhanVien> timKiemNhanVien(String maNV, String tenNV) {
		return nhanVienService.timKiemNhanVien(chuanHoaChuoiRongThanhNull(maNV), chuanHoaChuoiRongThanhNull(tenNV));
	}

	public List<NhanVien> layTatCaNhanVien() {
		return nhanVienService.layTatCaNhanVien();
	}

	public String layEmailTheoUsername(String username) {
		return nhanVienService.layEmailTheoUsername(chuanHoaChuoiRongThanhNull(username));
	}

	private LocalDate parseDateOrDefault(String rawValue, LocalDate defaultValue) {
		if (rawValue == null || rawValue.isBlank()) {
			return defaultValue;
		}
		try {
			return LocalDate.parse(rawValue.trim());
		} catch (DateTimeParseException ex) {
			return defaultValue;
		}
	}

	private String chuanHoaChuoi(String value) {
		return value == null ? null : value.trim();
	}

	private String chuanHoaChuoiRongThanhNull(String value) {
		if (value == null) {
			return null;
		}
		String normalized = value.trim();
		return normalized.isEmpty() ? null : normalized;
	}
}
