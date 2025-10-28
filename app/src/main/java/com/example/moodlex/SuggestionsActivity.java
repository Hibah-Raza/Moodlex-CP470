package com.example.moodlex;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Rule-based communication suggestions
 */
public class SuggestionsActivity extends AppCompatActivity {

    private TextView tvSuggestions;
    private MoodDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_suggestions);
        tvSuggestions = findViewById(R.id.tv_suggestions);
        dbHelper = new MoodDatabaseHelper(this);

        // Simple example: generate suggestion from last mood
        MoodEntry last = dbHelper.getLastMood();
        if (last == null) {
            tvSuggestions.setText("No mood data yet — try a check-in.");
        } else {
            tvSuggestions.setText(generateSuggestionForMood(last.getMood()));
        }
    }

    private String generateSuggestionForMood(int mood) {
        // 4=happy,3=neutral,2=sad,1=angry
        switch (mood) {
            case 4:
                return "You're feeling good — consider sharing gratitude with someone or scheduling a small celebration.";
            case 3:
                return "You're neutral — check in with what needs your attention. A short walk or journaling may help.";
            case 2:
                return "You seem sad — try expressing how you feel with 'I feel...' statements and ask for support.";
            case 1:
                return "Feeling angry? Pause, breathe, and use 'When X happened, I felt Y' to avoid blaming language.";
            default:
                return "Try checking in first.";
        }
    }
}
