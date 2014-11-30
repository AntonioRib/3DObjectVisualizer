package menus;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jogamp.newt.event.MouseAdapter;

import objects.BuildObject;

public class objectVisualizerFrame extends JFrame {

	public static int DEFAULT_WIDTH = 400;
	public static int DEFAULT_HEIGHT = 280;
	private static final long serialVersionUID = 1L;

	private boolean loaded;

	public objectVisualizerFrame(String name) {
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
		JMenu fileMenu = new JMenu("File");
		JMenu viewMenu = new JMenu("View");
		
		JMenu renderSubMenu = new JMenu("Render Type");
		ButtonGroup renderGroup = new ButtonGroup();
		
		JRadioButtonMenuItem renderSolidItem = new JRadioButtonMenuItem("Solid");
		JRadioButtonMenuItem renderWireItem = new JRadioButtonMenuItem("Wireframe");
		JRadioButtonMenuItem renderSolidWireItem = new JRadioButtonMenuItem("Solid + Wireframe");
		
		final JCheckBoxMenuItem textItem = new JCheckBoxMenuItem("Use Texture");
		
		renderGroup.add(renderSolidItem);
		renderSubMenu.add(renderSolidItem);
		
		renderGroup.add(renderWireItem);
		renderSubMenu.add(renderWireItem);
		
		renderSolidWireItem.setSelected(true);
		renderGroup.add(renderSolidWireItem);
		renderSubMenu.add(renderSolidWireItem);
		
		JMenu viewPortSubMenu = new JMenu("4th Viewport Scene");
		ButtonGroup viewPortGroup = new ButtonGroup();
		
		JRadioButtonMenuItem viewPortOblItem = new JRadioButtonMenuItem("Obliq");
		JRadioButtonMenuItem viewPortAxonItem = new JRadioButtonMenuItem("Axonometric");
		JRadioButtonMenuItem viewPortPrespItem = new JRadioButtonMenuItem("Prespective");
		
		viewPortGroup.add(viewPortOblItem);
		viewPortSubMenu.add(viewPortOblItem);
		viewPortOblItem.setSelected(true);
		
		viewPortGroup.add(viewPortAxonItem);
		viewPortSubMenu.add(viewPortAxonItem);
		
		viewPortGroup.add(viewPortPrespItem);
		viewPortSubMenu.add(viewPortPrespItem);

		JMenuItem openItem = new JMenuItem("Open");
		JMenuItem loadTextItem = new JMenuItem("Load Texture");
		JMenuItem clearItem = new JMenuItem("Clear");
		JMenuItem resetPresItem = new JMenuItem("Reset Prespectives");
		
		fileMenu.add(openItem);
		fileMenu.add(loadTextItem);

		viewMenu.add(clearItem);
		viewMenu.addSeparator();
		viewMenu.add(resetPresItem);
		viewMenu.addSeparator();
		viewMenu.add(renderSubMenu);
		viewMenu.addSeparator();
		viewMenu.add(viewPortSubMenu);
		viewMenu.addSeparator();
		viewMenu.add(textItem);

		menuBar.add(fileMenu);
		menuBar.add(viewMenu);

		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileNameExtensionFilter objFilter = new FileNameExtensionFilter(
						"obj files (*.obj)", "obj");
				JFileChooser openFile = new JFileChooser(System
						.getProperty("user.dir"));
				openFile.addChoosableFileFilter(objFilter);
				openFile.setFileFilter(objFilter);
				int response = openFile.showOpenDialog(null);
				if (response == JFileChooser.APPROVE_OPTION) {
					File file = openFile.getSelectedFile();
					bObj.setPath(file.getPath());
					canvas.repaint();
					loaded = true;
					System.out.println(file.getPath());
				}
			}
		});
		
		loadTextItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser openFile = new JFileChooser(System.getProperty("user.dir"));
				int response = openFile.showOpenDialog(null);
				if (response == JFileChooser.APPROVE_OPTION) {
					File file = openFile.getSelectedFile();
					bObj.setTextPath(file.getPath());
					canvas.repaint();
					System.out.println(file.getPath());
				}
			}
		});

		clearItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loaded) {
					System.out.println("Erased");
					bObj.setPath(null);
					canvas.repaint();
					loaded = false;
				}
			}
		});
		
		renderSolidItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bObj.setRendType(BuildObject.renderType.SOLID);
				canvas.repaint();
			}
		});
		
		renderWireItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bObj.setRendType(BuildObject.renderType.WIREFRAME);
				canvas.repaint();
			}
		});
		
		renderSolidWireItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bObj.setRendType(BuildObject.renderType.WIRESOLID);
				canvas.repaint();
			}
		});
		
		viewPortOblItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bObj.setDispViewPort4Type(BuildObject.displayType.PROJ_OBL);
				canvas.repaint();
			}
		});
		
		viewPortAxonItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bObj.setDispViewPort4Type(BuildObject.displayType.PROJ_AXON);
				canvas.repaint();
			}
		});
		
		viewPortPrespItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bObj.setDispViewPort4Type(BuildObject.displayType.PROJ_PRESP);
				canvas.repaint();
			}
		});
		
		textItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textItem.isSelected())
					bObj.turnOnApplyTexture();
				else
					bObj.turnOffApplyTexture();
				canvas.repaint();
			}
		});
		
		resetPresItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				bObj.resetAll();
				canvas.repaint();
			}
		});

		this.setJMenuBar(menuBar);

		canvas.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent event) {
				switch (event.getKeyCode()) {
				case KeyEvent.VK_1:
				case KeyEvent.VK_NUMPAD1:
					System.out.println("Vai para o 1");
					bObj.setViewPortType(BuildObject.sceneType.INDIVIDUAL1);
					canvas.repaint();
					break;
				case KeyEvent.VK_2:
				case KeyEvent.VK_NUMPAD2:
					System.out.println("Vai para o 2");
					bObj.setViewPortType(BuildObject.sceneType.INDIVIDUAL2);
					canvas.repaint();
					break;
				case KeyEvent.VK_3:
				case KeyEvent.VK_NUMPAD3:
					System.out.println("Vai para o 3");
					bObj.setViewPortType(BuildObject.sceneType.INDIVIDUAL3);
					canvas.repaint();
					break;
				case KeyEvent.VK_4:
				case KeyEvent.VK_NUMPAD4:
					System.out.println("Vai para o 4");
					bObj.setViewPortType(BuildObject.sceneType.INDIVIDUAL4);
					canvas.repaint();
					break;
				case KeyEvent.VK_ESCAPE:
					System.out.println("Vai para todos");
					bObj.setViewPortType(BuildObject.sceneType.ALL);
					canvas.repaint();
					break;
				case KeyEvent.VK_PLUS:
				case KeyEvent.VK_ADD:
					System.out.println("Zoom in");
					bObj.zoomIn();
					canvas.repaint();
					break;
				case KeyEvent.VK_MINUS:
				case KeyEvent.VK_SUBTRACT:
					System.out.println("Zoom out");
					bObj.zoomOut();
					canvas.repaint();
					break;
				case KeyEvent.VK_PERIOD:
					if (loaded) {
						System.out.println("Erased");
						bObj.setPath(null);
						canvas.repaint();
						loaded = false;
					}
					break;
				case KeyEvent.VK_RIGHT:
					if(event.isShiftDown()){
						bObj.increaseOblVar();
						bObj.increasePrespVar();
					} else if(event.isControlDown()) {
						bObj.decreaseAxonAAngle();
					} else
						bObj.increasePrincipalRotHorVar();
					canvas.repaint();
					break;
				case KeyEvent.VK_LEFT:
					if(event.isShiftDown()) {
						bObj.decreaseOblVar();
						bObj.decreasePrespVar();
					} else if(event.isControlDown()) {
						bObj.increaseAxonAAngle();
					} else 
						bObj.decreasePrincipalRotHorVar();
					canvas.repaint();
					break;
				case KeyEvent.VK_UP:
					if(event.isControlDown()) {
						bObj.increaseAxonBAngle();
					} else
						bObj.increasePrincipalRotVerVar();
					canvas.repaint();
					break;
				case KeyEvent.VK_DOWN:
					if(event.isControlDown()) {
						bObj.decreaseAxonBAngle();
					} else
						bObj.decreasePrincipalRotVerVar();
					canvas.repaint();
					break;
			}
		}
	});

		canvas.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					System.out.println("Zoom in");
					bObj.zoomIn();
					canvas.repaint();
				} else {
					System.out.println("Zoom out");
					bObj.zoomOut();
					canvas.repaint();
				}

			}
		});
	}
}
