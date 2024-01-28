package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class Player extends Entity implements InputProcessor {

    public Player(float x, float y, int layer, World currentWorld)
    {
        super("player", x, y, 2f, layer, currentWorld);

        // animation par défaut
        currentAnimation = animations.get("bas_idle");
    }

    @Override
    public void loadAnimations() {

        // en mouvement
        animations.put("bas", (new Animation(new int[]{0, 3}, spriteSheet,15)));
        animations.put("bas droite", (new Animation(new int[]{56, 59}, spriteSheet,15)));
        animations.put("bas gauche", (new Animation(new int[]{8, 11}, spriteSheet,15)));
        animations.put("gauche", (new Animation(new int[]{16, 19}, spriteSheet,15)));
        animations.put("haut", (new Animation(new int[]{32, 35}, spriteSheet,15)));
        animations.put("haut droite", (new Animation(new int[]{40, 43}, spriteSheet,15)));
        animations.put("haut gauche", (new Animation(new int[]{24, 27}, spriteSheet,15)));
        animations.put("droite", (new Animation(new int[]{48, 51}, spriteSheet,15)));

        // sans mouvement
        animations.put("bas_idle", (new Animation(new int[]{0, 0}, spriteSheet,15)));
        animations.put("bas droite_idle", (new Animation(new int[]{56, 56}, spriteSheet,15)));
        animations.put("bas gauche_idle", (new Animation(new int[]{8, 8}, spriteSheet,15)));
        animations.put("gauche_idle", (new Animation(new int[]{16, 16}, spriteSheet,15)));
        animations.put("haut_idle", (new Animation(new int[]{32, 32}, spriteSheet,15)));
        animations.put("haut droite_idle", (new Animation(new int[]{40, 40}, spriteSheet,15)));
        animations.put("haut gauche_idle", (new Animation(new int[]{24, 24}, spriteSheet,15)));
        animations.put("droite_idle", (new Animation(new int[]{48, 48}, spriteSheet,15)));
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
