package controller;

import javax.swing.JPanel;

import entity.TaiKhoan;
import view.PageFactory;

//TrangChinhController sẽ gọi PageFactory để tạo ra các page tương ứng với menu và menu con được chọn
//PageFactory sẽ sử dụng PhanQuyenService để kiểm tra quyền của tài khoản và trả về page phù hợp

public class TrangChinhController {
	private final TaiKhoan taiKhoan;
	private final PageFactory pageFactory;

    public TrangChinhController(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
        pageFactory = new PageFactory();
    }

    public JPanel moPage(String menu, String menuCon) {
        return pageFactory.taoPageTheoMenu(menu, menuCon, taiKhoan);
    }
}