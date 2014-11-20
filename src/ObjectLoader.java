import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import jogamp.opengl.util.av.impl.FFMPEGMediaPlayer;


public class ObjectLoader {
    
    private final static String OBJ_VERTEX = "v";
    private final static String OBJ_VERTEX_TEXTURE = "vt";
    private final static String OBJ_FACE = "f";
    
    private Vector<Point3D> points = new Vector<Point3D>();
    private Vector<Face> faces = new Vector<Face>();

	public void load(File objFile) throws IOException {
				
		FileReader fileReader = new FileReader(objFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		int lineCount = 0;
		int faceNumber = 0;
		
		String line = null;
		while(true) {
			line = bufferedReader.readLine();
			if(null == line) {
				break;
			}
			
			line = line.trim();
			
			if(line.length() == 0)
				continue;
			
			String tokens[] = line.split("[\t ]+");

            if(line.startsWith("#")) {
                continue;
            }
            else if(tokens[0].equals(OBJ_VERTEX)) {
                points.add(new Point3D(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
			} else if(tokens[0].equals(OBJ_VERTEX_TEXTURE)) {
				// parse texture coordinates
			}
			else if(tokens[0].equals(OBJ_FACE)) {
				for(int i=1; i<tokens.length; i++){
					faces.add(new Face());
					faces.get(faceNumber).addPoint(points.get(Integer.parseInt(tokens[i].split("[/]")[0])-1));
				}
				faceNumber++;
			}

            lineCount++;
		}
        bufferedReader.close();

        System.out.println("FaceNumber"+faceNumber);
        System.err.println("Loaded " + lineCount + " lines");
	}
    // member variables here...


	/*** GETTERS ***/
	public Vector<Point3D> getPoints() {
		return points;
	}
	
	public Vector<Face> getFaces() {
		return faces;
	}

	
	/*** SETTERS ***/
	
	

}




