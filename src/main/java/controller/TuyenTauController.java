package controller;

import entity.TuyenTau;
import java.util.List;
import service.TuyenTauService;
import service.TuyenTauService.KetQuaXuLy;

public class TuyenTauController {
	private final TuyenTauService tuyenTauService = new TuyenTauService();

	public List<TuyenTau> timKiemTuyenTau(String maTT, String gaDi, String gaDen) {
		return tuyenTauService.timKiemTuyenTau(chuanHoaRongThanhNull(maTT), chuanHoaRongThanhNull(gaDi),
				chuanHoaRongThanhNull(gaDen));
	}

	public KetQuaXuLy themTuyenTau(String maGaDi, String maGaDen, double khoangCach) {
		return tuyenTauService.themTuyenTau(chuanHoa(maGaDi), chuanHoa(maGaDen), khoangCach);
	}

	public KetQuaXuLy capNhatTuyenTau(String maTT, String maGaDi, String maGaDen, double khoangCach) {
		return tuyenTauService.capNhatTuyenTau(chuanHoa(maTT), chuanHoaRongThanhNull(maGaDi),
				chuanHoaRongThanhNull(maGaDen), khoangCach);
	}

	public KetQuaXuLy xoaTuyenTau(String maTT) {
		return tuyenTauService.xoaTuyenTau(chuanHoa(maTT));
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
