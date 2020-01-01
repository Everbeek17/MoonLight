package DefaultPackage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MenuPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	
	private FPSCounter fpsCounter;
	
	private static final Font comicSans = new Font("Comic Sans MS", Font.PLAIN, 18);
	private static final Font defaultFont = new Font("TimeRoman", Font.PLAIN, 18);
	private static final Font smallFont = new Font("TimeRoman", Font.PLAIN, 12);
	
	// all the buttons on the screen
	private MyMouseButton [] buttonArray = new MyMouseButton[2];
	
	// saves reference to the GameManager so we can dispose of this panel from within
	private GameManager gm;
	
	/** constructor */
	public MenuPanel(GameManager gm) {
		
		// saves GameManager reference locally
		this.gm = gm;
		
		// needed to make the keyboard inputs apply to this
		setFocusable(true);
		
		// adds this class' listeners to this class' JPanel
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setBackground(Color.WHITE);	// sets the background color of the panel
		
		// creates the buttons
		buttonArray[0] = new MyMouseButton("Start Game", comicSans,
				GameManager.windowWidth/2, 200, 300, 50);
		buttonArray[1] = new MyMouseButton("Level Editor", comicSans,
				GameManager.windowWidth/2, 300, 300, 50);
		
		// creates a new FPS counter
		//fpsCounter = new FPSCounter(gm.windowWidth - 10, 10, defaultFont, Color.WHITE);
		
	}

	// This call returns immediately and pixels are loaded in the background
	private Image image = Toolkit.getDefaultToolkit().getImage("Media/smile.png");
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		int keyID = arg0.getKeyCode();	// saves the ID of the key pressed;
		switch (keyID) {
		case KeyEvent.VK_ENTER:
			gm.startGame();
			break;
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
	 * overriding paintComponent allows us to paint things to the screen */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	// passes g to overridden super method
		// cast the Graphics interface object to a Graphics2D object to allow us
		// access to specific painting methods
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(image, 0, 0, GameManager.windowWidth, GameManager.windowHeight,
			       0, 0, image.getWidth(null), image.getHeight(null),
			       null);
			       
		// writes version number in the top right corner
		g2.setColor(Color.LIGHT_GRAY);
		g2.setFont(smallFont);
		g2.drawString(GameManager.GameVersion, GameManager.windowWidth - GameManager.GameVersion.length()*7, 16);
		
		// draws each button
		for (byte i = 0; i < buttonArray.length; i++)
			buttonArray[i].drawToGraphics(g2);
		
		g2.setColor(Color.WHITE);
		g2.setFont(defaultFont);
		//g2.drawString("Press 'esc' to exit at anytime.", 180, 80);
		
		
		if (fpsCounter != null)
			fpsCounter.drawToGraphics(g2);
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (buttonArray[0].contains(e.getX(), e.getY()))
			gm.startGame();
		else if (buttonArray[1].contains(e.getX(), e.getY()))
			gm.startEditor();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (byte i = 0; i < buttonArray.length; i++) {
			if (buttonArray[i].contains(e.getX(), e.getY()))
				buttonArray[i].setIsHighlighted(true);
			else
				buttonArray[i].setIsHighlighted(false);
		}
		repaint();
	}
}
