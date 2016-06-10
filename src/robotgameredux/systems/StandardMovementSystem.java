package robotgameredux.systems;

import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.MovementCommand;
import robotgameredux.input.RobotStates;

public class StandardMovementSystem implements MovementSystem{

	public StandardMovementSystem(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	
	//era move(Robot actor)
	@Override
	public void execute(MovementCommand command) {

		
		Vector2 destination = command.getDestination();
		Robot actor = command.getRobot();
		
		if (destinationCheck(destination, actor.getCoords())) {
			if (actor.getEnergy() > 0) { 
				if(destination.dst(actor.getCoords()) < actor.getRange()) {
					Vector2 oldPos = actor.getCoords();
					actor.setCoords(destination);
					//energy--;
					movementComplete(destination, oldPos);
					actor.setState(RobotStates.INACTIVE); //Questo poi lo mettiamo nel gamemanager
				} else {
					System.out.println("Movimento impossibile, supera il range");
				}
				//energy = energy - 10;
				actor.setState(RobotStates.INACTIVE); //Questo poi lo mettiamo nel gamemanager
			} else {
				//this.setState(RobotStates.INACTIVE); //Questo poi lo mettiamo nel gamemanager
			}
		} else {
			actor.setState(RobotStates.INACTIVE);
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
			System.out.println("Tile scelta occupata!");
			//actor.setState(RobotStates.INACTIVE);
			return false;
		}
	}
	
	private void movementComplete(Vector2 destination, Vector2 oldPos) {
		gameWorld.releaseTile(oldPos);
		gameWorld.occupyTile(destination);
	}
	
	private GameWorld gameWorld;

}
