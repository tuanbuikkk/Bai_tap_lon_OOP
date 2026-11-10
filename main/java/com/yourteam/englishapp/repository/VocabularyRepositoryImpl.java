package com.yourteam.englishapp.repository;

import com.yourteam.englishapp.data.local.dao.VocabularyDao;
import com.yourteam.englishapp.data.local.entity.Vocabulary;

import java.sql.SQLException;
import java.util.List;

public class VocabularyRepositoryImpl implements VocabularyRepository {
    private final VocabularyDao dao;

    public VocabularyRepositoryImpl() {
        this.dao = new VocabularyDao();
    }

    public VocabularyRepositoryImpl(VocabularyDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Vocabulary> findByTopicId(int topicId) throws SQLException {
        return dao.findByTopicId(topicId);
    }

    @Override
    public Vocabulary findById(int id) throws SQLException {
        return dao.findById(id);
    }

    @Override
    public List<Vocabulary> findByIds(List<Integer> ids) throws SQLException {
        return dao.findByIds(ids);
    }

    @Override
    public long insert(Vocabulary v) throws SQLException {
        return dao.insert(v);
    }

    @Override
    public void update(Vocabulary v) throws SQLException {
        dao.update(v);
    }

    @Override
    public void delete(int id) throws SQLException {
        dao.delete(id);
    }

    @Override
    public int countByTopicId(int topicId) throws SQLException {
        return dao.countByTopicId(topicId);
    }
}