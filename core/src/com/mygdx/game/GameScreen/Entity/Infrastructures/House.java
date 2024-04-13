package com.mygdx.game.GameScreen.Entity.Infrastructures;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Worlds.World;

public class House extends Infrastructure {

    public House(float x, float y, World world)
    {
        super("maison", new Vector2(x, y), world);
    }
}