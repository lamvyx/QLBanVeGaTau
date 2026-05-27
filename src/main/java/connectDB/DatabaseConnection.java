package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Lớp quản lý kết nối đến SQL Server SQLEXPRESS
 * Đã cấu hình tối ưu cho JDBC Driver 12.x/13.x và Java đời cao.
 */
public class DatabaseConnection {
    // Thông số kết nối SQL Server Express
    private static final String SERVER = "localhost";
    private static final String INSTANCE = "SQLEXPRESS";
    private static final String PORT = "1433";
    private static final String DATABASE = "QLBANVETAU";

    // SQL Server Authentication
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "sapassword";

    private static Connection connection = null;

    /**
     * Lấy chuỗi kết nối (Connection String)
     * Cấu hình chuẩn để vượt qua các rào cản SSL/TLS của Driver mới
     */
    private static String getConnectionString() {
        return "jdbc:sqlserver://" + SERVER + "\\" + INSTANCE + ":" + PORT
                + ";databaseName=" + DATABASE
                + ";user=" + USERNAME
                + ";password=" + PASSWORD
                + ";encrypt=true" // Bắt buộc cho driver bản mới
                + ";trustServerCertificate=true" // Bỏ qua kiểm tra SSL trên localhost
                + ";loginTimeout=30;";
    }

    /**
     * Kết nối đến SQL Server
     */
    public static Connection connect() {
        try {
            // Load driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String connectionString = getConnectionString();

            System.out.println("--- ĐANG THỬ KẾT NỐI ---");
            System.out.println("Địa chỉ: " + SERVER + "\\" + INSTANCE + ":" + PORT);
            System.out.println("Database: " + DATABASE);

            connection = DriverManager.getConnection(connectionString);

            if (connection != null) {
                ensureHoaDonDateTimeSchema(connection);
                ensureRailOperationSchema(connection);
                System.out.println("✓ Kết nối SQL Server thành công!");
                return connection;
            }
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Lỗi: Không tìm thấy driver JDBC. Hãy kiểm tra mục Referenced Libraries!");
        } catch (SQLException e) {
            System.err.println("✗ Lỗi kết nối: " + e.getMessage());
            System.err.println("\nHƯỚNG DẪN KHẮC PHỤC:");
            System.err.println("1. Đảm bảo SQL Server (SQLEXPRESS) trong Configuration Manager đang RUNNING.");
            System.err.println("2. Đảm bảo TCP/IP đã ENABLE và Port là 1433.");
            System.err.println("3. Kiểm tra tài khoản 'sa' và mật khẩu '" + PASSWORD + "'");
        }
        return null;
    }

    private static void ensureHoaDonDateTimeSchema(Connection conn) {
        try {
            String dataTypeSql = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS "
                    + "WHERE TABLE_NAME = 'HoaDon' AND COLUMN_NAME = 'thoiGian'";
            try (PreparedStatement stmt = conn.prepareStatement(dataTypeSql);
                    ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return;
                }

                String dataType = rs.getString("DATA_TYPE");
                if (dataType == null || !"date".equalsIgnoreCase(dataType.trim())) {
                    return;
                }
            }

            System.out.println("! Đang cập nhật schema: HoaDon.thoiGian từ DATE sang DATETIME...");

            String dropDefaultSql = "DECLARE @constraintName NVARCHAR(128); "
                    + "SELECT @constraintName = dc.name "
                    + "FROM sys.default_constraints dc "
                    + "JOIN sys.columns c ON c.default_object_id = dc.object_id "
                    + "JOIN sys.tables t ON t.object_id = c.object_id "
                    + "WHERE t.name = 'HoaDon' AND c.name = 'thoiGian'; "
                    + "IF @constraintName IS NOT NULL EXEC('ALTER TABLE HoaDon DROP CONSTRAINT [' + @constraintName + ']');";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(dropDefaultSql);
                stmt.execute("ALTER TABLE HoaDon ALTER COLUMN thoiGian DATETIME NOT NULL");
                stmt.execute("ALTER TABLE HoaDon ADD CONSTRAINT DF_HoaDon_thoiGian DEFAULT GETDATE() FOR thoiGian");
            }

            System.out.println("✓ Đã cập nhật schema HoaDon.thoiGian sang DATETIME.");
        } catch (SQLException e) {
            System.err.println("! Không thể tự động cập nhật schema HoaDon.thoiGian: " + e.getMessage());
        }
    }

    private static void ensureRailOperationSchema(Connection conn) {
        try {
            ensureToaMaTauNullable(conn);
            ensureChuyenTauStatusString(conn);
            ensureChiTietChuyenTauToaTable(conn);
            migrateExistingCoachAssignments(conn);
        } catch (SQLException e) {
            System.err.println("! Không thể tự động cập nhật schema chuyến tàu/toa: " + e.getMessage());
        }
    }

    private static void ensureToaMaTauNullable(Connection conn) throws SQLException {
        String sql = "SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_NAME = 'Toa' AND COLUMN_NAME = 'maTau'";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (!rs.next()) {
                return;
            }
            String nullable = rs.getString("IS_NULLABLE");
            if ("YES".equalsIgnoreCase(nullable)) {
                return;
            }
        }
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("ALTER TABLE Toa ALTER COLUMN maTau VARCHAR(10) NULL");
        }
    }

    private static void ensureChuyenTauStatusString(Connection conn) throws SQLException {
        String sql = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_NAME = 'ChuyenTau' AND COLUMN_NAME = 'trangThai'";
        String dataType = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                dataType = rs.getString("DATA_TYPE");
            }
        }
        if (dataType == null || !"bit".equalsIgnoreCase(dataType.trim())) {
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("ALTER TABLE ChuyenTau ADD trangThaiMoi VARCHAR(20) NULL");
            stmt.execute("UPDATE ChuyenTau SET trangThaiMoi = CASE WHEN trangThai = 1 THEN 'DA_LEN_LICH' ELSE 'DA_HOAN_THANH' END");
            stmt.execute("DECLARE @constraintName NVARCHAR(128); "
                    + "SELECT @constraintName = dc.name "
                    + "FROM sys.default_constraints dc "
                    + "JOIN sys.columns c ON c.default_object_id = dc.object_id "
                    + "JOIN sys.tables t ON t.object_id = c.object_id "
                    + "WHERE t.name = 'ChuyenTau' AND c.name = 'trangThai'; "
                    + "IF @constraintName IS NOT NULL EXEC('ALTER TABLE ChuyenTau DROP CONSTRAINT [' + @constraintName + ']');");
            stmt.execute("ALTER TABLE ChuyenTau DROP COLUMN trangThai");
            stmt.execute("EXEC sp_rename 'ChuyenTau.trangThaiMoi', 'trangThai', 'COLUMN'");
            stmt.execute("ALTER TABLE ChuyenTau ALTER COLUMN trangThai VARCHAR(20) NOT NULL");
            stmt.execute("ALTER TABLE ChuyenTau ADD CONSTRAINT DF_ChuyenTau_trangThai DEFAULT 'DA_LEN_LICH' FOR trangThai");
            stmt.execute("ALTER TABLE ChuyenTau ADD CONSTRAINT CHK_ChuyenTau_TrangThai "
                    + "CHECK (trangThai IN ('DA_LEN_LICH', 'DANG_CHAY', 'DA_HOAN_THANH'))");
        } catch (SQLException e) {
            if (!e.getMessage().contains("already an object named")) {
                throw e;
            }
        }
    }

    private static void ensureChiTietChuyenTauToaTable(Connection conn) throws SQLException {
        String existsSql = "SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'ChiTietChuyenTau_Toa'";
        try (PreparedStatement stmt = conn.prepareStatement(existsSql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return;
            }
        }

        String createSql = "CREATE TABLE ChiTietChuyenTau_Toa ("
                + "maCT VARCHAR(10) NOT NULL, "
                + "maToa VARCHAR(10) NOT NULL, "
                + "thuTuToa INT NOT NULL DEFAULT 1, "
                + "CONSTRAINT PK_ChiTietChuyenTau_Toa PRIMARY KEY (maCT, maToa), "
                + "CONSTRAINT FK_CTTT_ChuyenTau FOREIGN KEY (maCT) REFERENCES ChuyenTau(maCT), "
                + "CONSTRAINT FK_CTTT_Toa FOREIGN KEY (maToa) REFERENCES Toa(maToa), "
                + "CONSTRAINT CHK_CTTT_ThuTuToa CHECK (thuTuToa > 0)"
                + ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createSql);
        }
    }

    private static void migrateExistingCoachAssignments(Connection conn) throws SQLException {
        String countSql = "SELECT COUNT(*) AS tong FROM ChiTietChuyenTau_Toa";
        try (PreparedStatement stmt = conn.prepareStatement(countSql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next() && rs.getInt("tong") > 0) {
                return;
            }
        }

        String insertSql = "INSERT INTO ChiTietChuyenTau_Toa(maCT, maToa, thuTuToa) "
                + "SELECT ct.maCT, toa.maToa, "
                + "ROW_NUMBER() OVER (PARTITION BY ct.maCT ORDER BY TRY_CAST(toa.viTriToa AS INT), toa.maToa) "
                + "FROM ChuyenTau ct "
                + "JOIN Toa toa ON toa.maTau = ct.maTau "
                + "WHERE NOT EXISTS (SELECT 1 FROM ChiTietChuyenTau_Toa x WHERE x.maCT = ct.maCT AND x.maToa = toa.maToa)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertSql);
        }
    }

    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Đã đóng kết nối an toàn.");
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        if (!isConnected()) {
            connect();
        }
        return connection;
    }

    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn == null)
                return false;

            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT @@VERSION as version")) {
                if (rs.next()) {
                    System.out.println("✓ Truy vấn thử nghiệm thành công!");
                    System.out.println("✓ Phiên bản SQL: " + rs.getString("version"));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi khi test truy vấn: " + e.getMessage());
        }
        return false;
    }

    /**
     * Kiểm tra xem database có tồn tại hay không
     */
    public static boolean databaseExists() {
        try {
            Connection conn = getConnection();
            if (conn == null)
                return false;
            return !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Hiển thị thông tin kết nối
     */
    public static void displayConnectionInfo() {
        System.out.println("\n========== THÔNG TIN KẾT NỐI ==========");
        System.out.println("Server: " + SERVER + "\\" + INSTANCE + ":" + PORT);
        System.out.println("Database: " + DATABASE);
        System.out.println("Username: " + USERNAME);
        System.out.println("Connection String: " + getConnectionString());
        System.out.println("========================================\n");
    }

    public static void main(String[] args) {
        System.out.println("========== SQL SERVER CONNECTION TEST ==========");

        if (testConnection()) {
            System.out.println("\n>>> KẾT QUẢ: TẤT CẢ ĐỀU ỔN! <<<");
            System.out.println("Bạn có thể tiếp tục làm đồ án.");
        } else {
            System.out.println("\n>>> KẾT QUẢ: KẾT NỐI THẤT BẠI! <<<");
            System.out.println("Hãy kiểm tra lại Configuration Manager và Restart SQL Service.");
        }

        System.out.println("================================================");
        disconnect();
    }
}
