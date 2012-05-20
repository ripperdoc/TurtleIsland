/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-04-30 EliasAE
 * 	Fixed compilation errors. 
 *
 * 2003-04-30 Martin
 *	The shell finished. Much still do be done, such as giving this class any purpose at all :-)
 */
package graphicsengine;

/**
 * The Debug class controls various functions for logging important events in the code
 * and displaying important debug information on screen.
 */
public class Debug {

	/**
	 * You may not crete instances of this class.
	 */
	private Debug() {
	}

	/**
	 * The boolean that states if debug is set to true or false.
	 */
	private static boolean debugState = false;

	/**
	 * Logs (prints to System.out) the given string if the debug is set to true.
	 *
	 * @param logPost the string to print.
	 */
	public static void log(String logPost) {
		if (Debug.debugState) {
			System.out.println(logPost);
		}
	}

	/**
	 * Sets the debug state to the given value.
	 *
	 * @param debugState the boolean stating what the debugState should be set to.
	 */
	public static void setDebug(boolean debugState) {
		Debug.debugState = debugState;
	}

	/**
	 * Returns the debug state.
	 *
	 * @return the debug state.
	 */
	public static boolean getDebug() {
		return Debug.debugState;
	}

}
