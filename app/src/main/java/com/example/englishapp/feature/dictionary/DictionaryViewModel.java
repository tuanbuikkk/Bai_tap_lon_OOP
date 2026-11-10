package com.example.englishapp.feature.dictionary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.englishapp.data.local.AppDatabase;
import com.example.englishapp.data.local.entity.DictionaryHistory;
import com.example.englishapp.model.WordResult;
import com.example.englishapp.repository.DictionaryRepository;
import com.example.englishapp.repository.DictionaryRepositoryImpl;
import com.example.englishapp.service.DictionaryService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DictionaryViewModel extends AndroidViewModel {
    private static final int HISTORY_LIMIT = 20;

    private final DictionaryService service;
    private final MutableLiveData<DictionaryUiState> uiState =
            new MutableLiveData<>(DictionaryUiState.idle());
    private final LiveData<List<DictionaryHistory>> recentHistory;

    // Chống trường hợp bấm tra liên tiếp: chỉ nhận kết quả của lần tra mới nhất
    private final AtomicInteger requestCounter = new AtomicInteger(0);

    private WordResult selectedResult;       // cái mới thêm
    public DictionaryViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        DictionaryRepository repository = new DictionaryRepositoryImpl(db.dictionaryHistoryDao());
        service = new DictionaryService(repository);
        recentHistory = service.getRecentHistory(HISTORY_LIMIT);
    }

    public LiveData<DictionaryUiState> getUiState() {
        return uiState;
    }

    public LiveData<List<DictionaryHistory>> getRecentHistory() {
        return recentHistory;
    }

    public void search(String word) {
        final int requestId = requestCounter.incrementAndGet();
        uiState.setValue(DictionaryUiState.loading());

        service.lookup(word, new DictionaryService.LookupCallback() {
            @Override
            public void onSuccess(WordResult result) {
                if (requestId == requestCounter.get()) {
                    uiState.postValue(DictionaryUiState.success(result));
                }
            }

            @Override
            public void onNotFound(String w) {
                if (requestId == requestCounter.get()) {
                    uiState.postValue(DictionaryUiState.notFound(w));
                }
            }

            @Override
            public void onError(String message) {
                if (requestId == requestCounter.get()) {
                    uiState.postValue(DictionaryUiState.error(message));
                }
            }
        });
    }

    // mới
    public WordResult getSelectedResult() {
        return selectedResult;
    }

    // Lưu kết quả cho màn chi tiết, rồi reset trạng thái để quay lại không bị chuyển màn lần nữa
    public void openResult(WordResult result) {
        selectedResult = result;
        uiState.setValue(DictionaryUiState.idle());
    }
    public void deleteHistory(long id) {
        service.deleteHistory(id);
    }

    // mới
    public void clearHistory() {
        service.clearHistory();
    }

    @Override
    protected void onCleared() {
        service.shutdown();
    }
}
