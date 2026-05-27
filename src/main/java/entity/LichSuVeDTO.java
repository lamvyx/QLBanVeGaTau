package entity;

import java.time.LocalDateTime;

public class LichSuVeDTO {
    private String maVeTau;
    private String gaDi;
    private String gaDen;
    private LocalDateTime ngayKhoiHanh;
    private LocalDateTime ngayMua;
    private String maToa;
    private double giaVe;
    private String trangThai;

    public LichSuVeDTO() {}

    public String getMaVeTau() { return maVeTau; }
    public void setMaVeTau(String maVeTau) { this.maVeTau = maVeTau; }

    public String getGaDi() { return gaDi; }
    public void setGaDi(String gaDi) { this.gaDi = gaDi; }

    public String getGaDen() { return gaDen; }
    public void setGaDen(String gaDen) { this.gaDen = gaDen; }

    public LocalDateTime getNgayKhoiHanh() { return ngayKhoiHanh; }
    public void setNgayKhoiHanh(LocalDateTime ngayKhoiHanh) { this.ngayKhoiHanh = ngayKhoiHanh; }

    public LocalDateTime getNgayMua() { return ngayMua; }
    public void setNgayMua(LocalDateTime ngayMua) { this.ngayMua = ngayMua; }

    public String getMaToa() { return maToa; }
    public void setMaToa(String maToa) { this.maToa = maToa; }

    public double getGiaVe() { return giaVe; }
    public void setGiaVe(double giaVe) { this.giaVe = giaVe; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getTuyenDuong() {
        return gaDi + " - " + gaDen;
    }
}
