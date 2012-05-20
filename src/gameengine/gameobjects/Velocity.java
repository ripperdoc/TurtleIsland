/* 
 * 2003-05-12 EliasAE
 * 	Removed support for deletable.
 *
 * 2003-05-11 EliasAE
 * 	Added support for deletable.
 *
 * 2003-04-15 Martin
 * Implemented serializable.
 *
 * 2003-04-11 pergamon
 * 	added a constructor.
 * 2003-04-10 Martin
 *  Completed the class by adding "this." There are still formatting to
 *  be done.
 *
 * 2003-04-10 EliasAE
 * 	Formatted the code.
 */
package gameengine.gameobjects;

import gameengine.Vector;

import java.io.Serializable;

/**
 * An attribute describing an objects velocity, for example it's speed
 * and angle (described by a Vector). Has methods for setting and
 * getting the Vector of the object.
 */
public class Velocity implements Serializable {

	/**
	 * The Vector that stores the velocity.
	 */
	private Vector vector;

	/**
	 * Creates a Velocity class with no direction or magnitude.
	 *
	 * @param direction the direction for this Velocity.
	 * @param magnitude the magnitude of this Velocity.
	 */
	public Velocity() {
		this(0f,0f);
	}

	/**
	 * Creates a Velocity class with a direction and magnitude.
	 *
	 * @param direction the direction for this Velocity.
	 * @param magnitude the magnitude of this Velocity.
	 */
	public Velocity(float direction, float magnitude) {
		vector = new Vector(direction, magnitude);
	}

	/**
	 * Returns the Velocity's Vector.
	 *
	 * @return the Velocity's Vector.
	 */
	public Vector getVector() {
		return vector;
	}

	/**
	 * Sets the Velocity's current Vector to the given Vector.
	 *
	 * @param vector the given Vector.
	 */
	public void setVector(Vector vector) {
		this.vector = vector;
	}

}
