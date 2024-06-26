package com.mygdx.game.GameScreen.Entity.Characters.Animals;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

public class Cochon extends Character {

    public Cochon(int x, int y, World currentWorld)
    {
        super("cochon", new Vector2(x, y), 2f, 20, currentWorld, false);
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        // en mouvement
        animations.put(gauche+ move, (new Animation(new int[]{87, 89}, 15)));
        animations.put(haut+ move, (new Animation(new int[]{111, 113}, 15)));

        // immobile
        animations.put(bas+ idle, (new Animation(new int[]{135, 136}, 15)));


    }

    @Override
    public void update() {

    }
}