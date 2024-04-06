package com.mygdx.game.GameScreen.infrastructures;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.World;

public class Maison extends Infrastructure {

    public Maison(float x, float y, World world)
    {
        super("maison", new Vector2(x, y), world);
    }
}