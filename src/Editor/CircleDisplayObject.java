package Editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import PhysicsEngine.CircleObjectInterface;
import PhysicsEngine.Goal;
import PhysicsEngine.PhysicsObject;
import PhysicsEngine.Planet;
import PhysicsEngine.Star;

public class CircleDisplayObject implements SidePanelObjectInterface, CircleObjectInterface {

	private ObjectType objectType;
	private Color color;
	private double xPos, yPos;
	private Shape objectShape, highlighterShape;
	private double radius;
	private boolean hasHighlight;
	private static final double highlighterRadiusAddOn = 3;
	
	public CircleDisplayObject(double xPos, double yPos, double radius, Color color, ObjectType ot) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.radius = radius;
		this.color = color;
		hasHighlight = false;	// starts without a highlight
		objectType = ot;	// saves what type of object this item is
		updateShapes();
	}
	
	public double getX() { return xPos; }
	public double getY() { return yPos; }
	public double getRadius() { return radius; }
	public void setRadius(double newRadius) {
		radius = newRadius; 
		updateShapes();
	}
	public ObjectType getType() { return objectType; }
	
	/**
	 * Recreates the shape objects using the instance variables
	 */
	private void updateShapes() {
		objectShape = new Ellipse2D.Double(xPos - radius, yPos - radius,
				radius*2, radius*2);
		double highlightRadius = radius + highlighterRadiusAddOn;
		highlighterShape = new Ellipse2D.Double(xPos - highlightRadius, yPos - highlightRadius,
				highlightRadius*2, highlightRadius*2);
	}
	
	public void drawToGraphics(Graphics2D g2) {
		if (hasHighlight) {
			g2.setColor(Color.WHITE);
			g2.fill(highlighterShape);
		}
		g2.setColor(color);
		g2.fill(objectShape);
	}

	/**
	 * Highlights the current object if the given coordinates are on the object
	 */
	public void highlight(int x, int y) {
		if (highlighterShape.contains(x, y))
			hasHighlight = true;
		else
			hasHighlight = false;
	}
	
	public boolean isHighlighted() { return hasHighlight; }
	/**
	 * Returns a copy of this object
	 */
	public CircleDisplayObject getCopyOf() {
		return new CircleDisplayObject(xPos, yPos, radius, color, objectType);
	}

	// updates the xPos of the shape and it's highlighter
	public void setX(double newX) {
		xPos = newX;
		// recreates the shapes in the new location
		updateShapes();
	}
	public void setXAndY(double newX, double newY) {
		// updates the saved x and y pos variables
		xPos = newX;
		yPos = newY;
		// recreates the shapes in the new location
		updateShapes();
	}
	
	public PhysicsObject turnIntoPhysicsObject() {
		switch (objectType) {
		case PLANET:
			return new Planet(xPos, yPos, Math.PI*Math.pow(radius, 3)/6, radius, color);
		case STAR:
			return new Star(xPos, yPos, radius);
		case GOAL:
			return new Goal(xPos, yPos, radius, 10);
		}
		return null;
	}
}
