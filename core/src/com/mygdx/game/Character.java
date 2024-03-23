package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Tools.Animation;

import java.util.ArrayList;
import java.util.HashMap;

public class Character {

    // caractéristiques
    protected float speed;

    // mouvements
    protected boolean left, up, right, down;

    // collisions
    protected ArrayList<Rectangle> collisionsStop = new ArrayList<>();
    protected HashMap<Rectangle, String> collisionsTeleportation = new HashMap<>();
    protected ArrayList<Rectangle> collisionsEntitiesHaut = new ArrayList<>();
    protected ArrayList<Rectangle> collisionsEntitiesBas = new ArrayList<>();

    // animations
    protected String spriteSheetPath;
    protected ArrayList<TextureRegion> spriteSheet = new ArrayList<>();
    protected HashMap<String, Animation> animations = new HashMap<>();
    protected Animation currentAnimation;
    protected String orientation = "bas";
    protected Boolean moving = false;

    // affichage
    public int layer;
    protected int layerBas;
    protected int layerHaut;

}
