package isometric.shaders;

import java.nio.FloatBuffer;
import java.util.Scanner;

import org.lwjgl.opengl.*;

public class Shaders {
	
	public static int SHADERCommon		= load("Common");
	public static int SHADERFlat		= load("Flat");
	public static int SHADERBlend		= load("Blend");
	
	public static int currentShader = SHADERCommon;
	
	private static int load(String name) {
		int vertShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER), 
			fragShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		
		GL20.glShaderSource(vertShader, getFileAsString("/isometric/shaders/" + name + ".vsh"));
		GL20.glShaderSource(fragShader, getFileAsString("/isometric/shaders/" + name + ".fsh"));
		GL20.glCompileShader(vertShader);
		GL20.glCompileShader(fragShader);
		
		int program = GL20.glCreateProgram();
		
		GL20.glAttachShader(program, vertShader);
		GL20.glAttachShader(program, fragShader);
		
		GL20.glLinkProgram(program);
		
		System.out.println(GL20.glGetShaderInfoLog(fragShader, 512).trim());
		System.out.println(GL20.glGetShaderInfoLog(vertShader, 512).trim());
		GL20.glValidateProgram(program);
		
		return program;
	}
	
	private static String getFileAsString(String file) {
		StringBuilder sum = new StringBuilder();
		
		Scanner scan = new Scanner(Shaders.class.getResourceAsStream(file));
		
		while (scan.hasNext()) {
			sum.append(scan.nextLine());
			sum.append("\n");
		}
		
		scan.close();
		
		return sum.toString();
	}
	
	public static void setShader(int shader) {
		currentShader = shader;
		GL20.glUseProgram(shader);
	}
	
	public static void setParam1i(String name, int i) {
		int path = GL20.glGetUniformLocation(Shaders.currentShader, name);
		GL20.glUniform1i(path, i);
	}
	
	public static void setParam1f(String name, float f) {
		int path = GL20.glGetUniformLocation(Shaders.currentShader, name);
		GL20.glUniform1f(path, f);
	}
	
	public static void setParam2f(String name, float x, float y) {
		int path = GL20.glGetUniformLocation(Shaders.currentShader, name);
		GL20.glUniform2f(path, x, y);
	}
	
	public static void setParam3f(String name, float x, float y, float z) {
		int path = GL20.glGetUniformLocation(Shaders.currentShader, name);
		GL20.glUniform3f(path, x, y, z);
	}
	
	public static void setParam4f(String name, float x, float y, float z, float a) {
		int path = GL20.glGetUniformLocation(Shaders.currentShader, name);
		GL20.glUniform4f(path, x, y, z, a);
	}
	
	public static void setParam4m(String name, FloatBuffer fb) {
		int path = GL20.glGetUniformLocation(Shaders.currentShader, name);
		GL20.glUniformMatrix4(path, false, fb);
	}
	
}
