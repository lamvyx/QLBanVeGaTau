package controller;

import entity.ChiTietHoaDonItem;
import entity.HoaDon;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import service.HoaDonService;
import service.HoaDonService.KetQuaLapHoaDon;

public class HoaDonController {
	private final HoaDonService hoaDonService = new HoaDonService();

	public KetQuaLapHoaDon lapHoaDon(String maNV, String maKH, String maKM, List<ChiTietHoaDonItem> items) {
		return hoaDonService.lapHoaDon(chuanHoa(maNV), chuanHoa(maKH), chuanHoaRongThanhNull(maKM), items);
	}

	public KetQuaLapHoaDon lapHoaDonBanVe(String maNV, String maKH, String maKM, String maCT, String maToa,
			List<String> viTriGheList, BigDecimal giaVe) {
		return hoaDonService.lapHoaDonBanVe(chuanHoa(maNV), chuanHoa(maKH), chuanHoaRongThanhNull(maKM),
				chuanHoa(maCT), chuanHoa(maToa), viTriGheList, giaVe);
	}

	public Set<String> layGheDaDat(String maCT, String maToa) {
		return hoaDonService.layGheDaDat(chuanHoa(maCT), chuanHoa(maToa));
	}

	public List<HoaDon> timKiemHoaDon(String tuKhoa) {
		return hoaDonService.timKiemHoaDon(chuanHoaRongThanhNull(tuKhoa));
	}

	public BigDecimal layTongDoanhThu() {
		return hoaDonService.layTongDoanhThu();
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
