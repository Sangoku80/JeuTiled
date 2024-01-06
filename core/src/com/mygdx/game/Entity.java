package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Entity extends Sprite {

    // mouvements et collisions
    protected TextureMapObject object;
    protected float x, y;
    protected int width, height;
    protected float speed;
    protected Rectangle collisionRect;

    // animations
    protected String spriteSheetPath;
    protected ArrayList<TextureRegion> spriteSheet = new ArrayList<>();
    protected int ratioSpriteSheetX, ratioSpriteSheetY;
    protected TextureRegion[][] animations;
    protected TextureRegion currentAnimationFrame;

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

    public abstract void Update();

    public void Draw(SpriteBatch batch)
    {
        Update();
        currentAnimationFrame = spriteSheet.get(21);
        batch.draw(currentAnimationFrame, object.getX(), object.getY());
    }
}
