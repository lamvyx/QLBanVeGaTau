package entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VeTau {
	private String maVeTau;
	private String maKH;
	private String maChuyenTau;
	private String maTau;
	private String maToa;
	private String viTriGhe;
	private int soLuongVe;
	private String maNhanVien;
	private String hoTen;
	private String giayTo;
	private String gaDi;
	private String gaDen;
	private String thoiGianDi;
	private String thoiGianDen;
	private String loaiVe;
	private String loaiCho;
	private BigDecimal giaVe;
	private String trangThai;
	private final List<ChiTietVeTau> dsChiTietVeTau = new ArrayList<>();

	public VeTau() {
	}

	public VeTau(String maVeTau, String maKH, String maChuyenTau, String maTau, String maToa, String viTriGhe,
			int soLuongVe, String maNhanVien, String hoTen, String giayTo, String gaDi, String gaDen,
			String thoiGianDi, String thoiGianDen, String loaiVe, String loaiCho, BigDecimal giaVe) {
		this.maVeTau = maVeTau;
		this.maKH = maKH;
		this.maChuyenTau = maChuyenTau;
		this.maTau = maTau;
		this.maToa = maToa;
		this.viTriGhe = viTriGhe;
		this.soLuongVe = soLuongVe;
		this.maNhanVien = maNhanVien;
		this.hoTen = hoTen;
		this.giayTo = giayTo;
		this.gaDi = gaDi;
		this.gaDen = gaDen;
		this.thoiGianDi = thoiGianDi;
		this.thoiGianDen = thoiGianDen;
		this.loaiVe = loaiVe;
		this.loaiCho = loaiCho;
		this.giaVe = giaVe;

		ChiTietVeTau ct = new ChiTietVeTau();
		ct.setMaVeTau(maVeTau);
		ct.setTenHanhKhach(hoTen);
		ct.setCccd(giayTo);
		ct.setViTriGhe(viTriGhe);
		ct.setLoaiVe(loaiVe);
		ct.setGiaVeTheoLoai(giaVe);
		dsChiTietVeTau.add(ct);
	}

	private ChiTietVeTau layChiTietDauTien() {
		if (dsChiTietVeTau.isEmpty()) {
			return null;
		}
		return dsChiTietVeTau.get(0);
	}

	private ChiTietVeTau damBaoChiTietDauTien() {
		ChiTietVeTau ct = layChiTietDauTien();
		if (ct == null) {
			ct = new ChiTietVeTau();
			ct.setMaVeTau(maVeTau);
			dsChiTietVeTau.add(ct);
		}
		return ct;
	}

	public String getMaVeTau() { return maVeTau; }
	public void setMaVeTau(String maVeTau) { this.maVeTau = maVeTau; }
	public String getMaKH() { return maKH; }
	public void setMaKH(String maKH) { this.maKH = maKH; }
	public String getMaChuyenTau() { return maChuyenTau; }
	public void setMaChuyenTau(String maChuyenTau) { this.maChuyenTau = maChuyenTau; }
	public String getMaTau() { return maTau; }
	public void setMaTau(String maTau) { this.maTau = maTau; }
	public String getMaToa() { return maToa; }
	public void setMaToa(String maToa) { this.maToa = maToa; }
	public String getViTriGhe() {
		ChiTietVeTau ct = layChiTietDauTien();
		return ct != null && ct.getViTriGhe() != null ? ct.getViTriGhe() : viTriGhe;
	}
	public void setViTriGhe(String viTriGhe) {
		this.viTriGhe = viTriGhe;
		damBaoChiTietDauTien().setViTriGhe(viTriGhe);
	}
	public int getSoLuongVe() {
		return dsChiTietVeTau.isEmpty() ? soLuongVe : dsChiTietVeTau.size();
	}
	public void setSoLuongVe(int soLuongVe) { this.soLuongVe = soLuongVe; }
	public String getMaNhanVien() { return maNhanVien; }
	public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }
	public String getHoTen() {
		ChiTietVeTau ct = layChiTietDauTien();
		return ct != null && ct.getTenHanhKhach() != null ? ct.getTenHanhKhach() : hoTen;
	}
	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
		damBaoChiTietDauTien().setTenHanhKhach(hoTen);
	}
	public String getGiayTo() {
		ChiTietVeTau ct = layChiTietDauTien();
		return ct != null && ct.getCccd() != null ? ct.getCccd() : giayTo;
	}
	public void setGiayTo(String giayTo) {
		this.giayTo = giayTo;
		damBaoChiTietDauTien().setCccd(giayTo);
	}
	public String getGaDi() { return gaDi; }
	public void setGaDi(String gaDi) { this.gaDi = gaDi; }
	public String getGaDen() { return gaDen; }
	public void setGaDen(String gaDen) { this.gaDen = gaDen; }
	public String getThoiGianDi() { return thoiGianDi; }
	public void setThoiGianDi(String thoiGianDi) { this.thoiGianDi = thoiGianDi; }
	public String getThoiGianDen() { return thoiGianDen; }
	public void setThoiGianDen(String thoiGianDen) { this.thoiGianDen = thoiGianDen; }
	public String getLoaiVe() {
		ChiTietVeTau ct = layChiTietDauTien();
		return ct != null && ct.getLoaiVe() != null ? ct.getLoaiVe() : loaiVe;
	}
	public void setLoaiVe(String loaiVe) {
		this.loaiVe = loaiVe;
		damBaoChiTietDauTien().setLoaiVe(loaiVe);
	}
	public String getLoaiCho() { return loaiCho; }
	public void setLoaiCho(String loaiCho) { this.loaiCho = loaiCho; }
	public BigDecimal getGiaVe() { return giaVe; }
	public void setGiaVe(BigDecimal giaVe) { this.giaVe = giaVe; }
	public String getTrangThai() { return trangThai; }
	public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
	public List<ChiTietVeTau> getDsChiTietVeTau() { return dsChiTietVeTau; }
	public void setDsChiTietVeTau(List<ChiTietVeTau> dsChiTietVeTauMoi) {
		dsChiTietVeTau.clear();
		if (dsChiTietVeTauMoi != null) {
			dsChiTietVeTau.addAll(dsChiTietVeTauMoi);
		}
	}
}
