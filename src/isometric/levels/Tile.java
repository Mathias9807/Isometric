package isometric.levels;

import isometric.entities.structures.Building;

public class Tile {
	
	public Chunk	chunk;
	public byte 	type = 0;
	public float 	height = 0;
	
	protected Building 	build;
	
	public Tile(Chunk c) {
		chunk = c;
	}
	
	public void setBuilding(Building b) {
		chunk.entities.remove(build);
		build = b;
		if (b != null) chunk.entities.add(b);
	}
	
	public Building getBuilding() {
		return build;
	}
	
	public String toString() {
		return "Type: " + type + ", Height: " + height;
	}
	
}
