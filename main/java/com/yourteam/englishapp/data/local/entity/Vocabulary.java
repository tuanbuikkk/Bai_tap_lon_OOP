package com.yourteam.englishapp.data.local.entity;

public class Vocabulary {
    private int id;
    private int topicId;
    private String word;
    private String phonetic;
    private String partOfSpeech;
    private String meaning;
    private String exampleSentence;
    private String exampleTranslation;
    private String audioUrl;

    public Vocabulary() {}

    public Vocabulary(int topicId, String word, String phonetic, String partOfSpeech,
                      String meaning, String exampleSentence, String exampleTranslation, String audioUrl) {
        this.topicId = topicId;
        this.word = word;
        this.phonetic = phonetic;
        this.partOfSpeech = partOfSpeech;
        this.meaning = meaning;
        this.exampleSentence = exampleSentence;
        this.exampleTranslation = exampleTranslation;
        this.audioUrl = audioUrl;
    }

    public Vocabulary(int id, int topicId, String word, String phonetic, String partOfSpeech,
                      String meaning, String exampleSentence, String exampleTranslation, String audioUrl) {
        this.id = id;
        this.topicId = topicId;
        this.word = word;
        this.phonetic = phonetic;
        this.partOfSpeech = partOfSpeech;
        this.meaning = meaning;
        this.exampleSentence = exampleSentence;
        this.exampleTranslation = exampleTranslation;
        this.audioUrl = audioUrl;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTopicId() { return topicId; }
    public void setTopicId(int topicId) { this.topicId = topicId; }
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }
    public String getPhonetic() { return phonetic; }
    public void setPhonetic(String phonetic) { this.phonetic = phonetic; }
    public String getPartOfSpeech() { return partOfSpeech; }
    public void setPartOfSpeech(String partOfSpeech) { this.partOfSpeech = partOfSpeech; }
    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }
    public String getExampleSentence() { return exampleSentence; }
    public void setExampleSentence(String exampleSentence) { this.exampleSentence = exampleSentence; }
    public String getExampleTranslation() { return exampleTranslation; }
    public void setExampleTranslation(String exampleTranslation) { this.exampleTranslation = exampleTranslation; }
    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }
}