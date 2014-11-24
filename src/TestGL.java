

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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



public class TestGL implements GLEventListener {

	private double width;
	private double height;
	
	
	
	public void displayScene(GL2 gl) {
		
		GLUT glu = new GLUT() ;
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glColor3d(1.0, 0.0, 0.0);
		
		gl.glRotated(30.0, 1.0, 0, 0);
		gl.glRotated(30.0, 0, 1, 0);
		
		//glu.glutWireCube((float) 1.0);		
		glu.glutWireTeapot((float) 1.0);
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT() ;
		double aRatio ;
		     
	
		
	    aRatio = width / (double)height; 
	    	    
	    gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
	    if (width <= height)
	    	gl.glOrtho (-1.0, 1.0, -1.0 / aRatio, 1.0 / aRatio, -1.0, 1.0);
	    else
	    	gl.glOrtho (-1.0*aRatio, 1.0*aRatio, -1.0, 1.0, -1.0, 1.0);
	    
	   
		
	    gl.glViewport(0, 0, (int)this.width/2, (int)this.height/2);
	    displayScene(gl) ;
	  
	    
	    gl.glViewport(0, (int)this.height/2, (int)this.width/2, (int)this.height/2);
	    displayScene(gl) ;
	  
	    gl.glViewport((int)this.width/2, 0, (int)this.width/2, (int)this.height/2);
	    displayScene(gl) ;
	  
	    gl.glViewport((int) this.width/2, (int)this.height/2, (int)this.width/2, (int)this.height/2);
	    displayScene(gl) ;
	 
		
	    
		gl.glFlush() ;
				
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
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
		
		this.width = w; this.height = h;
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("JOGL TestGL demo");
				GLCanvas canvas = new GLCanvas();
				canvas.addGLEventListener(new TestGL());
				canvas.setSize(400,280);
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
