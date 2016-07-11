package robotgameredux.systemInterfaces;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.RobotAttackInteractCommand;
import robotgameredux.CommandsInterfaces.AtkInteractCommandInterface;

public interface AttackInteractionSystem {

	<T> Boolean execute(AtkInteractCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException;
	
}
