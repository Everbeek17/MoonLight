package DefaultPackage;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class FPSCounter {

	private int xPos, yPos;	// top right location of the counter
	private Font font;
	private Color color;
	private String displayString;
	private static final double updateInterval = 0.25;	// how often to update the counter
	
	long prev_frame_tick = System.nanoTime(), curr_frame_tick;
	
	public FPSCounter(int x, int y, Font font, Color color) {
		xPos = x;
		yPos = y;
		this.font = font;
		this.color = color;
		displayString = "0";
	}
	
	// calculates the number of frames being rendered per second based
	// on how fast the most recent frame was rendered, then convert that
	// to a string and save it.
	private int framesSinceLastUpdate;
	private double secondsSinceLastUpdate;
	private void increment(double nanoSecondsSinceLastFrame) {
		double secondsElapsed = nanoSecondsSinceLastFrame/1000000000;
		
		// continuously counts up how many frames have been rendered until
		// the interval time has passed and then updates based on those metrics
		secondsSinceLastUpdate += secondsElapsed;
		framesSinceLastUpdate++;
		
		if (secondsSinceLastUpdate >= updateInterval) {
			// the fps of the last interval (casted to int)
			int fps = (int) (framesSinceLastUpdate/secondsSinceLastUpdate);
			// saves calculated FPS as a String to be displayed
			displayString = Integer.toString(fps);
			// resets counters back to zero
			framesSinceLastUpdate = 0;
			secondsSinceLastUpdate = 0;
		}
	}
	
	public void drawToGraphics(Graphics2D g2) {
		// calculates how many nanoseconds since the last time this draw
		// method was called
		curr_frame_tick = System.nanoTime();
		increment(curr_frame_tick - prev_frame_tick);
		prev_frame_tick = curr_frame_tick;
		
		
		// draws the fps count
		// Get the FontMetrics
	    FontMetrics fontMetrics = g2.getFontMetrics(font);
	    // Set the font and color
	    g2.setColor(color);
	    g2.setFont(font);
	    // Draw the String
	    g2.drawString(displayString, xPos - fontMetrics.stringWidth(displayString),
	    		yPos + fontMetrics.getAscent() - 2);
	}
	
	
}
