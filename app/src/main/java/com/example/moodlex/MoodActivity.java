package com.example.moodlex;

import android.os.Bundle;
import android.view.Menu;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class MoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        Toolbar toolbar = findViewById(R.id.mood_toolbar);
        setSupportActionBar(toolbar);
        // Toolbar back button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Load default fragment (checkin. the toolbar helps with navigation in moodactivity.)
        if (savedInstanceState == null) {
            loadFragment(new MoodCheckInFragment());
        }

        //toolbar menu actions
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_home) {
                finish();
                return true;
            }
            if (item.getItemId() == R.id.menu_mood_checkin) {
                loadFragment(new MoodCheckInFragment());
                return true;
            }

            if (item.getItemId() == R.id.menu_mood_history) {
                loadFragment(new MoodHistoryFragment());
                return true;
            }

            if (item.getItemId() == R.id.menu_mood_help) {
                showHelpDialog();
                return true;
            }
            return false;
        });

        // handle back button, want to go back to main
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void showHelpDialog() {  // hardcoded string

        String helpMessage =
                "Mood Section (Version 1.0)\n\n" +
                        "Group Members:\n" +
                        "Samir Bani\n" +
                        "Hibah Choudhry\n" +
                        "Oluwaseun Ilori\n" +
                        "Bukunmi Kadri\n" +
                        "Vidya Puliadi Ravi Chandran\n\n" +

                        "Instructions:\n" +
                        "Check In: Select an emoji to record your mood and add an optional note, then press Save.\n" +
                        "History: Shows all your previous mood entries.\n" +
                        "  - Tap a mood to view details or delete it.\n" +
                        "  - Press 'Delete All Entries' to clear all saved moods.\n" +
                        "Navigation:\n" +
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

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.moodFragmentContainer, fragment)
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