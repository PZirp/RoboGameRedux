package robotgameredux.systems;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.MovementCommand;
import robotgameredux.input.RobotStates;

public class StandardMovementSystem implements MovementSystem{

	public StandardMovementSystem(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
		
	/*
	 * TODO
	 * 1) Ridurre l'energia in base a quante tile ci si sposta
	 * 2) Il cazzo di pathfinding
	 * 3) Fare l'interfaccia del movementCommand per non prendere sempre il robot
	 */
		
	@Override
	public void execute(MovementCommand command) throws InvalidTargetException, InsufficientEnergyException {

		Vector2 destination = command.getDestination();		
		Integer dist = (int) command.getCoords().dst(destination);
		
		if (command.getEnergy() == 0 || command.getEnergy() < dist)
			throw new InsufficientEnergyException(command);
		
		if (destinationCheck(destination, command.getCoords())) {
				if(dist < command.getRange()) {
					Vector2 oldPos = command.getCoords();
					command.setEnergy(command.getCoords().dst(destination));
					command.setCoords(destination);
					movementComplete(destination, oldPos);
					command.setState(RobotStates.INACTIVE); //Questo poi lo mettiamo nel gamemanager, oppure no
				} else {
					System.out.println("Movimento impossibile, supera il range");
					throw new InvalidTargetException(command);
				}
				command.setState(RobotStates.INACTIVE); //Questo poi lo mettiamo nel gamemanager
	
		} else {
			System.out.println("Casella occupata");
			throw new InvalidTargetException(command);
		}
		
	}

	//pathfinding qui, metodo privato
	
	private Boolean destinationCheck(Vector2 destination, Vector2 current) {
		if (current.dst(destination) == 0) {
			System.out.println("Sei già sulla tile scelta");
			//actor.setState(RobotStates.INACTIVE);
			return false;
		} else if (gameWorld.isTileFree(destination)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void movementComplete(Vector2 destination, Vector2 oldPos) {
		gameWorld.releaseTile(oldPos);
		gameWorld.occupyTile(destination);
	}
	
	private GameWorld gameWorld;

}
