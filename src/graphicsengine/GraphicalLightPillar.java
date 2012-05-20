/*	2003-05-20 pergamon
 *		created the class. This is just ugly due to lack of time. Need screenshots.
 *
 */

package graphicsengine;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
//import java.lang.Math;
import gameengine.gameobjects.LightPillarNode;

/**
 *
 */
public class GraphicalLightPillar extends GraphicalObject implements Selectable {

	private boolean selected;
	private int topRadius;
	private int bottomRadius;
	private int height;
	private LightPillarNode lightPillar;

	public GraphicalLightPillar(LightPillarNode lightPillar) {
		super(0, 0);
		this.lightPillar = lightPillar;
		this.topRadius = 10;
		this.bottomRadius = 20;
		this.height = 800;
	}

	public void draw(Graphics2D graphics) {
		CoordinateConverter cc = CoordinateConverter.getCoordinateConverter();
		setScreenX(cc.mapToScreenX(lightPillar.getPoint()));
		setScreenY(cc.mapToScreenY(lightPillar.getPoint()));

		// the scale is 2:1 there by bottomRadius/2 and bottomRadius*2
		graphics.setColor(Color.white);

		int topLeftX = getScreenX() - topRadius;
		int topCenterY = getScreenY() - height / 2;
		int topDiameter = topRadius * 2;

		int bottomLeftX = getScreenX() - bottomRadius;
		int bottomCenterY = getScreenY();
		int bottomDiameter = bottomRadius * 2;

		graphics.drawLine(topLeftX, topCenterY, bottomLeftX, bottomCenterY);
		graphics.drawLine(topLeftX + topDiameter, topCenterY,
				bottomLeftX + bottomDiameter, bottomCenterY);

		graphics.drawOval(
				topLeftX,
				topCenterY - topRadius / 2,
				topDiameter,
				topRadius
				);
		graphics.drawOval(
				bottomLeftX,
				bottomCenterY - bottomRadius / 2,
				bottomDiameter,
				bottomRadius
				);

		graphics.setColor(new Color(255,255,255,125));

		graphics.fillArc(
				topLeftX,
				topCenterY - topRadius / 2,
				topDiameter,
				topRadius,
				0,
				180
				);
		graphics.fillArc(
				bottomLeftX,
				bottomCenterY - bottomRadius / 2,
				bottomDiameter,
				bottomRadius,
				180,
				180
				);

		Polygon tmpPolygon = new Polygon();
		tmpPolygon.addPoint(topLeftX, topCenterY);
		tmpPolygon.addPoint(topLeftX + topDiameter, topCenterY);
		tmpPolygon.addPoint(bottomLeftX + bottomDiameter, bottomCenterY);
		tmpPolygon.addPoint(bottomLeftX, bottomCenterY);
		graphics.fillPolygon(tmpPolygon);

		super.draw(graphics);
	}

	/**
	 * Retrieves the game object associated with this object.
	 *
	 * @return the game object that this object represent and draws on the
	 * screen.
	 */
	public LightPillarNode getGameObject() {
		return lightPillar;
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
		int width = bottomRadius * 2;
		int height = bottomRadius / 2;
		int tmpPosX = getScreenX() - bottomRadius;
		int tmpPosY = getScreenY() - height;
		// Test if the screenX and Y are within this objects area.
		if ((screenX >= tmpPosX) &&
				(screenX <= tmpPosX + width) &&
				(screenY >= tmpPosY) &&
				(screenY <= tmpPosY + height + bottomRadius)) {
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
		int tmpGameObjectX = getScreenX();
		int tmpGameObjectY = getScreenY();
		if ((tmpGameObjectX >= screenLeftX) &&
				(tmpGameObjectX <= screenRightX) &&
				(tmpGameObjectY >= screenTopY) &&
				(tmpGameObjectY <= screenBottomY)) {
			tmpHit = true;
		}
		return tmpHit;
	}

}
