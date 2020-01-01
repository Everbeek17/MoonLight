package PhysicsEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Goal extends CirclePhysicsObject{
	
	private int maxCapacity, currentCapacity = 0;
	private String remainingCapacityString;
	private static final Font font  = new Font("TimeRoman", Font.PLAIN, 18);
	
	public Goal(double xPos, double yPos, double radius, int maxCapacity) {
		super(xPos, yPos, 0, radius);
		this.maxCapacity = maxCapacity;
		remainingCapacityString = Integer.toString(maxCapacity);
	}
	
	// increments the current capacity by one if not at the max yet.
	public void incrementToCapacity() {
		if (currentCapacity < maxCapacity) {
			currentCapacity++;
			remainingCapacityString = Integer.toString(maxCapacity - currentCapacity);
		}
	}
	
	public void drawToGraphics(Graphics2D g2) {
		g2.setColor(Color.GRAY.darker());
		g2.fill(new Ellipse2D.Double(getX()-getRadius(), getY()-getRadius(), getRadius()*2, getRadius()*2));
		g2.setColor(Color.GRAY.brighter());	// temp
		double innerRadius = getRadius()*currentCapacity/maxCapacity;
		g2.fill(new Ellipse2D.Double(getX()-innerRadius, getY()-innerRadius, innerRadius*2, innerRadius*2));
	
		
		
		// draws inside the goal how many more stars it needs to be filled
		
		// gets the font metrics
		FontMetrics fontMetrics = g2.getFontMetrics(font);
		// determines the baseline bottom left of the text
		float StringX = (float) (getX() - fontMetrics.stringWidth(remainingCapacityString)/2);
		float StringY = (float) (getY() + fontMetrics.getAscent()/2 - 2);
		// sets color and font
		g2.setColor(Color.WHITE);
		g2.setFont(font);
		// Draws the String
	    g2.drawString(remainingCapacityString, StringX, StringY);
	}
}
