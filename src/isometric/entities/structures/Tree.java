package isometric.entities.structures;

import org.lwjgl.util.vector.Vector2f;

public class Tree extends Building {
	
	public Tree(Vector2f p) {
		super(new Vector2f((int) p.x, (int) p.y), new Vector2f(1, 1));
		value = 1;
		model = 3;
		texture = 3;
	}
	
}
