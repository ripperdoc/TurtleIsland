/*
 * 2003-05-11 henko
 *  The amount field is not changed when trying to get more than there
 *  is available. Instead a temporary variable is used. This would otherwise
 *  mess up the reset().
 *
 * 2003-05-09 henko
 *  Changed ResourceContainer to Resources.
 *
 * 2003-05-03 Martin
 *	Corrected errors in the decision of when the task is done or not.
 *
 * 2003-04-23 EliasAE
 * 	Started implementing the class.
 *
 * 2003-04-11 EliasAE
 * 	Added execute and setPrivateData to make it compile due to
 * 	changes in Task.
 */
package gameengine.tasksystem;

import gameengine.World;
import gameengine.tasksystem.GeneralTaskException;
import gameengine.gameobjects.Resources;

/**
 * TransferTask transfers a specified amount of units of a specified
 * resource type between two objects carrying resources, or inside an
 * object with two storing places. If no resource units of the specified
 * type were available, the task is not set to done and thereby possibly
 * forces the owner of the task to wait until resources arrive. If not
 * enough resource units were available (i.e. not the same amount as
 * requested) the task is done but set to unsucessful.
 */
public class TransferTask extends Task implements java.io.Serializable{

	/**
	 * The Resources that resources will be taken
	 * from.
	 */
	private Resources fromContainer;

	/**
	 * The Resources that resources will be taken to.
	 */
	private Resources toContainer;

	/**
	 * An int describing the resource type.
	 */
	private int resourceType;

	/**
	 * An int desciribing the number of resource units to
	 * transfer.
	 */
	private int amount;

	/**
	 * Indicates if all the resources in the fromContainer should
	 * be transfered to the toContainer. If this is true the variablers
	 * amount and resourceType is NOT valid.
	 */
	private boolean transferAll;

	/**
	 * Constructs a TransferTask with access to two containers (one belonging
	 * to the owner of the task and the other belonging to the container to
	 * exchange with). The transfer will be done with the specified amount of
	 * the specified resource type.
	 *
	 * @param fromContainer the Resources that resources will be taken
	 * from.
	 * @param toContainer the Resources that resources will be taken to.
	 * @param resourceType an int describing the resource type.
	 * @param amount an int desciribing the number of resource units to
	 * transfer.
	 */
	public  TransferTask(
			Resources fromContainer,
			Resources toContainer,
			int resourceType,
			int amount
			) {
		if(amount < 0) {
			throw new IllegalArgumentException("Negative amount");
		}
		this.fromContainer = fromContainer;
		this.toContainer = toContainer;
		this.resourceType = resourceType;
		this.amount = amount;
		transferAll = false;
	}

	/**
	 * Constructs a TransferTask with access to two containers (one belonging
	 * to the owner of the task and the other belonging to the container to
	 * exchange with). The task will transfer ALL resources from the
	 * fromContainer to the toContainer.
	 *
	 * @param fromContainer the Resources that resources will be taken
	 * from.
	 * @param toContainer the Resources that resources will be taken to.
	 */
	public  TransferTask(Resources fromContainer, Resources toContainer) {
		this.fromContainer = fromContainer;
		this.toContainer = toContainer;
		transferAll = true;
	}

	/**
	 * Executes the Task. If the Task contains sub tasks, it will call execute()
	 * on those as well. If the Task is unsucessful (which sometimes is
	 * important to see) the method returns false, else it will return true.
	 * The task is unsuccessful when not all resources that was requested could
	 * be transfered.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	public boolean execute() throws GeneralTaskException {
		if(done == true) {
			throw new GeneralTaskException();
		}

		// Set values of done and success in advance and change if needed
		done = false;
		boolean successful = true;

		if(transferAll) {
			// Don't change successful, isn't needed when transfering all
			for(
					int resourceIndex = 0;
					resourceIndex < World.getEnvironment().getNumberOfResourceTypes();
					resourceIndex++
					) {
				amount = fromContainer.getResourceAmount(resourceIndex);
				if(amount > 0) {
					fromContainer.removeResource(resourceIndex, amount);
					toContainer.addResource(resourceIndex, amount);
					done = true;
				}
			// Set done to true to avoid errors
			// TODO Investigate this further
			done = true;
			}
		}
		// Transfer a specific resource
		else {
			int modifiedAmount = amount;

			if(amount > fromContainer.getResourceAmount(resourceType)) {
				// Not all resources requested are available
				// Reset the amount to the amount of resources available
				modifiedAmount = fromContainer.getResourceAmount(resourceType);
				successful = false;
			}

			if(modifiedAmount > 0) {
				// The requested (and available) amount is positive
				// Do the transfer
				fromContainer.removeResource(resourceType, modifiedAmount);
				toContainer.addResource(resourceType, modifiedAmount);
				done = true;
			}
			// If done hasn't been set to true yet, it isn't
		}
		return successful;
	}

}
