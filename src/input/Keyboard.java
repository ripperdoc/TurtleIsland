/* 
 * 2003-05-18 EliasAE
 * 	Formatting.
 *
 * 2003-04-13 EliasAE
 * 	Implemented all methods and removed the method keyboardEvent
 * 	because I could see no use for it or even what it was supposed
 * 	to do.
 *
 * 2003-04-11 pergamon
 *  Formatted sytax. Added "implements KeyListener".
 */
package input;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * A class to get input from the user through the keyboard.
 */
public class Keyboard implements KeyListener {

	/**
	 * The input event translator that will be called when a keyboard
	 * event is recieved.
	 */
	private InputEventTranslator inputEventTranslator;

	/**
	 * Map with keys that is an Integer for each constant. The constants
	 * is the ones for virtual keys from KeyEvent.
	 */
	private Set pressedKeys;

	/**
	 * Creates a Keyboard that will send events of key presses
	 * to a InputEventTranslator.
	 *
	 * @param inputEventTranslator the input event translator to send
	 * notifications to.
	 */
	public Keyboard(InputEventTranslator inputEventTranslator) {
		this.inputEventTranslator = inputEventTranslator;
		pressedKeys = new HashSet();
	}

	/**
	 * Called by Java when a key on the keyboard is pressed. Sends
	 * the key to the input event translator.
	 *
	 * @param event information about the event.
	 */
	public void keyPressed(KeyEvent event) {
		// Records that this key is now down.
		pressedKeys.add(new Integer(event.getKeyCode()));
		inputEventTranslator.keyDown(event.getKeyCode());
	}

	/**
	 * Called by Java when a key on the keyboard is released. Sends
	 * the key to the input event translator.
	 *
	 * @param event information about the event.
	 */
	public void keyReleased(KeyEvent event) {
		// This key is no longer down. Remove it from the set of
		// pressed keys.
		pressedKeys.remove(new Integer(event.getKeyCode()));
		inputEventTranslator.keyUp(event.getKeyCode());
	}

	/**
	 * Called by Java when the user presses and releases one or more
	 * keys and those keys together forms a unicode character.
	 * This event is not used.
	 *
	 * @param event information about the event.
	 */
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * Checks if a key is pressed at this moment. TODO this method
	 * will not produce correct results if the used holds down
	 * a key when the application loses focus and then releases it
	 * when another application has focus. If that occurrs this
	 * method will return true for that key until it is released
	 * when this application has focus.
	 *
	 * @param key the keycode to check for. This is one of the constans
	 * in java.awt.KeyEvent that is begins VK_
	 * @return true if the key with the id passed as parameter is pressed,
	 * otherwise false.
	 */
	public boolean isKeyPressed(int key) {
		return pressedKeys.contains(new Integer(key));
	}

}
