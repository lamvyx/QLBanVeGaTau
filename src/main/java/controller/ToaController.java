package controller;

import entity.Toa;
import java.util.List;
import service.ToaService;
import service.ToaService.KetQuaXuLy;

public class ToaController {
	private final ToaService toaService = new ToaService();

	public List<Toa> timKiemToa(String maToa) {
		return toaService.timKiemToa(chuanHoaRongThanhNull(maToa));
	}

	public List<String> layDanhSachMaTau() {
		return toaService.layDanhSachMaTau();
	}

	public KetQuaXuLy capNhatToa(String maToa, String maTau, String loaiToa, int soGhe, String viTriToa, boolean trangThai) {
		return toaService.capNhatToa(chuanHoa(maToa), chuanHoa(maTau), chuanHoa(loaiToa), soGhe, chuanHoa(viTriToa), trangThai);
	}

	public KetQuaXuLy xoaToa(String maToa) {
		return toaService.xoaToa(chuanHoa(maToa));
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
