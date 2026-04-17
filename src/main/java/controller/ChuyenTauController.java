package controller;

import entity.ChuyenTau;
import java.time.LocalDateTime;
import java.util.List;
import service.ChuyenTauService;
import service.ChuyenTauService.KetQuaXuLy;

public class ChuyenTauController {
	private final ChuyenTauService chuyenTauService = new ChuyenTauService();

	public List<ChuyenTau> timKiemChuyenTau(String tuKhoa) {
		return chuyenTauService.timKiemChuyenTau(chuanHoaRongThanhNull(tuKhoa));
	}

	public KetQuaXuLy themChuyenTau(String maTau, String maTuyenTau, LocalDateTime ngayGioKhoiHanh) {
		return chuyenTauService.themChuyenTau(chuanHoa(maTau), chuanHoa(maTuyenTau), ngayGioKhoiHanh);
	}

	public KetQuaXuLy capNhatChuyenTau(String maCT, String maTau, String maTuyenTau, LocalDateTime ngayGioKhoiHanh,
			boolean trangThai) {
		return chuyenTauService.capNhatChuyenTau(chuanHoa(maCT), chuanHoaRongThanhNull(maTau),
				chuanHoaRongThanhNull(maTuyenTau), ngayGioKhoiHanh, trangThai);
	}

	public KetQuaXuLy xoaChuyenTau(String maCT) {
		return chuyenTauService.xoaChuyenTau(chuanHoa(maCT));
	}

	private String chuanHoa(String s) {
		return s == null ? null : s.trim();
	}

	private String chuanHoaRongThanhNull(String s) {
		if (s == null) {
			return null;
		}
		String v = s.trim();
		return v.isEmpty() ? null : v;
	}
}
