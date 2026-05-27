package entity;

import java.math.BigDecimal;

public class HoaDonTongKetDTO {
    private BigDecimal tongTruocThue = BigDecimal.ZERO;
    private BigDecimal tongSauThue = BigDecimal.ZERO;
    private BigDecimal tyLeKhuyenMai = BigDecimal.ZERO;
    private BigDecimal tongThanhToan = BigDecimal.ZERO;

    public BigDecimal getTongTruocThue() { return tongTruocThue; }
    public void setTongTruocThue(BigDecimal tongTruocThue) { this.tongTruocThue = tongTruocThue; }

    public BigDecimal getTongSauThue() { return tongSauThue; }
    public void setTongSauThue(BigDecimal tongSauThue) { this.tongSauThue = tongSauThue; }

    public BigDecimal getTyLeKhuyenMai() { return tyLeKhuyenMai; }
    public void setTyLeKhuyenMai(BigDecimal tyLeKhuyenMai) { this.tyLeKhuyenMai = tyLeKhuyenMai; }

    public BigDecimal getTongThanhToan() { return tongThanhToan; }
    public void setTongThanhToan(BigDecimal tongThanhToan) { this.tongThanhToan = tongThanhToan; }
}
