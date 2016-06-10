package robotgameredux.systems;

import robotgameredux.actors.Robot;
import robotgameredux.actors.Support;
import robotgameredux.core.RobotFactory;
import robotgameredux.core.Vector2;
import robotgameredux.input.Command;
import robotgameredux.input.RobotStates;
import robotgameredux.input.SupportCommand;

public class StandardSupportSystem implements SupportSystem{

	public StandardSupportSystem() {
		this.actorsManager = null;
	}
	public void setActorManager(RobotFactory rf) {
		this.actorsManager = rf;
	}
	
	public void execute(SupportCommand command) {
		
		Support robot = command.getRobot();
		Integer index = command.getActiveObjectIndex();
		Vector2 target = command.getTarget();
		Robot r = actorsManager.isRobot(target); //Se il target non è un robot lanciare eccezione
		System.out.println(r.toString() + "ECCO IL ROBOOOOOOOOT");
		if (target.dst(robot.getCoords()) <= 100) {
			robot.getActiveTool(index).use(r);
		}
		robot.setState(RobotStates.INACTIVE);
	}


	private RobotFactory actorsManager; 
}
