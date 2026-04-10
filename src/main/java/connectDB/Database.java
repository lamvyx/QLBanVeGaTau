package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
	private static final String DEFAULT_URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true";
	private static final String DEFAULT_USER = "sa";
	private static final String DEFAULT_PASSWORD = "123456";

	private Database() {
	}

	public static Connection getConnection() throws SQLException {
		String url = readSetting("DB_URL", "db.url", DEFAULT_URL);
		String user = readSetting("DB_USER", "db.user", DEFAULT_USER);
		String password = readSetting("DB_PASSWORD", "db.password", DEFAULT_PASSWORD);
		return DriverManager.getConnection(url, user, password);
	}

	private static String readSetting(String envKey, String propKey, String fallback) {
		String fromEnv = System.getenv(envKey);
		if (fromEnv != null && !fromEnv.isBlank()) {
			return fromEnv.trim();
		}

		String fromProp = System.getProperty(propKey);
		if (fromProp != null && !fromProp.isBlank()) {
			return fromProp.trim();
		}

		return fallback;
	}
}