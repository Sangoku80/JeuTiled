package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
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

    }

    @Override
    public void updateDirections() {

    }
}
