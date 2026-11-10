package com.yourteam.englishapp.repository;

import com.yourteam.englishapp.data.local.entity.Vocabulary;

import java.sql.SQLException;
import java.util.List;

public interface VocabularyRepository {
    List<Vocabulary> findByTopicId(int topicId) throws SQLException;
    Vocabulary findById(int id) throws SQLException;
    List<Vocabulary> findByIds(List<Integer> ids) throws SQLException;
    long insert(Vocabulary v) throws SQLException;
    void update(Vocabulary v) throws SQLException;
    void delete(int id) throws SQLException;
    int countByTopicId(int topicId) throws SQLException;
}