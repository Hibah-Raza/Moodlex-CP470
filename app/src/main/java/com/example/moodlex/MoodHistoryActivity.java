package com.example.moodlex;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Shows saved mood entries in reverse chronological order
 */
public class MoodHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoodAdapter adapter;
    private MoodDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        dbHelper = new MoodDatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_moods);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadMoods();
    }

    private void loadMoods() {
        List<MoodEntry> moods = dbHelper.getAllMoods();
        adapter = new MoodAdapter(moods);
        recyclerView.setAdapter(adapter);
    }
}
