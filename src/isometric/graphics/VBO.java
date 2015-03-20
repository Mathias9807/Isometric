package isometric.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

public class VBO {
	
	private FloatBuffer data;
	private float[] dataArray;
	
	private int bufferId;
	
	public VBO(int attribute, float[] buffer, int size) {
		dataArray = buffer;
		data = BufferUtils.createFloatBuffer(buffer.length);
		data.put(buffer);
		data.flip();
		
		bufferId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public FloatBuffer getData() {
		return data;
	}
	
	public float[] getDataArray() {
		return dataArray;
	}
	
	public int getId() {
		return bufferId;
	}
	
}
