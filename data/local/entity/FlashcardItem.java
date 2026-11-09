package com.example.toeicapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "flashcard_items",
        foreignKeys = @ForeignKey(
                entity = Flashcard.class,
                parentColumns = "id",
                childColumns = "flashcardId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = {"flashcardId", "vocabularyId"}, unique = true)}
)
public class FlashcardItem {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long flashcardId;

    public long vocabularyId;

    @NonNull
    public String word = "";

    @NonNull
    public String meaning = "";

    public boolean learned;

    public FlashcardItem() {
    }

    @Ignore
    public FlashcardItem(long flashcardId, long vocabularyId, @NonNull String word, @NonNull String meaning) {
        this.flashcardId = flashcardId;
        this.vocabularyId = vocabularyId;
        this.word = word;
        this.meaning = meaning;
        this.learned = false;
    }
}
