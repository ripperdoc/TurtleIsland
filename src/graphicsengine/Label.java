/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-04-21 pergamon
 *  Added Font.
 *
 * 2003-04-28 pergamon
 *  Wrote the class.
 *
 */
package graphicsengine;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

/**
 * A label of text.
 */
public class Label extends GraphicalObject {

	/**
	 * Represents the text string of the label.
	 */
	private String label;

	/**
	 * The color of the label text.
	 */
	private Color labelColor;

	/**
	 * The font of the label text.
	 */
	private Font labelFont;

	/**
	 * Creates a label
	 *
	 * @param label The text of the label.
	 */
	public Label(String label, int relativeX, int relativeY) {
		this(label, relativeX, relativeY, Color.white, null);
	}

	/**
	 * Creates a label with specified color and relative position to its owner.
	 * If there is no owner the coordinates are relative to the screen.
	 *
	 * @param label The text of the label.
	 * @param relativeX the x-coordinate of the label relative to the screen or
	 * an owner.
	 * @param relativeY the y-coordinate of the label relative to the screen or
	 * an owner.
	 * @param labelColor the color of the text of the label.
	 */
	public Label(String label, int relativeX, int relativeY, Color labelColor) {
		this(label, relativeX, relativeY, labelColor, null);
	}

	/**
	 * Creates a label with specified color, font and relative position to its owner.
	 * If there is no owner the coordinates are relative to the screen.
	 *
	 * @param label The text of the label.
	 * @param relativeX the x-coordinate of the label relative to the screen or
	 * an owner.
	 * @param relativeY the y-coordinate of the label relative to the screen or
	 * an owner.
	 * @param labelColor the color of the text of the label.
	 * @param labelFont the font of the label.
	 */
	public Label(String label, int relativeX, int relativeY, Color labelColor, Font labelFont) {
		super(relativeX, relativeY);
		this.label = label;
		this.labelColor = labelColor;
		this.labelFont = labelFont;
	}

	/**
	 * Get the label.
	 *
	 * @return the label.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the label.
	 *
	 * @param label the label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Set the label.
	 *
	 * @param label the label to set.
	 */
	public void setLabel(Label label) {
		this.label = label.getLabel();
		this.labelColor = label.getColor();
		this.labelFont = label.getFont();
	}

	/**
	 * Get the label's color.
	 *
	 * @return the color of the label.
	 */
	public Color getColor() {
		return labelColor;
	}

	/**
	 * Set the label's color.
	 *
	 * @param labelColor the new color of the label.
	 */
	public void setColor(Color labelColor) {
		this.labelColor = labelColor;
	}

	/**
	 * Get the label's font.
	 *
	 * @return the label's font.
	 */
	public Font getFont() {
		return labelFont;
	}

	/**
	 * Set the label's font.
	 *
	 * @param font the new font of the label.
	 */
	public void setFont(Font font) {
		this.labelFont = font;
	}

	/**
	 * Draw the label on a graphics object..
	 *
	 * @param graphics the graphics to draw on.
	 */
	public void draw(Graphics2D graphics) {
		if (isVisible()) {
			Font tmpFont = graphics.getFont();
			if (!(labelFont == null)) {
				graphics.setFont(labelFont);
			}
			graphics.setColor(labelColor);
			graphics.drawString(label, getScreenX(), getScreenY() + graphics.getFont().getSize());
			graphics.setFont(tmpFont);
			super.draw(graphics);
		}
	}

}
