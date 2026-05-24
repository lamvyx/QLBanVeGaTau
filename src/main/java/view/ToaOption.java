package view;

import java.math.BigDecimal;

public class ToaOption {
    public String maToa, loaiToa;
    public int soGhe;
    public BigDecimal giaVe;
    public ToaOption(String maToa, String loaiToa, int soGhe, BigDecimal giaVe) {
        this.maToa = maToa; this.loaiToa = loaiToa; this.soGhe = soGhe; this.giaVe = giaVe;
    }
    @Override public String toString() {
        return maToa + " - " + loaiToa + " - " + soGhe + " ghế";
    }
}
