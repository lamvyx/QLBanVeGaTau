package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class KhuyenMai {
	private String maKM;
	private String tenKM;
	private BigDecimal tyLeKM;
	private LocalDate ngayBD;
	private LocalDate ngayKT;

	public KhuyenMai() {
	}

	public KhuyenMai(String maKM, String tenKM, BigDecimal tyLeKM, LocalDate ngayBD, LocalDate ngayKT) {
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.tyLeKM = tyLeKM;
		this.ngayBD = ngayBD;
		this.ngayKT = ngayKT;
	}

	public String getMaKM() { return maKM; }
	public void setMaKM(String maKM) { this.maKM = maKM; }
	public String getTenKM() { return tenKM; }
	public void setTenKM(String tenKM) { this.tenKM = tenKM; }
	public BigDecimal getTyLeKM() { return tyLeKM; }
	public void setTyLeKM(BigDecimal tyLeKM) { this.tyLeKM = tyLeKM; }
	public LocalDate getNgayBD() { return ngayBD; }
	public void setNgayBD(LocalDate ngayBD) { this.ngayBD = ngayBD; }
	public LocalDate getNgayKT() { return ngayKT; }
	public void setNgayKT(LocalDate ngayKT) { this.ngayKT = ngayKT; }
}
