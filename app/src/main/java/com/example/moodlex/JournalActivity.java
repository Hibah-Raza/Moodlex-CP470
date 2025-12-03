package com.example.moodlex;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class JournalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        Toolbar toolbar = findViewById(R.id.toolbar_journal);
        setSupportActionBar(toolbar);

        // enable back button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            loadFragment(new JournalWriteFragment());
        }
        // handle back button, want to go back to main
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.journal_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_journal_write) { // same logic used in MoodActivity
            loadFragment(new JournalWriteFragment());
            return true;
        }

        if (id == R.id.menu_journal_history) {
            loadFragment(new JournalHistoryFragment());
            return true;
        }

        if (id == R.id.menu_journal_help) {
            showHelpDialog();
            return true;
        }

        if (id == R.id.journal_home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {

        String helpMessage =
                "Journal Section (Version 2.0)\n\n" +
                        "Group Members:\n" +
                        "- Samir Bani\n" +
                        "- Hibah Choudhry\n" +
                        "- Oluwaseun Ilori\n" +
                        "- Bukunmi Kadri\n" +
                        "- Vidya Puliadi Ravi Chandran\n\n" +
                        "Instructions:\n" +
                        "- Write: Type a journal entry and press Save.\n" +
                        "- History: View all your past entries.\n" +
                        "  - Tap an entry to view or delete it.\n" +
                        "  - Use Delete All to clear all entries.\n";

        new AlertDialog.Builder(this)
                .setTitle("Journal Help")
                .setMessage(helpMessage)
                .setPositiveButton("OK", null)
                .show();
    }
}
