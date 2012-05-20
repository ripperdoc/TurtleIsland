/* 2003-05-19 Martin
 *	Fixed a weight on safety when calculating health, so that safety won't keep the
 *	health up above the correct mean value.
 *
 * 2003-05-13 Martin
 *	Corrected a stupid error in the getSkillEfficiency methods.
 *
 * 2003-05-12 EliasAE
 * 	Removed support for deletable.
 *
 * 2003-05-12 Martin
 *	Made the increaseSkill-methods also decrease the skills that weren't used.
 *	Added the isChild() method to characteristics. Corrected so that health isn't
 *	lowered until a turtle is adult. Corrected so that skills isn't changed on children.
 *
 * 2003-05-11 EliasAE
 * 	Added support for deletable.
 *
 * 2003-05-11 Martin
 *	Corrected, amongst others, the skill decreasers.
 *
 * 2003-05-10 henko
 *  isHungray and isDepressed now uses Randomizer.
 *
 * 2003-05-09 henko
 *  Changed ResourceContainer to Resources.
 *
 * 2003-05-05 Martin
 *	Added the method isStrolly().
 *
 * 2003-05-03 Martin
 *	Changed some details in the constructors. Wrote the setSafety() and recaulcalteHealth().
 *	Somewhat completed the constructor for newborn individuals. The isHungry() needs work.
 *
 * 2003-05-02 Martin
 *	Added the three methods for getting the factor efficiency. Is better calculated
 *	here if we would like to change the formula. Added the three methods isHungry,
 *	isDepressed and isUnsafe.
 *
 * 2003-04-30 Martin
 *	Made the increase skill methods public, they need to be acessed from the task system.
 *
 * 2003-04-28 EliasAE
 * 	Wrote getter methods and added two constructors.
 *
 * 2003-04-23 EliasAE
 * 	Formatting.
 *
 * 2003-04-15 Martin
 *  Implemented serializable.
 *
 */
package gameengine.gameobjects;

import java.io.*;
import gameengine.*;

/**
 * A container describing the characteristics of a &quot;living&quot; object
 * (preferably an individual). It describes needs (saturation, happiness,
 * safety), skills (gather, transportation, sacrificing) and other values
 * (health, lifiteime and gender).
 * Several methods allows manipulation of these values. Most of them is
 * intended for increasing the values, and the methods calculates
 * themselves how much to increase. Decreasing methods has a similar
 * pattern..There are also get methods for each value.
 */
public class Characteristics implements Serializable {

	/**
	 * A percentual value describing the saturation of the owner of the
	 * characteristics.
	 */
	private float saturation;

	/**
	 * A randomizer that keeps track of when it's time to eat.
	 */
	private Randomizer hungryRandomizer;

	/**
	 * A percentual value describing the happiness of the owner of the
	 * characteristics.
	 */
	private float happiness;

	/**
	 * A randomizer that keeps track of when it's time to get happiness.
	 */
	private Randomizer depressedRandomizer;

	/**
	 * A percentual value describing the safety of the owner of the
	 * characteristics.
	 */
	private float safety;

	/**
	 * A randomizer that keeps track of when it's time to feel unsafe.
	 */
	private Randomizer unsafeRandomizer;

	/**
	 * A randomizer that keeps track of when it's time to stroll.
	 */
	private Randomizer strollRandomizer;

	/**
	 * A percentual value describing the health of the owner of the
	 * characteristics.
	 */
	private float health;

	/**
	 * The time of birth of the owner of the characteristics, taken from the
	 * getWorldTime().
	 */
	private float timeOfBirth;

	/**
	 * An integer representing the gender of the owner of the characteristics.
	 */
	private int gender;

	/**
	 * A percentual value describing the gather skill of the owner of the
	 * characteristics.
	 */
	private float gatherSkill;

	/**
	 * A percentual value describing the sacrificing skill of the owner of the
	 * characteristics.
	 */
	private float sacrificeSkill;

	/**
	 * A percentual value describing the transportation skill of the owner of
	 * the characteristics.
	 */
	private float transportationSkill;

	/**
	 * A percentual value (procentual units) describing the current maximum
	 * possible health of the owner of the characteristics.
	 */
	private float maximumHealth;

	/**
	 * The age of the Indivudal when the game started. Of course only applicable
	 * when a new game is created.
	 */
	private int startAge;

	/**
	 * Constructs default characteristics.
	 */
	public Characteristics(int gender, int age) {
		saturation = World.getEnvironment().getDefaultNeedValue();
		happiness = World.getEnvironment().getDefaultNeedValue();
		// The value will be recalculates immideately
		safety = 1.0f;

		// Set up the randomizers
		hungryRandomizer = new IntervalRandomizer(World.getEnvironment().getNeedInterval());
		depressedRandomizer = new IntervalRandomizer(World.getEnvironment().getNeedInterval());
		// TODO Take from Environment.
		unsafeRandomizer = new ProbabilityRandomizer(0.5f);
		strollRandomizer = new IntervalRandomizer(World.getEnvironment().getStrollInterval());

		// Set health, with max health as the full health
		maximumHealth = 1f;
		recalculateHealth();

		this.gender = gender;

		startAge = age;
		timeOfBirth = World.getWorld().getWorldTime();

		gatherSkill = World.getEnvironment().getDefaultSkillValue();
		sacrificeSkill = World.getEnvironment().getDefaultSkillValue();
		transportationSkill = World.getEnvironment().getDefaultSkillValue();

	}

	/**
	 * Constructs the characteristics from two other characteristics, used
	 * for merging of two parent's characteristics into the child.
	 */
	public Characteristics(
			Characteristics firstCharacteristics, Characteristics secondCharacteristics) {

		// Randomize a gender and create the characteristics with zero age
		this((int)Math.round(Math.random()), 0);

		// Sets the skills to appropriate values according to the parents skills

		// TODO There should be a "loss" of skill between generations, i.e. that
		// TODO the childs skill isn't just a mean value of the parent's skills.
		gatherSkill = (firstCharacteristics.getGatherSkill() +
				secondCharacteristics.getGatherSkill()) / 2f;
		sacrificeSkill = (firstCharacteristics.getSacrificeSkill() +
				secondCharacteristics.getSacrificeSkill()) / 2f;
		transportationSkill = (firstCharacteristics.getTransportationSkill() +
				secondCharacteristics.getTransportationSkill()) / 2f;
	}

	/**
	 * Increases the saturation with an amount specified by a constant
	 * (getUnitNeedRatio).
	 */
	public void increaseSaturation() {
		saturation += World.getEnvironment().getUnitNeedRatio();
		if(saturation > 1f) {
			saturation = 1f;
		}
	}

	/**
	 * Decreases the saturation with an amount calculated from the time since
	 * the last update cycle multiplied by a constant (getNeedDecreaseSpeed).
	 * Needs to be called every update cycle to calculate correctly.
	 */
	void decreaseSaturation() {
		// Calculates the factor of decrease that should be multiplied
		// with the saturation
		float decreaseFactor = 1 - (World.getEnvironment().getNeedDecreaseSpeed() *
				World.getWorld().getTimeSinceCycle());
		saturation *= decreaseFactor;
	}

	/**
	 * Increases the happiness with an amount specified by a constant
	 * (getUnitNeedRatio).
	 */
	public void increaseHappiness() {
		happiness += World.getEnvironment().getUnitNeedRatio();
			if(happiness > 1f) {
				happiness = 1f;
		}
	}

	/**
	 * Decreases the happiness with an amount calculated from the time since
	 * the last update cycle multiplied by a constant (getNeedDecreaseSpeed).
	 * Needs to be called every update cycle to calculate correctly.
	 */
	void decreaseHappiness() {
		// Calculates the factor of decrease that should be multiplied
		// with the happiness
		float decreaseFactor = 1 - (World.getEnvironment().getNeedDecreaseSpeed() *
				World.getWorld().getTimeSinceCycle());
		happiness *= decreaseFactor;
	}

	/**
	 * Sets the safety according to the given Point. The safety is calculated
	 * through the distance from the light pillar, with a constant
	 * (getSacrificedUnitsDistanceRatio).
	 *
	 * @param point the Point describing the position of the owner of the
	 * characteristics.
	 */
	void setSafety(Point point) {
		LightPillarNode lightPillar = World.getWorld().getLightPillar();
		float distance = point.distanceTo(lightPillar.getPoint());
		distance = distance - lightPillar.getInfluenceRadius();
		if(distance > 0) {
			// Decreases the safety (which is 1) with the percentual value
			// that is defined by safetyDecreaseFactor. The decrease is made
			// distance times, so that safety = safetyDecrease ^ distance.
			safety = (float)Math.pow(World.getEnvironment().getSafetyDecreaseFactor(),
					distance);
		}
		else {
			safety = 1;
		}
	}

	/**
	 * Returns a float describing the current safety.
	 *
	 * @return a float describing the current safety.
	 */
	public float getSafety() {
		return safety;
	}

	/**
	 * Returns a float describing the current saturation.
	 *
	 * @return a float describing the current saturation.
	 */
	public float getSaturation() {
		return saturation;
	}

	/**
	 * Returns a float describing the current happiness.
	 *
	 * @return a float describing the current happiness.
	 */
	public float getHappiness() {
		return happiness;
	}

	/**
	 * Recalculates the health using the mean value of the three needs and the
	 * time since the last cycle. Health cannot be greater than the current
	 * maximum health, which is decreased constantly with a percentual
	 * constant. Needs to be called every update cycle to calculate correctly.
	 */
	void recalculateHealth() {
		// Calculates the factor of decrease that should be multiplied
		// with the maximum health
		if(!isChild()) {
			float agingFactor = 1 - (World.getEnvironment().getAgingFactor() *
					World.getWorld().getTimeSinceCycle());
			maximumHealth *= agingFactor;
		}

		float newHealth = (saturation + happiness + (safety / 2f) ) / 3f;
		if(newHealth > maximumHealth) {
			newHealth = maximumHealth;
		}

		health = newHealth;
	}

	/**
	 * Returns a float describing the current health.
	 *
	 * @return a float describing the current health.
	 */
	public float getHealth() {
		return health;
	}

	/**
	 * Returns an integer describing the gender.
	 *
	 * @return an int describing the gender.
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * Returns a float describing the current gather skill.
	 *
	 * @return a float describing the current gather skill.
	 */
	public float getGatherSkill() {
		return gatherSkill;
	}

	/**
	 * Returns a float describing the current sacrifice skill.
	 *
	 * @return a float describing the current sacrifice skill.
	 */
	public float getSacrificeSkill() {
		return sacrificeSkill;
	}

	/**
	 * Returns a float describing the current transportation skill.
	 *
	 * @return a float describing the current transportation skill.
	 */
	public float getTransportationSkill() {
		return transportationSkill;
	}

	/**
	 * Returns a float describing the current factor of efficiency
	 * when gathering.
	 *
	 * @return a float describing the current factor of efficiency
	 * when gathering.
	 */
	public float getGatherEfficiency() {
		return	percentToFactor(gatherSkill) * percentToFactor(health);

	}

	/**
	 * Returns a float describing the current factor of efficiency
	 * when sacrificing.
	 *
	 * @return a float describing the current factor of efficiency
	 * when sacrificing.
	 */
	public float getSacrificeEfficiency() {
		return	percentToFactor(sacrificeSkill) * percentToFactor(health);
	}

	/**
	 * Returns a float describing the current factor of efficiency
	 * when transporting.
	 *
	 * @return a float describing the current factor of efficiency
	 * when transporting.
	 */
	public float getTransportationEfficiency() {
		return	percentToFactor(transportationSkill) * percentToFactor(health);
	}

	/**
	 * Increases the value of the gather skill, using a formula containing
	 * getTimeSinceCycle and getSkillIncreaseSpeed.
	 */
	public void increaseGatherSkill() {
		// Children to dont get better or worse in any skills.
		if(!isChild()) {
			// Increases the gather skill with the speed multiplied by the
			// timeSinceCycle, but then modified with a factor of how close
			// the maximum skill value the current skill is. This means that
			// the skill will never increase to over 1f.
			gatherSkill += World.getEnvironment().getSkillIncreaseSpeed() *
					World.getWorld().getTimeSinceCycle() *
					(1f - gatherSkill);
			// Decrease the other two skills.
			decreaseSacrificeSkill();
			decreaseTransportationSkill();
		}
	}

	/**
	 * Increases the value of the sacrificing skill, using a formula containing
	 * getTimeSinceCycle and getSkillIncreaseSpeed.
	 */
	public void increaseSacrificeSkill() {
		// Children to dont get better or worse in any skills.
		if(!isChild()) {
			// Increases the sacrifice skill with the speed multiplied by the
			// timeSinceCycle, but then modified with a factor of how close
			// the maximum skill value the current skill is. This means that
			// the skill will never increase to over 1f.
			sacrificeSkill += World.getEnvironment().getSkillIncreaseSpeed() *
					World.getWorld().getTimeSinceCycle() *
					(1f - sacrificeSkill);
			// Decrease the other two skills.
			decreaseGatherSkill();
			decreaseTransportationSkill();
		}
	}

	/**
	 * Increases the value of the transportation skill, using a formula
	 * containing getTimeSinceCycle and getSkillIncreaseSpeed.
	 */
	public void increaseTransportationSkill() {
		// Children to dont get better or worse in any skills.
		if(!isChild()) {
			// Increases the transportation skill with the speed multiplied by the
			// timeSinceCycle, but then modified with a factor of how close
			// the maximum skill value the current skill is. This means that
			// the skill will never increase to over 1f.
			transportationSkill += World.getEnvironment().getSkillIncreaseSpeed() *
					World.getWorld().getTimeSinceCycle() *
					(1f - transportationSkill);
			// Decrease the other two skills.
			decreaseSacrificeSkill();
			decreaseGatherSkill();
		}
	}

	/**
	 * Calculates if the owner of the characteristics is hungry, and then
	 * returns the result as a boolean.
	 *
	 * @returns a boolean stating if the owner of the characteristics is hungry.
	 */
	public boolean isHungry() {

		// Make it more likely to eat when we're really hungry.
		float probabilityModifier =
				World.getEnvironment().getDefaultNeedValue() / saturation;

		// Time to eat?
		if (hungryRandomizer.isSuccessful(probabilityModifier)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Calculates if the owner of the characteristics is depressed, and then
	 * returns the result as a boolean.
	 *
	 * @returns a boolean stating if the owner of the characteristics is depressed.
	 */
	public boolean isDepressed() {

		// Make it more likely to eat when we're really depressed.
		float probabilityModifier =
				World.getEnvironment().getDefaultNeedValue() / happiness;

		// Time to get happy?
		if (depressedRandomizer.isSuccessful(probabilityModifier)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Calculates if the owner of the characteristics is unsafe, and then
	 * returns the result as a boolean.
	 *
	 * @returns a boolean stating if the owner of the characteristics is unsafe.
	 */
	public boolean isUnsafe() {

		// Make it more likely to feel unsafe when the safety is very low
		float probabilityModifier =
				World.getEnvironment().getDefaultNeedValue() / safety;

		// Time to get safe?
		if (unsafeRandomizer.isSuccessful(probabilityModifier)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Randomizes if the individual feel to take a stroll and returns the result
	 * as a boolean.
	 *
	 * @returns a boolean stating if the owner of the characteristics is feeling
	 * "strolly".
	 */
	public boolean isStrolly() {
		// Time to take a stroll?
		if (strollRandomizer.isSuccessful()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Decreases the value of the gather skill, using a formula containing
	 * getTimeSinceCycle and getSkillDecreaseSpeed.
	 */
	private void decreaseGatherSkill() {
		// Decreases the gather skill with the speed multiplied by the
		// timeSinceCycle, but then modified with a factor of how close
		// the mininimum skill value the current skill is. This means that
		// the skill will never decrease to less than 0f.
		gatherSkill -= World.getEnvironment().getSkillDecreaseSpeed() *
				World.getWorld().getTimeSinceCycle() *
				gatherSkill;
	}

	/**
	 * Decreases the value of the sacrificing skill, using a formula containing
	 * getTimeSinceCycle and getSkillDecreaseSpeed.
	 */
	private void decreaseSacrificeSkill() {
		// Decreases the sacrifice skill with the speed multiplied by the
		// timeSinceCycle, but then modified with a factor of how close
		// the mininimum skill value the current skill is. This means that
		// the skill will never decrease to less than 0f.
		sacrificeSkill -= World.getEnvironment().getSkillDecreaseSpeed() *
				World.getWorld().getTimeSinceCycle() *
				sacrificeSkill;
	}

	/**
	 * Decreases the value of the transportation skill, using a formula
	 * containing getTimeSinceCycle and getSkillDecreaseSpeed.
	 */
	private void decreaseTransportationSkill() {
		// Decreases the transportation skill with the speed multiplied by the
		// timeSinceCycle, but then modified with a factor of how close
		// the mininimum skill value the current skill is. This means that
		// the skill will never decrease to less than 0f.
		transportationSkill -= World.getEnvironment().getSkillDecreaseSpeed() *
				World.getWorld().getTimeSinceCycle() *
				transportationSkill;
	}

	/**
	 * Returns an integer representing the age in pseudo years, as and
	 * individual would experience it. The age is calculated from a constant
	 * (getTimeYearRatio()).
	 *
	 * @return a int representing the age in pseudo years.
	 */
	public int getAge() {
		return (int) (
				(World.getWorld().getWorldTime() - timeOfBirth) /
				World.getEnvironment().getYearSecondsRatio()
				) + startAge;
	}

	/**
	 * Tests if the current age (getAge) is under a constant getAdultAge(). If
	 * so, the Individual is a child.
	 *
	 * @return a boolean stating if the Indiviudal is a child.
	 */
	public boolean isChild() {
		return getAge() < World.getEnvironment().getAdultAge();
	}


	/**
	 * "Converts" a percentual value (like a skill) to a factor value (like the efficiency).
	 * A percentual value goes from 0 to 1f and has a default value of 0.5f, a factor goes
	 * from 0 to infinity and has a default value of 1f.
	 *
	 * @param a percentual value to be converted.
	 * @return the given percentual value as a factor value.
	 */
	private float percentToFactor(float percentualValue) {
		// Takes for example a skill, which is default 0.5f and adds 0.5f
		// resulting in a factor of 1f, which would (correctly) not affect a
		// value it's multiplied with.
		return percentualValue + World.getEnvironment().getDefaultSkillValue();
	}

}
