/* 
 * 2003-05-12 henko
 *  Made the class implement Serializable.
 *
 * 2003-05-11 henko
 *	Changed to abstract. Two different implementations are available:
 *  IntervallRandomizer and ProbabilityRandomizer.
 *
 * 2003-05-10 henko
 *	Implemented.
 *
 * 2003-05-05 henko
 *	Created class structure.
 */

package gameengine;

import java.io.Serializable;

/**
 * Randomizer is a class that periodically will test if a random number is
 * less or equal to a probability. The probability is calculated in different
 * ways depending on the implemention of sublcass. A probability modifier can 
 * be set to make it more or less likely that the test will be successful. 
 * This modifier can change over time, whereas the default probability cannot.
 */
public abstract class Randomizer implements Serializable {

	/**
	 * Represents the probability each second that the Randomizer will be
	 * successful. Value is within the inverval [0,1].
	 */
	protected float probability;

	/**
	 * ProbabilityModifier is used to modify the probability that the
	 * Randomizer will be successful. A value above 1 will make the probability
	 * larger, and a value below 1 will make it smaller.
	 */
	protected float probabilityModifier;
	
	/**
	 * Counter that remembers the time in seconds since last the Randomizer was
	 * last successful. Will be reset when raised above 1.
	 */
	private float timeAtLastAttempt;

	/**
	 * Returns whether the Randomizer is successful or not. This function is
	 * supposed to be called each cycle. Will set the probability modifier to
	 * the given value before testing if successful.
	 *
	 * @param probabilityModifier value used to modify the probability that the
	 * 		  Randomizer will be successful.
	 * @return whether the Randomizer is successful or not.
	 */
	public boolean isSuccessful(float probabilityModifier) {
		boolean successful = false;
		this.probabilityModifier = probabilityModifier;

		// Update counter

		// Time to roll the dices?
		if (World.getWorld().getWorldTime() - timeAtLastAttempt >
				World.getEnvironment().getRandomizeInterval()) {
			// Make the test
			if (Math.random() <= probability * probabilityModifier) {
				successful = true;
			}

			// Reset the counter
			timeAtLastAttempt = World.getWorld().getWorldTime();
		}

		return successful;
	}

	/**
	 * Returns whether the Randomizer is successful or not. This function is
	 * supposed to be called each cycle. Will use the last probabilityModifer
	 * set.
	 *
	 * @return whether the Randomizer is successful or not.
	 */
	public boolean isSuccessful() {
		return isSuccessful(probabilityModifier);
	}

	/**
	 * Returns the probability modifier. This value is used to modify the
	 * probability that the Randomizer will be successful. A value above 1 will
	 * make the probability larger, and a value below 1 will make it smaller.
	 *
	 * @return the probability modifier.
	 */
	public float getProbabilityModifier() {
		return probabilityModifier;
	}

}
