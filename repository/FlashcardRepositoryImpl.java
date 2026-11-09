package com.example.toeicapp.repository;

import androidx.lifecycle.LiveData;

import com.example.toeicapp.data.local.dao.FlashcardDao;
import com.example.toeicapp.data.local.entity.Flashcard;

import java.util.List;

public class FlashcardRepositoryImpl implements FlashcardRepository {

    private final FlashcardDao dao;

    public FlashcardRepositoryImpl(FlashcardDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<Flashcard>> getByUser(long userId) {
        return dao.getByUser(userId);
    }

    @Override
    public long insert(Flashcard flashcard) {
        return dao.insert(flashcard);
    }

    @Override
    public void delete(Flashcard flashcard) {
        dao.delete(flashcard);
    }
}
