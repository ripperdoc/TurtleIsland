/*
 * 2003-05-12 henko
 *	Added a special constructor for creating the bounds (in Environment). This
 *  makes it possible to check for out of bounds in Point(x, y).
 *
 * 2003-05-11 Martin/henko
 *	Made som fixes to avoid bugs, and a constructor for random points.
 *
 * 2003-05-04 EliasAE
 * 	Changed pow(number, 2) to number * number for performance.
 *
 * 2003-04-18 EliasAE
 * 	Implemented isOutOfBounds.
 *
 * 2003-04-15 Martin
 * 	Added serializable.
 *
 * 2003-04-14 Martin
 *	Added the time dependency of the calculation of a new point from a speed.
 *
 * 2003-04-13 EliasAE
 * 	Changed the angleTo to use atan2 for angles on the whole circle.
 *
 * 2003-04-13 pergamon
 *  Wrote the distanceTo and onPosition functions.
 *
 * 2003-04-12 pergamon
 *  Formatted syntax. Wrote simple functions to use positioning with mouse-click
 *  testing.
 *
 * 2003-04-12 henko
 * 	Implemented the constructor with arguments and angleTo().
 */
package gameengine;


import java.io.*;

/**
 * An helper object describing a point, composed by a X and a Y coordinate.
 * Has methods for getting the X and Y coordinate. It has also got methods
 * for comparing a Point to another Point (within a specified &quot;roundoff&quot;
 * distance), for calculating the distance between two points, calculating
 * the angle between two points and checking if the point is out of bounds.
 */
public class Point implements Serializable{

	/**
	 * Represents the X coordinate.
	 */
	protected float x;

	/**
	 * Represents the Y coordinate.
	 */
	protected float y;

	/**
	 * Constructs a Point with the x and y coordinates randomized
	 * within the allowed bounds.
	 */
	public Point() {
		
		float xSpan = World.getEnvironment().getMaxCoordinate().getX() -
				World.getEnvironment().getMinCoordinate().getX();
		float ySpan = World.getEnvironment().getMaxCoordinate().getY() -
				World.getEnvironment().getMinCoordinate().getY();

		x = World.getEnvironment().getMinCoordinate().getX() +
				(float)Math.random() * xSpan;
		y = World.getEnvironment().getMinCoordinate().getY() +
				(float)Math.random() * ySpan;

		// Does not check for out of bounds, because the random values can't
		// be out of bounds.
	}

	/**
	 * Constructs a Point by calculating the resulting position of applying the
	 * given velocity Vector to the given starting Point by using the time
	 * since the last update cycle (getTimeSinceCycle()).
	 *
	 * @param startPoint the starting Point before applying the velocity.
	 * @param velocity the velcity Vector to calculate the new Point.
	 */
	public Point(Point startPoint, Vector velocity) {
		
		// Calculate new point
		this.x = startPoint.getX() +
				((float) (velocity.getMagnitude() *
				World.getWorld().getTimeSinceCycle() *
				Math.cos(velocity.getDirection())));
		this.y = startPoint.getY() +
				((float) (velocity.getMagnitude() *
				World.getWorld().getTimeSinceCycle() *
				Math.sin(velocity.getDirection())));

		// Check if out of bounds
		if(isOutOfBounds()) {
			throw new IllegalArgumentException(
					"This point is out of bounds with (" + x + ", " + y + ")"
					);
		}
	}

	/**
	 * Constructs a Point with the given X and Y coordinate.
	 *
	 * @param x the X coordinate.
	 * @param y the Y coordinate.
	 */
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
		
		// Check if out of bounds
		if(isOutOfBounds()) {
			throw new IllegalArgumentException(
					"This point is out of bounds with (" + x + ", " + y + ")"
					);
		}
	}
	
	/**
	 * Constructs a Point with the given X and Y coordinate.
	 *
	 * @param x the X coordinate.
	 * @param y the Y coordinate.
	 */
	public Point(Point point) {
		x = point.x;
		y = point.y;

		// Check if out of bounds
		if(isOutOfBounds()) {
			throw new IllegalArgumentException(
					"This point is out of bounds with (" + x + ", " + y + ")"
					);
			}
		}

	/**
	 * Constructs a Point with the given X and Y coordinate. This is used by 
	 * Environment to create the bounds and does therefore NOT check if 
	 * arguments are out of bounds.
	 *
	 * @param coordinates a float array with two values, x and y.
	 */
	public Point(float[] coordinates) {
		this.x = coordinates[0];
		this.y = coordinates[1];
		
		// NO OUT OF BOUNDS CHECK
		// Since this constructor is used to create the bounds.
	}
	
	/**
	* Returns the Point's X coordinate.
	*
	* @return a float representing the Point's X coordinate.
	*/
	public float getX() {
		return x;
	}

	/**
	* Returns the Point's Y coordinate.
	*
	* @return a float representing the Point's Y coordinate.
	*/
	public float getY() {
		return y;
	}

	/**
	* Returns a boolean value stating if the specified point has the same
	* position, within a given coordinate offset, from the Environments
	* getOnPositionOffset().
	*
	* @param point the Point to compare this Point with.
	* @return a boolean stating if the given Point has the same position.
	*/
	public boolean onPosition(Point point) {
		boolean onPosition = false;
		// if the distance to the point is shorter than the onPositionOffset.
		if (distanceTo(point) <
				World.getEnvironment().getOnPositionOffset()) {
			onPosition = true;
		}
		return onPosition;
	}

	/**
	* Calculates the distance between the point itself and another specified
	* point.
	*
	* @param point the Point to calculate the distance to.
	* @return a float representing the distance between the points.
	*/
	public float distanceTo(Point point) {
		// Just used pythagoras.
		float dX = (this.x - point.getX());
		float dY = (this.y - point.getY());
		return (float) Math.sqrt(dX * dX + dY * dY);
	}

	/**
	* Calculates the angle from the point itself to another specified point.
	*
	* @param point the Point to calculate the angle to.
	* @return a float representing the angle between the points.
	*/
	public float angleTo(Point point) {
		float dX = point.x - this.x;
		float dY = point.y - this.y;

		// Gets angle between -pi and +pi.
		float angle = (float) Math.atan2(dY, dX);
		// Converts to angle between 0 and +2pi.
		if(angle < 0) {
			angle += 2 * Math.PI;
		}
		return angle;
	}

	/**
	* Checks if the given coordinates are out of bounds (greater than the
	* getMaxCoordinate() or lesser than the getMinCoordinate()).
	*
	* @return a boolean stating if the Point is out of bounds.
	*/
	public boolean isOutOfBounds() {
		Point minCoordinate = World.getEnvironment().getMinCoordinate();
		Point maxCoordinate = World.getEnvironment().getMaxCoordinate();
		return x < minCoordinate.getX() || x > maxCoordinate.getX()
				|| y < minCoordinate.getY() || y > maxCoordinate.getY();
	}

}
