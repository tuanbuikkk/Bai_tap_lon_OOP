package com.example.toeicapp.repository;

import androidx.lifecycle.LiveData;

import com.example.toeicapp.data.local.dao.DailyActivityDao;
import com.example.toeicapp.data.local.entity.DailyActivity;

import java.util.List;

public class DailyActivityRepositoryImpl implements DailyActivityRepository {

    private final DailyActivityDao dao;

    public DailyActivityRepositoryImpl(DailyActivityDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<DailyActivity>> getByUser(long userId) {
        return dao.getByUser(userId);
    }

    @Override
    public void record(long userId, String date, int cards) {
        dao.record(userId, date, cards);
    }
}
