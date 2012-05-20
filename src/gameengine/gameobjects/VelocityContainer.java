/*
 * 2003-05-10 henko
 * Created.
 */

package gameengine.gameobjects;

import gameengine.*;
import gameengine.*;

/**
 * VelocityContainer represents an object that has a position. It contains 
 * functions retrieving the current position as a point.
 */
public interface VelocityContainer {

	/**
	 * Returns a Vector with the velocity of the object.
	 *
	 * @return a Vector with the velocity of the object.
	 */
	public Vector getVector();
	
}