package com.example.toeicapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.toeicapp.data.local.entity.Flashcard;

import java.util.List;

@Dao
public interface FlashcardDao {

    @Insert
    long insert(Flashcard flashcard);

    @Delete
    void delete(Flashcard flashcard);

    @Query("SELECT * FROM flashcards WHERE userId = :userId ORDER BY createdAt DESC, id DESC")
    LiveData<List<Flashcard>> getByUser(long userId);
}
