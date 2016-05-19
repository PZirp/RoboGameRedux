package robotgameredux.core;

import robotgameredux.actors.Robot;
import robotgameredux.input.RobotStates;

public class StandardMovementSystem implements MovementSystem{

	public StandardMovementSystem(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	
	@Override
	public Boolean move(Robot actor) {
		
		if (destinationCheck(actor)) {
			if (actor.getEnergy() > 0) { 
				if(actor.getDest().dst(actor.getCoords()) < actor.getRange()) {
					Vector2 oldPos = actor.getCoords();
					actor.setCoords(actor.getDest());
					//energy--;
					movementComplete(actor, oldPos);
				} else {
					System.out.println("Movimento impossibile, supera il range");
				}
				//energy = energy - 10;
				actor.setState(RobotStates.INACTIVE);
			} else {
				//this.setState(RobotStates.INACTIVE);
			}
		}
		
		
		return null;
	}

	//pathfinding qui, metodo privato
	
	private Boolean destinationCheck(Robot actor) {
		if (actor.getCoords().dst(actor.getDest()) == 0) {
			System.out.println("Sei già sulla tile scelta");
			actor.setState(RobotStates.INACTIVE);
			return false;
		} else if (gameWorld.isTileFree(actor.getDest())) {
			return true;
		} else {
			System.out.println("Tile scelta occupata!");
			actor.setState(RobotStates.INACTIVE);
			return false;
		}
	}
	
	private void movementComplete(Robot actor, Vector2 oldPos) {
		actor.setDest(null);
		actor.setState(RobotStates.INACTIVE);
		gameWorld.releaseTile(oldPos);
		gameWorld.occupyTile(actor.getCoords());
	}
	
	private GameWorld gameWorld;
}
