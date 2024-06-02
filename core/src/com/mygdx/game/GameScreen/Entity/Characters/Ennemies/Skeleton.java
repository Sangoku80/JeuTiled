package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

public class Skeleton extends Enemy {

    public Skeleton(int x, int y, World currentWorld) {
        super("skeleton", x, y, currentWorld);
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        // en mouvement
        animations.put(bas+ move, (new Animation(new int[]{208, 209},15)));
        animations.put(gauche+ move, (new Animation(new int[]{212, 212}, 15, true, false)));
        animations.put(haut+ move, (new Animation(new int[]{210, 211}, 15)));
        animations.put(droite+ move, (new Animation(new int[]{212, 213}, 15)));

        // sans mouvement
        animations.put(bas+ idle, (new Animation(new int[]{204, 204}, 15)));
        animations.put(gauche+ idle, (new Animation(new int[]{212, 212}, 15, true, false)));
        animations.put(haut+ idle, (new Animation(new int[]{205, 205}, 15)));
        animations.put(droite+ idle, (new Animation(new int[]{212, 212}, 15)));
    }
}
