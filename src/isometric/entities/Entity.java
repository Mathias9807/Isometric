package isometric.entities;

import org.lwjgl.util.vector.Vector2f;

public class Entity {
	
	protected int 		model = 1, texture = 0;
	protected Vector2f 	pos;
	
	public Entity(Vector2f p) {
		pos = new Vector2f(p);
	}
	
	public int model() {
		return model;
	}
	
	public int texture() {
		return texture;
	}
	
	public Vector2f pos() {
		return pos;
	}
	
}
