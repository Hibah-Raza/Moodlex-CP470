package com.example.moodlex;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;

public class MoodHistoryFragment extends Fragment {

    private ArrayList<MoodEntry> moodList = new ArrayList<>();
    private ProgressBar progressBar;
    private ListView listView;

    public MoodHistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mood_history, container, false);

        Button deleteAll = view.findViewById(R.id.btn_delete_all);

        progressBar = view.findViewById(R.id.progress_history);
        listView = view.findViewById(R.id.list_moods);

        new LoadMoodsTask().execute();

        listView.setOnItemClickListener((adapterView, v, position, id) -> {

            MoodEntry entry = moodList.get(position);

            // Inflate custom dialog layout for items in list
            LayoutInflater inflaterL = LayoutInflater.from(getActivity());
            View dialogView = inflaterL.inflate(R.layout.dialog_mood_detail, null);

            TextView emoji = dialogView.findViewById(R.id.dialog_emoji);
            TextView note = dialogView.findViewById(R.id.dialog_note);
            TextView time = dialogView.findViewById(R.id.dialog_time);

            emoji.setText(entry.getMoodEmoji());
            note.setText("Note: " + entry.getNote());
            time.setText("Time: " + new Date(entry.getTimestamp()).toString());

            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setView(dialogView)
                    .create();

            // Handle delete button
            dialogView.findViewById(R.id.btn_dialog_delete).setOnClickListener(btn -> {

                SharedPreferences prefs = requireActivity().getSharedPreferences("moods", Context.MODE_PRIVATE);
                prefs.edit().remove("mood_" + entry.getTimestamp()).apply();

                moodList.remove(position);

                // Update adapter
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

                Snackbar.make(requireView(), "Mood deleted!", Snackbar.LENGTH_LONG).show();

                dialog.dismiss();
            });

            // Handle cancel button
            dialogView.findViewById(R.id.btn_dialog_cancel).setOnClickListener(btn -> {
                dialog.dismiss();
            });

            dialog.show();
        });


        deleteAll.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete All Moods");
            builder.setMessage("Are you sure you want to delete ALL mood entries?");

            builder.setPositiveButton("Yes", (dialog, which) -> {

                SharedPreferences prefs = requireActivity().getSharedPreferences("moods", Context.MODE_PRIVATE);
                prefs.edit().clear().apply();

                moodList.clear();

                // Refresh list
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        new ArrayList<String>()
                );
                listView.setAdapter(adapter);

                Snackbar.make(view, "All moods deleted!", Snackbar.LENGTH_LONG).show();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog dialogBox = builder.create();
            dialogBox.show();
        });


        return view;
    }

    private class LoadMoodsTask extends AsyncTask<Void, Void, ArrayList<MoodEntry>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MoodEntry> doInBackground(Void... voids) {
            ArrayList<MoodEntry> list = new ArrayList<>();
            SharedPreferences prefs = getActivity().getSharedPreferences("moods", Context.MODE_PRIVATE);

            for (String key : prefs.getAll().keySet()) {
                String[] parts = prefs.getString(key, "").split("\\|");
                if (parts.length == 3) {
                    list.add(new MoodEntry(parts[0], parts[1], Long.parseLong(parts[2])));
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<MoodEntry> result) {
            progressBar.setVisibility(View.GONE);
            moodList = result;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    moodList.stream().map(e -> e.getMoodEmoji() + " - " + e.getNote()).toArray(String[]::new)
            );

            listView.setAdapter(adapter);
        }
    }
}
