/*
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-19 Martin
 *	Corrected the use of a MapImage.
 *
 * 2003-05-18 EliasAE
 * 	Fixed drawing of tiles.
 *
 * 2003-05-18 pergamon
 *  fixed the scroll, neither "hacks" nor stop way out of the maps borders.
 *  Also optimiezed how the map and graphicalObjects are draw.
 *
 * 2003-05-17 pergamon
 *  Added a constructor to be able to set the width and height of the view.
 *  Also moved the deletion of dead gameObjects from GraphicalObject.draw()
 *  to this.updateGraphicalGameObjects();
 *
 * 2003-05-11 EliasAE
 * 	Failed to fix scroll.
 *
 * 2003-05-10 pergamon
 *  Wrote updateGraphicalGameObjects(), createGraphicalGameObject,
 *  getGraphicalGameObject and some other related things.
 *
 * 2003-05-01 pergamon
 *  Changed so that getObjectFromPosition now doesn't contaion any logic except
 *  what is needed to return all objects from the given position.
 *
 * 2003-04-15 pergamon
 *  implemented scroll function.  Changed from using Map coordinates ti Iso.
 *  Added functions: getIsoX, getIsoY, setMoveX and setMoveY
 *  Added variables: moveX, moveY, viewMoveOffset and scrollSpeedUnit
 *
 * 2003-04-14 EliasAE
 * 	The parameter map in the constructor is now saved in the member map.
 *
 * 2003-04-14 pergamon
 *  Wrote getObjectsFromPosition. Added drawMouseSelectBox.
 *
 * 2003-04-14 EliasAE
 * 	Changed to using the GraphicsManager.
 *
 * 2003-04-13 EliasAE
 * 	Changed to using the Rendeable interface.
 *
 * 2003-04-12 pergamon
 *  Added functionality for hitTest(int,int).
 *
 * 2003-04-11 pergamon
 *  Chaned the way GraphicalObjects is drawn. Now it's closer to how it should
 *  work. Wrote the constructor to load all GameObjects from the GameEngine.
 */
package graphicsengine;

import gameengine.*;
import gameengine.World;
import gameengine.GameObjectList;
import gameengine.gameobjects.*;
import graphicsengine.graphics.*;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.*;

/**
 * The graphical representation of the game world. Contains a map and game
 * objects. The game objects are taken from the game engines World.
 * This class and InformationPanel together make up the whole screen.
 */
public class View extends GraphicalObject {

	/**
	 * Key to keep track of Node's small buttons.
	 */
	private static final String FOOD_BUTTON = "foodButton";

	/**
	 * Key to keep track of Node's small buttons.
	 */
	private static final String HAPPINESS_BUTTON = "happinessButton";

	/**
	 * Key to keep track of Node's small buttons.
	 */
	private static final String SACRIFICE_BUTTON = "sacrificeButton";

	/**
	 * The x-coordinate of this view on the Map in isometric coordinates
	 */
	private float isoX;

	/**
	 * The y-coordinate of this view on the Map in isometric coordinates
	 */
	private float isoY;

	/**
	 * The width of this view, currently taken from the width of the displaymode.
	 */
	private int width;

	/**
	 * The height of this view, currently taken from the height of the displaymode.
	 */
	private int height;

	/**
	 * How close to the view's edges the mouse can come without any try to scroll
	 * the View.
	 */
	private int viewMoveOffset = 30;

	/**
	 * A unit to calculate how fast the view shall scroll.
	 */
	private static final float scrollSpeedUnit = 0.423226666667f;

	/**
	 * The direction and speed the view shall scroll. This is calculated from
	 * how close to the edge of the view the mouse is relative to the
	 * viewMoveOffset times the scrollSpeedUnit.
	 */
	private float moveX;
	private float moveY;

	private boolean drawSelectBox;
	private int selectionBoxLeftX;
	private int selectionBoxTopY;
	private int selectionBoxRightX;
	private int selectionBoxBottomY;

	/**
	 * The min coordinates for scrolling.
	 */
	private float scrollMinX;

	/**
	 * The min coordinates for scrolling.
	 */
	private float scrollMinY;

	/**
	 * The max coordinates for scrolling.
	 */
	private float scrollMaxX;

	/**
	 * The max coordinates for scrolling.
	 */
	private float scrollMaxY;

	/**
	 * A collection of GameObjects that keep track on what was drawn
	 * in the last cykle.
	 */
	private Collection gameObjects = new ArrayList();

	/**
	 * A map with tiles.
	 */
	private Map map;

	/**
	 * Object that manages graphics, that one can retrieve graphics from.
	 */
	GraphicsManager graphicsManager;

	/**
	 * Creates a view with a Map and with a position on this. It's width and
	 * height is taken from the GraphicsEngine's displaymode.
	 *
	 * @param graphicsManager the graphics manager to load graphics from.
	 * @param mapImage the map image to read map data from.
	 * @param viewPositionIsoX the position of the view over the map in iso
	 * coordinates.
	 * @param viewPositionIsoY the positoin of the view over the map in iso
	 * coordinates.
	 */
	public View(
			GraphicsManager graphicsManager,
			MapImage mapImage,
			int viewPositionIsoX,
			int viewPositionIsoY
			) {
		this(
				graphicsManager,
				mapImage,
				viewPositionIsoX,
				viewPositionIsoY,
				GraphicsEngine.getDisplayMode().getWidth(),
				GraphicsEngine.getDisplayMode().getHeight()
				);
	}

	/**
	 * Creates a view with a Map and with a position. It's width and
	 * height is taken from the GraphicsEngine's displaymode.
	 *
	 * @param graphicsManager the graphics manager to load graphics from.
	 * @param mapImage the MapImage to read the map data from.
	 * @param viewPositionIsoX the position of the view over the map in iso
	 * coordinates.
	 * @param viewPositionIsoY the positoin of the view over the map in iso
	 * coordinates.
	 * @param viewWidth the width of the view in screen coordinates.
	 * @param viewHeight the height of the view in screen coordinates.
	 */
	public View(
			GraphicsManager graphicsManager,
			MapImage mapImage,
			int viewPositionIsoX,
			int viewPositionIsoY,
			int viewWidth,
			int viewHeight
			) {
		this(graphicsManager,
				mapImage,
				viewPositionIsoX,
				viewPositionIsoY,
				viewWidth,
				viewHeight,
				0, // viewScreenX
				0  // viewScreenY
				);
	}

	/**
	 * Creates a view with a Map and with a position. It's width and
	 * height is taken from the GraphicsEngine's displaymode.
	 *
	 * @param graphicsManager the graphics manager to load graphics from.
	 * @param mapImage the MapImage to read the map data from.
	 * @param viewPositionIsoX the position of the view over the map in iso
	 * coordinates.
	 * @param viewPositionIsoY the positoin of the view over the map in iso
	 * coordinates.
	 * @param viewWidth the width of the view in screen coordinates.
	 * @param viewHeight the height of the view in screen coordinates.
	 * @param viewScreenX the position of the view relative to the window or
	 * screen if fullscreen. In pixels.
	 * @param viewScreenY the position of the view relative to the window or
	 * screen if fullscreen. In pixels.
	 */
	public View(
			GraphicsManager graphicsManager,
			MapImage mapImage,
			int viewPositionIsoX,
			int viewPositionIsoY,
			int viewWidth,
			int viewHeight,
			int viewScreenX,
			int viewScreenY
			) {
		super(viewScreenX, viewScreenY);
		this.graphicsManager = graphicsManager;

		// Initialize the CoordinateConverter.	// TODO this is ugly.
		CoordinateConverter.getCoordinateConverter().setView(this);

		this.map = new Map(graphicsManager, mapImage);

		// load all tiles from the map
		map.loadTiles();
		this.isoX = viewPositionIsoX;
		this.isoY = viewPositionIsoY;
		this.width = viewWidth;
		this.height = viewHeight;
	}

	/**
	 * Updates all GraphicalGameObjects from the worlds all gameobjects.
	 * Creates new if none is present.
	 */
	void updateGraphicalGameObjects() {
		// get all GameObjects from the gameengine
		Iterator gameObjectsIterator = (World.getWorld().getAllObjects()).iterator();

		while (gameObjectsIterator.hasNext()) {
			// TODO GameObjecs that aren't on screen shouldn't be added.
			GameObject tmpGameObject = (GameObject) gameObjectsIterator.next();
			// If the GameObject isn't represented on screen then we must
			// create a graphical representation of it.
			if (!gameObjects.contains(tmpGameObject)) {
				try {
					createGraphicalGameObject(tmpGameObject);
				} catch (GraphicalGameObjectExistsException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Retrives the offset of how close to the screen edges the mouse can come
	 * before the view scrolls.
	 *
	 * @return how close the mouse can come to the screen edge.
	 */
	public int getViewMoveOffset() {
		return viewMoveOffset;
	}

	/**
	 * Retrieves the width of the view.
	 *
	 * @return the width of the view in pixels. This cannot be less than zero.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Retrieves the height of the view.
	 *
	 * @return the height of the view in pixels. This cannot be less than zero.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Draws the view.
	 *
	 * @param graphics the graphics object to draw the view on.
	 */
	public void draw(Graphics2D graphics) {
		// Scroll the view if a movespeed is set.
		scroll(moveX, moveY);

		graphics.setColor(Color.blue.darker());
		graphics.fillRect(getScreenX(), getScreenY(), width, height);

		// draw the map.
		Iterator tileIterator =	map.getTileIterator(
				getScreenX(), getScreenY(), width, height);
		while (tileIterator.hasNext()) {
			Tile tile = (Tile) tileIterator.next();
			tile.draw(graphics);
		}

		// update all GraphicalGameObjects from the gameEngine.
		updateGraphicalGameObjects();

		// draw all GraphicalObjects.
		super.draw(graphics);

		// draw a selectionbox if any is present.
		if (drawSelectBox) {
			drawSelectionBox(graphics);
		}
	}

	/**
	 * Move the view relative to its current position over the map.
	 *
	 * @param moveX the number of pixels to move the view over the map on the
	 * x-axis. This is in iso-coordinates.
	 */
	private void scroll(float scrollX, float scrollY) {
		if (scrollX != 0 || scrollY != 0) {
			boolean haveScrollBounds = (map != null);

			if (haveScrollBounds) {
				CoordinateConverter cc =
						CoordinateConverter.getCoordinateConverter();
				Point[] corners = {
						new Point(0, 0),
						new Point(map.getWidth(), 0),
						new Point(0, map.getHeight()),
						new Point(map.getWidth(), map.getHeight())
				};
				scrollMinX = cc.mapToIsoX(corners[2]);
				scrollMinY = cc.mapToIsoY(corners[0]);
				scrollMaxX = cc.mapToIsoX(corners[1]) - width + Tile.getWidth();
				scrollMaxY = cc.mapToIsoY(corners[3]) - height + Tile.getHeight();
			}

			isoX += scrollX;
			isoY += scrollY;

			if (haveScrollBounds) {
				if (isoX > scrollMaxX) {
					isoX = scrollMaxX;
				}
				if (isoX < scrollMinX) {
					isoX = scrollMinX;
				}

				if (isoY > scrollMaxY) {
					isoY = scrollMaxY;
				}
				if (isoY < scrollMinY) {
					isoY = scrollMinY;
				}
			}
		}
	}

	/**
	 * This makes the view scroll over the map in the x-direction that is set.
	 * For example -1 move the view left. 1 move the view right.
	 *
	 * @param moveX the speed to move the view left or right.
	 */
	public void setMoveX(int moveX) {
		this.moveX = moveX * scrollSpeedUnit;
	}

	/**
	 * This makes the view scroll over the map in the y-direction that is set.
	 * i.e. -1 move the view up. 1 move the view down.
	 *
	 * @param moveY the speed to move the view up or down.
	 */
	public void setMoveY(int moveY) {
		this.moveY = moveY * scrollSpeedUnit;
	}

	/**
	 * Retrieves the map used by this view.
	 * @return the map that is used to paint the contents of this view.
	 */
	Map getMap() {
		return map;
	}

	/**
	 *
	 * @return
	 */
	public float getIsoX() {
		return isoX;
	}

	/**
	 *
	 * @return
	 */
	public float getIsoY() {
		return isoY;
	}

	/**
	 * Returns the current x-position in map-coordinates of this view.
	 *
	 * @return the current map x-position
	 */
	public float getMapX() {
		return CoordinateConverter.getCoordinateConverter().isoToMapX(isoX, isoY);
	}

	/**
	 * Returns the current y-position in map-coordinates of this view.
	 *
	 * @return the current map y-position
	 */
	public float getMapY() {
		return CoordinateConverter.getCoordinateConverter().isoToMapY(isoX, isoY);
	}

	/**
	 * Sets the x-position of the view in map-coordinates.
	 *
	 * @param mapX the new x-map-coordinate.
	 */
	public void setIsoX(float isoX) {
		this.isoX = isoX;
	}

	/**
	 * Sets the y-position of the view in map-coordinates.
	 *
	 * @param mapY the new y-map-coordinate.
	 */
	public void setIsoY(float isoY) {
		this.isoY = isoY;
	}

	/**
	 * Draw a rectangle that represents a "selectionbox" wich can select
	 * GraphicalObjects.
	 *
	 * @param screenLeftX
	 * @param screenTopY
	 * @param screenRightX
	 * @param screenBottomY
	 */
	public void drawMouseSelectBox(
			int screenLeftX, int screenTopY, int screenRightX, int screenBottomY) {
		drawSelectBox = true;
		selectionBoxLeftX = screenLeftX;
		selectionBoxTopY = screenTopY;
		selectionBoxRightX = screenRightX;
		selectionBoxBottomY = screenBottomY;
	}

	/**
	 * Draw the "selectionbox on screen.
	 *
	 * @param graphics
	 */
	private void drawSelectionBox(Graphics2D graphics) {
		graphics.setColor(Color.blue);
		graphics.drawRect(
				// topLeft coordinate
				selectionBoxLeftX, selectionBoxTopY,
				// width
				selectionBoxRightX - selectionBoxLeftX,
				// height
				selectionBoxBottomY - selectionBoxTopY
				);
	}

	/**
	 * Retrieves all selectable game objects that is at a given pixel on the screen.
	 *
	 * @param screenX the x-coordinate for the pixel to check for objects at.
	 * This is relative to the top left corner of the screen.
	 * @param screenY the y-coordinate for the pixel to check for objects at.
	 * This is relative to the top left corner of the screen.
	 * @return a collection with the objects at the positon.
	 */
	public List getObjectFromPosition(int screenX, int screenY) {
		drawSelectBox = false;
		List tmpList = new ArrayList();
		//Get all objects that are on screen.
		Iterator tmpGraphicalObjects = getSubObjects().iterator();
		while (tmpGraphicalObjects.hasNext()) {
			GraphicalObject tmpGraphicalObject =
					(GraphicalObject) tmpGraphicalObjects.next();
			if (tmpGraphicalObject instanceof Selectable) {
				// Check if there is any object at the position.
				if (((Selectable) tmpGraphicalObject).hitTest(screenX, screenY)) {
					// add the object to the "hitted" objects.
					tmpList.add(tmpGraphicalObject);
				}

				// If the object have Subobjects and they are visible,
				// check if any of those are hit.
				if (tmpGraphicalObject.isSubObjectsShown() &&
						tmpGraphicalObject.getSubObjects().size() > 0) {
					Iterator subObjectsIterator = tmpGraphicalObject.getSubObjects().
							iterator();
					while (subObjectsIterator.hasNext()) {
						GraphicalObject subObject =
								(GraphicalObject) subObjectsIterator.next();
						if (subObject instanceof Selectable) {
							// Check if there is any subObject at the position.
							if (((Selectable) subObject).hitTest(screenX, screenY)) {
								// add the subObject to the "hitted" objects.
								tmpList.add(subObject);
							}
						}
					}
				}
			}
		}
		return tmpList;
	}

	/**
	 * Retrieves all selectable graphical objects in a given rectangle on the screen.
	 *
	 * @param screenLeftX the left bound of the rectangle in pixels relative to
	 * the top left corner of the screen. This pixel is part of the rectangle,
	 * that is inclusive bound. This value must be greater or equal to zero and
	 * less or equal to screenRightX.
	 *
	 * @param screenTopY the top bound of the rectangle in pixels relative to
	 * the top left corner of the screen.This pixel is part of the rectangle,
	 * that is inclusive bound.This value must be greater or equal to zero and
	 * less or equal to screenBottomY.
	 *
	 * @param screenRightX the right bound of the rectangle in pixels relative
	 * to the top left corner of the screen. This pixel is not part of the
	 * rectangle, that is exclusive bound. This value must be less or equal to
	 * the width of the screen.
	 *
	 * @param screenBottomY the bottom bound of the rectangle in pixels
	 * relative to the top left corner of the screen. This pixel is not part of
	 * the rectangle, that is exclusive bound. This value must be less or equal
	 * to the height of the screen.
	 *
	 * @return a collection with all the objects contained in the rectangle.
	 */
	public List getObjectFromPosition(int screenLeftX, int screenTopY,
			int screenRightX, int screenBottomY) {
		drawSelectBox = false;
		List tmpList = new ArrayList();

		// get all objects that are on screen.
		Iterator tmpGraphicalObjects = getSubObjects().iterator();

		// get all objectes that are within the specified area.
		while (tmpGraphicalObjects.hasNext()) {
			GraphicalObject tmpGraphicalObject =
					(GraphicalObject) tmpGraphicalObjects.next();
			if (tmpGraphicalObject instanceof Selectable) {
				// Check what objects that are within the area.
				if (((Selectable) tmpGraphicalObject).hitTest(screenLeftX, screenTopY,
						screenRightX, screenBottomY)) {
					tmpList.add(tmpGraphicalObject);
				}
			}
		}
		return tmpList;
	}

	/**
	 * Adds a graphical object to the list of graphical objects that
	 * should be drawn
	 *
	 * @param grapicalObject the graphical object to add to the list
	 * to draw on the screen.
	 */
	public void addGraphicalObject(GraphicalObject graphicalObject) {
		if (graphicalObject != null) {
			addSubObject(graphicalObject);
		}
	}

	/**
	 * Creates an GraphicalGameObject of any GameObject that is sent in to this
	 * function.
	 *
	 * @param gameObject The GameObject to get an GraphicalGameObject from.
	 * @return the GraphicalGameObject of an GameObject.
	 * @exception GraphicalGameObjectExistsException the game object is already
	 * represented by a graphical game object.
	 */
	public GraphicalObject createGraphicalGameObject(GameObject gameObject) throws
			GraphicalGameObjectExistsException {
		GraphicalObject tmpGO = null;

		if (gameObjects.contains(gameObject)) {
			throw new GraphicalGameObjectExistsException();
		}

		// Individual
		if (gameObject instanceof Individual) {
			tmpGO = new GraphicalGameObject(
					graphicsManager.getTurtleGraphics(), gameObject);
		// SourceNode
		} else if (gameObject instanceof SourceNode) {
			int resourceType = ((SourceNode) gameObject).getNaturalResourceType();
			GameObjectGraphics sourceNodeGraphics =
					graphicsManager.getSourceNodeGraphics(resourceType);
			tmpGO = new GraphicalGameObject(sourceNodeGraphics, gameObject);
		// LightPillarNode
		} else if (gameObject instanceof LightPillarNode) {
			tmpGO = new GraphicalLightPillar((LightPillarNode) gameObject);
		// Node
		} else if (gameObject instanceof Node) {
			GameObjectGraphics nodeGraphics =
					graphicsManager.getTransportationNodeGraphics();
			tmpGO = new GraphicalGameObject(nodeGraphics, gameObject);
		}

		// Add small resourcebuttons to all nodes.
		if (gameObject instanceof Node) {
			Button tmpButton;

			// A button with an Integer representing what the button represent.
			tmpButton = new Button(
					graphicsManager.getFoodButtonGraphics(),
					new Integer(World.getEnvironment().FOOD), 4, 20);
			tmpButton.setSelected(false);
			tmpButton.setActive(true);
			tmpGO.addSubObject(FOOD_BUTTON, tmpButton);

			// A button with an Integer representing what the button represent.
			tmpButton =
					new Button(graphicsManager.getHappinessButtonGraphics(),
					new Integer(World.getEnvironment().HAPPINESS), 22, 20);
			tmpButton.setSelected(false);
			tmpButton.setActive(true);
			tmpGO.addSubObject(HAPPINESS_BUTTON, tmpButton);

			// A button with an Integer representing what the button represent.
			tmpButton =
					new Button(graphicsManager.getSacrificeButtonGraphics(),
					new Integer(World.getEnvironment().SACRIFICE), 39, 20);
			tmpButton.setSelected(false);
			tmpButton.setActive(true);

			tmpGO.addSubObject(SACRIFICE_BUTTON, tmpButton);
		}

		if (!(tmpGO == null)) {
			tmpGO.setShowSubObjects(false);
			// to keep track on which objects that are drawn on screen.
			gameObjects.add(gameObject);
			// to draw the GraphicalObject on screen with super.draw(graphics);
			addSubObject(tmpGO);
		}

		return tmpGO;
	}

	/**
	 * Retrieves the graphical game object that represents a game object.
	 *
	 * @param gameObject the game object to get the graphical game object for.
	 * @return the graphical game object that represents the argument or null
	 * if no representation is found.
	 */
	public GraphicalGameObject getGraphicalGameObject(GameObject gameObject) {
		if (gameObjects.contains(gameObject)) {
			return (GraphicalGameObject) getSubObject(gameObject);
		}
		return null;
	}

	/**
	 * Retrieves the graphics manager for this view.
	 *
	 * @return the grahpics manager for this view.
	 */
	public GraphicsManager getGraphicsManager() {
		return graphicsManager;
	}
}
