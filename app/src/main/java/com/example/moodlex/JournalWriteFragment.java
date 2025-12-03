package com.example.moodlex;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class JournalWriteFragment extends Fragment {

    private ProgressBar progressBar;

    public JournalWriteFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_journal_write, container, false);

        EditText titleInput = view.findViewById(R.id.journal_title);
        EditText bodyInput = view.findViewById(R.id.journal_body);
        Button saveButton = view.findViewById(R.id.save_journal_btn);
        progressBar = view.findViewById(R.id.journal_progress);

        saveButton.setOnClickListener(v -> {

            String title = titleInput.getText().toString().trim();
            String body = bodyInput.getText().toString().trim();

            if (body.isEmpty()) {
                Toast.makeText(getActivity(),
                        "Please write something in your journal",
                        Toast.LENGTH_SHORT).show();  // hardcoded string
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // UI feedback on save
            int defaultColor = getResources().getColor(R.color.mood_default);
            saveButton.setBackgroundTintList(ColorStateList.valueOf(defaultColor));

            new Handler().postDelayed(() ->
                            saveButton.setBackgroundTintList(ColorStateList.valueOf(defaultColor)),
                    500
            );

            new SaveJournalTask(title, body, titleInput, bodyInput).execute();
        });

        return view;
    }

    private class SaveJournalTask extends AsyncTask<Void, Void, Void> {

        private String title, body;
        private EditText titleInput, bodyInput;

        public SaveJournalTask(String title, String body,
                               EditText titleInput, EditText bodyInput) {
            this.title = title;
            this.body = body;
            this.titleInput = titleInput;
            this.bodyInput = bodyInput;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try { Thread.sleep(700); } catch (Exception ignored) {}

            SharedPreferences prefs =
                    requireActivity().getSharedPreferences("journals", Context.MODE_PRIVATE);

            long timestamp = System.currentTimeMillis();
            // Format: title | body | timestamp
            String entry = title + "|" + body + "|" + timestamp;

            prefs.edit().putString("journal_" + timestamp, entry).apply();

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            progressBar.setVisibility(View.GONE);

            // Reset UI
            titleInput.setText("");
            bodyInput.setText("");

            Snackbar.make(requireView(), "Journal entry saved!", Snackbar.LENGTH_LONG).show(); // hardcoded string
        }
    }
}
