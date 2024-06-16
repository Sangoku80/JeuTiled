package com.mygdx.game.GameScreen.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Tools.Vector2D;
import com.mygdx.game.GameScreen.Worlds.World;
import com.mygdx.game.GameScreen.Entity.Infrastructures.Infrastructure;
import java.util.ArrayList;

public class Entity {

    // caractéristiques
    public String name;
    public Vector2D position;
    protected int width, height;
    public Rectangle rect;

    // self collisions
    public TextureMapObject entity;
    public RectangleMapObject entityBas;
    protected RectangleMapObject entityHaut;
    public Rectangle collisionBas;

    // carte actuelle
    protected TiledMap tiledMap;

    // fichier qui stocke les collisions des entités
    protected TmxMapLoader mapLoader = new TmxMapLoader();
    public TiledMap collisionsEntities;

    // affichage
    protected TextureRegion image;

    // monde dans lequel est notre entité
    protected World currentWorld;

    public Entity(String name, Vector2 position, World world) {

        // monde actuel
        this.currentWorld = world;

        // mouvements et collisions
        this.name = name;
        this.collisionsEntities = mapLoader.load("maps/entités.tmx");
        this.tiledMap = world.tiledMap;
        this.entity = (TextureMapObject) collisionsEntities.getLayers().get("entités").getObjects().get(name);
        this.entityBas = (RectangleMapObject) collisionsEntities.getLayers().get("entités_bas").getObjects().get(name);
        this.width = entity.getTextureRegion().getRegionWidth();
        this.height = entity.getTextureRegion().getRegionHeight();
        this.position = new Vector2D(position.x, position.y);
        this.collisionBas = new Rectangle();

        // rect
        this.rect = new Rectangle(position.x, position.y, width, height);

        if(this instanceof Infrastructure)
        {
            // collisions
            ((Infrastructure) this).entityTeleportation = (RectangleMapObject) collisionsEntities.getLayers().get("teleportation").getObjects().get(name);
            ((Infrastructure) this).collisionTeleportation = new Rectangle();
            // affichage
            this.image = entity.getTextureRegion();
        }

        // chargement
        loadSelfCollisions();
    }

    public void deleteCollisionBas()
    {
        currentWorld.collisionsEntitiesBas.removeIf(rect -> rect.x == collisionBas.x);
    }

    public void loadSelfCollisions()
    {
        ArrayList<RectangleMapObject> list = new ArrayList<>();
        list.add(entityBas);

        ArrayList<Rectangle> list2 = new ArrayList<>();
        list2.add(collisionBas);

        if (this instanceof Infrastructure) {
            list.add(((Infrastructure) this).entityTeleportation);
            list2.add(((Infrastructure) this).collisionTeleportation);
        }
        for (int i = 0; i < list.size(); i++) {
            float décalageX = Math.abs(entity.getX() - list.get(i).getRectangle().x);
            float décalageY = Math.abs(entity.getY() - list.get(i).getRectangle().y);

            list2.get(i).x = position.x + décalageX;
            list2.get(i).y = position.y + décalageY;
            list2.get(i).width = list.get(i).getRectangle().width;
            list2.get(i).height = list.get(i).getRectangle().height;

        }
    }

    public void Draw(SpriteBatch batch) {

        if (this instanceof Character)
        {
            if (((Character) this).currentAnimation != null)
            {
                image = ((Character) this).currentAnimation.animate();
            }
        }
        else
        {
            image = entity.getTextureRegion();
        }

        // dessiner
        if (image != null)
        {
            batch.draw(image, position.x, position.y);
        }
    }
}