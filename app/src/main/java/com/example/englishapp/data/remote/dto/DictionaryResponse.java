package com.example.englishapp.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DictionaryResponse {
    @SerializedName("word")
    private String word;

    @SerializedName("phonetic")
    private String phonetic;

    @SerializedName("phonetics")
    private List<Phonetic> phonetics;

    @SerializedName("meanings")
    private List<Meaning> meanings;

    public String getWord() {
        return word;
    }

    public List<Phonetic> getPhonetics() {
        return phonetics != null ? phonetics : new ArrayList<>();
    }

    public List<Meaning> getMeanings() {
        return meanings != null ? meanings : new ArrayList<>();
    }

    /**
     * Phiên âm để hiển thị: ưu tiên trường phonetic, nếu trống thì lấy từ phonetics.
     */
    public String getDisplayPhonetic() {
        if (phonetic != null && !phonetic.trim().isEmpty()) {
            return phonetic;
        }
        for (Phonetic p : getPhonetics()) {
            if (p.getText() != null && !p.getText().trim().isEmpty()) {
                return p.getText();
            }
        }
        return "";
    }

    /**
     * Link audio đầu tiên có sẵn, trả về "" nếu không có.
     */
    public String getAudioUrl() {
        for (Phonetic p : getPhonetics()) {
            String audio = p.getAudio();
            if (audio != null && !audio.trim().isEmpty()) {
                return audio.startsWith("//") ? "https:" + audio : audio;
            }
        }
        return "";
    }

    public static class Phonetic {
        @SerializedName("text")
        private String text;

        @SerializedName("audio")
        private String audio;

        public String getText() {
            return text;
        }

        public String getAudio() {
            return audio;
        }
    }

    public static class Meaning {
        @SerializedName("partOfSpeech")
        private String partOfSpeech;

        @SerializedName("definitions")
        private List<Definition> definitions;

        @SerializedName("synonyms")
        private List<String> synonyms;

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public List<Definition> getDefinitions() {
            return definitions != null ? definitions : new ArrayList<>();
        }

        public List<String> getSynonyms() {
            return synonyms != null ? synonyms : new ArrayList<>();
        }
    }

    public static class Definition {
        @SerializedName("definition")
        private String definition;

        @SerializedName("example")
        private String example;

        @SerializedName("synonyms")
        private List<String> synonyms;

        public String getDefinition() {
            return definition;
        }

        public String getExample() {
            return example;
        }

        public List<String> getSynonyms() {
            return synonyms != null ? synonyms : new ArrayList<>();
        }
    }
}
