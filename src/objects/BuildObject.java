package objects;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;  // GL constants
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import static javax.media.opengl.GL.GL_LINES;
import static javax.media.opengl.GL.GL_POLYGON_OFFSET_FILL;
import static javax.media.opengl.GL2.GL_POLYGON; // GL2 constants
import static javax.media.opengl.GL2GL3.GL_FILL;
import static javax.media.opengl.GL2GL3.GL_LINE;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

/**
 * @author M. Pr�spero (Updated to JOGL2 by Fernando Birra)
 */

public class BuildObject implements GLEventListener {
	
	public enum sceneType {
		INDIVIDUAL1, INDIVIDUAL2,
		INDIVIDUAL3, INDIVIDUAL4,
		ALL;
	}
	
	public enum displayType { 
		PRINCIPAL, LATERAL_ESQ, LATERAL_DIR, 
		PLANTA, PROJ_OBL, PROJ_AXON, PROJ_PRESP;
	}
	
	public enum renderType { 
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
	
	private sceneType viewPortType;
	private renderType rendType;
	private displayType dispViewPort1Type;
	private displayType dispViewPort2Type;
	private displayType dispViewPort3Type;
	private displayType dispViewPort4Type;
	
	public BuildObject(int width, int height) {
		this.width = width;
		this.height = height;
		this.path = null;
		this.obj = null;
		this.gl = null;
		viewPortType = sceneType.ALL;
		rendType = renderType.WIRESOLID;
		dispViewPort1Type = displayType.PRINCIPAL;
		dispViewPort2Type = displayType.LATERAL_ESQ;
		dispViewPort3Type = displayType.PLANTA;
		dispViewPort4Type = displayType.PROJ_AXON;
	}
	
	public BuildObject(int width, int height, String path) {
		this.width = width;
		this.height = height;
		this.path = path;
		this.obj = null;
		this.gl = null;
		viewPortType = sceneType.ALL;
		rendType = renderType.WIRESOLID;
		dispViewPort1Type = displayType.PRINCIPAL;
		dispViewPort2Type = displayType.LATERAL_ESQ;
		dispViewPort3Type = displayType.PLANTA;
		dispViewPort4Type = displayType.PROJ_AXON;
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
	    
	    displayScene(viewPortType);
	    
	    gl.glFlush() ;
	}
	
	private void displayScene(sceneType type){
		switch (type) {
		case INDIVIDUAL1:
			gl.glViewport(0, 0, width, height);
			buildScene(dispViewPort1Type);
			break;
		case INDIVIDUAL2:
			gl.glViewport(0, 0, width, height);
			buildScene(dispViewPort2Type);
			break;
		case INDIVIDUAL3:
			gl.glViewport(0, 0, width, height);
			buildScene(dispViewPort3Type);
			break;
		case INDIVIDUAL4:
			gl.glViewport(0, 0, width, height);
			buildScene(dispViewPort4Type);
			break;
		case ALL:
			gl.glViewport(0, (height/2), (width/2), (height/2));
			buildScene(dispViewPort1Type);
					
			gl.glViewport((width/2), (height/2), (width/2), (height/2));
			buildScene(dispViewPort2Type);
			
			gl.glViewport(0, 0, (width/2), (height/2));
			buildScene(dispViewPort3Type);
			
			gl.glViewport((width/2), 0, (width/2), (height/2));
			buildScene(dispViewPort4Type);
			break;
		}
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
	
	private void buildScene(displayType type) {	
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
			renderScene(gl, rendType);
		}
	}
	
	
	private void renderScene(GL2 gl, renderType type){
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
	
	public sceneType getViewPortType() {
		return viewPortType;
	}

	public renderType getRendType() {
		return rendType;
	}

	public displayType getDispViewPort1Type() {
		return dispViewPort1Type;
	}

	public displayType getDispViewPort2Type() {
		return dispViewPort2Type;
	}

	public displayType getDispViewPort3Type() {
		return dispViewPort3Type;
	}

	public displayType getDispViewPort4Type() {
		return dispViewPort4Type;
	}
	
	/*** SETTERS ***/

	public void setPath(String path) {
		this.path = path;
		if(path == null)
			obj = null;
		else
			obj = new Shape(path);
	}

	public void setViewPortType(sceneType viewPortType) {
		this.viewPortType = viewPortType;
	}

	public void setRendType(renderType rendType) {
		this.rendType = rendType;
	}

	public void setDispViewPort1Type(displayType dispViewPort1Type) {
		this.dispViewPort1Type = dispViewPort1Type;
	}

	public void setDispViewPort2Type(displayType dispViewPort2Type) {
		this.dispViewPort2Type = dispViewPort2Type;
	}

	public void setDispViewPort3Type(displayType dispViewPort3Type) {
		this.dispViewPort3Type = dispViewPort3Type;
	}

	public void setDispViewPort4Type(displayType dispViewPort4Type) {
		this.dispViewPort4Type = dispViewPort4Type;
	}
	
}
