/* 2003-05-13 henko
 *	Added the pauseDuration.
 *
 * 2003-05-13 Martin
 *	Added the joinSocializationChance.
 *
 * 2003-05-13 henko
 *  Added a bunch of new names. =)
 *
 * 2003-05-12 henko
 *  Made the class implement Serializable.
 *
 * 2003-05-12 Martin
 *	Changed som values for balancing.
 *
 * 2003-05-12 EliasAE
 * 	Changed adultage to 2.
 *
 * 2003-05-12 henko
 *	Changed the initialization of the minCoordinate and maxCoordinate to make
 *  it fit better for Point.
 *
 * 2003-05-10 henko
 *	Added constants related to randomizations.
 *
 * 2003-05-10 Martin
 *	Corrected som constants concering the independent turtles.
 *	Added several list of names.
 *
 * 2003-05-09 henko
 * 	Changed ResourceContainer to Resources.
 *
 * 2003-05-06 EliasAE
 * 	Added reproduce constants.
 *
 * 2003-05-04 Martin
 *	Added the constant deathProbabilityOffset.
 *
 * 2003-05-03 Martin
 *	Added the constant for timeYearRatio and changed it's name to yearSecondsRatio. Added
 *	the safetyDecreaseFactor constant that calculates how the safety decreases outside the
 *	lightpillar radius. Added and corrected a lot's of values of constants.
 *	Added minimumNeed, the value that decides when a turtle should die of a low need.
 *
 * 2003-05-02 Martin
 *	Added the constant maxUnitsCarried that defines how much units an individudal can
 *	carry.
 *
 * 2003-04-30 Martin
 *	Added a maximal turning angle for the wobble task. Corrected de defaultSkillValue
 *
 *	2003-04-15 Martin
 *	Added a method for getting the radius of a wobbling. Also lowered the speed of a turtle a bit.
 *	(A bit too fast pergamon :-D)
 *
 * 2003-04-15 pergamon
 *  Just made the turtles move a little faster.
 *
 * 2003-04-14 Martin
 *	Changed the defaultIndividualSpeed to the correct format.
 *
 * 2003-04-13 Martin
 *	Added the default speed of an indivudal
 *
 * 2003-04-13 pergamon
 *  onPositionOffset is now 2.0f
 *
 * 2003-04-11 Martin
 * 	Generated a new Environment from PoseidonUML to get all variables for the get methods.
 *	Corrected all <P> tags and added the method getNumberOfResourceTypes() used by the Resources.
 * 	Made the class public, as it should be.
 *	Added public static final ints for resources and genders.
 */
package gameengine;


import java.io.Serializable;
import java.util.*;

/**
 * Environment is a class containing all important constants for the game
 * engine. All objects in the game engine can reach the Environments values
 * via it's get methods. The reference to the Environment object is reached
 * through the World object.
 */
public class Environment implements Serializable {

	/**
	 * A two-dimensional array of names that individuals can have.
	 * The first variable defines the gender, and the second the name.
	 */
	public static final String[][] INDIVIDUAL_NAMES =
		{	/* Female names */
			{"Adelita", "Beth", "Brynhilde", "Cassie", "Clothahump", "Desire", "Devdatt",
			"Esmeralda", "Estrelita", "Gamera", "Gloria", "Halie", "Jade", "Jen",
			"Jessea", "Kauila", "Kiko", "Kutsie", "Lacy", "Leonora", "Mandy", "Masa",
			"Minoaka", "Molly", "Morla", "Nanghihinayang", "Oceania", "Perdida",
			"Pinky", "Roshi", "Shelly", "Swift Foot", "Sydney", "Tokka", "Topsy Turvy",
			"Venus", "Vito", "Yamalet"}
		,
			/* Male names */
			{"Ace", "Aford", "Anthill", "Aristoteles", "Bante", "Bart", "Bartleby", "Bentley",
			"Bert", "Big Al", "Blungrobber", "Bobby", "Bosco", "Brutus", "Bubu",
			"Buddy", "Churchy", "Dewie", "Donatello", "Dr. Snuggles", "Edgar", "Fast",
			"Franklin", "Fred", "Geetamar", "Goombocker", "Hercules", "Hobbes", "Homer", 
			"Honu", "Humphrey", "JN21-21", "Joseph", "Killer", "Kwei", "Lancelot",
			"Lazy Lightning", "Leonardo", "Lonesome George", "Longnails", "Lump",
			"Mack", "Marigny", "Maximilian", "Mearld", "Michelangelo", "Naraht",
			"Norman", "Opuk", "Pancake", "Patch", "Pawikan", "Perky Pokey", "Piglet",
			"Puck", "Puzzling", "Rafael", "Ray", "Ringo", "Rover", "Scooter",
			"Skalman", "Skipperdee", "Slash", "Snapper", "Snorty", "Soup", "Speedy",
			"Spike", "Squirttle", "Stan IV", "T1", "Tiger", "Timmy", "Toots",
			"Triton", "Tuck", "Turtleneck", "Tuttle", "Verne", "Waldo", "Walter",
			"Weasel", "Xavier", "Yurtle", "Wulfgang"}
		};

	/**
	 * The array of names that objects with no gender can have.
	 */
	public static final String[] OTHER_NAMES = {"Salmon", "Trout", "Crab"};

	/**
	 * The int that specifies that an owner of a Characteristics object is female.
	 */
	public static final int FEMALE = 0;

	/**
	 * The int that specifies that an owner of a Characteristics object is male.
	 */
	public static final int MALE = 1;


	/**
	 * The int that specifies the food resource. NOTE: Names still not satisfying. Don't use before agreement. /M
	 */
	public static final int FOOD = 0;
	private static final String FOOD_STR = "Fish";

 	/**
	 * The int that specifies the food resource. NOTE: Names still not satisfying. Don't use before agreement. /M
	 */
	public static final int HAPPINESS = 1;
	private static final String HAPPINESS_STR = "Happiness";

	/**
	 * The int that specifies the food resource. NOTE: Names still not satisfying. Don't use before agreement. /M
	 */
	public static final int SACRIFICE = 2;
	private static final String SACRIFICE_STR = "Sacrifice";

	/**
	 * An array used by Point to create the min and max coordinates.
	 * Used this method to use another constructor in Point, so that
	 * the (x, y) one can have a out of bounds check.
	 * Syntax: {{minX, minY}, {maxX, maxY}}
	 */
	private float[][] boundsArray;

	/**
	 * A Point representing the minimum possible world coordinate.
	 */
	private Point minCoordinate;

	/**
	 * A Point representing the maximum coordinate of the world.
	 */
	private Point maxCoordinate;

	/**
	 * A float representing the allowed offset for being on a position.
	 */
	private float onPositionOffset = 0.083333f;

	/**
	 * A float representing the ratio of one unit of a resource to &quot;amount of
	 * need&quot;.
	 */
	private float unitNeedRatio = 0.25f;

	/**
	 * A float representing the speed of the decrease of a need, in percents per second.
	 */
	private float needDecreaseSpeed = 0.005f;

	/**
	 * A float representing the ratio of one sacrificed resource unit to a
	 * float valued distance.
	 */
	private float sacrificedUnitsDistanceRatio = 0.1f;

	/**
	 * A float representing the default maximum safety influence radius of the
	 * light pillar.
	 */
	private float defaultLightPillarRadius = 5f;

	/**
	 * A float representing the amount of seconds that equals a year in the
	 * individual's world.
	 */
	private float yearSecondsRatio = 10f;

	/**
	 * A float representing the gathering speed in units per second for
	 * an average turtle.
	 */
	private float gatherSpeed = 0.25f;

	/**
	 * A float representing the sacrificing speed in units per second for
	 * an average turtle.
	 */
	private float sacrificeSpeed = 0.25f;

	/**
	 * A float representing the default speed (in lenght units per second) of
	 * an individual.
	 */
	private float individualSpeed = 1.8f;

	/**
	 * A float representing the default skill value.
	 */
	private float defaultSkillValue = 0.5f; // Halfway filled skill bar

	/**
	 * A float representing the increase speed of a skill per second of work.
	 */
	private float skillIncreaseSpeed = 0.0025f;

	/**
	 * A float representing the decrease speed of a skill per second of work.
	 */
	private float skillDecreaseSpeed = 0.00125f; // Half the speed of increase

	/**
	 * An int representing the age in pseudo years that defines when an
	 * Individual becomes adult.
	 */
	private int adultAge = 18;

	/**
	 * An int representing the number of resource types for initalizing
	 * Resources.
	 */
	private int numberOfResourceTypes = 3;

	/**
	 * A float representing the maximum distance from a node an individual should
	 * be able to wobble.
	 */
	private float nodeWobbleRadius = 1f;

	/**
	 * A float representing the maximum angle that an indivudal could turn
	 * in one wobble move (each cycle).
	 */
	private float maxWobbleTurning = 0.8f;

	/**
	 * The maximum amount of units of a resource that an individudal can carry.
	 */
	private int maxUnitsCarried = 2;

	/**
	 * The factor that defines the percentual decrease of values (maximum health)
	 * from age per second.
	 */
	private float agingFactor = 0.005f;

	/**
	 * The factor that defines the percentual decrease of safety from the distance
	 * from the current LightPillarradius.
	 */
	private float safetyDecreaseFactor = 0.9f;

	/**
	 * The minimum allowed value of a need. If a need is below this, the indivudal
	 * will die.
	 */
	private float minimumNeed = 0.001f;

	/**
	 * The offset in percentual units of the killcheck that checks if the indivudal has died.
	 */
	private float deathProbabilityOffset = 0.3f;

	/**
	 * The wobble radius for socialize groups.
	 */
	private float socializeWobbleRadius = 2.0f;

	/**
	 * The time between attempts to reproduce. Assumes that the turtle has 100%
	 * health since lower health will reduce the chance of reproducing.
	 */
	private float reproduceInterval = 100f;

	/**
	 * The interval (time) in seconds during which an individual wants to take
	 * a stroll.
	 */
	private float strollInterval = 100f;

	/**
	 * The general interval in seconds between randomization attempts.
	 */
	private float randomizeInterval = 1f;

	/**
	 * Represents the saturation level where the Turtle feels satisfied, in
	 * other words, not hungry nor full. Below this level, the Turtle feels
	 * hungry and above it, it feels full. (Example was given for food, but it
	 * applies to all needs.)
	 */
	private float defaultNeedValue = 0.5f;

	/**
	 * The interval in seconds between two meals when the turtle feels
	 * satisfied (see saturationNormalizer). (Example was given for food, but
	 * it applies to all needs.)
	 */
	private float needInterval = 90f;

	/**
	 * The chance in percent of choosing to join an already existing group instead
	 * of creating an own group.
	 */
	private float joinSocializationChance = 0.8f;

	/**
	 * The number of seconds to pause after finishing a commanded task.
	 */
	private float pauseDuration = 4f;

////////////////////////////////////////////////////
//                Get methods                     //
////////////////////////////////////////////////////



	/**
	 * Returns the maximum coordinate of the world, i.e. the lower right corner
	 * of the world.
	 *
	 * @return a Point representing the maximum coordinate of the world.
	 */
	public Point getMaxCoordinate() {
		return maxCoordinate;
	}

	/**
	 * Returns the minimum possible world coordinate, i.e. the upper left
	 * corner of the world.
	 *
	 * @return a Point representing the minimum possible world coordinate.
	 */
	public Point getMinCoordinate() {
		return minCoordinate;
	}

	/**
	 * Returns a float value describing how far a position can be to another
	 * position but still be considered to be at the same position by the game
	 * logics.
	 *
	 * @return a float representing the allowed offset for being on a position.
	 */
	public float getOnPositionOffset() {
		return onPositionOffset;
	}

	/**
	 * Returns the ratio of one unit of a resource to &quot;amount of need&quot;, for
	 * example used to see how much a food unit would increase the saturation.
	 *
	 * @return a float representing the ratio of one unit of a resource to
	 * &quot;amount of need&quot;.
	 */
	public float getUnitNeedRatio() {
		return unitNeedRatio;
	}

	/**
	 * Returns a float representing the speed of the decrease of a need, in
	 * percentual units per second.
	 *
	 * @return a float representing the speed of the decrease of a need, in
	 * percentual units per second.
	 */
	public float getNeedDecreaseSpeed() {
		return needDecreaseSpeed;
	}

	/**
	 * Returns the ratio of one sacrificed resource unit to a float valued
	 * distance.
	 *
	 * @return a float representing the ratio of one sacrificed resource unit
	 * to a float valued distance.
	 */
	public float getSacrificedUnitsDistanceRatio() {
		return sacrificedUnitsDistanceRatio;
	}

	/**
	 * Returns a float representing the default maximum safety influence radius
	 * of the light pillar.
	 *
	 * @return a float representing the default maximum safety influence radius
	 * of the light pillar.
	 */
	public float getDefaultLightPillarRadius() {
		return defaultLightPillarRadius;
	}

	/**
	 * Returns the ratio between a year in the individual's world and real second.
	 *
	 * @return a float representing the ratio between a year in the individual's
	 * world and real second.
	 */
	public float getYearSecondsRatio() {
		return yearSecondsRatio;
	}

	/**
	 * Returns a float representing the gathering speed in units per second.
	 *
	 * @return a float representing the gathering speed in units per second.
	 */
	public float getGatherSpeed() {
		return gatherSpeed;
	}

	/**
	 * Returns a float representing the sacrificing speed in units per second.
	 *
	 * @return a float representing the sacrificing speed in units per second.
	 */
	public float getSacrificeSpeed() {
		return sacrificeSpeed;
	}

	/**
	 * Returns a float representing the default speed (in lenght units per
	 * second) of an individual.
	 *
	 * @return a float representing the default speed of an individual.
	 */
	public float getIndividualSpeed() {
		return individualSpeed;
	}

	/**
	 * Returns a float representing the default skill value.
	 *
	 * @return a float representing the default skill value.
	 */
	public float getDefaultSkillValue() {
		return defaultSkillValue;
	}

	/**
	 * Returns the increase speed of a skill, in "skill" per second.
	 *
	 * @return a float representing the increase speed of a skill.
	 */
	public float getSkillIncreaseSpeed() {
		return skillIncreaseSpeed;
	}

	/**
	 * Returns the decrease speed of a skill, in percents per seconds.
	 *
	 * @return a float representing the decrease speed of a skill.
	 */
	public float getSkillDecreaseSpeed() {
		return skillDecreaseSpeed;
	}

	/**
	 * Returns the age in pseudo years that defines when an Individual becomes
	 * adult.
	 *
	 * @return an int representing the age in pseudo years that defines when an
	 * Individual becomes adult.
	 */
	public int getAdultAge() {
		return adultAge;
	}

	/**
	 * Returns the number of resource types for initalizing Resources.
	 *
	 * @return an int representing the number of resource types for initalizing
	 * Resources.
	 */
	public int getNumberOfResourceTypes() {
		return numberOfResourceTypes;
	}

	/**
	 * Returns the maximum distance from a node an individual should
	 * be able to wobble.
	 *
	 * @return a float representing the maximum distance from a node an individual should
	 * be able to wobble.
	 */
	public float getNodeWobbleRadius() {
		return nodeWobbleRadius;
	}

	/**
	 * Returns the maximum angle that an indivudal could turn in one wobble move (each cycle).
	 *
	 * @return a float representing the maximum angle that an indivudal could turn
	 * in one wobble move (each cycle).
	 */
	public float getMaxWobbleTurning() {
		return maxWobbleTurning;
	}

	/**
	 * Returns the maximum amount of units of a resource that an individudal can carry.
	 *
	 * @return the maximum amount of units of a resource that an individudal can carry.
	 */
	public int getMaxUnitsCarried() {
		return maxUnitsCarried;
	}

	/**
	 * Returns the factor that defines the percentual decrease of values (maximum health)
	 * from age per second.
	 *
	 * @return the factor that defines the percentual decrease of values (maximum health)
	 * from age per second.
	 */
	public float getAgingFactor() {
		return agingFactor;
	}

	/**
	 * Returns the factor that defines the percentual decrease of safety from the distance
	 * from the current LightPillarradius.
	 *
	 * @return the factor that defines the percentual decrease of safety from the distance
	 * from the current LightPillarradius.
	 */
	public float getSafetyDecreaseFactor() {
		return safetyDecreaseFactor;
	}

	/**
	 * Returns minimum allowed value of a need. If a need is below this, the indivudal
	 * will die.
	 *
	 * @return the minimum allowed value of a need.
	 */
	public float getMinimumNeed() {
		return minimumNeed;
	}

	/**
	 * Returns the offset in percentual units of the killcheck that checks if the
	 * indivudal has died.
	 *
	 * @return the offset in percentual units of the killcheck that checks if the
	 * indivudal has died.
	 */
	public float getDeathProbabilityOffset() {
		return deathProbabilityOffset;
	}

	/**
	 * Retrives the String format of the resource types declared in Environment
	 *
	 * @param type the resource type to get a string of.
	 * @return the string of a resource type.
	 */
	public static String getResourceString(int type) {
		String tmpResourceType;
		if (type == Environment.FOOD) {
			tmpResourceType = "Fish";
		} else if (type == Environment.HAPPINESS) {
			tmpResourceType = "Flowers";
		} else if (type == Environment.SACRIFICE) {
			tmpResourceType = "Gold";
		} else {
			tmpResourceType = "Undefined";
		}

		return tmpResourceType;
	}

	/**
	 * Retrieves the radius of wobbling in a socialize group.
	 *
	 * @return the radius of wobbling in a socialize group.
	 */
	public float getSocializeWobbleRadius() {
		return socializeWobbleRadius;
	}

	/**
	 * Retrieves the interval between attempts to reproduce.
	 *
	 * @return the interval between attempts to reproduce in seconds.
	 */
	public float getReproduceInterval() {
		return reproduceInterval;
	}

	/**
	 * Gets the interval (time) in seconds during which an individual wants to take
	 * a stroll.
	 *
	 * @return the interval (time) in seconds during which an individual wants to take
	 * a stroll.
	 */
	public float getStrollInterval() {
		return strollInterval;
	}

	/**
	 * Gets the interval in seconds between randomization attempts.
	 *
	 * @return the interval in seconds between randomization attempts.
	 */
	public float getRandomizeInterval() {
		return randomizeInterval;
	}

	/**
	 * Represents the saturation level where the Turtle feels satisfied, in
	 * other words, not hungry nor full. Below this level, the Turtle feels
	 * hungry and above it, it feels full. (Example was given for food, but it
	 * applies to all needs.)
	 */
	public float getDefaultNeedValue() {
		return defaultNeedValue;
	}

	/**
	 * The interval in seconds between two meals when the turtle feels
	 * satisfied. (See saturationNormalizer) (Example was given for food, but
	 * it applies to all needs.)
	 */
	public float getNeedInterval() {
		return needInterval;
	}

	/**
	 * The chance in percent of choosing to join an already existing group instead
	 * of creating an own group.
	 */
	public float getJoinSocializationChance() {
		return joinSocializationChance;
	}

	/**
	 * The number of seconds to pause after finishing a commanded task.
	 */
	public float getPauseDuration() {
		return pauseDuration;
	}

	/**
	 * Sets the bounds, if they aren't set yet, to the given bounds. If the bounds
	 * already are set, nothing will happen.
	 *
	 * @param width the width in floats of the map of the world.
	 * @param height the height in floats of the map of the world.
	 */
	public void setBounds(float width, float height) {
		if(boundsArray == null) {
			boundsArray = new float[2][2];
			boundsArray[0][0] = 0f;
			boundsArray[0][1] = 0f;
			boundsArray[1][0] = width;
			boundsArray[1][1] = height;
			minCoordinate = new Point(boundsArray[0]);
			maxCoordinate = new Point(boundsArray[1]);
		} else {
			throw new IllegalStateException("Environment's bounds already set.");
		}
	}


}
