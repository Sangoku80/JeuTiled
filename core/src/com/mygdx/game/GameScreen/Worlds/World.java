package com.mygdx.game.GameScreen.Worlds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.*;
import com.mygdx.game.GameScreen.Entity.Characters.Animals.Cochon;
import com.mygdx.game.GameScreen.Entity.Characters.Animals.Vache;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Entity.Characters.Ennemies.Agent;
import com.mygdx.game.GameScreen.Entity.Characters.Ennemies.Goblin;
import com.mygdx.game.GameScreen.Entity.Characters.Ennemies.Skeleton;
import com.mygdx.game.GameScreen.Entity.Player;
import com.mygdx.game.GameScreen.Entity.Entity;
import com.mygdx.game.GameScreen.Entity.Infrastructures.Infrastructure;
import com.mygdx.game.GameScreen.Tools.Animation;
import java.util.*;

import static com.mygdx.game.Game.camera;
import static com.mygdx.game.Game.shapeRenderer;
import static com.mygdx.game.GameScreen.Tools.staticFunctions.*;

public abstract class World {

    // caractéristiques
    protected String name;

    // affichage des layers et des tuiles
    protected String tilesetPath;
    protected ArrayList<TextureRegion> tileset = new ArrayList<>();
    protected int ratioTilesetX, ratioTilesetY;
    protected TmxMapLoader mapLoader = new TmxMapLoader();
    public TiledMap tiledMap;
    public ArrayList<TiledMapTileLayer> layers = new ArrayList<>();
    protected ArrayList<String> nameLayers = new ArrayList<>();
    public static Vector3 tmpVector = new Vector3();

    // possibles destinations
    protected HashMap<Circle, String> possibleDestinations = new HashMap<>();

    // animation des tuiles
    protected HashMap<Integer, Animation> animatedTiles = new HashMap<>();

    // afficher l'ensemble des entités présentes dans le niveau
    public ArrayList<Entity> entities = new ArrayList<>();

    // création du joueur
    public Player player;

    // collisions
    public ArrayList<Rectangle> collisionsStop = new ArrayList<>();
    public HashMap<Rectangle, String> collisionsTeleportation = new HashMap<>();
    public ArrayList<Rectangle> collisionsEntitiesBas = new ArrayList<>();

    // toutes les collisions du monde
    public ArrayList<ArrayList> allCollisions = new ArrayList<>();

    public World(String name, String tilesetPath, String map, int ratioTilesetX, int ratioTilesetY)
    {
        // caractéristiques
        this.name = name;

        // affichage des layers et des tuiles
        this.tiledMap = mapLoader.load(map);
        this.tilesetPath = tilesetPath;
        this.ratioTilesetX = ratioTilesetX;
        this.ratioTilesetY = ratioTilesetY;

        // on récupère le nom de chaque layer de la carte
        for (MapLayer layer : tiledMap.getLayers())
        {
            if(layer.getObjects().getCount() == 0)
            {
                this.nameLayers.add(layer.getName());
            }
        }

        // chargement
        loadLayers();
        loadCollisions();
        loadCollisionsTile();
        loadEntities();
        loadEntitiesCollisions();
        loadTileset();
        loadAnimatedTiles();

        // rassembler toutes les collisions
        allCollisions.add(collisionsStop);
        allCollisions.add(collisionsEntitiesBas);

        loadPossibleDestinations();

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

    // loads
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

    public void loadEntitiesCollisions() {

        for (Entity entity : this.entities) {

            this.collisionsEntitiesBas.add(entity.collisionBas);

            if (entity instanceof Infrastructure) {
                this.collisionsTeleportation.put(((Infrastructure) entity).collisionTeleportation, (String) entity.collisionsEntities.getLayers().get("teleportation").getObjects().get(entity.entity.getName()).getProperties().get("destination"));
            }
        }
    }

    public void loadCollisions() {
        // dans la carte monde
        if (tiledMap.getLayers().get("collisions") != null) {
            if (tiledMap.getLayers().get("collisions").getObjects() != null) {
                for (Object object : tiledMap.getLayers().get("collisions").getObjects()) {
                    if (object instanceof RectangleMapObject) {
                        switch (((RectangleMapObject) object).getName()) {
                            case "teleportation":
                                collisionsTeleportation.put(((RectangleMapObject) object).getRectangle(), (String) ((RectangleMapObject) object).getProperties().get("destination"));

                            case "stop":
                                collisionsStop.add(((RectangleMapObject) object).getRectangle());
                        }
                    }
                }
            }
        }

        // ajouter des bords à la map
        TiledMapTileLayer layer = layers.get(0);
        float mapWidth = layer.getWidth() * layer.getTileWidth();
        float mapHeight = layer.getHeight() * layer.getTileHeight();
        float borderThickness = 0.05f;

        collisionsStop.add(new Rectangle(layer.getOffsetX(), mapHeight, mapWidth, borderThickness));
        collisionsStop.add(new Rectangle(layer.getOffsetX(), layer.getOffsetY() - borderThickness, mapWidth, borderThickness));
        collisionsStop.add(new Rectangle(layer.getOffsetX() - borderThickness, layer.getOffsetY(), borderThickness, mapHeight));
        collisionsStop.add(new Rectangle(mapWidth, layer.getOffsetY(), borderThickness, mapHeight));
    }

    // tile
    public void loadCollisionsTile()
    {
        TiledMapTileLayer layer;
        TiledMapTileLayer.Cell cell;

        for (int layerNumber = 0; layerNumber < layers.size(); layerNumber++)
        {
            layer = layers.get(layerNumber);

            for (int y = 0; y < layer.getHeight(); y++) {
                for (int x = 0; x < layer.getWidth(); x++) {
                    float tileWidth = layer.getTileWidth();
                    float tileHeight = layer.getTileHeight();

                    cell = layer.getCell(x, y);


                    // Vérifier si la cellule n'est pas vide
                    if (cell != null)
                    {
                        // Convertir les coordonnées du monde en coordonnées d'écran
                        tmpVector.set(x * tileWidth, y * tileHeight, 0);

                        // ajouter les collisions de la tuile
                        for (MapObject object : cell.getTile().getObjects())
                        {
                            if (object instanceof RectangleMapObject)
                            {
                                if (Objects.equals(object.getName(), "stop"))
                                {
                                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                                    this.collisionsStop.add(new Rectangle(tmpVector.x+rect.x, tmpVector.y+rect.y, rect.getWidth(), rect.getHeight()));
                                }
                                else if (Objects.equals(object.getName(), "teleportation"))
                                {
                                    this.collisionsTeleportation.put(((RectangleMapObject) object).getRectangle(), (String) object.getProperties().get("destination"));
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public abstract void loadAnimatedTiles();

    public void loadLayers() {

        for (int i=0; i < nameLayers.size(); i++)
        {
            layers.add((TiledMapTileLayer) tiledMap.getLayers().get(nameLayers.get(i)));
        }
    }

    public void loadPossibleDestinations()
    {
        for (TiledMapTileLayer layer : this.layers)
        {
            for (int y = 0; y < layer.getHeight(); y++) {
                for (int x = 0; x < layer.getWidth(); x++) {

                    float tileWidth = layer.getTileWidth();
                    float tileHeight = layer.getTileHeight();

                    // Vérifier si la cellule n'est pas vide
                    if (layer.getCell(x, y) != null)
                    {
                        // pour que l'on ait le milieu de la tuile
                        TextureRegion textureRegion = layer.getCell(x, y).getTile().getTextureRegion();

                        float X = x*tileHeight+ (float) textureRegion.getRegionHeight() /2;
                        float Y = y*tileWidth + (float) textureRegion.getRegionWidth() /2;

                        String answer = "";

                        for (ArrayList collisions : allCollisions)
                        {
                            for (Object collision : collisions)
                            {
                                if (collision instanceof Rectangle)
                                {
                                    if (Intersector.overlaps(new Rectangle(X*tileWidth, Y*tileHeight, tileWidth, tileHeight), (Rectangle) collision))
                                    {
                                        answer = "no disponible";
                                    }
                                    else
                                    {
                                        answer = "disponible";
                                    }
                                }
                            }
                        }

                        possibleDestinations.put(new Circle(X, Y, 1), answer);
                    }
                }
            }
        }
    }

    public void loadEntities()
    {
        // mettre dans le dico toutes les entities à créer dans le niveau

        if (tiledMap.getLayers().get("entités") != null)
        {
            for (MapObject object : tiledMap.getLayers().get("entités").getObjects())
            {
                if (object instanceof TextureMapObject)
                {
                    entities.add(new Entity(object.getName(), new Vector2(((TextureMapObject) object).getX(), ((TextureMapObject) object).getY()), this));
                }
            }
        }

        if (tiledMap.getLayers().get("characters") != null)
        {
            for (MapObject object : tiledMap.getLayers().get("characters").getObjects())
            {
                if (object instanceof TextureMapObject)
                {
                    switch (object.getName()) {

                        case "cochon":
                            entities.add(new Cochon((int) ((TextureMapObject) object).getX(), (int) ((TextureMapObject) object).getY(), this));
                            break;

                        case "vache":
                            entities.add(new Vache((int) ((TextureMapObject) object).getX(), (int) ((TextureMapObject) object).getY(), this));
                            break;

                        case "goblin":
                            entities.add(new Goblin((int) ((TextureMapObject) object).getX(), (int) ((TextureMapObject) object).getY(), this));
                            break;

                        case "agent":
                            entities.add(new Agent((int) ((TextureMapObject) object).getX(), (int) ((TextureMapObject) object).getY(), this));
                            break;

                        case "skeleton":
                            entities.add(new Skeleton((int) ((TextureMapObject) object).getX(), (int) ((TextureMapObject) object).getY(), this));
                            break;
                    }
                }
            }
        }

        if (tiledMap.getLayers().get("infrastructures") != null)
        {
            for (MapObject object : tiledMap.getLayers().get("infrastructures").getObjects())
            {
                if (object instanceof TextureMapObject)
                {
                    entities.add(new Infrastructure(object.getName(), new Vector2(((TextureMapObject) object).getX(), ((TextureMapObject) object).getY()), this));
                }
            }
        }

    }

    public void drawEntities(SpriteBatch batch)
    {
        sortByY(entities);

        for (Entity entity : entities)
        {
            entity.Draw(batch);
        }
    }

    public void drawCollisions() {

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(0, 0, 1, 0);

        for (ArrayList collisions : allCollisions) {
            for (Object collision : collisions) {
                if (collision instanceof Rectangle) {
                    shapeRenderer.rect((((Rectangle) collision).getX()), ((Rectangle) collision).getY(), ((Rectangle) collision).getWidth(), ((Rectangle) collision).getHeight());
                }
            }
        }

        shapeRenderer.end();
    }

    // updates
    public void updateEntities()
    {
        for (Entity entity : entities)
        {
            if (entity instanceof Character)
            {
                ((Character) entity).updates();
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
                    if (animatedTiles.containsKey(layer.getCell(x, y).getTile().getId()-1))
                    {
                        layer.getCell(x, y).getTile().setTextureRegion(animatedTiles.get(layer.getCell(x, y).getTile().getId()-1).animate());
                    }

                    batch.draw(layer.getCell(x, y).getTile().getTextureRegion(), tmpVector.x, tmpVector.y);
                }
            }
        }
    }

    public void drawLayers(SpriteBatch batch)
    {
        // mettre à jour les entités
        updateEntities();

        for (int layerNumber = 0; layerNumber < layers.size(); layerNumber++)
        {
            drawLayer(layers.get(layerNumber), batch);
        }

        drawEntities(batch);
    }
}