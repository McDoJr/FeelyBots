package dev.lockpickgames.feelybots.npc;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.asurasoftware.asuraplugin.utils.ThreadUtil;
import lombok.Setter;
import okhttp3.*;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class EmotionAnalysis {

    private static final String API_KEY = "ktRzGJT8vNeI4I6nO6rGK9RCq2ANYJ3NkNTGO5c0QWRYe8fc";
    private static final String API_URL = "https://api.hume.ai/v1/emotion";

    public static void startInferenceJob(String message, Consumer<String> consumer) {
        CompletableFuture.runAsync(() -> {
            OkHttpClient client = new OkHttpClient();
            JsonObject payload = new JsonObject();
            JsonObject language = new JsonObject();
            JsonObject granularity = new JsonObject();
            granularity.addProperty("granularity", "sentence");
            language.add("language", granularity);
            payload.add("models", language);
            JsonArray text = new JsonArray();
            text.add(message);
            payload.add("text", text);

            RequestBody body = RequestBody.create(
                    payload.toString(), MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url("https://api.hume.ai/v0/batch/jobs")
                    .post(body)
                    .addHeader("X-Hume-Api-Key", API_KEY)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if(!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                Gson gson = new Gson();
                JsonObject object = gson.fromJson(response.body().charStream(), JsonObject.class);
                String jobId = object.get("job_id").getAsString();
                consumer.accept(jobId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void analyzeEmotion(String message, Consumer<JsonArray> consumer) {

        startInferenceJob(message, jobId -> {
            ThreadUtil.runAsyncDelayed(() -> {
                Bukkit.getLogger().info("Received Job ID: " + jobId);

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://api.hume.ai/v0/batch/jobs/" + jobId + "/predictions")
                        .get()
                        .addHeader("X-Hume-Api-Key", API_KEY)
                        .build();


                try (Response response = client.newCall(request).execute()) {
                    if(!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    Gson gson = new Gson();
                    consumer.accept(formatResponse(gson.fromJson(response.body().charStream(), JsonArray.class)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }, 5);
        });
    }

    private static JsonArray formatResponse(JsonArray response) {
        return response.get(0).getAsJsonObject()
                .getAsJsonObject("results")
                .getAsJsonArray("predictions")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("models")
                .getAsJsonObject("language")
                .getAsJsonArray("grouped_predictions")
                .get(0)
                .getAsJsonObject()
                .getAsJsonArray("predictions")
                .get(0)
                .getAsJsonObject()
                .getAsJsonArray("emotions");
    }

    public static String getEmotionResult(JsonArray response) {
        List<Emotion> emotions = new ArrayList<>();
        for(int i = 0; i < response.size(); i++) {
            JsonObject object = response.get(i).getAsJsonObject();
            String emotion = object.get("name").getAsString();
            double score = object.get("score").getAsDouble();
            emotions.add(new Emotion(emotion, score));
        }

        emotions.sort(Comparator.comparing(Emotion::score));
        return emotions.get(emotions.size() - 1).name();
    }
}
