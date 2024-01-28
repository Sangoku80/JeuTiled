package com.mygdx.game;

public class Vache extends Entity {

    public Vache(float x, float y, int layer, World currentWorld)
    {
        super("vache", x, y, 2f, layer, currentWorld);

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
