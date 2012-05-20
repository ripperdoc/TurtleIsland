/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-19 Martin
 *	Corrected the use of a MapImage instead of MapInterpreter.
 *
 * 2003-05-12 Martin
 *	Implemented the use of graphical maps, and added a new tile type.
 *
 * 2003-05-10 pergamon
 *  Started to implement the class with a test "tileMap".
 *
 * 2003-04-14 EliasAE
 * 	Added getWidth and getHeight.
 *
 * 2003-04-11 pergamon
 *  Formatted syntax. Added width and height.
 */
package graphicsengine;

import gameengine.World;
import gameengine.Environment;
import graphicsengine.graphics.GraphicsManager;
import graphicsengine.Tile;
import graphicsengine.MapImage;
import java.util.*;
import java.awt.Graphics2D;
import java.awt.Color;


/**
 * The ground and other stationary things which are the environment that
 * the game objects exist in.
 */
public class Map {

	/**
	 * A graphicsmanager to get graphics from.
	 */
	private GraphicsManager graphicsManager;

	/**
	 * The width of the whole map.
	 */
	private int width;

	/**
	 * The height of the whole map.
	 */
	private int height;

	/**
	 * The tiles that should be drawn. A tile at position x,y is in this
	 * array with index tiles[y][x]
	 */
	public Tile[][] tiles;

	// TODO should these be somewhere else, in other names?
	public static int BRICK_STONE_NR = 1;
	public static int GRASS_NR = 2;
	public static int SAND_NR = 3;
	public static int WATER_NR = 4;

	private static String[] TILE_STRINGS;

	private MapImage mapImage;

	/**
	 * Constructs a standard Map.
	 *
	 * @param graphicsManager a graphicsManager to get graphics from.
	 * @param mapImage the MapImage to read the map data from..
	 */
	public Map(GraphicsManager graphicsManager, MapImage mapImage) {
		// Creates an array of strings corresponding to the tile numbers.
		// The array is one element longer than the amounts of strings,
		// because the tile number 0 has no string.
		TILE_STRINGS = new String[5];
		TILE_STRINGS[BRICK_STONE_NR] = "stone";
		TILE_STRINGS[SAND_NR] = "sand";
		TILE_STRINGS[GRASS_NR] = "grass";
		TILE_STRINGS[WATER_NR] = "water";

		this.mapImage = mapImage;
		this.width = mapImage.getWidth();
		this.height = mapImage.getHeight();
		this.graphicsManager = graphicsManager;
		this.tiles = new Tile[height][width];
	}

	/**
	 * Returns the map's MapImage.
	 *
	 * @return the map's MapImage.
	 */
	 public MapImage getMapImage() {
		 return mapImage;
	 }

	/**
	 * Load all tiles.
	 */
	void loadTiles() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Tile tile;
				int tileNr = mapImage.getTileNr(x,y);
				if (tileNr > 0) {
					// Create a tile with the string specified from
					// the current tileNr.
					tile = new Tile(graphicsManager.getTileGraphics(
								TILE_STRINGS[tileNr]), x, y);
				} else {
					// create an empty tile.
					tile = new Tile(x, y);
				}
				tiles[y][x] = tile;
			}
		}
	}

	/**
	 * Creates a new iterator over all the tiles that should be drawn on the
	 * screen.
	 *
	 * @param viewPositionX the x-position of the view in screen-coordinates.
	 * @param viewPositionY the y-position of the view in screen-coordinates.
	 * @param viewWidth the width of the view in pixels. Cannot be less than
	 * zero.
	 * @param viewHeight the height of the view in pixels. Cannot be less than
	 * zero.
	 * @return an iterator over all the tiles that should be drawn.
	 */
	public Iterator getTileIterator(int viewPositionX, int viewPositionY,
			int viewWidth, int viewHeight) {
		return new TileIterator((int) viewPositionX, (int) viewPositionY,
				viewWidth, viewHeight);
	}

	/**
	 * Makes the tiles at a position dirty, that is they will be returned from
	 * the iterators retrieved from getTileIterator.
	 *
	 * @param screenX the x-position of the tile to make dirty in screen
	 * coordinates.
	 * @param screenY the y-position of the tile to make dirty in screen
	 * coordinates.
	 */
	public void addDirtyTile(int screenX, int screenY) {

	}

	/**
	 * Mark all tiles for redrawing. That is the iterators returned from
	 * getTileIterator will return all tiles within the view.
	 */
	public void makeAllTilesDirty() {

	}

	/**
	 * Retrieves the with of the map.
	 * @return the number of tiles that is along the x-axis for
	 * map-coordinates. That is the width of the map in map coordinates.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Retrieves the height of the map.
	 * @return the number of tiles that is along the y-axis for
	 * map-coordinates. That is the width of the map in map coordinates.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Iterator over all tiles that must be redrawn. If the whole map must be
	 * redrawn this iterates over all tiles that will be displayed on the
	 * screen. If only a part of the map has to be drawn, this iterator
	 * iterates over all tiles nevertheless. This iteration will not return
	 * tiles outside the view.
	 */
	private class TileIterator implements Iterator {

		/**
		 * The current x-position in the 2-dimensional iteration.
		 */
		private int x;

		/**
		 * The current y-position in the 2-dimensional iteration.
		 */
		private int y;

		/**
		 * The current position of the view on the screen in
		 * screen coordinates.
		 */
		private int viewScreenX;

		/**
		 * The current position of the view on the screen in
		 * screen coordinates.
		 */
		private int viewScreenY;

		/**
		 * The width of view in pixels.
		 */
		private int viewWidth;

		/**
		 * The height of the view in pixels.
		 */
		private int viewHeight;

		/**
		 * True if the iteration is finished.
		 */
		private boolean finished = false;

		/**
		 * Creates a tile iterator.
		 *
		 * @param viewScreenX the position of the view on the
		 * screen in screen coordinates.
		 * @param viewScreenY the postion of the view on the
		 * screen in screen coordinates.
		 * @param viewWidth the width of the view in screen coordinates.
		 * @param viewHeight the height of the view in screen coordinates.
		 */
		public TileIterator(int viewScreenX, int viewScreenY,
				int viewWidth, int viewHeight) {
			x = 0;
			y = 0;
			finished = false;
			this.viewScreenX = viewScreenX;
			this.viewScreenX = viewScreenX;
			this.viewWidth = viewWidth;
			this.viewHeight = viewHeight;
		}

		/**
		 * Checks if there is any elements left in the iteration.
		 *
		 * @return true if there is more element, that is next will not 
		 * throw NoSuchElementException, otherwise false.
		 */
		public boolean hasNext() {
			return !finished;
		}

		/**
		 * Retrieves the next element in the iteration.
		 *
		 * @return the next element.
		 */
		public Object next() {
			if (finished) {
				throw new NoSuchElementException();
			}
			Object nextTile = tiles[y][x];
			do {
				x++;
				if (x >= tiles[0].length) {
					x = 0;
					y++;
					if (y >= tiles.length) {
						finished = true;
						return nextTile;
					}
				}
			} while (!tiles[y][x].isInArea(viewScreenX, viewScreenY, viewWidth, viewHeight));
			return nextTile;
		}

		/**
		 * This operation is not supported.
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
