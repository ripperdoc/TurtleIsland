/*
 * 2003-05-13 henko
 *  The class now extends CommandedTask which takes care of making the Turtle
 *  eat while at work.
 * 
 * 2003-05-11 Martin
 *	Corrected the naming of an exception.
 *
 * 2003-05-09 henko
 *  Changed ResourceContainer to Resources.
 *
 * 2003-05-03 Martin
 *	Corrected the code for eating food and getting happy.
 *
 * 2003-05-02 Martin
 *	Removed the code for calculating efficiency and added it in characteristics.
 *
 * 2003-04-30 Martin
 *	Added the increase of a skill when used (but the code for increasing
 *	a skill should still be written in the Characteristics). Fixed so that
 *	the individudal will try to eat and get happiness.
 *
 * 2003-04-23 EliasAE
 * 	The gathering now uses Characteristics.getGatherSkill.
 *
 * 2003-04-19 EliasAE
 * 	Check for null pointer in constructor. Added setPrivateInformation to
 * 	inform the subtask about changes in the private information. To avoid
 * 	NullPointerException when contained Tasks is created before the
 * 	private information is set.
 *
 * 2003-04-15 Martin
 *	Started implementing the class. Not finished yet.
 *
 * 2003-04-11 EliasAE
 * 	Added execute and setPrivateData to make it compile due to
 * 	changes in Task.
 */
package gameengine.tasksystem;

import gameengine.World;
import gameengine.gameobjects.Characteristics;
import gameengine.gameobjects.Position;
import gameengine.gameobjects.Resources;
import gameengine.gameobjects.SourceNode;
import gameengine.gameobjects.Velocity;

/**
  * A CommandedGatherTask is a commanded task and is always ordered by the
 * user. The task consists of a movement to the given SourceNode and a
 * continuous gathering of resources at the node. During the gathering the
 * owner of the task will &quot;wobble&quot; around the SourceNode. The gathering is
 * performed in a speed calculated from the owner of the task's gather
 * skill and a constant (getGatherSpeed). If the natural resources gets
 * depleted the task unregisters the gathering source from the worlds lists
 * of source nodes and the task is set to done.
 *
 * The CommandedGatherTask requires that the setPrivateInformation() is
 * used before execution.
 */
public class CommandedGatherTask extends CommandedTask {

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
	 * The task of wobblin during the gathering.
	 */
	private WobbleTask wobbleTask;

	/**
	 * Constructs a CommandedGatherTask with a given destination SourceNode (the resource type is
	 * chosen through the selection of the SourceNode).
	 * a given resource type.
	 *
	 * @param destination the SourceNode to gather at.
	 */
	public CommandedGatherTask(SourceNode destination) {
		super();
		if (destination == null) {
			throw new IllegalArgumentException("Destination was null");
		}
		this.destination = destination;
		moveToTask = new MoveToTask(destination);

		// The initiation of the wobble task is in the setPrivateInformation.

		gatherCounter = 0;
	}

	/**
	 * Executes the Task. If the Task contains sub tasks, it will call exeute()
	 * on those as well. If the Task is unsucessful (which sometimes is
	 * important to see) the method returns false, else it will return true.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	protected boolean executeTask() throws GeneralTaskException {
		
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
				}
			}

			// If we're hungry or depressed, get some food and/or happiness.				
			EatAndGetHappy(destination);

			// Wobble, i.e. gather
			wobbleTask.execute();

			// Increases the sacrifice counter according to the efficency
			// Multiplies a constant for sacrifice speed, the elapsed time
			// and the efficiency.
			gatherCounter +=
					World.getEnvironment().getGatherSpeed() *
					World.getWorld().getTimeSinceCycle() *
					characteristics.getGatherEfficiency();
			// Increase the skill of the owner of the task
			characteristics.increaseGatherSkill();
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
		super.setPrivateInformation(
				position, velocity, characteristics, resources);
		moveToTask.setPrivateInformation(position, velocity, characteristics, resources);

		// Creates the wobble task with the relative speed set by the efficiency factor.
		wobbleTask = new WobbleTask(
				destination,
				characteristics.getGatherEfficiency(),
				World.getEnvironment().getNodeWobbleRadius());

		wobbleTask.setPrivateInformation(position, velocity, characteristics, resources);
	}

}
