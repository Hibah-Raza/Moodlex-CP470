package com.example.moodlex;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JournalHistoryFragment extends Fragment {

    private ArrayList<JournalEntry> journalList = new ArrayList<>();
    private JournalAdapter adapter;
    private ListView listView;
    private ProgressBar progressBar;

    public JournalHistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_journal_history, container, false);

        listView = view.findViewById(R.id.journal_list);
        progressBar = view.findViewById(R.id.journal_history_progress);
        Button deleteAllBtn = view.findViewById(R.id.btn_journal_delete_all);

        adapter = new JournalAdapter(requireActivity(), journalList);
        listView.setAdapter(adapter);

        new LoadJournalsTask().execute();

        listView.setOnItemClickListener((parent, v, position, id) -> {

            JournalEntry entry = journalList.get(position);

            View dialogView = LayoutInflater.from(getActivity())
                    .inflate(R.layout.dialog_journal_detail, null);

            TextView title = dialogView.findViewById(R.id.dialog_journal_title);
            TextView content = dialogView.findViewById(R.id.dialog_journal_body);
            TextView time = dialogView.findViewById(R.id.dialog_journal_time);

            title.setText(entry.getTitle());
            content.setText(entry.getContent());
            time.setText(new Date(entry.getTimestamp()).toString());

            AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                    .setView(dialogView)
                    .create();

            dialogView.findViewById(R.id.btn_dialog_journal_delete).setOnClickListener(b -> {

                new AlertDialog.Builder(requireActivity()) // delete journal entry in this black
                        .setTitle("Delete Entry") // hardcoded string
                        .setMessage("Are you sure you want to delete this journal entry?") // hardcoded string
                        .setPositiveButton("Yes", (d1, w1) -> {

                            SharedPreferences prefs = requireActivity()
                                    .getSharedPreferences("journals", Context.MODE_PRIVATE);

                            prefs.edit().remove("journal_" + entry.getTimestamp()).apply();

                            journalList.remove(position);
                            adapter.notifyDataSetChanged();

                            Snackbar.make(requireView(), "Entry deleted!", Snackbar.LENGTH_LONG).show();  // hardcoded string
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            // cancel button
            dialogView.findViewById(R.id.btn_dialog_journal_cancel).setOnClickListener(b -> {
                dialog.dismiss();
            });

            dialog.show();
        });

        // delete all button logic
        deleteAllBtn.setOnClickListener(v -> {

            new AlertDialog.Builder(requireActivity())
                    .setTitle("Delete All Entries")  // hardcoded string
                    .setMessage("Are you sure you want to delete ALL journal entries?")  // hardcoded string
                    .setPositiveButton("Yes", (d, w) -> {

                        SharedPreferences prefs = requireActivity()
                                .getSharedPreferences("journals", Context.MODE_PRIVATE);
                        prefs.edit().clear().apply();

                        journalList.clear();
                        adapter.notifyDataSetChanged();

                        Snackbar.make(requireView(), "All journal entries deleted!", Snackbar.LENGTH_LONG).show();  // hardcoded string
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return view;
    }

    private class LoadJournalsTask extends AsyncTask<Void, Void, ArrayList<JournalEntry>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<JournalEntry> doInBackground(Void... voids) {

            ArrayList<JournalEntry> list = new ArrayList<>();
            SharedPreferences prefs = requireActivity()
                    .getSharedPreferences("journals", Context.MODE_PRIVATE);

            for (String key : prefs.getAll().keySet()) {

                String[] parts = prefs.getString(key, "").split("\\|");

                if (parts.length == 3) {
                    String title = parts[0];
                    String content = parts[1];
                    long timestamp = Long.parseLong(parts[2]);

                    list.add(new JournalEntry(title, content, timestamp));
                }
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<JournalEntry> result) {
            progressBar.setVisibility(View.GONE);

            journalList.clear();
            journalList.addAll(result);
            adapter.notifyDataSetChanged();
        }
    }

    private class JournalAdapter extends ArrayAdapter<JournalEntry> {

        private Context context;
        private List<JournalEntry> entries;

        public JournalAdapter(Context context, List<JournalEntry> list) {
            super(context, 0, list);
            this.context = context;
            this.entries = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            JournalEntry entry = entries.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.item_journal, parent, false);
            }

            TextView title = convertView.findViewById(R.id.row_journal_title);
            TextView preview = convertView.findViewById(R.id.row_journal_preview);

            title.setText(entry.getTitle().isEmpty() ? "(No Title)" : entry.getTitle());
            preview.setText(entry.getPreview());

            return convertView;
        }
    }


}
