package com.etk2000.ModLoading;

import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.SpaceGame.GameBase;
import com.etk2000.gui.GUIFont;

public class ModHealthThread extends Mod {
	public ModHealthThread() {
		super("Player Position Indicator", 0.1f);
		// Only This Is Needed In The Constructor: name, version
	}

	@Override
	// Override The Init
	public void Init() throws Exception {// No Exception Is Thrown, But This Stays
		System.out.println(this.getName() + " Started Init");
		// SideThread.registerThread(run);
		// ^ java.lang.RuntimeException: No OpenGL context found in the current thread. ^
		GameBase.addToRenderThread(run);// this works :)
	}

	@Override
	// Override The Exit
	public void Exit() {
		GameBase.removeFromRenderThread(run);// cleanup, not needed, but helpful
	}

	/** Thread **/
	Runnable run = new Runnable() {
		@Override
		public void run() {
			GUIFont.center(GameBase.font, DisplayHandler.width / 2, DisplayHandler.height - 50,
					(int) GameBase.player.getX() + ", " + (int) GameBase.player.getY());
		}
	};
}