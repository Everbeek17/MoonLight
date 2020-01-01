// This class is just a reference for how I draw pictures to the JPanel

package DefaultPackage;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
 
public class DrawImage {
 
  static Image image;
 /*
  public static void main(String[] args) {
 
	// The image URL - change to where your image file is located!
	 
	String imageURL = "Media/Dickbutt.png";
	 
	// This call returns immediately and pixels are loaded in the background
	 
	image = Toolkit.getDefaultToolkit().getImage(imageURL);
	 
	// Create a frame
	Frame frame = new Frame();
	 
	// Add a component with a custom paint method
	 
	frame.add(new CustomPaintComponent());
	 
	// Display the frame
	 
	int frameWidth = 300;
	 
	int frameHeight = 300;
	 
	frame.setSize(frameWidth, frameHeight);
	 
	frame.setVisible(true);
 
 }
 */
 
 /**
  * To draw on the screen, it is first necessary to subclass a Component 
  * and override its paint() method. The paint() method is automatically called 
  * by the windowing system whenever component's area needs to be repainted.
  */
  static class CustomPaintComponent extends Component {
 
public void paint(Graphics g) {
 
  // Retrieve the graphics context; this object is used to paint shapes
 
  Graphics2D g2d = (Graphics2D)g;
 
  /**
   * Draw an Image object
   * The coordinate system of a graphics context is such that the origin is at the 
   * northwest corner and x-axis increases toward the right while the y-axis increases 
   * toward the bottom.
   */
 
  int x = 0;
 
  int y = 0;
 
  g2d.drawImage(image, x, y, null);
 
}
 
  }
 
}