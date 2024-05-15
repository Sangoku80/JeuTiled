package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Game;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Tools.Animation;
import com.mygdx.game.GameScreen.Worlds.World;

import java.util.ArrayList;

import static com.mygdx.game.Game.*;

public class Enemy extends Character {

    // status
    protected static int status;
    protected static int Pursing=0;
    protected static int Idle=1;

    // cercle de détection du joueur
    protected Circle circleAttack;

    // compteur
    long currentTime = 0;
    float deltaTime = 0;
    long lastTime = 0;

    // ancienne position
    protected Vector2 lastPosition;

    // liste de positions du joueur
    protected ArrayList<Vector2> playerPositions = new ArrayList<>();

    public Enemy(int x, int y, World currentWorld) {
        super("enemy", new Vector2(x, y), 2f, 20, 2, currentWorld, true);

        // cercle de détection du joueur
        this.circleAttack = new Circle();

        // mettre l'ennemie en idle
        status = Idle;
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
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(circleAttack.x, circleAttack.y, circleAttack.radius);
        shapeRenderer.end();
    }

    public void checkCollisionWithCircleAttack()
    {
        if (Intersector.overlaps(circleAttack, Game.currentLevel.player.rect))
        {
            status = Pursing;
        }
    }

    public void setupPursuing()
    {
        playerPositions.add(position);
    }

    public void pursuing()
    {
        // décalage horaire actuel et passé
        deltaTime = (currentTime - lastTime) / 1000f;

        // Position du point cible
        Vector2 targetPosition = playerPositions.get(playerPositions.size()-1);

        // Calcul du vecteur direction
        Vector2 directionVector = new Vector2(targetPosition.x - position.x, targetPosition.y - position.y);

        // Calcul de l'angle en radians entre les deux points
        float angleRad = directionVector.angleRad();

        // Convertit l'angle en degrés
        float angleDeg = (float) Math.toDegrees(angleRad);

        // mettre à jour la direction
        direction = (int) angleDeg;
        moving = true;


        // si on est 0.3 seconde
        if (deltaTime >= 0.5)
        {
            // on remet à jour le premier temps
            lastTime = currentTime;

            // vérifier si le joueur à bouger
            lastPosition = position;

            // on vérifie s'il est arrivé à sa destination
            if(position.x >= targetPosition.x && position.y >= targetPosition.y)
            {
                // mise à jour de la liste des positions
                playerPositions.remove(targetPosition);
                playerPositions.add(position);
            }
        }
    }

    @Override
    public void update()
    {
        // mise à jour du temps actuel en millisecondes
        currentTime = TimeUtils.millis();

        // mettre à jour la position du cercle
        circleAttack.setPosition((position.x+ (float) width /2), (position.y+ (float) height /2));
        circleAttack.setRadius(50);

        // vérifier collision entre joueur et cercle
        checkCollisionWithCircleAttack();

        // directions
        if (status==Pursing)
        {
            setupPursuing();
            pursuing();
        }
    }
}
