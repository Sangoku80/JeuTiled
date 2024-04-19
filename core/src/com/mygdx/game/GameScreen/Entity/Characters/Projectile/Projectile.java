package com.mygdx.game.GameScreen.Entity.Characters.Projectile;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Entity.Entity;
import com.mygdx.game.GameScreen.Worlds.World;

public abstract class Projectile extends Character {

    public Projectile(String name, Vector2 position, float speed, float health, World world) {
        super(name, position, speed, health, world, false);
    }

    public abstract void attack();

    @Override
    public void update() {
        for (Entity entity : currentWorld.entities)
        {
            if (Intersector.overlaps(entity.rect, rect))
            {
                attack();
            }
        }
    }
}
