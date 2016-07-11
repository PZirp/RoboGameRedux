package robotgameredux.systemsImplementations;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.CommandsInterfaces.MovementCommandInterface;
import robotgameredux.core.GameWorld;
import robotgameredux.core.IGameWorld;
import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.MovementSystem;

public class StandardMovementSystem implements MovementSystem, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1841818356815052104L;
	public StandardMovementSystem(IGameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	
	
	@Override
	public <T> Boolean execute(MovementCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException {
		Integer dist = (int) command.getCoords().dst(command.getDestination());
		if (follow == null) {
			possiblePaths = gameWorld.pathfind(command.getCoords(), command.getRange());
			if (command.getEnergy() == 0 || command.getEnergy() < dist) {
				command.resetRobot();
				gameWorld.disablePath(possiblePaths);
				throw new InsufficientEnergyException(command);
			}
			if (!beginMovement(command)) {
				command.resetRobot();
				gameWorld.disablePath(possiblePaths); // Migliorare
				throw new InvalidTargetException(command, "Impossibile eseguire il movimento");
			}
			gameWorld.disablePath(possiblePaths);
		} else {
			if (continueMovement(command)) return true;	
		}
		return false;
	}
	
	private <T> Boolean beginMovement(MovementCommandInterface<T> command) {
		Coordinates destination = command.getDestination();
		Integer dist = (int) command.getCoords().dst(destination);
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
			//JOptionPane.showMessageDialog(null, "Eccomi qui");
			//System.out.println("Casella non raggiungibile o occupata");
			command.resetRobot();
			return false;
		}
	}
	
	private <T> Boolean continueMovement(MovementCommandInterface<T> command) {
		Coordinates oldPos = command.getCoords();
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
	
	private Boolean destinationCheck(Coordinates destination, Coordinates current) {
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
		for (Coordinates v : follow) {
			//JOptionPane.showMessageDialog(null, v.toString());
			if(!gameWorld.isTileFree(v)) {
				JOptionPane.showMessageDialog(null, "Non libera");
				follow = null;
				return false;
			}
		}
		return true;
	}
	
	private ArrayList<Coordinates> generatePath(Coordinates destination, Coordinates current) {
		Coordinates direction = destination.sub(current)/*Coordinates.sub(destination, current)*/;
		//JOptionPane.showMessageDialog(null, direction.toString());
		follow = new ArrayList<Coordinates>();
	
		if (direction.getX() > 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Coordinates(current.getX() + i, current.getY()));
			}			
		}
		if (direction.getX() < 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Coordinates(current.getX() - i, current.getY()));
			}			
		}
		if (direction.getY() > 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Coordinates(current.getX(), current.getY() + i));
			}			
		}
		if (direction.getY() < 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Coordinates(current.getX(), current.getY() - i));
			}			
		}
		
		return follow;
	}
	

	private Boolean validDestination(Coordinates destination) {
		for (Coordinates c : possiblePaths) {
			if (c.equals(destination))
				return true;
		}
		return false;
	}

	private void movementComplete(Coordinates destination, Coordinates oldPos) {
		gameWorld.releaseTile(oldPos);
		//gameWorld.highlightPath(possiblePaths);
		gameWorld.occupyTile(destination);
	}

	private int energyExpenditure;
	private IGameWorld gameWorld;
	private ArrayList<Coordinates> possiblePaths;
	private ArrayList<Coordinates> follow;
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

