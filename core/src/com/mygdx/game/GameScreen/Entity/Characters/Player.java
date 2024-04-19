package com.mygdx.game.GameScreen.Entity.Characters;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

public class Player extends Character implements InputProcessor {

    public Player(float x, float y, World currentWorld) {
        super("player", new Vector2(x, y), 2f, 20, 5, currentWorld);
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        // en mouvement
        animations.put("bas"+MOVE, (new Animation(new int[]{0, 3},15)));
        animations.put("bas droite"+MOVE, (new Animation(new int[]{56, 59}, 15)));
        animations.put("bas gauche"+MOVE, (new Animation(new int[]{8, 11}, 15)));
        animations.put("gauche", (new Animation(new int[]{16, 19}, 15)));
        animations.put("haut", (new Animation(new int[]{32, 35}, 15)));
        animations.put("haut droite", (new Animation(new int[]{40, 43},  15)));
        animations.put("haut gauche", (new Animation(new int[]{24, 27},  15)));
        animations.put("droite", (new Animation(new int[]{48, 51}, 15)));

        // sans mouvement
        animations.put("bas_idle", (new Animation(new int[]{0, 0}, 15)));
        animations.put("bas droite_idle", (new Animation(new int[]{56, 56}, 15)));
        animations.put("bas gauche_idle", (new Animation(new int[]{8, 8},  15)));
        animations.put("gauche_idle", (new Animation(new int[]{16, 16}, 15)));
        animations.put("haut_idle", (new Animation(new int[]{32, 32}, 15)));
        animations.put("haut droite_idle", (new Animation(new int[]{40, 40}, 15)));
        animations.put("haut gauche_idle", (new Animation(new int[]{24, 24}, 15)));
        animations.put("droite_idle", (new Animation(new int[]{48, 48}, 15)));
    }

    @Override
    public void update() {

    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
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

            case Input.Keys.SPACE:
                attack();
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
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
