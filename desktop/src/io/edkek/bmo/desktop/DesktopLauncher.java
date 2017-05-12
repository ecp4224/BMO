package io.edkek.bmo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.edkek.bmo.BMO;
import io.edkek.bmo.core.logic.Handler;
import io.edkek.bmo.handlers.BlankHandler;

public class DesktopLauncher {
	private static final boolean FULLSCREEN = false;
	private static final Handler HANDLER = new BlankHandler();

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "BMO";
		config.width = 800;
		config.height = 600;
		config.fullscreen = FULLSCREEN;
		BMO.setDefaultHandler(HANDLER);


		new CrashReporterApplication(BMO.getScreen(), config);
	}
}
