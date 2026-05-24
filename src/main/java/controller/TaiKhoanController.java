package controller;

import entity.TaiKhoan;
import service.TaiKhoanService;
import service.TaiKhoanService.KetQuaDangNhap;
import service.TaiKhoanService.KetQuaXuLy;

public class TaiKhoanController {
	private final TaiKhoanService taiKhoanService = new TaiKhoanService();

	public KetQuaDangNhap dangNhap(String tenDangNhap, String matKhau) {
		return taiKhoanService.dangNhap(chuanHoaChuoi(tenDangNhap), chuanHoaChuoi(matKhau));
	}

	public void dangXuat() {
		taiKhoanService.dangXuat();
	}

	public String layEmailTheoTaiKhoan(String tenDangNhap) {
		return taiKhoanService.layEmailTheoTaiKhoan(chuanHoaChuoiRongThanhNull(tenDangNhap));
	}

	public KetQuaXuLy datLaiMatKhau(String tenDangNhap, String matKhauMoi, String xacNhan) {
		return taiKhoanService.datLaiMatKhau(chuanHoaChuoi(tenDangNhap), chuanHoaChuoi(matKhauMoi),
				chuanHoaChuoi(xacNhan));
	}

	public TaiKhoan layTaiKhoanDangNhap() {
		return taiKhoanService.layTaiKhoanDangNhap();
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
