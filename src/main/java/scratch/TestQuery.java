package scratch;

import connectDB.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestQuery {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return;
            String sql = "SELECT ISNULL(SUM(v.tongThanhToan), 0) FROM v_TongTienHoaDon v JOIN HoaDon h ON v.maHD = h.maHD WHERE h.trangThaiThanhToan = 1 AND CAST(h.thoiGian AS DATE) = CAST(GETDATE() AS DATE)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Result: " + rs.getDouble(1));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
