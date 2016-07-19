package robotgameredux.systemInterfaces;

import robotgameredux.CommandsInterfaces.SupInteractCommandInterface;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;

public interface SupportInteractionSystem {

	<T> Boolean execute(SupInteractCommandInterface<T> command)
			throws InvalidTargetException, InsufficientEnergyException;
	// Take object from station, move obstacle, recharge
}
