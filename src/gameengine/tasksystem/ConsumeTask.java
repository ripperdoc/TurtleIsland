/* 2003-05-11 Martin
 *	Corrected the naming of an exception.
 *
 * 2003-05-09 henko
 *  Changed ResourceContainer to Resources.
 *
 * 2003-05-03 Martin
 *	Completed the class
 *
 * 2003-04-30 Martin
 *	Formatted.
 *
 * 2003-04-11 EliasAE
 * 	Added execute and setPrivateData to make it compile due to
 * 	changes in Task.
 */
package gameengine.tasksystem;

import gameengine.gameobjects.Position;
import gameengine.gameobjects.Velocity;
import gameengine.tasksystem.GeneralTaskException;
import gameengine.gameobjects.Characteristics;
import gameengine.gameobjects.Resources;
import gameengine.Environment;
import java.util.*;

/**
 * The ConsumeTask is instantly done. It uses one unit from the owner's
 * carried resources and consumes it, thereby deleting one of the carried
 * units and adding the need value, specified by the constant
 * getUnitNeedRatio(). If no unit was available, the task is set to
 * unsucessful.
 */
public class ConsumeTask extends Task {

	///////////////////////////////////////
	// operations

	/**
	 * The owner's characteristics attribute.
	 */
	private Characteristics characteristics;

	/**
	 * The owner's resource container attribute.
	 */
	private Resources resources;

	/*
	 * An int describing the resource type.
	 */
	private int resourceType;


	/**
	 * Constructs a ConsumeTask with access to the task's owner's
	 * Resources and Characteristics. The task will consume one unit of
	 * the specified resource type.
	 *
	 * @param resourceType an int describing the resource type.
	 */
	public  ConsumeTask(Resources resources,
			Characteristics characteristics, int resourceType) {
		this.resources = resources;
		this.characteristics = characteristics;
		this.resourceType = resourceType;
	}

	/**
	 * Executes the Task. If the Task contains sub tasks, it will call exeute()
	 * on those as well. If the Task is unsucessful (which sometimes is
	 * important to see) the method returns false, else it will return true.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	public boolean execute() throws GeneralTaskException {
		// If already done
		if(done == true) {
			throw new GeneralTaskException("Already done");
		}

		if(resources.getResourceAmount(resourceType) > 0) {
			resources.removeResource(resourceType, 1);

			if(resourceType == Environment.FOOD) {
				characteristics.increaseSaturation();
			}
			else {
				characteristics.increaseHappiness();
			}

			// Successful
			return true;
		}
		else {
			return false;
		}
	}
}
