package view;

public class TuyenOption {
    public String maTT;
    public String gaDi;
    public String gaDen;
    public double khoangCach;

    public TuyenOption(String maTT, String gaDi, String gaDen) {
        this(maTT, gaDi, gaDen, 0);
    }

    public TuyenOption(String maTT, String gaDi, String gaDen, double khoangCach) {
        this.maTT = maTT; this.gaDi = gaDi; this.gaDen = gaDen; this.khoangCach = khoangCach;
    }

    @Override public String toString() {
        String d = gaDi == null ? "" : gaDi;
        String n = gaDen == null ? "" : gaDen;
        return d + " → " + n;
    }
}
