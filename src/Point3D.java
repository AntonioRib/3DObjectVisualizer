
public class Point3D {

	private float x, y, z;
	
	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	
	/*** METHODS ***/
	
	public double distance(Point3D p1){
		float dx = x-p1.getX();
		float dy = y-p1.getY();
		float dz = z-p1.getZ();
		return Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
	}
	
	/*** GETTERS ***/

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	/*** SETTERS ***/
	
	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}
}
