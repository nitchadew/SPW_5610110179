

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

public abstract class Sprite {
	int x;
	int y;
	int width;
	int height;
	String type;
	
	public Sprite(int x, int y, int width, int height, String type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
	}

	abstract public void draw(Graphics2D g);
	
	public Double getRectangle() {                               //return position
		return new Rectangle2D.Double(x, y, width, height);
	}
}
