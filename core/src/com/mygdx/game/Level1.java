package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Level1 extends World {

    public Level1()
    {
        super("Images/Tileset.png", "maps/test.tmx", 8, 8);
    }

    @Override
    public void loadLayers() {

        layers.add((TiledMapTileLayer) tiledMap.getLayers().get("fond"));
        layers.add((TiledMapTileLayer) tiledMap.getLayers().get("végétations"));
        layers.add((TiledMapTileLayer) tiledMap.getLayers().get("maison_bas"));
        layers.add((TiledMapTileLayer) tiledMap.getLayers().get("maison_haut"));
    }
}
