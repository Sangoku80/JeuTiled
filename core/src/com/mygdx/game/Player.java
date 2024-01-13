package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public class Player extends Entity implements InputProcessor {

    public Player()
    {
        super((TextureMapObject) Game.tiledMap.getLayers().get("entités").getObjects().get("player"), (RectangleMapObject) Game.tiledMap.getLayers().get("entités").getObjects().get("foot"), 2f, "Images/sprite sheet.png", 16, 16);
        currentAnimation = animations.get("bas_idle");
    }

    @Override
    public void loadAnimations() {

        // en mouvement
        animations.put("bas", (new Animation(new int[]{0, 1, 2, 3}, spriteSheet, true, 15)));
        animations.put("bas droite", (new Animation(new int[]{56, 57, 58, 59}, spriteSheet, true, 15)));
        animations.put("bas gauche", (new Animation(new int[]{8, 9, 10, 11}, spriteSheet, true, 15)));
        animations.put("gauche", (new Animation(new int[]{16, 17, 18, 19}, spriteSheet, true, 15)));
        animations.put("haut", (new Animation(new int[]{32, 33, 34, 35}, spriteSheet, true, 15)));
        animations.put("haut droite", (new Animation(new int[]{40, 41, 42, 43}, spriteSheet, true, 15)));
        animations.put("haut gauche", (new Animation(new int[]{24, 25, 26, 27}, spriteSheet, true, 15)));
        animations.put("droite", (new Animation(new int[]{48, 49, 50, 51}, spriteSheet, true, 15)));

        // sans mouvement
        animations.put("bas_idle", (new Animation(new int[]{0}, spriteSheet, true, 15)));
        animations.put("bas droite_idle", (new Animation(new int[]{56}, spriteSheet, true, 15)));
        animations.put("bas gauche_idle", (new Animation(new int[]{8}, spriteSheet, true, 15)));
        animations.put("gauche_idle", (new Animation(new int[]{16}, spriteSheet, true, 15)));
        animations.put("haut_idle", (new Animation(new int[]{32}, spriteSheet, true, 15)));
        animations.put("haut droite_idle", (new Animation(new int[]{40, 41, 42, 43}, spriteSheet, true, 15)));
        animations.put("haut gauche_idle", (new Animation(new int[]{24, 25, 26, 27}, spriteSheet, true, 15)));
        animations.put("droite_idle", (new Animation(new int[]{48}, spriteSheet, true, 15)));
    }

    @Override
    public void updateDirections() {

    }

    @Override
    public void updateAnimation() {

        if (moving)
        {
            currentAnimation = animations.get(orientation);
        }
        else
        {
            currentAnimation = animations.get(orientation + "_idle");
        }
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

