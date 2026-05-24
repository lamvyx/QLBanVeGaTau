package view;

public class ChuyenTauOption {
    public String maCT, maTau, maTuyen, thoiGianKhoiHanh;
    public ChuyenTauOption(String maCT, String maTau, String maTuyen, String thoiGian) {
        this.maCT = maCT; this.maTau = maTau; this.maTuyen = maTuyen; this.thoiGianKhoiHanh = thoiGian;
    }
    @Override public String toString() {
        return maCT + " - " + maTau + " - " + maTuyen + " - " + thoiGianKhoiHanh;
    }
}
