package robotgameredux.core;

import robotgameredux.TargetImplementations.RobotTarget;

public interface IActorManager {

	// Crea attackrobot e supportrobot non interessano nella interfaccia

	RobotTarget getTarget(Coordinates target);

}
