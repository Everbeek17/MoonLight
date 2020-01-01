package DefaultPackage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * A Heads Up Display to show some onscreen data/art to the player.
 * @author Erkin Verbeek
 *
 */
public class HUD {
	// a couple fonts to use
	private static final Font comicSans = new Font("Comic Sans MS", Font.PLAIN, 18);
	private static final Font defaultFont = new Font("TimeRoman", Font.PLAIN, 18);
	private static final Font smallFont = new Font("TimeRoman", Font.PLAIN, 12);

	private FPSCounter fpsCounter;
	
	public HUD() {
		// instantiates the FPS Counter
		fpsCounter = new FPSCounter(GameManager.windowWidth - 6, 5, defaultFont, Color.WHITE);
	}
	
	
	public void drawToGraphics(Graphics2D g2) {
		
		// updates the FPS Counter
		if (fpsCounter != null)
			fpsCounter.drawToGraphics(g2);
		
		
		
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// notifies if time is being warped in either direction
		if (GameManager.timeFactor != 1.0) {
			g2.setFont(defaultFont);
			g2.drawString("Time: x" + GameManager.timeFactor, 10, 25);
		}
	}
}
