package isometric.graphics;

import static isometric.shaders.Shaders.*;
import static org.lwjgl.opengl.GL11.*;
import isometric.*;
import isometric.entities.Entity;
import isometric.entities.structures.*;
import isometric.gui.Menu;
import isometric.levels.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.*;

public class Render {
	
	public static final String 	TITLE = "Isometric View";
	public static int 			WIDTH = 800, HEIGHT = 600;
	public static boolean 		blending = true;
	
	private static float ratio;

	public static Matrix4f 
		projection 	= new Matrix4f(), 
		view 		= new Matrix4f();
	
	private FBO blendLayer;
	
	public static VAO[] VAOArray;
	
	public Render() {
		try {
			Profiler p = new Profiler();
			p.startTimer();
			
			openWindow();
			
			Textures.loadTextures();
			
			Model.loadModels();
			
			if (initOpenGL() != 1) System.err.println("Failed to initialize OpenGL. ");
			
			p.print("Renderer initialized and window opened in: ");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void openWindow() throws LWJGLException {
		DisplayMode dm = new DisplayMode(WIDTH, HEIGHT);
		Display.setDisplayMode(dm);
		Display.setTitle(TITLE);
		Display.setVSyncEnabled(false);
		try {
			Display.create(new PixelFormat(0, 8, 0, 8));
		}catch (Exception e) {
			try {
				Display.create(new PixelFormat(0, 8, 0, 4));
			}catch (Exception ee) {
				try {
					Display.create(new PixelFormat(0, 8, 0, 2));
				}catch (Exception eee) {
					Display.create();
				}
			}
		}

		IntBuffer mouseBuffer = BufferUtils.createIntBuffer(256);
		Mouse.setNativeCursor(new Cursor(16, 16, 0, 0, 1, mouseBuffer, null));
	}
	
	private int initOpenGL() {
		blendLayer = new FBO(WIDTH, HEIGHT).withColor(1);
		
		// Set initial shader parameters
		setShader(SHADERFlat);
		setParam1i("texture0", 5);
		
		setShader(SHADERCommon);
		setParam3f("sunDir", -0.4f, 0.8f, 0.6f);
		setParam1i("selected", 0);
		setParam1i("texture0", 0);
		setParam1i("texture1", 1);
		setParam1i("texture2", 2);
		setParam1i("texture3", 3);
		
		Textures.setTexture(Textures.tex[0].getTextureID(), 0);
		Textures.setTexture(Textures.tex[1].getTextureID(), 1);
		Textures.setTexture(Textures.tex[2].getTextureID(), 2);
		Textures.setTexture(Textures.tex[3].getTextureID(), 3);
		Textures.setTexture(Textures.tex[4].getTextureID(), 4);
		
		glEnable(GL_DEPTH_TEST);
		glAlphaFunc(GL_GREATER, 0f);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		ratio = (float) Display.getWidth() / Display.getHeight();
		double radius = 5;
		glOrtho(-radius * ratio, radius * ratio, -radius, radius, -50, 50);
		glRotated(45, 1, 0, 0);
		glRotated(45, 0, 1, 0);
		applyMatrix(projection, "proj");
		glLoadIdentity();
		applyMatrix(view, "view");
		
		// Create Vertex Array Objects from loaded models
		VAOArray = new VAO[Model.models.length];
		for (int i = 0; i < Model.models.length; i++) {
			VAOArray[i] = new VAO(i);
		}
		
		// Check for errors
		int error = glGetError();
		if (error != GL_NO_ERROR) {
			Display.setTitle(GLU.gluErrorString(error));
			return 0;
		}
		
		return 1;
	}
	
	public void update() {
		setShader(SHADERCommon);
		setParam1f("time", (float) Panel.time);
		glLoadIdentity();
		glTranslated(GameHandler.cameraPos.x, 0, GameHandler.cameraPos.y);
		applyMatrix(view, "view");
		
		FBO.setFrameBuffer(blendLayer);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0, 0, 0, 0);
		
		FBO.setFrameBuffer(null);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(1, 1, 1, 0);
		
		renderScene();
		
		glEnable(GL_ALPHA_TEST);
			setParam1i("render", 0);
			setParam3f("position_m", 
					(int) GameHandler.mouseWorldPos.x, 
					0.001f, 
					(int) GameHandler.mouseWorldPos.y);
			Textures.setTexture(Textures.tex[20].getTextureID(), 0);
			VAOArray[2].render();
			
			if (GameHandler.player.selected != 0 
					&& blending) {
				FBO.setFrameBuffer(blendLayer);
				Building b = GameHandler.buildSelected();
				setParam1i("render", 3 + (b.canBePlaced() ? 0 : 1));
				setParam3f("position_m", b.pos().x, 0, b.pos().y);
				Textures.setTexture(b.texture(), 0);
				VAOArray[b.model()].render();
				FBO.setFrameBuffer(null);
			}
		if (blending) {
			glEnable(GL_BLEND);
			setShader(SHADERBlend);
			setParam1i("texture0", 0);
			Textures.setTexture(blendLayer.getTexturesId()[0], 0);
			VAOArray[2].render();
			glDisable(GL_BLEND);
		}

		glDisable(GL_DEPTH_TEST);
		setShader(SHADERFlat);
		
		Menu.activeMenu.render();
		
		Menu.renderTex(GameHandler.mousePos, new Vector2f(16, 16), Textures.tex[16]);
		if (
				GameHandler.mouseWorldPos.x >= 0 
				&& GameHandler.mouseWorldPos.y >= 0 
				&& Level.getEntity((int) GameHandler.mouseWorldPos.x, (int) GameHandler.mouseWorldPos.y) != null) {
			Menu.renderString(Level.getEntity((int) GameHandler.mouseWorldPos.x, 
					(int) GameHandler.mouseWorldPos.y).getClass().getSimpleName(), 
					new Vector2f(GameHandler.mousePos.x + 12, GameHandler.mousePos.y - 12), 2);
		}
		
		glDisable(GL_ALPHA_TEST);
		glEnable(GL_DEPTH_TEST);
	}
	
	private void renderScene() {
		Vector4f topleft = new Vector4f(0, 0, 0, 1);
		topleft = screenToWorldCoords(topleft);
		int screenX = (int) (topleft.x / 16);
		int screenY = (int) (topleft.z / 16);
		screenX = (screenX < 1) ? 1 : screenX;
		screenX = (screenX > Level.MAP_WIDTH - 2) ? Level.MAP_WIDTH - 2 : screenX;
		screenY = (screenY < 1) ? 1 : screenY;
		screenY = (screenY > Level.MAP_HEIGHT - 2) ? Level.MAP_HEIGHT - 2 : screenY;
		
		for (int xx = screenX - 1; xx < screenX + 2; xx++) {
			for (int yy = screenY - 1; yy < screenY + 2; yy++) {
				Chunk c = Level.getChunks()[xx][yy];
				if (!isChunkVisible(c, xx, yy)) 
					continue;
				for (int x = 0; x < Chunk.CHUNK_WIDTH; x++) {
					for (int y = 0; y < Chunk.CHUNK_WIDTH; y++) {
						renderTile(c.tiles[x][y], 
								x + xx * Chunk.CHUNK_WIDTH, y + yy * Chunk.CHUNK_WIDTH);
						if (c.tiles[x][y].getBuilding() != null) 
							renderEntity(c.tiles[x][y].getBuilding());
					}
				}
			}
		}
	}
	
	private void renderTile(Tile t, int x, int z) {
		if (t.type == 0) 
			Textures.setTexture(Textures.tex[0].getTextureID(), 0);
		else 
			Textures.setTexture(Textures.tex[4].getTextureID(), 0);
		setParam1i("render", (t.type == 1) ? 1 : 0);
		setParam3f("position_m", x, t.height * 0.5f, z);
		VAOArray[0].render();
	}
	
	private void renderEntity(Entity e) {
		Textures.setTexture(Textures.tex[e.texture()].getTextureID(), 3);
		setParam1i("render", 2);
		setParam3f("position_m", e.pos().x, Level.getWorldTile((int) e.pos().x, (int) e.pos().y).height * 0.5f, e.pos().y);
		VAOArray[e.model()].render();
	}
	
	private boolean isChunkVisible(Chunk c, int x, int y) {
		Vector4f 	vnw = worldToScreenCoords(new Vector4f(x * 16 - 1, 		0, 	y * 16, 			1)), 
					vsw = worldToScreenCoords(new Vector4f(x * 16, 	   		-1, y * 16 + 16, 		1)), 
					vne = worldToScreenCoords(new Vector4f(x * 16 + 16, 	8, 	y * 16, 			1)), 
					vse = worldToScreenCoords(new Vector4f(x * 16 + 16 + 1, 0, 	y * 16 + 16 + 1, 	1));
		if (vnw.x > 1 || vse.x < -1) return false;
		if (vsw.y > 1 || vne.y < -1) return false;
		
		return true;
	}
	
	private void applyMatrix(Matrix4f m, String name) {
		FloatBuffer var0 = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_PROJECTION_MATRIX, var0);
		m.load(var0);
		var0.flip();
		setParam4m(name, var0);
	}
	
	private Vector4f worldToScreenCoords(Vector4f v) {
		Matrix4f.transform(view, v, v);
		Matrix4f.transform(projection, v, v);
		return v;
	}
	
	private Vector4f screenToWorldCoords(Vector4f v) {
		Matrix4f.transform(Matrix4f.invert(projection, null), v, v);
		Matrix4f.transform(Matrix4f.invert(view, null), v, v);
		return v;
	}
	
	
}
