package PhysicsEngine;
import java.awt.Graphics2D;

public abstract class PhysicsObject {
	
	private double xPos, yPos;
	protected double xVelocity, yVelocity;	// pixels/sec
	private double mass;
	
	// is this object allowed to be moved or not
	private boolean isStatic;

	public PhysicsObject(double xPos, double yPos, double mass) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.xVelocity = 0;
		this.yVelocity = 0;
		isStatic = true;
		this.mass = mass;
	}
	public PhysicsObject(double xPos, double yPos, double mass, 
			double xVelocity, double yVelocity) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		isStatic = false;
		this.mass = mass;
	}
	
	public double getX() { return xPos; }
	public double getY() { return yPos; }
	public double getMass() { return mass; }
	public double getXVelocity() { return xVelocity; }
	public double getYVelocity() { return yVelocity; }
	public boolean isStatic() { return isStatic; }
	public void setStatic(boolean newStatic) { isStatic = newStatic; }
	
	public void addForce(double xForce, double yForce) {
		// error checking
		if (isStatic) {
			System.out.println("What are you doing trying to add a force to a static object!");
		} else {
			xVelocity += xForce/mass;
			yVelocity += yForce/mass;
		}
	}
	
	/**
	 * Sets object's velocities to 0 to freeze it in place.
	 */
	public void cancelVelocity() {
		xVelocity = yVelocity = 0;
	}
	
	/**
	 * moves this Physics Object the distance and direction it would move in
	 * in the given elapsed time based on its current velocity.
	 * @param secondsElapsed	the number of seconds passed for the given interval
	 */
	public void increment(double secondsElapsed) {
		// only moves it if it is not static
		if (!isStatic) {
			xPos += xVelocity*secondsElapsed;
			yPos += yVelocity*secondsElapsed;
		}
	}
	
	/**
	 * Forces all subclasses to define a method on how to draw this specific object
	 * to the JPanel.
	 * @param g2	The Graphics2D of the JPanel to be drawn to.
	 */
	public abstract void drawToGraphics(Graphics2D g2);
}
