package com.example.moodlex;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //attach toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Moodlex");

        Button btnMood = findViewById(R.id.btn_mood);
        Button btnJournal = findViewById(R.id.btn_journal);
        Button btnTrends = findViewById(R.id.btn_trends);

        btnMood.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MoodActivity.class);
            startActivity(intent);
        });

        // placeholders until we build the other sections
        btnJournal.setOnClickListener(v ->
                showNotReadyMessage()
        );

        btnTrends.setOnClickListener(v ->
                showNotReadyMessage()
        );
    }

    private void showNotReadyMessage() {
        android.widget.Toast.makeText(this, "Coming soon!", android.widget.Toast.LENGTH_SHORT).show();
    }

    private void showMainHelpDialog() {

        String message =
                "Moodlex App (Version 1.0)\n\n" +
                        "Sections:\n" +
                        "• Mood Section – Track emotions and view history.\n" +
                        "• Journal Section – Write and store personal reflections.\n" +
                        "• Trends Section – View mood graphs and emotional patterns.\n\n" +
                        "Navigation:\n" +
                        "• Use the toolbar menu to switch between sections.\n" +
                        "• All sections include a Home option to return here.\n";

        new AlertDialog.Builder(this)
                .setTitle("Help & Instructions")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_mood) {
            startActivity(new Intent(this, MoodActivity.class));
            return true;
        }
        if (id == R.id.menu_journal) {
            // Placeholder until JournalActivity is built
            Toast.makeText(this, "Journal section coming soon!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.menu_trends) {
            // Placeholder until TrendsActivity is built
            Toast.makeText(this, "Trends section coming soon!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.menu_help) {
            showMainHelpDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}