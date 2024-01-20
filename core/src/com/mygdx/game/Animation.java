package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class Animation {

    private final ArrayList<TextureRegion> frames = new ArrayList<>();
    private final ArrayList<TextureRegion> spriteSheet;
    private int aniTick, aniIndex;
    private final int aniSpeed;


    public Animation(int[] framesIndex, ArrayList<TextureRegion> spriteSheet, int aniSpeed)
    {
        this.spriteSheet = spriteSheet;
        this.aniSpeed = aniSpeed;
        loadFrames(framesIndex);
    }

    private void loadFrames(int[] framesIndex)
    {

        int start = 0;
        int end = 0;

        if (framesIndex.length == 2)
        {
            for(int i = 0; i < 2; i++)
            {
                if (i == 0)
                {
                    start = framesIndex[i];
                }
                else {
                    end = framesIndex[i];
                }
            }
        }

        for(int i = start; i <= end; i++)
        {
            frames.add(spriteSheet.get(i));
        }
    }

    public TextureRegion animate()
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

        return frames.get(aniIndex);
    }

}