package com.example.englishapp.feature.dictionary;

import com.example.englishapp.model.WordResult;

public class DictionaryUiState {
    public enum Status {IDLE, LOADING, SUCCESS, NOT_FOUND, ERROR}

    private final Status status;
    private final WordResult data;
    private final String message;

    private DictionaryUiState(Status status, WordResult data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static DictionaryUiState idle() {
        return new DictionaryUiState(Status.IDLE, null, null);
    }

    public static DictionaryUiState loading() {
        return new DictionaryUiState(Status.LOADING, null, null);
    }

    public static DictionaryUiState success(WordResult data) {
        return new DictionaryUiState(Status.SUCCESS, data, null);
    }

    public static DictionaryUiState notFound(String word) {
        return new DictionaryUiState(Status.NOT_FOUND, null, "Không tìm thấy từ \"" + word + "\".");
    }

    public static DictionaryUiState error(String message) {
        return new DictionaryUiState(Status.ERROR, null, message);
    }

    public Status getStatus() {
        return status;
    }

    public WordResult getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
