package robotgameredux.players;

import java.util.ArrayList;

import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.SupportRobot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.RobotFactory;

public class AI extends Player {


	private GameWorld gameWorld;
	private RobotFactory actorManager;
	
	public AI(GameWorld gameWorld, RobotFactory actorManager) {
		this.gameWorld = gameWorld;
		this.actorManager = actorManager;		
	}
	
	
	
}
