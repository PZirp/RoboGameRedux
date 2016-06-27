package robotgameredux.systemInterfaces;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.MovementCommand;
import robotgameredux.actors.Robot;

public interface MovementSystem {
	Boolean execute(MovementCommand command) throws InvalidTargetException, InsufficientEnergyException;
}
