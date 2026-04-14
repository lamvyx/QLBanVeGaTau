package dto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChuyenTau_DTO {
	private String maCT;
    private String tenTau;
    private String gaDi;
    private String gaDen;
    private LocalDate ngayKhoiHanh;
    private LocalTime gioKhoiHanh;

    public ChuyenTau_DTO(String maCT, String tenTau, String gaDi, String gaDen,
                        LocalDate ngayKhoiHanh, LocalTime gioKhoiHanh) {
        this.maCT = maCT;
        this.tenTau = tenTau;
        this.gaDi = gaDi;
        this.gaDen = gaDen;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.gioKhoiHanh = gioKhoiHanh;
    }

    public String getMaCT() { return maCT; }
    public String getTenTau() { return tenTau; }
    public String getGaDi() { return gaDi; }
    public String getGaDen() { return gaDen; }
    public LocalDate getNgayKhoiHanh() { return ngayKhoiHanh; }
    public LocalTime getGioKhoiHanh() { return gioKhoiHanh; }

    @Override
    public String toString() {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        return tenTau + " " + gaDi + " → " + gaDen +
               " (" + ngayKhoiHanh.format(dateFmt) +
               " - " + gioKhoiHanh.format(timeFmt) + ")";
    }
}