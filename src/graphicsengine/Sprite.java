/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-04-18 EliasAE
 * 	Changed render of a sub image to not create a new image if the whole
 * 	image should be rendered. Removed the constructor Sprite(Image) which
 * 	previously lead to a NullPointerException.
 *
 * 2003-04-16 pergamon
 *	Added: public void render(Graphics2D graphics, int screenX, int screenY,
 *		int topLeftX, int topLeftY, int width, int height) {
 *
 * 2003-04-13 EliasAE
 * 	Changed to using the Rendeable interface.
 *
 * 2003-04-10	pergamon
 *	Wrote the constructor so that it loads the bufferedImage with a
 *	createCompatibleImage (returns a BufferedImage with a 'inner' VolatileImage,
 *	Almost as fast as a VolatileImage but with support for transparancy).
 *	Removed dispose().
 */
package graphicsengine;

import java.awt.*;
import java.awt.image.*;

/**
 * A sprite that can be painted to the screen. Contains image information.
 * The same sprite can be painted an infinite number of times on the screen
 * in the same cycle. Use only one sprite per picutre. This will save
 * memory.
 */
public class Sprite implements Renderable{

	/**
	 * Represents the sprite's width.
	 */
	private int width;

	/**
	 * Represents the sprite's height.
	 */
	private int height;

	/**
	 * The image that is used to render the sprite. This image can be transparent
	 * to create the illusion of irregular shaped objects. Alphablending is not
	 * supported.
	 */
	private BufferedImage bufferedImage;

	/**
	 * Creates a sprite.
	 *
	 * @param owner the component that ows the sprite.
	 * @param image the image that the sprite will paint, when requested to do
	 * so by a call to render.
	 */
	public Sprite(Component owner, Image image) {
		width = image.getWidth(owner);
		height = image.getHeight(owner);
		bufferedImage =
				owner.getGraphicsConfiguration().createCompatibleImage(
				width,
				height,
				Transparency.BITMASK
				);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, owner);
	}


	/**
	 * Renders the sprite on a graphics object.
	 *
	 * @param graphics the graphics object to render the sprite on.
	 * @param screenX the x-position in the coordinate system of the graphics
	 * object to render the sprite on. This position is relative to the upper
	 * left corner of the sprite.
	 * @param screenY the y-position in the coordinate system of the graphics
	 * object ro render the sprite on. This position is relative to the upper
	 * left corner of the sprite.
	 */
	public void render(
			Graphics2D graphics,
			int screenX,
			int screenY) {
		graphics.drawImage(bufferedImage, screenX, screenY, null);
	}

	/**
	 * Renders a specified subarea of the Sprite on a graphics object.
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
	public void render(
			Graphics2D graphics,
			int screenX,
			int screenY,
			int topLeftX,
			int topLeftY,
			int width,
			int height) {
		BufferedImage tmpBufferedImage = bufferedImage;
		if(
				topLeftX != 0
				|| topLeftY != 0
				|| width != getWidth()
				|| height != getHeight()
				) {
			tmpBufferedImage = bufferedImage.getSubimage(
					topLeftX,
					topLeftY,
					width,
					height
					);
		}
		graphics.drawImage(tmpBufferedImage, screenX, screenY, null);
	}

	/**
	 * Retrieves the width of the sprite.
	 *
	 * @return the width of the sprite in pixels.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Retrieves the height of the sprite.
	 *
	 * @return the height of the sprite in pixels.
	 */
	public int getHeight() {
		return height;
	}

}
