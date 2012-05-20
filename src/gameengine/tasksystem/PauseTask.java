/*
 * 2003-05-13 henko
 *  Created and implemented.
 */
package gameengine.tasksystem;

import gameengine.World;

/**
 * A PauseTask will make the Turtle stand where he is for a number of seconds
 * defined by getPauseDuration().
 */
public class PauseTask extends Task {
	
	/**
	 * The time when the task was first executed.
	 */
	private float startTime;
	
	/**
	 * Executes the Task. Will make the task's owner do nothing for a number
	 * of seconds, defined by getPauseDuration().
	 * 
	 * @return always returns true.
	 */
	public boolean execute() throws GeneralTaskException {
		
		// Start counting
		if (startTime == 0f) {
			startTime = World.getWorld().getWorldTime();
		
		// Has the time elapsed yet?
		} else {
			done = World.getWorld().getWorldTime() - startTime >
					World.getEnvironment().getPauseDuration();
		}
		
		return (true);
	}

}
