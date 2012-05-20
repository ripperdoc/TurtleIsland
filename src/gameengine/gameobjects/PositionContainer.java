/*
 * 2003-05-10 henko
 * Created.
 */

package gameengine.gameobjects;

import gameengine.*;
import gameengine.*;

/**
 * PositionContainer represents an object that has a position. It contains
 * functions retrieving the current position as a point.
 */
public interface PositionContainer {

	/**
	 * Returns a Point with the position of the GameObject.
	 *
	 * @return a Point with the position of the GameObject.
	 */
	public Point getPoint();

}