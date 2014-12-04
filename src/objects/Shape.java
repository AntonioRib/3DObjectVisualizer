package objects;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Shape {

	private final static String OBJ_VERTEX = "v";
	private final static String OBJ_VERTEX_TEXTURE = "vt";
	private final static String OBJ_FACE = "f";
	
	private final static String DEFAULT_OBJ_PATH = "objects/cube.obj";

	private Vector<Point3D> points = new Vector<Point3D>();
	private Vector<Point3D> texturePoints = new Vector<Point3D>();
	private Vector<Face> faces = new Vector<Face>();
	
	private float xMin = Float.MAX_VALUE, xMax = -Float.MAX_VALUE, 
					yMin = Float.MAX_VALUE, yMax = -Float.MAX_VALUE, 
					 zMin = Float.MAX_VALUE, zMax = -Float.MAX_VALUE;
	
	private float maxAbs = -Float.MAX_VALUE; 
	
	private int nFaces = 0, nEdges = 0;  

	/*** CONTRUCTORS ***/
	
	public Shape(){
		try {
			load(new File(DEFAULT_OBJ_PATH));
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public Shape(String path){
		try {
			load(new File(path));
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	/*** GETTERS ***/

	public Vector<Point3D> getPoints() {
		return points;
	}
	
	public Vector<Point3D> getTexturePoints() {
		return texturePoints;
	}

	public Vector<Face> getFaces() {
		return faces;
	}
	
	public float getxMin() {
		return xMin;
	}

	public float getxMax() {
		return xMax;
	}
	
	public float getxCenter() {
		return (xMax+xMin)/2;
	}

	public float getyMin() {
		return yMin;
	}

	public float getyMax() {
		return yMax;
	}
	
	public float getyCenter() {
		return (yMax+yMin)/2;
	}

	public float getzMin() {
		return zMin;
	}

	public float getzMax() {
		return zMax;
	}
	
	public float getzCenter() {
		return (zMax+zMin)/2;
	}

	public float getMaxAbs() {
		return maxAbs;
	}

	/*** SETTERS ***/


	/*** LOAD ***/

	public void load(File objFile) throws IOException {

		FileReader fileReader = new FileReader(objFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		int lineCount = 0;
		int nFaces = 0;
		int vt = 0;

		String line = null;
		while (true) {
			line = bufferedReader.readLine();
			if (null == line) {
				break;
			}

			line = line.trim();

			if (line.length() == 0)
				continue;

			String tokens[] = line.split("[\t ]+");

			if (line.startsWith("#")) {
				continue;
			} else if (tokens[0].equals(OBJ_VERTEX)) {
				Point3D p = new Point3D(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				points.add(p);
				updateCoordinates(p);
			} else if (tokens[0].equals(OBJ_VERTEX_TEXTURE)) {
				Point3D p = new Point3D(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
				texturePoints.add(p);
				vt++;
			} else if (tokens[0].equals(OBJ_FACE)) {
				for (int i = 1; i < tokens.length; i++) {
					String[] insideTok = tokens[i].split("[/ ]");
					faces.add(new Face());
					faces.get(nFaces).addPoint(points.get(Integer.parseInt(insideTok[0]) - 1));
					if(insideTok.length >= 2 && !insideTok[1].equalsIgnoreCase("/") && !insideTok[1].equalsIgnoreCase("") )
						faces.get(nFaces).addTexturePoint(texturePoints.get(Integer.parseInt(insideTok[1]) - 1));
				}
				nFaces++;
			}
			lineCount++;
		}
		updateMax();
		bufferedReader.close();

		System.out.println("FaceNumber" + nFaces);
		System.err.println("Loaded " + lineCount + " lines");
		System.err.println("vt Number " + vt);
	}
	
	private void updateCoordinates(Point3D p){
		if(p.getX() < xMin) {
			xMin = p.getX();
		}
		
		if(p.getY() < yMin) {
			yMin = p.getY();
		}
		
		if(p.getZ() < zMin) {
			zMin = p.getZ();
		}
		
		if(p.getX() > xMax) {
			xMax = p.getX();
		}
		
		if(p.getY() > yMax) {
			yMax = p.getY();
		}
		
		if(p.getZ() > zMax) {
			zMax = p.getZ();
		}
	}
	
	private void updateMax(){
		float x = Math.max(Math.abs(xMax), Math.max(Math.abs(yMax), Math.abs(zMax)));
		float y = Math.max(Math.abs(xMin), Math.max(Math.abs(yMin), Math.abs(zMin)));
		maxAbs = Math.max(x, y);
	}
}
