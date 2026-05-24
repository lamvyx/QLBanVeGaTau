package view;

public class TauOption {
    public String maTau;
    public String tenTau;
    public int soLuongToa;

    public TauOption(String maTau, String tenTau, int soLuongToa) {
        this.maTau = maTau;
        this.tenTau = tenTau;
        this.soLuongToa = soLuongToa;
    }

    @Override
    public String toString() {
        String ma = maTau == null ? "" : maTau;
        String ten = tenTau == null ? "" : tenTau;
        return ma + " - " + ten;
    }
}
