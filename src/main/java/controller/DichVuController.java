package controller;

import entity.DichVu;
import java.math.BigDecimal;
import java.util.List;
import service.DichVuService;
import service.DichVuService.KetQuaXuLy;

public class DichVuController {
	private final DichVuService dichVuService = new DichVuService();

	public List<DichVu> timKiemDichVu(String maDV, String tenDV) {
		return dichVuService.timKiemDichVu(chuanHoaRongThanhNull(maDV), chuanHoaRongThanhNull(tenDV));
	}

	public KetQuaXuLy capNhatDichVu(String maDV, String tenDV, BigDecimal giaDV, boolean trangThai) {
		return dichVuService.capNhatDichVu(chuanHoa(maDV), chuanHoa(tenDV), giaDV, trangThai);
	}

	public KetQuaXuLy xoaDichVu(String maDV) {
		return dichVuService.xoaDichVu(chuanHoa(maDV));
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
