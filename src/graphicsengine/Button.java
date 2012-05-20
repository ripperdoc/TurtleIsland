/*
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-04-30 EliasAE
 * 	Changed representedGraphicalGameObject to representedObject to allow for
 * 	resources to be represented. Used in the buttons for selection of
 * 	resource type during transportation task build up.
 *
 * 2003-04-16 pergamon
 *  Wrote the class using ButtonGraphics, changed constructors and wrote draw.
 *
 * 2003-04-14 pergamon
 *  Implementing the class.
 *
 * 2003-04-13 EliasAE
 * 	Changed to using the Rendeable interface.
 *
 * 2003-04-11 pergamon
 * 	Formatted syntax. Fixed the constructors.
 */
package graphicsengine;

import java.awt.Graphics2D;
import java.util.Collection;
import graphicsengine.graphics.ButtonGraphics;

/**
 * A button that the user can press. Positioned relative to its owner.
 */
public class Button extends GraphicalObject implements Selectable {

	/**
	 * This is used when the button represents an other Object, for example
	 * when the button is on the InformationPanel and represents a Selected
	 * GraphicalGameObject.
	 */
	private Object representedObject;

	/**
	 * The graphics of the button.
	 */
	private ButtonGraphics buttonGraphics;

	/**
	 * Represents this object's width. Together with this object's height it
	 * makes a rectangle that onscreen counts as a selectable area.
	 */
	private int width;

	/**
	 * Represents this object's height. Together with this object's width it
	 * makes a rectangle that onscreen counts as a selectable area.
	 */
	private int height;

	/**
	 * If this object is selected or not.
	 */
	private boolean selected;

	/**
	 * If the button is active or not.
	 */
	private boolean active;

	/**
	 * Creates a graphical button with the graphics that it will be seen as
	 * on screen, a position relative to its owner and the width and height
	 * from the buttonGraphics. If the owner is null the coordinates will
	 * be relative to the screen.
	 *
	 * @param buttonGraphics The graphics that represent this object on screen.
	 * @param relativeX this objects relative x-position to its owner.
	 * @param relativeY this objects relative y-position to its owner.
	 */
	public Button(ButtonGraphics buttonGraphics, int relativeX, int relativeY) {
		super(relativeX, relativeY);
		this.buttonGraphics = buttonGraphics;
		this.width = buttonGraphics.getActive().getWidth();
		this.height = buttonGraphics.getActive().getHeight();
	}

	/**
	 * Creates a graphical button with the graphics that it will be seen as
	 * on screen, a position relative to its owner, a connection to a
	 * graphicalGameObject which this button represent and the width and
	 * height from the buttonGraphics.
	 *
	 * @param buttonGraphics The graphics that represent this object on screen.
	 * @param representedObject the object that this button represents.
	 * @param relativeX this objects relative x-position to its owner.
	 * @param relativeY this objects relative y-position to its owner.
	 */
	public Button(ButtonGraphics buttonGraphics,
			Object representedObject, int relativeX, int relativeY) {
		this(buttonGraphics, relativeX, relativeY);
		this.representedObject = representedObject;
	}

	/**
	 * Does return the width of this object.
	 *
	 * @return The width of this object.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Does return the height of this object.
	 *
	 * @return The height of this object.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the renderables for this button.
	 *
	 * @param buttonGraphics a list of renderables.
	 */
	public void setRenderables(ButtonGraphics buttonGraphics) {
		this.buttonGraphics = buttonGraphics;
	}

	/**
	 * Draw this Button on the screen using its buttonGraphics.
	 *
	 * @param graphics the Graphics object to draw itself onto.
	 */
	public void draw(Graphics2D graphics) {
		if (isVisible()) {
			Renderable tmpRenderable;
			if (selected) {
				if (active) {
					tmpRenderable = buttonGraphics.getPressedActive();
				} else {
					tmpRenderable = buttonGraphics.getPressedInactive();
				}
			} else {
				if (active) {
					tmpRenderable = buttonGraphics.getActive();
				} else {
					tmpRenderable = buttonGraphics.getInactive();
				}
			}
			tmpRenderable.render(graphics, getScreenX(), getScreenY());

			super.draw(graphics);
		}
	}

	/**
	 * Retrieves if the object is active or not.
	 *
	 * @return true if the object is active, otherwise false.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets if the object should be active or not.
	 *
	 * @param active true if the object should become active, false
	 * if the object should become inactive.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Retrieves if the object is selected or not.
	 *
	 * @return true if the object is selected, otherwise false.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets if the object should be selected or not.
	 *
	 * @param selected true if the object should become selected, false
	 * if the object should become not selected.
	 */
	public void setSelected(boolean selected) {
		if (representedObject != null	&& representedObject instanceof Selectable) {
			((Selectable) representedObject).setSelected(selected);
		}
		this.selected = selected;
	}

	/**
	 * Does a check if the inparameter position is within this object's area.
	 *
	 * @param screenX The x-position to check against.
	 * @param screenY The y-position to check against.
	 * @return true if the position was within this objects area, otherwise
	 * false.
	 */
	public boolean hitTest(int screenX, int screenY) {
		boolean tmpHit = false;
		int tmpPosX = getScreenX();
		int tmpPosY = getScreenY();
		if ((screenX >= tmpPosX) &&
				(screenX <= tmpPosX + width) &&
				(screenY >= tmpPosY) &&
				(screenY <= tmpPosY + height)) {
			tmpHit = true;
		}
		return tmpHit;
	}

	/**
	 * Does a check if this object is within the rectangle that are
	 * inparameters. This function does not have a function for a button.
	 * Always return false.
	 *
	 * @param screenLeftX The top left x-position of the rectangle to check
	 * against, inclusive bound, in screen coordinates.
	 * @param screenTopY The top left y-position of the rectangle to check
	 * against, inclusive bound, in screen coordinates.
	 * @param screenRightX The bottom right x-position of the rectangle to
	 * check against, exclusive bound, in screen coordinates.
	 * @param screenBottomY The bottom right y-position of the rectangle to
	 * check against, exclusive bound, in screen coordinates.
	 * @return false
	 */
	public boolean hitTest(
			int screenLeftX,
			int screenTopY,
			int screenRightX,
			int screenBottomY
			) {
		return false;
	}

	/**
	 * Returns the object represented by this button. Can be any object. If it
	 * is a selectable object, the selection is trasfered to that object.
	 *
	 * @return the object represented by this button.
	 */
	public Object getRepresentedObject() {
		return representedObject;
	}

}
