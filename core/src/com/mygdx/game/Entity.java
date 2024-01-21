package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
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
    protected RectangleMapObject entityFoot;
    protected float x, y;
    protected int width, height;
    protected boolean left, up, right, down;
    protected float speed;
    protected Rectangle collisionBody;
    protected Rectangle collisionFoot;

    // carte actuelle
    protected TiledMap tiledMap;

    // animations
    protected String spriteSheetPath;
    protected ArrayList<TextureRegion> spriteSheet = new ArrayList<>();
    protected HashMap<String, Animation> animations = new HashMap<>();
    protected Animation currentAnimation;
    protected String orientation = "bas";
    protected Boolean moving = false;

    public Entity(String name, float x, float y, float xFoot, float yFoot, float speed)
    {
        // mouvements et collisions
        this.name = name;
        this.tiledMap = Game.tiledMap;
        this.entity = (TextureMapObject) tiledMap.getLayers().get("entités").getObjects().get(name);
        this.entityFoot = (RectangleMapObject) tiledMap.getLayers().get("entités_foot").getObjects().get(name);
        this.x = x;
        this.y = y;
        this.width = entity.getTextureRegion().getRegionWidth();
        this.height = entity.getTextureRegion().getRegionHeight();
        this.speed = speed;
        this.collisionBody = new Rectangle(x, y, width, height);
        this.collisionFoot = new Rectangle(xFoot, yFoot, entityFoot.getRectangle().width, entityFoot.getRectangle().height);

        // animations
        this.spriteSheetPath = entity.getTextureRegion().getTexture().toString();

        // chargement
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

    public abstract void loadAnimations();

    public abstract void updateDirections();

    public boolean checkCollisionsWithFoot(World world, Vector2 position)
    {

        boolean answer = false;

        for (Rectangle rectCollision : world.collisions)
        {
            if (Intersector.overlaps(new Rectangle(position.x, position.y, collisionFoot.width, collisionFoot.height), rectCollision))
            {
                answer = true;
            }
        }

        return answer;
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
        if (!checkCollisionsWithFoot(Game.level1, new Vector2(collisionFoot.x + xSpeed, collisionFoot.y + ySpeed)))
        {
            // entité
            x += xSpeed;
            y += ySpeed;

            // rectCollisions
            collisionFoot.y += ySpeed;
            collisionFoot.x += xSpeed;
        }
    }

    public abstract void updateAnimation();

    public void Draw(SpriteBatch batch)
    {
        // mise à jour
        updateDirections();
        updatePos();
        updateAnimation();

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