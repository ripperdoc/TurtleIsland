/*
 * 2003-05-10 henko
 * Created.
 */

package gameengine.gameobjects;

import gameengine.tasksystem.Task;

/**
 * TaskContainer represents an object that is capable of handeling tasks. It
 * contains functions for setting the current commanded task.
 */
public interface TaskContainer {

	/**
	 * Sets the object's current commanded task to the given one.
	 *
	 * @param task the commanded task to use.
	 */
	public void setCommandedTask(Task task);

}