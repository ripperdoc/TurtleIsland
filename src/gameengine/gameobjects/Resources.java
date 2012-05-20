/*
 * 2003-05-12 EliasAE
 * 	Removed support for deletable.
 *
 * 2003-05-11 EliasAE
 * 	Added support for deletable.
 *
 * 2003-05-09 henko
 * Renamed to Resources.
 *
 * 2003-04-15 Maritn
 * Implemented serializable
 *
 * 2003-04-11 Martin
 *  Completed the class. Tab formatting still to be done.
 */
package gameengine.gameobjects;

import gameengine.*;
import java.util.*;
import java.io.*;

/**
 * A container holding all the resource types. There are methods for
 * removing och adding a specified resource (with a specified amount) and a
 * get method for returning the amount of a certain resource.
 */
public class Resources implements Serializable {

	/**
	 * An array of integers, where the position represents the type of a
	 * resource and the values the amount of each resource.
	 */
	private int[] resources = new int[World.getEnvironment().getNumberOfResourceTypes()];

	/**
	 * Returns the amount of resource units of the specified resource type.
	 *
	 * @param resourceType an int describing the resource type.
	 * @return an int representing the amount of resource units of the
	 * specified resource type.
	 */
	public int getResourceAmount(int resourceType) {
		return resources[resourceType];
	}

	/**
	 * Adds the specified amount of resource units of the specified resource
	 * type to the Resources.
	 *
	 * @param resourceType an int describing the resource type.
	 * @param amount an int desciribing the number of resource units to add.
	 */
	public void addResource(int resourceType, int amount) {
		resources[resourceType] += amount;
	}

	/**
	 * Removes the specified amount of resource units of the specified resource
	 * type from the Resources. If more units were specified than there
	 * is currently available in the container the amount of the resource type
	 * is set to zero, not a negative number.
	 *
	 * @param resourceType an int describing the resource type.
	 * @param amount an int desciribing the number of resource units to remove.
	 */
	public void removeResource(int resourceType, int amount) {
		resources[resourceType] -= amount;
		if (resources[resourceType] < 0) {
			resources[resourceType] = 0;
		}
	}

}
