package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

public class Goblin extends Enemy {

    public Goblin(int x, int y, World currentWorld) {
        super("goblin", x, y, currentWorld);
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        // en mouvement
        animations.put(bas+ move, (new Animation(new int[]{174, 175},15)));
        animations.put(gauche+ move, (new Animation(new int[]{178, 179}, 15, true, false)));
        animations.put(haut+ move, (new Animation(new int[]{176, 177}, 15)));
        animations.put(droite+ move, (new Animation(new int[]{178, 179}, 15)));

        // sans mouvement
        animations.put(bas+ idle, (new Animation(new int[]{170, 170}, 15)));
        animations.put(gauche+ idle, (new Animation(new int[]{172, 172}, 15, true, false)));
        animations.put(haut+ idle, (new Animation(new int[]{176, 177}, 15)));
        animations.put(droite+ idle, (new Animation(new int[]{172, 172}, 15)));
    }
}
