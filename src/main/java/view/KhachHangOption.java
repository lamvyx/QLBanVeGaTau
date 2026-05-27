package view;

public class KhachHangOption {
    public String maKH, tenKH, sdt;
    public KhachHangOption(String maKH, String tenKH, String sdt) {
        this.maKH = maKH;
        this.tenKH = tenKH == null ? "" : tenKH;
        this.sdt = sdt == null ? "" : sdt;
    }
    @Override public String toString() {
        return maKH + " - " + tenKH + (sdt == null || sdt.isBlank() ? "" : " - " + sdt);
    }
}
