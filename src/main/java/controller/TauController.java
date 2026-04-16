package controller;

import entity.Tau;
import java.util.List;
import service.TauService;
import service.TauService.KetQuaXuLy;

public class TauController {
	private final TauService tauService = new TauService();

	public List<Tau> timKiemTau(String maTau, String tenTau) {
		return tauService.timKiemTau(chuanHoaRongThanhNull(maTau), chuanHoaRongThanhNull(tenTau));
	}

	public KetQuaXuLy themTau(String tenTau, int soLuongToa) {
		return tauService.themTau(chuanHoa(tenTau), soLuongToa);
	}

	public KetQuaXuLy capNhatTau(String maTau, String tenTau, int soLuongToa) {
		return tauService.capNhatTau(chuanHoa(maTau), chuanHoaRongThanhNull(tenTau), soLuongToa);
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
