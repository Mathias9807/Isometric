package isometric.entities.structures;

import org.lwjgl.util.vector.Vector2f;

public class House extends Building {
	
	public House(Vector2f p) {
		super(new Vector2f((int) p.x, (int) p.y), new Vector2f(3, 2));
		cost = 5;
		value = 3;
		model = 4;
		texture = 5;
	}
	
}
