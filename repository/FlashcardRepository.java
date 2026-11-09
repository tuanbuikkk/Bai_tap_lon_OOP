package com.example.toeicapp.repository;

import androidx.lifecycle.LiveData;

import com.example.toeicapp.data.local.entity.Flashcard;

import java.util.List;

public interface FlashcardRepository {

    LiveData<List<Flashcard>> getByUser(long userId);

    long insert(Flashcard flashcard);

    void delete(Flashcard flashcard);
}
