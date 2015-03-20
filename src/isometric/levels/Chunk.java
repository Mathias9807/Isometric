package isometric.levels;

import isometric.entities.Entity;

import java.util.ArrayList;

public class Chunk {
	
	public static final int CHUNK_WIDTH = 16;
	
	public final Tile[][] tiles = new Tile[CHUNK_WIDTH][CHUNK_WIDTH];
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public Chunk() {
	}
	
	public Chunk(Tile[][] data) {
		for (int x = 0; x < CHUNK_WIDTH; x++) {
			for (int y = 0; y < CHUNK_WIDTH; y++) {
				tiles[x][y] = data[x][y];
			}
		}
	}
	
}
