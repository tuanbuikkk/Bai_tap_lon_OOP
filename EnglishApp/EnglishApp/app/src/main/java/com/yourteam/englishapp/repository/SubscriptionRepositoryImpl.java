package com.yourteam.englishapp.repository;

import com.yourteam.englishapp.data.local.AppDatabase;
import com.yourteam.englishapp.data.local.dao.SubscriptionDao;
import com.yourteam.englishapp.data.local.entity.Subscription;
import com.yourteam.englishapp.model.AccessLevel;
import java.sql.SQLException;

public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    private final SubscriptionDao dao = AppDatabase.getInstance().subscriptionDao();

    @Override public long insert(Subscription s) throws SQLException { return dao.insert(s); }
    @Override public Subscription findLatestByUser(long id) throws SQLException { return dao.findLatestByUser(id); }
    @Override public Subscription findActiveByUser(long id, long now) throws SQLException { return dao.findActiveByUser(id, now); }
    @Override public int expireOutdated(long now) throws SQLException { return dao.expireOutdated(now); }
    @Override public int cancelActiveByUser(long id) throws SQLException { return dao.cancelActiveByUser(id); }
    @Override public int countActiveUsersByLevel(AccessLevel l, long now) throws SQLException { return dao.countActiveUsersByLevel(l, now); }
}
