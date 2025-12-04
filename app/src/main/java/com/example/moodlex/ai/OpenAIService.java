package com.example.moodlex.ai;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OpenAIService {
// wow frieren
    @POST("v1/chat/completions")
    Call<OpenAIResponse> getAIResponse(
            @Header("Authorization") String apiKey,
            @Body OpenAIRequest request
    );
}
