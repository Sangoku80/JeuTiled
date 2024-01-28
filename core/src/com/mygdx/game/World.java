package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public abstract class World {

    // affichage des layers et des tuiles
    protected String tilesetPath;
    protected ArrayList<TextureRegion> tileset = new ArrayList<>();
    protected int ratioTilesetX, ratioTilesetY;
    protected TmxMapLoader mapLoader = new TmxMapLoader();
    protected TiledMap tiledMap;
    protected ArrayList<TiledMapTileLayer> layers = new ArrayList<>();
    protected ArrayList<String> nameLayers = new ArrayList<>();
    protected Vector3 tmpVector = new Vector3();

    // animation des tuiles
    protected HashMap<Integer, Animation> animatedTiles = new HashMap<>();

    // affichage du joueur
    protected int entitiesLayer;

    // afficher l'ensemble des entités présentes dans le niveau
    protected ArrayList<Entity> entities = new ArrayList<>();

    // effet profondeur avec les autres entités
    protected int effetProfondeurBas;
    protected int effetProfondeurHaut;

    public World(String tilesetPath, String map, int ratioTilesetX, int ratioTilesetY, int entitiesLayer)
    {
        // affichage des layers et des tuiles
        this.tiledMap = mapLoader.load(map);
        this.tilesetPath = tilesetPath;
        this.ratioTilesetX = ratioTilesetX;
        this.ratioTilesetY = ratioTilesetY;

        // affichage du joueur avec effet profondeur avec les autres entités
        this.entitiesLayer = entitiesLayer;
        this.effetProfondeurBas = entitiesLayer;
        this.effetProfondeurHaut = entitiesLayer + 1;

        // on récupère le nom de chaque layer de la carte
        for (MapLayer layer : tiledMap.getLayers())
        {
            if(layer.getObjects().getCount() == 0)
            {
                this.nameLayers.add(layer.getName());
            }
        }

        // chargement
        loadEntities();
        loadTileset();
        loadLayers();
        loadAnimatedTiles();
    }

    public void loadTileset()
    {
        Texture img = new Texture(tilesetPath);

        for(int y = 0; y < img.getHeight()/ratioTilesetY; y++)
        {
            for(int x = 0; x < img.getWidth()/ratioTilesetX; x++)
            {
                tileset.add(new TextureRegion(img, x*ratioTilesetX, y*ratioTilesetY, ratioTilesetX, ratioTilesetY));
            }
        }
    }

    public abstract void loadAnimatedTiles();

    public void loadLayers() {

        for (String nameLayer : nameLayers)
        {
            layers.add((TiledMapTileLayer) tiledMap.getLayers().get(nameLayer));
        }
    }

    public void loadEntities()
    {
        for (MapObject object : tiledMap.getLayers().get("positions").getObjects())
        {
            if (object instanceof RectangleMapObject)
            {

                if (Objects.equals(object.getName(), "cochon"))
                {
                    entities.add(new Cochon((int) ((RectangleMapObject) object).getRectangle().getX(), (int) ((RectangleMapObject) object).getRectangle().getY(), entitiesLayer, this));
                }
            }
        }
    }

    public void drawEntities(SpriteBatch batch, int layerNumber)
    {
        for (Entity entity : entities)
        {
            if (entity.layer == layerNumber)
            {
                entity.Draw(batch);
            }
        }
    }

    public void drawLayer(TiledMapTileLayer layer, SpriteBatch batch)
    {
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                float tileWidth = layer.getTileWidth();
                float tileHeight = layer.getTileHeight();

                // Vérifier si la cellule n'est pas vide
                if (layer.getCell(x, y) != null) {

                    // Convertir les coordonnées du monde en coordonnées d'écran
                    tmpVector.set(x * tileWidth, y * tileHeight, 0);

                    // animer les tuiles animées
                    for (Integer key : animatedTiles.keySet())
                    {

                        if (layer.getCell(x, y).getTile().getId()-1 == 295)
                        {
                            layer.getCell(x, y).getTile().setTextureRegion(animatedTiles.get(key).animate());
                        }
                    }

                    batch.draw(layer.getCell(x, y).getTile().getTextureRegion(), tmpVector.x, tmpVector.y);
                }
            }
        }
    }

    public void drawLayers(SpriteBatch batch)
    {
        for (int layerNumber = 0; layerNumber < layers.size(); layerNumber++)
        {
            drawEntities(batch, layerNumber);
            drawLayer(layers.get(layerNumber), batch);
        }
    }
}