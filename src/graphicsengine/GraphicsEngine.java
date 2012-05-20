/*
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-19 Martin
 *	Corrected the use of a map to a MapImage.
 *
 * 2003-05-18 pergamon
 *  added some functions to get the graphicsengine's width, height and position.
 *  Also changed the size of view and infopanel and where/how they are drawn.
 *
 * 2003-05-12 henko
 *  Added suspend(boolean) to make it possible to hide the graphics engine.
 *  Is used by TurtleIslandGame.
 *
 * 2003-04-17 EliasAE
 * 	Added debug code to check why things don't always work with the graphics.
 *
 * 2003-04-15 pergamon
 *  changed clearGraphics() to dispose()
 *
 * 2003-04-15 pergamon
 *	Instead of that this object shall have several "cover" functions that just
 *	sends the information to objects that it have I added:
 *	getView() and getInformationPanel()
 *	and removed:
 *	getObjectFromPosition(int,int)
 *	getObjectFromPosition(int,int,int,int)
 *	drawMouseSelectBox
 *
 * 2003-04-14 pergamon
 *  Wrote getObjectsFromPosition(). Added function drawMouseSelectBox.
 *
 * 2003-04-14 EliasAE
 * 	Changed to using the GraphicsManager.
 *
 * 2003-04-13 EliasAE
 * 	Changed to using the Renderable interface.
 *
 * 2003-04-13 Martin
 *	Created a function for drawing the simulation time on screen.
 *
 * 2003-04-12 pergamon
 *	Added functionality for drawing the view, adding renderables and
 * 	getObjectFromPosition.
 *
 * 2003-04-10 pergamon
 *	created initialize graphics and some temporarly code in loadSprites (Loads 1
 *	sprite) and draw (and draws this sprite all over the screen.). Adds a Mouse
 */

package graphicsengine;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.DisplayMode;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.util.Collection;
import javax.swing.JFrame;

import gameengine.World;
import graphicsengine.graphics.GraphicsManager;
import input.Keyboard;
import input.Mouse;

/**
 * A class to control the graphicsengine, initialize and draw graphics.
 */
public class GraphicsEngine {

	/**
	 * The display mode used by the game.
	 */
	private static DisplayMode displayMode;

	/**
	 * The number of buffers used to paint the screen.
	 */
	private static int buffers = 2;

	/**
	 * True if the fps should be shown on the screen, otherwise false.
	 */
	private boolean showFPS = false;

	/**
	 * A boolean stating if the simulation time should be shown on screen.
	 */
	private boolean showTime = false;

	/**
	 * The start time of the fps count period. Is reset once in a while
	 * when a new period over which the fps is averaged starts.
	 */
	private long startTime;

	/**
	 * The number of frames since the startTime was reset.
	 */
	private int frames;

	/**
	 * The current FPS.
	 */
	private int fps;

	/**
	 * The main window upon which everything is drawn.
	 */
	private JFrame mainFrame;

	/**
	 * The bufferstrategy used to display the image.
	 */
	private BufferStrategy myStrategy;

	/**
	 * The device on which the image is displayed.
	 */
	private GraphicsDevice device;

	/**
	 * Manager for all graphics in the game. Is sent to each part of the
	 * application that need graphics to draw itself. Also takes care
	 * of loading of the graphics from the disk.
	 */
	private GraphicsManager graphicsManager;

	/**
	 * The information panel where information about the selected
	 * object is displayed.
	 */
	private InformationPanel informationPanel;

	/**
	 * The view where the whole game world is drawn.
	 */
	private View view;

	/**
	 * Creates a graphics engine with the displaymode: width, height and
	 * colordepth. For example the inparameters 800, 600, 16 gives a on-screen
	 * resolution of 800x600 in 16 bits color. It also initializes a view which
	 * show a part of a Map calculated from the given MapImage on screen.
	 *
	 * @param mapImage the map image that the graphics engine will use to draw the world
	 * from.
	 * @param width the width of the screenresolution.
	 * @param height the height of the screenresolution.
	 * @param colordepth the colordepth to use.
	 */
	public GraphicsEngine(
			MapImage mapImage,
			int width,
			int height,
			int colordepth
			)
			throws FileNotFoundException {
		displayMode = new DisplayMode(
				width, height, colordepth, DisplayMode.REFRESH_RATE_UNKNOWN);
		GraphicsEnvironment env =
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = env.getDefaultScreenDevice();
		GraphicsConfiguration gc = device.getDefaultConfiguration();

		Debug.log("GraphicsLog: Creating main window");
		mainFrame = new JFrame(gc);

		// Load the graphics
		graphicsManager = new GraphicsManager(mainFrame);

		Debug.log("GraphicsLog: Creating view");
		view = new View(graphicsManager, mapImage, 0, 0, width, height-151);

		// create a InformationPanel.
		informationPanel = new InformationPanel(graphicsManager, 0, height-150, width, 150);
	}

	/**
	 * Initializes the graphicmode
	 */
	public void initializeGraphics() {
		Debug.log("GraphicsLog: Initializing graphics");

		Debug.log("GraphicsLog: Setting up window");
		mainFrame.setIgnoreRepaint(true);
		mainFrame.setUndecorated(true);

		Debug.log("GraphicsLog: Setting full screen window");
		device.setFullScreenWindow(mainFrame);

		Debug.log("GraphicsLog: Testing display mode change support");
		if (device.isDisplayChangeSupported()) {
			Debug.log("GraphicsLog: Changing display mode");
			device.setDisplayMode(displayMode);
			// There seems to be a problem with concurrency. The only
			// solution that we have is to wait for a little while
			// to make it work.
			try {
				Debug.log("GraphicsLog: Waiting 2 seconds");
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			mainFrame.setSize(
					displayMode.getWidth() +
						mainFrame.getInsets().left +
						mainFrame.getInsets().right,
					displayMode.getHeight() +
						mainFrame.getInsets().top +
						mainFrame.getInsets().bottom
					);
		}

		Debug.log("GraphicsLog: Creating buffer strategy");
		mainFrame.createBufferStrategy(buffers);
		Debug.log("GraphicsLog: Retrieving buffer strategy");
		myStrategy = mainFrame.getBufferStrategy();

		Debug.log("GraphicsLog: Retrieving current time");
		// starttime for FPS calculation.
		startTime = System.currentTimeMillis();
		Debug.log("GraphicsLog: Finished initializing");
	}

	/**
	 * The main draw function that calls all other draw funcitons to draw
	 * everything on screen.
	 */
	public void draw() {
		Graphics2D graphics = (Graphics2D) myStrategy.getDrawGraphics();

		view.draw(graphics);
		informationPanel.draw(graphics);

		if (showFPS) {
			drawFPS(graphics);
		}
		if (showTime) {
			drawTime(graphics);
		}

		myStrategy.show();
	}

	/**
	 * Draws a FPS counter on screen.
	 *
	 * @param graphics the Graphics2D to draw the fps counter on.
	 */
	private void drawFPS(Graphics2D graphics) {
		long stopTime = System.currentTimeMillis();
		if ((stopTime - startTime) > 1000) {
			fps = (int) ((double) frames / ((stopTime - startTime) / 1000.0));
			startTime = System.currentTimeMillis();
			frames = 0;
		}
		frames++;

		graphics.setColor(Color.white);
		graphics.drawString("FPS: " + fps, 10, 10);
	}

	/**
	 * Draws the simulation time on screen.
	 *
	 * @param graphics the <code>Graphics2D</code> to draw the time on.
	 */
		private void drawTime(Graphics2D graphics) {
			float timeSeconds = World.getWorld().getWorldTime();
			graphics.setColor(Color.white);
			// Draws the time close to the top right corner
			graphics.drawString("Time: " + timeSeconds + " s", 500, 10);
		}

	/**
	 * Clear all graphics and exits full screen.
	 */
	public void dispose() {
		device.setFullScreenWindow(null);
	}

	/**
	 * To show fps or not.
	 *
	 * @param showFPS true if the fps should be drawn on the screen, otherwise
	 * false.
	 */
	public void showFps(boolean showFPS) {
		this.showFPS = showFPS;
	}

	/**
	 * To show simulation time or not.
	 *
	 * @param showTime true if the time should be drawn on the screen,
	 * otherwise false.
	 */
	public void showTime(boolean showTime) {
		this.showTime = showTime;
	}

	/**
	 * Retrives the View associated with the graphicsengine.
	 *
	 * @return the view of this graphicsengine.
	 */
	public View getView() {
		return view;
	}

	/**
	 * Retrives the InformationPanel associated with the
	 * graphicsengine.
	 *
	 * @return the informationPanel of this graphicsengine.
	 */
	public InformationPanel getInformationPanel() {
		return informationPanel;
	}

	/**
	 * Adds a Mouse to graphicsengine's JFrame
	 *
	 * @param mouse The Mouse to add.
	 */
	public void addMouse(Mouse mouse) {
		mainFrame.addMouseListener(mouse);
		mainFrame.addMouseMotionListener(mouse);
	}

	/**
	 * Adds a Keyboard to graphicsengine's JFrame
	 *
	 * @param keyboard The Keyboard to add.
	 */
	public void addKeyboard(Keyboard keyboard) {
		mainFrame.addKeyListener(keyboard);
	}

	/**
	 * Retrives the current displaymode that is used.
	 *
	 * @return the displaymode.
	 */
	public static DisplayMode getDisplayMode() {
		return displayMode;
	}

	/**
	 * Will show or hide the graphics engine depending on what value is sent in.
	 * true will suspend the GraphicsEngine, in other words, hide it.
	 * false will resume the GraphicsEngine, in other words, show it.
	 * Is used by TurtleIslandGame.
	 *
	 * @param suspend true if the GraphicsEngine should suspend,
	 * false otherwise.
	 */
	public void suspend(boolean suspend) {
		if (suspend) {
			mainFrame.hide();
		} else {
			mainFrame.show();
		}
	}

	/**
	 * Retrives the x-coordinate of the graphicsengein
	 *
	 * @return the x-coordinate
	 */
	public int getX() {
		return mainFrame.getX();
	}

	/**
	 * Retrives the y-coordinate of the graphicsengein
	 *
	 * @return the y-coordinate
	 */
	public int getY() {
		return mainFrame.getY();
	}

	/**
	 * Retrives the width of the GraphicsEngine's window
	 *
	 * @return the width of the graphicsengine's window
	 */
	public int getWidth() {
		return mainFrame.getWidth();
	}

	/**
	 * Retrives the height of the GraphicsEngine's window
	 *
	 * @return the height of the graphicsengine's window
	 */
	public int getHeight() {
		return mainFrame.getHeight();
	}
}

