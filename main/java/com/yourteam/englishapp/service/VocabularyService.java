package com.yourteam.englishapp.service;

import com.yourteam.englishapp.data.local.entity.Vocabulary;
import com.yourteam.englishapp.repository.VocabularyRepository;

import java.sql.SQLException;
import java.util.List;

public class VocabularyService {
    private final VocabularyRepository repo;

    public VocabularyService(VocabularyRepository repo) {
        this.repo = repo;
    }

    public boolean validate(Vocabulary v) {
        if (v == null) return false;
        if (v.getWord() == null || v.getWord().trim().isEmpty()) return false;
        if (v.getMeaning() == null || v.getMeaning().trim().isEmpty()) return false;
        return v.getTopicId() > 0;
    }

    public long saveVocabulary(Vocabulary v) throws SQLException {
        if (!validate(v)) {
            throw new IllegalArgumentException("Dữ liệu từ vựng không hợp lệ");
        }
        if (v.getId() > 0) {
            repo.update(v);
            return v.getId();
        } else {
            return repo.insert(v);
        }
    }

    public void deleteVocabulary(int id) throws SQLException {
        repo.delete(id);
    }

    public List<Vocabulary> getVocabulariesByTopic(int topicId) throws SQLException {
        return repo.findByTopicId(topicId);
    }

    public Vocabulary getVocabularyDetail(int id) throws SQLException {
        return repo.findById(id);
    }
}