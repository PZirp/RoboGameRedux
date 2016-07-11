package robotgameredux.systemInterfaces;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.CommandsInterfaces.MovementCommandInterface;
import robotgameredux.actors.Robot;

public interface MovementSystem {
	<T> Boolean execute(MovementCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException;
}
