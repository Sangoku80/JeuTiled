package com.mygdx.game.GameScreen.Worlds;

import com.mygdx.game.GameScreen.Tools.Animation;

public class Level1 extends World {

    public Level1() {
        super("Level1", "assets/Images/Tileset.png", "assets/maps/test.tmx", 8, 8);
    }

    @Override
    public void loadAnimatedTiles() {

        Animation.setSpriteSheet_Tileset(tileset);

        animatedTiles.put(487, new Animation(new int[]{487, 488}, 10000));
    }
}