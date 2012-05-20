/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-04-17 pergamon
 *  Wrote so that the zero-position can be adjusted. (in testing right now)
 *  It now throw exceptions if inparameters is wrong.
 *  More comments to come.
 *
 * 2003-04-17 pergamon
 *  Wrote the functionality for value inc, dec and how this is drawn.
 *  More comments will come, but not tonight...
 *
 * 2003-04-16 pergamon
 *  Formatted syntax and implemented most of the class.
 *
 */
package graphicsengine;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import graphicsengine.graphics.GraphicsEffects;
import graphicsengine.graphics.GraphicsManager;

/**
 * A graphical bar that show information. It can for example be a
 * progressbar or healthmeter. It is positioned relative to its owner.
 */
public class Bar extends GraphicalObject {

	/**
	 * Represents the current value of the bar.
	 */
	private float value;

	/**
	 * The scale of the bar. The value of the bar is multiplied by this
	 * value to get the length of the bar in pixels. That is the 
	 * value of the bar multiplied with this variable will equal the
	 * width of the bar.
	 */
	private float percentStep;

	/**
	 * The maximum value of the bar.
	 */
	private int maxValue;

	/**
	 * The minimum value of the bar.
	 */
	private int minValue;

	/**
	 * Represents the width of the bar in screen coordinates.
	 */
	private int width;

	/**
	 * Represents the height of the bar in screen coordinates.
	 */
	private int height;

	/**
	 * The Graphics of this Bar.
	 */
	private Renderable barGraphics;

	/**
	 * A reference to a GraphicsManager.
	 */
	private GraphicsManager graphicsManager;

	/**
	 * Creates a bar.
	 *
	 * @param graphicsManager a graphicsManager to get graphics from.
	 * @param screenX the x-coordinate relative to the screen.
	 * @param screenY the y-coordinate relative to the screen.
	 * @param width the width
	 * @param height the height
	 * @throws WrongValuesInBarException if any inparameters are wrong.
	 */
	public Bar(
			GraphicsManager graphicsManager,
			int screenX,
			int screenY,
			int width,
			int height){
		this(
				graphicsManager,
				screenX,
				screenY,
				width,
				height,
				0, // minValue
				100, // maxValue
				0 // startValue
				);
	}

	/**
	 * Creates a bar.
	 *
	 * @param graphicsManager a graphicsManager to get graphics from.
	 * @param relativeX the x-coordinate relative to the owner.
	 * @param relativeY the y-coordinate relative to the owner.
	 * @param width the width
	 * @param height the height
	 * @param startValue the start value
	 * @throws WrongValuesInBarException if any inparameters are wrong.
	 */
	public Bar(
			GraphicsManager graphicsManager,
			int relativeX,
			int relativeY,
			int width,
			int height,
			float startValue) {
		this(
				graphicsManager,
				relativeX,
				relativeY,
				width,
				height,
				0, // minValue
				100, // maxValue
				startValue
				);
	}

	/**
	 * Creates a bar.
	 *
	 *
	 * @param graphicsManager a graphicsManager to get graphics from.
	 * @param relativeX the x-coordinate relative to the owner.
	 * @param relativeY the y-coordinate relative to the owner.
	 * @param width the width
	 * @param height the height
	 * @param startValue the start value
	 * @param minColor the color for the minimum value
	 * @param maxColor  the color for the maximum value
	 * @throws WrongValuesInBarException if any inparameters are wrong.
	 */
  public Bar(
			GraphicsManager graphicsManager,
			int relativeX,
			int relativeY,
			int width,
			int height,
			float startValue,
			Color minColor,
			Color maxColor) {
		this(
				graphicsManager,
				relativeX,
				relativeY,
				width,
				height,
				0, // minValue
				100, // maxValue
				startValue,
				minColor,
				maxColor
				);
	}

	/**
	 * Creates a bar.
	 *
	 * @param graphicsManager a graphicsManager to get graphics from.
	 * @param relativeX the x-coordinate relative to the owner.
	 * @param relativeY the y-coordinate relative to the owner.
	 * @param width the width
	 * @param height the height
	 * @param minValue the minimum value
	 * @param maxValue the maximum value
	 * @param startValue the start value
	 * @throws WrongValuesInBarException if any inparameters are wrong.
	 */
	public Bar(
			 GraphicsManager graphicsManager,
			 int relativeX,
			 int relativeY,
			 int width,
			 int height,
			 int minValue,
			 int maxValue,
			 float startValue) {
		this(
				graphicsManager,
				relativeX,
				relativeY,
				width,
				height,
				minValue,
				maxValue,
				startValue,
				Color.red, // minColor
				Color.blue // maxColor
				);
	}

	/**
	 * Creates a bar.
	 *
	 * @param graphicsManager a graphicsManager to get graphics from.
	 * @param relativeX the x-coordinate relative to the owner.
	 * @param relativeY the y-coordinate relative to the owner.
	 * @param width the width
	 * @param height the height
	 * @param minValue the minimum value
	 * @param maxValue the maximum value
	 * @param startValue the start value
	 * @param minColor the color for the minimum value
	 * @param maxColor the color for the maximum value
	 * @throws WrongValuesInBarException if any inparameters are wrong.
	 */
	public Bar (
			 GraphicsManager graphicsManager,
			 int relativeX,
			 int relativeY,
			 int width,
			 int height,
			 int minValue,
			 int maxValue,
			 float startValue,
			 Color minColor,
			 Color maxColor) {
		this(
					graphicsManager,
					relativeX,
					relativeY,
					width,
					height,
					minValue,
					maxValue,
					startValue,
					minColor,
					Color.green.darker(), // zeroColor.
					maxColor
					);
	}

	/**
	 * Creates a bar.
	 *
	 * @param graphicsManager a graphicsManager to get graphics from.
	 * @param relativeX the x-coordinate relative to the owner.
	 * @param relativeY the y-coordinate relative to the owner.
	 * @param width the width
	 * @param height the height
	 * @param minValue the minimum value
	 * @param maxValue the maximum value
	 * @param startValue the start value
	 * @param minColor the color for the minimum value
	 * @param zeroColor the color for where the value is zero
	 * @param maxColor  the color for the maximum value
	 * @throws WrongValuesInBarException if any inparameters are wrong.
	 */
	public Bar (
			GraphicsManager graphicsManager,
			int relativeX,
			int relativeY,
			int width,
			int height,
			int minValue,
			int maxValue,
			float startValue,
			Color minColor,
			Color zeroColor,
			Color maxColor) {
		super(relativeX, relativeY);

		// if there are any wrong with the inparameters, throw an exception.
		if (minValue >= maxValue) {
			throw new WrongValuesInBarException("minValue >= maxValue");
		} else if (startValue > maxValue) {
			throw new WrongValuesInBarException("startValue > maxValue");
		} else if (startValue < minValue) {
			throw new WrongValuesInBarException("startValue < minValue");
		}

		this.width = width;
		this.height = height;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.value = startValue;
		this.graphicsManager = graphicsManager;
		// calculate how big one "percent" on the bar is.
		percentStep = width / (float) (maxValue-minValue);
		// create the image of the bar.
		createBarImage(minColor, zeroColor, maxColor);
	}

	/**
	 * Draws the bar on to the specified graphics.
	 *
	 * @param graphics The Graphics object to draw on.
	 */
	public void draw(Graphics2D graphics) {
		if (isVisible()) {
			// The current value of the bar times how big one percent step is.
			int valueWidth = Math.round(value * percentStep);
			// if there's a zero-position different from where the bar start or end.
			int zero = Math.abs(Math.round(minValue * percentStep));
			// if the current value of the bar is above zero
			if (valueWidth > 0) {
				barGraphics.render(
						graphics, getScreenX() + zero, getScreenY(), zero, 0, valueWidth,
						height);
				// or if it's below zero.
			} else if (valueWidth < 0) {
				barGraphics.render(
						graphics, getScreenX() + (zero + valueWidth), getScreenY(),
						zero + valueWidth, 0, -valueWidth, height);
			}

			// Draw a nice little border to the bar.
			graphics.setColor(Color.lightGray);
			graphics.drawRect(getScreenX(), getScreenY(), width, height);

			super.draw(graphics);
		}
	}

	/**
	 * This function creates the Bar's Image with a gradient from startcolor(, to
	 * zerocolor (if present)) to endcolor.
	 *
	 * @param startColor The Color the bar shall have when it's at its lowest value.
	 * @param zeroColor The Color the bar shall have at it's zero position.
	 * @param endColor The Color the bar shall have when it's at its highets value.
	 */
	private void createBarImage(Color startColor, Color zeroColor, Color endColor) {
		// A temporarely BufferedImage to draw the bar's graphics on.
		BufferedImage tmpBufferedImage =
				new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		// if the "zero" point is somewhere on the bar.
		if ((minValue < 0) && (maxValue > 0)) {
			int zero = Math.round(Math.abs(minValue* percentStep));
			// draw the image from the minvalue to zero
			BufferedImage firstHalf =
					tmpBufferedImage.getSubimage(0, 0, zero, height);
			GraphicsEffects.gradient(startColor, zeroColor, firstHalf);
			// and from zero to maxvalue
			BufferedImage secondHalf = tmpBufferedImage.getSubimage(zero, 0, width-zero, height);
			GraphicsEffects.gradient(zeroColor, endColor, secondHalf);
		} else {
			// draw the image from zero to maxvalue (or from minvalue to zero).
			GraphicsEffects.gradient(startColor, endColor, tmpBufferedImage);
		}

		// "save" the temporarely bar image to the bar's image.
		barGraphics = graphicsManager.createSprite(tmpBufferedImage);
	}

	/**
	 * Increases the value of the bar but not over its maximum.
	 *
	 * @param inc How much to increase the value of the bar.
	 */
	public void increase(float inc) {
		if (inc > 0) {
			value += inc;
			if (value > maxValue) {
				value = maxValue;
			}
		}
	}

	/**
	 * Decreases the value of the bar but not under its minimum.
	 *
	 * @param dec How much to decrease the value of the bar.
	 */
	public void decrease(float dec) {
		if (dec > 0) {
			value -= dec;
			if (value < minValue) {
				value = minValue;
			}
		}
	}

	/**
	 * To get the min value of this bar.
	 *
	 * @return the min value of this bar.
	 */
	public int getMinValue() {
		return minValue;
	}

	/**
	 * To get the max value of this bar.
	 *
	 * @return the max value of this bar.
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * To check if the bar is at its minimum value.
	 *
	 * @return true if the bar's value is equal to its min value.
	 */
	public boolean isMin() {
		return (value == minValue);
	}

	/**
	 * To check if the bar is at its maximum value.
	 *
	 * @return true if the bar's value is equal to its max value.
	 */
	public boolean isMax() {
		return (value == maxValue);
	}

	/**
	 * Does retrive the current value of this <code>Bar</code>.
	 *
	 * @return a float with the value of the bar.
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Set the current value of this <code>Bar</code>.
	 *
	 * @param value a float with the new value of the bar.
	 */
	public void setValue(float value) {
		if (value > maxValue) {
			this.value = maxValue;
		} else if (value < minValue) {
			this.value = minValue;
		} else {
			this.value = value;
		}
	}

	/**
	 * Does return the width of this bar.
	 *
	 * @return a int width the width of the bar.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Does return the height of this bar.
	 *
	 * @return a int height the width of the bar.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Return the width of this bar.
	 *
	 * @param width the width of the bar.
	 */
	void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Return the height of this bar.
	 *
	 * @param height the height of the bar.
	 */
	void setHeight(int height) {
		this.height = height;
	}

}

/**
 * An Exception to throw if the in-values in the Bar are wrong.
 */
class WrongValuesInBarException extends IllegalArgumentException {

	public WrongValuesInBarException(String message) {
		super(message);
	}

}
