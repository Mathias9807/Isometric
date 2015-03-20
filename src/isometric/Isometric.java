package isometric;

import static org.lwjgl.opengl.GL11.*;
import isometric.graphics.Render;
import isometric.levels.Level;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

public class Isometric {
	
	private Render render;
	private Profiler p;
	
	public Isometric() {
		p = new Profiler();
		p.startTimer();
		
		render = new Render();
		Level.initLevel();
		GameHandler.initGame();
		p.print("Initialization: ");
		
		loop();
		
		shutdown();
	}

	public void loop() {
		long lastTime = System.nanoTime();
		double delta = 0, framerate = 60, frames = 0;
		double ns = 1000000000.0 / framerate;
		int fps = 0;
		while (!Display.isCloseRequested()) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				if (!Panel.tick()) break;
				Panel.time += 1 / framerate;
				delta--;
				frames++;
			}
			p.startTimer();
			render.update();
			Display.update();
			Display.setTitle(Render.TITLE + " | " + p.timePassedString());
			fps++;
			
			while (frames >= framerate) {
				System.out.println(fps + " fps");
				fps = 0;
				frames = 0;
				GameHandler.longTick();
			}
			
			int error = glGetError();
			if (error != GL_NO_ERROR) Display.setTitle(GLU.gluErrorString(error));
		}
	}
	
	private void shutdown() {
		Display.destroy();
	}

	public static void main(String[] args) {
		new Isometric();
	}

}
