/*
 * 2003-05-11 EliasAE
 * 	Added support for deletable.
 *
 * 2003-05-09 henko
 *  Changed ResourceContainer to Resources.
 *
 * 2003-05-01 pergamon
 *  removed World.getWorld().register(this); from the constructor due to that
 *  the object already register itself in the GameObject's constructor.
 *
 * 2003-04-15 Martin
 * Completed the task, maybe the getGatherTask needs to be changed later.
 */
package gameengine.gameobjects;

import gameengine.tasksystem.TransferTask;
import gameengine.*;
import gameengine.gameobjects.ObjectDeletedException;
import java.util.*;

/**
 * A node with two separate possibilites to store resources, one of them
 * &quot;natural&quot;, i.e. not freely available and one of them free. The natural
 * resource container is supposed to contain only one resource type (for
 * example food), but it is possbile to store more types. The source nodes
 * could either be treated as nodes, and be a part of a transportation task
 * (only using the free resource container) and it could also be the
 * destination of a gathering task, intendended for moving resource from
 * the natural storing to a the free storing, i.e. gathering.
 */
public class SourceNode extends Node {

	/**
	 * The container of the natural resources of the source node.
	 */
	private Resources naturalResources;

	/**
	 * The type of the natural resource of the source node.
	 */
	private int naturalResourceType;

	/**
	 * Constructs a new source node at a specified position with a specified
	 * resource and initial amount of natural resource units. It also registers
	 * the node at one of the worlds lists of source nodes (specified by the
	 * resourceType) and at the list of all objects.
	 *
	 * @param point a Point describing the position of the SourceNode.
	 * @param resourceType an int representing the resource type.
	 * @param initialAmount an int representing the amount of resource units of
	 * the natural resource of the SourceNode.
	 */
	public SourceNode(Point point, int resourceType, int initialAmount) {
		// With dummy parameter
		super(point,0);
		naturalResources = new Resources();
		naturalResources.addResource(resourceType, initialAmount);
		naturalResourceType = resourceType;
		World.getWorld().register(this);
		setDescription("A place where turtles can gather resources.");
	}

	/**
	 * Returns a ready made gather task that will transfer one unit of the
	 * natural resource to the "free" container.
	 *
	 * @return a TransferTask that will transfer one unit of the
	 * natural resource to the "free" container.
	 */
	public TransferTask getGatherTask() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		// Returns a TransferTask that transfers 1 unit from the natural
		// resources to the free resources.
		return new TransferTask(naturalResources, resources, naturalResourceType, 1);
	}

	/**
	 * Returns an int representing the type of the natural resource of the
	 * SourceNode.
	 *
	 * @return an int representing the type of the natural resource.
	 */
	public int getNaturalResourceType() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return naturalResourceType;
	}

	/**
	 * Returns the amount of resource units available of the natural resource.
	 *
	 * @return an int representing the amount of resource units available of
	 * the natural resource.
	 */
	public int getNaturalResourceAmount() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return naturalResources.getResourceAmount(naturalResourceType);
	}

}
