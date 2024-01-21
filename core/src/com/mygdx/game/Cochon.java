package com.mygdx.game;

public class Cochon extends Entity{

    public Cochon(int x, int y)
    {
        super("cochon", x, y, x+2, y, 2f);

        // animation par défaut
        currentAnimation = animations.get("gauche");
    }

    @Override
    public void loadAnimations() {
        animations.put("gauche", (new Animation(new int[]{87, 89}, spriteSheet, 15)));
        animations.put("haut", (new Animation(new int[]{111, 113}, spriteSheet, 15)));
        animations.put("bas", (new Animation(new int[]{135, 136}, spriteSheet, 15)));
    }

    @Override
    public void updateDirections() {

    }

    @Override
    public void updateAnimation() {

    }
}
