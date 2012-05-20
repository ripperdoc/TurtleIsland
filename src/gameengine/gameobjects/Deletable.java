/*
 * 2003-05-11 EliasAE
 * 	Created the class.
 */

package gameengine.gameobjects;

/**
 * Interface to implement if the object is deleteable.
 */
interface Deletable {

	/**
	 * Marks an object for deletion.
	 */
	public void delete();

	/**
	 * Checks if an object is deleted.
	 */
	public boolean isDeleted();
}
