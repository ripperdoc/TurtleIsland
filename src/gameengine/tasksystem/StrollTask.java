/* 2003-05-04 Martin
 *	Completed the class.
 */
package gameengine.tasksystem;
import java.util.*;
import gameengine.gameobjects.GameObject;

/**
 * A "shell" class for MoveToTask which is only used to be easy
 * identified as a task that was made due to Individual.needcheck()
 * created a task to get the Indivudal to stroll.
 */
public class StrollTask extends MoveToTask {

	///////////////////////////////////////
	// operations


	/**
	 * Constructs a StrollTask with the given destination.
	 */
	public  StrollTask(GameObject destination) {
		super (destination);
	}
}