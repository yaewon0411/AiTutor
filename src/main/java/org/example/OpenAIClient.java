package org.example;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;

public class OpenAIClient {
    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey = "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"; // 여기에 API 키를 입력하세요.

    public String callOpenAI(String prompt) {
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"prompt\": \"" + prompt + "\"}");
            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/engines/davinci-codex/completions")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();

            return client.newCall(request).execute().body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}