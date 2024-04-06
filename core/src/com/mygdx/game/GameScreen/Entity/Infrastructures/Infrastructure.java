package com.mygdx.game.GameScreen.Entity.Infrastructures;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Entity;
import com.mygdx.game.GameScreen.Worlds.World;

public class Infrastructure extends Entity {

    // self collisions
    public RectangleMapObject entityTeleportation;
    public Rectangle collisionTeleportation;

    public Infrastructure(String name, Vector2 position, World world) {
        super(name, position, world);

    }
}
