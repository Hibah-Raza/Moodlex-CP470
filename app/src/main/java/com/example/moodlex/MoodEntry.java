package com.example.moodlex;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
