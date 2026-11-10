package com.yourteam.englishapp.data.local;

import com.yourteam.englishapp.data.local.dao.SubscriptionDao;
import com.yourteam.englishapp.data.local.dao.UserDao;

/** Nơi gom DAO của cả nhóm (Singleton). Thành viên khác nhờ Người 1 thêm DAO vào đây. */
public class AppDatabase {
    private static AppDatabase instance;

    private final UserDao userDao = new UserDao();
    private final SubscriptionDao subscriptionDao = new SubscriptionDao();
    // private final WordDao wordDao = new WordDao();   // thành viên khác nhờ bạn thêm vào

    private AppDatabase() {}

    public static synchronized AppDatabase getInstance() {
        if (instance == null) instance = new AppDatabase();
        return instance;
    }

    public UserDao userDao() { return userDao; }
    public SubscriptionDao subscriptionDao() { return subscriptionDao; }
}
