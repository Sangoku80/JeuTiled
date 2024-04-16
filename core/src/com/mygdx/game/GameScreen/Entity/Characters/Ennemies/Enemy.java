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
    protected static int pursing=0;
    protected static int idle=1;

    // cercle de détection du joueur
    protected Circle circleAttack;

    // position du joueur
    protected Vector2 playerPosition;

    public Enemy(int x, int y, World currentWorld) {
        super("enemy", new Vector2(x, y), 2f, 20, 2, currentWorld);

        // cercle de détection du joueur
        this.circleAttack = new Circle();

        // joueur
        // this.playerPosition = Game.currentLevel.player.position;
    }

    @Override
    public void loadAnimations() {

        Animation.setSpriteSheet_Tileset(spriteSheet);

        // en mouvement
        animations.put(DOWN+MOVE, (new Animation(new int[]{140, 141},15)));
        animations.put(LEFT+MOVE, (new Animation(new int[]{144, 145}, 15)));
        animations.put(UP+MOVE, (new Animation(new int[]{142, 143}, 15)));
        animations.put(RIGHT+MOVE, (new Animation(new int[]{144, 145}, 15)));

        // sans mouvement
        animations.put(DOWN+IDLE, (new Animation(new int[]{136, 136}, 15)));
        animations.put(LEFT+IDLE, (new Animation(new int[]{138, 138}, 15)));
        animations.put(UP+IDLE, (new Animation(new int[]{137, 137}, 15)));
        animations.put(RIGHT+IDLE, (new Animation(new int[]{138, 138}, 15)));
    }

    public void drawCircleAttack()
    {
        shapeRenderer.circle(circleAttack.x, circleAttack.y, circleAttack.radius);
    }

    public void checkCollisionWithCircleAttack()
    {
        if (Intersector.overlaps(circleAttack, Game.currentLevel.player.rect))
        {
            status = pursing;
        }
        else
        {
            status = idle;
        }
    }

    public Vector2 pursuing()
    {
        return new Vector2(playerPosition.x-position.x, playerPosition.y-position.y);
    }

    @Override
    public void update() {

        // mettre à jour la position du cercle
        circleAttack.setPosition((position.x+ (float) width /2), (position.y+ (float) height /2));
        circleAttack.setRadius(50);

        // vérifier collision entre joueur et cercle
        checkCollisionWithCircleAttack();

        // directions

    }
}
