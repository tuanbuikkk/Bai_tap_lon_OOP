package com.yourteam.englishapp.service;

import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.model.Role;
import com.yourteam.englishapp.repository.UserRepository;
import com.yourteam.englishapp.repository.UserRepositoryImpl;
import com.yourteam.englishapp.session.UserSession;
import com.yourteam.englishapp.utils.PasswordUtils;
import com.yourteam.englishapp.utils.Result;
import com.yourteam.englishapp.utils.ValidationUtils;
import java.sql.SQLException;

/** Đăng ký / đăng nhập / đăng xuất. Chạy trong luồng nền (do ViewModel đẩy sang). */
public class AuthService {
    public static final String DEFAULT_ADMIN_EMAIL = "admin@app.com";
    public static final String DEFAULT_ADMIN_PASSWORD = "Admin@123"; // ĐỔI trước khi nộp bài

    private static final String MSG_DB = "Không kết nối được cơ sở dữ liệu";
    // Mã lỗi SQL Server khi vi phạm UNIQUE
    private static final int DUP_KEY_1 = 2627, DUP_KEY_2 = 2601;

    private final UserRepository userRepo;
    private final UserSession session;

    public AuthService(UserSession session) {
        this(session, new UserRepositoryImpl());
    }

    public AuthService(UserSession session, UserRepository userRepo) {
        this.session = session;
        this.userRepo = userRepo;
    }

    public Result<User> register(String name, String email, String pass, String confirm) {
        if (!ValidationUtils.isValidName(name))     return Result.fail("Tên không hợp lệ");
        if (!ValidationUtils.isValidEmail(email))   return Result.fail("Email không hợp lệ");
        if (!ValidationUtils.isValidPassword(pass)) return Result.fail("Mật khẩu ≥ 8 ký tự, gồm chữ và số");
        if (!pass.equals(confirm))                  return Result.fail("Mật khẩu xác nhận không khớp");
        try {
            String mail = email.toLowerCase().trim();
            if (userRepo.findByEmail(mail) != null) return Result.fail("Email đã được đăng ký");

            String salt = PasswordUtils.generateSalt();
            User u = new User(name.trim(), mail, PasswordUtils.hash(pass, salt), salt, Role.USER);
            u.setId(userRepo.register(u));
            return Result.ok(u);
        } catch (SQLException e) {
            if (e.getErrorCode() == DUP_KEY_1 || e.getErrorCode() == DUP_KEY_2) {
                return Result.fail("Email đã được đăng ký");
            }
            return Result.fail(MSG_DB);
        }
    }

    public Result<User> login(String email, String pass) {
        if (email == null || pass == null) return Result.fail("Sai email hoặc mật khẩu");
        try {
            User u = userRepo.findByEmail(email.toLowerCase().trim());
            if (u == null || !PasswordUtils.verify(pass, u.getSalt(), u.getPasswordHash())) {
                return Result.fail("Sai email hoặc mật khẩu");   // không tiết lộ cái nào sai
            }
            if (!u.isActive()) return Result.fail("Tài khoản đã bị khóa");
            session.login(u);
            return Result.ok(u);
        } catch (SQLException e) {
            return Result.fail(MSG_DB);
        }
    }

    public void logout() { session.logout(); }

    /**
     * Mở lại app: nạp user từ uid đã lưu.
     * ok(user) = khôi phục được; ok(null) = chưa đăng nhập; fail = lỗi DB hoặc tài khoản bị khóa/xóa.
     */
    public Result<User> restoreSession() {
        if (!session.isLoggedIn()) return Result.ok(null);
        try {
            User u = userRepo.findById(session.getUserId());
            if (u == null || !u.isActive()) {
                session.logout();
                return Result.fail(u == null ? "Phiên đăng nhập không còn hiệu lực" : "Tài khoản đã bị khóa");
            }
            session.setCurrentUser(u);
            return Result.ok(u);
        } catch (SQLException e) {
            session.setCurrentUser(null);
            return Result.fail(MSG_DB);
        }
    }

    /** Nếu chưa có user nào thì tạo ADMIN mặc định. */
    public Result<User> ensureDefaultAdmin() {
        try {
            if (userRepo.count() > 0) return Result.ok(null);
            String salt = PasswordUtils.generateSalt();
            User admin = new User("Administrator", DEFAULT_ADMIN_EMAIL,
                    PasswordUtils.hash(DEFAULT_ADMIN_PASSWORD, salt), salt, Role.ADMIN);
            admin.setId(userRepo.register(admin));
            return Result.ok(admin);
        } catch (SQLException e) {
            return Result.fail(MSG_DB);
        }
    }
}
