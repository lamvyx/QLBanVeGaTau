package entity;

import java.time.LocalDateTime;

public class ChuyenTau {
	public static final String TRANG_THAI_DA_LEN_LICH = "DA_LEN_LICH";
	public static final String TRANG_THAI_DANG_CHAY = "DANG_CHAY";
	public static final String TRANG_THAI_DA_HOAN_THANH = "DA_HOAN_THANH";
	public static final String HIEN_THI_DA_LEN_LICH = "Đã lên lịch";
	public static final String HIEN_THI_DANG_CHAY = "Đang chạy";
	public static final String HIEN_THI_DA_HOAN_THANH = "Đã hoàn thành chuyến";

	private String maCT;
	private LocalDateTime ngayKhoiHanh;
	private LocalDateTime gioKhoiHanh;
	private String trangThai;
	private String maTau;
	private String maTuyenTau;

	public ChuyenTau() {
	}

	public ChuyenTau(String maCT, LocalDateTime ngayKhoiHanh, LocalDateTime gioKhoiHanh, boolean trangThai,
			String maTau, String maTuyenTau) {
		this(maCT, ngayKhoiHanh, gioKhoiHanh,
				trangThai ? TRANG_THAI_DA_LEN_LICH : TRANG_THAI_DA_HOAN_THANH, maTau, maTuyenTau);
	}

	public ChuyenTau(String maCT, LocalDateTime ngayKhoiHanh, LocalDateTime gioKhoiHanh, String trangThai,
			String maTau, String maTuyenTau) {
		this.maCT = maCT;
		this.ngayKhoiHanh = ngayKhoiHanh;
		this.gioKhoiHanh = gioKhoiHanh;
		this.trangThai = trangThai;
		this.maTau = maTau;
		this.maTuyenTau = maTuyenTau;
	}

	public String getMaCT() { return maCT; }
	public void setMaCT(String maCT) { this.maCT = maCT; }
	public LocalDateTime getNgayKhoiHanh() { return ngayKhoiHanh; }
	public void setNgayKhoiHanh(LocalDateTime ngayKhoiHanh) { this.ngayKhoiHanh = ngayKhoiHanh; }
	public LocalDateTime getGioKhoiHanh() { return gioKhoiHanh; }
	public void setGioKhoiHanh(LocalDateTime gioKhoiHanh) { this.gioKhoiHanh = gioKhoiHanh; }
	public boolean isTrangThai() { return !TRANG_THAI_DA_HOAN_THANH.equals(trangThai); }
	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai ? TRANG_THAI_DA_LEN_LICH : TRANG_THAI_DA_HOAN_THANH;
	}
	public String getTrangThai() { return trangThai; }
	public void setTrangThai(String trangThai) {
		this.trangThai = (trangThai == null || trangThai.isBlank()) ? TRANG_THAI_DA_LEN_LICH : trangThai;
	}
	public String getMaTau() { return maTau; }
	public void setMaTau(String maTau) { this.maTau = maTau; }
	public String getMaTuyenTau() { return maTuyenTau; }
	public void setMaTuyenTau(String maTuyenTau) { this.maTuyenTau = maTuyenTau; }

	public boolean laDaLenLich() {
		return TRANG_THAI_DA_LEN_LICH.equals(trangThai);
	}

	public boolean laDangChay() {
		return TRANG_THAI_DANG_CHAY.equals(trangThai);
	}

	public boolean laDaHoanThanh() {
		return TRANG_THAI_DA_HOAN_THANH.equals(trangThai);
	}

	public String getTrangThaiHienThi() {
		return sangTrangThaiHienThi(trangThai);
	}

	public static String sangTrangThaiHienThi(String trangThai) {
		if (TRANG_THAI_DANG_CHAY.equals(trangThai)) {
			return HIEN_THI_DANG_CHAY;
		}
		if (TRANG_THAI_DA_HOAN_THANH.equals(trangThai)) {
			return HIEN_THI_DA_HOAN_THANH;
		}
		return HIEN_THI_DA_LEN_LICH;
	}

	public static String tuTrangThaiHienThi(String hienThi) {
		if (HIEN_THI_DANG_CHAY.equals(hienThi)) {
			return TRANG_THAI_DANG_CHAY;
		}
		if (HIEN_THI_DA_HOAN_THANH.equals(hienThi)) {
			return TRANG_THAI_DA_HOAN_THANH;
		}
		return TRANG_THAI_DA_LEN_LICH;
	}
}
