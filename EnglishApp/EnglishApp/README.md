# English App – Người 1: Account, Auth & Core

Java 17 · Android (MVVM) · SQL Server qua JDBC (jTDS 1.3.1)

## Chạy thử
1. SSMS: chạy `database/schema.sql`, rồi `database/create_login.sql` (đổi mật khẩu cho khớp bước 2).
2. Bật TCP/IP port 1433 + mở firewall + chế độ xác thực "SQL Server and Windows" (xem tài liệu Bước 1).
3. Copy `local.properties.example` -> `local.properties`, sửa `db.host` (emulator: `10.0.2.2`; máy thật: IP LAN).
4. Android Studio: Open thư mục này -> Sync Gradle -> Run.
5. Lần chạy đầu app tự tạo admin: **admin@app.com / Admin@123** (đổi trước khi nộp bài).

## Cho các thành viên khác
- Biết ai đang đăng nhập: `UserSession.getInstance(ctx)` (`getCurrentUser()`, `getUserId()`, `isAdmin()`).
- Kiểm tra gói (gọi ở luồng nền): `new SubscriptionService().canAccess(userId, AccessLevel.PREMIUM)`.
- Chạy DB ở luồng nền: `AppExecutors.io().execute(() -> {...})`, trả về UI bằng `postValue`.
- Thêm DAO/bảng: gửi Người 1 để gộp vào `AppDatabase.java` và `database/schema.sql`.
- Thêm màn hình: khai báo `<fragment>` + `<action>` trong `res/navigation/nav_graph.xml`, rồi thay `NOT_INTEGRATED`
  trong `HomeFragment.java` bằng `R.id.<destination của bạn>`.

## Cấu trúc
`feature/{auth,home,admin}` → `service` → `repository` → `data/local/dao` → `DatabaseClient` → SQL Server
