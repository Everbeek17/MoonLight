/**
 * The stars that appear/fly through the sky
 * @author Erkin Verbeek
 *
 */

package PhysicsEngine;
import java.awt.Color;
import java.awt.Graphics2D;

public class Star extends CirclePhysicsObject {
	
	private static final double density = 2.5;	// density used to calculate mass based on size.
	
	// Temporary
	private Color color = Color.YELLOW;


	public Star(double x, double y, double radius) {
		super(x, y, density*(4/3)*Math.PI*Math.pow(radius, 3), radius);
	}
	public Star(double x, double y, double radius, Color c) {
		this(x, y, radius);
		color = c;
	}
	
	public Star(double x, double y, double radius, double xVelocity, double yVelocity) {
		super(x, y, density*(4/3)*Math.PI*Math.pow(radius, 3), radius, xVelocity, yVelocity);
	}
	public Star(double x, double y, double radius, 
			double xVelocity, double yVelocity, Color c) {
		this(x, y, radius, xVelocity, yVelocity);
		color = c;
	}
	
	// draws the star as a circle
	public void drawToGraphics(Graphics2D g2) {
		g2.setColor(color);
		super.drawToGraphics(g2);
	}
}
