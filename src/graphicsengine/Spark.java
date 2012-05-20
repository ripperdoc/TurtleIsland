package graphicsengine;

import java.awt.Graphics2D;
import java.awt.Color;

/**
 *
 */
public class Spark extends GraphicalObject {

	private int radius;
	private Color color;

  public Spark() {
		super(150, 150);
		this.color = Color.white;
		this.radius = 10;
  }

	public void draw(Graphics2D graphics) {

	}

}