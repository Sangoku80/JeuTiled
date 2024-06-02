package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Tools.AI.Vector2D;
import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

import java.util.*;

import static com.mygdx.game.Game.*;

public abstract class Enemy extends Character {

    Vector2D velocity;
    Vector2D acceleration;
    float maxSpeed;
    float maxForce;

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
        super(name, new Vector2(x, y), 1f, 20, 2, currentWorld, false);

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

    public void checkCollisionWithCircleAttack()
    {
        if (Intersector.overlaps(circleDetection, currentLevel.player.rect))
        {
            status = PURSUING;
        }
    }

    // updates
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

    @Override
    public void update()
    {
        this.velocity = this.velocity.add(this.acceleration);
        if (this.velocity.length() > maxSpeed) {
            this.velocity = this.velocity.normalize().multiply(maxSpeed);
        }

        direction = getDirection(this.position.add(this.velocity), position);
        this.acceleration = new Vector2D(0, 0);

        moving = !Intersector.overlaps(currentLevel.player.circleDetection, collisionBas);
    }
}