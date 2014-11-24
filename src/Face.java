import java.util.Vector;


public class Face {

	 private Vector<Point3D> points;
	 private int nPoints;
	 
	 public Face(){
		 points = new Vector<Point3D>();
	 }
	 
	 /*** METHODS ***/
	 
	 public void addPoint(float x, float y, float z){
		 points.add(new Point3D(x, y, z));
		 nPoints++;
	 }
	 
	 public void addPoint(Point3D p){
		 points.add(p);
		 nPoints++;
	 }
	 
	 /*** GETTERS ***/
	 
	 public Vector<Point3D> getPoints(){
		 return points;
	 }
	 
	 public int getNumberPoints(){
		 return nPoints;
	 }
	 
	 
	 
	 /*** SETTERS ***/
}