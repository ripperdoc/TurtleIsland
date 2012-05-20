/*
 * 2003-05-11 henko
 *	If a MoveTask is non-successful it means it tried to walk out of bounds.
 *  Such a MoveTask will set the MoveToTask to done.
 *
 * 2003-05-11 Martin
 *	Corrected the naming of an exception.
 *
 * 2003-05-11 henko
 *	Updated the fields to protected. They are used in
 *  CommandedTransportTask$TransportMoveToTask.
 *
 * 2003-05-04 henko
 *	Fixed the "may miss target" problem on low FPS systems.
 *  	TODO: The solution is not optimal. The turtle will actually miss target
 *  	TODO: and then "jump back". But hopefully it's not a very big problem
 *		TODO: since the FPS normally will be much higher.
 *
 * 2003-05-02 Martin
 *	Added a new constructor that also takes the relative speed of the
 *	movement.
 *
 * 2003-04-15 pergamon
 *  Changed so that the task checks if the destination have moved or not.
 *  If it has it follows.
 *
 * 2003-04-12 henko
 * 	Made the class public.
 *
 * 2003-04-11 henko
 * 	Implemented the constructor and execute() and also implemented
 * 	Serializable.
 */

package gameengine.tasksystem;

import gameengine.Point;
import gameengine.Vector;
import gameengine.gameobjects.GameObject;

/**
 * Is a movement to an object. It is finished when the individual has
 * reached the object (and then the velocity is set to zero). The task
 * requires that the setPrivateInformation() is used.
 */
public class MoveToTask extends Task {

	/**
	 * The GameObject to move to.
	 */
	private GameObject destination;

	/**
	 * To keep track on where we're going.
	 */
	private Point destinationPoint;

	/**
	 * The relative speed of movement.
	 */
	protected float relativeSpeed;

	/**
	 * The angle to the destination.
	 * Is saved to make sure that we don't pass the destination on low FPS
	 * systems.
	 */
	private float destinationAngle;

	/**
	 * Tells whether the owner has been pointed towards the destination or not.
	 * Is set to true the first time execute is called, to remember to only
	 * update the speed, and not the angle.
	 */
	private boolean positionSet = false;

	/**
	 * Constructs a MoveToTask with a specified destination.
	 *
	 * @param destination the GameObject to move to.
	 */
	public MoveToTask(GameObject destination) {
		this.destination = destination;
		destinationPoint = destination.getPoint();
		// The speed shpuld be multiplied with 1 (= no change)
		relativeSpeed = 1f;
	}

	/**
	 * Constructs a MoveToTask with a specified destination and relative speed
	 * of the movement.
	 *
	 * @param relativeSpeed the relative speed of the movement.
	 * @param destination the GameObject to move to.
	 */
	public MoveToTask(GameObject destination, float relativeSpeed) {
		this.destination = destination;
		destinationPoint = destination.getPoint();
		this.relativeSpeed = relativeSpeed;
	}

	public void reset() {
		super.reset();
		positionSet = false;
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
					"This task is done and therefore cannot be executed again."
					);
		}

		if (destination.isDeleted()) {
			System.out.println("MoveToTask aborted due to deleted destination: " + destination);
			done = true;
			return false;
		}

		// Create points/vectors for the calculations
		Point myPoint = position.getPoint();
		Vector myVector = velocity.getVector();
		float currentAngle = myPoint.angleTo(destinationPoint);

		// If we have not reached our destination nor passed it
		if ((!(myPoint.onPosition(destinationPoint))) &&
				(!positionSet ||
				(positionSet && ((int) destinationAngle == (int) currentAngle)))) {

			// Prepare another step
			Task moveTask;
			if (!positionSet) {
				// calculate new velocity
				destinationPoint = destination.getPoint();
				destinationAngle = myPoint.angleTo(destinationPoint);
				Vector newVector = new Vector(destinationAngle, myVector.getMagnitude());

				//create new task
				moveTask = new MoveTask(newVector, relativeSpeed);
			} else {
				// create new task, use same velocity
				moveTask = new MoveTask(relativeSpeed);
			}

			// Perform the next step
			moveTask.setPrivateInformation(position, velocity, characteristics,
					resources);
			if (!moveTask.execute()) {
				// Tried to walk out of bounds
				done = true;
			}

		} else {
			// if the angle has changed (this means we've passed the target,
			// can happen on low FPS systems), "jump back" to position.
			if (destinationAngle != currentAngle) {
				position.setPoint(destinationPoint);
			}

			// we're on position, report done
			done = true;
		}

		// do not to set the angle next time if the destinationPoint havn't moved
		if (destinationPoint.equals(destination.getPoint())) {
			positionSet = true;
		} else {
			positionSet = false;
		}

		return (true);
	}
}
