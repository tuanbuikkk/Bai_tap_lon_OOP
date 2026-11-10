package com.example.englishapp.repository;

public class WordNotFoundException extends Exception {
    public WordNotFoundException(String word) {
        super("No definitions found for: " + word);
    }
}
