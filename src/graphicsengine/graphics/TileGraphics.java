/*
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-09 EliasAE
 * 	Added support for variations of tiles.
 *
 * 2003-04-18 EliasAE
 * 	Wrote the class, at least as much of it that is needed now.
 */
package graphicsengine.graphics;

import graphicsengine.Renderable;
import java.util.*;

/**
 * Graphics for a tile. Created by the GraphicsManager. This is the variation
 * of one kind of tiles. For example, this object may contain all
 * variations of stone tiles, like stone1, stone2, etc.
 */
public class TileGraphics {

	/**
	 * The normal graphics for the tile. This graphcis will
	 * always be used to paint the tile.
	 */
	private java.util.Map variationsMap;

	/**
	 * The collection of all the values in the variationsMap.
	 */
	private Object[] variationsArray;

	/**
	 * Creates a graphics package for a tile.
	 *
	 * @param normal the normal graphics for the tile. This graphcis will
	 * always be used to paint the tile.
	 */
	public TileGraphics(Map variations) {
		variationsMap = variations;
		variationsArray = variationsMap.values().toArray();
	}

	/**
	 * Returns a random variation of the kind of tile that this
	 * TileGraphcics represents.
	 *
	 * @return a random variation of the graphics.
	 */
	public Renderable getRandomVariation() {
		int index = (int) (Math.random() * variationsArray.length);
		if (index < variationsArray.length) {
			return (Renderable) variationsArray[index];
		} else {
			return null;
		}
	}

}
