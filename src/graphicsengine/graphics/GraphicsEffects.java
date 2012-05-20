/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-04-17 pergamon
 *   created the class and wrote gradient.
 */

package graphicsengine.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

/**
 * Class with methods for graphics effects.
 */
public class GraphicsEffects {

	/**
	 * Create a gradient from startColor to endColor on the given image.
	 *
	 * @param startColor The Color to start the gradient from.
	 * @param endColor The Color to end the gradient at.
	 * @param image The image to draw the gradient on.
	 */
	public static void gradient(
			Color startColor, Color endColor, BufferedImage image) {
		Graphics2D g = (Graphics2D) image.getGraphics();
		int gradientWidth = image.getWidth();
		int gradientHeight = image.getHeight();
		float redStep =
			(endColor.getRed() - startColor.getRed()) / (float) gradientWidth;
		float greenStep =
			(endColor.getGreen() - startColor.getGreen()) / (float) gradientWidth;
		float blueStep =
			(endColor.getBlue() - startColor.getBlue())  / (float) gradientWidth;

		for (int lamellaNr = 0; lamellaNr <= gradientWidth; lamellaNr++) {
			Color tmpColor = new Color(
					startColor.getRed() + Math.round(lamellaNr * redStep),
					startColor.getGreen() + Math.round(lamellaNr * greenStep),
					startColor.getBlue() + Math.round(lamellaNr * blueStep)
					);
			g.setColor(tmpColor);
			g.drawLine(lamellaNr, 0, lamellaNr, gradientHeight);
		}
	}

}
