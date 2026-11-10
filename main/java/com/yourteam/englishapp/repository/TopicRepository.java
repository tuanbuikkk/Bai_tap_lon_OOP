package com.yourteam.englishapp.repository;

import com.yourteam.englishapp.data.local.entity.Topic;

import java.sql.SQLException;
import java.util.List;

public interface TopicRepository {
    List<Topic> getAllTopics() throws SQLException;
    Topic findById(int id) throws SQLException;
    long insert(Topic t) throws SQLException;
    void update(Topic t) throws SQLException;
    void delete(int id) throws SQLException;
}