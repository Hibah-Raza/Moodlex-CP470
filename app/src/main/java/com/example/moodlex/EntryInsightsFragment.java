package com.example.moodlex;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.moodlex.ai.ApiClient;
import com.example.moodlex.ai.OpenAIRequest;
import com.example.moodlex.ai.OpenAIResponse;
import com.example.moodlex.ai.OpenAIService;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryInsightsFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> entries = new ArrayList<>();
    private long lastInsightRequest = 0;
    private static final long ENTRY_COOLDOWN_MS = 5000; // 5 seconds

    public EntryInsightsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entry_insights, container, false);
        listView = view.findViewById(R.id.list_entry_insights);
        loadEntries();

        listView.setOnItemClickListener((adapterView, v, pos, id) -> {
            long now = System.currentTimeMillis();
            if (now - lastInsightRequest < ENTRY_COOLDOWN_MS) {
                long wait = (ENTRY_COOLDOWN_MS - (now - lastInsightRequest)) / 1000;
                Toast.makeText(getActivity(), "Please wait " + wait + "s before requesting again.", Toast.LENGTH_SHORT).show();
                return;
            }
            lastInsightRequest = now;
            analyzeSingleEntry(entries.get(pos));
        });

        return view;
    }

    private void loadEntries() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("moods", Context.MODE_PRIVATE);
        entries.clear();

        // temp list for entries with timestamps
        ArrayList<Pair<Long, String>> tempList = new ArrayList<>();
        for (String key : prefs.getAll().keySet()) {
            String entry = prefs.getString(key, "");
            if (!entry.contains("|")) continue;

            String[] parts = entry.split("\\|");
            if (parts.length != 3) continue;

            long timestamp = Long.parseLong(parts[2]);
            tempList.add(new Pair<>(timestamp, entry));
        }

        // sort by date (desc)
        tempList.sort((a, b) -> Long.compare(b.first, a.first));
        for (Pair<Long, String> pair : tempList) {
            String[] parts = pair.second.split("\\|");
            String emoji = parts[0];
            String note = parts[1];
            long timestamp = Long.parseLong(parts[2]);
            Date d = new Date(timestamp);

            entries.add(emoji + " - " + note + "\n" + d.toString());
        }

        listView.setAdapter(new ArrayAdapter<>(
                requireActivity(),
                android.R.layout.simple_list_item_1,
                entries
        ));
    }


    private void analyzeSingleEntry(String entry) {

        String prompt = "Provide supportive advice for someone who recorded this mood entry. the user cannot actually talk to you, so just give a standard reply that needs no response. if there is none, just say 'bruh'. Here you go :\n\n" + entry;

        OpenAIService service = ApiClient.getClient().create(OpenAIService.class);
        OpenAIRequest request = new OpenAIRequest(prompt);
        Call<OpenAIResponse> call = service.getAIResponse("Bearer " + BuildConfig.OPENAI_API_KEY,  request);

        LayoutInflater inflater = getLayoutInflater();
        View loadingView = inflater.inflate(R.layout.dialog_loading, null);

        AlertDialog loading = new AlertDialog.Builder(requireActivity())
                .setView(loadingView)
                .setCancelable(false)
                .create();

        loading.show();

        call.enqueue(new Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                loading.dismiss();

                if (!response.isSuccessful() || response.body() == null) {
                    showDialog("Error: " + response.code());
                    return;
                }

                String out = response.body().choices.get(0).message.content;
                showDialog(out);
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                loading.dismiss();
                showDialog("Failed: " + t.getMessage());
            }
        });
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(requireActivity())
                .setTitle("AI Insight")
                .setMessage(message)
                .setPositiveButton("Ok", (d,w)-> d.dismiss())
                .show();
    }
}
