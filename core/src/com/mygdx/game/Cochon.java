package com.mygdx.game;

public class Cochon extends Entity{

    public Cochon()
    {
        super("cochon", 2f);
        currentAnimation = animations.get("gauche");
    }

    @Override
    public void loadAnimations() {
        animations.put("gauche", (new Animation(new int[]{87, 88, 89}, spriteSheet, true, 15)));
    }

    @Override
    public void updateDirections() {

    }

    @Override
    public void updateAnimation() {

    }
}
