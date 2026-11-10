package com.yourteam.englishapp.data.local.dao;

import com.yourteam.englishapp.data.local.DatabaseClient;
import com.yourteam.englishapp.data.local.entity.Vocabulary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VocabularyDao {

    public long insert(Vocabulary v) throws SQLException {
        String sql = "INSERT INTO Vocabularies(TopicId, Word, Phonetic, PartOfSpeech, Meaning, ExampleSentence, ExampleTranslation, AudioUrl) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, v.getTopicId());
            ps.setString(2, v.getWord());
            ps.setString(3, v.getPhonetic());
            ps.setString(4, v.getPartOfSpeech());
            ps.setString(5, v.getMeaning());
            ps.setString(6, v.getExampleSentence());
            ps.setString(7, v.getExampleTranslation());
            ps.setString(8, v.getAudioUrl());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getLong(1) : -1;
            }
        }
    }

    public void update(Vocabulary v) throws SQLException {
        String sql = "UPDATE Vocabularies SET TopicId = ?, Word = ?, Phonetic = ?, PartOfSpeech = ?, Meaning = ?, ExampleSentence = ?, ExampleTranslation = ?, AudioUrl = ? WHERE Id = ?";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, v.getTopicId());
            ps.setString(2, v.getWord());
            ps.setString(3, v.getPhonetic());
            ps.setString(4, v.getPartOfSpeech());
            ps.setString(5, v.getMeaning());
            ps.setString(6, v.getExampleSentence());
            ps.setString(7, v.getExampleTranslation());
            ps.setString(8, v.getAudioUrl());
            ps.setInt(9, v.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Vocabularies WHERE Id = ?";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Vocabulary> findByTopicId(int topicId) throws SQLException {
        String sql = "SELECT * FROM Vocabularies WHERE TopicId = ? ORDER BY Id ASC";
        List<Vocabulary> list = new ArrayList<>();
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, topicId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public Vocabulary findById(int id) throws SQLException {
        String sql = "SELECT * FROM Vocabularies WHERE Id = ?";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<Vocabulary> findByIds(List<Integer> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = "SELECT * FROM Vocabularies WHERE Id IN (" + placeholders + ")";
        List<Vocabulary> list = new ArrayList<>();
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public int countByTopicId(int topicId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Vocabularies WHERE TopicId = ?";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, topicId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private Vocabulary mapRow(ResultSet rs) throws SQLException {
        return new Vocabulary(
                rs.getInt("Id"),
                rs.getInt("TopicId"),
                rs.getString("Word"),
                rs.getString("Phonetic"),
                rs.getString("PartOfSpeech"),
                rs.getString("Meaning"),
                rs.getString("ExampleSentence"),
                rs.getString("ExampleTranslation"),
                rs.getString("AudioUrl")
        );
    }
}