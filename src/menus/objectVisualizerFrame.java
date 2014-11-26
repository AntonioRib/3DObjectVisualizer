package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
		final BuildObject bObj = new BuildObject(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		canvas.addGLEventListener(bObj);
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
                	bObj.setPath(file.getPath());
                	canvas.repaint();
                	loaded = true;
                	System.out.println(file.getPath());
                }
			}
		});

		this.setJMenuBar(menuBar);
		
		canvas.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent event){
				switch(event.getKeyCode()){
					case KeyEvent.VK_1: case KeyEvent.VK_NUMPAD1:
						System.out.println("Vai para o 1");
						break;
					case KeyEvent.VK_2: case KeyEvent.VK_NUMPAD2:
						System.out.println("Vai para o 2");
						break;
					case KeyEvent.VK_3: case KeyEvent.VK_NUMPAD3:
						System.out.println("Vai para o 3");
						break;
					case KeyEvent.VK_4: case KeyEvent.VK_NUMPAD4:
						System.out.println("Vai para o 4");
						break;
					case KeyEvent.VK_ESCAPE:
						System.out.println("Vai para todos");
						break;
					case KeyEvent.VK_PLUS: case KeyEvent.VK_ADD:
						System.out.println("Zoom in");
						break;
					case KeyEvent.VK_MINUS: case KeyEvent.VK_SUBTRACT:
						System.out.println("Zoom out");
						break;
					case KeyEvent.VK_PERIOD:
						if(loaded) {
							System.out.println("Period");
							bObj.setPath(null);
							canvas.repaint();
							loaded = false;
						}
						break;
				}
			}
		});
	}
}
