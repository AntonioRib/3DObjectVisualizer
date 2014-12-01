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

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * @author M. Próspero (Updated to JOGL2 by Fernando Birra)
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
	private String textPath;
	private GL2 gl;
	
	private float zoomFactor = 1f;
	
	private sceneType viewPortType;
	private renderType rendType;
	private displayType dispViewPort1Type;
	private displayType dispViewPort2Type;
	private displayType dispViewPort3Type;
	private displayType dispViewPort4Type;
	
	private int[] textureBuf = new int[1];
	private boolean applyTexture;
	
	private int prespVar = 10;
	private double oblVar = 45;
	private int principalRotHorVar = 0;
	private int principalRotVerVar = 0;
	private int axonAAngle = 42;
	private int axonBAngle = 7;

	public BuildObject(int width, int height) {
		this.width = width;
		this.height = height;
		this.path = null;
		this.obj = null;
		this.gl = null;
		this.textPath = null;
		applyTexture = false;
		viewPortType = sceneType.ALL;
		rendType = renderType.WIRESOLID;
		dispViewPort1Type = displayType.PRINCIPAL;
		dispViewPort2Type = displayType.LATERAL_ESQ;
		dispViewPort3Type = displayType.PLANTA;
		dispViewPort4Type = displayType.PROJ_OBL;
	}
	
	public BuildObject(int width, int height, String path) {
		this(width, height);
		this.path = path;
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
	    
		if(applyTexture && textPath != null)
			applyTexture();
	    
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
		if(applyTexture && textPath != null){
			gl.glEnable(GL.GL_TEXTURE_2D);
			gl.glBindTexture(GL.GL_TEXTURE_2D, textureBuf[0]);
		}
		for(Face f : obj.getFaces()){
			gl.glBegin(GL_POLYGON);
			for(int i=0; i<f.getPoints().size(); i++){
				Point3D p = f.getPoints().get(i);
				if(f.getTexturePoints().size() > 0 && applyTexture) {
					Point3D tp = f.getTexturePoints().get(i);
					gl.glTexCoord2d(tp.getX(), tp.getY());
				}
				gl.glVertex3f(p.getX()/obj.getMaxAbs(), p.getY()/obj.getMaxAbs(), p.getZ()/obj.getMaxAbs());
			}
			gl.glEnd();
		}
		if(applyTexture && textPath != null)
			gl.glDisable(GL.GL_TEXTURE_2D);
	}
	
	private void applyTexture(){   
		File textureFile = new File(textPath); // returned by JFileChooser getSelectedFile() method
		BufferedImage img = null;
		try {
			img = ImageIO.read(textureFile);
		} catch (IOException e) { e.printStackTrace(); }
		WritableRaster raster = img.getRaster();
		int width = raster.getWidth();
		int height = raster.getHeight();
		DataBuffer buf = raster.getDataBuffer();
		switch( buf.getDataType() ) {
		case DataBuffer.TYPE_BYTE:
			DataBufferByte bb = (DataBufferByte) buf;
			byte im[] = bb.getData();
			
			//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
			//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			
			gl.glTexImage2D( GL.GL_TEXTURE_2D, 0, GL.GL_RGB, width, height,
					0, GL2GL3.GL_BGR, GL.GL_UNSIGNED_BYTE, ByteBuffer.wrap(im));
			
			break;
		case DataBuffer.TYPE_UNDEFINED:
			 	 // Report error here!
			 	 break;
		}
	}
	
	private void buildScene(displayType type) {	
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		switch (type){
			case PRINCIPAL:
				gl.glRotatef(principalRotHorVar, 0, 1, 0);
				gl.glRotatef(principalRotVerVar, 1, 0, 0);
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
			double mObl[] = {1, 0, 0, 0,
							 0,	1, 0, 0,
							 -Math.cos((oblVar*Math.PI)/180), -Math.sin((oblVar*Math.PI)/180), 1,  0,
							 0, 0, 0, 1};
				gl.glMultMatrixd(mObl, 0);
				break;
			case PROJ_AXON: 
				double teta = Math.atan(Math.sqrt(Math.tan(axonAAngle*Math.PI/180)/(Math.tan(axonBAngle*Math.PI/180)))-(Math.PI/2));
				double fi = Math.asin(Math.sqrt((Math.tan(axonAAngle*Math.PI/180))*(Math.tan(axonBAngle*Math.PI/180))));
				gl.glRotated(fi*180/Math.PI, 1, 0, 0);
				gl.glRotated(teta*180/Math.PI, 0, 1, 0);
				break;
			case PROJ_PRESP: 
			double[] mPresp = {1, 0, 0, 0,
						 	   0, 1, 0, 0,
							   0, 0, 1, -1f/prespVar,
							   0, 0, 0, 1 };
				gl.glMultMatrixd(mPresp, 0);
				break;
		}
		
		if(obj != null)		
			gl.glTranslatef(0, -(obj.getyCenter()/obj.getMaxAbs()), 0);	
		drawFloor();

		if(obj != null){
			gl.glTranslatef(-(obj.getxCenter()/obj.getMaxAbs()), 0, -(obj.getzCenter()/obj.getMaxAbs()));
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
	
		gl.glGenTextures(1, textureBuf, 0);
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
	
	public String getTextPath() {
		return textPath;
	}

	public boolean isApplyTexture() {
		return applyTexture;
	}
	
	public double getOblVar() {
		return oblVar;
	}
	
	public int getPrespVar() {
		return prespVar;
	}
	
	public int getPrincipalRotHotVar() {
		return principalRotHorVar;
	}
	
	public int getPrincipalRotVerVar() {
		return principalRotVerVar;
	}

	public int getAxonBAngle() {
		return axonBAngle;
	}
	
	public int getAxonAAngle() {
		return axonAAngle;
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
	
	public void setTextPath(String textPath) {
		this.textPath = textPath;
	}

	public void turnOnApplyTexture() {
		this.applyTexture = true;
	}
	
	public void turnOffApplyTexture() {
		this.applyTexture = false;
	}

	public void increasePrespVar() {
		this.prespVar+=10;
	}
	
	public void decreasePrespVar() {
		if(prespVar-10 > 0)
			this.prespVar-=10;
	}

	public void increaseOblVar() {
		this.oblVar += 5;
	}
	
	public void decreaseOblVar() {
		if(oblVar-5 > 0)
			this.oblVar -= 5;
	}

	public void increasePrincipalRotHorVar() {
		this.principalRotHorVar += 5;
	}
	
	public void decreasePrincipalRotHorVar() {
		this.principalRotHorVar -= 5;
	}
	
	public void increasePrincipalRotVerVar() {
		this.principalRotVerVar += 5;
	}
	
	public void decreasePrincipalRotVerVar() {
		this.principalRotVerVar -= 5;
	}
	
	public void increaseAxonAAngle() {
		if(this.axonAAngle+5 == 90)
			increaseAxonAAngle();
		this.axonAAngle += 5;
	}
	
	public void decreaseAxonAAngle() {
		if(axonAAngle-5 > 0 || this.axonAAngle+5 == 45)
			this.axonAAngle -= 5;
	}
	
	public void increaseAxonBAngle() {
		if(this.axonBAngle+5 == 90 || this.axonBAngle+5 == 45)
			increaseAxonBAngle();
		this.axonBAngle += 5;
	}
	
	public void decreaseAxonBAngle() {
		if(axonBAngle-5 > 0)
			this.axonBAngle -= 5;
	}
	
	public void resetPrespVar(){
		prespVar = 10;
	}
	
	public void resetOblVar(){
		oblVar = 45;
	}
	
	public void resetPrincipalRotHorVar() {
		this.principalRotHorVar = 0;
	}
	
	public void resetPrincipalRotVerVar() {
		this.principalRotVerVar = 0;
	}
	
	public void resetAll(){
		resetOblVar();
		resetPrespVar();
		resetPrincipalRotHorVar();
		resetPrincipalRotVerVar();
	}

}
