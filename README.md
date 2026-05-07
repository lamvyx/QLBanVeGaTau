------------------------------------------------------------------------
## 06/05/2026 - THỨ 4
------------------------------------------------------------------------

### 🔹 Bổ sung package `service`

Package này chịu trách nhiệm xử lý **nghiệp vụ (business logic)**

**Chức năng:** - Nhận dữ liệu từ `controller` / `view` - Gọi đến `DAO`
để truy xuất dữ liệu - Xử lý logic: - Validate dữ liệu - Tính toán - Áp
dụng quy tắc nghiệp vụ

------------------------------------------------------------------------

### 🔹 Bổ sung package `controller`

Dùng để xử lý các logic liên quan đến **GUI phức tạp**

**Chức năng:** - Bắt sự kiện từ giao diện (button, input, ...) - Điều
phối luồng xử lý - Gọi đến `service`

------------------------------------------------------------------------

### 🔹 Luồng xử lý mới

View (GUI) -> Controller -> Service -> DAO -> Database
------------------------------------------------------------------------

## 2. Các class mới được thêm

###  Package: `service`

### `OtpService (Dịch vụ xác thực mã OTP)`
- Đây là lớp quan trọng nhất để xử lý các tính năng bảo mật hai lớp hoặc khôi phục mật khẩu.
- Quản lý bộ nhớ tạm (Cache): Sử dụng ConcurrentHashMap để lưu trữ mã OTP đi kèm với các thông tin như thời điểm gửi, thời gian hết hạn và số lần nhập sai.
- Xác thực mã (verifyOtp):
    - Kiểm tra xem mã có tồn tại hay không.
    - Kiểm tra tính hiệu lực về thời gian (2 phút).
    - Giới hạn bảo mật: Nếu nhập sai quá 5 lần, mã OTP sẽ bị hủy ngay lập tức để chống tấn công Brute-force.
- Gửi Email (sendEmail): Sử dụng thư viện Jakarta Mail để kết nối với Gmail SMTP server và gửi mã đến người dùng.
- Kiểm soát tần suất gửi (sendOtpWithValidation):
    - Tạo mã ngẫu nhiên 6 chữ số.
    - Chống spam: Bắt buộc người dùng chờ 60 giây trước khi có thể yêu cầu gửi lại mã mới.
------------------------------------------------------------------------

### `TaiKhoanService (Logic nghiệp vụ Tài khoản)`
- Lớp này xử lý các quy tắc nghiệp vụ (Business Rules) liên quan đến tài khoản người dùng.
- Đăng nhập: Xác thực tên đăng nhập và mật khẩu từ cơ sở dữ liệu.
- Đổi mật khẩu: Cập nhật mật khẩu mới cho tài khoản.
- Kiểm tra tính hợp lệ (validateMatKhau): Đây là bộ lọc bảo mật bắt buộc mật khẩu phải đạt độ khó nhất định:
    - Tối thiểu 8 ký tự.
    - Phải có chữ hoa, chữ thường và chữ số.
- Quên mật khẩu: Xử lý logic tìm kiếm Email gắn liền với tài khoản để chuẩn bị cho quy trình gửi mã OTP.

------------------------------------------------------------------------

## 3. Các class đã chỉnh sửa

###  `LoginPage`

-   Không xử lý logic trực tiếp

------------------------------------------------------------------------

###  `OtpVerificationPage`

-   Gọi `OtpService` để:
    -   Kiểm tra OTP
    -   Gửi lại OTP
    -   Kiểm tra hết hạn

------------------------------------------------------------------------

###  `TaiKhoan_DAO`

-   Chỉ giữ lại chức năng:
    -   Truy vấn DB
    -   Insert / Update / Delete
-   Không chứa logic nghiệp vụ

------------------------------------------------------------------------

## 4. Thêm dữ liệu vào Database

###  Bảng `TaiKhoan`

-   Thêm tài khoản mới khi đăng ký

###  Bảng `NhanVien`

-   Insert thông tin nhân viên tương ứng

📌 Đảm bảo: - Đồng bộ dữ liệu giữa 2 bảng - Có thể dùng transaction nếu
cần

------------------------------------------------------------------------
## Tóm lại:
- Thêm: TaiKhoanService, OtpService (để tách phần xử lý nghiệp vụ khỏi TaiKhoan_DAO và OtpVerificationPage)
- Cập nhật: 
    - TaiKhoan_DAO chỉ xử lý liên kết database, không xử lý logic nghiệp vụ 
    - OtpVerificationPage, LoginPage chỉ làm chức năng hiển thị và gọi xử lý từ service

## Trong đó:
- TaiKhoanService xử lý:
    - đăng nhập
    - lấy email
    - đổi mật khẩu
    - kiểm tra tài khoản
    - khóa tài khoản
    - Kiểm tra độ mạnh của Mật khẩu (Validation)
    - cập nhật thông tin tài khoản

- OtpService xử lý:
    - Tạo mã OTP bảo mật
    - Quản lý lưu trữ tạm thời
    - Xác thực Email đầu vào
    - Kiểm soát thời gian gửi lại (Cooldown)
    - Cấu hình và gửi Email
    - Xác minh mã OTP (Verification)
    - Quản lý thời gian hết hạn (Expiration)
    - Cơ chế chống tấn công Brute-force

------------------------------------------------------------------------
## 07/05/2026 - THỨ 5
------------------------------------------------------------------------
## Class TrangChinhPage (hơn 500 dòng) --> Sau khi sửa (315 dòng) chỉ chứa mỗi giao diện, các phần xử lý thì chỉ gọi từ lớp khác

- Controller: 
    - Thêm class TrangChinhController : 
        - Quản lý trạng thái: Lưu trữ thông tin tài khoản (TaiKhoan) đang đăng nhập.
        - Làm cầu nối giữa dữ liệu người dùng và việc hiển thị trang.
        - Điều hướng: Nhận yêu cầu từ người dùng (khi họ click vào menu hoặc menu con) và gọi PageFactory để lấy về giao diện (JPanel) tương ứng.

- Service:
    - Thêm class PhanQuyenService: 
        - Kiểm tra vai trò: Xác định xem tài khoản đăng nhập là Quản lý (ADMIN/QUAN_LY) hay Nhân viên.
        - Lọc Menu: Trả về danh sách các mục Menu chính mà người dùng đó được phép nhìn thấy (ví dụ: Nhân viên không thấy menu "Nhân viên").
        - Xử lý Menu con: Trình bày các chức năng chi tiết dựa trên quyền. Ví dụ: Cùng là menu "Khách hàng", nhưng Quản lý thì có quyền "Thêm/Cập nhật", còn Nhân viên chỉ có quyền "Tra cứu/Lịch sử".
        - Định dạng hiển thị: Chuyển đổi mã vai trò từ database thành tên hiển thị thân thiện (ví dụ: "QUAN_LY" -> "Quản trị viên").

- View: 
    - Thêm class PageFactory: lớp quản lý đổi giao diện cho trang chính:
        - Khởi tạo linh hoạt: Thay vì viết code khởi tạo giao diện ở khắp nơi, lớp này tập trung logic new các Page() vào một chỗ.
        - Logic hiển thị: Dựa vào tên Menu và tên Menu con được truyền vào, nó sẽ switch-case để tạo ra đúng JPanel cần thiết (như NhanVienThemPage, VeTauPage,...).
        - Tính đóng gói: Giúp TrangChinhController không cần biết chi tiết cách khởi tạo từng trang, chỉ cần yêu cầu "Cho tôi trang X" là được.

## Tóm tắt luồng hoạt động
- Người dùng click vào một chức năng trên menu.

- TrangChinhController tiếp nhận sự kiện, lấy thông tin menu và menuCon.

- TrangChinhController gọi PageFactory.taoPageTheoMenu().

- PageFactory hỏi PhanQuyenService để kiểm tra quyền (nếu cần), sau đó new (tạo mới) trang giao diện tương ứng.

- TrangChinhController nhận lại trang đó và hiển thị lên vùng nội dung chính của ứng dụng.

