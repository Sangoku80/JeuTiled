package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Tools.Vector2D;
import com.mygdx.game.GameScreen.Tools.staticFunctions;
import com.mygdx.game.GameScreen.Worlds.World;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import static com.mygdx.game.Game.*;

public abstract class Enemy extends Character {

    Vector2D velocity;
    Vector2D acceleration;
    float maxSpeed;
    float maxForce;

    // IA
    ArrayList<HashMap<Vector2, Integer>> radars = new ArrayList<>();
    int angle = 0;
    Vector2 center = new Vector2();
    ArrayList<Enemy> ennemisToTrain = new ArrayList<>();

    // status
    protected static int status;
    protected static int PURSUING =0;
    protected static int IDLE =1;

    // compteur
    long currentTime = 0;

    // liste de positions du joueur
    protected ArrayList<Vector2> playerPositions = new ArrayList<>();

    // random
    protected Random random = new Random();

    // possibles destinations
    protected HashMap<Circle, String> possibleDestinations = new HashMap<>();

    public Enemy(String name, int x, int y, World currentWorld) {
        super(name, new Vector2(x, y), 1f, 20, 1, currentWorld, false);

        // mettre l'ennemie en idle
        status = IDLE;

        // chargement
        loadPossibleDestinations();

        // comportement AI
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
        this.maxSpeed  = 0.5f;
        this.maxForce = 0.1f;
    }

    // loads
    public void loadPossibleDestinations()
    {
        for (TiledMapTileLayer layer : currentWorld.layers)
        {
            for (int y = 0; y < layer.getHeight(); y++) {
                for (int x = 0; x < layer.getWidth(); x++) {

                    float tileWidth = layer.getTileWidth();
                    float tileHeight = layer.getTileHeight();

                    // Vérifier si la cellule n'est pas vide
                    if (layer.getCell(x, y) != null)
                    {
                        // pour que l'on ait le milieu de la tuile
                        TextureRegion textureRegion = layer.getCell(x, y).getTile().getTextureRegion();

                        float X = x*tileHeight+ (float) textureRegion.getRegionHeight() /2;
                        float Y = y*tileWidth + (float) textureRegion.getRegionWidth() /2;

                        possibleDestinations.put(new Circle(X, Y, 1), "");
                    }
                }
            }
        }
    }

    @Override
    public abstract void loadAnimations();

    // draws
    public void draw_radar()
    {
        for (HashMap radar : radars)
        {
            for (Object element : radar.keySet())
            {
                Vector2 position = (Vector2) element;

                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                shapeRenderer.setColor(0, 255, 0, 0);

                shapeRenderer.line(center, position);

                shapeRenderer.end();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                shapeRenderer.circle(position.x, position.y, 2);

                shapeRenderer.end();
            }
        }
    }
    public void drawLineOfSight()
    {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(0, 0, 1, 0);

        shapeRenderer.line(position.getVector2(), currentWorld.player.position.getVector2());

        shapeRenderer.end();
    }

    public void applyForce(Vector2D force) {
        this.acceleration = this.acceleration.add(force);
    }

    public Vector2D seek(Vector2D target) {
        Vector2D desired = target.subtract(this.position);
        desired = desired.normalize().multiply(maxSpeed);
        Vector2D steer = desired.subtract(this.velocity);
        if (steer.length() > maxForce) {
            steer = steer.normalize().multiply(maxForce);
        }
        return steer;
    }

    public void avoidObstacles() {
        Vector2D avoidanceForce = new Vector2D(0, 0);

        // Parcourir tous les obstacles (rectangles) potentiels
        for (Rectangle obstacle : currentWorld.collisionsEntitiesBas) {
            // Vérifier si l'ennemi est sur le point de collision avec l'obstacle
            if (Intersector.overlaps(circleDetection, obstacle)) {
                // Calculer la direction pour éviter l'obstacle
                Vector2D toObstacle = new Vector2D(obstacle.x + obstacle.width / 2, obstacle.y + obstacle.height / 2).subtract(position);
                avoidanceForce = toObstacle.normalize().multiply(maxForce);
            }
        }

        // Appliquer la force d'évitement aux mouvements de l'ennemi
        applyForce(avoidanceForce);
    }

    public void checkCollisionWithCircleDetection()
    {
        if (Intersector.overlaps(circleDetection, currentWorld.player.rect))
        {
            status = PURSUING;
        }
    }

    public void check_radar(int degree)
    {
        int lenght = 0;

        int x = (int) (center.x + Math.cos(Math.toRadians(360 - (angle + degree))) * lenght);
        int y = (int) (center.y + Math.sin(Math.toRadians(360 - (angle + degree))) * lenght);

        while (!intersectionLineWithAllCollisionsStop(center, new Vector2(x, y)) && (lenght < 100))
        {
            lenght += 1;
            x = (int) (center.x + Math.cos(Math.toRadians(360 - (angle + degree))) * lenght);
            y = (int) (center.y + Math.sin(Math.toRadians(360 - (angle + degree))) * lenght);
        }

        // on calcule la longueur du radar
        int dist = (int) Math.sqrt(Math.pow(x - center.x, 2) + Math.pow(y - center.x, 2));
        HashMap answer = new HashMap<>();
        answer.put(new Vector2(x, y), dist);
        radars.add(answer);
    }

    public ArrayList<Integer> getData() {
        // Déclare un tableau de retour avec des valeurs initiales de 0
        ArrayList<Integer> returnValues = new ArrayList<>();

        // Parcourt chaque élément dans radars et calcule la valeur correspondante
        for (int i = 0; i < radars.size() && i < 5; i++) {
            HashMap<Vector2, Integer> radar = radars.get(i);
            for (Integer value : radar.values()) {
                returnValues.add(i, value / 30);
                break; // Supposons qu'il n'y a qu'une seule valeur par hashmap
            }
        }

        return returnValues;
    }

    public void getOutputs() throws Exception
    {
        String urlString = "http://127.0.0.1:5000/getOutputs"; // Remplacez par l'URL de votre endpoint Flask

        try {
            // Création de l'objet URL
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configuration de la requête
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Vérification du code de réponse
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // Succès
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Affichage de la réponse JSON
                System.out.println("Réponse JSON: " + response.toString());
            } else {
                System.out.println("Échec de la requête. Code de réponse : " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postInputs() throws Exception
    {
        // URL de votre serveur Flask
        URL url = new URL("http://127.0.0.1:5000/postInputs");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        ArrayList<ArrayList> inputs = new ArrayList<>();

        for (Enemy enemy : ennemisToTrain)
        {
            inputs.add(enemy.getData());
        }

        // Données à envoyer à l'API Python
        String testData = inputs.toString();

        // Envoyer les données
        con.getOutputStream().write(testData.getBytes());

        // Lire la réponse de l'API
        int status = con.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = in.lines().collect(Collectors.joining());
            in.close();
        } else {
            System.out.println("Erreur HTTP : " + status);
        }
        con.disconnect();
    }

    public boolean intersectionLineWithAllCollisionsStop(Vector2 startLine, Vector2 endLine)
    {

        boolean answer = false;
        for (ArrayList collisions : currentWorld.allCollisions)
        {
            for (Object collision : collisions)
            {
                if (collision instanceof Rectangle)
                {
                    if (staticFunctions.isLineIntersectingRectangle(startLine, endLine, (Rectangle) collision))
                    {
                        if (((Rectangle) collision).x != collisionBas.x && ((Rectangle) collision).y != collisionBas.y && ((Rectangle) collision).width != collisionBas.width && ((Rectangle) collision).height != collisionBas.height)
                        {
                            answer = true;
                        }

                    }
                }
            }
        }

        return answer;
    }

    public void train()
    {
        // création des 30 ennemis à entrainer (on prend pour l'exemple le squelette)
        if (ennemisToTrain.size() !=30)
        {
            for (int i=0; i<=29; i++)
            {
                this.ennemisToTrain.add(new Skeleton((int) position.x, (int) position.y, currentWorld));
            }
        }

        // envoyer les données d'entrées du réseau de neurones
        try {
            postInputs();
            getOutputs();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getDirection(Vector2D targetPosition, Vector2D startPosition)
    {
        // Calcul du vecteur direction
        Vector2 directionVector = new Vector2(targetPosition.x - startPosition.x,  targetPosition.y - startPosition.y);

        // Calcul de l'angle en radians entre les deux points
        float angleRad = directionVector.angleRad();

        // Convertit l'angle en degrés
        float angleDeg = (float) Math.toDegrees(angleRad);

        // mettre à jour la direction
        return (int) angleDeg;
    }

    public void updateDirection()
    {

    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void pursuing()
    {
        this.velocity = this.velocity.add(this.acceleration);
        if (this.velocity.length() > maxSpeed) {
            this.velocity = this.velocity.normalize().multiply(maxSpeed);
        }

        direction = getDirection(this.position.add(this.velocity), position);
        this.acceleration = new Vector2D(0, 0);
    }

    @Override
    public void update()
    {
        pursuing();

        if (Intersector.overlaps(currentWorld.player.circleDetection, collisionBas))
        {
            moving = false;
            attack();
        }
        else
        {
            moving = true;
        }

        radars.clear();

        angle = -direction;

        // on calcule le centre de l'ennemi
        center.x = position.getVector2().x+ (float) width /2;
        center.y = position.getVector2().y+ (float) height /2;

        for (int d = -90; d < 120; d += 45) {
            check_radar(d);
        }

        train();
    }
}