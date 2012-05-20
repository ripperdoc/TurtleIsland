/* 2003-05-17 henko
 *  Added support for disabling the "Resume game" button and added a credits
 *  screen.
 *
 * 2003-05-13 henko
 *  Updated the "New game" function, to make sure it really creates a new
 *  World.
 *
 * 2003-05-12 henko
 *  More or less rewrote the class. Now supports resuming an old game.
 *  This class was previously called TurtleIsland.
 *
 * 2003-04-16 pergamon
 *  Just added some comments.
 *
 * 2003-04-14 EliasAE
 * 	Added the creation of a map.
 *
 * 2003-04-13 Martin
 *	Changed how the world is created and the starting of the simulation.
 *	Made some small changes for starting and displaying the simulation time correctly.
 *
 * 2003-04-12 henko
 *	Now, World.update() is called from the main loop.
 *
 * 2003-04-10 pergamon
 *  Added a main function with some code just to start up a graphicsengine and
 *	draws whatever this want to draw on screen.
 */
package pregame;

import gameengine.World;
import graphicsengine.Debug;
import graphicsengine.GraphicsEngine;
import input.InputEventTranslator;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.Component;
import java.awt.Container;
import java.awt.MediaTracker;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import graphicsengine.graphics.GraphicsManager;
import graphicsengine.MapImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The main class which controls the game.
 */
public class TurtleIslandGame {

	private static final String SERIALIZED_WORLD_FILENAME = "TurtleIsland.save";

	/**
	 * Subdiretory for images that represents different maps.
	 */
	private static final String MAPS_DIRECTORY = "maps" + File.separator;

	/**
	 * The extension that will be used for map images. All map image files
	 * MUST end with this extension.
	 */
	private static final String MAPFILE_EXTENSION = ".png";

	/**
	 * Represents the class that shall load all the information of a level,
	 * that is all the information that's needed to start the game.
	 */
	private LevelLoader levelLoader;

	/**
	 * The world the game is using.
	 */
	private World world = null;

	/**
	 * The graphicsengine the game is using.
	 */
	private GraphicsEngine graphicsEngine = null;

	/**
	 * The inputeventtranslator the game is using.
	 */
	private InputEventTranslator inputEventTranslator = null;

	/**
	 * The main function to start TurtleIslandGame.
	 */
	public void start() {

		// Debug ON
		Debug.setDebug(true);

		Menu mainMenu;
		do {

			// Show menu
			mainMenu = new Menu(saveGameAvailable());
			mainMenu.show();

			// Wait for user
			while (mainMenu.userResponse() == Menu.NO_RESPONSE) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}

			// Do something
			mainMenu.hide();
			performAction(mainMenu.userResponse());

		} while (mainMenu.userResponse() != Menu.EXIT_GAME);

		// Quit program
		System.exit(0);
	}


	/**
	 * Returns whether there is a savegame available. Checks if the file
	 * SERIALIZED_WORLD_FILENAME exists and contains any data.
	 *
	 * @return whether there is a savegame available.
	 */
	private boolean saveGameAvailable() {

		// If it exists and contains anyting, return true.
		File file = new File(SERIALIZED_WORLD_FILENAME);
		if (file.exists()) {
			if (file.length() > 0) {
				return true;
			}
		}

		// A game is already started
		if (world != null) {
			return true;
		}

		// Otherwise, return false
		return false;
	}


	/**
	 * Performs different actions depending on what choice the user made.
	 *
	 * @param choice number representing what button the user pressed.
	 */
	private void performAction(int choice) {

		switch (choice) {

			// Start to play, either a new game or the last one
			case Menu.NEW_GAME :
			case Menu.RESUME_GAME :

				try {
					// Create a new game and a new world
					if (choice == Menu.NEW_GAME) {
						MapImage mapImage = loadMapImage();
						world = createNewWorld(mapImage);
						graphicsEngine = initializeGraphicsEngine(world);

					// Resume previous game
					} else {
						if (world == null) {
							if (loadPreviousWorld()) {
								// Create graphics engine
								world = World.getWorld();
								graphicsEngine = initializeGraphicsEngine(world);
							} else {
								// Didn't work.
								// TODO Make some kind of alert when it didn't work.
								return;
							}

						} else {
							// Return to the current game.
							graphicsEngine.suspend(false);
						}
					}
					inputEventTranslator = initializeInputEventTranslator(graphicsEngine);

					// Start the simulation
					try {
						runSimulation(world, graphicsEngine, inputEventTranslator);
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(1);
					}
	
					// Pause game
					graphicsEngine.suspend(true);
				
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(1);
				}

				break;

			case Menu.SHOW_CREDITS :
				// TODO Show some credits screen.
				break;

			case Menu.EXIT_GAME :
				// Serialize world
				if (world != null) {
					saveCurrentWorld();
				}
				break;

			default :
				throw new IllegalStateException("");
		}
	}

	/**
	 * Orders World to serialize itself.
	 */
	private void saveCurrentWorld() {
		try {
			// Initialize output streams
			FileOutputStream fos = new FileOutputStream(SERIALIZED_WORLD_FILENAME);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			// Save world
			World.serializeWorld(oos);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Orders World to deserialize itself.
	 *
	 * @return returns true if the world loaded successfully.
	 */
	private boolean loadPreviousWorld() {
		try {
			// Initialize input streams
			FileInputStream fis = new FileInputStream(SERIALIZED_WORLD_FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);

			// Load world
			World.deserializeWorld(ois);
		} catch (IOException ioe) {
			return false;
		} catch (ClassNotFoundException cnfe) {
			return false;
		}

		return true;
	}

	/**
	 * Initializes a InputEventTranslator with a given GraphicsEngine.
	 *
	 * @param graphicsEngine the GraphicsEngine to translate input for.
	 * @return a InputEventTranslator with a given GraphicsEngine.
	 */
	private InputEventTranslator initializeInputEventTranslator(GraphicsEngine graphicsEngine) {

		// Creates the translator for inputs from the user
		InputEventTranslator inputEventTranslator =
				new InputEventTranslator(graphicsEngine);

		return inputEventTranslator;
	}

	/**
	 * Initializes a GraphicsEngine that will draw the given world.
	 *
	 * @param world the world to draw.
	 * @return a GraphicsEngine that will draw the given world.
	 */
	private GraphicsEngine initializeGraphicsEngine(World world) 
			throws FileNotFoundException {
		GraphicsEngine graphicsEngine = null;
		try {
			// Creates a GraphicsEngine with 800x600 resolution and 16 bit colors.
			graphicsEngine =
					new GraphicsEngine(World.getWorld().getMapImage(), 800, 600, 16);

			// Initialize graphics
			graphicsEngine.initializeGraphics();
			graphicsEngine.showFps(true);
			graphicsEngine.showTime(true);

		} catch (IllegalStateException e) {
			System.out.println("System wasn't ready, graphics could not be initialized.");
			System.out.println("Please try again...");
			throw e;
		}

		return graphicsEngine;
	}

	/**
	 * Creates a completely new world.
	 *
	 * @param mapImage the MapImage of the world.
	 * @return a brave new world.
	 */
	private World createNewWorld(MapImage mapImage) {

		// TODO Make more intelligent prehaps?

		// If world exists, throw it away!
		if (world != null) {
			World.reset();
		}

		// Create the world
		World world = World.getWorld();
		// Initialize and load objects
		world.initialize(mapImage);

		return world;
	}

	/**
	 * Makes the world go round. Starts the simulation and runs a loop
	 * where the world is updated and the graphicsengine draws every
	 * cycle until the inputeventtranslator tells it to stop, based on
	 * the user's input.
	 *
	 * @param world the World to simulate.
	 * @param graphicsEngine the GraphicsEngine to draw.
	 * @param inputEventTranslator the InputEventTranslator to listen to.
	 */
	private void runSimulation(
			World world,
			GraphicsEngine graphicsEngine,
			InputEventTranslator inputEventTranslator)
		{

		// Starts the simulation.
		world.startSimulation();

		// THE GAME LOOP
		while (!inputEventTranslator.isQuiting()) {
			world.update();
			graphicsEngine.draw();
		}

		// Stop simulation and exit fullscreen
		World.getWorld().stopSimulation();

	}

	/**
	 * Loads a random map image.
	 */
	private MapImage loadMapImage() {
		File sourceDirectory = new File(MAPS_DIRECTORY);
		// Retrieve all the files in the directory in question.
		File[] filesInDirectory = sourceDirectory.listFiles();
		// Check if the directory did not exist or if another error occured.
		if (filesInDirectory == null) {
			return null;
		}

		List mapFiles = new ArrayList();
		for(int i = 0; i < filesInDirectory.length; i++) {
			if (filesInDirectory[i].getName().endsWith(MAPFILE_EXTENSION)) {
				mapFiles.add(filesInDirectory[i]);
			}
		}

		// If there are any maps to load
		if (mapFiles.size() > 0) {
			// Load a random map
			Component dummyComponent = new Container();
			MediaTracker tracker = new MediaTracker(dummyComponent);
			int randomIndex = (int) (Math.random() * mapFiles.size());
			Image currentImage = dummyComponent.getToolkit().getImage(
					((File) mapFiles.get(randomIndex)).getPath()
					);
			tracker.addImage(currentImage, 0);
			// Wait so that the image is loaded.
			try {
				tracker.waitForAll();
			}
			catch (InterruptedException interruptedException) {
			}
			return new MapImage(currentImage);
		} else {
			return null;
		}
	}

}
