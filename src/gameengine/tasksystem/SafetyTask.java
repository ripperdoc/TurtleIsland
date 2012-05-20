/* 2003-05-11 Martin
 *	Corrected the naming of an exception.
 *
 * 2003-05-04 Martin
 *	Completed the class.
 */
package gameengine.tasksystem;

import gameengine.World;
import java.util.*;

/**
 * The individual moves straight to the light pillar, but the movement is
 * aborted when the individuals safety is high enough.
 * The task requires that the setPrivateInformation() is used.
 */
public class SafetyTask extends MoveToTask {

	///////////////////////////////////////
	// operations


	/**
	 * Constructs a SafetyTask with the light pillar as the destination.
	 */
	public  SafetyTask() {
		super (World.getWorld().getLightPillar());
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
					"This task is done and therefore cannot be executed again.");
		}

		if(characteristics.isUnsafe()) {
			super.execute();
		}
		else {
			done = true;
		}
		// Always successful
		return true;
	}


}