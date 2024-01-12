package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Entity extends Sprite {

    // mouvements et collisions
    protected TextureMapObject entity;
    protected float x, y;
    protected int width, height;
    protected boolean left, up, right, down;
    protected float speed;
    protected Rectangle collisionBody;
    protected Rectangle collisionFoot;

    // animations
    protected String spriteSheetPath;
    protected ArrayList<TextureRegion> spriteSheet = new ArrayList<>();
    protected int ratioSpriteSheetX, ratioSpriteSheetY;
    protected HashMap<String, Animation> animations = new HashMap<>();
    protected Animation currentAnimation;

    public Entity(TextureMapObject entity, RectangleMapObject foot, float speed, String spriteSheetPath, int ratioSpriteSheetX, int ratioSpriteSheetY)
    {
       // mouvements et collisions
       this.entity = entity;
       this.x = entity.getX();
       this.y = entity.getY();
       this.width = entity.getTextureRegion().getRegionWidth();
       this.height = entity.getTextureRegion().getRegionHeight();
       this.speed = speed;
       this.collisionBody = new Rectangle(x, y, width, height);
       this.collisionFoot = foot.getRectangle();

       // animations
       this.spriteSheetPath = spriteSheetPath;
       this.ratioSpriteSheetX = ratioSpriteSheetX;
       this.ratioSpriteSheetY = ratioSpriteSheetY;
       loadSpriteSheet();
       loadAnimations();
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

        collisionFoot.x += xSpeed;
        collisionFoot.y += ySpeed;

        collisionBody.y -= ySpeed;
        collisionBody.x -= xSpeed;
    }

    public void Draw(SpriteBatch batch)
    {
        // mise à jour
        updateDirections();
        updatePos();

        entity.setTextureRegion(currentAnimation.animate());
        batch.draw(entity.getTextureRegion(), x, y);
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
