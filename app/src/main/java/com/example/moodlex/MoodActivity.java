package com.example.moodlex;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        Toolbar toolbar = findViewById(R.id.mood_toolbar);
        setSupportActionBar(toolbar);
        // Toolbar back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Load default fragment (checkin. the toolbar helps with navigation in moodactivity.)
        if (savedInstanceState == null) {
            loadCheckInFragment();
        }

        //toolbar menu actions
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_home) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.menu_checkin) {
                loadCheckInFragment();
                return true;
            }

            if (item.getItemId() == R.id.menu_history) {
                loadHistoryFragment();
                return true;
            }

            if (item.getItemId() == R.id.menu_help) {
                showHelpDialog();
                return true;
            }
            return false;
        });
    }

    private void showHelpDialog() {  // hardcoded string

        String helpMessage =
                "Mood Section (Version 1.0)\n\n" +
                        "Group Members:\n" +
                        "! Samir Bani\n" +
                        "! Hibah Choudhry\n" +
                        "! Oluwaseun Ilori\n" +
                        "! Bukunmi Kadri\n" +
                        "! Vidya Puliadi Ravi Chandran\n\n" +

                        "Instructions:\n" +
                        "! Check In: Select an emoji to record your mood and add an optional note, then press Save.\n" +
                        "! History: Shows all your previous mood entries.\n" +
                        "  - Tap a mood to view details or delete it.\n" +
                        "  - Press 'Delete All Entries' to clear all saved moods.\n" +
                        "! Navigation:\n" +
                        "  - Use the toolbar to switch between Check In and History.\n" +
                        "  - Use the Home button to return to the main menu.\n\n" +
                        "This section tracks your emotional patterns to help with reflection and communication.";

        new AlertDialog.Builder(this)
                .setTitle("Help & Instructions")  // hardcoded string
                .setMessage(helpMessage)
                .setPositiveButton("OK", null)  // hardcoded string
                .create()
                .show();
    }

    private void loadCheckInFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.moodFragmentContainer, new MoodCheckInFragment())
                .commit();
    }

    private void loadHistoryFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.moodFragmentContainer, new MoodHistoryFragment())
                .addToBackStack(null)
                .commit();
    }

    // Enable back button behavior
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mood_menu, menu);
        return true;
    }
}