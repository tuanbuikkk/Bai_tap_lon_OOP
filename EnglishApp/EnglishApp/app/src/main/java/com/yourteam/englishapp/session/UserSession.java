package com.yourteam.englishapp.session;

import android.content.Context;
import android.content.SharedPreferences;
import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.model.Role;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

/**
 * Phiên đăng nhập hiện tại (Singleton). Mọi module khác dùng lớp này để biết ai đang đăng nhập.
 * Chỉ lưu uid xuống máy (mã hóa); User đầy đủ được nạp lại từ DB khi mở app.
 */
public class UserSession {
    private static final String KEY_UID = "uid";
    private static UserSession instance;

    private final SharedPreferences prefs;
    private volatile User currentUser;

    private UserSession(Context ctx) {
        SharedPreferences p;
        try {
            MasterKey key = new MasterKey.Builder(ctx)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            p = EncryptedSharedPreferences.create(ctx, "session", key,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (Exception e) {
            // Keystore lỗi (một số emulator/thiết bị): dùng SharedPreferences thường
            p = ctx.getSharedPreferences("session_plain", Context.MODE_PRIVATE);
        }
        prefs = p;
    }

    public static synchronized UserSession getInstance(Context ctx) {
        if (instance == null) instance = new UserSession(ctx.getApplicationContext());
        return instance;
    }

    public void login(User u) {
        currentUser = u;
        prefs.edit().putLong(KEY_UID, u.getId()).apply();
    }

    public void logout() {
        currentUser = null;
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() { return prefs.getLong(KEY_UID, -1) != -1; }
    public long getUserId() { return prefs.getLong(KEY_UID, -1); }
    public boolean isAdmin() { return currentUser != null && currentUser.getRole() == Role.ADMIN; }
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User u) { currentUser = u; }
}
