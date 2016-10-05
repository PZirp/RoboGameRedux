package robotgameredux.systemInterfaces;

import robotgameredux.CommandsInterfaces.MovementCommandInterface;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;

public interface MovementSystem {
	<T> Boolean execute(MovementCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException;
}
