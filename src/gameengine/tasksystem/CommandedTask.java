/*
 * 2003-05-13 henko
 *   All commanded tasks now pause for a few seconds after being finished.
 * 
 * 2003-05-13 henko
 *   Uppdated to prevent ObjectDeletedException.
 * 
 * 2003-05-13 henko
 *  Implemented. Works as superclass for all Commanded*Task.
 *  Makes sure all commanded turtles eat properly while at work.
 */
package gameengine.tasksystem;

import gameengine.Environment;
import gameengine.gameobjects.Node;

/**
 * CommandedTask is a task that is ordered by the user. It contains code that is
 * shared between all commanded tasks, such as figuring out if the task's owner
 * is hungry or depressed.
 * 
 * The CommandedGatherTask requires that the setPrivateInformation() is
 * used before execution.
 */
public abstract class CommandedTask extends Task {
	
	/**
	 * The pausetask to execute when finished with normal duties.
	 */
	private Task pauseTask;
	
	/**
	 * Represents whether we've reached the pause state of the task yet.
	 */
	private boolean paused;
	
	/**
	 * Represents whether the task's owner is hungry or not.
	 */
	protected boolean hungry;

	/**
	 * Represents whether the task's owner is depressed or not.
	 */	
	protected boolean depressed;

	public CommandedTask() {
		pauseTask = new PauseTask();
		paused = false;
	}

	/**
	 * Executes the Task. If the Task contains sub tasks, it will call exeute()
	 * on those as well. If the Task is unsucessful (which sometimes is
	 * important to see) the method returns false, else it will return true.
	 * 
	 * NOTE! This function is a template method and uses the method executeTask()
	 * to do the actual work. executeTask() is implemented differently in the
	 * different subclasses.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	public final boolean execute() throws GeneralTaskException {
		
		// Don't execute if already done.
		if (isDone()) {
			throw new GeneralTaskException(
					"This task is done and therefore cannot be executed again.");
		}
		
		// Am I hungry or depressed?
		if (!hungry) {
			hungry = characteristics.isHungry();
		}
		if (!depressed) {
			depressed = characteristics.isDepressed();
		}
		
		boolean success;
		
		if (!paused) {
			// Run the actual execute
			success = executeTask();
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

	/**
	 * Executes the Task. If the Task contains sub tasks, it will call exeute()
	 * on those as well. If the Task is unsucessful (which sometimes is
	 * important to see) the method returns false, else it will return true.
	 * 
	 * NOTE! This function is a hook method and is used by the execute() method.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	protected abstract boolean executeTask() throws GeneralTaskException;

	/**
	 * This routine will make sure that the turtle eats food if it's hungry
	 * and gets happiness if depressed.
	 * 
	 * @param currentNode the node that the turtle currently is at.
	 */
	protected void EatAndGetHappy(Node currentNode) {
		
		// Only proceed if the current node isn't deleted
		if (!currentNode.isDeleted()) {
			
			// As long as we are hungry
			while (hungry) {
						
				// Pick up some food
				TransferTask foodTransfer = currentNode.getPickUpTask(resources,
						Environment.FOOD, 1);
				try{foodTransfer.execute();
				} catch (GeneralTaskException gte) {}
				if (foodTransfer.isDone()) {
					// And eat it
					ConsumeTask eatFood = new ConsumeTask(resources,
							characteristics, Environment.FOOD);
					try{eatFood.execute();
					} catch (GeneralTaskException gte) {}
				} else {
					// No food available
					break;
				}
						
				// Still hungry?
				hungry = (characteristics.getSaturation() < 1);
			}
	
			// As long as we are depressed
			while (depressed) {
						
				// Pick up some happiness
				TransferTask getHappy = currentNode.getPickUpTask(
						resources, Environment.HAPPINESS, 1);
				try{getHappy.execute();
				} catch (GeneralTaskException gte) {}
				if (getHappy.isDone()) {
					// And consume it
					ConsumeTask beHappy = new ConsumeTask(resources,
							characteristics, Environment.HAPPINESS);
					try{beHappy.execute();
					} catch (GeneralTaskException gte) {}
				} else {
					// No happiness available
					break;
				}
						
				// Still hungry?
				depressed = (characteristics.getHappiness() < 1);
			}
			
		}
		
	}

}
