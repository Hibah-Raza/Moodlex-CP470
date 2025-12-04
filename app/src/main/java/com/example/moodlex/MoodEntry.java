package com.example.moodlex;

public class MoodEntry {
    private String moodEmoji;
    private String note;
    private long timestamp;

    public MoodEntry(String moodEmoji, String note, long timestamp) {
        this.moodEmoji = moodEmoji;
        this.note = note;
        this.timestamp = timestamp;
    }

    public String getMoodEmoji() { return moodEmoji; }
    public String getNote() { return note; }
    public long getTimestamp() { return timestamp; }
}
