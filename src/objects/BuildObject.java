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
	private GL2 gl;
	private GLU glu;
	
	private float zoomFactor = 1f;
	
	public BuildObject(int width, int height) {
		this.width = width;
		this.height = height;
		this.path = null;
		this.obj = null;
		this.gl = null;
	}
	
	public BuildObject(int width, int height, String path) {
		this.width = width;
		this.height = height;
		this.path = path;
		this.obj = null;
		this.gl = null;
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		
		double aRatio = width / (double)height; 

		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glClearColor(0.3f, 0.3f, 0.3f, 0.0f);
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	   
		
	    if (width <= height){
	    	gl.glOrtho (-1.0/zoomFactor, 1.0/zoomFactor, -1.0/zoomFactor*aRatio, 1.0/zoomFactor*aRatio, -2.0, 2.0);
	    } else {
	    	gl.glOrtho (-1.0*aRatio/zoomFactor, 1.0*aRatio/zoomFactor, -1.0/zoomFactor, 1.0/zoomFactor, -2.0, 2.0);
	    }
	    
		gl.glViewport(0, (height/2), (width/2), (height/2));
		displayScene(displayTypes.PRINCIPAL);
				
		gl.glViewport((width/2), (height/2), (width/2), (height/2));
		displayScene(displayTypes.LATERAL_ESQ);
		
		gl.glViewport(0, 0, (width/2), (height/2));
		displayScene(displayTypes.PLANTA);
		
		gl.glViewport((width/2), 0, (width/2), (height/2));
		displayScene(displayTypes.PROJ_AXON);
	    
	    gl.glFlush() ;
	}
	
	private void drawFloor(){
		gl.glColor3d(0.4, 0.8, 0.4);
		float min;
		if(obj != null)
			min = obj.getyMin()/obj.getMaxAbs();
		else
			min = 0;
		
		gl.glBegin(GL_LINES);
		for(int n=-GRID_SIDE*10; n<=GRID_SIDE*10; n++){
			float i = 0.1f*n;
	        gl.glVertex3f(i, min, -GRID_SIDE);
	        gl.glVertex3f(i, min, GRID_SIDE);
	 
	        gl.glVertex3f(-GRID_SIDE, min, i);
	        gl.glVertex3f(GRID_SIDE, min,  i);
		}
		gl.glEnd();
	}
	
	private void drawObj(){
		for(Face f : obj.getFaces()){
			gl.glBegin(GL_POLYGON);
			for(Point3D p : f.getPoints()){
				gl.glVertex3f(p.getX()/obj.getMaxAbs(), p.getY()/obj.getMaxAbs(), p.getZ()/obj.getMaxAbs());
			}
			gl.glEnd();
		}
	}
	
	private void displayScene(displayTypes type) {	
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		switch (type){
			case PRINCIPAL:
				break;
			case LATERAL_ESQ:
				gl.glRotatef(90, 0, 1, 0);
				break;
			case LATERAL_DIR:
				gl.glRotatef(-90, 0, 1, 0);
				break;
			case PLANTA: 
				gl.glRotatef(90, 1, 0, 0);
				break;
			case PROJ_OBL: 
				break;
			case PROJ_AXON: 
				gl.glRotatef(-15, 0, 1, 0);
				gl.glRotatef(15, 1, 0, 0);
				break;
			case PROJ_PRESP: 
				break;
		}
		
		if(obj != null)
			gl.glTranslatef(0, -(obj.getyCenter()/obj.getMaxAbs()), 0);	
		drawFloor();
		if(obj != null){
			gl.glTranslatef(-(obj.getxCenter()/obj.getMaxAbs()), 0, -1.5f*(obj.getzCenter()/obj.getMaxAbs()));
			renderScene(gl, renderTypes.WIREFRAME);
		}
	}
	
	
	private void renderScene(GL2 gl, renderTypes type){
		switch (type){
			case SOLID:
				gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL );
				gl.glColor3d(1.0, 1.0, 1.0);
				drawObj();
				break;
			case WIREFRAME:
				gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
				gl.glColor3d(1.0, 0.0, 0.0);
				drawObj();
				break;
			case WIRESOLID:
				gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL );
				gl.glEnable( GL_POLYGON_OFFSET_FILL );
				gl.glPolygonOffset( 1, 1 );
				gl.glColor3d(1.0, 1.0, 1.0);
				drawObj();
				gl.glDisable(GL_POLYGON_OFFSET_FILL);
	
				gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
				gl.glColor3d(1.0, 0.0, 0.0);
				drawObj();
				break;
		}
	}
	
	public void zoomIn(){
		zoomFactor += 0.1;
	}
	
	public void zoomOut(){
		if(zoomFactor >= 0.1)
			zoomFactor -= 0.1;
	}
	
	@Override
	public void dispose(GLAutoDrawable arg0) { }

	@Override
	public void init(GLAutoDrawable drawable) {
		this.gl = drawable.getGL().getGL2();
		gl.glEnable(GL_DEPTH_TEST);
		
		glu = new GLU();
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
