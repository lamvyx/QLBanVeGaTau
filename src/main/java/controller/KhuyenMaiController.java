package controller;

import entity.KhuyenMai;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import service.KhuyenMaiService;
import service.KhuyenMaiService.KetQuaXuLy;

public class KhuyenMaiController {
	private final KhuyenMaiService khuyenMaiService = new KhuyenMaiService();

	public List<KhuyenMai> timKiemKhuyenMai(String maKM, String tenKM) {
		return khuyenMaiService.timKiemKhuyenMai(chuanHoaRongThanhNull(maKM), chuanHoaRongThanhNull(tenKM));
	}

	public KetQuaXuLy capNhatKhuyenMai(String maKM, String tenKM, BigDecimal tyLeKM, LocalDate ngayBD,
			LocalDate ngayKT) {
		return khuyenMaiService.capNhatKhuyenMai(chuanHoa(maKM), chuanHoa(tenKM), tyLeKM, ngayBD, ngayKT);
	}

	public KetQuaXuLy xoaKhuyenMai(String maKM) {
		return khuyenMaiService.xoaKhuyenMai(chuanHoa(maKM));
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
