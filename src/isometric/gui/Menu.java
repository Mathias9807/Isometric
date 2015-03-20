package isometric.gui;

import static isometric.shaders.Shaders.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.*;
import org.newdawn.slick.opengl.Texture;

import isometric.GameHandler;
import isometric.graphics.*;

public class Menu {
	
	public static Menu activeMenu = new Menu();
	
	protected static Texture 	font 			= Textures.loadTexture("/textures/gui/font.png", false);
	protected static String 	alphabet 		= "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ.,:;+-*/=\\" + 
												  "abcdefghijklmnopqrstuvwxyzåäö1234567890";
	protected static int 		alphabetLength 	= 39;
	
	public void render() {
		renderString("Wood: " + GameHandler.player.wood, new Vector2f(0, Display.getHeight()), 4);
	}
	
	public static void renderString(String text, Vector2f pos, float scale) {
		renderString(text, pos, new Vector3f(1, 1, 1), scale);
	}
	
	public static void renderString(String text, Vector2f pos, Vector3f color, float scale) {
		for (int i = 0; i < text.length(); i++) {
			int xOffs = alphabet.indexOf(text.charAt(i)) % alphabetLength;
			int yOffs = alphabet.indexOf(text.charAt(i)) / alphabetLength;
			renderSubTex(new Vector2f(pos.x + 6 * i * scale, pos.y), 
					new Vector2f(6 * scale, 9 * scale), 
					new Vector2f(6, 9), 
					new Vector2f(6 * xOffs, 9 * yOffs), color, font);
		}
	}
	
	public static void renderTex(Vector2f pos, Vector2f size, Texture t) {
		setParam3f("tint", 1, 1, 1);
		Textures.setTexture(t.getTextureID(), 5);
		setParam3f("position_m", pos.x / Display.getWidth() * 2 - 1, pos.y / Display.getHeight() * 2 - 1, 0);
		setParam3f("scale_m", 2f / Display.getWidth() * size.x, 2f / Display.getHeight() * size.y, 1);
		setParam2f("uv_m", 1, 1);
		setParam2f("uv_p", 0, 0);
		Render.VAOArray[2].render();
	}
	
	public static void renderSubTex(Vector2f pos, Vector2f size, Vector2f sub, Vector2f subPos, Texture t) {
		renderSubTex(pos, size, sub, subPos, new Vector3f(1, 1, 1), t);
	}
	
	public static void renderSubTex(Vector2f pos, Vector2f size, Vector2f sub, Vector2f subPos, Vector3f color, Texture t) {
		setParam3f("tint", color.x, color.y, color.z);
		Textures.setTexture(t.getTextureID(), 5);
		setParam3f("position_m", pos.x / Display.getWidth() * 2 - 1, pos.y / Display.getHeight() * 2 - 1, 0);
		setParam3f("scale_m", 2f / Display.getWidth() * size.x, 2f / Display.getHeight() * size.y, 1);
		setParam2f("uv_m", (float) sub.x / t.getImageWidth(), (float) sub.y / t.getImageHeight());
		setParam2f("uv_p", (float) subPos.x / t.getImageWidth(), (float) subPos.y / t.getImageHeight());
		Render.VAOArray[2].render();
	}
	
}
