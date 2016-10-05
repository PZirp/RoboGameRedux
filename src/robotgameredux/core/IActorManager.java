package robotgameredux.core;

import robotgameredux.TargetInterfaces.RobotTarget;

public interface IActorManager {

	// Crea attackrobot e supportrobot non interessano nella interfaccia

	RobotTarget getTarget(Coordinates target);

}
