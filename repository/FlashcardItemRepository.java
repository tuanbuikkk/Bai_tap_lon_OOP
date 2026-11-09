package com.example.toeicapp.repository;

import androidx.lifecycle.LiveData;

import com.example.toeicapp.data.local.entity.FlashcardItem;

import java.util.List;

public interface FlashcardItemRepository {

    LiveData<List<FlashcardItem>> getByFlashcard(long flashcardId);

    boolean add(FlashcardItem item);

    void delete(FlashcardItem item);

    void updateLearned(long itemId, boolean learned);
}
