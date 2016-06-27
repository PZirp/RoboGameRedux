package robotgameredux.systemsImplementations;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.MovementCommand;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.MovementSystem;

public class StandardMovementSystem implements MovementSystem, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1841818356815052104L;
	public StandardMovementSystem(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	@Override
	public Boolean execute(MovementCommand command) throws InvalidTargetException, InsufficientEnergyException {
		Integer dist = (int) command.getCoords().dst(command.getDestination());
		if (follow == null) {
			if (command.getEnergy() == 0 || command.getEnergy() < dist) {
				command.resetRobot();
				throw new InsufficientEnergyException(command);
			}
			possiblePaths = gameWorld.pathfind(command.getCoords(), command.getRange());
			if (!beginMovement(command)) {
				command.resetRobot();
				gameWorld.highlightPath(); // Migliorare
				throw new InvalidTargetException(command);
			}
		} else {
			if (continueMovement(command)) return true;	
		}
		return false;
	}
	
	public Boolean beginMovement(MovementCommand command) {

		Vector2 destination = command.getDestination();
		Integer dist = (int) command.getCoords().dst(destination);
		Vector2 oldPos = command.getCoords();
		if (destinationCheck(destination, command.getCoords()) && dist < command.getRange()) {
			generatePath(destination, command.getCoords());		
			if(!collisionDetection()) {
				return false;
			}
			energyExpenditure = command.getCoords().dst(destination);
			//possiblePaths = null;
			//command.setCoords(follow.get(0));
			//follow.remove(0);
			//gameWorld.releaseTile(oldPos);
			//Bug: se mi muovo di una sola casella non completa il movimento (rimane il comando in attesa) < Fixed
			//Bug: Non libera la casella di partenza; < Fixato
			command.setState(RobotStates.MOVING);
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Eccomi qui");
			System.out.println("Casella non raggiungibile o occupata");
			command.resetRobot();
			return false;
		}
	}
	
	public Boolean continueMovement(MovementCommand command) {
		Vector2 oldPos = command.getCoords();
	//	JOptionPane.showMessageDialog(null, oldPos.toString());
			command.setCoords(follow.get(0));
			follow.remove(0);
			gameWorld.releaseTile(oldPos);
			if (command.getCoords().equals(command.getDestination())) {
				movementComplete(command.getDestination(), oldPos);
				command.removeEnergy(energyExpenditure);
				energyExpenditure = 0;
				command.resetRobot();
				follow = null;
				return true;
			}
			return false;

	}
	
	private Boolean destinationCheck(Vector2 destination, Vector2 current) {
		if (validDestination(destination)) {
			if (current.dst(destination) == 0) {
				System.out.println("Sei già sulla tile scelta");
				return false;
			} else if (gameWorld.isTileFree(destination)) {
				return true;
			} else {
				//JOptionPane.showMessageDialog(null, "Eccomi qui");
				return false;
			}
		}
		return false;
	}
	
	private Boolean collisionDetection() {
		for (Vector2 v : follow) {
			//JOptionPane.showMessageDialog(null, v.toString());
			if(!gameWorld.isTileFree(v)) {
				JOptionPane.showMessageDialog(null, "Non libera");
				follow = null;
				return false;
			}
		}
		return true;
	}
	
	private ArrayList<Vector2> generatePath(Vector2 destination, Vector2 current) {
		Vector2 direction = Vector2.sub(destination, current);
		//JOptionPane.showMessageDialog(null, direction.toString());
		follow = new ArrayList<Vector2>();
	
		if (direction.getX() > 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Vector2(current.getX() + i, current.getY()));
			}			
		}
		if (direction.getX() < 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Vector2(current.getX() - i, current.getY()));
			}			
		}
		if (direction.getY() > 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Vector2(current.getX(), current.getY() + i));
			}			
		}
		if (direction.getY() < 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Vector2(current.getX(), current.getY() - i));
			}			
		}
		
		return follow;
	}
	

	private Boolean validDestination(Vector2 destination) {
		for (Vector2 c : possiblePaths) {
			if (c.equals(destination))
				return true;
		}
		return false;
	}

	private void movementComplete(Vector2 destination, Vector2 oldPos) {
		gameWorld.releaseTile(oldPos);
		//gameWorld.highlightPath(possiblePaths);
		gameWorld.occupyTile(destination);
	}

	private int energyExpenditure;
	private GameWorld gameWorld;
	private ArrayList<Vector2> possiblePaths;
	private ArrayList<Vector2> follow;
}


/*private void pathfind(Vector2 origin, int range) {
	path = new ArrayList<Vector2>();

	for (int i = 0; i < range; i++) {
		if (origin.getY() + i < gameWorld.GRID_HEIGHT) {
			path.add(new Vector2(origin.getX(), origin.getY() + i));
		}
		if (origin.getY() - i >= 0) {
			path.add(new Vector2(origin.getX(), origin.getY() - i));
		}
		if (origin.getX() + i < gameWorld.GRID_LENGHT) {
			path.add(new Vector2(origin.getX() + i, origin.getY()));
		}
		if (origin.getX() - i >= 0) {
			path.add(new Vector2(origin.getX() - i, origin.getY()));
		}
	}
}*/

