/* 
 * 2003-05-12 henko
 *  Made the class implement Serializable.
 * 
 * 2003-05-08 Martin
 *	Move the fixes in the last changes to the superclass.
 *
 * 2003-05-03 Martin
 *	Formatted and added code for handling the concurrent modification exception
 *	that could be thrown when the list was modified during iteration.
 *
 * 2003-04-11 pergamon
 *  Formatted syntax and added some features for testing with mouse-click.
 *  Added functions for handling the the list.
 */
package gameengine;

import gameengine.gameobjects.GameObject;

import java.io.Serializable;
import java.util.*;

/**
 * A SortableList that can contain only GameObjects (i.e. all objects of
 * the game).
 */
public class GameObjectList extends SortableList implements Serializable {

	/**
	 * Returns the GameObject at the specified position in this list.
	 *
	 * @param index index of GameObject to return.
	 * @return the GameObject at the specified position in this list.
	 */
	public GameObject get(int index) {
		return (GameObject) list.get(index);
	}

	/**
	 * Registers, adds, the given GameObject to the list.
	 *
	 * @param gameObject the GameObject to be registered.
	 */
	public void register(GameObject gameObject) {
		super.register((Object)gameObject);
	}

	/**
	 * Unregisters, removes, the given GameObject from the list.
	 *
	 * @param gameObject the GameObject to be unregistered.
	 */
	public void unRegister(GameObject gameObject) {
		super.unRegister((Object)gameObject);
	}


}