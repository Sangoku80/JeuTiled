package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;

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
    protected HashMap<TextureRegion, Integer>  spriteSheet;
    protected int ratioSpriteSheet;
    protected TextureRegion[][] animations;

    public Entity(TextureMapObject object, float speed, String spriteSheetPath, int ratioSpriteSheet)
    {
       super(object.getTextureRegion());

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
       this.ratioSpriteSheet = ratioSpriteSheet;
    }

    public void loadSpriteSheet()
    {
        Texture img = new Texture(spriteSheetPath);

        for(int j = 0; j < img.getHeight()/ratioSpriteSheet; j++)
        {
            for (int i = 0; i < img.getWidth()/ratioSpriteSheet; i++)
            {
                spriteSheet.put(new TextureRegion(img, i*ratioSpriteSheet, j*ratioSpriteSheet, ratioSpriteSheet, ratioSpriteSheet), j*i);
            }
        }
    }

    public abstract void Update();

    public void Draw(SpriteBatch batch)
    {
        Update();
        batch.draw(object.getTextureRegion(), object.getX(), object.getY());
    }
}
