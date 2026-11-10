-- =====================================================================
-- schema.sql  -  Script tạo CSDL EnglishApp (chạy trong SSMS)
-- Người 1 giữ file này. Thành viên khác gửi đoạn CREATE TABLE để gộp vào
-- phần "MODULE KHÁC" ở cuối file.
-- Script chạy lại được nhiều lần (có kiểm tra tồn tại).
-- =====================================================================
IF DB_ID('EnglishApp') IS NULL
    CREATE DATABASE EnglishApp;
GO
USE EnglishApp;
GO

-- ---------------------------------------------------------------------
-- Người 1: Users
-- ---------------------------------------------------------------------
IF OBJECT_ID('dbo.Users', 'U') IS NULL
CREATE TABLE Users (
    Id            BIGINT IDENTITY(1,1) PRIMARY KEY,
    FullName      NVARCHAR(100) NOT NULL,
    Email         NVARCHAR(150) NOT NULL UNIQUE,
    PasswordHash  NVARCHAR(255) NOT NULL,
    Salt          NVARCHAR(64)  NOT NULL,
    Role          VARCHAR(10)   NOT NULL DEFAULT 'USER'
                  CHECK (Role IN ('USER','ADMIN')),
    IsActive      BIT           NOT NULL DEFAULT 1,
    CreatedAt     DATETIME2     NOT NULL DEFAULT SYSDATETIME()
);
GO

-- ---------------------------------------------------------------------
-- Người 1: Subscriptions
-- ---------------------------------------------------------------------
IF OBJECT_ID('dbo.Subscriptions', 'U') IS NULL
CREATE TABLE Subscriptions (
    Id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    UserId      BIGINT NOT NULL,
    PlanName    NVARCHAR(50) NOT NULL,
    AccessLevel VARCHAR(10)  NOT NULL CHECK (AccessLevel IN ('FREE','PREMIUM')),
    Status      VARCHAR(10)  NOT NULL CHECK (Status IN ('ACTIVE','EXPIRED','CANCELLED')),
    StartDate   DATETIME2 NOT NULL,
    EndDate     DATETIME2 NOT NULL,
    CONSTRAINT FK_Sub_User FOREIGN KEY (UserId) REFERENCES Users(Id) ON DELETE CASCADE
);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Sub_User')
    CREATE INDEX IX_Sub_User ON Subscriptions(UserId);
GO

-- ---------------------------------------------------------------------
-- MODULE KHÁC: dán CREATE TABLE của các thành viên vào bên dưới
-- ---------------------------------------------------------------------

-- Tài khoản ADMIN mặc định được app tự tạo khi khởi động lần đầu
-- (admin@app.com / Admin@123) vì mật khẩu phải băm bằng PBKDF2 của app.
