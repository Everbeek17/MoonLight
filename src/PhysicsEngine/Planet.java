package PhysicsEngine;
import java.awt.Color;
import java.awt.Graphics2D;

public class Planet extends CirclePhysicsObject {

	//public static double bounceFactor = 0.5;
	private Color color;
	
	/**
	 * Instantiates the planet as a static object in a specified location with specifed
	 * mass, radius, and color.
	 * @param x	X location.
	 * @param y	Y location.
	 * @param mass	The mass of the planet (for gravity calculations).
	 * @param radius	The radius of the planet (for drawing to screen).
	 * @param color	The color of the planet (for drawing to screen).
	 */
	public Planet(double x, double y, double mass, double radius, Color color) {
		super(x, y, mass, radius);
		this.color = color;
	}
	
	// draws the planet as a circle
	public void drawToGraphics(Graphics2D g2) {
		g2.setColor(color);
		super.drawToGraphics(g2);
	}
}
