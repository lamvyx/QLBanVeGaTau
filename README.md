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

### `OtpService`

**Chức năng chính:** - Tạo và gửi OTP - Quản lý thời hạn OTP - Chống
spam gửi OTP

**Tính năng:** - ⏱ OTP hết hạn sau **2 phút** -  Cooldown gửi lại:
**30 giây** -  Resend sẽ tạo OTP mới -  Thread-safe
(ConcurrentHashMap)

------------------------------------------------------------------------

### `TaiKhoanService`

**Mục đích:** - Tách nghiệp vụ ra khỏi `TaiKhoan_DAO`

**Chức năng:** - Validate dữ liệu đăng nhập / đăng ký - Gọi DAO để kiểm
tra tài khoản - Xử lý logic liên quan đến tài khoản

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
    - TaiKhoan_DAO chỉ xử lý liên kết database
    - OtpVerificationPage, LoginPage chỉ hiển thị và gọi xử lý từ service

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