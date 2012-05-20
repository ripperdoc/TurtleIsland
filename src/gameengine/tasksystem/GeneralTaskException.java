/* 2003-05-11 Martin
 *	Just made some corrections of the generated class.
 */
package gameengine.tasksystem;

import java.util.*;

/**
 * An exception from the task system that indicates an incorrect
 * use of a task, often executing a task that is already done.
 */
public class GeneralTaskException extends Exception{

	/**
	 * The task that threw the exception.
	 */
	private Task thrower;

	/**
	 * Constructs a GeneralTaskException.
	 */
	public GeneralTaskException() {
		super();
	}

	/**
	 * Constructs a GeneralTaskException with the given error message.
	 */
	public GeneralTaskException(String errorMessage) {
		super(errorMessage);
	}
}