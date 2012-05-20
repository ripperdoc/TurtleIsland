/*
 * 2003-05-11 henko
 *  Created. Is used by MoveTask to create a point that is within the bounds
 *  from a given point that is out of bounds.
 */
package gameengine;

import gameengine.*;

/**
 * An helper object describing a point, composed by a X and a Y coordinate.
 * Has methods for getting the X and Y coordinate. It has also got methods
 * for comparing a Point to another Point (within a specified &quot;roundoff&quot;
 * distance), for calculating the distance between two points, calculating
 * the angle between two points and checking if the point is out of bounds.
 * Can never be out of bounds, that is never lower than minCoordinate nor 
 * larger than maxCordinate.
 */
public class FailSafePoint extends Point {

	/**
	* Constructs a Point by calculating the resulting position of applying the
	* given velocity Vector to the given starting Point by using the time
	* since the last update cycle (getTimeSinceCycle()). Can never be out of 
	* bounds, that is never lower than minCoordinate nor larger than 
	* maxCordinate.
	*
	* @param startPoint the starting Point before applying the velocity.
	* @param velocity the velcity Vector to calculate the new Point.
	 */
	public FailSafePoint(Point startPoint, Vector velocity) {
		
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
		ensureWithinBounds();
	}

	/**
	* Constructs a Point with the given X and Y coordinate. Can never be out 
	* of bounds, that is never lower than minCoordinate nor larger than 
	* maxCordinate.
	*
	* @param x the X coordinate.
	* @param y the Y coordinate.
	 */
	public FailSafePoint(float x, float y) {
		// Save new point
		this.x = x;
		this.y = y;
		
		// Check if out of bounds
		ensureWithinBounds();
	}

	/**
	* Constructs a Point that is guaranteed to be within the bounds from a 
	* Point that might be out of bounds. 
	*
	* @param point the Point to clone. Is allowed to be out of bounds.
	 */
	public FailSafePoint(Point point) {
		// Copy coordinates
		x = point.x;
		y = point.y;
		
		
		// Check if out of bounds
		ensureWithinBounds();
	}

	/**
	 * Ensures that both coordinates are within the bounds. If they are 
	 * out of bounds, it will correct them.
	 */
	private void ensureWithinBounds() {
		Point minCoordinate = World.getEnvironment().getMinCoordinate();
		Point maxCoordinate = World.getEnvironment().getMaxCoordinate();
		
		if (x > maxCoordinate.getX()) {
			x = maxCoordinate.getX();
		} else if (x < minCoordinate.getX()) {
			x = minCoordinate.getX();
		}
		
		if (y > maxCoordinate.getY()) {
			y = maxCoordinate.getY();
		} else if (y < minCoordinate.getY()) {
			y = minCoordinate.getY();
		}
	}
}
