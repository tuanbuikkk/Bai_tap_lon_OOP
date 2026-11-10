package com.yourteam.englishapp.data.local.dao;

import com.yourteam.englishapp.data.local.DatabaseClient;
import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.model.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Truy vấn JDBC bảng Users. Luôn dùng PreparedStatement để chống SQL Injection. */
public class UserDao {

    public long insert(User u) throws SQLException {
        String sql = "INSERT INTO Users(FullName,Email,PasswordHash,Salt,Role,IsActive) VALUES(?,?,?,?,?,?)";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getSalt());
            ps.setString(5, u.getRole().name());
            ps.setBoolean(6, u.isActive());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getLong(1) : -1;
            }
        }
    }

    public User findByEmail(String email) throws SQLException {
        return findOne("SELECT * FROM Users WHERE Email = ?", email, -1);
    }

    public User findById(long id) throws SQLException {
        return findOne("SELECT * FROM Users WHERE Id = ?", null, id);
    }

    private User findOne(String sql, String email, long id) throws SQLException {
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (email != null) ps.setString(1, email); else ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<User> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM Users WHERE FullName LIKE ? OR Email LIKE ? ORDER BY CreatedAt DESC";
        List<User> list = new ArrayList<>();
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String kw = "%" + (keyword == null ? "" : keyword.trim()) + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public void setActive(long id, boolean active) throws SQLException {
        update("UPDATE Users SET IsActive=? WHERE Id=?", ps -> {
            ps.setBoolean(1, active);
            ps.setLong(2, id);
        });
    }

    public void setRole(long id, Role role) throws SQLException {
        update("UPDATE Users SET Role=? WHERE Id=?", ps -> {
            ps.setString(1, role.name());
            ps.setLong(2, id);
        });
    }

    public void delete(long id) throws SQLException {
        update("DELETE FROM Users WHERE Id=?", ps -> ps.setLong(1, id));
    }

    public int count() throws SQLException {
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM Users");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private interface Binder { void bind(PreparedStatement ps) throws SQLException; }

    private void update(String sql, Binder binder) throws SQLException {
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            binder.bind(ps);
            ps.executeUpdate();
        }
    }

    private static User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("Id"));
        u.setFullName(rs.getString("FullName"));
        u.setEmail(rs.getString("Email"));
        u.setPasswordHash(rs.getString("PasswordHash"));
        u.setSalt(rs.getString("Salt"));
        u.setRole(Role.valueOf(rs.getString("Role")));
        u.setActive(rs.getBoolean("IsActive"));
        u.setCreatedAt(rs.getTimestamp("CreatedAt").getTime());
        return u;
    }
}
