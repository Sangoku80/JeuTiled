package com.mygdx.game.GameScreen.Entity.Characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Ennemies.Enemy;
import com.mygdx.game.GameScreen.Entity.Entity;
import com.mygdx.game.GameScreen.Worlds.InternMaison;
import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

import java.util.*;

import static com.mygdx.game.Game.*;

public abstract class Character extends Entity {

    // caractéristiques
    public float health;
    public float initHealth;
    public float attack;
    public Circle circleAttack = new Circle();
    protected Boolean heightDirections;

    // mouvements
    protected float speed;
    protected boolean left, up, right, down;

    // animations
    protected String spriteSheetPath;
    protected ArrayList<TextureRegion> spriteSheet = new ArrayList<>();
    protected HashMap<String, Animation> animations = new HashMap<>();
    public Animation currentAnimation;
    protected String bas="bas", haut="haut", gauche="gauche", droite="droite", bas_gauche="bas_gauche", bas_droite="bas_droite", haut_gauche="haut_gauche", haut_droite="haut_droite";
    protected String orientation = bas;
    protected Boolean moving = false;
    protected String move ="_move", idle ="_idle";

    // directions
    public static int DOWN=270, UP=90, LEFT=180, RIGHT=0, DOWN_LEFT=225, DOWN_RIGHT=315, UP_LEFT=135, UP_RIGHT=45;
    protected int direction = DOWN;


    // affichage
    protected int layerBas;
    protected int layerHaut;

    // other collisions
    protected ArrayList<Rectangle> collisionsStop = new ArrayList<>();
    public static HashMap<Rectangle, String> collisionsTeleportation = new HashMap<>();
    public static ArrayList<Rectangle> collisionsEntitiesHaut = new ArrayList<>();
    public static ArrayList<Rectangle> collisionsEntitiesBas = new ArrayList<>();

    public Character(String name, Vector2 position, float speed, float health, float attack, World world, Boolean heightDirections)
    {
        super(name, position, world);

        // caractéristiques
        this.health = health;
        this.initHealth = health;
        this.speed = speed;
        this.attack = attack;
        this.heightDirections = heightDirections;

        // animations
        this.spriteSheetPath = entity.getTextureRegion().getTexture().toString();

        // affichage
        this.layerBas = layer-1;
        this.layerHaut = layer;

        // chargement
        loadCollisions();
        loadSpriteSheet();
        loadAnimations();
    }

    // personnages qui attaquent
    public Character(String name, Vector2 position, float speed, float health, World world, Boolean heightDirections)
    {
        super(name, position, world);

        // caractéristiques
        this.health = health;
        this.initHealth = health;
        this.speed = speed;
        this.heightDirections = heightDirections;

        // animations
        this.spriteSheetPath = entity.getTextureRegion().getTexture().toString();

        // affichage
        this.layerBas = layer - 1;
        this.layerHaut = layer;

        // chargement
        loadCollisions();
        loadSpriteSheet();
        loadAnimations();
    }

    // chargement
    public void loadSpriteSheet() {
        Texture img = new Texture(spriteSheetPath);

        for (int y = 0; y < img.getHeight() / height; y++) {
            for (int x = 0; x < img.getWidth() / width; x++) {
                spriteSheet.add(new TextureRegion(img, x * height, y * width, width, height));
            }
        }
    }

    public void loadCollisions() {
        // dans la carte monde
        if (tiledMap.getLayers().get("collisions") != null) {
            if (tiledMap.getLayers().get("collisions").getObjects() != null) {
                for (Object object : tiledMap.getLayers().get("collisions").getObjects()) {
                    if (object instanceof RectangleMapObject) {
                        switch (((RectangleMapObject) object).getName()) {
                            case "teleportation":
                                collisionsTeleportation.put(((RectangleMapObject) object).getRectangle(), (String) ((RectangleMapObject) object).getProperties().get("destination"));

                            case "stop":
                                collisionsStop.add(((RectangleMapObject) object).getRectangle());
                        }
                    }
                }
            }
        }
    }

    public abstract void loadAnimations();

    // collisions
    public boolean checkCollisionsWithFoot(Vector2 position) {
        boolean answer = false;
        ArrayList[] allCollisions = {collisionsStop, collisionsEntitiesBas};

        for (ArrayList collisions : allCollisions) {
            for (Object collision : collisions) {
                if (collision instanceof Rectangle)
                {
                    if (((Rectangle) collision).x != collisionBas.x && ((Rectangle) collision).y != collisionBas.y && ((Rectangle) collision).width != collisionBas.width && ((Rectangle) collision).height != collisionBas.height)
                    {
                        if (Intersector.overlaps(new Rectangle(position.x, position.y, collisionBas.width, collisionBas.height), (Rectangle) collision)) {
                            answer = true;
                        }
                    }
                }
            }
        }

        for (Map.Entry<Rectangle, String> collisionTeleportation : collisionsTeleportation.entrySet()) {
            if (Intersector.overlaps(new Rectangle(position.x, position.y, collisionBas.width, collisionBas.height), collisionTeleportation.getKey())) {
                switch (collisionTeleportation.getValue()) {
                    case "Maison1":
                        changeWorld(new InternMaison());
                        break;
                }
            }
        }

        return answer;
    }

    // pour le changement de monde
    public void changeWorld(World newWorld) {
        currentLevel = newWorld;
    }

    // updates
    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    // updates
    public abstract void update();

    public void updateOrientation()
    {
        if (!heightDirections)
        {
            if (direction > DOWN && direction < UP)
            {
                orientation = droite;
            }
            else if (direction == UP)
            {
                orientation = haut;
            }
            else if (direction > UP && direction < DOWN)
            {
                orientation = gauche;
            }
            else if (direction == DOWN)
            {
                orientation = bas;
            }
        }
        else
        {
            if (direction == RIGHT)
            {
                orientation = droite;
            }
            else if (direction == LEFT)
            {
                orientation = gauche;
            }
            else if (direction == UP)
            {
                orientation = haut;
            }
            else if (direction == DOWN)
            {
                orientation = bas;
            }
            else if (direction == DOWN_LEFT)
            {
                orientation = bas_gauche;
            }
            else if (direction == DOWN_RIGHT)
            {
                orientation = bas_droite;
            }
            else if (direction == UP_LEFT)
            {
                orientation = haut_gauche;
            }
            else if (direction == UP_RIGHT)
            {
                orientation = haut_droite;
            }
        }
    }

    public void updateDirection()
    {
        // vérifier s'il n'y a aucun mouvement
        if (!left && !right && !up && !down) {
            moving = false;
        } else {

            moving = true;

            // bouger en fonction de la direction
            if (left && !right) {
                direction = LEFT;

            } else if (right && !left) {
                direction = RIGHT;
            }

            if (up && !down) {
                direction = UP;

            } else if (down && !up) {
                direction = DOWN;
            }
            if (right && up) {
                direction = UP_RIGHT;
            } else if (right && down) {
                direction = DOWN_RIGHT;
            }
            if (left && up) {
                direction = UP_LEFT;
            } else if (left && down) {
                direction = DOWN_LEFT;
            }
        }
    }
    public void updatePos() {
        float xSpeed, ySpeed;

        xSpeed = (float) (speed*Math.cos(Math.toRadians(direction)));
        ySpeed = (float) (speed*Math.sin(Math.toRadians(direction)));

        // vérifier les collisions
        if (!checkCollisionsWithFoot(new Vector2(collisionBas.x + xSpeed, collisionBas.y + ySpeed)) && moving) {
            // entité
            position.x += xSpeed;
            position.y += ySpeed;

            // rectCollisions
            collisionBas.x += xSpeed;
            collisionBas.y += ySpeed;

            // rect
            rect.x += xSpeed;
            rect.y += ySpeed;
        }
    }

    public void updateLayer(Vector2 position) {
        layer = layerHaut;
        for (Rectangle collision : collisionsEntitiesHaut) {
            if (collision.x != collisionHaut.x && collision.y != collisionHaut.y && collision.width != collisionHaut.width && collision.height != collisionHaut.height)
            {
                if ((Intersector.overlaps(new Rectangle(position.x, position.y, collisionBas.width, collisionBas.height), collision))) {
                    layer = layerBas;
                }
            }
        }

    }

    public void updateAnimation()
    {
        if (moving) {
            if (animations.containsValue(animations.get(orientation+ move)))
            {
                currentAnimation = animations.get(orientation+ move);
            }
        } else {
            if (animations.containsValue(animations.get(orientation+ idle)))
            {
                currentAnimation = animations.get(orientation+ idle);
            }
        }
    }

    public void updateCircleAttack()
    {
        // cercle d'attaque
        circleAttack.setPosition((position.x+ (float) width /2), (position.y+ (float) height /2));
        circleAttack.setRadius(50);
    }

    public void updates() {
        // mise à jour
        update();
        updateDirection();
        updatePos();
        updateOrientation();
        updateAnimation();
        updateLayer(new Vector2(collisionBas.x, collisionBas.y));

        // action attack
        if (attack > 0)
        {
            updateCircleAttack();
        }
    }

    // draw
    public void drawHealthBar()
    {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(position.x+1, position.y+height+2, 15, 2);
        shapeRenderer.setColor(Color.GREEN);

        if(this instanceof Enemy)
        {
            shapeRenderer.setColor(Color.RED);
        }

        shapeRenderer.rect(position.x+1, position.y+height+2, 15*(health/initHealth), 2);

        shapeRenderer.end();
    }

    public void drawCircleAttack()
    {
        // dessin des collisions
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.circle(circleAttack.x, circleAttack.y, circleAttack.radius);
        shapeRenderer.end();
    }

    public void drawCollisions() {

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        ArrayList[] allCollisions = {collisionsStop, collisionsEntitiesBas, collisionsEntitiesHaut};

        shapeRenderer.setColor(0, 0, 1, 0);

        for (ArrayList collisions : allCollisions) {
            for (Object collision : collisions) {
                if (collision instanceof Rectangle) {
                    shapeRenderer.rect((((Rectangle) collision).getX()), ((Rectangle) collision).getY(), ((Rectangle) collision).getWidth(), ((Rectangle) collision).getHeight());
                }
            }
        }

        shapeRenderer.end();
    }

    // actions
    public void attack()
    {
        // utilisation d'un itérateur pour parcourir la liste
        Iterator<Entity> iterator = currentWorld.entities.iterator();

        while (iterator.hasNext())
        {
            Entity entity = iterator.next();

            if (Intersector.overlaps(circleAttack, entity.rect))
            {
                if (entity instanceof Character && ((Character) entity).health>0 && !Objects.equals(entity.name, "player"))
                {
                    ((Character) entity).health -= attack;

                    // enlever le perso s'il a plus de vie
                    if (((Character) entity).health <= 0)
                    {
                        iterator.remove();
                    }
                }

            }
        }


    }

}
