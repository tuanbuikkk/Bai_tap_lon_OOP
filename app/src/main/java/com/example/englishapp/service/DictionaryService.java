package com.example.englishapp.service;

import androidx.lifecycle.LiveData;

import com.example.englishapp.data.local.entity.DictionaryHistory;
import com.example.englishapp.data.remote.dto.DictionaryResponse;
import com.example.englishapp.model.WordResult;
import com.example.englishapp.repository.DictionaryRepository;
import com.example.englishapp.repository.WordNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class DictionaryService {
    /**
     * Các hàm callback được gọi ở LUỒNG NỀN (ViewModel sẽ dùng postValue).
     */
    public interface LookupCallback {
        void onSuccess(WordResult result);

        void onNotFound(String word);

        void onError(String message);
    }

    private static final int MAX_LENGTH = 50;
    private static final Pattern VALID_WORD = Pattern.compile("^[a-z][a-z '\\-]*$");
    private static final Type RESPONSE_LIST_TYPE =
            new TypeToken<List<DictionaryResponse>>() {
            }.getType();

    private final DictionaryRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Gson gson = new Gson();

    public DictionaryService(DictionaryRepository repository) {
        this.repository = repository;
    }

    public void lookup(String rawWord, LookupCallback callback) {
        final String word = normalize(rawWord);
        String error = validate(word);
        if (error != null) {
            callback.onError(error);
            return;
        }
        executor.execute(() -> doLookup(word, callback));
    }

    public LiveData<List<DictionaryHistory>> getRecentHistory(int limit) {
        return repository.getRecentHistory(limit);
    }

    public void deleteHistory(long id) {
        executor.execute(() -> repository.deleteHistory(id));
    }

    public void clearHistory() {
        executor.execute(repository::clearHistory);
    }

    public void shutdown() {
        executor.shutdown();
    }

    // ---------------------------------------------------------------

    private String normalize(String raw) {
        if (raw == null) return "";
        return raw.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    private String validate(String word) {
        if (word.isEmpty()) return "Vui lòng nhập từ cần tra.";
        if (word.length() > MAX_LENGTH) return "Từ quá dài (tối đa " + MAX_LENGTH + " ký tự).";
        if (!VALID_WORD.matcher(word).matches())
            return "Chỉ hỗ trợ tra từ tiếng Anh (chữ cái a-z).";
        return null;
    }

    private void doLookup(String word, LookupCallback callback) {
        try {
            WordResult cached = readCache(word);
            if (cached != null) {
                callback.onSuccess(cached);
                return;
            }

            List<DictionaryResponse> definitions = repository.fetchDefinition(word);
            String vi = safeTranslate(word);
            WordResult result = WordResult.from(word, definitions, vi, false);
            saveHistory(word, result, definitions);
            callback.onSuccess(result);

        } catch (WordNotFoundException e) {
            callback.onNotFound(word);
        } catch (IOException e) {
            callback.onError("Không thể kết nối. Hãy kiểm tra mạng và thử lại.");
        } catch (Exception e) {
            callback.onError("Đã xảy ra lỗi khi tra từ. Vui lòng thử lại.");
        }
    }

    /**
     * Trả về null nếu chưa có cache (hoặc cache hỏng) -> sẽ gọi mạng.
     */
    private WordResult readCache(String word) {
        DictionaryHistory history = repository.findHistory(word);
        if (history == null || history.getDictionaryJson() == null) return null;

        try {
            List<DictionaryResponse> definitions =
                    gson.fromJson(history.getDictionaryJson(), RESPONSE_LIST_TYPE);
            if (definitions == null || definitions.isEmpty()) return null;

            String vi = history.getMeaningVi();
            if (vi == null || vi.isEmpty()) {
                vi = safeTranslate(word); // lần trước dịch lỗi thì thử dịch lại
            }
            WordResult result = WordResult.from(word, definitions, vi, true);
            saveHistory(word, result, definitions); // cập nhật thời gian tra
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private String safeTranslate(String word) {
        try {
            return repository.fetchVietnameseMeaning(word);
        } catch (Exception e) {
            return "";
        }
    }

    private void saveHistory(String word, WordResult result, List<DictionaryResponse> definitions) {
        DictionaryHistory h = new DictionaryHistory();
        h.setWord(word); // khóa cache = từ người dùng tra (đã chuẩn hóa)
        h.setMeaningVi(result.getMeaningVi());
        h.setPhonetic(result.getPhonetic());
        h.setAudioUrl(result.getAudioUrl());
        h.setDictionaryJson(gson.toJson(definitions));
        h.setSearchedAt(System.currentTimeMillis());
        repository.saveHistory(h);
    }
}
