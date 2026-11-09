package com.example.toeicapp.repository;

import androidx.lifecycle.LiveData;

import com.example.toeicapp.data.local.dao.FlashcardItemDao;
import com.example.toeicapp.data.local.entity.FlashcardItem;

import java.util.List;

public class FlashcardItemRepositoryImpl implements FlashcardItemRepository {

    private final FlashcardItemDao dao;

    public FlashcardItemRepositoryImpl(FlashcardItemDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<FlashcardItem>> getByFlashcard(long flashcardId) {
        return dao.getByFlashcard(flashcardId);
    }

    @Override
    public boolean add(FlashcardItem item) {
        return dao.insert(item) != -1;
    }

    @Override
    public void delete(FlashcardItem item) {
        dao.delete(item);
    }

    @Override
    public void updateLearned(long itemId, boolean learned) {
        dao.updateLearned(itemId, learned);
    }
}
