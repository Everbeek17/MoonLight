package DefaultPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class MyMouseButton {

	// instance variables for text
	private Color textColor, highlightedTextColor;
	private Font textFont;
	private String textString;
	private float stringX, stringY;
	
	// instance variables for the button
	private Color boxColor, highlightedBoxColor;
	private Shape boxShape;
	
	// private boolean to know if button is highlighted or not
	private boolean isHighlighted;
	
	public MyMouseButton(String textString, Font textFont, Color textColor, Color highlightedTextColor,
			Color boxColor, Color highlightedBoxColor, double xPos, double yPos, double width, double height) {
		this.textString = textString;
		this.textFont = textFont;
		this.textColor = textColor;
		this.highlightedTextColor = highlightedTextColor;
		this.boxColor = boxColor;
		this.highlightedBoxColor = highlightedBoxColor;
		// creates and saves the shape of the box
		boxShape = new RoundRectangle2D.Double(xPos - width/2, yPos - height/2, width, height, 20, 20);
	}
	
	// constructor that uses some default values
	public MyMouseButton(String textString, Font textFont, double xPos, double yPos, double width, double height) {
		this(textString, textFont, Color.WHITE, Color.BLACK, Color.BLUE, Color.WHITE, xPos, yPos, width, height);
	}
	
	public void setIsHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}
	
	public boolean contains(int x, int y) {
		return boxShape.contains(x, y);
	}
	
	public void drawToGraphics(Graphics2D g2) {
		// if string Location hasn't been calculated yet
		if (stringX == 0) {	// we only do this once
			// uses the font metrics of the supplied font and the string to calculate how
			// to place the text in the center of the rectangle
			FontMetrics fontMetrics = g2.getFontMetrics(textFont);
			stringX = (float) (boxShape.getBounds().getX() + (boxShape.getBounds().getWidth() - fontMetrics.stringWidth(textString))/2);
			stringY = (float) (boxShape.getBounds().getY() + (boxShape.getBounds().getHeight() - fontMetrics.getHeight())/2 + fontMetrics.getAscent());
		}
		g2.setFont(textFont);
		if (isHighlighted) {
			g2.setColor(highlightedBoxColor);
			g2.fill(boxShape);
			g2.setColor(highlightedTextColor);
		} else {
			g2.setColor(boxColor);
			g2.fill(boxShape);
			g2.setColor(textColor);
		}
		// Draws the String
		g2.drawString(textString, stringX, stringY);
	}
}
