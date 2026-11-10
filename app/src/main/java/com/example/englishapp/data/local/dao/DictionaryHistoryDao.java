package com.example.englishapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.englishapp.data.local.entity.DictionaryHistory;

import java.util.List;

@Dao
public interface DictionaryHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DictionaryHistory history);

    // LiveData: danh sách tự cập nhật trên màn hình khi có thay đổi
    @Query("SELECT * FROM dictionary_history ORDER BY searched_at DESC LIMIT :limit")
    LiveData<List<DictionaryHistory>> getRecent(int limit);

    @Query("SELECT * FROM dictionary_history WHERE word = :word LIMIT 1")
    DictionaryHistory findByWord(String word);

    @Query("DELETE FROM dictionary_history WHERE id = :id")
    void deleteById(long id);

    @Query("DELETE FROM dictionary_history")
    void clearAll();
}
