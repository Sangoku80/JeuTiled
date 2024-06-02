package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

public class Agent extends Enemy {

    public Agent(int x, int y, World currentWorld) {
        super("agent", x, y, currentWorld);
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        // en mouvement
        animations.put(bas+ move, (new Animation(new int[]{140, 141},15)));
        animations.put(gauche+ move, (new Animation(new int[]{144, 145}, 15, true, false)));
        animations.put(haut+ move, (new Animation(new int[]{142, 143}, 15)));
        animations.put(droite+ move, (new Animation(new int[]{144, 145}, 15)));

        // sans mouvement
        animations.put(bas+ idle, (new Animation(new int[]{136, 136}, 15)));
        animations.put(gauche+ idle, (new Animation(new int[]{138, 138}, 15, true, false)));
        animations.put(haut+ idle, (new Animation(new int[]{137, 137}, 15)));
        animations.put(droite+ idle, (new Animation(new int[]{138, 138}, 15)));
    }
}
