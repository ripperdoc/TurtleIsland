/*
 * 2003-05-12 EliasAE
 * 	Socialization and remove of socialization groups.
 *
 * 2003-05-11 EliasAE
 * 	Added support for deletable.
 *
 * 2003-05-11 Martin
 *	Created a new constructor that should only be used from the game start.
 *
 * 2003-05-10 Martin
 *	Corrected the constructor.
 *
 * 2003-05-05 EliasAE
 * 	Added getRandomPartner to support SocializeTask.
 *
 * 2003-04-15 Martin
 * 	Corrected the registration and unregistration procedure to the world.
 *
 * 2003-04-11 Martin
 * 	Completed the class. Possible error handling to be done.
 *	Idea: calle the error messages external or from private method.
 *  In this class the same error message appear on two places.
 */
package gameengine.gameobjects;

import gameengine.*;
import java.util.*;
import java.util.List;

/**
 * A group has a position and indicators to calculate if there are both
 * male and females in the group that can reproduce. It also has a list of
 * children, i.e. individuals that are members but aren't reproductive. The
 * group has methods for registering and unregistering individuals and a
 * method for updating the groups information when one of its child members
 * becomes an adult.
 */
public class SocializeGroup extends GameObject {

	/**
	 * The reproductive males in the group.
	 */
	private List males = new ArrayList();

	/**
	 * The reproductive females in the group.
	 */
	private List females = new ArrayList();

	/**
 	 * A list of all children in the group, with reference to each Individual.
 	 */
	private List children = new ArrayList();

	/**
	 * Constructs a SocializeGroup at a specified point with a specified inital
	 * member. Also registers the group at the world's list of SocializeGroups.
	 *
	 * @param position a Point with the position of the SocializeGroup.
	 * @param initialMember the Individual that should be the first member
	 * (the indiviudal who started the group).
	 */
	// TODO This is not needed?
	/*public SocializeGroup(Point position, Individual initialMember) {
		super(position);

		World.getWorld().register(this);

		register(initialMember);
	}*/

	/**
	 * Constructs a SocializeGroup at a specified point with no inital
	 * member. Also registers the group at the world's list of SocializeGroups.
	 * This constructor should ONLY be used when creating a game, otherwise an initial
	 * member must be sent.
	 *
	 * @param position a Point with the position of the SocializeGroup.
	 */
	public SocializeGroup(Point position) {
		super(position);

		World.getWorld().register(this);
	}

	/**
	 * Checks if the specified individual is child or not. If it is a child it
	 * is sent to the list of children, if it is an adult the gender is checked
	 * and then an integer representing the amount of reproductive individuals
	 * of that gender is increased. If it is an adult the group will not have a
	 * reference to the Individual itself.
	 *
	 * @param individual the Individual to be registered to the group.
	 */
	public void register(Individual individual) {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		if (
				children.contains(individual) ||
				females.contains(individual) ||
				males.contains(individual)) {
			throw new IllegalArgumentException("Cannot register the same individual twice.");
		}
		if(individual.isChild()) {
			children.add(individual);
		}
		else {
			if(individual.getGender() ==  Environment.MALE) {
				males.add(individual);
			}
			else if(individual.getGender() ==  Environment.FEMALE) {
				females.add(individual);
			}
			else {
				// If the gender int wasn't correct, i.e. 0 or 1.
				throw new IllegalStateException("Illegal value of the gender integer. " +
						"Must be 0 or 1. Correct the value of the gender integer");
			}
		}
	}

	/**
	 * Checks if the specified individual is a child or not. If it is a child
	 * it is removed from the list of children, if it is an adult the gender is
	 * checked and then an integer representing the amount of reproductive
	 * individuals of that gender is decreased. If there are no members left in
	 * the group, it unregisters itself from the worlds list of SocializeGroups
	 * and from the list of all objects.
	 *
	 * @param individual the Indvidual to be removed from the group.
	 */
	public void unRegister(Individual individual) {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		removeIndividual(individual);
		if(children.isEmpty() && males.size() == 0 && females.size() == 0) {
			World.getWorld().unRegister(this);
			// Doesn't need to be unregistered from the allObjects list
			// because SocializeGroups doesn't need to be drawn.
			delete();
		}
	}

	/**
	 * Checks if the individual is a child and if not, which gender, and then
	 * removes the indivudal from the children list or decreases the counters
	 * of the specified gender.
	 *
	 * @param individual the Indvidual to be removed from the group.
	 */
	private void removeIndividual(Individual individual) {
		boolean success = false;
		if(individual.isChild()) {
			success = children.remove(individual);
		}
		else if(individual.getGender() ==  Environment.MALE) {
			success = males.remove(individual);
		}
		else if(individual.getGender() ==  Environment.FEMALE) {
			success = females.remove(individual);
		}
		else {
			throw new IllegalStateException("Illegal value of the gender integer. " +
					"Must be 0 or 1. Correct the value of the gender integer");
		}

		if (!success) {
			throw new IllegalArgumentException(
					"Cannot unregister this individual, it has not been registered ");
		}
	}

	/**
	 * Checks if it is possible to reproduce in the group. It simply checks
	 * that there exists both reproductive males and females.
	 *
	 * @return a boolean stating if it is possible to reproduce in the group.
	 */
	public boolean isReproduceable() {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		// Returns true if there are reproductives of both genders.
		return (males.size() > 0 && females.size() > 0);
	}

	/**
	 * Removes the former child from the list of children and registers it
	 * again, as an adult. Is called when an individual has grown up during
	 * it's time in a group.
	 *
	 * @param individual the individual which has left childhood.
	 */
	public void childToAdult(Individual individual) {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		if (!children.contains(individual)) {
			throw new IllegalArgumentException(
					"Cannot convert this individual from child to adult, " + 
					"because it is not a member of this group." + individual + 
					" thrower: " + this
					);
		}
		children.remove(individual);
		register(individual);
	}

	/**
	 * Retrieves a random partner for an individual.
	 *
	 * @param individual the individual to get a partner for.
	 */
	public Individual getRandomPartner(Individual individual) {
		if (isDeleted()) {
			throw new ObjectDeletedException(this);
		}
		List partnerList;
		if (individual.getGender() == Environment.MALE) {
			partnerList = females;
		} else if(individual.getGender() == Environment.FEMALE) {
			partnerList = males;
		} else {
			throw new IllegalArgumentException("Illegal individual gender");
		}

		// Truncate to get a int less than size.
		int partnerIndex = (int) (Math.random() * partnerList.size());
		return (Individual) partnerList.get(partnerIndex);
	}

	public String toString() {
		// TODO Debug
		return super.toString() + "(" +
			males.size() + ", " +
			females.size() + ", " +
			children.size() + ")"
			;
	}
}
