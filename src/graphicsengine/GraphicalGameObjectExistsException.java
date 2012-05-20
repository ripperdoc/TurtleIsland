/*
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-18 EliasAE
 * 	Created the class instead of GraphicalGameObjectExcistError
 */

package graphicsengine;

/**
 * Exception thrown when the view try to create a graphical game
 * object for an game object, when a graphical game object
 * for that game object already exists.
 */
public class GraphicalGameObjectExistsException extends Exception {

	/**
	 * Creates the excepiton.
	 */
	public GraphicalGameObjectExistsException() {
	}

}
