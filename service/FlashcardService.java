package com.example.toeicapp.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.toeicapp.data.local.AppDatabase;
import com.example.toeicapp.data.local.entity.Flashcard;
import com.example.toeicapp.data.local.entity.FlashcardItem;
import com.example.toeicapp.repository.FlashcardItemRepository;
import com.example.toeicapp.repository.FlashcardItemRepositoryImpl;
import com.example.toeicapp.repository.FlashcardRepository;
import com.example.toeicapp.repository.FlashcardRepositoryImpl;

import java.util.List;

public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final FlashcardItemRepository itemRepository;

    public FlashcardService(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        flashcardRepository = new FlashcardRepositoryImpl(db.flashcardDao());
        itemRepository = new FlashcardItemRepositoryImpl(db.flashcardItemDao());
    }

    public LiveData<List<Flashcard>> getFlashcards(long userId) {
        return flashcardRepository.getByUser(userId);
    }

    public LiveData<List<FlashcardItem>> getItems(long flashcardId) {
        return itemRepository.getByFlashcard(flashcardId);
    }

    public long createFlashcard(long userId, String name, String description) {
        String cleanName = clean(name);
        if (cleanName.isEmpty()) {
            return -1;
        }
        return flashcardRepository.insert(new Flashcard(userId, cleanName, clean(description)));
    }

    public void deleteFlashcard(Flashcard flashcard) {
        flashcardRepository.delete(flashcard);
    }

    public boolean addVocabulary(long flashcardId, long vocabularyId, String word, String meaning) {
        FlashcardItem item = new FlashcardItem(flashcardId, vocabularyId, clean(word), clean(meaning));
        return itemRepository.add(item);
    }

    public void removeItem(FlashcardItem item) {
        itemRepository.delete(item);
    }

    public void markLearned(long itemId, boolean learned) {
        itemRepository.updateLearned(itemId, learned);
    }

    private static String clean(String text) {
        return text == null ? "" : text.trim();
    }
}
