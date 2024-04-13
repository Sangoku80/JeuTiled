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
import com.mygdx.game.Game;
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

    // mouvements
    protected float speed;
    protected boolean left, up, right, down;

    // animations
    protected String spriteSheetPath;
    protected ArrayList<TextureRegion> spriteSheet = new ArrayList<>();
    protected HashMap<String, Animation> animations = new HashMap<>();
    public Animation currentAnimation;
    protected String orientation = "bas";
    protected Boolean moving = false;

    // affichage
    protected int layerBas;
    protected int layerHaut;

    // other collisions
    protected ArrayList<Rectangle> collisionsStop = new ArrayList<>();
    public static HashMap<Rectangle, String> collisionsTeleportation = new HashMap<>();
    public static ArrayList<Rectangle> collisionsEntitiesHaut = new ArrayList<>();
    public static ArrayList<Rectangle> collisionsEntitiesBas = new ArrayList<>();

    public Character(String name, Vector2 position, float speed, float health, float attack, World world)
    {
        super(name, position, world);

        // caractéristiques
        this.health = health;
        this.initHealth = health;
        this.speed = speed;
        this.attack = attack;

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

    // personnages qui attaquent
    public Character(String name, Vector2 position, float speed, float health, World world)
    {
        super(name, position, world);

        // caractéristiques
        this.health = health;
        this.initHealth = health;
        this.speed = speed;

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
                                collisionsTeleportation.put(((RectangleMapObject) object).getRectangle(), (String) tiledMap.getLayers().get("teleportation").getObjects().get(entity.getName()).getProperties().get("destination"));

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
    public void updatePos() {
        float xSpeed = 0, ySpeed = 0;

        // vérifier s'il n'y a aucun mouvement
        if (!left && !right && !up && !down) {
            moving = false;
        } else {

            moving = true;

            // bouger en fonction de la direction
            if (left && !right) {
                xSpeed -= speed;
                orientation = "gauche";

            } else if (right && !left) {
                xSpeed = speed;
                orientation = "droite";
            }

            if (up && !down) {
                ySpeed = speed;
                orientation = "haut";

            } else if (down && !up) {

                ySpeed = -speed;
                orientation = "bas";
            }
            if (right && up) {
                orientation = "haut droite";
            } else if (right && down) {
                orientation = "bas droite";
            }
            if (left && up) {
                orientation = "haut gauche";
            } else if (left && down) {
                orientation = "bas gauche";
            }
        }

        // vérifier les collisions
        if (!checkCollisionsWithFoot(new Vector2(collisionBas.x + xSpeed, collisionBas.y + ySpeed))) {
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
            if (animations.containsValue(animations.get(orientation)))
            {
                currentAnimation = animations.get(orientation);
            }
        } else {
            if (animations.containsValue(animations.get(orientation + "_idle")))
            {
                currentAnimation = animations.get(orientation + "_idle");
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
        updatePos();
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
