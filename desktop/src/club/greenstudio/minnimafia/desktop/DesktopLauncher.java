package club.greenstudio.minnimafia.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import club.greenstudio.minnimafia.MinniMafia;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MinniMafia.WIDTH;
		config.height = MinniMafia.HEIGHT;
		config.title = MinniMafia.TITLE;
		new LwjglApplication(new MinniMafia(), config);
	}
}
