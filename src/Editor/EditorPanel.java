package Editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import DefaultPackage.GameManager;
import DefaultPackage.MyMouseButton;
import PhysicsEngine.*;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

	private GameManager gm;
	private SidePanel sidePanel;
	private CircleDisplayObject mouseObject;	// the object the mouse is currently holding
	private List<PhysicsObject> physicsObjects;	// all the objects the user places on the screen
	// all the buttons on the screen
	private MyMouseButton [] buttonArray = new MyMouseButton[1];
	
	private CircleDisplayObject resizingObject;
	
	private enum Status {OPEN, CLOSED, OPENING, CLOSING}
	
	public EditorPanel(GameManager gm) {
		this.gm = gm;
		
		sidePanel = new SidePanel();	// instantiates side panel
		
		// needed to make the keyboard inputs apply to this
		setFocusable(true);
		
		// adds this class' listeners to this class' JPanel
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setBackground(Color.BLACK);	// sets the background color of the panel
		
		physicsObjects = new ArrayList<PhysicsObject>();
		
		// creates buttons
		buttonArray[0] = new MyMouseButton("Test Level", new Font("Comic Sans MS", Font.PLAIN, 18),
				GameManager.windowWidth - 90, GameManager.windowHeight - 50, 120, 38);
	}
	
	public void incrementEditor(double timeElapsed) {
		// converts nanoseconds to seconds
		double secondsElapsed = timeElapsed/1000000000;
		sidePanel.increment(secondsElapsed);
		
	}
	
	/** overriding paintComponent allows us to paint things to the screen */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	// passes g to overridden super method
		// cast the Graphics object to a Graphics2D object to allow us
		// access to specific painting methods
		Graphics2D g2 = (Graphics2D) g;
		
		
		// draws all physics objects that have been created and added already
		for (int i = 0; i < physicsObjects.size(); i++)
			physicsObjects.get(i).drawToGraphics(g2);
		// draws the resizingObject if it exists
		if (resizingObject != null)
			resizingObject.drawToGraphics(g2);
		// draws the mouse object if it exists
		if (mouseObject != null)
			mouseObject.drawToGraphics(g2);
		// draws sidePanel and/or its little arrow
		sidePanel.drawToGraphics(g2);
		
		// draws each button
		for (byte i = 0; i < buttonArray.length; i++)
			buttonArray[i].drawToGraphics(g2);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		if (mouseObject != null) {
			// keeps the mouse object a little bit below the mouse cursor
			mouseObject.setXAndY(e.getX() + 16, e.getY() + 19);
			
			// updates the shape of the resizing object if it exists
			if (resizingObject != null) {
				// if the object is a circle we are updating its radius
				if (resizingObject instanceof CircleDisplayObject) {
					CircleDisplayObject testObject = resizingObject.getCopyOf();
					// calculate the new radius
					double newRadius = Math.sqrt(Math.pow(e.getX() - testObject.getX(), 2) + Math.pow(e.getY() - testObject.getY(), 2));
					// set test object to new radius
					testObject.setRadius(newRadius);
					// test if the new radius will intersect with any other objects
					// if no collision detected, set new radius
					if (!doesObjectOverlap(testObject))
						resizingObject.setRadius(newRadius);
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// if side panel is closed and mouse is in specified area then we tell it to open
		if ((sidePanel.status == Status.CLOSED ||
				sidePanel.status == Status.CLOSING) &&
				sidePanel.isPointTouching(e.getX(), e.getY()))
			sidePanel.status = Status.OPENING;
		// if side panel is open
		else if ((sidePanel.status == Status.OPEN ||
				sidePanel.status == Status.OPENING)) {
			// if mouse is too far from panel we start closing it
			if (!sidePanel.isPointTouching(e.getX(), e.getY()))
				sidePanel.status = Status.CLOSING;
			
			// if mouse is over any object then we highlight it
			for (int i = 0; i < sidePanel.panelObjects.length; i++)
				sidePanel.panelObjects[i].highlight(e.getX(), e.getY());
			
			
		}
		
		// keeps the mouse object a little bit below the mouse cursor
		if (mouseObject != null) {
			mouseObject.setXAndY(e.getX() + 16, e.getY() + 19);
		}
		
		// highlights each mouse button if on top of it
		for (byte i = 0; i < buttonArray.length; i++) {
			if (buttonArray[i].contains(e.getX(), e.getY()))
				buttonArray[i].setIsHighlighted(true);
			else
				buttonArray[i].setIsHighlighted(false);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		// iterate through every side panel object
		for (int i = 0; i < sidePanel.panelObjects.length; i++) {
			// if any of the objects are highlighted when we click
			// then we take a copy of that object and attach it to the mouse
			if (sidePanel.panelObjects[i].isHighlighted()) {
				mouseObject = sidePanel.panelObjects[i].getCopyOf();
				// makes the radius smaller
				mouseObject.setRadius(7);
				break;
			}
		}
		
		if (buttonArray[0].contains(e.getX(), e.getY()))
			gm.startGame(physicsObjects);

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	
	@Override
	public void mousePressed(MouseEvent e) {
		// starts creating a new instance of the mouse object where the user clicked
		// if they are not currently on the side panel
		if (mouseObject != null && !sidePanel.isPointTouching(e.getX(), e.getY())) {
			resizingObject = mouseObject.getCopyOf();
			resizingObject.setXAndY(e.getX(), e.getY());
			// test if new object is overlapping anything and deletes it if it is
			if (doesObjectOverlap(resizingObject))
				resizingObject = null;
			else {
				// if the object being created is a Star then we immediately create it without
				// waiting for mouse to be unclicked
				if (resizingObject.getType().equals(SidePanelObjectInterface.ObjectType.STAR)) {
					//resizingObject.setRadius(20);	// the radius for each star
					physicsObjects.add(resizingObject.turnIntoPhysicsObject());
					resizingObject = null;
				} else	// else we start creating the new object with a radius of zero
					resizingObject.setRadius(0);
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// if an object is currently being defined
		if (resizingObject != null) {
			// we get the physics object instance of that object and add
			// it to the list of all physics objects on the screen at the moment
			physicsObjects.add(resizingObject.turnIntoPhysicsObject());
			resizingObject = null;
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyID = e.getKeyCode();	// saves the ID of the key pressed;
		switch (keyID) {
		case KeyEvent.VK_ESCAPE:
			System.out.println("Quitting Game.");
			gm.quitGame();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	
	/**
	 * Tests if the provided object would collide with anything already on the screen
	 * @param testObject
	 * @return
	 */
	private boolean doesObjectOverlap(CircleDisplayObject testObject) {
		// iterate through every object already placed on the screen
		for (int i = 0; i < physicsObjects.size(); i++) {
			PhysicsObject collisionTestObject = physicsObjects.get(i);
			// if the object testing against is also a circle and they overlap
			if ((collisionTestObject instanceof CirclePhysicsObject) && 
					PhysicsEngine.isThereOverlap(testObject, (CirclePhysicsObject) collisionTestObject)) {
				// then return true
				return true;
			}
		}
		// if no collision detected, return false
		return false;
	}
	
	
	private class SidePanel {
		
		public Status status;
		private static final int mouseThreshold = 20;
		private static final int fullWidth = 120;	// the width of the panel when it's fully open
		
		private double xPos;
		private static final int height = GameManager.windowHeight;
		private double openingSpeed;	// pixels per second
		private double closingSpeed;
		
		public SidePanelObjectInterface [] panelObjects;
		
		public SidePanel() {
			status = Status.CLOSED;	// starts the panel closed
			xPos = 0;	// x Position of panel
			openingSpeed = closingSpeed = 325;	// speed the panel opens and closes
			
			// creates and saves the objects to be available on the side object
			panelObjects = new SidePanelObjectInterface[3];
			
			// Planet
			panelObjects[0] = new CircleDisplayObject(xPos, 60, 25, Color.BLUE, SidePanelObjectInterface.ObjectType.PLANET);
			// Star
			panelObjects[1] = new CircleDisplayObject(xPos, 120, 5, Color.YELLOW, SidePanelObjectInterface.ObjectType.STAR);
			// Goal
			panelObjects[2] = new CircleDisplayObject(xPos, 180, 20, Color.GREEN.darker(), SidePanelObjectInterface.ObjectType.GOAL);
			
			
		}
		public boolean isPointTouching(int x, int y) {
			if (x < xPos + mouseThreshold)
				return true;
			return false;
		}
		
		// moves the panel open or close
		public void increment(double secondsElapsed) {
			// updates the xPos of the rightmost of the panel
			// until it is fully open or closed
			if (status == Status.OPENING && xPos < fullWidth)
				xPos += openingSpeed*secondsElapsed;
			else if (status == Status.CLOSING && xPos > 0)
				xPos -= closingSpeed*secondsElapsed;
			
			// updates panel objects each time xPos is updated
			updatePanelObjectsXPos();
			
			// changes status once opened or closed all the way
			if (xPos > fullWidth) {
				xPos = fullWidth;
				status = Status.OPEN;
				updatePanelObjectsXPos();	// updates panel objects a single time after reaching full open
			} else if (xPos < 0) {
				xPos = 0;
				status = Status.CLOSED;
			}
		}
		
		/**
		 * Updates the xPosition of each object on the panel
		 */
		private void updatePanelObjectsXPos() {
			for (int i = 0; i < panelObjects.length; i++)
				panelObjects[i].setX(xPos - fullWidth/2);
		}
		
		private static final double triangleSideLen = 10;
		private Color triangleColor = Color.WHITE;
		
		public void drawToGraphics(Graphics2D g2) {
			// draws actual panel only if it isn't closed
			if (status != Status.CLOSED) {
				// draws background rectangle
				g2.setColor(Color.GRAY);
				g2.fill(new Rectangle2D.Double(0, 0, xPos, height));
				
				// draws each object on the panel
				for (int i = 0; i < panelObjects.length; i++)
					panelObjects[i].drawToGraphics(g2);
			}
			// draws side panel's little triangle
			if (status != Status.OPEN) {
				// defines triangle as 3 vertices
				// (can't use fillPolygon because that only takes int and I want to use double)
				Path2D openTriangle = new Path2D.Double();
				openTriangle.moveTo(xPos + triangleSideLen/2, (height - triangleSideLen)/2);
				openTriangle.lineTo(xPos + triangleSideLen/2, (height + triangleSideLen)/2);
				openTriangle.lineTo(xPos + (1 + Math.sqrt(2))*triangleSideLen/2, height/2);
				openTriangle.closePath();
				
				// draws triangle with respective transparency based on it's location
				g2.setColor(new Color(triangleColor.getRed(),
						triangleColor.getGreen(), triangleColor.getBlue(),
						(int) (255*(1-xPos/fullWidth))));
				g2.fill(openTriangle);
				
			}
		}
	}
	
}
