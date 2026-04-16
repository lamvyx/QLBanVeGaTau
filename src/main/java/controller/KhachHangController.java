package controller;

import entity.KhachHang;
import java.util.List;
import service.KhachHangService;
import service.KhachHangService.KetQuaXuLy;

public class KhachHangController {
	private final KhachHangService khachHangService = new KhachHangService();

	public KetQuaXuLy themKhachHang(String tenKH, String sdt, String cccd, String email, String diaChi,
			boolean gioiTinh, boolean loaiKH) {
		return khachHangService.themKhachHang(chuanHoaChuoi(tenKH), chuanHoaChuoi(sdt), chuanHoaChuoi(cccd),
				chuanHoaChuoiRongThanhNull(email), chuanHoaChuoiRongThanhNull(diaChi), gioiTinh, loaiKH);
	}

	public KetQuaXuLy capNhatKhachHang(String maKH, String tenKH, String sdt, String cccd, String email,
			String diaChi, boolean gioiTinh, boolean loaiKH) {
		return khachHangService.capNhatKhachHang(chuanHoaChuoi(maKH), chuanHoaChuoi(tenKH),
				chuanHoaChuoiRongThanhNull(sdt), chuanHoaChuoiRongThanhNull(cccd), chuanHoaChuoiRongThanhNull(email),
				chuanHoaChuoiRongThanhNull(diaChi), gioiTinh, loaiKH);
	}

	public List<KhachHang> timKiemKhachHang(String maKH, String tenKH) {
		return khachHangService.timKiemKhachHang(chuanHoaChuoiRongThanhNull(maKH), chuanHoaChuoiRongThanhNull(tenKH));
	}

	public List<KhachHang> layTatCaKhachHang() {
		return khachHangService.layTatCaKhachHang();
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
