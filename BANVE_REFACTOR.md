# Báo cáo Refactor Trang Bán Vé (BanVePage)

Tài liệu này tóm tắt các hành động đã thực hiện để tối ưu hóa, rút gọn mã nguồn và chuẩn hóa kiến trúc cho chức năng Bán vé thuộc dự án Quản lý Bán Vé Ga Tàu.

## 1. Mục tiêu thực hiện
- Giảm số lượng dòng code trong file chính `BanVePage.java` (từ >1100 dòng xuống <350 dòng).
- Đảm bảo nguyên tắc **Separation of Concerns (SoC)**: Tách biệt giao diện, logic xử lý và dữ liệu.
- Chuẩn hóa tên gọi các file liên quan với tiền tố `BanVe`.

## 2. Các hành động cụ thể

### Hành động 1: Tách các trạng thái giao diện phụ (View States)
Thay vì chứa toàn bộ mã khởi tạo UI cho màn hình "Thành công" và "In vé" trong file chính, chúng đã được tách thành:
- **`BanVeSuccessPanel.java`**: Quản lý thẻ thông báo lập hóa đơn thành công.
- **`BanVePrintPanel.java`**: Quản lý thẻ xem trước và in hóa đơn/vé.

### Hành động 2: Tách thành phần chọn chỗ ngồi (Modular UI Components)
Lưới chọn ghế (Seat Grid) và các chú thích màu sắc chiếm rất nhiều dòng code UI và logic xử lý button:
- **`BanVeSeatPanel.java`**: Chứa toàn bộ logic vẽ lưới ghế, xử lý sự kiện click ghế, và cập nhật màu sắc dựa trên trạng thái (Trống, Đã đặt, Đang chọn).

### Hành động 3: Tách bảng tóm tắt đơn hàng và giá cả
Phần bên phải của giao diện chứa rất nhiều logic tính toán hiển thị và các nút hành động chính:
- **`BanVeSummaryPanel.java`**: Quản lý hiển thị thông tin khách hàng, chuyến tàu đã chọn và bảng tính toán giá vé (VAT, Khuyến mãi, Tổng tiền).

### Hành động 4: Tách biệt Logic nghiệp vụ và Tiện ích (Pure Logic)
Các phương thức xử lý chuỗi, định dạng tiền và quy tắc giá vé không nên nằm trong View:
- **`BanVeUtils.java`**: Chứa các hàm tĩnh (`static`) như `formatMoney`, `chuanHoaMaGhe`, và `xacDinhGiaVeTheoLoaiToa`.

### Hành động 5: Quy chuẩn hóa Model dữ liệu nội bộ
Các lớp Option dành cho JComboBox được tách ra để dễ tái sử dụng và giảm tải cho file View:
- **`BanVeOptionModels.java`**: Chứa `KhachHangOption`, `ChuyenTauOption`, và `ToaOption`.

## 3. Danh sách các file sau khi refactor

| File | Loại | Vai trò chính |
| :--- | :--- | :--- |
| `BanVePage.java` | **View (Main)** | Bộ khung chính, điều phối luồng dữ liệu và chuyển đổi giữa các Panel. |
| `BanVeSeatPanel.java` | **View (Child)** | Hiển thị và quản lý chọn ghế. |
| `BanVeSummaryPanel.java` | **View (Child)** | Tóm tắt đơn hàng và thực hiện thanh toán/làm mới. |
| `BanVeSuccessPanel.java` | **View (Card)** | Trạng thái hiển thị khi giao dịch thành công. |
| `BanVePrintPanel.java` | **View (Card)** | Giao diện in vé và hóa đơn. |
| `BanVeUtils.java` | **Logic/Util** | Xử lý logic thuần túy (định dạng, quy tắc nghiệp vụ). |
| `BanVeOptionModels.java` | **Model/DTO** | Các lớp dữ liệu hỗ trợ hiển thị UI. |

## 4. Kết quả đạt được
- **Độ phức tạp**: Giảm đáng kể, mỗi file hiện tại đảm nhận một trách nhiệm duy nhất (Single Responsibility).
- **Khả năng bảo trì**: Việc thay đổi giao diện chọn ghế hoặc quy tắc tính thuế/giá vé giờ đây chỉ cần sửa đổi trong các file tương ứng (`BanVeSeatPanel` hoặc `BanVeUtils`) mà không sợ làm hỏng toàn bộ trang.
- **Kích thước**: File `BanVePage.java` giảm từ **~1.186 dòng** xuống còn **~340 dòng**.
