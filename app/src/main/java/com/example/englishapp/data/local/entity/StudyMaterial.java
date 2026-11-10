package com.example.englishapp.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "study_material")
public class StudyMaterial {
    public static final String TYPE_TEXT = "TEXT";   // content là nội dung bài học
    public static final String TYPE_LINK = "LINK";   // content là đường link web/YouTube

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String description;
    private String type;
    private String content;
    private String category;   // Part 1..Part 7, Ngữ pháp, Từ vựng
    private String level;      // ví dụ: 450+, 650+, 800+
    private long createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
