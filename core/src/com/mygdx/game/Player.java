package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public class Player extends Entity {

    public Player()
    {
        super((TextureMapObject) Game.tiledMap.getLayers().get("entités").getObjects().get("player"), 1f, "Images/sprite sheet.png", 16, 16);
        currentAnimation = animations.get("marche face");
    }

    @Override
    public void loadAnimations() {

        animations.put("marche face", (new Animation(new int[]{0, 1, 2, 3}, spriteSheet, true, 15)));
        animations.put("marche 3 quart face gauche", new Animation(new int[]{8, 9, 10, 11}, spriteSheet, true, 15));
        animations.put("marche profil gauche", (new Animation(new int[]{16, 17, 18, 19}, spriteSheet, true, 15)));
        animations.put("marche 3 quart dos gauche", new Animation(new int[]{8, 9, 10, 11}, spriteSheet, true, 15));
        animations.put("marche dos", (new Animation(new int[]{32, 33, 34, 35}, spriteSheet, true, 15)));
        animations.put("marche 3 quart dos droite", new Animation(new int[]{40, 41, 42, 43}, spriteSheet, true, 15));
        animations.put("marche profil droite", (new Animation(new int[]{48, 49, 50, 51}, spriteSheet, true, 15)));
        animations.put("marche 3 quart face droite", new Animation(new int[]{56, 57, 58, 59}, spriteSheet, true, 15));
    }

    @Override
    public void updateDirections() {

        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            setUp(true);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            setDown(true);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            setLeft(true);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            setRight(true);
        }
        else
        {
            resetDirBooleans();
        }

    }

}
