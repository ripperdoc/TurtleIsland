/*
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-?? pergamon
 *  wrote the rest.
 *
 * 2003-05-03 pergamon
 *  Wrote most of its functionality.
 *
 * 2003-05-01 pergamon
 *  Created the class.
 */
package graphicsengine;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;

/**
 * A Panel to contain other GraphicalObjects such as Bars, Buttons, Labels or
 * even other Panels.
 */
public class Panel extends GraphicalObject {

	/**
	 * The font for text on the panel.
	 */
	private Font font;

	/**
	 * Color of the description text.
	 */
	private Color descriptionColor;

	/**
	 * Color of the panel.
	 */
	private Color informationColor;

	/**
	 * Width of the panel.
	 */
	private int width;

	/**
	 * Height of the panel.
	 */
	private int height;

	/**
	 * Creates a panel.
	 *
	 * @param name the name of the panel.
	 * @param relativeX the position relative to its parent.
	 * @param relativeY the position relative to its parent.
	 */
	public Panel(String name, int relativeX, int relativeY) {
		super(relativeX, relativeY);
		setName(name);
		this.descriptionColor = Color.gray;
		this.informationColor = Color.white;
	}

	/**
	 * Create a panel.
	 *
	 * @param name the name of the panel.
	 * @param relativeX the position relative to its parent.
	 * @param relativeY the position relative to its parent.
	 * @param width the width of the panel in pixels.
	 * @param height the height of the panel in pixels.
	 */
	public Panel(String name, int relativeX, int relativeY, int width, int height) {
		super(relativeX, relativeY);
		setName(name);
		this.width = width;
		this.height = height;
		this.descriptionColor = Color.gray;
		this.informationColor = Color.white;
	}

	/**
	 * Set the name of this Panel.
	 *
	 * @param name The new name of the Panel.
	 */
	public void setName(String name) {
		Label tmpLabel =	new Label(name, 0, 0, descriptionColor, font);
		addSubObject("name", tmpLabel);
	}

	/**
	 * Adds a object that the panel will contain.
	 *
	 * @param key the key for the subobject.
	 * @param description the description of the subobject.
	 * @param newItem the subobject itself.
	 */
	public void addSubObject(Object key, Label description, GraphicalObject newItem) {
		if (!(description == null)) {
			description.setFont(font);
			description.setColor(descriptionColor);
			description.owner = this;
			newItem.setDescription(description);
		}

		if (newItem instanceof Label) {
			((Label) newItem).setFont(font);
			((Label) newItem).setColor(informationColor);
		}

		addSubObject(key, newItem);
	}

	/**
	 * Set the font of the panel's all labels.
	 *
	 * @param font the new font.
	 */
	public void setFont(Font font) {
		this.font = font;
		Iterator itemsIterator = getSubObjects().iterator();

		while (itemsIterator.hasNext()) {
			Object tmpSubObject = itemsIterator.next();
			if (tmpSubObject instanceof Label) {
				((Label) tmpSubObject).setFont(font);
			}
		}
	}

	/**
	 * Sets the color of the information.
	 *
	 * @param color the new color.
	 */
	public void setInformationColor(Color color) {
		this.informationColor = color;
		Iterator itemsIterator = getSubObjects().iterator();

		while (itemsIterator.hasNext()) {
			Object tmpSubObject = itemsIterator.next();
			if (tmpSubObject instanceof Label) {
				((Label) tmpSubObject).setColor(color);
			}
		}
	}

	/**
	 * Sets the color of the descriptions.
	 *
	 * @param color the new color of the descriptions.
	 */
	public void setDescriptionColor(Color color) {
		this.descriptionColor = color;
		Iterator itemsIterator = getSubObjects().iterator();

		while (itemsIterator.hasNext()) {
			GraphicalObject tmpSubObject = (GraphicalObject) itemsIterator.next();
			if (!(tmpSubObject.getDescription() == null)) {
				tmpSubObject.getDescription().setFont(font);
			}
		}
	}

	/**
	 * Draws the Panel onto a graphics object.
	 *
	 * @param graphics the graphics object to draw on.
	 */
	public void draw(Graphics2D graphics) {
		if (isVisible()) {
			Iterator itemsIterator = getSubObjects().iterator();
			while (itemsIterator.hasNext()) {
				GraphicalObject tmpItem = (GraphicalObject) itemsIterator.next();
				tmpItem.draw(graphics);
			}
			super.draw(graphics);
		}
	}

}
