/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-04-16 pergamon
 *  removed shape and added hotspotX and Y. Corrected the getAngle(float).
 *
 * 2004-04-16 pergamon
 *  Added shape.
 *
 * 2003-04-16 pergamon
 *  created the class
 */

package graphicsengine.graphics;

import graphicsengine.Renderable;
import java.util.List;
import java.awt.Polygon;

/**
 * Package for all the graphics that a game object needs. Created by
 * GraphicsManager.
 */
public class GameObjectGraphics {

	/**
	 * List of renderables that displays the game object in difference angles.
	 */
	private List angles;

	/**
	 * Renderable that will be painted along with one of the angles when the 
	 * game object is slected. This will be painted on top of the angles.
	 */
	private Renderable selected;

	/**
	 * Renderable that will be painted instead of the angles when the 
	 * game object is eliminated.
	 */
	private Renderable eliminated;

	/**
	 * Where the GameObject's position Point should be relative to the top-left
	 * coordinate of the Renderables.
	 */
	private int hotspotX;

	/**
	 * Where the GameObject's position Point should be relative to the top-left
	 * coordinate of the Renderables.
	 */
	private int hotspotY;

	/**
	 * Creates a GameObjectGraphics with information on how it "looks" and is
	 * represented.
	 *
	 * @param shape A model over how the object really take up space in the
	 * gameworld.
	 * @param angles The graphics of the GameObject in the represented angles.
	 * @param selected The graphics to show that the object is selected.
	 * @param eliminated The graphics to show when the object is eliminated.
	 */
	public GameObjectGraphics(List angles, Renderable selected,
			Renderable eliminated, int hotspotX, int hotspotY) {
		this.angles = angles;
		this.selected = selected;
		this.eliminated = eliminated;
		this.hotspotX = hotspotX;
		this.hotspotY = hotspotY;
	}

	/**
	 * Returns the correct Renderable for a specific angle.
	 *
	 * @param angle What angle to get a Renderable for.
	 * @return the Renderable that represents the given angle.
	 */
	public Renderable getAngle(float angle) {
		if (angles.size() == 0) {
			return null;
		}
		float sectorDegree = (float) ((2 * Math.PI) / angles.size());
		int renderableNumber = Math.round(angle / sectorDegree) % angles.size();
		return (Renderable) angles.get(renderableNumber);
	}

	/**
	 * Get the Renderable to show that the object is selected.
	 *
	 * @return the selected Renderable
	 */
	public Renderable getSelected() {
		return selected;
	}

	/**
	 * Get the Renderable to show that the object is eliminated.
	 *
	 * @return the eliminated Renderable
	 */
	public Renderable getEliminated() {
		return eliminated;
	}

	/**
	 * Retrives where the GameObject's x-position should be relative to the
	 * top-left corner of the Renderables.
	 *
	 * @return the hotspot x-coordinate relative to the top-left of the Renderables
	 */
	public int getHotspotX() {
		return hotspotX;
	}

	/**
	 * Retrives where the GameObject's y-position should be relative to the
	 * top-left corner of the Renderables.
	 *
	 * @return the hotspot y-coordinate relative to the top-left of the Renderables
	 */
	public int getHotspotY() {
		return hotspotY;
	}

}
