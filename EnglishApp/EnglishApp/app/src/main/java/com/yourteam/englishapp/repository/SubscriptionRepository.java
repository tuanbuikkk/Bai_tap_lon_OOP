package com.yourteam.englishapp.repository;

import com.yourteam.englishapp.data.local.entity.Subscription;
import com.yourteam.englishapp.model.AccessLevel;
import java.sql.SQLException;

public interface SubscriptionRepository {
    long insert(Subscription s) throws SQLException;
    Subscription findLatestByUser(long userId) throws SQLException;
    Subscription findActiveByUser(long userId, long now) throws SQLException;
    int expireOutdated(long now) throws SQLException;
    int cancelActiveByUser(long userId) throws SQLException;
    int countActiveUsersByLevel(AccessLevel level, long now) throws SQLException;
}
