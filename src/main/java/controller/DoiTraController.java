package controller;

import entity.DonDoiTra;
import entity.VeTau;
import java.util.List;
import service.DoiTraService;
import service.DoiTraService.KetQuaXuLy;

public class DoiTraController {
	private final DoiTraService doiTraService = new DoiTraService();

	public KetQuaXuLy taoDonDoiTra(String maVeCu, String maVeMoi, String loaiDon, String ghiChu) {
		return doiTraService.taoDonDoiTra(chuanHoa(maVeCu), chuanHoaRongThanhNull(maVeMoi), chuanHoa(loaiDon),
				chuanHoaRongThanhNull(ghiChu));
	}

	public KetQuaXuLy xacNhanDonDoiTra(String maDon) {
		return doiTraService.xacNhanDonDoiTra(chuanHoa(maDon));
	}

	public DonDoiTra timDonTheoMa(String maDon) {
		return doiTraService.timDonTheoMa(chuanHoa(maDon));
	}

	public VeTau timVeTheoMa(String maVe) {
		return doiTraService.timVeTheoMa(chuanHoa(maVe));
	}

	public List<VeTau> layVeTheoChuyenTau(String maCT) {
		return doiTraService.layVeTheoChuyenTau(chuanHoa(maCT));
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
