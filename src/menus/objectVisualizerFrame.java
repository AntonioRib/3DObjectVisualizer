package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import objects.BuildObject;

public class objectVisualizerFrame extends JFrame {
	
	public static int DEFAULT_WIDTH = 400;
	public static int DEFAULT_HEIGHT = 280;
	private static final long serialVersionUID = 1L;
	
	private boolean loaded;

	public objectVisualizerFrame(String name){
		super(name);
		loaded = false;
		setupUI();
	}

	
	private void setupUI() {
		final GLCanvas canvas = new GLCanvas();
		canvas.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		this.add(canvas);
		this.pack();
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		
		JMenuItem open = new JMenuItem("Open");
		file.add(open);
		menuBar.add(file);
		
		open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileNameExtensionFilter objFilter = new FileNameExtensionFilter("obj files (*.obj)", "obj");
                JFileChooser openFile = new JFileChooser(System.getProperty("user.dir"));
                openFile.addChoosableFileFilter(objFilter);
                openFile.setFileFilter(objFilter);
                int response = openFile.showOpenDialog(null);
                if(response == JFileChooser.APPROVE_OPTION){
                	File file = openFile.getSelectedFile();
                	canvas.addGLEventListener(new BuildObject(DEFAULT_WIDTH, DEFAULT_HEIGHT, file.getPath()));
                	canvas.repaint();
                	loaded = true;
                	System.out.println(file.getPath());
                }
			}
		});

		this.setJMenuBar(menuBar);
	}
}
