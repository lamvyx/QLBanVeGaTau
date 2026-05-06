package entity;

import java.time.LocalDateTime;

public class Phien {
	private String maPhien;
	private LocalDateTime thoiGianBD;
	private LocalDateTime thoiGianKT;

	public Phien() {
	}

	public Phien(String maPhien, LocalDateTime thoiGianBD, LocalDateTime thoiGianKT) {
		this.maPhien = maPhien;
		this.thoiGianBD = thoiGianBD;
		this.thoiGianKT = thoiGianKT;
	}

	public String getMaPhien() { return maPhien; }
	public void setMaPhien(String maPhien) { this.maPhien = maPhien; }
	public LocalDateTime getThoiGianBD() { return thoiGianBD; }
	public void setThoiGianBD(LocalDateTime thoiGianBD) { this.thoiGianBD = thoiGianBD; }
	public LocalDateTime getThoiGianKT() { return thoiGianKT; }
	public void setThoiGianKT(LocalDateTime thoiGianKT) { this.thoiGianKT = thoiGianKT; }
}
