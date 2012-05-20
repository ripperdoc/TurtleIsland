/*
 * 2003-05-12 henko
 *  Created and implemented. Contains the main() method and only creates
 *  a pregame.TurtleIslandGame object and uses it.
 */

import pregame.TurtleIslandGame;

/**
 * This class is the one that is called by the user and is responsible for
 * starting up a new game. It only functions as a wrapper for TurtleIslandGame.
 */
public class TurtleIsland {
	
	/**
	 * The main function, started by the user. It creates a new Turtle Island
	 * Game and starts it.
	 * 
	 * @param args command line input from the user.
	 */
	public static void main(String[] args) {
		TurtleIslandGame turtleIslandGame = new TurtleIslandGame();
		turtleIslandGame.start();
	}
	
}
