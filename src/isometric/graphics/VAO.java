package isometric.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class VAO {
	
	private int id;
	private Model model;
	@SuppressWarnings("unused")
	private VBO vertVBO, uvVBO, normVBO;
	private float[] vertData, uvData, normData;
	
	public VAO(int m) {
		model = Model.models[m];

		ArrayList<Vector3f> vertices = model.getFinalVertices();
		ArrayList<Vector2f> UVs = model.getFinalUVs();
		ArrayList<Vector3f> normals = model.getFinalNormals();
		vertData = new float[vertices.size() * 3];
		for (int i = 0; i < vertData.length; i += 3) {
			vertData[i] = vertices.get(i / 3).x;
			vertData[i + 1] = vertices.get(i / 3).y;
			vertData[i + 2] = vertices.get(i / 3).z;
		}
		uvData = new float[UVs.size() * 2];
		for (int i = 0; i < uvData.length; i += 2) {
			uvData[i] = UVs.get(i / 2).x;
			uvData[i + 1] = UVs.get(i / 2).y;
		}
		normData = new float[normals.size() * 3];
		for (int i = 0; i < normData.length; i += 3) {
			normData[i] = normals.get(i / 3).x;
			normData[i + 1] = normals.get(i / 3).y;
			normData[i + 2] = normals.get(i / 3).z;
		}
		
		id = glGenVertexArrays();
		glBindVertexArray(id);
		{
			vertVBO = new VBO(0, vertData, 3);
			uvVBO   = new VBO(1, uvData, 2);
			normVBO = new VBO(2, normData, 3);
		}
		glBindVertexArray(0);
	}
	
	public void render() {
		glBindVertexArray(id);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, vertData.length / 3);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}
	
	/**
	 * Creates a VAO array and fills it with the models loaded in the Model class. 
	 * @return
	 */
	
	public static VAO[] createVAOArray() {
		ArrayList<Integer> models = new ArrayList<Integer>();
		for (int i = 0; i < Model.models.length; i++) {
			if (Model.models[i] == null) break;
			models.add(i);
		}
		VAO[] r = new VAO[models.size()];
		for (int i = 0; i < models.size(); i++) {
			r[i] = new VAO(i);
			System.out.println(i);
		}
		return r;
	}
	
}
