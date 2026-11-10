package com.yourteam.englishapp.repository;

import com.yourteam.englishapp.data.local.AppDatabase;
import com.yourteam.englishapp.data.local.dao.UserDao;
import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.model.Role;
import java.sql.SQLException;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final UserDao dao = AppDatabase.getInstance().userDao();

    @Override public long register(User u) throws SQLException { return dao.insert(u); }
    @Override public User findByEmail(String e) throws SQLException { return dao.findByEmail(e); }
    @Override public User findById(long id) throws SQLException { return dao.findById(id); }
    @Override public List<User> search(String k) throws SQLException { return dao.search(k); }
    @Override public void setActive(long id, boolean a) throws SQLException { dao.setActive(id, a); }
    @Override public void setRole(long id, Role r) throws SQLException { dao.setRole(id, r); }
    @Override public void delete(long id) throws SQLException { dao.delete(id); }
    @Override public int count() throws SQLException { return dao.count(); }
}
