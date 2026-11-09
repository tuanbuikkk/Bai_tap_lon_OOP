package com.example.toeicapp.repository;

import androidx.lifecycle.LiveData;

import com.example.toeicapp.data.local.entity.DailyActivity;

import java.util.List;

public interface DailyActivityRepository {

    LiveData<List<DailyActivity>> getByUser(long userId);

    void record(long userId, String date, int cards);
}
