/* 
 * 2003-05-12 EliasAE
 * 	Socialization and remove of socialization groups.
 *
 * 2003-05-11 Martin
 *	Added the method removeFromGroup() to correctly remove indiviudals
 *	when this task is finished by the TaskList. Corrected the naming
 *	of an exception.
 *
 * 2003-05-10 henko
 *  SocializeTask now uses Randomizer.
 *
 * 2003-05-09 henko
 *  Changed ResourceContainer to Resources.
 *
 * 2003-05-09 EliasAE
 * 	Added health as a prarameter in the calculation of if the
 * 	task should make an offspring or not.
 *
 * 2003-05-06 EliasAE
 * 	Started the implementation
 *
 * 2003-04-30 Martin
 * 	Formatted.
 *
 * 2003-04-11 EliasAE
 * 	Added execute and setPrivateData to make it compile due to
 * 	changes in Task.
 */
package gameengine.tasksystem;

import gameengine.gameobjects.*;
import gameengine.tasksystem.*;
import gameengine.*;

/**
 * The SocializeTask is created when an owner of a Characteristics
 * attribute have no other needs that have to be fulfilled. The task
 * consists of a movement to the specified SocializeGroup and then a
 * continuous wobbling around the centre of the group. The owner of the
 * task will continue with this until other tasks are prioritized. During
 * the wobbling, checks will be made if reproduction is performed in the
 * group, which if sucessful creates a new Individual.
 * The task will never be set to done.
 */
public class SocializeTask extends Task {

	/**
	 * The group to which the socialize task should walk and socialize.
	 */
	private SocializeGroup destinationGroup;

	/**
	 * If the executer is registered with the destinationGroup.
	 * This happens when the executer arrives, that is the moveTo
	 * task is finished.
	 */
	private boolean registeredWithGroup;

	/**
	 * If the individual was a child last cycle. Used to check if the 
	 * individual hag grown up to an adult, in which case the group
	 * needs to be notified.
	 */
	private boolean wasChild;

	/**
	 * The move task to move to the socialize group.
	 */
	private MoveToTask moveToGroupTask;

	/**
	 * The wobble task when socializing.
	 */
	private WobbleTask wobbleTask;

	/**
	 * Time since last try to mate.
	 */
	private float timeSinceLastMateAttempt = 0;

	/**
	 * The individual that will socialize.
	 */
	private Individual executer;

	/**
	 * A randomizer that keeps track of when it's time to "get some company".
	 */
	private Randomizer socializeRandomizer;

	/**
	 * Constructs a SoicalizeTask with a given destination SocializationGroup.
	 *
	 * @param destination the SocializationGroup to join.
	 * @param executer the individual that will socialize.
	 */
	public  SocializeTask(SocializeGroup destinationGroup, Individual executer) {
		this.destinationGroup = destinationGroup;
		this.executer = executer;
		registeredWithGroup = false;
		wasChild = executer.isChild();

		moveToGroupTask = new MoveToTask(destinationGroup);
		socializeRandomizer = new IntervalRandomizer(World.getEnvironment().getReproduceInterval());
	}

	/**
	 * Executes the Task. If the Task contains sub tasks, it will call exeute()
	 * on those as well. If the Task is unsucessful (which sometimes is
	 * important to see) the method returns false, else it will return true.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	public boolean execute() throws GeneralTaskException {
		if (isDone()) {
			throw new GeneralTaskException(
					"This task is done and therefore cannot be executed again.");
		}

		// Moves to the socialize group.
		if (!moveToGroupTask.isDone()) {
			// If the group was deleted, we create a new one at this point,
			// with only us in it. 
			if (destinationGroup.isDeleted()) {
				registeredWithGroup = false;
				destinationGroup = new SocializeGroup(position.getPoint());
				moveToGroupTask = new MoveToTask(destinationGroup);
				moveToGroupTask.setPrivateInformation(
						position, velocity, characteristics, resources);
				wobbleTask = new WobbleTask(
						destinationGroup,
						characteristics.getHealth(),
						World.getEnvironment().getSocializeWobbleRadius()
						);
				wobbleTask.setPrivateInformation(
						position, velocity, characteristics, resources);
			} else {
				moveToGroupTask.execute();
				// When we have arrived, we need to register with the group.
				if (moveToGroupTask.isDone()) {
					destinationGroup.register(executer);
					registeredWithGroup = true;
					wasChild = executer.isChild();
				}
			}
		} else {
			checkExecuterGrowth();

			if (destinationGroup.isReproduceable()) {
				float probabilityModifier = characteristics.getHealth()
						/ World.getEnvironment().getDefaultNeedValue();

				// Time to mate? ;)
				if (socializeRandomizer.isSuccessful(probabilityModifier)) {
					new Individual(
							destinationGroup.getPoint(),
							executer,
							destinationGroup.getRandomPartner(executer)
							);
				}

			}
			wobbleTask.execute();
		}

		return true;
	}

	/**
	 * Performs a check if the individual has grown from child to adult.
	 * If that is the case, it notifies the group of the change.
	 */
	private void checkExecuterGrowth() {
		if (wasChild && !executer.isChild() && registeredWithGroup) {
			destinationGroup.childToAdult(executer);
			wasChild = false;
		}
	}

	/**
	 * Removes the executer, the individual in question, from it's group.
	 * The method is to be used when the SocializeTask is finished by the
	 * TaskList.
	 */
	public void removeFromGroup() {
		checkExecuterGrowth();

		// Only unregister from the socialize group if we registered,
		// which we did when we arrived. Thats is, if we have not 
		// arrived, we have not registered, and we cannot unregister.
		if (registeredWithGroup) {
			destinationGroup.unRegister(executer);
			registeredWithGroup = false;
		}
		// Set to done to make sure this task isn't executed any more.
		done = true;
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

		wobbleTask = new WobbleTask(
				destinationGroup,
				characteristics.getHealth(),
				World.getEnvironment().getSocializeWobbleRadius()
				);
		wobbleTask.setPrivateInformation(
				position, velocity, characteristics, resources);

		moveToGroupTask.setPrivateInformation(
				position, velocity, characteristics, resources);
	}

}
