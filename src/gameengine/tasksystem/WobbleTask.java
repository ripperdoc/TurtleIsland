/* 2003-05-09 henko
 * 	Changed ResourceContainer to Resources.
 *
 * 2004-05-02 Martin
 *	Made changes in the code that creates new MoveTasks. There is now
 *	a big TODO problem down there.
 *
 * 2003-04-30 Martin
 *	Completed the class.
 *
 * 2003-04-15 Martin
 *	Corrected the constructor.
 *
 * 2003-04-11 EliasAE
 * 	Added execute and setPrivateData to make it compile due to
 * 	changes in Task.
 */
package gameengine.tasksystem;

import gameengine.Point;
import gameengine.Vector;
import gameengine.World;
import gameengine.gameobjects.GameObject;

/**
 * The WobbleTask makes the owner of the task randomly move around a given
 * object continuously with a speed calculated from the specified relative
 * speed and the normal speed of movement. The movement is random but
 * inscribed in a circle around the given objects position. Is never done
 * and always sucessful.
 */
public class WobbleTask extends Task {

	/**
	 * The object to wobble around, the center point.
	 */
	private GameObject centerObject;

	/**
	 * A procentual value of the speed of the wobbling moves, relative to
	 * the normal move speed.
	 */
	private float relativeSpeed;

	/**
	 * The radius of the wobbling, i.e. the maximum distance from the center
	 * the wobbling moves can take the indivudal.
	 */
	private float radius;

	/**
	 * Constructs a WobbleTask with access to he position and velocity of the
	 * owner of the task, and with specifications about which object to wobble
	 * around, in which speed and in what radius from the object to allow
	 * wobble movement.
	 *
	 * @param centerObject the GameObject to wobble, move, around.
	 * @param relativeSpeed the relative speed to set the movements around the
	 * object. Varies with skill level, for example.
	 * @param radius the radius around the centerObject, i.e. the maxium
	 * distance from the centerObject that is allowed in the wobble movement.
	 */
	public WobbleTask(GameObject centerObject, float relativeSpeed, float radius) {
		this.centerObject = centerObject;
		this.relativeSpeed = relativeSpeed;
		this.radius = radius;
	}

	/**
	 * Executes the Task. If the Task contains sub tasks, it will call exeute()
	 * on those as well. If the Task is unsucessful (which sometimes is
	 * important to see) the method returns false, else it will return true.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	public boolean execute() throws GeneralTaskException {
		// Is never done, therefore no check for exception

		// Creates the move task and executes it

		/*
		TODO There is a problem here for low FPS. This code beneath is corrected
			 so that the program will not hang up, but it also makes it possible
			 for the turtle to, quite often, walk outside the radius and then walk
			 back the next cycle. When FPS are very low the turtle would go very far
			 from the center object and then back again, like a jojo.
			 A possible other solution would be to check if the movement would be
			 outside the radius, and then in some way shorten or replace the turtle
			 so that it stays at the maximum radius, alternatively even bounces on the
			 edge of the circle that is defined by the radius. A low FPS would still
			 make strange results, though.
			
			 Another problem is that if the could should be rewritten to test the
			 the position before setting it, it would force the relative speed to
			 to be calculated with the old speed twice, once here to check the
			 position and once in the MoveTask.
		*/

		Vector vector = velocity.getVector();
		Point point = position.getPoint();
		float direction = vector.getDirection();

		// Checks if the individual has wobbled outside the radius
		if (centerObject.getPoint().distanceTo(point) > radius) {
			// Corrects the angle to point directly to the centerObject
			direction = point.angleTo(centerObject.getPoint());
		}
		vector = new Vector(randomizeDirection(direction),vector.getMagnitude());

		MoveTask moveTask = new MoveTask(vector, relativeSpeed);
		moveTask.setPrivateInformation(position, velocity, characteristics, resources);
		moveTask.execute();

		return true;
	}

	/**
	 * Randomizes a direction by adding a randomized turn angle (positive
	 * or negative) to the given direction and returns it.
	 *
	 * @param oldDirection the direction to be randomized.
	 * @return a float representing the new direction.
	 */
	private float randomizeDirection(float oldDirection) {
			// Randomizes the angle of the turn
			float turnAngle = (float)Math.random() *
					World.getEnvironment().getMaxWobbleTurning();
			if(Math.random() < 0.5) {
				turnAngle = turnAngle * -1f;
			}
			float newAngle = oldDirection + turnAngle;
			if (newAngle < 0) {
				newAngle += 2 * Math.PI;
			}
			return newAngle;
	}
}
