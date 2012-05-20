/* 2003-05-04 Martin
 *	Completed the task with correct inheritance from GetResourceTask.
 *
 * 2003-04-15 Martin
 *	Formatted the code.
 *
 * 2003-04-11 EliasAE
 * 	Added execute and setPrivateData to make it compile due to
 * 	changes in Task.
 */
package gameengine.tasksystem;
import gameengine.Environment;
import gameengine.gameobjects.SourceNode;

/**
 * The GetFoodTask is created when the owner of the task has a need of
 * food. The task consists of a movement to the given SourceNode, and then
 * a repeated gathering of resources at the node and the consumption of
 * those resources gathered. The task will continue until the need of food
 * is completely fulfilled. During the gathering the owner of the task will
 * &quot;wobble&quot; around the SourceNode. The gathering is performed in a speed
 * calculated from the owner of the task's gather skill and a constant
 * (getGatherSpeed). The task is done when the food need is fulfilled. If
 * the natural resources gets depleted the task unregisters the SourceNode
 * from the world's lists of food sources, and the task is also set to done.
 */
public class GetFoodTask extends GetResourceTask {

	///////////////////////////////////////
	// operations

	/**
	 * Constructs a GetFoodTask with a given destination SourceNode
	 *
	 * @param destination the SourceNode to get the food at.
	 */
	public  GetFoodTask(SourceNode destination) {
		super(destination, Environment.FOOD);

	}
}
