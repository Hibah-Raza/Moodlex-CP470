package com.example.moodlex;

public class MoodEntry {
    private int id;
    private int mood;
    private String note;
    private long timestamp;

    public MoodEntry(int id, int mood, String note, long timestamp) {
        this.id = id;
        this.mood = mood;
        this.note = note;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public int getMood() {
        return mood;
    }

    public String getNote() {
        return note;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
