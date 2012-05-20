/*
 * 2003-05-13 henko
 *  The class now extends CommandedTask which takes care of making the Turtle
 *  eat while at work.
 * 
 * 2003-05-12 EliasAE
 * 	Pickup now work and support for deleted nodes.
 *
 * 2003-05-11 henko
 *  The resetting of the tasks before a new loop is started now works.
 *
 * 2003-05-09 henko
 *  Changed ResourceContainer to Resources.
 *
 * 2003-05-03 Martin
 *	Corrected an exception thrown from the constructor.
 *
 * 2003-05-02 Martin
 *	Completed the task, even though changes maybe should be made
 *	to the execute() method. TODO: The constructor now forces
 *	the MoveToTask to cas an exception when first used.
 *
 * 2003-04-30 EliasAE
 * 	Added commit(). Fixed version conflict.
 *
 * 2003-04-30 Martin
 *	Formatting.
 *
 * 2003-04-11 EliasAE
 * 	Added execute and setPrivateData to make it compile due to
 * 	changes in Task.
 */
package gameengine.tasksystem;

import gameengine.World;
import gameengine.gameobjects.Characteristics;
import gameengine.gameobjects.Node;
import gameengine.gameobjects.Position;
import gameengine.gameobjects.Resources;
import gameengine.gameobjects.Velocity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A CommandedTransportTask is a commanded task and is always ordered by
 * the user. The task itself can consist of a transport route of any
 * length, composed by Nodes (including SourceNodes and the
 * LightPillarNode) as way points and specifications about where to pick up
 * a specified resource. When the CommandedTransportTask is first created
 * it consists of only one Node, which is the starting point, and the task
 * will force it's owner to move to the Node. To make the owner of the task
 * to pick up a certain resource at the the last desdignated Node a
 * addPickUp() method call is needed with a specified resource type. New
 * way points can be added to the task by addNode(). The
 * CommandedTransportTask contains a list of MoveToTasks and TransferTasks
 * that makes up the transport route.
 * The CommandedTransportTask requires that the setPrivateInformation() is
 * used before execution. The task will never be set to done.
 */
public class CommandedTransportTask extends CommandedTask {

	/**
	 * A boolean that states if the task is commited or not, i.e. that
	 * it has been completed by the user (and therefore can be performed)
	 * continuously.
	 */
	private boolean commited;

	/**
	 * A list containing all orders in the transport task.
	 */
	private List transportOrders;

	/**
	 * The correspondending node for each task.
	 */
	private List transportNodes;
	
	/**
	 * The first node that is sent to the constructor. This is needed, because
	 * we cannot create a node until we have valid Private Information.
	 * This is equal the argument to the constructor until the 
	 * setPrivateInformation is called the first time, at which point it is
	 * set to null and never used again.
	 */
	private Node initializationNode;

	/**
	 * The current position in the list of transport orders.
	 */
	private int index;

	/**
	 * Constructs a CommandedTransportTask with a specified starting Node.
	 *
	 * @param node the Node to start the transportation at.
	 */
	public CommandedTransportTask(Node node) {
		super();
		transportOrders = new ArrayList();
		transportNodes = new ArrayList();
		index = 0;
		commited = false;

		initializationNode = node;
	}

	/**
	 * Adds a new waypoint to the transport route. If the addPickUp weren't
	 * called before this method, the transportation between the second last
	 * and the last node (added with this method) could be quite useless but
	 * sometimes needed (it's a transportation without any resources carried).
	 * The method adds a MoveToTask and then a TransferTask which empties the
	 * individuals resource container at the reached node.
	 *
	 * @param node the Node to be added to the transport route.
	 */
	public void addNode(Node node) {
		if (commited) {
			throw new IllegalStateException(
					"This task is commited and cannot be altered.");
		}
		// Creates a new MoveToTask with a relative speed given by the
		// transport efficiency of the owner of the task.
		Task transportMovement = new TransportMoveToTask(node);
		transportMovement.setPrivateInformation(position, velocity,
				characteristics, resources);
		transportOrders.add(transportMovement);
		transportNodes.add(node);
		transportOrders.add(node.getLeaveTask(resources));
		transportNodes.add(node);
	}

	/**
	 * Adds a &quot;pick up order&quot; to the individual, which forces it to pick up a
	 * certain resource at the last given way point (node).
	 *
	 * @param resourceType an int representing the resource type to pick up.
	 */
	public void addPickUp(int resourceType) {
		if (commited) {
			throw new IllegalStateException(
					"This task is commited and cannot be altered.");
		}
		if (transportNodes.size() == 0) {
			throw new IllegalStateException("No node was added before the pickup");
		}
		Node lastNode = (Node)
				transportNodes.get(transportNodes.size() - 1);
		Task pickUpTask = lastNode.getPickUpTask(
				resources, resourceType, 
				World.getEnvironment().getMaxUnitsCarried());
		transportOrders.add(pickUpTask);
		transportNodes.add(lastNode);
	}

	/**
	 * Commits the task. This signals that the task is finished, which means
	 * that the task is allowed to run continuously, not waiting at the last
	 * waypoint.
	 *
	 * @param commited a boolean stating if the task is commited (finished) or not.
	 */
	public void setCommited(boolean commited) {
		this.commited = commited;
	}

	/**
	 * Executes the Task. If the Task contains sub tasks, it will call exeute()
	 * on those as well. If the Task is unsucessful (which sometimes is
	 * important to see) the method returns false, else it will return true.
	 *
	 * @return a boolean stating if the Task was sucessful.
	 */
	protected boolean executeTask() throws GeneralTaskException {
				
		Task currentTask = (Task)transportOrders.get(index);

		// Only execute if the current task isn't done.
		if(!currentTask.isDone()) {

			// Execute our present task
			currentTask.execute();
			
			// If the current task was a MoveToTask and it is just finished
			// the indivudal must be at a node and can eat.
			// OR if we're currently working on a TransferTask, we must be at a
			// node. 
			if(((currentTask instanceof MoveToTask) && currentTask.isDone()) || 
					(currentTask instanceof TransferTask)) 
				{
				
				Node currentNode = (Node) transportNodes.get(index);

				// Eat and get happiness if neccecery
				EatAndGetHappy(currentNode);

			}
			
			// If the current task is finished, carry on with next task
			if (currentTask.isDone()) {
				increaseIndex();
			}

			// Increase the skill of the owner of the task
			characteristics.increaseTransportationSkill();
		}
		else {
			// The current task was done, so try increasing the index for the next
			// execution cycle.
			increaseIndex();
		}

		return true;
	}

	/**
	 * Increases the index of the list, but checks if the index has
	 * reached is maximum. If so, it will reset the index to 0 if
	 * the TransportTask is allowed to repeat (is commited). If not
	 * commited, it will not increase the index.
	 */
	private void increaseIndex() {
		if (transportOrders.size() == 0) {
			throw new IllegalStateException("No orders added yet.");
		}

		index++;
		// Unitl we finds a node that is not deleted.
		while (true) {
			// If the index has gone beyond the max index
			if(index >= (transportOrders.size())) {
				// If the TransportTask is commited, loop around the order list
				// and reset all done variables.
				if(commited) {
					index = 0;
					for(int i = 0; i < transportOrders.size(); i++) {
						((Task)transportOrders.get(i)).reset();
					}
				}
				// Else, set back the index 1 step, so that increaseIndex() didn't
				// change the index when done.
				else {
					index--;
				}
			}
			Node destination = (Node) transportNodes.get(index);

			// Normaly this will be true and we just return from the function.
			if (!destination.isDeleted()) {
				break;
			}

			// Now delete the task, because the node that i would have
			// operated on is deleted.
			transportOrders.remove(index);
			transportNodes.remove(index);

			// Do not change index, because we just deleted the element
			// at index and the element at the former index + 1 is now
			// at index. Repeat until a undeleted node is found or we 
			// wrap around due to end of list. 

			// However, we have to check
			// for that all nodes that we used to travel to has been
			// deleted in which case we set this task to done.
			if (transportOrders.size() == 0) {
				index = 0;
				done = true;
				break;
			}
		}
	}

	/**
	 * Sends all the attributes of the task's owner to the Task. That
	 * gives the Task all the information it needs. The methods also sends
	 * the appropriate information on to the sub tasks.
	 *
	 * @param position a Position attribute describing the position of the
	 * owner of the attribute.
	 * @param velocity a Velocity attribute describing the velocity of the
	 * owner of the attribute.
	 * @param characteristics a Characteristics attribute describing the
	 * characteristics of the owner of the attribute.
	 * @param resources a Resources describing the resources of the
	 * owner of the attribute.
	 */
	public void setPrivateInformation(
			Position position,
			Velocity velocity,
			Characteristics characteristics,
			Resources resources
			) {
		super.setPrivateInformation(position, velocity, characteristics,
				resources);
		Iterator iterator = transportOrders.iterator();
		while (iterator.hasNext()) {
			Task current = (Task) iterator.next();
			current.setPrivateInformation(
					position, velocity, characteristics, resources);
		}

		// Add the first node which could not be added in the constructor.
		if (initializationNode != null) {
			addNode(initializationNode);
			initializationNode = null;
		}
	}

	/**
	 * This class is a wrapper for MoveToTask and is only used by 
	 * CommandedTransportTask and has special setPrivateInformation() and
	 * reset() functions.
	 */
	class TransportMoveToTask extends MoveToTask {

		/**
		 * Constructs a MoveToTask with a specified destination.
		 *
		 * @param destination the GameObject to move to.
		 */
		public TransportMoveToTask(Node destination) {
			super(destination);
		}

		/* (non-Javadoc)
		 * @see gameengine.tasksystem.Task#setPrivateInformation(gameengine.gameobjects.Position, gameengine.gameobjects.Velocity, gameengine.gameobjects.Characteristics, gameengine.gameobjects.Resources)
		 * 
		 * Sets relativeSpeed to the turtle's current value.
		 */
		public void setPrivateInformation(
				Position position,
				Velocity velocity,
				Characteristics characteristics,
				Resources resources
				) {
			super.setPrivateInformation(position, velocity, characteristics, resources);
			if (characteristics != null) {
				relativeSpeed = characteristics.getTransportationEfficiency();
			}
		}

		/* (non-Javadoc)
		 * @see gameengine.tasksystem.Task#reset()
		 * 
		 * Updates relativeSpeed to the turtle's current value.
		 */
		public void reset() {
			super.reset();
			relativeSpeed = characteristics.getTransportationEfficiency();
		}

	}

}
