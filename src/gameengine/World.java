/* 2003-05-19 Martin
 *	Added a field for storing the MapImage, as well as correcting the method
 *	loadObjects to initialize().
 *
 * 2003-05-13 henko
 *  Solved the problem with source nodes that never was unregistered.
 *  Added a method to remove the current world.
 *
 * 2003-05-12 henko
 *  Made the class implement Serializable and added functions for serializing
 *  and deserializing the world.
 *
 * 2003-05-10 Martin
 *	Added a SourceNode to the list.
 *
 * 2003-05-03 Martin
 *	Added gender when creating Indivudals. Corrected the worldTime to be in seconds.
 *
 * 2003-04-24 EliasAE
 * 	Added the light pillar node.
 *
 * 2003-04-19 EliasAE
 * 	Added try-block for the ConcurrentModificationException.
 *
 * 2003-04-15 Martin
 * Implemented two new methods, register and unregister, that automatically
 * registers or unregisters the given GameObject from the correct list.
 * This can make the get methods on the list unnecessary. Also implemented
 * serializable.
 *
 * 2003-04-14 Martin
 *	Some corrections on the timing.
 *
 * 2003-04-13 Martin
 *	Created a loadObjects() method for loading objects for testing, instead
 *	of doing it in the constructor.
 *	Implemented the time functions. You should look at them and try to see if
 *	anything is incorrect. We wouldn't wanna leak time during run, would we?
 *
 * 2003-04-12 henko
 *	Implemented update(). It now iterates through all GameObjects and calls
 * 	update() on all Updateable objects.
 *
 * 2003-04-12 pergamon
 *  Added some features in the constructor for testing with mouse-click.
 *
 * 2003-04-11 Martin
 *	Removed setLightPillar (can be set from constructor).
 *	Changed the getEnvironment() to public static to avoid long method calls in other parts
 *	of the game. Formatted with macro. This class is NOT finished!
 */
package gameengine;

import gameengine.gameobjects.*;
import gameengine.gameobjects.SourceNode;
import graphicsengine.MapImage;
import java.util.*;
import java.io.*;
import gameengine.tasksystem.*;

/**
 * The world represents all objects in the game, plus several helper
 * functions. The world contains several lists grouping the game objects
 * into specific categories. The lists are then searched by for example the
 * graphics engine. The world has also got a constant reference to the
 * Environment object, which holds all evonrment values in the game.
 */
public class World implements Serializable{

	/**
	 * Represents the world itself.
	 */
	private static World theWorld;

	/**
	 * A class that holds the data about the map of the world.
	 */
	private MapImage mapData;

	/**
	 * A list of all food sources in the world, used to search for destinations
	 * for indivudals in need of food.
	 */
	private SourceNodeList foodSources;

	/**
	 * A list of all happiness sources in the world, used to search for
	 * destinations for indivudals in need of happiness.
	 */
	private SourceNodeList happinessSources;

	/**
	 * A list of all game objects, used primarily by the graphics engine for
	 * searching after objects to draw in a specified area.
	 */
	private GameObjectList allObjects;

	/**
	 * A list of SocializeGroups that contains all created groups at the
	 * current time. Is used for searching for groups to join for individuals
	 * with a need of socialization.
	 */
	private SocializeGroupList socializeGroups;

	/**
	 * Represents the light pillar of the world, the centre of the individuals
	 * community.
	 */
	private LightPillarNode lightPillarNode;

	/**
	 * Represents the processor time in milliseconds at the start of the last
	 * update cycle.
	 * Used internally only.
	 */
	private long lastProcessorTime;

	/**
	 * Represents the time in seconds since the start of the last update cycle.
	 */
	private float timeSinceCycle;

	/**
	 * Represents the time in milliseconds since the start of last update cycle.
	 * Used internally only.
	 */
	private long timeSinceCycleLong;

	/**
	 * Represents the time since the simulation first was started in
	 * seconds, not including time when the simulation has been stopped in
	 * between.
	 */
	private float worldTime;

	/**
	 * An object holding all the world's constants and environmental values.
	 */
	private static Environment environment = new Environment();

	/**
	 * Returns the world itself. There exists only one world (singleton).
	 *
	 * @return the world itself.
	 */
	public static World getWorld() {
		if (theWorld==null) {
			theWorld = new World();
		}
		return theWorld;
	}

	/**
	 * Will throw away the current world, to make it posible to start all over
	 * again.
	 */
	public static void reset() {
		theWorld = null;
	}

	/**
	 * Will serialize the world to the specified stream, if it exists.
	 *
	 * @param oos the stream to serialize to.
	 */
	public static void serializeWorld(ObjectOutputStream oos) throws IOException {

		try {

			// Write the world to the output stream
			if (theWorld != null) {
				oos.writeObject(theWorld);
			}

		} catch (IOException ioe) {
			throw ioe;
		}

	}

	/**
	 * Deserializes the world from the given stream.
	 *
	 * @return the stream to deserialize from.
	 */
	public static void deserializeWorld(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		World world = null;

		try {

			// Read objects from the input stream
			theWorld = (World) ois.readObject();

		} catch (IOException ioe) {
			throw ioe;
		} catch (ClassNotFoundException cnfe) {
			throw cnfe;
		}

	}

	/**
	 * Constructs a World object. Can only be called from within the World
	 * object.
	 */
	private World() {
		// Initialize time variables
		lastProcessorTime = 0;
		timeSinceCycle = 0f;
		timeSinceCycleLong = 0;
		worldTime = 0f;

		environment = new Environment();

		// Initialize object lists
		allObjects = new GameObjectList();
		foodSources = new SourceNodeList();
		happinessSources = new SourceNodeList();
		socializeGroups = new SocializeGroupList();

	}

	/**
	 * Returns the Environment object containing all constants.
	 *
	 * @return the Environment object containing all constants.
	 */
	public static Environment getEnvironment() {
		return environment;
	}

	/**
	 * Returns the list of food sources.
	 *
	 * @return the list of food sources.
	 */
	public SourceNodeList getFoodSources() {
		return foodSources;
	}

	/**
	 * Returns the list of happiness sources.
	 *
	 * @return the list of happiness sources.
	 */
	public SourceNodeList getHappinessSources() {
		return happinessSources;
	}

	/**
	 * Returns the list of all game objects.
	 *
	 * @return the list of all game objects.
	 */
	public GameObjectList getAllObjects() {
		return allObjects;
	}

	/**
	 * Returns the list of socialize groups.
	 *
	 * @return the list of SocializeGroups.
	 */
	public SocializeGroupList getSocializeGroups() {
		return socializeGroups;
	}

	/**
	 * Returns the light pillar.
	 *
	 * @return the LightPillarNode.
	 */
	public LightPillarNode getLightPillar() {
		return lightPillarNode;
	}

	/**
	 * Returns a float representing the world time in seconds since the last
	 * update cycle (last time update() was called in World).
	 *
	 * @return a float representing the world time in seconds since the last
	 * update cycle.
	 */
	public float getTimeSinceCycle() {
		return timeSinceCycle;
	}

	/**
	 * Returns a float representing the world time in seconds, i.e. the
	 * time since the simulation first started.
	 *
	 * @return a float representing the world time in seconds.
	 */
	public float getWorldTime() {
		return worldTime;
	}

	/**
	 * Returns the MapImage object that holds the data about the map of the world.
	 *
	 * @return the MapImage object that holds the data about the map of the world.
	 */
	public MapImage getMapImage() {
		return mapData;
	}


	/**
	 * The main method for updating the whole simulation. Calculates the
	 * timeSinceCycle = currentTimeMillis() - lastProcessorTime and then worldTime +=
	 * timeSinceCycle. The timeSinceCycle is then used by several functions in
	 * the game for calculating values based on time, such as speed.
	 */
	public void update() {
		// Update time

		// Saves current processor time in local variable
		long currentProcessorTime = System.currentTimeMillis();
		// Calculates  the timeSinceCycleLong in long format (for performance)
		timeSinceCycleLong = currentProcessorTime - lastProcessorTime;
		// Sets the current processor time to the last processor time
		// for the next cycle
		lastProcessorTime = currentProcessorTime;
		// Converts the timeSinceCycle into float and seconds for performance
		timeSinceCycle = (float)(timeSinceCycleLong / 1000f);
		// Increases the world clock with the timeSinceCycle.
		worldTime += timeSinceCycle;

		foodSources.update();
		happinessSources.update();
		socializeGroups.update();
		allObjects.update();

		GameObject gameObject;
		Iterator it = allObjects.iterator();

		try {
			// Order all Updateable objects to update().
			while (it.hasNext()) {
				gameObject = (GameObject) it.next();
				if (gameObject instanceof Updateable) {
					((Updateable) gameObject).update();
				}
			}
		} catch(java.util.ConcurrentModificationException e) {
			System.out.println(
					"Modification of allObjects during iteration in update."
					+ " Skipping the rest of the update cycle.");
		}
	}

	/**
	 * Starts the simulated time by setting the lastProcessorTime = currentTimeMillis()
	 * - timeSinceCycleLong.
	 */
	public void startSimulation() {
		// Recreates/simulates the lastProcessorTime by substracting the length of the
		// last cycle from the current processor time.
		lastProcessorTime = System.currentTimeMillis() - timeSinceCycleLong;
	}

	/**
	 * Stops the simulation by calculating the timeSinceCycle = currentTimeMillis() -
	 * lastProcessorTime.
	 */
	public void stopSimulation() {
		// Saves the length of the last cycle for use when the simulation starts again
		timeSinceCycleLong = System.currentTimeMillis() - lastProcessorTime;
	}

	/**
	 * Registers the specified GameObject to the correct list(s), according to it's
	 * type.
	 *
	 * @param gameObject the GameObject to register.
	 */
	public void register(GameObject gameObject) {
		// If it's an Individual, register it at allObjects
		if(gameObject instanceof Individual) {
			allObjects.register(gameObject);
		}
		// If it's a SourceNode, register it att allObjects
		else if(gameObject instanceof SourceNode) {
			allObjects.register(gameObject);
			// And register it at it's specific source list according
			// to the type of the natural resource.
			if(((SourceNode)gameObject).getNaturalResourceType() == Environment.FOOD) {
				foodSources.register((SourceNode)gameObject);
			}
			else if(((SourceNode)gameObject).getNaturalResourceType() == Environment.HAPPINESS) {
				happinessSources.register((SourceNode)gameObject);
			}
		}
		// If it's a light pillar, register it at allObjects and set the world's
		// light pillar to it.
		else if(gameObject instanceof LightPillarNode) {
			allObjects.register(gameObject);
			lightPillarNode = (LightPillarNode)gameObject;
		}
		// If its a transport node, register it at allObjects
		else if(gameObject instanceof Node) {
			allObjects.register(gameObject);
		}
		// If it's a SocializeGroup, register it at the socializeGroups,
		// but not on the allObjects list, because it shouldn't be drawn on screen.
		else if(gameObject instanceof SocializeGroup) {
			socializeGroups.register((SocializeGroup)gameObject);
		}

	}

	/**
	 * Unregisters the specified GameObject from the correct list(s), according to it's
	 * type.
	 *
	 * @param gameObject the GameObject to unregister.
	 */
	public void unRegister(GameObject gameObject) {
		// If it's an Individual, unregister it from allObjects
		if(gameObject instanceof Individual) {
			allObjects.unRegister(gameObject);
		}
		// If it's a SourceNode, unregister it from allObjects
		else if(gameObject instanceof SourceNode) {
			allObjects.unRegister(gameObject);
			// And unregister it from it's specific source list according
			// to the type of the natural resource.
		if(((SourceNode)gameObject).getNaturalResourceType() == Environment.FOOD) {
				foodSources.unRegister((SourceNode)gameObject);
			}
			else if(((SourceNode)gameObject).getNaturalResourceType() == Environment.HAPPINESS) {
				happinessSources.unRegister((SourceNode)gameObject);
			}
		}
		// If it's a transport node, unregister it from allObjects
		// NOTE THE ORDER. This must be after SourceNode.
		else if(gameObject instanceof Node) {
			allObjects.unRegister(gameObject);
		}
		// If it's a SocializeGroup, unregister it from the socializeGroups.
		else if(gameObject instanceof SocializeGroup) {
			socializeGroups.unRegister((SocializeGroup)gameObject);
		}
	}

	/**
	 * Initializes the world by setting the bounds and loading the required starting
	 * objects, such as the pillar of light and some individuals.
	 *
	 * @param mapWidth an int describing the width of the world's area.
	 * @param mapHeight an int describing the height of the world's area.
	 */
	public void initialize(MapImage mapImage) {
		if(mapImage == null) {
			throw new IllegalArgumentException("MapImage that should initialize World was null");
		}
		mapData = mapImage;
		float width = (float)mapData.getWidth();
		float height = (float)mapData.getHeight();

		// Sets the bounds of the environment (thus also the world)
		environment.setBounds(width, height);

		float maxX = World.getEnvironment().getMaxCoordinate().getX();
		float maxY = World.getEnvironment().getMaxCoordinate().getY();
		float minX = World.getEnvironment().getMinCoordinate().getX();
		float minY = World.getEnvironment().getMinCoordinate().getY();

		// Creates a light pillar in the middle of the map

		lightPillarNode = new LightPillarNode(new Point((maxX-minX)/2f,(maxY-minY)/2f));

		(new Individual(new Point(9.0f,23.0f), 1)).setName("Martin");
		(new Individual(new Point(20.0f,23.0f), 0)).setName("Henrik");
		(new Individual(new Point(23.0f,22.0f), 1)).setName("Tomas");
		(new Individual(new Point(17.0f,17.0f), 0)).setName("Andreas");
		new SocializeGroup(new Point(15.0f,15.0f));
		new SourceNode(new Point(12.0f, 15.0f), Environment.FOOD, 100);
		new SourceNode(new Point(21.0f, 9.0f), Environment.FOOD, 100);
		new SourceNode(new Point(20.0f, 20.0f), Environment.HAPPINESS, 100);
		new SourceNode(new Point(12.0f, 6.0f), Environment.HAPPINESS, 100);
		new SourceNode(new Point(18.0f, 11f), Environment.SACRIFICE, 100);
	}

}
