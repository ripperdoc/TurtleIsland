/*
 * 2003-05-20 EliasAE
 * 	Now throws FileNotFoundException when the graphics files is not found,
 * 	instead of returning null.
 *
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-04-17 EliasAE
 * 	General clean up and support for white space in extra information file.
 *
 * 2003-04-17 EliasAE
 * 	Wrote createSprite
 *
 * 2003-04-16 EliasAE
 * 	Wrote loadRenderable
 *
 * 2003-04-15 pergamon
 *  Changed package.
 *
 * 2003-04-13 EliasAE
 * 	Created the class.
 */

package graphicsengine.graphics;

import java.util.*;

import gameengine.*;
import graphicsengine.*;
import java.awt.Component;
import java.awt.MediaTracker;
import java.awt.Image;
import java.io.*;

/**
 * Manages all graphics in the game. Call this class to get the graphics
 * you are looking for.
 */
public class GraphicsManager {

	/**
	 * The directory for the images.
	 */
	private static final String IMAGES_DIRECTORY = "images" + File.separator;

	/**
	 * The extension that will be used for graphics. All graphics files
	 * MUST end with this extension.
	 */
	private static final String FILE_EXTENSION = ".png";

	/**
	 * Subdirectory to images that contains turtle graphics.
	 */
	private static final String TURTLE_DIRECTORY = "turtle" + File.separator;

	/**
	 * Subdirectory to images that contains food source graphics.
	 */
	private static final String FOOD_SOURCE_DIRECTORY =
		"foodSource" + File.separator;

	/**
	 * Subdirectory to images that contains happinessSource graphics.
	 */
	private static final String HAPPINESS_SOURCE_DIRECTORY =
		"happinessSource" + File.separator;

	/**
	 * Subdirectory to images that contains sacrificeSource graphics.
	 */
	private static final String SACRIFICE_SOURCE_DIRECTORY =
		"sacrificeSource" + File.separator;

	/**
	 * Subdirectory to images that contains lightPillarNode graphics.
	 */
	private static final String LIGHT_PILLAR_NODE_DIRECTORY =
		"lightPillarNode" + File.separator;

	/**
	 * Subdirectory to images that contains transportationNode graphics.
	 */
	private static final String TRANSPORTATION_NODE_DIRECTORY =
		"transportationNode" + File.separator;

	/**
	 * Subdirectory to images that contains foodButton graphics.
	 */
	private static final String FOOD_BUTTON_DIRECTORY =
		"buttons" + File.separator + "food" + File.separator;

	/**
	 * Subdirectory to images that contains happienessButton graphics.
	 */
	private static final String HAPPINESS_BUTTON_DIRECTORY =
		"buttons" + File.separator + "happiness" + File.separator;

	/**
	 * Subdirectory to images that contains sacrificeButton graphics.
	 */
	private static final String SACRIFICE_BUTTON_DIRECTORY =
		"buttons" + File.separator + "sacrifice" + File.separator;
	/**
	 * Subdirectory to images that conatins the exit button graphics
	 * used to display the exit button in the information panel.
	 */
	private static final String EXIT_BUTTON_GRAPHICS =
		"buttons" + File.separator + "exit" + File.separator;

	/**
	 * Subdirectory to images that contains the tile graphics.
	 */
	private static final String TILES_DIRECTORY =
		"tiles" + File.separator;

	/**
	 * All the angles of the turtle. A three digit index will be appended to
	 * this file name before the animation index is appended, before the
	 * extension.
	 */
	private static final String ANGLE_FILE_NAME = "angle";

	/**
	 * The file containing the selected circle for the turtle.
	 */
	private static final String SELECTED_FILE_NAME = "selected";

	/**
	 * The file containing the eliminated turtle image.
	 */
	private static final String ELIMINATED_FILE_NAME = "eliminated";

	/**
	 * The parts of ButtonGraphic's filenames.
	 */
	private static final String PRESSED = "pressed";
	private static final String ACTIVE = "active";
	private static final String INACTIVE = "inactive";

	/**
	 * The component to use for the graphics.
	 */
	private Component component;

	/**
	 * The graphics for a turtle.
	 */
	private GameObjectGraphics turtleGraphics;

	/**
	 * The graphics for a light pillar.
	 */
	private GameObjectGraphics lightPillarNodeGraphics;

	/**
	 * The graphics for a transportation node.
	 */
	private GameObjectGraphics transportationNodeGraphics;

	/**
	 * Array with grapics for every source node. Indexed like all other
	 * resource arrays.
	 */
	private GameObjectGraphics[] sourceNodeGraphics;

	/**
	 * The graphics for a food button
	 */
	private ButtonGraphics foodButtonGraphics;

	/**
	 * The graphics for a happiness button
	 */
	private ButtonGraphics happinessButtonGraphics;

	/**
	 * The graphics for a sacrifice button
	 */
	private ButtonGraphics sacrificeButtonGraphics;

	/**
	 * The graphics for the exit button in the information panel.
	 */
	private ButtonGraphics exitButtonGraphics;

	/**
	 * The graphics for tiles mapped with the directory they are in as String.
	 * Values are TileGraphics.
	 */
	private java.util.Map allTilesGraphics;

	/**
	 * The graphics for a map. Specifies what kind of tiles there are at each
	 * position with colors.
	 */
	private Image mapImage;

	/**
	 * Creates the graphics manager. Loads the graphics from file.
	 */
	public GraphicsManager(Component component) 
			throws FileNotFoundException {
		this.component = component;

		// Load all resource source graphcis.
		sourceNodeGraphics = new GameObjectGraphics[
			World.getEnvironment().getNumberOfResourceTypes()
			];
		sourceNodeGraphics[Environment.FOOD] =
			loadGameObjectGraphics(FOOD_SOURCE_DIRECTORY);
		sourceNodeGraphics[Environment.HAPPINESS] =
			loadGameObjectGraphics(HAPPINESS_SOURCE_DIRECTORY);
		sourceNodeGraphics[Environment.SACRIFICE] =
			loadGameObjectGraphics(SACRIFICE_SOURCE_DIRECTORY);

		// Load turtle graphics
		turtleGraphics = loadGameObjectGraphics(TURTLE_DIRECTORY);
		lightPillarNodeGraphics =
			loadGameObjectGraphics(LIGHT_PILLAR_NODE_DIRECTORY);
		transportationNodeGraphics =
			loadGameObjectGraphics(TRANSPORTATION_NODE_DIRECTORY);

		// load button graphics
		foodButtonGraphics = loadButtonGraphics(FOOD_BUTTON_DIRECTORY);
		happinessButtonGraphics = loadButtonGraphics(HAPPINESS_BUTTON_DIRECTORY);
		sacrificeButtonGraphics = loadButtonGraphics(SACRIFICE_BUTTON_DIRECTORY);
		exitButtonGraphics = loadButtonGraphics(EXIT_BUTTON_GRAPHICS);

		// load tile graphics
		loadAllTilesGraphics();
	}

	/**
	 * Loads graphics for a game object. The graphics of a game object should
	 * be in a separate directory and named after the standardized game object
	 * graphics names. Currently: angle000.png selected.png and eliminated.png
	 *
	 * @param directory the path to the directory relative the image directory.
	 * @return a graphics package with the graphics in the directory indicated
	 * by the paramter.
	 */
	private GameObjectGraphics loadGameObjectGraphics(String directory) 
			throws FileNotFoundException {
		// List with the turtle in all angles.
		List angles = new ArrayList();

		// As long as there are more angles to get, load them and place them
		// in the list.
		int angleIndex = 0;
		while (angleIndex < 1000) {
			String sourceBaseName =
				directory
				+ ANGLE_FILE_NAME
				// The index for the angle should be padded with zeros
				// to width of three digits.
				+ (angleIndex / 100) % 10
				+ (angleIndex / 10) % 10
				+ angleIndex % 10
				;
			Renderable currentAngle =
				loadRenderable(sourceBaseName);
			if (currentAngle == null) {
				break;
			}
			angles.add(currentAngle);
			angleIndex++;
		}

		if (angles.size() == 0) {
			throw new FileNotFoundException("Could not load game object " +
					"graphics from " + directory);
		}

		// Load the selected image for the turtle
		Renderable selected = 
				loadRenderable(directory + SELECTED_FILE_NAME);

		// Load the image of an eliminated turtle
		Renderable eliminated = 
				loadRenderable(directory + ELIMINATED_FILE_NAME);
		
		Renderable defaultImage = (Renderable) angles.get(0);
		GraphicsExtraInformation extraInformation =
			loadExtraInformation(directory, defaultImage);
		// Create the turtle graphics.
		return new GameObjectGraphics(
				angles,
				selected,
				eliminated,
				extraInformation.getHotSpotX(),
				extraInformation.getHotSpotY()
				);
	}

	/**
	 * Loads graphics information for a file.
	 *
	 * @param directory the directory to load graphics information from.
	 * @param defaultImage the image to try to generate default information
	 * from if the file cannot be loaded. Can be null in which case
	 * a best guess without an image will be done, which is probably
	 * terrible wrong.
	 * @return the graphcis information for the directory specified.
	 */
	private GraphicsExtraInformation loadExtraInformation(
			String directory, Renderable defaultImage) {
		File extraInformationFile = new File(IMAGES_DIRECTORY, directory);
		GraphicsExtraInformation extraInformation = null;
		try {
			// Load the extra information.
			extraInformation = new GraphicsExtraInformation(
					extraInformationFile.getPath());
		} catch (Exception e) {
			Debug.log("Failed to create GraphicsExtraInformation: " + e.getMessage());
		}

		// if no extraInformation is loaded then guess the hotspot.
		if (extraInformation == null) {
			if (defaultImage == null) {
				extraInformation = new GraphicsExtraInformation(0, 0);
			}
			else {
				extraInformation = new GraphicsExtraInformation(
						defaultImage.getWidth() / 2,
						defaultImage.getHeight() / 2
						);
			}
		}
		return extraInformation;
	}

	/**
	 * Loads graphics for a button. The graphics of a button should
	 * be in a separate directory under a "buttons" directory and named after
	 * the standardized button graphics names:
	 * active.png inactive.png pressedactive and pressedinactive.png
	 *
	 * @param directory the path to the directory relative the image directory.
	 * @return a graphics package with the graphics in the directory indicated
	 * by the paramter.
	 */
	private ButtonGraphics loadButtonGraphics(String directory) 
			throws FileNotFoundException {
		Renderable inactive = loadRenderable(directory + INACTIVE);
		Renderable active = loadRenderable(directory + ACTIVE);
		Renderable pressedInactive = loadRenderable(directory + PRESSED + INACTIVE);
		Renderable pressedActive = loadRenderable(directory + PRESSED + ACTIVE);

		if (
				inactive == null &&
				active == null && 
				pressedInactive == null &&
				pressedActive == null
				) {
			throw new FileNotFoundException("The button graphics could no be " +
					"loaded from folder: " + directory);
		}
		// Load the extra information.
		GraphicsExtraInformation extraInformation =
			loadExtraInformation(directory, inactive);
		return new ButtonGraphics(
				inactive,
				active,
				pressedInactive,
				pressedActive,
				extraInformation.getHotSpotX(),
				extraInformation.getHotSpotY()
				);
	}

	/**
	 * Loads all TileGraphics in the tile directory. For each
	 * subdirectory to the tiles directory, this method
	 * will call loadTileGraphics to load a TileGraphics from
	 * that directory. All those TileGraphics will be collected
	 * into a Map with TileGraphics:es named allTilesGraphics.
	 */
	public void loadAllTilesGraphics() 
			throws FileNotFoundException {
		allTilesGraphics = new HashMap();
		File tilesDirectoryFile = new File(IMAGES_DIRECTORY, TILES_DIRECTORY);
		File[] filesInDirectory = tilesDirectoryFile.listFiles();
		if (filesInDirectory == null) {
			return;
		}

		// Load all the tiles, one from each subdirectory in the directory
		for (
				int fileIndex = 0;
				fileIndex < filesInDirectory.length;
				fileIndex++
			) {
			if (filesInDirectory[fileIndex].isDirectory()) {
				String directoryName = filesInDirectory[fileIndex].getName();
				allTilesGraphics.put(
						directoryName, loadTileGraphics(directoryName + File.separator));
			}
		}
	}

	/**
	 * Loads a TileGraphics from a directory. The TileGraphics will
	 * contain all the png-files in that directory as variations.
	 *
	 * @param directory the directory relative to the tiles directory
	 * where the tile graphics should be loaded from.
	 * @return a TileGraphics loaded from the specified directory.
	 */
	private TileGraphics loadTileGraphics(String directory) 
			throws FileNotFoundException {
		java.util.Map tileVariations = new HashMap();
		File tileDirectoryFile =
			new File(new File(IMAGES_DIRECTORY, TILES_DIRECTORY), directory);
		File[] filesInDirectory = tileDirectoryFile.listFiles();
		if (filesInDirectory == null) {
			return new TileGraphics(tileVariations);
		}

		//Load each graphics file in the directory as a own tilevariation.
		for (int fileIndex = 0; fileIndex < filesInDirectory.length; fileIndex++) {
			String fileName = filesInDirectory[fileIndex].getName();
			if (fileName.endsWith(FILE_EXTENSION)) {

				// Skip any digits at the end, because that is animation
				// indices.
				int stringIndex =
					fileName.length() - FILE_EXTENSION.length() - 1;
				for (
						;
						stringIndex > 0 &&
							Character.isDigit(fileName.charAt(stringIndex));
						stringIndex--
					)
					;

				// The base name if the name without the animation index at
				// the end. Use this to load the tile.
				String fileBaseName = fileName.substring(0, stringIndex + 1);
				Renderable tileRenderable =
					loadRenderable(TILES_DIRECTORY + directory + fileBaseName);
				if (tileRenderable == null) {
					throw new FileNotFoundException("The tile graphics could not "
					+ "be loaded:" + fileBaseName);
				}
				tileVariations.put(fileBaseName, tileRenderable);
			}
		}
		return new TileGraphics(tileVariations);
	}

	/**
	 * Loads a renderable from file. If there is only one file with the given
	 * base name the renderable will be a sprite, otherwise if will be an
	 * animation with all the images with the given base name.
	 * Animations is not implemented, so this function will in this
	 * case return null.
	 *
	 * @param component the component used to load the images.
	 * @param sourceBaseName the base of the file name and path relative to
	 * the image directory. To load a animation for a pressed button this
	 * parameter could be "button/pressed", which will load the animation
	 * of all the files "%CurrentDirectory%/images/button/pressed*.png" where
	 * can be any digit in numerical order.
	 * @return the renderable loaded from the images or null if no graphics
	 * files could be found or could be loaded. If there is no files
	 * at all in the folder the FileNotFoundException is thrown.
	 * @exception FileNotFoundException when the indicated folder
	 * does not contain any files at all. If the folder contain one or
	 * more files, this exception is not thrown. If there is files in
	 * the folder, but no graphics files or none of the graphics
	 * files could be loaded, this method returns null instead of 
	 * throwing an exception.
	 */
	private Renderable loadRenderable(String sourceBasePath) 
			throws FileNotFoundException {
		// Split the path in directory and file name.
		File sourceBasePathFile = new File(IMAGES_DIRECTORY, sourceBasePath);
		File sourceDirectory = sourceBasePathFile.getParentFile();
		String sourceBaseFileName = sourceBasePathFile.getName();
		// Retrieve all the files in the directory in question.
		File[] filesInDirectory = sourceDirectory.listFiles();
		// Check if the directory did not exist or if another error occured.
		if (filesInDirectory == null) {
			throw new FileNotFoundException("Graphics image files not found");
		}

		SortedMap fileNames = new TreeMap();
		// For each file in the directory, test if the file matches the sought
		// ones, as specified by sourceBasePath's file name. If the file name
		// match, that is the file name is sourceBasePath's file name with
		// a optional variable number of digits before the extension, the
		// digits before the extension is extracted and the file name is
		// sorted in increasing order compared to the digits of other file
		// names.
		for (int i = 0; i < filesInDirectory.length; i++) {
			String fileName = filesInDirectory[i].getName();
			// Check to see if it is the right kind of file. If the file name
			// starts correctly, extract the digits, check the extension and
			// add the file to the list.
			if (fileName.startsWith(sourceBaseFileName)) {
				int currentChar = sourceBaseFileName.length();
				while (Character.isDigit(fileName.charAt(currentChar))) {
					currentChar++;
				}
				String fileNumber = fileName.substring(
						sourceBaseFileName.length(),
						currentChar
						);
				if (fileName.substring(currentChar).equals(FILE_EXTENSION)) {
					Integer fileKey = null;
					if (fileNumber.equals("")) {
						fileKey = new Integer(0);
					}
					else {
						fileKey = new Integer(fileNumber);
					}
					// Add the file to the list if everything was alright.
					fileNames.put(fileKey, fileName);
				}
			}
		}
		Collection sortedFileNames = fileNames.values();
		MediaTracker tracker = new MediaTracker(component);
		List imageList = new ArrayList();
		// Load all the images
		Iterator iterator = sortedFileNames.iterator();
		while (iterator.hasNext()) {
			String fileName = (String) iterator.next();
			Image currentImage = component.getToolkit().getImage(
					(new File(sourceDirectory, fileName)).getPath()
					);
			imageList.add(currentImage);
			tracker.addImage(currentImage, 0);
		}
		// Wait so that the images are all loaded.
		try {
			tracker.waitForAll();
		}
		catch (InterruptedException interuptedException) {
		}
		// Return either an animation or a sprite, depening on if there
		// was one image or more than one image.
		if (imageList.size() == 0) {
			return null;
		}
		if (imageList.size() == 1) {
			return new Sprite(component, (Image) imageList.get(0));
		}
		else {
			// TODO no animation class yet.
			return null;
		}
	}

	/**
	 * Creates a sprite from an image.
	 * @param image the image that the sprite will be created from.
	 * @return a sprite that can be used to paint the image sent as
	 * parameter on the screen.
	 */
	public Sprite createSprite(Image image) {
		return new Sprite(component, image);
	}

	/**
	 * Retrieves graphics for the turtle.
	 * @return a List with the graphics for the turtle.
	 */

	public GameObjectGraphics getTurtleGraphics() {
		return turtleGraphics;
	}

	/**
	 * Retrieves the graphics for a light pillar node.
	 */
	public GameObjectGraphics getLightPillarNodeGraphics() {
		return lightPillarNodeGraphics;
	}

	/**
	 * Retrieves the graphics for a transportation node.
	 */
	public GameObjectGraphics getTransportationNodeGraphics() {
		return transportationNodeGraphics;
	}

	/**
	 * Retrieves the graphics for the tiles.
	 * @return the graphics for a tile.
	 */
	public TileGraphics getTileGraphics(String tileType) {
		return (TileGraphics) allTilesGraphics.get(tileType);
	}

	/**
	 * Retrieves the graphics for a food button.
	 */
	public ButtonGraphics getFoodButtonGraphics() {
		return foodButtonGraphics;
	}

	/**
	 * Retrieves the graphics for a happiness button.
	 */
	public ButtonGraphics getHappinessButtonGraphics() {
		return happinessButtonGraphics;
	}

	/**
	 * Retrieves the graphics for a sacrifice button.
	 */
	public ButtonGraphics getSacrificeButtonGraphics() {
		return sacrificeButtonGraphics;
	}

	/**
	 * Retrieves the graphics for the exit button in the information panel.
	 */
	public ButtonGraphics getExitButtonGraphics() {
		return exitButtonGraphics;
	}

	/**
	 * Retrieves the graphics for a source node.
	 * @param resourceType the type of resource of the source node.
	 * @return the graphics package for the indicated type of resource
	 * source node.
	 * @exception IllegalArgumentException the argument was invalid.
	 */
	public GameObjectGraphics getSourceNodeGraphics(int resourceType) {
		if (
				resourceType < 0
				|| resourceType > World.getEnvironment().getNumberOfResourceTypes()
		   ) {
			throw new IllegalArgumentException("Invalid resource type");
		}
		return sourceNodeGraphics[resourceType];
	}
}

class GraphicsExtraInformation {

	/**
	 * The file name of the file that in each directory specifed
	 * extra infromation about the graphics in that directory.
	 */
	private static final String GRAPHICS_EXTRA_INFORMATION_FILE_NAME =
		"extraInfo";

	/**
	 * The x position of the hot spot in pixels
	 * relative to the upper left corner of the image.
	 */
	private int hotSpotX;

	/**
	 * The y position of the hot spot in pixels
	 * relative to the upper left corner of the image.
	 */
	private int hotSpotY;

	/**
	 * Creates extra information for a graphics file.
	 * @param hotSpotX the x position of the hot spot in pixels
	 * relative to the upper left corner of the image.
	 * @param hotSpotY the y position of the hot spot in pixels
	 * relative to the upper left corner of the image.
	 */
	public GraphicsExtraInformation(int hotSpotX, int hotSpotY) {
		this.hotSpotX = hotSpotX;
		this.hotSpotY = hotSpotY;
	}

	public GraphicsExtraInformation(String sourceDirectory)
		throws IOException, GraphicsExtraInformationFormatException {
		loadGraphicsExtraInformation(sourceDirectory);
	}

	/**
	 * Loads extra information about the graphics in a directory. The file
	 * format is a single line starting with the character
	 * '(' followed by two integers as text(separated by a single ',') and
	 * a ')' at the end. This single line specifies the x and y
	 * coordinate for the hot spot. The x coordinate is the first
	 * integer and the y coordinate is the second. Example of file contents
	 * <br />
	 * (34,51)
	 *
	 * @param sourceDirectory the directory in which the graphics files,
	 * for which the extra information should be loaded, exists.
	 * @return the extra infromation about the files in the directory.
	 * @exception IOException if the file with the extra information
	 * or the directory could not be read from.
	 * @exception GraphicsExtraInformationFormatException the contents of the
	 * file was invalid.
	 */
	private void loadGraphicsExtraInformation(
			String sourceDirectory)
			throws IOException, GraphicsExtraInformationFormatException {
		// Build the file name
		File extraInformationFile = new File(
				sourceDirectory,
				GRAPHICS_EXTRA_INFORMATION_FILE_NAME
				);
		//Open the file
		BufferedReader reader =
			new BufferedReader(new FileReader(extraInformationFile));
		try {
			// Read the first line, which will contain the hot spot
			// on the form (x,x) where x is any whole number.
			// Example:
			// (23,35)
			String currentLine = skipWhiteSpace(reader.readLine());
			if (currentLine.charAt(0) != '(') {
				throw new GraphicsExtraInformationFormatException();
			}

			int currentCharIndex = 1;
			while (Character.isDigit(currentLine.charAt(currentCharIndex))) {
				currentCharIndex++;
			}

			hotSpotX =
				Integer.parseInt(currentLine.substring(1, currentCharIndex));

			if (currentLine.charAt(currentCharIndex) != ',') {
				throw new GraphicsExtraInformationFormatException();
			}

			currentCharIndex++;

			int hotSpotYStartIndex = currentCharIndex;
			while (Character.isDigit(currentLine.charAt(currentCharIndex))) {
				currentCharIndex++;
			}

			hotSpotY = Integer.parseInt(
					currentLine.substring(hotSpotYStartIndex, currentCharIndex)
					);

			if (currentLine.charAt(currentCharIndex) != ')') {
				throw new GraphicsExtraInformationFormatException();
			}
		} catch (StringIndexOutOfBoundsException e) {
			throw new GraphicsExtraInformationFormatException();
		} finally {
			reader.close();
		}
	}

	/**
	 * Strips all white space from a string. White space characters is the
	 * character for which Character.isWhitespace return true.
	 *
	 * @param string any string.
	 * @return the parameter without any whitspace.
	 */
	private String skipWhiteSpace(String string) {
		StringBuffer buffer = new StringBuffer(string.length());
		for (
				int currentChar = 0;
				currentChar < string.length();
				currentChar++
			) {
			if (!Character.isWhitespace(string.charAt(currentChar))) {
				buffer.append(string.charAt(currentChar));
			}
		}
		return buffer.toString();
	}

	/**
	 * Retrieves the hot spot.
	 * @return the x position of the hot spot in pixels
	 * relative to the upper left corner of the image.
	 */
	public int getHotSpotX() {
		return hotSpotX;
	}

	/**
	 * Retrieves the hot spot.
	 * @return the y position of the hot spot in pixels
	 * relative to the upper left corner of the image.
	 */
	public int getHotSpotY() {
		return hotSpotY;
	}
}

/**
 * Indicates that the file containing the extra information about graphical
 * files could not be read due to invalid contents.
 */
class GraphicsExtraInformationFormatException extends Exception {}
