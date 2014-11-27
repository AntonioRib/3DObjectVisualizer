package objects;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.util.gl2.GLUT;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * @author M. Próspero (Updated to JOGL2 by Fernando Birra)
 */

public class BuildObject implements GLEventListener {
	
	public enum displayTypes { 
		PRINCIPAL, LATERAL_ESQ, LATERAL_DIR, 
		PLANTA, PROJ_OBL, PROJ_AXON, PROJ_PRESP;
	}
	
	public enum renderTypes { 
		SOLID, WIREFRAME, WIRESOLID;
	}

	public static int VIEWPORTNUMBER = 4;
	public static int GRID_SIDE = 1;
	
	private Shape obj;
	private int width , height;
	private String path;
	
	public BuildObject(int width, int height) {
		this.width = width;
		this.height = height;
		this.path = null;
		this.obj = null;
	}
	
	public BuildObject(int width, int height, String path) {
		this.width = width;
		this.height = height;
		this.path = path;
		this.obj = null;
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		double aRatio = width / (double)height; 
	    	    
	    gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glClearColor(0.3f, 0.3f, 0.3f, 0.0f);
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	   
		
	    if (width <= height)
	    	gl.glOrtho (-1.0, 1.0, -1.0 / aRatio, 1.0 / aRatio, -2.0, 2.0);
	    else
	    	gl.glOrtho (-1.0*aRatio, 1.0*aRatio, -1.0, 1.0, -2.0, 2.0);
	    
		gl.glViewport(0, (height/2), (width/2), (height/2));
		displayScene(gl, displayTypes.PRINCIPAL);
				
		gl.glViewport((width/2), (height/2), (width/2), (height/2));
		displayScene(gl, displayTypes.LATERAL_ESQ);
		
		gl.glViewport(0, 0, (width/2), (height/2));
		displayScene(gl, displayTypes.PRINCIPAL);
		
		gl.glViewport((width/2), 0, (width/2), (height/2));
		displayScene(gl, displayTypes.PRINCIPAL);
	    
	    gl.glFlush() ;
	}
	
	private void drawFloor(GL2 gl){
		gl.glColor3d(1.0, 1.0, 1.0);
		float min;
		if(obj != null)
			min = obj.getyMin()/obj.getMaxAbs();
		else
			min = 0;
		
		gl.glBegin(GL_LINES);
		for(float i=-GRID_SIDE; i<GRID_SIDE; i += 0.1f){
	        gl.glVertex3f(i, min, -GRID_SIDE);
	        gl.glVertex3f(i, min, GRID_SIDE);
	 
	        gl.glVertex3f(-GRID_SIDE, min, i);
	        gl.glVertex3f(GRID_SIDE, min,  i);
		}
		gl.glEnd();
	}
	
	private void drawObj(GL2 gl){
		for(Face f : obj.getFaces()){
			gl.glBegin(GL_POLYGON);
			for(Point3D p : f.getPoints()){
				gl.glVertex3f(p.getX()/obj.getMaxAbs(), p.getY()/obj.getMaxAbs(), p.getZ()/obj.getMaxAbs());
//				System.out.println("X: "+p.getX()/obj.getMaxAbs()+" Y: "+p.getY()/obj.getMaxAbs()+" Z: "+p.getZ()/obj.getMaxAbs());
			}
			gl.glEnd();
		}
//		System.out.println();
	}
	
	private void displayScene(GL2 gl, displayTypes type) {	
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		displayFloor(gl, type);
		if(obj != null){
			switch (type){
				case PRINCIPAL:
					gl.glTranslatef(-(obj.getxCenter()/obj.getMaxAbs()), 0, 0);
					break;
				case LATERAL_ESQ:
					gl.glTranslatef(0, 0, -(obj.getzCenter()/obj.getMaxAbs()));
					break;
				case LATERAL_DIR:
					break;
				case PLANTA: 
					break;
				case PROJ_OBL: 
					break;
				case PROJ_AXON: 
					break;
				case PROJ_PRESP: 
					break;
			}
			renderScene(gl, renderTypes.WIRESOLID);
		}
	}
	
	private void displayFloor(GL2 gl, displayTypes type){
		GLUT glu = new GLUT() ;
		switch (type){
			case PRINCIPAL:
				break;
			case LATERAL_ESQ:
				gl.glRotatef(90, 0, 1, 0);
				break;
			case LATERAL_DIR:
				break;
			case PLANTA: 
				//gl.glRotatef(90, 1, 0, 0);
				break;
			case PROJ_OBL: 
				break;
			case PROJ_AXON: 
				break;
			case PROJ_PRESP: 
				break;
		}
		drawFloor(gl);
	}
	
	private void renderScene(GL2 gl, renderTypes type){
		switch (type){
			case SOLID:
				gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL );
				gl.glColor3d(1.0, 1.0, 1.0);
				drawObj(gl);
				break;
			case WIREFRAME:
				gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
				gl.glColor3d(1.0, 0.0, 0.0);
				drawObj(gl);
				break;
			case WIRESOLID:
				gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL );
				gl.glEnable( GL_POLYGON_OFFSET_FILL );
				gl.glPolygonOffset( 1, 1 );
				gl.glColor3d(1.0, 1.0, 1.0);
				drawObj(gl);
				gl.glDisable(GL_POLYGON_OFFSET_FILL);
	
				gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
				gl.glColor3d(1.0, 0.0, 0.0);
				drawObj(gl);
				break;
		}
	}
	
	@Override
	public void dispose(GLAutoDrawable arg0) { }

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL_DEPTH_TEST);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		this.width = width; this.height = height;
	}
	

	/*** GETTERS ***/

	public Shape getObj() {
		return obj;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getPath() {
		return path;
	}
	
	/*** SETTERS ***/

	public void setPath(String path) {
		this.path = path;
		if(path == null)
			obj = null;
		else
			obj = new Shape(path);
	}
	
}
