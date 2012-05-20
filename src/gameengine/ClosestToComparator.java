/* 
 * 2003-05-12 henko
 *  Made the class implement Serializable.
 * 
 * 2003-05-09 Martin
 *	Finished the class.
 */
package gameengine;

import gameengine.gameobjects.GameObject;

import java.io.Serializable;
import java.util.*;

/**
 * A ClosestToComparator compares to GameObjects after how close they
 * are to a specific Point, where the closest will be sorted first.
 */
public class ClosestToComparator implements Serializable, Comparator {

	/**
	 * The point to compare the distance to.
	 */
	private Point comparePoint;

	/**
	 * Constructs a ClosestToComparator with the given point as the
	 * point to calculate the distances to.
	 *
	 * @param point the point to calculate the distances to
	 */
	public ClosestToComparator(Point point) {
		comparePoint = point;
	}

	/**
	 * Compares the two given objects by calculating their distance
	 * to the ClosestToComparator's reference Point. If the first object
	 * has a lower distance than the other, the methods returns -1. If
	 * the first object has a greater distance than the other, the method
	 * returns 1. If the distances are equal, the methods returns 0.
	 * If one of the objects are deleted, that one will be considered
	 * farther away than the other. If both are deleted, they will
	 * be equal far away.
	 *
	 * @param firstObject the first object to be compared.
	 * @param secondObject the second object to be compared.
	 */
	public int compare(Object firstObject, Object secondObject) {
		GameObject firstGameObject = ((GameObject)firstObject);
		GameObject secondGameObject = ((GameObject)secondObject);
		if (firstGameObject.isDeleted()) {
			if (secondGameObject.isDeleted()) {
				return 0;
			}
			return 1;
		}
		if (secondGameObject.isDeleted()) {
			return -1;
		}
		float firstDistance =
				firstGameObject.getPoint().distanceTo(comparePoint);
		float secondDistance =
				firstGameObject.getPoint().distanceTo(comparePoint);

		if(firstDistance < secondDistance) {
			return -1;
		}
		if(firstDistance == secondDistance) {
			return 0;
		}
		return 1;
	}
}
