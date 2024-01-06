package com.mygdx.game;

import com.badlogic.gdx.maps.objects.TextureMapObject;

public class Player extends Entity {

    public Player()
    {
        super((TextureMapObject) Game.tiledMap.getLayers().get("entités").getObjects().get("player"), 200, "Images/sprite sheet.png", 16);
    }

    @Override
    public void Update() {

    }
}
