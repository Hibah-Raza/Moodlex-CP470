package com.example.moodlex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity - navigation hub
 */
public class MainActivity extends AppCompatActivity {

    private Button btnCheckIn, btnHistory, btnJournal, btnSuggestions, btnSettings;
    private MoodDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MoodDatabaseHelper(this);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        btnCheckIn = findViewById(R.id.btn_check_in);
        btnHistory = findViewById(R.id.btn_history);
        btnJournal = findViewById(R.id.btn_journal);
        btnSuggestions = findViewById(R.id.btn_suggestions);
        btnSettings = findViewById(R.id.btn_settings);
    }

    private void setupClickListeners() {
        btnCheckIn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MoodCheckInActivity.class)));
        btnHistory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MoodHistoryActivity.class)));
        btnJournal.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, JournalActivity.class)));
        btnSuggestions.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SuggestionsActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));
    }
}
