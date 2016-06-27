package robotgameredux.systemsImplementations;

import java.io.Serializable;
import java.util.ArrayList;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.MovementCommand;
import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.MovementSystem;

public class TeleportMovementSystem implements MovementSystem, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3887894413285630332L;
	public TeleportMovementSystem(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	@Override
	public Boolean execute(MovementCommand command) throws InvalidTargetException, InsufficientEnergyException {

		Vector2 destination = command.getDestination();
		Integer dist = (int) command.getCoords().dst(destination);
		Vector2 oldPos = command.getCoords();

		if (command.getEnergy() == 0 || command.getEnergy() < dist)
			throw new InsufficientEnergyException(command);

		pathfind(command.getCoords(), command.getRange());

		if (destinationCheck(destination, command.getCoords())) {
			if (dist < command.getRange()) {
				command.removeEnergy(command.getCoords().dst(destination));
				command.setCoords(destination);
				movementComplete(destination, oldPos, command.getRange());
				return true;
			} else {
				System.out.println("Movimento impossibile, supera il range");
				gameWorld.highlightPath(oldPos, command.getRange(), false);
				throw new InvalidTargetException(command);
			}

		} else {
			System.out.println("Casella occupata");
			gameWorld.highlightPath(oldPos, command.getRange(), false);
			throw new InvalidTargetException(command);
		}

	}

	private void pathfind(Vector2 origin, int range) {
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
	}

	// pathfinding qui, metodo privato

	private Boolean destinationCheck(Vector2 destination, Vector2 current) {

		Boolean b = validDestination(destination);

		if (b) {
			if (current.dst(destination) == 0) {
				System.out.println("Sei già sulla tile scelta");
				return false;
			} else if (gameWorld.isTileFree(destination)) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	private Boolean validDestination(Vector2 destination) {
		for (Vector2 c : path) {
			if (c.equals(destination))
				return true;
		}
		return false;
	}

	private void movementComplete(Vector2 destination, Vector2 oldPos, int range) {
		gameWorld.releaseTile(oldPos);
		gameWorld.highlightPath(oldPos, range, false);
		gameWorld.occupyTile(destination);
	}

	private GameWorld gameWorld;
	private ArrayList<Vector2> path;
}
