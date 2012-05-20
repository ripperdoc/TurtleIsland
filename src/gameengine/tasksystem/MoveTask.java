/*
 * 2003-05-11 henko
 *	Made execute() return false if it tried to walk out of bounds. This is
 *  used by MoveToTask.
 *
 * 2003-05-11 Martin
 *	Corrected the naming of an exception.
 *
 * 2003-05-09 henko
 * 	Changed ResourceContainer to Resources.
 *
 * 2003-05-02 Martin
 *	TODO The class has no check for out-of-bounds, and there is still the
 *	TODO problem with low FPS (currently <25) that makes the turtle miss it's
 *	TODO destination.
 *	Changed the constructor and code to accept relative speed variables
 *	that changes the effective speed of the turtle without changing the
 *	velocity-object.
 *
 * 2003-04-11 EliasAE
 * 	Removed setPrivateData due to changes in Task. Task will handle
 * 	setPrivateData and has protected members to save the
 * 	arguments passed to it.
 *
 * 2003-04-11 EliasAE
 * 	Added execute and setPrivateData to make it compile due to
 * 	changes in Task.
 *
 * 2003-04-11 EliasAE
 * 	Implemented the methods.
 */
package gameengine.tasksystem;

import gameengine.FailSafePoint;
import gameengine.Point;
import gameengine.Vector;

/**
 * The MoveTask is instantly done and always sucessful. The task changes
 * the given position (i.e. moves the owner of the task) to a new position
 * calculated from the owner of the task's velocity. It is also possible to
 * first change the velocity (by specifying a new Vector describing the
 * velocity) before applying the move. The movement could also be created
 * from a starting point and a destination point (but that could result in
 * a &quot;teleporting&quot;). Before changing the position MoveTask
 * always checks if the new coordinates is out of bounds. If so, it will
 * recalculate the position accordingly.
 */
class MoveTask extends Task {

	/**
	 * The new velocity to set before the move task begins the move.
	 * If no velocity should be set this is null.
	 */
	private Vector newVelocity;

	/**
	 * Destination for a teleportation that should be done without
	 * changing the velocity.
	 */
	private Point destination;

	/**
	 * The relative speed of the movement.
	 */
	private float relativeSpeed;

	/**
	 * Creates a MoveTask with the given velocity to set before
	 * moving and the factor to multiply with the speed.
	 *
	 * @param newVelocity the new velocity to be set before moving.
	 * @param relativeSpeed the relative speed of the movement.
	 */
	public MoveTask(Vector newVelocity, float relativeSpeed) {
		this.newVelocity = newVelocity;
		this.relativeSpeed = relativeSpeed;
	}

	/**
	 * Creates a MoveTask with the given factor to multiply with the speed.
	 * The task makes no changes to the velocity.
	 *
	 * @param relativeSpeed the relative speed of the movement.
	 */
	public MoveTask(float relativeSpeed) {
		this.relativeSpeed = relativeSpeed;

	}

	/**
	 * Creates a MoveTask with a given destination. Does not change the velocity.
	 * The movement is done to the given destination, and is not dependent on
	 * the velocity of the owner of the task, which isn't changed. NOTE: In
	 * most cases this type of MoveTask would act as an instant teleportation,
	 * and shouldn't be used for objecs that can have a velocity.
	 *
	 * @param destination the GameObject to change the position to.
	 */
	public MoveTask(Point destination) {
		this.destination = destination;
	}

	/**
	 * Moves the position depending on how the task was created.
	 * Either a teleport or an update with the right velocity.
	 * If a new velocity was specified the velocity is set once
	 * before the task executes the first time.
	 * Returns true if the move was successful, or false if the
	 * given position was out of bounds.
	 *
	 * @return true if successful, false if out of bounds.
	 */
	public boolean execute() throws GeneralTaskException {
		boolean successful = true;

		// One cannot execute a task that is already done.
		if (isDone()) {
			throw new GeneralTaskException(
					"This task is done and therefore cannot be executed again."
					);
		}
		// Change the velocity if we have a new one to set. Only change
		// the velocity once per MoveTask
		if (newVelocity != null) {
			velocity.setVector(newVelocity);
			newVelocity = null;
		}

		if (destination != null) {
			// Teleport to a destination
			position.setPoint(destination);
			done = true;
			
		} else {
			Point point = position.getPoint();
			Vector oldVector = velocity.getVector();

			// Creates a new vector with speed corrected by the relativeSpeed
			Vector vector = new Vector(oldVector.getDirection(),
					oldVector.getMagnitude() * relativeSpeed);

			// Update the position with the velocity
			try {
				position.setPoint(new Point(point, vector));
			} catch (IllegalArgumentException iae) {
				position.setPoint(new FailSafePoint(point, vector));
				successful = false;
			}
		}

		return successful;
	}

}
