package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

public class Enemy extends Character {

    // status
    protected static int status;
    protected static int PURSUING=0;
    protected static int IDLE=1;

    // cercle de détection du joueur
    protected Circle circleAttack = new Circle(position, 5);

    public Enemy(int x, int y, World currentWorld) {
        super("enemy", new Vector2(x, y), 2f, currentWorld);

        // status par défaut
        status = IDLE;
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        // en mouvement
        animations.put("bas", (new Animation(new int[]{140, 141},15)));
        animations.put("gauche", (new Animation(new int[]{144, 145}, 15)));
        animations.put("haut", (new Animation(new int[]{142, 143}, 15)));
        animations.put("droite", (new Animation(new int[]{144, 145}, 15)));

        // sans mouvement
        animations.put("bas_idle", (new Animation(new int[]{136, 136}, 15)));
        animations.put("gauche_idle", (new Animation(new int[]{138, 138}, 15)));
        animations.put("haut_idle", (new Animation(new int[]{137, 137}, 15)));
        animations.put("droite_idle", (new Animation(new int[]{138, 138}, 15)));
    }

    @Override
    public void updateDirections() {
    }
}
