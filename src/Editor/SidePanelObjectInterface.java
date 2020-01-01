package Editor;

import java.awt.Graphics2D;
import PhysicsEngine.PhysicsObject;

public interface SidePanelObjectInterface {

	public static enum ObjectType {PLANET, STAR, GOAL}
	
	public void drawToGraphics(Graphics2D g2);
	
	public void highlight(int x, int y);
	
	public boolean isHighlighted();
	
	public CircleDisplayObject getCopyOf();
	
	public void setX(double newX);

	public void setXAndY(double newX, double newY);
	
	public PhysicsObject turnIntoPhysicsObject();
	
	public ObjectType getType();
}
