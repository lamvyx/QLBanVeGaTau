package controller;

import javax.swing.JPanel;

import entity.TaiKhoan;
import view.PageFactory;

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