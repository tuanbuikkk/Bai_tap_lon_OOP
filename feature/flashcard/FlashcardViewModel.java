package com.example.toeicapp.feature.flashcard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.toeicapp.data.local.entity.Flashcard;
import com.example.toeicapp.data.local.entity.FlashcardItem;
import com.example.toeicapp.service.FlashcardService;
import com.example.toeicapp.service.StreakService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlashcardViewModel extends AndroidViewModel {

    private final FlashcardService flashcardService;
    private final StreakService streakService;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<Long> userId = new MutableLiveData<>();

    private final LiveData<List<Flashcard>> flashcards;
    private final MediatorLiveData<List<FlashcardItem>> items = new MediatorLiveData<>();
    private LiveData<List<FlashcardItem>> itemsSource;

    private final MutableLiveData<Integer> index = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> flipped = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> saved = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public FlashcardViewModel(@NonNull Application application) {
        super(application);
        flashcardService = new FlashcardService(application);
        streakService = new StreakService(application);
        flashcards = Transformations.switchMap(userId, id -> flashcardService.getFlashcards(id));
    }

    public void setUserId(long id) {
        userId.setValue(id);
    }

    public LiveData<List<Flashcard>> getFlashcards() {
        return flashcards;
    }

    public LiveData<List<FlashcardItem>> getItems() {
        return items;
    }

    public LiveData<Integer> getIndex() {
        return index;
    }

    public LiveData<Boolean> getFlipped() {
        return flipped;
    }

    public LiveData<Boolean> getSaved() {
        return saved;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void createFlashcard(String name, String description) {
        Long uid = userId.getValue();
        if (uid == null) {
            error.setValue("Chưa đăng nhập");
            return;
        }
        if (name == null || name.trim().isEmpty()) {
            error.setValue("Vui lòng nhập tên bộ thẻ");
            return;
        }
        String cleanName = name.trim();
        String cleanDesc = description == null ? "" : description.trim();
        executor.execute(() -> {
            flashcardService.createFlashcard(uid, cleanName, cleanDesc);
            saved.postValue(true);
        });
    }

    public void deleteFlashcard(Flashcard flashcard) {
        executor.execute(() -> flashcardService.deleteFlashcard(flashcard));
    }

    public void selectFlashcard(long id) {
        if (itemsSource != null) {
            items.removeSource(itemsSource);
        }
        items.setValue(null);
        itemsSource = flashcardService.getItems(id);
        items.addSource(itemsSource, items::setValue);
        restart();
    }

    public void addVocabulary(long flashcardId, long vocabularyId, String word, String meaning) {
        executor.execute(() -> {
            boolean added = flashcardService.addVocabulary(flashcardId, vocabularyId, word, meaning);
            if (!added) {
                error.postValue("Từ này đã có trong bộ thẻ");
            }
        });
    }

    public void removeItem(FlashcardItem item) {
        executor.execute(() -> flashcardService.removeItem(item));
    }

    public void flip() {
        Boolean current = flipped.getValue();
        flipped.setValue(current == null || !current);
    }

    public void answer(boolean learned) {
        List<FlashcardItem> list = items.getValue();
        Integer position = index.getValue();
        Long uid = userId.getValue();
        if (list == null || position == null || uid == null || position >= list.size()) {
            return;
        }
        FlashcardItem item = list.get(position);
        executor.execute(() -> {
            flashcardService.markLearned(item.id, learned);
            streakService.recordStudy(uid, 1);
        });
        flipped.setValue(false);
        index.setValue(position + 1);
    }

    public void restart() {
        index.setValue(0);
        flipped.setValue(false);
    }

    public void clearSaved() {
        saved.setValue(null);
    }

    public void clearError() {
        error.setValue(null);
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
