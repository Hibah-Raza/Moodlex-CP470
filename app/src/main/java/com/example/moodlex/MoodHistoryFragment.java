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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoodHistoryFragment extends Fragment {

    private ArrayList<MoodEntry> moodList = new ArrayList<>();
    private MoodAdapter adapter;
    private ProgressBar progressBar;
    private ListView listView;

    public MoodHistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mood_history, container, false);

        progressBar = view.findViewById(R.id.progress_history); // Get views
        listView = view.findViewById(R.id.list_moods);
        Button deleteAll = view.findViewById(R.id.btn_delete_all);

        adapter = new MoodAdapter(requireActivity(), moodList); // Set adapter after listview is ready
        listView.setAdapter(adapter);

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
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Mood")  // hardcoded string
                        .setMessage("Are you sure you want to delete this mood?")
                        .setPositiveButton("Yes", (d, which) -> {  // hardcoded string
                            SharedPreferences prefs = requireActivity()
                                    .getSharedPreferences("moods", Context.MODE_PRIVATE);
                            prefs.edit().remove("mood_" + entry.getTimestamp()).apply();
                            moodList.remove(position);
                            adapter.notifyDataSetChanged();

                            Snackbar.make(requireView(), "Mood deleted!", Snackbar.LENGTH_LONG).show();  // hardcoded string

                            d.dismiss();
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", (d, which) -> d.dismiss())  // hardcoded string
                        .create()
                        .show();
            });

            // Handle cancel button
            dialogView.findViewById(R.id.btn_dialog_cancel).setOnClickListener(btn -> {
                dialog.dismiss();
            });

            dialog.show();
        });


        deleteAll.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete All Moods");  // hardcoded string
            builder.setMessage("Are you sure you want to delete ALL mood entries?");  // hardcoded string

            builder.setPositiveButton("Yes", (dialog, which) -> {  // hardcoded string

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

                Snackbar.make(view, "All moods deleted!", Snackbar.LENGTH_LONG).show();  // hardcoded string
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());  // hardcoded string

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

            SharedPreferences prefs = requireActivity().getSharedPreferences("moods", Context.MODE_PRIVATE);

            for (String key : prefs.getAll().keySet()) {
                String value = prefs.getString(key, "");
                String[] parts = value.split("\\|");

                if (parts.length == 3) {
                    String emoji = parts[0];
                    String note = parts[1];
                    long timestamp = Long.parseLong(parts[2]);

                    list.add(new MoodEntry(emoji, note, timestamp));
                }
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<MoodEntry> result) {
            progressBar.setVisibility(View.GONE);
            moodList.clear();
            moodList.addAll(result);
            adapter.notifyDataSetChanged();
        }
    }

    private class MoodAdapter extends ArrayAdapter<MoodEntry> {

        private Context context;
        private List<MoodEntry> list;

        public MoodAdapter(Context context, List<MoodEntry> list) {
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MoodEntry entry = list.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.item_mood, parent, false);
            }

            TextView emoji = convertView.findViewById(R.id.tv_row_emoji);
            TextView note = convertView.findViewById(R.id.tv_row_note);
            TextView time = convertView.findViewById(R.id.tv_row_time);

            emoji.setText(entry.getMoodEmoji());
            note.setText(entry.getNote());
            time.setText(new java.util.Date(entry.getTimestamp()).toString());

            return convertView;
        }
    }
}
