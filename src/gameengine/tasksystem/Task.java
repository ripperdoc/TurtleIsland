/* 2003-05-10 Martin
 *	Added the method reset(), because it is unfortunately needed in some tasks.
 *
 * 2003-04-11 henko
 * 	Implemented setPrivateInformation (it should be implemented),
 * 	created the local attribute fields and implemented Serializable.
 *
 * 2003-04-11 EliasAE
 * 	Made the class more or less finished. Made execute and
 * 	setPrivateInformation abstract.
 */
package gameengine.tasksystem;

import gameengine.gameobjects.Characteristics;
import gameengine.gameobjects.Position;
import gameengine.gameobjects.Resources;
import gameengine.gameobjects.Velocity;
import gameengine.tasksystem.GeneralTaskException;
import java.io.Serializable;

/**
 * An abstract class defining tasks, for example more or less complex
 * actions that an individual can perform. A task can be executed, and
 * if sucessful (when applicable) it will return true. A task can be
 * given all the attributes of game objects that it needs.
 */
public abstract class Task implements Serializable {


	/**
	 * The owner's position attribute.
	 */
	protected Position position;

	/**
	 * The owner's velocity attribute.
	 */
	protected Velocity velocity;


	/**
	 * The owner's characteristics attribute.
	 */
	protected Characteristics characteristics;

	/**
	 * The owner's resource container attribute.
	 */
	protected Resources resources;

	/**
	 * A boolean that states if the Task is done, that is has performed
	 * everything it should.
	 */
	protected boolean done;

	/**
	 * Returns a booelan stating if the Task is done.
	 *
	 * @return true if the task is finished, otherwise false.
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * Resets the done variable. I used to "recycle" a task.
	 */
	public void reset() {
		done = false;;
	}

	/**
	 * Executes the Task. If the Task contains sub tasks, it will call
	 * exeute() on those as well. If the Task is unsucessful (which
	 * sometimes is important to see) the method returns false, else
	 * it will return true.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	public abstract boolean execute() throws GeneralTaskException;

	/**
	 * Sends all the attributes of the task's owner to the Task. That
	 * gives the Task all the information it needs.
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
		this.position = position;
		this.velocity = velocity;
		this.characteristics = characteristics;
		this.resources = resources;
	}

}
