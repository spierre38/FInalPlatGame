package com.badlogic.gdx.knightwatch;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.knightwatch.MainGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
                config.useVsync(true);
                config.setWindowedMode(960, 640);

		config.setTitle("platformergame");
		new Lwjgl3Application(new MainGame(), config);
	}
}
