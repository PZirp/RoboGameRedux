package robotgameredux.core;

import java.awt.Robot;

import robotgameredux.TargetImplementations.RobotTarget;

public interface IActorManager {

	//Crea attackrobot e supportrobot non interessano nella interfaccia
	
	RobotTarget getTarget(Coordinates target);
	
}
