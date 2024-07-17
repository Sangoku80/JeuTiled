package com.mygdx.game.Tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VoitureAI extends ApplicationAdapter {

    private static final int LARGEUR = 1920;
    private static final int HAUTEUR = 1080;
    private static final int TAILLE_VOITURE_X = 60;
    private static final int TAILLE_VOITURE_Y = 60;
    private static final String URL_SERVEUR = "http://127.0.0.1:5000/predict";
    static final OkHttpClient client = new OkHttpClient();

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private Texture voitureTexture;
    private Texture mapTexture;
    private Pixmap mapPixmap;  // Ajout du Pixmap pour la vérification des pixels
    private List<Voiture> voitures;

    private static final int generationActuelle = 0;

    static class Voiture {
        Sprite sprite;
        Vector2 position;
        float angle;
        float vitesse;
        boolean vitesseDefinie;
        Vector2 centre;
        List<Radar> radars;
        List<Vector2> coins;
        boolean vivant;
        float distance;
        int temps;

        public Voiture(Texture texture) {
            this.sprite = new Sprite(texture);
            this.sprite.setSize(TAILLE_VOITURE_X, TAILLE_VOITURE_Y);
            this.position = new Vector2(830, 920);
            this.angle = 0;
            this.vitesse = 0;
            this.vitesseDefinie = false;
            this.centre = new Vector2(position.x + TAILLE_VOITURE_X / 2, position.y + TAILLE_VOITURE_Y / 2);
            this.radars = new ArrayList<>();
            this.coins = new ArrayList<>();
            this.vivant = true;
            this.distance = 0;
            this.temps = 0;
        }

        public void dessiner(SpriteBatch batch) {
            sprite.setPosition(position.x, position.y);
            sprite.setRotation(angle);
            sprite.draw(batch);
        }

        public void dessinerRadar(ShapeRenderer shapeRenderer) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.GREEN);
            for (Radar radar : radars) {
                shapeRenderer.line(centre, radar.position);
                shapeRenderer.circle(radar.position.x, radar.position.y, 5);
            }
            shapeRenderer.end();
        }

        public void verifierCollision(Pixmap mapPixmap) {
            vivant = true;
            for (Vector2 point : coins) {
                if (mapPixmap.getPixel((int) point.x, (int) point.y) == Color.rgba8888(255, 255, 255, 255)) {
                    vivant = false;
                    break;
                }
            }
        }

        public void verifierRadar(int degree, Pixmap mapPixmap) {
            int longueur = 0;
            int x = (int) (centre.x + MathUtils.cosDeg(360 - (angle + degree)) * longueur);
            int y = (int) (centre.y + MathUtils.sinDeg(360 - (angle + degree)) * longueur);

            while (!(mapPixmap.getPixel(x, y) == Color.rgba8888(255, 255, 255, 255)) && longueur < 300) {
                longueur++;
                x = (int) (centre.x + MathUtils.cosDeg(360 - (angle + degree)) * longueur);
                y = (int) (centre.y + MathUtils.sinDeg(360 - (angle + degree)) * longueur);
            }

            int dist = (int) Math.sqrt(Math.pow(x - centre.x, 2) + Math.pow(y - centre.y, 2));
            radars.add(new Radar(new Vector2(x, y), dist));
        }

        public void mettreAJour(Pixmap mapPixmap) {
            if (!vitesseDefinie) {
                vitesse = 20;
                vitesseDefinie = true;
            }

            position.x += MathUtils.cosDeg(360 - angle) * vitesse;
            position.x = MathUtils.clamp(position.x, 20, LARGEUR - 120);

            position.y += MathUtils.sinDeg(360 - angle) * vitesse;
            position.y = MathUtils.clamp(position.y, 20, HAUTEUR - 120);

            distance += vitesse;
            temps++;

            centre.set(position.x + TAILLE_VOITURE_X / 2, position.y + TAILLE_VOITURE_Y / 2);

            float longueur = 0.5f * TAILLE_VOITURE_X;
            coins.clear();
            coins.add(new Vector2(centre.x + MathUtils.cosDeg(360 - (angle + 30)) * longueur, centre.y + MathUtils.sinDeg(360 - (angle + 30)) * longueur));
            coins.add(new Vector2(centre.x + MathUtils.cosDeg(360 - (angle + 150)) * longueur, centre.y + MathUtils.sinDeg(360 - (angle + 150)) * longueur));
            coins.add(new Vector2(centre.x + MathUtils.cosDeg(360 - (angle + 210)) * longueur, centre.y + MathUtils.sinDeg(360 - (angle + 210)) * longueur));
            coins.add(new Vector2(centre.x + MathUtils.cosDeg(360 - (angle + 330)) * longueur, centre.y + MathUtils.sinDeg(360 - (angle + 330)) * longueur));

            verifierCollision(mapPixmap);
            radars.clear();
            for (int d = -90; d <= 120; d += 45) {
                verifierRadar(d, mapPixmap);
            }
        }

        public int[] obtenirDonnees() {
            int[] donnees = new int[5];
            for (int i = 0; i < radars.size(); i++) {
                donnees[i] = radars.get(i).distance / 30;
            }
            return donnees;
        }

        public boolean estEnVie() {
            return vivant;
        }

        public float obtenirRecompense() {
            return distance / (TAILLE_VOITURE_X / 2);
        }
    }

    static class Radar {
        Vector2 position;
        int distance;

        public Radar(Vector2 position, int distance) {
            this.position = position;
            this.distance = distance;
        }
    }

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, LARGEUR, HAUTEUR);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        voitureTexture = new Texture(Gdx.files.internal("car.png"));
        mapTexture = new Texture(Gdx.files.internal("map2.png"));
        mapPixmap = new Pixmap(Gdx.files.internal("map2.png"));

        voitures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            voitures.add(new Voiture(voitureTexture));
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(mapTexture, 0, 0);
        for (Voiture voiture : voitures) {
            if (voiture.estEnVie()) {
                voiture.dessiner(batch);
            }
        }
        batch.end();

        for (Voiture voiture : voitures) {
            if (voiture.estEnVie()) {
                voiture.mettreAJour(mapPixmap);
                voiture.dessinerRadar(shapeRenderer);
            }
        }

        for (Voiture voiture : voitures) {
            if (voiture.estEnVie()) {
                envoyerRequete(voiture.obtenirDonnees());
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        voitureTexture.dispose();
        mapTexture.dispose();
        mapPixmap.dispose();
        shapeRenderer.dispose();
    }

    private void envoyerRequete(int[] donnees) {
        String json = "{ \"inputs\": [";
        for (int i = 0; i < donnees.length; i++) {
            json += donnees[i];
            if (i < donnees.length - 1) {
                json += ", ";
            }
        }
        json += "] }";

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(URL_SERVEUR)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String responseData = response.body().string();
                System.out.println("Server response: " + responseData);
            }
        });
    }
}