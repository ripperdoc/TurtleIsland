
/* 2003-04-11 Martin
 *  Formatted and completed.
 */
package gameengine.gameobjects;

import java.util.*;

/**
 * An interface that defines object that is updated directly by the game
 * each cycle. The objects all have an update() method.
 */
public interface Updateable {

	/**
	 * The method updates, i.e. recalculates and performs tasks, the
	 * objects implementing the Updateable interface.
	 */
	public void update();

}

