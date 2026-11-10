package com.example.englishapp.model;

import com.example.englishapp.data.remote.dto.DictionaryResponse;

import java.util.ArrayList;
import java.util.List;

public class WordResult {
    private final String word;
    private final String phonetic;
    private final String audioUrl;
    private final String meaningVi;
    private final List<DictionaryResponse.Meaning> meanings;
    private final boolean fromCache;

    private WordResult(String word, String phonetic, String audioUrl, String meaningVi,
                       List<DictionaryResponse.Meaning> meanings, boolean fromCache) {
        this.word = word;
        this.phonetic = phonetic;
        this.audioUrl = audioUrl;
        this.meaningVi = meaningVi;
        this.meanings = meanings;
        this.fromCache = fromCache;
    }

    /**
     * Gộp danh sách mục từ API trả về (có thể nhiều mục) thành một kết quả.
     */
    public static WordResult from(String queriedWord, List<DictionaryResponse> definitions,
                                  String meaningVi, boolean fromCache) {
        String word = queriedWord;
        String phonetic = "";
        String audio = "";
        List<DictionaryResponse.Meaning> meanings = new ArrayList<>();

        for (DictionaryResponse d : definitions) {
            if (d == null) continue;
            if (phonetic.isEmpty()) phonetic = d.getDisplayPhonetic();
            if (audio.isEmpty()) audio = d.getAudioUrl();
            meanings.addAll(d.getMeanings());
        }
        if (!definitions.isEmpty() && definitions.get(0) != null
                && definitions.get(0).getWord() != null) {
            word = definitions.get(0).getWord();
        }
        return new WordResult(word, phonetic, audio,
                meaningVi == null ? "" : meaningVi, meanings, fromCache);
    }

    public String getWord() {
        return word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getMeaningVi() {
        return meaningVi;
    }

    public List<DictionaryResponse.Meaning> getMeanings() {
        return meanings;
    }

    public boolean isFromCache() {
        return fromCache;
    }
}
