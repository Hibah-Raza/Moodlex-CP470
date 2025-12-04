package com.example.moodlex;

public class JournalEntry {

    private String title, content;
    private long timestamp;

    public JournalEntry(String title, String content, long timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }

    public String getPreview() { // get a snippet of the content
        return content.length() < 40 ? content : content.substring(0, 40) + "...";
    }
}
