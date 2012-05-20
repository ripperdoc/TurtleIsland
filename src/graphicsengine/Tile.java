/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-18 EliasAE
 * 	Changed drawing to take into account the position of the
 * 	view over tha map.
 *
 * 2003-05-10 pergamon
 *  changed a little bit of this and that.
 *
 * 2003-04-14 EliasAE
 * 	Changes to use the new interface for CoordinateConverter.
 *
 * 2003-04-13 EliasAE
 * 	Changed to using the Rendeable interface.
 *
 * 2003-04-13 pergamon
 *  Added static width and height..  maybe shouldn't be here?
 *
 * 2003-04-11 pergamon
 *  Made the constructor, think this is all that is needed for this class...
 */
package graphicsengine;

import java.awt.Graphics2D;
import java.util.List;

import gameengine.Point;
import graphicsengine.graphics.TileGraphics;

/**
 * A tile on the isometric map. It is a square placed on its corner and
 * together with other tiles it creates a map.
 */
public class Tile extends GraphicalObject {

	/**
	 * The width of a tile in screen coordinates when painted on the screen.
	 */
	private static int width = 64;

	/**
	 * The height of a tile in screen coordinates when painted on the screen.
	 */
	private static int height = 32;

	/**
	 * Represents the tile's position in isometric coordinates.
	 */
	private float isoX;

	/**
	 * Represents the tile's position in isometric coordinates.
	 */
	private float isoY;

	/**
	 * The renderable used to paint the tile.
	 */
	private Renderable tileRenderable;

	/**
	 * Creates a blank tile.
	 *
	 * @param mapPositionX The position on the map for the tile in map
	 * coordinates.
	 * @param mapPositionY The position on the map for the tile in map
	 * coordinates.
	 */
	public Tile(int mapPositionX, int mapPositionY) {
		this(null, mapPositionX, mapPositionY);
	}

	/**
	 * Creates a tile with a specified graphics.
	 *
	 * @param tileGraphics the Renderable that the tile will use to draw itself.
	 * @param mapPositionX The position on the map for the tile in map
	 * coordinates.
	 * @param mapPositionY The position on the map for the tile in map
	 * coordinates.
	 */
	public Tile(TileGraphics tileGraphics, int mapPositionX, int mapPositionY) {
		super(
				CoordinateConverter.getCoordinateConverter().mapToScreenX(
					 new Point(mapPositionX, mapPositionY)),
				CoordinateConverter.getCoordinateConverter().mapToScreenY(
					 new Point(mapPositionX, mapPositionY))
				);
		if (tileGraphics != null) {
			// TODO should this always be random variation.,.  no!  (?)
			tileRenderable = tileGraphics.getRandomVariation();
		}

		Point mapPosition = new Point(mapPositionX, mapPositionY);
		isoX = CoordinateConverter.getCoordinateConverter().mapToIsoX(mapPosition);
		isoY = CoordinateConverter.getCoordinateConverter().mapToIsoY(mapPosition);
	}

	/**
	 * Draw the tile to a graphics object.
	 *
	 * @param graphics the graphics to draw on.
	 */
	public void draw(Graphics2D graphics) {
		if (tileRenderable != null && this.isVisible()) {
			tileRenderable.render(graphics, getScreenX(), getScreenY());
		}
		super.draw(graphics);
	}

	/**
	 * To get the width of tile.
	 *
	 * @return the width of a tile.
	 */
	public static int getWidth() {
		return width;
	}

	/**
	 * To get the height of tile.
	 *
	 * @return the height of a tile.
	 */
	public static int getHeight() {
		return height;
	}

	/**
	 * To check if the GraphicalObject is inside the given area.
	 *
	 * @param x the x-coordinate of the area in screen coordinates.
	 * @param y the y-coordinate of the area in screen coordinates.
	 * @param width the width of the area in screen coordinates.
	 * @param height the height of the area in screen coordinates.
	 * @return true if the tile is within, false if not.
	 */
	public boolean isInArea(int x, int y, int width, int height) {
		int screenX = getScreenX();
		int screenY = getScreenY();
		return (
				screenX + getWidth() > x &&
				screenY + getHeight() > y &&
				screenX < x + width &&
				screenY < y + height
				);
	}

	/**
	 * Retrieves the x-position of this object on the screen.
	 *
	 * @return the x-position of this object's upper left corner relative to
	 * the upper left corner of the screen.
	 */
	public int getScreenX() {
		CoordinateConverter cc =
				CoordinateConverter.getCoordinateConverter();
		int relativeX = cc.isoToScreenX(isoX, isoY);
		setRelativeX(relativeX);
		return super.getScreenX();
	}

	/**
	 * Retrieves the y-position of this object on the screen.
	 *
	 * @return the y-position of this object's upper left corner relative to
	 * the upper left corner of the screen.
	 */
	public int getScreenY() {
		CoordinateConverter cc =
				CoordinateConverter.getCoordinateConverter();
		int relativeY = cc.isoToScreenY(isoX, isoY);
		setRelativeY(relativeY);
		return super.getScreenY();
	}

	/**
	 * Retrieves the relative x-position of this object.
	 *
	 * @return the x-position of this object relative to the upper left corner
	 * of its owner in pixels.
	 */
	public int getRelativeX() {
		CoordinateConverter cc =
				CoordinateConverter.getCoordinateConverter();
		int relativeX = cc.isoToScreenX(isoX, isoY);
		setRelativeX(relativeX);
		return super.getRelativeX();
	}

	/**
	 * Retrieves the relative y-position of this object.
	 *
	 * @return the y-position of this object relative to the upper left corner
	 * of its owner in pixels.
	 */
	public int getRelativeY() {
		CoordinateConverter cc =
				CoordinateConverter.getCoordinateConverter();
		int relativeY = cc.isoToScreenY(isoX, isoY);
		setRelativeY(relativeY);
		return super.getRelativeY();
	}

}
