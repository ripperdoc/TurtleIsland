/*
 * 2003-04-12 henko
 * 	Made class public.
 *
 * 2003-04-10 Martin
 * 	Completed the class. There are still formatting to be done.
 *
 * 2003-04-10 EliasAE
 * 	Formatted the code.
 */
package gameengine.gameobjects;

import gameengine.*;

import java.util.*;

/**
 * A point with only a position. The only purpose of the special point is
 * to act as a destination object for a movement task. After the task is
 * finished, the special point will be unreferenced and thereby discarded.
 */
public class SpecialPoint extends GameObject {

	/**
	 * Constructs a SpecialPoint at the specified position. NOTE: Does not
	 * register itself at any list in the world, and the object will
	 * &quot;disappear&quot; when no task is referencing it.
	 *
	 * @param position a Point representing the position of the SpecialPoint.
	 */
	public SpecialPoint(Point position) {
		super(position);
	}

}
