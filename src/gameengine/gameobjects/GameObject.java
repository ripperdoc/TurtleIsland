/* 
 * 2003-05-20 EliasAE
 * 	Added descriptions.
 *
 * 2003-05-11 EliasAE
 * 	Added implementation of Deletable.
 *
 * 2003-05-10 Martin
 *	Corrected the constructor and moved the Velocity to the Invididual.
 *
 * 2003-04-15 Martin
 * Corrected the registration procedure and implemented Serializable.
 *
 * 2003-04-15 EliasAE
 * 	Added the registration of the game object with World in the constructor.
 *
 * 2003-04-14 pergamon
 *  Added getVector.
 *
 * 2003-04-12 henko
 * 	Changed position to protected.
 *
 * 2003-04-10 Martin
 * 	Completed the raw code of the class. There are still formatting to be done.
 *
 * 2003-04-10 EliasAE
 * 	Formatted the code.
 *
 * 2003-04-10 EliasAE
 * 	Finished getter and setters, which makes the whole class more or
 * 	less finished.
 */
package gameengine.gameobjects;

import gameengine.gameobjects.*;
import gameengine.*;
import java.util.*;
import gameengine.World;
import java.io.*;

/**
 * The abstract class that defines all objects that have a position and
 * &quot;exists&quot; in the game world as some sort of object.
 */
public abstract class GameObject
		implements Serializable, PositionContainer, Deletable {

	private boolean deleted = false;

	/**
	 * The description of the game object.
	 */
	private String description;

	/**
	 * A String representing the name of the GameObject.
	 */
	protected String name;

	/**
	 * 	The position attribute that holds the position as of the GameObject.
	 */
	protected Position position;

	/**
	 * Creates a game object without specifying a name. The object is
	 * registered in the list over all game objects in World.
	 *
	 * @param point the position of the game object.
	 */
	public GameObject(Point point) {
		position = new Position();
		position.setPoint(point);
	}

	/**
	 * Sets the description of the GameObject.
	 * 
	 * @param description the new description of the game object.
	 */
	public void setDescription(String description) {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		if (description == null) {
			throw new IllegalArgumentException("description cannot be null");
		}
		this.description = description;
	}

	/**
	 * Gets the description of the game object.
	 *
	 * @return the description of the game object. Null if no description
	 * exists.
	 */
	public String getDescription() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return description;
	}

	/**
	 * Sets the current name of the GameObject.
	 *
	 * @param name the new name of the GameObject, represented by a String.
	 */
	public void setName(String name) {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}
		this.name = name;
	}

	/**
	 * Returns a String with the name of the GameObject.
	 *
	 * @return a String with the name of the GameObject.
	 */
	public String getName() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return name;
	}

	/**
	 * Returns a Point with the position of the GameObject.
	 *
	 * @return a Point with the position of the GameObject.
	 */
	public Point getPoint() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return position.getPoint();
	}

	/**
	 * Deletes the object.
	 */
	public void delete() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		// TODO Debug
		World.getWorld().unRegister(this);
		deleted = true;
	}

	/**
	 * Retrieves if the object is deleted or not.
	 */
	public boolean isDeleted() {
		return deleted;
	}
}
