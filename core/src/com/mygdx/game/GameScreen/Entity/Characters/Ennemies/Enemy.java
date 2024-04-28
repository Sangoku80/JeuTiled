package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Game;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;
import static com.mygdx.game.Game.shapeRenderer;

public class Enemy extends Character {

    // status
    protected static int status;
    protected static int Pursing=0;
    protected static int Idle=1;

    // cercle de détection du joueur
    protected Circle circleAttack;

    // position du joueur
    protected Vector2 playerPosition;

    public Enemy(int x, int y, World currentWorld) {
        super("enemy", new Vector2(x, y), 2f, 20, 2, currentWorld, false);

        // cercle de détection du joueur
        this.circleAttack = new Circle();
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        // en mouvement
        animations.put(bas+ move, (new Animation(new int[]{140, 141},15)));
        animations.put(gauche+ move, (new Animation(new int[]{144, 145}, 15)));
        animations.put(haut+ move, (new Animation(new int[]{142, 143}, 15)));
        animations.put(droite+ move, (new Animation(new int[]{144, 145}, 15)));

        // sans mouvement
        animations.put(bas+ idle, (new Animation(new int[]{136, 136}, 15)));
        animations.put(gauche+ idle, (new Animation(new int[]{138, 138}, 15)));
        animations.put(haut+ idle, (new Animation(new int[]{137, 137}, 15)));
        animations.put(droite+ idle, (new Animation(new int[]{138, 138}, 15)));
    }

    public void drawCircleAttack()
    {
        shapeRenderer.circle(circleAttack.x, circleAttack.y, circleAttack.radius);
    }

    public void checkCollisionWithCircleAttack()
    {
        if (Intersector.overlaps(circleAttack, Game.currentLevel.player.rect))
        {
            status = Pursing;
        }
        else
        {
            status = Idle;
        }
    }

    @Override
    public void update() {

        // mettre à jour la position du cercle
        circleAttack.setPosition((position.x+ (float) width /2), (position.y+ (float) height /2));
        circleAttack.setRadius(50);

        // vérifier collision entre joueur et cercle
        checkCollisionWithCircleAttack();

        // directions
        if (status==Pursing)
        {

        }
    }
}
