/* 
 * 2003-05-11 EliasAE
 * 	Added support for deletable.
 *
 * 2003-05-11 henko
 *  Corrected a bug in getLeaveTask(). It previously moved resources 
 *  from itself and to itself. 
 *
 * 2003-05-10 Martin
 *	Added the hack for registering correctly (another constructor).
 *
 * 2003-05-09 henko
 *  Changed ResourceContainer to Resources. Node now implements
 *  ResourceContainer.
 *
 * 2003-04-15 Martin
 * 	Completed the class, by writing the getTask methods and making the
 * 	constructor
 */

package gameengine.gameobjects;

import gameengine.*;
import gameengine.World;
import gameengine.tasksystem.TransferTask;
import java.util.*;

/**
 * A node is a game object that has a position and a resource container.
 * All nodes can be a part of a transportation route of a transportation
 * task.
 */
public class Node extends GameObject implements ResourceContainer {

	/**
	 * The container of the node's resources.
	 */
	protected Resources resources;

	/**
	 * Constructs a Node with the position given by the Point. It also
	 * registers the Node to the World.
	 *
	 * @param point the position for the Node.
	 */
	public Node(Point point) {
		super(point);
		resources = new Resources();
		World.getWorld().register(this);
		setDescription("A flag where turtles can store resources for further " +
				"transportation later");
	}

	protected Node(Point point, int dummyParameter) {
		super(point);
		resources = new Resources();
	}

	/**
	 * Returns a task that transfers a specific amount of a specific
	 * resource type to the given resource container.
	 *
	 * @param resources the Resources to transfer the nodes
	 * resources to.
	 * @param resourceType the type of resource to transfer.
	 * @param amount the amount of resource units to transfer.
	 * @return a TransferTask that picks up the specified resources from the node,
	 */
	public TransferTask getPickUpTask(Resources otherResources, int resourceType, int amount) {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return new TransferTask(resources, otherResources, resourceType, amount);
	}

	/**
	 * Returns a task that leaves all the resources of given resource container
	 * to the node's resource container.
	 *
	 * @param resources the Resources to take all the resources
	 * from.
	 * @return a TransferTask that leaves all the resources to the node.
	 */
	public TransferTask getLeaveTask(Resources otherResources) {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return new TransferTask(otherResources, resources);
	}

	/**
	 * Returns the amount of resource units available of the specified resource
	 * type.
	 *
	 * @param resourceType an int representing the resource type.
	 * @return an int representing the amount of resource units available of
	 * the specified resource type.
	 */
	public int getResourceAmount(int resourceType) {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return resources.getResourceAmount(resourceType);
	}

}
