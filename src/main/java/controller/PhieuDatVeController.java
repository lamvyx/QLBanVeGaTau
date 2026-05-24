package controller;

import entity.PhieuDatVeInfo;
import java.math.BigDecimal;
import java.util.List;
import service.PhieuDatVeService;
import service.PhieuDatVeService.KetQuaTaoPhieuDat;

public class PhieuDatVeController {
	private final PhieuDatVeService phieuDatVeService = new PhieuDatVeService();

	public KetQuaTaoPhieuDat taoPhieuDat(String maNV, String maKH, String maCT, String maToa,
			List<String> viTriGheList, BigDecimal giaVe, String ghiChu) {
		return phieuDatVeService.taoPhieuDat(chuanHoa(maNV), chuanHoa(maKH), chuanHoa(maCT), chuanHoa(maToa),
				viTriGheList, giaVe, ghiChu == null ? null : ghiChu.trim());
	}

	public PhieuDatVeInfo layPhieuDatTheoMa(String maPhieu) {
		return phieuDatVeService.layPhieuDatTheoMa(chuanHoa(maPhieu));
	}

	public List<PhieuDatVeInfo> layTatCaPhieuDat() {
		return phieuDatVeService.layTatCaPhieuDat();
	}

	public boolean capNhatTrangThai(String maPhieu, boolean trangThai) {
		return phieuDatVeService.capNhatTrangThai(chuanHoa(maPhieu), trangThai);
	}

	private String chuanHoa(String s) {
		return s == null ? null : s.trim();
	}
}