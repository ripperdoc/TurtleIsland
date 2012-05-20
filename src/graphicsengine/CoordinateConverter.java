/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-10 EliasAE
 * 	Change TILE_WIDTH and TILE_HEIGHT to the right values.
 *
 * 2003-04-16 pergamon
 *  Added  float angleMapToScreen(float). That converts from angles in the
 *  Map-coordinate system to the screen coordinate system.
 *
 * 2003-04-15 pergamon
 *  Changed all iso-coordinates from int to float. Changed all
 *  mapToIsoX(new Point(view.getMapX(), view.getMapY()))
 *  to
 *  view.getIsoX()
 *
 * 2003-04-15 pergamon
 *	Chagned:
 *  public int screenToIsoX(int screenX, int screenY) {
 *		return screenX - view.getMapX();
 *	}
 *  to:
 *  public int screenToIsoX(int screenX, int screenY) {
 *		return screenX + mapToIsoX(new Point(view.getMapX(), view.getMapY()));
 *  }
 *
 *  Also changed
 *	public int mapToScreenX(Point mapCoordinate) {
 *	 return mapToIsoX(mapCoordinate) - view.getMapX();
 *  }
 *  to:
 * public int mapToScreenX(Point mapCoordinate) {
 *	 return mapToIsoX(new Point(
 *			 mapCoordinate.getX() - view.getMapX(),
 *			 mapCoordinate.getY() - view.getMapY())
 *			 );
 * }
 *
 * 2003-04-14 EliasAE
 * 	Rewrote the class from scratch.
 *
 * 2003-04-14 pergamon
 *  Fixed screenToMap .. they did "heltalsdivition" before..
 *
 * 2003-04-13 EliasAE
 * 	Changed the comment for coordinate system. The unit of measurement
 * 	for map coordinates is now one tile instead of one pixel.
 *
 * 2003-04-13 pergamon
 * 	Formatted syntax. Wrote the constructor and mapToScreen
 */
package graphicsengine;

import gameengine.Point;

/**
 * Singelton class that is used to convert between different coordiante
 * systems. Before it can be used one must call setView with reference to
 * the view that will be used to convert between the coordinate systems
 * involving the view. The view position is used to for exemaple convert
 * between iso-coordinates and screen coordinates.
 *
 * Overview of the differrent coordinate systems:
 *
 * <dl>
 * <dt>map coordinates</dt><dd>coordinates used in the game world and has
 * little to do with the graphical representation. This is the coordinates
 * that the game engine uses.The bounds of the map is axis-aligned in this
 * coordinate system and the upper left corner of the upper left tile is
 * the origin. The units of measurement do not have to be anything special,
 * but happens to be one unit = one tile.</dd>
 *
 * <dt>iso coordinates</dt><dd>This is the same as screen coordinates
 * but relative the map instead of the screen's upper left corner.</dd>
 *
 * <dt>screen coordinates</dt><dd>same as iso coordinates except that the
 * origin is in the upper left corner of the screen. Note that these
 * coordinates change possible as often as each cycle, because movement of
 * the screen will change these coordinates. That is, do not save these
 * coordinates, just use them to draw things on the screen.</dd>
 * </dl>
 */

public class CoordinateConverter {

	/**
	 * The one and only instance of this class. It is created by
	 * getCoordinateConverter and retrieved by other classes by using
	 * getCoordinateConveter. It is part of the singelton pattern.
	 */
	private static CoordinateConverter theCoordinateConverter;

	/**
	 * The amount that the iso map is tiled. If a turtle moves upwards
	 * or downwards it will not move as many pixels per second as
	 * when he moves left or right. That is because he is moving
	 * in depth. This value is the same as the ratio of the tile's
	 * screen-coordinate-height compared to its screen-coordinate-width.
	 */
	private static float TILE_HEIGHT_WIDTH_RATIO = 0.5f;

	/**
	 * The width of a tile if one would look at it from straight above.
	 */
	public static float TILE_WIDTH = (float) (64 / Math.sqrt(2));

	/**
	 * The height of a tile if one would look at it from straight above.
	 */
	public static float TILE_HEIGHT = (float) (64 / Math.sqrt(2));
	/**
	 * The angle that the map is rotated to produce iso coordinates.
	 */
	private static float MAP_ROTATE_ANGLE = (float) (Math.PI / 4.0);

	/**
	 * The full circle angle 2 PI.
	 */
	private static float TWO_PI = (float) (2 * Math.PI);

	/**
	 * The view, from which the position is retrieved to calculate
	 * the screen coordiantes, which is relative the position of the
	 * view.
	 */
	private View view;

	/**
	 * Converts an angle in Map to an angle in Screen.
	 *
	 * @param angle The map-angle to convert.
	 * @return an on-screen angle.
	 */
	public float angleMapToScreen(float angle) {
		angle += MAP_ROTATE_ANGLE;
		if (angle > TWO_PI) {
			angle -= TWO_PI;
		}
		return angle;
	}

	/**
	 * Converts from map coordinate to screen coordinate on the x-axis.
	 * @param mapCoordinate the map coordinate to convert.
	 * @return the screen coordinate corespondending to the parameter.
	 */
	public int mapToScreenX(Point mapCoordinate) {
		return Math.round(
				mapToIsoX(new Point(mapCoordinate.getX(),mapCoordinate.getY()))
				- view.getIsoX()
				);
	}

	/**
	 * Converts from map coordinate to screen coordinate on the y-axis.
	 * @param mapCoordinate the map coordinate to convert.
	 * @return the screen coordinate corespondending to the parameter.
	 */
	public int mapToScreenY(Point mapCoordinate) {
		return Math.round(
				mapToIsoY(new Point(mapCoordinate.getX(),	mapCoordinate.getY()))
				- view.getIsoY());
	}

	/**
	 * Converts from screen coordinate to map coordinate on the x-axis.
	 * @param screenX the screen x-coordinate to convert.
	 * @param screenY the screen y-coordinate to convert.
	 * @return the map x-coordinate corespondending to the parameter.
	 */
	public float screenToMapX(int screenX, int screenY) {
		Point tmpPoint = screenToMap(screenX, screenY);
		return tmpPoint.getX();
	}

	/**
	 * Converts from screen coordinate to map coordinate on the y-axis.
	 * @param screenX the screen x-coordinate to convert.
	 * @param screenY the screen y-coordinate to convert.
	 * @return the map y-coordinate corespondending to the parameter.
	 */
	public float screenToMapY(int screenX, int screenY) {
		Point tmpPoint = screenToMap(screenX, screenY);
		return tmpPoint.getY();
	}

	/**
	 * Converts from screen coordinate to map coordinate.
	 * @param screenX the screen x-coordinate to convert.
	 * @param screenY the screen y-coordinate to convert.
	 * @return the map coordinate corespondending to the parameter.
	 */
	public Point screenToMap(int screenX, int screenY) {
		return isoToMap(
				screenX + view.getIsoX(),
				screenY + view.getIsoY()
				);
	}

	/**
	 * Converts from iso coordinate to screen coordinate.
	 * @param isoX the iso x-coordinate to convert.
	 * @param isoY the iso y-coordinate to convert.
	 * @return the screen x-coordinate corespondending to the parameter.
	 */
	public int isoToScreenX(float isoX, float isoY) {
		return Math.round(isoX - view.getIsoX());
	}

	/**
	 * Converts from iso coordinate to screen coordinate.
	 * @param isoX the iso x-coordinate to convert.
	 * @param isoY the iso y-coordinate to convert.
	 * @return the screen y-coordinate corespondending to the parameter.
	 */
	public int isoToScreenY(float isoX, float isoY) {
		return Math.round(isoY - view.getIsoY());
	}

	/**
	 * Converts from screen coordinate to iso coordinate.
	 * @param screenX the screen x-coordinate to convert.
	 * @param screenY the screen y-coordinate to convert.
	 * @return the iso x-coordinate corespondending to the parameter.
	 */
	public float screenToIsoX(int screenX, int screenY) {
		return screenX + view.getIsoX();
	}

	/**
	 * Converts from screen coordinate to iso coordinate.
	 * @param screenX the screen x-coordinate to convert.
	 * @param screenY the screen y-coordinate to convert.
	 * @return the iso y-coordinate corespondending to the parameter.
	 */
	public float screenToIsoY(int screenX, int screenY) {
		return screenY + view.getIsoY();
	}

	/**
	 * Converts from map coordinate to iso coordinate.
	 * @param mapCoordinate the map coordinate to convert.
	 * @return the iso coordinate corespondending to the parameter.
	 */
	public float mapToIsoX(Point mapCoordinate) {

		// Transform the coordinate to use the center of the map
		// as the origin and pixels as unit.

		final float pixelX =
				(mapCoordinate.getX() - view.getMap().getWidth() / 2.0f)
				* TILE_WIDTH;
		final float pixelY =
				(mapCoordinate.getY() - view.getMap().getHeight() / 2.0f)
				* TILE_HEIGHT;

		// Rotate the coordinate about the origin (which is now at the center
		// of the map).
		float isoX = (float) (
				pixelX * Math.cos(MAP_ROTATE_ANGLE)
				-
				pixelY * Math.sin(MAP_ROTATE_ANGLE)
				);

		// Move back the origin to the upper left corner
		isoX += TILE_WIDTH * view.getMap().getWidth() / 2.0f;

		return isoX;
	}

	/**
	 * Converts from map coordinate to iso coordinate.
	 * @param mapCoordinate the map coordinate to convert.
	 * @return the iso coordinate corespondending to the parameter.
	 */
	public float mapToIsoY(Point mapCoordinate) {
		// Transform the coordinate to use the center of the map
		// as the origin and pixels as unit.
		final float pixelX =
				(mapCoordinate.getX() - view.getMap().getWidth() / 2.0f)
				* TILE_WIDTH;
		final float pixelY =
				(mapCoordinate.getY() - view.getMap().getHeight() / 2.0f)
				* TILE_HEIGHT;

		// Rotate the coordinate about the origin (which is now at the center
		// ot he map).
		float isoY = (float) (
				pixelX * Math.sin(MAP_ROTATE_ANGLE)
				+
				pixelY * Math.cos(MAP_ROTATE_ANGLE)
				);

		// Move back the origin to the upper left corner
		isoY += TILE_HEIGHT * view.getMap().getHeight() / 2.0f;

		// Compensate for that the third dimension. The coordinates
		// does not change as rapidly in the Y-direction. Gives
		// the feeling of depth.
		isoY *= TILE_HEIGHT_WIDTH_RATIO;

		return isoY;
	}

	/**
	 * Converts from iso coordinate to map coordinate.
	 * @param isoX the iso x-coordinate to convert.
	 * @param isoY the iso y-coordinate to convert.
	 * @return the map coordinate corespondending to the parameter.
	 */
	public float isoToMapY(float isoX, float isoY) {
		// Take away feeling of depth. Look straight from above.
		isoY /= TILE_HEIGHT_WIDTH_RATIO;

		// Move the origin to the center of the map and convert to tiles
		// as unit of mesurement.
		final float tileX =
				(isoX / (float) TILE_WIDTH - view.getMap().getWidth() / 2.0f);
		final float tileY =
				(isoY / (float) TILE_HEIGHT - view.getMap().getHeight() / 2.0f);

		// Rotate about the origin which is now at the center of the map.
		float mapY = (float) (
				tileX * Math.sin(-MAP_ROTATE_ANGLE)
				+
				tileY * Math.cos(-MAP_ROTATE_ANGLE)
				);

		// Move the origin back to the upper left corner.
		mapY += view.getMap().getHeight() / 2.0f;

		return mapY;
	}



	/**
	 * Converts from iso coordinate to map coordinate.
	 * @param isoX the iso x-coordinate to convert.
	 * @param isoY the iso y-coordinate to convert.
	 * @return the map coordinate corespondending to the parameter.
	 */
	public float isoToMapX(float isoX, float isoY) {
		// Move the origin to the center of the map and convert to tiles
		// as unit of mesurement.
		final float tileX =
				(isoX / (float) TILE_WIDTH - view.getMap().getWidth() / 2.0f);
		final float tileY =
				(isoY / (float) TILE_HEIGHT - view.getMap().getHeight() / 2.0f);

		// Rotate about the origin which is now at the center of the map.
		float mapX = (float) (
				tileX * Math.cos(-MAP_ROTATE_ANGLE)
				-
				tileY * Math.sin(-MAP_ROTATE_ANGLE)
				);

		// Move the origin back to the upper left corner.
		mapX += view.getMap().getWidth() / 2.0f;

		return mapX;
	}

	/**
	 * Converts from iso coordinate to map coordinate.
	 * @param isoX the iso x-coordinate to convert.
	 * @param isoY the iso y-coordinate to convert.
	 * @return the map coordinate corespondending to the parameter.
	 */
	public Point isoToMap(float isoX, float isoY) {
		// Take away feeling of depth. Look straight from above.
		isoY /= TILE_HEIGHT_WIDTH_RATIO;

		// Move the origin to the center of the map and convert to tiles
		// as unit of mesurement.
		final float tileX =
				(isoX / (float) TILE_WIDTH - view.getMap().getWidth() / 2.0f);
		final float tileY =
				(isoY / (float) TILE_HEIGHT - view.getMap().getHeight() / 2.0f);

		// Rotate about the origin which is now at the center of the map.
		float mapY = (float) (
				tileX * Math.sin(-MAP_ROTATE_ANGLE)
				+
				tileY * Math.cos(-MAP_ROTATE_ANGLE)
				);

		float mapX = (float) (
				tileX * Math.cos(-MAP_ROTATE_ANGLE)
				-
				tileY * Math.sin(-MAP_ROTATE_ANGLE)
				);

		// Move the origin back to the upper left corner.
		mapX += view.getMap().getWidth() / 2.0f;
		mapY += view.getMap().getHeight() / 2.0f;

		return new Point(mapX, mapY);
	}

	/**
	 * Retrieves the only instance of the coordinate converter.
	 *
	 * @return the coordinate converter that is the only one that can exists.
	 */
	public static CoordinateConverter getCoordinateConverter() {
		if (theCoordinateConverter == null) {
			theCoordinateConverter = new CoordinateConverter();
		}
		return theCoordinateConverter;
	}

	/**
	 * Creates a cooridnate converter. This is private to prevent creation of
	 * more instances than the one and only that is created by
	 * getCooridnateConverter.
	 */
	private CoordinateConverter() {

	}

	/**
	 * Sets the view for the coordinate converter. This view is used to
	 * calculate convertions to and from screen coordinates, which is dependent
	 * upon the position of the view on the map. The coordinate converter will
	 * use this view for conversion until a new i specified.
	 *
	 * @param view the view that will be used for cooridnate conversion.
	 */
	public void setView(View view) {
		if (view == null)	{
			throw new NullPointerException("view was null in CoordinateConveter.setView()");
		}
		this.view = view;
	}

}
