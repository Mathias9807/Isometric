package isometric;

import static org.lwjgl.input.Keyboard.*;

public class Panel {
	
	public static double time = 0;
	
	public static boolean K_EXIT  = false;
	public static boolean K_UP    = false;
	public static boolean K_DOWN  = false;
	public static boolean K_LEFT  = false;
	public static boolean K_RIGHT = false;
	public static boolean K_1 	  = false;
	public static boolean K_2 	  = false;
	
	public static boolean tick() {
		control();
		GameHandler.tickGame();
		if (K_EXIT) return false;
		return true;
	}
	
	private static void control() {
		if (isKeyDown(KEY_ESCAPE)) 	K_EXIT 		= true;
		else						K_EXIT 		= false;
		if (isKeyDown(KEY_W)) 		K_UP 		= true;
		else						K_UP 		= false;
		if (isKeyDown(KEY_A)) 		K_LEFT 		= true;
		else						K_LEFT 		= false;
		if (isKeyDown(KEY_S)) 		K_DOWN 		= true;
		else						K_DOWN 		= false;
		if (isKeyDown(KEY_D)) 		K_RIGHT 	= true;
		else						K_RIGHT 	= false;
		if (isKeyDown(KEY_1)) 		K_1 		= true;
		else						K_1 		= false;
		if (isKeyDown(KEY_2)) 		K_2 		= true;
		else						K_2 		= false;
	}
	
}
