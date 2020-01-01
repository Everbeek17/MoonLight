package PhysicsEngine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class CirclePhysicsObject extends PhysicsObject implements CircleObjectInterface {

	private double radius;
	private static final float highlighterRadius = 0.75f;
	
	public CirclePhysicsObject(double xPos, double yPos, double mass, double radius) {
		super(xPos, yPos, mass);
		this.radius = radius;
	}
	
	// Constructor with starting Velocities
	public CirclePhysicsObject(double xPos, double yPos, double mass, double radius, double xVelocity, double yVelocity) {
		super(xPos, yPos, mass, xVelocity, yVelocity);
		this.radius = radius;
	}
	
	public double getRadius() { return radius; }
	
	// draws this circle centered at xPos, yPos
	public void drawToGraphics(Graphics2D g2) {
		double innerRadius = radius - highlighterRadius;
		Color c = g2.getColor();
		g2.setColor(Color.DARK_GRAY);
		g2.fill(new Ellipse2D.Double(getX()-radius, getY()-radius, radius*2, radius*2));
		g2.setColor(c);
		g2.fill(new Ellipse2D.Double(getX() - innerRadius, getY() - innerRadius,
				innerRadius*2, innerRadius*2));
	}
	
	// draws a white circle slightly larger than the current Circle Physics Object
	// so that we can draw this object on top and it'll look like a ring around it.
	public void drawSelectionRing(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		double selectionRingRadius = radius*1.2;
		g2.fill(new Ellipse2D.Double(getX()-selectionRingRadius, getY()-selectionRingRadius,
				selectionRingRadius*2, selectionRingRadius*2));
	}
}
