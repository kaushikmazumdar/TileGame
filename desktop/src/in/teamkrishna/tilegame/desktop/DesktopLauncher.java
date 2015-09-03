package in.teamkrishna.tilegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import in.teamkrishna.tilegame.TileGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = TileGame.iViewPortWidth;
		config.height = TileGame.iViewPortHeight;
		new LwjglApplication(new TileGame(), config);
	}
}
