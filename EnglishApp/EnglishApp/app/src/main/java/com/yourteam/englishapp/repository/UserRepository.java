package com.yourteam.englishapp.repository;

import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.model.Role;
import java.sql.SQLException;
import java.util.List;

/** Trừu tượng hóa nguồn dữ liệu người dùng. Gọi từ luồng nền. */
public interface UserRepository {
    long register(User user) throws SQLException;
    User findByEmail(String email) throws SQLException;
    User findById(long id) throws SQLException;
    List<User> search(String keyword) throws SQLException;
    void setActive(long id, boolean active) throws SQLException;
    void setRole(long id, Role role) throws SQLException;
    void delete(long id) throws SQLException;
    int count() throws SQLException;
}
