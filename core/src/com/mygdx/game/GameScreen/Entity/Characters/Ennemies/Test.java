package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) throws Exception {
        // URL de votre serveur Flask
        URL url = new URL("http://127.0.0.1:5000/test");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        // Données à envoyer à l'API Python
        String testData = "{\"message\": \"Hello from Java!\"}";

        // Envoyer les données
        con.getOutputStream().write(testData.getBytes());

        // Lire la réponse de l'API
        int status = con.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = in.lines().collect(Collectors.joining());
            System.out.println("Réponse de l'API Python : " + response);
            in.close();
        } else {
            System.out.println("Erreur HTTP : " + status);
        }
        con.disconnect();
    }
}
