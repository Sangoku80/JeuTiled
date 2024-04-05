package com.mygdx.game.GameScreen.infrastructure;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity;
import com.mygdx.game.World;

public class Infrastructure extends Entity {

    // self collisions
    protected RectangleMapObject entityTeleportation;
    protected Rectangle collisionTeleportation;

    public Infrastructure(String name, Vector2 position, World world) {
        super(name, position, world);

        // collisions
        this.entityTeleportation = (RectangleMapObject) collisionsEntities.getLayers().get("teleportation").getObjects().get(name);
        this.collisionTeleportation = new Rectangle();

        // affichage
        this.image = entity.getTextureRegion();

    }
}
