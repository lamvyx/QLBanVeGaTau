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

	public KetQuaLapHoaDon lapHoaDonBanVe(String maNV, String maKH, String maKM, String maCT, String maToa,
			List<String> viTriGheList, BigDecimal giaVe, java.util.Set<String> gheBoQuaKiemTra) {
		return hoaDonService.lapHoaDonBanVe(chuanHoa(maNV), chuanHoa(maKH), chuanHoaRongThanhNull(maKM),
				chuanHoa(maCT), chuanHoa(maToa), viTriGheList, giaVe, gheBoQuaKiemTra);
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

	public List<ChiTietHoaDonItem> layChiTietHoaDon(String maHD) {
		return hoaDonService.layChiTietHoaDon(chuanHoa(maHD));
	}

	public boolean updateTrangThaiThanhToan(String maHD, boolean daThanhToan) {
		return hoaDonService.updateTrangThaiThanhToan(chuanHoa(maHD), daThanhToan);
	}

	public boolean kiemTraThanhToan(String maHD) {
		return hoaDonService.kiemTraThanhToan(chuanHoa(maHD));
	}

	public BigDecimal tinhThueVAT(BigDecimal giaTruocThue) {
		return hoaDonService.tinhThueVAT(giaTruocThue);
	}

	public BigDecimal tinhChietKhau(BigDecimal giaTruocThue, String maKM) {
		return hoaDonService.tinhChietKhau(giaTruocThue, maKM);
	}

	public BigDecimal tinhTongThanhToan(BigDecimal giaTruocThue, BigDecimal thue, BigDecimal ck) {
		return hoaDonService.tinhTongThanhToan(giaTruocThue, thue, ck);
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
