package view;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BanVeUtils {
    private static final DecimalFormat MONEY_FORMAT;
    static {
        java.text.DecimalFormatSymbols symbols = new java.text.DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        MONEY_FORMAT = new DecimalFormat("#,### đ", symbols);
    }

    public static String formatMoney(BigDecimal value) {
        if (value == null) return "0 đ";
        return MONEY_FORMAT.format(value);
    }

    public static String chuanHoaMaGhe(String raw) {
        if (raw == null) return "";
        String value = raw.trim().toUpperCase();
        if (value.matches("[A-Z]\\d+")) {
            char cot = value.charAt(0);
            int row = Integer.parseInt(value.substring(1));
            return cot + String.format("%02d", row);
        }
        if (value.matches("\\d+[A-Z]")) {
            char cot = value.charAt(value.length() - 1);
            int row = Integer.parseInt(value.substring(0, value.length() - 1));
            return cot + String.format("%02d", row);
        }
        return value;
    }

    public static BigDecimal xacDinhGiaVeTheoLoaiToa(String loaiToa) {
        if (loaiToa == null) return new BigDecimal("1105000");
        String value = loaiToa.toLowerCase();
        if (value.contains("ngồi mềm") || value.contains("ghế mềm")) {
            return new BigDecimal("1105000");
        }
        if (value.contains("nằm")) {
            return new BigDecimal("1530000");
        }
        return new BigDecimal("850000");
    }
}
