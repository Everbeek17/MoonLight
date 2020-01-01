package DefaultPackage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import PhysicsEngine.*;

public class MoonLightEngine extends PhysicsEngine {
	
	private Shape playableArea;
	
	private List<CirclePhysicsObject> stars;
	private List<CirclePhysicsObject> planets;
	
	private Goal goal;
	
	private Star selectedStar;
	private int selectedStarTargetX, selectedStarTargetY;

	private Queue<CirclePhysicsObject> aimingStars;
	
	private double shooterStrength = 0.5;	// higher value = stronger shot
	
	// creates a dashed stroke
	private final static float dash1[] = {5.0f};
	private final static BasicStroke dashedStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
            10.0f, dash1, 0.0f);
	
	public MoonLightEngine(Shape playableArea) {
		stars = new ArrayList<CirclePhysicsObject>();
		planets = new ArrayList<CirclePhysicsObject>();
		this.playableArea = playableArea;
		aimingStars = new LinkedList<CirclePhysicsObject>();
	}
	
	public void setSelectedStar(Star s) { selectedStar = s; }
	public Star getSelectedStar() { return selectedStar; }
	
	public void setupLevel(List<? extends PhysicsObject> list) {
		// clears the board
		stars.clear();
		planets.clear();
		goal = null;
		// iterate through all objects passed
		// adding each object to its respective group
		for (int i = 0; i < list.size(); i++) {
			PhysicsObject obj = list.get(i);
			if (obj instanceof Star) {
				stars.add((CirclePhysicsObject) obj);
			} else if (obj instanceof Planet) {
				planets.add((CirclePhysicsObject) obj);
			} else if (obj instanceof Goal) {
				goal = (Goal) obj;
			}
		}
	}
	
	public void setupLevel(int level) {
		// clears the board first
		stars.clear();
		switch (level) {
		case -1:	// testing level
			
			stars.add(new Star(800, 100, 4, -60, -10));
			//stars.add(new Star(200, 204, 4, -10, 0));
			
			goal = new Goal(500,500, 25, 10);
			
			for (int i = 0; i < 40; i++) {
				for (int j = 0; j < 60; j++) {
					stars.add(new Star(25+ 9*i, 25 + 9*j, 4));
				}
			}

			planets.add(new Planet(450, 300, 12800, 26, Color.BLUE));
			
			
			break;
		case 1:	// level 1
			// set star locations
			stars.add(new Star(200, 200, 5));
			stars.add(new Star(300, 200, 5));
			stars.add(new Star(100, 100, 2));
			//stars.add(new Star(500, 500, 10, -10, 0));
			planets.add(new Planet(400, 200, 12000, 25, Color.GREEN));
			//moon = new Goal(700,500,40,20,0);
			// just for fun:
			
			goal = new Goal(500,500, 25, 10);
			break;
		}
	}
	
	/**
	 * Returns the exact position where the two supplied star are colliding.
	 * @param a	First Star
	 * @param b	Second Star
	 * @return	a double array containing the X and Y coordinates, respectively, of where the two stars are colliding
	 */
	private static double [] getCollisionPoint(Star a, Star b) {
		double collisionPointX = ((a.getX() * b.getRadius()) + (b.getX() * a.getRadius())) /
				(a.getRadius() + b.getRadius());
		double collisionPointY = ((a.getY() * b.getRadius()) + (b.getY() * a.getRadius())) /
				(a.getRadius() + b.getRadius());
		double [] returnPos = {collisionPointX, collisionPointY};
		return returnPos;
	}
	
	/**
	 * Checks for and then handles all collisions that happen between the items in the given list
	 * @param list	The PhysicsObjects being checked and handled
	 */
	private void checkForAndHandleCollisions(List<? extends CirclePhysicsObject> list) {
		
		double xDist, yDist;
		
		// iterate through every pair of items in the list
		for (int i = 0; i < list.size()-1; i++) {
			CirclePhysicsObject A = list.get(i);
			for (int j = i+1; j < list.size(); j++) {
				CirclePhysicsObject B = list.get(j);
				// makes sure at least one is not static and the AABBs are overlapping
				if ((!A.isStatic() || !B.isStatic()) && areAABBsOverlapping(A, B)) {
					
					
					
					
					
					
					// NOT MY CODE, FOUND ON STACK EXCHANGE( with some modifications from me):
					// https://gamedev.stackexchange.com/questions/20516/ball-collisions-sticking-together
					
					// I wish I could write code this nice :'(, in due time...
					
					xDist = A.getX() - B.getX();
		            yDist = A.getY() - B.getY();
		            double distSquared = xDist*xDist + yDist*yDist;
		            //Check the squared distances instead of the the distances, same result, but avoids a square root.
		            if(distSquared <= Math.pow(A.getRadius() + B.getRadius(), 2)){
		            	// if two circle items are touching/overlapping

		            	// if one of the objects colliding is a Goal
		            	if ((A instanceof Goal) || (B instanceof Goal)) {
		            		// if the other object colliding is a Star
		            		if (A instanceof Star) {	// if A is a star
		            			// then B is the goal
		            			((Goal) B).incrementToCapacity();	// we increment the Goal
		            			stars.remove(A);					// and remove the star
		            		} else if (B instanceof Star){	// if B is a star
		            			// then A is the goal
		            			((Goal) A).incrementToCapacity();	// we increment the Goal
		            			stars.remove(B);					// and remove the star
		            		}	// if neither are Stars (both are goals) then do nothing
		            	} else { // if neither are Goals
			                double xVelocity = B.getXVelocity() - A.getXVelocity();
			                double yVelocity = B.getYVelocity() - A.getYVelocity();
			                double dotProduct = xDist*xVelocity + yDist*yVelocity;
			                //Neat vector maths, used for checking if the objects moves towards one another.
			                if(dotProduct > 0){
			                	double collisionScale = dotProduct / distSquared;
			                    
			                	double xCollision = xDist * collisionScale;
			                    double yCollision = yDist * collisionScale;
			                    //The Collision vector is the speed difference projected on the Dist vector,
			                    //thus it is the component of the speed difference needed for the collision.
			                    double combinedMass = A.getMass() + B.getMass();
			                    double collisionWeightA = 2 * B.getMass() / combinedMass;
			                    double collisionWeightB = 2 * A.getMass() / combinedMass;
			                    
			                    
			                    
			                    
			                    
			                    
			                    // back to my code
			                    // sets both to non-static and applies force after they've participated in a collision if they're Stars
			                    if (A instanceof Star) {
			                    	A.setStatic(false);
			                    	A.addForce(A.getMass() * collisionWeightA * xCollision,
				                    		A.getMass() * collisionWeightA * yCollision);
			                    }
			                    if (B instanceof Star) {
			                    	B.setStatic(false);
			                    	B.addForce(-B.getMass() * collisionWeightB * xCollision,
				                    		-B.getMass() * collisionWeightB * yCollision);
			                    }
			                    
			                    
			                    
		
			                }
		            	}
		            }
				}
				/*	My original attempt
				// if the stars have collided
				if (haveStarsCollided(a, b)) {
					// sets them both to non-static (so that we can hit static stars to make them non-static)
					a.setStatic(false);
					b.setStatic(false);
					// bounces them off of each other (update their velocities)
					bounceStars(a, b);
				}
				*/
			}
		}
	}
	
	// finds the closest static star to the given location that is also within the given range,
	// then sets that star as the "selectedStar"
	// if no stars are in range then selectedStar is set to null
	public void setSelectedStarClosestWithinRange(int x, int y, double range) {
		Star closestStar = null;
		double closestDistance = range;
		for (PhysicsObject star : stars) {
			if (star.isStatic()) {	// only selects a static star
				double currDistance = (Math.sqrt(Math.pow(star.getX() - x, 2) + Math.pow(star.getY() - y, 2)));
				if (currDistance < closestDistance) {
					closestDistance = currDistance;
					closestStar = (Star) star;
				}
			}
		}
		selectedStar = closestStar;
	}
	
	
	void pointSelectedStarTowards(int x, int y) {
		selectedStarTargetX = x;
		selectedStarTargetY = y;
	}
	
	// sets selected star to null.
	public void deselectSelectedStar() { selectedStar = null; }
	public boolean isAStarSelected() { return selectedStar != null; }
	
	
	// maybe tell the game to shoot the star on the next game tick that way we aren't overlapping with the incrementing method
	// as this class is called by a different thread than the thread incrementing the game
	// hpefully it will fix that one arrayIndec out of bounds issue I had.
	private boolean shootOnNextGameTick;
	public void shootSelectedStar() {
		shootOnNextGameTick = true;
	}

	private boolean removeAimingStars = false;
	private void incrementAimingStars(double secondsElapsed) {
		int numAimingStars = 25;	// how many aimingStars to fill the path
		int secondsAhead = 5;	// how many seconds into the future will this predict
		// shoot new star from source
		
		if (selectedStar != null) {
			removeAimingStars = false;	// stops removing aiming stars if new aiming star is selected
			double xAccel = (selectedStarTargetX - selectedStar.getX())/shooterStrength;
			double yAccel = (selectedStarTargetY - selectedStar.getY())/shooterStrength;
			
			aimingStars.add(new Star(selectedStar.getX(), selectedStar.getY(), selectedStar.getRadius()/2,
					selectedStar.getXVelocity()+xAccel,
					selectedStar.getYVelocity()+yAccel,
					Color.GRAY));
		}
		
		// starts removing oldest aiming star once size reaches a certain number
		if (aimingStars.size() > numAimingStars)
			removeAimingStars = true;
		else if (aimingStars.isEmpty())	// keeps removing aiming stars until empty
			removeAimingStars = false;
		
		if (removeAimingStars)
			aimingStars.poll();	// removes oldest aiming star
		
		// increments the aimingStars multiple times to get a future vision of where the selected Star would travel
		// only if non-empty
		if (!aimingStars.isEmpty())
			for (int i = 0; i < (int) (secondsAhead/(secondsElapsed*numAimingStars)); i++ ) {
				applyFocusedGravity(planets, aimingStars);
				incrementCollection(aimingStars, secondsElapsed);
			}
	}
	
	private void shoot(Star shootingStar) {
		// calculates desired accelerations based on  mouse location
		double xAccel = (selectedStarTargetX - shootingStar.getX())/shooterStrength;
		double yAccel = (selectedStarTargetY - shootingStar.getY())/shooterStrength;
		
		shootingStar.setStatic(false);	// makes star dynamic
		
		// shoots the star with the calculated force
		shootingStar.addForce(shootingStar.getMass()*xAccel,
				shootingStar.getMass()*yAccel);
	}
	
	public void incrementGame(double secondsElapsed) {
		
		// applies gravitational forces
		//applyInterGravity(stars);
		applyFocusedGravity(planets, stars);
		// moves stars in their respective velocities for the given time interval
		incrementCollection(stars, secondsElapsed);
		
		// deletes all stars that are out of bounds
		removeOutOfBounds(stars, playableArea);
		
		// creates a list with all stars, then appends all planets to it.
		List<CirclePhysicsObject> StarsPlanetsAndGoals = new ArrayList<CirclePhysicsObject>(stars);
		StarsPlanetsAndGoals.addAll(planets);
		if (goal != null)
			StarsPlanetsAndGoals.add(goal);
		// check and account for all collisions between all Stars and Planets
		checkForAndHandleCollisions(StarsPlanetsAndGoals);
		
		incrementAimingStars(secondsElapsed);
		// if a star is currently selected
		if (selectedStar != null) {
			// if the selectedStar is no longer static then deselect it
			if (!selectedStar.isStatic())
				selectedStar = null;
			else {
				// if a shoot is requested
				if (shootOnNextGameTick) {
					shootOnNextGameTick = false;
					shoot(selectedStar);	// shoots star
					selectedStar = null;	// deselects star
				} else {
					// otherwise just increment the aimingStars
					
				}
			}
		}
	}

	/**
	 * Draws all physics objects to the given Graphics2D object
	 * @param g2	the Graphics2D object to draw to 
	 */
	public void drawToGraphics(Graphics2D g2) {

		// draws all planets
		for (PhysicsObject planet : planets)
			planet.drawToGraphics(g2);
		if (selectedStar != null) {
			/*
			g2.setColor(Color.GRAY);
			g2.setStroke(dashedStroke);
			g2.draw(new Line2D.Double(selectedStar.getX(), selectedStar.getY(),
					selectedStarTargetX, selectedStarTargetY));
			g2.setStroke(new BasicStroke());	// resets stroke back to default
			*/
			
			// so that we can have a ring around the selected star
			selectedStar.drawSelectionRing(g2);
			
			
			
			
			// THROWS NULL POINTER EXCEPTION SOMETIMES BECAUSE IDK...
			// concurrent modification?! bitch nuh uh it's all on the same thread! I think...
			
		}
		// draws all aimingStars
		for (CirclePhysicsObject aimingStar : aimingStars)
			aimingStar.drawToGraphics(g2);
		// draws all stars
		for (PhysicsObject star : stars)
			star.drawToGraphics(g2);
		
		// draws goal
		if (goal != null)
			goal.drawToGraphics(g2);
	}
}
