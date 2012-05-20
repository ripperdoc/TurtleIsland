/*
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-17 pergamon
 *  moved the remove of dead gameObjects from this.draw() to
 *  View.updateGameObjects();
 *
 * 2003-05-?? pergamon
 *  Changed how sub objects are added and removed. Now uses hashmap to be able
 *  to get specific objects.
 *
 * 2003-05-01 pergamon
 *  added private setOwner and removed the constructor with an owner as
 *  parameter.
 *
 * 2003-04-30 EliasAE
 * 	Added sub objects.
 *
 * 2003-04-16 pergamon
 *  Took away the variable "List renderables" from the class and made
 *  draw(Graphics2D graphics) to an abstract function.
 *
 * 2003-04-13 EliasAE
 * 	Changed to using the Rendeable interface.
 *
 * 2003-04-11 pergamon
 * 	Formatted syntax (not indent on comments). Added all functionality for all
 *  functions.
 */
package graphicsengine;

import java.awt.Graphics2D;
import java.util.*;
import java.util.Collection;
import gameengine.gameobjects.*;

/**
 * A graphical representation of an object on screen. It can have an owner,
 * if so its position is relative to this object.
 */
public abstract class GraphicalObject {

	/**
	 * This object's top-left screen x-coordinate relative the upper left corner of
	 * its owner. If there is no owner (that is owner is null) this position is
	 * relative to the upper left corner of the screen.
	 */
	private int relativeX;

	/**
	 * This object's top-left screen y-coordinate relative the upper left corner of
	 * its owner. If there is no owner (that is owner is null) this position is
	 * relative to the upper left corner of the screen.
	 */
	private int relativeY;

	/**
	 * The owner of this object. If this object does not have an owner this
	 * variable is null.
	 */
	protected GraphicalObject owner;

	/**
	 * All sub objects for this graphical object.
	 */
	private HashMap subObjects;

	/**
	 * If sub objects should be drawn.
	 */
	private boolean showSubObjects;

	/**
	 * If this object is visible or not.
	 */
	private boolean visible;

	/**
	 * Creates a Graphical object that can have several renderables and a onscreen
	 * position.
	 *
	 * @param relativeX The screen x-coordinate for this object relative
	 * to the parent.
	 * @param relativeY The screen y-coordinate for this object relative
	 * to the parent.
	 */
	public GraphicalObject(int relativeX, int relativeY) {
		setRelativeX(relativeX);
		setRelativeY(relativeY);
		showSubObjects = true;
		visible = true;
		subObjects = new HashMap();
	}

	/**
	 * Draw the GraphicalObject.
	 *
	 * @param graphics the graphics object to draw on.
	 */
	public void draw(Graphics2D graphics) {
		if (showSubObjects) {
			if (subObjects != null) {
				Iterator subObjectsIterator = subObjects.values().iterator();
				try {
					while (subObjectsIterator.hasNext()) {
						GraphicalObject subObject =
								(GraphicalObject) subObjectsIterator.next();
						if (subObject instanceof GraphicalGameObject) {
							GameObject gameObject =
									((GraphicalGameObject) subObject).getGameObject();
							// If a GameObject have died.
							if (gameObject.isDeleted()) {
								subObjectsIterator.remove();
								continue;
							}
						}
						subObject.draw(graphics);
					}
				}
				catch (ConcurrentModificationException e) {
					if (Debug.getDebug()) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Retrieves the relative x-position of this object.
	 *
	 * @return the x-position of this object relative to the upper left corner
	 * of its owner in pixels.
	 */
	public int getRelativeX() {
		return relativeX;
	}

	/**
	 * Retrieves the relative y-position of this object.
	 *
	 * @return the y-position of this object relative to the upper left corner
	 * of its owner in pixels.
	 */
	public int getRelativeY() {
		return relativeY;
	}

	/**
	 * Sets the relative x-position of this object.
	 *
	 * @param relativeX the x-position of this object relative to the upper
	 * left corner of its owner in pixels.
	 */
	protected void setRelativeX(int relativeX) {
		this.relativeX = relativeX;
	}

	/**
	 * Sets the relative y-position of this object.
	 *
	 * @param relativeY the y-position of this object relative to the upper
	 * left corner of its owner in pixels.
	 */
	protected void setRelativeY(int relativeY) {
		this.relativeY = relativeY;
	}

	/**
	 * Sets the on screen x-position of this object.
	 *
	 * @param screenX the x-position of this object relative to the upper
	 * left corner of its owner in pixels.
	 */
	public void setScreenX(int screenX) {
		int ownerScreenX = 0;
		if (owner != null) {
			ownerScreenX = owner.getScreenX();
		}
		relativeX = screenX - ownerScreenX;
	}

	/**
	 * Sets the on screen y-position of this object.
	 *
	 * @param screenY the y-position of this object relative to the upper
	 * left corner of the screen.
	 */
	public void setScreenY(int screenY) {
		int ownerScreenY = 0;
		if (owner != null) {
			ownerScreenY = owner.getScreenY();
		}
		relativeY = screenY - ownerScreenY;
	}

	/**
	 * Retrieves the x-position of this object on the screen.
	 *
	 * @return the x-position of this object's upper left corner relative to
	 * the upper left corner of the screen.
	 */
	public int getScreenX() {
		int ownerScreenX = 0;
		if (owner != null) {
			ownerScreenX = owner.getScreenX();
		}
		return ownerScreenX + relativeX;
	}

	/**
	 * Retrieves the y-position of this object on the screen.
	 *
	 * @return the y-position of this object's upper left corner relative to
	 * the upper left corner of the screen.
	 */
	public int getScreenY() {
		int ownerScreenY = 0;
		if (owner != null) {
			ownerScreenY = owner.getScreenY();
		}
		return ownerScreenY + relativeY;
	}

	/**
	 * Sets if sub objects should be drawn.
	 */
	public void setShowSubObjects(boolean show) {
		showSubObjects = show;
	}

	/**
	 * Retrieve if the sub objects of this object is shown or not.
	 *
	 * @return true if the sub objects is shown, otherwise false.
	 */
	public boolean isSubObjectsShown() {
		return showSubObjects;
	}

	/**
	 * Sets if this object is visible or not.
	 *
	 * @param visible true if it should be visible, othervise false.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Retrive if this object is visible or not.
	 *
	 * @return true if it's visible, othervise false.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Just to add a subObject with no possibility to retrive it explicitly
	 * later.
	 *
	 * @param subObject
	 */
	protected void addSubObject(GraphicalObject subObject) {
		addSubObject(subObject, subObject);
	}

	/**
	 * Adds a sub object to this object. This will be painted along
	 * with the object when setShowSubObjects(true) has been called.
	 *
	 * @param subObject the new sub object.
	 */
	protected void addSubObject(Object key, GraphicalObject subObject) {
		// set the owner to this object.
		subObject.owner = this;
		subObjects.put(key, subObject);
	}

	protected void removeSubObject(GraphicalObject subObject) {
		subObjects.remove(subObject);
	}

	/**
	 * Retrieves the subobject with a given key.
	 *
	 * @param key the key for the subobject to retrieve. Was specified
	 * in addSubObject.
	 * @return the subobject.
	 */
	protected GraphicalObject getSubObject(Object key) {
		return (GraphicalObject) subObjects.get(key);
	}

	/**
	 * Retrives all sub objects.
	 *
	 * @return Return all subobjects.
	 */
	protected Collection getSubObjects() {
		return subObjects.values();
	}

	/**
	 * Retrieves the description of this graphical object.
	 *
	 * @return the label with the description.
	 */
	public Label getDescription() {
		return (Label) subObjects.get("descriptionLabel");
	}

	/**
	 * Sets the description of this graphical object.
	 *
	 * @param label the label with the description.
	 */
	public void setDescription(Label label) {
		label.owner = this;
		subObjects.put("descriptionLabel", label);
	}

}
