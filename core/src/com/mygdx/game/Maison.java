package com.mygdx.game;

public class Maison extends Entity {

    public Maison(float x, float y, int layer, World world)
    {
        super("maison", x, y, 0f, layer, 56, 56, world);

        // animation par défaut
        currentAnimation = animations.get("idle");
    }

    @Override
    public void loadAnimations() {
        animations.put("idle",  new Animation(new int[]{0, 0}, spriteSheet, 15));
    }

    @Override
    public void updateDirections() {

    }

    @Override
    public void updateAnimation() {

    }
}
