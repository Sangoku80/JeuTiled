package com.mygdx.game.GameScreen.Entity.Characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
    public Circle circleDetection = new Circle();
    protected Boolean heightDirections;
    float radiusCircleDetection = 5;

    // mouvements
    protected float speed;

    // animations
    protected String spriteSheetPath;
    protected ArrayList<TextureRegion> spriteSheet = new ArrayList<>();
    protected HashMap<String, Animation> animations = new HashMap<>();
    public Animation currentAnimation;
    protected String bas="bas", haut="haut", gauche="gauche", droite="droite", bas_gauche="bas_gauche", bas_droite="bas_droite", haut_gauche="haut_gauche", haut_droite="haut_droite";
    public String orientation = bas;
    public Boolean moving = false;
    protected String move ="_move", idle ="_idle";

    // directions
    public static int DOWN=270, UP=90, LEFT=180, RIGHT=0, DOWN_LEFT=225, DOWN_RIGHT=315, UP_LEFT=135, UP_RIGHT=45;
    public int direction = DOWN;

    public Character(String name, Vector2 position, float speed, float health, float attack, World world, Boolean heightDirections)
    {
        super(name, position, world);

        // caractéristiques
        this.health = health;
        this.initHealth = health;
        this.speed = speed;
        this.attack = attack;
        this.heightDirections = heightDirections;

        // world
        this.currentWorld = world;

        // animations
        this.spriteSheetPath = entity.getTextureRegion().getTexture().toString();

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

        // world
        this.currentWorld = world;

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
                                currentWorld.collisionsTeleportation.put(((RectangleMapObject) object).getRectangle(), (String) ((RectangleMapObject) object).getProperties().get("destination"));

                            case "stop":
                                currentWorld.collisionsStop.add(((RectangleMapObject) object).getRectangle());
                        }
                    }
                }
            }
        }

        // ajouter des bords à la map
        TiledMapTileLayer layer = currentWorld.layers.get(0);
        float mapWidth = layer.getWidth() * layer.getTileWidth();
        float mapHeight = layer.getHeight() * layer.getTileHeight();
        float borderThickness = 0.05f;

        currentWorld.collisionsStop.add(new Rectangle(layer.getOffsetX(), mapHeight, mapWidth, borderThickness));
        currentWorld.collisionsStop.add(new Rectangle(layer.getOffsetX(), layer.getOffsetY() - borderThickness, mapWidth, borderThickness));
        currentWorld.collisionsStop.add(new Rectangle(layer.getOffsetX() - borderThickness, layer.getOffsetY(), borderThickness, mapHeight));
        currentWorld.collisionsStop.add(new Rectangle(mapWidth, layer.getOffsetY(), borderThickness, mapHeight));
    }

    public abstract void loadAnimations();

    // collisions
    public boolean checkCollisionsWithFoot(Vector2 position) {
        boolean answer = false;

        for (ArrayList collisions : currentWorld.allCollisions) {
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

        String destination = "";

        for (Map.Entry<Rectangle, String> collisionTeleportation : currentWorld.collisionsTeleportation.entrySet()) {

            if (Intersector.overlaps(new Rectangle(position.x, position.y, collisionBas.width, collisionBas.height), collisionTeleportation.getKey()))
            {
                destination=collisionTeleportation.getValue();
                break;
            }
        }

        if (Objects.equals(this.name, "player"))
        {
            switch (destination) {
                case "Maison1":
                    changeWorld(new InternMaison());
                    break;
            }
        }

        return answer;
    }

    // pour le changement de monde
    public void changeWorld(World newWorld) {
        currentLevel = newWorld;
    }

    // updates
    public abstract void update();

    public void updateOrientation(float xSpeed, float ySpeed)
    {
        double angle = Math.toDegrees(Math.atan2(ySpeed, xSpeed));

        if (angle < 0) {
            angle += 360;
        }

        if (!heightDirections)
        {
            if ((angle >= 0 && angle < 45) || (angle >= 315 && angle < 360)) {
                orientation = droite;
            } else if (angle >= 45 && angle < 135) {
                orientation = haut;
            } else if (angle >= 135 && angle < 225) {
                orientation = gauche;
            } else if (angle >= 225 && angle < 315) {
                orientation = bas;
            }
        }
        else
        {
            if ((angle >= 0 && angle < 22.5) || (angle >= 337.5 && angle < 360)) {
                orientation = droite;
            } else if (angle >= 22.5 && angle < 67.5) {
                orientation = haut_droite;
            } else if (angle >= 67.5 && angle < 112.5) {
                orientation = haut;
            } else if (angle >= 112.5 && angle < 157.5) {
                orientation = haut_gauche;
            } else if (angle >= 157.5 && angle < 202.5) {
                orientation = gauche;
            } else if (angle >= 202.5 && angle < 247.5) {
                orientation = bas_gauche;
            } else if (angle >= 247.5 && angle < 292.5) {
                orientation = bas;
            } else if (angle >= 292.5 && angle < 337.5) {
                orientation = bas_droite;
            }
        }


    }

    public void updatePos() {
        float xSpeed, ySpeed;

        xSpeed = (float) (speed * Math.cos(Math.toRadians(direction)));
        ySpeed = (float) (speed * Math.sin(Math.toRadians(direction)));

        updateOrientation(xSpeed, ySpeed);

        // Diviser le mouvement en plusieurs étapes
        float step = 1.0f; // Taille de l'étape pour vérifier la collision
        float remainingDistance = speed; // Distance totale à parcourir

        while (remainingDistance > 0) {
            float moveDistance = Math.min(remainingDistance, step);

            if (!checkCollisionsWithFoot(new Vector2((float) (collisionBas.x + moveDistance * Math.cos(Math.toRadians(direction))),
                    (float) (collisionBas.y + moveDistance * Math.sin(Math.toRadians(direction)))))
                    && moving) {
                // Déplacer l'entité et les collisions rectangulaires
                position.x += (float) (moveDistance * Math.cos(Math.toRadians(direction)));
                position.y += (float) (moveDistance * Math.sin(Math.toRadians(direction)));
                collisionBas.x += (float) (moveDistance * Math.cos(Math.toRadians(direction)));
                collisionBas.y += (float) (moveDistance * Math.sin(Math.toRadians(direction)));
                rect.x += (float) (moveDistance * Math.cos(Math.toRadians(direction)));
                rect.y += (float) (moveDistance * Math.sin(Math.toRadians(direction)));
            }

            remainingDistance -= moveDistance;
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

    public void updateCircleDetection()
    {
        // cercle d'attaque
        circleDetection.setPosition((position.x+ (float) width /2), (position.y+ (float) height /2));
        circleDetection.setRadius(radiusCircleDetection);
    }

    public void updates() {
        // mise à jour
        updateAnimation();
        updatePos();

        // action attack
        if (attack > 0)
        {
            updateCircleDetection();
        }

        // mises à jour personnelles
        update();
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

    public void drawCircleDetection()
    {
        // dessin des collisions
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.circle(circleDetection.x, circleDetection.y, circleDetection.radius);
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

            if (Intersector.overlaps(circleDetection, entity.rect))
            {
                if (entity instanceof Character && ((Character) entity).health>0 && !Objects.equals(entity.name, name))
                {
                    ((Character) entity).health -= attack;

                    // enlever le perso s'il a plus de vie
                    if (((Character) entity).health <= 0)
                    {
                        entity.deleteCollisionBas();
                        iterator.remove();
                    }
                }

            }
        }
    }

}
