package com.pewpewman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pewpewman.Pewpewman;

public class DesktopLauncher {

	public static void main(String[] args) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title  = "Pewpewman";
		config.height = 900;
		config.width  = 1650;
		config.vSyncEnabled = true;
		config.fullscreen = true;

		new LwjglApplication(new Pewpewman(), config);
	}
}

