package com.mygdx.game.Tests;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IA {

    private static final String URL_INPUTS = "http://localhost:5000/inputs";
    static final OkHttpClient client = new OkHttpClient();


    public static void main(String[] args) {
        get_inputs(new int[]{0, 0, 12, 21});
    }

    private static void get_inputs(int[] donnees) {
        StringBuilder json = new StringBuilder("{ \"inputs\": [");
        for (int i = 0; i < donnees.length; i++) {
            json.append(donnees[i]);
            if (i < donnees.length - 1) {
                json.append(", ");
            }
        }
        json.append("] }");

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(URL_INPUTS)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                assert response.body() != null;
            }
        });
    }
}
