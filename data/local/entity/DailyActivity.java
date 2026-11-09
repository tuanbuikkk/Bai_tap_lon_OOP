package com.example.toeicapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "daily_activities",
        indices = {@Index(value = {"userId", "date"}, unique = true)}
)
public class DailyActivity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long userId;

    @NonNull
    public String date = "";

    public int cardsStudied;

    public DailyActivity() {
    }

    @Ignore
    public DailyActivity(long userId, @NonNull String date, int cardsStudied) {
        this.userId = userId;
        this.date = date;
        this.cardsStudied = cardsStudied;
    }
}
