package com.yourteam.englishapp.repository;

import com.yourteam.englishapp.data.local.dao.TopicDao;
import com.yourteam.englishapp.data.local.entity.Topic;

import java.sql.SQLException;
import java.util.List;

public class TopicRepositoryImpl implements TopicRepository {
    private final TopicDao dao;

    public TopicRepositoryImpl() {
        this.dao = new TopicDao();
    }

    public TopicRepositoryImpl(TopicDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Topic> getAllTopics() throws SQLException {
        return dao.getAllTopics();
    }

    @Override
    public Topic findById(int id) throws SQLException {
        return dao.findById(id);
    }

    @Override
    public long insert(Topic t) throws SQLException {
        return dao.insert(t);
    }

    @Override
    public void update(Topic t) throws SQLException {
        dao.update(t);
    }

    @Override
    public void delete(int id) throws SQLException {
        dao.delete(id);
    }
}