package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class Animation {

    private final ArrayList<TextureRegion> frames = new ArrayList<>();
    private final ArrayList<TextureRegion> spriteSheet;
    private Boolean isLoop;
    private int aniTick, aniIndex;
    private final int aniSpeed;
    public TextureRegion currentFrame;


    public Animation(int[] framesIndex, ArrayList<TextureRegion> spriteSheet, Boolean isLoop, int aniSpeed)
    {
        this.spriteSheet = spriteSheet;
        this.isLoop = isLoop;
        this.aniSpeed = aniSpeed;
        loadFrames(framesIndex);
    }

    private void loadFrames(int[] framesIndex)
    {
        for(int frameIndex : framesIndex)
        {
            frames.add(spriteSheet.get(frameIndex));
        }
    }

    public void animate()
    {

        // passer à la frame suivante
        aniTick++;

        // on prend en compte la vitesse d'animation
        if(aniTick >= aniSpeed)
        {
            aniTick = 0;

            // on change de frame
            aniIndex++;

            // on vérifie si on atteint la fin de l'animation
            if(aniIndex >= frames.size())
            {
                // on remet l'animation à zéro
                aniIndex = 0;
            }
        }

        currentFrame = frames.get(aniIndex);
    }

}
