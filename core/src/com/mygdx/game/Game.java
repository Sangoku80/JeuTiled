package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.mygdx.game.GameScreen.Entity.Characters.Character;
import com.mygdx.game.GameScreen.Entity.Characters.Ennemies.Enemy;
import com.mygdx.game.GameScreen.Entity.Entity;
import com.mygdx.game.GameScreen.Tools.Vector2D;
import com.mygdx.game.GameScreen.Worlds.Level1;
import com.mygdx.game.GameScreen.Worlds.World;

public class Game extends ApplicationAdapter {
	public static OrthographicCamera camera;
	private SpriteBatch batch;
	public static ShapeRenderer shapeRenderer;
	public static TiledMap tiledMap;
	private static final float CAMERA_SPEED = 200f;
	public static World currentLevel;

	@Override
	public void create() {

		// création de la caméra
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = 0.2f; // (0.2)
		camera.position.x -= 200;
		camera.position.y -= 120;

		// initialisation de spriteBatch
		batch = new SpriteBatch();

		// initialisation du shapeRenderer
		shapeRenderer = new ShapeRenderer();

		// création du level1
		currentLevel = new Level1();
	}

	@Override
	public void render() {

		// fond noir
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Définissez l'InputProcessor pour détecter les événements du clavier
		Gdx.input.setInputProcessor(currentLevel.player);

		// recentrer la caméra en fonction du joueur
		float newCameraX = currentLevel.player.position.x;
		float newCameraY = currentLevel.player.position.y;

		// Limites de la caméra
		TiledMapTileLayer layer = currentLevel.layers.get(0);
		float mapWidth = layer.getWidth() * layer.getTileWidth();
		float mapHeight = layer.getHeight() * layer.getTileHeight();

		float viewportWidth = camera.viewportWidth * camera.zoom;
		float viewportHeight = camera.viewportHeight * camera.zoom;

		float cameraMinX = viewportWidth / 2;
		float cameraMaxX = mapWidth - viewportWidth / 2;
		float cameraMinY = viewportHeight / 2;
		float cameraMaxY = mapHeight - viewportHeight / 2;

		newCameraX = Math.max(cameraMinX, Math.min(newCameraX, cameraMaxX));
		newCameraY = Math.max(cameraMinY, Math.min(newCameraY, cameraMaxY));

		// mise à jour de la caméra
		camera.position.set(newCameraX, newCameraY, 0);
		camera.update();

		// mise à jour du monde
		currentLevel.update(batch);

		// affichage des FPS
		System.out.println("FPS: "+Gdx.graphics.getFramesPerSecond());

		// dessin des barres de vie
		for (Entity entity : currentLevel.entities)
		{
			if (entity instanceof Character)
			{
				((Character) entity).drawHealthBar();

				if (entity instanceof Enemy)
				{
					// test comportement AI de l'ennemi
					Vector2D steering = ((Enemy) entity).seek(currentLevel.player.position);
					((Enemy) entity).applyForce(steering);

					// ((Enemy) entity).drawCircleDetection();
					// ((Enemy) entity).drawLineOfSight();

					((Enemy) entity).draw_radar();
				}
			}
		}

		// currentLevel.player.drawCircleDetection();

		currentLevel.drawCollisions();

		handleInput(System.currentTimeMillis());
    }

	@Override
	public void dispose() {
		batch.dispose();
		tiledMap.dispose();
		shapeRenderer.dispose();
	}


	private void handleInput(float deltaTime) {
		float moveSpeed = CAMERA_SPEED * deltaTime;

		// Déplacement vers la gauche
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-moveSpeed, 0);
		}

		// Déplacement vers la droite
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(moveSpeed, 0);
		}

		// Déplacement vers le bas
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -moveSpeed);
		}

		// Déplacement vers le haut
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, moveSpeed);
		}

		// mise à jour de la caméra
		camera.update();
	}
}