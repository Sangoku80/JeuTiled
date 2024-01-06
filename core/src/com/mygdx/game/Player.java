package com.mygdx.game;

import com.badlogic.gdx.maps.objects.TextureMapObject;

public class Player extends Entity {

    public Player()
    {
        super((TextureMapObject) Game.tiledMap.getLayers().get("entités").getObjects().get("player"), 200, "Images/sprite sheet.png", 16, 16);
    }

    @Override
    public void loadAnimations() {

        animations.put("marche face", (new Animation(new int[]{0, 1, 2, 3}, spriteSheet, true, 15)));
        animations.put("marche 3 quart face gauche", new Animation(new int[]{8, 9, 10, 11}, spriteSheet, true, 15));
        animations.put("marche profil gauche", (new Animation(new int[]{16, 17, 18, 19}, spriteSheet, true, 15)));
        animations.put("marche 3 quart face gauche", new Animation(new int[]{8, 9, 10, 11}, spriteSheet, true, 15));
        animations.put("marche face", (new Animation(new int[]{0, 1, 2, 3}, spriteSheet, true, 15)));
        animations.put("marche 3 quart face gauche", new Animation(new int[]{8, 9, 10, 11}, spriteSheet, true, 15));
        animations.put("marche face", (new Animation(new int[]{0, 1, 2, 3}, spriteSheet, true, 15)));
        animations.put("marche 3 quart face gauche", new Animation(new int[]{8, 9, 10, 11}, spriteSheet, true, 15));
    }

    @Override
    public void Update() {

    }
}
