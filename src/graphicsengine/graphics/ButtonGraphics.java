/* 
 * 2003-05-20 EliasAE
 * 	Added support for some nulls in the constructor.
 *
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2004-04-16 pergamon
 *  created the class.
 */

package graphicsengine.graphics;

import graphicsengine.Renderable;
import java.awt.Polygon;

/**
 * A class to contain all of the graphics for a button.
 */
public class ButtonGraphics {

	/**
	 * Renderable for an inactive button.
	 */
	private Renderable inactive;

	/**
	 * Renderable for an active button.
	 */
	private Renderable active;

	/**
	 * Renderable for a pressed inactive button.
	 */
	private Renderable pressedInactive;

	/**
	 * Renderable for a pressed active button.
	 */
	private Renderable pressedActive;

	/**
	 * Where the ButtonObject's position Point should be relative to the top-left
	 * coordinate of the Renderables.
	 */
	private int hotspotX;

	/**
	 * Where the ButtonObject's position Point should be relative to the top-left
	 * coordinate of the Renderables.
	 */
	private int hotspotY;

	/**
	 * Create a button with Renderables for each state it can be
	 * in.
	 *
	 * @param inactive The Renderable to show when the button is inactive.
	 * @param active The Renderable to show when the button is active.
	 * @param pressedInactive The Renderable to show when the Button is inactive and pressed.
	 * @param pressedActive The Renderable to show when the Button is active and pressed.
	 */
	public ButtonGraphics(
			Renderable inactive,
			Renderable active,
			Renderable pressedInactive,
			Renderable pressedActive,
			int hotspotX,
			int hotspotY
			) {
		// Get a non null renderable, by trying the following in alternatives
		// in priority: active, pressedActive, inactive, pressedInactive.
		Renderable nonNullRenderable = null;
		if (active != null) {
			nonNullRenderable = active;
		} else if (pressedActive != null) {
			nonNullRenderable = pressedActive;
		} else if (inactive != null) {
			nonNullRenderable = inactive;
		} else if (pressedInactive != null) {
			nonNullRenderable = pressedInactive;
		} else {
			throw new IllegalArgumentException("Must have a non-null " +
					"renderable for the button.");
		}

		if (inactive == null) {
			inactive = nonNullRenderable;
		}
		if (active == null) {
			active = nonNullRenderable;
		}
		if (pressedInactive == null) {
			pressedInactive = nonNullRenderable;
		}
		if (pressedActive == null) {
			pressedActive = nonNullRenderable;
		}
		
		this.inactive = inactive;
		this.active = active;
		this.pressedInactive = pressedInactive;
		this.pressedActive = pressedActive;
		this.hotspotX = hotspotX;
		this.hotspotY = hotspotY;
	}

	/**
	 * To get the Renderable for wich the button is inactive.
	 *
	 * @return the inactive Renderable.
	 */
	public Renderable getInactive() {
		return inactive;
	}

	/**
	 * To get the Renderable for wich the button is active.
	 *
	 * @return the active Renderable.
	 */
	public Renderable getActive() {
		return active;
	}

	/**
	 * To get the Renderable for wich the button is pressed and inactive.
	 *
	 * @return the pressedInactive Renderable.
	 */
	public Renderable getPressedInactive(){
		return pressedInactive;
	}

	/**
	 * To get the Renderable for wich the button is pressed and Active.
	 *
	 * @return the pressedActive Renderable.
	 */
	public Renderable getPressedActive() {
		return pressedActive;
	}

	/**
	 * Retrives where the ButtonObject's x-position should be relative to the
	 * top-left corner of the Renderables.
	 *
	 * @return the hotspot x-coordinate relative to the top-left of the Renderables
	 */
	public int getHotspotX() {
		return hotspotX;
	}

	/**
	 * Retrives where the ButtonObject's y-position should be relative to the
	 * top-left corner of the Renderables.
	 *
	 * @return the hotspot y-coordinate relative to the top-left of the Renderables
	 */
	public int getHotspotY() {
		return hotspotY;
	}

}
