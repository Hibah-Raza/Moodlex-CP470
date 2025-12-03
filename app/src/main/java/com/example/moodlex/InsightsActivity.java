package com.example.moodlex;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class InsightsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_insights);
        setSupportActionBar(toolbar);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        // default fragment ts
        loadFragment(new WeeklyInsightsFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.insights_fragment_container, fragment)
                //.addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insights_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_weekly_insights) {
            loadFragment(new WeeklyInsightsFragment());
            return true;
        }
        if (id == R.id.menu_entry_insights) {
            loadFragment(new EntryInsightsFragment());
            return true;
        }
        if (id == R.id.menu_insights_help) {
            showHelpDialog();
            return true;
        }
        if (id == R.id.insights_home) {
            //startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {

        String helpMessage =
                "Version 1.3\n\n" +
                        "Group Members:\n" +
                        "- Samir Bani\n" +
                        "- Hibah Choudhry\n" +
                        "- Oluwaseun Ilori\n" +
                        "- Bukunmi Kadri\n" +
                        "- Vidya Puliadi Ravi Chandran\n\n" +
                        "Instructions:\n" +
                        "- Weekly Insights analyzes your mood history using AI.\n" +
                        "- Entry Insights analyzes individual mood logs.\n" +
                        "- Powered by OpenAI API.\n";

        new AlertDialog.Builder(this)
                .setTitle("Journal Help")
                .setMessage(helpMessage)
                .setPositiveButton("Ok", null)
                .show();
    }
}