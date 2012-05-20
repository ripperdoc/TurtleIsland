/*
 * 2003-05-09 pergamon
 * Created the class.
 *
 */

package gameengine.gameobjects;

/**
 * SkillsContainer means that it's implementor will have a Skills object.
 */
public interface CharacteristicsContainer {

	/**
	 * Returns an integer representing the age in pseudo years, as and
	 * individual would experience it. The age is calculated from a constant
	 * (getTimeYearRatio()).
	 *
	 * @return a int representing the age in pseudo years.
	 */
	public int getAge();

	/**
	 * Returns a float describing the current health.
	 *
	 * @return a float describing the current health.
	 */
	public float getHealth();

	/**
	 * Returns an integer describing the gender.
	 *
	 * @return an int describing the gender.
	 */
	public int getGender();

	/**
	 * Returns a float describing the current gather skill.
	 *
	 * @return a float describing the current gather skill.
	 */
	public float getGatherSkill();

	/**
	 * Returns a float describing the current sacrifice skill.
	 *
	 * @return a float describing the current sacrifice skill.
	 */
	public float getSacrificeSkill();

	/**
	 * Returns a float describing the current transportation skill.
	 *
	 * @return a float describing the current transportation skill.
	 */
	public float getTransportationSkill();

	/**
	 * Returns a float describing the current safety.
	 *
	 * @return a float describing the current safety.
	 */
	public float getSafety();

	/**
	 * Returns a float describing the current saturation.
	 *
	 * @return a float describing the current saturation.
	 */
	public float getSaturation();

	/**
	 * Returns a float describing the current happiness.
	 *
	 * @return a float describing the current happiness.
	 */
	public float getHappiness();

}