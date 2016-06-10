package robotgameredux.systems;

import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.AtkInteractCommand;
import robotgameredux.input.InteractCommand;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Weapon;

public class StandardAttackInteractionSystem implements AttackInteractionSystem {

	public StandardAttackInteractionSystem(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	@Override
	public void execute(AtkInteractCommand command) {
		//Robot robot = command.getRobot();
		Vector2 target = command.getTarget();
		
		switch (command.getState()) {
		case DESTROY_OBSTACLE: 
			if (gameWorld.isObstacle(target) && command.getCoords().dst(target) <= 1.5/* && gameWorld.isObstacle(target)*/) {
				gameWorld.destroyObstacle(target, command.getStrenght());
			}
			command.setState(RobotStates.INACTIVE);
			break;
		case RECHARGE:
			if(gameWorld.isStation(target) && command.getCoords().dst(target) <= 1.5){
				Integer charge = gameWorld.recharge();
				if (charge != null)
					command.addEnergy(charge);

			}
			command.setState(RobotStates.INACTIVE);
			break;
		case TAKE_WEAPON:
			if(gameWorld.isStation(target) && command.getCoords().dst(target) <= 1.5) {
				Weapon w = gameWorld.getWeapon();
				command.addWeapon(w);
			}

			command.setState(RobotStates.INACTIVE);
			break;
		}
			
		
	}
	
	private GameWorld gameWorld;


}
