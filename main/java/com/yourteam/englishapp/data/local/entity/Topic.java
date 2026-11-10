package com.yourteam.englishapp.data.local.entity;

public class Topic {
    private int id;
    private String name;
    private String description;
    private String iconUrl;
    private int totalWords;

    public Topic() {}

    public Topic(String name, String description, String iconUrl, int totalWords) {
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.totalWords = totalWords;
    }

    public Topic(int id, String name, String description, String iconUrl, int totalWords) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.totalWords = totalWords;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    public int getTotalWords() { return totalWords; }
    public void setTotalWords(int totalWords) { this.totalWords = totalWords; }
}