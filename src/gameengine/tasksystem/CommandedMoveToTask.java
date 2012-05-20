/*
 * 2003-05-13 henko
 *  Created and implemented.
 *  CommandedMoveToTask is a MoveToTask followed by a PauseTask.
 */
package gameengine.tasksystem;

import gameengine.gameobjects.GameObject;


/**
 * Represents a MoveToTask created by the user. Will automatically pause a few 
 * seconds after finishing moving, using a PauseTask.
 */
public class CommandedMoveToTask extends MoveToTask {
	
	/**
	 * The pausetask to execute when finished with normal duties.
	 */
	private Task pauseTask;
	
	/**
	 * Represents whether we've reached the pause state of the task yet.
	 */
	private boolean paused;
	
	/**
	 * Constructs a CommandedMoveToTask with a specified destination.
	 *
	 * @param destination the GameObject to move to.
	 */
	public CommandedMoveToTask(GameObject destination) {
		super(destination);
		pauseTask = new PauseTask();
	}

	/**
	 * Constructs a CommandedMoveToTask with a specified destination and relative speed
	 * of the movement.
	 *
	 * @param relativeSpeed the relative speed of the movement.
	 * @param destination the GameObject to move to.
	 */
	public CommandedMoveToTask(GameObject destination, float relativeSpeed) {
		super(destination, relativeSpeed);
		pauseTask = new PauseTask();
	}
	
	/**
	 * Executes the Task. If the Task contains sub tasks, it will call exeute()
	 * on those as well. If the Task is unsucessful (which sometimes is
	 * important to see) the method returns false, else it will return true.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	public boolean execute() throws GeneralTaskException {
		boolean success;
		
		if (!paused) {
			// Run the actual execute
			success = super.execute();
			if (done) {
				done = false;
				paused = true;
			}
			
		} else {
			// Wait before running away
			success = true;
			pauseTask.execute();
			done = pauseTask.isDone();
		}
		
		return success;
 	}
}
