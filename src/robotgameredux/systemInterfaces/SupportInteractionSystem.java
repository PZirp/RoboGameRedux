package robotgameredux.systemInterfaces;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.RobotSupportInteractCommand;
import robotgameredux.CommandsInterfaces.SupInteractCommandInterface;

public interface SupportInteractionSystem {

	<T> Boolean execute(SupInteractCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException;
	//Take object from station, move obstacle, recharge
}
