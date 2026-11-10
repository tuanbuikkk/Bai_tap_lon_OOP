package com.example.englishapp.repository;

import androidx.lifecycle.LiveData;

import com.example.englishapp.data.local.entity.DictionaryHistory;
import com.example.englishapp.data.remote.dto.DictionaryResponse;

import java.io.IOException;
import java.util.List;

/**
 * Các hàm có "throws"/đọc ghi DB là hàm chặn (blocking): chỉ gọi ở luồng nền.
 * Riêng getRecentHistory trả LiveData nên gọi ở luồng chính được.
 */
public interface DictionaryRepository {
    List<DictionaryResponse> fetchDefinition(String word) throws IOException, WordNotFoundException;

    /**
     * Trả về "" nếu không dịch được (hết hạn mức, lỗi API...).
     */
    String fetchVietnameseMeaning(String word) throws IOException;

    DictionaryHistory findHistory(String word);

    void saveHistory(DictionaryHistory history);

    void deleteHistory(long id);

    void clearHistory();

    LiveData<List<DictionaryHistory>> getRecentHistory(int limit);
}
