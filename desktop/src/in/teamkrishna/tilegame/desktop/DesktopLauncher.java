package in.teamkrishna.tilegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import in.teamkrishna.tilegame.TileGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width=458;
		config.height=343;
		new LwjglApplication(new TileGame(), config);
	}
}
