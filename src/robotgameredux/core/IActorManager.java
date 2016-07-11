package robotgameredux.core;

import robotgameredux.TargetImplementations.RobotTarget;

public interface IActorManager {

	Boolean isRobot(Coordinates target);
	RobotTarget getTarget(Coordinates target);
	
}
