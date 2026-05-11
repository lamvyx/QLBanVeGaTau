Các hành động đã thực hiện để rút gọn code:
Tách các thành phần giao diện phức tạp (Component Extraction):

Tạo BanVeSuccessPanel.java: Chuyển toàn bộ logic vẽ giao diện "Lập hóa đơn thành công" sang một lớp riêng.
Tạo BanVePrintPanel.java: Chuyển toàn bộ giao diện "In vé / Hóa đơn" sang một lớp riêng.
Việc này giúp BanVePage không còn phải chứa hàng trăm dòng code boilerplate (khởi tạo Label, Button, layout) cho các màn hình phụ này.
Tách biệt Model dữ liệu:

Tạo BanVeOptionModels.java: Chuyển các lớp hỗ trợ như KhachHangOption, ChuyenTauOption, và ToaOption (dùng cho JComboBox) ra tệp riêng.
Tối ưu hóa Logic trong BanVePage:

Sử dụng các Component mới thông qua các Interface/Lambda đơn giản để xử lý sự kiện (như quay lại hoặc bán vé mới), giúp code xử lý luồng (Flow logic) trở nên rõ ràng hơn.
Dọn dẹp mã nguồn:

Xóa bỏ các trường (field) không sử dụng, các import dư thừa và các khối mã bị trùng lặp sau khi refactor.

Các thành phần mới đã được tách:
Giao diện & Thành phần UI (Modular UI Components):
--------------------------------------------------------------------------------

BanVeSeatPanel.java: Quản lý toàn bộ lưới chọn ghế, chú thích (legend) và trạng thái hiển thị của các nút ghế.

BanVeSummaryPanel.java: Quản lý phần Tóm tắt đơn hàng và Chi tiết giá cả (bên phải giao diện), bao gồm cả các nút bấm "Lập hóa đơn" và "Làm mới".

BanVeSuccessPanel.java: Giao diện thông báo thành công.

BanVePrintPanel.java: Giao diện xem trước hóa đơn và in vé.
Logic Nghiệp vụ & Tiện ích (Pure Logic & Utils):


BanVeUtils.java: Chứa các logic không liên quan đến giao diện như: định dạng tiền tệ, chuẩn hóa mã ghế (String manipulation), và quy tắc xác định giá vé theo loại toa.

BanVeOptionModels.java: Các mô hình dữ liệu dùng cho các JComboBox (KhachHang, ChuyenTau, Toa).
Kết quả trên 
--------------------------------------------------------------------------------


## Chức năng Thống kê (Statistics Module)

Chức năng thống kê được thiết kế theo kiến trúc MVC (Model-View-Controller) để đảm bảo tách biệt giữa dữ liệu, logic nghiệp vụ và giao diện người dùng.

### Cấu trúc luồng dữ liệu:

```
Database (SQL Server)
    ↓
DAO Layer (ThongKe_DAO.java)
    ↓
Service Layer (ThongKeService.java)
    ↓
Controller Layer (ThongKeController.java)
    ↓
View Layer (ThongKePage.java, DoanhThuThongKePage.java, v.v.)
```

### 1. DAO Layer - Truy xuất dữ liệu từ Database

**File:** `dao/ThongKe_DAO.java`

Đây là lớp chịu trách nhiệm kết nối và truy vấn dữ liệu trực tiếp từ SQL Server database. Các methods chính:

- `getRevenueByPeriod(String period)` - Lấy doanh thu theo ngày/tháng/năm
  - Truy vấn view `v_TongTienHoaDon` và bảng `HoaDon`
  - Lọc theo trạng thái thanh toán và thời gian

- `getTicketStatusCounts()` - Lấy số lượng vé theo trạng thái
  - Truy vấn bảng `VeTau`, group by `trangThai`

- `getDailyRevenueChart(int days)` - Lấy doanh thu theo ngày cho biểu đồ
  - Truy vấn view `v_TongTienHoaDon`, group by ngày

- `getTopCustomers(int limit)` - Lấy top khách hàng theo doanh thu
  - Join các bảng: `v_TongTienHoaDon`, `HoaDon`, `KhachHang`

- `getTripOccupancy()` - Lấy tỷ lệ lấp đầy của các chuyến tàu
  - Join bảng `ChuyenTau`, `VeTau`, `Toa`

- `getTicketsByCarriageType()` - Lấy số vé theo loại toa
  - Join bảng `Toa` và `VeTau`

- `getNewCustomersByMonth(int year)` - Lấy số khách hàng mới theo tháng
  - Truy vấn bảng `KhachHang`

- `getTotalCustomers()` - Lấy tổng số khách hàng
- `getTripCountByRoute()` - Lấy số chuyến theo tuyến đường
- `getRevenueByMonth(int year)` - Lấy doanh thu theo tháng
- `getRevenueByQuarter(int year)` - Lấy doanh thu theo quý
- `getTotalTrips()` - Lấy tổng số chuyến tàu

**Đặc điểm:** Tất cả methods đều trả về dữ liệu thực từ database, không có dữ liệu giả (mock data).

### 2. Service Layer - Logic nghiệp vụ

**File:** `service/ThongKeService.java`

Lớp này đóng vai trò trung gian, chứa logic nghiệp vụ và gọi các methods từ DAO:

- Khởi tạo các DAO objects: `VeTau_DAO`, `HoaDon_DAO`, `ThongKe_DAO`
- Cung cấp các methods business logic như `thongKeSoLuongVe()`, `thongKeDoanhThu()`
- Định nghĩa class inner `ThongKeSoLuongVe` để đóng gói dữ liệu thống kê số lượng vé

**Đặc điểm:** Không có logic phức tạp, chủ yếu pass-through dữ liệu từ DAO lên Controller.

### 3. Controller Layer - Điều phối

**File:** `controller/ThongKeController.java`

Lớp này đóng vai trò điều phối giữa Service và View:

- Khởi tạo `ThongKeService`
- Expose các methods tương ứng từ Service cho View gọi
- Không chứa logic nghiệp vụ, chỉ điều phối luồng dữ liệu

### 4. View Layer - Hiển thị và Vẽ biểu đồ

**Các file View:**
- `view/ThongKePage.java` - Trang thống kê tổng hợp (Dashboard)
- `view/DoanhThuThongKePage.java` - Trang thống kê doanh thu chi tiết
- `view/VeThongKePage.java` - Trang thống kê vé
- `view/KhachHangThongKePage.java` - Trang thống kê khách hàng
- `view/ChuyenTauThongKePage.java` - Trang thống kê chuyến tàu

**Cách hoạt động:**

1. **Khởi tạo Controller:** Mỗi View đều có một instance của `ThongKeController`

2. **Gọi dữ liệu:** Khi cần hiển thị, View gọi methods từ Controller:
   ```java
   BigDecimal doanhThu = thongKeController.getRevenueByPeriod("month");
   ThongKeSoLuongVe stats = thongKeController.thongKeSoLuongVe();
   Map<String, Integer> ticketsByType = thongKeController.getTicketsByCarriageType();
   ```

3. **Vẽ biểu đồ:** Dữ liệu được truyền vào các custom panel để vẽ biểu đồ:
   - `BarChartPanel` - Biểu đồ cột
   - `LineChartPanel` - Biểu đồ đường
   - `HorizontalBarChartPanel` - Biểu đồ cột ngang
   - `TicketStatusPanel` - Biểu đồ tròn (pie chart) trạng thái vé
   - `RankingPanel` - Biểu đồ xếp hạng

4. **Fallback khi không có dữ liệu:** Nếu database trống, các biểu đồ sẽ hiển thị giá trị mặc định (0) để đảm bảo UI vẫn hoạt động.

### Ví dụ luồng dữ liệu cụ thể:

**Để hiển thị doanh thu theo tháng:**

1. `DoanhThuThongKePage.createMonthPanel()` gọi `thongKeController.getRevenueByMonth(2026)`
2. `ThongKeController.getRevenueByMonth()` gọi `thongKeService.getRevenueByMonth(2026)`
3. `ThongKeService.getRevenueByMonth()` gọi `thongKeDAO.getRevenueByMonth(2026)`
4. `ThongKe_DAO.getRevenueByMonth()` thực thi SQL query:
   ```sql
   SELECT MONTH(h.thoiGian) as month, SUM(v.tongThanhToan)
   FROM HoaDon h JOIN v_TongTienHoaDon v ON h.maHD = v.maHD
   WHERE h.trangThaiThanhToan = 1 AND YEAR(h.thoiGian) = ?
   GROUP BY MONTH(h.thoiGian)
   ```
5. Kết quả trả về là `Map<String, Long>` với key là "T1", "T2", ..., "T12"
6. View chuyển đổi Map thành mảng và truyền vào `BarChartPanel` để vẽ biểu đồ

### Lợi ích của kiến trúc này:

- **Tách biệt trách nhiệm:** Mỗi layer có nhiệm vụ riêng, dễ bảo trì
- **Dữ liệu thực:** Tất cả thống kê đều lấy từ database, không có dữ liệu giả
- **Dễ mở rộng:** Muốn thêm thống kê mới chỉ cần thêm method ở DAO, Service, Controller
- **Dễ test:** Có thể test từng layer độc lập

### Các thống kê đã được cập nhật sử dụng dữ liệu thực:

 Doanh thu (ngày/tháng/năm/quý)  
 Số lượng vé theo trạng thái  
 Số vé theo loại toa  
 Top khách hàng theo doanh thu  
 Tỷ lệ lấp đầy chuyến tàu  
 Số khách hàng mới theo tháng  
 Số chuyến tàu theo tuyến đường  

### Các thống kê cần database thêm:

 Tỷ lệ đúng giờ/trễ chuyến (cần bảng tracking thời gian thực tế)  
 Tỉ lệ hài lòng khách hàng (cần bảng khảo sát)  
 Điểm tích lũy khách hàng (cần bảng loyalty points)
