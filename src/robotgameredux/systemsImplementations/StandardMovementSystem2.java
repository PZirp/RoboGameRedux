package robotgameredux.systemsImplementations;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.MovementCommand;
import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.MovementSystem;

public class StandardMovementSystem2 implements MovementSystem {

	public StandardMovementSystem2(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	private Vector2 start;
	
	@Override
	public Boolean execute(MovementCommand command) throws InvalidTargetException, InsufficientEnergyException {

		Vector2 destination = command.getDestination();
		Integer dist = (int) command.getCoords().dst(destination);
		Vector2 oldPos = command.getCoords();
		

		if (command.getEnergy() == 0 || command.getEnergy() < dist)
			throw new InsufficientEnergyException(command);

		//movementComplete(destination, oldPos, command.getRange());
		
		possiblePaths = gameWorld.pathfind(command.getCoords(), command.getRange());
		if (follow == null) {
			if (destinationCheck(destination, command.getCoords()) && dist < command.getRange()) {
				generatePath(destination, command.getCoords());		
				collisionDetection();
				
				
				
				command.removeEnergy(command.getCoords().dst(destination));
				command.setCoords(follow.get(0));
				follow.remove(0);
				return true;
			} else {
				System.out.println("Casella occupata");
				//gameWorld.highlightPath();
				command.resetRobot();
				throw new InvalidTargetException(command);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Mi muovo");
			command.setCoords(follow.get(0));
			follow.remove(0);
			if (command.getCoords().equals(command.getDestination())) {
				movementComplete(destination, oldPos, command.getRange());
				command.resetRobot();
				follow = null;
			}
			return true;
		}
	}
	
	private Boolean destinationCheck(Vector2 destination, Vector2 current) {
		if (validDestination(destination)) {
			if (current.dst(destination) == 0) {
				System.out.println("Sei già sulla tile scelta");
				return false;
			} else if (gameWorld.isTileFree(destination)) {
				return true;
			} else {
				JOptionPane.showMessageDialog(null, "Eccomi qui");
				return false;
			}
		}
		return false;
	}
	
	private void collisionDetection() {
		for (Vector2 v : follow) {
			JOptionPane.showMessageDialog(null, v.toString());
			if(!gameWorld.isTileFree(v)) {
				JOptionPane.showMessageDialog(null, "Non libera");
				follow = null;
				return;
			}
		}
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

	private void movementComplete(Vector2 destination, Vector2 oldPos, int range) {
		gameWorld.releaseTile(oldPos);
		//gameWorld.highlightPath();
		gameWorld.occupyTile(destination);
	}

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

