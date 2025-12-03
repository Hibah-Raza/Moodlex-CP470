package com.example.moodlex;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodlex.BuildConfig;
import com.example.moodlex.R;
import com.example.moodlex.ai.ApiClient;
import com.example.moodlex.ai.OpenAIRequest;
import com.example.moodlex.ai.OpenAIResponse;
import com.example.moodlex.ai.OpenAIService;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeeklyInsightsFragment extends Fragment {

    private TextView summaryText;
    private TextView aiResultText;
    private ProgressBar progressBar;
    private long lastRequestTime = 0;
    private static final long COOLDOWN_MS = 5000; // 5 seconds

    public WeeklyInsightsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_insights, container, false);

        Button analyzeBtn = view.findViewById(R.id.btn_analyze_week);
        summaryText = view.findViewById(R.id.txt_weekly_summary);
        aiResultText = view.findViewById(R.id.txt_ai_output);
        progressBar = view.findViewById(R.id.progress_ai);

        analyzeBtn.setOnClickListener(v -> { // cooldown logic
            long now = System.currentTimeMillis();
            if (now - lastRequestTime < COOLDOWN_MS) {
                long wait = (COOLDOWN_MS - (now - lastRequestTime)) / 1000;
                Toast.makeText(getActivity(), "Please wait " + wait + "s before requesting again.", Toast.LENGTH_SHORT).show();
                return;
            }
            lastRequestTime = now;
            analyzeWeeklyMoods();
        });

        return view;
    }

    private void analyzeWeeklyMoods() {
        ArrayList<String> moods = new ArrayList<>();
        SharedPreferences prefs = requireActivity().getSharedPreferences("moods", Context.MODE_PRIVATE);

        long now = System.currentTimeMillis();
        long weekMillis = 7 * 24 * 60 * 60 * 1000L;

        int happy = 0, neutral = 0, sad = 0;

        for (String key : prefs.getAll().keySet()) {
            String entry = prefs.getString(key, "");
            if (!entry.contains("|")) continue;

            String[] parts = entry.split("\\|");
            if (parts.length != 3) continue;

            long timestamp = Long.parseLong(parts[2]);
            if (now - timestamp > weekMillis) continue;

            if (parts[0].equals("😊")) happy++;
            if (parts[0].equals("😐")) neutral++;
            if (parts[0].equals("😢")) sad++;
        }

        int total = happy + neutral + sad;
        if (total == 0) {
            summaryText.setText("No mood entries for the past 7 days.");
            return;
        }

        double avgScore = (happy * 2 + neutral * 1 + sad * 0) / (double) total;

        String summary = "Past 7-day Summary:\n" +
                "😊 Happy: " + happy + "\n" +
                "😐 Neutral: " + neutral + "\n" +
                "😢 Sad: " + sad + "\n" +
                "Average Mood Score: " + String.format("%.2f", avgScore);

        summaryText.setText(summary);

        sendToAI(summary);
    }

    private void sendToAI(String summary) {
        progressBar.setVisibility(View.VISIBLE);
        aiResultText.setText("");

        OpenAIService service = ApiClient.getClient().create(OpenAIService.class);

        String prompt = "Analyze this 7-day mood summary and give supportive mental-health advice. the user cannot actually talk to you, so just give a standard reply that needs no response. if there is none, just say 'bruh'. Here you go :\n" + summary;

        OpenAIRequest request = new OpenAIRequest(prompt);
        Call<OpenAIResponse> call = service.getAIResponse("Bearer " + BuildConfig.OPENAI_API_KEY,  request);
        call.enqueue(new Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    String out = response.body().choices.get(0).message.content;
                    aiResultText.setText(out);
                } else {
                    aiResultText.setText("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                aiResultText.setText("Failed: " + t.getMessage());
            }
        });
    }
}
