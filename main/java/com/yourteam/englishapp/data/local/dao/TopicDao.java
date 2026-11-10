package com.yourteam.englishapp.data.local.dao;

import com.yourteam.englishapp.data.local.DatabaseClient;
import com.yourteam.englishapp.data.local.entity.Topic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TopicDao {

    public long insert(Topic t) throws SQLException {
        String sql = "INSERT INTO Topics(Name, Description, IconUrl, TotalWords) VALUES (?, ?, ?, ?)";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getDescription());
            ps.setString(3, t.getIconUrl());
            ps.setInt(4, t.getTotalWords());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getLong(1) : -1;
            }
        }
    }

    public void update(Topic t) throws SQLException {
        String sql = "UPDATE Topics SET Name = ?, Description = ?, IconUrl = ?, TotalWords = ? WHERE Id = ?";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getDescription());
            ps.setString(3, t.getIconUrl());
            ps.setInt(4, t.getTotalWords());
            ps.setInt(5, t.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Topics WHERE Id = ?";
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Topic> getAllTopics() throws SQLException {
        String sql = "SELECT * FROM Topics ORDER BY Id ASC";
        List<Topic> list = new ArrayList<>();
        try (Connection c = DatabaseClient.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Topic findById(int id) throws SQLException {
        String sql = "SELECT * FROM Topics WHERE Id = ?";
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

    private Topic mapRow(ResultSet rs) throws SQLException {
        return new Topic(
                rs.getInt("Id"),
                rs.getString("Name"),
                rs.getString("Description"),
                rs.getString("IconUrl"),
                rs.getInt("TotalWords")
        );
    }
}