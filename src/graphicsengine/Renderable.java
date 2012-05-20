/*
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-04-16 pergamon
 *	Added: public void render(Graphics2D graphics, int screenX, int screenY,
 *		int topLeftX, int topLeftY, int width, int height) {
 *
 * 2003-03-13 EliasAE
 * 	Created the interface.
 */
package graphicsengine;

import java.awt.Graphics2D;

/**
 * Interface that all objects that can render themself to the
 * screen should implement. This is for objects that do not
 * have a position of other properties, but just a graphical resource
 * that can be rendered on the screen. Sprites and animations should
 * implement this interface.
 */
public interface Renderable {

	/**
	 * Renders the object on the specified graphics object.
	 *
	 * @param graphics the graphics object to render the object
	 * that implements this interface on. How the
	 * object is rendered is implementation defined.
	 * @param screenX the x-coordinate to draw the object on.
	 * @param screenY the y-coordinate to draw the object on.
	 */
	public void render(Graphics2D graphics, int screenX, int screenY);

	/**
	 * Renders a specified subarea of the object on the specified graphics object.
	 *
	 * @param graphics the graphics object to render the object
	 * that implements this interface on. How the
	 * object is rendered is implementation defined.
	 * @param screenX the x-coordinate to draw the object on.
	 * @param screenY the y-coordinate to draw the object on.
	 * @param topLeftX the topLeft x-coordinate of the subarea to render.
	 * @param topLeftY the topLeft y-coordinate of the subarea to render.
	 * @param width the width of the subarea to render.
	 * @param height the height of the subarea to render.
	 */
	public void render(Graphics2D graphics, int screenX, int screenY,
			int topLeftX, int topLeftY, int width, int height);

	/**
	 * Retrieves the width of the renderable object in pixels.
	 *
	 * @return the width of the renderable object in pixels.
	 */
	public int getWidth();

	/**
	 * Retrieves the height of the renderable object in pixels.
	 *
	 * @return the height of the renderable object in pixels.
	 */
	public int getHeight();

}
