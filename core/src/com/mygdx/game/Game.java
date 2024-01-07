package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Game extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	public static TiledMap tiledMap;
	private OrthogonalTiledMapRenderer renderer;
	private static final float CAMERA_SPEED = 200f;
	private Player player;

	@Override
	public void create() {

		// création de la caméra
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom -= 0.5f;
		camera.position.x -= 300;
		camera.position.y -= 150;

		// initialisation de spriteBatch
		batch = new SpriteBatch();

		// Charger la carte TMX
		TmxMapLoader mapLoader = new TmxMapLoader();
		tiledMap = mapLoader.load("maps/test.tmx");

		// initialisation du renderer
		renderer = new OrthogonalTiledMapRenderer(tiledMap);

		// création du joueur
		player = new Player();
	}

	@Override
	public void render() {

		// fond noir
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// dessin de la carte
		renderer.setView(camera);
		renderer.render();

		// dessin des objets texture
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		player.Draw(batch);

		batch.end();

		// mise à jour de la caméra
		camera.update();

	}

	@Override
	public void dispose() {
		batch.dispose();
		tiledMap.dispose();
		renderer.dispose();
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

