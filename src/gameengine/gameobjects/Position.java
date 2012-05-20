/* 
 * 2003-05-12 EliasAE
 * 	Removed support for deletable.
 *
 * 2003-05-11 EliasAE
 * 	Added support for deletable.
 *
 * 2003-04-15 Martin
 *
 * 2003-04-10 Martin
 * 	Completed class from generation by adding "this." Formatting still
 * 	to be done.
 *
 * 2003-04-10 EliasAE
 * 	Formatted the code.
 */
package gameengine.gameobjects;

import gameengine.*;

import java.util.*;
import java.io.*;

/**
 * An attribute describing an objects position, for example it's X and Y
 * coordinate (described by a Point). Has methods that can set or get the
 * Point.
 */
public class Position implements Serializable {

	/**
	 * The Point that stores the position.
	 */
	private Point point;

	/**
	 * Returns the Position's Point.
	 *
	 * @return the Position's Point.
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * Sets the Position's Point to the given Point.
	 *
	 * @param point the new Point.
	 */
	public void setPoint(Point point) {
		this.point = point;
	}

}
