package entity;

import java.time.LocalDateTime;

public class DonDoiTra {
	private String maDon;
	private String maVeCu;
	private String maVeMoi;
	private String loaiDon; // DOI | TRA
	private String trangThai; // CHO_XAC_NHAN | DA_XAC_NHAN | TU_CHOI
	private String ghiChu;
	private LocalDateTime thoiGianTao;
	private LocalDateTime thoiGianXacNhan;

	public DonDoiTra() {
	}

	public DonDoiTra(String maDon, String maVeCu, String maVeMoi, String loaiDon, String trangThai, String ghiChu,
			LocalDateTime thoiGianTao, LocalDateTime thoiGianXacNhan) {
		this.maDon = maDon;
		this.maVeCu = maVeCu;
		this.maVeMoi = maVeMoi;
		this.loaiDon = loaiDon;
		this.trangThai = trangThai;
		this.ghiChu = ghiChu;
		this.thoiGianTao = thoiGianTao;
		this.thoiGianXacNhan = thoiGianXacNhan;
	}

	public String getMaDon() {
		return maDon;
	}

	public void setMaDon(String maDon) {
		this.maDon = maDon;
	}

	public String getMaVeCu() {
		return maVeCu;
	}

	public void setMaVeCu(String maVeCu) {
		this.maVeCu = maVeCu;
	}

	public String getMaVeMoi() {
		return maVeMoi;
	}

	public void setMaVeMoi(String maVeMoi) {
		this.maVeMoi = maVeMoi;
	}

	public String getLoaiDon() {
		return loaiDon;
	}

	public void setLoaiDon(String loaiDon) {
		this.loaiDon = loaiDon;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public LocalDateTime getThoiGianTao() {
		return thoiGianTao;
	}

	public void setThoiGianTao(LocalDateTime thoiGianTao) {
		this.thoiGianTao = thoiGianTao;
	}

	public LocalDateTime getThoiGianXacNhan() {
		return thoiGianXacNhan;
	}

	public void setThoiGianXacNhan(LocalDateTime thoiGianXacNhan) {
		this.thoiGianXacNhan = thoiGianXacNhan;
	}
}
