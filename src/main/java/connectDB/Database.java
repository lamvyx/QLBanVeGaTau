package connectDB;

import entity.TaiKhoan;
import java.util.HashMap;
import java.util.Map;

public class Database {
	private static final Database INSTANCE = new Database();
	private final Map<String, TaiKhoan> taiKhoanTable = new HashMap<>();

	private Database() {
		taiKhoanTable.put("quanly",
				new TaiKhoan("quanly", "123456", "quanly@gatsaigon.vn", "Nguyễn Văn Admin", "QUAN_LY"));
		taiKhoanTable.put("nhanvien",
				new TaiKhoan("nhanvien", "123456", "nhanvien@gatsaigon.vn", "Trần Thị Nhân Viên", "NHAN_VIEN_BAN_VE"));
		taiKhoanTable.put("demo", new TaiKhoan("demo", "demo1234!", "demo@gmail.com", "Demo User", "QUAN_LY"));
	}

	public static Database getInstance() {
		return INSTANCE;
	}

	public TaiKhoan findByUsername(String username) {
		return taiKhoanTable.get(username);
	}

	public boolean updatePassword(String username, String newPassword) {
		TaiKhoan taiKhoan = taiKhoanTable.get(username);
		if (taiKhoan == null) {
			return false;
		}
		taiKhoan.setMatKhau(newPassword);
		return true;
	}
}