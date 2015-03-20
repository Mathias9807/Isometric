package isometric.levels;

import isometric.entities.Entity;
import isometric.entities.structures.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class Level {
	
	public static final int MAP_WIDTH  = 16, 
							MAP_HEIGHT = 16;
	
	private static Chunk[][] chunks = new Chunk[MAP_WIDTH][MAP_HEIGHT];
	
	private static final float 	WATER_HEIGHT 		= -0.3f, 
							   	GROUND_HEIGHT		= 0, 
							   	MOUNTAIN_HEIGHT 	= 0.5f, 
								HEIGHT_VARIATION 	= 1;
	
	public static void initLevel() {
		Display.setTitle("Generating map");
		for (int x = 0; x < MAP_WIDTH; x++) {
			Display.setTitle("Generating map... " + 
					(x * 100 / MAP_HEIGHT) + "%");
			for (int y = 0; y < MAP_HEIGHT; y++) {
				chunks[x][y] = new Chunk();
				generateChunk(x, y);
			}
		}
	}
	
	private static void generateChunk(int arrayX, int arrayY) {
		// Chunk in world co-ordinates. 
		int wx = arrayX * Chunk.CHUNK_WIDTH, wy = arrayY * Chunk.CHUNK_WIDTH;
		
		Chunk current = chunks[arrayX][arrayY];
		for (int x = 0; x < Chunk.CHUNK_WIDTH; x++) {
			for (int y = 0; y < Chunk.CHUNK_WIDTH; y++) {
				Tile t = current.tiles[x][y] = new Tile(current);
				t.height = 1;
				if (wx + x != 0 && wy + y != 0) {
					t.height = (float) ((getWorldTile(wx + x - 1, wy + y).height + 
							   getWorldTile(wx + x, wy + y - 1).height) / 3f
							   + (Math.random() * 2 - 1) * HEIGHT_VARIATION);
				}else {
					t.height = 0;
				}
			}
		}
		
		smoothChunk(current);
		smoothChunk(current);
		smoothChunk(current);
		
		for (int x = 0; x < Chunk.CHUNK_WIDTH; x++) {
			for (int y = 0; y < Chunk.CHUNK_WIDTH; y++) {
				Tile t = current.tiles[x][y];
				
				if (t.height 		>= MOUNTAIN_HEIGHT) {
					t.height = (int) (t.height * 5);
					t.type 		= 2;
				}else if (t.height 	<= WATER_HEIGHT) {
					t.height 	= -1;
					t.type 		= 1;
				}else {
//					if (Math.random() > 0.8)
//						addEntity(new Tree(new Vector2f(arrayX * Chunk.CHUNK_WIDTH + x, arrayY * Chunk.CHUNK_WIDTH + y)));
					t.height 	= GROUND_HEIGHT;
					t.type 		= 0;
				}
			}
		}
	}
	
	private static void smoothChunk(Chunk c) {
		for (int x = 0; x < Chunk.CHUNK_WIDTH; x++) {
			for (int y = 0; y < Chunk.CHUNK_WIDTH; y++) {
				Tile t = c.tiles[x][y];
				
				float avgHeight = t.height;
				int max = Chunk.CHUNK_WIDTH;
				avgHeight += c.tiles[Math.max(x - 1, 0)]
									[Math.min(y + 1, max - 1)].height;
				avgHeight += c.tiles[Math.min(x + 1, max - 1)]
									[Math.min(y + 1, max - 1)].height;
				avgHeight += c.tiles[Math.min(x + 1, max - 1)]
									[Math.max(y - 1, 0)].height;
				avgHeight += c.tiles[Math.max(x - 1, 0)]
									[Math.max(y - 1, 0)].height;

				avgHeight += c.tiles[Math.max(x - 1, 0)]
						  			[y].height;
				avgHeight += c.tiles[x]
		  				  			[Math.min(y + 1, max - 1)].height;
				avgHeight += c.tiles[Math.min(x + 1, max - 1)]
		  				  			[y].height;
				avgHeight += c.tiles[x]
		  				  			[Math.max(y - 1, 0)].height;
				
				avgHeight /= 9f;
				t.height = avgHeight;
			}
		}
	}
	
	public static void addEntity(Entity e) {
		if (e instanceof Building) {
			if (getEntity((int) e.pos().x, (int) e.pos().y) != null) {
				return;
			}
			getWorldTile((int) e.pos().x, (int) e.pos().y).build = (Building) e;
		}
	}
	
	public static Entity getEntity(int x, int y) {
		int chunkX = x / Chunk.CHUNK_WIDTH;
		int chunkY = y / Chunk.CHUNK_WIDTH;
		Chunk c = chunks[chunkX][chunkY];
		return c.tiles[x % Chunk.CHUNK_WIDTH][y % Chunk.CHUNK_WIDTH].getBuilding();
	}
	
	public static Tile getWorldTile(int x, int y) {
		x = x < 0 ? 0 : x;
		x = Math.min(x, MAP_WIDTH * Chunk.CHUNK_WIDTH - 1);
		y = y < 0 ? 0 : y;
		y = Math.min(y, MAP_HEIGHT * Chunk.CHUNK_WIDTH - 1);
		return chunks[x / Chunk.CHUNK_WIDTH][y / Chunk.CHUNK_WIDTH].tiles[x % Chunk.CHUNK_WIDTH][y % Chunk.CHUNK_WIDTH];
	}
	
	public static Tile getWorldTile(Vector2f v) {
		return getWorldTile((int) v.x, (int) v.y);
	}
	
	public static Tile getWorldTile(int cx, int cy, int tx, int ty) {
		return chunks[cx][cy].tiles[tx][ty];
	}
	
	public static Chunk[][] getChunks() {
		return chunks;
	}
	
}
