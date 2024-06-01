package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

import java.util.*;

import static com.mygdx.game.Game.*;

public class Enemy extends Character {

    // status
    protected static int status;
    protected static int PURSUING =0;
    protected static int IDLE =1;

    // cercle de détection du joueur
    protected Circle circleAttack;

    // compteur
    long currentTime = 0;

    // liste de positions du joueur
    protected ArrayList<Vector2> playerPositions = new ArrayList<>();

    // random
    protected Random random = new Random();

    // possibles destinations
    protected HashMap<Circle, String> possibleDestinations = new HashMap<>();

    public Enemy(int x, int y, World currentWorld) {
        super("enemy", new Vector2(x, y), 1f, 20, 2, currentWorld, true);

        // cercle de détection du joueur
        this.circleAttack = new Circle();

        // mettre l'ennemie en idle
        status = IDLE;

        // chargement
        loadPossibleDestinations();
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
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        // en mouvement
        animations.put(bas+ move, (new Animation(new int[]{140, 141},15)));
        animations.put(gauche+ move, (new Animation(new int[]{144, 145}, 15)));
        animations.put(haut+ move, (new Animation(new int[]{142, 143}, 15)));
        animations.put(droite+ move, (new Animation(new int[]{144, 145}, 15)));

        // sans mouvement
        animations.put(bas+ idle, (new Animation(new int[]{136, 136}, 15)));
        animations.put(gauche+ idle, (new Animation(new int[]{138, 138}, 15)));
        animations.put(haut+ idle, (new Animation(new int[]{137, 137}, 15)));
        animations.put(droite+ idle, (new Animation(new int[]{138, 138}, 15)));
    }

    // draws
    public void drawCircleAttack()
    {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(circleAttack.x, circleAttack.y, circleAttack.radius);
        shapeRenderer.end();
    }

    public void drawPossibleDestinations()
    {
        for (Map.Entry<Circle, String> position : possibleDestinations.entrySet())
        {
            // dessin des destinations possibles
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            if (Objects.equals(position.getValue(), "disponible"))
            {
                shapeRenderer.setColor(Color.BLUE);
            }
            else
            {
                shapeRenderer.setColor(Color.YELLOW);
            }

            shapeRenderer.circle(position.getKey().x, position.getKey().y, 1);
            shapeRenderer.end();
        }
    }

    public void checkCollisionWithCircleAttack()
    {
        if (Intersector.overlaps(circleAttack, currentLevel.player.rect))
        {
            status = PURSUING;
        }
    }

    public void lineOfSight()
    {

    }

    public void pursuing()
    {

    }

    // updates
    public void updatePossibleDestinations()
    {
        for (Map.Entry<Circle, String> element : possibleDestinations.entrySet())
        {
            for (Rectangle rect : collisionsStop)
            {
                if (Intersector.overlaps(element.getKey(), rect))
                {
                    possibleDestinations.put(element.getKey(), "no disponible");
                }
                else
                {
                    possibleDestinations.put(element.getKey(), "disponible");
                }
            }

        }

    }

    public int getDirection(Vector2 targetPosition, Vector2 startPosition)
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

    public int fromToVector(Vector2 fromPosition, Vector2 toPosition)
    {
        return getDirection(new Vector2(toPosition.x - fromPosition.x, toPosition.y - fromPosition.y), position);
    }

    public void updateDirection()
    {
        if (status==PURSUING)
        {
            direction = fromToVector(position, currentLevel.player.position);
            moving = true;
        }
    }

    @Override
    public void update()
    {
        // mise à jour du temps actuel en millisecondes
        currentTime = TimeUtils.millis();

        // vérifier collision entre joueur et cercle
        checkCollisionWithCircleAttack();

        // mise à jour des destinations possibles
        updatePossibleDestinations();

        // updateDirection
        updateDirection();

        // mettre à jour la position du cercle
        circleAttack.setPosition((position.x+ (float) width /2), (position.y+ (float) height /2));
        circleAttack.setRadius(50);
    }
}
