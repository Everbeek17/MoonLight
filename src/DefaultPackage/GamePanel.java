package DefaultPackage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JPanel;

import PhysicsEngine.PhysicsObject;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener   {
	// saves reference to Game Manager so we can do some top-end tasks
	private GameManager gm;
	
	
	
	// the radius of playable area outside of the visible window
	private static int windowBuffer = 200;	// in pixels
	
	// Physics Engine handles all physics-bound object calculations
	private MoonLightEngine engine;

	// to Draw some heads up display stuff
	private HUD hud;
	
	
	/** Constructor */
	public GamePanel(GameManager gm) {
		this.gm = gm;	// saves reference
		
		// constructs the Physics Engine passing it the playable area
		Rectangle playableArea = new Rectangle(-windowBuffer, -windowBuffer,
				(2*windowBuffer) + GameManager.windowWidth, (2*windowBuffer) + GameManager.windowHeight); 
		engine = new MoonLightEngine(playableArea);
		engine.setupLevel(-1);	// starts at level 1
		
		// needed to make the keyboard inputs apply to this
		setFocusable(true);
		
		// adds this class' listeners to this class' JPanel
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setBackground(Color.BLACK);	// sets the background color of the panel
		
		
		// instantiates HUD
		hud = new HUD();
	}
	
	public GamePanel(GameManager gm, List<? extends PhysicsObject> list) {
		this.gm = gm;	// saves reference
		
		// constructs the Physics Engine passing it the playable area
		Rectangle playableArea = new Rectangle(-windowBuffer, -windowBuffer,
				(2*windowBuffer) + GameManager.windowWidth, (2*windowBuffer) + GameManager.windowHeight); 
		engine = new MoonLightEngine(playableArea);
		engine.setupLevel(list);	// setups level
		
		// needed to make the keyboard inputs apply to this
		setFocusable(true);
		
		// adds this class' listeners to this class' JPanel
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setBackground(Color.BLACK);	// sets the background color of the panel
		
		// instantiates HUD
		hud = new HUD();
	}
	
	
	/** 
	 * Does required calculations/updates required variables based on how much time has passed.
	 * Also converts incoming nanoseconds to seconds.
	 * @param timeElapsed time since last update in nanoseconds
	 */
	public void incrementGame(double timeElapsed) {
		// converts nanoseconds to seconds and multiplies by timeFactor
		double secondsElapsed = (timeElapsed/1000000000)*GameManager.timeFactor;
		
		
		engine.incrementGame(secondsElapsed);
		//fpsCounter.updateCounter(secondsElapsed);
	}
	
	
	/** overriding paintComponent allows us to paint things to the screen */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	// passes g to overridden super method
		// cast the Graphics object to a Graphics2D object to allow us
		// access to specific painting methods
		Graphics2D g2 = (Graphics2D) g;
		if (gm.isGameRunning()) {
			engine.drawToGraphics(g2);	// draws all physics objects
			
			hud.drawToGraphics(g2);
			

		} else { // else we have reached a game over.
			g2.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
			g2.drawString("Game Over. Thanks for playing!", 220, 300);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		int keyID = arg0.getKeyCode();	// saves the ID of the key pressed;
		switch (keyID) {
		case KeyEvent.VK_ENTER:
			System.out.println("Enter pressed while in-game.");
			break;
		case KeyEvent.VK_ESCAPE:
			if (engine.isAStarSelected()) {
				engine.deselectSelectedStar();
			} else {
				System.out.println("Quitting Game.");
				gm.quitGame();
			}
			break;
		case KeyEvent.VK_LEFT:
			//player.setDirection(Player.DIR.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			//player.setDirection(Player.DIR.RIGHT);
			break;
		case KeyEvent.VK_SPACE:
			//pe.spawnBullet();	// shoot a bullet if space is pressed
			break;
		case KeyEvent.VK_UP:
			//GameManager.timeFactor += 0.5;
			break;
		case KeyEvent.VK_DOWN:
			//if (GameManager.timeFactor > 0.5)
			//	GameManager.timeFactor -= 0.5;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		int keyID = arg0.getKeyCode();	// saves the ID of the key pressed;
		switch (keyID) {
		// stops moving in current direction if that directional key was released
		case KeyEvent.VK_LEFT:
			//if (player.getDirection() == Player.DIR.LEFT) player.setDirection(Player.DIR.NULL);
			break;
		case KeyEvent.VK_RIGHT:
			//if (player.getDirection() == Player.DIR.RIGHT) player.setDirection(Player.DIR.NULL);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent e) {
		
		
		// shoots star on second click
		if (engine.getSelectedStar() != null) {
			engine.shootSelectedStar();
		} else {
			// sets active star to closest star
			engine.setSelectedStarClosestWithinRange(e.getX(), e.getY(), 100);
			// and points to mouse
			if (engine.getSelectedStar() != null)
				engine.pointSelectedStarTowards(e.getX(), e.getY());
			}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		//player.setHasDottedLine(false);
		//pe.spawnBullet();
	}

	/**
	 * Updates strength and direction of shooter while mouse is held.
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) {}
	@Override
	public void mouseMoved(MouseEvent e) {
		if (engine.getSelectedStar() != null)
			engine.pointSelectedStarTowards(e.getX(), e.getY());
	}
}
