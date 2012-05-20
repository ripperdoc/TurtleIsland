
/*
 * 2003-05-11 EliasAE
 * 	Added support for deletable.
 *
 * 2003-04-10 EliasAE
 * 	Changes the filename from gen10499582473901.txt to Bird.java
 */

package gameengine.gameobjects;

import java.util.*;

/**
 * A stupid class with no use at all.
 */
public class Bird extends GameObject implements Updateable {

	/**
	 * Creates a Bird.
	 */
	public Bird() {
		// TODO just to make it compile.
		super (null);
		setDescription("This is a bird that has no real meaning.");
	}

	/**
	 * Updates the bird.
	 */
	public void update() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}

	}

	/**
	 * Makes a wish to the bird to go kill itself.
	 */
	public void goKillYourselfCuzYouArentWanted() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		delete();
	}

}
