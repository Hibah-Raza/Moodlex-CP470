package com.example.moodlex;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Simple journal creation + list view (single screen for brevity)
 */
public class JournalActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private Button btnSave;
    private MoodDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        dbHelper = new MoodDatabaseHelper(this);

        etTitle = findViewById(R.id.et_journal_title);
        etContent = findViewById(R.id.et_journal_content);
        btnSave = findViewById(R.id.btn_save_journal);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "Write something first", Toast.LENGTH_SHORT).show();
                return;
            }
            long id = dbHelper.insertJournal(title, content);
            if (id != -1) {
                Toast.makeText(this, "Journal saved", Toast.LENGTH_SHORT).show();
                etTitle.setText("");
                etContent.setText("");
            } else {
                Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

