package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreen.Characters.Character;
import com.mygdx.game.GameScreen.Characters.Cochon;
import com.mygdx.game.GameScreen.Characters.Player;
import com.mygdx.game.GameScreen.Characters.Vache;
import com.mygdx.game.GameScreen.Entity;
import com.mygdx.game.GameScreen.infrastructures.Maison;
import com.mygdx.game.Tools.Animation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class World {

    // caractéristiques
    protected String name;

    // static
    protected static ArrayList<World> worlds = new ArrayList<>();

    // affichage des layers et des tuiles
    protected String tilesetPath;
    protected ArrayList<TextureRegion> tileset = new ArrayList<>();
    protected int ratioTilesetX, ratioTilesetY;
    protected TmxMapLoader mapLoader = new TmxMapLoader();
    public TiledMap tiledMap;
    protected ArrayList<TiledMapTileLayer> layers = new ArrayList<>();
    protected ArrayList<String> nameLayers = new ArrayList<>();
    protected Vector3 tmpVector = new Vector3();

    // animation des tuiles
    protected HashMap<Integer, Animation> animatedTiles = new HashMap<>();

    // affichage du joueur
    public int entitiesLayer;

    // afficher l'ensemble des entités présentes dans le niveau
    public ArrayList<Entity> entities = new ArrayList<>();

    // effet profondeur avec les autres entités
    protected int effetProfondeurBas;
    protected int effetProfondeurHaut;

    // création du joueur
    public Player player;

    public World(String name, String tilesetPath, String map, int ratioTilesetX, int ratioTilesetY)
    {
        // caractéristiques
        this.name = name;

        // affichage des layers et des tuiles
        this.tiledMap = mapLoader.load(map);
        this.tilesetPath = tilesetPath;
        this.ratioTilesetX = ratioTilesetX;
        this.ratioTilesetY = ratioTilesetY;

        // on ajoute ce monde à la liste des mondes
        worlds.add(this);

        // affichage du joueur avec effet profondeur avec les autres entités
        this.entitiesLayer = tiledMap.getLayers().getIndex("entités");
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

        // création du joueur
        this.entities.add(new Player(120, 120, this));

        for (Entity entity : this.entities)
        {
            if (Objects.equals(entity.name, "player"))
            {
                player = (Player) entity;
            }
        }
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

        for (int i=0; i < nameLayers.size(); i++)
        {
            layers.add((TiledMapTileLayer) tiledMap.getLayers().get(nameLayers.get(i)));
        }

        // pour l'effet profondeur
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        int largeur = layer.getWidth();
        int hauteur = layer.getHeight();
        TiledMapTileLayer newLayer = new TiledMapTileLayer(largeur, hauteur, layer.getTileWidth(), layer.getTileHeight());
        layers.add(entitiesLayer, newLayer);
    }

    public void loadEntities()
    {
        // mettre dans le dico toutes les entities à créer dans le niveau
        HashMap<String, Entity> entitiesAMettre = new HashMap<>();

        if (tiledMap.getLayers().get("entités") != null)
        {
            for (MapObject object : tiledMap.getLayers().get("entités").getObjects())
            {
                if (object instanceof TextureMapObject)
                {
                    if (Objects.equals(object.getName(), "cochon"))
                    {
                        entities.add(new Cochon((int) ((TextureMapObject) object).getX(), (int) ((TextureMapObject) object).getY(), this));
                    }
                }

                if (object instanceof TextureMapObject)
                {
                    if (Objects.equals(object.getName(), "maison"))
                    {
                        entities.add(new Maison((int) ((TextureMapObject) object).getX(), (int) ((TextureMapObject) object).getY(), this));
                    }
                }

                if (object instanceof TextureMapObject)
                {
                    if (Objects.equals(object.getName(), "vache"))
                    {
                        entities.add(new Vache((int) ((TextureMapObject) object).getX(), (int) ((TextureMapObject) object).getY(), this));
                    }
                }
            }
        }

    }

    public void drawEntities(SpriteBatch batch, int layerNumber)
    {
        for (Entity entity : entities)
        {
            if (entity instanceof Character)
            {
                if (((Character) entity).layer == layerNumber)
                {
                    entity.Draw(batch);
                }
            }

        }
    }

    public void updateEntities()
    {
        for (Entity entity : entities)
        {
            if (entity instanceof Character)
            {
                ((Character) entity).update();
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
                    for (Map.Entry<Integer, Animation> key : animatedTiles.entrySet())
                    {
                        if (layer.getCell(x, y).getTile().getTextureRegion() == key.getValue().animate())
                        {
                            layer.getCell(x, y).getTile().setTextureRegion(animatedTiles.get(key.getKey()).animate());
                            System.out.println("ok");
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
            drawLayer(layers.get(layerNumber), batch);
            drawEntities(batch, layerNumber);
        }

        // mettre à jour les entités
        updateEntities();
    }
}