import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import menus.objectVisualizerFrame;
import objects.BuildObject;


public class objectVisualizer {

	//TODO ISTO NÃO VAI SER AQUI.
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame =  new objectVisualizerFrame("3D Object Visualizer");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
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
