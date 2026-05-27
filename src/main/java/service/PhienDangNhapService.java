package service;

import entity.TaiKhoan;
import java.time.LocalDateTime;

public class PhienDangNhapService {
	private static TaiKhoan taiKhoanDangNhap;
	private static LocalDateTime thoiGianDangNhap;

	private PhienDangNhapService() {
	}

	public static void batDauPhien(TaiKhoan taiKhoan) {
		taiKhoanDangNhap = taiKhoan;
		thoiGianDangNhap = LocalDateTime.now();
	}

	public static void ketThucPhien() {
		taiKhoanDangNhap = null;
		thoiGianDangNhap = null;
	}

	public static TaiKhoan layTaiKhoanDangNhap() {
		return taiKhoanDangNhap;
	}

	public static LocalDateTime layThoiGianDangNhap() {
		return thoiGianDangNhap;
	}

	public static boolean daDangNhap() {
		return taiKhoanDangNhap != null;
	}
}
