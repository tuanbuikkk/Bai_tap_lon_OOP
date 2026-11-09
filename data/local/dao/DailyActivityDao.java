package com.example.toeicapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.toeicapp.data.local.entity.DailyActivity;

import java.util.List;

@Dao
public interface DailyActivityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(DailyActivity activity);

    @Query("UPDATE daily_activities SET cardsStudied = cardsStudied + :cards "
            + "WHERE userId = :userId AND date = :date")
    int addCards(long userId, String date, int cards);

    @Query("SELECT * FROM daily_activities WHERE userId = :userId ORDER BY date ASC")
    LiveData<List<DailyActivity>> getByUser(long userId);

    @Transaction
    default void record(long userId, String date, int cards) {
        insert(new DailyActivity(userId, date, 0));
        addCards(userId, date, cards);
    }
}
