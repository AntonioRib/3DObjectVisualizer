import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import menus.objectVisualizerFrame;


public class objectVisualizer {

	/**
	 * @author A. Ribeiro - 41674 Vasco Coelho - 41825
	 */
	
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
