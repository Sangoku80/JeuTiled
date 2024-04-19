package com.mygdx.game.GameScreen.Entity.Characters.Animals;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

public class Vache extends Character {

    public Vache(float x, float y, World currentWorld)
    {
        super("vache", new Vector2(x, y), 2f, 20, currentWorld, false);
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        animations.put(DOWN+ idle, (new Animation(new int[]{91, 93}, 15)));
        animations.put(UP+ move, (new Animation(new int[]{115, 117}, 15)));
        animations.put(RIGHT+ move, (new Animation(new int[]{87, 89}, 15)));
    }

    @Override
    public void update() {

    }
}