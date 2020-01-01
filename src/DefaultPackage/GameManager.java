package DefaultPackage;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;

import Editor.EditorPanel;
import PhysicsEngine.PhysicsObject;

@SuppressWarnings("serial")
public class GameManager extends JFrame implements Runnable {
	
	// JPanels
	private MenuPanel mp;
	private GamePanel gp;
	private EditorPanel ep;
	
	private Thread gameThread;
	
	private boolean running = false, editting = false;
	
	public static final double desiredCalculationsPerSec = 120D;
	public static double timeFactor = 1;
	public static final String GameVersion = "V0.1";
	
	// dimensions for the JFrame window
	public static final int windowWidth = 800, windowHeight = 600;	
	
	/** Constructor */
	public GameManager() {
		// initialization stuff for the frame
		setTitle("Test");		// sets the title of the Window (the JFrame)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// frees up memory when the window is closed
		setResizable(false);					// allows/disallows resizing of window
		setLocationByPlatform(true);			// lets the OS handle the positioning of the window
		setVisible(true);	// makes window visible

		// instantiates menuPanel
		mp = new MenuPanel(this);
		mp.setPreferredSize(new Dimension(windowWidth, windowHeight));
		// adds the menu JPanel to this JFrame
		add(mp);
		pack();
	}
	
	public boolean isGameRunning() { return running; }

	
	// exits editor panel and starts game with given objects
	public void startGame(List<? extends PhysicsObject> list) {
		remove(ep);	// removes editor panel
		
		// creates and adds game panel with the list objects
		gp = new GamePanel(this, list);
		add(gp);
		
		// validate() needed to switch JPanels for some reason.
		validate();
		gp.requestFocusInWindow();	// gives input control to gp
		
		// switches gameThread from editting to playing
		editting = false;
	}
	
	/** removes the menu panel and creates/adds game panel. */
	public void startGame() {
		// removes menu panel
		remove(mp);
		
		// creates and adds game panel
		gp = new GamePanel(this);
		add(gp);
		
		// validate() needed to switch JPanels for some reason.
		validate();
		gp.requestFocusInWindow();	// gives input control to gp

		
		// creates and starts the Thread for the gameThread
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	// removes menu panel and starts editor panel
	public void startEditor() {
		remove(mp);	// removes the menu panel
		
		ep = new EditorPanel(this);	// creates the editor panel
		add(ep);	// adds the editor panel to this frame
		
		validate();	// have to do this while switching Jpanels for whatever reason
		ep.requestFocusInWindow();	// gives input control to ep
		
		// creates and starts game thread but in editor mode
		editting = true;
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	/** disposes of this JFrame and the gameThread, aka ends the game. **/
	public void quitGame() { 
		if (running) {
			running = false;	// stops gameThread
			// waits for gameThread to end
			try {
				gameThread.join();
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		this.dispose();
	}
	
	public void gameOver() {
		running = false;
		
		
		
	}
	
	
	
	/**
	 * This method is called when the GameThread begins. Contains main game loop.
	 * Found this method on an online game tutorial.
	 */
	@Override
	public void run() {
		long prev_frame_tick = System.nanoTime(), curr_frame_tick, timeElapsed,
				prev_physics_tick = prev_frame_tick;
		double ns = 1000000000 / (desiredCalculationsPerSec * timeFactor);
		double delta = 0;
		
		
		
		// Main Game Loop
		while (running) {
		    curr_frame_tick = System.nanoTime();
		    timeElapsed = curr_frame_tick - prev_frame_tick;
		    delta += timeElapsed / ns;
		    prev_frame_tick = curr_frame_tick;
		    
		    // updates game (desiredCalculationsPerSec) times a second
		    if (delta >= 1) {
		    	// sends the number of nanoseconds since the last physics tick happened
		    	if (editting)
		    		ep.incrementEditor(curr_frame_tick - prev_physics_tick);
		    	else
		    		gp.incrementGame(curr_frame_tick - prev_physics_tick);
		    	prev_physics_tick = curr_frame_tick;
		    	delta--;
		    }
		    // paints screen as many times as we can
		    if (editting)
		    	ep.repaint();
		    else
		    	gp.repaint();
		}
	}
	
	/** main class to instantiate an instance of this GameManager which
	 *  will also create the JFrame that everyone will appear on. 
	 */
	public static void main(String [] args) {
		// creates an instance of this game manager
		// (whose reference doesn't have to be saved apparently)
		new GameManager();

		
		
	}
}
