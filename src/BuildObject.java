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

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

/**
 * @author M. Próspero (Updated to JOGL2 by Fernando Birra)
 */

public class BuildObject implements GLEventListener {

	public static int VIEWPORTNUMBER = 4;
	public static int DEFAULT_WIDTH = 400;
	public static int DEFAULT_HEIGHT = 280;
	
	private ObjectLoader obj;
	private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();
		
		gl.glClearColor(0,0,0,0);
		gl.glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

		gl.glMatrixMode(GL_PROJECTION);  // Set up the projection.
		gl.glLoadIdentity();
		gl.glOrtho(-10,10,-10,10,-20,20);
		gl.glMatrixMode(GL_MODELVIEW);
		
		gl.glLoadIdentity();  
			
		gl.glViewport(0, (height/2), (width/2), (height/2));
		gl.glRotatef(30, 1, 0, 0);
		gl.glRotatef(30, 0, 1, 0);
		drawObj(gl);
				
		gl.glViewport((width/2), (height/2), (width/2), (height/2));	
		drawObj(gl);
		
		gl.glViewport(0, 0, (width/2), (height/2));
		drawObj(gl);
		
		gl.glViewport((width/2), 0, (width/2), (height/2));
		drawObj(gl);
	}
	
	private void drawObj(GL2 gl){
		for(Face f : obj.getFaces()){
			gl.glPushMatrix();
			
//			gl.glColor3d(Math.random(), Math.random(), Math.random());         // Color
			gl.glTranslatef(0,0,0.5f);    
			gl.glNormal3f(0,0,1);       
			gl.glBegin(GL_POLYGON);
			for(Point3D p : f.getPoints()){
				gl.glVertex3f(p.getX(), p.getY(), p.getZ());
				System.out.println("X: "+p.getX()+" Y: "+p.getY()+" Z: "+p.getZ());
			}
			gl.glEnd();
			
			gl.glPopMatrix();
		}
	}

	
	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glEnable(GL_DEPTH_TEST);
		try {
			obj = new ObjectLoader();
			obj.load(new File("objects/cow-nonormals.obj"));
		} catch (IOException e) {e.printStackTrace();}
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("3D Object Visualizer");
				GLCanvas canvas = new GLCanvas();
				canvas.addGLEventListener(new BuildObject());
				canvas.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
				frame.add(canvas);
				frame.pack();
				frame.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				frame.setVisible(true);
			}
		});
	}
}