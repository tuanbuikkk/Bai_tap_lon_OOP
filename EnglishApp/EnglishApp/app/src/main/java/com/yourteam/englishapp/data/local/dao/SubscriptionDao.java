package com.yourteam.englishapp.data.local.dao;

import com.yourteam.englishapp.data.local.DatabaseClient;
import com.yourteam.englishapp.data.local.entity.Subscription;
import com.yourteam.englishapp.model.AccessLevel;
import com.yourteam.englishapp.model.SubscriptionStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * Truy vấn JDBC bảng Subscriptions.
 * Mọi so sánh thời gian dùng mốc "now" truyền từ app (không dùng SYSDATETIME của server)
 * để đồng nhất múi giờ với StartDate/EndDate do app ghi vào.
 */
public class SubscriptionDao {

    public long insert(Subscription s) throws SQLException {
        String sql = "INSERT INTO Subscriptions(UserId,PlanName,AccessLevel,Status,StartDate,EndDate) VALUES(?,?,?,?,?,?)";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, s.getUserId());
            ps.setString(2, s.getPlanName());
            ps.setString(3, s.getAccessLevel().name());
            ps.setString(4, s.getStatus().name());
            ps.setTimestamp(5, new Timestamp(s.getStartDate()));
            ps.setTimestamp(6, new Timestamp(s.getEndDate()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getLong(1) : -1;
            }
        }
    }

    /** Gói mới nhất của user (theo EndDate), bất kể trạng thái. */
    public Subscription findLatestByUser(long userId) throws SQLException {
        String sql = "SELECT TOP 1 * FROM Subscriptions WHERE UserId=? ORDER BY EndDate DESC";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** Gói đang ACTIVE và chưa hết hạn tại thời điểm now. */
    public Subscription findActiveByUser(long userId, long now) throws SQLException {
        String sql = "SELECT TOP 1 * FROM Subscriptions "
                + "WHERE UserId=? AND Status='ACTIVE' AND EndDate>=? ORDER BY EndDate DESC";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setTimestamp(2, new Timestamp(now));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** Đánh dấu EXPIRED các gói ACTIVE đã quá hạn. */
    public int expireOutdated(long now) throws SQLException {
        String sql = "UPDATE Subscriptions SET Status='EXPIRED' WHERE Status='ACTIVE' AND EndDate < ?";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(now));
            return ps.executeUpdate();
        }
    }

    /** Hủy các gói ACTIVE của user (dùng trước khi kích hoạt gói mới). */
    public int cancelActiveByUser(long userId) throws SQLException {
        String sql = "UPDATE Subscriptions SET Status='CANCELLED' WHERE UserId=? AND Status='ACTIVE'";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            return ps.executeUpdate();
        }
    }

    /** Số user đang có gói ACTIVE (chưa hết hạn) ở mức level. */
    public int countActiveUsersByLevel(AccessLevel level, long now) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT UserId) FROM Subscriptions "
                + "WHERE Status='ACTIVE' AND AccessLevel=? AND EndDate>=?";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, level.name());
            ps.setTimestamp(2, new Timestamp(now));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private static Subscription map(ResultSet rs) throws SQLException {
        Subscription s = new Subscription();
        s.setId(rs.getLong("Id"));
        s.setUserId(rs.getLong("UserId"));
        s.setPlanName(rs.getString("PlanName"));
        s.setAccessLevel(AccessLevel.valueOf(rs.getString("AccessLevel")));
        s.setStatus(SubscriptionStatus.valueOf(rs.getString("Status")));
        s.setStartDate(rs.getTimestamp("StartDate").getTime());
        s.setEndDate(rs.getTimestamp("EndDate").getTime());
        return s;
    }
}
