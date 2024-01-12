package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public class Player extends Entity implements InputProcessor {

    public Player()
    {
        super((TextureMapObject) Game.tiledMap.getLayers().get("entités").getObjects().get("player"), (RectangleMapObject) Game.tiledMap.getLayers().get("entités").getObjects().get("foot"), 1f, "Images/sprite sheet.png", 16, 16);
        currentAnimation = animations.get("marche 3 quart face gauche");
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

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode)
        {
            case Input.Keys.UP:
                setUp(true);
                break;

            case Input.Keys.DOWN:
                setDown(true);
                break;

            case Input.Keys.RIGHT:
                setRight(true);
                break;

            case Input.Keys.LEFT:
                setLeft(true);
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode)
        {
            case Input.Keys.UP:
                setUp(false);
                break;

            case Input.Keys.DOWN:
                setDown(false);
                break;

            case Input.Keys.RIGHT:
                setRight(false);
                break;

            case Input.Keys.LEFT:
                setLeft(false);
                break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}

