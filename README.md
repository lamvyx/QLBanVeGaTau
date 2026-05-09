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

