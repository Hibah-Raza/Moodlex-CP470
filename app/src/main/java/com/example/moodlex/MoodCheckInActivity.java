package com.example.moodlex;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Mood check-in screen: emoji buttons + optional note
 */
public class MoodCheckInActivity extends AppCompatActivity {

    private Button btnHappy, btnSad, btnAngry, btnNeutral, btnSave;
    private EditText etNote;
    private int selectedMood = -1;
    private MoodDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_checkin);
        dbHelper = new MoodDatabaseHelper(this);
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        btnHappy = findViewById(R.id.btn_happy);
        btnSad = findViewById(R.id.btn_sad);
        btnAngry = findViewById(R.id.btn_angry);
        btnNeutral = findViewById(R.id.btn_neutral);
        btnSave = findViewById(R.id.btn_save_mood);
        etNote = findViewById(R.id.et_mood_note);
    }

    private void setupListeners() {
        btnHappy.setOnClickListener(v -> selectMood(4, btnHappy));
        btnNeutral.setOnClickListener(v -> selectMood(3, btnNeutral));
        btnSad.setOnClickListener(v -> selectMood(2, btnSad));
        btnAngry.setOnClickListener(v -> selectMood(1, btnAngry));

        btnSave.setOnClickListener(v -> {
            if (selectedMood == -1) {
                Toast.makeText(this, "Select a mood first", Toast.LENGTH_SHORT).show();
                return;
            }
            String note = etNote.getText().toString().trim();
            long id = dbHelper.insertMood(selectedMood, note);
            if (id != -1) {
                Toast.makeText(this, "Mood saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectMood(int moodValue, View btn) {
        selectedMood = moodValue;
        // simple visual feedback
        btnHappy.setAlpha(moodValue == 4 ? 1f : 0.6f);
        btnNeutral.setAlpha(moodValue == 3 ? 1f : 0.6f);
        btnSad.setAlpha(moodValue == 2 ? 1f : 0.6f);
        btnAngry.setAlpha(moodValue == 1 ? 1f : 0.6f);
    }
}
