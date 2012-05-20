/* 2003-04-30 Martin
 * Added a correction in the constructor that assures that the angle is in the
 * correct interval.
 *
 * 2003-04-15 Martin
 *	Implemented serializable.
 *
 * 2003-04-11 EliasAE
 * 	Wrote the class with equals and hashCode.
 */
package gameengine;

import java.io.*;

/**
 * A helper object, describing a vector, composed by a direction and a
 * magnitude (i.e. &quot;size&quot;). Has methods for getting the direction and
 * magnitude.
 */
public class Vector implements Serializable{

	/**
	 * The direction, angle, of the vector.
	 */
	private float direction;

	/**
	 * The magnitude, &quot;size&quot;, of the vector.
	 */
	private float magnitude;

	/**
	 * Constructs a Vector with direction and magnitude set to zero.
	 */
	public Vector() {
		direction = 0.0f;
		magnitude = 0.0f;
	}

	/**
	 * Constructs a Vector with a given direction and magnitude.
	 *
	 * @param direction the direction of the vector.
	 * @param magnitude the magnitude of the vector.
	 */
	public Vector(float direction, float magnitude) {
		// Corrects the angle to the interval 0 to 2 pi
		if(direction > 2 * Math.PI) {
			direction = (float)(direction - 2 * Math.PI);
		}
		else if (direction < 0) {
			direction = (float)(2 * Math.PI + direction);
		}
		this.direction = direction;
		this.magnitude = magnitude;
	}

	/**
	 * Returns the direction of the vector.
	 *
	 * @return a float representing the direction of the vector.
	 */
	public float getDirection() {
		return direction;
	}

	/**
	 * Returns the magnitude of the vector.
	 *
	 * @return a float representing the magnitude of the vector.
	 */
	public float getMagnitude() {
		return magnitude;
	}

	/**
	 * Compares two objects to see if they are equal.
	 *
	 * @return true if the objects repesents the same vector otherwise false.
	 */
	public boolean equals(Object otherObject) {
		if (otherObject == null) {
			return false;
		}

		if (otherObject instanceof Vector) {
			Vector otherVector = (Vector) otherObject;
			return (
					direction == otherVector.direction
					&& magnitude == otherVector.magnitude
					);
		}
		return false;
	}

	/**
	 * Retrieves the hash code for the object.
	 *
	 * @return the hash code as specified by Java.
	 */
	public int hashCode() {
		short highWord = (short) direction;
		short lowWord = (short) magnitude;
		return ((highWord << 16) | lowWord);
	}
}
