package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
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
            if (conn == null) return false;

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
            if (conn == null) return false;
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