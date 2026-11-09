package com.example.toeicapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "flashcards", indices = {@Index("userId")})
public class Flashcard {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long userId;

    @NonNull
    public String name = "";

    @NonNull
    public String description = "";

    public long createdAt;

    public Flashcard() {
    }

    @Ignore
    public Flashcard(long userId, @NonNull String name, @NonNull String description) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
    }
}
