-- Tạo login riêng cho app (KHÔNG dùng sa). Chạy bằng tài khoản quản trị trong SSMS,
-- SAU KHI đã chạy schema.sql. Đổi mật khẩu trước khi dùng và khớp với local.properties.
USE master;
GO
IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = 'app_user')
    CREATE LOGIN app_user WITH PASSWORD = 'MatKhauCuaBan@123', CHECK_POLICY = OFF;
GO
USE EnglishApp;
GO
IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = 'app_user')
    CREATE USER app_user FOR LOGIN app_user;
GO
ALTER ROLE db_owner ADD MEMBER app_user;   -- bài tập lớn: cho đơn giản
GO
