package com.example.englishapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// Mỗi từ chỉ có 1 dòng (unique theo word). Tra lại từ cũ thì ghi đè và cập nhật thời gian.
@Entity(tableName = "dictionary_history", indices = {@Index(value = "word", unique = true)})
public class DictionaryHistory {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "word")
    private String word = "";

    @ColumnInfo(name = "meaning_vi")
    private String meaningVi;

    @ColumnInfo(name = "phonetic")
    private String phonetic;

    @ColumnInfo(name = "audio_url")
    private String audioUrl;

    // JSON của kết quả Free Dictionary API, dùng để xem lại từ mà không cần gọi mạng
    @ColumnInfo(name = "dictionary_json")
    private String dictionaryJson;

    @ColumnInfo(name = "searched_at")
    private long searchedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getWord() {
        return word;
    }

    public void setWord(@NonNull String word) {
        this.word = word;
    }

    public String getMeaningVi() {
        return meaningVi;
    }

    public void setMeaningVi(String meaningVi) {
        this.meaningVi = meaningVi;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getDictionaryJson() {
        return dictionaryJson;
    }

    public void setDictionaryJson(String dictionaryJson) {
        this.dictionaryJson = dictionaryJson;
    }

    public long getSearchedAt() {
        return searchedAt;
    }

    public void setSearchedAt(long searchedAt) {
        this.searchedAt = searchedAt;
    }
}
