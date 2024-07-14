package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.Tests.VoitureAI;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Jeu tiled");
		config.setForegroundFPS(120);
		config.setResizable(false);
		config.setWindowIcon("Images/game-boy.png");
		config.setWindowedMode(600, 400);
		// grand écran
		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		new Lwjgl3Application(new VoitureAI(), config);
	}
}