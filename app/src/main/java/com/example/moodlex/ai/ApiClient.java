package com.example.moodlex.ai;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openai.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
// https://www.omi.me/blogs/ai-integrations/how-to-integrate-openai-with-android-studio
// also thank you gemina for solving this import issues WOOO
// https://www.geeksforgeeks.org/android/how-to-build-a-chatgpt-like-app-in-android-using-openai-api/