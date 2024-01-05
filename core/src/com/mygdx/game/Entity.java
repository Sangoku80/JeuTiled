package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public abstract class Entity extends Sprite {

    protected TextureMapObject object;
    protected float x, y;
    protected int width, height;
    protected float speed;

    public Entity(TextureMapObject object, float speed)
    {
        super(object.getTextureRegion());
       this.object = object;
       this.x = object.getX();
       this.y = object.getY();
       this.width = object.getTextureRegion().getRegionWidth();
       this.height = object.getTextureRegion().getRegionHeight();
       this.speed = speed;
    }

    public abstract void Update();

    public void Draw(SpriteBatch batch)
    {
        Update();
        this.draw(batch);
    }
}
