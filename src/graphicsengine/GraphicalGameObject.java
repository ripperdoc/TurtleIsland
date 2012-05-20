/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-10 Martin
 *	Corrected the use of velocity to check if the object is a VelocityContainer.
 *
 * 2003-04-16 pergamon
 *  Changed so that it now uses the GameObjectGraphics and it's "hotspot".
 *
 * 2003-04-14 EliasAE
 * 	Changes to use the new interface for CoordinateConverter.
 *
 * 2003-04-14 pergamon
 *  Fixed the hitTest more precise, also wrote hitTest(int,int,int,int)
 *
 * 2003-04-13 EliasAE
 * 	Changed to using the Rendeable interface.
 *
 * 2003-04-12 pergamon
 *  Coded the constructor and draw function. Created getDirection. Added
 *  simple funtionality for hitTest(int,int).
 *
 * 2003-04-11 pergamon
 *  Formatted syntax. Fixed: setSelected, isSelected.
 */
package graphicsengine;



 import graphicsengine.graphics.GameObjectGraphics;
 import gameengine.*;
 import gameengine.gameobjects.*;
 import java.awt.Graphics2D;
 import java.awt.Color;
 import java.awt.Polygon;
 import java.util.Collection;

/**
 * A graphical representation of a game object on the screen. It can be
 * selected by a user due to its implementation of Selectable.
 */
public class GraphicalGameObject extends GraphicalObject implements Selectable {

	/**
	 * A reference to wich this object representes in the graphical world.
	 */
	private GameObject gameObject;

	/**
	 * If this object is selected or not.
	 */
	private boolean selected;

	/**
	 * All the graphics for this object.
	 */
	private GameObjectGraphics gameObjectGraphics;

	/**
	 *
	 */
	private int hotspotX;

	/**
	 *
	 */
	private int hotspotY;

	/**
	 * The width of this object.
	 */
	private int width;

	/**
	 * The height of this object.
	 */
	private int height;

	/**
	 * Creates a graphicalGameObject that represents and draw a game
	 * object on the screen.
	 *
	 * @param gameObject the game object that this graphical game object
	 * represents and draws on the screen.
	 * @param sprites the sprites that will be used to draw the game object.
	 * This can be one or more sprites. Multiple sprites represent the
	 * different angels of the object. Angel zero is when the object is
	 * traveling along the positive x-axis, that is right, and the angel
	 * increases towards the positive y-axis.
	 * @param screenX the x-position of this game object on the screen in
	 * screen coordinates relative to the upper left corner of the screen in
	 * pixels.
	 * @param screenY the x-position of this game object on the screen in
	 * screen coordinates relative to the upper left corner of the screen in
	 * pixels.
	 * @param radius the radius of the graphical representation of the game
	 * object. This is the accepted area where a hit test will return true. It
	 * does not need to have any meaning in the game engine.
	 */
	public GraphicalGameObject(GameObjectGraphics gameObjectGraphics,
			GameObject gameObject) {
		// The coordinates will change as soon as we start drawing anyway.
		// No need to set them to anything special.
		super(0,0);
		this.gameObjectGraphics = gameObjectGraphics;
		this.gameObject = gameObject;

		if (gameObjectGraphics != null) {
			this.hotspotX = gameObjectGraphics.getHotspotX();
			this.hotspotY = gameObjectGraphics.getHotspotY();
		
			// Get the width and height from the graphical representation.
			if (gameObjectGraphics.getAngle(0) != null) {
				this.width = gameObjectGraphics.getAngle(0).getWidth();
				this.height = gameObjectGraphics.getAngle(0).getHeight();
			}
		}
	}

	/**
	 * Draws the graphical representation of the game object depending on which
	 * direction it has.
	 *
	 * @param graphics the graphics object to draw on.
	 */
	public void draw(Graphics2D graphics) {
		CoordinateConverter cc = CoordinateConverter.getCoordinateConverter();
		Renderable tmpRenderable;
		float angle = 0f;

		// The screen coordinates of an image should be drawn from it's top coorner
		// while the GameObject's position is at the "hotspot". So we substract
		// the hotspot modifications from the GameObject's screen coordinates and
		// we get the coordinates where we shall draw our Renderable on screen.
		setScreenX(cc.mapToScreenX(gameObject.getPoint()) - hotspotX);
		setScreenY(cc.mapToScreenY(gameObject.getPoint()) - hotspotY);

		/* Draw a renderable so that it's seen if the object is selected. */
		if (selected) {
			// Get the graphics for when this GameObject is selected.
			tmpRenderable = gameObjectGraphics.getSelected();
			if (tmpRenderable != null) {
				// draw the renderable on screen using this objects
				// screen coordinates.
				tmpRenderable.render(graphics, getScreenX(), getScreenY());
			}
		}

		/* Draw the renderable of the Individual depening of what angle it is in. */
		// convert the gameobject's angle from map to screen coordinate system.
		if(gameObject instanceof VelocityContainer) {
			angle = cc.angleMapToScreen(
				((VelocityContainer)gameObject).getVector().getDirection());
		}
		// Get the correct Renderable for this angle. If no angle has been set
		// it should be zero, rednering the first renderable angle.
		tmpRenderable = gameObjectGraphics.getAngle(angle);
		if (tmpRenderable != null) {
			// draw the renderable on screen using this object's
			// screen coordinates.
			tmpRenderable.render(graphics, getScreenX(), getScreenY());
		}

		super.draw(graphics);
	}

	/**
	 * Retrieves the game object associated with this object.
	 *
	 * @return the game object that this object represent and draws on the
	 * screen.
	 */
	public GameObject getGameObject() {
		return gameObject;
	}

	/**
	 * Retrieves if the object is selected
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
		// Test if the screenX and Y are within this objects area.
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
	 * inparameters.
	 *
	 * @param screenLeftX The top left x-position of the rectangle to check
	 * against, inclusive bound, in screen coordinates.
	 * @param screenTopY The top left y-position of the rectangle to check
	 * against, inclusive bound, in screen coordinates.
	 * @param screenRightX The bottom right x-position of the rectangle to
	 * check against, exclusive bound, in screen coordinates.
	 * @param screenBottomY The bottom right y-position of the rectangle to
	 * check against, exclusive bound, in screen coordinates.
	 * @return true if this object was within the rectangle, otherwise false.
	 */
	public boolean hitTest(
			int screenLeftX, int screenTopY, int screenRightX,	int screenBottomY) {
		boolean tmpHit = false;
		// tmpGameObject is where the "true" position of the gameobject is.
		// getScreenX and Y are where the Renderable of this object starts to draw.
		int tmpGameObjectX = getScreenX() + hotspotX;
		int tmpGameObjectY = getScreenY() + hotspotY;
		if ((tmpGameObjectX >= screenLeftX) &&
				(tmpGameObjectX <= screenRightX) &&
				(tmpGameObjectY >= screenTopY) &&
				(tmpGameObjectY <= screenBottomY)) {
			tmpHit = true;
		}
		return tmpHit;
	}

}
