package robotgameredux.systemsImplementations;

import java.io.Serializable;
import java.util.ArrayList;

import robotgameredux.CommandsInterfaces.MovementCommandInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IGameWorld;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.systemInterfaces.MovementSystem;


/**
 * Classe che implementa una modalità standard per eseguire il comando di movimento.
 * Quando un attore viene creato, è possibile aggiungere questo sistema standard se 
 * non si vuole dare un comportamento specifico al movimento dell'attore.
 * Questo sistema permette di muoversi solo nelle quattro direzioni
 * (su, giù, destra e sinistra) una volta per turno, tenendo conto del fatto che la
 * strada scelta deve essere priva di ostacoli e caselle occupate.
 * Questa classe implementa l'interfaccia MovementSystem.
 * 
 * @author Paolo Zirpoli
 */

public class StandardMovementSystem implements MovementSystem, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1841818356815052104L;

	public StandardMovementSystem(IGameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	@Override
	public <T> Boolean execute(MovementCommandInterface<T> command)
			throws InvalidTargetException, InsufficientEnergyException {
		Integer dist = (int) command.getCoords().dst(command.getDestination());
		if (follow == null) {
			possiblePaths = gameWorld.pathfind(command.getCoords(), command.getRange());
			if (command.getEnergy() == 0 || command.getEnergy() < dist) {
				command.resetActor();
				gameWorld.disablePath(possiblePaths);
				throw new InsufficientEnergyException(command, dist);
			}
			if (!beginMovement(command)) {
				command.resetActor();
				gameWorld.disablePath(possiblePaths);
				throw new InvalidTargetException(command, "Impossibile eseguire il movimento");
			}
			gameWorld.disablePath(possiblePaths);
		} else {
			if (continueMovement(command))
				return true;
		}
		return false;
	}

	private <T> Boolean beginMovement(MovementCommandInterface<T> command) {
		Coordinates destination = command.getDestination();
		Integer dist = (int) command.getCoords().dst(destination);
		if (destinationCheck(destination, command.getCoords()) && dist < command.getRange()) {
			generatePath(destination, command.getCoords());
			if (!collisionDetection()) {
				return false;
			}
			energyExpenditure = command.getCoords().dst(destination);
			command.setState(RobotStates.MOVING);
			return true;
		} else {
			command.resetActor();
			return false;
		}
	}

	private <T> Boolean continueMovement(MovementCommandInterface<T> command) {
		Coordinates oldPos = command.getCoords();
		command.setCoords(follow.get(0));
		follow.remove(0);
		gameWorld.releaseTile(oldPos);
		if (command.getCoords().equals(command.getDestination())) {
			movementComplete(command.getDestination(), oldPos);
			command.removeEnergy(energyExpenditure);
			energyExpenditure = 0;
			command.resetActor();
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
				return false;
			}
		}
		return false;
	}

	private Boolean collisionDetection() {
		for (Coordinates v : follow) {
			if (!gameWorld.isTileFree(v)) {
				follow = null;
				return false;
			}
		}
		return true;
	}

	private ArrayList<Coordinates> generatePath(Coordinates destination, Coordinates current) {
		Coordinates direction = destination.sub(current);
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
		gameWorld.occupyTile(destination);
	}

	@Override
	public String toString() {
		return getClass().getName() + "[EnergyExpenditure: " + energyExpenditure + " GameWorld: " + gameWorld.toString()
				+ " PossiblePaths: " + possiblePaths.toString() + " Follow: " + follow.toString() + "]";
	}

	@Override
	public StandardMovementSystem clone() {
		try {
			StandardMovementSystem clone = (StandardMovementSystem) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		StandardMovementSystem other = (StandardMovementSystem) otherObject;
		return energyExpenditure == other.energyExpenditure && gameWorld.equals(other.gameWorld)
				&& possiblePaths.equals(other.possiblePaths) && follow.equals(other.follow);
	}

	private int energyExpenditure;
	private IGameWorld gameWorld;
	private ArrayList<Coordinates> possiblePaths;
	private ArrayList<Coordinates> follow;
}
