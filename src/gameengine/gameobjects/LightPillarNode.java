/* 
 * 2003-05-11 EliasAE
 * 	Added support for deletable.
 *
 * 2003-05-09 henko
 *  Changed ResourceContainer to Resources.
 *
 * 2003-04-15 Martin
 * Completed the task, maybe the getSacrificeTask needs to be changed later.
 */
package gameengine.gameobjects;

import gameengine.tasksystem.TransferTask;
import gameengine.*;
import java.util.*;
import java.io.*;

/**
  * A node with two separate possibilites to store resources, one of them
 * &quot;sacrificed&quot;, i.e. not freely available and one of them free. The
 * sacrificed resource container is supposed to contain only one resource
 * type (the sacrificiable resource), but it is possbile to store more
 * types. The source nodes could either be treated as nodes, and be a part
 * of a transportation task (only using the free resource container) and it
 * could also be the destination of a gathering task, intendended for
 * moving resource from the natural storing to a the free storing, i.e.
 * gathering.
 */
public class LightPillarNode extends Node {

	/**
  	 * The container of the sacrificed resources of the light pillar.
  	 */
	private Resources sacrificedResources;

	/**
	 * Constructs a light pillar at the specified point. Registers the light
	 * pillar at the world.
	 *
	 * @param point the position to create the LightPillarNode at.
	 */
	public  LightPillarNode(Point point) {
		// The zero is a hack for not using the constructor in Node
		// that would call World.register().
		super(point,0);
		World.getWorld().register(this);
		sacrificedResources = new Resources();
		setDescription("A light pillar. Here you can sacrifice gold.");
	}

	/**
	 * Returns a TransferTask that transfers one unit from the free resources
	 * at the node to the sacrificed units.
	 *
	 * @return a TransferTask that sacrifices one unit from the node's free
	 * resources.
	 */
	public TransferTask getSacrificeTask() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		// Returns a TransferTask that transfers 1 unit from the free resources
		// to the sacrificed resources.
	    return new TransferTask(resources, sacrificedResources, Environment.SACRIFICE, 1);
	}

	/**
	 * Returns the amount of sacrificed resource units at the light pillar.
	 *
	 * @return an int representing the amount of sacrificed resource units at
	 * the light pillar.
	 */
	public int getSacrificedAmount() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return sacrificedResources.getResourceAmount(Environment.SACRIFICE);
	}

	/**
	 * Calculates the current maximum safety influence radius of the light
	 * pillar and returns a float representing that radius. Formula: radius =
	 * getDefaultLightPillarRadius + getSacrificedAmount *
	 * getSacrificedUnitsDistanceRatio.
	 *
	 * @return a float representing the current maximum safety influence radius
	 * of the light pillar.
	 */
	public float getInfluenceRadius() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return World.getEnvironment().getDefaultLightPillarRadius() +
				(getSacrificedAmount() *
				World.getEnvironment().getSacrificedUnitsDistanceRatio());
	}

}
