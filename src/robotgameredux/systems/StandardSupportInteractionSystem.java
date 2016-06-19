package robotgameredux.systems;

import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.input.SupInteractCommand;
import robotgameredux.tools.UsableTool;

public class StandardSupportInteractionSystem implements SupportInteractionSystem {

	public StandardSupportInteractionSystem(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	@Override
	public void execute(SupInteractCommand command) {
		//Robot robot = command.getRobot();
		Vector2 target = command.getTarget();
		
		switch (command.getState()) {
		case PUSH_OBSTACLE: 	
			if (gameWorld.isObstacle(target) && command.getCoords().dst(target) <= 1.5/* && gameWorld.isObstacle(target)*/) {
				gameWorld.pushObstacle(target, command.getStrenght(), command.getCoords());
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
		case TAKE_OBJECT:
			if(gameWorld.isStation(target) && command.getCoords().dst(target) <= 1.5) {
				UsableTool t = gameWorld.getTool();
				if (t != null) 
					command.addTool(t);
			}
			command.setState(RobotStates.INACTIVE);
			break;
		}
			
		
	}
	
	private GameWorld gameWorld;
}
