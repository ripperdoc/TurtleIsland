/*
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-04-10 EliasAE
 * 	Renamed this file from GrapichalIcon.java to GraphicalIcon.java and
 * 	renamed the class too.
 */

package graphicsengine;

import java.awt.Graphics2D;
import java.util.*;

/**
 * A class to show an small image on screen with a relative position to its
 * owner.
 */
public class Icon extends GraphicalObject {

	/**
	 * Creates a graphical icon.
	 */
	public Icon(int relativeX, int relativeY) {
		super (relativeX, relativeY);
	}

	/**
	 * Draws this object on screen with its sprites.
	 *
	 * @param graphics The graphics to draw on.
	 */
	public void draw(Graphics2D graphics) {
		if (isVisible()) {
			super.draw(graphics);
		}
	}

}
