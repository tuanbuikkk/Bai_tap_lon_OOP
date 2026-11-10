package com.example.englishapp.repository;

import androidx.lifecycle.LiveData;

import com.example.englishapp.data.local.dao.DictionaryHistoryDao;
import com.example.englishapp.data.local.entity.DictionaryHistory;
import com.example.englishapp.data.remote.RetrofitClient;
import com.example.englishapp.data.remote.api.DictionaryApi;
import com.example.englishapp.data.remote.api.TranslateApi;
import com.example.englishapp.data.remote.dto.DictionaryResponse;
import com.example.englishapp.data.remote.dto.TranslateResponse;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

public class DictionaryRepositoryImpl implements DictionaryRepository {
    private final DictionaryApi dictionaryApi = RetrofitClient.getDictionaryApi();
    private final TranslateApi translateApi = RetrofitClient.getTranslateApi();
    private final DictionaryHistoryDao historyDao;

    public DictionaryRepositoryImpl(DictionaryHistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    @Override
    public List<DictionaryResponse> fetchDefinition(String word)
            throws IOException, WordNotFoundException {
        Response<List<DictionaryResponse>> response = dictionaryApi.getDefinition(word).execute();

        if (response.code() == 404) {
            throw new WordNotFoundException(word);
        }
        List<DictionaryResponse> body = response.body();
        if (!response.isSuccessful() || body == null) {
            throw new IOException("HTTP " + response.code());
        }
        if (body.isEmpty()) {
            throw new WordNotFoundException(word);
        }
        return body;
    }

    @Override
    public String fetchVietnameseMeaning(String word) throws IOException {
        Response<TranslateResponse> response = translateApi.translate(word, "en|vi", null).execute();
        TranslateResponse body = response.body();

        if (!response.isSuccessful() || body == null || !body.isSuccess()) {
            return "";
        }
        String text = body.getTranslatedText().trim();
        // Khi hết hạn mức, MyMemory trả câu cảnh báo thay vì bản dịch -> bỏ qua
        if (text.toUpperCase(Locale.ROOT).startsWith("MYMEMORY WARNING")) {
            return "";
        }
        return text;
    }

    @Override
    public DictionaryHistory findHistory(String word) {
        return historyDao.findByWord(word);
    }

    @Override
    public void saveHistory(DictionaryHistory history) {
        historyDao.insert(history);
    }

    @Override
    public void deleteHistory(long id) {
        historyDao.deleteById(id);
    }

    @Override
    public void clearHistory() {
        historyDao.clearAll();
    }

    @Override
    public LiveData<List<DictionaryHistory>> getRecentHistory(int limit) {
        return historyDao.getRecent(limit);
    }
}
