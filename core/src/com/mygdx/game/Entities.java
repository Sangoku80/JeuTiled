package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.Tools.Animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

abstract class Entity {

    // caractéristiques
    public String name;
    public Vector2 position;
    protected int width, height;
    protected float speed;

    // collisions
    protected TextureMapObject entity;
    protected RectangleMapObject entityBas;
    protected RectangleMapObject entityHaut;
    protected RectangleMapObject entityTeleportation; // pour les infrastructures
    protected Rectangle collisionBas;
    protected Rectangle collisionHaut;
    protected Rectangle collisionTeleportation; // pour les infrastructures

    // mouvements
    protected boolean left, up, right, down;

    // carte actuelle
    protected TiledMap tiledMap;

    // fichier qui stocke les collisions des entités
    protected TmxMapLoader mapLoader = new TmxMapLoader();
    protected TiledMap collisionsEntities;

    // collisions
    protected ArrayList<Rectangle> collisionsStop = new ArrayList<>();
    protected ArrayList<Rectangle> collisionsTeleportation = new ArrayList<>();

    // collisions avec les entités et effet de profondeur
    protected ArrayList<Rectangle> collisionsEntitiesHaut = new ArrayList<>();
    protected ArrayList<Rectangle> collisionsEntitiesBas = new ArrayList<>();

    // animations
    protected String spriteSheetPath;
    protected ArrayList<TextureRegion> spriteSheet = new ArrayList<>();
    protected HashMap<String, Animation> animations = new HashMap<>();
    protected Animation currentAnimation;
    protected String orientation = "bas";
    protected Boolean moving = false;

    // affichage
    public int layer;
    protected int layerBas;
    protected int layerHaut;

    // monde dans lequel est notre entité
    protected World currentWorld;

    public Entity(String name, Vector2 position, float speed, World world)
    {
        // monde actuel
        this.currentWorld = world;

        // mouvements et collisions
        this.name = name;
        this.collisionsEntities = mapLoader.load("maps/entités.tmx");
        this.tiledMap = world.tiledMap;
        this.entity = (TextureMapObject) collisionsEntities.getLayers().get("entités").getObjects().get(name);
        this.entityBas = (RectangleMapObject) collisionsEntities.getLayers().get("entités_bas").getObjects().get(name);
        this.entityHaut = (RectangleMapObject) collisionsEntities.getLayers().get("entités_haut").getObjects().get(name);
        this.width = entity.getTextureRegion().getRegionWidth();
        this.height = entity.getTextureRegion().getRegionHeight();
        this.position = new Vector2(position.x, position.y);
        this.collisionBas = new Rectangle();
        this.collisionHaut = new Rectangle();
        this.speed = speed;

        // pour les infrastructures
        if (this instanceof Infrastructure)
        {
            this.entityTeleportation = (RectangleMapObject) collisionsEntities.getLayers().get("teleportation").getObjects().get(name);
            this.collisionTeleportation = new Rectangle();
        }

        // animations
        this.spriteSheetPath = entity.getTextureRegion().getTexture().toString();

        // affichage
        this.layer = currentWorld.entitiesLayer;
        this.layerBas = layer-1;
        this.layerHaut = layer;

        // chargement
        loadCollisionsBasHautTeleportation();
        loadCollisionsEntitiesBasHaut();
        loadCollisions();
        loadSpriteSheet();
        loadAnimations();
    }

    public void loadSpriteSheet()
    {
        Texture img = new Texture(spriteSheetPath);

        for(int y = 0; y < img.getHeight()/height; y++)
        {
            for(int x = 0; x < img.getWidth()/width; x++)
            {
                spriteSheet.add(new TextureRegion(img, x*height, y*width, width, height));
            }
        }
    }

    public void loadCollisionsEntitiesBasHaut()
    {
        for (Entity entity : currentWorld.entities)
        {
            if (!Objects.equals(entity.name, name))
            {
                collisionsEntitiesHaut.add(entity.collisionHaut);
                collisionsEntitiesBas.add(entity.collisionBas);

                if (entity instanceof Infrastructure)
                {
                    collisionsTeleportation.add(entity.collisionTeleportation);
                }
            }
        }
    }

    public void loadCollisions()
    {
        // dans la carte monde
        if (tiledMap.getLayers().get("collisions") != null)
        {
            if (tiledMap.getLayers().get("collisions").getObjects() != null)
            {
                for (Object object :tiledMap.getLayers().get("collisions").getObjects())
                {
                    if (object instanceof RectangleMapObject)
                    {
                        switch (((RectangleMapObject) object).getName())
                        {
                            case "teleportation":
                                collisionsTeleportation.add(((RectangleMapObject) object).getRectangle());

                            case "stop":
                                collisionsStop.add(((RectangleMapObject) object).getRectangle());
                        }
                    }
                }
            }
        }
    }


    public abstract void loadAnimations();

    public void loadCollisionsBasHautTeleportation()
    {

        ArrayList<RectangleMapObject> list = new ArrayList<>();
        list.add(entityBas);
        list.add(entityHaut);

        ArrayList<Rectangle> list2 = new ArrayList<>();
        list2.add(collisionBas);
        list2.add(collisionHaut);

        if (this instanceof Infrastructure)
        {
            list.add(entityTeleportation);
            list2.add(collisionTeleportation);
        }
        for (int i = 0; i < list.size(); i++)
        {
            float décalageX = Math.abs(entity.getX()-list.get(i).getRectangle().x);
            float décalageY = Math.abs(entity.getY()-list.get(i).getRectangle().y);

            list2.get(i).x = position.x + décalageX;
            list2.get(i).y = position.y + décalageY;
            list2.get(i).width = list.get(i).getRectangle().width;
            list2.get(i).height = list.get(i).getRectangle().height;

        }

    }

    public void drawCollisions(ShapeRenderer shapeRenderer)
    {

        ArrayList[] allCollisions = {collisionsStop, collisionsEntitiesBas, collisionsEntitiesHaut, collisionsTeleportation};

        shapeRenderer.setColor(0, 0, 1, 0);

        for (ArrayList collisions : allCollisions)
        {
            for (Object collision : collisions)
            {
                if (collision instanceof Rectangle)
                {
                    shapeRenderer.rect((((Rectangle) collision).getX()), ((Rectangle) collision).getY(), ((Rectangle) collision).getWidth(), ((Rectangle) collision).getHeight());
                }
            }
        }

    }

    public boolean checkCollisionsWithFoot(Vector2 position)
    {
        boolean answer = false;
        int i = 0;
        ArrayList[] allCollisions = {collisionsStop, collisionsEntitiesBas, collisionsTeleportation};

        for (ArrayList collisions : allCollisions)
        {
            i++;
            for (Object collision : collisions)
            {
                if (collision instanceof Rectangle)
                {
                    if (Intersector.overlaps(new Rectangle(position.x, position.y, collisionBas.width, collisionBas.height), (Rectangle) collision))
                    {
                        if (i != 3)
                        {
                            answer = true;
                        }
                        else
                        {
                            changeWorld(new InternMaison());
                        }
                    }

                }
            }
        }

        return answer;
    }

    public void updateLayer(Vector2 position)
    {
        layer = layerHaut;
        for (Rectangle collision : collisionsEntitiesHaut)
        {
            if((Intersector.overlaps(new Rectangle(position.x, position.y, collisionBas.width, collisionBas.height), collision)))
            {
                layer = layerBas;
            }
        }
    }

    public void changeWorld(World newWorld)
    {
        Game.currentLevel = newWorld;
    }

    public void updatePos()
    {
        float xSpeed = 0, ySpeed = 0;

        // vérifier s'il n'y a aucun mouvement
        if(!left && !right && !up && !down)
        {
            moving = false;
        }
        else {

            moving = true;

            // bouger en fonction de la direction
            if (left && !right) {
                xSpeed -= speed;
                orientation = "gauche";

            }
            else if (right && !left) {
                xSpeed = speed;
                orientation = "droite";
            }

            if (up && !down) {
                ySpeed = speed;
                orientation = "haut";

            }
            else if (down && !up) {

                ySpeed = -speed;
                orientation = "bas";
            }
            if (right && up)
            {
                orientation = "haut droite";
            }
            else if (right && down)
            {
                orientation = "bas droite";
            }
            if (left && up)
            {
                orientation = "haut gauche";
            }
            else if(left && down)
            {
                orientation = "bas gauche";
            }
        }

        // vérifier les collisions
        if (!checkCollisionsWithFoot(new Vector2(collisionBas.x + xSpeed, collisionBas.y + ySpeed)))
        {
            // entité
            position.x += xSpeed;
            position.y += ySpeed;

            // rectCollisions
            collisionBas.y += ySpeed;
            collisionBas.x += xSpeed;
        }
    }

    public abstract void updateAnimation();

    public void update()
    {
        // mise à jour
        if (this instanceof Personnage)
        {
            ((Personnage) this).updateDirections();
        }

        updatePos();
        updateAnimation();
        // updateWorld();
        updateLayer(new Vector2(collisionBas.x, collisionBas.y));
    }

    public void Draw(SpriteBatch batch)
    {
        // dessiner
        batch.draw(currentAnimation.animate(), position.x, position.y);
    }

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
}

class Player extends Entity implements InputProcessor, Personnage {

    public Player(float x, float y, World currentWorld) {
        super("player", new Vector2(x, y), 2f, currentWorld);

        // animation par défaut
        currentAnimation = animations.get("bas_idle");
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        // en mouvement
        animations.put("bas", (new Animation(new int[]{0, 3},15)));
        animations.put("bas droite", (new Animation(new int[]{56, 59}, 15)));
        animations.put("bas gauche", (new Animation(new int[]{8, 11}, 15)));
        animations.put("gauche", (new Animation(new int[]{16, 19}, 15)));
        animations.put("haut", (new Animation(new int[]{32, 35}, 15)));
        animations.put("haut droite", (new Animation(new int[]{40, 43},  15)));
        animations.put("haut gauche", (new Animation(new int[]{24, 27},  15)));
        animations.put("droite", (new Animation(new int[]{48, 51}, 15)));

        // sans mouvement
        animations.put("bas_idle", (new Animation(new int[]{0, 0}, 15)));
        animations.put("bas droite_idle", (new Animation(new int[]{56, 56}, 15)));
        animations.put("bas gauche_idle", (new Animation(new int[]{8, 8},  15)));
        animations.put("gauche_idle", (new Animation(new int[]{16, 16}, 15)));
        animations.put("haut_idle", (new Animation(new int[]{32, 32}, 15)));
        animations.put("haut droite_idle", (new Animation(new int[]{40, 40}, 15)));
        animations.put("haut gauche_idle", (new Animation(new int[]{24, 24}, 15)));
        animations.put("droite_idle", (new Animation(new int[]{48, 48}, 15)));
    }

    @Override
    public void updateDirections() {
    }

    @Override
    public void updateAnimation() {

        if (moving) {
            currentAnimation = animations.get(orientation);
        } else {
            currentAnimation = animations.get(orientation + "_idle");
        }
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.UP:
                setUp(true);
                break;

            case Input.Keys.DOWN:
                setDown(true);
                break;

            case Input.Keys.RIGHT:
                setRight(true);
                break;

            case Input.Keys.LEFT:
                setLeft(true);
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.UP:
                setUp(false);
                break;

            case Input.Keys.DOWN:
                setDown(false);
                break;

            case Input.Keys.RIGHT:
                setRight(false);
                break;

            case Input.Keys.LEFT:
                setLeft(false);
                break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}

class Vache extends Entity implements Personnage {

    public Vache(float x, float y, World currentWorld)
    {
        super("vache", new Vector2(x, y), 2f, currentWorld);

        // animation par défaut
        currentAnimation = animations.get("bas");
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        animations.put("bas", (new Animation(new int[]{91, 93}, 15)));
        animations.put("haut", (new Animation(new int[]{115, 117}, 15)));
        animations.put("droite", (new Animation(new int[]{87, 89}, 15)));
    }

    @Override
    public void updateDirections() {

    }

    @Override
    public void updateAnimation() {

    }
}

class Cochon extends Entity implements Personnage {

    public Cochon(int x, int y, World currentWorld)
    {
        super("cochon", new Vector2(x, y), 2f, currentWorld);

        // animation par défaut
        currentAnimation = animations.get("gauche");
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        animations.put("gauche", (new Animation(new int[]{87, 89}, 15)));
        animations.put("haut", (new Animation(new int[]{111, 113}, 15)));
        animations.put("bas", (new Animation(new int[]{135, 136}, 15)));
    }

    @Override
    public void updateDirections() {

    }

    @Override
    public void updateAnimation() {

    }
}

class Maison extends Entity implements Infrastructure {

    public Maison(float x, float y, World world)
    {
        super("maison", new Vector2(x, y), 0f, world);

        // animation par défaut
        currentAnimation = animations.get("idle");
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        animations.put("idle",  new Animation(new int[]{0, 0}, 15));
    }

    @Override
    public void updateAnimation() {

    }
}