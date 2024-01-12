package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.*;

public class Game extends ApplicationAdapter {
	public static OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	public static TiledMap tiledMap;
	private static final float CAMERA_SPEED = 200f;
	public static Player player;
	public static Level1 level1;

	@Override
	public void create() {

		// création de la caméra
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = 0.5f;
		camera.position.x -= 200;
		camera.position.y -= 120;

		// initialisation de spriteBatch
		batch = new SpriteBatch();

		// initialisation du shapeRenderer
		shapeRenderer = new ShapeRenderer();

		// Charger la carte TMX
		TmxMapLoader mapLoader = new TmxMapLoader();
		tiledMap = mapLoader.load("maps/test.tmx");

		// création du joueur
		player = new Player();

		// création du level1
		level1 = new Level1();
	}

	@Override
	public void render() {

		// fond noir
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// recentrer la caméra en fonction du joueur
		float newCameraX = player.x;
		float newCameraY = player.y;

		camera.position.set(newCameraX, newCameraY, 0);
		camera.update();

		// dessin des objets texture
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		level1.drawLayers(batch);

		batch.end();

		// dessin des collisions
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

		// level1.drawCollisions(shapeRenderer);
		// shapeRenderer.rect(player.collisionFoot.x, player.collisionFoot.y, player.collisionFoot.width, player.collisionFoot.height);

		shapeRenderer.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		tiledMap.dispose();
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
