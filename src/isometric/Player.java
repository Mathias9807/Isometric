package isometric;

import isometric.entities.structures.Building;
import isometric.levels.Level;

public class Player {
	
	public int wood = 0;
	public int selected = 0;
	
	public Player() {
	}
	
	public boolean build(Building b) {
		if (b == null) return false;
		if (wood >= b.cost 
				&& Level.getWorldTile((int) b.pos().x, (int) b.pos().y).getBuilding() == null
				&& b.canBePlaced()) {
			wood -= b.cost;
			for (int x = 0; x < b.size.x; x++) {
				for (int z = 0; z < b.size.y; z++) {
					Level.getWorldTile((int) b.pos().x + x, (int) b.pos().y - z).setBuilding(b);
				}
			}
			return true;
		}
		return false;
	}
	
}
