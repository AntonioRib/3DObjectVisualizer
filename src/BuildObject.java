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

import com.jogamp.opengl.util.gl2.GLUT;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * @author M. Próspero (Updated to JOGL2 by Fernando Birra)
 */

public class BuildObject implements GLEventListener {

	public static int VIEWPORTNUMBER = 4;
	public static int DEFAULT_WIDTH = 400;
	public static int DEFAULT_HEIGHT = 280;
	
	private Shape obj;
	private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT() ;
		double aRatio = width / (double)height; 
	    	    
	    gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
	    if (width <= height)
	    	gl.glOrtho (-1.5, 1.5, -1.5/aRatio, 1.5/aRatio, -1.5, 1.5);
	    else
	    	gl.glOrtho (-1.5*aRatio, 1.5*aRatio, -1.5, 1.5, -1.5, 1.5);
	    
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
	    
		gl.glViewport(0, (height/2), (width/2), (height/2));
		displayScene(gl);
				
		gl.glViewport((width/2), (height/2), (width/2), (height/2));
		gl.glLoadIdentity();
		gl.glRotatef(90, 0, 1, 0);
		displayScene(gl);
		
		gl.glViewport(0, 0, (width/2), (height/2));
		gl.glLoadIdentity();
		gl.glRotatef(90, 1, 0, 0);
		displayScene(gl);
		
		gl.glViewport((width/2), 0, (width/2), (height/2));
		gl.glLoadIdentity();
		gl.glRotatef(10, 1, 0, 0);
		gl.glRotatef(235, 0, 1, 0);
		displayScene(gl);
	    
	    gl.glFlush() ;
	}
	
	private void drawObj(GL2 gl){
		for(Face f : obj.getFaces()){
			gl.glBegin(GL_POLYGON);
			for(Point3D p : f.getPoints()){
				gl.glVertex3f(p.getX()/obj.getMaxAbs(), p.getY()/obj.getMaxAbs(), p.getZ()/obj.getMaxAbs());
				System.out.println("X: "+p.getX()/obj.getMaxAbs()+" Y: "+p.getY()/obj.getMaxAbs()+" Z: "+p.getZ()/obj.getMaxAbs());
			}
			gl.glEnd();
		}
	}
	
	public void displayScene(GL2 gl) {
		
		GLUT glu = new GLUT() ;
		//gl.glMatrixMode(GL_MODELVIEW);
		//gl.glLoadIdentity();
		gl.glColor3d(1.0, 0.0, 0.0);
		
		//gl.glRotated(30.0, 1.0, 0, 0);
		//gl.glRotated(30.0, 0, 1, 0);

		drawObj(gl);
	}

	
	@Override
	public void dispose(GLAutoDrawable arg0) { }

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glEnable(GL_DEPTH_TEST);
		obj = new Shape("objects/berserker.obj");
		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		this.width = width; this.height = height;
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
