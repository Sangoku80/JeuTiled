package com.mygdx.game;

public class Vache extends Entity {

    public Vache(float x, float y)
    {
        super("vache", x, y, x+2, y, 2f);

        // animation par défaut
        currentAnimation = animations.get("bas");
    }

    @Override
    public void loadAnimations() {
        animations.put("bas", (new Animation(new int[]{91, 93}, spriteSheet, 15)));
        animations.put("haut", (new Animation(new int[]{115, 117}, spriteSheet, 15)));
        animations.put("droite", (new Animation(new int[]{87, 89}, spriteSheet, 15)));
    }

    @Override
    public void updateDirections() {

    }

    @Override
    public void updateAnimation() {

    }
}
