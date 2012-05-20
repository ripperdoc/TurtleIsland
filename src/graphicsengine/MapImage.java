/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-13 Martin
 *	Added a method for returning an array with information about a tile's
 *	neighbouring tiles.
 *
 * 2003-05-12 Martin
 *	Created the class.
 */
package graphicsengine;

import java.awt.image.PixelGrabber;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.Serializable;

/**
 * The MapImage reads a map from a given image file.
 * The class can then return information about tiles at specific
 * positions and information about which tiles that neighbours a tile.
 */
public class MapImage implements Serializable {

	public static final int RED = 0;
	public static final int GREEN = Map.GRASS_NR;
	public static final int BLUE = Map.WATER_NR;
	public static final int YELLOW = Map.SAND_NR;
	public static final int TURQUOISE = 0;
	public static final int PURPLE = 0;
	public static final int BLACK = 0;
	public static final int WHITE = Map.BRICK_STONE_NR;
	public static final int GRAY = Map.BRICK_STONE_NR;

	/**
	 * The two-dimensional array representing the tiles of the map.
	 */
	private int[][] tileMap;

	/**
	 * The width in pixels/tiles of the map.
	 */
	private int width;

	/**
	 * The height in pixels/tiles of the map.
	 */
	private int height;

	/**
	 * Constructs a MapImage for the given image.
	 *
	 * @param image an image.
	 */
	public MapImage(Image image) {
		width = image.getWidth(null);
		height = image.getHeight(null);

		tileMap = new int[width][height];

		// Constructs a pixel array for storing the pixels
		int[] pixels = new int[width * height];
		// Constructs a PixelGrabber for the specified image
		PixelGrabber pixelGrabber =
				new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
		// Try to grab all the pixels and stuff them into pixels[].
		try {
			pixelGrabber.grabPixels();
		} catch (InterruptedException e) {
			Debug.log("Interrupted waiting for pixels!");
			return;
		}
		if ((pixelGrabber.getStatus() & ImageObserver.ABORT) != 0) {
			Debug.log("Image fetch aborted or errored");
			return;
		}
		for (int y = 0; y < height; y++) {
		    for (int x = 0; x < width; x++) {
				tileMap[x][y] = handlePixel(pixels[y * width + x]);
		    }
		}
	}

	/**
	 * Returns the tile number at the specified coordinate.
	 *
	 * @return the tile number at the specified coordinate.
	 */
	public int getTileNr(int x, int y) {
		return tileMap[x][y];
	}

	/**
	 * Returns the width of the map.
	 *
	 * @return the width of the map.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the map.
	 *
	 * @return the height of the map.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns an integer array that describes the "neighbours" of the
	 * tile with the given coordinates. If the neighbours tile graphics
	 * has a higher priority this will be saved in the array, otherwise
	 * the array will pretend that there are no neighbour worth accounting
	 * for.
	 *
	 * @param x an integer describing the X-coordinate of the tile in the map.
	 * @param y an integer describing the Y-coordinate of the tile in the map.
	 * @return an integer of length 4 that describes the neighbouring tiles
	 * on all sides.
	 */
	public int[] getTransitionInfo(int x, int y) {
		int thisTile = tileMap[x][y];
		int[] transitions = new int[4];
		// Set all neighbours to zero, i.e. no tile gfx
		int leftTile = 0;
		int rightTile = 0;
		int upperTile = 0;
		int lowerTile = 0;

		// Correct by checking if a neighbour exist and if so
		// assign its value to the respective tile.
		if(x > 0) {
			leftTile = tileMap[x-1][y];
		}
		if(x < width - 1) {
			rightTile = tileMap[x+1][y];
		}
		if(y > 0) {
			upperTile = tileMap[x][y-1];
		}
		if(y < height - 1) {
			lowerTile = tileMap[x][y+1];
		}

		// Set all neighbours to zero
		for(int i = 0;i < transitions.length; i++) {
			transitions[i] = 0;
		}

		// If the neighbour tile has a higher priority (lower
		// ID-value) assign that ID as a neighbour. If not
		// the zero from above will be left, making the
		if(leftTile < thisTile) {
			transitions[0] = leftTile;
		}
		if(lowerTile < thisTile) {
			transitions[1] = leftTile;
		}
		if(rightTile < thisTile) {
			transitions[2] = leftTile;
		}
		if(upperTile < thisTile) {
			transitions[3] = leftTile;
		}
		return transitions;

	/*	HOW TO USE THE TRANSITION IDENTIFIER SYSTEM, OR, "HOW TO KNOW
	 *  YOUR NEIGHBOURS"
	 *
	 *   1   2   3   4   5   6   7   8   9  10  11  12  13  14  15  16
	 * -___-___-___-___-___-___-___-___-___-___-___-___-___-___-___-___
	 *      *   *** ***   * * * *** ***     *   *** ***   * * * *** ***
	 *      *       *     * * *   * * *     *       *     * * *   * * *
	 *      *       *     * * *   * * * *** *** *** *** *** *** *** ***
	 * _---_---_---_---_---_---_---_---_---_---_---_---_---_---_---_---
	 *       x   0   x   0   x   0   x   0   x   0   x   0   x   0   x
	 *		 0   0   0   0   0   0   0   x   x   x   x   x   x   x   x
	 *       0   0   0   x   x   x   x   0   0   0   0   x   x   x   x
	 *       0   x   x   0   0   x   x   0   0   x   x   0   0   x   x
	 * ----------------------------------------------------------------
	 * This means that when a tile has a "neighbour" x at the illustrated
	 * sides, the transition code is an array as following, where the upmost
	 * number is the first in the array. Of course, x can be different on
	 * different sides.
	 *
	 * Example:
	 * A tile has a stone tile over it, a grass tile to the right and
	 * a water below it. This would correspond to figure nr 15. The transition
	 * array for this would be {0, 4, 2, 1} where 4 represents water, 2 grass
	 * and 1 stone, just as the constants (of course).
	 */

	}

	/**
	 * Checks the given bit-coded RGB pixel and identifies which color it is
	 * according to several predefined colors. If a color matches, it returns
	 * the tile number associated with that color. If there is no match, it
	 * returns a zero meaning that no tile exists.
	 *
	 * @param pixel an int representing an 32-bit ARGB-colored pixel.
	 * @return an int representing the number of the specific tile
	 */
	private int handlePixel(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel ) & 0xff;

		// Red 255,0, 0
		if (red == 255 && green == 0 && blue == 0) {
			return RED;
		}
		// Green 0, 255, 0
		else if(red == 0 && green == 255 && blue == 0) {
			return GREEN;
		}
		// Blue 0,0,255
		else if(red == 0 && green == 0 && blue == 255) {
			return BLUE;
		}
		// Yellow 255,255,0
		else if(red == 255 && green == 255 && blue == 0) {
			return YELLOW;
		}
		// Turquoise 0,255,255
		else if(red == 0 && green == 255 && blue == 255) {
			return TURQUOISE;
		}
		// Purple 255,0,255
		else if(red == 255 && green == 0 && blue == 255) {
			return PURPLE;
		}
		// Black 0,0,0
		else if(red == 0 && green == 0 && blue == 0) {
			return BLACK;
		}
		// White 255,255,255
		else if(red == 255 && green == 255 && blue == 255) {
			return WHITE;
		}
		// Gray 153,153,153
		else if(red == 153 && green == 153 && blue == 153) {
			return GRAY;
		}
		else {
			return 0;
		}
	}

}
