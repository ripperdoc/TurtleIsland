/* 2003-05-19 Martin
 *	Commented needcheck.
 *
 * 2003-05-13 Martin
 *	Made a small correction in the randomization in the Socialization decision,
 *	if the individual should start a new group or join an already existing one.
 *
 * 2003-05-12 Martin
 *	Moved the decrease of skills to the characteristics. Corrected so that children
 *	can't be commanded.
 *
 * 2003-05-11 EliasAE
 * 	Added support for deletable.
 *
 * 2003-05-10 henko
 *  The class now implements a bunch of new interfaces (the *Container
 *  interfaces).
 *
 * 2003-05-05 Martin
 *	Completed the shell of the needcheck. Commented some methods.
 *
 * 2003-05-04 Martin
 *	Implemented the taskList, but no independent tasks are made yet. Completed
 *	the killcheck.
 *
 * 2003-05-03 Martin
 *	Added the killcheck, wrote some code for recalculation.
 *
 * 2003-04-23 EliasAE
 * 	Created the characteristics and resources.
 *
 * 2003-04-15 Martin
 * Corrected the registration of the Individual in the constructor.
 *
 * 2003-04-13 Martin
 *	Corrected a hard-coded speed to be returned from Environment.
 *
 * 2003-04-12 henko
 *	Implemented support for a commanded task in update().
 *
 * 2003-04-12 pergamon
 *	Added the creation of a velocity in the constructor.
 *
 * 2003-04-11 Martin
 *	Formatted, commented and completed the "shell" of the class. Update still
 *	to be done.
 */
package gameengine.gameobjects;

import gameengine.*;
import gameengine.tasksystem.*;

/**
 * The most important game object, describing the individual members of the
 * game's civilization. It contains get methods for relevant, open
 * information, such as age or gender, and also methods for adding
 * commanded tasks. The individual has a variable for a commanded task, a
 * list of uncommanded task, and the attribute objects Characteristics,
 * Velocity, Position and Resources.
 * Is registered to the list of all objects when created and unregistered
 * when the needcheck has confirmed a death.
 */
public class Individual extends GameObject
		implements Updateable, VelocityContainer, ResourceContainer,
		CharacteristicsContainer, TaskContainer {

	/**
	 * The list of independent (not commanded) tasks.
	 */
	private TaskList taskList;

	/**
	 * The current commanded task.
	 */
	private Task commandedTask;

	/**
	 * The attribute of velocity for the individual.
	 */
	private Velocity velocity;

	/**
	 * The attribute of characteristics for the individual.
	 */
	private Characteristics characteristics;

	/**
	 * The attribute of having Resources for the individual.
	 */
	private Resources resources;

	/**
	 * The randomizer for the killcheck.
	 */
	private Randomizer killRandomizer;

	/**
	 * Constructs an adult Individual at a specified position. With the specified
	 * gender. (The age is set to 18). It also registers the Individual at the World.
	 * Is only to be used when creating a game.
	 *
	 * @param point the position to create the Individual at.
	 * @param gender the gender of the Individual.
	 */
	public Individual(Point point, int gender) {
		super(point);

		// Gives the turtle a Velocity with zero angle and the default speed
		velocity = new Velocity(0.0f,World.getEnvironment().getIndividualSpeed());

		characteristics = new Characteristics(gender, 18);
		resources = new Resources();
		String[] names = Environment.INDIVIDUAL_NAMES[gender];
		super.setName(names[(int)(Math.random()*names.length)]);

		// TODO Take value from Environment.
		killRandomizer = new ProbabilityRandomizer(0.01f);
		taskList = new TaskList();

		World.getWorld().register(this);

		setDescription("A turtle, which needs food and likes to smell the " + 
				"flowers. It can also collect gold to sacrifice and " + 
				"transport things."
				);
	}

	/**
	 * Constructs an Individual from two parents, i.e. a new indivudal is born.
	 * Also registers the Individual at the World.
	 *
	 * @param point the position to create the Individual at.
	 * @param firstParent the first parent.
	 * @param secondParent the second parent.
	 */
	public Individual(Point point, Individual firstParent,
			Individual secondParent) {
		super(point);

		// Gives the turtle a Velocity with zero angle and the default speed
		velocity = new Velocity(0.0f,World.getEnvironment().getIndividualSpeed());

		characteristics = new Characteristics(firstParent.characteristics,
				secondParent.characteristics);
		resources = new Resources();
		String[] names = Environment.INDIVIDUAL_NAMES[getGender()];
		super.setName(names[(int)(Math.random()*names.length)]);

		taskList = new TaskList();
		// TODO Take value from Environment.
		killRandomizer = new ProbabilityRandomizer(World.getEnvironment().getMinimumNeed());

		World.getWorld().register(this);
		System.out.println(name + " was born");
	}

	/**
	 * Updates the Individual by recalculating all values, stored in the
	 * Individual's attribute objects (Position, Velocity, Characteristics and
	 * Resources) and then executing the current task, by calling
	 * execute() from the Individuals TaskList or from the current commanded
	 * task.
	 */
	public void update() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		recalculate();
		if(!killcheck()) {
			// Perform a task
			if (commandedTask != null) {
				// Execute the commanded task
				try {
					commandedTask.execute();
				} catch (GeneralTaskException gte) {
					System.err.println("Individual: GeneralTaskException");
				}
				// Remove the commanded task if it's done
				if (commandedTask.isDone()) {
					commandedTask = null;
				}
			} else {
				needcheck();
				try {
					taskList.execute();
				}
				catch (GeneralTaskException gte) {
					System.err.println("Individual: GeneralTaskException");
				}
			}
		}
	}

	/**
	 * Checks the needs and creates new tasks as a response. It goes through
	 * all needs in the prioritized order (food, happiness, safety) and then stroll
	 * and socializing. If there doesn't exist a food task and the individual is
	 * hungry, then a GetFoodTask is created and the method is exited, if not
	 * it will check the happiness, and so on.
	 *
	 * If the check didn't found a "need" for food, happiness, safety or a stroll
	 * it will always create a SocializeTask, on the condition that the indivudual
	 * doesn't already socialize. When a new socialize task is created, a random
	 * factor decides if the individual wants to start its own group or join an
	 * existing.
	 *
	 * Always when the needcheck tries to find a destination for a new task it will
	 * search for the closest one.
	 */
	private void needcheck() {
		// Get the lists, they might come in handy
		SourceNodeList foodSources = World.getWorld().getFoodSources();
		SourceNodeList happinessSources = World.getWorld().getHappinessSources();
		SocializeGroupList socializeGroups = World.getWorld().getSocializeGroups();

		// Check if there is a need for an independent task, given that there doesn't
		// already exisst such task and given that there exist destinations for the
		// tasks (e.g. SourceNodes).
		if(characteristics.isHungry() && !taskList.hasFoodTask() &&
				!foodSources.isEmpty()) {
			// Create the best destination by sorting the food list by how close to
			// the individual the food sources are.
			SourceNode destination =
					(SourceNode)foodSources.getBestItem(
					new ClosestToComparator(super.getPoint()));
			GetFoodTask getFood = new GetFoodTask(destination);
			getFood.setPrivateInformation(position, velocity,
					characteristics, resources);
			taskList.addTask(getFood);
		}
		else if(characteristics.isDepressed() && !taskList.hasHappinessTask() &&
				!happinessSources.isEmpty()) {
			// Create the best destination by sorting the happiness list by how close to
			// the individual the happiness sources are.
			SourceNode destination = (
					SourceNode)happinessSources.getBestItem(
					new ClosestToComparator(super.getPoint()));
			GetHappinessTask getHappiness = new GetHappinessTask(destination);
			getHappiness.setPrivateInformation(position, velocity,
					characteristics, resources);
			taskList.addTask(getHappiness);
		}
		else if(characteristics.isUnsafe() && !taskList.hasSafetyTask()) {
			// Creates a safetytask that will make the indivudal start moving
			// to the Light Pillar
			SafetyTask safetyTask = new SafetyTask();
			safetyTask.setPrivateInformation(position, velocity,
						characteristics, resources);
			taskList.addTask(safetyTask);
		}
		else if(characteristics.isStrolly() && !taskList.hasStrollTask()) {
			// Creates a StrollTask with a random point as destination
			// (new Point() returns a Point with random coordinates)
			StrollTask strollTask = new StrollTask(new SpecialPoint(new Point()));
			strollTask.setPrivateInformation(position, velocity,
									characteristics, resources);
			taskList.addTask(strollTask);
		}
		else if(!taskList.hasSocializeTask()) {
			// Checks if the indivudal wants to start a own group
			// or if there are no other groups.
			boolean socializeTaskCreated = false;
			if (Math.random() < World.getEnvironment().getJoinSocializationChance() &&
					!socializeGroups.isEmpty()) {
				SocializeGroup destinationGroup =
					(SocializeGroup)socializeGroups.getBestItem(
					new ClosestToComparator(super.getPoint()));

				if (!destinationGroup.isDeleted()) {
					SocializeTask socializeTask = new SocializeTask(destinationGroup, this);
					socializeTask.setPrivateInformation(position, velocity,
							characteristics, resources);
					taskList.addTask(socializeTask);
					socializeTaskCreated = true;
				}
			}
			if(!socializeTaskCreated) {
				SocializeTask socializeTask = new SocializeTask(
						new SocializeGroup(super.getPoint()),
						this);
				socializeTask.setPrivateInformation(position, velocity,
						characteristics, resources);
				taskList.addTask(socializeTask);
			}
		}
	}

	/**
	 * Recalculates all important values in the characteristics. The saturation
	 * and the happiness are decreased and the safety and health is recalculated.
	 * The skills which aren't used, if the individual is commanded, is also decreased.
	 */
	private void recalculate() {
		characteristics.decreaseSaturation();
		characteristics.decreaseHappiness();
		characteristics.setSafety(super.getPoint());
		characteristics.recalculateHealth();

		// The decrease of skills is now moved to Characteristics.
	}

	/**
	 * Checks if the current needs are to low and thereby killing the individual.
	 * If so, the killcheck will unregister the individual appropriately and return
	 * true, if not, it will return false.
	 *
	 * @return a boolean stating if the individual has died
	 */
	private boolean killcheck() {
		float minNeed = World.getEnvironment().getMinimumNeed();
		// Makes it more probably do to die below defaultNeedValue and
		// less likely over.
		float probabilityModifier =
				(World.getEnvironment().getDeathProbabilityOffset() -
				getHealth()) / getHealth();
		if(
				getSaturation() < minNeed ||
				getHappiness() < minNeed ||
				getSafety() < minNeed ||
				killRandomizer.isSuccessful(probabilityModifier)
				) {
			System.out.println(name + " has died at the age of " + getAge() + "!");
			delete();
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Sets the commanded task for the individual. Is callen by the Input
	 * package. The method clears the TaskList.
	 *
	 * @param task the commanded Task to be set.
	 */
	public void setCommandedTask(Task task) {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		if(!isChild()) {
			task.setPrivateInformation(position, velocity, characteristics, resources);
			commandedTask = task;
			taskList.removeAllTasks();
		}
	}

	/**
	 * Returns an integer representing the age in pseudo years, as and
	 * individual would experience it.
	 *
	 * @return a int representing the age in pseudo years.
	 */
	public int getAge() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return characteristics.getAge();
	}

	/**
	 * Returns an integer describing the gender.
	 *
	 * @return an int describing the gender.
	 */
	public int getGender() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return characteristics.getGender();
	}

	/**
	 * Returns the amount of resource units available of the specified resource
	 * type.
	 *
	 * @param resourceType an int representing the resource type.
	 * @return an int representing the amount of resource units available of
	 * the specified resource type.
	 */
	public int getResourceAmount(int resourceType) {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return resources.getResourceAmount(resourceType);
	}

	/**
	 * Returns a float describing the current safety.
	 *
	 * @return a float describing the current safety.
	 */
	public float getSafety() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return characteristics.getSafety();
	}

	/**
	 * Returns a float describing the current saturation.
	 *
	 * @return a float describing the current saturation.
	 */
	public float getSaturation() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return characteristics.getSaturation();
	}

	/**
	 * Returns a float describing the current happiness.
	 *
	 * @return a float describing the current happiness.
	 */
	public float getHappiness() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return characteristics.getHappiness();
	}

	/**
	 * Returns a float describing the current health.
	 *
	 * @return a float describing the current health.
	 */
	public float getHealth() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return characteristics.getHealth();
	}

	/**
	 * Returns a float describing the current gather skill.
	 *
	 * @return a float describing the current gather skill.
	 */
	public float getGatherSkill() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return characteristics.getGatherSkill();
	}

	/**
	 * Returns a float describing the current sacrifice skill.
	 *
	 * @return a float describing the current sacrifice skill.
	 */
	public float getSacrificeSkill() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return characteristics.getSacrificeSkill();
	}

	/**
	 * Returns a float describing the current transportation skill.
	 *
	 * @return a float describing the current transportation skill.
	 */
	public float getTransportationSkill() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return characteristics.getTransportationSkill();
	}

	/**
	 * Tests if the current age (getAge) is under a constant getAdultAge(). If
	 * so, the Individual is a child.
	 *
	 * @return a boolean stating if the Indiviudal is a child.
	 */
	public boolean isChild() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return characteristics.isChild();
	}

	/**
	 * Returns a Vector with the velocity of the Individual.
	 *
	 * @return a Vector with the velocity of the Individual.
	 */
	public Vector getVector() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		return velocity.getVector();
	}

	/**
	 * Outputs for debugging.
	 */
	public String toString() {
		return super.toString() + "(" + getName() + ")";
	}

	/**
	 * Deletes the object.
	 */
	public void delete() {
		taskList.removeAllTasks();
		commandedTask = null;
		super.delete();
	}

}
