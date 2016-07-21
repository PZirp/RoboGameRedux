package robotgameredux.systemInterfaces;

import robotgameredux.CommandsInterfaces.InteractCommandInterface;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;

public interface InteractionSystem {

	<T> Boolean execute(InteractCommandInterface<T> command)
			throws InvalidTargetException, InsufficientEnergyException;
}
