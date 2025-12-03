package com.example.moodlex;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;

public class MoodCheckInFragment extends Fragment {

    private String selectedMood = "";
    private ProgressBar progressBar;

    public MoodCheckInFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_checkin, container, false);

        Button happy = view.findViewById(R.id.btn_happy);
        Button neutral = view.findViewById(R.id.btn_neutral);
        Button sad = view.findViewById(R.id.btn_sad);
        Button save = view.findViewById(R.id.btn_save);

        EditText noteInput = view.findViewById(R.id.et_note);
        progressBar = view.findViewById(R.id.progress_save);

        // Emoji listeners
        happy.setOnClickListener(v -> selectedMood = "😊");
        neutral.setOnClickListener(v -> selectedMood = "😐");
        sad.setOnClickListener(v -> selectedMood = "😢");

        save.setOnClickListener(v -> {
            if (selectedMood.isEmpty()) {
                Toast.makeText(getActivity(), "Please select a mood", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            new SaveMoodTask(noteInput.getText().toString()).execute();
        });

        return view;
    }

    private class SaveMoodTask extends AsyncTask<Void, Void, Void> {
        private String note;

        public SaveMoodTask(String note) { this.note = note; }

        @Override
        protected Void doInBackground(Void... voids) {
            try { Thread.sleep(800); } catch (Exception ignored) {}

            SharedPreferences prefs = getActivity().getSharedPreferences("moods", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            long timestamp = System.currentTimeMillis();
            String entry = selectedMood + "|" + note + "|" + timestamp;

            editor.putString("mood_" + timestamp, entry);
            editor.apply();

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            progressBar.setVisibility(View.GONE);

            Snackbar.make(getView(), "Mood saved!", Snackbar.LENGTH_LONG).show();
        }
    }
}
