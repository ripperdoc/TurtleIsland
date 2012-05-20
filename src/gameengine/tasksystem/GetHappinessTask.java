/* 2003-05-04 Martin
 *	Completed the task with correct inheritance from GetResourceTask.
 *
 * 2003-04-30 Martin
 *	Formatted.
 *
 * 2003-04-11 EliasAE
 * 	Added execute and setPrivateData to make it compile due to
 * 	changes in Task.
 */
package gameengine.tasksystem;
import gameengine.Environment;
import gameengine.gameobjects.SourceNode;

/**
 * The GetHappinessTask is created when the owner of the task has a need of
 * happiness. The task consists of a movement to the given SourceNode, and
 * then a repeated gathering of resources at the node and the consumption
 * of those resources gathered. The task will continue until the need of
 * happiness is completely fulfilled. During the gathering the owner of the
 * task will &quot;wobble&quot; around the SourceNode. The gathering is performed in
 * a speed calculated from the owner of the task's gather skill and a
 * constant (getGatherSpeed). The task is done when the happiness need is
 * fulfilled. If the natural resources gets depleted the task unregisters
 * the SourceNode from the world's lists of happiness sources, and the task
 * is also set to done.
 */
public class GetHappinessTask extends GetResourceTask {

	///////////////////////////////////////
	// operations

	/**
	 * Constructs a GetHappinessTask with a given destination SourceNode
	 *
	 * @param destination the SourceNode to get the food at.
	 */
	public  GetHappinessTask(SourceNode destination) {
		super(destination, Environment.HAPPINESS);

	}
}