/* 2003-05-12 Martin
 *	Commented correctly.
 *
 * 2003-05-11 henko
 *  Created.
 */
package gameengine;

/**
 * Randomizer is a class that periodically will test if a random number is
 * less or equal to a probability. The probability is calculated from a given
 * interval that represents the mean time between successful tests. A
 * probability modifier can be set to make it more or less likely that the
 * test will be successful. This modifier can change over time, whereas the
 * mean interval cannot.
 */
public class ProbabilityRandomizer extends Randomizer {

	/**
	 * Creates a ProbabilityRandomizer object with a given default probability
	 * and a modifier.
	 *
	 * @param defaultProbability the default probability that the randomization
	 * will be successful each randomize interval.
	 * @param probabilityModifier value used to modify the probability that the
	 * ProbabilityRandomizer will be successful.
	 */
	public ProbabilityRandomizer(float defaultProbability, float probabilityModifier) {
		probability = defaultProbability;
		this.probabilityModifier = probabilityModifier;
	}

	/**
	 * Creates a ProbabilityRandomizer object with a given default probability
	 * but no modifier.
	 *
	 * @param defaultProbability the default probability that the randomization
	 * will be successful each randomize interval.
	 */
	public ProbabilityRandomizer(float defaultProbability) {
		this(defaultProbability, 1f);
	}

}
