package robotgameredux.systemsImplementations;

import java.io.Serializable;
import java.util.ArrayList;

import robotgameredux.CommandsInterfaces.MovementCommandInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.core.GameWorld;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
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
	public Boolean execute(MovementCommandInterface command)
			throws InvalidTargetException, InsufficientEnergyException {

		Coordinates destination = command.getDestination();
		Integer dist = (int) command.getCoords().dst(destination);
		Coordinates oldPos = command.getCoords();

		if (command.getEnergy() == 0 || command.getEnergy() < dist)
			// throw new InsufficientEnergyException(command);

			pathfind(command.getCoords(), command.getRange());

		if (destinationCheck(destination, command.getCoords())) {
			if (dist < command.getRange()) {
				command.removeEnergy(command.getCoords().dst(destination));
				command.setCoords(destination);
				movementComplete(destination, oldPos, command.getRange());
				return true;
			} else {
				System.out.println("Movimento impossibile, supera il range");
				gameWorld.disablePath(path);
				throw new InvalidTargetException(command);
			}

		} else {
			System.out.println("Casella occupata");
			gameWorld.disablePath(path);
			throw new InvalidTargetException(command);
		}

	}

	private void pathfind(Coordinates origin, int range) {
		path = new ArrayList<Coordinates>();

		for (int i = 0; i < range; i++) {
			if (origin.getY() + i < GameWorld.GRID_HEIGHT) {
				path.add(new Coordinates(origin.getX(), origin.getY() + i));
			}
			if (origin.getY() - i >= 0) {
				path.add(new Coordinates(origin.getX(), origin.getY() - i));
			}
			if (origin.getX() + i < GameWorld.GRID_LENGHT) {
				path.add(new Coordinates(origin.getX() + i, origin.getY()));
			}
			if (origin.getX() - i >= 0) {
				path.add(new Coordinates(origin.getX() - i, origin.getY()));
			}
		}
	}

	// pathfinding qui, metodo privato

	private Boolean destinationCheck(Coordinates destination, Coordinates current) {

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

	private Boolean validDestination(Coordinates destination) {
		for (Coordinates c : path) {
			if (c.equals(destination))
				return true;
		}
		return false;
	}

	private void movementComplete(Coordinates destination, Coordinates oldPos, int range) {
		gameWorld.releaseTile(oldPos);
		gameWorld.disablePath(path);
		gameWorld.occupyTile(destination);
	}

	private GameWorld gameWorld;
	private ArrayList<Coordinates> path;

}
