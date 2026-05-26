# Luồng xử lý các thao tác chính trong BanVePage

Tài liệu này mô tả luồng luân chuyển dữ liệu và các thành phần tham gia khi người dùng thực hiện các thao tác chính trên trang Bán vé (`BanVePage`). Tất cả đều tuân thủ mô hình phân lớp **View -> Service Facade -> Controller -> DAO -> Database**.

---

## 1. Tìm kiếm chuyến tàu (`onSearchTrips`)
Khi người dùng nhập Ga đi, Ga đến, Ngày đi và nhấn nút **Tìm kiếm**:

1. **`BanVePage` (View):** Bắt sự kiện nhấn nút và gọi phương thức `BanVeService.searchTrips(...)`.
2. **`BanVeService`:** Nhận yêu cầu và chuyển tiếp cho Controller tương ứng điều xử lý. Gọi `ChuyenTauController.traCuuChuyenTau(...)`.
3. **`ChuyenTauController`:** Kiểm tra và chuẩn hóa dữ liệu đầu vào.
4. **`ChuyenTau_DAO`:** Tạo và thực thi câu lệnh SQL (`SELECT ... FROM ChuyenTau WHERE ...`) xuống SQL Server.
5. Luồng trả kết quả ngược lại: DAO -> Controller -> Service -> View, sau đó View cập nhật danh sách (`tripListPanel.setTrips(...)`).

---

## 2. Tải dữ liệu ban đầu (`taiDuLieuBanDau`)
Mỗi khi trang Bán vé được mở lên, hệ thống cần tải danh sách Khác hàng, Khuyến mãi và thông tin Toa tàu:

1. **`BanVePage` (View):** Gọi `BanVeService.getInitialData()`.
2. **`BanVeService`:** 
    * Gọi `KhachHangController.layTatCaKhachHang()`
    * Gọi `KhuyenMaiController.layTatCaKhuyenMai()`
    * Gọi `ToaController.timKiemToa(null)`
3. **Các Controller tương ứng:** Sẽ gọi đến các DAO của chúng (`KhachHang_DAO`, `KhuyenMai_DAO`, `Toa_DAO`) để truy vấn thực tế.
4. **`BanVeService`:** Nhận dữ liệu từ Controller, đóng gói lại thành đối tượng `InitialData` (chứa các DTO như `KhachHangOption`, `KhuyenMaiOption`, danh sách `ToaOption`).
5. **`BanVePage` (View):** Lấy dữ liệu từ `InitialData` và đổ vào các ComboBox.

---

## 3. Xác nhận bán vé (`confirmSale`) - Phương thức quan trọng nhất
Khi người dùng đã chọn xong ghế và nhấn nút **Xác nhận**:

1. **`BanVePage` (View):** Thu thập dữ liệu trên UI (khách hàng, ghế ngồi, chuyến, v.v) và gọi `BanVeService.confirmSale(...)`.
2. **`BanVeService`:** Chuyển tiếp trách nhiệm cho `HoaDonController.lapHoaDonBanVe(...)`.
3. **`HoaDonController`:** Kiểm tra và chuẩn hóa tham số, sau đó gọi `HoaDonService.lapHoaDonBanVe(...)`.
4. **`HoaDonService`:** Chứa logic nghiệp vụ phức tạp nhất, bao gồm:
    * Kiểm tra xem ghế đã có người đặt hay chưa (gọi `VeTau_DAO.layGheDaDat`).
    * Tính Toán tổng tiền.
    * Tạo ID hóa đơn và chuẩn bị dữ liệu (như thông tin hành khách).
    * Gọi hàm transaction trong `HoaDon_DAO.taoHoaDonBanVe(...)`.
5. **`HoaDon_DAO` và `VeTau_DAO`:** Thực hiện INSERT dữ liệu vào 2 bảng `HoaDon` và `ChiTietHoaDon/VeTau`, commit transaction.
6. Kết quả (thành công hay thất bại, mã hóa đơn là gì) được gói trong `KetQuaLapHoaDon` và trả ngược về `BanVePage` để thông báo cho người dùng.

---

## 4. Xử lý khi có Phiếu đặt chỗ (`apDungPhieuDat`)
Khi người dùng chọn một phiếu đặt chỗ đã có sẵn trong danh sách chờ:

1. **`BanVePage` (View):** Truyền tham số `PhieuDatVeInfo` cho `BanVeService.parseReservation(...)`.
2. **`BanVeService`:** Phân tách mã `PhieuDatVeInfo` và lần lượt gọi các Controller để "lấy lời giải thích" cho các mã định danh chứa trong phiếu:
    * Gọi `KhachHangController` để tìm thông tin chi tiết khách hàng.
    * Gọi `ChuyenTauController` để tìm ga đi, ga đến, ngày khởi hành.
    * Gọi `ToaController` để lấy thông tin loại toa.
3. Trả về đối tượng DTO `ReservationData` chứa các chuỗi thông tin dễ đọc.
4. **`BanVePage` (View):** Nhận `ReservationData` và đặt (set) giá trị cho các biến quản lý trạng thái, đồng thời đồng bộ giao diện (`cboKhachHang`, `seatPanel`) sao cho khớp với phiếu đặt.

---

## 5. Tính toán và cập nhật tóm tắt (`refreshSummary`)
Mỗi khi người dùng đổi loại ghế, thay đổi số lượng, chọn mã khuyến mãi:

1. **`BanVePage` (View):** Nhận sự kiện thay đổi và gọi `BanVeService.calculatePrice(đơn giá, số lượng ghế, mã KM)`.
2. **`BanVeService`:** Tính giá gốc `base`, sau đó gọi:
    * `HoaDonController.tinhThueVAT(base)` 
    * `HoaDonController.tinhChietKhau(base, maKM)`
    * `HoaDonController.tinhTongThanhToan(...)`
3. Trả về đối tượng `PriceSummary` gồm giá gốc, thuế VAT, tiền giảm giá và tổng tiền cuối cùng.
4. **`BanVePage` (View):** Chuyển các con số này lên `BanVeSummaryPanel` để hiển thị trên màn hình cho khách xem.
