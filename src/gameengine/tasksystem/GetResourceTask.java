/* 2003-05-13 henko
 *  Updated the execute method. We shouldn't try to eat when we know that
 *  there are no resources.
 *
 * 2003-05-11 Martin
 *	Corrected the naming of an exception.
 *
 * 2003-05-09 henko
 *  Changed ResourceContainer to Resources.
 *
 * 2003-05-04 Martin
 *	Completed the class.
 *
 * 2003-04-15 Martin
 *	Formatted the code.
 */
package gameengine.tasksystem;

import gameengine.gameobjects.Characteristics;
import gameengine.gameobjects.Position;
import gameengine.gameobjects.Resources;
import gameengine.gameobjects.Velocity;
import gameengine.tasksystem.GeneralTaskException;
import gameengine.gameobjects.SourceNode;
import gameengine.Environment;
import gameengine.World;
import java.util.*;

/**
 * The GetResourceTask is a generic class that is used to gather the code
 * for collecting a resource for a indpendent individual until the need is
 * fulfilled. The task will make the owner move to the specified destination
 * and when there, start gathering resources and consuming them, until the need
 * is fulfilled. During the gathering the owner of the task will
 * &quot;wobble&quot; around the SourceNode. The gathering is performed in a speed
 * calculated from the owner of the task's gather skill and a constant
 * (getGatherSpeed). The task is done when the need is fulfilled. If
 * the natural resources gets depleted the task unregisters the SourceNode
 * from the world's lists of food sources, and the task is also set to done.
 */
public class GetResourceTask extends Task{

	///////////////////////////////////////
	// operations

	/**
	 * The destination for the gathering.
	 */
	private SourceNode destination;

	/**
	 * A counter that keeps track on how much that has been gathered.
	 * Each time it gets larger than 1, that unit will be transfered.
	 */
	private float gatherCounter;

	/**
	 * The task to move to the source node to gather at.
	 */
	private MoveToTask moveToTask;

	/**
	 * The task of wobbling during the gathering.
	 */
	private WobbleTask wobbleTask;

	/**
	 * The type of resource to be gathered.
	 */
	private int resourceType;


	/**
	 * Constructs a GetResourceTask with a given destination SourceNode
	 *
	 * @param destination the SourceNode to get the food at.
	 */
	public  GetResourceTask(SourceNode destination, int resourceType) {
		if (destination == null) {
			throw new IllegalArgumentException("Destination was null");
		}
		this.destination = destination;
		this.resourceType = resourceType;
		gatherCounter = 0;
		// The creation of the moveToTask and the wobble task lies in the
		// setPrivateInformation()
	}

	/**
	 * Executes the Task. If the Task contains sub tasks, it will call exeute()
	 * on those as well. If the Task is unsucessful (which sometimes is
	 * important to see) the method returns false, else it will return true.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	public boolean execute() throws GeneralTaskException {
		// Don't execute if already done.
		if (isDone()) {
			throw new GeneralTaskException(
					"This task is done and therefore cannot be executed again.");
		}
		// Moves the individual to the destination if that hasn't been done yet.
		if(!moveToTask.isDone()) {
			moveToTask.execute();
		}
		// The individual is at the node. Gather!
		else {

			// If enough gathering has been done for 1 unit
			if(gatherCounter > 1f) {
				gatherCounter -= 1f;
				// Create a new transfer task and do the transfer of one unit
				TransferTask transferTask = destination.getGatherTask();
				transferTask.execute();

				// If the transfer didn't work (the source node depleted), unregister it from
				// one of the lists of source nodes. Set this task to done.
				if(!transferTask.isDone()) {
					World.getWorld().unRegister(destination);
					done = true;
				} else {

					// Get food and happiness
					// Picks upp one (1) food
					TransferTask resourceTransfer = destination.getPickUpTask(resources,
							resourceType, 1);
					resourceTransfer.execute();
					if (resourceTransfer.isDone()) {
						ConsumeTask consumeResource = new ConsumeTask(resources,
								characteristics, resourceType);
						consumeResource.execute();
					}
	
					// If the specific need is full, set the task to done
					if(resourceType == Environment.FOOD) {
						done = (characteristics.getSaturation() >= 1f);
					}
					else if(resourceType == Environment.HAPPINESS) {
						done = (characteristics.getHappiness() >= 1f);
					}
				}
				
			}
			// Wobble, i.e. gather
			wobbleTask.execute();
			// Also increase the gathering skill (but not much).
			characteristics.increaseGatherSkill();

			// Increases the sacrifice counter according to the efficency
			// Multiplies a constant for sacrifice speed, the elapsed time
			// and the efficiency.
			gatherCounter +=
					World.getEnvironment().getGatherSpeed() *
					World.getWorld().getTimeSinceCycle() *
					characteristics.getGatherEfficiency();
		}
		return true;
	}

	/**
	 * Sends all the attributes of the task's owner to the Task. That
	 * gives the Task all the information it needs. The methods also sends
	 * the appropriate information on to the sub tasks.
	 *
	 * @param position a Position attribute describing the position of the
	 * owner of the attribute.
	 * @param velocity a Velocity attribute describing the velocity of the
	 * owner of the attribute.
	 * @param characteristics a Characteristics attribute describing the
	 * characteristics of the owner of the attribute.
	 * @param resources a Resources describing the resources of the
	 * owner of the attribute.
	 */
	public void setPrivateInformation(
			Position position,
			Velocity velocity,
			Characteristics characteristics,
			Resources resources
			) {
		super.setPrivateInformation(position, velocity, characteristics,
				resources);
		moveToTask = new MoveToTask(destination);
		moveToTask.setPrivateInformation(position, velocity, characteristics,
				resources);

		wobbleTask = new WobbleTask(
				destination,
				characteristics.getGatherEfficiency(),
				World.getEnvironment().getNodeWobbleRadius());

		wobbleTask.setPrivateInformation(position, velocity, characteristics, resources);
	}

}
