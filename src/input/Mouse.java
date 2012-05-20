/*
 * 2003-05-18 EliasAE
 * 	Formatting.
 *
 * 2003-04-10 pergamon
 *	Listen to if the left, middle and right button is pressed.
 *
 * 2003-04-11 EliasAE
 * 	Listen to if the left or right button is released.
 */
package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * A class to get input from the user through the mouse and send it
 * to an input event translator.
 */
public class Mouse implements MouseListener, MouseMotionListener {

	/**
	 * The input event translator that will be called when a mouse
	 * event is recieved.
	 */
	private InputEventTranslator inputEventTranslator;

	/**
	 * Creates a mouse.
	 *
	 * @param inputEventTranslator the object that will be called when
	 * a mouse event is recieved.
	 */
	public Mouse(InputEventTranslator inputEventTranslator) {
		this.inputEventTranslator = inputEventTranslator;
	}

	/**
	 * Nothing, this event is not used.
	 *
	 * @param e information about the event.
	 */
	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * Nothing, this event is not used.
	 *
	 * @param e information about the event.
	 */
	public void mouseEntered(MouseEvent e) {

	}

	/**
	 * Nothing, this event is not used.
	 *
	 * @param e information about the event.
	 */
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * Called when a mouse button is pressed. Calls the right method in
	 * inputEventTranslator.
	 *
	 * @param e information about the event.
	 */
	public void mousePressed(MouseEvent e) {
		if (MouseEvent.BUTTON1 == e.getButton()) {
			inputEventTranslator.leftMouseButtonDown(e.getX(), e.getY());
		} else if (MouseEvent.BUTTON3 == e.getButton()) {
			inputEventTranslator.rightMouseButtonDown(e.getX(), e.getY());
		}
	}

	/**
	 * Called when a mouse button is released. Calls the right method in
	 * inputEventTranslator.
	 *
	 * @param e information about the event.
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			inputEventTranslator.leftMouseButtonUp(e.getX(), e.getY());
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			inputEventTranslator.rightMouseButtonUp(e.getX(), e.getY());
		}
	}

	/**
	 * Called when the mouse is dragged, that is moved with a button down. 
	 * Calls the right method in 
	 * inputEventTranslator.
	 *
	 * @param e information about this event.
	 */
	public void mouseDragged(MouseEvent e) {
		inputEventTranslator.mouseDragged(e.getX(), e.getY());
	}

	/**
	 * Called when the mouse is moved. Calls the right method in 
	 * inputEventTranslator.
	 *
	 * @param e information about this event.
	 */
	public void mouseMoved(MouseEvent e) {
		inputEventTranslator.mouseMoved(e.getX(), e.getY());
	}

}
