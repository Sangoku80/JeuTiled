package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import jdk.tools.jmod.Main;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Entity extends Sprite {

    // mouvements et collisions
    protected TextureMapObject object;
    protected float x, y;
    protected int width, height;
    protected boolean left, up, right, down;
    protected float speed;
    protected Rectangle collisionRect;

    // animations
    protected String spriteSheetPath;
    protected ArrayList<TextureRegion> spriteSheet = new ArrayList<>();
    protected int ratioSpriteSheetX, ratioSpriteSheetY;
    protected HashMap<String, Animation> animations = new HashMap<>();
    protected Animation currentAnimation;

    // affichage
    protected TiledMapTileLayer layer;


    public Entity(TextureMapObject object, float speed, String spriteSheetPath, int ratioSpriteSheetX, int ratioSpriteSheetY)
    {
       // mouvements et collisions
       this.object = object;
       this.x = object.getX();
       this.y = object.getY();
       this.width = object.getTextureRegion().getRegionWidth();
       this.height = object.getTextureRegion().getRegionHeight();
       this.collisionRect = new Rectangle(x, y, width, height);
       this.speed = speed;

       // animations
       this.spriteSheetPath = spriteSheetPath;
       this.ratioSpriteSheetX = ratioSpriteSheetX;
       this.ratioSpriteSheetY = ratioSpriteSheetY;
       loadSpriteSheet();
       loadAnimations();
       currentAnimation = animations.get("marche face");

        // affichage
/*        layer = (TiledMapTileLayer) Game.tiledMap.getLayers().get("entités");*/

    }

    public void loadSpriteSheet()
    {
        Texture img = new Texture(spriteSheetPath);

        for(int y = 0; y < img.getHeight()/ratioSpriteSheetY; y++)
        {
            for(int x = 0; x < img.getWidth()/ratioSpriteSheetX; x++)
            {
                spriteSheet.add(new TextureRegion(img, x*ratioSpriteSheetX, y*ratioSpriteSheetY, ratioSpriteSheetX, ratioSpriteSheetY));
            }
        }
    }

    public abstract void loadAnimations();

    public abstract void updateDirections();

    public void updatePos()
    {

        float xSpeed = 0, ySpeed = 0;

        if(left && !right)
        {
            xSpeed -= speed;
        }

        else if(right && !left)
        {
            xSpeed = speed;
        }

        if(up && !down)
        {
            ySpeed = speed;
        }

        else if(down && !up)
        {
            ySpeed = -speed;
        }

        x += xSpeed;
        y += ySpeed;
    }

    public void Draw(SpriteBatch batch)
    {
        // mise à jour
        updateDirections();
        updatePos();
        System.out.println("right: " + right + " left: "+ left + " up: " + up + " down: " + down);

        // mise à jour de l'affichage
        currentAnimation.animate();
        batch.draw(currentAnimation.currentFrame, x, y);
    }

    public void resetDirBooleans()
    {
        left = false;
        right = false;
        up = false;
        down = false;
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
