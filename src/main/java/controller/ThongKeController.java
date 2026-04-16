package controller;

import java.math.BigDecimal;
import service.ThongKeService;
import service.ThongKeService.ThongKeSoLuongVe;

public class ThongKeController {
	private final ThongKeService thongKeService = new ThongKeService();

	public ThongKeSoLuongVe thongKeSoLuongVe() {
		return thongKeService.thongKeSoLuongVe();
	}

	public BigDecimal thongKeDoanhThu() {
		return thongKeService.thongKeDoanhThu();
	}
}
