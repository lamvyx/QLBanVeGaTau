package view;

import java.math.BigDecimal;

public class DichVuOption {
    public final String maDV;
    public final String tenDV;
    public final BigDecimal giaDV;

    public DichVuOption(String maDV, String tenDV, BigDecimal giaDV) {
        this.maDV = maDV;
        this.tenDV = tenDV == null ? "" : tenDV;
        this.giaDV = giaDV == null ? BigDecimal.ZERO : giaDV;
    }

    @Override
    public String toString() {
        return tenDV + " - " + BanVeUtils.formatMoney(giaDV);
    }
}
