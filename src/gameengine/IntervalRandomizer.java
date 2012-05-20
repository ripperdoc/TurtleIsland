/*
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
public class IntervalRandomizer extends Randomizer {
	
	/**
	 * Creates a Randomizer object. Supports setting the wanted mean time
	 * between successful random tests. Also supports the setting of a
	 * probability modifier.
	 *
	 * @param meanTimeBetweenSuccessful the mean time between successful tests.
	 * @param probabilityModifier value used to modify the probability that the
	 * 		  Randomizer will be successful.
	 */
	public IntervalRandomizer(float meanTimeBetweenSuccessful, float probabilityModifier) {
		if (meanTimeBetweenSuccessful != 0) {
			probability = World.getEnvironment().getRandomizeInterval() /
					meanTimeBetweenSuccessful;
		} else {
			probability = 1;
		}		
		// Calculate the probability and set local values.

		this.probabilityModifier = probabilityModifier;
	}

	/**
	 * Creates a Randomizer object. Supports setting the wanted mean time
	 * between successful random tests. Will set the probability modifier to 1.
	 * That means, no modification.
	 *
	 * @param meanTimeBetweenSuccessful the mean time between when the
	 *		  Randomizer are successful.
	 */
	public IntervalRandomizer(float meanTimeBetweenSuccessful) {
		this(meanTimeBetweenSuccessful, 1f);
	}

}
