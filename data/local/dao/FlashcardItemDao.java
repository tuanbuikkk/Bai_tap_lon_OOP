package com.example.toeicapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.toeicapp.data.local.entity.FlashcardItem;

import java.util.List;

@Dao
public interface FlashcardItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(FlashcardItem item);

    @Delete
    void delete(FlashcardItem item);

    @Query("SELECT * FROM flashcard_items WHERE flashcardId = :flashcardId ORDER BY id ASC")
    LiveData<List<FlashcardItem>> getByFlashcard(long flashcardId);

    @Query("UPDATE flashcard_items SET learned = :learned WHERE id = :itemId")
    void updateLearned(long itemId, boolean learned);
}
