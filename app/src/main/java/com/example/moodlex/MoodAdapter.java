package com.example.moodlex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter to display mood history in a RecyclerView.
 */
public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.ViewHolder> {

    private final List<MoodEntry> moodList;

    public MoodAdapter(List<MoodEntry> moodList) {
        this.moodList = moodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mood, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoodEntry moodEntry = moodList.get(position);

        holder.tvMood.setText(moodToEmoji(moodEntry.getMood()));
        holder.tvNote.setText(moodEntry.getNote() == null ? "" : moodEntry.getNote());
        holder.tvTimestamp.setText(formatTimestamp(moodEntry.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return moodList.size();
    }

    // --- Helper methods ---

    private String moodToEmoji(int mood) {
        switch (mood) {
            case 4: return "😊";
            case 3: return "😐";
            case 2: return "😢";
            case 1: return "😡";
            default: return "❓";
        }
    }

    private String formatTimestamp(long ts) {
        Date date = new Date(ts * 1000L); // SQLite stores seconds, convert to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    // --- ViewHolder class ---
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMood, tvNote, tvTimestamp;

        ViewHolder(View itemView) {
            super(itemView);
            tvMood = itemView.findViewById(R.id.tv_item_mood);
            tvNote = itemView.findViewById(R.id.tv_item_note);
            tvTimestamp = itemView.findViewById(R.id.tv_item_time);
        }
    }
}
