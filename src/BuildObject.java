import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
	
	private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();
		
		gl.glClear(GL_COLOR_BUFFER_BIT);
		for(int i=0; i<VIEWPORTNUMBER/2; i++){
			gl.glViewport(i*(width/2), (height/2), (width/2), (height/2));
			gl.glMatrixMode(GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluOrtho2D(0.0, DEFAULT_WIDTH, -DEFAULT_HEIGHT,  0.0);
			buildHouse(drawable);
			
			gl.glViewport(i*(width/2), 0, (width/2), (height/2));
			gl.glMatrixMode(GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluOrtho2D(0.0, DEFAULT_WIDTH, -DEFAULT_HEIGHT,  0.0);
			buildHouse(drawable);
		}
	}

	private void buildHouse(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		
		gl.glColor3d(0.0, 0.0, 0.0);	// walls
		gl.glBegin(GL_LINES);
		gl.glVertex2d(120.0, -220.0);
		gl.glVertex2d(120.0, -140.0);
		gl.glVertex2d(280.0, -220.0);
		gl.glVertex2d(280.0, -140.0);
		gl.glEnd();

		gl.glColor3d(0.0, 0.0, 1.0);	// door
		gl.glBegin(GL_LINE_STRIP);
		gl.glVertex2d(140.0, -220.0);
		gl.glVertex2d(140.0, -160.0);
		gl.glVertex2d(180.0, -160.0);
		gl.glVertex2d(180.0, -220.0);
		gl.glEnd();

		gl.glColor3d(1.0, 0.0, 1.0);	// window
		gl.glBegin(GL_LINE_LOOP);
		gl.glVertex2d(200.0, -160.0);
		gl.glVertex2d(260.0, -160.0);
		gl.glVertex2d(260.0, -200.0);
		gl.glVertex2d(200.0, -200.0);
		gl.glEnd();

		gl.glColor3d(1.0, 0.0, 0.0);	// roof
		gl.glBegin(GL_TRIANGLES);
		gl.glVertex2d(100.0, -140.0);
		gl.glVertex2d(200.0, -60.0);
		gl.glVertex2d(300.0, -140.0);
		gl.glEnd();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
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