/*
 * 2003-05-11 EliasAE
 * 	Wrote the class.
 */

package gameengine.gameobjects;

/**
 * Exception thrown when a method is called on a deleted object.
 */
public class ObjectDeletedException extends IllegalStateException {
	/**
	 * Constructs the exception.
	 */
	public ObjectDeletedException(Deletable thrower) {
		super("The object is deleted and cannot be manipulated." + thrower);
	}
}
