package isometric.entities.structures;

import isometric.entities.Entity;
import isometric.levels.Level;

import org.lwjgl.util.vector.Vector2f;

public class Building extends Entity {
	
	public int 	cost = 5, value = 1;
	public Vector2f size;

	public Building(Vector2f p, Vector2f s) {
		super(p);
		size = s;
	}
	
	public void moveTo(Vector2f v) {
		pos = new Vector2f((int) v.x, (int) v.y);
	}
	
	public void destroy() {
		for (int x = 0; x < size.x; x++) {
			for (int z = 0; z < size.y; z++) {
				Level.getWorldTile((int) pos.x + x, (int) pos.y - z).setBuilding(null);
			}
		}
	}
	
	public boolean canBePlaced() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (!canBePlaced((int) pos.x + x, (int) pos.y - y)) return false;
			}
		}
		return true;
	}
	
	public static boolean canBePlaced(Vector2f v) {
		return canBePlaced((int) v.x, (int) v.y);
	}
	
	public static boolean canBePlaced(int x, int y) {
		return Level.getWorldTile(x, y).type == 0
				&& Level.getWorldTile(x, y).getBuilding() == null;
	}
	
}
