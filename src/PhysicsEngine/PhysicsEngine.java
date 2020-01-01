package PhysicsEngine;
import java.awt.Shape;
import java.util.List;

public class PhysicsEngine {

	/**
	 * Tests if there is any overlap between the two Circle Objects
	 * @param A	First Object
	 * @param B	Second Object
	 * @return	true if there is overlap between the two objects
	 */
	public static boolean isThereOverlap(CircleObjectInterface A, CircleObjectInterface B) {
		// first test if AABBs are overlapping before doing any more complicated calculations
		if (areAABBsOverlapping(A, B)) {
			// the component distances between the two objects
			double xDist = A.getX() - B.getX();
			double yDist = A.getY() - B.getY();
			// test if the distance between two circles is less than the sum of the radii
			if ((xDist*xDist + yDist*yDist) < Math.pow(A.getRadius() + B.getRadius(), 2))
				return true;
		}
		// if no overlap detected
		return false;
	}
	
	/**
	 * Tests if the the bounding boxes around each circle have overlapped (aka collided).
	 * @param a	First Circle Object
	 * @param b	Second Circle Object
	 * @return	true if the bounding boxes are overlapping
	 */
	protected static boolean areAABBsOverlapping(CircleObjectInterface a, CircleObjectInterface b) {
		if (a.getX() + a.getRadius() + b.getRadius() > b.getX() &&
				a.getX() < b.getX() + a.getRadius() + b.getRadius() &&
				a.getY() + a.getRadius() + b.getRadius() > b.getY() &&
				a.getY() < b.getY() + a.getRadius() + b.getRadius())
			return true;
		return false;
	}
	
	/**
	 * Calculates gravitational forces for the given Physics Object pair
	 * and then only applies the forces to either object if it is non-static.
	 * @param a	first object of pair.
	 * @param b second object of pair.
	 */
	private static void applyGravityPair(PhysicsObject a, PhysicsObject b) {
		// the component distances between the two objects
		double xDist = a.getX() - b.getX();
		double yDist = a.getY() - b.getY();
		
		// if both objects are circles then check if they are already pressed together
		// if they are pressed together then don't do this calculation
		if (!((a instanceof CirclePhysicsObject) && (b instanceof CirclePhysicsObject)) ||
				(!isThereOverlap((CirclePhysicsObject) a, (CirclePhysicsObject) b))) {

			// gravity force equation (without the G constant tho)
			double Force = a.getMass()*b.getMass()/(
					Math.pow(xDist, 2) +
					Math.pow(yDist, 2));
			
			// the inverted angle between the two objects
			// (inverted because the JPanel uses inverted Y coordinates)
			double theta = Math.atan2(yDist, xDist);
			
			// the component forces between the two objects
			double xForce = Force*Math.cos(theta);
			double yForce = Force*Math.sin(theta);
			
			// adds force to object if it is non-static
			// (adding negative force to one of them)
			if (!a.isStatic())
				a.addForce(-xForce, -yForce);
			if (!b.isStatic())
				b.addForce(xForce, yForce);
		}
	}
	
	/**
	 * Calculates and applies all inter-Gravitational forces to every non-static
	 * physics object pair in the given List.
	 * @param list	The list of Physics Objects to be used.
	 * Time Complexity: O(n^2)
	 */
	protected static void applyInterGravity(List<? extends PhysicsObject> list) {
		// object 'a' iterates from beginning to second-to-last
		// object 'b' iterates from one ahead of 'a' to the last object
		// this^ method iterates through every pair in the given collection in O(n^2)
		for (int i = 0; i < list.size() - 1; i++) {
			PhysicsObject a = list.get(i);
			for (int j = i+1; j < list.size(); j++) {
				PhysicsObject b = list.get(j);
				
				// only do calculation if at least one object can be moved
				if (!(a.isStatic() && b.isStatic()))
					applyGravityPair(a,b);
			}
		}
	}
	/**
	 * Calculates and applies gravity from every static object onto each dynamic object.
	 * This method exists to supply faster calculations than just to calculate every object 
	 * against every other object.
	 * Assumes every object in their Collections are static or dynamic respectively.
	 * @param staticObjects		Collection of all static Objects.
	 * @param dynamicObjects	Collection of all dynamic Objects.
	 */
	protected static void applyFocusedGravity(Iterable<? extends PhysicsObject> staticObjects, Iterable<? extends PhysicsObject> dynamicObjects) {
		// iterates every dynamic object through every static object.
		// (e.g. every star iterates through every planet that will act on it)
		for (PhysicsObject dynObj : dynamicObjects)
			for (PhysicsObject statObj : staticObjects)
				applyGravityPair(dynObj, statObj);
	}
	
	/**
	 * Increments all the physics objects in an Iterable collection by the amount of time supplied.
	 * @param c	The Iterable collection of Physics Objects to be incremented.
	 * @param secondsElapsed	the number of seconds to increment the Collection by.
	 * Time Complexity: O(n)
	 */
	protected static void incrementCollection(Iterable<? extends PhysicsObject> objects, double secondsElapsed) {
		// iterate through every object
		for (PhysicsObject po : objects) {
			if (!po.isStatic())	// only update the non-static objects in the collection
				po.increment(secondsElapsed);	// update by supplied amount of time
		}
	}
	
	protected static void removeOutOfBounds(List<? extends PhysicsObject> list, Shape bounds) {
		for (int i = 0; i < list.size(); i++) {
			PhysicsObject po = list.get(i);
			if (!bounds.contains(po.getX(), po.getY())) {
				list.remove(i--);
			}
		}
	}
}
