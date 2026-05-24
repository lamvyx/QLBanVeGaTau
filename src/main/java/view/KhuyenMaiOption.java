package view;

import java.math.BigDecimal;

public class KhuyenMaiOption {
    public final String maKM;
    public final String tenKM;
    public final BigDecimal tyLe;

    public KhuyenMaiOption(String maKM, String tenKM, BigDecimal tyLe) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.tyLe = tyLe;
    }

    @Override
    public String toString() {
        if (maKM == null) return "Không áp dụng";
        return tenKM + " (-" + tyLe + "%)";
    }
}
