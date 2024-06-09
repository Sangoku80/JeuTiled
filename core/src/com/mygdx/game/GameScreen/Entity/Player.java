package com.mygdx.game.GameScreen.Entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

import java.util.HashMap;

public class Player extends Character implements InputProcessor {

    // mouvements
    protected boolean left, up, right, down;

    public Player(float x, float y, World currentWorld) {
        super("player", new Vector2(x, y), 2f, 100, 5, currentWorld, true);
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        // en mouvement
        animations.put(bas+move, (new Animation(new int[]{0, 3},15)));
        animations.put(bas_droite+move, (new Animation(new int[]{56, 59}, 15)));
        animations.put(bas_gauche+move, (new Animation(new int[]{8, 11}, 15)));
        animations.put(gauche+move, (new Animation(new int[]{16, 19}, 15)));
        animations.put(haut+move, (new Animation(new int[]{32, 35}, 15)));
        animations.put(haut_droite+move, (new Animation(new int[]{40, 43},  15)));
        animations.put(haut_gauche+move, (new Animation(new int[]{24, 27},  15)));
        animations.put(droite+move, (new Animation(new int[]{48, 51}, 15)));

        // sans mouvement
        animations.put("bas_idle", (new Animation(new int[]{0, 0}, 15)));
        animations.put("bas_droite_idle", (new Animation(new int[]{56, 56}, 15)));
        animations.put("bas_gauche_idle", (new Animation(new int[]{8, 8},  15)));
        animations.put("gauche_idle", (new Animation(new int[]{16, 16}, 15)));
        animations.put("haut_idle", (new Animation(new int[]{32, 32}, 15)));
        animations.put("haut_droite_idle", (new Animation(new int[]{40, 40}, 15)));
        animations.put("haut_gauche_idle", (new Animation(new int[]{24, 24}, 15)));
        animations.put("droite_idle", (new Animation(new int[]{48, 48}, 15)));
    }

    // updates
    public void updateDirection()
    {
        // vérifier s'il n'y a aucun mouvement
        if (!left && !right && !up && !down) {
            moving = false;

        } else {

            moving = true;

            // bouger en fonction de la direction
            if (left && !right) {
                direction = LEFT;

            } else if (right && !left) {
                direction = RIGHT;
            }
            if (up && !down) {
                direction = UP;

            } else if (down && !up) {
                direction = DOWN;
            }
            if (right && up) {
                direction = UP_RIGHT;

            } else if (right && down) {
                direction = DOWN_RIGHT;
            }
            if (left && up) {
                direction = UP_LEFT;

            } else if (left && down) {
                direction = DOWN_LEFT;
            }
        }
    }

    @Override
    public void update() {
        updateDirection();
    }

    @Override
    public boolean keyDown(int keycode) {

        // flèches
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

            // flèches
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

    // updates
    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
