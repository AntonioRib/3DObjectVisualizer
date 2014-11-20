import java.util.Vector;


public class Face {

	 private Vector<Point3D> points;
	 
	 public Face(){
		 points = new Vector<Point3D>();
	 }
	 
	 /*** METHODS ***/
	 
	 public void addPoint(float x, float y, float z){
		 points.add(new Point3D(x, y, z));
	 }
	 
	 public void addPoint(Point3D p){
		 points.add(p);
	 }
	 
	 /*** GETTERS ***/
	 
	 public Vector<Point3D> getPoints(){
		 return points;
	 }
	 
	 
	 /*** SETTERS ***/
}