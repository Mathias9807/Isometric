package isometric;

import static isometric.Panel.*;
import isometric.entities.structures.*;
import isometric.graphics.Render;
import isometric.levels.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.*;

public class GameHandler {
	
	public static Vector2f 	cameraPos;
	public static Vector2f 	mousePos, mouseWorldPos;
	public static boolean  	mouseClicked = false;
	private static boolean 	mousePressedLastFrame = false;
	public static float 	cameraSpeed;
	
	public static Player 	player;
	
	private static final int[] treeKernel = { 
		0, 0, -4, 0, -2, 0,  2, 0,  4, 0, 
		       0,-4,  0,-2,  0, 2,  0, 4, 
		      -3,-3,  3,-3, -3, 3,  3, 3
	};
	
	public static void initGame() {
		Profiler p = new Profiler();
		p.startTimer();
		cameraPos 			= new Vector2f(0, 0);
		mousePos 			= new Vector2f(0, 0);
		mouseWorldPos 		= new Vector2f(0, 0);
		cameraSpeed 		= 0.1f;
		player = new Player();
		p.print("Game state set in: ");
	}
	
	public static void tickGame() {
		if (Mouse.isButtonDown(0)) {
			if (mousePressedLastFrame) {
				mouseClicked = false;
			}else {
				mouseClicked = true;
			}
			mousePressedLastFrame = true;
		}else {
			mouseClicked = false;
			mousePressedLastFrame = false;
		}
		if (Mouse.isButtonDown(1)) {
			player.selected = 0;
		}
		
		mousePos.x = Mouse.getX();
		mousePos.y = Mouse.getY();
		Vector4f mouse = new Vector4f(
				GameHandler.mousePos.x / Display.getWidth() * 2 - 1, 
				(GameHandler.mousePos.y / Display.getHeight() * 2 - 1), 
				(GameHandler.mousePos.y / Display.getHeight() * 2 - 1) / 10, 1);
		Matrix4f.transform(Matrix4f.invert(Render.projection, null), mouse, mouse);
		Matrix4f.transform(Matrix4f.invert(Render.view, null), mouse, mouse);
		mouse.z++;
		mouseWorldPos.x = mouse.x;
		mouseWorldPos.y = mouse.z;
		
		if (mouseClicked) {
			Building b = Level.getWorldTile(mouseWorldPos).getBuilding();
			if (player.selected == 0 && b != null) {
				player.wood += b.value;
				b.destroy();
			}else {
				if (player.build(buildSelected())) 
					player.selected = 0;
			}
		}
		
		if (K_1) {
			player.selected = 1;
		}else if (K_2) {
			player.selected = 2;
		}
		
		Vector2f d = new Vector2f();
		if (K_UP) 		d.y += cameraSpeed;
		if (K_DOWN) 	d.y -= cameraSpeed;
		if (K_LEFT) 	d.x += cameraSpeed;
		if (K_RIGHT) 	d.x -= cameraSpeed;
		double angle = 45 / 180.0 * Math.PI;
		cameraPos.x += (float) (d.x * Math.cos(angle) - d.y * Math.sin(angle));
		cameraPos.y += (float) (d.y * Math.cos(angle) + d.x * Math.sin(angle));
	}
	
	public static void longTick() {
		for (int i = 0; i < 10 * Level.MAP_WIDTH * Level.MAP_HEIGHT; i++) {
			Vector2f treeP = new Vector2f(
					(int) (Math.random() * Level.MAP_WIDTH * Chunk.CHUNK_WIDTH), 
					(int) (Math.random() * Level.MAP_HEIGHT * Chunk.CHUNK_WIDTH));
			if (checkTreeKernel((int) treeP.x, (int) treeP.y)) {
				Tree t = new Tree(treeP);
				if (t.canBePlaced()) 
					Level.getWorldTile(treeP).setBuilding(t);
			}
		}
	}
	
	public static Building buildSelected() {
		switch (player.selected) {
		case 1 : return new Tree(new Vector2f(mouseWorldPos));
		case 2 : return new House(new Vector2f(mouseWorldPos));
		default : return null;
		}
	}
	
	private static boolean checkTreeKernel(int x, int y) {
		for (int i = 0; i < treeKernel.length; i += 2) {
			if (!Tree.canBePlaced(new Vector2f(x + treeKernel[i], y + treeKernel[i + 1]))) 
				return false;
		}
		return true;
	}
	
}
