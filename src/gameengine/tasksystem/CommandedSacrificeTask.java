/*
 * 2003-05-13 henko
 *  The class now extends CommandedTask which takes care of making the Turtle
 *  eat while at work.
 * 
 * 2003-05-09 henko
 *  Changed ResourceContainer to Resources.
 *
 * 2003-05-03 Martin
 *	Corrected the code for eating food and getting happy.
 *
 * 2003-05-02 Martin
 *	Removed the code for calculating efficiency and added it in characteristics.
 *	Corrected a bug with the creation of the WobbleTask.
 *
 * 2003-04-30 Martin
 *	Small corrections and replacement of a dummy value. Implemented the increase
 * 	of the skill.  Fixed so that the individudal will try to eat and get happiness.
 *
 * 2003-04-23 EliasAE
 * 	Implemented the class.
 *
 * 2003-04-11 EliasAE
 * 	Added execute and setPrivateData to make it compile due to
 * 	changes in Task.
 */
package gameengine.tasksystem;

import gameengine.World;
import gameengine.gameobjects.Characteristics;
import gameengine.gameobjects.LightPillarNode;
import gameengine.gameobjects.Position;
import gameengine.gameobjects.Resources;
import gameengine.gameobjects.Velocity;

/**
 * A CommandedSacrificeTask is a commanded task and is always ordered by
 * the user. The task consists of a movement to the LightPillarNode and a
 * continuous sacrificing of resources at the node. During the sacrificing
 * the owner of the task will &quot;wobble&quot; around the LightPillarNode. The
 * sacrificing is performed in a speed calculated from the owner of the
 * task's sacrifice skill and a constant (getSacrificeSpeed()). If there
 * are no sacrificable resources at the LightPillarNode at the current time
 * the owner of the task will continue it's wobbling but without
 * sacrificing, until new resoruces have arrived.
 * The CommandedSacrificeTask requires that the setPrivateInformation() is
 * used before execution. The task will never be set to done.
 */
public class CommandedSacrificeTask extends CommandedTask {

	/**
	 * The current amount of gathered resources. Every time it is greater
	 * than one, one unit of resources is transfered.
	 */
	private float sacrificeCounter;

	/**
	 * The task to move to the light pillar node to gather at.
	 */
	private MoveToTask moveToTask;

	/**
	 * The task of wobblin during the sacrificing.
	 */
	private WobbleTask wobbleTask;

	/**
	 * The light pillar. Is just a faster reference..
	 */
	private LightPillarNode lightPillar;

	/**
	 * Constructs a CommandedSacrificeTask with the light pillar as destination.
	 */
	public  CommandedSacrificeTask() {
		super();
		lightPillar = World.getWorld().getLightPillar();
		moveToTask = new MoveToTask(lightPillar);

		sacrificeCounter = 0;
	}

	/**
	 * Executes the Task. If the Task contains sub tasks, it will call exeute()
	 * on those as well. If the Task is unsucessful (which sometimes is
	 * important to see) the method returns false, else it will return true.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	protected boolean executeTask() throws GeneralTaskException {

		// If we have arrived at the light pillar, start sacrificing
		if (moveToTask.isDone()) {

			// If we have wobbled around "working" long enough to transfer one
			// unit.
			if (sacrificeCounter > 1.0f) {
				TransferTask transferTask = lightPillar.getSacrificeTask();
				transferTask.execute();

				// Only start sacrificing the next one if we succeeded with
				// this one. The transfer will not be done if there is nothing
				// to sacrifice. If that's the case, we leave the
				// sacrificeCounter as it is (above zero) and will try again
				// each new cycle until there is something to sacrifice.
				if (transferTask.isDone()) {
					sacrificeCounter -= 1.0f;
				}
			}

			// If we're hungry or depressed, get some food and/or happiness.				
			EatAndGetHappy(lightPillar);

			// Wobble around
			wobbleTask.execute();
			// Increases the sacrifice counter according to the efficency
			// Multiplies a constant for sacrifice speed, the elapsed time,
			// the skill and the health of the indivudal.
			sacrificeCounter +=
					World.getEnvironment().getSacrificeSpeed() *
					World.getWorld().getTimeSinceCycle() *
					characteristics.getSacrificeEfficiency();
			// Increase the skill of the owner of the task
			characteristics.increaseGatherSkill();

		// We are not at the light pillar, then go there.
		} else {
			moveToTask.execute();
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
		super.setPrivateInformation(position, velocity, characteristics, resources);
		moveToTask.setPrivateInformation(position, velocity, characteristics, resources);

		// Creates the wobble task with the relative speed set by the efficiency factor.
		wobbleTask = new WobbleTask(
				World.getWorld().getLightPillar(),
				characteristics.getSacrificeEfficiency(),
				World.getEnvironment().getNodeWobbleRadius()
				);

		wobbleTask.setPrivateInformation(position, velocity, characteristics, resources);
	}

}

