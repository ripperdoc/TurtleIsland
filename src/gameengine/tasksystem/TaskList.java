/* 
 * 2003-05-12 henko
 *  Made TaskList implement Serializable.
 * 
 * 2003-05-11 Martin
 *	Made corrections so that the socializeTask will be correctly removed.
 *
 * 2003-05-09 Martin
 *	Corrected the addTask() again.
 *
 * 2003-05-05 Martin
 *	Correcteed the addTask() a little.
 *
 * 2003-05-04 Martin
 *	Formatted and completed the code.
 *	TODO The code could still be fixed a bit and the handling of prioritites
 *	must be looked over later.
 */
package gameengine.tasksystem;

import java.io.Serializable;
import java.util.*;

/**
 * A TaskList can contain any number of tasks.
 */
public class TaskList implements  Serializable {

	/**
	 * The ints that sets the priority and identifes the different independent
	 * tasks.
	 * TODO Maybe a temporary solution. Constants should be elsewhere?
	 */
	public static final int FOOD_PRIORITY = 0;
	public static final int HAPPINESS_PRIORITY = 1;
	public static final int SAFETY_PRIORITY = 2;
	public static final int STROLL_PRIORITY = 3;
	public static final int SOCIALIZE_PRIORITY = 4;

	/**
	 * The array that holds the different tasks that the individual can use.
	 */
	private Task[] taskList;

	/**
	 * Constructs a TaskList by initializing the list and it's variables.
	 */
	public TaskList() {
		taskList = new Task[5];
	}

	/**
	 * Clears the list of tasks. Is primarily called when a
	 * commanded task is added to an individual.
	 */
	public void removeAllTasks() {
		removeSocialize();
		for(int i=0;i<5;i++){
			taskList[i] = null;
		}
	}

	/**
	 * Executes the first task in the list that's available. Because they are
	 * already ordered by priority, the execute doesn't need to care about priority.
	 * If the task is finished, the execute() method will remove it from the list. If the
	 * finished task is a SafetyTask, the list will also be cleared, to prevent
	 * &quot;loops&quot; when an individual could get stuck in trying to stroll to a
	 * point on the map that's too unsafe.
	 */
	public void execute() throws GeneralTaskException {
		// Loops all tasks
		for(int i = 0; i < 5; i++) {
			// Execute the task if it exist
			if(taskList[i] != null) {
				try {
					taskList[i].execute();
				}
				catch (GeneralTaskException gte) {
					System.err.println("TaskList: GeneralTaskException");
				}
				// If the task is done after execution, remove it.
				// It the task was a SafetyTask, clear all tasks
				if(taskList[i].isDone() && taskList[i] instanceof SafetyTask) {
					removeAllTasks();
				}
				else if(taskList[i].isDone()) {
					taskList[i] = null;
				}
				// One task has been executed, break and don't execute more
				break;
			}
		}
	}

	/**
	 * Adds a new task to the list. The task is added to the appropriate place
	 * according to it's priority. A food task will for example always be
	 * placed first in the list. The addTask also sets the boolean flags that
	 * states that a task of a certain type is present in the list.
	 *
	 * @param task the Task to be added.
	 */
	public void addTask(Task task) {
		// Removes the socialize task. This requires that a check is made
		// before this call (in Individual) that there are SocializeTask
		// if a new one is added, otherwise removeSocialize() will delete
		// the current, valid SocializeTask.
        removeSocialize();

        // The priorities of the tasks are the following (where the number
        // represents the index in the list)
        // 0 - food, 1 - happiness, 2 - safety, 3 - stroll, 4 - socialize
        if(!hasFoodTask() && task instanceof GetFoodTask) {
			taskList[FOOD_PRIORITY] = task;
		}
		else if(!hasHappinessTask() && task instanceof GetHappinessTask) {
			taskList[HAPPINESS_PRIORITY] = task;
		}
		else if(!hasSafetyTask() && task instanceof SafetyTask) {
			taskList[SAFETY_PRIORITY] = task;
		}
		else if(!hasStrollTask() && task instanceof StrollTask) {
			taskList[STROLL_PRIORITY] = task;
		}
		else if(!hasSocializeTask() && task instanceof SocializeTask) {
			taskList[SOCIALIZE_PRIORITY] = task;
		}
	}

	/**
	 * A private method for removing the SocializeTask as a response
	 * to getting any other task.
	 */
	private void removeSocialize() {
		if(hasSocializeTask()) {
			((SocializeTask)taskList[SOCIALIZE_PRIORITY]).removeFromGroup();
			taskList[SOCIALIZE_PRIORITY] = null;
		}
	}


	/**
	 * Returns a boolean stating if there is a food task in the list.
	 *
	 * @return a boolean stating if there is a food task in the list.
	 */
	public boolean hasFoodTask() {
		return (taskList[FOOD_PRIORITY] != null);
	}

	/**
	 * Returns a boolean stating if there is a hapiness task in the list.
	 *
	 * @return a boolean stating if there is a hapiness task in the list.
	 */
	public boolean hasHappinessTask() {
		return (taskList[HAPPINESS_PRIORITY] != null);
	}

	/**
	 * Returns a boolean stating if there is a safety task in the list.
	 *
	 * @return a boolean stating if there is a safety task in the list.
	 */
	public boolean hasSafetyTask() {
		return (taskList[SAFETY_PRIORITY] != null);
	}

	/**
	 * Returns a boolean stating if there is a stroll task in the list.
	 *
	 * @return a boolean stating if there is a stroll task in the list.
	 */
	public boolean hasStrollTask() {
        return (taskList[STROLL_PRIORITY] != null);
	}

	/**
	 * Returns a boolean stating if there is a socialize task in the list.
	 *
	 * @return a boolean stating if there is a socialize task in the list.
	 */
    public boolean hasSocializeTask() {
		return (taskList[SOCIALIZE_PRIORITY] != null);
	}

}
