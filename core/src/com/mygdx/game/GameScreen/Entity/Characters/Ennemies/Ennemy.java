package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

public class Ennemy extends Character {

    // status
    protected static int status;
    protected static int PURSUING=0;
    protected static int IDLE=1;

    // cercle de détection du joueur
    protected Circle circleAttack = new Circle(position, 5);

    public Ennemy(String name, Vector2 position, float speed, World world) {
        super(name, position, speed, world);

        // status par défaut
        status = IDLE;

        // animation par défaut
        currentAnimation = animations.get("gauche");
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        animations.put("gauche", (new Animation(new int[]{87, 89}, 15)));
        animations.put("haut", (new Animation(new int[]{111, 113}, 15)));
        animations.put("bas", (new Animation(new int[]{135, 136}, 15)));
    }

    @Override
    public void updateDirections() {

    }

    @Override
    public void updateAnimation() {

    }
}
