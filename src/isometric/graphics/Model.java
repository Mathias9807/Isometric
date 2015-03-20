package isometric.graphics;

import java.io.*;
import java.util.*;

import org.lwjgl.util.vector.*;

public class Model implements Runnable {
	
	public static Model[] models;
	
	private volatile ArrayList<Vector3f> outVertices;
	private volatile ArrayList<Vector2f> outUvs;
	private volatile ArrayList<Vector3f> outNormals;
	private volatile ArrayList<Vector3f> finalVertices;
	private volatile ArrayList<Vector3f> finalNormals;
	private volatile ArrayList<Vector2f> finalUVs;
	private volatile ArrayList<Integer>  faces;
	
	private volatile Scanner s;
	
	private volatile int list;
	
	public volatile boolean done = false;
	
	private volatile InputStream is;
	
	public static void loadModels() {
		ArrayList<Model> tempArray = new ArrayList<Model>();
		tempArray.add(new Model(Model.class.getResourceAsStream("/models/cube.obj")));
		tempArray.add(new Model(Model.class.getResourceAsStream("/models/sphere.obj")));
		tempArray.add(new Model(Model.class.getResourceAsStream("/models/ground.obj")));
		tempArray.add(new Model(Model.class.getResourceAsStream("/models/tree.obj")));
		tempArray.add(new Model(Model.class.getResourceAsStream("/models/house.obj")));
		models = new Model[tempArray.size()];
		tempArray.toArray(models);
	}

	public Model(InputStream is)  {
		this.is = is;
		new Thread(this).start();
	}

	public Model(String path)  {
		this.is = Model.class.getResourceAsStream(path);
		new Thread(this).start();
	}
	
	public void run() {
		decodeOBJ(is);
		done = true;
	}
	
	private synchronized void decodeOBJ(InputStream is) {
		s = new Scanner(is);
		
		outVertices 	= new ArrayList<Vector3f>();
		outUvs 			= new ArrayList<Vector2f>();
		outNormals		= new ArrayList<Vector3f>();
		finalVertices	= new ArrayList<Vector3f>();
		finalNormals	= new ArrayList<Vector3f>();
		finalUVs		= new ArrayList<Vector2f>();
		faces			= new ArrayList<Integer>();
		
		String line;
		while (true) {
			if (s.hasNext()) line = s.nextLine();
			else break;
			
			if (!line.startsWith("v") && !line.startsWith("f")) continue;
			
			Scanner lineScan = new Scanner(line);
			String type = lineScan.next();
			
			if (type.equals("v")) {
				outVertices.add(new Vector3f(
						Float.parseFloat(lineScan.next()), 
						Float.parseFloat(lineScan.next()), 
						Float.parseFloat(lineScan.next())));
			}else if (type.equals("vt")) {
				outUvs.add(new Vector2f(
						Float.parseFloat(lineScan.next()), 
						Float.parseFloat(lineScan.next())));
			}else if (type.equals("vn")) {
				outNormals.add(new Vector3f(
						Float.parseFloat(lineScan.next()), 
						Float.parseFloat(lineScan.next()), 
						Float.parseFloat(lineScan.next())));
			}else if (type.equals("f")) {
				String[] indices = new String[3];
				indices[0] = lineScan.next();
				indices[1] = lineScan.next();
				indices[2] = lineScan.next();
				
				Scanner wordScan = null;
				
				for (int i = 0; i < 3; i++) { // Loops through the vertex, the normal and the UV coordinates.
					wordScan = new Scanner(indices[i]);
					wordScan.useDelimiter("/");
					
					faces.add(Integer.parseInt(wordScan.next()));
					faces.add(Integer.parseInt(wordScan.next()));
					faces.add(Integer.parseInt(wordScan.next()));
					wordScan.close();
				}
			}
			lineScan.close();
		}
		s.close();
		try {
			is.close();
		}catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < faces.size(); i += 3) {
			finalVertices.add(outVertices.get(faces.get(i) - 1));
			finalUVs.add(outUvs.get(faces.get(i + 1) - 1));
			finalNormals.add(outNormals.get(faces.get(i + 2) - 1));
			
		}
	}
	
	public int getList() {
		return list;
	}
	
	public ArrayList<Vector3f> getVertices() {
		while (!done)
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return outVertices;
	}
	
	public ArrayList<Vector2f> getUVs() {
		while (!done)
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return outUvs;
	}
	
	public ArrayList<Vector3f> getNormals() {
		while (!done)
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return outNormals;
	}
	
	public ArrayList<Vector3f> getFinalVertices() {
		while (!done)
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return finalVertices;
	}
	
	public ArrayList<Vector2f> getFinalUVs() {
		while (!done)
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return finalUVs;
	}
	
	public ArrayList<Vector3f> getFinalNormals() {
		while (!done)
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return finalNormals;
	}

}
