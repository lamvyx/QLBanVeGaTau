package entity;

import java.util.ArrayList;
import java.util.List;

public class PhieuDatVeInfo {
	private PhieuDatVe phieuDatVe;
	private List<ChiTietPhieuDat> chiTietList;

	public PhieuDatVeInfo() {
		this.phieuDatVe = new PhieuDatVe();
		this.chiTietList = new ArrayList<>();
	}

	public PhieuDatVeInfo(PhieuDatVe phieuDatVe, List<ChiTietPhieuDat> chiTietList) {
		this.phieuDatVe = phieuDatVe;
		this.chiTietList = chiTietList == null ? new ArrayList<>() : new ArrayList<>(chiTietList);
	}

	public PhieuDatVe getPhieuDatVe() {
		return phieuDatVe;
	}

	public void setPhieuDatVe(PhieuDatVe phieuDatVe) {
		this.phieuDatVe = phieuDatVe;
	}

	public List<ChiTietPhieuDat> getChiTietList() {
		return chiTietList;
	}

	public void setChiTietList(List<ChiTietPhieuDat> chiTietList) {
		this.chiTietList = chiTietList == null ? new ArrayList<>() : new ArrayList<>(chiTietList);
	}
}