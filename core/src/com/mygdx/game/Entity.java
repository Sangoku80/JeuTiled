package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public abstract class Entity {

    // caractéristiques
    protected String name;

    // mouvements et collisions
    protected TextureMapObject entity;
    protected RectangleMapObject entityBas;
    protected RectangleMapObject entityHaut;
    protected float x, y;
    protected int width, height;
    protected boolean left, up, right, down;
    protected float speed;
    protected Rectangle collisionBas;
    protected Rectangle collisionHaut;

    // carte actuelle
    protected TiledMap tiledMap;

    // collisions avec le décor
    protected HashMap<String, Rectangle> collisions = new HashMap<>();

    // collisions avec les entités et effet de profondeur
    protected HashMap<String, Rectangle> collisionsEntitiesHaut = new HashMap<>();
    protected HashMap<String, Rectangle> collisionsEntitiesBas = new HashMap<>();

    // animations
    protected String spriteSheetPath;
    protected ArrayList<TextureRegion> spriteSheet = new ArrayList<>();
    protected HashMap<String, Animation> animations = new HashMap<>();
    protected Animation currentAnimation;
    protected String orientation = "bas";
    protected Boolean moving = false;

    // affichage
    protected int layer;
    protected int layerBas;
    protected int layerHaut;

    // monde dans lequel est notre entité
    protected World currentWorld;

    public Entity(String name, float x, float y, float speed, int layer, World world)
    {
        // mouvements et collisions
        this.name = name;
        this.tiledMap = Game.tiledMap;
        this.entity = (TextureMapObject) tiledMap.getLayers().get("entités").getObjects().get(name);
        this.entityBas = (RectangleMapObject) tiledMap.getLayers().get("entités_bas").getObjects().get(name);
        this.entityHaut = (RectangleMapObject) tiledMap.getLayers().get("entités_haut").getObjects().get(name);
        this.width = entity.getTextureRegion().getRegionWidth();
        this.height = entity.getTextureRegion().getRegionHeight();
        this.x = x;
        this.y = y;
        this.collisionBas = new Rectangle();
        this.collisionHaut = new Rectangle();
        this.speed = speed;

        // animations
        this.spriteSheetPath = entity.getTextureRegion().getTexture().toString();

        // monde actuel
        this.currentWorld = world;

        // affichage
        this.layer = layer;
        this.layerBas = layer-1;
        this.layerHaut = layer;

        // chargement
        loadCollisionsBasHaut();
        loadCollisionsEntitiesBasHaut();
        loadCollisionsWithDecor();
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

        int test = 0;

        for (Entity entity : currentWorld.entities)
        {
            test++;
            if (!Objects.equals(entity.name, name))
            {
                collisionsEntitiesHaut.put(entity.name + test, entity.collisionHaut);
                collisionsEntitiesBas.put(entity.name + test, entity.collisionBas);
            }
        }
    }

    public void loadCollisionsWithDecor()
    {
        int test = 0;

        for(MapObject collision : tiledMap.getLayers().get("collisions").getObjects())
        {
            test++;

            if (collision instanceof RectangleMapObject)
            {
                collisions.put(collision.getName()+test, ((RectangleMapObject) collision).getRectangle());
            }
        }
    }

    public abstract void loadAnimations();

    public void loadCollisionsBasHaut()
    {
        RectangleMapObject[] list = {entityBas, entityHaut};
        Rectangle[] list2 = {collisionBas, collisionHaut};

        for (int i = 0; i < 2; i++)
        {
            float décalageX = Math.abs(entity.getX()-list[i].getRectangle().x);
            float décalageY = Math.abs(entity.getY()-list[i].getRectangle().y);


            list2[i].x = x + décalageX;
            list2[i].y = y + décalageY;
            list2[i].width = list[i].getRectangle().width;
            list2[i].height = list[i].getRectangle().height;


        }

    }

    public void drawCollisions(ShapeRenderer shapeRenderer)
    {

        HashMap[] allCollisions = {collisions, collisionsEntitiesBas, collisionsEntitiesHaut};

        shapeRenderer.setColor(0, 0, 1, 0);

        for (HashMap collisions : allCollisions)
        {
            for (Object collision : collisions.values())
            {
                if (collision instanceof Rectangle)
                {
                    shapeRenderer.rect((((Rectangle) collision).getX()), ((Rectangle) collision).getY(), ((Rectangle) collision).getWidth(), ((Rectangle) collision).getHeight());
                }
            }
        }

    }

    public abstract void updateDirections();

    public boolean checkCollisionsWithFoot(Vector2 position)
    {

        boolean answer = false;

        HashMap[] allCollisions = {collisions, collisionsEntitiesBas};


        for (HashMap collisions : allCollisions)
        {
            for (Object collision : collisions.values())
            {
                if (collision instanceof Rectangle)
                {
                    if (Intersector.overlaps(new Rectangle(position.x, position.y, collisionBas.width, collisionBas.height), (Rectangle) collision))
                    {
                        answer = true;
                    }

                }
            }
        }

        return answer;
    }

    public void updateLayer(Vector2 position)
    {
        layer = layerHaut;
        for (Rectangle collision : collisionsEntitiesHaut.values())
        {
            if((Intersector.overlaps(new Rectangle(position.x, position.y, collisionBas.width, collisionBas.height), collision)))
            {
                layer = layerBas;
            }
        }
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
            x += xSpeed;
            y += ySpeed;

            // rectCollisions
            collisionBas.y += ySpeed;
            collisionBas.x += xSpeed;
        }
    }

    public abstract void updateAnimation();

    public void update()
    {
        // mise à jour
        updateDirections();
        updatePos();
        updateAnimation();
        updateLayer(new Vector2(collisionBas.x, collisionBas.y));
        System.out.println(layer);
    }

    public void Draw(SpriteBatch batch)
    {
        // dessiner
        batch.draw(currentAnimation.animate(), x, y);
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