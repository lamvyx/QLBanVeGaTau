package entity;

public class TuyenTau {
	private String maTT;
	private String maGaDi;
	private String maGaDen;
	private double khoangCach;

	public TuyenTau() {
	}

	public TuyenTau(String maTT, String maGaDi, String maGaDen, double khoangCach) {
		this.maTT = maTT;
		this.maGaDi = maGaDi;
		this.maGaDen = maGaDen;
		this.khoangCach = khoangCach;
	}

	public String getMaTT() { return maTT; }
	public void setMaTT(String maTT) { this.maTT = maTT; }
	public String getMaGaDi() { return maGaDi; }
	public void setMaGaDi(String maGaDi) { this.maGaDi = maGaDi; }
	public String getMaGaDen() { return maGaDen; }
	public void setMaGaDen(String maGaDen) { this.maGaDen = maGaDen; }
	public double getKhoangCach() { return khoangCach; }
	public void setKhoangCach(double khoangCach) { this.khoangCach = khoangCach; }
}
