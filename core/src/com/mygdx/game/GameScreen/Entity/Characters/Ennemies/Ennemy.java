package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
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
    }

    @Override
    public void loadAnimations() {

    }

    @Override
    public void updateDirections() {

    }
}
