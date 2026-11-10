package com.yourteam.englishapp.service;

import com.yourteam.englishapp.data.local.entity.Subscription;
import com.yourteam.englishapp.model.AccessLevel;
import com.yourteam.englishapp.model.SubscriptionStatus;
import com.yourteam.englishapp.repository.SubscriptionRepository;
import com.yourteam.englishapp.repository.SubscriptionRepositoryImpl;
import com.yourteam.englishapp.utils.Result;
import java.sql.SQLException;

/**
 * Gói sử dụng. Module khác gọi canAccess(userId, requiredLevel) để khóa nội dung Premium
 * (gọi từ luồng nền).
 */
public class SubscriptionService {
    private static final String MSG_DB = "Không kết nối được cơ sở dữ liệu";
    private static final long DAY_MS = 24L * 60 * 60 * 1000;

    private final SubscriptionRepository repo;

    public SubscriptionService() {
        this(new SubscriptionRepositoryImpl());
    }

    public SubscriptionService(SubscriptionRepository repo) {
        this.repo = repo;
    }

    /** Kích hoạt gói mới: hủy gói ACTIVE cũ rồi tạo gói mới trong "days" ngày. */
    public Result<Subscription> activate(long userId, String planName, int days, AccessLevel level) {
        if (days <= 0) return Result.fail("Số ngày không hợp lệ");
        try {
            repo.cancelActiveByUser(userId);
            long now = System.currentTimeMillis();
            Subscription s = new Subscription();
            s.setUserId(userId);
            s.setPlanName(planName);
            s.setAccessLevel(level);
            s.setStatus(SubscriptionStatus.ACTIVE);
            s.setStartDate(now);
            s.setEndDate(now + days * DAY_MS);
            s.setId(repo.insert(s));
            return Result.ok(s);
        } catch (SQLException e) {
            return Result.fail(MSG_DB);
        }
    }

    /** Mức truy cập hiện tại; tự đánh dấu hết hạn trước. Không có gói hợp lệ = FREE. */
    public Result<AccessLevel> getCurrentAccessLevel(long userId) {
        try {
            long now = System.currentTimeMillis();
            repo.expireOutdated(now);
            Subscription s = repo.findActiveByUser(userId, now);
            return Result.ok(s == null ? AccessLevel.FREE : s.getAccessLevel());
        } catch (SQLException e) {
            return Result.fail(MSG_DB);
        }
    }

    /** true nếu user đủ quyền. Lỗi DB => false (từ chối cho an toàn). */
    public boolean canAccess(long userId, AccessLevel required) {
        Result<AccessLevel> r = getCurrentAccessLevel(userId);
        return r.isSuccess() && r.getData().canAccess(required);
    }

    /** Số user đang có gói PREMIUM còn hạn (cho dashboard admin). */
    public Result<Integer> countPremiumUsers() {
        try {
            long now = System.currentTimeMillis();
            repo.expireOutdated(now);
            return Result.ok(repo.countActiveUsersByLevel(AccessLevel.PREMIUM, now));
        } catch (SQLException e) {
            return Result.fail(MSG_DB);
        }
    }
}
