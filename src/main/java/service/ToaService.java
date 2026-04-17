package service;

import dao.Toa_DAO;
import entity.Toa;
import java.util.ArrayList;
import java.util.List;

public class ToaService {
	private final Toa_DAO toaDAO = new Toa_DAO();

	public List<Toa> timKiemToa(String maToa) {
		if (maToa != null && !maToa.trim().isEmpty()) {
			Toa toa = toaDAO.timTheoMa(maToa.trim());
			return toa == null ? List.of() : List.of(toa);
		}
		return toaDAO.layTatCaToa();
	}

	public List<String> layDanhSachMaTau() {
		return new ArrayList<>(toaDAO.layDanhSachMaTau());
	}

	public KetQuaXuLy capNhatToa(String maToa, String maTau, String loaiToa, int soGhe, String viTriToa, boolean trangThai) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maToa == null || maToa.isBlank()) {
			ketQua.thongBao = "Mã toa không hợp lệ";
			return ketQua;
		}
		if (maTau == null || maTau.isBlank()) {
			ketQua.thongBao = "Mã tàu không được để trống";
			return ketQua;
		}
		if (loaiToa == null || loaiToa.isBlank()) {
			ketQua.thongBao = "Loại toa không được để trống";
			return ketQua;
		}
		if (soGhe <= 0) {
			ketQua.thongBao = "Số ghế phải lớn hơn 0";
			return ketQua;
		}

		Toa current = toaDAO.timTheoMa(maToa.trim());
		if (current == null) {
			ketQua.thongBao = "Không tìm thấy toa";
			return ketQua;
		}

		current.setMaTau(maTau.trim());
		current.setLoaiToa(loaiToa.trim());
		current.setSoGhe(soGhe);
		current.setViTriToa(viTriToa == null ? null : viTriToa.trim());
		current.setTrangThai(trangThai);
		boolean ok = toaDAO.capNhatToa(current);
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maToa.trim();
		ketQua.thongBao = ok ? "Cập nhật toa thành công" : "Không thể cập nhật toa";
		return ketQua;
	}

	public KetQuaXuLy xoaToa(String maToa) {
		KetQuaXuLy ketQua = new KetQuaXuLy();
		if (maToa == null || maToa.isBlank()) {
			ketQua.thongBao = "Mã toa không hợp lệ";
			return ketQua;
		}
		boolean ok = toaDAO.xoaToa(maToa.trim());
		ketQua.thanhCong = ok;
		ketQua.maThamChieu = maToa.trim();
		ketQua.thongBao = ok ? "Xóa toa thành công" : "Không thể xóa toa (có thể đang liên kết với vé/chuyến tàu)";
		return ketQua;
	}

	public static class KetQuaXuLy {
		public boolean thanhCong;
		public String thongBao;
		public String maThamChieu;
	}
}
