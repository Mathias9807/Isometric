package isometric.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Textures {
	
	public static Texture[] tex = new Texture[24];
	
	public static void loadTextures() {
		long startTime = System.nanoTime();
		try {
			loadTexture("/textures/testImage.png", 				0, false);
			loadTexture("/textures/water0.png", 				1, false);
			loadTexture("/textures/water1.png", 				2, false);
			loadTexture("/textures/tree.png", 					3, false);
			loadTexture("/textures/stone.png", 					4, false);
			loadTexture("/textures/house uv.png", 				5, false);
			loadTexture("/textures/pointer.png", 				16, false);
			loadTexture("/textures/selectors/s0.png", 			20, false);
			loadTexture("/textures/selectors/s1.png", 			21, false);
			loadTexture("/textures/selectors/s2.png", 			22, false);
			loadTexture("/textures/selectors/s3.png", 			23, false);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to load textures :(");
		}
		System.out.println("Textures loaded in: " + 
				(System.nanoTime() - startTime) / 1000000d + "ms");
	}
	
	public static void loadTexture(String path, int loc, boolean linear) throws IOException {
		tex[loc] = TextureLoader.getTexture("PNG", Textures.class.getResourceAsStream(path));
		tex[loc].bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, linear ? GL_LINEAR : GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, linear ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST);
		if (linear) GL30.glGenerateMipmap(GL_TEXTURE_2D);
	}
	
	public static Texture loadTexture(String path, boolean linear) {
		Texture t;
		try {
			t = TextureLoader.getTexture("PNG", Textures.class.getResourceAsStream(path));
			t.bind();
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, linear ? GL_LINEAR : GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, linear ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST);
			if (linear) GL30.glGenerateMipmap(GL_TEXTURE_2D);
			return t;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static void setTexture(int id, int loc) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + loc);
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
}
